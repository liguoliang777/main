
package cn.ngame.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.FileUtil;

/**
 *
 * @author gp
 */
public class MsgDetailGridAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    ImageLoader imageLoader = ImageLoader.getInstance();

    public MsgDetailGridAdapter(Context context, List<String> list) {
        super();
        this.context = context;
        this.list = list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.msg_detail_gridview_item, parent, false);
            holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        imageLoader.displayImage(list.get(position), holder.iv_img, FileUtil.getModelOptions(R.drawable.ic_def_logo_720_288, 0));
        return convertView;
    }

    class ViewHolder {
        private ImageView iv_img;
    }
}














