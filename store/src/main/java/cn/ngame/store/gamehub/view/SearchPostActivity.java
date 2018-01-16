package cn.ngame.store.gamehub.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jzt.hol.android.jkda.sdk.bean.gamehub.PostSearchListBean;
import com.jzt.hol.android.jkda.sdk.bean.search.SearchBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.gamehub.SearchPostClient;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.SearchPostAdapter;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.util.StringUtil;
import cn.ngame.store.view.LoadStateView;

/**
 * 搜索帖子
 * Created by gp on 2017/3/10 0010.
 */

public class SearchPostActivity extends BaseFgActivity implements View.OnClickListener {

    private LoadStateView loadStateView;
    private TextView tv_search;
    private EditText et_search;
    private String searchName;
    private ImageView bt_fork;
    ListView listView;
    List<PostSearchListBean.DataBean> list = new ArrayList<>();
    private SearchPostAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_post_activity);
        init();
    }

    private void init() {
        loadStateView = (LoadStateView) findViewById(R.id.loadStateView);
        tv_search = (TextView) findViewById(R.id.search_bt);
        et_search = (EditText) findViewById(R.id.et_search);
        bt_fork = (ImageView) findViewById(R.id.but_fork);
        listView = (ListView) findViewById(R.id.listView);
        loadStateView.setReLoadListener(this);
        tv_search.setOnClickListener(this);
        bt_fork.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(SearchPostActivity.this, MsgDetailActivity.class);
                i.putExtra("msgId", list.get(position).getId());
                startActivity(i);
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    searchName = ConvUtil.NS(s);
                    tv_search.setText("搜索");
                    runService();
                } else {
                    tv_search.setText("取消");
                    loadStateView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = et_search.getText().toString();
                if (!TextUtil.isEmpty(searchText)) {
                    bt_fork.setVisibility(View.VISIBLE);
                } else {
                    bt_fork.setVisibility(View.GONE);
                }
            }
        });
        //编辑完之后点击软键盘上的回车键才会触发
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    searchName = et_search.getText().toString();
                    if (searchName != null && searchName.length() > 0) {

                        // 先隐藏键盘
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(SearchPostActivity.this.getCurrentFocus()
                                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_ING);
                        runService();
                    } else {
                        Toast.makeText(SearchPostActivity.this, "请输入您的心愿，亲！", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }

    public void runService() {
        SearchBodyBean bean = new SearchBodyBean();
        bean.setKeywords(et_search.getText().toString().trim());
        bean.setAppTypeId(0);
        new SearchPostClient(SearchPostActivity.this, bean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<PostSearchListBean>() {
                    @Override
                    public void onError(Throwable e) {
                        e.toString();
                        listView.setVisibility(View.GONE);
                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_END);
                    }

                    @Override
                    public void onNext(PostSearchListBean result) {
                        if (result != null && result.getCode() == 0) {
                            listView.setVisibility(View.VISIBLE);
                            list.clear();
                            list.addAll(result.getData());
                            if (adapter == null) {
                                adapter = new SearchPostAdapter(SearchPostActivity.this, list);
                                listView.setAdapter(adapter);
                            } else {
                                adapter.setSearchResultList(list);
                            }
                            if (result.getData().size() == 0) {
                                listView.setVisibility(View.GONE);
                                loadStateView.setVisibility(View.VISIBLE);
                                loadStateView.setState(LoadStateView.STATE_END);
                            }
                        } else {
                            listView.setVisibility(View.GONE);
                            loadStateView.setVisibility(View.VISIBLE);
                            loadStateView.setState(LoadStateView.STATE_END);
                        }
                    }
                });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right); //搜索页面退出动画
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_bt:
                if (StringUtil.isEmpty(et_search.getText().toString().trim())) {
                    this.finish();
                } else {
//                    et_search.setText("");
//                    loadStateView.setVisibility(View.GONE);
                }
                break;
            case R.id.but_fork:
                et_search.setText("");
                loadStateView.setVisibility(View.GONE);
                break;
        }
    }
}
