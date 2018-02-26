package com.ngame.Utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.lx.pad.MyApplication;

/**
 * Created by Administrator on 2017/12/5.
 */

public class DisplayMetricsMgr {
    private static int width = 0;  //填值时必然大于height(如果height>width，那这里就会跟height的值交换)
    private static int height = 0;

    public static float getHeightScale(Context context){
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if(Build.VERSION.SDK_INT >= 17){
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        }else{
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        float retScal = displayMetrics.heightPixels / 640f;
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            retScal = displayMetrics.heightPixels / 360f;
        }
        return retScal;
    }

    public static Point getDisplayInfo(){
        WindowManager wm = (WindowManager)MyApplication.getContextObj().getSystemService(Context.WINDOW_SERVICE);
        Point pt = new Point();
        pt.x = wm.getDefaultDisplay().getWidth();
        pt.y = wm.getDefaultDisplay().getHeight();
        return pt;
    }

    public static int getLessSide(){
        WindowManager wm = (WindowManager)MyApplication.getContextObj().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return (width < height) ? width : height;
    }

    public static int getDisWidthMax(){
        if(width == 0){
            WindowManager wm = (WindowManager)MyApplication.getContextObj().getSystemService(Context.WINDOW_SERVICE);
            int w = wm.getDefaultDisplay().getWidth();
            int h = wm.getDefaultDisplay().getHeight();
            width = (w > h) ? w : h;
        }
        return width;
    }

    public static int getDisHeightMin(){
        if(height == 0){
            WindowManager wm = (WindowManager)MyApplication.getContextObj().getSystemService(Context.WINDOW_SERVICE);
            int w = wm.getDefaultDisplay().getWidth();
            int h = wm.getDefaultDisplay().getHeight();
            height = (w < h) ? w : h;
        }
        return height;
    }

    public static int getOri(){
        int result = 0;

        WindowManager windowManager = (WindowManager) MyApplication.getContextObj().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if(Build.VERSION.SDK_INT >= 17){
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        }else{
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        if(MyApplication.getContextObj().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            return 0;
        }else if(MyApplication.getContextObj().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            return 1;
        }else{
            return MyApplication.getContextObj().getResources().getConfiguration().orientation;
        }

//        return result;
    }

    public static int dip2px(Context context, float dpVal){
        return (dpVal <= 0f) ? (int)dpVal : (int)(context.getResources().getDisplayMetrics().density * dpVal + 0.5f);
    }

    public static int px2dip(Context context, float pxVal){
        return (pxVal <= 0f) ? (int)pxVal : (int)(pxVal / DisplayMetricsMgr.getWidthScale(context) + 0.5f);
    }

    public static int sWidthScaleToPx(Context context, float dpVal){
        return (dpVal <= 0f) ? (int)dpVal : (int)(DisplayMetricsMgr.getWidthScale(context) * dpVal + 0.5f);
    }

    private static float getWidthScale(Context context){
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if(Build.VERSION.SDK_INT >= 17){
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        }else{
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        float retScale = displayMetrics.widthPixels / 360f;
        //判断是否是全屏
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            retScale = displayMetrics.widthPixels / 640f;
        }
        return retScale;
    }

    public static int getStatusBarHeight(Context context){
        int result = 0;
        int resourceID = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resourceID > 0){
            result = context.getResources().getDimensionPixelSize(resourceID);
        }
        return result;
    }
}
