package cn.ngame.store.game.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import cn.ngame.store.activity.ReviewActivity;
import cn.ngame.store.activity.ReviewListActivity;
import cn.ngame.store.adapter.LvCommentAdapter;
import cn.ngame.store.bean.Comment;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.user.view.LoginActivity;
import cn.ngame.store.view.ReviewScoreView;

/**
 * 显示游戏评论的Fragment
 *
 * @author flan
 * @since 2016/5/17
 */
public class GameReviewFragment extends Fragment {

    public static final String TAG = GameReviewFragment.class.getSimpleName();
    private static GameReviewFragment gameReviewFragment = null;
    private Button review;
    private Context context;
    private ReviewScoreView reviewScoreView;
    private ListView listView;
    private LvCommentAdapter adapter;

    private GameInfo gameInfo;
    private List<Comment> commentList;

    private TextView listViewFooterView;

    private int pageIndex = 1;                 //默认起始页
    private int pageSize = 5;                 //默认没用加载条数
    private long totals = 0;                   //总记录数
    private int lastItem;
    private String pwd;

    public static GameReviewFragment newInstance(GameInfo gameInfo) {
        GameReviewFragment fragment = new GameReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GameInfo.TAG, gameInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        //获取初始参数
        gameInfo = (GameInfo) getArguments().getSerializable(GameInfo.TAG);
        pageIndex = 1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fragment_game_review, container, false);
        review = (Button) view.findViewById(R.id.but1);
        reviewScoreView = (ReviewScoreView) view.findViewById(R.id.reviewScoreView);
        listView = (ListView) view.findViewById(R.id.listView);

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd = StoreApplication.passWord;
                if (pwd != null && !"".endsWith(pwd) || !Constant.PHONE.equals(StoreApplication.loginType)) {
                    Intent intent = new Intent(context, ReviewActivity.class);
                    intent.putExtra("categoryId", 1);
                    intent.putExtra("targetId", gameInfo.id);
                    context.startActivity(intent);
                } else {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }

            }
        });

        adapter = new LvCommentAdapter(context, null);
        listView.setAdapter(adapter);

        //setListener();
        initListViewFooterView();

        return view;
    }

    private void initListViewFooterView() {

        listViewFooterView = new TextView(context);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        params.height = CommonUtil.dip2px(context, 35);
        listViewFooterView.setLayoutParams(params);
        listViewFooterView.setGravity(Gravity.CENTER);
        listViewFooterView.setText("全部评论");
        listViewFooterView.setTextSize(16);
        listViewFooterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReviewListActivity.class);
                intent.putExtra("Id", gameInfo.id);
                intent.putExtra("Type", 1);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.clean();
        }
        getCommentData();
        //reviewScoreView.setData(gameInfo.questionResults);
        reviewScoreView.setData(gameInfo);
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

                    commentList = result.data;

                    if (result.totals > pageSize) {
                        listViewFooterView.setText("全部评论(" + result.totals + "条)");
                        listView.removeFooterView(listViewFooterView);
                        listView.addFooterView(listViewFooterView);
                    }

                    adapter.clean();
                    adapter.setData(commentList);
                    adapter.notifyDataSetChanged();

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

        Request<JsonResult<List<Comment>>> request = new GsonRequest<JsonResult<List<Comment>>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<List<Comment>>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("commentType", "1");
                params.put("code", String.valueOf(gameInfo.id));
                params.put("pageIndex", String.valueOf(pageIndex));
                params.put("pageSize", String.valueOf(pageSize));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);

    }

}
