package com.ngds.pad.server;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.lx.pad.MainActivity;
import com.lx.pad.util.LLog;
import com.ngds.pad.BaseEvent;
import com.ngds.pad.Protocol;
import com.ngds.pad.ProtocolBle;
import com.ngds.pad.utils.Utils;

import java.util.Iterator;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by Administrator on 2017/11/28.
 */

/**
 * 蓝牙BLE协议的管理类，用于管理使用BLE方式连接的蓝牙设备
 */

public class DeviceBle extends BaseDevice {
    public static final UUID CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private BluetoothManager bleManager = null;
    private ProtocolBle.PkgBleMgr m_pkgBleMgr = null;
    private BluetoothDevice bleDevice = null;
    private Handler m_handler = null;
    private BluetoothGatt bleGatt = null;
    private BluetoothGattService bleGattService = null;
    private BluetoothGattCharacteristic bleGattCharWrite = null;
    private BluetoothGattCharacteristic bleGattCharNotify = null;
    private BluetoothGattCharacteristic bleGattCharPacket = null;
    private static final UUID PAD_SERVICE_UUID_SUB_WRITE = UUID.fromString("91680002-1111-6666-8888-0123456789ab");
    public static final UUID PAD_SERVICE_UUID = UUID.fromString("91680001-1111-6666-8888-0123456789ab");
    private static final UUID PAD_SERVICE_UUID_SUB_NOTIFY = UUID.fromString("91680003-1111-6666-8888-0123456789ab");

    //keyBle start
//    private static final UUID PAD_SERVICE_UUID_SUB_WRITE = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
//    public static final UUID PAD_SERVICE_UUID = UUID.fromString("00000000-0000-1000-8000-0000805f9b34fb");
//    private static final UUID PAD_SERVICE_UUID_SUB_NOTIFY = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb");
    //keyBle end

    private static final UUID PAD_SERVICE_UUID_SUB_PACKET = UUID.fromString("91680004-1111-6666-8888-0123456789ab");
    private BluetoothGattCallback m_bleGattCallback = null;


    public DeviceBle(final Context context, String mac) {
        super(context, mac);

        if(bleManager == null){
            bleManager = (BluetoothManager)m_context.getSystemService(Context.BLUETOOTH_SERVICE);
            if(bleManager == null){
                LLog.d("DeviceBle->DeviceBle Unable to initialize BluetoothManager");
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    m_bleAdapter = bleManager.getAdapter();
                }
                if(m_bleAdapter == null){
                    LLog.d("DeviceBle->DeviceBle Unable to obtain a BluetoothAdapter.");
                }
            }
        }

        if(m_bleAdapter != null) {
            bleDevice = m_bleAdapter.getRemoteDevice(m_mac);
        }

