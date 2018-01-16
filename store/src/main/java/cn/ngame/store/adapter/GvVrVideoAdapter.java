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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;

import cn.ngame.store.R;
import cn.ngame.store.bean.VideoInfo;

/**
 * VR视频 GridView控件适配器
 *
 * @author zeng
 * @since 2016-06-07
 */
public class GvVrVideoAdapter extends BaseAdapter {

    private static final String TAG = GvVrVideoAdapter.class.getSimpleName();

    private List<VideoInfo> videoInfoList;
    private Context context;
    private static Handler uiHandler = new Handler();

    //int width;

    public GvVrVideoAdapter(Context context) {
        super();
        this.context = context;

        //WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //width = wm.getDefaultDisplay().getWidth();
        //int height = wm.getDefaultDisplay().getHeight();
    }

    public void setData(List<VideoInfo> videoInfos) {

        if(videoInfoList == null || videoInfoList.size() <= 0){
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
        if (videoInfoList != null) {
            videoInfoList.clear();
            videoInfoList = null;
        }
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {

            holder = new ViewHolder(context);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_video, parent, false);

            holder.img = (ImageView) convertView.findViewById(R.id.img_1);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VideoInfo videoInfo = videoInfoList.get(position);
        holder.update(videoInfo);

        return convertView;
    }

    /**
     * 用于保存GridView中重用的item视图的引用
     *
     * @author flan
     * @date 2015年10月28日
     */
    public static class ViewHolder {

        private Context context;
        private ImageView img;
        private TextView tv_title, tv_summary;

        private VideoInfo videoInfo;
        private Timer timer = new Timer();

        public ViewHolder(Context context) {
            this.context = context;
            //init();
        }

        public void update(final VideoInfo videoInfo) {

            this.videoInfo = videoInfo;

            String title = videoInfo.videoName == null ? "" : videoInfo.videoName;
            title = title.length() > 5 ? title.substring(0, 5) : title;
            tv_title.setText(title);

            //tv_summary.setVisibility(View.GONE);

            //更新图片
            /*Bitmap bitmap = memoryCache.getBitmap(videoInfo.videoImageLink);
            if (bitmap != null) {
                Drawable drawable = new BitmapDrawable(bitmap);
                img.setBackground(drawable);
            }else{
                img.setBackground(context.getResources().getDrawable(R.drawable.default_video));

                //加载图片
                String imgUrl = videoInfo.videoImageLink;
                if(imgUrl != null && imgUrl.length() > 0){
                    StoreService.loadImage(videoInfo.videoImageLink,480,800);
                }
            }*/

            Picasso.with(context)
                    .load(videoInfo.videoImageLink)
                    .placeholder(R.drawable.ic_def_logo_412_200)
                    .error(R.drawable.ic_def_logo_412_200)
                    //.resizeDimen(R.dimen.list_detail_image_size, R.dimen.recycle_detail_image_size)
                    .resize(700,950)
                    .centerCrop()
                    .tag(context)
                    .into(img);
        }

    }

}














