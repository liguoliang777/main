package cn.ngame.store.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.video.view.VideoDetailActivity;
import cn.ngame.store.adapter.GvVrVideoAdapter;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.VideoInfo;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;

/**
 * 显示视频排行榜的页面
 * Created by zeng on 2016/6/7.
 */
public class VideoRankFragment extends Fragment {

    public static final String TAG = VideoRankFragment.class.getSimpleName();

    private Context context;
    private List<VideoInfo> videoInfoList;

    private GridView gridView;
    private GvVrVideoAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        getVideoList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_rank, null);
        gridView = (GridView) view.findViewById(R.id.gridView);
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

                VideoInfo videoInfo = videoInfoList.get(position);

                Intent intent = new Intent(context, VideoDetailActivity.class);
                intent.putExtra("id", videoInfo.id);
                context.startActivity(intent);

            }
        });
    }

    /**
     * 获取指定分类下的视频列表
     */
    private void getVideoList() {
        String url = Constant.WEB_SITE + Constant.URL_VIDEO_RANK_LIST;
        Response.Listener<JsonResult<List<VideoInfo>>> successListener = new Response.Listener<JsonResult<List<VideoInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<VideoInfo>> result) {

                if (result != null && result.code == 0) {
                    videoInfoList = result.data;

                    adapter.setData(videoInfoList);
                    adapter.notifyDataSetChanged();


                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                //TODO 提示检测错误，隐藏等待窗口
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<List<VideoInfo>>> request = new GsonRequest<JsonResult<List<VideoInfo>>>(Request.Method.POST, url,
                successListener,errorListener, new TypeToken<JsonResult<List<VideoInfo>>>() {}.getType());
        StoreApplication.requestQueue.add(request);
    }


}
