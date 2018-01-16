package cn.ngame.store.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.ngame.store.R;
import cn.ngame.store.base.service.ConnectionChangeReceiver;

/**
 * 通用基类
 */
@SuppressLint("WrongConstant")
public class BaseFgActivity extends FragmentActivity {

    public ConnectionChangeReceiver myReceiver;
    protected static final String TAG = "777";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        registerReceiver();
    }

    /* 注册广播，监听网络异常 */
    public void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);

    }

    /* 取消网络监听 */
    public void unregisterReceiver() {
        if (myReceiver != null) {
            this.unregisterReceiver(myReceiver);
        }
    }

    /**
     * 点击空白处，关闭软键盘
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }


    //沉浸式状态栏
    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    //透明状态栏
    protected void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            //tintManager.setStatusBarTintEnabled(true); // 激活导航栏设//
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.mainColor);//通知栏所需颜色
        }
    }
}
