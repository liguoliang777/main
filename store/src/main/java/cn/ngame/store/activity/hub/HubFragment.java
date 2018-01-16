package cn.ngame.store.activity.hub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.gamehub.GameHubMainClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.NetUtil;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

/**
 * 精选
 * Created by gp on 2017/3/14 0014.
 */
@SuppressLint("WrongConstant")
public class HubFragment extends BaseSearchFragment {
    public static final String TAG = HubFragment.class.getSimpleName();
    private PullToRefreshListView pullListView;
    private ImageView game_big_pic_1, game_big_pic_2;
    private SimpleDraweeView from_img_1, from_img_2;
    private TextView gamename_1, gamename_2, summary_2;
    private TextView from_1, from_2, summary_1;
    private LoadStateView loadStateView;
    private HubFragmentAdapter adapter;
    private PageAction pageAction;
    public static int PAGE_SIZE = 8;
    List<GameHubMainBean.DataBean> topList = new ArrayList<>();
    List<GameHubMainBean.DataBean> list = new ArrayList<>();
    private FragmentActivity context;
    private Intent singeTopicsDetailIntent = new Intent();
    private LinearLayout.LayoutParams hParams;
    private int wrapContent;
    private boolean mIsShow = false;
    private ListView refreshableView;
    private Picasso picasso;


