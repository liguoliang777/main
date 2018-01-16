package cn.ngame.store.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.gamehub.CommentBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.util.DateUtil;
import cn.ngame.store.util.StringUtil;

/**
 * 战略adapter
 */
public class NoticeCommentAdapter extends BaseAdapter {
    Context context;
    List<CommentBean.DataBean> list;
    int type;
    ImageLoader imageLoader = ImageLoader.getInstance();

    public NoticeCommentAdapter(Context context, List<CommentBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<CommentBean.DataBean> list) {
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
            view = View.inflate(viewGroup.getContext(), R.layout.comment_list_item, null);
            holder = new MyHolder();
            holder.tv_type = (TextView) view.findViewById(R.id.tv_type);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            holder.tv_from = (TextView) view.findViewById(R.id.tv_from);
            holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            view.setTag(holder);
        } else {
            holder = (MyHolder) view.getTag();
        }
        CommentBean.DataBean item = list.get(i);
        holder.tv_time.setText(DateUtil.getStrTime_ymd_hm2(item.getCreateTime()));
        holder.tv_from.setText("来自" + item.getNickName());
        holder.tv_content.setText(item.getContent());
        holder.tv_title.setText(item.getPostTitle());
        holder.tv_type.setText(StringUtil.getGameHubMsgType(item.getPostTagId()));
        return view;
    }

    public class MyHolder {
        TextView tv_type, tv_title, tv_content, tv_from, tv_time;
    }
}
