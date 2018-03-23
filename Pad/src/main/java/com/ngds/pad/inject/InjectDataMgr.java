package com.ngds.pad.inject;

import android.os.Handler;
import android.os.Message;

import com.lx.pad.util.LLog;
import com.ngds.pad.BaseEvent;

/**
 * Created by Administrator on 2017/12/12.
 */

//管理 与映射端通迅  的类
public class InjectDataMgr {
    public static final byte VERSION = 1;       //版本号   版本号为0时为心跳包
    public static final byte SCREEN_XY = 2;       //版本号   版本号为0时为心跳包
    public static final byte ACTION_PRESS = 0;  //按下
    public static final byte ACTION_MOVE = 1;   //移动
    public static final byte ACTION_UP = 2;     //弹起

    private static Handler handler = null;
    private static ClientFinally clientFinally = null;

    private static class RecvHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            LLog.d("InjectDataMgr->handleMessage what:" + msg.what);
        }
    }

    public static void initSocket(boolean reset) {
        if (clientFinally != null) {
            if (!reset)
                return;
            clientFinally.close();
            clientFinally = null;
        }
        if (handler == null) {
            handler = new RecvHandler();
        }
        clientFinally = new ClientFinally(handler);
        new Thread(clientFinally).start();
        LLog.d("InjectDataMgr->initSocket");
    }

    //判断当前Injecte服务是否己经启动
    public static boolean isInjectStart() {
        boolean result = false;

        if (clientFinally != null) {
            result = clientFinally.getSocketConnectState();
        }

        return result;
    }

    public static byte makeCheckSum(byte[] bAry) {
        int sum = 0;
        for (int i = 1; i < bAry.length - 1; i++) {
            sum += bAry[i];
        }
        LLog.d("InjectDataMgr->makeCheckSum : " + Integer.toHexString(sum));
        return (byte) sum;
    }

    public static byte getKeySid(int keyCode) {
        byte keySid = -1;
        switch (keyCode) {
            case BaseEvent.KEYCODE_BACK:
                keySid = 1;
                break;
            case BaseEvent.KEYCODE_DPAD_UP:
                keySid = 2;
                break;
            case BaseEvent.KEYCODE_DPAD_DOWN:
                keySid = 3;
                break;
            case BaseEvent.KEYCODE_DPAD_LEFT:
                keySid = 4;
                break;
            case BaseEvent.KEYCODE_DPAD_RIGHT:
                keySid = 5;
                break;
            case BaseEvent.KEYCODE_BUTTON_A:
                keySid = 6;
                break;
            case BaseEvent.KEYCODE_BUTTON_B:
                keySid = 7;
                break;
            case BaseEvent.KEYCODE_BUTTON_X:
                keySid = 8;
                break;
            case BaseEvent.KEYCODE_BUTTON_Y:
                keySid = 9;
                break;
            case BaseEvent.KEYCODE_BUTTON_L1:
                keySid = 10;
                break;
            case BaseEvent.KEYCODE_BUTTON_R1:
                keySid = 11;
                break;
            case BaseEvent.KEYCODE_BUTTON_L2:
                keySid = 12;
                break;
            case BaseEvent.KEYCODE_BUTTON_R2:
                keySid = 13;
                break;
            case BaseEvent.KEYCODE_BUTTON_THUMBL:
                keySid = 14;
                break;
            case BaseEvent.KEYCODE_BUTTON_THUMBR:
                keySid = 15;
                break;
            case BaseEvent.KEYCODE_BUTTON_START:
                keySid = 16;
                break;
            case BaseEvent.KEYCODE_LEFT_STICK:
                keySid = 17;
                break;
            case BaseEvent.KEYCODE_RIGHT_STICK:
                keySid = 18;
                break;
            default:
                LLog.d("InjectDataMgr->getKeySid error! default. keyCode:" + keyCode);
                break;
        }
        return keySid;
    }

    //keySid  keyCode --> keySid (keyCode对应的一个索引ID，用于在底层将触控操作分离开来，即支持多点触控)
    //keyAction 按下、移动、弹起
    public static void sendKeyInfo(byte keySid, byte keyAction, int x, int y) {
        LLog.d("InjectDataMgr:sendKeyInfo:" + keySid + "," + keyAction + "," + x + "," + y);
        byte[] bAry = new byte[30];
        bAry[1] = VERSION;
        bAry[2] = keySid;
        bAry[3] = keyAction;
        //将整型X坐标转换成内存对应的值写入数组
        bAry[4] = (byte) (x & 0xFF);         //屏英坐标不会超过0x7FFF（32767)
        bAry[5] = (byte) ((x >> 8) & 0xFF);
        //将整型Y坐标转换成内存对应的值写入数组
        bAry[6] = (byte) (y & 0xFF);
        bAry[7] = (byte) ((y >> 8) & 0xFF);
        //生成校验值
        bAry[0] = makeCheckSum(bAry);

        sendData(bAry);
    }

    //keySid  keyCode --> keySid (keyCode对应的一个索引ID，用于在底层将触控操作分离开来，即支持多点触控)
    //keyAction 按下、移动、弹起
    public static void sendScreenXY(int x, int y) {
        LLog.d("InjectDataMgr:sendKeyInfo:" + "," + x + "," + y);
        byte[] bAry = new byte[30];
        bAry[1] = SCREEN_XY;
        bAry[2] = 0;
        bAry[3] = 0;
        //将整型X坐标转换成内存对应的值写入数组
        bAry[4] = (byte) (x & 0xFF);         //屏英坐标不会超过0x7FFF（32767)
        bAry[5] = (byte) ((x >> 8) & 0xFF);
        //将整型Y坐标转换成内存对应的值写入数组
        bAry[6] = (byte) (y & 0xFF);
        bAry[7] = (byte) ((y >> 8) & 0xFF);
        //生成校验值
        bAry[0] = makeCheckSum(bAry);

        sendData(bAry);
    }

    public static void sendData(byte[] bytes) {
        if (clientFinally != null) {
            clientFinally.send(bytes);
        }
    }
}
