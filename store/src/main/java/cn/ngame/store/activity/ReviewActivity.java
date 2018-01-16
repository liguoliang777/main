package cn.ngame.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.Token;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.fragment.SimpleDialogFragment;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.video.view.VideoDetailActivity;
import cn.ngame.store.view.SimpleTitleBar;

/**
 * 显示评论窗口的界面
 * Created by zeng on 2016/5/17.
 */
public class ReviewActivity extends BaseFgActivity {

    public static final String TAG = ReviewActivity.class.getSimpleName();

    private SimpleTitleBar titleBar;
    private RatingBar ratingBarBig;
    private EditText contextTextView;

    private int categoryId; //1.游戏，2 视频
    private long targetId;  //目标ID
    private float rating;   //评分值
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_review);

        titleBar = (SimpleTitleBar) findViewById(R.id.title_bar);
        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ratingBarBig = (RatingBar) findViewById(R.id.rating_bar);
        contextTextView = (EditText) findViewById(R.id.contextTextView);

        categoryId = getIntent().getIntExtra("categoryId", 0);
        targetId = getIntent().getLongExtra("targetId", 0);
    }

    /**
     * 处理评论提交逻辑
     *
     * @param view
     */
    public void onSendCommentClick(View view) {
        rating = ratingBarBig.getRating();
        content = contextTextView.getText().toString();

        if (null == content || content.isEmpty() || content.length() > 250 || content.length() < 6) {
            Toast.makeText(this, "请填写评论(6-250个字符)", Toast.LENGTH_SHORT).show();
            return;
        }/*else if(!TextUtil.isLegal(content.trim(),"[A-Za-z0-9\\u4e00-\\u9fa5_?，。,.!！]+")){
            Toast.makeText(this,"亲，只允许中文，英文，数字等字符哦！",Toast.LENGTH_SHORT).show();
            return;
        }*/

        if (rating <= 0) {
            Toast.makeText(this, "请点击星星打分", Toast.LENGTH_SHORT).show();
            return;
        }

        doComment();

    }

    private void doComment() {
        DialogHelper.showWaiting(getSupportFragmentManager(), "发送中...");
        String url = Constant.WEB_SITE + Constant.URL_COMMENT_ADD_COMMENT;
        Response.Listener<JsonResult<Token>> successListener = new Response.Listener<JsonResult<Token>>() {
            @Override
            public void onResponse(JsonResult<Token> result) {
                DialogHelper.hideWaiting(getSupportFragmentManager());
                if (result == null) {
                    Toast.makeText(ReviewActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.code == 0) {
//                    showDialog(true, "评论成功！");
                    if (categoryId == 1) {
                        Intent intent = new Intent(ReviewActivity.this, GameDetailActivity.class);
                        startActivity(intent);
                    } else if (categoryId == 2) {
                        Intent intent = new Intent(ReviewActivity.this, VideoDetailActivity.class);
                        intent.putExtra("id",targetId);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    Toast.makeText(ReviewActivity.this, result.msg, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "HTTP请求成功：服务端返回错误: " + result.msg);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DialogHelper.hideWaiting(getSupportFragmentManager());
                volleyError.printStackTrace();
                Toast.makeText(ReviewActivity.this, "评论失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<Token>> versionRequest = new GsonRequest<JsonResult<Token>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<Token>>() {}.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("categoryId", String.valueOf(categoryId));
                params.put("toMatterCode", String.valueOf(targetId));    //目标ID
                params.put("value", String.valueOf((int) Math.floor(rating)));           //评分值
                params.put("content", content);
                params.put("token", ((StoreApplication) getApplication()).token);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    /**
     * 显示注册结果对话框
     */
    private void showDialog(final boolean isSuccess, String msg) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        dialogFragment.setTitle("提示框");
        dialogFragment.setDialogWidth(250);

        TextView tv = new TextView(ReviewActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 20, 0, 0);
        params.gravity = Gravity.CENTER;
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(msg);
        tv.setTextColor(getResources().getColor(R.color.color000000));
        dialogFragment.setContentView(tv);

        dialogFragment.setNegativeButton(R.string.sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();

                if (isSuccess) {
                    if (categoryId == 1) {
                        Intent intent = new Intent(ReviewActivity.this, GameDetailActivity.class);
                        startActivity(intent);
                    } else if (categoryId == 2) {
                        Intent intent = new Intent(ReviewActivity.this, VideoDetailActivity.class);
                        intent.putExtra("id",targetId);
                        startActivity(intent);
                    }
                    finish();
                }
            }
        });
        dialogFragment.show(ft, "successDialog");
    }
}
