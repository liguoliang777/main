package cn.ngame.store.activity.classify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jzt.hol.android.jkda.sdk.bean.main.DiscoverListBean;
import com.jzt.hol.android.jkda.sdk.bean.main.DiscoverTopBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBody;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.main.DiscoverClient;
import com.jzt.hol.android.jkda.sdk.services.main.YunduanClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.main.TopicsDetailActivity;
import cn.ngame.store.adapter.classify.ClassifyIvAdapter;
import cn.ngame.store.adapter.classify.ClassifyTopAdapter;
import cn.ngame.store.adapter.classify.DiscoverTop2Adapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.ClassifyTopBean;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.NetUtil;
import cn.ngame.store.game.view.SeeMoreActivity;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.BannerView;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.view.PicassoImageView;
import cn.ngame.store.view.RecyclerViewDivider;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

/**
 * 分类
 * Created by gp on 2017/3/14 0014.
 */
@SuppressLint("WrongConstant")
public class DiscoverFragment extends BaseSearchFragment implements View.OnClickListener {
    private FragmentActivity context;
    private PullToRefreshListView pullListView;
    private RecyclerView mClassifyAllRv;
    private ClassifyTopAdapter categroyTopAdapter;
    private BannerView bannerView;
    private List<DiscoverTopBean> mEverydayList = new ArrayList();
    private ClassifyIvAdapter mTopicsAdapter;
    private RecyclerView mEverydayRv;
    private RecyclerView mHotRecentRv;
    private RecyclerView mSubjectRv;
    private DiscoverTop2Adapter mEverydayAdapter;
    private List<DiscoverTopBean> mHotRecentList = new ArrayList();
    private DiscoverTop2Adapter mHotRecentAdapter;
    private PicassoImageView picassoImageView;
    private int match_parent;
    private LinearLayout.LayoutParams hParams;
    private String selectImage;
    private List<YunduanBean.DataBean> topicsInfoList = new ArrayList<>();
    private ClassifyAdapter categroy18Adapter;
    private List<ClassifyTopBean> categroyAllList = new ArrayList<>();
    private List<DiscoverListBean.DataBean.ResultListBean> categroy18ListBean = new ArrayList<>();
    private DiscoverListBean.DataBean.DailyNewGamesListBean dailyNewGames;
    private DiscoverListBean.DataBean.WeeklyNewGamesListBean hotGames;
    private int categoryId = -1;
    private String categoryName = "";
    private LinearLayoutManager linearLayoutManager;
    private ClassifyTopBean mClassifyTopBean;

    public DiscoverFragment() {
        android.util.Log.d(TAG, "DiscoverFragment: ()");
    }

    public static DiscoverFragment newInstance(String arg) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle bundle = new Bundle();
        bundle.putString("", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        context = getActivity();
        init(view);
        View headView = View.inflate(context, R.layout.classify_header_view, null);//头部
        //分类
        init0ClassifyView(headView);
        getBannerData();

        //每日新发现
        initEveryday(headView);
        //近期最热
        initHotRecentView(headView);
        //   ===  专题   ====
        //initTopicsView(headView);

        //添加头部
        ListView refreshableView = pullListView.getRefreshableView();
        if (refreshableView.getHeaderViewsCount() == 0) {
            refreshableView.addHeaderView(headView);
        }
        getData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {//3
        super.onActivityCreated(savedInstanceState);
    }

    //分类
    private void init0ClassifyView(View headView) {
        bannerView = headView.findViewById(R.id.banner_view);
        //获取RecyclerView实例
        LinearLayoutManager lLManager = new LinearLayoutManager(context);
        lLManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mClassifyAllRv = headView.findViewById(R.id.discover_head_rv_classify);//条目
        mClassifyAllRv.setLayoutManager(lLManager);
        categroyAllList.add(new ClassifyTopBean("键鼠", 120, R.drawable.ic_jianshu));
        categroyAllList.add(new ClassifyTopBean("手柄", 101, R.drawable.ic_shoubin));
        categroyAllList.add(new ClassifyTopBean("破解", 103, R.drawable.ic_pojie));
        categroyAllList.add(new ClassifyTopBean("模拟器", 153, R.drawable.ic_moniqi));
        categroyAllList.add(new ClassifyTopBean("汉化", 104, R.drawable.ic_hanhua));
        categroyAllList.add(new ClassifyTopBean("策略", 111, R.drawable.ic_sheji));
        categroyAllList.add(new ClassifyTopBean("角色", 107, R.drawable.ic_juese));
        categroyAllList.add(new ClassifyTopBean("冒险", 109, R.drawable.ic_maoxian));
        //categroyAllList.add(new ClassifyTopBean("全部",-1,R.drawable.ic_quanbu));

        categroyTopAdapter = new ClassifyTopAdapter(context, categroyAllList);
        mClassifyAllRv.setAdapter(categroyTopAdapter);
    }

    //每日最新
    private void initEveryday(View headView) {
        mEverydayRv = headView.findViewById(R.id.everyday_discover_recyclerview);
        setOnMoreBtClickListener(headView, R.id.everyday_more_tv1);
        GridLayoutManager layoutManage = new GridLayoutManager(getContext(), 2);
        mEverydayRv.setLayoutManager(layoutManage);
        mEverydayAdapter = new DiscoverTop2Adapter(context, mEverydayList);
        mEverydayRv.setAdapter(mEverydayAdapter);
        mEverydayRv.addItemDecoration(new RecyclerViewDivider(context,
                R.dimen.main_margin_left_30px, R.dimen.main_margin_24px, mEverydayList.size()));
    }

    //近期最热
    private void initHotRecentView(View headView) {
        mHotRecentRv = (RecyclerView) headView.findViewById(R.id.rv_hot_recent);
        GridLayoutManager layoutManage = new GridLayoutManager(getContext(), 2);
        mHotRecentRv.setLayoutManager(layoutManage);
        mHotRecentAdapter = new DiscoverTop2Adapter(context, mHotRecentList);
        mHotRecentRv.setAdapter(mHotRecentAdapter);
        mHotRecentRv.addItemDecoration(new RecyclerViewDivider(context,
                R.dimen.dm018, R.dimen.dm010, mHotRecentList.size()));
        setOnMoreBtClickListener(headView, R.id.more_hot_recent_tv);
    }

    //专题
    private void initTopicsView(View headView) {
        //mSubjectRv = headView.findViewById(R.id.rv_subject);
        //setOnMoreBtClickListener(headView, R.id.more_subject_tv);
        linearLayoutManager = new LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false);
        mSubjectRv.setLayoutManager(linearLayoutManager);

        mTopicsAdapter = new ClassifyIvAdapter(context, topicsInfoList);
        mSubjectRv.setAdapter(mTopicsAdapter);
        //条目距离
        mSubjectRv.addItemDecoration(new RecyclerViewDivider(context,
                R.dimen.dm018, R.dimen.dm010, topicsInfoList.size()));
    }

