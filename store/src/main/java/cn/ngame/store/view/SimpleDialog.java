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

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.CommonUtil;

/**
 * 自定义统一风格的通用对话框
 * @author flan
 * @since  2016年4月13日
 */
public class SimpleDialog extends Dialog {

	public SimpleDialog(Context context) {
		super(context);
	}

	public SimpleDialog(Context context, int theme) {
		super(context, theme);
	}
	
	public static class Builder{
		
		private Context context;
		private View contentView;
		private String title;
		private int dialogWidth = 0;
		private int checkedItem = 0;
		private boolean isSingleChoice = false;
		private String positiveButtonText;
		private String negativeButtonText;
		private OnClickListener singleChoiceClickListener;
		private OnClickListener positiveButtonClickListener;
		private OnClickListener negativeButtonClickListener;
		
		public Builder(Context context) {
			this.context = context;
		}

		/**
		 * 设置对话框的宽度 单位dp
		 * @param width
         */
		public void setDialogWidth(int width){
			this.dialogWidth = width;
		}

		/**
		 * 设置对话框中间的内容
		 * @param v
         * @return
         */
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}
		
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}
		
		public Builder setPositiveButton(int positiveButtonText,OnClickListener listener){
			this.positiveButtonText = (String) context.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}
		
		public Builder setPositiveButton(String positiveButtonText,OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}
		
		public Builder setNegativeButton(int negativeButtonText,OnClickListener listener) {
			this.negativeButtonText = (String) context.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}
		
		public Builder setSingleChoiceItems(int itemsId, int checkedItem,final OnClickListener listener) {
            //items = context.getResources().getTextArray(itemsId);
            singleChoiceClickListener = listener;
            this.checkedItem = checkedItem;
            this.isSingleChoice = true;
            return this;
        }

		/**
		 * 构建对话框，所有其他属性设置方法必须在此方法前调用
		 * @return
         */
		public SimpleDialog create(){

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.layout_dialog_simple, null);

			final SimpleDialog dialog = new SimpleDialog(context, R.style.DownloadDialog);
			dialog.setContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			//设置标题
			TextView titleTv = (TextView) layout.findViewById(R.id.title);
			if(title != null && !"".equals(title)){
				titleTv.setText(title);
			}else {
				titleTv.setVisibility(View.GONE);
			}

			//将内容布局加入到对话框中
			LinearLayout contentWrapper = (LinearLayout) layout.findViewById(R.id.content_wrapper);
			if(contentView != null){
				contentWrapper.removeAllViews();
				contentWrapper.addView(contentView);
			}

			//设置左侧监听器
			if(positiveButtonText != null){
				TextView ptv = (TextView) layout.findViewById(R.id.left_tv);
				ptv.setText(positiveButtonText);
				if(positiveButtonClickListener != null){
					ptv.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						}
					});
				}
			}else {
				layout.findViewById(R.id.left_tv).setVisibility(View.GONE);
			}

			//设置右侧监听器
			if (negativeButtonText != null) {
				TextView ntv = (TextView) layout.findViewById(R.id.right_tv);
				ntv.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					ntv.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							negativeButtonClickListener.onClick(dialog,DialogInterface.BUTTON_NEGATIVE);
						}
					});
				}
			} else {
				layout.findViewById(R.id.right_tv).setVisibility(View.GONE);
			}
			dialog.setContentView(layout);

			if(dialogWidth > 0){
				//设置对话框窗体位置
				//WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
				//Display display = wm.getDefaultDisplay();
				android.view.WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
				lp.width = CommonUtil.dip2px(context,dialogWidth);
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				dialog.getWindow().setAttributes(lp);
			}

			return dialog;
		}
		
	}
	
}




















