
package cn.ngame.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.admin.SystemMsgBean;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.util.DateUtil;


/**
 * @author gp
 */
public class SystemInfoAdapter extends BaseAdapter {

    private Context context;
    private List<SystemMsgBean.DataBean> list;
    private List<String> readList;
    private List<String> selectIds;
    int type;

    public SystemInfoAdapter(Context context, List<SystemMsgBean.DataBean> list, List<String> readList, int type) {
        super();
        this.context = context;
        this.list = list;
        this.readList = readList;
        this.type = type;
        if (selectIds == null) {
            selectIds = new ArrayList<>();
        }
    }

    public void setList(List<SystemMsgBean.DataBean> list, List<String> readList, int type) {
        this.list = list;
        this.readList = readList;
        this.type = type;
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

    public List<String> getSelectIds() {
        return selectIds;
    }

    public void setSelectIds(List<String> selectIds) {
        this.selectIds = selectIds;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.system_info_item, parent, false);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SystemMsgBean.DataBean item = list.get(position);
        holder.tv_content.setText(item.getTitle());
        holder.tv_time.setText(DateUtil.getStrTime_ymd2(list.get(position).getCreateTime()));
        if (type == 1) {
            holder.iv_select.setVisibility(View.VISIBLE);
        } else {
            holder.iv_select.setVisibility(View.GONE);
        }
        if (readList.contains(item.getId() + "")) {
            holder.tv_content.setTextColor(context.getResources().getColor(R.color.color_333333));
        } else {
            holder.tv_content.setTextColor(context.getResources().getColor(R.color.color999999));
        }
        if (selectIds != null && selectIds.contains(position + "")) {
            holder.iv_select.setBackgroundResource(R.drawable.info_select);
        } else {
            holder.iv_select.setBackgroundResource(R.drawable.info_unselect);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_content, tv_time;
        private ImageView iv_select;
    }
}














