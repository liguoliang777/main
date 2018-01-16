package cn.ngame.store.activity.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.admin.AdminGameUpdateBody;
import com.jzt.hol.android.jkda.sdk.bean.admin.QuestionListBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.admin.QuestionListClient;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.QuestionListAdapter;
import cn.ngame.store.base.activity.FeedbackActivity;
import cn.ngame.store.util.StringUtil;

/**
 * 帮助与反馈
 * Created by gp on 2017/4/12 0012.
 */

public class HelpAndFeedbackActivity extends BaseFgActivity implements View.OnClickListener {

    private LinearLayout ll_back;
    private TextView tv_title;
    private ListView listView;
    private LinearLayout ll_sb, ll_sheb, ll_app, ll_other;
    private Button btn_submit;
    QuestionListAdapter adapter;
    List<QuestionListBean.DataBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_and_feedback);
        init();
    }

    private void init() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("帮助与反馈");
        btn_submit = (Button) findViewById(R.id.btn_submit);
        listView = (ListView) findViewById(R.id.listView);
        ll_sb = (LinearLayout) findViewById(R.id.ll_sb);
        ll_sheb = (LinearLayout) findViewById(R.id.ll_sheb);
        ll_app = (LinearLayout) findViewById(R.id.ll_app);
        ll_other = (LinearLayout) findViewById(R.id.ll_other);

        btn_submit.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        ll_sb.setOnClickListener(this);
        ll_sheb.setOnClickListener(this);
        ll_app.setOnClickListener(this);
        ll_other.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(HelpAndFeedbackActivity.this, QuestionDetailActivity.class);
                i.putExtra("url", list.get(position).getUrl());
                i.putExtra("id", list.get(position).getId());
                startActivity(i);
            }
        });
        runService();
    }

    private void runService() {
        AdminGameUpdateBody body = new AdminGameUpdateBody();
        new QuestionListClient(this, body).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<QuestionListBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(QuestionListBean result) {
                        if (result != null && result.getCode() == 0) {
                            if (result.getData() == null) {
                                return;
                            }
                            if (result.getData().size() > 0) {
                                list.clear();
                                list.addAll(result.getData());
                                if (adapter == null) {
                                    adapter = new QuestionListAdapter(HelpAndFeedbackActivity.this, list);
                                    listView.setAdapter(adapter);
                                    StringUtil.setListViewHeightBasedOnChildren(listView, 0);
                                } else {
                                    adapter.setList(list);
                                }
                            }
                        } else {
//                            ToastUtil.show(getActivity(), result.getMsg());
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                this.finish();
                break;
            case R.id.btn_submit:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.ll_sb:
                Intent i = new Intent();
                i.setClass(this, QuestionListActivity.class);
                i.putExtra("title", "手柄介绍");
                i.putExtra("type", 2);
                startActivity(i);
                break;
            case R.id.ll_sheb:
                Intent i2 = new Intent();
                i2.setClass(this, QuestionListActivity.class);
                i2.putExtra("title", "设备连接");
                i2.putExtra("type", 1);
                startActivity(i2);
                break;
            case R.id.ll_app:
                Intent i3 = new Intent();
                i3.setClass(this, QuestionListActivity.class);
                i3.putExtra("title", "APP使用");
                i3.putExtra("type", 3);
                startActivity(i3);
                break;
            case R.id.ll_other:
                Intent i4 = new Intent();
                i4.setClass(this, QuestionListActivity.class);
                i4.putExtra("title", "其他问题");
                i4.putExtra("type", 4);
                startActivity(i4);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
        list = null;
    }
}