    public static HubFragment newInstance(int arg) {
        HubFragment fragment = new HubFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initViewsAndEvents(View view) {
//        typeValue = getArguments().getInt("", 1);
        context = getActivity();
        picasso = Picasso.with(context);
        initListView(view);     //初始化
    }


    private void getGameList() {
        loadStateView.setVisibility(View.VISIBLE);
        loadStateView.setState(LoadStateView.STATE_ING);

        GameHubMainBodyBean bodyBean = new GameHubMainBodyBean();
        bodyBean.setPageIndex(0);
        bodyBean.setPageIndex(pageAction.getCurrentPage());
        new GameHubMainClient(getActivity(), bodyBean).observable()
                .subscribe(new ObserverWrapper<GameHubMainBean>() {
                    @Override
                    public void onError(Throwable e) {
                        if (list != null && list.size() > 0) {
                            loadStateView.setVisibility(View.GONE);
                            ToastUtil.show(context, getString(R.string.server_exception_2_pullrefresh));
                        } else {
                            loadStateView.setState(LoadStateView.STATE_END, getString(R.string.server_exception_2_pullrefresh));
                            loadStateView.setVisibility(View.VISIBLE);
                        }
                        pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();
                    }

                    @Override
                    public void onNext(GameHubMainBean result) {
                        if (result != null && result.getCode() == 0) {
                            listData(result);
                        } else {
                            loadStateView.setState(LoadStateView.STATE_END, getString(R.string.server_exception));
                            pullListView.onPullUpRefreshComplete();
                            pullListView.onPullDownRefreshComplete();
                        }
                    }
                });
    }


    private List<YunduanBean.DataBean> gameInfo;

    public void listData(GameHubMainBean result) {
        loadStateView.setVisibility(View.GONE);
        if (result.getData() == null) {
            return;
        }
        if (pageAction.getCurrentPage() == 0) {//当前页
            this.list.clear(); //清除数据
            this.topList.clear();
            if (result.getData() == null || result.getData().size() == 0) {
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                return;
            }
        }
        List<GameHubMainBean.DataBean> resultData = result.getData();
        int totals = result.getTotals();
        if (result.getData().size() > 0) {//刷新后进来
            pageAction.setTotal(totals);
            this.list.addAll(resultData);
            this.topList.addAll(resultData);
        }
        if (result.getData().size() > 0 && pageAction.getCurrentPage() == 0) {
            //第一次进来
            this.list.clear(); //清除数据
            this.topList.clear();
            pageAction.setTotal(totals);
            this.list.addAll(resultData); //清除数据
            this.topList.addAll(resultData);
            //设置头部布局
          /*  if (list.size() > 1) {
                setHeaderInfo(list);
                list.remove(0);
                list.remove(0);
            }*/
        }
        if (adapter == null) {
            adapter = new HubFragmentAdapter(context, getSupportFragmentManager(), list, 0);
            pullListView.getRefreshableView().setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
       /* if ((mStickyLV.size() == 0 && pageAction.getTotal() == 0) || mStickyLV.size() >= pageAction.getTotal()) {
            pullListView.setPullLoadEnabled(true);
        } else {
            pullListView.setPullLoadEnabled(true);
        }*/

        if (0 == pageAction.getCurrentPage() && result.getData().size() <= 2) {
            //pullListView.setScrollLoadEnabled(false);
            //pullListView.setPullRefreshEnabled(false);
            //pullListView.setPullLoadEnabled(false);
            pullListView.getRefreshableView().setSelection(0);
        }
        if (pageAction.getCurrentPage() > 0 && result.getData().size() > 2) {//// TODO: 2017/7/17 0017
            int index = pullListView.getRefreshableView().getFirstVisiblePosition();
            View v = pullListView.getRefreshableView().getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - v.getHeight());
            pullListView.getRefreshableView().setSelectionFromTop(index, top);
        }
        pullListView.onPullUpRefreshComplete();
        pullListView.onPullDownRefreshComplete();
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
    }

    public void initListView(final View view) {
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        loadStateView =  view.findViewById(R.id.load_state_view2);
        loadStateView.isShowLoadBut(false);
        pullListView =  view.findViewById(R.id.pullListView);
        pullListView.setPullLoadEnabled(true);
        pullListView.setPullRefreshEnabled(true);
        pullListView.setScrollLoadEnabled(true);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadStateView.setVisibility(View.GONE);
                pullListView.setPullLoadEnabled(true);
                pageAction.setCurrentPage(0);
                if (!NetUtil.isNetworkConnected(context)) {
                    pullListView.onPullUpRefreshComplete();
                    pullListView.onPullDownRefreshComplete();
                    if (0 == pageAction.getCurrentPage()) {
                        pullListView.getRefreshableView().setSelection(0);
                    }
                    if (list != null && list.size() > 0) {
                        ToastUtil.show(context, getString(R.string.no_network));
                    } else {
                        ToastUtil.show(context, getString(R.string.no_network));
                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_END, getString(R.string.no_network));
                    }
                } else {
                    //下拉请求数据
                    getGameList();//竖着的,游戏位
                }
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //少于指定条数不加载
                if (pageAction.getTotal() < pageAction.getPageSize()) {
                    pullListView.setHasMoreData(false);
                    ToastUtil.show(context, getString(R.string.no_more_data));
                    pullListView.onPullUpRefreshComplete();
                    return;
                }
                if (pageAction.getCurrentPage() * pageAction.getPageSize() < pageAction.getTotal()) {
                    pageAction.setCurrentPage(pageAction.getCurrentPage() == 0 ?
                            pageAction.getCurrentPage() + 2 : pageAction.getCurrentPage() + 1);
                    //上拉请求数据
                    getGameList();
                } else {
                    ToastUtil.show(context, getString(R.string.no_more_data));
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                }
            }
        });
        //点击事件
        refreshableView = pullListView.getRefreshableView();
        refreshableView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    /**   pullListView的头部position是0  第一个item 索引是 1
                     1-1= 0(所以position是1时,要拿list里的0处数据, position是2时,拿1处数据)   */
                    GameHubMainBean.DataBean dataBean = list.get(position);
                    //埋点
                /*    HashMap<String, String> map = new HashMap<>();
                    map.put(KeyConstant.index, position + "");
                    map.put(KeyConstant.game_Name, dataBean.getGameName());
                    MobclickAgent.onEvent(context, UMEventNameConstant.mainRecommendPositionClickCount, map);
*/
                    Intent intent = new Intent(context, HubItemActivity.class);
                    intent.putExtra(KeyConstant.ID, dataBean.getId());
                    startActivity(intent);
            }
        });
     /*   //滑动事件(搜索栏渐变)
        refreshableView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                }

            }

            //向下头部颜色渐变
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });*/

        //添加头布局
   /*     View headView = View.inflate(context, R.layout.recommend_header_view, null);
        //initHeadView(headView);
        //头布局放入listView中
        if (refreshableView.getHeaderViewsCount() == 0) {
            refreshableView.addHeaderView(headView);
        }*/
        //第一次进来,请求数据
        if (!NetUtil.isNetworkConnected(context)) {
            pullListView.onPullUpRefreshComplete();
            pullListView.onPullDownRefreshComplete();
            if (0 == pageAction.getCurrentPage()) {
                pullListView.getRefreshableView().setSelection(0);
            }
            if (list != null && list.size() > 0) {
                ToastUtil.show(context, getString(R.string.no_network));
            } else {
                loadStateView.setVisibility(View.VISIBLE);
                loadStateView.setState(LoadStateView.STATE_END, getString(R.string.no_network));
            }
        } else {
            getGameList();
        }
    }

    public void scroll2Top() {
        if (mIsShow && pullListView != null) {
            ListView refreshableView = pullListView.getRefreshableView();
            //refreshableView.setSelectionAfterHeaderView();
            int firstVisiblePosition = refreshableView.getFirstVisiblePosition();
            View childAt0 = refreshableView.getChildAt(0);
            if (null == childAt0) {
                return;
            }
            int top = childAt0.getTop();
            if (firstVisiblePosition == 0 && top == 0) {
                getGameList();
            } else {
                refreshableView.setSelection(0);
            }
        }
    }
    //头部点击

    //设置头部数据
    public void setHeaderInfo(List<RecommendListBean.DataBean> list) {
        RecommendListBean.DataBean dataBean0 = list.get(0);
        RecommendListBean.DataBean dataBean1 = list.get(1);

        if (null == dataBean0) {
            return;
        }

        from_img_1.setImageURI(dataBean0.getGameLogo());//来自...头像
        picasso.load(dataBean0.getGameRecommendImg()).placeholder(R.drawable.ic_def_logo_720_288)
                .error(R.drawable.ic_def_logo_720_288)
                // .resize(screenWidth,150)
                .into(game_big_pic_1);
        gamename_1.setText(dataBean0.getGameName());
        from_1.setText("来自" + dataBean0.getRecommender());
        summary_1.setText(dataBean0.getRecommend());

        if (null == dataBean1) {
            return;
        }
        picasso.load(dataBean1.getGameRecommendImg()).placeholder(R.drawable.ic_def_logo_720_288)
                .error(R.drawable.ic_def_logo_720_288)
                // .resize(screenWidth,150)
                .into(game_big_pic_2);
        from_img_2.setImageURI(dataBean1.getGameLogo());//来自...头像

        gamename_2.setText(dataBean1.getGameName());
        from_2.setText("" + dataBean1.getRecommender());
        summary_2.setText(dataBean1.getRecommend());

        //广告
       // setAdView();
    }

    /**
     * 广告
     */
 /*   private void setAdView() {
        InMobiSdk.init(context, Constant.InMobiSdk_Id);

        InMobiNative nativeAd = new InMobiNative(context, Constant.AD_PlacementID_RecommendFragment, new InMobiNative
                .NativeAdListener() {
            @Override
            public void onAdLoadSucceeded(@NonNull InMobiNative inMobiNative) {
                adLayout.setVisibility(View.VISIBLE);
                //JSONObject content = inMobiNative.getCustomAdContent();
                NewsSnippet item = new NewsSnippet();
                item.title = inMobiNative.getAdTitle();
                gamename_ad.setText(item.title);//广告标题

                item.imageUrl = inMobiNative.getAdIconUrl();
                from_img_ad.setImageURI(item.imageUrl);

                item.description = inMobiNative.getAdDescription();
                summary_ad.setText(item.description);

                item.inMobiNative = new WeakReference<>(inMobiNative);
                //item.view =inMobiNative.getPrimaryViewOfWidth(mAdapter.,viewGroup,0);
                adContainer.removeAllViews();
                adContainer.addView(inMobiNative.getPrimaryViewOfWidth(adContainer, adContainer,
                        adContainer.getWidth()));
            }

            @Override
            public void onAdLoadFailed(@NonNull InMobiNative inMobiNative, @NonNull InMobiAdRequestStatus inMobiAdRequestStatus) {
                Log.d(TAG, "广告加载失败");
                adLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAdFullScreenDismissed(InMobiNative inMobiNative) {
                Log.d(TAG, "onAdFullScreenDismissed");
            }

            @Override
            public void onAdFullScreenWillDisplay(InMobiNative inMobiNative) {
                Log.d(TAG, "onAdFullScreenWillDisplay");
            }

            @Override
            public void onAdFullScreenDisplayed(InMobiNative inMobiNative) {
                Log.d(TAG, "onAdFullScreenDisplayed");
            }

            @Override
            public void onUserWillLeaveApplication(InMobiNative inMobiNative) {
                Log.d(TAG, "onUserWillLeaveApplication");
            }

            @Override
            public void onAdImpressed(@NonNull InMobiNative inMobiNative) {
                Log.d(TAG, "onAdImpressed");
            }

            @Override
            public void onAdClicked(@NonNull InMobiNative inMobiNative) {
                if (inMobiNative == null) {
                    return;
                }
                //广告埋点
                HashMap<String, String> map = new HashMap<>();
                map.put(KeyConstant.index, 1 + "");
                map.put(KeyConstant.game_Name, inMobiNative.getAdTitle());
                MobclickAgent.onEvent(context, UMEventNameConstant.mainRecommendPositionClickCount, map);
            }

            @Override
            public void onMediaPlaybackComplete(@NonNull InMobiNative inMobiNative) {
                Log.d(TAG, "onMediaPlaybackComplete");
            }

            @Override
            public void onAdStatusChanged(@NonNull InMobiNative inMobiNative) {
                Log.d(TAG, "onAdStatusChanged");
            }
        });

        Map<String, String> map = new HashMap<>();
        nativeAd.setExtras(map);
        nativeAd.setDownloaderEnabled(true);
        nativeAd.load();
    }*/

    @Override
    protected void onFirstUserVisible() {
        Log.d(TAG, "onUserVisible:当前 " + pageAction.getCurrentPage());
    }

    @Override
    protected void onUserVisible() {
        Log.d(TAG, "onUserVisible:当前 " + pageAction.getCurrentPage());
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadView(View view) {
        return null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        list = null;
    }

    public void setShow(boolean isShow) {
        mIsShow = isShow;
    }

}
