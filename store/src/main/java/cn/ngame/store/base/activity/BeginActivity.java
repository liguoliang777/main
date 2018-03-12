/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ngame.store.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.main.MainHomeActivity;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.fileload.FileLoadService;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.NetUtil;
import cn.ngame.store.core.utils.SPUtils;
import cn.ngame.store.core.utils.UrlConstant;
import cn.ngame.store.push.model.PushMessage;
import cn.ngame.store.util.ConvUtil;

/**
 * App启动时的等待窗口，处理进入home页时需要预先加载的内容
 *
 * @author flan
 * @since 2016年5月3日
 */
public class BeginActivity extends Activity {

    public static final String TAG = BeginActivity.class.getSimpleName();
    private boolean isFirstInstall = true;
    private Timer timer;
    private BeginActivity content;
    private long SHOW_TIME = 1000;
    private long SHOW_TIME_isFirstInstall = 1000;
    private Button skipBt;
    private FrameLayout adsParent;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        setContentView(R.layout.activity_splash_fullscreen);
        content = this;
        //启动后台服务
        skipBt = (Button) findViewById(R.id.skip_bt);
        Intent serviceIntent = new Intent(this, FileLoadService.class);
        startService(serviceIntent);

        //请求签名列表
        if (NetUtil.isNetworkConnected(content)) {
            getSignList();
        }

        //CommonUtil.verifyStatePermissions(content);
        //友盟相关
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(true);

        //判断是否是安装后第一次启动
        isFirstInstall = ConvUtil.NB(SPUtils.get(this, Constant.CONFIG_FIRST_INSTALL, true));
        timer = new Timer();

        skip2Main();

    }

    private void getSignList() {
        preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
        editor = preferences.edit();
        String url = Constant.WEB_SITE + UrlConstant.URL_QUERY_SIGNTOOL_LIST;
        Response.Listener<JsonResult> successListener =
                new Response.Listener<JsonResult>() {
                    @Override
                    public void onResponse(JsonResult result) {
                        int code = result.code;
                        if (code == 0 && result.data != null) {
                            Set<String> stringSet = new HashSet<>();
                            try {

                                ArrayList jsonArray = (ArrayList) result.data;
                                if (jsonArray != null) {
                                    for (Object o : jsonArray) {
                                        String object = String.valueOf(((Map) o).get(KeyConstant
                                                .signValue));
                                        stringSet.add(object);
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            editor.putStringSet(Constant.SIGN_TOOL_KEY, stringSet);
                            editor.apply();
                        }
                    }
                };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "请求签名信息失败:" + volleyError.getMessage());
            }
        };

        Request<JsonResult> versionRequest = new GsonRequest<JsonResult>(Request
                .Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                return params;
            }
        };
   /*     versionRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        StoreApplication.requestQueue.add(versionRequest);

    }

    private void go2Main() {
        final long pushMsgId = getIntent().getLongExtra("msgId", 0);
        final int pushMsgType = getIntent().getIntExtra("type", 0);
        final PushMessage msg = (PushMessage) getIntent().getSerializableExtra("msg");
        if (timer == null) {
            skip2Main();
            return;
        }
        if (isFirstInstall) {
            Log.d(TAG, "滑动页");
            final Intent intent = new Intent(content, GuideViewActivity.class);
            if (pushMsgId > 0) {
                intent.putExtra("msgId", pushMsgId);
                intent.putExtra("type", pushMsgType);
                intent.putExtra("msg", msg);
            }
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(intent);
                    //更新安装状态值
                    SPUtils.put(content, Constant.CONFIG_FIRST_INSTALL, false);
                    finish();
                }
            }, SHOW_TIME_isFirstInstall);
        } else {
            final Intent intent = new Intent(content, MainHomeActivity.class);
            if (pushMsgId > 0) {
                intent.putExtra("msgId", pushMsgId);
                intent.putExtra("type", pushMsgType);

                intent.putExtra("msg", msg);
            }
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(intent);
                    finish();
                }
            }, SHOW_TIME);
        }
    }

    private LinearLayout mFrameLayoutView;
    //InMobiNative nativeAd;
  /*  private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastUtil.show(content, "收到消息");
            //handler.sendEmptyMessageAtTime(0, 1000);

        }
    };*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void skip2Main() {
        // handler.sendEmptyMessageAtTime(0, 1000);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        final long pushMsgId = getIntent().getLongExtra("msgId", 0);
        final int pushMsgType = getIntent().getIntExtra("type", 0);
        final PushMessage msg = (PushMessage) getIntent().getSerializableExtra("msg");

        //去掉欢迎的滑动页
/*        if (isFirstInstall) {
            Log.d(TAG, "skip2Main 滑动页");
            final Intent intent = new Intent(content, GuideViewActivity.class);
            if (pushMsgId > 0) {
                intent.putExtra("msgId", pushMsgId);
                intent.putExtra("type", pushMsgType);
                intent.putExtra("msg", msg);
            }
            startActivity(intent);
            //更新安装状态值
            SPUtils.put(content, Constant.CONFIG_FIRST_INSTALL, false);
            finish();

        } else {*/
        Log.d(TAG, "skip2Main 跳主页面");
        final Intent intent = new Intent(content, MainHomeActivity.class);
        if (pushMsgId > 0) {
            intent.putExtra("msgId", pushMsgId);
            intent.putExtra("type", pushMsgType);
            intent.putExtra("msg", msg);
        }
        startActivity(intent);
        finish();
        //}

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }
}