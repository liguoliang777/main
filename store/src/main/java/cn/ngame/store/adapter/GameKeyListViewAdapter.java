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

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.GameKey;

/**
 * 游戏键位的ListView控件适配器
 * @author  zeng
 * @since   2016-09-03
 */
public class GameKeyListViewAdapter extends BaseAdapter {

	private static final String TAG = GameKeyListViewAdapter.class.getSimpleName();

	private List<GameKey> gameKeyList;

	private Context context;

	public GameKeyListViewAdapter(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 设置ListView中的数据
	 * @param gameKeyList 游戏数据
	 */
	public void setDate(List<GameKey> gameKeyList) {

		this.gameKeyList = gameKeyList;

	}

	@Override
	public int getCount() {

		if (gameKeyList != null) {
			return gameKeyList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {

		if (gameKeyList != null) {
			return gameKeyList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void clean(){
		if (gameKeyList != null)
			gameKeyList.clear();
	}



	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {

		ViewHolder holder;
		if(convertView == null){

			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_game_key, parent, false);

			holder.tv_key = (TextView) convertView.findViewById(R.id.tv_key);
			holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		GameKey key = gameKeyList.get(position);
		holder.tv_key.setText(key.keyName);
		holder.tv_desc.setText(key.keyDesc);

		return convertView;
	}

	/**
	 * 用于保存ListView中重用的item视图的引用
	 * @author flan
	 * @date 2016年09月03日
	 */
	public static class ViewHolder {
		private TextView tv_key, tv_desc;
	}

}














