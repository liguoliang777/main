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

package cn.ngame.store.video.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.view.GameLoadProgressBar2;

/**
 * 手柄游戏的ListView控件适配器
 * @author  zeng
 * @since   2016-05-16
 */
public class CommentNSLVAdapter extends BaseAdapter {

	private static final String TAG = CommentNSLVAdapter.class.getSimpleName();

	private List<GameInfo> gameInfoList;
	private FragmentManager fragmentManager;

	private Context context;
	private static Handler uiHandler = new Handler();

	public CommentNSLVAdapter(Context context, FragmentManager fm) {
		super();
		this.context = context;
		this.fragmentManager = fm;
	}

	/**
	 * 设置ListView中的数据
	 * @param gameInfoList 游戏数据
	 */
	public void setDate(List<GameInfo> gameInfoList) {

		this.gameInfoList = gameInfoList;

	}

	@Override
	public int getCount() {

		if (gameInfoList != null) {
			return gameInfoList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {

		if (gameInfoList != null) {
			return gameInfoList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void clean(){
		if (gameInfoList != null)
			gameInfoList.clear();
	}



	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		
		final GameInfo gameInfo = gameInfoList == null ? null : gameInfoList.get(position);

		ViewHolder holder;
		if(convertView == null){

			holder = new ViewHolder(context,fragmentManager);
			convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_noscroll, parent, false);
			holder.img = (ImageView) convertView.findViewById(R.id.img_1);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
			holder.tv_size = (TextView) convertView.findViewById(R.id.text1);
			holder.tv_count = (TextView) convertView.findViewById(R.id.text2);
			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
			holder.progressBar = (GameLoadProgressBar2) convertView.findViewById(R.id.progress_bar);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.update(gameInfo);

		return convertView;
	}

	/**
	 * 用于保存ListView中重用的item视图的引用
	 *
	 * @author flan
	 * @date 2015年10月28日
	 */
	public static class ViewHolder {

		private Context context;
		private GameInfo gameInfo;

		private ImageView img;
		private TextView tv_title, tv_summary,tv_size,tv_count;
		private RatingBar ratingBar;
		private GameLoadProgressBar2 progressBar;    //下载进度条
		private FragmentManager fragmentManager;

		private Timer timer = new Timer();

		public ViewHolder(Context context,FragmentManager fm) {
			this.context = context;
			this.fragmentManager = fm;
			init();
		}

		private void init() {

			timer.schedule(new TimerTask() {
				@Override
				public void run() {

					uiHandler.post(new Runnable() {
						@Override
						public void run() {
						}
					});
				}
			}, 0, 500);
		}

		public void update(final GameInfo gameInfo) {

			this.gameInfo = gameInfo;

			String gameName = gameInfo.gameName;
			if (!"".equals(gameName)) {
				gameName = gameName.length() > 8 ? gameName.substring(0, 8) : gameName;
				tv_title.setText(gameName);
			}
			String gameDesc = gameInfo.gameDesc;
			if (gameDesc != null && !"".equals(gameDesc)) {
				gameDesc = gameDesc.length() > 30 ? gameDesc.substring(0, 30) : gameDesc;
				tv_summary.setText(gameDesc);
			}else {
				tv_summary.setText("");
			}

			long gameSize = gameInfo.gameSize;
			String gameSizeStr = TextUtil.formatFileSize(gameSize);
			tv_size.setText(gameSizeStr);

			long gameCount = gameInfo.downloadCount;
			tv_count.setText("/"+gameCount+"次下载");

			ratingBar.setRating(gameInfo.percentage);

			progressBar.setState(GameLoadProgressBar2.STATE_UN_INSTALL);

			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_def_logo_720_288);
			img.setImageBitmap(bitmap);

			String imgUrl = gameInfo.gameLogo;
			if(imgUrl != null && imgUrl.trim().equals("")){
				imgUrl = null;
			}
			Picasso.with(context)
					.load(imgUrl)
					.placeholder(R.drawable.ic_def_logo_720_288)
					.error(R.drawable.ic_def_logo_720_288)
					.resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
					.centerInside()
					.tag(context)
					.into(img);
		}

	}

}














