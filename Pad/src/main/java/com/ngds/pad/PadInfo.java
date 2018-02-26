package com.ngds.pad;

import android.os.Parcel;
import android.os.Parcelable;

import com.lx.pad.util.LLog;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/11/28.
 */

public class PadInfo implements Parcelable {
    public static Creator<PadInfo> CREATOR = new Creator<PadInfo>(){
        @Override
        public PadInfo createFromParcel(Parcel source) {
            return new PadInfo(source);
        }

        @Override
        public PadInfo[] newArray(int size) {
            return new PadInfo[size];
        }
    };

    protected String m_name = "";
    protected String m_mac = "";
    protected int m_controllerID = -1;
    protected String m_UUID = "";
    protected HashMap<Integer, Boolean> m_keyStateMap = new HashMap<Integer, Boolean>();

    protected PadInfo(){
        super();
    }

    PadInfo(Parcel parcel){
        m_name = parcel.readString();
        m_mac = parcel.readString();
        m_UUID = parcel.readString();
        m_keyStateMap = parcel.readHashMap(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_name);
        dest.writeString(m_mac);
        dest.writeString(m_UUID);
        dest.writeMap(m_keyStateMap);
    }

    public String getName(){
        LLog.d("PadInfo->getName name:" + m_name);
        return m_name;
    }

    public String getMac(){
        return m_mac;
    }

    public void setName(String name){
        m_name = name;
    }

    public int getControllerID(){
        return m_controllerID;
    }

    public void setControllerID(int nControllerID){
        m_controllerID = nControllerID;
    }

    public boolean isKeyDown(int[] keyArray){
        boolean result = false;
        int len = keyArray.length;
        for(int i = 0; i < len; i++){
            int key = keyArray[i];
            if(!m_keyStateMap.containsKey(Integer.valueOf(key))
                    || !m_keyStateMap.get(Integer.valueOf(key)).booleanValue()){
                result = true;
                break;
            }
        }
        return result;
    }
}
