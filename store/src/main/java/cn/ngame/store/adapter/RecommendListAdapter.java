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
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBean;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.ngame.store.R;

public class RecommendListAdapter extends BaseAdapter {

    private LinearLayout.LayoutParams params;
    private Context context;
    private FragmentManager fm;
    private List<RecommendListBean.DataBean> list;
    private int type;

    public RecommendListAdapter(Context context, FragmentManager fm, List<RecommendListBean
            .DataBean> list, int type) {
        super();
        this.context = context;
        this.fm = fm;
        this.list = list;
        this.type = type;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params.setMargins(0, 0, 8, 0);
    }

    public void setList(List<RecommendListBean.DataBean> list) {
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
        final RecommendListBean.DataBean gameInfo = (list == null) ? null : list.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recommend_list_item,
                    parent, false);
            holder = new ViewHolder(context, fm);
            holder.fromIv = convertView.findViewById(R.id.recommend_game_img_1);
            holder.tipLayout = convertView.findViewById(R.id.tip_layout);
            // holder.recommenderSDV = convertView.findViewById(R.id.recommend_from_icon);
            holder.game_logo_Iv = convertView.findViewById(R.id.recommend_game_pic);
            holder.tv_title = convertView.findViewById(R.id.recommend_game_title_tv);
            holder.downLoadTv = convertView.findViewById(R.id.recommend_game_download_num);
            holder.recommenderNameTv = convertView.findViewById(R.id.recommend_from_name_tv);
            holder.contentTv = convertView.findViewById(R.id.tv_summary);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (gameInfo != null) {
            holder.update(gameInfo, type, position);
        }

        return convertView;
    }

    public class ViewHolder {
        private LinearLayout tipLayout;
        private Context context;
        private SimpleDraweeView fromIv, recommenderSDV;
        private TextView downLoadTv, tv_title, contentTv, recommenderNameTv;
        public ImageView game_logo_Iv;
        private Picasso picasso;
        private int[] tipColorArr = new int[]{R.color.tip_0, R.color.tip_1, R.color.tip_2, R
                .color.tip_3, R.color.tip_4};
        private List<RecommendListBean.DataBean.GameTipsList> gameTipsList;

        public ViewHolder(Context context, FragmentManager fm) {
            this.context = context;
            picasso = Picasso.with(context);
        }

        /**
         * 更新普通数据
         *
         * @param gameInfo 游戏信息
         */
        public void update(final RecommendListBean.DataBean gameInfo, int type, int position) {
            Log.d("888", "推荐数据: " + gameInfo.toString());
            String gameBigUrl = gameInfo.getGameRecommendImg();
            String gameLogoUrl = gameInfo.getGameLogo();
            if (gameBigUrl != null && gameBigUrl.trim().equals("")) {
                gameBigUrl = null;
            }
            picasso.load(gameBigUrl).placeholder(R.drawable.ic_def_logo_720_288)
                    .error(R.drawable.ic_def_logo_720_288)
                    .into(game_logo_Iv);
            fromIv.setImageURI(gameLogoUrl);
            //recommenderSDV.setImageURI(gameLogoUrl);

            String gameName = gameInfo.getGameName();
            tv_title.setText(gameName == null ? "" : gameName);
            long downloadCount = gameInfo.getDownloadCount();
            downLoadTv.setText(downloadCount == 0 ? "0" : downloadCount + "次下载");

            String gameDesc = gameInfo.getRecommend();
            String recommenderString = gameInfo.getRecommender();
            recommenderNameTv.setText(recommenderString == null ? "" : recommenderString);
            contentTv.setText(gameDesc == null ? "" : gameDesc);
            gameTipsList = gameInfo.getGameTipsList();
            tipLayout.removeAllViews();
            if (gameTipsList != null) {
                RecommendListBean.DataBean.GameTipsList gameTipsListInfo;
                for (int i = 0; i < gameTipsList.size(); i++) {
                    gameTipsListInfo = gameTipsList.get(i);
                    if (gameTipsListInfo != null && i < 5) {
                        TextView textView = new TextView(context);
                        String itemText = gameTipsListInfo.getGameTipsName();
                        textView.setText(itemText);
                        textView.setSingleLine();
                        textView.setPadding(8, 3, 8, 3);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(10);
                        textView.setLayoutParams(params);
                        textView.setBackgroundResource(tipColorArr[i]);
                        tipLayout.addView(textView);
                    }
                }
            }

        }
    }
}