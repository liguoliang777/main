package cn.ngame.store.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.main.YunduanClient;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.TopicsListAdapter;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.view.LoadStateView;


/**
 * 全部专题
 * Created by gp on 2017/4/13 0013.
 */

public class TopicsListActivity extends BaseFgActivity {

    private Button tv_title;
    List<YunduanBean.DataBean> list = new ArrayList<>();
    TopicsListAdapter adapter;
    private TopicsListActivity content;
    private GridView gview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.selected_topics_activity);
        content = TopicsListActivity.this;
        init();
    }

    private LoadStateView loadStateView;

    private void init() {
        loadStateView = (LoadStateView) findViewById(R.id.load_state_view2);
        loadStateView.isShowLoadBut(false);
        tv_title = (Button) findViewById(R.id.left_bt);
        tv_title.setText("全部专题");
        gview = (GridView) findViewById(R.id.gview);
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.finish();
            }
        });
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(content, TopicsDetailActivity.class);
                YunduanBean.DataBean dataBean = list.get(position);
                i.putExtra(KeyConstant.category_Id, dataBean.getId());
                i.putExtra(KeyConstant.TITLE, dataBean.getTypeName());
                i.putExtra(KeyConstant.DESC, dataBean.getTypeDesc());
                i.putExtra(KeyConstant.URL, dataBean.getLogoUrl());
                startActivity(i);
            }
        });
        runService();
    }

    public void runService() {
        loadStateView.setVisibility(View.VISIBLE);
        loadStateView.setState(LoadStateView.STATE_ING);
        new YunduanClient(this, new YunduanBodyBean()).observable()
                .subscribe(new ObserverWrapper<YunduanBean>() {
                    @Override
                    public void onError(Throwable e) {
                        loadStateView.setState(LoadStateView.STATE_END);
                    }

                    @Override
                    public void onNext(YunduanBean result) {
                        if (result != null && result.getCode() == 0) {
                            loadStateView.setVisibility(View.GONE);
                            setData(result);
                        } else {
                            loadStateView.setState(LoadStateView.STATE_END);
                        }
                    }
                });
    }

    private void setData(YunduanBean result) {
        if (result.getData() == null) {
            return;
        }
        if (result.getData().size() <= 0) {
            return;
        }
        list.addAll(result.getData());
        if (adapter == null) {
            adapter = new TopicsListAdapter(this, list);
            gview.setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
        list = null;
    }
}
