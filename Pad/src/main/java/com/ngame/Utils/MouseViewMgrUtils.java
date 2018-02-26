package com.ngame.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.lx.pad.R;
import com.lx.pad.util.LLog;
import com.ngame.widget.MouseView;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MouseViewMgrUtils {
    private static MouseView m_mouseView;
    private static WindowManager m_windowMgr;
    private static WindowManager.LayoutParams m_layoutParams;
    private static boolean m_bMouseAdded;
    private static int m_statusBarHeight;
    private static float m_axisX;
    private static float m_axisY;
    private static Timer m_mouseViewTimer = new Timer();
    private static TimerTask m_timerTask;
    private static Handler m_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch(msg.what){
                case 1:{
                    if(m_mouseView != null) {
                        m_mouseView.setVisibility(View.GONE);
                    }
                    break;
                }
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    public static boolean sMouseViewState(){
//        LLog.d("MouseViewMgrUtils->sMouseViewState  --------------->  m_bMouseAdded:" + m_bMouseAdded);
        return m_bMouseAdded;
    }

    public static void sRemoveMouseView(){
//        if(m_mouseViewTimer != null){
//            m_mouseViewTimer.cancel();
//            m_mouseViewTimer.purge();
////            m_mouseViewTimer = null;
//        }
        if(m_timerTask != null){
            m_timerTask.cancel();
            m_timerTask = null;
        }
//        LLog.d("MouseViewMgrUtils->sRemoveMouseView  --------------->  m_bMouseAdded:" + m_bMouseAdded);
        m_bMouseAdded = false;
        m_handler.sendEmptyMessage(1);
    }

    public static synchronized void sAddMouseView(Context context){
        if(!m_bMouseAdded){
            if(m_mouseView == null){
                m_mouseView = new MouseView(context.getApplicationContext(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.pointer_arrow));
            }

            if(m_windowMgr == null){
                m_windowMgr = (WindowManager)context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

                if(m_layoutParams == null){
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    m_layoutParams = layoutParams;
                    layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
                    m_layoutParams.flags = FLAG_NOT_TOUCHABLE | FLAG_NOT_TOUCH_MODAL | FLAG_NOT_FOCUSABLE;
                    m_layoutParams.format = PixelFormat.RGBA_8888;
                    m_layoutParams.width = -1;
                    m_layoutParams.height = -1;
                }

                if(m_statusBarHeight < 0){
                    Resources res = context.getResources();
                    int nStatusBarHeight = res.getIdentifier("status_bar_height", "dimen", "android");
                    if(nStatusBarHeight > 0){
                        nStatusBarHeight = res.getDimensionPixelSize(nStatusBarHeight);
                    }
                    m_statusBarHeight = nStatusBarHeight;
                }

                m_windowMgr.addView(m_mouseView, m_layoutParams);
            }
            m_mouseView.m_x = m_mouseView.getXmax() / 2f;
            m_mouseView.m_y = m_mouseView.getYmax() / 2f;
            m_mouseView.postInvalidate();
            sShowMouseView(0f, 0f);
            m_timerTask = new TimerTask() {
                @Override
                public void run() {
                    float xOffset = m_axisX * 15f;
                    float yOffset = m_axisY * 15f;
                    m_mouseView.m_x = xOffset + m_mouseView.m_x;
                    if(m_mouseView.m_x < 0f){
                        m_mouseView.m_x = 0f;
                    }else if(m_mouseView.m_x > m_mouseView.getXmax()){
                        m_mouseView.m_x = m_mouseView.getXmax();
                    }

                    m_mouseView.m_y += yOffset;
                    if(m_mouseView.m_y < 0f){
                        m_mouseView.m_y = 0f;
                    }else if(m_mouseView.m_y > m_mouseView.getYmax()){
                        m_mouseView.m_y = m_mouseView.getYmax();
                    }
                    m_mouseView.postInvalidate();
                }
            };
            m_mouseViewTimer.schedule(m_timerTask, 200, 20);
            m_bMouseAdded = true;
            Toast.makeText(context, "***鼠标已开启***", Toast.LENGTH_SHORT);
        }
    }

    public static void sShowMouseView(float axisX, float axisY){
        LLog.d(String.format("MouseViewMgrUtils->sShowMouseView axisX:%d axisY:%d", (int)axisX, (int)axisY));

        m_axisX = axisX;
        m_axisY = axisY;
        m_mouseView.setVisibility(View.VISIBLE);
        m_handler.removeMessages(1);
        m_handler.sendEmptyMessageDelayed(1, 5000);
    }

    public static float sGetMouseViewPtX(){
        return m_mouseView.getPointerX();
    }

    public static float sGetMouseViewPtY(){
        return m_mouseView.getPointerY()/* + m_statusBarHeight*/;
    }
}
