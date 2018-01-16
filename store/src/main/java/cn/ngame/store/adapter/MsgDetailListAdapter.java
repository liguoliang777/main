
package cn.ngame.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.gamehub.CommentListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.FileUtil;
import cn.ngame.store.gamehub.view.MsgDetailActivity;
import cn.ngame.store.util.DateUtil;

import static cn.ngame.store.R.id.tv_item_agreeNum;

/**
 * 帖子详情 listview
 *
 * @author gp
 */
public class MsgDetailListAdapter extends BaseAdapter {

    private Context context;
    private List<CommentListBean.DataBean> list;
    List<Integer> agreeList = new ArrayList<>();
    ImageLoader imageLoader = ImageLoader.getInstance();

    public MsgDetailListAdapter(Context context, List<CommentListBean.DataBean> list) {
        super();
        this.context = context;
        this.list = list;
    }

    public void setList(List<CommentListBean.DataBean> list, List<Integer> agreeList) {
        this.list = list;
        this.agreeList = agreeList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.msg_detail_list_item, parent, false);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.iv_agree = (ImageView) convertView.findViewById(R.id.iv_agree);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_item_agreeNum = (TextView) convertView.findViewById(tv_item_agreeNum);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.rl_agree = (RelativeLayout) convertView.findViewById(R.id.rl_agree);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CommentListBean.DataBean item = list.get(position);
        imageLoader.displayImage(item.getHeadPhoto(), holder.iv_icon, FileUtil.getRoundOptions(R.drawable.imgbg, 0));
        holder.tv_name.setText(item.getNickName());
        holder.tv_date.setText(DateUtil.getStrTime_ymd_hm(item.getCreateTime()));
        holder.tv_content.setText(item.getContent());
        holder.tv_item_agreeNum.setText(item.getPointCount() + "");
        if (item.getIsPoint() == 0) {
            holder.iv_agree.setBackgroundResource(R.drawable.msg_detail_agree);
            holder.tv_item_agreeNum.setTextColor(context.getResources().getColor(R.color.color999999));
            holder.rl_agree.setEnabled(true);
        } else {
            holder.iv_agree.setBackgroundResource(R.drawable.msg_detail_disagree);
            holder.tv_item_agreeNum.setTextColor(context.getResources().getColor(R.color.mainColor));
            holder.rl_agree.setEnabled(false);
        }
        holder.rl_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MsgDetailActivity) context).clickAgree(2, item.getId(), position);
            }
        });
        //点赞页面而来，最后执行
        if (agreeList != null) {
            for (int j = 0; j < agreeList.size(); j++) {
                if (agreeList.get(j) == position) {
                    holder.rl_agree.setEnabled(false);
                    holder.tv_item_agreeNum.setText(item.getPointCount() + 1 + "");
                    holder.iv_agree.setBackgroundResource(R.drawable.msg_detail_disagree);
                    holder.tv_item_agreeNum.setTextColor(context.getResources().getColor(R.color.mainColor));
                }
            }
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView iv_icon, iv_agree;
        private TextView tv_name, tv_date, tv_item_agreeNum, tv_content;
        private RelativeLayout rl_agree;
    }
}