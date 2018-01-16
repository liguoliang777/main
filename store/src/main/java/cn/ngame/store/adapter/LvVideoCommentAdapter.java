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

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.Comment;

/**
 * 视频评论的Listview控件适配器
 * @author  zeng
 * @since   2016-05-30
 */
public class LvVideoCommentAdapter extends BaseAdapter {

	private static final String TAG = LvVideoCommentAdapter.class.getSimpleName();

	private List<Comment> comments;
	private Context context;

	public LvVideoCommentAdapter(Context context, List<Comment> comments) {
		super();
		this.context = context;
		this.comments = comments;
	}
	
	/**
	 * 设置ListView中的数据
	 * @param comments
	 */
	public void setDate(List<Comment> comments){
		this.comments = comments;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		
		if(comments != null){
			return comments.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(comments != null){
			return comments.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void clean(){
		comments.clear();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if(convertView == null){
			
			holder = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_comment, parent, false);
			/*holder.img = (ImageView) convertView.findViewById(R.id.img_1);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);*/
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		Comment gameInfo = comments.get(position);

		//holder.tv_title.setText(gameInfo.name);
		//holder.tv_summary.setText(gameInfo.summary);
		
		
		//asyncImageLoader.DisplayImage(GameInfo.getImgUrl(), holder.img);
		
		return convertView;
	}
	
	/**
	 * 用于保存ListView中重用的item视图的引用
	 * @author flan
	 * @date   2015年10月28日
	 */
	class ViewHolder {
		public ImageView img;
		public TextView tv_title,tv_summary;
	}

}














