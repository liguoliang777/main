package cn.ngame.store.activity.sm;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.activity.manager.NecessaryFragment;

/**
 * 显示 关于信息的 界面
 * Created by zeng on 2016/5/23.
 */
public class NecessaryActivity extends BaseFgActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_necessary);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        NecessaryFragment fragment = new NecessaryFragment(this);
        transaction.replace(R.id.necessary_frameLayout, fragment);
        transaction.commit();

        findViewById(R.id.left_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView centerTv = findViewById(R.id.center_tv);
        centerTv.setText("必备工具");

    }
}
