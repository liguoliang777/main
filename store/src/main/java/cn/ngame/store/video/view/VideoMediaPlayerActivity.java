package cn.ngame.store.video.view;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.utils.Player;

/**
 * 使用MediaPlayer播放视频
 * Created by zeng on 2016/7/23.
 */
public class VideoMediaPlayerActivity extends BaseFgActivity {

    private static final int STATE_NO_PLAY      = 0;    //还未开始播放
    private static final int STATE_PLAY_ING     = 1;    //播放中
    private static final int STATE_PAUSE        = 2;    //暂停在


    private SurfaceView surfaceView;
    private ImageButton bt_play;
    private SeekBar seekBar;

    private Player player;

    private int currentPosition;  //当前播放进度

    private int playState = STATE_NO_PLAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_video_mediaplayer);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        bt_play = (ImageButton) findViewById(R.id.video_play);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);

        player = new Player(surfaceView,seekBar);

        setListener();
    }

    private void setListener(){

        //播放按钮监听器
        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(playState == STATE_NO_PLAY){
                    player.play("http://oss.ngame.cn/dianying/%E9%9D%92%E5%86%A5%E5%AE%9D%E5%89%91.mp4");
                }else if(playState == STATE_PLAY_ING){
                    bt_play.setSelected(false);
                    player.pause();
                }else if(playState == STATE_PAUSE){
                    player.reStart(1);
                    bt_play.setSelected(true);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentPosition = progress * player.mediaPlayer.getDuration() / seekBar.getMax();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.mediaPlayer.seekTo(currentPosition);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(player != null){
            player.stop();
        }
    }
}
