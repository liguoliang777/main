package cn.ngame.store.activity.hub;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.HubAdapter;
import cn.ngame.store.bean.PostsInfo;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.util.ToastUtil;

/**
 * 圈子
 * Created by liguoliang on 2017/11/23 0023.
 */
@SuppressLint("WrongConstant")
public class HubPostsActivity extends BaseFgActivity {
    protected static final String TAG = HubPostsActivity.class.getSimpleName();
    private LinearLayout ll_back;
    private HubPostsActivity mContext;
    private TextView titleTv;
    private RecyclerView mRecyclerView;
    private HubAdapter mAdapter;
    private TextView headerLastUpdateTv;
    private List<PostsInfo.DataBean> mDatas=new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_posts);
        initStatusBar();
        mContext = this;
        init();
    }

    private void init() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        titleTv = (TextView) findViewById(R.id.tv_title);

        titleTv.setText("圈子");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setPrimaryColors(Color.WHITE);
        refreshLayout.autoRefresh();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new HubAdapter(mContext, mDatas);
        mRecyclerView.setAdapter(mAdapter);

        //设置 ==============  Header的样式
        final ClassicsHeader header = new ClassicsHeader(this);
        header.getTitleText().setTextSize(14);
        headerLastUpdateTv = header.getLastUpdateText();
        headerLastUpdateTv.setVisibility(View.GONE);
        header.setDrawableProgressSizePx(62);
        header.setDrawableArrowSizePx(57);
        refreshLayout.setRefreshHeader(header, ImageUtil.getScreenWidth(this), 200);
        //设置 ==============  Footer
        ClassicsFooter footer = new ClassicsFooter(mContext);
        footer.getTitleText().setTextSize(14);
        footer.setDrawableArrowSizePx(57);
        refreshLayout.setRefreshFooter(footer, ImageUtil.getScreenWidth(this), 180);
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //请求数据
                getDatas(refreshLayout);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(0);
            /*    mDatas.add("新数据11");
                mAdapter.setData(mDatas);*/
            }
        });

    }

    private void getDatas(final RefreshLayout refreshLayout) {
        String url = Constant.WEB_SITE + Constant.URL_POSTS_LIST;
        Response.Listener<PostsInfo> successListener = new Response.Listener<PostsInfo>() {
            @Override
            public void onResponse(PostsInfo result) {
                if (result == null || result.getCode() != 0) {
                    ToastUtil.show(mContext, getString(R.string.server_exception));
                    return;
                }
                mDatas = result.getData();
                mAdapter.setData(mDatas);

                refreshLayout.finishRefresh(0);
                headerLastUpdateTv.setVisibility(View.GONE);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                refreshLayout.finishRefresh(0);
                headerLastUpdateTv.setVisibility(View.GONE);
            }
        };

        Request<PostsInfo> request = new GsonRequest<PostsInfo>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<PostsInfo>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.parentId, Constant.APP_TYPE_ID_0_ANDROID);
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }


}