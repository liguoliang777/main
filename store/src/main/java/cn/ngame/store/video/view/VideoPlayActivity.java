package cn.ngame.store.video.view;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.VideoInfo;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.local.model.WatchRecord;
import cn.ngame.store.local.model.WatchRecordModel;

/**
 * 用于VR视频播放的界面
 * Created by zeng on 2016/5/25.
 */
public class VideoPlayActivity extends BaseFgActivity {

    private static final String TAG = VideoPlayActivity.class.getSimpleName();

    private static final String STATE_IS_PAUSED = "isPaused";
    private static final String STATE_PROGRESS_TIME = "progressTime";
    private static final String STATE_VIDEO_DURATION = "videoDuration";
    public static final int LOAD_VIDEO_STATUS_UNKNOWN = 0;
    public static final int LOAD_VIDEO_STATUS_SUCCESS = 1;
    public static final int LOAD_VIDEO_STATUS_ERROR = 2;

    private int loadVideoStatus = LOAD_VIDEO_STATUS_UNKNOWN;

    public int getLoadVideoStatus() {
        return loadVideoStatus;
    }

    private Uri fileUri;
    private VideoLoaderTask backgroundVideoLoaderTask;

    private VrVideoView videoWidgetView;

    //private SeekBar seekBar;

    private String url;
    private VideoInfo videoInfo;

