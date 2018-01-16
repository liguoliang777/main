package cn.ngame.store.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.gamehub.VoteListBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.gamehub.presenter.VoteRankListen;

/**
 * 战略adapter
 */
public class OrderMagListAdapter extends BaseAdapter {
    Context context;
    List<VoteListBean.DataBean> list;
    VoteRankListen listen;
    List<Integer> agreeList = new ArrayList<>();
    private int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    public OrderMagListAdapter(Context context, List<VoteListBean.DataBean> list, VoteRankListen listen) {
        this.context = context;
        this.list = list;
        this.listen = listen;
    }

    public void setList(List<VoteListBean.DataBean> list, List<Integer> agreeList) {
        this.list = list;
        this.agreeList = agreeList;
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
            view = View.inflate(viewGroup.getContext(), R.layout.order_msg_item, null);
            holder = new MyHolder();
            holder.tv_order = (TextView) view.findViewById(R.id.tv_order);
            holder.tv_order_num = (TextView) view.findViewById(R.id.tv_order_num);
            holder.tv_click = (TextView) view.findViewById(R.id.tv_click);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);
        } else {
            holder = (MyHolder) view.getTag();
        }
        final VoteListBean.DataBean item = list.get(i);
        holder.tv_order.setText(i + 1 + "");
        holder.tv_order_num.setText(item.getVoteCount() + "");
        holder.tv_name.setText(item.getPostTitle());
        if (item.getIsPoint() == 0) {
            holder.tv_click.setBackgroundResource(R.drawable.shape_app_open_bt_nomal);
            holder.tv_click.setEnabled(true);
        } else {
            holder.tv_click.setBackgroundResource(R.drawable.shape_gray_vote);
            holder.tv_click.setEnabled(false);
        }
        holder.tv_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 防止连续点击
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    listen.clickVote(item.getId(), i);
                }
            }
        });
        //投票页面而来，最后执行
        if (agreeList != null) {
            for (int j = 0; j < agreeList.size(); j++) {
                if (agreeList.get(j) == i) {
                    holder.tv_click.setBackgroundResource(R.drawable.shape_gray_vote);
                    holder.tv_click.setEnabled(false);
                    holder.tv_order_num.setText(item.getVoteCount() + 1 + "");
                }
            }
        }
        return view;
    }

    public class MyHolder {
        TextView tv_order, tv_name, tv_order_num, tv_click;
    }
}
