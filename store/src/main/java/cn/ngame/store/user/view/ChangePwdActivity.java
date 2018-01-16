package cn.ngame.store.user.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.User;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.UrlConstant;
import cn.ngame.store.fragment.SimpleDialogFragment;
import cn.ngame.store.view.BaseTitleBar;


/**
 * 找回密码界面
 * Created by zeng on 2016/5/17.
 */
public class ChangePwdActivity extends BaseFgActivity {

    public static final String TAG = "777";

    private Button bt_find_pwd;
    private EditText et_old_pwd, newPwdET1, ensurePwdEt;

    private boolean isShowPwd = false;
    private Handler handler = new Handler();
    private static final int WAIT_TIME = 61;
    private int second = 60;


    private boolean isFromUserCenter;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_change_pwd);
        SharedPreferences preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
        editor = preferences.edit();
        isFromUserCenter = getIntent().getBooleanExtra(KeyConstant.IS_FROM_USER_CENTER, false);
        BaseTitleBar titleBar = (BaseTitleBar) findViewById(R.id.title_bar);
        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePwdActivity.this, UserCenterActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);//左进,右出
            }
        });

        bt_find_pwd = (Button) findViewById(R.id.bt_find_pwd);
        et_old_pwd = (EditText) findViewById(R.id.old_pwd_et);
        newPwdET1 = (EditText) findViewById(R.id.new_pwd_et1);

        ensurePwdEt = (EditText) findViewById(R.id.ensure_pwd_et);

        bt_find_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPwdStr = et_old_pwd.getText().toString().trim();
                String ensurePwdStr = ensurePwdEt.getText().toString().trim();
                String newPwdETStr1 = newPwdET1.getText().toString().trim();

                if (oldPwdStr == null || oldPwdStr.length() <= 0) {
                    Toast.makeText(ChangePwdActivity.this, "旧密码不能为空哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (oldPwdStr.equals(newPwdETStr1)) {
                    Toast.makeText(ChangePwdActivity.this, "新密码和旧密码不能一致哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPwdETStr1 == null || newPwdETStr1.length() <= 0) {
                    Toast.makeText(ChangePwdActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPwdETStr1.length() < 6) {
                    Toast.makeText(ChangePwdActivity.this, "新密码不能少于六位哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ensurePwdStr == null || ensurePwdStr.length() <= 0) {
                    Toast.makeText(ChangePwdActivity.this, "请确认新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPwdETStr1.equals(ensurePwdStr)) {
                    Toast.makeText(ChangePwdActivity.this, "两次输入的新密码不一致哦", Toast.LENGTH_SHORT).show();
                    return;
                }

                doFindPwd(oldPwdStr, newPwdETStr1);
            }
        });
    }

    /**
     * 处理HTTP找回密码操作
     */
    private void doFindPwd(final String oldPwdStr, final String newPwdETStr1) {
        String url = Constant.WEB_SITE + UrlConstant.URL_MODIFY_PASSWORD;
        Response.Listener<JsonResult<User>> successListener = new Response.Listener<JsonResult<User>>() {
            @Override
            public void onResponse(JsonResult<User> result) {
                if (result == null) {
                    Toast.makeText(ChangePwdActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                int code = result.code;
                Log.d(TAG, "找回密码:" + code);
                Log.d(TAG, "找回密码:" + result.msg);
                if (code == 0) {
                    showDialog(true, "密码重置成功,请重新登录！");
                    logoutClearData();
                } else if (code >= -4 && code <= -1) {
                    showReLoginDialog();
                    //需要重新登录
                    logoutClearData();
                    //UserCenterActivity.this.finish();
                } else {
                    showDialog(false, result.msg);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Toast.makeText(ChangePwdActivity.this, "更新失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "更新密码失败：网络连接错误！" + volleyError.toString());
            }
        };

        Request<JsonResult<User>> versionRequest = new GsonRequest<JsonResult<User>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<User>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //设置POST请求参数
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.USER_CODE, StoreApplication.userCode);
                params.put(KeyConstant.TOKEN, StoreApplication.token);
                params.put(KeyConstant.old_Password, oldPwdStr);
                params.put(KeyConstant.new_Password, newPwdETStr1);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    /**
     * 显示注册结果对话框
     */
    private void showReLoginDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        dialogFragment.setDialogWidth(220);
        TextView tv = new TextView(ChangePwdActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        params.setMargins(0, 20, 0, 0);
        params.gravity = Gravity.CENTER;
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText("当前设备的登录信息已失效,\n需要重新登录后,才能执行修改操作");
        tv.setTextColor(getResources().getColor(R.color.color000000));
        dialogFragment.setContentView(tv);

        dialogFragment.setNegativeButton("去登录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                Intent intent = new Intent(ChangePwdActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialogFragment.show(ft, "successDialog");
    }

    //退出登录
    private void logoutClearData() {
        editor.putString(Constant.CONFIG_USER_PWD, "");
        editor.putBoolean(KeyConstant.AVATAR_HAS_CHANGED, true);
        editor.apply();

        StoreApplication.userHeadUrl = "";
        StoreApplication.nickName = "";
        StoreApplication.userCode = "";
        StoreApplication.userName = "";
        StoreApplication.passWord = "";
        StoreApplication.token = null;
        StoreApplication.user = null;
    }

    /**
     * 显示注册结果对话框
     */
    private void showDialog(final boolean isSuccess, String msg) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        dialogFragment.setDialogWidth(220);

        TextView tv = new TextView(ChangePwdActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);
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
                    Intent intent = new Intent(ChangePwdActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        dialogFragment.show(ft, "successDialog");
    }
}
