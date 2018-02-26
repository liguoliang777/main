package com.ngds.pad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/11/29.
 */

public class PadKeyEvent extends BaseEvent implements Parcelable {
    public static final Creator<PadKeyEvent> CREATOR = new Creator<PadKeyEvent>(){

        @Override
        public PadKeyEvent createFromParcel(Parcel source) {
            return new PadKeyEvent(source);
        }

        @Override
        public PadKeyEvent[] newArray(int size) {
            return new PadKeyEvent[size];
        }
    };

    final int m_keyCode;
    final int m_action;
    final float m_pressure;

    public PadKeyEvent(long eventTime, int deviceId, int keyCode, int action, float pressure) {
        super(eventTime, deviceId);
        m_keyCode = keyCode;
        m_action = action;
        m_pressure = pressure;
    }

    PadKeyEvent(Parcel parcel){
        super(parcel);
        m_keyCode = parcel.readInt();
        m_action = parcel.readInt();
        m_pressure = parcel.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(m_keyCode);
        dest.writeInt(m_action);
        dest.writeFloat(m_pressure);
    }

    public final int getKeyCode(){
        return m_keyCode;
    }

    public final int getAction(){
        return m_action;
    }

    public final float getPressure(){
        return m_pressure;
    }
}
