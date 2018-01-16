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

package cn.ngame.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.SearchHistoryBean;
import cn.ngame.store.search.view.SearchActivity;

/**
 * 搜索结果的ListView控件适配器
 *
 * @author zeng
 * @since 2016-05-16
 */
public class LvSearchAdapter extends BaseAdapter {

    private List<SearchHistoryBean> searchResultList;
    private Context context;

    public LvSearchAdapter(Context context, List<SearchHistoryBean> searchResultList) {
        super();
        this.context = context;
        this.searchResultList = searchResultList;
    }

    /**
     * 设置ListView中的数据
     *
     * @param searchResultList 游戏数据
     */
    public void setDate(List<SearchHistoryBean> searchResultList) {
        this.searchResultList = searchResultList;
    }

    @Override
    public int getCount() {
        if (searchResultList != null) {
            return searchResultList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (searchResultList != null) {
            return searchResultList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clean() {
        if (searchResultList != null)
            searchResultList.clear();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final SearchHistoryBean result = searchResultList == null ? null : searchResultList.get(position);
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder(context);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_search, parent, false);
            holder.rl_delete = (ImageView) convertView.findViewById(R.id.rl_delete);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
//			holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
//			holder.tv_size = (TextView) convertView.findViewById(R.id.text1);
//			holder.tv_count = (TextView) convertView.findViewById(R.id.text2);
//			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(searchResultList.get(position).getTitle());
        holder.rl_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchActivity) context).deleteItemhistory(searchResultList.get(position).title);
            }
        });
//		holder.update(result);

        return convertView;
    }

    /**
     * 用于保存ListView中重用的item视图的引用
     *
     * @author flan
     * @since 2015年10月28日
     */
    static class ViewHolder {

        private Context context;
        private ImageView rl_delete;
        private TextView tv_title;
        private RatingBar ratingBar;

        public ViewHolder(Context context) {
            this.context = context;
        }

        public void update(final SearchHistoryBean searchResult) {

//			String gameName = searchResult.name;
//			if (!"".equals(gameName)) {
//				gameName = gameName.length() > 8 ? gameName.substring(0, 8) : gameName;
//				tv_title.setText(gameName);
//			}
//			String gameDesc = searchResult.summary;
//			if (!"".equals(gameDesc)) {
//				gameDesc = gameDesc.length() > 30 ? gameDesc.substring(0, 30) : gameDesc;
//				tv_summary.setText(gameDesc);
//			}else {
//				tv_summary.setText("");
//			}
//
//			if(searchResult.type == 1){
//
//				ratingBar.setRating(searchResult.percentage);
//
//				long gameSize = searchResult.fileSize;
//				String gameSizeStr = TextUtil.formatFileSize(gameSize);
//				tv_size.setText(gameSizeStr);
//
//				long gameCount = searchResult.downloadCount;
//				tv_count.setText("/"+gameCount+"次下载");
//
//			}else {
//				tv_size.setText("");
//				tv_count.setText("");
//			}
//
//			//加载图片
//			String imgUrl = searchResult.imgUrl;
//			if(imgUrl != null && imgUrl.trim().equals("")){
//				imgUrl = null;
//			}
//			Picasso.with(context)
//					.load(imgUrl)
//					.placeholder(R.drawable.ic_def_logo_720_288)
//					.error(R.drawable.ic_def_logo_720_288)
//					.resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
//					.centerInside()
//					.tag(context)
//					.into(img);
        }

    }

}














