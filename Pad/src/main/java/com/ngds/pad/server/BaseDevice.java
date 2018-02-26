package com.ngds.pad.server;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.lx.pad.util.LLog;
import com.ngds.pad.PadInfo;

import java.util.Timer;

/**
 * Created by Administrator on 2017/11/28.
 */

public class BaseDevice extends PadInfo {
    public static final String PARAM_MAC = "param_mac";
    public static final float ACC_UNIT = 0.000599f;
    public static final float GYR_UNIT = 0.001065f;
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_NONE = 0;
    protected Context m_context = null;
    protected Timer m_timer = new Timer();
    protected boolean m_queueInit = false;
    protected BluetoothAdapter m_bleAdapter = null;
    protected int m_state = 0;

    public BaseDevice(Context context, String mac){
        super();
        m_context = context;
        m_state = 0;
        m_mac = mac;
    }

    public synchronized int getState(){
        return m_state;
    }

    public synchronized void setState(int state){
        LLog.d("BaseDevice->setState " + m_mac + " " + m_state + " -> " + state);
        m_state = state;
    }

    public synchronized void disConnect(){
        LLog.d("BaseDevice->disConnect " + m_mac + " disConnect");
        setState(STATE_NONE);
        m_queueInit = false;
    }

    public boolean sendPkg(int type, byte[] data){
        LLog.d("BaseDevice->sendCmdPkg <<< if this function reached, maybe error!");
        return false;
    }

    protected  boolean sendData(byte[] data){
        LLog.d("BaseDevice->sendData <<< if this function reached, maybe error!");
        return false;
    }

    public boolean sendNullPkg(int type){
        LLog.d("BaseDevice->sendNullPkg <<< if this function reached, maybe error!");
        return sendPkg(type, null);
    }
}
