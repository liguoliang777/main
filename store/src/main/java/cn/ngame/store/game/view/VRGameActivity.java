package cn.ngame.store.game.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.GvGameSubjectAdapter;
import cn.ngame.store.adapter.NoScrollGameListAdapter;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.GameType;
import cn.ngame.store.bean.HotInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.search.view.SearchActivity;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.video.view.VideoDetailActivity;
import cn.ngame.store.view.BannerView;
import cn.ngame.store.view.BaseTitleBar;
import cn.ngame.store.view.NoScrollListView;
import cn.ngame.store.view.PicassoImageView;

/**
 * 显示VR游戏列表的页面
 * Created by zeng on 2016/6/16.
 */
public class VRGameActivity extends BaseFgActivity {

    public static final String TAG = VRGameActivity.class.getSimpleName();

    private BannerView bannerView;
    private NoScrollListView listView;

    private GridView gv_game;
    private GvGameSubjectAdapter subjectGameAdapter;
    private List<GameType> gameTypeList;

    private NoScrollGameListAdapter adapter;
    private List<GameInfo> gameInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_vr_game);

        BaseTitleBar titleBar = (BaseTitleBar) findViewById(R.id.title_bar);
        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VRGameActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        bannerView = (BannerView) findViewById(R.id.banner_view);
        listView = (NoScrollListView) findViewById(R.id.listView);
        gv_game = (GridView) findViewById(R.id.gv_game);


        getBannerData();    //加载轮播广告数据
        getGameSubject();
        getGameList();

        setListener();
    }

    private void setListener(){

        adapter = new NoScrollGameListAdapter(this,getSupportFragmentManager());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VRGameActivity.this, GameDetailActivity.class);
                int position = v.getId();
                intent.putExtra("id", gameInfoList.get(position).id);
                VRGameActivity.this.startActivity(intent);
            }
        });

        subjectGameAdapter = new GvGameSubjectAdapter(this);
        gv_game.setAdapter(subjectGameAdapter);
        gv_game.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(gameTypeList != null){
                    GameType gameType = gameTypeList.get(position);
                    Intent intent = new Intent(VRGameActivity.this,SeeMoreActivity.class);
                    intent.putExtra("categoryId",gameType.id);
                    intent.putExtra("title",gameType.typeName);
                    VRGameActivity.this.startActivity(intent);
                }
            }
        });
    }


    /** 获取游戏专题分类 */
    private void getGameSubject(){

        String url = Constant.WEB_SITE + Constant.URL_HOME_GAME_CATEGORY;
        Response.Listener<JsonResult<List<GameType>>> successListener = new Response.Listener<JsonResult<List<GameType>>>() {
            @Override
            public void onResponse(JsonResult<List<GameType>> result) {

                if (result == null) {
                    //Toast.makeText(context, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.code == 0) {
                    gameTypeList = result.data;
                    subjectGameAdapter.setData(gameTypeList);
                    subjectGameAdapter.notifyDataSetChanged();

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

        Request<JsonResult<List<GameType>>> request = new GsonRequest<JsonResult<List<GameType>>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<List<GameType>>>() {}.getType()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("markId", String.valueOf(1));
                params.put("appTypeId",String.valueOf(0));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    /** 获取推荐游戏列表 */
    private void getGameList() {

        String url = Constant.WEB_SITE + Constant.URL_GAME_LIST;
        Response.Listener<JsonResult<List<GameInfo>>> successListener = new Response.Listener<JsonResult<List<GameInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<GameInfo>> result) {

                if (result != null && result.code == 0) {

                    gameInfoList = result.data;

                    adapter.setDate(gameInfoList);
                    listView.notifyDataSetChanged();

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

        Request<JsonResult<List<GameInfo>>> request = new GsonRequest<JsonResult<List<GameInfo>>>(Request.Method.POST, url, successListener,
                errorListener, new TypeToken<JsonResult<List<GameInfo>>>() {}.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("categoryId",String.valueOf(0));
                params.put("gameLabelId",String.valueOf(1));
                params.put("pageSize", String.valueOf(10));
                params.put("pageIndex", String.valueOf(1));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    /** 获取轮播图片数据 */
    private void getBannerData() {

        String url = Constant.WEB_SITE + Constant.URL_BANNER2;
        Response.Listener<JsonResult<List<HotInfo>>> successListener = new Response.Listener<JsonResult<List<HotInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<HotInfo>> result) {

                if (result == null) {
                    //Toast.makeText(VRGameActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.code == 0) {

                    List<ImageView> list = createBannerView(result.data);
                    bannerView.setData(list);

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

        Request<JsonResult<List<HotInfo>>> request = new GsonRequest<JsonResult<List<HotInfo>>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<List<HotInfo>>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("typeName", "VR游戏");
                params.put("advTypeId", String.valueOf(0));
                params.put("appTypeId", String.valueOf(0));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    /** 创建轮播视图 */
    private List<ImageView> createBannerView(List<HotInfo> hotInfoList) {

        if (hotInfoList == null || hotInfoList.size() <= 0) {
            return null;
        }

        ArrayList<ImageView> list = new ArrayList<>();
        for (int i = 0; i < hotInfoList.size(); i++) {

            final HotInfo info = hotInfoList.get(i);
            PicassoImageView img = new PicassoImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            img.setLayoutParams(params);
            img.setId((int) info.id);
            img.setTag(info.advImageLink);

            img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.type == 1) {
                        Intent intent = new Intent(VRGameActivity.this, GameDetailActivity.class);
                        intent.putExtra("id", info.gameId);
                        startActivity(intent);
                    } else if (info.type == 2) {
                        Intent intent = new Intent(VRGameActivity.this, VideoDetailActivity.class);
                        intent.putExtra("id", info.videoId);
                        startActivity(intent);
                    }
                }
            });
            list.add(img);
        }

        return list;
    }


}
