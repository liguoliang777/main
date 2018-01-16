package cn.ngame.store.activity.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.game.GameListBody;
import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.main.GameSelectClient;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.TopicsDetailAdapter;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;


/**
 * 专题详情
 * Created by gp on 2017/4/13 0013.
 */

public class TopicsDetailActivity extends BaseFgActivity {

    private SimpleDraweeView sdv_img;
    private TextView tv_info;
    private PullToRefreshListView pullListView;
    TopicsDetailAdapter adapter;
    List<GameRankListBean.DataBean> list = new ArrayList<>();
    private String desc, url;
    private PageAction pageAction;
    public static int PAGE_SIZE = 10;
    private TopicsDetailActivity content;
    private Object categoryId;
    private RelativeLayout mTitleRlay;
    private Button leftBt;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.transparent);
        }
        setContentView(R.layout.topics_detail_activity);
        content = TopicsDetailActivity.this;
        title = getIntent().getStringExtra(KeyConstant.TITLE);
        categoryId = getIntent().getExtras().get(KeyConstant.category_Id);
        Log.d(TAG, "categoryId: " + categoryId);
        //获取状态栏高度设置给标题栏==========================================
        mTitleRlay = (RelativeLayout) findViewById(R.id.ll_title);
        mTitleRlay.setBackgroundResource(R.color.transparent);
        int statusBarHeight = ImageUtil.getStatusBarHeight(content);
       /* RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                mTitleRlay.getLayoutParams());
        layoutParams.setMargins(0, statusBarHeight, 0, 0);*/
        // mTitleRlay.setLayoutParams(layoutParams);
        mTitleRlay.setPadding(0, statusBarHeight, 0, 0);
        //======================================================================
        leftBt = (Button) findViewById(R.id.left_bt);
        leftBt.setPadding(48, statusBarHeight, 0, 0);
        leftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.finish();
            }
        });

        init();
    }

    private void init() {
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);

        pullListView = (PullToRefreshListView) findViewById(R.id.pullListView);

        desc = getIntent().getStringExtra("desc");
        url = getIntent().getStringExtra("url");

        pullListView.setPullLoadEnabled(true); //false,不允许上拉加载
        pullListView.setScrollLoadEnabled(true);
        pullListView.setPullRefreshEnabled(true);
        pullListView.setLastUpdatedLabel(DateFormat.getTimeFormat(this).format(new Date()));
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullListView.setPullLoadEnabled(true);
                pullListView.setPullRefreshEnabled(true);
                pageAction.setCurrentPage(0);
                getDataList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //少于指定条数不加载
                if (pageAction.getTotal() < pageAction.getPageSize()) {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                    ToastUtil.show(content, getString(R.string.no_more_data));
                    return;
                }
                if (pageAction.getCurrentPage() * pageAction.getPageSize() < pageAction.getTotal()) {
                    pageAction.setCurrentPage(pageAction.getCurrentPage() == 0 ? pageAction.getCurrentPage() + 2 : pageAction
                            .getCurrentPage() + 1);
                    getDataList();
                } else {
                    ToastUtil.show(content, getString(R.string.no_more_data));
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                }
            }
        });
        //点击事件
        ListView refreshableView = pullListView.getRefreshableView();
        refreshableView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intent i = new Intent();
                    i.setClass(TopicsDetailActivity.this, GameDetailActivity.class);
                    i.putExtra(KeyConstant.ID, list.get(position - 1).getId());
                    startActivity(i);
                }
            }
        });     //状态栏渐变
        setTitleBGColor(refreshableView);
        //添加头布局
        View headView = View.inflate(this, R.layout.topics_detail_header, null);
        initHeadView(headView);
        //头布局放入listView中
        if (refreshableView.getHeaderViewsCount() == 0) {
            refreshableView.addHeaderView(headView);
        }
        getDataList();
    }

    private void setTitleBGColor(final ListView refreshableView) {
        final float total_height = 400f;
        //滑动事件(搜索栏渐变)
        refreshableView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View childAt = refreshableView.getChildAt(0);
                if (firstVisibleItem == 0 && childAt != null && childAt.getTop() !=
                        0) {
                    int viewScrollHeigh = Math.abs(childAt.getTop());
                    if (viewScrollHeigh < total_height) {
                        float alpha = (total_height - viewScrollHeigh) / total_height;
                        mTitleRlay.setAlpha(1 - alpha);
                        int color = 1 - alpha > 0 ? R.color.mainColor : R.color.transparent;
                        mTitleRlay.setBackgroundResource(color);
                    } else {
                        mTitleRlay.setAlpha(1f);
                        leftBt.setText(title);
                        mTitleRlay.setBackgroundResource(R.color.mainColor);
                    }
                } else {
                    if (firstVisibleItem != 0) {
                        leftBt.setText(title);
                        mTitleRlay.setBackgroundResource(R.color.mainColor);
                        mTitleRlay.setAlpha(1f);

                    } else {
                        leftBt.setText("");
                        mTitleRlay.setBackgroundResource(R.color.transparent);
                        mTitleRlay.setAlpha(1f);
                    }
                }
            }
        });
    }

    private void initHeadView(View headView) {
        sdv_img = (SimpleDraweeView) headView.findViewById(R.id.sdv_img);
        tv_info = (TextView) headView.findViewById(R.id.tv_info);
        tv_info.setText(desc);
        sdv_img.setImageURI(url);
    }

    public void getDataList() {
        GameListBody bodyBean = new GameListBody();
        int id = ConvUtil.NI(categoryId);
        bodyBean.setCategoryId2(id);
        bodyBean.setPageIndex(pageAction.getCurrentPage());
        bodyBean.setPageSize(PAGE_SIZE);
        new GameSelectClient(this, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<GameRankListBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                        pullListView.getRefreshableView().setAdapter(adapter); //数据位空时，加载头部
                        pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();
                    }

                    @Override
                    public void onNext(GameRankListBean result) {

                        if (result != null && result.getCode() == 0) {
                            setData(result);
                        } else {
                            pullListView.getRefreshableView().setAdapter(adapter); //数据位空时，加载头部
                        }
                        pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();
                        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                    }
                });
    }

    private void setData(GameRankListBean result) {
        if (result.getData() == null) {
            return;
        }
        if (pageAction.getCurrentPage() == 0) {
            this.list.clear(); //清除数据
            if (result.getData() == null || result.getData().size() == 0) {
                pullListView.getRefreshableView().setAdapter(adapter); //数据位空时，加载头部
                return;
            }
        }
        pageAction.setTotal(result.getTotals());
        list.addAll(result.getData());
        if (adapter == null) {
            adapter = new TopicsDetailAdapter(this, getSupportFragmentManager(), list, 1);
            pullListView.getRefreshableView().setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
        //
       /* if ((mStickyLV.size() == 0 && pageAction.getTotal() == 0) || mStickyLV.size() >= pageAction.getTotal()) {
            pullListView.setPullLoadEnabled(true);
        } else {
            pullListView.setPullLoadEnabled(true);
        }*/
        if (pageAction.getCurrentPage() > 0 && result.getData().size() > 0) { //设置上拉刷新后停留的地方
            int index = pullListView.getRefreshableView().getFirstVisiblePosition();
            View v = pullListView.getRefreshableView().getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - v.getHeight());
            pullListView.getRefreshableView().setSelectionFromTop(index, top);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
        list = null;
    }
}
