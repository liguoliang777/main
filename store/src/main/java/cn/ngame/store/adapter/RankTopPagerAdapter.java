package cn.ngame.store.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by gp on 16-9-20.
 */
public class RankTopPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;
    private String[] tabList;


    public RankTopPagerAdapter(FragmentManager fm, List<Fragment> list, String[] tabList) {
        super(fm);
        this.list = list;
        this.tabList = tabList;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabList[position];
    }

    //防止fragment自动销毁
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}