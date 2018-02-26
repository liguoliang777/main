package com.lx.pad;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/12/12.
 */

public class MyApplication extends Application {
    private static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContextObj(){
        return context;
    }
}
