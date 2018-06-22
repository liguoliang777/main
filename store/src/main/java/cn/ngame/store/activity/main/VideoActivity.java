package cn.ngame.store.activity.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZUserAction;
import cn.jzvd.JZVideoPlayer;
import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.HubAdapter;
import cn.ngame.store.bean.PostsInfo;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.NgameJZVideoPlayerStandard;

/**
 * 圈子
 * Created by liguoliang on 2017/11/23 0023.
 */
@SuppressLint("WrongConstant")
public class VideoActivity extends Activity {
    protected static final String TAG = VideoActivity.class.getSimpleName();
    private LinearLayout ll_back;
    private VideoActivity mContext;
    private TextView titleTv;
    private RecyclerView mRecyclerView;
    private HubAdapter mAdapter;
    private TextView headerLastUpdateTv;
    private List<PostsInfo.DataBean> mDatas = new ArrayList<>();
    private NgameJZVideoPlayerStandard vedioPlay;
    private String videoUrl="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_rank);
        videoUrl=getIntent().getStringExtra("videoUrl");
        mContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        vedioPlay = findViewById(R.id.vedio_play);
        vedioPlay.setUp(
                videoUrl, JZVideoPlayer.SCREEN_WINDOW_FULLSCREEN, "");
        vedioPlay.startVideo();
        vedioPlay.onEvent(JZUserAction.ON_ENTER_FULLSCREEN);
        vedioPlay.startWindowFullscreen();
        vedioPlay.fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(mContext, "点击全屏");
                finish();
            }
        });
        vedioPlay.topContainer.setVisibility(View.GONE);
        vedioPlay.backButton.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onVideoBackClick(View view) {
        finish();
    }
}