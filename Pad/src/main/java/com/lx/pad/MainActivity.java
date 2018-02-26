package com.lx.pad;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.lx.pad.component.MyGameActivity;
import com.lx.pad.util.LLog;
import com.ngame.Utils.KeyMgrUtils;
import com.ngame.app.KeyboardEditActivity;
import com.ngds.pad.Msg.LooperEventManager;
import com.ngds.pad.PadServiceBinder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int MSG_CMD_START_KEYBOARD_EDIT_ACTIVITY = 1;   //启动按键设置activity的消息号
    public static final int MSG_CMD_BLE_CONNECT_STATE = 2;              //通知蓝牙连接状态
    public static final int MSG_CMD_REMOTE_SOCKET_STATE = 3;            //通知晨风射socket连接状态

    public static final int MSG_VAL_BLE_NONE = 0;
    public static final int MSG_VAL_BLE_CONNECTED = 1;

    public static final int MSG_VAL_SOCKET_CLOSE = 0;
    public static final int MSG_VAL_SOCKET_CONNECTED = 1;


    TextView tv = null;
    Button btnStartGame = null;
    Button btnStartService = null;
    Button btnTestKeyEditActivity = null;
    Button btnTestPromptView = null;
    Button btnTestMsgQueue = null;

    boolean bleConnect = false;
    boolean socketConnect = false;

    private static MainActivity activity = null;
    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {    //这个handler只是测试代码的，不
//            super.handleMessage(msg);
            if (activity != null) {
                if(msg.what == MSG_CMD_START_KEYBOARD_EDIT_ACTIVITY){
                    final KeyboardEditActivity ac = KeyboardEditActivity.getInstance();
                    if(ac == null || ac.isFinishing()) {
                        Intent intent = new Intent(activity, KeyboardEditActivity.class);
                        activity.startActivity(intent);
                    }else{
                        ac.finish();
                    }
                }else if(msg.what == MSG_CMD_BLE_CONNECT_STATE){
                    //ble state connected
    //                Toast.makeText(MyApplication.getContextObj(), "蓝牙连接成功!", Toast.LENGTH_LONG);
                    LLog.d("ble connect success! <<===========================");
                    if(msg.arg1 == MSG_VAL_BLE_CONNECTED) {
                        activity.bleConnect = true;
                    }else if(msg.arg1 == MSG_VAL_BLE_NONE){
                        activity.bleConnect = false;
                    }
                    activity.updateStatePrompt();
                }else if(msg.what == MSG_CMD_REMOTE_SOCKET_STATE){
                    if(msg.arg1 == MSG_VAL_SOCKET_CONNECTED){
                        activity.socketConnect = true;
                    }else if(msg.arg1 == MSG_VAL_SOCKET_CLOSE){
                        activity.socketConnect = false;
                    }
                    activity.updateStatePrompt();
                }
            }
        }
    };

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public static void sendHandleMsg(int cmd, int val){
        Message msg = new Message();
        msg.what = cmd;
        msg.arg1 = val;
        handler.sendMessage(msg);
    }

    public void updateStatePrompt(){
        String sBle = "蓝牙未连接";
        String sSocket = "InjectServer未运行";
        if(bleConnect){
            sBle = "蓝牙连接成功";
        }
        if(socketConnect){
            sSocket = "InjectServer开启成功";
        }

        tv.setText(sBle + " | " + sSocket);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //设置全屏
        setContentView(R.layout.activity_main);

        activity = this;

        // Example of a call to a native method
        tv = (TextView) findViewById(R.id.sample_text);
        btnStartGame = (Button)findViewById(R.id.btn_startGame);
        btnStartService = (Button)findViewById(R.id.btn_startService);
        btnTestKeyEditActivity = (Button)findViewById(R.id.btn_testKeyEditActivity);
        btnTestPromptView = (Button)findViewById(R.id.btn_testPromptView);
        btnTestMsgQueue = findViewById(R.id.btn_testMsgQueue);

//        looperThread = new LooperThread();
//        looperThread.start();
        LooperEventManager.init();

        updateStatePrompt();
//        tv.setText(stringFromJNI());
        btnStartGame.setOnClickListener(this);
        btnStartService.setOnClickListener(this);
        btnTestKeyEditActivity.setOnClickListener(this);
        btnTestPromptView.setOnClickListener(this);
        btnTestMsgQueue.setOnClickListener(this);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_startGame) {
            startGame();
        } else if (i == R.id.btn_startService) {
            PadServiceBinder.getInstance(this).initArgs("");
        } else if (i == R.id.btn_testKeyEditActivity) {
            Intent intent = new Intent(this, KeyboardEditActivity.class);
            startActivity(intent);
        } else if (i == R.id.btn_testPromptView) {
            KeyMgrUtils.promptFrameLayoutView(this);
        } else if (i == R.id.btn_testMsgQueue) {
        }
    }

    @Override
    protected void onDestroy() {
//        PadServiceBinder.getInstance(this).unRegisterAndUnbindService();
        super.onDestroy();
    }

    public void startGame(){
        //首先需要判断后台映射代码是否己启动

        //判断手柄是否己连接

        //开始游戏
        startActivity(new Intent(this, MyGameActivity.class));
    }

}
