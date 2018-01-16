package cn.ngame.store.activity.sm;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.DataCleanManager;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.core.utils.SPUtils;
import cn.ngame.store.util.ToastUtil;

/**
 * App设置页面
 * Created by zeng on 2016/12/7.
 */
public class SystemSettingsActivity extends BaseFgActivity implements CompoundButton.OnCheckedChangeListener {

    private ToggleButton but_push, but_load, but_install;
    private int delayMillis = 100;
    private SystemSettingsActivity content;
    private TextView tv_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_manager_settings);
        content = this;
        Button left_but = (Button) findViewById(R.id.left_bt);
        left_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        but_push = (ToggleButton) findViewById(R.id.but_push);
        if (StoreApplication.isReceiveMsg) {
            but_push.setChecked(true);
        } else {
            but_push.setChecked(false);
        }

        but_load = (ToggleButton) findViewById(R.id.but_load);
        if (StoreApplication.allowAnyNet) {
            but_load.setChecked(true);
        } else {
            but_load.setChecked(false);
        }

        but_install = (ToggleButton) findViewById(R.id.but_install);
        if (StoreApplication.isDeleteApk) {
            but_install.setChecked(true);
        } else {
            but_install.setChecked(false);
        }

        //but_update = (ToggleButton) findViewById(R.id.but_update);

        tv_clear = (TextView) findViewById(R.id.tv_clear);
        RelativeLayout layout_1 = (RelativeLayout) findViewById(R.id.layout_1);
        layout_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = tv_clear.getText().toString();
                if ("0KB".equals(text)) {
                    ToastUtil.show(SystemSettingsActivity.this, "没有缓存了~");
                    return;
                }

                if (text.endsWith("MB")) {
                    delayMillis = 1000;
                } else if (text.endsWith("KB")) {
                    delayMillis = 200;
                } else {
                    delayMillis = 1000;
                }
                showLogoutDialog();
            }
        });

        but_push.setOnCheckedChangeListener(this);
        but_load.setOnCheckedChangeListener(this);
        but_install.setOnCheckedChangeListener(this);
        //but_update.setOnCheckedChangeListener(this);

        //显示App缓存
        try {
            String cacheSize = DataCleanManager.getTotalCacheSize(this);
            tv_clear.setText(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //退出登录
    public void showLogoutDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog_From_Bottom_Style);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_dialog_logout, null);

        TextView title_tv = (TextView) inflate.findViewById(R.id.dialog_top_title_tv);
        title_tv.setText("确定清除缓存数据吗?");
        TextView clearBt = (TextView) inflate.findViewById(R.id.logout_yes_bt);
        clearBt.setText("清除数据");
        clearBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                DataCleanManager.clearAllCache(SystemSettingsActivity.this);
                final DialogHelper dialogHelper = new DialogHelper(getSupportFragmentManager(), SystemSettingsActivity.this);
                dialogHelper.showAlert("清理中...", false);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogHelper.hideAlert();
                        ToastUtil.show(SystemSettingsActivity.this, "缓存已清除~");
                        if (null != tv_clear) {
                            tv_clear.setText("0KB");
                        }
                    }
                }, delayMillis);
            }
        });
        inflate.findViewById(R.id.logout_cancel_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.setContentView(inflate);//将布局设置给Dialog

        setDialogWindow(dialog);
    }

    private void setDialogWindow(Dialog dialog) {
        Window dialogWindow = dialog.getWindow(); //获取当前Activity所在的窗体
        dialogWindow.setGravity(Gravity.BOTTOM);//设置Dialog从窗体底部弹出
        WindowManager.LayoutParams params = dialogWindow.getAttributes();   //获得窗体的属性
        //params.y = 20;  Dialog距离底部的距离
        params.width = WindowManager.LayoutParams.MATCH_PARENT;//设置Dialog距离底部的距离
        dialogWindow.setAttributes(params); //将属性设置给窗体
        dialog.show();//显示对话框
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.but_push:
                doPushClick(isChecked);
                break;
            case R.id.but_load:
                doLoadClick(isChecked);
                break;
            case R.id.but_install:
                StoreApplication.isDeleteApk = isChecked;
                if (isChecked) {
                    SPUtils.put(this, Constant.CFG_DELETE_APK, true);
                } else {
                    SPUtils.put(this, Constant.CFG_DELETE_APK, false);
                }
                break;
            //游戏更新提醒
           /* case R.id.but_update:
                if(isChecked){
                    SPUtils.put(this,Constant.CFG_NOTIFY_GAME_UPDATE,true);
                }else {
                    SPUtils.put(this,Constant.CFG_NOTIFY_GAME_UPDATE,false);
                }
                break;*/
        }
    }

    private void doPushClick(boolean isChecked) {
        StoreApplication.isReceiveMsg = isChecked;
        if (isChecked) {
            SPUtils.put(this, Constant.CFG_RECEIVE_MSG, true);
            //启动百度推送
            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, Constant.PUSH_API_KEY);
            PushManager.disableLbs(this);   //关闭精确LBS推送模式
        } else {
            SPUtils.put(this, Constant.CFG_RECEIVE_MSG, false);
            //关闭百度推送
            PushManager.stopWork(this);
        }
    }

    private void doLoadClick(boolean isChecked) {
        //允许 isChecked=true, 
        StoreApplication.allowAnyNet = isChecked;
        //如果允许
        if (isChecked) {
            DialogHelper dialogHelper = new DialogHelper(getSupportFragmentManager(), this);
            dialogHelper.showConfirm("提示", "允许数据流量下载可能导致手机流量超额，确认开启？", "取消", "开启", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    but_load.setChecked(false);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SPUtils.put(SystemSettingsActivity.this, Constant.CFG_ALLOW_4G_LOAD, true);
                }
            });
        } else {
            SPUtils.put(this, Constant.CFG_ALLOW_4G_LOAD, false);
        }
    }

}
