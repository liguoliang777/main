/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.GameLoadProgressBar;

/**
 * 手柄游戏的ListView控件适配器
 *
 * @author zeng
 * @since 2016-05-16
 */
public class GameListAdapter extends BaseAdapter {

    //private static final String TAG = LvSbGameAdapter.class.getSimpleName();

    private List<GameInfo> gameInfoList;

    private Context context;
    private FragmentManager fm;
    private static Handler uiHandler = new Handler();

    public GameListAdapter(Context context, FragmentManager fm) {
        super();
        this.fm = fm;
        this.context = context;
    }

    /**
     * 设置ListView中的数据
     *
     * @param gameInfos 游戏数据
     */
    public void setDate(List<GameInfo> gameInfos) {

        if (gameInfoList == null || gameInfoList.size() <= 0) {
            gameInfoList = gameInfos;
        } else {
            gameInfoList.addAll(gameInfos);
        }

    }

    @Override
    public int getCount() {

        if (gameInfoList != null) {
            return gameInfoList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (gameInfoList != null) {
            return gameInfoList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clean() {
        if (gameInfoList != null)
            gameInfoList.clear();
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final GameInfo gameInfo = (gameInfoList == null) ? null : gameInfoList.get(position);

        ViewHolder holder;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_game_2, parent, false);

            holder = new ViewHolder(context, fm);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.img_1);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_size = (TextView) convertView.findViewById(R.id.text1);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
            holder.progressBar = (GameLoadProgressBar) convertView.findViewById(R.id.progress_bar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (gameInfo != null)
            holder.update(gameInfo);

        return convertView;
    }

    /**
     * 用于保存ListView中重用的item视图的引用
     *
     * @author flan
     * @since 2015年10月28日
     */
    public static class ViewHolder {

        private Context context;
        private GameInfo gameInfo;

        private SimpleDraweeView img;
        private TextView tv_title, tv_size;
        private RatingBar ratingBar;
        private GameLoadProgressBar progressBar;    //下载进度条
        private FragmentManager fm;

        private IFileLoad fileLoad;

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
                            GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(gameInfo.filename, gameInfo.gameLink,
                                    gameInfo.packages, ConvUtil.NI(gameInfo.versionCode));
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
        public void update(final GameInfo gameInfo) {

            this.gameInfo = gameInfo;

            img.setImageURI(gameInfo.gameLogo);

            String gameName = gameInfo.gameName;
            if (null != gameName && !"".equals(gameName)) {
                //gameName = gameName.length() > 15 ? gameName.substring(0, 15) : gameName;
                tv_title.setText(gameName);
            }

           /* long gameSize = gameInfo.gameSize;
            String gameSizeStr = TextUtil.formatFileSize(gameSize);*/

            tv_size.setText(gameInfo.percentage + "");

            ratingBar.setRating(gameInfo.percentage);//星星

            //设置进度条状态
            progressBar.setLoadState(fileLoad.getGameFileLoadStatus(gameInfo.filename, gameInfo.gameLink, gameInfo.packages,
                    ConvUtil.NI(gameInfo.versionCode)));
            //必须设置，否则点击进度条后无法进行响应操作
            FileLoadInfo fileLoadInfo = new FileLoadInfo(gameInfo.filename, gameInfo.gameLink, gameInfo.md5, ConvUtil.NI
                    (gameInfo.versionCode), gameInfo.gameName, gameInfo.gameLogo, gameInfo.id, FileLoadInfo.TYPE_GAME);
            fileLoadInfo.setPackageName(gameInfo.packages);
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














