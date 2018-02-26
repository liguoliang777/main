package com.ngds.pad.inject;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Handler;
import android.os.Message;

import com.lx.pad.MainActivity;
import com.lx.pad.util.LLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Administrator on 2017/12/11.
 */

/**
 * ClientFinally不要直接调用，统一使用InjectDataMgr做为终端来管理
 */

public class ClientFinally implements Runnable {
    private static final String LOCAL_SOCKET_NAME="ngame_socket_remotesensor";
    private static final int LOCAL_SOCKET_PORT = 6559;
    private int timeout = 30000;
    LocalSocket client;
    OutputStream outputStream = null;
    InputStream inputStream = null;

    Socket socketClient = null;
    Handler handler = null;

    boolean socketConnect = false;

    public ClientFinally(Handler handler) {
        this.handler = handler;
    }

    //获取当前socket的状态，即映射服务是否开启了
    public boolean getSocketConnectState(){
        return socketConnect;
    }

    public boolean send(byte[] data){
        if(outputStream != null){
            try{
                outputStream.write(data);
                return true;
            }catch(SocketException eSocket){
                eSocket.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public void restartSocket(){
        close();

        client = new LocalSocket();
        while(inputStream == null || outputStream == null) {
            try {
                client.connect(new LocalSocketAddress(LOCAL_SOCKET_NAME));
                LLog.d("ClientFinally->run connect localsocket success!");
                client.setSoTimeout(timeout);
                outputStream = client.getOutputStream();
                inputStream = client.getInputStream();
            } catch (IOException e) {
                LLog.d("ClientFinally->run connect localsocket IOException");
                e.printStackTrace();
            }

            if (outputStream == null || inputStream == null) {
                try {
                    socketClient = new Socket("127.0.0.1", LOCAL_SOCKET_PORT);
                    inputStream = socketClient.getInputStream();
                    outputStream = socketClient.getOutputStream();
                } catch (IOException e) {
                    LLog.d("ClientFinally->run normal socket IOException");
                    e.printStackTrace();
                }
            }
            if(inputStream == null || outputStream == null){
                try{
                    LLog.d("ClientFinally->run socket error! connect continue");
                    Thread.sleep(5000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {

        while(true) {
            if(isServerClose()) {
                if(socketConnect){
                    MainActivity.sendHandleMsg(MainActivity.MSG_CMD_REMOTE_SOCKET_STATE, MainActivity.MSG_VAL_SOCKET_CLOSE);
                    socketConnect = false;
                }
                LLog.d("ClientFinally->run isServerClose, try restartSocket! ++++++++++++");
                restartSocket();
            }

            if(!socketConnect){
                MainActivity.sendHandleMsg(MainActivity.MSG_CMD_REMOTE_SOCKET_STATE, MainActivity.MSG_VAL_SOCKET_CONNECTED);
                socketConnect = true;
            }

            try{
//                LLog.d("ClientFinally->run heart pkg!");
                Thread.sleep(3000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

//        String recv = "";
//        while(true){
//            try{
//                byte[] bytes = new byte[30];
//                int readBytes = inputStream.read(bytes);
//                if(readBytes != -1 && handler != null){
//                    recv = new String(bytes, 0, readBytes);
//                    Message msg = handler.obtainMessage();
//                    msg.what = 1;
//                    msg.arg1 = 0x12;
//                    msg.obj = recv;
//                    handler.sendMessage(msg);
//                }
//            }catch (IOException e){
//                LLog.d("ClientFinally->run socket msg IOException  <<======================");
//                e.printStackTrace();
//                break;
//            }
//        }
//        LLog.d("ClientFinally->run over!  <<====================== !!!");
    }

    public boolean isServerClose(){
        if(client != null && outputStream != null){
            boolean result = send(new byte[]{0, 0});
            return !result;
        }
        return true;
    }

    public void close(){
        try{
            if(outputStream != null){
                outputStream.close();
                outputStream = null;
            }
            if(inputStream != null){
                inputStream.close();
                inputStream = null;
            }
            if(client != null){
                client.close();
                client = null;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
