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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.Category;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.util.ConvUtil;

/**
 * 导航的Tab滑动控件
 *
 * @author zeng
 * @since 2016-05-16
 */
public class ScrollTabView extends HorizontalScrollView {

    private static final String TAG = ScrollTabView.class.getSimpleName();
    private Context context;
    private LinearLayout container;
    private List<Category> textList;

    private int textViewWidth = 0;
    private int screenWidth;                    //屏幕像素宽度
    private int currentTab = 0;                    //当前选择的tab
    private int currentTabBackgroundColor = -1;    //当前选择的tab的背景色

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;

    private int currentTabTextColor = -1;        //当前选择的tab字体的颜色
    private int currentTabTextSize = 16;        //当前选择的tab字体的大小

    private int defaultTextColor;
    private float defaultTextSize = 16;

    private OnTabViewClickListener listener;

    public ScrollTabView(Context context) {
        super(context, null);
    }

    public ScrollTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.animationTabWidget);
        currentTab = typedArray.getInt(R.styleable.animationTabWidget_current_index, -1);
        typedArray.recycle();

        container = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        container.setLayoutParams(params);
        container.setOrientation(LinearLayout.HORIZONTAL);
        this.removeAllViews();
        this.addView(container);

        currentTabBackgroundColor = context.getResources().getColor(R.color.f5f5f5);
        currentTabTextColor = context.getResources().getColor(R.color.mainColor);

        defaultTextColor = context.getResources().getColor(R.color.color_666666);

        textViewWidth = CommonUtil.dip2px(context, 65);
    }

    /**
     * 设置tag数据
     */
    public void setTextList(List<Category> textList) {
        this.textList = textList;
        init();
    }

    private void init() {

        if (container != null) {
            container.removeAllViews();
        }

        if (textList != null && textList.size() > 0) {

            int marginLeft = CommonUtil.dip2px(context, 10);

            for (int i = 0; i < textList.size(); i++) {

                final TextView tv = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER_VERTICAL;

                if (i > 0) {
                    if (i == (textList.size() - 1)) {
                        params.setMargins(marginLeft, 0, marginLeft, 0);
                    } else {
                        params.setMargins(marginLeft, 0, 0, 0);
                    }
                    tv.setTextSize(defaultTextSize);
                    tv.setTextColor(defaultTextColor);
                } else {
                    tv.setTextSize(currentTabTextSize);
                    tv.setTextColor(currentTabTextColor);
                }
                tv.setLayoutParams(params);
                tv.setGravity(Gravity.CENTER);

				/*TextPaint tp = tv.getPaint();
                tp.setFakeBoldText(true);*/

                Category category = textList.get(i);
                tv.setText(category.typeName);
                tv.setTag(category.id);

                tv.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        listener.onClick(ConvUtil.NI(tv.getTag()), tv.getText().toString()); //处理TAG被点击
                        setCurrentTab(tv);

                        Log.d(TAG, "当前类别ID：" + tv.getTag().toString() + " 当前类别名称：" + tv.getText().toString());
                        return false;
                    }
                });

                container.addView(tv);
            }
        }

        globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View view = container.getChildAt(0);
                if (view != null)
                    textViewWidth = view.getWidth();
            }
        };
        container.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

    }

    /**
     * 设置当前选中样式
     *
     * @param currentTabTv 当前的TextView
     */
    private void setCurrentTab(TextView currentTabTv) {

        if (container != null)
            container.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);

        for (int i = 0; i < container.getChildCount(); i++) {
            TextView tv = (TextView) container.getChildAt(i);
            tv.setTextSize(defaultTextSize);
            tv.setTextColor(defaultTextColor);

        }

        currentTabTv.setTextColor(currentTabTextColor);
        currentTabTv.setTextSize(currentTabTextSize);
    }

    /**
     * 设置当前选中样式
     *
     * @param position 当前的TextView
     */
    public void setCurrentTab(final int position) {
        if (container != null)
            container.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);

        for (int i = 0; i < container.getChildCount(); i++) {
            TextView tv = (TextView) container.getChildAt(i);

            if (i == position) {
                tv.setTextSize(currentTabTextSize);
                tv.setTextColor(currentTabTextColor);

                //int tvWidth = tv.getWidth();
                final int tvWidth = textViewWidth;
                int textViewX = position * tvWidth;

                if (textViewX > tvWidth * 2) {
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollTo((position - 2) * tvWidth, 0);
                        }
                    });

                } else if (textViewX <= tvWidth * 2) {
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollTo(-tvWidth, 0);
                        }
                    });
                }

            } else {
                tv.setTextSize(defaultTextSize);
                tv.setTextColor(defaultTextColor);
            }

        }
    }


    public void setOnTabViewClickListener(OnTabViewClickListener listener) {
        this.listener = listener;
    }

    /**
     * tab被点击世界监听器
     */
    public interface OnTabViewClickListener {
        void onClick(int typeId, String typeName);
    }
}














