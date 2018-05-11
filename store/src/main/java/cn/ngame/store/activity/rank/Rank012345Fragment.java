package cn.ngame.store.activity.rank;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBean;
import com.jzt.hol.android.jkda.sdk.bean.rank.RankListBody;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.game.GameCommentListClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.adapter.Ranking012345Adapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.NetUtil;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.ExRadioGroup;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

/**
 * 下载榜
 * Created by gp on 2017/3/22 0022.
 */
@SuppressLint({"ValidFragment", "WrongConstant"})
public class Rank012345Fragment extends BaseSearchFragment {
    private PullToRefreshListView pullListView;
    private Ranking012345Adapter adapter;
    protected final static String TAG = Rank012345Fragment.class.getSimpleName();
    private PageAction pageAction;
    private int PAGE_SIZE = 10;
    private List<LikeListBean.DataBean.GameListBean> list = new ArrayList<>();
    private int tab0_Id = 163;

    /*    private Handler handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                //获取系统定义的最低滑动距离
                mTouchSlop = ViewConfiguration.get(content).getScaledTouchSlop();
                mTopCheckedTv.setVisibility(View.GONE);

                Log.d(TAG, mShow + "索引777: " + tab0_Id);

                list.clear();
                adapter.setList(list);
                //getRankList();

                return;
            }
        };*/
    private FragmentActivity content;
    private int tab2_Id = 0;
    private FragmentManager fm;
    private LoadStateView loadStateView;
    private int px20;
    private int px86;
    private int px18;
    private int mTouchSlop;
    private float mFirstY;
    private float mLastY;
    private boolean mShow = true;
    private ObjectAnimator mAnimator;
    private ListView refreshableView;
    private TextView mTopCheckedTv;
    private ExRadioGroup mTabLayout2_ExRadioGroup;
    private XTabLayout mTabLayout0;
    private String tab0Text = "全部分类";
    private int tabArrIndex = 0;

    public static Rank012345Fragment newInstance(int type) {
        Rank012345Fragment fragment = new Rank012345Fragment(0);
        Bundle bundle = new Bundle();
        //bundle.putString("type", type+"");
        fragment.setArguments(bundle);
        return fragment;
    }

    public Rank012345Fragment(int serial) {
    }

    public Rank012345Fragment() {
    }

/*    public void sendMessage() {
        Message message = handler.obtainMessage();
        message.sendToTarget();
    }*/

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_rank01234;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void initViewsAndEvents(View view) {
        content = getActivity();
        px20 = getResources().getDimensionPixelOffset(R.dimen.dm020);
        px86 = getResources().getDimensionPixelOffset(R.dimen.dm080);
        px18 = getResources().getDimensionPixelOffset(R.dimen.dm018);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        fm = getSupportFragmentManager();
        pullListView = view.findViewById(R.id.pullListView);
        //mTopCheckedTv = view.findViewById(R.id.rank01234_top_llay);

        mTopCheckedTv = view.findViewById(R.id.fragment_01234_top_show_checked_tv);
        mTopCheckedTv.setText(tab0Text + "·" + tab2_Country_Text_Array[tabArrIndex]);
        mTopCheckedTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startAnim(1);
                mTopCheckedTv.setVisibility(View.GONE);
                refreshableView.setSelection(0);
            }
        });
        View headView = View.inflate(content, R.layout.fragment_rank_012345_header, null);
        mTabLayout2_ExRadioGroup = headView.findViewById(R.id.rank01234_header_rg);

        mTabLayout0 = headView.findViewById(R.id.rank01234_header_tablayout);
        mTabLayout0.addTab(mTabLayout0.newTab().setText("全部分类"));
        mTabLayout0.addTab(mTabLayout0.newTab().setText("国别"));
        mTabLayout0.getTabAt(1).select();
        mTabLayout0.getTabAt(0).select();
        mTabLayout0.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                int positionTop = tab.getPosition();
                //全部分类
                if (positionTop == 0) {
                    mTabLayout2_ExRadioGroup.removeAllViews();
                    initTabLayout2(tab2_Categary_Text_Array, tab2_Categary_Id_Array);
                } else {
                    mTabLayout2_ExRadioGroup.removeAllViews();
                    initTabLayout2(tab2_Country_Text_Array, tab2_Country_Id_Array);
                }

                mTabLayout2_ExRadioGroup.check(0);
                tab2_Id = 0;

                list.clear();
                adapter.setList(list);
                pageAction.setCurrentPage(0);
                //重新请求数据
                getRankList();

                tab0Text = tab.getText().toString();
                mTopCheckedTv.setText(tab0Text + "·" + tab2_Country_Text_Array[0]);
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });

        //二级标签
        initTabLayout2(tab2_Categary_Text_Array, tab2_Categary_Id_Array);

        loadStateView = view.findViewById(R.id.load_state_view);
        loadStateView.isShowLoadBut(false);
        pullListView.setPullRefreshEnabled(true);
        pullListView.setPullLoadEnabled(true);
        pullListView.setScrollLoadEnabled(false);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullListView.setPullLoadEnabled(true);
                pageAction.setCurrentPage(0);
                list.clear();
                adapter.setList(list);
