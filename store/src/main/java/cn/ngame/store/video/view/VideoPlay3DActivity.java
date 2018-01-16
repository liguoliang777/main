package cn.ngame.store.video.view;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.bean.VideoInfo;
import cn.ngame.store.local.model.WatchRecord;
import cn.ngame.store.local.model.WatchRecordModel;
import cn.ngame.store.receiver.ScreenListener;


/**
 * 播放普通3D视频的页面
 * Created by zeng on 2016/7/5.
 */
public class VideoPlay3DActivity extends Activity{

    public static final String TAG = VideoPlay3DActivity.class.getSimpleName();

    private RelativeLayout toolBar;
    private LinearLayout loading;

    private VideoView videoView;
    private ImageButton bt_play;
    private TextView tv_current,tv_length;
    private SeekBar seekBar;

    private boolean toolBarIsShow = false;
    private boolean isPlaying;

    private VideoInfo videoInfo;

    private long playPosition = 0;
    private long videoLength = 0;

    private ScreenListener screenListener;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_video_play_360);

        toolBar = (RelativeLayout) findViewById(R.id.layout_1);
        loading = (LinearLayout) findViewById(R.id.loading);

        bt_play = (ImageButton) findViewById(R.id.video_play);
        bt_play.setSelected(true);
        tv_current = (TextView) findViewById(R.id.text1);
        tv_length = (TextView) findViewById(R.id.text2);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);

        videoView = (VideoView) findViewById(R.id.video_view);


        Intent intent = getIntent();
        String playUrl = intent.getStringExtra("url");
        videoInfo = (VideoInfo) intent.getSerializableExtra("videoInfo");
        if(playUrl == null || playUrl.length() <= 0){

            Toast.makeText(this,"视频播放链接错误！",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //Uri uri = Uri.parse("http://oss.ngame.cn/1467688049264.mp4");
        //Uri uri = Uri.parse("http://oss.ngame.cn/dianying/%E5%8F%98%E5%BD%A2%E9%87%91%E5%88%9A3_0_Mux.mp4");
        Uri uri = Uri.parse(playUrl);
        videoView.setVideoURI(uri);
        videoView.start();

        setListener();
        play(0);

        // 开始线程，更新进度条的刻度
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if(isPlaying){
                    int current = videoView.getCurrentPosition();
                    playPosition = current;
                    seekBar.setProgress(current);

                    final String timeStr = secToTime(current/1000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_current.setText(timeStr);
                        }
                    });
                }
                //Log.d(TAG,"----------->>> 正在循环 ");
            }
        },0,500);
    }

    private void setListener(){

        //加载错误监听器
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if(what != 1){
                    Toast.makeText(VideoPlay3DActivity.this,"出现异常 what : "+what +" extra : "+extra,Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 设置进度条的最大进度为视频流的最大播放时长
                int length = mp.getDuration();
                videoLength = length;
                seekBar.setMax(length);
                // 设置视频最大时长
                tv_length.setText(secToTime(length/1000));
                loading.setVisibility(View.GONE);
            }
        });

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START){
                    loading.setVisibility(View.VISIBLE);
                }else if(what == MediaPlayer.MEDIA_INFO_BUFFERING_END){
                    //此接口每次回调完START就回调END,若不加上判断就会出现缓冲图标一闪一闪的卡顿现象
                    if(mp.isPlaying()){
                        loading.setVisibility(View.GONE);
                    }
                }
                return true;
            }
        });

        //播放完毕监听器
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                pause();
            }
        });

        //触摸屏幕监听器
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(toolBarIsShow){
                    toolBar.setVisibility(View.GONE);
                    toolBarIsShow = false;
                }else {
                    toolBar.setVisibility(View.VISIBLE);
                    toolBarIsShow = true;
                }
                return false;
            }
        });
        //监听拖动进度条
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 当进度条停止修改的时候触发
                // 取得当前进度条的刻度
                int progress = seekBar.getProgress();
                playPosition = progress;
                if(videoView != null && videoView.isPlaying()){
                    // 设置当前播放的位置
                    videoView.seekTo(progress);

                    String timeStr = "00:00:00";
                    if(progress >= 0){
                        timeStr = secToTime(progress/1000);
                    }
                    tv_current.setText(timeStr);
                }
            }
        });
        //播放按钮
        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(videoView.isPlaying()){
                    if(videoView.canPause()){
                        bt_play.setSelected(false);
                        pause();
                    }
                }else {
                    videoView.start();
                    isPlaying = true;
                    bt_play.setSelected(true);
                }
            }
        });


        screenListener = new ScreenListener(this);
        screenListener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {

            }
            @Override
            public void onScreenOff() {
                pause();
            }
            @Override
            public void onUserPresent() {}
        });
    }

    /**
     * 播放操作
     * @param m 毫秒数
     */
    private void play(int m){

        // 按照初始位置播放
        videoView.seekTo(m);
        isPlaying = true;
    }

    /**
     * 暂停播放
     */
    private void pause(){
        videoView.pause();
        isPlaying = false;
    }

    @Override
    protected void onDestroy() {

        timer.cancel();
        if(screenListener != null){
            screenListener.unregisterListener();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(videoInfo != null){
                    //添加播放记录
                    WatchRecord record = new WatchRecord((int)videoInfo.id,videoInfo.videoName,videoInfo.videoImageLink,playPosition,videoLength);
                    WatchRecordModel model = new WatchRecordModel(VideoPlay3DActivity.this);
                    model.addWatchRecord(record);
                }
            }
        }).start();

        super.onDestroy();
    }


    private String secToTime(int time) {

        String timeStr;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:"+ unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    private String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else{
            retStr = "" + i;
        }
        return retStr;
    }

}
