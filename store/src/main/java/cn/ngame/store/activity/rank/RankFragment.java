package cn.ngame.store.activity.rank;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import cn.ngame.store.R;
import cn.ngame.store.adapter.RankTopPagerAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;


/**
 * 排行
 * Created by gp on 2017/3/14 0014.
 */

public class RankFragment extends BaseSearchFragment {
    private ViewPager viewpager;
    private ArrayList<Fragment> fragments;
    private RankTopPagerAdapter adapter;
    private TabLayout tablayout;
    private TextView textView;
    private LinearLayout.LayoutParams layoutParams;
    private Rank012345Fragment fragment012345;

    public static RankFragment newInstance(String arg) {
        RankFragment fragment = new RankFragment();
        Bundle bundle = new Bundle();
        bundle.putString("", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int curTab = 0;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_rank;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        //        typeValue = getArguments().getInt("", 1);
        tablayout = view.findViewById(R.id.rank_tablayout);
        viewpager = view.findViewById(R.id.viewpager);

        viewpager.setOffscreenPageLimit(1);//预加载  多缓存2个页面
        initViewPager();
        initTabs();
    }

    private void initTabs() {
        Resources resources = getResources();
        int px34 = resources.getDimensionPixelOffset(R.dimen.dm034);
        int px26 = resources.getDimensionPixelOffset(R.dimen.dm026);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tablayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tablayout.setupWithViewPager(viewpager);
      /*  ViewGroup viewGroup = (ViewGroup) tablayout.getChildAt(0);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            -===============================================================================
            ViewGroup view = (ViewGroup) viewGroup.getChildAt(i);
            textView = (TextView) view.getChildAt(1);
            textView.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.AT_MOST);//textciew的宽度   AT_MOST
            textView.setTextSize(px24);
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            =========================================================================
            int width = textView.getMeasuredWidth();
            layoutParams.weight = width;
            if (0 == i) {
                layoutParams.setMargins(0, 0, 0, 0);
            } else {
                layoutParams.setMargins(0, 0, 0, 0);
            }
        }*/
        int pxWidth = resources.getDimensionPixelOffset(R.dimen.dm026);
        setIndicatorParams(tablayout, px34, px26, pxWidth);
    }

    /**
     * 设置   字体大小  指示器 长度
     * @param tabs
     * @param marginLeft
     * @param marginRight
     * @param width
     */
    private void setIndicatorParams(TabLayout tabs, int marginLeft, int marginRight, int width) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);

            ViewGroup view = (ViewGroup) child;
            textView = (TextView) view.getChildAt(1);
            textView.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.AT_MOST);//textciew的宽度   AT_MOST
            textView.setTextSize(width);
            textView.setSingleLine();
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


            child.setPadding(0, 0, 0, 0);
            int textCount = tabList[i].length();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((width + 3) * textCount, LinearLayout.LayoutParams
                    .MATCH_PARENT, 1);
            params.leftMargin = marginLeft;
            params.rightMargin = marginRight;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    private String tabList[] = new String[]{"全部", "原生手柄", "云适配", "破解游戏", "汉化游戏", "特色游戏", "模拟器"};

    private void initViewPager() {
        fragments = new ArrayList<>();
        final int length = tabList.length;
        for (int i = 0; i < length; i++) {
            fragment012345 = new Rank012345Fragment(curTab);
            fragment012345.setTabPos(i);
            fragments.add(fragment012345);
        }
        adapter = new RankTopPagerAdapter(getChildFragmentManager(), fragments, tabList);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(curTab);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //滑动监听加载数据，一次只加载一个标签页
                if (position < length) {
                    ((Rank012345Fragment) adapter.getItem(position)).sendMessage();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadView(View view) {
        return null;
    }

    public void setTab(int position) {

    }
}
