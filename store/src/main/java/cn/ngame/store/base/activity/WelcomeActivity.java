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

package cn.ngame.store.base.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.WelcomeAdapter;

/**
 * 初次安装时的引导界面
 * @author flan
 * @date   2016年5月3日
 */
public class WelcomeActivity extends BaseFgActivity implements OnPageChangeListener{

    private ViewPager viewPager;
    //底部小圆点
    private ImageView[] dots;
    //当前选中页面
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_welcome);
        viewPager = (ViewPager) this.findViewById(R.id.viewpager);

        List<View> viewList = getPagers();

        viewPager.setAdapter(new WelcomeAdapter(viewList));	//设置viewpager适配器
        //viewPager.setOnPageChangeListener(this);		//设置viewpager切换监听器
        viewPager.addOnPageChangeListener(this);

        //初始化底部小圆点
        initDot();
    }

    //初始化每个导航页面的布局
    @SuppressLint("InflateParams")
    private List<View> getPagers(){

        List<View> viewList = new ArrayList<>();
        LayoutInflater inflater = this.getLayoutInflater();

        View pager1 = inflater.inflate(R.layout.item_welcome_a, null);
        View pager2 = inflater.inflate(R.layout.item_welcome_b, null);

        Button button = (Button) pager2.findViewById(R.id.but_comein);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewList.add(pager1);
        viewList.add(pager2);

        return viewList;
    }

    //初始化底部小圆点
    private void initDot(){

        LinearLayout ll = (LinearLayout) this.findViewById(R.id.lay_dot);

        int size = viewPager.getAdapter().getCount();
        dots = new ImageView[size];

        for (int i = 0; i < dots.length; i++) {

            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setTag(i);
            //dots[i].setEnabled(true);
            //给下方小圆点绑定点击事件
            dots[i].setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    int position = (int) v.getTag();	//获得点击的小圆点位置

                    setCurrentDot(position);
                    setCurrentView(position);
                }
            });
        }

        dots[currentIndex].setEnabled(false);	//初始化时默认选中第一个小圆点
    }

    //设置小圆点被选中状态
    private void setCurrentDot(int position){

        if(position < 0 || position >= viewPager.getAdapter().getCount() || position == currentIndex){
            return;
        }

        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = position;
    }

    //点击小圆点后切换到响应的页面
    private void setCurrentView(int position){

        if(position < 0 || position > viewPager.getAdapter().getCount()){
            return;
        }

        viewPager.setCurrentItem(position);
    }



    @Override
    public void onPageScrollStateChanged(int state) {

        //state 有三个值：
        //SCROLL_STATE_IDLE		滚动完全停止，当前页面填满视图且不再有滚动效果
        //SCROLL_STATE_DRAGGING	用户正在拖拽，此时手指与屏幕接触
        //SCROLL_STATE_SETTLING 页面开始完成剩下的自动回位过程，此时手指离开屏幕；
        //如果手指一直滑动到新的页面填满视图后才松开，则不会出现此状态
        //System.out.println("=========>>>>> "+state);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        //position当前页面
        //positionOffset是当前页面滑动比例，如果页面向右翻动，这个值不断变大，最后在趋近1的情况后突变为0。如果页面向左翻动，这个值不断变小，最后变为0。
        //positionOffsetPixels是当前页面滑动像素，变化情况和positionOffset一致。
        //System.out.println("=========>>>>> "+position +" | "+positionOffset+" | "+positionOffsetPixels);
    }

    //当页面被权重
    @Override
    public void onPageSelected(int position) {
        setCurrentDot(position);
    }

}














