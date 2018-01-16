package cn.ngame.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.adapter.ProgressBarStateListener;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.local.view.MyGameActivity;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.GameLoadProgressBar;

/**
 * 映射精灵下载页面
 * Created by zeng on 2016/11/17.
 */
public class PadToolActivity extends BaseFgActivity {

    public static final String TAG = PadToolActivity.class.getName();
    private GameLoadProgressBar progressBar;

    private long gameId = 500;
    private GameInfo gameInfo;

    private IFileLoad fileLoad;
    private Timer timer = new Timer();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        Button left_but = (Button) findViewById(R.id.left_but);
        left_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button right_but = (Button) findViewById(R.id.right_but);
        right_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PadToolActivity.this, MyGameActivity.class);
                startActivity(intent);
            }
        });

        progressBar = (GameLoadProgressBar) findViewById(R.id.progress_bar);

        fileLoad = FileLoadManager.getInstance(this);
        getGameInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    /**
     * 获取游戏详情
     */
    private void getGameInfo() {

        String url = Constant.WEB_SITE + Constant.URL_GAME_DETAIL;
        Response.Listener<JsonResult<GameInfo>> successListener = new Response.Listener<JsonResult<GameInfo>>() {
            @Override
            public void onResponse(JsonResult<GameInfo> result) {

                if(result == null || result.code != 0){
                    return;
                }
                gameInfo = result.data;
                if (gameInfo != null) {

                    //设置进度条状态
                    progressBar.setLoadState(fileLoad.getGameFileLoadStatus(gameInfo.filename,gameInfo.gameLink,gameInfo.packages, ConvUtil.NI(gameInfo.versionCode)));
                    //必须设置，否则点击进度条后无法进行响应操作
                    FileLoadInfo fileLoadInfo = new FileLoadInfo(gameInfo.filename,gameInfo.gameLink,gameInfo.md5,ConvUtil.NI(gameInfo.versionCode),gameInfo.gameName,gameInfo.gameLogo,gameInfo.id,FileLoadInfo.TYPE_GAME);
                    progressBar.setFileLoadInfo(fileLoadInfo);
                    fileLoadInfo.setPackageName(gameInfo.packages);
                    fileLoadInfo.setVersionCode(ConvUtil.NI(gameInfo.versionCode));
                    progressBar.setOnStateChangeListener(new ProgressBarStateListener(PadToolActivity.this,PadToolActivity.this.getSupportFragmentManager()));
                    progressBar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.toggle();
                        }
                    });

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<GameInfo>> request = new GsonRequest<JsonResult<GameInfo>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<GameInfo>>() {}.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("gameId", String.valueOf(gameId));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(gameInfo != null){
                            GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(gameInfo.filename,gameInfo.gameLink,gameInfo.packages,ConvUtil.NI(gameInfo.versionCode));
                            progressBar.setLoadState(fileStatus);
                        }
                    }
                });
            }
        }, 0, 500);
    }
}
