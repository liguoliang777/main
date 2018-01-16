package cn.ngame.store.gamehub.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.utils.SPUtils;

/**
 * 选择版块
 * Created by gp on 2017/3/1 0001.
 */

public class ChooseModuleActivity extends BaseFgActivity implements View.OnClickListener {

    private LinearLayout ll_back;
    private TextView tv_title;
    private LinearLayout ll_gonglue, ll_qiuzhu, ll_toupiao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_module_activity);
        init();
    }

    private void init() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("选择板块");
        ll_back.setOnClickListener(this);
        ll_gonglue = (LinearLayout) findViewById(R.id.ll_gonglue);
        ll_qiuzhu = (LinearLayout) findViewById(R.id.ll_qiuzhu);
        ll_toupiao = (LinearLayout) findViewById(R.id.ll_toupiao);
        ll_gonglue.setOnClickListener(this);
        ll_qiuzhu.setOnClickListener(this);
        ll_toupiao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:

                break;
            case R.id.ll_gonglue:
                SPUtils.put(this, "choose_module", 1);
                break;
            case R.id.ll_qiuzhu:
                SPUtils.put(this, "choose_module", 2);
                break;
            case R.id.ll_toupiao:
                SPUtils.put(this, "choose_module", 3);
                break;
        }
        this.finish();
    }
}
