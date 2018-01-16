package cn.ngame.store.user.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.fragment.SimpleDialogFragment;
import cn.ngame.store.view.BaseTitleBar;

/**
 * 用户账号注册页面
 * Created by zeng on 2016/5/16.
 */
public class RegisterActivity extends BaseFgActivity {

    public static final String TAG = RegisterActivity.class.getSimpleName();
    private Timer timer = new Timer();
    private Handler handler = new Handler();

    private TextView tv_captcha;
    private Button bt_register;
    private ImageButton bt_show_pwd;
    private EditText et_name, et_pwd, et_captcha;

    private boolean isShowPwd = false;

    private static final int WAIT_TIME = 61;
    private int second = 60;

    /**
     * 执行倒计时操作
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < WAIT_TIME; i++) {
                if (second <= 1) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_captcha.setText(getResources().getString(R.string.register_get_captcha));
                            tv_captcha.setBackgroundResource(R.drawable.shape_bg_verif_code_bt_send);
                            tv_captcha.setClickable(true);
                            return;
                        }
                    });
                } else {
                    second--;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_captcha.setText("重新发送(" + second + "s)");
                            tv_captcha.setBackgroundResource(R.drawable.shape_bg_verif_code_bt_waiting);
                            tv_captcha.setClickable(false);
                        }
                    });
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        this.setContentView(R.layout.activity_register);

        BaseTitleBar titleBar = (BaseTitleBar) findViewById(R.id.title_bar);
        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_name = (EditText) findViewById(R.id.et_login_user);
        et_captcha = (EditText) findViewById(R.id.et_captcha);
        bt_show_pwd = (ImageButton) findViewById(R.id.bt_show_pwd);
        et_pwd = (EditText) findViewById(R.id.et_login_pwd);

        bt_register = (Button) findViewById(R.id.register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_name.getText().toString();
                String pwd = et_pwd.getText().toString();
                String captcha = et_captcha.getText().toString();

                if (userName == null && userName.equals("")) {
                    Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtil.isMobile(userName)) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (captcha == null || captcha.equals("")) {
                    Toast.makeText(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd == null || pwd.equals("")) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "密码要大于六位", Toast.LENGTH_SHORT).show();
                    return;
                }
                doRegister(userName, pwd, captcha);
            }
        });

        tv_captcha = (TextView) findViewById(R.id.tv_captcha);
        tv_captcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = et_name.getText().toString();
                if (mobile != null && !"".equals(mobile)) {
                    if (!TextUtil.isMobile(mobile)) {
                        Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getSMSCode(mobile);
                } else {
                    Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_show_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowPwd) {
                    isShowPwd = true;
                    bt_show_pwd.setSelected(true);
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().length());
                } else {
                    isShowPwd = false;
                    bt_show_pwd.setSelected(false);
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().length());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

      /**
     * 获取手机验证码
     */
    private void getSMSCode(final String mobile) {

        String url = Constant.WEB_SITE + Constant.URL_FORGOT_REGIST_SMS_CODE;
        Response.Listener<JsonResult<Object>> successListener = new Response.Listener<JsonResult<Object>>() {
            @Override
            public void onResponse(JsonResult result) {
                if (result == null) {
                    Toast.makeText(RegisterActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.code == 0) {
                    second = 60;
                    new Thread(runnable).start();

                    Toast.makeText(RegisterActivity.this, "验证码已发送，请查收！", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "HTTP请求成功：服务端返回：" + result.data);

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误：" + result.msg);
                    showDialog(false, result.msg);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Toast.makeText(RegisterActivity.this, "更新失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<Object>> versionRequest = new GsonRequest<JsonResult<Object>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.PHONE_NUMBER, mobile);
                params.put(KeyConstant.TYPE, "1");//type 短信类型（1注册，2忘记密码）
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    /**
     * 执行HTTP注册操作
     */
    private void doRegister(final String userName, final String pwd, final String captcha) {

        String url = Constant.WEB_SITE + Constant.URL_USER_REGISTER;
        Response.Listener<JsonResult> successListener = new Response.Listener<JsonResult>() {
            @Override
            public void onResponse(JsonResult result) {

                if (result == null) {
                    Toast.makeText(RegisterActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.code == 0) {
                    SharedPreferences preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Constant.CONFIG_USER_NAME, userName);
                    editor.apply();

                    showDialog(true, "恭喜您，注册成功！");
                } else {
                    showDialog(false, result.msg);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Toast.makeText(RegisterActivity.this, "更新失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult> versionRequest = new GsonRequest<JsonResult>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.LOGIN_NAME, userName);
                params.put(KeyConstant.pass_Word, pwd);
                params.put(KeyConstant.SMS_CODE, captcha);
                params.put(KeyConstant.TYPE, "1");
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    /**
     * 显示注册结果对话框
     */
    private void showDialog(final boolean isSuccess, String msg) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        dialogFragment.setDialogWidth(220);
        TextView tv = new TextView(RegisterActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        params.setMargins(0, 20, 0, 0);
        params.gravity = Gravity.CENTER;
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(msg);
        tv.setTextColor(getResources().getColor(R.color.color000000));
        dialogFragment.setContentView(tv);

        int stringId;
        if (isSuccess) {
            stringId = R.string.login_now;
        } else {
            stringId = R.string.sure;
        }

        dialogFragment.setNegativeButton(stringId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                if (isSuccess) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        dialogFragment.show(ft, "successDialog");
    }
}
