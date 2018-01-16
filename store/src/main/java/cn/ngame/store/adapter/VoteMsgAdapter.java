package cn.ngame.store.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.gamehub.VoteListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.FileUtil;
import cn.ngame.store.util.DateUtil;
import cn.ngame.store.util.StringUtil;

/**
 * 战略adapter
 */
public class VoteMsgAdapter extends BaseAdapter {
    Context context;
    List<VoteListBean.DataBean> list;
    int type;
    ImageLoader imageLoader = ImageLoader.getInstance();

    public VoteMsgAdapter(Context context, List<VoteListBean.DataBean> list) {
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
            view = View.inflate(viewGroup.getContext(), R.layout.vote_msg_item, null);
            holder = new MyHolder();
            holder.tv_type = (TextView) view.findViewById(R.id.tv_type);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
            holder.tv_look_num = (TextView) view.findViewById(R.id.tv_look_num);
            holder.tv_comment_num = (TextView) view.findViewById(R.id.tv_comment_num);
            holder.iv_content = (ImageView) view.findViewById(R.id.iv_content);
            holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            view.setTag(holder);
        } else {
            holder = (MyHolder) view.getTag();
        }
        VoteListBean.DataBean item = list.get(i);
        holder.tv_type.setText(StringUtil.getGameHubMsgType(item.getPostTagId()));
        holder.tv_title.setText(item.getPostTitle());
        holder.tv_content.setText(item.getPostContent());
        if (StringUtil.isEmpty(item.getPostImage())) {
            holder.iv_content.setVisibility(View.GONE);
        } else {
            holder.iv_content.setVisibility(View.VISIBLE);
            if (item.getPostImage().contains(",")) {
                imageLoader.displayImage(item.getPostImage().split(",")[0], holder.iv_content, FileUtil.getModelOptions(R.drawable.ic_def_logo_720_288, 0));
            } else {
                imageLoader.displayImage(item.getPostImage(), holder.iv_content, FileUtil.getModelOptions(R.drawable.ic_def_logo_720_288, 0));
            }
        }
        imageLoader.displayImage(item.getHeadPhoto(), holder.iv_icon, FileUtil.getModelOptions(R.drawable.ic_def_logo_720_288, 0));
        holder.tv_name.setText(item.getPostPublisher());
        holder.tv_date.setText(DateUtil.getShowTime(new DateTime(new Timestamp(new Date(item.getCreateTime()).getTime())), DateUtil.getStrTime_ymd(item.getCreateTime())));
        holder.tv_look_num.setText(item.getWatchNum() + "");
        holder.tv_comment_num.setText(item.getVoteCount() + "");
        return view;
    }

    public class MyHolder {
        TextView tv_type, tv_title, tv_content, tv_name, tv_date, tv_look_num, tv_comment_num;
        ImageView iv_content, iv_icon;
    }
}
