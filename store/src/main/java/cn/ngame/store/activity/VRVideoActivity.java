package cn.ngame.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.adapter.VrViewFragmentAdapter;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.VideoLabel;
import cn.ngame.store.video.view.VRVideoFragmentNew;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.search.view.SearchActivity;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.video.view.VideoPlayActivity;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.view.SimpleTitleBar;
import cn.ngame.store.view.VideoTabView;

/**
 * 显示VR视频分类的界面
 * Created by zeng on 2016/6/7.
 */
public class VRVideoActivity extends BaseFgActivity {

    public static final String TAG = VideoPlayActivity.class.getSimpleName();
    private SimpleTitleBar title_bar;
    private VideoTabView videoTabView;       //分类标签控件

    private ViewPager viewPager;
    private VrViewFragmentAdapter adapter;

    private LoadStateView loadStateView;

    private List<VideoLabel> videoLabelList;
    private long currentLabelId;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_vr_video);

        Intent intent = getIntent();
        index = intent.getIntExtra("index",0);  //0是全景，1是3D电影

        getCategory();  //加载label

        title_bar = (SimpleTitleBar) findViewById(R.id.title_bar);
        title_bar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VRVideoActivity.this.finish();
            }
        });
        title_bar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VRVideoActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        adapter = new VrViewFragmentAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

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

    /**
     * 获取视频分类数据
     */
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

                        currentLabelId = videoLabelList.get(index).id;

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
                successListener, errorListener, new TypeToken<JsonResult<List<VideoLabel>>>() {
        }.getType());
        StoreApplication.requestQueue.add(request);
    }

}
