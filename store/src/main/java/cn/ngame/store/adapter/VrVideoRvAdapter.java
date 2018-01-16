package cn.ngame.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.video.view.VideoDetailActivity;
import cn.ngame.store.bean.VideoInfo;

/**
 * RecycleView 适配器
 * Created by zeng on 2016/7/18.
 */
public class VrVideoRvAdapter extends RecyclerView.Adapter<VrVideoRvAdapter.MyViewHolder> {

    private List<VideoInfo> videoInfoList;
    private Context context;

    public VrVideoRvAdapter(Context context) {
        this.context = context;
    }

    public void setDate(List<VideoInfo> videoInfos){
        if(videoInfoList != null && videoInfos.size() > 0){
            videoInfoList.addAll(videoInfos);
        }else {
            videoInfoList = videoInfos;
        }
    }

    public void clean(){

        if(videoInfoList != null){
            videoInfoList.clear();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_video,parent,false));

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.update(videoInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        if(videoInfoList != null){
            return videoInfoList.size();
        }
        return 0;
    }


    class MyViewHolder extends ViewHolder{

        private TextView tv_title,tv_summary;
        private ImageView img;
        private VideoInfo videoInfo;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_summary = (TextView) itemView.findViewById(R.id.tv_summary);
            img = (ImageView) itemView.findViewById(R.id.img_1);
        }

        public void update(final VideoInfo info){
            videoInfo = info;

            String title = videoInfo.videoName == null ? "" : videoInfo.videoName;
            //title = title.length() > 10 ? title.substring(0, 10) : title;
            tv_title.setText(title);

            String summary = videoInfo.simpleDesc == null ? "" : videoInfo.simpleDesc;
            //summary = summary.length() > 10 ? summary.substring(0, 10) : summary;
            tv_summary.setText(summary);

            String imgUrl = videoInfo.videoImageLink;
            if(imgUrl != null && imgUrl.trim().equals("")){
                imgUrl = null;
            }
            Picasso.with(context)
                    .load(imgUrl)
                    .placeholder(R.drawable.ic_def_logo_412_200)
                    .error(R.drawable.ic_def_logo_412_200)
                    //.resizeDimen(R.dimen.list_detail_image_size, R.dimen.recycle_detail_image_size)
                    //.resize(640,360)
                    .fit()
                    .centerCrop()
                    .tag(context)
                    .into(img);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VideoDetailActivity.class);
                    intent.putExtra("id",info.id);
                    context.startActivity(intent);
                }
            });
        }
    }
}
