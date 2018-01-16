package cn.ngame.store.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.VRVideoActivity;
import cn.ngame.store.bean.HotInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.VRHotInfo;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.game.view.VRGameActivity;
import cn.ngame.store.video.view.VideoDetailActivity;
import cn.ngame.store.view.BannerView;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.view.PicassoImageView;

/**
 * 显示酷炫VR的Fragment
 * @author flan
 * @since 2016/5/9
 */
public class VRFragment extends Fragment {

    public static final String TAG = VRFragment.class.getSimpleName();
    private static VRFragment vrFragment = null;
    private Context context;
    private BannerView bannerView;
    private LinearLayout bt_Video, bt_Game,bt_Qj, game_container, video_container, qj_container;

    private LoadStateView loadStateView;
    private LinearLayout layout_1;

    private VRHotInfo vrHotInfo;

    public static VRFragment getInstance() {
        if (vrFragment == null) {
            vrFragment = new VRFragment();
        }
        return vrFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        getListData();      //加载数据
        getBannerData();    //加载轮播广告数据

        ScrollView view = (ScrollView) inflater.inflate(R.layout.fragment_vr, null);
        bannerView = (BannerView) view.findViewById(R.id.banner_view);

        layout_1 = (LinearLayout) view.findViewById(R.id.layout_1);
        loadStateView = (LoadStateView) view.findViewById(R.id.loadStateView);
        loadStateView.setReLoadListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListData(); //重新加载
            }
        });

        bt_Game = (LinearLayout) view.findViewById(R.id.vr_game);
        bt_Video = (LinearLayout) view.findViewById(R.id.vr_video);
        bt_Qj = (LinearLayout) view.findViewById(R.id.vr_qj);

        game_container = (LinearLayout) view.findViewById(R.id.game_container);
        video_container = (LinearLayout) view.findViewById(R.id.video_container);
        qj_container = (LinearLayout) view.findViewById(R.id.qj_container);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bt_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vrFragment.getActivity(), VRGameActivity.class);
                vrFragment.getActivity().startActivity(intent);
            }
        });

        bt_Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vrFragment.getActivity(), VRVideoActivity.class);
                intent.putExtra("index",1);
                vrFragment.getActivity().startActivity(intent);
            }
        });

        bt_Qj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vrFragment.getActivity(), VRVideoActivity.class);
                intent.putExtra("index",0);
                vrFragment.getActivity().startActivity(intent);
            }
        });

    }


    @Override
    public void onStop() {
        super.onStop();
        bannerView.stopScroll();
    }

    /**
     * 获取列表数据
     */
    private void getListData() {

        String url = Constant.WEB_SITE + Constant.URL_VR_LIST;
        Response.Listener<JsonResult<VRHotInfo>> successListener = new Response.Listener<JsonResult<VRHotInfo>>() {
            @Override
            public void onResponse(JsonResult<VRHotInfo> result) {

                if (result == null) {
                    loadStateView.setVisibility(View.VISIBLE);
                    loadStateView.setState(LoadStateView.STATE_END);
                    return;
                }

                if (result.code == 0) {
                    vrHotInfo = result.data;
                    if(vrHotInfo != null){
                        createView();   //创建视图
                        layout_1.setVisibility(View.VISIBLE);
                        loadStateView.setVisibility(View.GONE);
                    }else {
                        loadStateView.isShowLoadBut(false);
                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_END,"没有数据");
                    }

                } else {
                    Log.d(TAG,"HTTP请求成功：服务端返回错误！");
                    loadStateView.isShowLoadBut(true);
                    loadStateView.setState(LoadStateView.STATE_END);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Toast.makeText(context, "更新失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
                loadStateView.isShowLoadBut(true);
                loadStateView.setState(LoadStateView.STATE_END);
            }
        };

        Request<JsonResult<VRHotInfo>> versionRequest = new GsonRequest<>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<VRHotInfo>>() {
        }.getType());
        StoreApplication.requestQueue.add(versionRequest);
    }

    private void createView() {

        if (vrHotInfo != null) {
            List<HotInfo> videoHotInfoList = vrHotInfo.videoHotInfo;
            List<HotInfo> gameHotInfoList = vrHotInfo.gameHotInfo;
            List<HotInfo> QJ360HotInfoList = vrHotInfo.QJ360HotInfo;

            game_container.removeAllViews();
            for (int i = 0; i < gameHotInfoList.size(); i++) {

                final HotInfo info = gameHotInfoList.get(i);
                View view = LayoutInflater.from(context).inflate(R.layout.item_gv_video, null);
                if (i > 0) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(CommonUtil.dip2px(context, 5), 0, 0, 0);
                    view.setLayoutParams(params);
                }

                PicassoImageView img = (PicassoImageView) view.findViewById(R.id.img_1);
                img.setImageUrl(info.advImageLink,CommonUtil.dip2px(context,115),CommonUtil.dip2px(context,160),R.drawable.ic_def_logo_412_200);

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, GameDetailActivity.class);
                        if(info.gameId <= 0){
                            return;
                        }
                        intent.putExtra("id", info.gameId);
                        startActivity(intent);
                    }
                });

                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                String name = info.advName;
                /*if(name != null){
                    name = name.length() > 10 ? name.substring(0,10) : name;
                }*/
                tv_title.setText(name);

                game_container.addView(view);
            }

            video_container.removeAllViews();
            for (int i = 0; i < videoHotInfoList.size(); i++) {

                final HotInfo info = videoHotInfoList.get(i);
                View view = LayoutInflater.from(context).inflate(R.layout.item_gv_video, null);
                if (i > 0) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(CommonUtil.dip2px(context, 5), 0, 0, 0);
                    view.setLayoutParams(params);
                }

                PicassoImageView img = (PicassoImageView) view.findViewById(R.id.img_1);
                img.setImageUrl(info.advImageLink,CommonUtil.dip2px(context,115),CommonUtil.dip2px(context,160),R.drawable.ic_def_logo_412_200);

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VideoDetailActivity.class);
                        if(info.videoId <= 0){
                            return;
                        }
                        intent.putExtra("id", info.videoId);
                        startActivity(intent);
                    }
                });

                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                String name = info.advName;
                if(name != null){
                    name = name.length() > 10 ? name.substring(0,10) : name;
                }
                tv_title.setText(name);
                //TextView tv_summary = (TextView) view.findViewById(R.id.tv_summary);
                video_container.addView(view);
            }

            qj_container.removeAllViews();
            for (int i = 0; i < QJ360HotInfoList.size(); i++) {

                final HotInfo info = QJ360HotInfoList.get(i);
                View view = LayoutInflater.from(context).inflate(R.layout.item_gv_video, null);
                if (i > 0) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(CommonUtil.dip2px(context, 5), 0, 0, 0);
                    view.setLayoutParams(params);
                }

                PicassoImageView img = (PicassoImageView) view.findViewById(R.id.img_1);
                img.setImageUrl(info.advImageLink,CommonUtil.dip2px(context,115),CommonUtil.dip2px(context,160),R.drawable.ic_def_logo_412_200);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VideoDetailActivity.class);
                        if(info.videoId <= 0){
                            return;
                        }
                        intent.putExtra("id", info.videoId);
                        startActivity(intent);
                    }
                });


                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                String name = info.advName;
                if(name != null){
                    name = name.length() > 10 ? name.substring(0,10) : name;
                }
                tv_title.setText(name);
                qj_container.addView(view);
            }
        }
    }

    /**
     * 获取轮播图片数据
     */
    private void getBannerData() {

        String url = Constant.WEB_SITE + Constant.URL_BANNER;
        Response.Listener<JsonResult<List<HotInfo>>> successListener = new Response.Listener<JsonResult<List<HotInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<HotInfo>> result) {

                if (result == null) {
                    Toast.makeText(context, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.code == 0) {

                    List<ImageView> list = createBannerView(result.data);
                    bannerView.setData(list);

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<List<HotInfo>>> request = new GsonRequest<JsonResult<List<HotInfo>>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<List<HotInfo>>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("type", String.valueOf(2));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    /**
     * 创建轮播视图
     */
    private List<ImageView> createBannerView(List<HotInfo> hotInfoList) {

        if (hotInfoList == null || hotInfoList.size() <= 0) {
            return null;
        }

        ArrayList<ImageView> list = new ArrayList<>();
        for (int i = 0; i < hotInfoList.size(); i++) {

            final HotInfo info = hotInfoList.get(i);
            ImageView img = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            img.setLayoutParams(params);
            img.setId((int) info.id);
            img.setTag(info.advImageLink);
            img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.type == 1) {
                        Intent intent = new Intent(context, GameDetailActivity.class);
                        intent.putExtra("id", info.gameId);
                        startActivity(intent);
                    } else if (info.type == 2) {
                        Intent intent = new Intent(context, VideoDetailActivity.class);
                        intent.putExtra("id", info.videoId);
                        startActivity(intent);
                    }
                }
            });
            list.add(img);
        }
        return list;
    }
}
