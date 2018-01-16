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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.GameType;

/**
 * 首页 GridView控件适配器
 * @author zeng
 * @since 2016-06-07
 */
public class GvGameSubjectAdapter extends BaseAdapter {

    private static final String TAG = GvGameSubjectAdapter.class.getSimpleName();

    List<GameType> gameTypeList;
    private Context context;

    public GvGameSubjectAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setData(List<GameType> gameTypes) {
        this.gameTypeList = gameTypes;
    }

    @Override
    public int getCount() {

        if (gameTypeList != null) {
            return gameTypeList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (gameTypeList != null) {
            return gameTypeList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clean() {
        if (gameTypeList != null) {
            gameTypeList.clear();
        }
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final GameType gameType = gameTypeList == null ? null : gameTypeList.get(position);

        ViewHolder holder;
        if(convertView == null){

            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_game_rv, parent, false);
            holder.img = (ImageView) convertView.findViewById(R.id.img_1);
            holder.tv_title = (TextView) convertView.findViewById(R.id.text1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String gameName = gameType.typeName;
        if (!"".equals(gameName)) {
            gameName = gameName.length() > 8 ? gameName.substring(0, 8) : gameName;
            holder.tv_title.setText(gameName);
        }

        String imgUrl = gameType.logoUrl;
        if(imgUrl != null && imgUrl.trim().equals("")){
            imgUrl = null;
        }
        Picasso.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.ic_def_logo_188_188)
                .error(R.drawable.ic_def_logo_188_188)
                .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                .centerInside()
                .tag(context)
                .into(holder.img);

        return convertView;
    }

    /**
     * 用于保存ListView中重用的item视图的引用
     * @author flan
     * @date 2015年10月28日
     */
    public static class ViewHolder {

        public ImageView img;
        public TextView tv_title;

    }

}














