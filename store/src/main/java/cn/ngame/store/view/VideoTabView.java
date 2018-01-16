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
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.VideoLabel;

/**
 * 自定义VR视频窗口中，带动画效果的viewpager Tab指示器控件
 *
 * @author flan
 * @date 2015年11月6日
 */
public class VideoTabView extends RelativeLayout {

    public static final String TAG = VideoTabView.class.getSimpleName();

    private Context context;
    private ViewPager viewPager;
    private ImageView img_cursor;
    private LinearLayout tag_container;

    private int currentTab = 0;
    private int currentTextColor;
    private int defaultTextColor;
    private int defaultTextSize = 16;

    private int imgWidth;
    private int tagWidth;
    private int img_cursor_margin = 80;

    private List<VideoLabel> labelList;
    private List<TextView> tagList;

    public VideoTabView(Context context) {
        super(context, null);
    }

    public VideoTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        currentTextColor = getResources().getColor(R.color.mainColor);
        defaultTextColor = getResources().getColor(R.color.aaaaaa);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.animationTabWidget);
        currentTab = typedArray.getInt(R.styleable.animationTabWidget_current_index, 0);
        typedArray.recycle();

        inflate(context, R.layout.layout_video_tabview, this);
        img_cursor = (ImageView) this.findViewById(R.id.tag_img_cursor);
        tag_container = (LinearLayout) findViewById(R.id.tag_text_layout);

    }

    public void setVideoLabels(List<VideoLabel> labels) {

        this.labelList = labels;

        if (labels != null && labels.size() > 0) {
            tagList = new ArrayList<>();
            tag_container.removeAllViews();

            for (int i = 0; i < labels.size(); i++) {

                VideoLabel label = labels.get(i);
                TextView tv = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.width = 0;
                params.weight = 1;
                tv.setGravity(Gravity.CENTER);
                tv.setLayoutParams(params);
                tv.setText(label.labelName);
                tv.setTextSize(defaultTextSize);
                tv.setId(i);
                tv.setTextColor(defaultTextColor);
                tv.setTag(label.id);
                tv.setClickable(false);
                tagList.add(tv);
                tag_container.addView(tv);
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentTab = v.getId();
                        if (viewPager != null) {
                            viewPager.setCurrentItem(currentTab);
                        }
                        setCurrentTab(currentTab);
                    }
                });
            }
            initDefaultIndex();
        }
    }

    /**
     * 初始化 默认 指示位置
     */
    private void initDefaultIndex() {

        if (tagList == null || tagList.size() <= 0) {
            return;
        }

        //获取屏幕分辨率 宽度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenW = dm.widthPixels;
        tagWidth = screenW / tagList.size();
        //imgWidth = screenW / tagList.size() - img_cursor_margin;
        //imgWidth = screenW / tagList.size();
        imgWidth = 124;

        LayoutParams para = (LayoutParams) img_cursor.getLayoutParams();
        para.width = imgWidth;
        img_cursor.setLayoutParams(para);

        if (viewPager != null) {
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    currentTab = position;
                    setCurrentTab(currentTab);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        } else {
            setCurrentTab(currentTab);
        }
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
                setCurrentTab(currentTab);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 设置当前指示器 位置
     *
     * @param position
     */
    public void setCurrentTab(int position) {

        for (int i = 0; i < tagList.size(); i++) {
            TextView tv = tagList.get(i);
            if (i == position) {
                tv.setTextSize(defaultTextSize);
                tv.setTextColor(currentTextColor);
            } else {
                tv.setTextSize(defaultTextSize);
                tv.setTextColor(defaultTextColor);
            }
        }

        //Animation animation = new TranslateAnimation(tagWidth*currentTab+img_cursor_margin/2, tagWidth*position+img_cursor_margin/2, 0, 0);
        Animation animation = new TranslateAnimation(tagWidth * currentTab, tagWidth * position, 0, 0);

        animation.setFillAfter(true);//True:图片停在动画结束位置
        animation.setDuration(200);    //设置动画完成时间
        img_cursor.startAnimation(animation);

        currentTab = position;

    }

}





















