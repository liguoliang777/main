package cn.ngame.store.user.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.User;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.NetUtil;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.local.model.IWatchRecordModel;
import cn.ngame.store.local.model.WatchRecordModel;
import cn.ngame.store.util.ToastUtil;

/**
 * 用户登录界面
 * Created by zeng on 2016/5/12.
 */
public class LoginActivity extends BaseFgActivity implements View.OnClickListener {

    public static final String TAG = LoginActivity.class.getSimpleName();

    private EditText et_user, et_pwd;
    private String userName, password;
    private TextView bt_find_pwd, bt_register;
    private Button bt_login;
    private ImageButton bt_show_pwd;

    private boolean isShowPwd = false;

    private SharedPreferences preferences;
    private ImageView deleteIv;
    private UMShareAPI mShareAPI;
    private LoginActivity mContext;
    private DialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
        View.SYSTEM_UI_FLAG_FULLSCREEN,   //全屏，状态栏和导航栏不显示
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION, //隐藏导航栏
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN, //全屏，状态栏会盖在布局上
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION,
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE,
                View.SYSTEM_UI_FLAG_LOW_PROFILE,
                View.SYSTEM_UI_FLAG_VISIBLE,  //显示状态栏和导航栏*/
/*
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/

        //getWindow().setWindowAnimations(R.style.activity_left_in_style);
        this.setContentView(R.layout.activity_login);

        /*======================================透明标题栏==========================================*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
        }
        //获取状态栏高度设置给标题栏==========================================
       /* RelativeLayout titleRlay = (RelativeLayout) findViewById(R.id.title_rlay);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                titleRlay.getLayoutParams());
        int statusBarHeight = ImageUtil.getStatusBarHeight(this);
        layoutParams.setMargins(0, statusBarHeight, 0, 0);
        titleRlay.setLayoutParams(layoutParams);*/
        //======================================================================
        mContext = this;
        preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);

        et_user = (EditText) findViewById(R.id.et_login_user);
        et_pwd = (EditText) findViewById(R.id.et_login_pwd);

        bt_show_pwd = (ImageButton) findViewById(R.id.bt_show_pwd);
        bt_show_pwd.setOnClickListener(this);
        bt_find_pwd = (TextView) findViewById(R.id.tv_find_pwd);
        bt_find_pwd.setOnClickListener(this);
        bt_register = (TextView) findViewById(R.id.tv_register);
        deleteIv = (ImageView) findViewById(R.id.delete_iv);
        deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_user.setText("");
            }
        });
        bt_register.setOnClickListener(this);
        bt_login = (Button) findViewById(R.id.but_login);
        bt_login.setOnClickListener(this);

        et_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    deleteIv.setVisibility(View.VISIBLE);
                    if (s.length() >= 11) {
                        et_pwd.requestFocus();
                    }
                } else {
                    deleteIv.setVisibility(View.GONE);
                }
            }


        });
        dialogHelper = new DialogHelper(getSupportFragmentManager(), mContext);
        mShareAPI = UMShareAPI.get(this);
        findViewById(R.id.left_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.login_qq_bt).setOnClickListener(this);
        findViewById(R.id.login_wechat_bt).setOnClickListener(this);
        findViewById(R.id.login_sina_bt).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userName = preferences.getString(Constant.CONFIG_USER_NAME, "");
        if (!userName.equals("") && userName.length() <= 11) {
            et_user.setText(userName);
            et_user.setSelection(userName.length());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_show_pwd:
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
                break;
            case R.id.but_login:
                userName = et_user.getText().toString();
                password = et_pwd.getText().toString();
                if (userName == null || "".equals(userName)) {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtil.isMobile(userName)) {
                    Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password == null || "".equals(password)) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                doLogin(Constant.PHONE, userName, password, nicknameStr, URL_HEAD_PHOTO);
                break;
            case R.id.tv_find_pwd:
                Intent fIntent = new Intent(this, FindPwdActivity.class);
                fIntent.putExtra(KeyConstant.IS_FROM_USER_CENTER, false);
                startActivity(fIntent);

                break;
            case R.id.tv_register:
                Intent rIntent = new Intent(this, RegisterActivity.class);
                startActivity(rIntent);
                break;
            //微信
            case R.id.login_wechat_bt:
                if (!NetUtil.isNetworkConnected(mContext)) {
                    ToastUtil.show(mContext, getString(R.string.no_network));
                    return;
                }
                if (!mShareAPI.isInstall(mContext, SHARE_MEDIA.WEIXIN)) {
                    ToastUtil.show(mContext, "尚未安装该应用,请先下载安装");
                    return;
                }
                mShareAPI.getPlatformInfo(mContext, SHARE_MEDIA.WEIXIN, authListener);
                break;
            //qq
            case R.id.login_qq_bt:
                if (!NetUtil.isNetworkConnected(mContext)) {
                    ToastUtil.show(mContext, getString(R.string.no_network));
                    return;
                }
                if (!mShareAPI.isInstall(mContext, SHARE_MEDIA.QQ)) {
                    ToastUtil.show(mContext, "尚未安装该应用,请先下载安装");
                    return;
                }
                try {
                    mShareAPI.getPlatformInfo(mContext, SHARE_MEDIA.QQ, authListener);
                } catch (Exception e) {
                    android.util.Log.d(TAG, "获取QQ第三方登录信息出现错误: ");
                }
                break;
            //新浪
            case R.id.login_sina_bt:
                if (!NetUtil.isNetworkConnected(mContext)) {
                    ToastUtil.show(mContext, getString(R.string.no_network));
                    return;
                }
                if (!mShareAPI.isInstall(mContext, SHARE_MEDIA.SINA)) {
                    ToastUtil.show(mContext, "尚未安装该应用,请先下载安装");
                    return;
                }
                mShareAPI.getPlatformInfo(mContext, SHARE_MEDIA.SINA, authListener);
                break;
        }
    }

    //第三方登录回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            android.util.Log.d(TAG, requestCode + "第三方登录回调出现错误: ");
        }

    }

    private UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            dialogHelper.showAlert("加载中...", true);
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            if (dialogHelper != null) {
                dialogHelper.hideAlert();
            }
            ToastUtil.show(mContext, "授权失败");
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            if (dialogHelper != null) {
                dialogHelper.hideAlert();
            }
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (dialogHelper != null) {
                dialogHelper.hideAlert();
            }
            String type = Constant.PHONE;//（1手机，2QQ，3微信，4新浪微博）
            switch (platform.toString()) {
                case "WEIXIN":
                    type = Constant.WEIXIN;
                    break;
                case "SINA":
                    type = Constant.SINA;
                    break;
                case "QQ":
                    type = Constant.QQ;
                    break;
            }
            if (null == data) {
                ToastUtil.show(mContext, "登录失败");
                return;
            }
            doLogin(type, data.get("uid"), "", data.get("name"), data.get("iconurl"));
        }
    };

    /**
     * 执行HTTP登录操作
     */
    private void doLogin(final String LOGIN_TYPE, final String userName, final String password,
                         final String nicknameStr, final String URL_HEAD_PHOTO) {
        dialogHelper.showAlert("正在登录...", true);
        String url = Constant.WEB_SITE + Constant.URL_USER_LOGIN;

        Response.Listener<JsonResult<User>> succesListener = new Response.Listener<JsonResult<User>>() {
            @Override
            public void onResponse(JsonResult<User> result) {
                if (result == null) {
                    if (null != mContext && !mContext.isFinishing()) {
                        dialogHelper.hideAlert();
                    }
                    Toast.makeText(mContext, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.code == 0) {
                    User user = result.data;
                    String token = user.token;
                    String userCode = user.userCode;
                    String headPhoto = user.headPhoto;
                    String nickName = user.nickName;

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Constant.CONFIG_TOKEN, token);
                    editor.putString(Constant.CONFIG_USER_HEAD, headPhoto);//头像
                    editor.putString(Constant.CONFIG_NICK_NAME, nickName);
                    editor.putString(Constant.CONFIG_USER_NAME, userName);
                    editor.putString(Constant.CONFIG_USER_PWD, password);
                    editor.putString(Constant.CONFIG_LOGIN_TYPE, LOGIN_TYPE);
                    editor.putString(Constant.CONFIG_USER_CODE, userCode);//userCode

                    editor.putBoolean(KeyConstant.AVATAR_HAS_CHANGED, true);
                    editor.apply();

                    StoreApplication.token = token;
                    StoreApplication.passWord = password;
                    StoreApplication.userHeadUrl = headPhoto;
                    StoreApplication.nickName = nickName;
                    StoreApplication.userName = userName;
                    StoreApplication.loginType = LOGIN_TYPE;
                    StoreApplication.userCode = userCode;

                    if (LOGIN_TYPE.equals(Constant.QQ)) {
                        MobclickAgent.onProfileSignIn("QQ", userCode);
                    } else if (LOGIN_TYPE.equals(Constant.WEIXIN)) {
                        MobclickAgent.onProfileSignIn("WEIXIN", userCode);
                    } else {
                        MobclickAgent.onProfileSignIn(userName);
                    }

                    //同步本地观看记录到服务器
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            IWatchRecordModel watchRecordModel = new WatchRecordModel(LoginActivity.this);
                            watchRecordModel.synchronizeWatchRecord();
                        }
                    }).start();
                    mContext.finish();
                } else {
                    ToastUtil.show(mContext, "登录失败，" + result.msg);
                    if (null != mContext && !mContext.isFinishing()) {
                        dialogHelper.hideAlert();
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Toast.makeText(LoginActivity.this, "登录失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！" + volleyError.getMessage());
                if (null != mContext && !mContext.isFinishing()) {
                    dialogHelper.hideAlert();
                }
            }
        };
        Request<JsonResult<User>> versionRequest1 = new GsonRequest<JsonResult<User>>(Request.Method.POST, url,
                succesListener, errorListener, new TypeToken<JsonResult<User>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //设置POST请求参数
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.LOGIN_NAME, userName);//uid
                params.put(KeyConstant.NICK_NAME, nicknameStr);//
                params.put(KeyConstant.pass_word, password);//""
                params.put(KeyConstant.TYPE, LOGIN_TYPE); //（1手机，2QQ，3微信，4新浪微博）
                params.put(KeyConstant.HEAD_PHOTO, URL_HEAD_PHOTO);  //头像
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);  //
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest1);
       /* Request<JsonResult<Token>> versionRequest = new GsonRequest<JsonResult<Token>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<Token>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //设置POST请求参数
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.NICK_NAME, nicknameStr);
                params.put(KeyConstant.LOGIN_NAME, userName);
                params.put(KeyConstant.pass_word, password);
                params.put(KeyConstant.TYPE, LOGIN_TYPE); //（1手机，2QQ，3微信，4新浪微博）
                params.put(KeyConstant.HEAD_PHOTO, URL_HEAD_PHOTO);  //头像
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);  //
                return params;
            }
        };*/

    }

    private String nicknameStr = "";
    private String URL_HEAD_PHOTO = "";

    /**
     * 获取用户信息
     */
   /* private void getUserInfo() {

        final String url = Constant.WEB_SITE + Constant.URL_USER_INFO;
        Response.Listener<JsonResult<User>> successListener = new Response.Listener<JsonResult<User>>() {
            @Override
            public void onResponse(JsonResult<User> result) {
                DialogHelper.hideWaiting(getSupportFragmentManager());
                if (result == null) {
                    Toast.makeText(LoginActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.code == 0) {

                    User user = result.data;
                    StoreApplication.user = user;

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Constant.CONFIG_USER_HEAD, user.headPhoto);
                    editor.putString(Constant.CONFIG_NICK_NAME, user.nickName);
                    editor.apply();

                    //加载用户头像
                    StoreApplication.userHeadUrl = user.headPhoto;
                    StoreApplication.nickName = user.nickName;

//                    //跳转到用户中心
//                    Intent intent = new Intent(LoginActivity.this,UserCenterActivity.class);
//                    startActivity(intent);
                    LoginActivity.this.finish();

                    //同步本地观看记录到服务器
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            IWatchRecordModel watchRecordModel = new WatchRecordModel(LoginActivity.this);
                            watchRecordModel.synchronizeWatchRecord();
                        }
                    }).start();

                    finish();

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误: " + result.msg);
                    Toast.makeText(LoginActivity.this, "服务端异常，请重新登录！", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                DialogHelper.hideWaiting(getSupportFragmentManager());
                Toast.makeText(LoginActivity.this, "更新失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<User>> versionRequest = new GsonRequest<JsonResult<User>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<User>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("token", StoreApplication.token);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        mShareAPI.deleteOauth(mContext, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
            }
        });
    }
}
