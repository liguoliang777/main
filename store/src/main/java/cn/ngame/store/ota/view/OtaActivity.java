package cn.ngame.store.ota.view;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.fragment.SimpleDialogFragment;
import cn.ngame.store.ota.model.DeviceInfo;
import cn.ngame.store.ota.model.OtaService;
import cn.ngame.store.ota.presenter.OtaPresentListener;
import cn.ngame.store.ota.presenter.OtaPresenter;
import cn.ngame.store.view.RoundProgressBar;


/**
 * 用于OTA升级，我的设备页面
 * Created by zeng on 2016/8/15.
 */
public class OtaActivity extends BaseFgActivity implements View.OnClickListener, OtaUpdateListener {

    public static final String TAG = "777";
    private OtaActivity context;
    public static final int REQUEST_CODE_BLUETOOTH_SETTINGS = 1;

    private OtaPresentListener presenter;

    private ListView listView;
    private DeviceLvAdapter adapter;

    private RoundProgressBar progressBar;
    private Button checkBt, bt_back;
    private TextView tv_title_left, refreshBt;

    private boolean isUpdating = false;
    private Timer timer = new Timer();
    //OtaService绑定监听
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "OtaService连接成功 ");
            OtaService.MyBinder myBinder = (OtaService.MyBinder) service;
            otaService = myBinder.getService();

            //创建控制层
            presenter = new OtaPresenter(context, otaService);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isUpdating) {
                        if (null != presenter) {
                            presenter.scanDevice();
                            timer.cancel();
                        }
                    } else {
                        Log.d(TAG, "扫描蓝牙");
                        if (null != presenter) {
                            presenter.scanDevice();
                            timer.cancel();
                            Log.d(TAG, "扫描cancel");
                        }
                    }
                }
            }, 0, 1000);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "OtaService连接断开");
        }
    };
    private OtaService otaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ota);
        context = this;
        initView();
        //启动OTA升级后台服务
        Intent otaService = new Intent(this, OtaService.class);
        startService(otaService);
        bindService(otaService, serviceConnection, Context.BIND_AUTO_CREATE);
        //bindService(intent,conn,flag)->Service:onCreate()->Service:onBind()->Activity:onServiceConnected()
        //申请下载权限
        CommonUtil.verifyStoragePermissions(this);
    }

    private void initView() {
        progressBar = (RoundProgressBar) findViewById(R.id.progress_bar);
        checkBt = (Button) findViewById(R.id.but1);
        bt_back = (Button) findViewById(R.id.left_but);
        tv_title_left = (TextView) findViewById(R.id.left_tv);
        refreshBt = (TextView) findViewById(R.id.right_tv);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new DeviceLvAdapter(this);
        listView.setAdapter(adapter);

        //初始化圆形进度条
        progressBar.setProgress(100);
        progressBar.setState("手柄连接状态");
        progressBar.setStateDetail("连接查询中...");

        bt_back.setOnClickListener(this);
        tv_title_left.setOnClickListener(this);
        refreshBt.setOnClickListener(this);
        progressBar.setOnClickListener(this);
        checkBt.setOnClickListener(this);
        registerReceiver(mBleConnectUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            //presenter.clear();
            presenter = null;
        }
        listView = null;
        adapter = null;

        unbindService(serviceConnection);
        unregisterReceiver(mBleConnectUpdateReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_BLUETOOTH_SETTINGS) {

            if (presenter != null) {
                progressBar.setState("手柄连接状态");
                progressBar.setStateDetail("查询中...");
                presenter.scanDevice();
            } else {

              /*  progressBar.setState("绑定服务失败");
                progressBar.setStateDetail("退出页面重新进入");*/
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_but:

                if (isUpdating) {
                    showNotifyBack();
                } else {
                    finish();
                }

                break;
            case R.id.left_tv:
                if (isUpdating) {
                    showNotifyBack();
                } else {
                    finish();
                }
                break;
            case R.id.right_tv:
            /*    if (presenter != null) {
                    //progressBar.setState("手柄连接状态:");
                    presenter.scanDevice();
                    progressBar.setStateDetail("连接状态查询中...");
                }*/

                startActivity(new Intent(context, OtaHelpActivity.class));
                break;
            case R.id.progress_bar:

                Intent bluetoothIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivityForResult(bluetoothIntent, REQUEST_CODE_BLUETOOTH_SETTINGS);

                break;
            //刷新
            case R.id.but1:
                if (null != presenter) {
                    presenter.checkNewVersion();
                }
                break;
        }
    }

    //HID协议 指 系统的蓝牙是hid协议
    private static IntentFilter makeGattUpdateIntentFilter() {

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OtaService.ACTION_BLUETOOTH_NONSUPPORT);
        intentFilter.addAction(OtaService.ACTION_BLUETOOTH_DISABLE);
        intentFilter.addAction(OtaService.ACTION_BLUETOOTH_FIND_DEVICE);
        intentFilter.addAction(OtaService.ACTION_BLUETOOTH_CHECK_UPDATE);
        intentFilter.addAction(OtaService.ACTION_OTA_UPDATING);

        return intentFilter;
    }

    //监听蓝牙连接,更新广播
    private final BroadcastReceiver mBleConnectUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "收到广播: " + action);
            //不支持蓝牙设备
            if (OtaService.ACTION_BLUETOOTH_NONSUPPORT.equals(action)) {

                isUpdating = false;
                listView.setVisibility(View.GONE);
                progressBar.setState("连接失败");
                progressBar.setStateDetail("不支持蓝牙设备");
                progressBar.setProgress(100);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment updateDialog = getSupportFragmentManager().findFragmentByTag("updateDialog");
                Fragment notifyBackDialog = getSupportFragmentManager().findFragmentByTag("notifyBackDialog");
                if (updateDialog != null) {
                    ft.remove(updateDialog);
                }
                if (notifyBackDialog != null) {
                    ft.remove(notifyBackDialog);
                }
                ft.commit();

                //未开启蓝牙功能
            } else if (OtaService.ACTION_BLUETOOTH_DISABLE.equals(action)) {
                Log.d(TAG, "未开启蓝牙功能: " + action);
                isUpdating = false;
                listView.setVisibility(View.GONE);
                progressBar.setState("连接失败");
                progressBar.setStateDetail("蓝牙功能未开启");
                progressBar.setProgress(100);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment updateDialog = getSupportFragmentManager().findFragmentByTag("updateDialog");
                Fragment notifyBackDialog = getSupportFragmentManager().findFragmentByTag("notifyBackDialog");
                if (updateDialog != null) {
                    ft.remove(updateDialog);
                }
                if (notifyBackDialog != null) {
                    ft.remove(notifyBackDialog);
                }
                ft.commit();

                //找到设备
            } else if (OtaService.ACTION_BLUETOOTH_FIND_DEVICE.equals(action)) {
                isUpdating = false;
                String title = intent.getStringExtra("title");
                String subtitle = intent.getStringExtra("subtitle");
                ArrayList<DeviceInfo> deviceInfos = intent.getParcelableArrayListExtra("devices");
                Log.d(TAG, "找到设备: " + action);
                Log.d(TAG, "找到设备:title " + title);
                Log.d(TAG, "找到设备:subtitle " + subtitle);
                Log.d(TAG, "找到设备:deviceInfos " + deviceInfos);

                if (deviceInfos != null && deviceInfos.size() > 0) {
                    listView.setVisibility(View.VISIBLE);
                    adapter.setData(deviceInfos);
                    adapter.notifyDataSetChanged();
                } else {
                    listView.setVisibility(View.GONE);
                }

                progressBar.setState(title);
                progressBar.setStateDetail(subtitle);
                progressBar.setProgress(100);
                //检查更新
            } else if (OtaService.ACTION_BLUETOOTH_CHECK_UPDATE.equals(action)) {
                Log.d(TAG, "检查更新: " + action);
                isUpdating = false;
                String title = intent.getStringExtra("title");
                String subtitle = intent.getStringExtra("subtitle");
                ArrayList<DeviceInfo> deviceInfos = intent.getParcelableArrayListExtra("devices");

                listView.setVisibility(View.VISIBLE);
                adapter.setData(deviceInfos);
                adapter.notifyDataSetChanged();

                progressBar.setState(title);
                progressBar.setStateDetail(subtitle);
                progressBar.setProgress(100);


            } else if (OtaService.ACTION_OTA_UPDATING.equals(action)) {
                Log.d(TAG, "正在更新: " + action);
                String title = intent.getStringExtra("title");
                String subtitle = intent.getStringExtra("subtitle");
                int progress = intent.getIntExtra("progress", 100);
                isUpdating = intent.getBooleanExtra("isUpdating", false);
                boolean isFinished = intent.getBooleanExtra("isFinished", false);

                if (progress >= 100 && isFinished) {//升级完成
                    adapter.setData(null);
                    adapter.notifyDataSetChanged();
                    listView.setVisibility(View.GONE);

                } else {
                    listView.setVisibility(View.VISIBLE);
                }

                progressBar.setState(title);
                progressBar.setStateDetail(subtitle);
                progressBar.setProgress(progress);

            } else {
                Log.d(TAG, "其他: " + action);
            }

        }
    };

    @Override
    public void showCheckVersionResult(final int state) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (state == -1) {
                    Toast.makeText(OtaActivity.this, "请先连接手柄设备", Toast.LENGTH_SHORT).show();
                } else if (state == -2) {
                    Toast.makeText(OtaActivity.this, "正在检测，请稍后...", Toast.LENGTH_SHORT).show();
                } else if (state == -3) {
                    Toast.makeText(OtaActivity.this, "OTA正在升级更新，请勿重复操作", Toast.LENGTH_SHORT).show();
                } else if (state == 0) {
                    Toast.makeText(OtaActivity.this, "手柄已是最新版本", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OtaActivity.this, "检测完成", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showUpdateDialog(final DeviceInfo info) {
        if (isUpdating) {
            Toast.makeText(context, "OTA正在更新中...", Toast.LENGTH_SHORT).show();
            return;
        }

        //final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        final SimpleDialogFragment dialogFragment = SimpleDialogFragment.newInstance();

        if (dialogFragment.isShow()) {
            return;
        }
        dialogFragment.setDialogWidth(250);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.layout_dialog_update, null);
        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_title.setText("确定立即更新：" + info.getNewVersionName() + "版本吗？");
        TextView tv_summary = (TextView) contentView.findViewById(R.id.tv_summary);
        tv_summary.setText(info.getContent());
        dialogFragment.setContentView(contentView);

        dialogFragment.setPositiveButton(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
            }
        });
        dialogFragment.setNegativeButton(R.string.update_now, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                showNotify(info);
            }
        });
        if (!dialogFragment.isAdded()) {
            dialogFragment.show(getSupportFragmentManager(), "updateDialog");
            dialogFragment.setIsShow(true);
        }
    }

    private void showNotify(final DeviceInfo info) {
        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        dialogFragment.setDialogWidth(280);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.layout_dialog_update, null);
        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);
        TextView tv_summary = (TextView) contentView.findViewById(R.id.tv_summary);
        tv_summary.setText("更新过程中请将手机和手柄放在一起。" +
                "请勿关闭手柄、断开蓝牙连接及退出APP等其他操作！升级过程中手柄重启是正常情况。");

        dialogFragment.setContentView(contentView);

        dialogFragment.setNegativeButton("知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogFragment.isVisible()) {
                    dialogFragment.dismiss();
                }
                presenter.updateDevice(info);
            }
        });
        if (dialogFragment != null)
            dialogFragment.show(getSupportFragmentManager(), "notifyDialog");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isUpdating) {
                showNotifyBack();
            } else {
                finish();
            }
        }
        return false;
    }

    private void showNotifyBack() {

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        dialogFragment.setDialogWidth(250);
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.layout_dialog_update, null);
        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);
        TextView tv_summary = (TextView) contentView.findViewById(R.id.tv_summary);
        tv_summary.setText("手柄固件升级中，退出将会导致升级失败!是否退出?");

        dialogFragment.setContentView(contentView);

        dialogFragment.setPositiveButton("退出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                finish();
            }
        });
        dialogFragment.setNegativeButton("继续升级", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogFragment.isVisible())
                    dialogFragment.dismiss();
            }
        });
        if (dialogFragment != null) {
            dialogFragment.show(getSupportFragmentManager(), "notifyBackDialog");
        }
    }
}
