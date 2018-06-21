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
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;
import cn.ngame.store.R;
import cn.ngame.store.activity.main.TopicsDetailActivity;
import cn.ngame.store.bean.GameImage;
import cn.ngame.store.bean.VideoInfo;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.NetUtil;
import cn.ngame.store.view.NgameJZVideoPlayerStandard;

public class Recommend0Adapter extends BaseAdapter {

    private static final String TAG = Recommend0Adapter.class.getSimpleName();

    private List<YunduanBean.DataBean> listOData;
    private Context context;

    public Recommend0Adapter(Context context, List<YunduanBean.DataBean> comments) {
        super();
        this.context = context;
        this.listOData = comments;
    }

    public void setDate(List<YunduanBean.DataBean> comments) {
        this.listOData = comments;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (listOData != null) {
            return listOData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (listOData != null) {
            return listOData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout
                    .item_recommend_list_view0_item, parent, false);
            holder.jzVideoPlayerStandard = convertView.findViewById(R.id.recommend_lv0_vidio);
            holder.jzVideoPlayerStandard.topContainer.setVisibility(View.GONE);

            holder.tv_title = (TextView) convertView.findViewById(R.id.recommend_lv0_title_tv);
            holder.recommend_rl_video_layout = (RelativeLayout) convertView.findViewById(R.id
                    .recommend_rl_video_layout);
            holder.tv_content = (TextView) convertView.findViewById(R.id.recommend_lv0_content_tv);
            holder.game_big_img = (SimpleDraweeView) convertView.findViewById(R.id
                    .recommend_lv0_sdv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final YunduanBean.DataBean info = listOData.get(position);
        final String gameImage = info.getLogoUrl();//获取每一张图片
        final String gameVideoLink = info.getLogoUrl();//获取每一张图片

        if (null == gameImage) {
            holder.recommend_rl_video_layout.setVisibility(View.INVISIBLE);
            holder.game_big_img.setVisibility(View.VISIBLE);
            holder.game_big_img.setImageURI(gameImage);//游戏 -大图
        } else {
            holder.game_big_img.setVisibility(View.INVISIBLE);
            //视频播放
            holder.recommend_rl_video_layout.setVisibility(View.VISIBLE);
            holder.jzVideoPlayerStandard.thumbImageView.setVisibility(View.VISIBLE);
            Picasso.with(context).load(gameImage).into(holder.jzVideoPlayerStandard
                    .thumbImageView);
            holder.jzVideoPlayerStandard.setUp(
                    "http://s1.xflqv.cn/topvideo/%E6%A0%BC%E5%BC%8F%E5%B7%A5%E5%8E%82CF%E6%89%8B%E6%B8%B8%E5%B9%B4%E" +
                            "5%BA%A6%E5%AE%A3%E4%BC%A0%E7%89%87%20-%20%E5%88%80%E9%94%8B%E7%AF%87.mp4", JZVideoPlayerStandard
                    .SCREEN_LAYOUT_NORMAL, "");
            holder.jzVideoPlayerStandard.backButton.setVisibility(View.GONE);
            if (NetUtil.isWifiConnected(context)) {
                holder.jzVideoPlayerStandard.startVideo();
            }

        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, TopicsDetailActivity.class);
                intent.putExtra(KeyConstant
                        .category_Id, info.getId());
                intent.putExtra(KeyConstant.TITLE,
                        info.getTypeName());
                intent.putExtra(KeyConstant.DESC,
                        info.getTypeDesc());
                intent.putExtra(KeyConstant.URL,
                        gameImage);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        public NgameJZVideoPlayerStandard jzVideoPlayerStandard;
        public RelativeLayout recommend_rl_video_layout;
        public TextView tv_title, tv_content;
        public SimpleDraweeView game_big_img;
    }
}














