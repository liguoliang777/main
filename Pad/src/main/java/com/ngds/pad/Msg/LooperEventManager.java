package com.ngds.pad.Msg;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lx.pad.util.LLog;
import com.ngds.pad.BaseEvent;
import com.ngds.pad.inject.InjectDataMgr;

/**
 * Created by Administrator on 2017/12/9.
 */

/**
 * 消息队列管理类，所有按键消息最终都会放入到到消息列中
 */

public class LooperEventManager {
    private static LooperThread looperThread = null;

    public static final int MSG_PAD_NORMAL = 0;
    public static final int MSG_PAD_KEYEVENT = 1;   //对应PadKeyEvent
    public static final int MSG_PAD_MOTIONEVENT = 2;    //对应PadMotionEvent
    public static final int MSG_PAD_STATEEVENT = 3;     //对应PadStateEvent


    public LooperEventManager() {
        super();
        init();
    }

    public static void init(){
        if(looperThread == null){
            looperThread = new LooperThread();
            looperThread.start();
            try {
                //尽量保证线程已启动
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LLog.d("LooperEventManager->LooperThread  init");
        }

        InjectDataMgr.initSocket(false);
    }

    public static boolean isInit(){
        return looperThread != null;
    }

    public static void sendEventMsg(int arg1, int arg2){
        if(looperThread != null){
            Handler handler = looperThread.mHandler;
            Message msg = Message.obtain();
            msg.what = MSG_PAD_NORMAL;
            msg.arg1 = arg1;
            msg.arg2 = arg2;
            handler.sendMessage(msg);
        }
    }

    public static void sendEventMsg(int msgType, BaseEvent baseEvent){
        if(looperThread != null){
            LLog.d("LooperEventManager->发送事件类型( 1=按键,2=遥杆) :" + msgType);
            Handler handler = looperThread.mHandler;
            Bundle bundle = new Bundle();
            bundle.putParcelable("event", baseEvent);
            Message msg = Message.obtain();
            msg.what = msgType;
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }
}
