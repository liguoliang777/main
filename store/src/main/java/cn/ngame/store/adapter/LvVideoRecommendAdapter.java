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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.VideoInfo;
import cn.ngame.store.core.utils.TextUtil;

/**
 * 视评论的Listview控件适配器
 *
 * @author zeng
 * @since 2016-05-30
 */
public class LvVideoRecommendAdapter extends BaseAdapter {

    private static final String TAG = LvVideoRecommendAdapter.class.getSimpleName();

    private List<VideoInfo> videoInfoList;

    private Context context;

    public LvVideoRecommendAdapter(Context context) {
        super();
        this.context = context;
    }

    /**
     * 设置ListView中的数据
     */
    public void setData(List<VideoInfo> videoInfos) {

        if(videoInfoList == null){
            videoInfoList = videoInfos;
        }else {
            videoInfoList.addAll(videoInfos);
        }
    }

    @Override
    public int getCount() {

        if (videoInfoList != null) {
            return videoInfoList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (videoInfoList != null) {
            return videoInfoList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clean() {
        if(videoInfoList != null){
            videoInfoList.clear();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder(context);

            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_video_recommend, parent, false);;
            holder.img = (ImageView) convertView.findViewById(R.id.img_1);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_score = (TextView) convertView.findViewById(R.id.tv_score);
            holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VideoInfo VideoInfo = videoInfoList.get(position);
        holder.update(VideoInfo);

        return convertView;
    }

    /**
     * 用于保存ListView中重用的item视图的引用
     * @author flan
     * @since 2015年10月28日
     */
    public static class ViewHolder {

        private Context context;

        public ImageView img;
        public TextView tv_title, tv_summary,tv_score;

        public ViewHolder(Context context) {
            this.context = context;

        }

        public void update(VideoInfo videoInfo) {

            String title = videoInfo.videoName;
            if(!TextUtil.isEmpty(title)){
                title = title.length() > 9 ? title.substring(0,9) : title;
                tv_title.setText(title);
            }

            tv_score.setText(videoInfo.percentage+"");
            tv_summary.setText(videoInfo.videoIntroduce);

            if(!TextUtil.isEmpty(videoInfo.videoImageLink)){
                Picasso.with(context)
                        .load(videoInfo.videoImageLink)
                        .placeholder(R.drawable.ic_def_logo_412_200)
                        .error(R.drawable.ic_def_logo_412_200)
                        .fit()
                        .tag(context)
                        .into(img);
            }
        }
    }

}














