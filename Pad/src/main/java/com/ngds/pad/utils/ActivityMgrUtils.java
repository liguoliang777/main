package com.ngds.pad.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.lx.pad.MainActivity;
import com.lx.pad.PadContext;
import com.lx.pad.util.LLog;
import com.ngame.Utils.DisplayMetricsMgr;
import com.ngame.Utils.KeyMgrUtils;
import com.ngame.Utils.MouseViewMgrUtils;
import com.ngame.app.KeyboardEditActivity;
import com.ngds.pad.BaseEvent;
import com.ngds.pad.PadKeyEvent;
import com.ngds.pad.PadMotionEvent;
import com.ngds.pad.PadStateEvent;
import com.ngds.pad.inject.InjectDataMgr;
import com.ngds.pad.server.DeviceManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/12/12.
 */

public class ActivityMgrUtils {

    private static boolean btnStartPress = false;
    private static boolean btnL1Press = false;
    private static boolean btnHelpPress = false;
    private static boolean btnThumbL = false;
    private static boolean btnThumbR = false;
    private static boolean leftStickPress = false;  //并不是摇杆按下，而是指当前摇杆是否处理摇动模式
    private static boolean rightStickPress = false;  //并不是摇杆按下，而是指当前摇杆是否处理摇动模式

    private static int relateLeftStickKeyCode = -1;  //关联左摇杆的keyCode
    private static int offsetLeftStickX = 0;
    private static int offsetLeftStickY = 0;
    private static int relateRightStickKeyCode = -1; //关联右摇杆的keyCode
    private static int offsetRightStickX = 0;
    private static int offsetRightStickY = 0;

    private static final Timer timerLeft = new Timer();
    private static final Timer timerRight = new Timer();
    private static TimerTask leftStickTask = null;
    private static TimerTask rightStickTask = null;

