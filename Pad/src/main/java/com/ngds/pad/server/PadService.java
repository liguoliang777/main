package com.ngds.pad.server;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.lx.pad.util.LLog;
import com.ngame.Utils.KeyMgrUtils;
import com.ngds.pad.IPadCommand;
import com.ngds.pad.Msg.LooperEventManager;
import com.ngds.pad.PadInfo;

/**
 * Created by Administrator on 2017/11/27.
 */

/**
 * 核心服务，所有非UI的操作都是经由此服务来操作的
 */

public class PadService extends Service {
    private BluetoothReceiver m_bleReceiver;
    private final IPadCommand.Stub m_IPadCommandStub = new IPadCommand.Stub() {
        @Override
        public void registerCallback(String packageName) throws RemoteException {
            LLog.d("PadService->IPadCommandStub->registerCallback");
            DeviceManager.getInstance(PadService.this).registerCallback(packageName);
        }

        @Override
        public void unRegisterCallback(String packageName) throws RemoteException {
            LLog.d("PadService->IPadCommandStub->unRegisterCallback");
            DeviceManager.getInstance(PadService.this).unRegisterCallback(packageName);
        }

        @Override
        public int execCommand(int type, String mac, byte[] data) throws RemoteException {
            LLog.d("PadService->IPadCommandStub->execCommand");
            return 0;
        }

        @Override
        public PadInfo[] getPadList() throws RemoteException {
            LLog.d("PadService->IPadCommandStub->getPadList");
            return new PadInfo[0];
        }

        @Override
        public boolean isKeysDown(int controllerID, int[] keyArray) throws RemoteException {
            LLog.d("PadService->IPadCommandStub->isKeysDown");
            return false;
        }

        @Override
        public boolean setName(String mac, byte[] data) throws RemoteException {
            LLog.d("PadService->IPadCommandStub->setName");
            return false;
        }

        @Override
        public boolean ota(String mac, byte[] data) throws RemoteException {
            LLog.d("PadService->IPadCommandStub->ota");
            return false;
        }

        @Override
        public boolean setMotor(int controllerID, int left, int right) throws RemoteException {
            LLog.d("PadService->IPadCommandStub->setMotor");
            return false;
        }

        @Override
        public boolean setVibrate(int controllerID, float left, float right) throws
                RemoteException {
            LLog.d("PadService->IPadCommandStub->setVibrate");
            return false;
        }

        @Override
        public boolean setMode(String mac, byte mode) throws RemoteException {
            LLog.d("PadService->IPadCommandStub->setMode");
            return false;
        }

        @Override
        public boolean setLight(int controllerID, byte r, byte g, byte b) throws RemoteException {
            LLog.d("PadService->IPadCommandStub->setLight");
            return false;
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LLog.d("PadService->onBind");
        return m_IPadCommandStub;
    }

    @Override
    public void onCreate() {
        if (m_bleReceiver == null) {
            m_bleReceiver = new BluetoothReceiver();
        }
        LLog.d("PadService->onCreate");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.device.action.FOUND");
        intentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        intentFilter.addAction("android.bluetooth.device.action.UUID");
        registerReceiver(m_bleReceiver, intentFilter);

        //初始化按键映射消息队列
        LooperEventManager.init();

        KeyMgrUtils.sUpdateKeyEnumHashMap(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        LLog.d("PadService->onDestory");
        unregisterReceiver(m_bleReceiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        LLog.d("PadService->onStartCommand");

        if (intent != null) {
            String action = intent.getAction();
            if (action != null && !action.isEmpty()) {
                String mac = intent.getStringExtra("param_mac");
                if (action.equals("com.ngds.pad.server.PadService.Connect")) {
                    LLog.d("PadService->onStartCommand 已连接:" + action);
                    DeviceManager.getInstance(this).connect(mac, true);
                } else if (action.equals("com.ngds.pad.server.PadService.Disconnect")) {
                    LLog.d("PadService->onStartCommand 连接断开:" + action);
                    DeviceManager.getInstance(this).disConnect(mac);
                } else if (action.equals("com.ngds.pad.server.PadService.Connect.Normal")) {
                    LLog.d("PadService->onStartCommand 连接Normal：" + action);
                    DeviceManager.getInstance(this).connectNormal(mac);
                } else if (action.equals("com.ngds.pad.server.PadService.Connect.BLE")) {
                    LLog.d("PadService->onStartCommand action:连接..BLE:" + action);
                    DeviceManager.getInstance(this).connectBle(mac);
                } else if (action.equals("com.ngds.pad.server.PadService.SppOff")) {
                    LLog.d("PadService->onStartCommand 连接Spp...OFF" + action);
                    DeviceManager.getInstance(this).execCommand(0, null, null);
                } else if (action.equals("com.ngds.pad.server.action.USB_DEVICE_CONNECTED")) {
                    LLog.d("PadService->onStartCommand action:" + action);
                    //USB的连接方式，暂时不处理
                } else if (action.equals("com.ngds.pad.server.action.USB_DEVICE_DISCONNECTED")) {
                    LLog.d("PadService->onStartCommand action:" + action);
                    //USB的连接方式，暂时不处理
                } else if (action.equals("com.ngds.pad.server.PadService.Home")) {
                    LLog.d("PadService->onStartCommand action:" + action);
                    DeviceManager.getInstance(this).execCommand(0, null, null);
                } else {
                    LLog.d("PadService->onStartCommand else action:" + action);
                }
            }
        }

        return Service.START_REDELIVER_INTENT;
    }
}
