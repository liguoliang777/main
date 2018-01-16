package cn.ngame.store.base.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.manager.FeedbackSucceedActivity;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.UpLoadBean;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.gamehub.bean.PictureBean;
import cn.ngame.store.gamehub.view.CommonBaseActivity;
import cn.ngame.store.util.CPUInformation;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.widget.BaseGridView;
import cn.ngame.store.widget.UploadFileHttp;
import cn.ngame.store.widget.mulpicture.MulPictureActivity;

/**
 * 显示意见反馈的页面
 * Created by zeng on 2016/5/30.
 */
public class FeedbackActivity extends CommonBaseActivity implements View.OnClickListener {

    public static final String TAG = FeedbackActivity.class.getSimpleName();
    private LinearLayout ll_back;
    private TextView tv_title, tv_rightTxt;
    private EditText et_content, et_contact;
    private TextView tv_content_num;
    private Button bt_submit;
    private String content;
    private String contact;
    private HashMap<String, File> files;
    BluetoothAdapter bluetoothAdapter;
    private String mtyb; //手机品牌
    private String mtype; //手机型号
    private String mtbbh; //系统版本
    private boolean flag; //是否支持OTG

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_feedback);

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_rightTxt = (TextView) findViewById(R.id.tv_rightTxt);
        tv_title.setText(getString(R.string.feedback));
        tv_rightTxt.setText("关闭");
        et_content = (EditText) findViewById(R.id.et_content);
        et_contact = (EditText) findViewById(R.id.et_contact);
        tv_content_num = (TextView) findViewById(R.id.tv_content_num);
        bt_submit = (Button) findViewById(R.id.btn_submit);
        iv_upload = (ImageView) findViewById(R.id.iv_upload);
        gridView = (BaseGridView) findViewById(R.id.horizontal_gridview);
        tv_info = (TextView) findViewById(R.id.tv_info);
        ll_back.setOnClickListener(this);
        tv_rightTxt.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        iv_upload.setOnClickListener(this);
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_content_num.setText("可输入" + (400 - s.length() + "字"));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mtyb = android.os.Build.BRAND; //手机品牌
        mtype = android.os.Build.MODEL; // 手机型号
        mtbbh = android.os.Build.VERSION.RELEASE; //获取版本号
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.getName();
        flag = getPackageManager().hasSystemFeature("androi.hardware.usb.host");
    }

    private String getCpuInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("android.os.Build.CPU_ABI:" + CPUInformation.getCPU_ABI() + " ");
        sb.append("android.os.Build.CPU_ABI2:" + CPUInformation.getCPU_ABI2() + " ");
        sb.append("CPU_FAMILY:" + CPUInformation.getCPU_FAMILY() + " ");
        sb.append("CPU_ARCHITECTURE:" + CPUInformation.getCPU_ARCHITECTURE() + "");
        return sb.toString();
    }

//    /**
//     * 手机CPU信息
//     */
//    private String[] getCpuInfo() {
//        String str1 = "/proc/cpuinfo";
//        String str2 = "";
//        String[] cpuInfo = {"", ""};  //1-cpu型号  //2-cpu频率
//        String[] arrayOfString;
//        try {
//            FileReader fr = new FileReader(str1);
//            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
//            str2 = localBufferedReader.readLine();
//            arrayOfString = str2.split("\\s+");
//            for (int i = 2; i < arrayOfString.length; i++) {
//                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
//            }
//            str2 = localBufferedReader.readLine();
//            arrayOfString = str2.split("\\s+");
//            cpuInfo[1] += arrayOfString[2];
//            localBufferedReader.close();
//        } catch (IOException e) {
//        }
//        Log.i("text", "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
//        return cpuInfo;
//    }

