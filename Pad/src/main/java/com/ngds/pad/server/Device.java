package com.ngds.pad.server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.lx.pad.MainActivity;
import com.lx.pad.util.LLog;
import com.ngds.pad.BaseEvent;
import com.ngds.pad.PadKeyEvent;
import com.ngds.pad.PadSensorEvent;
import com.ngds.pad.Protocol;
import com.ngds.pad.utils.Utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by Administrator on 2017/11/28.
 */

/**
 * 蓝牙的SSP协议管理类
 */

public class Device extends BaseDevice {
    class CommunicationThread extends Thread{
        private BluetoothSocket m_socket;
        private InputStream m_inputStream;
        private OutputStream m_outputStream;

        public CommunicationThread(BluetoothSocket socket, String socketType){
            super();
            m_socket = socket;
            OutputStream tmpOut = null;
            InputStream tmpIn = null;
            try{
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            }catch(Exception e){
                LLog.d("Device->CommunicationThread " + m_mac + " temp sockets not created", e);
            }

            m_inputStream = tmpIn;
            m_outputStream = tmpOut;
            m_handler.obtainMessage(1).sendToTarget();
        }

        public void closeSocket(){
            try{
                if(m_socket != null){
                    m_socket.close();
                    m_socket = null;
                }
            }catch (Exception e){
                LLog.d("Device->CommunicationThread->closeSocket " + m_mac + " close() of connect socket failed", e);
            }
        }

        public boolean sendMessage(byte[] buf){
            boolean result = false;
            try{
                m_outputStream.write(buf);
                result = true;
            }catch(Exception e){
                LLog.d("Device->CommunicationThread->sendMessage " + m_mac + " Exception during sendMessage", e);
                try{
                    m_socket.close();
                }catch(Exception e1){
                    LLog.d("Device->CommunicationThread->sendMessage " + m_mac + " unable to close() socket during CommunicationThread failure", e1);
                }
                result = false;
            }
            return result;
        }

        @Override
        public void run() {
//            super.run();
            byte[] bufferContent = null;
            byte[] bufferHeader = new byte[4];
            try{
                while(true){
                    m_inputStream.read(bufferHeader);
                    int sizeContent = bufferHeader[3];
                    if(sizeContent > 4){
                        bufferContent = new byte[sizeContent - 4];
                    }else{
                        bufferContent = new byte[32];
                    }
                    m_inputStream.read(bufferContent);
                    encodePkg(Utils.catByteAry(bufferHeader, bufferContent));
                }
            }catch (Exception e){
                LLog.d("Device->CommunicationThread->run exception!", e);
                try{
                    m_socket.close();
                }catch (Exception e1){
                    LLog.d("Device->CommunicationThread->run " + m_mac + " unable to close() socket during CommunicationThread failure", e1);
                }
                errDisConnect("Device connection was lost");
            }
        }
    }

    class SecureSocketThread extends Thread{
        private BluetoothSocket m_bleSocket;
        private String m_strSocketType;

        public SecureSocketThread(BluetoothDevice device, boolean secure){
            super();
            m_bleAdapter.cancelDiscovery();
            String strType = secure ? "Secure" : "Insecure";
            m_strSocketType = strType;
            try{
                if(!secure){
                    m_bleSocket = device.createInsecureRfcommSocketToServiceRecord(UUID_SPP);
                }else{
                    m_bleSocket = device.createRfcommSocketToServiceRecord(UUID_SPP);
                }
                LLog.d("Device->SecureSocketThread create socket success!");
            }catch(Exception e){
                LLog.d("Device->SecureSocketThread " + m_mac + " Socket Type: " + m_strSocketType + " create() failed ", e);
            }
        }

        public void close(){
            try{
                if(m_bleSocket != null){
                    m_bleSocket.close();
                    m_bleSocket = null;
                }
            }catch(Exception e){
                LLog.d("Device->SecureSocketThread " + m_mac + " close() of connect " + m_strSocketType + " socket failed", e);
            }
        }

        @Override
        public void run() {
//            super.run();
            try{
                m_bleAdapter.cancelDiscovery();
                m_bleSocket.connect();
                LLog.d("Device->SecureSocketThread run connect success!");
            }catch(Exception e){
                LLog.d("Device->SecureSocketThread->run " + m_mac + " to close() " + m_strSocketType + " socket during connection failure", e);
                try{
                    if(m_bleSocket != null){
                        m_bleSocket.close();
                        m_bleSocket = null;
                    }
                }catch(Exception e1){
                    LLog.d("Device->SecureSocketThread->run " + m_mac + " unable to close " + m_strSocketType + " socket during connection failure", e1);
                }
                errDisConnect("unable to open socket");
                return ;
            }
            synchronized (Device.this){
                m_secureSocketThread = null;
            }

            restartCommunicationThread(m_bleSocket, m_strSocketType);
        }
    }