//                getGameList();
                getRankList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //少于指定条数不加载
                int currentPage = pageAction.getCurrentPage();
                //当前页
                if (list.size() < pageAction.getTotal()) {
                    pageAction.setCurrentPage(currentPage + 1);
                    getRankList();
                } else {
                    pullListView.setHasMoreData(false);
                    ToastUtil.show(content, "没有更多数据了");
                    pullListView.onPullUpRefreshComplete();
                }
            }
        });
        //点击事件
        refreshableView = pullListView.getRefreshableView();
        adapter = new Ranking012345Adapter(content, fm, list, 0);
    /*    mTopCheckedTv.measure(0, 0);
        int measuredWidth = mTopCheckedTv.getMeasuredWidth();
        int measuredH = mTopCheckedTv.getMeasuredHeight();
        Log.d(TAG, "测量高度:" + measuredWidth);
        Log.d(TAG, "测量:" + measuredH);*/
        if (refreshableView.getHeaderViewsCount() == 0) {
            refreshableView.addHeaderView(headView);
        }
        refreshableView.setAdapter(adapter);
        refreshableView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                    intent.putExtra(KeyConstant.ID, list.get(position - 1).getId());
                    startActivity(intent);
                }
            }
        });

        //mTopCheckedTv.setVisibility(View.GONE);
        //tablayout2 = (TabLayout) view.findViewById(R.id.rank01234_tablayout);
 /*       if (IMITATOR_ID == tab0_Id) {
            //默认FC的id
            tab2_Id = NDS_ID;
            int length = tabList5.length;
            for (int i = 0; i < length; i++) {
                tablayout2.addTab(tablayout2.newTab().setText(tabList5[i]));
            }
            tab2_Country_Id_Array = tab2_id5;
            //二级标签
            initTabs1234();
        } else {
            int length = tab2_Country_Text_Array.length;
            for (int i = 0; i < length; i++) {
                tablayout2.addTab(tablayout2.newTab().setText(tab2_Country_Text_Array[i]));
            }
            //二级标签
            initTabs1234();
        }*/
      /*  if (refreshableView != null) {
            refreshableView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mFirstY = event.getY();//getY获取的是相对于View的坐标，getRawY获取的是相对于屏幕的坐标
                            break;
                        case MotionEvent.ACTION_MOVE:
                            mLastY = event.getY();
                            if (mLastY - mFirstY > mTouchSlop) {//手指向下滑动，显示toolbar
                                if (!mShow) {
                                    Log.i("tag", "mLastY_手指下滑=" + (mLastY - mFirstY));
                                    startAnim(0);//显示
                                }
                            } else if (mFirstY - mLastY > mTouchSlop && list != null && list.size
                                    () > 5) {//手指向上滑动，隐藏toolbar
                                if (mShow) {
                                    Log.i("tag", "mLastY_手指上滑=" + (mFirstY - mLastY));
                                    startAnim(1);//隐藏
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    return false;//一般返回false，提交给上级
                }
            });
        }*/
        pullListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int i1, int i2) {
                int visibility = mTopCheckedTv.getVisibility();
                if (firstVisibleItem > 0) {//&& list != null && list.size() > 5
                    if (visibility != View.VISIBLE) {
                        mTopCheckedTv.setVisibility(View.VISIBLE);
                    }
                    //startAnim(0);//显示
                } else {
                    if (visibility == View.VISIBLE) {
                        mTopCheckedTv.setVisibility(View.GONE);
                    }
                }
            }
        });
        getRankList();
    }

    private void initTabLayout2(final String[] tab2StringArr, final int[] tab2IdArr) {
        int length = tab2StringArr.length;
        for (int position = 0; position < length; position++) {
            RadioButton codeBtn = new RadioButton(content);
            if (0 == position) {
                codeBtn.setChecked(true);
            }
            if (null == codeBtn) {
                return;
            }
            codeBtn.setBackgroundResource(R.drawable.selector_rank_tab_top_bg);
            codeBtn.setTextColor(ContextCompat.getColorStateList(content, R.color
                    .fragment_rank01234_top_text_color));
            codeBtn.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            codeBtn.setTextSize(14.8f);
            codeBtn.setId(position);
            codeBtn.setText(tab2StringArr[position]);
            codeBtn.setPadding(25, 10, 25, 15);

            codeBtn.setGravity(Gravity.CENTER);
            final int finalPosition = position;
            codeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tabArrIndex = finalPosition;
                    tab2_Id = tab2IdArr[finalPosition];
                    list.clear();
                    adapter.setList(list);
                    pageAction.setCurrentPage(0);
                    getRankList();
                    mTopCheckedTv.setText(tab0Text + "·" + tab2StringArr[tabArrIndex]);
                }
            });
            LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                    .WRAP_CONTENT, LinearLayout.LayoutParams
                    .WRAP_CONTENT);

            codeBtn.setLayoutParams(rlp);

            mTabLayout2_ExRadioGroup.addView(codeBtn);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) codeBtn
                    .getLayoutParams();
            layoutParams.setMargins(40, 40, 0, 0);//4个参数按顺序分别是左上右下
            codeBtn.setLayoutParams(layoutParams);
        }
    }

    public void startAnim(int flag) {

        //第一个参数用于指定这个动画要操作的是哪个控件   0 == 显示
        //第二个参数用于指定这个动画要操作这个控件的哪个属性
        //第三个参数是可变长参数,指这个属性值是从多少变到多少
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        if (flag == 0) {
            Log.i("tag", "下滑===========显示");
            mAnimator = ObjectAnimator.ofFloat(mTopCheckedTv, "translationY", mTopCheckedTv
                            .getTranslationY()
                    , 0);
        } else if (flag == 1) {
            Log.i("tag", "上滑===========隐藏");
            mAnimator = ObjectAnimator.ofFloat(mTopCheckedTv, "translationY", mTopCheckedTv
                            .getTranslationY(),
                    -mTopCheckedTv.getHeight());
        }
        mAnimator.start();//开始动画

        mShow = !mShow;
    }

    //第一级标签
    private int tab0_Id_Arr[] = new int[]{163, 164, 165};

    //第二级标签   =======   全部分类
    private String tab2_Categary_Text_Array[] = new String[]{"全部", "原生手柄", "云适配", "破解", "汉化", "特色",
            "角色", "冒险", "策略", "模拟器",
    };
    private int tab2_Categary_Id_Array[] = new int[]{0, 101, 102, 103, 104, 106, 107, 109, 111,
            112};

    //             =======  国别
    private String tab2_Country_Text_Array[] = new String[]{"全部", "大陆", "美国", "日本", "韩国", "港澳台"};
    private int tab2_Country_Id_Array[] = new int[]{0, 147, 149, 150, 151, 148};

    //顶部下面的二级标签
    private void initTabs1234() {
        ViewGroup viewGroup = (ViewGroup) (View) mTopCheckedTv;//测试,删除
        //ViewGroup viewGroup = (ViewGroup) tablayout2.getChildAt(0);
        int childCount = viewGroup.getChildCount() - 1;
        for (int i = 0; i <= childCount; i++) {
            ViewGroup view = (ViewGroup) viewGroup.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view
                    .getLayoutParams();
            TextView textView = (TextView) view.getChildAt(1);
            textView.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.AT_MOST);//textciew的宽度
            // AT_MOST
            textView.setTextSize(px20);
            textView.setSingleLine();
            layoutParams.weight = px86;
            layoutParams.height = px20 * 2;
            if (i < childCount) {
                layoutParams.setMargins(0, 0, px18, 0);
            }
        }
    }

    /**
     * 获取排行榜列表数据
     */
    @SuppressLint("WrongConstant")
    private void getRankList() {
        // Log.d(TAG, tab0_Id + ",请求数据,当前索引:" + tab2_Id);
        //tab0_Id :0=全部   1=手柄   2=破解   3=汉化  4=特色
        loadStateView.setVisibility(View.VISIBLE);
        loadStateView.setState(LoadStateView.STATE_ING);
        int startRecord = pageAction.getCurrentPage() * PAGE_SIZE;
        Log.d(TAG, "排行榜------参数: " + tab0_Id + "  " + tab2_Id + "========" + startRecord + "   " +
                PAGE_SIZE);
        RankListBody bodyBean = new RankListBody();
        bodyBean.setStartRecord(startRecord);
        bodyBean.setRecords(PAGE_SIZE);
        bodyBean.setParentCategoryId(tab0_Id);
        bodyBean.setCategoryId(tab2_Id);
        new GameCommentListClient(content, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<LikeListBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                        pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();
                        if (!NetUtil.isNetworkConnected(content)) {
                            loadStateView.setState(LoadStateView.STATE_END, getString(R.string
                                    .no_network));
                        } else {
                            loadStateView.setState(LoadStateView.STATE_END, getString(R.string
                                    .requery_failed));
                        }
                    }

                    @Override
                    public void onNext(LikeListBean result) {
                        if (result != null && result.getCode() == 0) {
                            listData(result);
                        } else {
                            if (list != null && list.size() > 0) {
                                ToastUtil.show(content, getString(R.string
                                        .server_exception_2_pullrefresh));
                            } else {
                                loadStateView.setState(LoadStateView.STATE_END, getString(R.string
                                        .server_exception_2_pullrefresh));
                            }
                            pullListView.onPullUpRefreshComplete();
                            pullListView.onPullDownRefreshComplete();
                        }
                    }
                });
    }

    //设置数据
    @SuppressLint("WrongConstant")
    public void listData(LikeListBean dataBean) {
        LikeListBean.DataBean result = dataBean.getData();
        if (result == null || result.getGameList() == null) {
            loadStateView.setState(LoadStateView.STATE_END, getString(R.string.no_data));
            return;
        }
        List<LikeListBean.DataBean.GameListBean> gameList = result.getGameList();
        int size = gameList.size();
        if (pageAction.getCurrentPage() == 0) {
            list.clear(); //清除数据
            adapter.setList(list);
            if (gameList == null || size == 0) {
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                loadStateView.setState(LoadStateView.STATE_END, getString(R.string.no_data));
                return;
            }
        }
        loadStateView.setVisibility(View.GONE);
        if (size > 0) {
            pageAction.setTotal(result.getTotals());
            list.addAll(gameList);
        }
        if (size > 0 && pageAction.getCurrentPage() == 0) {
            list = gameList;
            pageAction.setTotal(result.getTotals());
        }
        ListView refreshableView = pullListView.getRefreshableView();
        //设置适配器
        adapter.setList(list);
        Log.d(TAG, "排行榜----总数: "+list.size());
        if (pageAction.getCurrentPage() > 0 && size > 0) { //设置上拉刷新后停留的地方
            int index = refreshableView.getFirstVisiblePosition();
            View v = refreshableView.getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - v.getHeight());
            refreshableView.setSelectionFromTop(index, top);
        }
        pullListView.onPullUpRefreshComplete();
        pullListView.onPullDownRefreshComplete();
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        list = null;
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

    public void setTab(int tabIndex) {
        if (refreshableView != null && adapter != null) {
            list.clear();
            adapter.setList(list);
            pageAction.setCurrentPage(0);
            refreshableView.setSelection(0);
            mTabLayout2_ExRadioGroup.check(0);
            mTabLayout0.getTabAt(0).select();
        }
        tab2_Id=0;
        tab0_Id = tab0_Id_Arr[tabIndex];
        getRankList();
    }
}
