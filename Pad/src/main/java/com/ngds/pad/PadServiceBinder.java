package com.ngds.pad;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.lx.pad.util.LLog;
import com.ngds.pad.server.DeviceManager;
import com.ngds.pad.server.PadService;

/**
 * Created by Administrator on 2017/11/29.
 */

public class PadServiceBinder {
    static PadServiceBinder padServiceBinder = null;
    private Context context;
    private ServiceConnection serviceConnection;
    private String pkgName = null;
    private IPadCommand m_IPadCommand = null;

    private PadServiceBinder(final Context context){
        super();
        this.context = context.getApplicationContext();

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                m_IPadCommand = IPadCommand.Stub.asInterface(service);
                LLog.d("PadServiceBinder->onServiceConnected");
//                DeviceManager.getInstance(PadServiceBinder.this.context).registerCallback(" ");
                if(m_IPadCommand != null){
                    try {
                        m_IPadCommand.registerCallback(context.getPackageName());
                    }catch(Exception e){
                        LLog.d("PadServiceBinder->onServiceConnected registerCallback fail: " + e.toString());
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                m_IPadCommand = null;
            }
        };
    }

    public static synchronized PadServiceBinder getInstance(Context context){
        Class clz = PadServiceBinder.class;
        synchronized (clz){
            if(padServiceBinder == null){
                padServiceBinder = new PadServiceBinder(context);
            }
        }
        return padServiceBinder;
    }

    private void startAndBindService(){
        Intent intent = new Intent(context, PadService.class);
        ComponentName name = context.startService(intent);
        boolean bResult = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        if(name == null){
            LLog.d("PadServiceBinder->startAndBindService startService error!");
        }
        LLog.d("PadServiceBinder->startAndBindService name:" + name.getClassName() + " bindResult:" + bResult);
    }

    public void unRegisterAndUnbindService(){
        if(serviceConnection != null && m_IPadCommand != null) {
            context.unbindService(serviceConnection);
            serviceConnection = null;
        }
        Intent intent = new Intent(context, PadService.class);
        context.stopService(intent);
    }

    //可以获取当前连接上了的设备列表
    public PadInfo[] getPadCommandList(){
        try{
            return DeviceManager.getInstance(context).getPadList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void initArgs(String pkgName){
        startAndBindService();
        this.pkgName = pkgName;
    }

    public PadInfo[] getPadList(){
        PadInfo[] result = null;
        try{
            if(m_IPadCommand != null){
                result = m_IPadCommand.getPadList();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean isKeysDown(int controllerID, int[] keyArray){
        try{
            if(m_IPadCommand != null){
                return m_IPadCommand.isKeysDown(controllerID, keyArray);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean ota(String mac, byte[] data){
        return false;
    }

    public boolean setVibrate(int controllerID, float left, float right){
        boolean result = false;
        try{
            if(m_IPadCommand != null){
                result = m_IPadCommand.setVibrate(controllerID, left, right);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
