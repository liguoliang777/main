package cn.ngame.store.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.gamehub.VoteListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.util.DateUtil;
import cn.ngame.store.util.StringUtil;

import static cn.ngame.store.R.id.tv_time;

/**
 * 战略adapter
 */
public class NoticeBrowseAdapter extends BaseAdapter {
    Context context;
    List<VoteListBean.DataBean> list;
    int type;
    ImageLoader imageLoader = ImageLoader.getInstance();

    public NoticeBrowseAdapter(Context context, List<VoteListBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<VoteListBean.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final MyHolder holder;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.browse_list_item, null);
            holder = new MyHolder();
            holder.tv_type = (TextView) view.findViewById(R.id.tv_type);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_time = (TextView) view.findViewById(tv_time);
            view.setTag(holder);
        } else {
            holder = (MyHolder) view.getTag();
        }
        VoteListBean.DataBean item = list.get(i);
        holder.tv_type.setText(StringUtil.getGameHubMsgType(item.getPostTagId()));
        holder.tv_title.setText(item.getPostTitle());
        holder.tv_time.setText(DateUtil.getStrTime_ymd_hm2(item.getCreateTime()));
        return view;
    }

    public class MyHolder {
        TextView tv_type, tv_title, tv_time;
    }
}
