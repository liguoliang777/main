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

package cn.ngame.store.local.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.local.model.WatchRecord;
import cn.ngame.store.view.PicassoImageView;

/**
 * 手柄游戏的ListView控件适配器
 * @author  zeng
 * @since   2016-05-16
 */
public class NoScrollListViewAdapter extends BaseAdapter {

	private static final String TAG = NoScrollListViewAdapter.class.getSimpleName();

	private List<WatchRecord> watchRecordList;
	private WatchHistoryActivity activity;

	private boolean isEdit = false;
	private int checkAllState = 0;


	public NoScrollListViewAdapter(WatchHistoryActivity activity) {
		super();
		this.activity = activity;
	}

	public void setEdit(boolean edit) {
		isEdit = edit;
	}

	public void setCheckAllState(int checkAllState) {
		this.checkAllState = checkAllState;
	}

	/**
	 * 设置ListView中的数据
	 * @param watchRecordList 游戏数据
	 */
	public void setDate(List<WatchRecord> watchRecordList) {

		this.watchRecordList = watchRecordList;

	}

	public List<WatchRecord> getWatchRecordList() {
		return watchRecordList;
	}

	@Override
	public int getCount() {

		if (watchRecordList != null) {
			return watchRecordList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {

		if (watchRecordList != null) {
			return watchRecordList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void clean(){
		if (watchRecordList != null)
			watchRecordList.clear();
	}



	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {

		ViewHolder holder;
		if(convertView == null){

			holder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(R.layout.local_item_watch_record, parent, false);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
			holder.img = (PicassoImageView) convertView.findViewById(R.id.img_1);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final WatchRecord record = watchRecordList.get(position);

		if(isEdit){
			holder.checkBox.setVisibility(View.VISIBLE);
		}else {
			holder.checkBox.setVisibility(View.GONE);
		}
		holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				activity.checkedChanged(isChecked,record);
			}
		});

		if(checkAllState == 1){
			holder.checkBox.setChecked(true);
		}else if(checkAllState == 2){
			holder.checkBox.setChecked(false);
		}

		holder.tv_title.setText(record.getVideoName());
		holder.tv_time.setText("上次观看至 "+secToTime((int)(record.getViewDuration()/1000)));

		String imgUrl = record.getVideoImageLink();
		if(imgUrl != null && !imgUrl.trim().equals("")){
			holder.img.setImageUrl(imgUrl,100f,70f,R.drawable.ic_def_logo_412_200);
		}

		return convertView;
	}

	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "1秒";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + "分" + unitFormat(second)+"秒";
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + "小时" + unitFormat(minute) + "分" + unitFormat(second)+"秒";
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	/**
	 * 用于保存ListView中重用的item视图的引用
	 *
	 * @author flan
	 * @date 2015年10月28日
	 */
	public static class ViewHolder {

		private PicassoImageView img;
		private TextView tv_title, tv_time;
		private CheckBox checkBox;

	}

}














