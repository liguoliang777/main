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
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.GameLoadProgressBar;
import cn.ngame.store.view.QuickAction;

import static cn.ngame.store.R.id.tv_title;


/**
 * 显示正在下载游戏的ListView控件适配器
 *
 * @author zeng
 * @since 2016-07-4
 */
public class LikeFragmentAdapter extends BaseAdapter {
    private final QuickAction mItemClickQuickAction;
    private Timer timer = new Timer();
    private List<TimerTask> timerTasks;
    private List<LikeListBean.DataBean.GameListBean> fileInfoList;
    private Context context;
    private FragmentManager fm;
    private Handler uiHandler = new Handler();
    private int mPosition;

    public LikeFragmentAdapter(Context context, FragmentManager fm,
                               QuickAction mItemClickQuickAction, List<TimerTask> timerTasks) {
        super();
        this.context = context;
        this.mItemClickQuickAction = mItemClickQuickAction;
        this.fm = fm;
        this.timerTasks = timerTasks;
    }

    /**
     * 设置ListView中的数据
     *
     * @param fileInfoList 下载文件信息
     */
    public void setDate(List<LikeListBean.DataBean.GameListBean> fileInfoList) {
        //uiHandler = new Handler();
        this.fileInfoList = fileInfoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (fileInfoList != null) {
            return fileInfoList.size();
        }
        return 0;
    }

    public int getItemGameId() {
        return mPosition;
    }

    @Override
    public Object getItem(int position) {
        if (fileInfoList != null) {
            return fileInfoList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clean() {
        if (fileInfoList != null) {
            fileInfoList.clear();
            uiHandler = null;
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final LikeListBean.DataBean.GameListBean fileInfo = (fileInfoList == null) ? null : fileInfoList.get(position);

        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder(context, fm);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_like, parent, false);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.img_1);
            holder.tv_title = (TextView) convertView.findViewById(tv_title);
            holder.versionTv = (TextView) convertView.findViewById(R.id.tv_version_time);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_length);
            holder.progressBar = (GameLoadProgressBar) convertView.findViewById(R.id.progress_bar);
            holder.more_bt = (ImageView) convertView.findViewById(R.id.manager_installed_more_bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (fileInfo != null) {
            holder.update(fileInfo);
            holder.more_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosition = position;
                    if (null != mItemClickQuickAction) {
                        mItemClickQuickAction.show(v);
                    }
                }
            });
        }

        return convertView;
    }

    /**
     * 用于保存ListView中重用的item视图的引用
     *
     * @author flan
     * @date 2015年10月28日
     */
    public class ViewHolder {

        private Context context;
        private FragmentManager fm;
        private LikeListBean.DataBean.GameListBean gameInfo;
        private ImageView more_bt;
        private SimpleDraweeView img;
        private TextView tv_title, tv_size, versionTv;
        private GameLoadProgressBar progressBar;    //下载进度条
        private IFileLoad fileLoad;

        public ViewHolder(Context context, FragmentManager fm) {
            this.context = context;
            this.fm = fm;
            fileLoad = FileLoadManager.getInstance(context);
            init();
        }

        private void init() {

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (uiHandler == null) {
                        this.cancel();
                        return;
                    }
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setLoadState(fileLoad.getGameFileLoadStatus(gameInfo.getFilename(), gameInfo
                                    .getGameLink(), gameInfo.getPackages(), ConvUtil.NI(gameInfo.getVersionCode())));
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });
                }
            };
            timerTasks.add(task);
            timer.schedule(task, 0, 300);
        }

        public void update(final LikeListBean.DataBean.GameListBean gameInfo) {
            this.gameInfo = gameInfo;
            String gameName = gameInfo.getGameName();
            if (null != gameName) {
                tv_title.setText(gameName);
            }
            tv_size.setText(Formatter.formatFileSize(context, gameInfo.getGameSize()));
            String timeLike = new SimpleDateFormat("yyyy-MM-dd").format(new Date(gameInfo
                    .getUpdateTime()));
            versionTv.setText("V" + gameInfo.getVersionName() + " / " + timeLike);
            progressBar.setVisibility(View.INVISIBLE);
            //设置进度条状态
            progressBar.setLoadState(fileLoad.getGameFileLoadStatus(gameInfo.getFilename(), gameInfo
                    .getGameLink(), gameInfo.getPackages(), ConvUtil.NI(gameInfo.getVersionCode())));
            //必须设置，否则点击进度条后无法进行响应操作
            FileLoadInfo fileLoadInfo = new FileLoadInfo(gameInfo.getFilename(), gameInfo.getGameLink(), gameInfo
                    .getMd5(), gameInfo.getVersionCode(), gameInfo.getGameName(), gameInfo.getGameLogo(), gameInfo
                    .getId(), FileLoadInfo.TYPE_GAME);
            fileLoadInfo.setPackageName(gameInfo.getPackages());
            progressBar.setFileLoadInfo(fileLoadInfo);
            progressBar.setOnStateChangeListener(new ProgressBarStateListener(context, fm));
            progressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.toggle();
                }
            });

            //加载图片
            img.setImageURI(gameInfo.getGameLogo());
        }

    }

}














