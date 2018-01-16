package cn.ngame.store.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

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
import cn.ngame.store.video.view.VideoDetailActivity;
import cn.ngame.store.adapter.GvVrVideoAdapter;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.VideoCategory;
import cn.ngame.store.bean.VideoInfo;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.view.ScrollGridView;
import cn.ngame.store.view.VideoTypeScrollTabView;

/**
 * 显示VR视频的页面
 * Created by zeng on 2016/6/7.
 */
public class VRVideoFragment extends Fragment {

    public static final String TAG = VRVideoFragment.class.getSimpleName();

    private Context context;
    private VideoCategory videoCategory;

    private ScrollView scrollView;
    private FrameLayout layout;
    private ScrollGridView gridView;
    private GvVrVideoAdapter adapter;
    private VideoTypeScrollTabView scrollTabView;

    private LoadStateView loadStateView;

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
        videoCategory = (VideoCategory) getArguments().getSerializable(VideoCategory.TAG);

        currentTypeId = videoCategory.videoTypes.get(0).id;
        currentLabelId = getArguments().getLong("labelId", 1);
        getVideoList();

        View view = inflater.inflate(R.layout.fragment_vr_video, null);

        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        layout = (FrameLayout) view.findViewById(R.id.layout_1);

        scrollTabView = (VideoTypeScrollTabView) view.findViewById(R.id.auto_tab);
        gridView = (ScrollGridView) view.findViewById(R.id.gridView);

        loadStateView = (LoadStateView) view.findViewById(R.id.loadStateView);
        loadStateView.setReLoadListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStateView.setState(LoadStateView.STATE_ING);
                getVideoList(); //重新加载
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new GvVrVideoAdapter(this.getActivity());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                VideoInfo videoInfo = (VideoInfo) adapter.getItem(position);

                Intent intent = new Intent(context, VideoDetailActivity.class);
                intent.putExtra("id", videoInfo.id);
                context.startActivity(intent);

            }
        });
        scrollTabView.setTextList(videoCategory.videoTypes);
        scrollTabView.setOnTabViewClickListener(new VideoTypeScrollTabView.OnTabViewClickListener() {
            @Override
            public void onClick(long typeId, String typeName) {

                // 执行TAG切换
                if (currentTypeId != typeId) {

                    currentTypeId = typeId;
                    pageIndex = 1;
                    totals = 0;
                    adapter.clean();
                    getVideoList();

                    gridView.setVisibility(View.GONE);
                    loadStateView.setState(LoadStateView.STATE_ING);
                    loadStateView.setVisibility(View.VISIBLE);
                }

            }
        });

        //处理上滑加载更多
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            private int lastY = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP){
                    lastY = scrollView.getScrollY();
                    if(lastY == (layout.getHeight() - scrollView.getHeight())){
                        long count = adapter.getCount();
                        if(count < totals){
                            pageIndex += 1;
                            getVideoList();
                        }
                    }
                }
                return false;
            }
        });
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

                        adapter.setData(videoInfoList);
                        adapter.notifyDataSetInvalidated();

                        gridView.setVisibility(View.VISIBLE);
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