    //更多按钮设置点击监听
    private void setOnMoreBtClickListener(View headView, int moreBtId) {
        headView.findViewById(moreBtId).setOnClickListener(mMoreBtClickListener);
    }

    //查看更多 按钮点击
    View.OnClickListener mMoreBtClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            Intent intent = new Intent();
            //每日新发现
            if (id == R.id.everyday_more_tv1) {
                if (null != dailyNewGames) {
                    categoryId = dailyNewGames.getCategoryId();
                    categoryName = dailyNewGames.getCategoryName();
                }
                intent.setClass(context, SeeMoreActivity.class);
                intent.putExtra(KeyConstant.category_Id, String.valueOf(categoryId));//云端适配id 336
                intent.putExtra(KeyConstant.TITLE, categoryName);
                //近期最热
            } else if (id == R.id.more_hot_recent_tv) {
                if (null != hotGames) {
                    categoryId = hotGames.getCategoryId();
                    categoryName = hotGames.getCategoryName();
                }
                intent.setClass(context, SeeMoreActivity.class);
                intent.putExtra(KeyConstant.category_Id, String.valueOf(categoryId));//云端适配id 336
                intent.putExtra(KeyConstant.TITLE, categoryName);
                //专题
            } /*else if (id == R.id.more_subject_tv) {
                intent.setClass(context, TopicsListActivity.class);
                //大厂
            }  else if (id == R.id.more_big_chang_tv) {
                intent.setClass(context, TopicsListActivity.class);
            }else if (id == R.id.more_strategy_tv) {
                intent.setClass(context, LabelGameListActivity.class);
                intent.putExtra(KeyConstant.category_Id, 368 + "");//单机id 336
                intent.putExtra(KeyConstant.TITLE, "策略");
                context.startActivity(intent);
            }*/
            context.startActivity(intent);
        }
    };
    private LoadStateView loadStateView;

    private void init(View view) {
        loadStateView = (LoadStateView) view.findViewById(R.id.load_state_view);
        loadStateView.isShowLoadBut(false);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pullListView);
        pullListView.setPullRefreshEnabled(true); //false,不允许上拉加载
        //pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            //下拉
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData();
                getBannerData();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                pullListView.onPullDownRefreshComplete();
                pullListView.onPullUpRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //少于指定条数不加载
              /*  if (pageAction.getTotal() < pageAction.getPageSize()) {
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
                    getData();
                } else {
                    ToastUtil.show(context, getString(R.string.no_more_data));
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                }*/
            }
        });
        categroy18Adapter = new ClassifyAdapter(context, getSupportFragmentManager(),
                categroy18ListBean, 0);
        pullListView.getRefreshableView().setAdapter(categroy18Adapter);
    }

    private final static String TAG = DiscoverFragment.class.getSimpleName();

    //请求数据
    private void getData() {
        loadStateView.setVisibility(View.VISIBLE);
        loadStateView.setState(LoadStateView.STATE_ING);
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, "网络异常,请检查网络设置");
            pullListView.getRefreshableView().setSelection(0);
            loadStateView.setVisibility(View.GONE);
            return;
        }
        //请求数据
        RecommendListBody bodyBean = new RecommendListBody();
        new DiscoverClient(context, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<DiscoverListBean>() {
                    @Override
                    public void onError(Throwable e) {
                       /* pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();*/
                        loadStateView.setVisibility(View.GONE);
                        ToastUtil.show(context, getString(R.string.server_exception));
                    }

                    @Override
                    public void onNext(DiscoverListBean result) {
                     /*   pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();
                        pullListView.setLastUpdatedLabel(new Date().toLocaleString());*/
                        loadStateView.setVisibility(View.GONE);
                        if (result != null && result.getCode() == 0) {
                            setData(result);
                        } else {
                            //请求失败
                            android.util.Log.d(TAG, "请求失败");
                        }

                    }
                });
    }

    // 设置数据
    public void setData(DiscoverListBean result) {
        DiscoverListBean.DataBean data = result.getData();
        if (data == null || context == null) {
            return;
        }
        //----------------------- 每日最新 ------------------------------
        dailyNewGames = data.getDailyNewGamesList();
        if (null != dailyNewGames) {
            mEverydayList = dailyNewGames.getList();
            //每日新发现
            mEverydayAdapter.setList(mEverydayList);
            mEverydayRv.setAdapter(mEverydayAdapter);
        }

        //近期最热
        hotGames = data.getWeeklyNewGamesList();
        if (null != hotGames) {
            mHotRecentList = hotGames.getList();
            //每日新发现
            mHotRecentAdapter.setList(mHotRecentList);
            mHotRecentRv.setAdapter(mHotRecentAdapter);
        }
        //下面18个分类
        categroy18ListBean = data.getResultList();
        categroy18Adapter.setList(categroy18ListBean);
        pullListView.getRefreshableView().setAdapter(categroy18Adapter);
    }

    private PageAction pageAction;
    public static int PAGE_SIZE = 8;


    /**
     * 获取轮播图片数据
     */
    private void getBannerData() {
        if (list != null) {
            list.clear();
        }
        new YunduanClient(context, new YunduanBodyBean()).observable()
                .subscribe(new ObserverWrapper<YunduanBean>() {
                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(YunduanBean result) {
                        if (result != null && result.getCode() == 0) {
                            topicsInfoList = result.getData();
                            List<ImageView> list = createBannerView(topicsInfoList);
                            bannerView.setData(list);

                            //mTopicsAdapter.setList(topicsInfoList);
                            //mSubjectRv.setAdapter(mTopicsAdapter);
                        } else {
                        }
                    }
                });

    }

    /**
     * 创建轮播视图
     */
    private ArrayList<ImageView> list = new ArrayList<>();

    private List<ImageView> createBannerView(List<YunduanBean.DataBean> bannerInfoList) {

        int bannerListSize = bannerInfoList.size();
        if (bannerInfoList == null || bannerListSize <= 0) {
            return null;
        }

        int ic_def_logo_720_228 = R.drawable.ic_def_logo_720_288;
        match_parent = ViewGroup.LayoutParams.MATCH_PARENT;
        final Intent singeTopicsDetailIntent = new Intent();
        singeTopicsDetailIntent.setClass(context, TopicsDetailActivity.class);
        hParams = new LinearLayout.LayoutParams(
                match_parent, match_parent);
        hParams.height = CommonUtil.dip2px(context, 158f);
        for (int i = 0; i < bannerListSize; i++) {
            final YunduanBean.DataBean info = bannerInfoList.get(i);
            selectImage = info.getLogoUrl();
            picassoImageView = new PicassoImageView(context);
            picassoImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            picassoImageView.setLayoutParams(hParams);
            // picassoImageView.setId(info.getId());
            // picassoImageView.setTag(info.getSelectImage());
            picassoImageView.setImageUrl(selectImage, ic_def_logo_720_228);
            picassoImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            picassoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singeTopicsDetailIntent.putExtra(KeyConstant.category_Id, info.getId());
                    singeTopicsDetailIntent.putExtra(KeyConstant.TITLE, info.getTypeName());
                    singeTopicsDetailIntent.putExtra(KeyConstant.DESC, info.getTypeDesc());
                    singeTopicsDetailIntent.putExtra(KeyConstant.URL, info.getLogoUrl());
                    startActivity(singeTopicsDetailIntent);
                }
            });
            list.add(picassoImageView);
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private boolean mIsShow = false;

    public void setShow(boolean isShow) {
        mIsShow = isShow;
    }

    public void scroll2Top() {
        if (mIsShow && pullListView != null) {
            //refreshableView.setSelectionAfterHeaderView();
            ListView refreshableView = pullListView.getRefreshableView();
            //refreshableView.setSelectionAfterHeaderView();
            refreshableView.setSelection(0);
            //getGameList();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
