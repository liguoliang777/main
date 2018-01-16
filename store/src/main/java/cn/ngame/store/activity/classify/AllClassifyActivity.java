package cn.ngame.store.activity.classify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.jzt.hol.android.jkda.sdk.bean.classification.AllClassifyBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.Classification.ClassifiHomeClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.HomeRaiderAdapter;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.game.view.SeeMoreActivity;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

import static cn.ngame.store.core.utils.ImageUtil.setGridViewHeight;

/**
 * 分类
 * Created by zeng on 2016/6/16.
 */
public class AllClassifyActivity extends BaseFgActivity {

    private PullToRefreshListView pullListView;
    private LoadStateView loadStateView;

    private PageAction pageAction;
    public static int PAGE_SIZE = 20;
    private long categoryId;
    private AllClassifyActivity content;
    private GridView mStyleGView, mCategoryGView, mManufacturerGView, mCountyGV;

    List<AllClassifyBean.DataBean.GameCountyListBean> mCountyList = new ArrayList<>();
    ClassifyCategoryAdapter mCategoryAdapter;
    List<AllClassifyBean.DataBean.GameCategoryListBean> mCategoryList = new ArrayList<>();
    AllClassifyManufacturerAdapter mManufacturerAdapter;
    List<AllClassifyBean.DataBean.GameManufacturerListBean> mManufacturerList = new ArrayList<>();
    AllClassifyStyleAdapter mStyleAdapter;
    List<AllClassifyBean.DataBean.GameStyleListBean> mStyleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        this.setContentView(R.layout.activity_all_classify);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        content = AllClassifyActivity.this;

        findViewById(R.id.center_tv).setVisibility(View.GONE);
        Button leftBt = (Button) findViewById(R.id.left_bt);
        leftBt.setText("分类");
        leftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.finish();
            }
        });
        loadStateView = (LoadStateView) findViewById(R.id.load_state_view);
        pullListView = (PullToRefreshListView) findViewById(R.id.pullListView);
        pullListView.setPullLoadEnabled(false); //false,不允许上拉加载
        pullListView.setScrollLoadEnabled(false);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getGameList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        //添加头布局
        View headView = View.inflate(content, R.layout.all_classify_header_view, null);
        initHeadView(headView);
        //头布局放入listView中
        ListView refreshableView = pullListView.getRefreshableView();
        if (refreshableView.getHeaderViewsCount() == 0) {
            refreshableView.addHeaderView(headView);
        }
        List<String> list = new ArrayList<>();
        list.add(new String("测试"));
        list.add(new String("测试2"));
        HomeRaiderAdapter adapter = new HomeRaiderAdapter(content, list, "0");
        refreshableView.setAdapter(adapter);

        mCountyAdapter = new AllClassifyCountyAdapter(content, mCountyList);
        mCountyGV.setAdapter(mCountyAdapter);

        mStyleAdapter = new AllClassifyStyleAdapter(content, mStyleList);
        mStyleGView.setAdapter(mStyleAdapter);

        mCategoryAdapter = new ClassifyCategoryAdapter(content, mCategoryList);
        mCategoryGView.setAdapter(mCategoryAdapter);

        mManufacturerAdapter = new AllClassifyManufacturerAdapter(content, mManufacturerList);
        mManufacturerGView.setAdapter(mManufacturerAdapter);
        getGameList();
    }

    private void initHeadView(View headView) {
        mStyleGView = (GridView) headView.findViewById(R.id.gridView_remen);
        mCategoryGView = (GridView) headView.findViewById(R.id.gridView_qiangzhan);
        mManufacturerGView = (GridView) headView.findViewById(R.id.gridView_maoxian);
        mCountyGV = (GridView) headView.findViewById(R.id.gridView_dongzuo);
        clickGradView(mCountyGV, 1);
        clickGradView(mCategoryGView, 2);
        clickGradView(mManufacturerGView, 3);
        clickGradView(mStyleGView, 4);
    }

    //条目点击事件 传游戏id，不传lab查询id
    public void clickGradView(GridView gridView, final int i) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent classifyIntent = new Intent(content, SeeMoreActivity.class);
                switch (i) {
                    case 1:
                        if (position < mCountyList.size()) {
                            int typeId = mCountyList.get(position).getId();
                            String typeName = mCountyList.get(position).getCName();
                            classifyIntent.putExtra(KeyConstant.category_Id, typeId + "");//原生手柄 id 367
                            classifyIntent.putExtra(KeyConstant.TITLE, typeName);
                            content.startActivity(classifyIntent);
                        }
                        break;
                    case 2:
                        if (position < mCategoryList.size()) {
                            int typeId = mCategoryList.get(position).getId();
                            String typeName = mCategoryList.get(position).getCName();
                            classifyIntent.putExtra(KeyConstant.category_Id, typeId + "");//原生手柄 id 367
                            classifyIntent.putExtra(KeyConstant.TITLE, typeName);
                            content.startActivity(classifyIntent);
                        }
                        break;
                    case 3:
                        if (position < mManufacturerList.size()) {
                            int typeId = mManufacturerList.get(position).getId();
                            String typeName = mManufacturerList.get(position).getCName();
                            classifyIntent.putExtra(KeyConstant.category_Id, typeId + "");//原生手柄 id 367
                            classifyIntent.putExtra(KeyConstant.TITLE, typeName);
                            content.startActivity(classifyIntent);
                        }
                        break;
                    case 4:
                        if (position < mStyleList.size()) {
                            int typeId = mStyleList.get(position).getId();
                            String typeName = mStyleList.get(position).getCName();
                            classifyIntent.putExtra(KeyConstant.category_Id, typeId + "");//原生手柄 id 367
                            classifyIntent.putExtra(KeyConstant.TITLE, typeName);
                            content.startActivity(classifyIntent);
                        }
                        break;
                }
            }
        });
    }

    private void getGameList() {
        loadStateView.setVisibility(View.VISIBLE);
        loadStateView.setState(LoadStateView.STATE_ING, getString(R.string.loading));
        YunduanBodyBean bodyBean = new YunduanBodyBean();
        new ClassifiHomeClient(content, bodyBean).observable()
                .subscribe(new ObserverWrapper<AllClassifyBean>() {
                    @Override
                    public void onError(Throwable e) {
                        loadStateView.setVisibility(View.GONE);
                    }
                    @Override
                    public void onNext(AllClassifyBean result) {
                        loadStateView.setVisibility(View.GONE);
                        if (result != null && result.getCode() == 0) {
                            listData(result);//更新数据
                        } else {
                        }
                    }
                });
    }

    AllClassifyCountyAdapter mCountyAdapter;

    //更新适配器数据
    private void listData(AllClassifyBean result) {
        if (null == result) {
            return;
        }
        AllClassifyBean.DataBean data = result.getData();
        if (data == null) {
            return;
        }
        //=================游戏特点
        mStyleList = data.getGameStyleList();
        mStyleAdapter.setList(mStyleList);
        setGridViewHeight(mStyleGView);

        //==============游戏类别
        mCategoryList = data.getGameCategoryList();
        mCategoryAdapter.setList(mCategoryList);
        setGridViewHeight(mCategoryGView);
        //=====================国别
        mCountyList = data.getGameCountyList();
        mCountyAdapter.setList(mCountyList);
        setGridViewHeight(mCountyGV);
        //================厂商
        mManufacturerList = data.getGameManufacturerList();
        mManufacturerAdapter.setList(mManufacturerList);
        setGridViewHeight(mManufacturerGView);

        pullListView.onPullUpRefreshComplete();
        pullListView.onPullDownRefreshComplete();
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
    }

}
