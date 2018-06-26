package cn.ngame.store.activity.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.main.MallBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBean;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBody;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.main.MallClient;
import com.jzt.hol.android.jkda.sdk.services.main.YunduanClient;
import com.jzt.hol.android.jkda.sdk.services.recommend.RecommendClient;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import cn.ngame.store.R;
import cn.ngame.store.adapter.Recommend0Adapter;
import cn.ngame.store.adapter.RecommendListAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.NetUtil;
import cn.ngame.store.core.utils.UMEventNameConstant;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.view.PullScrollView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static android.content.Context.SENSOR_SERVICE;

/**
 * 精选
 * Created by gp on 2017/3/14 0014.
 */
@SuppressLint("WrongConstant")
public class RecommendFragment extends BaseSearchFragment {
    public static final String TAG = RecommendFragment.class.getSimpleName();
    private PullToRefreshListView pullListView;
    private ImageView game_big_pic_1, game_big_pic_2;
    private SimpleDraweeView fromImg_1, from_img_ad, from_img_2;
    private TextView gamename_1, gamename_2, summary_2, summary_ad, title_ad;
    private TextView summary_1;
    private LoadStateView loadStateView;
    private RecommendListAdapter adapter;
    private PageAction pageAction;
    public int PAGE_SIZE = 8;
    List<RecommendListBean.DataBean> topList = new ArrayList<>();
    List<RecommendListBean.DataBean> list = new ArrayList<>();
    private LinearLayout mallLayout;
    private FragmentActivity context;
    private Intent singeTopicsDetailIntent = new Intent();
    private LinearLayout.LayoutParams hParams;
    private SimpleDraweeView simpleImageView;
    private boolean mIsShow = false;
    private ListView refreshableView;
    private Picasso picasso;
    private PullScrollView boutiqueLayout;
    private PullScrollView shopMallLayout;
    private ListView listView0;
    private Recommend0Adapter list0Adapter;
    private SensorManager sensorManager;
    private JZVideoPlayer.JZAutoFullscreenListener sensorEventListener;
    private int firstVisibleItem;

    public static RecommendFragment newInstance(int arg) {
        RecommendFragment fragment = new RecommendFragment();
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
        context = getActivity();
        picasso = Picasso.with(context);
        initListView(view);
    }

    private void getGameList() {
        RecommendListBody bodyBean = new RecommendListBody();
        bodyBean.setPageIndex(pageAction.getCurrentPage());
        bodyBean.setPageSize(PAGE_SIZE);
        new RecommendClient(getActivity(), bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<RecommendListBean>() {
                    @Override
                    public void onError(Throwable e) {
                        if (list != null && list.size() > 0) {
                            loadStateView.setVisibility(View.GONE);
                            ToastUtil.show(context, getString(R.string
                                    .server_exception_2_pullrefresh));
                        } else {
                            loadStateView.setState(LoadStateView.STATE_END, getString(R.string
                                    .server_exception_2_pullrefresh));
                            loadStateView.setVisibility(View.VISIBLE);
                        }
                        pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();
                    }

                    @Override
                    public void onNext(RecommendListBean result) {
                        if (result != null && result.getCode() == 0) {
                            listData(result);
                        } else {
                            loadStateView.setVisibility(View.GONE);
                            pullListView.onPullUpRefreshComplete();
                            pullListView.onPullDownRefreshComplete();
                        }
                    }
                });
    }


    private List<YunduanBean.DataBean> gameInfo;

