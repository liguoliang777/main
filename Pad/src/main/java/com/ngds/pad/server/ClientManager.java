package com.ngds.pad.server;

import com.inuker.bluetooth.library.BluetoothClient;
import com.lx.pad.PadContext;

/**
 * Created by Administrator on 2018/4/16.
 */

public class ClientManager {

    private static BluetoothClient mClient;

    public static BluetoothClient getClient() {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(PadContext.getContextObj());
                }
            }
        }
        return mClient;
    }
}