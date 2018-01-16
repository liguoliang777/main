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
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.search.SearchBean;

import java.util.List;

import cn.ngame.store.R;

/**
 * @author gp
 * @since 2017-02-22
 */
public class SearchAdapter extends BaseAdapter {

    private List<SearchBean.DataBean> searchResultList;
    private Context context;

    public SearchAdapter(Context context, List<SearchBean.DataBean> searchResultList) {
        super();
        this.context = context;
        this.searchResultList = searchResultList;
    }

    public void setSearchResultList(List<SearchBean.DataBean> searchResultList) {
        this.searchResultList = searchResultList;
        notifyDataSetChanged();
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(searchResultList.get(position).getKeywords());

        return convertView;
    }

    static class ViewHolder {
        private TextView tv_title;
    }

}














