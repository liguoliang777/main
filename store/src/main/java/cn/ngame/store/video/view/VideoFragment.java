package cn.ngame.store.video.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.view.VideoTabView;

/**
 * 用于显示视频首页
 * @author flan
 * @since 2016/9/7
 */
public class VideoFragment extends Fragment {

    public static final String TAG = VideoFragment.class.getSimpleName();

    private static VideoFragment vrFragment = null;
    public static VideoFragment getInstance() {
        if (vrFragment == null) {
            vrFragment = new VideoFragment();
        }
        return vrFragment;
    }

    private VideoTabView videoTabView;       //分类标签控件

    private ViewPager viewPager;
    private VrViewFragmentAdapter adapter;

    private LoadStateView loadStateView;

    private List<VideoLabel> videoLabelList;
    //private long currentLabelId;
    private int index = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.video_frgament_video,container,false);
        getCategory();  //加载label

        adapter = new VrViewFragmentAdapter(getChildFragmentManager());
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        videoTabView = (VideoTabView) view.findViewById(R.id.auto_tab);
        videoTabView.setViewPager(viewPager);

        loadStateView = (LoadStateView) view.findViewById(R.id.loadStateView);
        loadStateView.setReLoadListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStateView.setVisibility(View.VISIBLE);
                loadStateView.setState(LoadStateView.STATE_ING);
                getCategory(); //重新加载
            }
        });

        return view;
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
