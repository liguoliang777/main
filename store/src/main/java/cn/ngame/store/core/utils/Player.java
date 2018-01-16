package cn.ngame.store.core.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 播放器控制类
 * Created by zeng on 2016/7/23.
 */
public class Player implements OnBufferingUpdateListener,OnCompletionListener,
        OnPreparedListener,SurfaceHolder.Callback{

    public static final String TAG = Player.class.getSimpleName();

    private int videoWidth;
    private int videoHeight;
    public  MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;

    private SeekBar seekBar;
    private Timer timer = new Timer();

    public Player(SurfaceView surfaceView,SeekBar seekBar){
        this.seekBar = seekBar;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        timer.schedule(timerTask,0,1000);
    }

    /*******************************************************
     * 通过定时器和Handler来更新进度条
     ******************************************************/
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if(mediaPlayer == null){
                return;
            }
            if(mediaPlayer.isPlaying() && seekBar.isPressed() == false){
                handler.sendEmptyMessage(0);
            }
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();

            if(duration > 0){
                long pos = seekBar.getMax() * position / duration;
                seekBar.setProgress((int) pos);
            }
        }
    };

    /**
     * 开始播放视频  调用了start方法
     * @param videoUrl 播放视频的url地址
     */
    public void play(String videoUrl){

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();
            //prepare后自动播放
            //mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停后继续播放
     * @param position 继续播放的位置
     */
    public void reStart(int position){

        mediaPlayer.seekTo(position);
    }

    public void pause(){
        mediaPlayer.pause();
    }

    public void stop(){
        if(mediaPlayer != null){
            mediaPlayer.start();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG,"surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG,"surfaceDestroyed");
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
        int currentProgress  = seekBar.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
        Log.d(TAG,currentProgress+"% play  "+percent + "% buffer");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        videoWidth = mediaPlayer.getVideoWidth();
        videoHeight = mediaPlayer.getVideoHeight();
        if(videoHeight != 0 && videoWidth != 0){
            mp.start();
        }
        Log.d(TAG,"onPrepared");
    }
}
