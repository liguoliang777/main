package cn.ngame.store.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.GameLoadProgressBar;

/**
 * Created by Administrator on 2017/6/17 0017.
 */

public class TopicsDetailAdapter extends BaseAdapter {

    private Context context;
    private FragmentManager fm;
    private List<GameRankListBean.DataBean> list;
    private int type;

    private static Handler uiHandler = new Handler();

    public TopicsDetailAdapter(Context context, FragmentManager fm, List<GameRankListBean.DataBean> list, int type) {
        super();
        this.context = context;
        this.fm = fm;
        this.list = list;
        this.type = type;
    }

    public void setList(List<GameRankListBean.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final GameRankListBean.DataBean gameInfo = (list == null) ? null : list.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_topics_detail, parent, false);
            holder = new ViewHolder(context, fm);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.img_1);
            holder.gameBigLogoIv = (SimpleDraweeView) convertView.findViewById(R.id.game_big_logo);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
            holder.tv_size = (TextView) convertView.findViewById(R.id.text1);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
            holder.progressBar = (GameLoadProgressBar) convertView.findViewById(R.id.progress_bar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (gameInfo != null) {
            holder.update(gameInfo, type, position);
        }

        return convertView;
    }

    public static class ViewHolder {
        private Context context;
        private GameRankListBean.DataBean gameInfo;
        private SimpleDraweeView img, gameBigLogoIv;
        private TextView tv_title, tv_summary, tv_size;
        private RatingBar ratingBar;
        private GameLoadProgressBar progressBar;    //下载进度条
        private IFileLoad fileLoad;
        private FragmentManager fm;
        private Timer timer = new Timer();

        public ViewHolder(Context context, FragmentManager fm) {
            this.context = context;
            this.fm = fm;
            fileLoad = FileLoadManager.getInstance(context);
            init();
        }

        private void init() {

            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(gameInfo.getFilename(), gameInfo
                                    .getGameLink(), gameInfo.getPackages(), ConvUtil.NI(gameInfo.getVersionCode()));
                            progressBar.setLoadState(fileStatus);
                        }
                    });
                }
            }, 0, 500);
        }

        /**
         * 更新普通数据
         *
         * @param gameInfo 游戏信息
         */
        public void update(final GameRankListBean.DataBean gameInfo, int type, int position) {
            this.gameInfo = gameInfo;

            img.setImageURI(gameInfo.getGameLogo());

            gameBigLogoIv.setImageURI(gameInfo.getImgLink());//游戏大图


            String gameName = gameInfo.getGameName();
            tv_title.setText(gameName == null ? "" : gameName);

            String gameDesc = gameInfo.getGameDesc();
            tv_summary.setText(gameDesc == null ? "" : gameDesc);

            float percentage = gameInfo.getScoreLevel();
            tv_size.setText(percentage + "");
            ratingBar.setRating(percentage);

            //设置进度条状态
            progressBar.setLoadState(fileLoad.getGameFileLoadStatus(gameInfo.getFilename(), gameInfo.getGameLink(), gameInfo
                    .getPackages(), ConvUtil.NI(gameInfo.getVersionCode())));
            //必须设置，否则点击进度条后无法进行响应操作
            FileLoadInfo fileLoadInfo = new FileLoadInfo(gameInfo.getFilename(), gameInfo.getGameLink(), gameInfo.getMd5(),
                    gameInfo.getVersionCode(), gameInfo.getGameName(), gameInfo.getGameLogo(), gameInfo.getId(), FileLoadInfo
                    .TYPE_GAME);
            fileLoadInfo.setPackageName(gameInfo.getPackages());
            progressBar.setFileLoadInfo(fileLoadInfo);
            progressBar.setOnStateChangeListener(new ProgressBarStateListener(context, fm));
            progressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.toggle();
                }
            });

        }
    }
}
