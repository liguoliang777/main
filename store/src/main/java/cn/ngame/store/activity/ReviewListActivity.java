package cn.ngame.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
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
import cn.ngame.store.adapter.LvCommentAdapter;
import cn.ngame.store.bean.Comment;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.User;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.user.view.LoginActivity;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;

/**
 * 显示评论列表
 * Created by zeng on 2016/7/27.
 */
public class ReviewListActivity extends BaseFgActivity {

    public static final String TAG = ReviewListActivity.class.getSimpleName();

    private TextView tv_right;
    private Button bt_review;
    private ListView listview;
    private LvCommentAdapter adapter;

    private Button left_but;
    private TextView left_tv;

    private long targetId = 0;
    private int targetType = 1; //1.游戏，2.视频

    private int pageIndex = 1;                 //默认起始页
    private int pageSize = 10;                 //默认没用加载条数
    private long totals = 0;                   //总记录数
    private int lastItem;

    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_review_list);

        Intent intent = getIntent();
        targetId = intent.getLongExtra("Id",0);
        targetType = intent.getIntExtra("Type",1);

        left_but = (Button) findViewById(R.id.left_but);
        left_tv = (TextView) findViewById(R.id.left_tv);

        tv_right = (TextView) findViewById(R.id.right_tv);
        bt_review = (Button) findViewById(R.id.but1);
        listview = (ListView) findViewById(R.id.listView);

        adapter = new LvCommentAdapter(this,null);
        listview.setAdapter(adapter);

        getCommentData();   //加载评论数据
        setListener();
    }

    private void setListener(){

        left_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        left_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = StoreApplication.user;
                if(user == null){
                    Intent intent = new Intent(ReviewListActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(ReviewListActivity.this, ReviewActivity.class);
                    intent.putExtra("categoryId", targetType);
                    intent.putExtra("targetId", targetId);
                    startActivity(intent);
                }
            }
        });

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                long count = adapter.getCount();
                Log.d(TAG,"-------------------------count: "+count);

                if (lastItem >= (count - 1) && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE ) {
                    //TODO 执行上滑分页加载
                    if(count < totals && !isLoading){
                        isLoading = true;
                        pageIndex += 1;
                        getCommentData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount;
                Log.d(TAG,"-------------------------lastItem: "+lastItem);
            }
        });
    }

    public void onBackClick(View v){
        finish();
    }

    /**
     * 获取评论数据
     */
    private void getCommentData() {

        String url = Constant.WEB_SITE + Constant.URL_COMMENT_LIST;
        Response.Listener<JsonResult<List<Comment>>> successListener = new Response.Listener<JsonResult<List<Comment>>>() {
            @Override
            public void onResponse(JsonResult<List<Comment>> result) {

                if (result != null && result.code == 0) {

                    totals = result.totals;
                    tv_right.setText(result.totals+"条评论");
                    adapter.setData(result.data);
                    adapter.notifyDataSetInvalidated();

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                }
                isLoading = false;
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
                isLoading = false;
            }
        };

        Request<JsonResult<List<Comment>>> request = new GsonRequest<JsonResult<List<Comment>>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<List<Comment>>>() {}.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("commentType", String.valueOf(targetType));
                params.put("code", String.valueOf(targetId));
                params.put("pageIndex", String.valueOf(pageIndex));
                params.put("pageSize", String.valueOf(pageSize));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

}
