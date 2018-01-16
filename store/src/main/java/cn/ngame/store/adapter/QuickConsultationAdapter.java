package cn.ngame.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.FileUtil;
import cn.ngame.store.gamehub.bean.PictureBean;

/**
 * Created by jztdzsw on 15-8-19.
 */
public class QuickConsultationAdapter extends BaseAdapter {
    Context context;
    List<PictureBean> list;
    LayoutInflater layoutInflater;
    OnGridViewItemClickListener listener;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public QuickConsultationAdapter(Context context, List<PictureBean> list, OnGridViewItemClickListener listener) {
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.inquiry_horizontal_gridview_item, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_horizontal_gridview_item);
            holder.deleteButton = (Button) convertView.findViewById(R.id.btn_horizontal_gridview_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PictureBean pictureBean = list.get(position);
        imageLoader.displayImage("file://" + pictureBean.getLocalURL(), holder.imageView, FileUtil.getModelOptions(R.drawable.imgbg, 0));
        // holder.imageView.setImageBitmap(FileTools.getThumbnailByFileUrl(context.getContentResolver(),pictureBean.getLocalURL()));

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGridViewItemClick(position, false);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGridViewItemClick(position, true);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        Button deleteButton;
    }

    //item点击后时行处理
    public interface OnGridViewItemClickListener {
        public void onGridViewItemClick(int position, Boolean isDelete);
    }
}