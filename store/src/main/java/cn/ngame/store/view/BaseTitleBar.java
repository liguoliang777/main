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
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.TextUtil;

public class BaseTitleBar extends RelativeLayout{

	private Button leftButton,rightButton;
	private TextView tv_title;

	private String titleText;
	private float titleSize;
	private boolean leftButVisible,rightButVisible;
	private int leftDrawableId,rightDrawableId;

	private Context context;

	public BaseTitleBar(Context context) {
		super(context,null);
	}

	public BaseTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
		
		//获取控件中自定义属性值
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseTitleBar);

		titleText = typedArray.getString(R.styleable.BaseTitleBar_title_text);
		titleSize = typedArray.getDimension(R.styleable.BaseTitleBar_title_size,12);

		leftButVisible = typedArray.getBoolean(R.styleable.BaseTitleBar_left_but_visible, false);
		rightButVisible = typedArray.getBoolean(R.styleable.BaseTitleBar_right_but_visible, false);

		leftDrawableId = typedArray.getResourceId(R.styleable.BaseTitleBar_left_but_img, -1);
		rightDrawableId = typedArray.getResourceId(R.styleable.BaseTitleBar_right_but_img, -1);
		
		typedArray.recycle();
		
		//获取组合控件中的各个子控件
		inflate(context, R.layout.layout_titlebar_base, this);
		leftButton = (Button) this.findViewById(R.id.left_but);
		rightButton = (Button) this.findViewById(R.id.right_but);
		tv_title = (TextView) this.findViewById(R.id.tv_title);

		if(leftButVisible){
			leftButton.setVisibility(VISIBLE);
		}

		if(rightButVisible){
			rightButton.setVisibility(VISIBLE);
		}

		if(leftDrawableId != -1){
			leftButton.setBackgroundResource(leftDrawableId);
		}

		if(rightDrawableId != -1){
			rightButton.setBackgroundResource(rightDrawableId);
		}

		if(!TextUtil.isEmpty(titleText)){
			tv_title.setText(titleText);
			tv_title.setTextSize(CommonUtil.px2sp(context,titleSize));
		}

	}

	public void setTitleText(String text){
		if(tv_title != null){
			tv_title.setText(text);
		}
	}
	
	/**
	 * 设置左侧按钮的监听器
	 * @param listener
	 */
	public void setOnLeftClickListener(OnClickListener listener){
		leftButton.setOnClickListener(listener);
	}
	
	/**
	 * 设置右侧按钮的监听器
	 * @param listener
	 */
	public void setOnRightClickListener(OnClickListener listener){
		rightButton.setOnClickListener(listener);
	}

}

















