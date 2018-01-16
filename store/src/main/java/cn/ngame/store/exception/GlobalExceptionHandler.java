/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ngame.store.exception;

import android.content.Context;
import android.os.Looper;

import com.umeng.analytics.MobclickAgent;

import java.lang.Thread.UncaughtExceptionHandler;

import cn.ngame.store.core.utils.Log;

/**
 * 自定义全局未捕获异常处理
 * @author   flan
 * @since    2015年11月17日
 */
public class GlobalExceptionHandler implements UncaughtExceptionHandler {

    private static final String TAG = GlobalExceptionHandler.class.getSimpleName();

    private Context context;

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;

    private static GlobalExceptionHandler globalExceptionHandler;

    private GlobalExceptionHandler() {}

    /**
     * 单例模式创建全局异常处理对象
     */
    public static GlobalExceptionHandler getInstance(){
        if(globalExceptionHandler == null){
            globalExceptionHandler = new GlobalExceptionHandler();
            return globalExceptionHandler;
        }
        return globalExceptionHandler;
    }

    public void init(Context context){
        this.context = context;

        //获取系统默认的UncaughtException处理器
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        //为系统设置自定义的全局异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        Log.d(TAG,"全局异常处理器初始化完毕！");
    }

    /**
     * 出现未捕获的异常时会进入该方法中进行处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        MobclickAgent.reportError(context,ex);

        if(!handlerException(ex) && defaultExceptionHandler != null){

            Log.d(TAG, "自定义的全局异常处理器不处理，交给系统处理！");
            Log.e(TAG, "处理器异常:"+ex.getMessage());
            defaultExceptionHandler.uncaughtException(thread, ex);

        }else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(TAG, "全局异常 : "+e.getMessage());
            }

            Log.e(TAG,"==============>>>>> 严重，APP异常退出了！！！！！");

            MobclickAgent.onKillProcess(context);   //发送友盟异常统计
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 全局异常处理逻辑
     * @param ex 异常信息
     * @return	true 该方法已处理了异常，false 该方法未处理异常
     */
    private boolean handlerException(Throwable ex){

        Log.e(TAG, "//////////////////////////// 全局异常处理器，处理未捕获异常 ////////////////////");
        Log.e(TAG, " ============== > 异常:"+ex.toString());

        if(ex == null){
            return false;
        }

        ex.printStackTrace();

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                //Toast.makeText(context, "很抱歉,程序出现未知异常，即将退出", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        /*Intent intent = new Intent(context.getApplicationContext(),BeginActivity.class);
        PendingIntent restartIntent = PendingIntent.getActivity(
                context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, restartIntent); // 1秒钟后重启应用*/

        return true;
    }

}
