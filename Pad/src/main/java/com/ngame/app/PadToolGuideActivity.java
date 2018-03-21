package com.ngame.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lx.pad.R;
import com.ngame.Utils.KeyMgrUtils;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/2.
 */

public class PadToolGuideActivity extends BaseFragmentActivity {
    ViewPager m_viewPager;
    ThePagerAdapter m_thePagerAdapter;
    private ArrayList<Integer> m_integerAry = new ArrayList();

    public class ThePagerAdapter extends PagerAdapter {

        private ImageView view;

        @Override
        public int getCount() {
            return (m_integerAry == null) ? 0 : m_integerAry.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public ImageView instantiateItem(ViewGroup container, final int position) {
            Log.d("图片内存溢出", "====== " + position);
//            return super.instantiateItem(container, position);
            if (view != null) {
                ((BitmapDrawable) view.getDrawable()).getBitmap().recycle();
            }
            view = new ImageView(PadToolGuideActivity.this);
            //view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
            // .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setImageBitmap(readBitMap(PadToolGuideActivity.this, m_integerAry.get(position)));
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setFocusable(true);
            view.setClickable(true);
            if (view != null) {
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
            return (view == (View) object) ? true : false;
        }

    }

    private Bitmap readBitMap(Context context, int resId) {

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);

        return BitmapFactory.decodeStream(is, null, opt);

    }

    public final ImageView getImgViewByIndex(int nIndex) {
        ImageView pagerItemImgView = (ImageView) getLayoutInflater().inflate(R.layout
                .pager_item_image, null);
        //pagerItemImgView.setImageResource(m_integerAry[nIndex]);
        return pagerItemImgView;
    }

    private void sSetPrefFirstGuideFalse() {
        KeyMgrUtils.sPutPrefBooleanVal(this, "tool_config", "key_first_guide", false);
        finish();
    }

    public void setCurrentPageView(int nIndex) {
        if (nIndex < m_integerAry.size() - 1) {
            m_viewPager.setCurrentItem(nIndex + 1, true);
        } else {
            sSetPrefFirstGuideFalse();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_padtool);
        m_integerAry.add(R.mipmap.bg_guide_1);
        m_integerAry.add(R.mipmap.bg_guide_2);
        m_integerAry.add(R.mipmap.bg_guide_3);
        m_viewPager = findViewById(R.id.vp_guid);
        m_thePagerAdapter = new ThePagerAdapter();
        m_viewPager.setAdapter(m_thePagerAdapter);
        m_viewPager.setOffscreenPageLimit(1);
        m_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean press = true;
            int position = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
                this.position = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.OVER_SCROLL_NEVER) {
                    press = false;
                } else {
                    if (state == ViewPager.OVER_SCROLL_ALWAYS && press) {
                        if (position == m_integerAry.size() - 1) {
                            sSetPrefFirstGuideFalse();
                        }
                        return;
                    }
                    press = true;
                }
            }
        });
    }
}