    private static int leftCurMaxOffsetX = 0;
    private static int leftCurMaxOffsetY = 0;
    private static int leftCurX = 0;
    private static int leftCurY = 0;
    private static boolean leftStickRestart = true;
    private static int rightCurMaxOffsetX = 0;
    private static int rightCurMaxOffsetY = 0;
    private static int rightCurX = 0;
    private static int rightCurY = 0;
    private static boolean rightStickRestart = true;

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    if (!MouseViewMgrUtils.sMouseViewState()) {
                        MouseViewMgrUtils.sAddMouseView(PadContext.getContextObj());
                    } else {
                        MouseViewMgrUtils.sRemoveMouseView();
                    }
                    break;
                }
                case 2: {
                    KeyMgrUtils.promptFrameLayoutView(PadContext.getContextObj());
                    break;
                }
                case 3: {
                    Message ms = new Message();
                    ms.what = MainActivity.MSG_CMD_START_KEYBOARD_EDIT_ACTIVITY;
                    MainActivity.handler.sendMessage(ms);
                    break;
                }
            }
        }
    };

    public static boolean sDispatchKeyEvent(int keyCode, PadKeyEvent padKeyEvent) {
        int x = KeyMgrUtils.sGetKeyInfoXByKeyCode(keyCode);
        int y = KeyMgrUtils.sGetKeyInfoYByKeyCode(keyCode);
        int r = KeyMgrUtils.sGetKeyInfoRByKeyCode(keyCode);
        int mode = KeyMgrUtils.sGetKeyInfoModeByKeyCode(keyCode);   //是否摇杆关联
        int flag = KeyMgrUtils.sGetKeyInfoFlagByKeyCode(keyCode);   //是否反向技能
        LLog.d("ActivityMgrUtils->按键事件: keyCode:" + keyCode + " action:" + (padKeyEvent.getAction
                () == 0 ? "press " : "up ") +
                String.format("x:%d, y:%d, r:%d mode:%d flag:%d",
                        x, y, r, mode, flag));

//        if(MouseViewMgrUtils.sMouseViewState()){
//            x = (int)MouseViewMgrUtils.sGetMouseViewPtX();
//            y = (int)MouseViewMgrUtils.sGetMouseViewPtY();
//        }

        int action = padKeyEvent.getAction();

        if (padKeyEvent.getAction() == BaseEvent.ACTION_PRESSURE) {
            if (padKeyEvent.getPressure() > 0.2f) {
                action = BaseEvent.ACTION_DOWN;
            } else {
                action = BaseEvent.ACTION_UP;
            }
        }

        //更新摇杆关联的状态，如果多个关联的按键来了也只关联第一个，后续的都为普通按键
        if (action == BaseEvent.ACTION_DOWN) {
            if (mode == KeyMgrUtils.KEYMODE_LEFT_STICK && !leftStickPress) {
                if (relateLeftStickKeyCode < 0) {
                    relateLeftStickKeyCode = keyCode;
                }
            } else if (mode == KeyMgrUtils.KEYMODE_RIGHT_STICK && !rightStickPress) {
                if (relateRightStickKeyCode < 0) {
                    relateRightStickKeyCode = keyCode;
                }
            }

        } else if (action == BaseEvent.ACTION_UP) {   //弹起时如果是关联按键需要修改弹起点
            if (relateLeftStickKeyCode >= 0 && relateLeftStickKeyCode == keyCode) {
                relateLeftStickKeyCode = -1;
                //判断是否反向技能
                if ((flag & 0x1) != 0) {
                    offsetLeftStickX = -offsetLeftStickX;
                    offsetLeftStickY = -offsetLeftStickY;
                }
                x = x + offsetLeftStickX;
                y = y - offsetLeftStickY;
            }
            if (relateRightStickKeyCode >= 0 && relateRightStickKeyCode == keyCode) {
                relateRightStickKeyCode = -1;
                //判断是否反向技能
                if ((flag & 0x1) != 0) {
                    offsetRightStickX = -offsetRightStickX;
                    offsetRightStickY = -offsetRightStickY;
                }
                x = x + offsetRightStickX;
                y = y - offsetRightStickY;
            }
        }

        //改变部分特定按键的状态，可以用于处理组合键问题
        boolean pressed = (padKeyEvent.getAction() == BaseEvent.ACTION_DOWN) ? true : false;
        switch (keyCode) {
            case BaseEvent.KEYCODE_BUTTON_HELP: {
                btnHelpPress = pressed;
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_START: {
                btnStartPress = pressed;
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_L1: {
                btnL1Press = pressed;
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_THUMBL: {
                btnThumbL = pressed;
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_THUMBR: {
                btnThumbR = pressed;
                break;
            }
        }

        if (btnStartPress && btnL1Press) {
            btnHelpPress = true;
        } else if ((!btnStartPress || !btnL1Press) && (keyCode != BaseEvent.KEYCODE_BUTTON_HELP)) {
            btnHelpPress = false;
        }

        //需要即时操作的放这里
        Context context = PadContext.getContextObj();
        if (btnHelpPress) {
//            Intent intent = new Intent();
//            intent.setPackage(context.getPackageName());
//            intent.setAction("com.ngame.padtool.app.KeyboardEditActivity");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);


            //处理i 键  / start+LB按下
            final KeyboardEditActivity mKeyboardEditAty = KeyboardEditActivity.getInstance();
            if (mKeyboardEditAty == null || mKeyboardEditAty.isFinishing()) {
                Intent intent = new Intent(context, KeyboardEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                mKeyboardEditAty.finish();
            }
            return true;
        } else if (btnThumbL) {
//            KeyMgrUtils.promptFrameLayoutView(context);
//            return true;
        } else if (btnThumbR) {
//            if(!MouseViewMgrUtils.sMouseViewState()) {
//                MouseViewMgrUtils.sAddMouseView(PadContext.getContextObj());
//            }else{
//                MouseViewMgrUtils.sRemoveMouseView();
//            }
//            return true;
        }

        //需要延时的放这里
        if (keyCode == BaseEvent.KEYCODE_BUTTON_THUMBR) {
            handler.removeMessages(1);
            if (btnThumbR) {
                handler.sendEmptyMessageDelayed(1, 3000);
            }
        } else if (keyCode == BaseEvent.KEYCODE_BUTTON_THUMBL) {
            handler.removeMessages(2);
            if (btnThumbL) {
                handler.sendEmptyMessageDelayed(2, 3000);
            }
        } else if (keyCode == BaseEvent.KEYCODE_BUTTON_START
                || keyCode == BaseEvent.KEYCODE_BUTTON_L1
                || keyCode == BaseEvent.KEYCODE_BUTTON_HELP) {
//            handler.removeMessages(3);
//            if(btnHelpPress){
//                handler.sendEmptyMessageDelayed(3, 3000);
//            }
        }

        if (x >= 0 && y >= 0) {
            byte keySid = InjectDataMgr.getKeySid(keyCode);
            int nLessSide = DisplayMetricsMgr.getLessSide();
            if (keySid > 0) {
                int nTmp = x;
                x = nLessSide - y;
                y = nTmp;
                LLog.d("ActivityMgrUtils->按键状态 ori:" + DisplayMetricsMgr.getOri() + " nLessSide:"
                        + nLessSide);
                if (action == BaseEvent.ACTION_DOWN) {
                    InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr.ACTION_PRESS, x, y);
                } else if (action == BaseEvent.ACTION_UP) {
                    InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr.ACTION_UP, x, y);
                    //按键弹起时根据状态清除指定数据
                    if (relateLeftStickKeyCode < 0) {
                        offsetLeftStickX = 0;
                        offsetLeftStickY = 0;
                    }
                    if (relateRightStickKeyCode < 0) {
                        offsetRightStickX = 0;
                        offsetRightStickY = 0;
                    }
                } else if (action == BaseEvent.ACTION_PRESSURE) {
                    LLog.d("ActivityMgrUtils->sDispatchKeyEvent ACTION_PRESSURE not realize");
                } else {
                    LLog.d("ActivityMgrUtils->sDispatchKeyEvent ACTION else not realize, error?  " +
                            "action:" + action);
                }
            }
        }


        return true;
    }

    static int radius = 120;

    public static boolean sDispatchMotionEvent(final int keyCode, PadMotionEvent padMotionEvent) {
        int x = KeyMgrUtils.sGetKeyInfoXByKeyCode(keyCode);
        int y = KeyMgrUtils.sGetKeyInfoYByKeyCode(keyCode);
        int r = KeyMgrUtils.sGetKeyInfoRByKeyCode(keyCode);
        int mode = KeyMgrUtils.sGetKeyInfoModeByKeyCode(keyCode);
        int frequency = KeyMgrUtils.sGetKeyInfoFrequencyByKeyCode(keyCode);
        int step = KeyMgrUtils.sGetKeyInfoStepByKeyCode(keyCode);
        LLog.d("ActivityMgrUtils->sDispatchMotionEvent keyCode:" + keyCode + " " +
                String.format("x:%d, y:%d, r:%d mode:%d frequency:%d step:%d",
                        x, y, r, mode, frequency, step));

        if (keyCode == BaseEvent.KEYCODE_RIGHT_STICK && MouseViewMgrUtils.sMouseViewState()) {
            float axisX = padMotionEvent.getX();
            float axisY = padMotionEvent.getY();
            LLog.d("ActivityMgrUtils->sDispatchKeyEvent mouseView axisX:" + axisX + " axisY:" +
                    axisY);
            MouseViewMgrUtils.sShowMouseView(axisX, -axisY);
            return true;
        }

        if ((x >= 0 && y >= 0) || (relateLeftStickKeyCode >= 0 || relateRightStickKeyCode >= 0)) {

            int relateKeyCode = keyCode;
            int offsetX = (int) (padMotionEvent.getX() * r);
            int offsetY = (int) (padMotionEvent.getY() * r);
            LLog.d("ActivityMgrUtils->sDispatchMotionEvent1  offsetX:" + offsetX + " offsetY:" +
                    offsetY);
            final int nLessSide = DisplayMetricsMgr.getLessSide();
            if (mode == KeyMgrUtils.MOTION_MODE_NONE || mode == KeyMgrUtils.MOTION_MODE_NORMAL) {
                //常规摇杆模式 ----------------------------
                if (keyCode == BaseEvent.KEYCODE_LEFT_STICK) {
                    if (relateLeftStickKeyCode >= 0) {  //大于0  表示有与左摇杆的按键
                        x = KeyMgrUtils.sGetKeyInfoXByKeyCode(relateLeftStickKeyCode);
                        y = KeyMgrUtils.sGetKeyInfoYByKeyCode(relateLeftStickKeyCode);
                        relateKeyCode = relateLeftStickKeyCode;
                        offsetX = (int) (padMotionEvent.getX() * radius);
                        offsetY = (int) (padMotionEvent.getY() * radius);
                        offsetLeftStickX = offsetX;
                        offsetLeftStickY = offsetY;
                    }
                } else if (keyCode == BaseEvent.KEYCODE_RIGHT_STICK) {
                    if (relateRightStickKeyCode >= 0) {
                        x = KeyMgrUtils.sGetKeyInfoXByKeyCode(relateRightStickKeyCode);
                        y = KeyMgrUtils.sGetKeyInfoYByKeyCode(relateRightStickKeyCode);
                        relateKeyCode = relateRightStickKeyCode;
                        offsetX = (int) (padMotionEvent.getX() * radius);
                        offsetY = (int) (padMotionEvent.getY() * radius);
                        offsetRightStickX = offsetX;
                        offsetRightStickY = offsetY;
                        LLog.d("ActivityMgrUtils->sDispatchMotionEvent2  offsetX:" + offsetX + " offsetY:" +
                                offsetY);
                    }
                }
                byte keySid = InjectDataMgr.getKeySid(relateKeyCode);

                int newX = x + offsetX;
                int newY = y - offsetY;

                //修正，因为视图的坐标在底层的坐标有区别
                int nTmp = newX;
                newX = nLessSide - newY;
                newY = nTmp;
                LLog.d("ActivityMgrUtils->sDispatchKeyEvent ori:" + DisplayMetricsMgr.getOri() +
                        " nLessSide:" + nLessSide);

                //如果遥杆动了，但没有设置状态，则首先发一个在中心点按下的消息
                switch (keyCode) {
                    case BaseEvent.KEYCODE_LEFT_STICK: {
                        if (!leftStickPress && relateLeftStickKeyCode < 0) {
                            int newStartX = nLessSide - y;
                            int newStartY = x;
                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr.ACTION_PRESS,
                                    newStartX, newStartY);
                            leftStickPress = true;
                            try {
                                Thread.sleep(25);   //延时，防止两个移动点之前时间太近被忽略
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (leftStickPress == true && offsetX == 0 && offsetY == 0) {
                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr.ACTION_UP, newX, newY);
                            leftStickPress = false;
                            return true;
                        }
                        if (leftStickPress || relateLeftStickKeyCode >= 0) {
                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr.ACTION_MOVE, newX,
                                    newY);
                        }
                        break;
                    }
                    case BaseEvent.KEYCODE_RIGHT_STICK: {
                        if (!rightStickPress && relateRightStickKeyCode < 0) {
                            int newStartX = nLessSide - y;
                            int newStartY = x;
                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr.ACTION_PRESS,
                                    newStartX, newStartY);
                            rightStickPress = true;
                            try {
                                Thread.sleep(25);   //延时，防止两个移动点之前时间太近被忽略
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (rightStickPress == true && offsetX == 0 && offsetY == 0) {
                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr.ACTION_UP, newX, newY);
                            rightStickPress = false;
                            return true;
                        }
                        if (rightStickPress || relateRightStickKeyCode >= 0) {
                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr.ACTION_MOVE, newX,
                                    newY);
                        }
                        break;
                    }
                }
            } else if (mode == KeyMgrUtils.MOTION_MODE_FINAL) {   //滑动模式，区域
                // ----------------------------------
                final byte keySid = InjectDataMgr.getKeySid(keyCode);
                final int nStartX = x;
                final int nStartY = y;

                final float scale = (float) step / (float) r;
                //如果遥杆动了，但没有设置状态，则首先发一个在中心点按下的消息
                switch (keyCode) {
                    case BaseEvent.KEYCODE_LEFT_STICK: {
                        leftCurMaxOffsetX = offsetX;
                        leftCurMaxOffsetY = -offsetY;
                        if (!leftStickPress) {
                            leftStickTask = new TimerTask() {
                                @Override
                                public void run() {

                                    //使用定时器模拟滑动模式
                                    if (leftStickRestart) {
                                        leftCurX = nStartX;
                                        leftCurY = nStartY;
                                        int nTmp = leftCurX;
                                        int newX = nLessSide - leftCurY;
                                        int newY = nTmp;
                                        InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr
                                                .ACTION_PRESS, newX, newY);
                                        leftStickRestart = false;
                                    } else {
                                        int newOffsetX = (int) (leftCurMaxOffsetX * scale);
                                        int newOffsetY = (int) (leftCurMaxOffsetY * scale);
                                        leftCurX += newOffsetX;
                                        leftCurY += newOffsetY;
                                        if (newOffsetX < 0 && (leftCurX <= (nStartX +
                                                leftCurMaxOffsetX))) {
                                            leftStickRestart = true;
                                        } else if (newOffsetX > 0 && (leftCurX >= (nStartX +
                                                leftCurMaxOffsetX))) {
                                            leftStickRestart = true;
                                        } else if (newOffsetY < 0 && (leftCurY <= (nStartY +
                                                leftCurMaxOffsetY))) {
                                            leftStickRestart = true;
                                        } else if (newOffsetY > 0 && (leftCurY >= (nStartY +
                                                leftCurMaxOffsetY))) {
                                            leftStickRestart = true;
                                        }

                                        if (leftStickRestart) {
                                            leftCurX = nStartX + leftCurMaxOffsetX;
                                            leftCurY = nStartY + leftCurMaxOffsetY;
                                        }

                                        int nTmp = leftCurX;
                                        int newX = nLessSide - leftCurY;
                                        int newY = nTmp;
                                        if (leftStickRestart) {
                                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr
                                                    .ACTION_UP, newX, newY);
                                            leftCurX = nStartX;
                                            leftCurY = nStartY;
                                        } else {
                                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr
                                                    .ACTION_MOVE, newX, newY);
                                        }
                                    }
                                }
                            };
                            timerLeft.schedule(leftStickTask, 0/*frequency*/, frequency);
                            leftStickPress = true;
                        } else if (leftStickPress == true && offsetX == 0 && offsetY == 0) {
//                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr.ACTION_UP, x, y);
                            if (leftStickTask != null) {
                                leftStickTask.cancel();
                                leftStickTask = null;
                            }
                            leftStickPress = false;
                            int nTmp = leftCurX;
                            int newX = nLessSide - leftCurY;
                            int newY = nTmp;
                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr.ACTION_UP, newX, newY);
                            leftStickRestart = true;
                            return true;
                        }
                        if (leftStickPress) {
                            leftStickRestart = true;
                        }
                        break;
                    }
                    case BaseEvent.KEYCODE_RIGHT_STICK: {
                        rightCurMaxOffsetX = offsetX;
                        rightCurMaxOffsetY = -offsetY;
                        if (!rightStickPress) {
                            rightStickTask = new TimerTask() {
                                @Override
                                public void run() {

                                    //使用定时器模拟滑动模式
                                    if (rightStickRestart) {
                                        rightCurX = nStartX;
                                        rightCurY = nStartY;
                                        int nTmp = rightCurX;
                                        int newX = nLessSide - rightCurY;
                                        int newY = nTmp;
                                        InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr
                                                .ACTION_PRESS, newX, newY);
                                        rightStickRestart = false;
                                    } else {
                                        int newOffsetX = (int) (rightCurMaxOffsetX * scale);
                                        int newOffsetY = (int) (rightCurMaxOffsetY * scale);
                                        rightCurX += newOffsetX;
                                        rightCurY += newOffsetY;
                                        if (newOffsetX < 0 && (rightCurX <= (nStartX +
                                                rightCurMaxOffsetX))) {
                                            rightStickRestart = true;
                                        } else if (newOffsetX > 0 && (rightCurX >= (nStartX +
                                                rightCurMaxOffsetX))) {
                                            rightStickRestart = true;
                                        } else if (newOffsetY < 0 && (rightCurY <= (nStartY +
                                                rightCurMaxOffsetY))) {
                                            rightStickRestart = true;
                                        } else if (newOffsetY > 0 && (rightCurY >= (nStartY +
                                                rightCurMaxOffsetY))) {
                                            rightStickRestart = true;
                                        }

                                        if (rightStickRestart) {
                                            rightCurX = nStartX + rightCurMaxOffsetX;
                                            rightCurY = nStartY + rightCurMaxOffsetY;
                                        }

                                        int nTmp = rightCurX;
                                        int newX = nLessSide - rightCurY;
                                        int newY = nTmp;
                                        if (rightStickRestart) {
                                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr
                                                    .ACTION_UP, newX, newY);
                                            rightCurX = nStartX;
                                            rightCurY = nStartY;
                                        } else {
                                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr
                                                    .ACTION_MOVE, newX, newY);
                                        }
                                    }
                                }
                            };
                            timerRight.schedule(rightStickTask, 0/*frequency*/, frequency);
                            rightStickPress = true;
                        } else if (rightStickPress == true && offsetX == 0 && offsetY == 0) {
                            if (rightStickTask != null) {
                                rightStickTask.cancel();
                                rightStickTask = null;
                            }
                            rightStickPress = false;
                            int nTmp = rightCurX;
                            int newX = nLessSide - rightCurY;
                            int newY = nTmp;
                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr.ACTION_UP, newX, newY);
                            ;
                            rightStickRestart = true;
                            return true;
                        }
                        if (rightStickPress) {
//                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr.ACTION_MOVE, x, y);
                            rightStickRestart = true;
                        }
                        break;
                    }
                }
            } else if (mode == KeyMgrUtils.MOTION_MODE_DYNAMIC) {   //滑动模式，全屏
                // ----------------------------------------
                final byte keySid = InjectDataMgr.getKeySid(keyCode);
                final int nStartX = x;
                final int nStartY = y;

                final float scale = (float) step / (float) r;
                switch (keyCode) {
                    case BaseEvent.KEYCODE_LEFT_STICK: {
                        leftCurMaxOffsetX = offsetX;
                        leftCurMaxOffsetY = -offsetY;
                        if (!leftStickPress) {
                            leftStickTask = new TimerTask() {
                                @Override
                                public void run() {

                                    //使用定时器模拟滑动模式
                                    if (leftStickRestart) {
                                        leftCurX = nStartX;
                                        leftCurY = nStartY;
                                        int nTmp = leftCurX;
                                        int newX = nLessSide - leftCurY;
                                        int newY = nTmp;
                                        InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr
                                                .ACTION_PRESS, newX, newY);
                                        leftStickRestart = false;
                                    } else {
                                        int newOffsetX = (int) (leftCurMaxOffsetX * scale);
                                        int newOffsetY = (int) (leftCurMaxOffsetY * scale);
                                        leftCurX += newOffsetX;
                                        leftCurY += newOffsetY;

                                        //不允许超出屏幕
                                        if (leftCurX < 0) {
                                            leftCurX = 0;
                                        }
                                        if (leftCurY < 0) {
                                            leftCurY = 0;
                                        }
                                        if (leftCurX > DisplayMetricsMgr.getDisWidthMax()) {
                                            leftCurX = DisplayMetricsMgr.getDisWidthMax();
                                        }
                                        if (leftCurY > DisplayMetricsMgr.getDisHeightMin()) {
                                            leftCurY = DisplayMetricsMgr.getDisHeightMin();
                                        }

                                        int nTmp = leftCurX;
                                        int newX = nLessSide - leftCurY;
                                        int newY = nTmp;

                                        if (newX >= DisplayMetricsMgr.getDisHeightMin()) {
                                            newX = DisplayMetricsMgr.getDisHeightMin() - 1;
                                        }
                                        if (newY >= DisplayMetricsMgr.getDisWidthMax()) {
                                            newY = DisplayMetricsMgr.getDisWidthMax() - 1;
                                        }

                                        InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr
                                                .ACTION_MOVE, newX, newY);
                                    }
                                }
                            };
                            timerLeft.schedule(leftStickTask, 0/*frequency*/, frequency);
                            leftStickPress = true;
                        } else if (leftStickPress == true && offsetX == 0 && offsetY == 0) {
                            if (leftStickTask != null) {
                                leftStickTask.cancel();
                                leftStickTask = null;
                            }
                            leftStickPress = false;
                            int nTmp = leftCurX;
                            int newX = nLessSide - leftCurY;
                            int newY = nTmp;
                            leftStickRestart = true;
                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr.ACTION_UP, newX, newY);
                            return true;
                        }
