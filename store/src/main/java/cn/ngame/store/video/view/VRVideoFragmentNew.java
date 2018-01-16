package cn.ngame.store.video.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.adapter.DividerGridItemDecoration;
import cn.ngame.store.adapter.VrVideoRvAdapter;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.VideoInfo;
import cn.ngame.store.bean.VideoType;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.view.LoadMoreRecyclerView;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.view.VideoTypeScrollTabView;

/**
 * 显示VR视频的页面
 * Created by zeng on 2016/6/7.
 */
public class VRVideoFragmentNew extends Fragment {

    public static final String TAG = VRVideoFragmentNew.class.getSimpleName();

    private Context context;

    private VideoTypeScrollTabView scrollTabView;
    private LoadMoreRecyclerView recyclerView;
    private VrVideoRvAdapter adapter;
    private LoadStateView loadStateView;

    private List<VideoType> videoTypes;

    private long currentTypeId;
    private long currentLabelId;

    private int pageIndex = 1;
    private int pageSize = 9;
    private long totals;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //获取初始参数
        currentLabelId = getArguments().getLong("labelId", 1);
        getVideoType(); //根据LabelId加载type

        View view = inflater.inflate(R.layout.fragment_vr_video_new, null);

        loadStateView = (LoadStateView) view.findViewById(R.id.loadStateView);
        loadStateView.setReLoadListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStateView.setState(LoadStateView.STATE_ING);
                getVideoType(); //重新加载
            }
        });

        scrollTabView = (VideoTypeScrollTabView) view.findViewById(R.id.auto_tab);
        recyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(context));
        adapter = new VrVideoRvAdapter(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setmAdapter(adapter);
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e(TAG,"我在加载更多...");

                if(totals > adapter.getItemCount()){
                    recyclerView.setLoadingMore(true);  //正在加载更多
                    pageIndex += 1;
                    getVideoList();
                }
            }
        });
        return view;
    }

    /**
     * 根据CategoryId获取VideoType
     */
    private void getVideoType(){

        String url = Constant.WEB_SITE + Constant.URL_VIDEO_TYPE;
        Response.Listener<JsonResult<List<VideoType>>> successListener = new Response.Listener<JsonResult<List<VideoType>>>() {
            @Override
            public void onResponse(JsonResult<List<VideoType>> result) {

                if (result == null) {
                    loadStateView.setVisibility(View.VISIBLE);
                    loadStateView.setState(LoadStateView.STATE_END);
                    return;
                }

                if (result.code == 0) {

                    videoTypes = result.data;
                    if(videoTypes != null && videoTypes.size() > 0){

                        VideoType allType = new VideoType();
                        allType.id = 0;
                        allType.text = "全部";
                        videoTypes.add(0,allType);

                        //默认加载第一个分类下的视频数据
                        currentTypeId = videoTypes.get(0).id;
                        getVideoList();

                        scrollTabView.setTextList(videoTypes);
                        scrollTabView.setOnTabViewClickListener(new VideoTypeScrollTabView.OnTabViewClickListener() {
                            @Override
                            public void onClick(long typeId, String typeName) {

                                // 执行TAG切换
                                if (currentTypeId != typeId) {

                                    currentTypeId = typeId;
                                    pageIndex = 1;
                                    totals = 0;
                                    adapter.clean();
                                    recyclerView.setLoadingMore(true);
                                    getVideoList();

                                    loadStateView.isShowLoadBut(true);
                                    loadStateView.setState(LoadStateView.STATE_ING);
                                    loadStateView.setVisibility(View.VISIBLE);
                                }

                            }
                        });

                    }else {

                        loadStateView.isShowLoadBut(false);
                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_END,"没有数据");
                    }

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                    loadStateView.isShowLoadBut(true);
                    loadStateView.setState(LoadStateView.STATE_END);
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

        Request<JsonResult<List<VideoType>>> request = new GsonRequest<JsonResult<List<VideoType>>>(Request.Method.POST, url,
                successListener,errorListener, new TypeToken<JsonResult<List<VideoType>>>() {}.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("videoLabelId", String.valueOf(currentLabelId));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    /**
     * 获取指定分类下的视频列表
     */
    private void getVideoList() {
        String url = Constant.WEB_SITE + Constant.URL_VIDEO_LIST;
        Response.Listener<JsonResult<List<VideoInfo>>> successListener = new Response.Listener<JsonResult<List<VideoInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<VideoInfo>> result) {

                if (result == null) {
                    loadStateView.setVisibility(View.VISIBLE);
                    loadStateView.setState(LoadStateView.STATE_END);
                    return;
                }

                if (result.code == 0) {

                    List<VideoInfo> videoInfoList = result.data;
                    totals = result.totals;

                    if(videoInfoList != null && videoInfoList.size() > 0){

                        adapter.setDate(videoInfoList);
                        recyclerView.notifyMoreFinish();

                        loadStateView.setVisibility(View.GONE);
                        loadStateView.setState(LoadStateView.STATE_END);

                    }else {

                        loadStateView.isShowLoadBut(false);
                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_END,"没有数据");
                    }

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                    loadStateView.isShowLoadBut(true);
                    loadStateView.setVisibility(View.VISIBLE);
                    loadStateView.setState(LoadStateView.STATE_END);
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

        Request<JsonResult<List<VideoInfo>>> request = new GsonRequest<JsonResult<List<VideoInfo>>>(Request.Method.POST, url,
                successListener,errorListener, new TypeToken<JsonResult<List<VideoInfo>>>() {}.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("videoTypeId", String.valueOf(currentTypeId));
                params.put("videoLabelId", String.valueOf(currentLabelId));
                params.put("pageIndex", String.valueOf(pageIndex));
                params.put("pageSize", String.valueOf(pageSize));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }


}
