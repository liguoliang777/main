package com.ngame.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lx.pad.R;
import com.ngame.Utils.KeyMgrUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/12/2.
 */

public class PadToolGuideActivity extends BaseFragmentActivity {
    ViewPager m_viewPager;
    ThePagerAdapter m_thePagerAdapter;
    private Integer[] m_integerAry;

    public class ThePagerAdapter<T> extends PagerAdapter{
        private List<T> m_list;

        public ThePagerAdapter(List<T> list) {
            super();
            m_list = list;
        }

        @Override
        public int getCount() {
            return (m_list == null) ? 0 : m_list.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView((View)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
//            return super.instantiateItem(container, position);
            ImageView view = getImgViewByIndex(position);
            if(view != null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setCurrentPageView(position);
                    }
                });
            }
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == (View)object) ? true : false;
        }
    }

    public PadToolGuideActivity(){
        super();
        m_integerAry = new Integer[]{Integer.valueOf(R.mipmap.bg_guide_1), Integer.valueOf(R.mipmap.bg_guide_2), Integer.valueOf(R.mipmap.bg_guide_3)};
    }

    public final ImageView getImgViewByIndex(int nIndex){
        ImageView pagerItemImgView = (ImageView)getLayoutInflater().inflate(R.layout.pager_item_image, null);
        pagerItemImgView.setImageResource(m_integerAry[nIndex].intValue());
        return pagerItemImgView;
    }

    private void sSetPrefFirstGuideFalse(){
        KeyMgrUtils.sPutPrefBooleanVal(this, "tool_config", "key_first_guide", false);
        finish();
    }

    public void setCurrentPageView(int nIndex){
        if(nIndex < m_integerAry.length - 1){
            m_viewPager.setCurrentItem(nIndex + 1, true);
        }else{
            sSetPrefFirstGuideFalse();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_padtool);
        m_viewPager = findViewById(R.id.vp_guid);
        m_thePagerAdapter = new ThePagerAdapter(Arrays.asList(m_integerAry));
        m_viewPager.setAdapter(m_thePagerAdapter);
        m_viewPager.setOffscreenPageLimit(m_integerAry.length - 1);
        m_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean press = true;
            int position = 0;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                this.position = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.OVER_SCROLL_NEVER){
                    press = false;
                }else{
                    if(state == ViewPager.OVER_SCROLL_ALWAYS && press){
                        if(position == m_integerAry.length - 1){
                            sSetPrefFirstGuideFalse();
                        }
                        return ;
                    }
                    press = true;
                }
            }
        });
    }
}


