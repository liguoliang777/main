package cn.ngame.store.gamehub.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jzt.hol.android.jkda.sdk.bean.gamehub.AddPointBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.NormalDataBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.VoteListBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.VoteRankBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.gamehub.AddPointClient;
import com.jzt.hol.android.jkda.sdk.services.gamehub.VoteRankListClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.adapter.OrderMagListAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.bean.User;
import cn.ngame.store.core.utils.APIErrorUtils;
import cn.ngame.store.gamehub.presenter.VoteRankListen;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

/**
 * 投票fragment - 排行
 * Created by gp on 2017/3/3 0003.
 */

public class OrderMagFragment extends BaseSearchFragment implements VoteRankListen {

    PullToRefreshListView pullListView;
    private PageAction pageAction;
    public static int PAGE_SIZE = 20;
    int type;
    OrderMagListAdapter adapter;
    List<VoteListBean.DataBean> list = new ArrayList<>();
    List<Integer> agreeList = new ArrayList<>();

    public static OrderMagFragment newInstance() {
        OrderMagFragment fragment = new OrderMagFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.strategy_fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {
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

            }
        });
    }

    private void runService() {
        VoteRankBodyBean bodyBean = new VoteRankBodyBean();
        User user = StoreApplication.user;
        if (user != null) {
            bodyBean.setUserCode(user.userCode);
        } else {
            bodyBean.setDeviceOnlyNum(StoreApplication.deviceId);
        }
        bodyBean.setPageIndex(pageAction.getCurrentPage());
        bodyBean.setPageSize(PAGE_SIZE);
        new VoteRankListClient(getActivity(), bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<VoteListBean>() {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(VoteListBean result) {
                        if (result != null && result.getCode() == 0) {
                            listData(result);
                        } else {
                            ToastUtil.show(getActivity(), result.getMsg());
                        }
                    }
                });
    }

    public void listData(VoteListBean result) {
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
            adapter = new OrderMagListAdapter(getActivity(), list, this);
            pullListView.getRefreshableView().setAdapter(adapter);
        } else {
            adapter.setList(list, null);
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
    public void clickVote(int id, final int position) {
        //帖子id
        AddPointBodyBean bodyBean = new AddPointBodyBean();
        User user = StoreApplication.user;
        if (user != null) {
            bodyBean.setUserCode(user.userCode);
        } else {
            bodyBean.setDeviceOnlyNum(StoreApplication.deviceId);
        }
        bodyBean.setType(3);  //type：1表示帖子点赞，2表示评论点赞，3表示投票
        bodyBean.setPostId(id);  //帖子id
        new AddPointClient(getActivity(), bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<NormalDataBean>() {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(NormalDataBean result) {
                        if (result != null && result.getCode() == 0) {
                            if (adapter != null) {
                                agreeList.add(new Integer(position));
                                adapter.setList(list, agreeList);
                            }
                        } else {
                            ToastUtil.show(getActivity(), result.getMsg());
                        }
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

}
