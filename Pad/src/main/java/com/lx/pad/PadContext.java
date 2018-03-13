package com.lx.pad;

import android.content.Context;

import com.lx.pad.util.LLog;

/**
 * Created by Administrator on 2017/12/12.
 */

public class PadContext {
    private static Context context = null;

    public static void setContext(Context c) {
        LLog.d("PadContext onCreate");
        context = c;
    }

    public static Context getContextObj(){
        return context;
    }
}
