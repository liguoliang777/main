package com.ngds.pad.Msg;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.lx.pad.util.LLog;
import com.ngds.pad.BaseEvent;
import com.ngds.pad.PadKeyEvent;
import com.ngds.pad.PadMotionEvent;
import com.ngds.pad.PadStateEvent;
import com.ngds.pad.utils.ActivityMgrUtils;

/**
 * Created by Administrator on 2017/12/9.
 */

/**
 * LooperThread不要独立调用，统一使用LooperEventManager来管理
 */

//整个消息队列用于处理按键消息
public class LooperThread extends Thread {
    public Handler mHandler;

    @Override
    public void run() {
        Looper.prepare();

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                LLog.d("LooperThread->run handleMessage what:" + msg.what + " arg1:" + msg.arg1);
                switch(msg.what){
                    case LooperEventManager.MSG_PAD_NORMAL:{
                        parserNormal(msg.arg1, msg.arg2);
                        break;
                    }
                    case LooperEventManager.MSG_PAD_KEYEVENT:{
                        BaseEvent baseEvent = msg.getData().getParcelable("event");
                        if(baseEvent != null){
                            if(baseEvent instanceof PadKeyEvent){
                                parserPadKeyEvent(msg.arg1, msg.arg2, (PadKeyEvent)baseEvent);
                            }
                        }
                        break;
                    }
                    case LooperEventManager.MSG_PAD_MOTIONEVENT:{
                        BaseEvent baseEvent = msg.getData().getParcelable("event");
                        if(baseEvent != null){
                            if(baseEvent instanceof PadMotionEvent){
                                parserPadMotionEvent(msg.arg1, msg.arg2, (PadMotionEvent)baseEvent);
                            }
                        }
                        break;
                    }
                    case LooperEventManager.MSG_PAD_STATEEVENT:{
                        BaseEvent baseEvent = msg.getData().getParcelable("event");
                        if(baseEvent != null){
                            if(baseEvent instanceof PadStateEvent){
                                parserPadStateEvent(msg.arg1, msg.arg2, (PadStateEvent)baseEvent);
                            }
                        }
                        break;
                    }
                }
            }
        };

        Looper.loop();
    }

    private void parserNormal(int arg1, int arg2){
        LLog.d("LooperThread->parserNormal arg1:" + arg1 + " arg2:" + arg2);
    }

    private void parserPadKeyEvent(int arg1, int arg2, PadKeyEvent event){
        LLog.d("LooperThread->parserPadKeyEvent " + String.format("arg1:%d arg2:%d deviceId:%d keyCode:%d action:%d pressure:%f",
                arg1, arg2, event.getControllerId(), event.getKeyCode(), event.getAction(), event.getPressure()) );
        //
        ActivityMgrUtils.sDispatchKeyEvent(event.getKeyCode(), event);
    }

    private void parserPadMotionEvent(int arg1, int arg2, PadMotionEvent event){
        LLog.d("LooperThread->parserPadMotionEvent " + String.format("arg1:%d arg2:%d deviceId:%d keyCode:%d m_x:%f m_y:%f",
                arg1, arg2, event.getControllerId(), event.getKeyCode(), event.getX(), event.getY()) );

        ActivityMgrUtils.sDispatchMotionEvent(event.getKeyCode(), event);
    }

    //有可能蓝牙突然断掉之类的需要将所有按键进行弹起操作
    private void parserPadStateEvent(int arg1, int arg2, PadStateEvent event){
        LLog.d("LooperThread->parserPadStateEvent " + String.format("arg1:%d arg2:%d deviceId:%d action:%d state:%d",
                arg1, arg2, event.getControllerId(), event.getAction(), event.getState()));

        ActivityMgrUtils.sDispatchStateEvent(event);
    }
}
