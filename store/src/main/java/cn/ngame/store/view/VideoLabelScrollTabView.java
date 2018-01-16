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
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.VideoLabel;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Log;

/**
 * 导航的Tab滑动控件
 *
 * @author zeng
 * @since 2016-05-16
 */
public class VideoLabelScrollTabView extends HorizontalScrollView {

    private static final String TAG = VideoLabelScrollTabView.class.getSimpleName();
    private HorizontalScrollView parentScrollView;
    private Context context;
    private LinearLayout container;
    private List<VideoLabel> textList;
    private ArrayList<TextView> tvList;
    private int textviewWidth = 0;
    private int screenWidth;            //屏幕像素宽度
    private int currentTab = 0;            //当前选择的tab
    private int currentTabBackgroundColor = -1;    //当前选择的tab的背景色
    private int currentTabTextColor = -1;        //当前选择的tab字体的颜色
    private int currentTabTextSize = -1;        //当前选择的tab字体的大小

    private int defaultTextColor;
    private float defaultTextSize;

    private OnTabViewClickListener listener;

    private ViewPager viewPager;

    public VideoLabelScrollTabView(Context context) {
        super(context, null);
    }

    public VideoLabelScrollTabView(Context context, AttributeSet attrs) {
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
        currentTabTextSize = 20;

        defaultTextColor = context.getResources().getColor(R.color.font_default);
        defaultTextSize = 18;

    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentTab = position;
                //setCurrentTab(currentTabText);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置tag数据
     *
     * @param textList
     */
    public void setTextList(List<VideoLabel> textList) {
        this.textList = textList;
        init();
    }

    private void init() {

        if (textList != null && textList.size() > 0) {
            tvList = new ArrayList<>();

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

                VideoLabel videoLabel = textList.get(i);
                tv.setText(videoLabel.labelName);
                tv.setTag(videoLabel.id);

                tv.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        listener.onClick((Long) tv.getTag(), tv.getText().toString()); //处理TAG被点击
                        setCurrentTab(tv);

                        Log.d(TAG, "当前类别ID：" + tv.getTag().toString() + " 当前类别名称：" + tv.getText().toString());
                        return false;
                    }
                });

                tvList.add(tv);
                container.addView(tv);
            }
        }
    }

    /**
     * 设置当前选中样式
     *
     * @param currentTabTv 当前的TextView
     */
    private void setCurrentTab(TextView currentTabTv) {

        for (TextView tv : tvList) {
            tv.setTextSize(defaultTextSize);
            tv.setTextColor(defaultTextColor);
        }

        currentTabTv.setTextColor(currentTabTextColor);
        currentTabTv.setTextSize(currentTabTextSize);
    }


    public void setOnTabViewClickListener(OnTabViewClickListener listener) {
        this.listener = listener;
    }

    /**
     * tab被点击世界监听器
     */
    public interface OnTabViewClickListener {
        void onClick(long typeId, String typeName);
    }
}














