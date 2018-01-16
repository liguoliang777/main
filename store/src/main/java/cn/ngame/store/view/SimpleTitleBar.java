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

public class SimpleTitleBar extends RelativeLayout{
	
	private Button leftButton,rightButton;
	private TextView leftTv,rightTv;

	
	private String leftText,rightText;
	private float leftTextSize,rightTextSize;
	private boolean leftButVisable,rightButVisable,leftTvVisable,rightTvVisable;
	private int leftDrawableId,rightDrawableId;
	
	private Context context;

	public SimpleTitleBar(Context context) {
		super(context,null);
	}

	public SimpleTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
		
		//获取控件中自定义属性值
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleTitleBar);
		
		leftText = typedArray.getString(R.styleable.SimpleTitleBar_left_text);
		leftTextSize = typedArray.getDimension(R.styleable.SimpleTitleBar_left_text_size,12);
		rightText = typedArray.getString(R.styleable.SimpleTitleBar_left_text);
		rightTextSize = typedArray.getDimensionPixelSize(R.styleable.SimpleTitleBar_right_text_size,12);

		leftButVisable = typedArray.getBoolean(R.styleable.SimpleTitleBar_left_but_visable, false);
		rightButVisable = typedArray.getBoolean(R.styleable.SimpleTitleBar_right_but_visable, false);
		leftTvVisable = typedArray.getBoolean(R.styleable.SimpleTitleBar_left_tv_visable, false);
		rightTvVisable = typedArray.getBoolean(R.styleable.SimpleTitleBar_right_tv_visable, false);
		
		leftDrawableId = typedArray.getResourceId(R.styleable.SimpleTitleBar_left_but, -1);
		rightDrawableId = typedArray.getResourceId(R.styleable.SimpleTitleBar_right_but, -1);
		
		typedArray.recycle();
		
		//获取组合控件中的各个子控件
		inflate(context, R.layout.layout_titlebar_simple, this);
		leftButton = (Button) this.findViewById(R.id.left_but);
		rightButton = (Button) this.findViewById(R.id.right_but);
		leftTv = (TextView) this.findViewById(R.id.left_tv);
		rightTv = (TextView) this.findViewById(R.id.right_tv);

		leftTv.setText(leftText);
		leftTv.setTextSize(CommonUtil.px2sp(context,leftTextSize));
		rightTv.setText(rightText);
		rightTv.setTextSize(CommonUtil.px2sp(context,leftTextSize));

		if(leftButVisable){
			leftButton.setVisibility(VISIBLE);
		}
		
		if(rightButVisable){
			rightButton.setVisibility(VISIBLE);
		}

		if(leftTvVisable){
			leftTv.setVisibility(VISIBLE);
		}

		if(rightTvVisable){
			rightTv.setVisibility(VISIBLE);
		}
		
		if(leftDrawableId != -1){
			//leftButton.setBackgroundResource(leftDrawableId);
		}
		
		if(rightDrawableId != -1){
			rightButton.setBackgroundResource(rightDrawableId);
		}
		
	}

	public void setLeftText(String text){
		if(leftTv != null){
			leftTv.setText(text);
		}
	}
	
	/**
	 * 设置左侧按钮的监听器
	 * @param listener
	 */
	public void setOnLeftClickListener(OnClickListener listener){
		leftButton.setOnClickListener(listener);
		leftTv.setOnClickListener(listener);
	}
	
	/**
	 * 设置右侧按钮的监听器
	 * @param listener
	 */
	public void setOnRightClickListener(OnClickListener listener){
		rightButton.setOnClickListener(listener);
	}

}

















