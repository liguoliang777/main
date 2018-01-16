/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ngame.store.activity.hub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBean;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.KeyConstant;

public class HubFragmentAdapter extends BaseAdapter {
    private Context context;
    private FragmentManager fm;
    private List<GameHubMainBean.DataBean> list;
    private int type;
    private LinearLayout.LayoutParams params_height_dm196;
    private LinearLayout.LayoutParams params_height_dm296;

    public HubFragmentAdapter(Context context, FragmentManager fm, List<GameHubMainBean.DataBean> list, int type) {
        super();
        this.context = context;
        this.fm = fm;
        this.list = list;
        this.type = type;
        params_height_dm196 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources()
                .getDimensionPixelSize
                        (R.dimen.dm196));
        params_height_dm296 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources()
                .getDimensionPixelSize
                        (R.dimen.dm296));
    }

    public void setList(List<GameHubMainBean.DataBean> list) {
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
        final GameHubMainBean.DataBean gameInfo = (list == null) ? null : list.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.game_hub_new_lv_item, parent, false);
            holder = new ViewHolder(context, fm);
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
            holder.postNameTv = convertView.findViewById(R.id.game_hub_pos_name_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (gameInfo != null) {
            holder.update(gameInfo, type, position);
        }

        return convertView;
    }

    public final String TAG = HubFragment.class.getSimpleName();

    public class ViewHolder {
        private Context context;
        private SimpleDraweeView fromLogoIv;
        private TextView tv_title, timeTv, lookNub, tv_summary, tv_from, postNameTv;
        private LinearLayout hubPicLayout;
        private GameHubMainBean.DataBean.ShowPostCategoryBean showPostCategoryBean;
        public SimpleDraweeView game_logo_Iv_0, game_logo_Iv_1, game_logo_Iv_2;

        public ViewHolder(Context context, FragmentManager fm) {
            this.context = context;
        }

        /**
         * 更新普通数据
         *
         * @param gameInfo 游戏信息
         */
        @SuppressLint("WrongConstant")
        public void update(final GameHubMainBean.DataBean gameInfo, int type, final int position) {
            List<GameHubMainBean.DataBean.PostImageListBean> postImageList = gameInfo.getPostImageList();
            if (null != postImageList) {
                int size = postImageList.size();
                game_logo_Iv_1.setVisibility(View.GONE);
                game_logo_Iv_2.setVisibility(View.GONE);
                if (size >= 1) {
                    hubPicLayout.setLayoutParams(params_height_dm296);
                    GameHubMainBean.DataBean.PostImageListBean postImageListBean = postImageList.get(0);
                    game_logo_Iv_0.setImageURI(postImageListBean.getPostImageAddress());
                }
                if (size >= 2) {
                    hubPicLayout.setLayoutParams(params_height_dm296);
                    GameHubMainBean.DataBean.PostImageListBean postImageListBean1 = postImageList.get(1);
                    game_logo_Iv_1.setVisibility(View.VISIBLE);
                    game_logo_Iv_1.setImageURI(postImageListBean1.getPostImageAddress());
                }
                if (size >= 3) {
                    hubPicLayout.setLayoutParams(params_height_dm196);
                    GameHubMainBean.DataBean.PostImageListBean postImageListBean2 = postImageList.get(2);
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
            //timeTv.setText(DateUtils.getRelativeTimeSpanString(gameInfo.getUpdateTime()));
            lookNub.setText(String.valueOf(gameInfo.getWatchNum()));
            tv_from.setText(gameInfo.getPostRoleName());
            showPostCategoryBean = gameInfo.getShowPostCategory();
            if (showPostCategoryBean != null) {
                postNameTv.setText(showPostCategoryBean.getPostCategoryName());
                postNameTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra(KeyConstant.postId, showPostCategoryBean.getId());
                        intent.setClass(context, CircleActivity.class);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }
}