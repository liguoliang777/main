package cn.ngame.store.game.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.adapter.GameListAdapter;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.view.LoadStateView;

/**
 * 用于显示游戏排行榜的页面
 *
 * @author flan
 * @since 2016/5/9
 */
public class GameRankFragment extends Fragment {

    public static final String TAG = GameRankFragment.class.getSimpleName();
    private static GameRankFragment homeFragment = null;

    private static FragmentManager fragmentManager;

    public static GameRankFragment getInstance(FragmentManager fm) {
        fragmentManager = fm;
        if (homeFragment == null) {
            homeFragment = new GameRankFragment();
        }
        return homeFragment;
    }

    private Context context;

    private ListView listView;
    private GameListAdapter adapter;
    private LoadStateView loadStateView;       //加载等待控件

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        getGameList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_game_rank, null);
        listView = (ListView) view.findViewById(R.id.listView);

        loadStateView = (LoadStateView) view.findViewById(R.id.loadStateView);
        loadStateView.setReLoadListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStateView.setState(LoadStateView.STATE_ING);
                getGameList();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new GameListAdapter(context,fragmentManager);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(context, GameDetailActivity.class);
                intent.putExtra("id", ((GameInfo)adapter.getItem(position)).id);
                context.startActivity(intent);
            }
        });

    }

    private void getGameList() {
        String url = Constant.WEB_SITE + Constant.URL_GAME_RANK;
        Response.Listener<JsonResult<List<GameInfo>>> successListener = new Response.Listener<JsonResult<List<GameInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<GameInfo>> result) {

                if(result == null){
                    loadStateView.setVisibility(View.VISIBLE);
                    loadStateView.setState(LoadStateView.STATE_END);
                    return;
                }

                if(result.code == 0){

                    List<GameInfo> gameInfoList = result.data;

                    if(gameInfoList != null && gameInfoList.size() > 0){

                        loadStateView.setVisibility(View.GONE);

                        adapter.setDate(gameInfoList);
                        adapter.notifyDataSetChanged();

                    }else {
                        loadStateView.isShowLoadBut(false);
                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_END,"没有数据");
                    }

                }else {
                    Log.d(TAG,"HTTP请求成功：服务端返回错误！");
                    loadStateView.setState(LoadStateView.STATE_END);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
                loadStateView.setState(LoadStateView.STATE_END);
            }
        };

        Request<JsonResult<List<GameInfo>>> request = new GsonRequest<>(Request.Method.POST,url, successListener,
                errorListener, new TypeToken<JsonResult<List<GameInfo>>>() {}.getType());
        StoreApplication.requestQueue.add(request);
    }
}
