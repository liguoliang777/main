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

import com.jzt.hol.android.jkda.sdk.bean.search.SearchGameVideoBean;

import java.util.List;

import cn.ngame.store.R;

/**
 * 搜索结果的ListView控件适配器
 *
 * @author gp
 */
public class SearchOtherAdapter extends BaseAdapter {

    private List<SearchGameVideoBean.DataBean.HotSearchGameListBean> list;
    private Context context;

    public SearchOtherAdapter(Context context, List<SearchGameVideoBean.DataBean.HotSearchGameListBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<SearchGameVideoBean.DataBean.HotSearchGameListBean> list) {
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.search_other_item, parent, false);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.search_position = (TextView) convertView.findViewById(R.id.tv_search_position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(list.get(position).getAdvName());
        holder.search_position.setText(position + 1 + "");
        return convertView;
    }

    static class ViewHolder {
        private TextView tv_title, search_position;
    }
}














