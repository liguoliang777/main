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

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBean;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.GameLoadProgressBar;

public class Ranking012345Adapter extends BaseAdapter {
    private Activity context;
    private FragmentManager fm;
    private List<LikeListBean.DataBean.GameListBean> list;
    private int type;
    private static Handler uiHandler = new Handler();

    public Ranking012345Adapter(Activity context, FragmentManager fm, List<LikeListBean.DataBean
            .GameListBean> list, int type) {
        super();
        this.context = context;
        this.fm = fm;
        this.list = list;
        this.type = type;
    }

    public void setList(List<LikeListBean.DataBean.GameListBean> list) {
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
        final LikeListBean.DataBean.GameListBean gameInfo = (list == null) ? null : list.get
                (position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ranking_list_item,
                    parent, false);
            holder = new ViewHolder(context, fm);
            holder.img01234 = (SimpleDraweeView) convertView.findViewById(R.id.rank01234_list_item_sdv);
            holder.tv_position = (TextView) convertView.findViewById(R.id.tv_position);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_percentage = (TextView) convertView.findViewById(R.id.text1);
            holder.downloadNumTv = (TextView) convertView.findViewById(R.id
                    .rank_list_item_download_num);
            holder.tank01234LLay = (LinearLayout) convertView.findViewById(R.id.tank01234_ll);
            holder.moreBt = (ImageView) convertView.findViewById(R.id.rank_more_bt);
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
        private Activity context;
        private LikeListBean.DataBean.GameListBean gameInfo;
        private SimpleDraweeView img01234;
        private TextView tv_position, tv_title, tv_percentage;
        private GameLoadProgressBar progressBar;    //下载进度条
        private TextView tv_shoubing, tv_vr, tv_toukong, tv_yun_duan;
        private IFileLoad fileLoad;
        private FragmentManager fm;
        private ImageView moreBt;
        private Timer timer = new Timer();
        private SpannableString textSpan;
        private PopupWindow popupWindow;
        private LinearLayout tank01234LLay;
        private TextView textView, downloadNumTv;
        private LinearLayout.LayoutParams params;
        private String typeNameStr;
    /*    private String itemTag = "原生手柄";
        private String itemTag1 = "云适配";
        private String itemTag2 = "破解";
        private String itemTag3 = "汉化";
        private String itemTag4 = "特色游戏";*/

        public ViewHolder(Activity context, FragmentManager fm) {
            this.context = context;
            this.fm = fm;
            fileLoad = FileLoadManager.getInstance(context);
            //init();
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                    .LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_VERTICAL;
            params.setMargins(0, 0, 18, 0);
        }

        private void init() {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(gameInfo
                                    .getFilename(), gameInfo
                                    .getGameLink(), gameInfo.getPackages(), ConvUtil.NI(gameInfo
                                    .getVersionCode()));
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
        public void update(final LikeListBean.DataBean.GameListBean gameInfo, int type, int
                position) {
            this.gameInfo = gameInfo;
            final String gameName = gameInfo.getGameName();
            if (!"".equals(gameName)) {
                tv_title.setText(gameName);
            }
            //更多按钮
            moreBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    moreBt.setSelected(true);

                    View popView = LayoutInflater.from(context).inflate(R.layout
                            .layout_rank_more_popup, null);
                    //===================进度条==========================================
                    progressBar = (GameLoadProgressBar) popView.findViewById(R.id.progress_bar);
                    //设置进度条状态
                    final GameFileStatus fileLoadStatus = fileLoad.getGameFileLoadStatus(gameInfo
                            .getFilename(), gameInfo
                            .getGameLink(), gameInfo.getPackages(), ConvUtil.NI(gameInfo
                            .getVersionCode()));
                    progressBar.setLoadState(fileLoadStatus);
                    //必须设置，否则点击进度条后无法进行响应操作
                    FileLoadInfo fileLoadInfo = new FileLoadInfo(gameInfo.getFilename(), gameInfo
                            .getGameLink(), gameInfo
                            .getMd5(), gameInfo.getVersionCode(), gameInfo.getGameName(),
                            gameInfo.getGameLogo(), gameInfo
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
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setLoadState(fileLoad.getGameFileLoadStatus
                                            (gameInfo.getFilename(), gameInfo
                                                            .getGameLink(), gameInfo.getPackages(),
                                                    ConvUtil.NI
                                                            (gameInfo.getVersionCode())));
                                }
                            });
                        }
                    }, 0, 500);
                    //============================================================================
                    //版本信息
                    TextView versionTv = (TextView) popView.findViewById(R.id.rank_popupp_vesion);
                    textSpan = new SpannableString("版本\n" + gameInfo.getVersionName());
                    textSpan.setSpan(new TextAppearanceSpan(context, R.style.style_14dp_989999),
                            0, 2, Spanned
                                    .SPAN_EXCLUSIVE_EXCLUSIVE);
                    versionTv.setText(textSpan);

                    //更新的时间
                    TextView updateTimeTv = (TextView) popView.findViewById(R.id
                            .rank_popupp_update_time);
                    String updateTime = new SimpleDateFormat("yyyy-MM").format(new Date(gameInfo
                            .getUpdateTime()));
                    textSpan = new SpannableString("更新时间\n" + updateTime);
                    textSpan.setSpan(new TextAppearanceSpan(context, R.style.style_14dp_989999),
                            0, 4, Spanned
                                    .SPAN_EXCLUSIVE_EXCLUSIVE);
                    updateTimeTv.setText(textSpan);
                    int dp120 = CommonUtil.dip2px(context, 128);
                    int dp132 = CommonUtil.dip2px(context, 140);
                    popupWindow = ImageUtil.showPopupWindow(context, moreBt, popView, dp132, dp120);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            v.setSelected(false);
                            timer.cancel();
                        }
                    });

                }
            });
            tv_position.setText(position + 1 + "");
            int color123 = R.color.cccccc;
            if (position < 3) {
                if (0 == position) {
                    color123 = R.color.f92b2b;
                } else if (1 == position) {
                    color123 = R.color.fa702a;
                } else if (2 == position) {
                    color123 = R.color.fab52a;
                }
            } else {
                color123 = R.color.cccccc;
            }
            tv_position.setTextColor(ContextCompat.getColor(context, color123));
            downloadNumTv.setText(gameInfo.getDownloadCount() + "次下载");
            String imgUrl = gameInfo.getImgLink();
            if (imgUrl != null && imgUrl.trim().equals("")) {
                imgUrl = null;
            }
            img01234.setImageURI(imgUrl);

            //long gameSize = gameInfo.getGameSize();
            //String gameSizeStr = TextUtil.formatFileSize(gameSize);
            tv_percentage.setText(gameInfo.getScoreLevel() + "分");

            //是否手柄、vr，头控,
            typeNameStr = gameInfo.getCName();
            if (typeNameStr == null) {
                return;
            }
          /*  tv_shoubing.setVisibility(View.GONE);
            tv_vr.setVisibility(View.GONE);
            tv_toukong.setVisibility(View.GONE);
            tv_yun_duan.setVisibility(View.GONE);*/
            //String[] typeNameArray = typeNameStr.split("\\,");

            tank01234LLay.removeAllViews();
         /*   createTagItemView(itemTag);
            createTagItemView(itemTag1);
            createTagItemView(itemTag2);
            createTagItemView(itemTag3);
            createTagItemView(itemTag4);*/
            String[] typeNameArray = typeNameStr.split("\\,");
            for (int i = 0; i < typeNameArray.length; i++) {
                textView = new TextView(context);
                String itemText = typeNameArray[i];
                textView.setText(itemText);
                textView.setSingleLine();
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setPadding(17, 4, 17, 5);
                textView.setTextColor(ContextCompat.getColor(context, R.color.mainColor));
                textView.setTextSize(11);
                textView.setLayoutParams(params);
                textView.setBackgroundResource(R.drawable.shape_14px_b8ccc0);
                tank01234LLay.addView(textView);

            }
        }

        private void createTagItemView(String itemTag) {
            if (typeNameStr.contains(itemTag)) {
                textView = new TextView(context);
                textView.setText(itemTag);
                textView.setPadding(16, 4, 16, 4);
                textView.setTextColor(ContextCompat.getColor(context, R.color.mainColor));
                textView.setTextSize(11);
                textView.setLayoutParams(params);
                textView.setBackgroundResource(R.drawable.shape_14px_b8ccc0);
                tank01234LLay.addView(textView);
            }
        }
    }
}