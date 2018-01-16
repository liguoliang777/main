
package cn.ngame.store.activity.classify;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.classification.AllClassifyBean;

import java.util.List;

import cn.ngame.store.R;

/**
 * @author gp
 */
public class AllClassifyCountyAdapter extends BaseAdapter {

    private Context context;
    private List<AllClassifyBean.DataBean.GameCountyListBean> list;

    public AllClassifyCountyAdapter(Context context, List<AllClassifyBean.DataBean.GameCountyListBean> list) {
        super();
        this.context = context;
        this.list = list;
    }

    public void setList(List<AllClassifyBean.DataBean.GameCountyListBean> list) {
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
            convertView = View.inflate(parent.getContext(), R.layout.classify_gridview_item, null);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position > (list.size() - 1)) {
            holder.tv_content.setText("");
        } else {
            holder.tv_content.setText(list.get(position).getCName());
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_content;
    }
}