        if(bleDevice == null){
            LLog.d("DeviceBle->DeviceBle Device not fond Unable to connect.");
        }else{
            m_name = bleDevice.getName();
            m_handler = new Handler(m_context.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
                    switch(msg.what){
                        case 1:{
                            m_timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    sendNullPkg(6);
                                }
                            }, 500);
                            m_timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    m_queueInit = true;
                                    DeviceManager.getInstance(context).execCommand(8, null, new byte[]{0});
                                    DeviceManager.getInstance(context).addPadStateEvent(m_mac, m_controllerID, BaseEvent.STATE_CONNECTED, BaseEvent.ACTION_CONNECTED);
//                                    LLog.d("DeviceBle->DeviceBle stateEvent need realize!");
                                }
                            }, 1000);
                            break;
                        }
                    }
                }
            };
        }
    }

    public void setCharNotification(BluetoothGattCharacteristic characteristic, boolean enable){
        if(m_bleAdapter == null || bleGatt == null){
            LLog.d("DeviceBle->setCharNotification some val can't null");
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bleGatt.setCharacteristicNotification(characteristic, enable);
                if(PAD_SERVICE_UUID_SUB_NOTIFY.equals(characteristic.getUuid())){
                    BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CHARACTERISTIC_CONFIG);
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    bleGatt.writeDescriptor(descriptor);
                }
            }else{
                LLog.d("DeviceBle->setCharNotification SDK error! sdk ver:" + Build.VERSION.SDK_INT);
            }
        }
    }

    @Override
    public boolean sendPkg(int type, byte[] data) {
//        return super.sendPkg(type, data);
        boolean result = false;
        LLog.d("DeviceBle->sendPkg type:" + type + " data:" + LLog.covertHexStr(data));

        ProtocolBle protocolBle = new ProtocolBle();
        try{
            switch(type){
                case Protocol.CMD_SPP_OFF:{
                    if(!m_queueInit){
                        return result;
                    }
                    ProtocolBle.PkgInputModeSendToPad pkgInputModeSendToPad = protocolBle.new PkgInputModeSendToPad();
                    pkgInputModeSendToPad.setData(new byte[]{0});
                    if(!sendDataBle(pkgInputModeSendToPad.catPkgBuf())){
                        return result;
                    }
                    LLog.d("DeviceBle->sendPkg  CMD_SPP_OFF send:" + LLog.covertHexStr(pkgInputModeSendToPad.catPkgBuf()));
                    result = true;
                    break;
                }
                case Protocol.CMD_MOTOR:{
                    ProtocolBle.PkgVibration pkgVibration = protocolBle.new PkgVibration();
                    pkgVibration.setData(data);
                    LLog.d("DeviceBle->sendPkg  CMD_MOTOR send:" + LLog.covertHexStr(pkgVibration.catPkgBuf()));
                    return sendDataBle(pkgVibration.catPkgBuf());
//                    break;
                }
                case Protocol.CMD_VERSION_QUERY:{
                    ProtocolBle.PkgHardId pkgHardId = protocolBle.new PkgHardId();
                    result = sendDataBle(pkgHardId.catPkgBuf());
                    LLog.d("DeviceBle->sendPkg  CMD_VERSION_QUERY send:" + LLog.covertHexStr(pkgHardId.catPkgBuf()));
                    return result;
//                    break;
                }
                case Protocol.CMD_SPP_ON:{
                    if(!m_queueInit){
                        return result;
                    }
                    ProtocolBle.PkgInputModeRecvFromPad pkgInputModeRecvFromPad = protocolBle.new PkgInputModeRecvFromPad();
                    pkgInputModeRecvFromPad.setData(new byte[]{1});
                    if(!sendDataBle((pkgInputModeRecvFromPad.catPkgBuf()))){
                        return result;
                    }
                    LLog.d("DeviceBle->sendPkg CMD_SSP_ON mac:" + m_mac + " data:" + LLog.covertHexStr(pkgInputModeRecvFromPad.catPkgBuf()));
                    result = true;
                    break;
                }
                case 11:{
                    ProtocolBle.PkgSensor pkgSensor = protocolBle.new PkgSensor();
                    pkgSensor.setData(data);
                    LLog.d("DeviceBle->sendPkg  data:" + LLog.covertHexStr(pkgSensor.catPkgBuf()));
                    return sendDataBle(pkgSensor.catPkgBuf());
//                    break;
                }
            }
        }catch (Exception e){
            LLog.d("DeviceBle->sendPkg mac:" + m_mac + " type:" + type + " sendMsg fail:" + e.toString());
            e.printStackTrace();
        }

        return result;
    }

    protected boolean sendDataBle(byte[] data){
        boolean result = false;
        if(getState() == STATE_CONNECTED && bleGatt != null && bleGattCharWrite != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                LLog.d("DeviceBle->sendDataBle param:" + LLog.covertHexStr(data));
                bleGattCharWrite.setValue(data);
                result = bleGatt.writeCharacteristic(bleGattCharWrite);
            }else{
                LLog.d("DeviceBle->sendDataBle err! SDK ver:" + Build.VERSION.SDK_INT);
            }
        }else{
            LLog.d("DeviceBle->sendDataBle err! some val mast be can't null");
        }
        return result;
    }

    @Override
    public boolean sendNullPkg(int type) {
        return sendPkg(type, null);
    }

    public synchronized void closeBleGatt(){
        setState(STATE_NONE);
        if(m_bleAdapter != null && bleGatt != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bleGatt.close();
            }else{
                LLog.d("DeviceBle->sendDataBle err! SDK ver:" + Build.VERSION.SDK_INT);
            }
        }
        m_queueInit = false;
        MainActivity.sendHandleMsg(MainActivity.MSG_CMD_BLE_CONNECT_STATE, MainActivity.MSG_VAL_BLE_NONE);
        DeviceManager.getInstance(m_context).addPadStateEvent(m_mac, m_controllerID, BaseEvent.STATE_DISCONNECTED, BaseEvent.ACTION_DISCONNECTED);
    }

    protected void decodeData(byte[] body){
        if(body != null && body.length >= 3){
            LLog.d("DeviceBle->decodeData data:" + LLog.covertHexStr(body));

            ProtocolBle protocolBle = new ProtocolBle();
            try{
                switch(body[2]){
                    case Protocol.CMD_KEYEVENT:{
                        ProtocolBle.PkgBleMgr keyInfo = protocolBle.new PkgBleMgr();
                        keyInfo.setDeviceID(m_controllerID);
                        if(!keyInfo.encodeData(body)){
                            return;
                        }
                        keyInfo.checkEvent(m_pkgBleMgr);
                        //将数据发送到消息队列
                        LLog.d("DeviceBle->decodeData  CMD_KEYEVENT <<< function is not realize");
                        m_pkgBleMgr = keyInfo;
                        break;
                    }
                    case Protocol.CMD_VERSION_QUERY:{
                        LLog.d("DeviceBle->decodData CMD_VERSION_QUERY data:" + Utils.hexToStr(body, "-"));
                        m_UUID = Utils.hexToStr(body, "-").substring(9);
                        break;
                    }
                    case 11:{
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @TargetApi(18)
    public synchronized void resetBleGatt(){
        LLog.d("DeviceBle->resetBleGatt");
        if(m_state == STATE_CONNECTING || m_state == STATE_CONNECTED){
            return ;
        }

        try{
            setState(STATE_CONNECTING);
            setControllerID(-1);
            if(bleGatt != null){
                bleGatt.close();
                bleGatt = null;
            }

            m_bleGattCallback = new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//                    super.onConnectionStateChange(gatt, status, newState);
                    if(newState == STATE_CONNECTING){
                        LLog.d("DeviceBle->resetBleGatt onConnectionStateChange Ble connecting status:" + status + " newState:" + newState);
                        boolean result = gatt.discoverServices();
                        LLog.d("DeviceBle->resetBleGatt onConnectionStateChange discoverServices result:" + result);
                    }else if(newState == STATE_NONE){
                        LLog.d("DeviceBle->resetBleGatt onConnectionStateChange Ble disConnect " + m_mac);
                        closeBleGatt();
                    }
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//                    super.onServicesDiscovered(gatt, status);
                    LLog.d("DeviceBle->resetBleGatt onServicesDiscovered status:" + status);
                    if(status == 0){
                        Iterator<BluetoothGattService> iter = gatt.getServices().iterator();
                        while(iter.hasNext()){
                            BluetoothGattService service = iter.next();
                            if(!PAD_SERVICE_UUID.equals(service.getUuid())){
                                continue;
                            }
                            bleGattService = service;
                            bleGattCharWrite = bleGattService.getCharacteristic(PAD_SERVICE_UUID_SUB_WRITE);
                            bleGattCharNotify = bleGattService.getCharacteristic(PAD_SERVICE_UUID_SUB_NOTIFY);
                            bleGattCharPacket = bleGattService.getCharacteristic(PAD_SERVICE_UUID_SUB_PACKET);
                            bleGattCharPacket.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                            setCharNotification(bleGattCharNotify, true);
                            setState(STATE_CONNECTED);
                            m_handler.obtainMessage(1).sendToTarget();
                            //测试代码，发送蓝牙连接成功的提示
                            MainActivity.sendHandleMsg(MainActivity.MSG_CMD_BLE_CONNECT_STATE, MainActivity.MSG_VAL_BLE_CONNECTED);
                        }
                    }else{
                        LLog.d("DeviceBle->resetBleGatt onServicesDiscovered received:" + status);
                    }
                }

                @Override
                public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                    super.onCharacteristicRead(gatt, characteristic, status);
                }

                @Override
                public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//                    super.onCharacteristicWrite(gatt, characteristic, status);
                    if(characteristic.getUuid().equals(PAD_SERVICE_UUID_SUB_PACKET)){
                        LLog.d("DeviceBle->resetBleGatt onCharacteristicWrite " + LLog.covertHexStr(characteristic.getValue()) + " status:" + status);
                    }
                }

                @Override
                public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//                    super.onCharacteristicChanged(gatt, characteristic);
                    byte[] data = characteristic.getValue();
                    if(data != null && data.length != 0){
                        decodeData(data);
                    }
                }
            };

            bleGatt = bleDevice.connectGatt(m_context, false, m_bleGattCallback);
            LLog.d("DeviceBle->resetBleGatt connectGatt");
            if(bleGatt == null){
                LLog.d("DeviceBle->resetBleGatt err! bleGatt is null!");
            }

        }catch (Throwable e){
            throw e;
        }
    }
}
