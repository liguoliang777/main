package cn.ngame.store.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.VrViewFragmentAdapter;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.VideoLabel;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.video.view.VRVideoFragmentNew;
import cn.ngame.store.video.view.VideoFragment;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.view.VideoTabView;


/**
 * 虚拟现实
 * Created by gp on 2017/3/21 0021.
 */

public class VRActivity extends BaseFgActivity{

    public static final String TAG = VideoFragment.class.getSimpleName();
    private static VideoFragment vrFragment = null;

    private LinearLayout ll_back;
    private TextView tv_title;
    private VideoTabView videoTabView;       //分类标签控件
    private ViewPager viewPager;
    private VrViewFragmentAdapter adapter;
    private LoadStateView loadStateView;
    private List<VideoLabel> videoLabelList;
    //private long currentLabelId;
    private int index = 0;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fictitious_activity);
        init();
    }

    private void init() {
        getCategory();  //加载label

        adapter = new VrViewFragmentAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getIntent().getStringExtra("title"));
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VRActivity.this.finish();
            }
        });
        videoTabView = (VideoTabView) findViewById(R.id.auto_tab);
        videoTabView.setViewPager(viewPager);

        loadStateView = (LoadStateView) findViewById(R.id.loadStateView);
        loadStateView.setReLoadListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStateView.setVisibility(View.VISIBLE);
                loadStateView.setState(LoadStateView.STATE_ING);
                getCategory(); //重新加载
            }
        });
    }

    /** 获取视频分类数据 */
    private void getCategory() {

        String url = Constant.WEB_SITE + Constant.URL_VIDEO_LABEL;
        Response.Listener<JsonResult<List<VideoLabel>>> successListener = new Response.Listener<JsonResult<List<VideoLabel>>>() {
            @Override
            public void onResponse(JsonResult<List<VideoLabel>> result) {

                if (result == null) {
                    loadStateView.setVisibility(View.VISIBLE);
                    loadStateView.setState(LoadStateView.STATE_END);
                    return;
                }

                if (result.code == 0) {
                    videoLabelList = result.data;

                    if(videoLabelList != null && videoLabelList.size() > 0){

                        //currentLabelId = videoLabelList.get(index).id;

                        videoTabView.setVideoLabels(videoLabelList);
                        videoTabView.setCurrentTab(index);

                        ArrayList<Fragment> fragments = new ArrayList<>();
                        for (int i = 0; i < videoLabelList.size(); i++) {

                            VideoLabel label = videoLabelList.get(i);
                            Bundle bundle = new Bundle();
                            bundle.putLong("labelId", label.id);

                            //VRVideoFragment vrVideoFragment = new VRVideoFragment();
                            VRVideoFragmentNew vrVideoFragment = new VRVideoFragmentNew();
                            vrVideoFragment.setArguments(bundle);
                            fragments.add(vrVideoFragment);
                        }
                        adapter.setDate(fragments);
                        adapter.notifyDataSetChanged();
                        viewPager.setCurrentItem(index);

                        loadStateView.setVisibility(View.GONE);
                        loadStateView.setState(LoadStateView.STATE_END);

                    }else {

                        loadStateView.isShowLoadBut(false);
                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_END,"没有数据");
                    }

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误 : " + result.msg);
                    loadStateView.isShowLoadBut(true);
                    loadStateView.setState(LoadStateView.STATE_END);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
                loadStateView.isShowLoadBut(true);
                loadStateView.setState(LoadStateView.STATE_END);
            }
        };

        Request<JsonResult<List<VideoLabel>>> request = new GsonRequest<>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<List<VideoLabel>>>() {}.getType());
        StoreApplication.requestQueue.add(request);
    }
}
