package com.ngds.pad.server;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.ParcelUuid;
import android.os.Parcelable;

import com.lx.pad.util.LLog;

/**
 * Created by Administrator on 2017/11/28.
 */

public class BluetoothReceiver extends BroadcastReceiver {

    BluetoothReceiver(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String logStr = "";
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
        logStr = "手柄 " + device.getName() + " " + device.getAddress();
        Intent intentService = new Intent(context, PadService.class);
        intentService.putExtra("param_mac", device.getAddress());
        if("android.bluetooth.device.action.FOUND".equals(action)){
            logStr = logStr + " 发现";
        }else if("android.bluetooth.device.action.ACL_CONNECTED".equals(action)){
            logStr = logStr + " 己连接";
            intentService.setAction("com.ngds.pad.server.PadService.Connect");
            context.startService(intentService);
        }else if("android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED".equals(action)){
            logStr = logStr + " 请求断开连接";
        }else if("android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)){
            logStr = logStr + " 己断开";
            intentService.setAction("com.ngds.pad.server.PadService.Disconnect");
            context.startService(intentService);
        }else if("android.bluetooth.device.action.UUID".equals(action)){
            logStr = logStr + " 收到UUID";
            boolean isBle = false;
            Parcelable[] uuids = intent.getParcelableArrayExtra("android.bluetooth.device.extra.UUID");
            if(uuids != null && uuids.length > 0){
                isBle = true;
                int nLen = uuids.length;
                LLog.d("uuid Len:" + nLen + " self UUID:" + Device.UUID_SPP);
                for(int i = 0; i < nLen; i++){
                    LLog.d("[" + i + "] uuid:" + ((ParcelUuid)uuids[i]).getUuid());
                    if(Device.UUID_SPP.compareTo(((ParcelUuid)uuids[i]).getUuid()) == 0){
                        isBle = false;
                        LLog.d("SSP find ble set false");
                    }else{
                        i++;
                        continue;
                    }
                    break;
                }
            }

            if(isBle){
                intentService.setAction("com.ngds.pad.server.PadService.Connect.BLE");
            }else{
                intentService.setAction("com.ngds.pad.server.PadService.Connect.Normal");
            }

            context.startService(intentService);
        }

        LLog.d(logStr);
    }
}
