package cn.ngame.store.game.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.adapter.GameKeyListViewAdapter;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.GameKey;
import cn.ngame.store.bean.GameKeyDesc;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.view.GameKeyListView;
import cn.ngame.store.view.PicassoImageView;

/**
 * 显示游戏键位的Fragment
 * @author zeng
 * @since 2016/5/17
 */
public class GameKeyFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = GameKeyFragment.class.getSimpleName();
    private static GameKeyFragment gameKeyFragment = null;

    public static GameKeyFragment newInstance(GameInfo gameInfo) {
        GameKeyFragment fragment = new GameKeyFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GameInfo.TAG, gameInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Activity context;
    private GameInfo gameInfo;
    private GameKeyListView listView;
    private GameKeyListViewAdapter adapter;
    private PicassoImageView imageView;

    private Button bt_classic,bt_slim;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取初始参数
        gameInfo = (GameInfo) getArguments().getSerializable(GameInfo.TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();

        ScrollView rootLayout = (ScrollView) inflater.inflate(R.layout.fragment_game_key,container,false);

        bt_classic = (Button) rootLayout.findViewById(R.id.bt_classic);
        bt_slim = (Button) rootLayout.findViewById(R.id.bt_slim);

        imageView = (PicassoImageView) rootLayout.findViewById(R.id.img_1);
        listView = (GameKeyListView) rootLayout.findViewById(R.id.listView);
        adapter = new GameKeyListViewAdapter(context);
        listView.setAdapter(adapter);

        if(gameInfo != null){
            //List<GameImage> imageList = gameInfo.gameKeyMapsImages;
            List<GameKey> keyList = gameInfo.gameKeyDescList;

            /*if(imageList != null && imageList.size() > 0){
                imageView.setImageUrl(gameInfo.gameKeyMapsImages.get(0).imageLink);
            }*/

            if(keyList != null ){
                adapter.setDate(keyList);
                listView.notifyDataSetChanged();
            }
        }
        return rootLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bt_classic.setOnClickListener(this);
        bt_slim.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_classic:

                bt_classic.setBackgroundResource(R.drawable.shape_yj_right_blue_rectangle);
                bt_classic.setTextColor(getResources().getColor(R.color.white));
                bt_slim.setBackgroundResource(R.drawable.shape_yj_left_white_rectangle);
                bt_slim.setTextColor(getResources().getColor(R.color.mainColor));
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_def_logo_188_188));
                getKeyDescList(1);
                break;
            case R.id.bt_slim:

                bt_classic.setBackgroundResource(R.drawable.shape_yj_right_white_rectangle);
                bt_classic.setTextColor(getResources().getColor(R.color.mainColor));
                bt_slim.setBackgroundResource(R.drawable.shape_yj_left_blue_rectangle);
                bt_slim.setTextColor(getResources().getColor(R.color.white));
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_def_logo_188_188));
                getKeyDescList(2);
                break;
        }
    }

    /**
     * 获取手柄键位信息
     * @param handleTypeId  手柄类型
     */
    private void getKeyDescList(final int handleTypeId){

        String url = Constant.WEB_SITE + Constant.URL_GAME_KEY;
        Response.Listener<JsonResult<GameKeyDesc>> successListener = new Response.Listener<JsonResult<GameKeyDesc>>() {
            @Override
            public void onResponse(JsonResult<GameKeyDesc> result) {

                if(result == null || result.code != 0){
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                }
                GameKeyDesc gameKeyDesc = result.data;
                if(gameKeyDesc != null){

                    adapter.setDate(gameKeyDesc.gameKeyDescList);
                    listView.notifyDataSetChanged();

                }else {
                    Log.d(TAG, "没有键位图描述");
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

        Request<JsonResult<GameKeyDesc>> request = new GsonRequest<JsonResult<GameKeyDesc>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<GameKeyDesc>>() {}.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("gameId", String.valueOf(gameInfo.id));
                params.put("handleType", String.valueOf(handleTypeId));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);

    }


}
