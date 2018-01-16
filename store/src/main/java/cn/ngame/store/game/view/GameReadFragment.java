package cn.ngame.store.game.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.game.bean.GameStrategy;
import cn.ngame.store.view.AutoHeightViewPager;

import static cn.ngame.store.R.id.strategy_content_tv;

/**
 * 必读
 *
 * @author zeng
 * @since 2016/12/15
 */
@SuppressLint("ValidFragment")
public class GameReadFragment extends Fragment {
    public static final String TAG = GameReadFragment.class.getSimpleName();
    private TextView titleTv;
    private TextView contentTv;
    private int SCREEN_HEIGHT = 2200;
    private static AutoHeightViewPager vp;
    private Activity context;
    private GameInfo gameInfo;
    private LinearLayout readLL;
    private List<GameStrategy> gameStrategyList = new ArrayList<>();
    private GameStrategy gameStrategy;

    public GameReadFragment(AutoHeightViewPager vp, GameInfo gameInfo) {
        this.gameInfo = gameInfo;
        this.vp = vp;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_game_read, container, false);
        readLL = (LinearLayout) view.findViewById(R.id.read_ll);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        if (gameInfo != null && gameInfo.gameStrategyList != null) {
            gameStrategyList = gameInfo.gameStrategyList;
            for (int i = 0; i < gameStrategyList.size(); i++) {
                View itemView = inflater.inflate(R.layout.fragment_game_read_item, container, false);
                titleTv = (TextView) itemView.findViewById(R.id.strategy_title_tv);
                contentTv = (TextView) itemView.findViewById(strategy_content_tv);
                gameStrategy = gameStrategyList.get(i);
                titleTv.setText(gameStrategy.getStrategyTitle());
                contentTv.setText(gameStrategy.getStrategyContent());
                readLL.addView(itemView);
            }
            readLL.measure(0, 0);
            int height = readLL.getMeasuredHeight();
            int EMPTY_HEIGHT = SCREEN_HEIGHT - height;
            if (EMPTY_HEIGHT > 0) {
                params.height = EMPTY_HEIGHT;
                TextView bottomTv = new TextView(context);
                bottomTv.setLayoutParams(params);
                readLL.addView(bottomTv);
            } else {
                View contentTv = new View(context);
                params.height = 100;
                contentTv.setLayoutParams(params);
                readLL.addView(contentTv);
            }
        } else {
            params.height = SCREEN_HEIGHT;
            TextView contentTv = new TextView(context);
            contentTv.setGravity(Gravity.CENTER);
            contentTv.setText("暂无数据~");
            contentTv.setLayoutParams(params);
            readLL.addView(contentTv);
        }
        vp.setObjectForPosition(view, 1);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // gameInfo = (GameInfo) getArguments().getSerializable(GameInfo.TAG);
    }
    /**
     * 获取攻略
     */
    private void getData() {
        String url = Constant.WEB_SITE + Constant.URL_GAME_STRATEGY;
        Response.Listener<JsonResult<GameStrategy>> successListener = new Response.Listener<JsonResult<GameStrategy>>() {
            @Override
            public void onResponse(JsonResult<GameStrategy> result) {

                if (result == null || result.code != 0) {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                }
                GameStrategy strategy = result.data;

                if (strategy != null) {
                    Log.d(TAG, "请求成功");

                } else {
                    Log.d(TAG, "正在整理中...");
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

        Request<JsonResult<GameStrategy>> request = new GsonRequest<JsonResult<GameStrategy>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<GameStrategy>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.GAME_ID, gameInfo.id + "");
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);

    }


    public void setViewPager(AutoHeightViewPager viewpager) {
        this.vp = viewpager;
    }
}
