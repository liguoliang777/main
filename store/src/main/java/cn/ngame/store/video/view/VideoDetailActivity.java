package cn.ngame.store.video.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.activity.ReviewActivity;
import cn.ngame.store.activity.ReviewListActivity;
import cn.ngame.store.adapter.LvCommentAdapter;
import cn.ngame.store.bean.Comment;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.User;
import cn.ngame.store.bean.VideoInfo;
import cn.ngame.store.bean.VideoType;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.user.view.LoginActivity;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.view.PicassoImageView;

/**
 * 显示视频详情的界面
 * Created by zeng on 2016/5/18.
 */
public class VideoDetailActivity extends BaseFgActivity {

    public static final String TAG = VideoDetailActivity.class.getSimpleName();

    /** 评论相关*/
    private CommentNoScrollListView listView;
    private LvCommentAdapter adapter;
    private List<Comment> commentList;

    private ImageButton video_play;
    private PicassoImageView img_video;
    private RatingBar rating_bar;
    private TextView tv_title, tv_count, tv_score, tv_year,tv_time,tv_state,tv_type, tv_summary, tv_footer;

    private Button bt_review;

    private LinearLayout video_container;      //放置相关视频

    private LoadStateView loadStateView;       //加载等待控件

    private VideoInfo videoInfo;

