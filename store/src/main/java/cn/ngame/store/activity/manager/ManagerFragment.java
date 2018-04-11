package cn.ngame.store.activity.manager;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lx.pad.util.LLog;
import com.ngds.pad.inject.InjectDataMgr;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.ngame.store.R;
import cn.ngame.store.activity.main.MainHomeActivity;
import cn.ngame.store.adapter.CloudGameAdapter;
import cn.ngame.store.adapter.InstalledGameAdapter;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.utils.AppInstallHelper;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.FileUtil;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.util.AssetsCopyer;
import cn.ngame.store.util.MD5Utils;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.util.Utils;
import cn.ngame.store.view.ActionItem;
import cn.ngame.store.view.QuickAction;

/**
 * 下载更新fragment (懒加载-当滑动到当前fragment时，才去加载。而不是进入到activity时，加载所有fragment)
 * Created by gp on 2017/3/3 0003.
 */
@SuppressLint({"ValidFragment", "WrongConstant"})
public class ManagerFragment extends Fragment {
    final static String TAG = ManagerFragment.class.getSimpleName();
    ListView listView;
    public static int PAGE_SIZE = 10;
    protected QuickAction mItemClickQuickAction;
    private IFileLoad fileLoad;
    private InstalledGameAdapter alreadyLvAdapter;
    private MainHomeActivity content;
    private boolean mHidden = false;
    private PackageInfo mfileUnstalledInfo;
    private PackageManager packageManager;
    private String pkgNameListStr = "";
    private List<FileLoadInfo> openFileInfoList;
    private List<PackageInfo> packageInfos = new ArrayList<>();
    private PackageInfo packageInfo = new PackageInfo();
    private ApplicationInfo applicationInfo;
    private TextView emptyTv;
    private int oldLength;
    private TextView mBlueToothConnectedTv, mInjectServerConnectedTv, mAddGameTv;
    private Boolean mInjectServerState = false;
    private Button mInjectServerBt;
    private TextView mCloudTabTv, mNativceTabTv;
    private View mCloudLine, mNativeLine;
    private ListView mCloudListView;
    private CloudGameAdapter mCloudGameAdapter;
    private QuickAction mItemClickQuickActionCloud;
    private boolean readException = false;
    private String signData = "";
    private Set<String> CLOUD_MD5_SET = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager, container, false);
        listView = view.findViewById(R.id.manager_lv);
        emptyTv = view.findViewById(R.id.manager_empty_tv);
        mBlueToothConnectedTv = view.findViewById(R.id.bluetooth_connect_state_tv);
        mInjectServerConnectedTv = view.findViewById(R.id.inject_server_connect_state_tv);
        mAddGameTv = view.findViewById(R.id.manager_add_game_bt);

        //连接蓝牙
        view.findViewById(R.id.bluetooth_connect_bt).setOnClickListener(managerOnClikListener);

        //添加游戏
        mAddGameTv.setOnClickListener(managerOnClikListener);

        //开启映射
        mInjectServerBt = view.findViewById(R.id.inject_server_connect_bt);
        mInjectServerBt.setOnClickListener(managerOnClikListener);

        //顶部按钮
        mCloudTabTv = view.findViewById(R.id.cloud_games_tab_tv);
        mCloudTabTv.setSelected(true);
        mNativceTabTv = view.findViewById(R.id.native_games_tab_tv);

        mCloudTabTv.setOnClickListener(managerOnClikListener);
        mNativceTabTv.setOnClickListener(managerOnClikListener);

        mCloudLine = view.findViewById(R.id.cloud_games_tab_line);
        mNativeLine = view.findViewById(R.id.native_games_tab_line);

        mCloudListView = view.findViewById(R.id.manager_cloud_lv);
        return view;
    }

    private View.OnClickListener managerOnClikListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cloud_games_tab_tv:
                    setTopTabColor(true);

                    break;
                case R.id.native_games_tab_tv:
                    setTopTabColor(false);
                    break;
                //添加游戏
                case R.id.manager_add_game_bt:
                    startActivity(new Intent(content, AddGameActivity.class));
                    break;
                //连接蓝牙
                case R.id.bluetooth_connect_bt:
                    Intent bluetoothIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(bluetoothIntent);
                    break;
                //InjectServer
                case R.id.inject_server_connect_bt:
                    if (Utils.isRooted()) {
                        if (mInjectServerState) {
                            ToastUtil.show(content, "映射服务已开启");
                            return;
                        }
                        mInjectServerBt.setClickable(false);
                        final String saveDir = "data/local/tmp/";
                        try {
                            AssetsCopyer.copyAssets(content, "", saveDir);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        new AsyncTask<Void, Void, Integer>() {
                            @Override
                            protected Integer doInBackground(Void... voids) {
                                try {
                                    Utils.execRootCmd("chmod 777 " + saveDir + getString(R
                                            .string.inject_server_name));
                                    Utils.execRootCmd("" + saveDir + getString(R.string
                                            .inject_server_name));
                                } catch (Exception e) {
                                }
                                return 0;
                            }

                            @Override
                            protected void onPostExecute(Integer isOpened) {
                                super.onPostExecute(isOpened);
                                LLog.d("执行");
                                mInjectServerBt.setClickable(true);
                            }
                        }.execute();
                    } else {
                        ToastUtil.show(content, "手机没有root管理权限,无法开启映射服务");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        content = (MainHomeActivity)getActivity();
        packageManager = content.getPackageManager();
        initPop();
        initCloudPop();
        initListView();

        EventBus.getDefault().register(this);
    }


    public void initListView() {
        alreadyLvAdapter = new InstalledGameAdapter(content, content.getSupportFragmentManager(),
                mItemClickQuickAction);
        mCloudGameAdapter = new CloudGameAdapter(content, content.getSupportFragmentManager(),
                mItemClickQuickActionCloud);
        listView.setAdapter(alreadyLvAdapter);
        mCloudListView.setAdapter(mCloudGameAdapter);
        fileLoad = FileLoadManager.getInstance(content);
    }

    private List<PackageInfo> cloudAppList = new ArrayList<>();
    private List<PackageInfo> localAppList = new ArrayList<>();

    private JSONArray jsonArray = new JSONArray();


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart:" + CLOUD_MD5_SET);
        CLOUD_MD5_SET = content.getSharedPreferences(Constant
                .CONFIG_FILE_NAME, content.MODE_PRIVATE).getStringSet(Constant
                .SIGN_TOOL_KEY, null);
        initData();
    }

    private void initData() {
        //获取本地
        try {
            pkgNameListStr = FileUtil.readFile();
            if (null != pkgNameListStr) {
                jsonArray = new JSONArray(pkgNameListStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            readException = true;
        }
        oldLength = jsonArray.length();
        //获取数据库 =>  添加
        openFileInfoList = fileLoad.getOpenFileInfo();
        for (FileLoadInfo openFileInfo : openFileInfoList) {
            String gamePackageName = openFileInfo.getPackageName();
            if (pkgNameListStr == null || !pkgNameListStr.contains(gamePackageName)) {
                jsonArray.put(gamePackageName);
            }
        }
        if (jsonArray.length() > oldLength) {
            FileUtil.writeFile2SDCard(jsonArray.toString());
            pkgNameListStr = FileUtil.readFile();
        }
        getLocalApp();
        //原生
        if (null != alreadyLvAdapter) {
            alreadyLvAdapter.setDate(localAppList);
        }
        //云端
        if (mCloudGameAdapter != null) {
            mCloudGameAdapter.setDate(cloudAppList);
            //列表为空
            emptyTv.setVisibility(mCloudTabTv.isSelected() ? cloudAppList.size() == 0 ? View
                    .VISIBLE : View
                    .GONE : View.GONE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //获取蓝牙设备
            getConnectBlueTooth();
            if (CLOUD_MD5_SET == null) {
                //没读签名
                CLOUD_MD5_SET = content.getSharedPreferences(Constant
                        .CONFIG_FILE_NAME, content.MODE_PRIVATE).getStringSet(Constant
                        .SIGN_TOOL_KEY, null);
            }

            if (readException) {
                readException = false;
                initData();
            }
        }
    }

    private void getLocalApp() {
        packageInfos = packageManager.getInstalledPackages(0);
        localAppList.clear();
        cloudAppList.clear();
        if (pkgNameListStr != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                packageInfo = packageInfos.get(i);
                applicationInfo = packageInfo.applicationInfo;
                //非系统应用
                if (null != applicationInfo) {
                    if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) <= 0) {
                        String packageName = applicationInfo.packageName;
                        //如果包名
                        if (pkgNameListStr.contains(packageName) && !"cn.ngame.store".equals
                                (packageName)) {
                            //所有的包
                            String signPkg = MD5Utils.getMD5(content, packageName).toLowerCase()
                                    .trim();
                            if (CLOUD_MD5_SET != null && CLOUD_MD5_SET.contains(signPkg)) {
                                //再云端游戏列表里面
                                cloudAppList.add(packageInfo);
                            } else {
                                //不在,就放在本地
                                localAppList.add(packageInfo);
                            }
                        }
                    }
                }
            }
        }
    }

    private void initPop() {
        // 设置Action
        mItemClickQuickAction = new QuickAction(content, QuickAction.VERTICAL);
        ActionItem pointItem = new ActionItem(0, "卸载", null);
        mItemClickQuickAction.addActionItem(pointItem);
        mItemClickQuickAction.setOnActionItemClickListener(new QuickAction
                .OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                if (pos == 0) {
                    //删除文件下载任务
                    mfileUnstalledInfo = alreadyLvAdapter.getItemInfo();
                    if (mfileUnstalledInfo == null) {
                        mItemClickQuickAction.dismiss();
                        source.dismiss();
                        return;
                    }
                    //卸载
                    String packageName = mfileUnstalledInfo.applicationInfo.packageName;

                    AppInstallHelper.unstallApp(content, packageName);

                    if (null == packageName) {
                        return;
                    }
                    //删除安装包和正在下载的文件
                    List<FileLoadInfo> loadingFileInfo = fileLoad.getOpenFileInfo();
                    for (FileLoadInfo fileLoadInfo : loadingFileInfo) {
                        if (packageName.equals(fileLoadInfo.getPackageName())) {
                            fileLoad.delete(fileLoadInfo.getUrl());
                        }

                    }
                    mItemClickQuickAction.dismiss();
                    source.dismiss();
                }

            }
        });
    }

    private void initCloudPop() {
        // 设置Action
        mItemClickQuickActionCloud = new QuickAction(content, QuickAction.VERTICAL);
        ActionItem pointItem = new ActionItem(0, "卸载", null);
        mItemClickQuickActionCloud.addActionItem(pointItem);
        mItemClickQuickActionCloud.setOnActionItemClickListener(new QuickAction
                .OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                if (pos == 0) {
                    //删除文件下载任务
                    mfileUnstalledInfo = mCloudGameAdapter.getItemInfo();
                    if (mfileUnstalledInfo == null) {
                        mItemClickQuickActionCloud.dismiss();
                        source.dismiss();
                        return;
                    }
                    //卸载
                    String packageName = mfileUnstalledInfo.applicationInfo.packageName;

                    AppInstallHelper.unstallApp(content, packageName);

                    if (null == packageName) {
                        return;
                    }
                    //删除安装包和正在下载的文件
                    List<FileLoadInfo> loadingFileInfo = fileLoad.getOpenFileInfo();
                    for (FileLoadInfo fileLoadInfo : loadingFileInfo) {
                        if (packageName.equals(fileLoadInfo.getPackageName())) {
                            fileLoad.delete(fileLoadInfo.getUrl());
                        }

                    }
                    mItemClickQuickActionCloud.dismiss();
                    source.dismiss();
                }

            }
        });
    }

    private void getConnectBlueTooth() {
        int connectDevices = 0;
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;
        //得到BluetoothAdapter的Class对象
        try {//得到连接状态的方法
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState",
                    (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(adapter, (Object[]) null);

            if (state == BluetoothAdapter.STATE_CONNECTED) {
                LLog.d("BluetoothAdapter.STATE_CONNECTED");
                Set<BluetoothDevice> devices = adapter.getBondedDevices();
                LLog.d("devices:" + devices.size());

                try {
                    for (BluetoothDevice device : devices) {

                        Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod
                                ("isConnected", (Class[]) null);
                        method.setAccessible(true);
                        boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[])
                                null);
                        if (isConnected && device != null) {
                            connectDevices++;
                            if (!content.isFinishing() && content != null) {
                                mBlueToothConnectedTv.setText(getString(R.string.bt_connect_on) +
                                        " " + device.getName());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (connectDevices == 0) {
                    if (!content.isFinishing() && content != null) {
                        mBlueToothConnectedTv.setText(getString(R.string.bt_connect_off));
                    }
                }

            } else {
                if (connectDevices == 0) {
                    if (!content.isFinishing() && content != null) {
                        mBlueToothConnectedTv.setText(getString(R.string.bt_connect_off));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 设置顶部导航按钮的颜色切换
     *
     * @param isCloudSelected
     */
    private void setTopTabColor(boolean isCloudSelected) {
        mCloudTabTv.setSelected(isCloudSelected);
        mNativceTabTv.setSelected(!isCloudSelected);

        mCloudLine.setVisibility(isCloudSelected ? View.VISIBLE : View.INVISIBLE);
        mNativeLine.setVisibility(isCloudSelected ? View.INVISIBLE : View.VISIBLE);

        mCloudListView.setVisibility(isCloudSelected ? View.VISIBLE : View.INVISIBLE);

        emptyTv.setVisibility(isCloudSelected ? cloudAppList.size() == 0 ? View.VISIBLE : View
                .GONE : View.GONE);
    }


    //监听蓝牙
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bluetoothEvent(String blueToothMsg) {
        if (!content.isFinishing() && content != null && mBlueToothConnectedTv != null) {
            mBlueToothConnectedTv.setText(blueToothMsg);
        }
    }

    //映射服务
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void injectServerEvent(Boolean state) {
        mInjectServerState = state;
        if (!content.isFinishing() && content != null && mInjectServerConnectedTv != null) {
            mInjectServerConnectedTv.setText(getString(state ? R.string
                    .inject_server_state_on : R.string
                    .inject_server_state_off));

            mInjectServerBt.setClickable(true);
        }

        //映射服务开启
        if (state) {
            int navigationBarHeight = ImageUtil.getNavigationBarHeight(content);
            int screenWidth = ImageUtil.getScreenWidth(content);
            int screenHeight = ImageUtil.getScreenHeight(content);
            InjectDataMgr.sendScreenXY(screenWidth, screenHeight + navigationBarHeight);
        }
    }
}
