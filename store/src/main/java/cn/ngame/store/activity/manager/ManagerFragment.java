package cn.ngame.store.activity.manager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.main.MainHomeActivity;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.core.utils.Constant;


/**
 * 管理
 * Created by gp on 2017/3/14 0014.
 */
@SuppressLint("WrongConstant")
public class ManagerFragment extends BaseSearchFragment {
    String typeValue = "";
    private static MainHomeActivity context;
    private String pwd;
    private boolean isNeedLoad = true;
    private InstalledFragment installedFragment;
    private LikeFragment necessaryFragment;
    private LikeFragment likeFragment;
    private FragmentManager fragmentManager;
    private RelativeLayout tab0, tab1, tab2;
    private FragmentTransaction transaction;
    private ImageView iv_0, iv_1, iv_2;
    private TextView tv_0, tv_1, tv_2;
    private ArrayList<ImageView> ivList;
    private ArrayList<TextView> tvList;
    protected final static String TAG = ManagerFragment.class.getSimpleName();

    public static ManagerFragment newInstance(MainHomeActivity c) {
        context = c;
        Log.d(TAG, "12345 newInstance: ");
        Bundle args = new Bundle();
        ManagerFragment fragment = new ManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        Log.d(TAG, "getContentViewLayoutID: ");
        return R.layout.fragment_manager;
    }

    @Override
    protected void initViewsAndEvents(View view) {//初始化
        Log.d(TAG, "6666initViewsAndEvents: ");
        fragmentManager = getChildFragmentManager();
        transaction = fragmentManager.beginTransaction();

        if (installedFragment == null) {
            installedFragment = InstalledFragment.newInstance("2", 2, context);
        }
        if (likeFragment == null) {
            likeFragment = LikeFragment.newInstance(typeValue, 1, context);
        }
        if (necessaryFragment == null) {
            necessaryFragment = LikeFragment.newInstance("1", 3, context);
        }

        transaction.add(R.id.viewpager, installedFragment);
        transaction.add(R.id.viewpager, likeFragment);
        transaction.add(R.id.viewpager, necessaryFragment);

        transaction.hide(necessaryFragment).hide(likeFragment).show(installedFragment);
        transaction.commit();

        tab0 = view.findViewById(R.id.manager_bt_0);
        tab1 = view.findViewById(R.id.manager_bt_1);
        tab2 = view.findViewById(R.id.manager_bt_2);

        tab0.setOnClickListener(onTabClickListener);
        tab1.setOnClickListener(onTabClickListener);
        tab2.setOnClickListener(onTabClickListener);

        iv_0 = view.findViewById(R.id.manager_top_iv_0);
        tv_0 = view.findViewById(R.id.manager_top_tv_0);
        iv_1 = view.findViewById(R.id.manager_top_iv_1);
        tv_1 = view.findViewById(R.id.manager_top_tv_1);
        iv_2 = view.findViewById(R.id.manager_top_iv_2);
        tv_2 = view.findViewById(R.id.manager_top_tv_2);

        iv_0.setSelected(true);
        tv_0.setSelected(true);

        ivList = new ArrayList<>();
        ivList.add(0, iv_0);
        ivList.add(1, iv_1);
        ivList.add(2, iv_2);
        tvList = new ArrayList<>();
        tvList.add(0, tv_0);
        tvList.add(1, tv_1);
        tvList.add(2, tv_2);


    }

    int IS_TAB = 0;
    private View.OnClickListener onTabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            transaction = fragmentManager.beginTransaction();
            switch (view.getId()) {
                case R.id.manager_bt_0:
                    IS_TAB = 0;
                    transaction.show(installedFragment).hide(likeFragment).hide(necessaryFragment);
                    break;
                case R.id.manager_bt_1:
                    IS_TAB = 1;
                    likeFragment.onUserVisible();
                    transaction.show(likeFragment).hide(installedFragment).hide(necessaryFragment);
                    break;
                case R.id.manager_bt_2:
                    IS_TAB = 2;
                    transaction.show(necessaryFragment).hide(installedFragment).hide(likeFragment);
                    break;
            }
            setTopColor();
            transaction.commit();
        }
    };

    /**
     * 设置顶部Tab控件,文字的颜色
     */
    private void setTopColor() {
        for (int i = 0; i < ivList.size(); i++) {
            ivList.get(i).setSelected(IS_TAB == i ? true : false);

            TextView tabTv = tvList.get(i);
            TextPaint paint = tabTv.getPaint();
            if (IS_TAB == i) {
                tabTv.setSelected(true);
                paint.setUnderlineText(true);
                paint.setAntiAlias(true);
            } else {
                tabTv.setSelected(false);
                paint.setUnderlineText(false);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: 开始");
        //是否显示了,显示了就去加载
        if (isNeedLoad) {
            setTabViewPagerData();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "改变"+hidden);
        isNeedLoad = !hidden;
        if (isNeedLoad) {
            setTabViewPagerData();
        }
        installedFragment.onHiddenChanged(hidden);
        likeFragment.onHiddenChanged(hidden);
        necessaryFragment.onHiddenChanged(hidden);
    }

    private void setTabViewPagerData() {
        //没有登录
        pwd = StoreApplication.passWord;
        if ((pwd != null && !"".endsWith(pwd)) || !Constant.PHONE.equals(StoreApplication
                .loginType)) {
            //已登录
            tab1.setVisibility(View.VISIBLE);
        } else {
            //未登录
            tab1.setVisibility(View.GONE);
            if (IS_TAB == 1) {
                IS_TAB = 0;
            }
        }
        transaction = fragmentManager.beginTransaction();
        switch (IS_TAB) {
            case 0:
                transaction.show(installedFragment).hide(likeFragment).hide(necessaryFragment);
                break;
            case 1:
                transaction.show(likeFragment).hide(installedFragment).hide(necessaryFragment);
                likeFragment.onUserVisible();
                break;
            case 2:
                transaction.show(necessaryFragment).hide(installedFragment).hide(likeFragment);
                break;
        }
        setTopColor();
        transaction.commit();
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

}
