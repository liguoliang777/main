
package cn.ngame.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;

import java.util.List;

import cn.ngame.store.R;


/**
 *
 * @author gp
 */
public class TopicsListAdapter extends BaseAdapter {

    private Context context;
    private List<YunduanBean.DataBean> list;

    public TopicsListAdapter(Context context, List<YunduanBean.DataBean> list) {
        super();
        this.context = context;
        this.list = list;
    }

    public void setList(List<YunduanBean.DataBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.topics_item, parent, false);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.sdv_img = (SimpleDraweeView) convertView.findViewById(R.id.sdv_img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        YunduanBean.DataBean item = list.get(position);
        holder.sdv_img.setImageURI(item.getLogoUrl());
        holder.tv_title.setText(item.getTypeName());

        return convertView;
    }

    class ViewHolder {
        private SimpleDraweeView sdv_img;
        private TextView tv_title;

    }
}