//    /**
//     * 显示注册结果对话框
//     */
//    private void showDialog(final boolean isSuccess, String msg) {
//
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//
//        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
//        dialogFragment.setTitle("提示框");
//        dialogFragment.setDialogWidth(250);
//
//        TextView tv = new TextView(FeedbackActivity.this);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        params.setMargins(0, 20, 0, 0);
//        params.gravity = Gravity.CENTER;
//        tv.setLayoutParams(params);
//        tv.setGravity(Gravity.CENTER);
//        tv.setText(msg);
//        tv.setTextColor(getResources().getColor(R.color.black));
//        dialogFragment.setContentView(tv);
//
//        dialogFragment.setNegativeButton(R.string.sure, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogFragment.dismiss();
//
//                if (isSuccess) {
//                    finish();
//                }
//            }
//        });
//        dialogFragment.show(ft, "successDialog");
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
            case R.id.tv_rightTxt:
                this.finish();
                break;
            case R.id.btn_submit:
                content = et_content.getText().toString();
                contact = et_contact.getText().toString();
                if (content == null || content.length() <= 0) {
                    Toast.makeText(FeedbackActivity.this, "请提出您的宝贵意见！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (content.length() < 5) {
                    Toast.makeText(FeedbackActivity.this, "请至少输入5个字符", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (contact != null && contact.length() > 0) {
                    if (!TextUtil.isMobile(contact)) {
                        Toast.makeText(FeedbackActivity.this, "请填写正确的手机号！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                submitMsg();
                break;
            case R.id.iv_upload:
                int choose = 9 - pictures.size();
                if (choose > 0) {
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        intent = new Intent(this, MulPictureActivity.class);
                        bundle = setBundle();
                        bundle.putInt("imageNum", choose);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 101);
                    } else {
                        MPermissions.requestPermissions(this, MulPictureActivity.SDCARD_READ, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                } else {
                    ToastUtil.show(this, "最多只能选择1张照片");
                }
                break;
        }
    }

    /**
     * 图片上传+提交数据
     */
    public void submitMsg() {
        DialogHelper.showWaiting(getSupportFragmentManager(), "提交中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    files = new HashMap<String, File>();
                    if (pictures != null) {
                        for (PictureBean pictureBean : pictures) {
                            File file = new File(pictureBean.getLocalURL());
                            files.put(file.getName(), file);
                        }
                    }
                    if(files.size() > 0){
                        UpLoadBean result = UploadFileHttp.INSTANCE.uploadFile(Constant.WEB_SITE + Constant.URL_FEEDBACK_FILE, files);
                        if (result == null) {
                            return;
                        }
                        if (result.getCode() == 0) {
                            sendHandle(result.getData(), 1);
                        } else {
                            DialogHelper.hideWaiting(getSupportFragmentManager());
                            sendHandle("", 0);
                        }
                    } else {
                        sendHandle("", 1);
                    }
                } catch (Exception e) {
                    DialogHelper.hideWaiting(getSupportFragmentManager());
                }
            }
        }).start();
    }

    public void sendHandle(String message, int success) {
        Looper.prepare();
        Message msg = new Message();
        msg.what = success;
        msg.obj = message;
        handler.sendMessage(msg);
        Looper.loop();
    }

    /**
     * 更新ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    doFeedback(ConvUtil.NS(msg.obj), content, contact);
                    break;
                case 0:
                    DialogHelper.hideWaiting(getSupportFragmentManager());
                    ToastUtil.show(FeedbackActivity.this, ConvUtil.NS(msg.obj));
                    break;
            }
            pictures.clear();
        }
    };

    /**
     * 发送反馈意见
     *
     * @param file
     * @param content
     * @param contact
     */
    private void doFeedback(final String file, final String content, final String contact) {

        String url = Constant.WEB_SITE + Constant.URL_FEEDBACK;
        Response.Listener<JsonResult> successListener = new Response.Listener<JsonResult>() {
            @Override
            public void onResponse(JsonResult result) {
                DialogHelper.hideWaiting(getSupportFragmentManager());
                if (result == null) {
                    Toast.makeText(FeedbackActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.code == 0) {
                    startActivity(new Intent(FeedbackActivity.this, FeedbackSucceedActivity.class));
                    FeedbackActivity.this.finish();
                } else {
                    Log.d(TAG, "服务器返回错误：" + result.msg);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                DialogHelper.hideWaiting(getSupportFragmentManager());
                Toast.makeText(FeedbackActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult> versionRequest = new GsonRequest<JsonResult>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("appTypeId", "0");
                params.put("content", content);
                params.put("contact", contact);
                params.put("feedbackImage", file);
                params.put("token", StoreApplication.token);
                params.put("brand", mtyb);
                params.put("model", mtype);
                params.put("system", mtbbh);
                params.put("cpu", ConvUtil.NS(getCpuInfo()));
                params.put("bluetooth", "4.0");
                params.put("isSupportOTG", ConvUtil.NS(flag == true ? 1 : 0));
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (arg0 == 101) {
            if (arg1 == 1) {
                iv_upload.setVisibility(View.GONE);
                tv_info.setVisibility(View.GONE);
            } else if (arg1 > 0) {
                tv_info.setVisibility(View.GONE);
            } else {
                iv_upload.setVisibility(View.VISIBLE);
//                tv_info.setVisibility(View.VISIBLE);
            }
            setIntent(arg2);
            getBundle();
            setGridView();
        }
        super.onActivityResult(arg0, arg1, arg2);
    }

    //读取手机存储
    @PermissionGrant(MulPictureActivity.SDCARD_READ)
    public void requestSdcardReadSuccess() {
        int choose = 1 - pictures.size();
        intent = new Intent(this, MulPictureActivity.class);
        bundle = setBundle();
        bundle.putInt("imageNum", choose);
        bundle.putInt("imageAllNum", 1);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        startActivityForResult(intent, 101);
    }

    //读取手机存储
    @PermissionDenied(MulPictureActivity.SDCARD_READ)
    public void requestSdcardReadFailed() {
        ToastUtil.show(this, "用户拒绝读写手机存储!");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right); //搜索页面退出动画
    }
}