//                        if(leftStickPress) {
//                            leftStickRestart = true;
//                        }
                        break;
                    }
                    case BaseEvent.KEYCODE_RIGHT_STICK: {
                        rightCurMaxOffsetX = offsetX;
                        rightCurMaxOffsetY = -offsetY;
                        if (!rightStickPress) {
                            rightStickTask = new TimerTask() {
                                @Override
                                public void run() {

                                    //使用定时器模拟滑动模式
                                    if (rightStickRestart) {
                                        rightCurX = nStartX;
                                        rightCurY = nStartY;
                                        int nTmp = rightCurX;
                                        int newX = nLessSide - rightCurY;
                                        int newY = nTmp;
                                        InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr
                                                .ACTION_PRESS, newX, newY);
                                        rightStickRestart = false;
                                    } else {
                                        int newOffsetX = (int) (rightCurMaxOffsetX * scale);
                                        int newOffsetY = (int) (rightCurMaxOffsetY * scale);
                                        rightCurX += newOffsetX;
                                        rightCurY += newOffsetY;

                                        //不允许超出屏幕
                                        if (rightCurX < 0) {
                                            rightCurX = 0;
                                        }
                                        if (rightCurY < 0) {
                                            rightCurY = 0;
                                        }
                                        if (rightCurX > DisplayMetricsMgr.getDisWidthMax()) {
                                            rightCurX = DisplayMetricsMgr.getDisWidthMax();
                                        }
                                        if (rightCurY > DisplayMetricsMgr.getDisHeightMin()) {
                                            rightCurY = DisplayMetricsMgr.getDisHeightMin();
                                        }

                                        int nTmp = rightCurX;
                                        int newX = nLessSide - rightCurY;
                                        int newY = nTmp;

                                        if (newX >= DisplayMetricsMgr.getDisHeightMin()) {
                                            newX = DisplayMetricsMgr.getDisHeightMin() - 1;
                                        }
                                        if (newY >= DisplayMetricsMgr.getDisWidthMax()) {
                                            newY = DisplayMetricsMgr.getDisWidthMax() - 1;
                                        }

                                        InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr
                                                .ACTION_MOVE, newX, newY);
                                    }
                                }
                            };
                            timerRight.schedule(rightStickTask, 0/*frequency*/, frequency);
                            rightStickPress = true;
                        } else if (rightStickPress == true && offsetX == 0 && offsetY == 0) {
                            if (rightStickTask != null) {
                                rightStickTask.cancel();
                                rightStickTask = null;
                            }
                            rightStickPress = false;
                            int nTmp = rightCurX;
                            int newX = nLessSide - rightCurY;
                            int newY = nTmp;
                            rightStickRestart = true;
                            InjectDataMgr.sendKeyInfo(keySid, InjectDataMgr.ACTION_UP, newX, newY);
                            return true;
                        }
//                        if(rightStickPress) {
//                            rightStickRestart = true;
//                        }
                        break;
                    }
                }
            }
        }
        return true;
    }

    public static boolean sDispatchStateEvent(PadStateEvent event) {
        LLog.d("ActivityMgrUtils->手柄状态: state:" + event.getState());
        DeviceManager.getInstance(PadContext.getContextObj()).onStateEvent(event);
        return true;
    }


}
