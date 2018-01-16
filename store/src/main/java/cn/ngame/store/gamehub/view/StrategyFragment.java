package cn.ngame.store.gamehub.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.gamehub.HelpClient;
import com.jzt.hol.android.jkda.sdk.services.gamehub.StrategyClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.adapter.StrategyListAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.utils.APIErrorUtils;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

/**
 * 攻略fragment (懒加载-当滑动到当前fragment时，才去加载。而不是进入到activity时，加载所有fragment)
 * Created by gp on 2017/3/3 0003.
 */

public class StrategyFragment extends BaseSearchFragment {

    PullToRefreshListView pullListView;
    private PageAction pageAction;
    public static int PAGE_SIZE = 10;
    private int typeValue;
    private String type;
    StrategyListAdapter adapter;
    List<GameHubMainBean.DataBean> list = new ArrayList<>();

    public static StrategyFragment newInstance(String type, int arg) {
        StrategyFragment fragment = new StrategyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putInt("typeValue", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.strategy_fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        typeValue = getArguments().getInt("typeValue", 1);
        type = getArguments().getString("type");

        pullListView = (PullToRefreshListView) view.findViewById(R.id.pulllistview);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        initListView();
        runService();
    }

    public void initListView() {
        pullListView.setPullLoadEnabled(true);
        pullListView.setScrollLoadEnabled(false);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullListView.setPullLoadEnabled(true);
                pageAction.setCurrentPage(0);
                runService();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //少于指定条数不加载
                if (pageAction.getTotal() < pageAction.getPageSize()) {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                    return;
                }
                if (pageAction.getCurrentPage() * pageAction.getPageSize() < pageAction.getTotal()) {
                    pageAction.setCurrentPage(pageAction.getCurrentPage() == 0 ? pageAction.getCurrentPage() + 2 : pageAction.getCurrentPage() + 1);
                    runService();
                } else {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                }
            }
        });

        pullListView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent();
                i.setClass(getActivity(), MsgDetailActivity.class);
                i.putExtra("msgId", list.get(position).getId());
                startActivity(i);
            }
        });
    }

    private void runService() {
        GameHubMainBodyBean bodyBean = new GameHubMainBodyBean();
        bodyBean.setPageIndex(pageAction.getCurrentPage());
        bodyBean.setType(typeValue);
        if(type.equals("1")){
            new StrategyClient(getActivity(), bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                    .subscribe(new ObserverWrapper<GameHubMainBean>() {
                        @Override
                        public void onError(Throwable e) {
                            ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                        }

                        @Override
                        public void onNext(GameHubMainBean result) {
                            if (result != null && result.getCode() == 0) {
                                listData(result);
                            } else {
                                ToastUtil.show(getActivity(), result.getMsg());
                            }
                        }
                    });
        } else {
            new HelpClient(getActivity(), bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                    .subscribe(new ObserverWrapper<GameHubMainBean>() {
                        @Override
                        public void onError(Throwable e) {
                            ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                        }

                        @Override
                        public void onNext(GameHubMainBean result) {
                            if (result != null && result.getCode() == 0) {
                                listData(result);
                            } else {
                                ToastUtil.show(getActivity(), result.getMsg());
                            }
                        }
                    });
        }
    }

    public void listData(GameHubMainBean result) {
        if (result.getData() == null) {
            return;
        }
        if (pageAction.getCurrentPage() == 0) {
            this.list.clear(); //清除数据
            if (result.getData() == null || result.getData().size() == 0) {
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                return;
            }
        }
        if (result.getData().size() > 0) {
            pageAction.setTotal(result.getTotals());
            this.list.addAll(result.getData());
        }
        if (adapter == null) {
            adapter = new StrategyListAdapter(getActivity(), list);
            pullListView.getRefreshableView().setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
        //设置下位列表
        if ((list.size() == 0 && pageAction.getTotal() == 0) || list.size() >= pageAction.getTotal()) {
            pullListView.setPullLoadEnabled(false);
        } else {
            pullListView.setPullLoadEnabled(true);
        }
        if (pageAction.getCurrentPage() > 0 && result.getData().size() > 0) { //设置上拉刷新后停留的地方
            int index = pullListView.getRefreshableView().getFirstVisiblePosition();
            View v = pullListView.getRefreshableView().getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - v.getHeight());
            pullListView.getRefreshableView().setSelectionFromTop(index, top);
        }
        pullListView.onPullUpRefreshComplete();
        pullListView.onPullDownRefreshComplete();
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
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
