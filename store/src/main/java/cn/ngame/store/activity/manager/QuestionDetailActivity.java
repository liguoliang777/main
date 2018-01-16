package cn.ngame.store.activity.manager;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.db.DatabaseManager;
import cn.ngame.store.view.ScrollInterface;
import cn.ngame.store.widget.webview.UIWebView;

/**
 * 问题详情-h5
 * Created by gp on 2017/4/14 0014.
 */

public class QuestionDetailActivity extends BaseFgActivity {

    private LinearLayout ll_back;
    private TextView tv_title;
    ProgressBar progressBar;
    private UIWebView uiWebView;
    private int id;
    private String url, type;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_detail_activity);
        init();
    }

    private void init() {
        dbManager = DatabaseManager.getInstance(this);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        uiWebView = (UIWebView) findViewById(R.id.uiWebView);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionDetailActivity.this.finish();
            }
        });
        uiWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (url.contains("goLeftBack")) { //左上角返回
//                    onBackPressed();
//                    return true;
//                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        uiWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }

            }
        });
        uiWebView.setOnCustomScroolChangeListener(new ScrollInterface() {
            @Override
            public void onSChanged(int l, int t, int oldl, int oldt) {
                /*float webcontent = uiWebView.getContentHeight() * uiWebView.getScale();//webview的高度
                float webnow = uiWebView.getHeight() + uiWebView.getScrollY();//当前webview的高度
                if ((int) (uiWebView.getContentHeight() * uiWebView.getScale() - (uiWebView.getHeight() + uiWebView.getScrollY())) <= 1) {
                    //已经处于底端
                    uiWebView.loadUrl("javascript:comment()");
//                    ToastUtil.show(CommonH5Activity.this, "底部");
                } else {
//
                }
                //已经处于顶端
                if (uiWebView.getScaleY() == 0) {

                }*/
            }
        });
        id = getIntent().getIntExtra("id", 0);
        url = getIntent().getStringExtra("url");
        type = getIntent().getStringExtra("type");
        if (("systemMsg").equals(type)) {
            tv_title.setText("消息详情");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dbManager.markMsgReaded(id);
                }
            }).start();
        } else {
            tv_title.setText("");
        }
        uiWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        uiWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        uiWebView.loadUrl(url + "?id=" + id);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right); //搜索页面退出动画
    }

    @Override
    public void onBackPressed() {
        if (uiWebView.canGoBack()) {
            if (uiWebView.getUrl().equals(url + "?id=" + id)) {
                super.onBackPressed();
            } else {
                uiWebView.goBack();
            }
        } else {
            super.onBackPressed();
        }
    }
}
