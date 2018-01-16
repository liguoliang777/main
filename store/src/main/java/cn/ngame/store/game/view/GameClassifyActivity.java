package cn.ngame.store.game.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.GameListAdapter;
import cn.ngame.store.bean.Category;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.search.view.SearchActivity;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.BaseTitleBar;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.view.ScrollTabView;

/**
 * 显示手柄分类详情
 * Created by zeng on 2016/8/24.
 */
public class GameClassifyActivity extends BaseFgActivity {

    public static final String TAG = GameClassifyActivity.class.getSimpleName();

    private ListView listView;
    private GameListAdapter adapter;

    private ScrollTabView scrollTabView;       //分类标签控件
    private List<Category> categories = null;  //游戏分类标签
    private TextView tv_labName;
    private TextView tv_all_class;

    private LoadStateView loadStateView;       //加载等待控件

    private int categoryId;                   //当前类別ID
    private int pageIndex = 1;                 //默认起始页
    private int pageSize = 10;                 //默认没用加载条数
    private long totals = 0;                   //总记录数
    private boolean isLoading = false;         //是否正在上滑加载数据

    private int lastItem;
    private int markId = 0;
    private String title;
    private String labName;
    private int isVr = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.fragment_sb_game);
        Intent intent = getIntent();
        if (intent != null) {
            markId = intent.getIntExtra("markId", 0);
            categoryId = intent.getIntExtra("tagPosition", 0);
            title = intent.getStringExtra("title");
            labName = intent.getStringExtra("labName");
            isVr = intent.getIntExtra("isVr", 0);
        }
        BaseTitleBar titleBar = (BaseTitleBar) findViewById(R.id.title_bar);
        titleBar.setTitleText(title);
        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameClassifyActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        tv_labName = (TextView) findViewById(R.id.tv_labName);
        tv_all_class = (TextView) findViewById(R.id.tv_all_class);
        View ft = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = CommonUtil.dip2px(this, 4);
        ft.setBackgroundColor(getResources().getColor(R.color.f5f5f5));
        listView.addFooterView(ft);

        scrollTabView = (ScrollTabView) findViewById(R.id.auto_tab);
        loadStateView = (LoadStateView) findViewById(R.id.loadStateView);
        loadStateView.setReLoadListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStateView.setState(LoadStateView.STATE_ING);
                //重新加载
                if (markId == 0) {
                    getGameList();
                } else {
                    getCategory();
                }
            }
        });
        scrollTabView.setOnTabViewClickListener(new ScrollTabView.OnTabViewClickListener() {
            @Override
            public void onClick(int typeId, String typeName) {
                // 执行TAG切换
                if (categoryId != typeId) {
                    categoryId = typeId;
                    pageIndex = 1;
                    totals = 0;
                    adapter.clean();

                    loadStateView.setVisibility(View.VISIBLE);
                    getGameList();
                }
            }
        });
        adapter = new GameListAdapter(this, getSupportFragmentManager());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GameClassifyActivity.this, GameDetailActivity.class);
                intent.putExtra("id", ((GameInfo) adapter.getItem(position)).id);
                startActivity(intent);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                long count = adapter.getCount();

                if (lastItem >= (count - 1) && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //TODO 执行上滑分页加载
                    if (count < totals && !isLoading) {
                        isLoading = true;
                        pageIndex += 1;
                        getGameList();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount;
                //Log.d(TAG,"-------------------------lastItem: "+lastItem);
            }
        });

        init();//第一次加载
    }

    private void init() {
        pageIndex = 1;                 //默认起始页
        pageSize = 10;                 //默认没用加载条数
        totals = 0;                   //总记录数
        isLoading = false;         //是否正在上滑加载数据
        if (isVr == 1) {
            tv_all_class.setVisibility(View.GONE);
        }
        if (markId == 0) {
            tv_labName.setVisibility(View.VISIBLE);
            tv_labName.setText(labName);
            getGameList();
        } else {
            getCategory();
        }
    }

    /**
     * 获取游戏分类数据
     */
    private void getCategory() {

        String url = Constant.WEB_SITE + Constant.URL_GAME_LAB;
        Response.Listener<JsonResult<List<Category>>> successListener = new Response.Listener<JsonResult<List<Category>>>() {
            @Override
            public void onResponse(JsonResult<List<Category>> result) {

                if (result == null) {
                    loadStateView.setVisibility(View.VISIBLE);
                    loadStateView.setState(LoadStateView.STATE_END);
                    return;
                }

                if (result.code == 0) {
                    categories = result.data;
                    if (isVr == 1) {
                        Category cg = new Category(0, "全部");
                        categories.add(0, cg);
                    }
                    if (categories != null && categories.size() > 0) {
                        //给分类控件传值
                        scrollTabView.setTextList(categories);
                        //默认加载第一个分类
                        categoryId = ConvUtil.NI(categories.get(0).id);
                        getGameList();
                    } else {
                        loadStateView.isShowLoadBut(false);
                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_END, "没有数据");
                    }
                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                    loadStateView.isShowLoadBut(true);
                    loadStateView.setVisibility(View.VISIBLE);
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
                loadStateView.setVisibility(View.VISIBLE);
                loadStateView.setState(LoadStateView.STATE_END);
            }
        };

//        Request<JsonResult<List<Category>>> request = new GsonRequest<>(Request.Method.POST, url,
//                successListener,errorListener,new TypeToken<JsonResult<List<Category>>>(){}.getType());

        Request<JsonResult<List<Category>>> request = new GsonRequest<JsonResult<List<Category>>>(Request.Method.POST, url, successListener,
                errorListener, new TypeToken<JsonResult<List<Category>>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("markId", String.valueOf(markId));
                params.put("appTypeId", String.valueOf(0));

                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    /**
     * 获取制定分类下的游戏列表
     */
    private void getGameList() {

        String url = Constant.WEB_SITE + Constant.URL_GAME_LIST;
        Response.Listener<JsonResult<List<GameInfo>>> successListener = new Response.Listener<JsonResult<List<GameInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<GameInfo>> result) {

                if (result == null) {
                    loadStateView.setVisibility(View.VISIBLE);
                    loadStateView.setState(LoadStateView.STATE_END);
                    isLoading = false;
                    return;
                }

                if (result.code == 0) {

                    List<GameInfo> gameInfoList = result.data;
                    totals = result.totals;
                    //Log.e(TAG,"---------------------->>>>> TOTAL "+totals +"count "+adapter.getCount());

                    if (gameInfoList != null && gameInfoList.size() > 0) {

                        loadStateView.setVisibility(View.GONE);
                        /*for(GameInfo i : gameInfoList){
                            Log.e(TAG,"---------------->>> "+i.gameName + " des "+i.gameDesc);
                        }*/
                        adapter.setDate(gameInfoList);
                        adapter.notifyDataSetChanged();

                    } else {
                        loadStateView.isShowLoadBut(false);
                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_END, "没有数据");
                    }

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                    loadStateView.setState(LoadStateView.STATE_END);
                }
                isLoading = false;
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
                loadStateView.setState(LoadStateView.STATE_END);
                isLoading = false;
            }
        };

        Request<JsonResult<List<GameInfo>>> request = new GsonRequest<JsonResult<List<GameInfo>>>(Request.Method.POST, url, successListener,
                errorListener, new TypeToken<JsonResult<List<GameInfo>>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("categoryId", String.valueOf(categoryId));
                params.put("gameLabelId", String.valueOf(isVr));
                params.put("pageSize", String.valueOf(pageSize));
                params.put("pageIndex", String.valueOf(pageIndex));

                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }
}
