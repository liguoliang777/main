package cn.ngame.store.core.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * App系统相关的功能
 * Created by zeng on 2016/12/2.
 */
public class SystemUtils {

    /**
     * 判断App是否在后台运行
     * @param context app上下文
     * @return  如果APP正在前台显示返回true
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

}
