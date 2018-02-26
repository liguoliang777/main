package com.ngds.pad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/11/29.
 */

public class BaseEvent implements Parcelable {
    public static final Creator<BaseEvent> CREATOR = new Creator<BaseEvent>() {
        @Override
        public BaseEvent createFromParcel(Parcel in) {
            return new BaseEvent(in);
        }

        @Override
        public BaseEvent[] newArray(int size) {
            return new BaseEvent[size];
        }
    };

    public static final int ACTION_DOWN = 0;
    public static final int ACTION_PRESSURE = -1;
    public static final int ACTION_UP = 1;
    public static final int KEYCODE_BACK = 4;
    public static final int KEYCODE_BUTTON_A = 96;
    public static final int KEYCODE_BUTTON_B = 97;
    public static final int KEYCODE_BUTTON_HELP = 198;
    public static final int KEYCODE_BUTTON_KEYBOARD = 199;
    public static final int KEYCODE_BUTTON_L1 = 102;
    public static final int KEYCODE_BUTTON_L2 = 104;
    public static final int KEYCODE_BUTTON_R1 = 103;
    public static final int KEYCODE_BUTTON_R2 = 105;
    public static final int KEYCODE_BUTTON_START = 108;
    public static final int KEYCODE_BUTTON_THUMBL = 106;
    public static final int KEYCODE_BUTTON_THUMBR = 107;
    public static final int KEYCODE_BUTTON_X = 99;
    public static final int KEYCODE_BUTTON_Y = 100;
    public static final int KEYCODE_DPAD_DOWN = 20;
    public static final int KEYCODE_DPAD_LEFT = 21;
    public static final int KEYCODE_DPAD_RIGHT = 22;
    public static final int KEYCODE_DPAD_UP = 19;
    public static final int KEYCODE_LEFT_STICK = 200;
    public static final int KEYCODE_RIGHT_STICK = 201;
    public static final int ACTION_CONNECTED = 1;   //PadStateEvent
    public static final int ACTION_CONNECTING = 2;
    public static final int ACTION_DISCONNECTED = 0;

    public static final int STATE_CONNECTED = 1;
    public static final int STATE_CONNECTED_COUNTS = -1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECT_REJECT = 8;
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_UPGRADE_PROCESS = 9;

    final long m_deviceTime;
    final int m_deviceId;

    public BaseEvent(long eventTime, int deviceId){
        super();
        m_deviceTime = eventTime;
        m_deviceId = deviceId;
    }

    protected BaseEvent(Parcel in) {
        super();
        m_deviceTime = in.readLong();
        m_deviceId = in.readInt();
    }

    public final long getEventTime(){
        return m_deviceTime;
    }

    public final int getControllerId(){
        return m_deviceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(m_deviceTime);
        dest.writeInt(m_deviceId);
    }
}
