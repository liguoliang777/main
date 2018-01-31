package cn.ngame.store.game.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.MoreGameListAdapter;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;


/**
 * 游戏列表
 * Created by zeng on 2016/6/16.
 */
@SuppressLint("WrongConstant")
public class SeeMoreActivity extends BaseFgActivity {

    private String TAG = SeeMoreActivity.class.getSimpleName();
    private PullToRefreshListView pullListView;
    private LoadStateView loadStateView;
    private MoreGameListAdapter adapter;
    private List<LikeListBean.DataBean.GameListBean> gameInfoList;
    private PageAction pageAction;
    public int PAGE_SIZE = 10;
    private String mLabelId;
    private SeeMoreActivity content;
    private List<TimerTask> timerTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        this.setContentView(R.layout.activity_see_more);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        content =this;
        Intent intent = getIntent();
        String title = intent.getStringExtra(KeyConstant.TITLE);
        mLabelId = intent.getStringExtra(KeyConstant.category_Id);
        Button leftBt = findViewById(R.id.left_bt);
        findViewById(R.id.center_tv).setVisibility(View.GONE);
        leftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.finish();
            }
        });
        leftBt.setText(title);

        loadStateView = findViewById(R.id.loadStateView);
        loadStateView.isShowLoadBut(false);

        pullListView = findViewById(R.id.pullListView);
        pullListView.setPullRefreshEnabled(true); //刷新
        pullListView.setPullLoadEnabled(true); //false,不允许上拉加载
        pullListView.setScrollLoadEnabled(false);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullListView.setPullLoadEnabled(true);
                pageAction.setCurrentPage(0);
                loadStateView.setVisibility(View.GONE);

                gameInfoList.clear();
                if (adapter != null) {
                    adapter.setDate(gameInfoList);
                    adapter.notifyDataSetChanged();
                }

                getGameList(); //下拉,刷新
                ListView refreshableView = pullListView.getRefreshableView();
                refreshableView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //少于指定条数不加载
             /*   int total = pageAction.getTotal();
                if (total < pageAction.getPageSize()) {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                    return;
                }*/

                getGameList(); //上拉,加载更多

            }
        });
        //点击事件
        ListView refreshableView = pullListView.getRefreshableView();
        refreshableView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(content, GameDetailActivity.class);
                intent.putExtra(KeyConstant.ID, adapter.getItem(position).getId());
                startActivity(intent);
            }
        });
        gameInfoList = new ArrayList<>();

        adapter = new MoreGameListAdapter(this, getSupportFragmentManager(), timerTasks,
                gameInfoList);
        refreshableView.setAdapter(adapter);
        getGameList();//第一次进来加载
    }

    private void getGameList() {
        String url = Constant.WEB_SITE + Constant.URL_LABEL_GAME_LIST;
        Response.Listener<LikeListBean> successListener = new Response.Listener<LikeListBean>() {
            @Override
            public void onResponse(LikeListBean result) {
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                if (result == null || result.getData() == null) {
                    if (gameInfoList != null && gameInfoList.size() > 0) {
                        loadStateView.setVisibility(View.GONE);
                        ToastUtil.show(content, getString(R.string.server_exception));
                    } else {
                        loadStateView.setState(LoadStateView.STATE_END, getString(R.string
                                .server_exception));
                        loadStateView.setVisibility(View.VISIBLE);
                    }

                    return;
                }
                LikeListBean.DataBean data = result.getData();
                List<LikeListBean.DataBean.GameListBean> gameList = data.getGameList();
                if (result.getCode() == 0) {
                    if (pageAction.getCurrentPage() == 0) {
                        gameInfoList.clear();
                        adapter.setDate(gameInfoList);

                        //下拉,第一页,数据为空
                        if (gameList == null || gameList.size() == 0) {
                            loadStateView.setVisibility(View.VISIBLE);
                            loadStateView.setState(LoadStateView.STATE_END, getString(R.string
                                    .no_data));

                        } else {
                            /*  if (size > 0) {
                            pageAction.setTotal(data.getTotals());}*/
                            gameInfoList = gameList;
                            adapter.setDate(gameInfoList);
                            loadStateView.setVisibility(View.GONE);
                            pageAction.setCurrentPage(1);
                        }
                    } else {
                        //不是第一页
                      /*  if (size > 0) {
                            pageAction.setTotal(data.getTotals());
                            gameInfoList.addAll(gameList);

                            0    0--10
                            1
                        }*/
                        loadStateView.setVisibility(View.GONE);
                        if (gameList == null) {
                            ToastUtil.show(content, getString(R.string.server_exception));
                            return;
                        }
                        int size = gameList.size();
                        if (size == 0) {
                            ToastUtil.show(content, getString(R.string.no_more_data));
                            pullListView.setHasMoreData(false);
                            return;
                        }
                        pageAction.setCurrentPage(pageAction.getCurrentPage() + 1);
                       /* if ( size < PAGE_SIZE) {

                        }*/
                        //数据不为空
                        if (gameInfoList != null) {
                            gameInfoList.addAll(gameList);
                            Log.d(TAG, "总数:" + gameInfoList.size());
                            for (LikeListBean.DataBean.GameListBean gameListBean : gameInfoList) {
                                String gameName = gameListBean.getGameName();
                            }
                            if (null != adapter) {
                                adapter.setDate(gameInfoList);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            loadStateView.setVisibility(View.VISIBLE);
                            loadStateView.setState(LoadStateView.STATE_END, getString(R.string
                                    .server_exception_2_pullrefresh));
                        }

                        //下拉停留的位置
                     /*   ListView refreshableView = pullListView.getRefreshableView();
                        int index = refreshableView.getFirstVisiblePosition();
                        View v = refreshableView.getChildAt(0);
                        int top = (v == null) ? 0 : (v.getTop() - v.getHeight());
                        refreshableView.setSelectionFromTop(index, top);*/
                    }


                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                    if (pageAction.getCurrentPage() == 0) {
                        loadStateView.setState(LoadStateView.STATE_END, getString(R.string
                                .server_exception_2_pullrefresh));
                    } else {
                        loadStateView.setVisibility(View.GONE);
                        ToastUtil.show(content, getString(R.string.server_exception));
                    }
                }
                //设置下位列表
          /*      if ((gameInfoList.size() == 0 && pageAction.getTotal() == 0) || gameInfoList
          .size() >= pageAction.getTotal()) {
                    pullListView.setPullLoadEnabled(false);
                } else {
                    pullListView.setPullLoadEnabled(true);
                }*/

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
                if (gameInfoList != null && gameInfoList.size() > 0) {
                    loadStateView.setVisibility(View.GONE);
                    ToastUtil.show(content, getString(R.string.server_exception));
                } else {
                    loadStateView.setState(LoadStateView.STATE_END, getString(R.string
                            .server_exception));
                    loadStateView.setVisibility(View.VISIBLE);
                }
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
            }
        };

        Request<LikeListBean> request = new GsonRequest<LikeListBean>(Request.Method.POST, url,
                successListener, errorListener,
                new TypeToken<LikeListBean>() {
                }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                params.put(KeyConstant.category_Id, mLabelId);
                int startRecord = PAGE_SIZE * pageAction.getCurrentPage();
                params.put(KeyConstant.start_Record, String.valueOf(startRecord));
                params.put(KeyConstant.RECORDS, String.valueOf(PAGE_SIZE));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != adapter && timerTasks != null) {
            adapter.clean();
            for (TimerTask timerTask : timerTasks) {
                timerTask.cancel();
            }
            timerTasks.clear();
        }
    }

}
