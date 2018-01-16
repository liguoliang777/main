package cn.ngame.store.gamehub.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.gamehub.GameHubMainClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.adapter.GameHubAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.HotInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.user.view.LoginActivity;
import cn.ngame.store.video.view.VideoDetailActivity;
import cn.ngame.store.view.BannerView;
import cn.ngame.store.view.PicassoImageView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

import static cn.ngame.store.R.id.banner_view;

/**
 * 游戏圈
 * Created by gp on 2016/8/24.
 */
public class GameHubOldFragment extends BaseSearchFragment implements View.OnClickListener {

    public static final String TAG = GameHubOldFragment.class.getSimpleName();
    private BannerView bannerView;
    private PullToRefreshListView pullListView;
    private GameHubAdapter adapter;
    private LinearLayout ll_gl, ll_qz, ll_tp;
    private RelativeLayout rl_notifi, rl_add;
    private TextView et_search;
    List<GameHubMainBean.DataBean> list = new ArrayList<>();
    private PageAction pageAction;
    public static int PAGE_SIZE = 10;

    public static GameHubOldFragment newInstance() {
        Bundle args = new Bundle();
        GameHubOldFragment fragment = new GameHubOldFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_game_hub;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        //top栏+listview
        rl_add = (RelativeLayout) view.findViewById(R.id.rl_add);
        rl_notifi = (RelativeLayout) view.findViewById(R.id.rl_notifi);
        et_search = (TextView) view.findViewById(R.id.et_search);
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pullListView);
        rl_add.setOnClickListener(this);
        rl_notifi.setOnClickListener(this);
        et_search.setOnClickListener(this);

        initListView();
        getBannerData();
    }

    @Override
    public void onResume() {
        super.onResume();
        pageAction.setCurrentPage(0);
        getDataList();
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
                getDataList();
                getBannerData();
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
                    pageAction.setCurrentPage(pageAction.getCurrentPage() == 0 ? pageAction.getCurrentPage() + 2 : pageAction
                            .getCurrentPage() + 1);
                    getDataList();
                } else {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                }
            }
        });
        pullListView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(getActivity(), MsgDetailActivity.class);
//                int position = v.getId();
                i.putExtra("msgId", list.get(position - 1).getId());
                startActivity(i);
            }
        });
        //添加头布局
        View view = View.inflate(getActivity(), R.layout.gamehub_head_view, null);
        initHeadView(view);
        //头布局放入listView中
        if (pullListView.getRefreshableView().getHeaderViewsCount() == 0) {
            pullListView.getRefreshableView().addHeaderView(view);
        }
    }

    private void initHeadView(View view) {
        bannerView = (BannerView) view.findViewById(banner_view);
        ll_gl = (LinearLayout) view.findViewById(R.id.ll_gl);
        ll_qz = (LinearLayout) view.findViewById(R.id.ll_qz);
        ll_tp = (LinearLayout) view.findViewById(R.id.ll_tp);
        ll_gl.setOnClickListener(this);
        ll_qz.setOnClickListener(this);
        ll_tp.setOnClickListener(this);
    }

    // 请求game圈首页列表
    private void getDataList() {
        GameHubMainBodyBean bodyBean = new GameHubMainBodyBean();
        bodyBean.setPageIndex(0);
        bodyBean.setPageIndex(pageAction.getCurrentPage());
        new GameHubMainClient(getActivity(), bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<GameHubMainBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(GameHubMainBean result) {
                        if (result != null && result.getCode() == 0) {
                            listData(result);
                        } else {
//                            ToastUtil.show(getActivity(), result.getMsg());
                        }
                        pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();
                        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                    }
                });
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
                pullListView.getRefreshableView().setAdapter(adapter); //数据位空时，加载头部
                return;
            }
        }
        if (result.getData().size() > 0) {
            pageAction.setTotal(result.getTotals());
            this.list.addAll(result.getData());
        }
        if (adapter == null) {
            adapter = new GameHubAdapter(getActivity(), list);
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

    /**
     * 获取轮播图片数据
     */
    private void getBannerData() {
        String url = Constant.WEB_SITE + Constant.URL_BANNER;
        Response.Listener<JsonResult<List<HotInfo>>> successListener = new Response.Listener<JsonResult<List<HotInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<HotInfo>> result) {

                if (result == null) {
//                    Toast.makeText(getActivity(), "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.code == 0) {

                    List<ImageView> list = createBannerView(result.data);
                    bannerView.setData(list);

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<List<HotInfo>>> request = new GsonRequest<JsonResult<List<HotInfo>>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<List<HotInfo>>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("type", String.valueOf(42));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    /**
     * 创建轮播视图
     */
    private List<ImageView> createBannerView(List<HotInfo> hotInfoList) {

        if (hotInfoList == null || hotInfoList.size() <= 0) {
            return null;
        }

        ArrayList<ImageView> list = new ArrayList<>();
        for (int i = 0; i < hotInfoList.size(); i++) {

            final HotInfo info = hotInfoList.get(i);
            PicassoImageView img = new PicassoImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                    .LayoutParams.MATCH_PARENT);
            img.setLayoutParams(params);
            img.setId((int) info.id);
            img.setTag(info.advImageLink);

            img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.type == 1) {
                        Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                        intent.putExtra(KeyConstant.ID, info.gameId);
                        startActivity(intent);
                    } else if (info.type == 2) {
                        Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                        intent.putExtra(KeyConstant.ID, info.videoId);
                        startActivity(intent);
                    }
                }
            });
            list.add(img);
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_gl:
                Intent i = new Intent();
                i.setClass(getActivity(), StrategyActivity.class);
                i.putExtra("typeValue", "1");
                startActivity(i);
                break;
            case R.id.ll_qz:
                Intent j = new Intent();
                j.setClass(getActivity(), StrategyActivity.class);
                j.putExtra("typeValue", "2");
                startActivity(j);
                break;
            case R.id.ll_tp:
                startActivity(new Intent(getActivity(), VoteActivity.class));
                break;
            case R.id.rl_notifi:
                if (StoreApplication.passWord == "") {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), NoticeActivity.class));
                }
                break;
            case R.id.rl_add:
                if (StoreApplication.passWord == "") {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), PostMsgActivity.class));
                }
                break;
            case R.id.et_search:
                startActivity(new Intent(getActivity(), SearchPostActivity.class));
                break;
        }
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
