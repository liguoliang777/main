package com.ngds.pad.server;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.InputDevice;
import android.widget.Toast;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.lx.pad.util.LLog;
import com.ngame.cj007.Cj007EditActivity;
import com.ngds.pad.BaseEvent;
import com.ngds.pad.Msg.LooperEventManager;
import com.ngds.pad.PadInfo;
import com.ngds.pad.PadStateEvent;
import com.ngds.pad.utils.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import static android.view.InputDevice.SOURCE_GAMEPAD;
import static com.inuker.bluetooth.library.Code.REQUEST_FAILED;
import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Code.REQUEST_TIMEDOUT;

/**
 * Created by Administrator on 2017/11/28.
 */

public class DeviceManager {
    private static DeviceManager m_deviceManager = null;
    private Context mContext = null;
    private ConcurrentHashMap<String, BaseDevice> m_baseDeviceMap = new ConcurrentHashMap<String,
            BaseDevice>();
    private static final BluetoothAdapter m_bleAdapter = BluetoothAdapter.getDefaultAdapter();
    private static boolean m_bSleep = false;
    private Timer m_timer = new Timer();
    private String gamePkgName;
    private BluetoothClient mClient;
    private BluetoothAdapter mBluetoothAdapter;

    private DeviceManager(Context context) {
        super();
        mContext = context;
        initDeviceConnect();
    }

    public static DeviceManager getInstance(Context ctx) {
        synchronized (DeviceManager.class) {
            if (m_deviceManager == null) {
                m_deviceManager = new DeviceManager(ctx);
            }
        }
        return m_deviceManager;
    }