    private void getHorizontalData2() {
        RecommendListBody bodyBean = new RecommendListBody();
        bodyBean.setPageIndex(1);
        bodyBean.setPageSize(50);
        new MallClient(context, bodyBean).observable()
                .subscribe(new ObserverWrapper<MallBean>() {
                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(MallBean result) {
                        if (result != null && result.getCode() == 0) {
                            List<MallBean.DataBean> data = result.getData();
                            if (null == data || data.size() == 0) {
                                Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                            } else {
                                mallLayout.removeAllViews();
                                int size = data.size();
                                Resources resources = getResources();
                                int pxRound = resources.getDimensionPixelOffset(R.dimen.dm000);
                                int pxTop = resources.getDimensionPixelOffset(R.dimen.dm016);
                                int pxHeight = resources.getDimensionPixelOffset(R.dimen.dm500);

                                for (int i = 0; i < size; i++) {
                                    MallBean.DataBean info = data.get(i);
                                    final String gameImage = info.getMallImg();
                                    final String mallUrl = info.getMallLink();
                                    simpleImageView = new SimpleDraweeView(context);
                                    RoundingParams roundingParams = RoundingParams
                                            .fromCornersRadius(pxRound);
                                    GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder
                                            .newInstance(resources)
                                            .setPlaceholderImage(R.color.e5e5e5)
                                            .setFailureImage(R.color.e5e5e5)
                                            .setActualImageScaleType(ScalingUtils.ScaleType
                                                    .FOCUS_CROP)
                                            .setRoundingParams(roundingParams)
                                            .setFadeDuration(0)
                                            .build();
                                    simpleImageView.setHierarchy(hierarchy);
                                    //为  PicassoImageView设置属性
                                    hParams = new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT, pxHeight);
                                    // hParams.height = pxHeight;
                                    //有多个图片的话
                                    hParams.setMargins(0, 0, 0, pxTop);
                                    simpleImageView.setLayoutParams(hParams);
                                    //加载网络图片
                                    simpleImageView.setImageURI(gameImage);
                                    simpleImageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent();
                                            intent.setAction("android.intent.action.VIEW");
                                            intent.setData(Uri.parse(mallUrl));
                                            startActivity(intent);
                                        }
                                    });
                                    mallLayout.addView(simpleImageView, i);
                                }
                            }
                        } else {
                        }
                    }
                });
    }

    private void getHorizontalData() {
        loadStateView.setVisibility(View.VISIBLE);
        loadStateView.setState(LoadStateView.STATE_ING);
        YunduanBodyBean bodyBean = new YunduanBodyBean();
        new YunduanClient(context, bodyBean).observable()
                .subscribe(new ObserverWrapper<YunduanBean>() {
                    @Override
                    public void onError(Throwable e) {
                    }

                    @SuppressLint("NewApi")
                    @Override
                    public void onNext(YunduanBean result) {
                        if (result != null && result.getCode() == 0 && context != null) {
                            gameInfo = result.getData();
                            if (null == gameInfo || gameInfo.size() == 0) {
                                Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                                loadStateView.setVisibility(View.VISIBLE);
                                loadStateView.setState(LoadStateView.STATE_END, getString(R.string
                                        .no_data));
                            } else {
                                loadStateView.setVisibility(View.GONE);
                                list0Adapter.setDate(gameInfo);
                                setListViewHeightBasedOnChildren(listView0);
                            }
                        } else {
                            loadStateView.setVisibility(View.VISIBLE);
                            loadStateView.setState(LoadStateView.STATE_END, getString(R.string
                                    .no_data));
                        }
                    }
                });
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        View view;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, null, listView);
            //宽度为屏幕宽度
            int i1 = View.MeasureSpec.makeMeasureSpec(ImageUtil.getScreenWidth(context),
                    View.MeasureSpec.EXACTLY);
            //根据屏幕宽度计算高度
            int i2 = View.MeasureSpec.makeMeasureSpec(i1, View.MeasureSpec.UNSPECIFIED);
            view.measure(i1, i2);
            totalHeight += view.getMeasuredHeight();
        }
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void listData(RecommendListBean result) {
        if (result.getData() == null) {
            return;
        }
        loadStateView.setVisibility(View.GONE);
        if (pageAction.getCurrentPage() == 0) {//当前页
            if (list != null) {
                this.list.clear(); //清除数据
                this.topList.clear();
            }
            if (result.getData() == null || result.getData().size() == 0) {
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                return;
            }
        }
        List<RecommendListBean.DataBean> resultData = result.getData();
        int totals = result.getTotals();
        if (result.getData().size() > 0) {//刷新后进来
            pageAction.setTotal(totals);
            if (list != null) {
                this.list.addAll(resultData);
                this.topList.addAll(resultData);
            }
        }
        if (result.getData().size() > 0 && pageAction.getCurrentPage() == 0 && list != null) {
            //第一次进来
            this.list.clear(); //清除数据
            this.topList.clear();
            pageAction.setTotal(totals);
            this.list.addAll(resultData); //清除数据
            this.topList.addAll(resultData);
          /*  if (list.size() > 1) {
                setHeaderInfo(list);//设置头部布局
            }*/
        }
        if (adapter == null) {
            adapter = new RecommendListAdapter(context, getSupportFragmentManager(), list, 0);
            pullListView.getRefreshableView().setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
        //设置下位
       /* if ((mStickyLV.size() == 0 && pageAction.getTotal() == 0) || mStickyLV.size() >=
       pageAction.getTotal()) {
            pullListView.setPullLoadEnabled(true);
        } else {
            pullListView.setPullLoadEnabled(true);
        }*/
        //设置上拉刷新后停留的地方

        if (0 == pageAction.getCurrentPage() && result.getData().size() <= 2) {
            //pullListView.setScrollLoadEnabled(false);
            //pullListView.setPullRefreshEnabled(false);
            //pullListView.setPullLoadEnabled(false);
            pullListView.getRefreshableView().setSelection(0);
        }
        if (pageAction.getCurrentPage() > 0 && result.getData().size() > 2) {
            int index = pullListView.getRefreshableView().getFirstVisiblePosition();
            View v = pullListView.getRefreshableView().getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - v.getHeight());
            pullListView.getRefreshableView().setSelectionFromTop(index, top);
        }
        pullListView.onPullUpRefreshComplete();
        pullListView.onPullDownRefreshComplete();
        pullListView.setLastUpdatedLabel(new Date().toLocaleString()); //全名  功夫  最终
    }

    @Override
    public void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.
                getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener,
                accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @SuppressLint("NewApi")
    public void initListView(final View view) {
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        loadStateView = (LoadStateView) view.findViewById(R.id.load_state_view2);

        boutiqueLayout = (PullScrollView) view.findViewById(R.id.recommend_1_boutique);
        mallLayout = (LinearLayout) view.findViewById(R.id.recommend_2_mall_layout);

        listView0 = (ListView) view.findViewById(R.id.horizontalView_container);
        list0Adapter = new Recommend0Adapter(context, gameInfo);
        listView0.setFocusable(false);
        listView0.setAdapter(list0Adapter);

        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JZVideoPlayer.JZAutoFullscreenListener();

        shopMallLayout = (PullScrollView) view.findViewById(R.id.recommend_2_mall);
        loadStateView.isShowLoadBut(false);
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pullListView);

        pullListView.setPullLoadEnabled(true);
        pullListView.setPullRefreshEnabled(true);
        pullListView.setScrollLoadEnabled(true);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
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
                if (pageAction.getCurrentPage() * pageAction.getPageSize() < pageAction.getTotal
                        ()) {
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
                RecommendListBean.DataBean dataBean = list.get(position);
                //埋点
                HashMap<String, String> map = new HashMap<>();
                map.put(KeyConstant.index, position + "");
                map.put(KeyConstant.game_Name, dataBean.getGameName());
                MobclickAgent.onEvent(context, UMEventNameConstant
                        .mainRecommendPositionClickCount, map);

                Intent intent = new Intent(context, GameDetailActivity.class);
                intent.putExtra(KeyConstant.ID, dataBean.getGameId());
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
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
            }
        });*/

        //todo添加头布局
        singeTopicsDetailIntent.setClass(context, TopicsDetailActivity.class);
    /*    View headView = View.inflate(context, R.layout.recommend_header_view, null);
        initHeadView(headView);
        //头布局放入listView中
        if (refreshableView.getHeaderViewsCount() == 0) {
            refreshableView.addHeaderView(headView);
        }*/
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
            getHorizontalData();
            getHorizontalData2();
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

    //顶部2个位置
    private void initHeadView(View view) {
        fromImg_1 = (SimpleDraweeView) view.findViewById(R.id.sdv_img_from_iv);//来自 头像
        from_img_2 = (SimpleDraweeView) view.findViewById(R.id.img_from_2);

        gamename_1 = (TextView) view.findViewById(R.id.tv_gamename_1);//游戏名字
        gamename_2 = (TextView) view.findViewById(R.id.tv_gamename_2);


        game_big_pic_1 = (ImageView) view.findViewById(R.id.recommend_game_pic_1);//游戏图片
        game_big_pic_2 = (ImageView) view.findViewById(R.id.recommend_game_pic_2);

        summary_1 = (TextView) view.findViewById(R.id.tv_summary1);//游戏摘要
        summary_2 = (TextView) view.findViewById(R.id.tv_summary2);

        //view.findViewById(R.id.recommend_head_llay_0).setOnClickListener(headClickListener);
        //view.findViewById(R.id.recommend_head_llay_1).setOnClickListener(headClickListener);

    }

    //头部点击
    private View.OnClickListener headClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, GameDetailActivity.class);
            switch (v.getId()) {
                case R.id.recommend_head_llay_0:
                    RecommendListBean.DataBean dataBean = topList.get(0);
                    //埋点
                    HashMap<String, String> map = new HashMap<>();
                    map.put(KeyConstant.index, 0 + "");
                    map.put(KeyConstant.game_Name, dataBean.getGameName());
                    MobclickAgent.onEvent(context, UMEventNameConstant
                            .mainRecommendPositionClickCount, map);

                    intent.putExtra(KeyConstant.ID, dataBean.getGameId());
                    startActivity(intent);
                    break;
                case R.id.recommend_head_llay_1:
                    RecommendListBean.DataBean dataBean1 = topList.get(1);
                    //埋点
                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put(KeyConstant.index, 2 + "");
                    map1.put(KeyConstant.game_Name, dataBean1.getGameName());
                    MobclickAgent.onEvent(context, UMEventNameConstant
                            .mainRecommendPositionClickCount, map1);
                    intent.putExtra(KeyConstant.ID, dataBean1.getGameId());
                    startActivity(intent);
                    break;

                //查看更多--全部专题
                //startActivity(new Intent(context, TopicsListActivity.class));
            }
        }
    };

    //设置头部数据
    public void setHeaderInfo(List<RecommendListBean.DataBean> list) {
        RecommendListBean.DataBean dataBean0 = list.get(0);
        RecommendListBean.DataBean dataBean1 = list.get(1);

        if (null == dataBean0) {
            return;
        }

        fromImg_1.setImageURI(dataBean0.getGameLogo());//来自...头像
        picasso.load(dataBean0.getGameRecommendImg()).placeholder(R.drawable.ic_def_logo_720_288)
                .error(R.drawable.ic_def_logo_720_288)
                // .resize(screenWidth,150)
                .into(game_big_pic_1);
        gamename_1.setText(dataBean0.getGameName());
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
        summary_2.setText(dataBean1.getRecommend());

    }

    /**
     * 广告
     */
  /*  private void setAdView() {
        InMobiSdk.init(context, Constant.InMobiSdk_Id);

        InMobiNative nativeAd = new InMobiNative(context, Constant
        .AD_PlacementID_RecommendFragment, new InMobiNative
                .NativeAdListener() {
            @Override
            public void onAdLoadSucceeded(@NonNull InMobiNative inMobiNative) {
                adLayout.setVisibility(View.VISIBLE);
                //JSONObject content = inMobiNative.getCustomAdContent();
                NewsSnippet item = new NewsSnippet();
                item.title = inMobiNative.getAdTitle();
                title_ad.setText(item.title);//广告标题

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
            public void onAdLoadFailed(@NonNull InMobiNative inMobiNative, @NonNull
            InMobiAdRequestStatus inMobiAdRequestStatus) {
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
                MobclickAgent.onEvent(context, UMEventNameConstant
                .mainRecommendPositionClickCount, map);
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

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }

    public void setShow(boolean isShow) {
        mIsShow = isShow;
    }

    public void setTab(int position) {
        boutiqueLayout.setVisibility(0 == position ? View.VISIBLE : View.GONE);
        pullListView.setVisibility(1 == position ? View.VISIBLE : View.GONE);
        shopMallLayout.setVisibility(2 == position ? View.VISIBLE : View.GONE);
        //pullListView.setVisibility(0 == position?View.VISIBLE:View.GONE);

    }
}
