
package cn.ngame.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.FileUtil;


/**
 * game圈
 *
 * @author gp
 */
public class GameHubAdapter extends BaseAdapter {

    private Context context;
    private List<GameHubMainBean.DataBean> list;
    private String[] imgs;
    ImageLoader imageLoader = ImageLoader.getInstance();

    public GameHubAdapter(Context context, List<GameHubMainBean.DataBean> list) {
        super();
        this.context = context;
        this.list = list;
    }

    public void setList(List<GameHubMainBean.DataBean> list) {
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
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.game_hub_list_item, parent, false);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
            holder.iv_photo_2 = (ImageView) convertView.findViewById(R.id.iv_photo_2);
            holder.iv_photo_3 = (ImageView) convertView.findViewById(R.id.iv_photo_3);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);

            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_eye_num = (TextView) convertView.findViewById(R.id.tv_eye_num);
            holder.tv_comment_num = (TextView) convertView.findViewById(R.id.tv_comment_num);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);

            holder.ll_img = (LinearLayout) convertView.findViewById(R.id.ll_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
      /*    GameHubMainBean.DataBean item = list.get(position);
      holder.tv_name.setText(item.getPostPublisher());
//        holder.tv_time.setText(DateUtil.getShowTime(DateTime.parse(DateUtil.getStrTime_ymd_hms(item.getCreateTime()),
"yyyy-MM-dd HH:mm:ss")));
        holder.tv_time.setText(DateUtil.getShowTime(new DateTime(new Timestamp(new Date(item.getCreateTime()).getTime())),
        DateUtil.getStrTime_ymd(item.getCreateTime())));
//            DateUtil.getStrTime_ymd_hms(item.getCreateTime());
        holder.tv_title.setText(item.getPostTitle());
        holder.tv_content.setText(item.getPostContent());
        holder.tv_eye_num.setText(item.getWatchNum() + "");
        holder.tv_comment_num.setText(item.getCommentNum() + "");
        setImgPicasso(item.getHeadPhoto(), holder.iv_icon);
        holder.tv_type.setText(StringUtil.getGameHubMsgType(item.getPostTagId()));

        if (StringUtil.isEmpty(item.getPostImage())) {
            holder.ll_img.setVisibility(View.GONE);
        } else {
            if (StringUtil.isEmpty(item.getPostImage().replace(",", ""))) {
                holder.ll_img.setVisibility(View.GONE);
            } else {
                holder.ll_img.setVisibility(View.VISIBLE);
                holder.iv_photo.setVisibility(View.VISIBLE);
                holder.iv_photo_2.setVisibility(View.GONE);
                holder.iv_photo_3.setVisibility(View.GONE);
                if (item.getPostImage().contains(",")) {
                    imgs = item.getPostImage().split(",");
                    if (imgs.length == 1) {
                        holder.iv_photo.setVisibility(View.VISIBLE);
                        setImgPicasso(imgs[0], holder.iv_photo);
                        holder.iv_photo_2.setVisibility(View.GONE);
                        holder.iv_photo_3.setVisibility(View.GONE);
                    } else if (imgs.length == 2) {
                        holder.iv_photo.setVisibility(View.VISIBLE);
                        holder.iv_photo_2.setVisibility(View.VISIBLE);
                        setImgPicasso(imgs[0], holder.iv_photo);
                        setImgPicasso(imgs[1], holder.iv_photo_2);
                        holder.iv_photo_3.setVisibility(View.GONE);
                    } else if (imgs.length >= 3) {
                        holder.iv_photo.setVisibility(View.VISIBLE);
                        holder.iv_photo_2.setVisibility(View.VISIBLE);
                        holder.iv_photo_3.setVisibility(View.VISIBLE);
                        setImgPicasso(imgs[0], holder.iv_photo);
                        setImgPicasso(imgs[1], holder.iv_photo_2);
                        setImgPicasso(imgs[2], holder.iv_photo_3);
                    }
                } else {
                    setImgPicasso(item.getPostImage(), holder.iv_photo);
                }
            }
        }*/
        return convertView;
    }

    void setImgPicasso(String url, ImageView view) {
//        Picasso.with(context)
//                .load(url)
//                .placeholder(R.drawable.ic_def_logo_720_288)
//                .error(R.drawable.ic_def_logo_720_288)
//                .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
//                .centerInside()
//                .tag(context)
//                .into(view);
        imageLoader.displayImage(url, view, FileUtil.getModelOptions(R.drawable.ic_def_logo_720_288, 0));
    }

    class ViewHolder {
        private ImageView iv_icon, iv_photo, iv_photo_2, iv_photo_3;
        private TextView tv_name, tv_time, tv_eye_num, tv_comment_num, tv_type, tv_title, tv_content;
        private LinearLayout ll_img;
    }
}














