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

package cn.ngame.store.activity.classify;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.main.DiscoverListBean;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.game.view.SeeMoreActivity;

public class ClassifyAdapter extends BaseAdapter {

    private final int wrapContent;
    private final Intent labelGameIntent;
    private Context context;
    private FragmentManager fm;
    private List<DiscoverListBean.DataBean.ResultListBean> list;
    private int type;
    private final LayoutInflater inflater;
    private TextView gameNameTv;
    private Intent intent;

    public ClassifyAdapter(Context context, FragmentManager fm, List<DiscoverListBean.DataBean
            .ResultListBean> list, int type) {
        super();
        this.context = context;
        this.fm = fm;
        this.list = list;
        this.type = type;
        wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
        inflater = LayoutInflater.from(context);
        intent = new Intent();
        intent.setClass(context, GameDetailActivity.class);

        labelGameIntent = new Intent();
        labelGameIntent.setClass(context, SeeMoreActivity.class);
    }

    public void setList(List<DiscoverListBean.DataBean.ResultListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
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
        final DiscoverListBean.DataBean.ResultListBean listInfo = (list == null) ? null : list
                .get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.classify_list_item, parent, false);
            holder = new ViewHolder(context, fm);
            holder.titleTv = (TextView) convertView.findViewById(R.id.discover18_tviv_title);
            holder.moreTv = (TextView) convertView.findViewById(R.id.more_action_tv);
            holder.horizontalViewContainer = convertView.findViewById(R.id
                    .horizontalView_container);
            holder.scroll_view = convertView.findViewById(R.id.discover18_scroll_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (listInfo != null) {
            holder.scroll_view.fullScroll(HorizontalScrollView.FOCUS_LEFT);
            final String categoryName = listInfo.getCategoryName();
            holder.titleTv.setText(categoryName);
            holder.update(listInfo, type, position);
            holder.moreTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    labelGameIntent.putExtra(KeyConstant.category_Id, listInfo.getCategoryId() +
                            "");
                    labelGameIntent.putExtra(KeyConstant.TITLE, categoryName);
                    context.startActivity(labelGameIntent);
                }
            });
        }

        return convertView;
    }

    public class ViewHolder {
        private Context context;
        private HorizontalScrollView scroll_view;
        private LinearLayout horizontalViewContainer;
        private SimpleDraweeView gameIV;
        private LinearLayout.LayoutParams hParams;
        private List<DiscoverListBean.DataBean.ResultListBean.ListBean> gameInfoList;
        private DiscoverListBean.DataBean.ResultListBean.ListBean gameInfoBean;
        public TextView titleTv;
        public TextView moreTv;

        public ViewHolder(Context context, FragmentManager fm) {
            this.context = context;
            //fileLoad = FileLoadManager.getInstance(context);
        }

        /**
         * 更新普通数据
         *
         * @param gameInfo 游戏信息
         */
        public void update(final DiscoverListBean.DataBean.ResultListBean gameInfo, int type, int
                position) {
            horizontalViewContainer.removeAllViews();

            Resources resources = context.getResources();
            int pxMarginLeft = resources.getDimensionPixelSize(R.dimen.dm016);
            int px20 = resources.getDimensionPixelSize(R.dimen.dm002);
            gameInfoList = gameInfo.getList();
            for (int i = 0; i < gameInfoList.size(); i++) {
                gameInfoBean = gameInfoList.get(i);
                String gameImage = gameInfoBean.getImgLink();//获取每一张图片
                View view = inflater.inflate(R.layout.item_discover_18, horizontalViewContainer,
                        false);
                gameNameTv = (TextView) view.findViewById(R.id.tviv_item_tv);
                gameIV = (SimpleDraweeView) view.findViewById(R.id.tviv_item_iv);

                gameIV.setImageURI(gameImage);
                gameNameTv.setText(gameInfoBean.getGameName());
                gameIV.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //为  PicassoImageView设置属性
                hParams = new LinearLayout.LayoutParams(
                        wrapContent, wrapContent);
        /*        hParams.width = width240;
                hParams.height = heght114;*/
                //有多个图片的话
                if (0 == i) {
                    hParams.setMargins(pxMarginLeft, 0, px20, 0);
                } else {
                    hParams.setMargins(0, 0, px20, 0);
                }
                view.setLayoutParams(hParams);

                final long id = gameInfoBean.getId();
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent.putExtra(KeyConstant.ID, id);
                        context.startActivity(intent);
                    }
                });
                horizontalViewContainer.addView(view, i);
            }

        }
    }
}