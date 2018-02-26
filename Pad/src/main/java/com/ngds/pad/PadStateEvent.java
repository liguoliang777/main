package com.ngds.pad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/11/29.
 */

public class PadStateEvent extends BaseEvent implements Parcelable {
    public static final Creator<PadStateEvent> CREATOR = new Creator<PadStateEvent>(){

        @Override
        public PadStateEvent createFromParcel(Parcel source) {
            return new PadStateEvent(source);
        }

        @Override
        public PadStateEvent[] newArray(int size) {
            return new PadStateEvent[size];
        }
    };

    final int m_state;
    final int m_action;
    protected String m_mac;

    public PadStateEvent(long eventTime, int deviceId, int state, int action){
        super(eventTime, deviceId);
        m_mac = "";
        m_state = state;
        m_action = action;
    }

    PadStateEvent(Parcel parcel){
        super(parcel);
        m_mac = "";
        m_state = parcel.readInt();
        m_action = parcel.readInt();
        m_mac = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(m_state);
        dest.writeInt(m_action);
        dest.writeString(m_mac);
    }

    public final int getAction(){
        return m_action;
    }

    public String getMac(){
        return m_mac;
    }

    public final int getState(){
        return m_state;
    }

    public void setMac(String mac){
        m_mac = mac;
    }
}
