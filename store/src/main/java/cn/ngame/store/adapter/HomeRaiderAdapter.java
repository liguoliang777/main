
package cn.ngame.store.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ngame.store.R;

/**
 *  首页下方攻略列表
 * @author gp
 */
public class HomeRaiderAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    String type;
    ImageLoader imageLoader = ImageLoader.getInstance();

    public HomeRaiderAdapter(Context context, List<String> list, String type) {
        super();
        this.context = context;
        this.list = list;
        this.type = type;
    }

    public void setList(List<String> list) {
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.home_raider_item, null);
            holder.ll_show = (LinearLayout) convertView.findViewById(R.id.ll_show);
            holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_content.setText(list.get(position));
//        imageLoader.displayImage(mStickyLV.get(position), holder.iv_img, FileUtil.getModelOptions(R.drawable.ic_def_logo_720_288, 0));
        if(type.equals("1")){
            holder.ll_show.setVisibility(View.VISIBLE);
        } else {
            holder.ll_show.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        private LinearLayout ll_show;
        private ImageView iv_img;
        private TextView tv_type, tv_date, tv_content;
    }
}