    private long videoLenght;

    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_video_play);

        videoWidgetView = (VrVideoView) findViewById(R.id.video_view);
        videoWidgetView.setEventListener(new ActivityEventListener());

        //seekBar = (SeekBar) findViewById(R.id.seek_bar);
        //seekBar.setOnSeekBarChangeListener(new SeekBarListener());

        url = getIntent().getStringExtra("url");
        videoInfo = (VideoInfo) getIntent().getSerializableExtra("videoInfo");

        loadVideoStatus = LOAD_VIDEO_STATUS_UNKNOWN;

        handleIntent(getIntent());

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume the 3D rendering.
        videoWidgetView.resumeRendering();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Prevent the view from rendering continuously when in the background.
        videoWidgetView.pauseRendering();
        // If the video is playing when onPause() is called, the default behavior will be to pause
        // the video and keep it paused when onResume() is called.
        isPaused = true;
    }

    @Override
    protected void onDestroy() {
        // Destroy the widget and free memory.
        videoWidgetView.shutdown();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(videoInfo != null){
                    //添加播放记录
                    WatchRecord record = new WatchRecord(videoInfo.id,videoInfo.videoName,videoInfo.videoImageLink,0,videoLenght);
                    WatchRecordModel model = new WatchRecordModel(VideoPlayActivity.this);
                    model.addWatchRecord(record);
                }
            }
        }).start();
        super.onDestroy();
    }

    private void togglePause() {
        if (isPaused) {
            videoWidgetView.playVideo();
        } else {
            videoWidgetView.pauseVideo();
        }
        isPaused = !isPaused;
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Log.i(TAG, "ACTION_VIEW Intent received");

            fileUri = intent.getData();
            if (fileUri == null) {
                Log.w(TAG, "No data uri specified. Use \"-d /path/filename\".");
            } else {
                Log.i(TAG, "Using file " + fileUri.toString());
            }
        } else {
            Log.i(TAG, "Intent is not ACTION_VIEW. Using the default video.");
            fileUri = null;
        }

        // Load the bitmap in a background thread to avoid blocking the UI thread. This operation can
        // take 100s of milliseconds.
        if (backgroundVideoLoaderTask != null) {
            // Cancel any task from a previous intent sent to this activity.
            backgroundVideoLoaderTask.cancel(true);
        }
        backgroundVideoLoaderTask = new VideoLoaderTask();
        backgroundVideoLoaderTask.execute(fileUri);
    }

    /**
     * When the user manipulates the seek bar, update the video position.
     */
    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                videoWidgetView.seekTo(progress);
            } // else this was from the ActivityEventHandler.onNewFrame()'s seekBar.setProgress update.
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    }

    /**
     * Listen to the important events from widget.
     */
    private class ActivityEventListener extends VrVideoEventListener {
        /**
         * Called by video widget on the UI thread when it's done loading the video.
         */
        @Override
        public void onLoadSuccess() {
            Log.i(TAG, "Sucessfully loaded video " + videoWidgetView.getDuration());
            loadVideoStatus = LOAD_VIDEO_STATUS_SUCCESS;
            videoLenght = videoWidgetView.getDuration();
            //seekBar.setMax((int) videoWidgetView.getDuration());
        }

        /**
         * Called by video widget on the UI thread on any asynchronous error.
         */
        @Override
        public void onLoadError(String errorMessage) {
            // An error here is normally due to being unable to decode the video format.
            loadVideoStatus = LOAD_VIDEO_STATUS_ERROR;
            //Toast.makeText(VideoPlayActivity.this, "Error loading video: " + errorMessage, Toast.LENGTH_LONG).show();
            Toast.makeText(VideoPlayActivity.this, "亲，视频解码器暂不支持本视频哦！", Toast.LENGTH_LONG).show();

            Log.e(TAG, "Error loading video: " + errorMessage);
        }

        @Override
        public void onClick() {
            togglePause();
        }

        /**
         * Update the UI every frame.
         */
        @Override
        public void onNewFrame() {
            //seekBar.setProgress((int) videoWidgetView.getCurrentPosition());
        }

        /**
         * Make the video play in a loop. This method could also be used to move to the next video in
         * a playlist.
         */
        @Override
        public void onCompletion() {
            videoWidgetView.seekTo(0);
        }
    }

    /**
     * Helper class to manage threading.
     */
    class VideoLoaderTask extends AsyncTask<Uri, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Uri... uri) {
            try {
                if (uri == null || uri.length < 1 || uri[0] == null) {

                    if(url != null && url.length() > 0){
                        videoWidgetView.loadVideo(Uri.parse(url),null);
                    }

                    //videoWidgetView.loadVideoFromAsset("hgrw.mp4");
                    //videoWidgetView.loadVideo(Uri.parse("http://192.168.2.113:8080/ngame/download/hgrw.mp4"));
                    //videoWidgetView.loadVideo(Uri.parse("http://www.chuangsike.cn/video/hgrw.mp4"));
                    //videoWidgetView.loadVideo(Uri.parse("http://localhost:8080/vr/yzw.mp4"));
                    //--videoWidgetView.loadVideo(Uri.parse("http://oss.ngame.cn/1466404000919.mp4"));
                    //videoWidgetView.loadVideo(Uri.parse("http://oss.ngame.cn/1466404187704.mp4"));
                    //videoWidgetView.loadVideo(Uri.parse("http://video1.nytimes.com/video/360-demo/cool.mp4"));
                    //videoWidgetView.loadVideo(Uri.parse("http://oss.ngame.cn/1466480101870.mp4"));//林忆莲
                    //videoWidgetView.loadVideo(Uri.parse("http://oss.ngame.cn/1466480802875.mp4"));//new雨中舞
                    //videoWidgetView.loadVideo(Uri.parse("http://oss.ngame.cn/1466487257538.mp4"));//国际空间站
                    //videoWidgetView.loadVideo(Uri.parse("http://www.chuangsike.cn/video/gjkjz.mp4"));//国际空间站
                    //videoWidgetView.loadVideo(Uri.parse("http://www.chuangsike.cn/video/yzwnew.mp4"));//国际空间站
                    //videoWidgetView.loadVideo(Uri.parse("http://oss.ngame.cn/1466490148973.mp4"));//兔女郎
                } else {
                    videoWidgetView.loadVideo(uri[0],null);
                }
            } catch (IOException e) {
                // An error here is normally due to being unable to locate the file.
                loadVideoStatus = LOAD_VIDEO_STATUS_ERROR;
                // Since this is a background thread, we need to switch to the main thread to show a toast.
                videoWidgetView.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(VideoPlayActivity.this, "Error opening file. ", Toast.LENGTH_LONG).show();
                    }
                });
                Log.e(TAG, "Could not open video: " + e);
            }

            return true;
        }
    }

}
