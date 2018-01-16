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

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.ngame.store.R;

/**
 * 导航的Tab滑动控件
 * @author zeng
 * @since 2016-05-16
 */
public class AutoTabView extends HorizontalScrollView implements View.OnClickListener{
	
	private ScrollView parentScrollView;
	private Context context;
	private TextView tv_news,tv_pankou,tv_lungu,tv_gonggao,tv_summary,tv_caiwu,tv_yanbao;
	private ArrayList<TextView> tvs;
	private int screenWidth;	//屏幕像素宽度
	private int tabNum = 1;		//菜单的个数
	private int currentTab;
	//private int currentTabBackgroundColor;
	private int currentTabColor;

	public AutoTabView(Context context) {
		super(context,null);
	}

	public AutoTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		/*TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.animationTabWidget);
		currentTab = typedArray.getInt(R.styleable.animationTabWidget_current_index, 0);
		tabNum = typedArray.getInt(R.styleable.animationTabWidget_visable_tab_num, 1);
		typedArray.recycle();*/
		
		//currentTabBackgroundColor = context.getResources().getColor(R.color.gray);
		currentTabColor = context.getResources().getColor(R.color.mainColor);

		inflate(context, R.layout.layout_auto_tabview, this);
		
		tvs = new ArrayList<>();
		tv_news = (TextView) this.findViewById(R.id.tv_news);
		tv_pankou = (TextView) this.findViewById(R.id.tv_pankou);
		tv_lungu = (TextView) this.findViewById(R.id.tv_lungu);
		tv_gonggao = (TextView) this.findViewById(R.id.tv_gonggao);
		tv_summary = (TextView) this.findViewById(R.id.tv_summary);
		tv_caiwu = (TextView) this.findViewById(R.id.tv_caiwu);
		tv_yanbao = (TextView) this.findViewById(R.id.tv_yanbao);
		tvs.add(tv_news);
		tvs.add(tv_pankou);
		tvs.add(tv_lungu);
		tvs.add(tv_gonggao);
		tvs.add(tv_summary);
		tvs.add(tv_caiwu);
		tvs.add(tv_yanbao);
		
		setTvWidth(tabNum);
		setCurrentcurrentTab(currentTab);
	}

	/**
	 * 设置组合控件中每个 TextView的宽度
	 * @param num	当前屏幕宽度中显示TextView的个数
	 */
	private void setTvWidth(int num){
		
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;// 获取屏幕分辨率宽度

		num = ((ViewGroup)getChildAt(0)).getChildCount();
		int tvWidth = screenWidth/num;
		for(TextView tv : tvs){
			tv.setWidth(tvWidth);
		}
		
	}
	
	public void setParentScrollView(ScrollView scrollView){
		this.parentScrollView = scrollView;
	}
	
	/**
	 * 为每项菜单注册点击事件
	 * @param listener
	 */
	public void setOnClickListener(OnClickListener listener) {
		for(TextView tv : tvs){
			tv.setOnClickListener(listener);;
		}
	};

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
			case R.id.tv_news:
				currentTab = 0;
				break;
			case R.id.tv_pankou:
				currentTab = 1;
				break;
			case R.id.tv_lungu:
				currentTab = 2;
				break;
			case R.id.tv_gonggao:
				currentTab = 3;
				break;
			case R.id.tv_summary:
				currentTab = 4;
				break;
			case R.id.tv_caiwu:
				currentTab = 5;
				break;
			case R.id.tv_yanbao:
				currentTab = 6;
				break;
		}
		
		if(parentScrollView != null){
			if(parentScrollView.getScrollY() == 0){
				parentScrollView.scrollTo(0, 60);
			}
		}
		
		setCurrentcurrentTab(currentTab);
	}
	
	/**
	 * 当前菜单项不在屏幕中央时，滚动scroolview
	 * @param currentTab
	 */
	private void setCurrentcurrentTab(int currentTab){
		
		/*for(TextView tv : tvs){
			tv.setBackgroundColor(Color.WHITE);
		}*/
		TextView tv = tvs.get(currentTab);
		//tv.setBackgroundColor(currentTabBackgroundColor);
		tv.setTextColor(currentTabColor);

		int tvWidth = tv.getWidth();
		int textViewX = currentTab*tvWidth;
		
		if(textViewX > tvWidth*2){
			this.scrollTo((currentTab-2)*tvWidth, 0);
		}else if (textViewX < tvWidth*2) {
			this.scrollTo(-tvWidth, 0);
		}
	}
	
}














