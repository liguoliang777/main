package cn.ngame.store.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.hub.CircleActivity;
import cn.ngame.store.activity.hub.HubPostsActivity;
import cn.ngame.store.bean.PostsInfo;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.core.utils.KeyConstant;

/**
 * Created by liguoliang
 * on 2017/11/29 0029.
 */

public class HubAdapter extends RecyclerView.Adapter<HubAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<PostsInfo.DataBean> mDatas;
    private TextView tv, tvPostNum;
    private HubPostsActivity mContext;
    private View itemView;
    private LinearLayout.LayoutParams params;
    private List<PostsInfo.DataBean.ShowPostCategoryListBean> showPostCategoryList;

    public HubAdapter(HubPostsActivity context, List<PostsInfo.DataBean> datats) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        params = new LinearLayout.LayoutParams(ImageUtil.getScreenWidth(context) / 2 -
                8, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setData(List<PostsInfo.DataBean> data) {
        this.mDatas = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        GridLayout mLayoutTags;
        TextView mTxt;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public HubAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_hub_rv_item,
                viewGroup, false);
        HubAdapter.ViewHolder viewHolder = new HubAdapter.ViewHolder(view);
        viewHolder.mTxt = view.findViewById(R.id.singer_item_tv);
        viewHolder.mLayoutTags = view.findViewById(R.id.layout_tags2);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(HubAdapter.ViewHolder viewHolder, final int position) {
        PostsInfo.DataBean dataBean = mDatas.get(position);
        if (dataBean == null) {
            return;
        }
        viewHolder.mTxt.setText(dataBean.getPostCategoryName());
        viewHolder.mLayoutTags.removeAllViews();
        showPostCategoryList = dataBean.getShowPostCategoryList();
        if (showPostCategoryList != null) {
            for (final PostsInfo.DataBean.ShowPostCategoryListBean showPostCategoryListBean : showPostCategoryList) {
                if (showPostCategoryListBean != null) {
                    itemView = mInflater.inflate(R.layout.layout_hub_gl_item, null);
                    itemView.setLayoutParams(params);
                    //这是显示数据的控件
                    tv = itemView.findViewById(R.id.hub_gl_item_tv);
                    tvPostNum = itemView.findViewById(R.id.hub_gl_item_posts_num_tv);

                    tv.setText(showPostCategoryListBean.getPostCategoryName());
                    tvPostNum.setText(String.valueOf(showPostCategoryListBean.getPostCategoryCount()));

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.putExtra(KeyConstant.postId, showPostCategoryListBean.getId());
                            intent.setClass(mContext, CircleActivity.class);
                            mContext.startActivity(intent);
                        }
                    });
                    viewHolder.mLayoutTags.addView(itemView);
                }
            }
        }
    }

}
