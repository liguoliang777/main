package cn.ngame.store.gamehub.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.CommentBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.VoteListBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.gamehub.BrowseClient;
import com.jzt.hol.android.jkda.sdk.services.gamehub.MsgPostClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.adapter.NoticeBrowseAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.bean.Token;
import cn.ngame.store.bean.User;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.APIErrorUtils;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.user.view.LoginActivity;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.popupwin.DeleteBrowsePop;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

import static cn.ngame.store.StoreApplication.user;

/**
 * 浏览fragment (懒加载-当滑动到当前fragment时，才去加载。而不是进入到activity时，加载所有fragment)
 * Created by gp on 2017/3/3 0003.
 */

public class BrowseFragment extends BaseSearchFragment implements View.OnClickListener {

    PullToRefreshListView pullListView;
    private PageAction pageAction;
    public static int PAGE_SIZE = 10;
    private int typeValue;
    NoticeBrowseAdapter adapter;
    List<VoteListBean.DataBean> list = new ArrayList<>();
    private DeleteBrowsePop popupwin;
    int deleteId;

    public static BrowseFragment newInstance(int arg) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle bundle = new Bundle();
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
        pullListView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (typeValue == 1) { //浏览记录
                    deleteId = 0;
                    deleteId = list.get(position).getId();
                    popupwin = new DeleteBrowsePop(getActivity(), BrowseFragment.this);
                    popupwin.showAtLocation(getActivity().findViewById(R.id.rl_match), Gravity.CENTER, 0, 0);
                } else { // 我的帖子

                }
                return true;
            }
        });
    }

    private void runService() {
        CommentBodyBean bodyBean = new CommentBodyBean();
        bodyBean.setToken(StoreApplication.token);
        User user = StoreApplication.user;
        if (user != null) {
            bodyBean.setUserCode(user.userCode);
        }
        if (typeValue == 1) {
            new BrowseClient(getActivity(), bodyBean).observable()
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
                            } else if(result.getCode() == -2){
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            }
                        }
                    });
        } else {
            new MsgPostClient(getActivity(), bodyBean).observable()
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
                            } else if(result.getCode() == -2){
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            }
                        }
                    });
        }
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
            adapter = new NoticeBrowseAdapter(getActivity(), list);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_toLogin:
                deleteBrowse();
                break;
        }
    }

    private void deleteBrowse() {
        String url = Constant.WEB_SITE + Constant.URL_DELETE_BROWSE;
        Response.Listener<JsonResult<Token>> successListener = new Response.Listener<JsonResult<Token>>() {
            @Override
            public void onResponse(JsonResult<Token> result) {

                if (result == null) {
                    ToastUtil.show(getActivity(), "服务端异常");
                    return;
                }

                if (result.code == 0) {
                    //刷新页面
                    pageAction.setCurrentPage(0);
                    if (popupwin != null) {
                        popupwin.dismiss();
                    }
                    runService();
                    ToastUtil.show(getActivity(), "浏览删除成功");
                } else {
                    ToastUtil.show(getActivity(), "请求失败");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                ToastUtil.show(getActivity(), "请求失败");
            }
        };

        Request<JsonResult<Token>> versionRequest = new GsonRequest<JsonResult<Token>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<Token>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //设置POST请求参数
                Map<String, String> params = new HashMap<>();
                params.put("token", StoreApplication.token);
                params.put("userCode", user.userCode);
                params.put("postIdStr", deleteId + ""); //帖子ID
                params.put("appTypeId", "0");
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
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
