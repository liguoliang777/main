
package cn.ngame.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.admin.QuestionListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ngame.store.R;


/**
 *
 * @author gp
 */
public class QuestionListAdapter extends BaseAdapter {

    private Context context;
    private List<QuestionListBean.DataBean> list;
    ImageLoader imageLoader = ImageLoader.getInstance();

    public QuestionListAdapter(Context context, List<QuestionListBean.DataBean> list) {
        super();
        this.context = context;
        this.list = list;
    }

    public void setList(List<QuestionListBean.DataBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.question_list_item, parent, false);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        QuestionListBean.DataBean item = list.get(position);
        holder.tv_content.setText(item.getHelpTitle());

        return convertView;
    }

    class ViewHolder {
        private TextView tv_content;

    }
}














