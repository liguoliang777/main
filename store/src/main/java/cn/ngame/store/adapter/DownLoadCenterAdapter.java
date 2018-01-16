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

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.view.GameLoadProgressBar;
import cn.ngame.store.view.QuickAction;

import static cn.ngame.store.R.id.tv_title;


/**
 * 显示正在下载游戏的ListView控件适配器
 *
 * @author zeng
 * @since 2016-07-4
 */
public class DownLoadCenterAdapter extends BaseAdapter {
    private final QuickAction mItemClickQuickAction;

    //private static final String TAG = LoadIngLvAdapter.class.getSimpleName();

    private List<FileLoadInfo> fileInfoList;

    private Context context;
    private FragmentManager fm;
    private Handler uiHandler = new Handler();
    private int mPosition;

    public DownLoadCenterAdapter(Context context, FragmentManager fm, QuickAction mItemClickQuickAction) {
        super();
        this.context = context;
        this.mItemClickQuickAction = mItemClickQuickAction;
        this.fm = fm;
    }

    /**
     * 设置ListView中的数据
     *
     * @param fileInfoList 下载文件信息
     */
    public void setDate(List<FileLoadInfo> fileInfoList) {
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

    @Override
    public Object getItem(int position) {
        if (fileInfoList != null) {
            return fileInfoList.get(mPosition);
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
            //uiHandler = null;
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final FileLoadInfo fileInfo = (fileInfoList == null) ? null : fileInfoList.get(position);

        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder(context, fm);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_download_center, parent, false);
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.img_1);
            holder.tv_title = (TextView) convertView.findViewById(tv_title);
            // holder.tv_percent = (TextView) convertView.findViewById(R.id.tv_percent);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.tv_finished = (TextView) convertView.findViewById(R.id.tv_finished);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_length);
            holder.pb = (NumberProgressBar) convertView.findViewById(R.id.pb);
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
        private FileLoadInfo fileInfo;
        private ImageView more_bt;
        private SimpleDraweeView img;
        private TextView tv_title, tv_percent, tv_state, tv_finished, tv_size;
        private NumberProgressBar pb;
        private GameLoadProgressBar progressBar;    //下载进度条

        private Timer timer = new Timer();
        private IFileLoad fileLoad;

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
                    if (uiHandler == null) {
                        this.cancel();
                        return;
                    }
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(fileInfo.getName(), fileInfo.getUrl(),
                                    fileInfo.getPackageName(), fileInfo.getVersionCode());

                            progressBar.setLoadState(fileStatus);
                            long finished = fileInfo.getFinished();
                            long total = fileInfo.getLength();
                            if (finished == total) {
                                tv_finished.setText("");
                            } else {
                                tv_finished.setText(Formatter.formatFileSize(context, finished) + "/");
                            }
                            tv_size.setText(Formatter.formatFileSize(context, total));
                            if (fileStatus == null) {
                                return;
                            }
                            int process = (int) ((double) fileStatus.getFinished() / (double) fileStatus.getLength() * 100);
                            if (process >= 100) {
                                process = 0;
                            }
                            pb.setProgress(process);
                           /* String processStr = "";
                            if (process < 100) {
                                processStr = process + "%";
                            }*/
                            // tv_percent.setText(processStr);
                            progressBar.setVisibility(View.VISIBLE);
                            int status = fileStatus.getStatus();
                            if (status == GameFileStatus.STATE_DOWNLOAD) {
                                String url = fileInfo.getUrl();
                            /*    if (!url.toString().contains(".apk")) {
                                    //tv_percent.setText("");
                                    tv_state.setText("下载地址错误");
                                } else {*/
                                tv_state.setText("下载中...");
                                //}
                                pb.setVisibility(View.VISIBLE);
                            } else if (status == GameFileStatus.STATE_PAUSE) {
                                tv_state.setText("暂停中");
                                pb.setVisibility(View.VISIBLE);
                            } else if (status == GameFileStatus.STATE_HAS_DOWNLOAD) {
                                pb.setVisibility(View.INVISIBLE);
                                if (tv_size != null && tv_size.getText().toString().endsWith("KB")) {
                                    tv_state.setText("安装包异常");
                                } else {
                                    tv_state.setText("已完成");
                                }

                            } else {
                                pb.setVisibility(View.INVISIBLE);
                                tv_state.setText("获取资源失败");
                            }
                        }
                    });
                }
            }, 0, 300);
        }

        public void update(final FileLoadInfo fileInfo) {
            this.fileInfo = fileInfo;
            String gameName = fileInfo.getTitle();
            if (null != gameName) {
                tv_title.setText(gameName);
            }
            tv_finished.setText("");
            //tv_percent .setText("");
            tv_state.setText("");
            pb.setVisibility(View.INVISIBLE);
            tv_size.setText(Formatter.formatFileSize(context, fileInfo.getLength()));
            progressBar.setVisibility(View.INVISIBLE);
            //设置进度条状态
            progressBar.setLoadState(fileLoad.getGameFileLoadStatus(fileInfo.getName(), fileInfo.getPreviewUrl(), fileInfo
                    .getPackageName(), fileInfo.getVersionCode()));
            //必须设置，否则点击进度条后无法进行响应操作
            progressBar.setFileLoadInfo(fileInfo);
            progressBar.setOnStateChangeListener(new ProgressBarStateListener(context, fm));
            progressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.toggle();
                }
            });

            //加载图片
            img.setImageURI(fileInfo.getPreviewUrl());
        }

    }

}














