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

package cn.ngame.store.push.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.push.model.PushMessage;

/**
 * 手柄游戏的ListView控件适配器
 * @author  zeng
 * @since   2016-05-16
 */
public class PushMsgLvAdapter extends BaseAdapter {

	private static final String TAG = PushMsgLvAdapter.class.getSimpleName();

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private List<PushMessage> msgList;

	private Context context;
	private FragmentManager fm;

	PushMsgLvAdapter(Context context, FragmentManager fm) {
		super();
		this.fm = fm;
		this.context = context;
	}

	/**
	 * 设置ListView中的数据
	 * @param msgs 游戏数据
	 */
	public void setDate(List<PushMessage> msgs) {

		/*if(msgList == null || msgList.size() <= 0){
			msgList = msgs;
		}else {
			msgList.addAll(msgs);
		}*/
		msgList = msgs;

	}

	@Override
	public int getCount() {

		if (msgList != null) {
			return msgList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {

		if (msgList != null) {
			return msgList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void clean(){
		if (msgList != null)
			msgList.clear();
	}


	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {

		final PushMessage msg = (msgList == null) ? null : msgList.get(position);

		ViewHolder holder;
		if(convertView == null){

			convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_push_msg, parent, false);

			holder = new ViewHolder();
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.img = (ImageView) convertView.findViewById(R.id.img_1);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(msg != null){

			if(msg.getIsRead() == 0){
				holder.img.setBackgroundResource(R.drawable.msg_dot_red);
			}else {
				holder.img.setBackgroundResource(R.drawable.msg_dot_gray);
			}

			String title = msg.getTitle();
			if (!"".equals(title)) {
				title = title.length() > 20 ? title.substring(0,20) : title;
				holder.tv_title.setText(title);
			}

			String desc = msg.getDescription();
			if (!TextUtil.isEmpty(desc)) {
				desc = desc.replaceAll("\\r\\n|\\r|\\n|\\n\\r","");
				if(msg.getType() == PushMessage.MSG_TYPE_TZ){
					desc = desc.length() > 20 ? desc.substring(0,20)+"..." : desc;
				}else {
					desc = desc.length() > 120 ? desc.substring(0,120)+"..." : desc;
				}
				holder.tv_summary.setText(desc);

			}else {
				holder.tv_summary.setText("");
			}

			long time = msg.getReceiveDate();
			String gameSizeStr = df.format(new Date(time));
			holder.tv_time.setText(gameSizeStr);
		}

		return convertView;
	}

	/**
	 * 用于保存ListView中重用的item视图的引用
	 * @author flan
	 * @since 2015年10月28日
	 */
	public static class ViewHolder {

		private TextView tv_title, tv_summary,tv_time;
		private ImageView img;

	}

}