    private long videoId = 0;
    private int pageSize = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_detail);

        video_play = (ImageButton) findViewById(R.id.video_play);
        listView = (CommentNoScrollListView) findViewById(R.id.listView);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_score = (TextView) findViewById(R.id.tv_score);

        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_type = (TextView) findViewById(R.id.tv_type);

        tv_summary = (TextView) findViewById(R.id.tv_summary);

        tv_footer = (TextView) findViewById(R.id.tv_footer);

        video_container = (LinearLayout) findViewById(R.id.video_container);

        img_video = (PicassoImageView) findViewById(R.id.img_video);
        rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        bt_review = (Button) findViewById(R.id.review);

        Button left_but = (Button) findViewById(R.id.left_but);
        left_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        videoId = getIntent().getLongExtra("id", 0);
        if (videoId > 0) {
            getVideoInfo(videoId); //获取视频详情
            getCommentData();
        }

        adapter = new LvCommentAdapter(this, null);
        listView.setAdapter(adapter);

        loadStateView = (LoadStateView) findViewById(R.id.loadStateView);
        loadStateView.setReLoadListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStateView.setState(LoadStateView.STATE_ING);
                getVideoInfo(videoId); //重新加载
            }
        });

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        videoId = intent.getLongExtra("id", 0);
        if (videoId > 0) {
            getVideoInfo(videoId); //获取视频详情
            getCommentData();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getCommentData();
    }

    /** 获取视频详情 */
    private void getVideoInfo(final long id) {

        String url = Constant.WEB_SITE + Constant.URL_VIDEO_DETAIL;
        Response.Listener<JsonResult<VideoInfo>> successListener = new Response.Listener<JsonResult<VideoInfo>>() {
            @Override
            public void onResponse(JsonResult<VideoInfo> result) {

                if (result == null) {
                    loadStateView.setVisibility(View.VISIBLE);
                    loadStateView.setState(LoadStateView.STATE_END);
                    return;
                }

                if (result.code == 0) {
                    videoInfo = result.data;

                    bt_review.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            User user = StoreApplication.user;
                            if(user == null){
                                Intent intent = new Intent(VideoDetailActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(VideoDetailActivity.this, ReviewActivity.class);
                                intent.putExtra("categoryId", 2);
                                intent.putExtra("targetId", videoInfo.id);
                                startActivity(intent);
                            }
                        }
                    });

                    //img_video.setImageUrl(videoInfo.videoImageLink,90f,135f,R.drawable.default_video);
                    img_video.setImageUrl(videoInfo.videoImageLink,120f,85f,R.drawable.ic_def_logo_412_200);

                    String title = videoInfo.videoName == null ? "" : videoInfo.videoName;
                    if(title.length() > 9){
                        title = title.substring(0,9);
                    }
                    tv_title.setText(title);

                    if(videoInfo.percentage<=0){
                        tv_score.setText("0分");
                    }else {
                        tv_score.setText(videoInfo.percentage+"分");
                    }

                    tv_count.setText((int)videoInfo.commentPeople+"人评论");

                    rating_bar.setRating(videoInfo.percentage);
                    tv_summary.setText(videoInfo.videoIntroduce);

                    if(videoInfo.videoTypeList != null && videoInfo.videoTypeList.size() > 0){
                        String videoType = "";
                        for(VideoType t : videoInfo.videoTypeList){
                            videoType += t.text+"/";
                        }
                        if(videoType.length() > 1){
                            videoType = videoType.substring(0,videoType.length()-1);
                        }
                        tv_type.setText(videoType);
                    }else {
                        tv_type.setText("");
                    }

                    String address = videoInfo.produceCountry;
                    if(address != null && address.length() > 2){
                        address = address.substring(0,2);
                    }
                    tv_state.setText(address);

                    String duration = videoInfo.duration;
                    if(duration != null && duration.length() > 5){
                        duration = duration.substring(0,5);
                    }
                    tv_time.setText(duration);

                    String time = videoInfo.releaseDate;
                    if(time != null && time.length() > 5){
                        time = time.substring(0,5);
                    }
                    tv_year.setText(time);

                    video_play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(videoInfo!= null && videoInfo.videoLink != null && videoInfo.videoLink.length() > 0){

                                /*new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //添加播放记录
                                        WatchRecord record = new WatchRecord((int)videoInfo.id,videoInfo.videoName,videoInfo.videoImageLink,100);
                                        WatchRecordModel model = new WatchRecordModel(VideoDetailActivity.this);
                                        model.addWatchRecord(record);
                                    }
                                }).start();*/

                                Intent intent;
                                if(videoInfo.isPanorama == 1){
                                    intent = new Intent(VideoDetailActivity.this, VideoPlayActivity.class);
                                }else {
                                    intent = new Intent(VideoDetailActivity.this, VideoPlay3DActivity.class);
                                }
                                Log.e(TAG,videoInfo.videoLink);
                                intent.putExtra("url", videoInfo.videoLink);
                                intent.putExtra("videoInfo",videoInfo);
                                startActivity(intent);
                            }else{
                                Toast.makeText(VideoDetailActivity.this,"视频链接为空，无法播放!",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    createVideoAboutView();

                    loadStateView.setVisibility(View.GONE);
                    loadStateView.setState(LoadStateView.STATE_END);

                } else {

                    Log.d(TAG,"HTTP请求成功：服务端返回错误！"+result.msg);
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
                loadStateView.isShowLoadBut(true);
                loadStateView.setVisibility(View.VISIBLE);
                loadStateView.setState(LoadStateView.STATE_END);
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<VideoInfo>> versionRequest = new GsonRequest<JsonResult<VideoInfo>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<VideoInfo>>() {}.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("videoId", String.valueOf(id));
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    private void createVideoAboutView() {

        if (videoInfo != null && videoInfo.videoList3 != null && videoInfo.videoList3.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(this);
            video_container.removeAllViews();
            int i = 0;
            for (final VideoInfo aboutInfo : videoInfo.videoList3) {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.width = 0;
                params.weight = 1;
                if (i++ > 0) {
                    params.setMargins(CommonUtil.dip2px(VideoDetailActivity.this, 5), 0, 0, 0);
                }
                LinearLayout aboutLayout = (LinearLayout) inflater.inflate(R.layout.item_video_about, null);
                aboutLayout.setLayoutParams(params);
                PicassoImageView img = (PicassoImageView) aboutLayout.findViewById(R.id.img_1);
                img.setImageUrl(aboutInfo.videoImageLink,R.drawable.ic_def_logo_412_200);

                TextView tv = (TextView) aboutLayout.findViewById(R.id.tv_title);
                String title = aboutInfo.videoName == null ? "" : aboutInfo.videoName;
                title = title.length() > 6 ? title.substring(0, 6) : title;
                tv.setText(title);
                aboutLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "-----------------------------about: " + aboutInfo.id);
                        Intent intent = new Intent(VideoDetailActivity.this, VideoDetailActivity.class);
                        intent.putExtra("id", aboutInfo.id);
                        startActivity(intent);
                    }
                });
                video_container.addView(aboutLayout);
            }
        }
    }

    /**
     * 获取评论数据
     */
    private void getCommentData() {

        String url = Constant.WEB_SITE + Constant.URL_COMMENT_LIST;
        Response.Listener<JsonResult<List<Comment>>> successListener = new Response.Listener<JsonResult<List<Comment>>>() {
            @Override
            public void onResponse(JsonResult<List<Comment>> result) {

                if (result != null && result.code == 0) {
                    commentList = result.data;

                    if(result.totals > pageSize){
                        tv_count.setText(result.totals+"人评论");
                        tv_footer.setText("全部评论（"+result.totals+"）");
                        tv_footer.setVisibility(View.VISIBLE);
                        tv_footer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(VideoDetailActivity.this, ReviewListActivity.class);
                                intent.putExtra("Id",videoInfo.id);
                                intent.putExtra("Type",2);
                                startActivity(intent);
                            }
                        });
                    }else {
                        tv_footer.setVisibility(View.GONE);
                        tv_count.setText("0人评论");
                    }

                    adapter.clean();
                    adapter.setData(commentList);
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
        Request<JsonResult<List<Comment>>> request = new GsonRequest<JsonResult<List<Comment>>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<List<Comment>>>() {}.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("commentType", "2");
                params.put("code", String.valueOf(videoId));
                params.put("pageIndex", String.valueOf(1));
                params.put("pageSize", String.valueOf(pageSize));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);

    }
}
