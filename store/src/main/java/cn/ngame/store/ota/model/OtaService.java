package cn.ngame.store.ota.model;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import cn.ngame.store.StoreApplication;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.VersionInfo;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.MD5;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.exception.NoSDCardException;

/**
 * 进行设备OTA申请的服务
 * Created by zeng on 2016/8/10.
 */
public class OtaService extends Service {

    public final static String TAG = "777";

    public final static String ACTION_BLUETOOTH_NONSUPPORT = "cn.ngame.bluetooth.le.ACTION_BLUETOOTH_NONSUPPORT"; //手机不支持蓝牙
    public final static String ACTION_BLUETOOTH_DISABLE = "cn.ngame.bluetooth.le.ACTION_BLUETOOTH_DISABLE";  //蓝牙未开启
    public final static String ACTION_BLUETOOTH_FIND_DEVICE = "cn.ngame.bluetooth.le.ACTION_BLUETOOTH_FIND_DEVICE";  //发现可用设备
    public final static String ACTION_BLUETOOTH_CHECK_UPDATE = "cn.ngame.bluetooth.le.ACTION_BLUETOOTH_CHECK_UPDATE";  //检测是否有可用更新

    public final static String ACTION_OTA_UPDATING = "cn.ngame.bluetooth.le.ACTION_OTA_UPDATING"; //正在升级

    public final static String ACTION_GATT_DISCONNECTED = "cn.ngame.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_NOTIFY_ENABLE = "cn.ngame.bluetooth.le.ACTION_GATT_NOTIFY_ENABLE";

    private boolean isUpdating = false;         //是否正在进行OTA升级
    private boolean isCheckNewVersioning = false;       //是否正在检测设备的版本信息

    private boolean isWriteLogFile = false;


    private MyBinder myBinder = new MyBinder();
    private ClassicService classicService;
    private BLEService bleService;
    private ArrayList<DeviceInfo> deviceInfoList;

    private BluetoothAdapter bluetoothAdapter;

    static {
            //System.loadLibrary("Crc8Jni");
    }

//    private int updateCount;

