package com.ngame.widget;


import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/12/9.
 */

public final class FrameLayoutMgrUtils {
    public WindowManager m_windowManager;
    public WindowManager.LayoutParams m_layoutParams;
    public int m_halfWidth;
    public int m_halfHeight;
    public Context m_context;
    public FrameLayout[] m_frameLayouts;
    private static FrameLayoutMgrUtils sInstance;

    public FrameLayoutMgrUtils(Context context) {
        super();
        m_frameLayouts = new FrameLayout[26];
        m_context = context;
        m_windowManager = (WindowManager)m_context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        m_layoutParams = new WindowManager.LayoutParams();
        m_layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;    // TYPE_PHONE 电话窗口，应用之上状态栏之下
        m_layoutParams.format = PixelFormat.RGBA_8888;  //期望的位图格式，默认不透明
        m_layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// |   //不获得焦点
//                                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;   //让window占满整个手机屏幕不留任何边界
        m_layoutParams.gravity = Gravity.TOP | Gravity.LEFT;    // 窗口如何停靠
    }

    public final boolean hasFrameLayout(){
        boolean result = false;
        FrameLayout[] frameLayouts = m_frameLayouts;
        int nLen = frameLayouts.length;
        for(int i = 0; i < nLen; i++){
            if(frameLayouts[i] != null){
                result = true;
                break;
            }
        }
        return result;
    }

    public static FrameLayoutMgrUtils getInstance(Context context){
        if(sInstance == null){
            sInstance = new FrameLayoutMgrUtils(context);
        }
        return sInstance;
    }

    public final void removeAllFrameLayoutView(){
        for(int i = 0; i < m_frameLayouts.length; i++){
            if(m_frameLayouts[i] != null){
                m_windowManager.removeView(m_frameLayouts[i]);
                m_frameLayouts[i] = null;
            }
        }
    }
}
