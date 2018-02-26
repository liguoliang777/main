package com.ngds.pad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/11/29.
 */

public class PadMotionEvent extends BaseEvent implements Parcelable {
    public static final Creator<PadMotionEvent> CREATOR = new Creator<PadMotionEvent>(){

        @Override
        public PadMotionEvent createFromParcel(Parcel source) {
            return new PadMotionEvent(source);
        }

        @Override
        public PadMotionEvent[] newArray(int size) {
            return new PadMotionEvent[size];
        }
    };

    final int m_keyCode;
    final float m_x;
    final float m_y;

    public PadMotionEvent(long eventTime, int deviceId, int keyCode, float x, float y){
        super(eventTime, deviceId);
        m_keyCode = keyCode;
        m_x = x;
        m_y = y;
    }

    PadMotionEvent(Parcel parcel){
        super(parcel);
        m_keyCode = parcel.readInt();
        m_x = parcel.readFloat();
        m_y = parcel.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(m_keyCode);
        dest.writeFloat(m_x);
        dest.writeFloat(m_y);
    }

    public final int getKeyCode(){
        return m_keyCode;
    }

    public final float getX(){
        return m_x;
    }

    public final float getY(){
        return m_y;
    }
}