    private native byte getCrc8(byte[] bytes);


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        classicService = new ClassicService();
        bleService = new BLEService();
        deviceInfoList = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeLogFile();
    }

    /**
     * 通过广播方式通知更新UI
     */
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    /**
     * 用于朝SD卡上写日志
     */
    private File file;
    private FileWriter fw = null;

    private void initOutPutLogFile() {
        try {
            file = new File(CommonUtil.getFileLoadBasePath());
            if (!file.exists()) {
                file.mkdirs();
            }

            file = new File(file, "ota.txt");
            if (file.exists()) {
                file.delete();
            }
            fw = new FileWriter(file);

        } catch (NoSDCardException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //写日志数据到SD卡
    private void writeLogData(byte[] buffer) {
        String dataStr = bytesToHexString(buffer);
        try {
            fw.write(dataStr + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "-------->>>> " + dataStr);
    }

    private void closeLogFile() {
        try {
            if (fw != null)
                fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化本地蓝牙适配器
     *
     * @return 如果手机支持蓝牙设备则返回true
     */
    private boolean checkBlueToothSupportable() {

        if (bluetoothAdapter != null) {
            return true;
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //手机不支持蓝牙设备
        if (bluetoothAdapter == null) {
            final Intent intent = new Intent(ACTION_BLUETOOTH_NONSUPPORT);
            sendBroadcast(intent);
            Log.d(TAG, "----------->>>　手机不支持蓝牙设备");
            return false;
        }
        return true;
    }

    /**
     * 扫描可用设备
     */
    public void scanDevice() {

        if (isUpdating) {
            if (deviceInfoList != null && deviceInfoList.size() > 0) {
                Intent intent = new Intent(ACTION_BLUETOOTH_CHECK_UPDATE);
                intent.putExtra("title", "升级中");
                intent.putExtra("subtitle", "正在复制文件...");
                intent.putParcelableArrayListExtra("devices", deviceInfoList);
                sendBroadcast(intent);
                return; //如果正在升级，忽略该操作
            } else {
                isUpdating = false;
            }
        }

        //检查设备是否支持蓝牙
        boolean isSupportBt = checkBlueToothSupportable();
        if (!isSupportBt) {
            broadcastUpdate(ACTION_BLUETOOTH_NONSUPPORT);
            return;
        }

        int btState = bluetoothAdapter.getState();
        //蓝牙未开启  正在关闭蓝牙
        if (btState == BluetoothAdapter.STATE_OFF || btState == BluetoothAdapter.STATE_TURNING_OFF) {
            broadcastUpdate(ACTION_BLUETOOTH_DISABLE);
        } else {  //蓝牙已开启
            Set<BluetoothDevice> pairDevice = bluetoothAdapter.getBondedDevices();
            if (pairDevice.size() > 0) {
                deviceInfoList.clear(); //清空已扫描到的设备
                //======================================循环  已配对设备===========================================
                for (BluetoothDevice device : pairDevice) {
                    int type = device.getType();
                    Log.d(TAG, "循环 已配对的设备名字: " + device.getName() + ",地址" + device.getAddress() + " 配对状态:" + device
                            .getBondState() + "类型:" + type);
                    if (type == BluetoothDevice.DEVICE_TYPE_LE) { //	 type, Low Energy - LE-only
                        int versionCode = bleService.queryVersionCode(device);
                        Log.d(TAG, "-------------->>>　　我是BLE设备,版本号:" + versionCode);
                        //if (versionCode > 0) {
                        DeviceInfo deviceInfo = new DeviceInfo(device.getName(), device.getAddress(), type);
                        deviceInfo.setCurrentVersionCode(versionCode);
                        deviceInfo.setCurrentVersionName("V" + versionCode);
                        deviceInfoList.add(deviceInfo);
                        //}

                    } else if (type == BluetoothDevice.DEVICE_TYPE_CLASSIC) { // type, Classic - BR/EDR
                        int versionCode = classicService.queryVersionCode(device);//查询版本号
                        Log.d(TAG, "-------------->>>　我是经典蓝牙3.0设备,版本号" + versionCode);
                        if (versionCode != -1) {
                            DeviceInfo deviceInfo = new DeviceInfo(device.getName(), device.getAddress(), type);
                            deviceInfo.setCurrentVersionCode(versionCode);
                            deviceInfo.setCurrentVersionName("V" + versionCode);
                            deviceInfoList.add(deviceInfo);
                        }
                    } else {
                        Log.d(TAG, "未知设备");
                        //未知设备  未搜索到设备
                        Intent intent = new Intent(ACTION_BLUETOOTH_FIND_DEVICE);
                        intent.putExtra("title", "连接失败");
                        intent.putExtra("subtitle", "未搜索到设备");
                        intent.putParcelableArrayListExtra("devices", null);
                        sendBroadcast(intent);
                    }
                }
                //==========循环  end ============

                //如果上面的循环,查到的设备个数大于0
                if (deviceInfoList.size() > 0) {
                    android.util.Log.d(TAG, "查询到的设备个数大于0,发送广播");
                    Intent intent = new Intent(ACTION_BLUETOOTH_FIND_DEVICE);
                    intent.putExtra("title", "连接成功");
                    intent.putExtra("subtitle", "");
                    intent.putParcelableArrayListExtra("devices", deviceInfoList);
                    sendBroadcast(intent);
                    //未查到设备,个数==0
                } else {
                    android.util.Log.d(TAG, "查询到的设备小于0,发送广播");
                    //发送广播,更新界面
                    Intent intent = new Intent(ACTION_BLUETOOTH_FIND_DEVICE);
                    intent.putExtra("title", "连接失败");
                    intent.putExtra("subtitle", "无设备连接");
                    intent.putParcelableArrayListExtra("devices", null);
                    sendBroadcast(intent);
                }

            } else {
                Log.d(TAG, "配对设备列表为空");
                Intent intent = new Intent(ACTION_BLUETOOTH_FIND_DEVICE);
                intent.putExtra("title", "连接失败");
                intent.putExtra("subtitle", "未搜索到设备");
                intent.putParcelableArrayListExtra("devices", null);
                sendBroadcast(intent);
            }
        }
    }


    /**
     * 检测是否有最新版本
     *
     * @return
     */
    public int checkNewVersion() {

        if (isCheckNewVersioning) {
            return -2;
        }

        if (isUpdating) {
            if (deviceInfoList != null && deviceInfoList.size() > 0) {
                return -3;
            } else {
                isUpdating = false;
                return -1;
            }
        }

        isCheckNewVersioning = true;

        int updateCount = 0;
        if (deviceInfoList != null && deviceInfoList.size() > 0) {

            Intent intent = new Intent(ACTION_BLUETOOTH_CHECK_UPDATE);
            intent.putExtra("title", "版本检测");
            intent.putExtra("subtitle", "正在检测新版本...");
            intent.putParcelableArrayListExtra("devices", deviceInfoList);
            sendBroadcast(intent);

            String url = Constant.WEB_SITE + Constant.URL_APP_UPDATE;

            for (final DeviceInfo info : deviceInfoList) {
//                Response.Listener<JsonResult<VersionInfo>> successListener = new Response.Listener<JsonResult<VersionInfo>>() {
//                    @Override
//                    public void onResponse(JsonResult<VersionInfo> result) {
//                        if (result == null) {
//                            return;
//                        }
//                        if (result != null && result.code == 0 && result.data != null) {
//                            VersionInfo versionInfo = result.data;
//                            if (versionInfo.versionCode > info.getCurrentVersionCode()) {
//                                updateCount++;
//                                info.setNewVersionCode(versionInfo.versionCode);
//                                info.setNewVersionName(versionInfo.versionName);
//                                info.setOtaUrl(versionInfo.url);
//                                info.setOtaMd5(versionInfo.md5);
//                                info.setContent(versionInfo.content);
//                            }
//                        } else {
//                            Log.d(TAG, "HTTP请求成功：服务端返回错误 : " + result.msg);
//                        }
//                    }
//                };
//                Response.ErrorListener errorListener = new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        volleyError.printStackTrace();
//                        Log.d(TAG, "HTTP请求失败：网络连接错误！");
//                    }
//                };
//
//                Request<JsonResult<VersionInfo>> versionRequest = new GsonRequest<JsonResult<VersionInfo>>(Request.Method
// .POST, url,
//                        successListener, errorListener, new TypeToken<JsonResult<VersionInfo>>() {
//                }.getType()) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//
//                        Map<String, String> params = new HashMap<>();
//                        if (info.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
//                            params.put("appType", "3");
//                        } else if (info.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC) {
//                            params.put("appType", "4");
//                        }
//                        return params;
//                    }
//                };
//                StoreApplication.requestQueue.add(versionRequest);
                RequestFuture<JsonResult<VersionInfo>> future = RequestFuture.newFuture();
                Request<JsonResult<VersionInfo>> versionRequest = new GsonRequest<JsonResult<VersionInfo>>(Request.Method.POST,
                        url, future, future, new TypeToken<JsonResult<VersionInfo>>() {
                }.getType()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        if (info.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                            params.put("appType", "3");
                        } else if (info.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC) {
                            params.put("appType", "4");
                        }
                        return params;
                    }
                };
                StoreApplication.requestQueue.add(versionRequest);

                try {
                    JsonResult<VersionInfo> result = future.get();
                    if (result != null && result.code == 0 && result.data != null) {
                        VersionInfo versionInfo = result.data;
                        if (versionInfo.versionCode > info.getCurrentVersionCode()) {
                            updateCount++;
                            info.setNewVersionCode(versionInfo.versionCode);
                            info.setNewVersionName(versionInfo.versionName);
                            info.setOtaUrl(versionInfo.url);
                            info.setOtaMd5(versionInfo.md5);
                            info.setContent(versionInfo.content);
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return 0;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    return 0;
                }
            }

            intent.putExtra("title", "检测完成");
            intent.putExtra("subtitle", "");
            intent.putParcelableArrayListExtra("devices", deviceInfoList);
            sendBroadcast(intent);

            isCheckNewVersioning = false;

            return updateCount;

        } else {
            isCheckNewVersioning = false;
            return -1; //未搜索到设备
        }
    }

    /**
     * 开始更新游戏手柄
     *
     * @param deviceInfo 可更新设备的信息
     */
    public void updateDevice(DeviceInfo deviceInfo) {
        if (isUpdating) {
            return;
        }
        Set<BluetoothDevice> pairDevice = bluetoothAdapter.getBondedDevices();
        if (pairDevice.size() > 0) {
            String mac = deviceInfo.getMac();
            Intent intent = new Intent(ACTION_GATT_DISCONNECTED);
            intent.putExtra("state", "升级失败");
            intent.putExtra("stateMsg", "设备连接断开");
            for (BluetoothDevice device : pairDevice) {
                if (device.getAddress().equals(mac)) {

                    if (device.getType() == BluetoothDevice.DEVICE_TYPE_LE) {//Ble蓝牙-
                        android.util.Log.d(TAG, "ble蓝牙升级: ");
                        bleService.update(device, deviceInfo);
                    } else if (device.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC) {//经典蓝牙
                        android.util.Log.d(TAG, "经典蓝牙升级: ");
                        classicService.update(device, deviceInfo);
                    }
                } else {
                   //升级失败
                    sendBroadcast(intent);
                }
            }
        } else {
            Intent intent = new Intent(ACTION_GATT_DISCONNECTED);
            intent.putExtra("state", "连接失败");
            intent.putExtra("stateMsg", "未搜索到设备");
            sendBroadcast(intent);
        }

    }


    public class MyBinder extends Binder {
        public OtaService getService() {
            return OtaService.this;
        }
    }

/**********************************************************************************************/
/**********************************************************************************************/

    /**
     * 单模低功耗蓝牙（BLE）通信服务类
     *
     * @author flan
     * @since 2016-09-20
     */
    public class BLEService {

        /**
         * 开启蓝牙notify功能的UUID
         */
        private UUID CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID
                = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

        private UUID MY_UUID_SERVER = UUID.fromString("91680001-1111-6666-8888-0123456789AB");
        private UUID MY_UUID_WRITE = UUID.fromString("91680002-1111-6666-8888-0123456789AB");
        private UUID MY_UUID_NOTIFY = UUID.fromString("91680003-1111-6666-8888-0123456789AB");
        private UUID MY_UUID_PACKAGE = UUID.fromString("91680004-1111-6666-8888-0123456789AB");

        private static final int DATA_PACKAGE_COUNT = 0x01; //每次循环发送数据包的个数
        private static final int DATA_PACKAGE_SIZE = 20;    //每个数据中的字节数

        private BluetoothGatt bluetoothGatt;
        private BluetoothGattCharacteristic characteristicWrite;
        private BluetoothGattCharacteristic characteristicPackage;
        private final BluetoothGattCallback gattCallback = new MyBluetoothGattCallback();

        private int versionCode;
        private boolean isConnecting;
        private int connectState = 0;   //连接状态

        private byte[] OtaFileBuffer;
        private int fileSize = 0;
        private int filePosition = 0;

        private int packageCount = 0;
        private int packageSize = 0;    //实际发送ota数据包时包的长度


        private boolean otaInitSuccess = false;     //OTA初始化是否完成
        private boolean prepareReceive = false;     //设置是否已经准备好开始接受数据
        private boolean isFinished = false;         //升级文件是否完全发送完成
        private boolean isReceived = false;         //设备是否收到发送的字节数据
        private boolean isSendSuccess = false;      //android系统层是否发送数据成功

        private boolean isUpdating = false;         //是否正在升级


        private static final int STATE_CONNECT_FAIL = -1;
        private static final int STATE_CONNECT_SUCCESS = 1;

        /**
         * 查询设备的版本信息
         */
        int queryVersionCode(BluetoothDevice device) {

            versionCode = 0;
            connectState = 0;
            isConnecting = true;

            //1.打开GATT
            device.connectGatt(OtaService.this, false, gattCallback);

            //2.等待连接成功
            int connectCount = 0;
            while (isConnecting) {

                if (connectState == STATE_CONNECT_FAIL) {
                    return 0;
                }

                try {
                    Thread.sleep(200);
                    connectCount++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //十秒钟没有连接成功，则放弃
                if (connectCount * 200 > 10000) {
                    return 0;
                }
            }

            //3.查询手柄版本信息
            queryGamePadVersion();

            //4.等待查询结果
            int versionCount = 0;
            while (versionCode <= 0) {
                try {
                    Thread.sleep(200);
                    versionCount++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //十秒钟没有查询到版本号，则放弃
                if (versionCount * 200 > 10000) {
                    return 0;
                }
            }

            close();   //断开连接

            return versionCode;
        }

        /**
         * BLE 手柄升级
         *
         * @param device 被升级的设备
         */
        public void update(BluetoothDevice device, DeviceInfo deviceInfo) {
            isUpdating = true;
            isConnecting = true;

            connectState = 0;   //重新连接
            //1.打开GATT连接
            if (bluetoothGatt != null) {
                bluetoothGatt.disconnect();
                bluetoothGatt.discoverServices();
            }
            device.connectGatt(OtaService.this, false, gattCallback);

            Intent intent = new Intent(ACTION_OTA_UPDATING);
            intent.putExtra("title", "升级中");
            intent.putExtra("subtitle", "正在连接设备...");
            intent.putExtra("isUpdating", true);
            intent.putExtra("progress", 100);
            sendBroadcast(intent);

            //2.等待连接成功
            int connectCount = 0;
            while (isConnecting) {
                if (connectState == STATE_CONNECT_FAIL) {
                    isUpdating = false;
                    intent.putExtra("title", "升级失败");
                    intent.putExtra("subtitle", "无法连接设备");
                    intent.putExtra("isUpdating", isUpdating);
                    intent.putExtra("progress", 100);
                    sendBroadcast(intent);
                    return;
                }

                try {
                    Thread.sleep(200);
                    connectCount++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //十秒钟没有连接成功，则放弃
                if (connectCount * 200 > 10000) {
                    isUpdating = false;
                    intent.putExtra("title", "升级失败");
                    intent.putExtra("subtitle", "无法连接设备");
                    intent.putExtra("isUpdating", isUpdating);
                    intent.putExtra("progress", 100);
                    sendBroadcast(intent);
                    return;
                }
            }

            //3.下载OTA升级包
            File file = loadOTA(deviceInfo.getOtaUrl(), deviceInfo.getOtaMd5());
            if (file == null || !file.isFile() || file.length() <= 0) {
                isUpdating = false;
                intent.putExtra("title", "升级失败");
                intent.putExtra("subtitle", "下载ota文件失败");
                intent.putExtra("isUpdating", isUpdating);
                intent.putExtra("progress", 100);
                sendBroadcast(intent);
                return;
            }

            //4.1 启动bootLoader
            startBootloader();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //4.2 初始化OTA操作
            int initRes = initOTA(file);
            if (initRes != 0) {
                isUpdating = false;
                return;
            }

            //等待初始化OTA完成
            int otaInitCount = 0;
            while (!otaInitSuccess) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isUpdating = true;
                intent.putExtra("title", "升级中");
                intent.putExtra("subtitle", "初始化OTA...");
                intent.putExtra("isUpdating", true);
                intent.putExtra("progress", 100);
                sendBroadcast(intent);

                Log.e(TAG, "-----------------------------------------------正在初始化OTA");
                otaInitCount++;
                if (otaInitCount * 200 > 12000) {

                    isUpdating = false;
                    intent.putExtra("title", "升级失败");
                    intent.putExtra("subtitle", "初始化OTA失败");
                    intent.putExtra("isUpdating", false);
                    intent.putExtra("progress", 100);
                    sendBroadcast(intent);

                    Log.e(TAG, "-----------------------------------------------初始化OTA失败");
                    return;
                }
            }

            //4.3 开始发送OTA数据包
            startSendData();

        }

        /***
         * 断开BLE连接，释放资源
         */
        private void close() {

            if (bluetoothGatt != null) {
                bluetoothGatt.disconnect();
                bluetoothGatt.close();
                bluetoothGatt = null;
            }
            characteristicWrite = null;
        }

        /**
         * 1.启动bootLoader
         */
        private void startBootloader() {

            Intent intent = new Intent(ACTION_OTA_UPDATING);
            intent.putExtra("title", "升级中");
            intent.putExtra("subtitle", "启动Bootloader...");
            intent.putExtra("isUpdating", true);
            intent.putExtra("progress", 100);
            sendBroadcast(intent);

            byte[] bytes = {0x20, 0x03, (byte) 0x81};
            writeMsgCharacteristic(bytes);
        }

        /**
         * 2.初始化OTA操作
         */
        private int initOTA(File file) {

            otaInitSuccess = false;

            Intent intent = new Intent(ACTION_OTA_UPDATING);
            intent.putExtra("title", "升级中");
            intent.putExtra("subtitle", "初始化OTA...");
            intent.putExtra("isUpdating", true);
            intent.putExtra("progress", 100);
            sendBroadcast(intent);

            FileInputStream fis = null;
            try {
                try {

                    //1.将文件读入byte数组
                    long length = file.length();
                    fis = new FileInputStream(file);
                    if (length > Integer.MAX_VALUE) {
                        Log.d(TAG, "---------->>>>> 文件太大，无法升级！");
                        isUpdating = false;
                        intent.putExtra("title", "升级失败");
                        intent.putExtra("subtitle", "ota文件异常");
                        intent.putExtra("isUpdating", false);
                        intent.putExtra("progress", 100);
                        sendBroadcast(intent);

                        return -1;
                    }
                    fileSize = (int) length;
                    byte[] fileBuffer = new byte[fileSize];
                    int offset = 0;
                    int numRead;
                    while (offset < fileBuffer.length && (numRead = fis.read(fileBuffer, offset, fileBuffer.length - offset))
                            >= 0) {
                        offset += numRead;
                    }

                    // 确保所有数据均被读取
                    if (offset != fileBuffer.length) {
                        throw new IOException("Could not completely read file " + file.getName());
                    }

                    OtaFileBuffer = Arrays.copyOf(fileBuffer, fileBuffer.length);

                    //2.计算文件的crc8值
                    byte crcByte = getCrc8(OtaFileBuffer);
                    Log.e(TAG, "----------------------------------------------->>> CRC值 ： " + crcByte);

                    //4.创建指令消息
                    byte[] msgBytes = {0x20, 0x0b, (byte) 0x82, 0x04, 0x00, 0x00, 0x00, 0x00, crcByte, 0x00,
                            DATA_PACKAGE_COUNT}; //0x0a每收到10个数据包发送一个notify

                    byte[] fileLength = int2byte((int) length);
                    if (fileLength.length == 4) {
                        for (int i = 0; i < 4; i++) {
                            msgBytes[i + 4] = fileLength[i];
                        }
                    } else {
                        isUpdating = false;
                        intent.putExtra("title", "升级失败");
                        intent.putExtra("subtitle", "ota文件异常");
                        intent.putExtra("isUpdating", isUpdating);
                        intent.putExtra("progress", 100);
                        sendBroadcast(intent);
                        return -1;
                    }

                    //5.发送初始化指令
                    writeMsgCharacteristic(msgBytes);

                } finally {
                    if (fis != null)
                        fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();

                isUpdating = false;
                intent.putExtra("title", "升级失败");
                intent.putExtra("subtitle", "初始化OTA失败");
                intent.putExtra("isUpdating", isUpdating);
                intent.putExtra("progress", 100);
                sendBroadcast(intent);

                return -1;
            }
            return 0;
        }

        private byte[] receivedByte;

        /**
         * 3. 开始传输文件
         */
        private void startSendData() {

            Intent intent = new Intent(ACTION_OTA_UPDATING);

            receivedByte = new byte[OtaFileBuffer.length];

            packageCount = 0;
            filePosition = 0;

            //1.通知设备开始接收数据
            notifyReceiveFile(filePosition);

            // 等待设备准备接受数据包
            int prepareCount = 0;
            while (!prepareReceive) {
                Log.d(TAG, "-------------------->>>>>>>>> 发申请 等待设备响应中...");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                prepareCount++;
                if (prepareCount * 200 > 10000) {  //如果设备超时，无法开始后续操作，升级失败

                    isUpdating = false;
                    intent.putExtra("title", "升级失败");
                    intent.putExtra("subtitle", "复制文件失败");
                    intent.putExtra("isUpdating", false);
                    intent.putExtra("progress", 100);
                    sendBroadcast(intent);

                    Log.e(TAG, "------------>>>>>  设备未响应，复制文件失败");
                    return;
                }
            }


            //2.开始循环发送ota数据包
            int count = 0;  //用来判断超时的计数器
            isFinished = false; //刚开始下载，未完成
            isSendSuccess = true;
            isReceived = true;
            while (!isFinished) {

                if (OtaFileBuffer == null || filePosition >= OtaFileBuffer.length) {
                    isFinished = true;
                    isUpdating = false;
                    return;
                }

                if (isSendSuccess && isReceived) {
                    count = 0;
                    sendOtaDataPackage(filePosition); //发送数据包
                } else {

                    count++;
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (count * 30 > 10000) {
                        count = 0;
                        //超时时，如果协议层没有收到数据才进行重传
                        if (!isSendSuccess && !isReceived) {

                            sendOtaDataPackage(filePosition);

                        } else {
                            Log.e(TAG, "-----------------------------------------丢包了，数据传输失败：" + filePosition);
                            isUpdating = false;
                            isFinished = true;
                            return;
                        }

                    }
                }
            }
            isUpdating = false;
        }

        /**
         * 3.通知设备接收固件文件
         *
         * @param offset 文件开始位置，用于丢包重传
         */
        private void notifyReceiveFile(int offset) {

            prepareReceive = false;
            byte[] msgBytes = {0x20, 0x07, (byte) 0x83, 0x00, 0x00, 0x00, 0x00};

            if (offset > 0) {
                byte[] offsets = int2byte(offset);
                for (int i = 0; i < 4; i++) {
                    msgBytes[i + 3] = offsets[i];
                }
            }
            writeMsgCharacteristic(msgBytes);
        }

        /**
         * 3.1发送OTA固件文件
         *
         * @param offset 文件开始位置 从0开始
         */
        private void sendOtaDataPackage(int offset) {

            if (OtaFileBuffer == null || OtaFileBuffer.length <= 0) {
                Log.e(TAG, "-------------->>> 没有文件数据可以发送");
                return;
            }

            if (offset >= OtaFileBuffer.length) {
                Log.e(TAG, "-------------->>> 文件已经发送完");
                return;
            }

            isReceived = false;
            isSendSuccess = false;

            byte[] buffer;
            int end = (offset + DATA_PACKAGE_SIZE > OtaFileBuffer.length) ? OtaFileBuffer.length : offset + DATA_PACKAGE_SIZE;
            //每次向外写20个字节数据
            buffer = Arrays.copyOfRange(OtaFileBuffer, offset, end);

            for (int i = 0; i < buffer.length; i++) {
                receivedByte[offset + i] = buffer[i];
            }

            packageSize = buffer.length;
            writeDataCharacteristic(buffer);
            //Log.e(TAG,"-------->>>>> 发送包的次数 packageCount ："+ (++packageCount)+" offset: "+ offset + " packageSize: "+buffer
            // .length);

            if (isWriteLogFile) {
                writeLogData(buffer); //写日志数据到文件
            }

        }


        /**
         * .激活新固件并复位
         */
        private void invokeAndResetOta() {
            byte[] msgBytes = {0x20, 0x03, (byte) 0x86};
            writeMsgCharacteristic(msgBytes);
        }

        /**
         * 1.查询手柄信息
         */
        private void queryGamePadVersion() {
            byte[] msgBytes = {0x20, 0x03, (byte) 0xf1};
            writeMsgCharacteristic(msgBytes);
        }

        /**
         * 蓝牙设备连接的回调类，用于接收手柄的数据响应
         */
        class MyBluetoothGattCallback extends BluetoothGattCallback {

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d(TAG, "------------>>>> connect 成功");

                    bluetoothGatt = gatt;

                    //连接成功后就去找出该设备中的服务
                    gatt.discoverServices();

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d(TAG, "------------>>>> connect 断开");
                    connectState = STATE_CONNECT_FAIL; //连接失败
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {

                if (status == BluetoothGatt.GATT_SUCCESS) {

                    //1.获取设备上的服务器
                    BluetoothGattService gattService = gatt.getService(MY_UUID_SERVER);

                    if (gattService != null) {

                        //2.设置监听notify，用于接收服务器发回的notify消息
                        BluetoothGattCharacteristic characteristicNotify = gattService.getCharacteristic(MY_UUID_NOTIFY);
                        setCharacteristicNotification(characteristicNotify, true);
                        //3.创建消息写特性
                        characteristicWrite = gattService.getCharacteristic(MY_UUID_WRITE);
                        //4.创建数据写特性
                        characteristicPackage = gattService.getCharacteristic(MY_UUID_PACKAGE);

                    } else {
                        connectState = STATE_CONNECT_FAIL; //连接失败
                    }

                } else {
                    connectState = STATE_CONNECT_FAIL; //连接失败
                }

            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    //Log.d(TAG,"---------->>> 我发出的命令 value: "+characteristic.getValue()[2]);

                    //TODO 判断发送的是不是OTA文件数据
                    if (characteristic.getValue().length == packageSize) {
                        //Log.d(TAG,"--------------->>>> 发送的数据指令");
                        isSendSuccess = true;
                        filePosition += 20;
                    }

                } else {
                    Log.d(TAG, "---------->>> 写出数据失败");
                }

            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {

                if (CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID.toString().equals(descriptor.getUuid().toString())) {
                    broadcastUpdate(ACTION_GATT_NOTIFY_ENABLE); //notify开关成功打开
                    Log.d(TAG, "---------------->>> 设置监听 成功");

                    isConnecting = false;
                    connectState = STATE_CONNECT_SUCCESS; //连接成功
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

                //此处用于接收设备回传的Notify消息
                byte[] bytes = characteristic.getValue();
                if (bytes != null && bytes.length > 0) {

                    Intent intent = new Intent(ACTION_OTA_UPDATING);
                    switch (bytes[2]) {

                        case (byte) 0x82:

                            Log.d(TAG, "-------->>>>> 初始化OTA ：" + bytes[3]);

                            if (bytes[3] == 0) {

                                otaInitSuccess = true;

                            } else {

                                otaInitSuccess = false;

                                isUpdating = false;
                                intent.putExtra("title", "升级失败");
                                intent.putExtra("subtitle", "初始化OTA失败");
                                intent.putExtra("isUpdating", false);
                                intent.putExtra("progress", 100);
                                sendBroadcast(intent);
                            }

                            break;
                        case (byte) 0x83:

                            if (bytes[3] == 0) {
                                prepareReceive = true;
                                Log.d(TAG, "-------->>>>>>>> 接受申请，你可以发数据了");
                            } else {
                                prepareReceive = false;
                                isUpdating = false;
                                Log.d(TAG, "-------->>>>>>>> 我不想接收数据");
                            }

                            break;
                        case (byte) 0x84:

                            //Log.d(TAG,"-------->>>>>>>> 已接收的字节数 84 :"+bytes[3] + " "+bytes[4] + " "+bytes[5] + " "+bytes[6]);

                            isReceived = true;
                            isUpdating = true;

                            byte[] received = {bytes[3], bytes[4], bytes[5], bytes[6]};
                            int receivedOffset = byte2int(received);

                            //Log.d(TAG,"-------->>>>>>>> 已接收的字节数 84 : receivedOffset " + receivedOffset + " fileSize
                            // "+fileSize);
                            int progress = (int) ((float) receivedOffset / (float) fileSize * 100);

                            intent.putExtra("title", "升级中");
                            intent.putExtra("subtitle", "正在下载文件: " + progress + "%");
                            intent.putExtra("isUpdating", true);
                            intent.putExtra("progress", progress);
                            sendBroadcast(intent);
                            if (progress >= 100) {
                                isFinished = true;
                                isUpdating = false;
                            }

                            break;
                        case (byte) 0x85:

                            closeLogFile();

                            Log.e(TAG, "-------->>>>> 固件传送完成 85 ：" + bytes[3]);

                            byte b = getCrc8(receivedByte);
                            boolean b2 = Arrays.equals(OtaFileBuffer, receivedByte);
                            Log.d(TAG, "------------------>>>>>>>>> 传送数据的crc8: " + b + " 相等：" + b2);

                            isFinished = true;
                            isUpdating = false;

                            if (bytes[3] == 0) {

                                intent.putExtra("title", "升级成功");
                                intent.putExtra("subtitle", "");
                                intent.putExtra("isUpdating", false);
                                intent.putExtra("isFinished", true);
                                intent.putExtra("progress", 100);
                                sendBroadcast(intent);

                                invokeAndResetOta();//激活新固件,并复位
                            } else {

                                intent.putExtra("title", "升级失败");
                                intent.putExtra("subtitle", "数据传输错误");
                                intent.putExtra("isUpdating", false);
                                intent.putExtra("progress", 100);
                                sendBroadcast(intent);

                                /*byte b = getCrc8(receivedByte);
                                boolean b2 = Arrays.equals(OtaFileBuffer,receivedByte);
                                Log.d(TAG,"------------------>>>>>>>>> 传送数据的crc8: "+b+" 相等："+b2);*/
                            }

                            if (deviceInfoList != null) {
                                deviceInfoList.clear();
                            }

                            break;

                        case (byte) 0xf1:

                            Log.e(TAG, "-------->>>>> 手柄版本信息 ：" + bytes[3] + " " + bytes[4] + " " + bytes[5] + " " + bytes[6]);

                            //新版本信息
                            byte[] versionByte = new byte[]{0x00, 0x00, bytes[5], bytes[6]};
                            versionCode = byte2int(versionByte);

                            break;
                    }
                }
            }
        }

        /**
         * 向外围设备发送指令包
         *
         * @param bytes 发送指令的字节数组
         */
        private void writeMsgCharacteristic(byte[] bytes) {
            if (characteristicWrite == null) {
                Log.d(TAG, "------------->>>> 写特性为空 无法发送指令");
                return;
            }
            characteristicWrite.setValue(bytes);
            bluetoothGatt.writeCharacteristic(characteristicWrite);
        }

        /**
         * 向外围设备发送数据包
         *
         * @param bytes 数据包字节数组
         */
        private void writeDataCharacteristic(byte[] bytes) {
            if (characteristicPackage == null) {
                Log.d(TAG, "------------->>>> 写特性为空 无法发送数据");
                return;
            }
            characteristicPackage.setValue(bytes);
            bluetoothGatt.writeCharacteristic(characteristicPackage);
        }

        /**
         * 设置对蓝牙设备发送Notify消息的监听器
         * Enables or disables notification on a give characteristic.
         *
         * @param characteristic Characteristic to act on.
         * @param enabled        If true, enable notification.  False otherwise.
         */
        private void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {

            if (bluetoothAdapter == null || bluetoothGatt == null) {
                Log.w(TAG, "BluetoothAdapter not initialized");
                return;
            }
            bluetoothGatt.setCharacteristicNotification(characteristic, enabled);

            //打开蓝牙设备的notify开关，不打开无法收到设备回传的notify信息
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID);
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                characteristic.addDescriptor(descriptor);
                bluetoothGatt.writeDescriptor(descriptor);
            }
        }

    }

    /**
     * 经典蓝牙通信服务类
     *
     * @author flan
     * @since 2016-09-20
     */
    public class ClassicService {

        private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        private static final int DATA_PACKAGE_SIZE = 50;

        private BluetoothSocket socket;

        /**
         * 查询设备的版本信息
         */
        int queryVersionCode(BluetoothDevice device) {

            Log.d(TAG, "---------------->>>>> 正在检测版本号: " + device.getAddress() + " ---- " + device.getName());

            InputStream is = null;
            OutputStream os = null;

            byte[] buffer = new byte[30];

            try {
                try {
                    socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    socket.connect();

                    is = socket.getInputStream();
                    os = socket.getOutputStream();

                    //查询设备信息指令
                    byte[] versionCommand = new byte[]{0x4e, 0x47, 0x10, 0x05, 0x06};

                    os.write(versionCommand);

                    int b = is.read(buffer);
                    Log.d(TAG, "--------------------->>>>>>>> byte " + b);
                    Log.e(TAG, "----->>> " + bytesToHexString(buffer));

                } finally {
                    if (is != null)
                        is.close();
                    if (os != null)
                        os.close();
                    if (socket != null)
                        socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }

            byte[] versionByte = {0x00, 0x00, buffer[23], buffer[24]};
            int versionInt = byte2int(versionByte);
            Log.d(TAG, "--------------------->>>>>>>> class versionCode " + versionInt);

            return versionInt;
        }

        /**
         * 升级手柄设备
         *
         * @param device 被升级的设备
         */
        public void update(BluetoothDevice device, DeviceInfo deviceInfo) {
            if (isWriteLogFile) {
                initOutPutLogFile();
            }

            isUpdating = true;

            Log.e(TAG, "------------------>>>> 我在进行升级");

            //1.下载OTA文件
            File file = loadOTA(deviceInfo.getOtaUrl(), deviceInfo.getOtaMd5());

            Intent intent = new Intent(ACTION_OTA_UPDATING);
            if (file == null || file.length() <= 0) {
                isUpdating = false;
                intent.putExtra("title", "升级失败");
                intent.putExtra("subtitle", "下载ota文件失败");
                intent.putExtra("progress", 100);
                intent.putExtra("isUpdating", isUpdating);
                sendBroadcast(intent);
                return;
            }

            FileInputStream fis = null;
            OutputStream os = null;
            InputStream is = null;


            try {
                try {

                    intent.putExtra("title", "升级中");
                    intent.putExtra("subtitle", "正在初始化...");
                    intent.putExtra("progress", 100);
                    intent.putExtra("isUpdating", true);
                    sendBroadcast(intent);

                    //2.将OTA文件读入数组，计算crc8值
                    long length = file.length();
                    fis = new FileInputStream(file);
                    if (length > Integer.MAX_VALUE) {
                        Log.d(TAG, "---------->>>>> 文件太大，无法升级！");
                        isUpdating = false;
                        intent.putExtra("title", "升级失败");
                        intent.putExtra("subtitle", "ota文件异常");
                        intent.putExtra("progress", 100);
                        intent.putExtra("isUpdating", isUpdating);
                        sendBroadcast(intent);
                        return;
                    }

                    int fileSize = (int) length;
                    byte[] fileBuffer = new byte[fileSize];
                    int offset = 0;
                    int numRead;
                    while (offset < fileBuffer.length && (numRead = fis.read(fileBuffer, offset, fileBuffer.length - offset))
                            >= 0) {
                        offset += numRead;
                    }

                    // 确保所有数据均被读取
                    if (offset != fileBuffer.length) {
                        throw new IOException("Could not completely read file " + file.getName());
                    }

                    //3.计算文件的crc8值
                    byte crcByte = getCrc8(fileBuffer);
                    Log.e(TAG, "----------------------------------------------->>> CRC值 ： " + crcByte);

                    //4.创建初始化OTA命令
                    byte[] initCommand = {0x4e, 0x47, 0x10, 0x0a, 0x09, 0x00, 0x00, 0x00, 0x00, crcByte};
                    byte[] fileLengthByte = int2byte((int) length);
                    if (fileLengthByte.length == 4) {
                        for (int i = 0; i < 4; i++) {
                            initCommand[i + 5] = fileLengthByte[i];
                        }
                        Log.d(TAG, "-------------->>>>> 初始化命令：" + bytesToHexString(initCommand));
                    } else {
                        Log.d(TAG, "-------------->>>>> OTA文件长度异常");

                        isUpdating = false;
                        intent.putExtra("title", "升级失败");
                        intent.putExtra("subtitle", "文件传输失败");
                        intent.putExtra("progress", 100);
                        intent.putExtra("isUpdating", isUpdating);
                        sendBroadcast(intent);
                        return;
                    }

                    //5.打开手机与设备连接
                    try {
                        socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                        socket.connect();
                        os = socket.getOutputStream();
                        is = socket.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();

                        isUpdating = false;
                        intent.putExtra("title", "升级失败");
                        intent.putExtra("subtitle", "设备未连接");
                        intent.putExtra("progress", 100);
                        intent.putExtra("isUpdating", isUpdating);
                        sendBroadcast(intent);
                        return;
                    }
                    //发送OTA升级命令
                    os.write(initCommand);

                    //等待5秒钟,进入升级模式
                    try {
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //5.创建并发送升级文件数据包
                    byte[] dataPackageHead = {0x4e, 0x47, 0x10, 0x00, 0x0a, 0x00, 0x00, 0x00, 0x00};
                    int position = 0;
                    int packageNum = 1;
                    while (position < fileSize) {

                        isUpdating = true;
                        byte[] tempData;
                        int end = (position + DATA_PACKAGE_SIZE > fileBuffer.length) ? fileBuffer.length : position +
                                DATA_PACKAGE_SIZE;
                        tempData = Arrays.copyOfRange(fileBuffer, position, end);

                        byte[] dataPackage = new byte[dataPackageHead.length + (end - position)];
                        System.arraycopy(dataPackageHead, 0, dataPackage, 0, dataPackageHead.length);
                        System.arraycopy(tempData, 0, dataPackage, dataPackageHead.length, tempData.length);

                        //设置包大小
                        dataPackage[3] = (byte) dataPackage.length;

                        //设置包序号
                        byte[] packageNumByte = int2byte(packageNum);
                        //byte[] packageNumByte = int2byte(position);
                        for (int i = 0; i < packageNumByte.length; i++) {
                            dataPackage[5 + i] = packageNumByte[i];
                        }

                        //发送数据包
                        os.write(dataPackage);
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (isWriteLogFile) {
                            writeLogData(dataPackage);  //写日志到文件
                        }

                        position += DATA_PACKAGE_SIZE; //执行累加，用于发后续数据

                        //Log.d(TAG,"--->>>> packageCount "+packageNum);
                        packageNum++; //包序号累加

                        double progress = (double) position / (double) fileSize * 100;
                        intent.putExtra("title", "升级中");
                        intent.putExtra("subtitle", "正在更新文件...");
                        intent.putExtra("isUpdating", true);
                        intent.putExtra("progress", (int) progress);
                        sendBroadcast(intent);
                    }

                    closeLogFile();//关闭日志

                    //6.创建并发送复位命令
                    //byte[] resetCommand = {0x4e,0x47,0x10,0x06,0x0b,0x00};
                    //os.write(resetCommand);

                    byte[] resByte = new byte[30];
                    is.read(resByte);
                    Log.d(TAG, "--->> " + bytesToHexString(resByte));

                    //文件复制完成，升级成功
                    isUpdating = false;
                    intent.putExtra("title", "升级成功");
                    intent.putExtra("subtitle", "");
                    intent.putExtra("progress", 100);
                    intent.putExtra("isUpdating", isUpdating);
                    intent.putExtra("isFinished", true);
                    sendBroadcast(intent);

                    //TODO 重新检查版本信息
                    if (deviceInfoList != null) {
                        deviceInfoList.clear();
                    }

                } finally {
                    if (fis != null)
                        fis.close();
                    /*if(os != null)
                        os.close();
                    if(is != null)
                        is.close();
                    if(socket != null)
                        socket.close();*/
                }
            } catch (IOException e) {
                e.printStackTrace();

                isUpdating = false;
                intent.putExtra("title", "升级失败");
                intent.putExtra("subtitle", "文件传输失败");
                intent.putExtra("progress", 100);
                intent.putExtra("isUpdating", isUpdating);
                sendBroadcast(intent);
            }
        }

    }

    /**
     * 下载OTA固件包
     *
     * @param url 固件包下载地址
     * @param md5 固件包的MD5值
     * @return 下载失败返回-1
     */
    private File loadOTA(String url, String md5) {

        isUpdating = true;
        Intent intent = new Intent(ACTION_OTA_UPDATING);
        File file = null;
        try {

            file = new File(CommonUtil.getFileLoadBasePath());
            if (!file.exists()) {
                file.mkdirs();
            }

            file = new File(file, "ota.bin");
            if (file.exists() && file.isFile()) {
                file.delete();
            }

            OutputStream os = null;
            HttpURLConnection conn = null;
            try {

                URL imageUrl = new URL(url);
                conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);
                InputStream is = conn.getInputStream();
                os = new FileOutputStream(file);

                int fileLength = conn.getContentLength();
                int alreadyRead = 0;
                int buffer_size = 1024;
                byte[] bytes = new byte[buffer_size];
                for (; ; ) {//死循环
                    int count = is.read(bytes, 0, buffer_size);
                    if (count == -1)
                        break;

                    os.write(bytes, 0, count);

                    alreadyRead += count;

                    int progress = (int) ((float) alreadyRead / (float) fileLength * 100);

                    intent.putExtra("title", "升级中");
                    intent.putExtra("subtitle", "下载升级文件：" + progress + "%");
                    intent.putExtra("isUpdating", true);
                    intent.putExtra("progress", progress);
                    sendBroadcast(intent);

                }

                intent.putExtra("title", "升级中");
                intent.putExtra("subtitle", "正在校验文件...");
                intent.putExtra("isUpdating", true);
                intent.putExtra("progress", 100);
                sendBroadcast(intent);

                String currentMd5 = MD5.getFileMD5(file);
                if (!TextUtil.isEmpty(currentMd5) && currentMd5.equalsIgnoreCase(md5)) {
                    isUpdating = true;
                    intent.putExtra("title", "升级中");
                    intent.putExtra("subtitle", "校验成功");
                    intent.putExtra("isUpdating", isUpdating);
                    intent.putExtra("progress", 100);
                    sendBroadcast(intent);

                } else {
                    isUpdating = false;
                    intent.putExtra("title", "升级中");
                    intent.putExtra("subtitle", "下载文件失败");
                    intent.putExtra("isUpdating", isUpdating);
                    intent.putExtra("progress", 100);
                    sendBroadcast(intent);
                    return null;
                }

            } finally {
                if (os != null)
                    os.close();
                if (conn != null)
                    conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            isUpdating = false;
            intent.putExtra("title", "升级失败");
            intent.putExtra("subtitle", "下载文件失败");
            intent.putExtra("isUpdating", isUpdating);
            intent.putExtra("progress", 100);
            sendBroadcast(intent);
        }
        return file;
    }


    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }


    /**
     * Int型数据转byte[]
     */
    private static byte[] int2byte(int res) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (res & 0xff);// 最低位
        targets[2] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[1] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[0] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }

    /**
     * byte[]转int
     */
    private static int byte2int(byte[] res) {
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
        int targets = (res[3] & 0xff) | ((res[2] << 8) & 0xff00) | ((res[1] << 24) >>> 8) | (res[0] << 24);
        return targets;
    }

}