    public static final UUID UUID_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Handler m_handler;
    private CommunicationThread m_communicationThread;
    private SecureSocketThread m_secureSocketThread;
    private BluetoothDevice m_bleDevice;
    private Protocol.PkgMgrSSP m_pkgMgrSSP;

    public Device(final Context context, String mac) {
        super(context, mac);
        m_pkgMgrSSP = null;
        m_bleAdapter = BluetoothAdapter.getDefaultAdapter();
        m_bleDevice = m_bleAdapter.getRemoteDevice(m_mac);
        m_name = m_bleDevice.getName();
        m_handler = new Handler(m_context.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
                switch(msg.what){
                    case 1:{
                        m_timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendNullPkg(19);
                            }
                        }, 300);
                        m_timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                m_queueInit = true;
                                DeviceManager.getInstance(context).execCommand(8, null, null);
                                MainActivity.sendHandleMsg(MainActivity.MSG_CMD_BLE_CONNECT_STATE, MainActivity.MSG_VAL_BLE_CONNECTED);
                                DeviceManager.getInstance(context).addPadStateEvent(m_mac, m_controllerID, BaseEvent.STATE_CONNECTED, BaseEvent.ACTION_CONNECTED);
                                LLog.d("Device->Device->timetask function is invalid");
                            }
                        }, 600);
                        break;
                    }
                }
            }
        };
    }

    private void errDisConnect(String errorMsg){
        LLog.d("Device->errDisConnect " + errorMsg);
        disConnect();
    }

    public synchronized void restartCommunicationThread(BluetoothSocket socket, String socketType){
        LLog.d("Device->restartCommunicationThread");
        if(m_secureSocketThread != null){
            m_secureSocketThread.close();
            m_secureSocketThread = null;
        }
        if(m_communicationThread != null){
            m_communicationThread.closeSocket();
            m_communicationThread = null;
        }

        m_communicationThread = new CommunicationThread(socket, socketType);
        m_communicationThread.start();
        setState(STATE_CONNECTED);
        MainActivity.sendHandleMsg(MainActivity.MSG_CMD_BLE_CONNECT_STATE, MainActivity.MSG_VAL_BLE_CONNECTED);
    }

    public synchronized void restartSecureSocketThread(){
        LLog.d("Device->restartSecureSocketThread m_state:" + m_state);
        if(m_state == STATE_CONNECTING){
            if(m_state == STATE_CONNECTED){
                m_queueInit = true;
            }
            return ;
        }else if(m_state != STATE_CONNECTED){

        }else{
            if(m_state == STATE_CONNECTED){
                m_queueInit = true;
            }
            return ;
        }
        LLog.d("Device->restartSecureSocketThread renew socketThread");
        setControllerID(-1);
        if(m_communicationThread != null){
            m_communicationThread.closeSocket();
            m_communicationThread = null;
        }

        m_secureSocketThread = new SecureSocketThread(m_bleDevice, false);
        m_secureSocketThread.start();
        setState(STATE_CONNECTING);
    }

    @Override
    public boolean sendPkg(int type, byte[] data) {
//        return super.sendPkg(type, data);
        boolean result = false;
        LLog.d("Device->sendPkg type:" + type + " data:" + LLog.covertHexStr(data));

        Protocol protocol = new Protocol();
        try{
            switch(type){
                case Protocol.CMD_SPP_OFF:{
                    if(!m_queueInit){
                        return result;
                    }
                    Protocol.PkgSSPType8_send pkgCmd8Send = protocol.new PkgSSPType8_send();
                    pkgCmd8Send.setPkgData(new byte[]{0});
                    if(!sendData(pkgCmd8Send.catPkg())){
                        return result;
                    }
                    LLog.d("Device->sendPkg mac:" + m_mac + " send:" + LLog.covertHexStr(pkgCmd8Send.catPkg()));
                    result = true;
                    break;
                }
                case Protocol.CMD_MOTOR:{
                    Protocol.PkgSSPType2 pkgCmd2 = protocol.new PkgSSPType2();
                    pkgCmd2.setPkgData(data);
                    LLog.d("Device->sendPkg motor send: " + LLog.covertHexStr(pkgCmd2.catPkg()));
                    return sendData(pkgCmd2.catPkg());
//                    break;
                }
                case Protocol.CMD_SPP_ON:{
                    if(!m_queueInit){
                        return result;
                    }
                    Protocol.PkgSSPType8 pkgCmd8 = protocol.new PkgSSPType8();
                    pkgCmd8.setPkgData(new byte[]{1});
                    if(!sendData(pkgCmd8.catPkg())){
                        return result;
                    }
                    LLog.d("Device->sendPkg mac:" + m_mac + " send:" + LLog.covertHexStr(pkgCmd8.catPkg()));
                    result = true;
                    break;
                }
                case Protocol.CMD_UUID_QUERY:{
                    Protocol.PkgSSPType19 pkgCmd19 = protocol.new PkgSSPType19();
                    return sendData(pkgCmd19.catPkg());
//                    break;
                }
                case Protocol.CMD_SENSOR_QUERY:{
                    Protocol.PkgSSPType33 pkgCmd33 = protocol.new PkgSSPType33();
                    pkgCmd33.setPkgData(data);
                    LLog.d("Device->sendPkg send:" + LLog.covertHexStr(pkgCmd33.catPkg()));
                    return sendData(pkgCmd33.catPkg());
//                    break;
                }
            }
        }catch(Exception e){
            LLog.d("Device->sendPkg mac:" + m_mac + " type:" + type + " sendMsg Fail:" + e.toString());
            e.printStackTrace();
            return result;
        }

//        LLog.d("Device->sendPkg function not realize");

        return result;
    }

    @Override
    protected boolean sendData(byte[] data) {
//        return super.sendData(out);
        LLog.d("Device->sendData data:" + LLog.covertHexStr(data));
        CommunicationThread communicationThread;
        boolean result = false;
        synchronized (this){
            if(m_state != STATE_CONNECTED){
                return result;
            }else{
                communicationThread = m_communicationThread;
            }
        }
        return communicationThread.sendMessage(data);
    }

    @Override
    public boolean sendNullPkg(int type) {
//        return super.sendNullPkg(type);
        return sendPkg(type, null);
    }

    @Override
    public synchronized void disConnect() {
//        super.disConnect();
        if(m_secureSocketThread != null){
            m_secureSocketThread.close();
            m_secureSocketThread = null;
        }
        if(m_communicationThread != null){
            m_communicationThread.closeSocket();
            m_communicationThread = null;
        }

        LLog.d("Device->disConnect all ble ssp socket close");

//        m_state = STATE_NONE;
        setState(STATE_NONE);
        m_queueInit = false;
    }

    protected void encodePkg(byte[] data){
//        LLog.d("Device->encodePkg data:" + LLog.covertHexStr(data));
        if(data != null && data.length >= 5){
//            LLog.d("Device->encodePkg function is not realize");
            //判断协议包头是否正确


            //解析协议
            try{
                Protocol protocol = new Protocol();
                switch(data[Protocol.CMD_ID_SET]){
                    case Protocol.CMD_KEYEVENT:{
                        Protocol.PkgMgrSSP pkgMgrSSP = protocol.new PkgMgrSSP();
                        pkgMgrSSP.setDeviceId(m_controllerID);
                        if(!pkgMgrSSP.encodeData(data)){
                            return ;
                        }
                        pkgMgrSSP.checkEvent(m_pkgMgrSSP);
                        // 这里应该考虑往线程队列里放，放入的数据由线程去管理发送
//                        LLog.d("Device->encodePkg CMD_KEYEVENT");
                        m_pkgMgrSSP = pkgMgrSSP;
                        break;
                    }
                    case Protocol.CMD_UUID_RESULT:{
                        LLog.d("Device->encodePkg CMD_UUID_RESULT data:" + LLog.covertHexStr(data, "-"));
                        m_UUID = LLog.covertHexStr(data, "-").substring(15);
                        break;
                    }
                    case Protocol.CMD_SENSOR_QUERY:{
                        LLog.d("Device->encodePkg CMD_SENSOR_QUERY data:" + LLog.covertHexStr(data, "-"));
                        byte[] tmp = new byte[2];
                        short[] shortAry = new short[6];
                        int srcIndex = 5;
                        for(int destIndex = 0; srcIndex < 16; destIndex++){
                            int temp = srcIndex + 1;
                            tmp[0] = data[srcIndex];
                            tmp[1] = data[temp];
                            shortAry[destIndex] = Utils.sGetShortVal(tmp, 0);
                            srcIndex = temp + 1;
                        }
                        float[] srcAry = new float[]{shortAry[1] * ACC_UNIT,
                            shortAry[0] * ACC_UNIT,
                            -shortAry[2] * ACC_UNIT,
                            shortAry[4] * GYR_UNIT,
                            shortAry[3] * GYR_UNIT,
                            -shortAry[5] * GYR_UNIT};
                        PadSensorEvent padSensorEvent = new PadSensorEvent(System.nanoTime(), m_controllerID, srcAry.length);
                        System.arraycopy(srcAry, 0, padSensorEvent.vals, 0, srcAry.length);
                        LLog.d("Device->encodePkg <<< need realize!");
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
