package cn.ngame.store.game.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.hub.HubItemActivity;
import cn.ngame.store.adapter.CircleAdapter;
import cn.ngame.store.bean.CirclePostsInfo;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.AutoHeightViewPager;
import cn.ngame.store.view.StickyScrollView;

/**
 * 显示游戏键位的Fragment
 *
 * @author zeng
 * @since 2016/5/17
 */
@SuppressLint("ValidFragment")
public class GameDetailHubFragment extends Fragment {
    private StickyScrollView scrollView;
    public String TAG = GameDetailHubFragment.class.getSimpleName();
    private int SCREEN_HEIGHT = 2200;
    private AutoHeightViewPager vp;
    private List<CirclePostsInfo.DataBean> mDatas;
    private List<CirclePostsInfo.DataBean> mDataList = new ArrayList<>();
    private ListView mListView;
    private CircleAdapter mAdapter;
    private LinearLayout mTopLayout;
    private View mTopItemBt;
    private TextView mTopTv;
    private CirclePostsInfo.DataBean.ShowPostCategoryBean showPostCategoryBean;
    private String postCategoryName = "";
    private TextView mEmptyTv;
    private FragmentActivity context;

    public GameDetailHubFragment(AutoHeightViewPager vp, List<CirclePostsInfo.DataBean> gameInfo, StickyScrollView
            scrollView) {

        this.mDatas = gameInfo;
        this.scrollView = scrollView;
        this.vp = vp;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.activity_game_detail_hub, container, false);

        mListView = (ListView) view.findViewById(R.id.hub_circle_lv);
        mEmptyTv = view.findViewById(R.id.game_detail_empty_tv);
        //头部
        View headerView = inflater.inflate(R.layout.item_game_detail_hub_circle_header, null);
        mTopLayout = headerView.findViewById(R.id.circle_post_top_layout);

        mListView.addHeaderView(headerView);
        //设置布局管理器
        mAdapter = new CircleAdapter(context, mDataList);
        mListView.setAdapter(mAdapter);

        vp.setObjectForPosition(view, 1);
        if (mDatas != null) {
            setView(mDatas);
        }
        return view;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        this.vp.resetHeight(params.height);
        listView.setLayoutParams(params);
    }

    private void setView(List<CirclePostsInfo.DataBean> mDatas) {
        int size = mDatas.size();
        if (size > 0) {
            mEmptyTv.setVisibility(View.GONE);
            mTopLayout.setPadding(0, context.getResources().getDimensionPixelSize(R
                    .dimen
                    .dm012), 0, context.getResources().getDimensionPixelSize(R.dimen
                    .dm010));
            mTopLayout.removeAllViews();
            mDataList.clear();
            for (final CirclePostsInfo.DataBean mData : mDatas) {
                if (mData != null) {
                    //顶部
                    if (showPostCategoryBean == null) {
                        showPostCategoryBean = mData.getShowPostCategory();
                    }
                    //置顶帖子
                    if (mData.getOrderNO() == 1) {
                        mTopItemBt = LayoutInflater.from(context).inflate(R.layout
                                .layout_circle_top_item, null);
                        mTopTv = mTopItemBt.findViewById(R.id.circle_top_title_tv);
                        mTopTv.setText(mData.getPostTitle());
                        mTopItemBt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.putExtra(KeyConstant.ID, mData.getId());
                                intent.setClass(context, HubItemActivity.class);
                                context.startActivity(intent);
                            }
                        });
                        mTopLayout.addView(mTopItemBt);
                    } else {
                        //不是置顶的帖子
                        mDataList.add(mData);
                    }
                }
            }
        }
        mAdapter.setData(mDataList);
        setListViewHeightBasedOnChildren(mListView);
    }


    public void setViewPager(AutoHeightViewPager viewpager) {
        this.vp = viewpager;
    }

}
