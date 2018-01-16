package cn.ngame.store.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import cn.ngame.store.StoreApplication;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.User;
import cn.ngame.store.core.net.GsonRequest;

/**
 * 用户登录辅助类
 * Created by zeng on 2016/7/26.
 */
public class LoginHelper {

    public static final String TAG = "777";

    private Context context;
    private SharedPreferences preferences;
    private String userName;
    private String passWord;

    public LoginHelper(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(Constant.CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        userName = preferences.getString(Constant.CONFIG_USER_NAME, "");
        passWord = preferences.getString(Constant.CONFIG_USER_PWD, "");
    }

    public void reLoadSP() {
        StoreApplication.userName = preferences.getString(Constant.CONFIG_USER_NAME, "");
        StoreApplication.passWord = preferences.getString(Constant.CONFIG_USER_PWD, "");
        StoreApplication.userCode = preferences.getString(Constant.CONFIG_USER_CODE, "");
        StoreApplication.userHeadUrl = preferences.getString(Constant.CONFIG_USER_HEAD, "");
        StoreApplication.token = preferences.getString(Constant.CONFIG_TOKEN, "");
        StoreApplication.loginType = preferences.getString(Constant.CONFIG_LOGIN_TYPE, Constant.PHONE);
    }

    /**
     * 重新登录
     */
    public void reLogin() {
        String url = Constant.WEB_SITE + Constant.URL_USER_LOGIN;
        android.util.Log.d(TAG, "重新登录1:账号 " + StoreApplication.userName);
        android.util.Log.d(TAG, "重新登录1密码: " + StoreApplication.passWord);
        Response.Listener<JsonResult<User>> succesListener = new Response.Listener<JsonResult<User>>() {
            @Override
            public void onResponse(JsonResult<User> result) {
                if (result == null) {
                    return;
                }
                if (result.code == 0) {
                    User user = result.data;
                    StoreApplication.user = user;

                /*    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Constant.CONFIG_USER_HEAD, user.headPhoto);
                    editor.putString(Constant.CONFIG_NICK_NAME, user.nickName);
                    editor.putString(Constant.CONFIG_USER_NAME, user.loginName);
                    editor.putString(Constant.CONFIG_USER_PWD, StoreApplication.passWord);
                    editor.putString(Constant.CONFIG_LOGIN_TYPE, StoreApplication.loginType);
                    editor.apply();*/

                    //加载用户头像
                    Log.d(TAG, "重新登录.昵称: " + user.nickName);
                    Log.d(TAG, "重新登录.账号: " + user.loginName);
                    Log.d(TAG, "重新登录.密码: " + StoreApplication.passWord);
                    Log.d(TAG, "重新.User对象密码: " + user.password);
                    android.util.Log.d(TAG, "userToken:" + user.token);
                } else {
                    Log.d(TAG, "重新登录 HTTP请求成功：服务端返回错误: " + result.msg);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "重新登录 HTTP请求失败：网络连接错误！" + volleyError.getMessage());
            }
        };
        Request<JsonResult<User>> versionRequest1 = new GsonRequest<JsonResult<User>>(Request.Method.POST, url,
                succesListener, errorListener, new TypeToken<JsonResult<User>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //设置POST请求参数
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.NICK_NAME, StoreApplication.nickName);
                params.put(KeyConstant.LOGIN_NAME, StoreApplication.userName);
                params.put(KeyConstant.pass_word, StoreApplication.passWord);
                android.util.Log.d(TAG, "重新登录: " + StoreApplication.passWord);
                android.util.Log.d(TAG, "重新登录: " + StoreApplication.userName);
                params.put(KeyConstant.TYPE, StoreApplication.loginType); //（1手机，2QQ，3微信，4新浪微博）
                params.put(KeyConstant.HEAD_PHOTO, StoreApplication.userHeadUrl);  //头像
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);  //
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest1);
    }
}