    private void initDeviceConnect() {
        LLog.d("DeviceManager->initDeviceConnect");
        HashMap<String, BluetoothDevice> devices = getBondedPad();
        int i = 0;
        Iterator<BluetoothDevice> iterBleDevice = devices.values().iterator();
        while (iterBleDevice.hasNext()) {
            final BluetoothDevice bluetoothDevice = iterBleDevice.next();
            i++;
            LLog.d("DeviceManager->initDeviceConnect bleAddr:" + bluetoothDevice.getAddress() + "" +
                    " i:" + i);
            m_timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    connect(bluetoothDevice.getAddress(), false);
                }
            }, (long) (i * 2000));
        }
    }

    private String getApplicationInfo(String pkgName) {
        String result = null;
        try {
            result = (String) mContext.getPackageManager().getApplicationLabel(mContext
                    .getPackageManager().getApplicationInfo(pkgName, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addPadStateEvent(String mac, int nControllerId, int state, int action) {
        //LooperEventManager.sendEventMsg(LooperEventManager.MSG_PAD_MOTIONEVENT, new
        // PadMotionEvent(m_time, m_deviceId, keyCode, stickPercent(axis_x, false), stickPercent
        // (axis_y, true)));
        PadStateEvent padStateEvent = new PadStateEvent(System.currentTimeMillis(),
                nControllerId, state, action);
        padStateEvent.setMac(mac);
        LooperEventManager.sendEventMsg(LooperEventManager.MSG_PAD_STATEEVENT, padStateEvent);
    }

    public synchronized void connectBle(final String mac) {
        LLog.d("DeviceManager->connectBle mac:" + mac);
        try {
            BaseDevice device = m_baseDeviceMap.get(mac);
            if (device == null || (device instanceof Device)) {
                if (device instanceof Device) {
                    m_baseDeviceMap.remove(mac);
                }
                m_baseDeviceMap.put(mac, new DeviceBle(mContext, mac));
            } else if (device.getState() != BaseDevice.STATE_CONNECTING) {
                if (device.getState() == BaseDevice.STATE_CONNECTED) {
                    LLog.d("DeviceManager->connectBle state connected return!");
                    return;
                }
            }

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    DeviceBle dev = (DeviceBle) m_baseDeviceMap.get(mac);
                    if (dev != null) {
                        dev.resetBleGatt();
                    }
                }
            }, 100);
            LLog.d("DeviceManager->connectBle run over! mapsize:" + m_baseDeviceMap.size());
        } catch (Throwable e) {
            LLog.d("DeviceManager->connectBle throwable exception!");
            throw e;
        }
    }

    public synchronized void connectNormal(final String mac) {
        LLog.d("DeviceManager->connectNormal mac:" + mac);
        try {
            BaseDevice device = m_baseDeviceMap.get(mac);
            if (device == null || (device instanceof DeviceBle)) {
                if (device instanceof DeviceBle) {
                    m_baseDeviceMap.remove(mac);
                }
                m_baseDeviceMap.put(mac, new Device(mContext, mac));
            } else if (device.getState() != BaseDevice.STATE_CONNECTING) {
                if (device.getState() == BaseDevice.STATE_CONNECTED) {
                    return;
                }
            }

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    int count = getConnectedDeviceCount();
                    if (m_bSleep) {
                        for (int i = 0; i < 10; i++) {
                            if (count < sGetGamePadCount()) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                                LLog.d("DeviceManager->connectNormal sleep:" + (i + 1) * 1000 + "" +
                                        " " + count + " -> " + getConnectedDeviceCount());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    Device dev = (Device) m_baseDeviceMap.get(mac);
                    if (dev != null) {
                        dev.restartSecureSocketThread();
                    }
                }
            }, 500);
        } catch (Throwable e) {
            throw e;
        }
    }

    private HashMap<String, BluetoothDevice> getBondedPad() {
        LLog.d("DeviceManager->getBondedPad");
        HashMap<String, BluetoothDevice> devices = new HashMap<String, BluetoothDevice>();
        if (m_bleAdapter != null && m_bleAdapter.isEnabled()) {
            Iterator<BluetoothDevice> iter = m_bleAdapter.getBondedDevices().iterator();
            while (iter.hasNext()) {
                BluetoothDevice dev = iter.next();
                devices.put(dev.getAddress(), dev);
            }
        }
        return devices;
    }

    private void switchDevice(String mac) {
        LLog.d("DeviceManager->switchDevice mac:" + mac);
        if (Build.VERSION.SDK_INT < 18 || !this.mContext.getPackageManager().hasSystemFeature
                ("android.hardware.bluetooth_le")) {
            connectNormal(mac);
        } else if (!m_bleAdapter.getRemoteDevice(mac).fetchUuidsWithSdp()) {
            LLog.d("DeviceManager->switchDevice cannot get uuid");
        }
    }

    public void connect(String mac, boolean isFromBroadcast) {
        if (mac == null || mac.isEmpty()) {
            LLog.d("DeviceManager->connect mac empty");
            return;
        }

        if (m_bleAdapter != null && m_bleAdapter.isEnabled()) {
            m_bSleep = isFromBroadcast;
            if (isFromBroadcast) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            switchDevice(mac);
        }
    }

    public void disConnect() {
        m_timer.cancel();
        execCommand(0, null, null);
        Iterator<BaseDevice> iter = m_baseDeviceMap.values().iterator();
        try {
            while (iter.hasNext()) {
                BaseDevice device = iter.next();
                device.disConnect();
                addPadStateEvent(device.getMac(), device.getControllerID(), BaseEvent
                        .STATE_DISCONNECTED, BaseEvent.ACTION_DISCONNECTED);
                LLog.d("DeviceManager->disConnect function change state is not realize");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        m_baseDeviceMap.clear();
        m_deviceManager = null;
    }

    public void disConnect(String mac) {
        BaseDevice dev = m_baseDeviceMap.get(mac);
        if (dev != null) {
            addPadStateEvent(dev.getMac(), dev.getControllerID(), BaseEvent.STATE_DISCONNECTED,
                    BaseEvent.ACTION_DISCONNECTED);
            dev.disConnect();
            LLog.d("DeviceManager->disConnect change state not realize! mac:" + mac);
            m_baseDeviceMap.remove(mac);
        }
    }

    private HashMap<Integer, BaseDevice> getConnectedDeviceMap() {
        HashMap<Integer, BaseDevice> mBaseDeviceIDList = new HashMap<Integer, BaseDevice>();
        Iterator<BaseDevice> iter = m_baseDeviceMap.values().iterator();
        while (iter.hasNext()) {
            BaseDevice dev = iter.next();
            if (dev.getState() != BaseDevice.STATE_CONNECTED) {
                continue;
            }
            if (dev.getControllerID() == -1) {
                continue;
            }
            mBaseDeviceIDList.put(Integer.valueOf(dev.getControllerID()), dev);
        }
        return mBaseDeviceIDList;
    }

    private int getConnectedDeviceCount() {
        int result = 0;
        Iterator<BaseDevice> iterBaseDevice = m_baseDeviceMap.values().iterator();
        while (iterBaseDevice.hasNext()) {
            if (iterBaseDevice.next().getState() != BaseDevice.STATE_CONNECTED) {
                continue;
            }
            result++;
        }
        return result;
    }

    public int execCommand(int type, String mac, byte[] data) {
        LLog.d("DeviceManager->execCommand not realize type:" + type + " data:" + LLog
                .covertHexStr(data));
        int result = 0;
        switch (type) {
            case 0:
            case 8: {
                if (mac != null) {
                    if (!mac.isEmpty()) {
                        BaseDevice dev = m_baseDeviceMap.get(mac);
                        if (dev == null) {
                            return result;
                        }
                        if (!dev.sendPkg(type, data)) {
                            return result;
                        }
                        return 1;
                    }
                }
                Iterator<BaseDevice> iter = m_baseDeviceMap.values().iterator();
                while (iter.hasNext()) {
                    iter.next().sendPkg(type, data);
                }
                result = -1;
                break;
            }
            default: {
                result = -1;
                break;
            }
        }
        return result;
    }

    private int findKeyDeviceIndex() {
        int result = 1;
        HashMap<Integer, BaseDevice> ids = getConnectedDeviceMap();
        for (int i = 1; i <= 12; i++) {
            if (!ids.containsKey(Integer.valueOf(i))) {
                result = i;
                break;
            }
        }
        return result;
    }

    private static int sGetGamePadCount() {
        int flags = InputDevice.SOURCE_JOYSTICK;//0x1000010;
        int result = 0;
        int[] arrayOfInt = InputDevice.getDeviceIds();
        for (int i = 0; i < arrayOfInt.length; i++) {
            InputDevice localInputDevice = InputDevice.getDevice(arrayOfInt[i]);
            LLog.d("DeviceManager->sGetGamePadCount " + i + " id:" + localInputDevice.getId() + "" +
                    " type:" + localInputDevice.getKeyboardType() + " source:" + localInputDevice
                    .getSources() + " name:" + localInputDevice.getName());
            int source = localInputDevice.getSources();
            if ((source & SOURCE_GAMEPAD) == SOURCE_GAMEPAD && (source & flags) == flags) {
                result++;
            }
        }
        return result;
    }

    private BaseDevice getDevice(int controllerID) {
        BaseDevice dev = null;
        Iterator<BaseDevice> iter = m_baseDeviceMap.values().iterator();
        while (iter.hasNext()) {
            dev = iter.next();
            if (dev.getState() != BaseDevice.STATE_CONNECTED) {
                continue;
            }
            if (dev.getControllerID() != controllerID) {
                continue;
            }
            break;
        }
        return dev;
    }

    public void onStateEvent(PadStateEvent padStateEvent) {
        if (padStateEvent.getControllerId() == -1 && padStateEvent.getState() == BaseEvent
                .STATE_CONNECTED) {
            Iterator<BaseDevice> iter = m_baseDeviceMap.values().iterator();
            while (iter.hasNext()) {
                BaseDevice device = iter.next();
                if (device.getState() != BaseDevice.STATE_CONNECTED) {
                    continue;
                }
                if (device.getControllerID() != -1) {
                    continue;
                }
                device.setControllerID(findKeyDeviceIndex());
                String mac = padStateEvent.getMac();
                padStateEvent = new PadStateEvent(System.currentTimeMillis(), device
                        .getControllerID(), BaseEvent.STATE_CONNECTED, BaseEvent.ACTION_CONNECTED);
                padStateEvent.setMac(mac);
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (padStateEvent.getControllerId() != -1) {
            if (padStateEvent.getState() == BaseEvent.STATE_CONNECTED) {
//                execCommand(8, null, null);   //在接收到这个消息之前己经处理，所以这里就不再填写这个了，否则还要处理是否是BLE还是SSP的问题
            }
        }
    }

    public void registerCallback(String pkgName) {
        LLog.d("DeviceManager->registerCallback pkgName:" + pkgName);
        if (pkgName != null && !pkgName.isEmpty()) {
//            execCommand(8, null, null);
            gamePkgName = pkgName;
        }
        execCommand(8, null, null);
    }

    public String getGamePackageName() {
        return gamePkgName;
    }

    public PadInfo[] getPadList() {
        PadInfo[] result = null;
        Vector<PadInfo> lst = new Vector<PadInfo>();
        Iterator<BaseDevice> iter = m_baseDeviceMap.values().iterator();
        while (iter.hasNext()) {
            BaseDevice dev = iter.next();
            if (dev.getState() != BaseDevice.STATE_CONNECTED) {
                continue;
            }
            lst.add(dev);
        }
        if (!lst.isEmpty()) {
            result = lst.toArray(new PadInfo[lst.size()]);
        }
        return result;
    }


    public boolean isKeysDown(int controllerID, int[] keyArray) {
        BaseDevice dev = getDevice(controllerID);
        return (dev == null) ? false : dev.isKeyDown(keyArray);
    }

    public boolean isAppOnForeground() {
        ActivityManager.RunningAppProcessInfo appProcess = null;
        boolean result = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context
                .ACTIVITY_SERVICE);
        String pkgName = mContext.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses != null) {
            LLog.d("DeviceManager->isAppOnForeground pkgName:" + pkgName);
            Iterator<ActivityManager.RunningAppProcessInfo> iter = appProcesses.iterator();
            while (iter.hasNext()) {
                appProcess = iter.next();
                LLog.d("DeviceManager->isAppOnForeground pkgName all:" + appProcess.processName);
                if (appProcess.processName.equals(pkgName) && appProcess.importance != 100) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public void unRegisterCallback(String pkgName) {
        LLog.d("DeviceManager->unRegisterCallback");
        execCommand(0, null, null);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mContext.stopService(new Intent(mContext, PadService.class));
            }
        }, 3000);
    }

    private UUID serviceUUID = UUID.fromString("00000000-0000-1000-8000-0000805f9b34fb");
    private UUID characterUUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    private UUID nitifyOrReadCharacterUUID = UUID.fromString
            ("0000ffe2-0000-1000-8000-00805f9b34fb");
    private byte[] bytesReadData = {(byte) 0xffe1};
    private byte TAG_PRESS = -1;
    ;

    public void connect_CJ007(final String mMacAddress2Connect) {
        LLog.d("键鼠 准备连接!" + mMacAddress2Connect);
        if (mClient == null) {
            mClient = ClientManager.getClient();
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)   // 连接如果失败重试5次
                .setConnectTimeout(6000)   // 连接超时30s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
                .build();

        mClient.connect(mMacAddress2Connect, options, new BleConnectResponse() {

            @Override
            public void onResponse(int code, BleGattProfile profile) {
                LLog.d("键鼠 连接返回:" + code);
                if (code == REQUEST_SUCCESS) {
                    //连接成功
                    mClient.write(mMacAddress2Connect, serviceUUID
                            , characterUUID, bytesReadData, new BleWriteResponse() {
                                @Override
                                public void onResponse(int code) {

                                    if (0 == code) {
                                        Toast.makeText(mContext, "键鼠连接成功!", Toast.LENGTH_SHORT);
                                    } else {

                                    }
                                }
                            });
                    mClient.notify(mMacAddress2Connect, serviceUUID, nitifyOrReadCharacterUUID, new BleNotifyResponse
                                    () {
                                @Override
                                public void onNotify(UUID service, UUID character, byte[] CJ007_BYTE) {
                                    // todo  ======================  开始解析键鼠发来的数据 ===============================
                                    int length = CJ007_BYTE.length;

                                    LLog.d("键鼠数据:" + Utils.hexToStr(CJ007_BYTE));

                                    //===================   解析第 10 11 12 13 14 15 16 (按键消息)  =====================
                                    // 单击时:数据是出现在第10位 ( 抬起数据: 0 )   有按键关联: 会出现在其他位,但是按键的值是不变的
                                    byte cj007Byte_10 = CJ007_BYTE[10];
                                    if (cj007Byte_10 != 0) {
                                        TAG_PRESS = cj007Byte_10;
                                    } else {
                                        if (TAG_PRESS == 58) {
                                            LLog.d(" 键鼠数据 按 F1");
                                            final Cj007EditActivity cj007Activity = Cj007EditActivity.getInstance();
                                            if (cj007Activity == null || cj007Activity.isFinishing()) {
                                                Intent intent = new Intent(mContext, Cj007EditActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                mContext.startActivity(intent);
                                            } else {
                                                cj007Activity.finish();
                                            }

                                        }
                                    }


                                }

                                @Override
                                public void onResponse(int code) {
                                }
                            }
                    );
                } else if (code == REQUEST_FAILED) {
                    if (mClient != null) {
                        mClient.disconnect(mMacAddress2Connect);
                    }
                } else if (code == REQUEST_TIMEDOUT) {
                    if (mClient != null) {
                        mClient.disconnect(mMacAddress2Connect);
                    }
                }
            }
        });

    }

}
