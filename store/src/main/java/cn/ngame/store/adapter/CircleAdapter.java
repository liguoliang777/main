package cn.ngame.store.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.hub.CircleActivity;
import cn.ngame.store.activity.hub.HubItemActivity;
import cn.ngame.store.bean.CirclePostsInfo;
import cn.ngame.store.core.utils.KeyConstant;

/**
 * Created by liguoliang
 * on 2017/11/29 0029.
 */

public class CircleAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    List<CirclePostsInfo.DataBean> list;
    CircleActivity mContext;
    LinearLayout.LayoutParams params_height_dm196;
    LinearLayout.LayoutParams params_height_dm296;

    public CircleAdapter(CircleActivity context, List<CirclePostsInfo.DataBean> datats) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        list = datats;
        params_height_dm196 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources()
                .getDimensionPixelSize
                        (R.dimen.dm196));
        params_height_dm296 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources()
                .getDimensionPixelSize
                        (R.dimen.dm296));
    }

    public void setData(List<CirclePostsInfo.DataBean> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    @Override
    public Object getItem(int i) {
        if (list == null) {
            return null;
        } else {
            return list.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final CirclePostsInfo.DataBean postInfo = (list == null) ? null : list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new CircleAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.item_hub_circle_item, viewGroup, false);
            holder.fromLogoIv = convertView.findViewById(R.id.img_1);
            holder.hubPicLayout = convertView.findViewById(R.id.hub_pic_ll);
            holder.hubPicLayout.setLayoutParams(params_height_dm296);
            holder.game_logo_Iv_0 = convertView.findViewById(R.id.recommend_game_pic_new_0);
            holder.game_logo_Iv_1 = convertView.findViewById(R.id.recommend_game_pic_new_1);
            holder.game_logo_Iv_2 = convertView.findViewById(R.id.recommend_game_pic_new_2);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_summary = convertView.findViewById(R.id.tv_summary);
            holder.timeTv = convertView.findViewById(R.id.game_hub_time_tv);
            holder.tv_from = convertView.findViewById(R.id.text1);
            holder.lookNub = convertView.findViewById(R.id.game_hub_look_nub_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (postInfo != null) {
            holder.setData(postInfo);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra(KeyConstant.ID, postInfo.getId());
                    intent.setClass(mContext, HubItemActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    public class ViewHolder {
        private SimpleDraweeView fromLogoIv;
        private TextView tv_title, timeTv, lookNub, tv_summary, tv_from;
        private LinearLayout hubPicLayout;
        public SimpleDraweeView game_logo_Iv_0, game_logo_Iv_1, game_logo_Iv_2;
        private List<CirclePostsInfo.DataBean.PostImageListBean> postImageList;

        @SuppressLint("WrongConstant")
        public void setData(CirclePostsInfo.DataBean gameInfo) {
            postImageList = gameInfo.getPostImageList();
            if (null != postImageList) {
                int size = postImageList.size();
                game_logo_Iv_1.setVisibility(View.GONE);
                game_logo_Iv_2.setVisibility(View.GONE);
                if (size >= 1) {
                    CirclePostsInfo.DataBean.PostImageListBean postImageListBean = postImageList.get(0);
                    game_logo_Iv_0.setImageURI(postImageListBean.getPostImageAddress());
                }
                if (size >= 2) {
                    hubPicLayout.setLayoutParams(params_height_dm296);
                    CirclePostsInfo.DataBean.PostImageListBean postImageListBean1 = postImageList.get(1);
                    game_logo_Iv_1.setVisibility(View.VISIBLE);
                    game_logo_Iv_1.setImageURI(postImageListBean1.getPostImageAddress());
                }
                if (size >= 3) {
                    hubPicLayout.setLayoutParams(params_height_dm196);
                    CirclePostsInfo.DataBean.PostImageListBean postImageListBean2 = postImageList.get(2);
                    game_logo_Iv_2.setVisibility(View.VISIBLE);
                    game_logo_Iv_2.setImageURI(postImageListBean2.getPostImageAddress());
                }
            }
            fromLogoIv.setImageURI(gameInfo.getPostRoleHeadPhoto());
            String gameName = gameInfo.getPostTitle();
            if (!"".equals(gameName)) {
                tv_title.setText(gameName);
            }

            String gameDesc = gameInfo.getPostContent();
            if (gameDesc != null && !"".equals(gameDesc)) {
                tv_summary.setText(gameDesc);
            } else {
                tv_summary.setText("");
            }
            long updateTime = gameInfo.getUpdateTime();
            timeTv.setText(String.valueOf(DateUtils.getRelativeTimeSpanString(
                    updateTime)).replace(" ", ""));
            lookNub.setText(String.valueOf(gameInfo.getWatchNum()));
            tv_from.setText(gameInfo.getPostRoleName());
        }
    }

}
