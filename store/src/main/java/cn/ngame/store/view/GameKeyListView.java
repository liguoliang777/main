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

package cn.ngame.store.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 不可滚动的listview，用来解决ListView去ScrollView嵌套时的冲突
 * @author flan
 * @date   2015年10月29日
 */
public class GameKeyListView extends LinearLayout {

	public static final String TAG = GameKeyListView.class.getSimpleName();

	private BaseAdapter adapter;
	private Context context;
	private ListView a;

	private OnClickListener listener;

	private Handler handler = new Handler();

	public GameKeyListView(Context context) {
		super(context,null);
	}

	public GameKeyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setOrientation(VERTICAL);
	}
	
	public BaseAdapter getAdapter() {
		return adapter;
	}
	
	/**
	 * 设置适配器
	 * @param adapter
	 */
	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
		notifyChange();
	}

	public void notifyDataSetChanged(){

		this.removeAllViews();

		notifyChange();
	}

	/**
	 * 将adapter中的View绑定到LinearLayout中，并显示
	 */
	private void notifyChange(){
		
		if(adapter != null){

			int itemCount = adapter.getCount();

			for(int i=0;i<itemCount;i++){
				
				final View view = adapter.getView(i, null, null);
				addView(view,i);

			}
		}
	}
	
	/**
	 * 设置每个Item被点击后的事件
	 */
	public void setOnItemClickListener(OnClickListener listener){
		this.listener = listener;
	}
	
}
