package com.ngame.DabaBase;

import com.ngds.pad.BaseEvent;

/**
 * Created by Administrator on 2017/12/2.
 */

public enum KeyEnum {
    Q("q"),
    A("a"),
    B("b"),
    X("x"),
    Y("y"),
    L1("l1"),
    R1("r1"),
    L2("l2"),
    R2("r2"),
    L("l"),
    R("r"),
    F("f"),
    UP("up"),
    DOWN("down"),
    LEFT("left"),
    RIGHT("right"),
    THUMBL("thumbl"),
    THUMBR("thumbr"),
    START("start"),
    BACK("back"),
    MOTION("motion");

    public String keyName;

    private KeyEnum(String keyName){
        this.keyName = keyName;
    }

    public final String getPref_r(){return "pref_" + keyName + "_r";}
    public final String getPref_x(){return "pref_" + keyName + "_x";}
    public final String getPref_y(){return "pref_" + keyName + "_y";}
    public final String getPref_m(){return "pref_" + keyName + "_m";}
    public final String getPref_step(){return "pref_" + keyName + "_step";}
    public final String getPref_frequency(){return "pref_" + keyName + "_frequency";}
    public final String getPref_Flags(){return "pref_" + keyName + "_flags";}

    public static KeyEnum getCode(int keyCode){
        switch(keyCode){
            case BaseEvent.KEYCODE_BACK:
                return BACK;
            case BaseEvent.KEYCODE_DPAD_UP:
                return UP;
            case BaseEvent.KEYCODE_DPAD_DOWN:
                return DOWN;
            case BaseEvent.KEYCODE_DPAD_LEFT:
                return LEFT;
            case BaseEvent.KEYCODE_DPAD_RIGHT:
                return RIGHT;
            case BaseEvent.KEYCODE_BUTTON_A:
                return A;
            case BaseEvent.KEYCODE_BUTTON_B:
                return B;
            case BaseEvent.KEYCODE_BUTTON_X:
                return X;
            case BaseEvent.KEYCODE_BUTTON_Y:
                return Y;
            case BaseEvent.KEYCODE_BUTTON_L1:
                return L1;
            case BaseEvent.KEYCODE_BUTTON_R1:
                return R1;
            case BaseEvent.KEYCODE_BUTTON_L2:
                return L2;
            case BaseEvent.KEYCODE_BUTTON_R2:
                return R2;
            case BaseEvent.KEYCODE_BUTTON_THUMBL:
                return THUMBL;
            case BaseEvent.KEYCODE_BUTTON_THUMBR:
                return THUMBR;
            case BaseEvent.KEYCODE_BUTTON_START:
                return START;
            case BaseEvent.KEYCODE_LEFT_STICK:
                return L;
            case BaseEvent.KEYCODE_RIGHT_STICK:
                return R;
            default:
                return null;
        }
    }
}
