package cn.ngame.store.activity.sm;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;

/**
 * 显示 关于信息的 界面
 * Created by zeng on 2016/5/23.
 */
public class NecessaryActivity extends BaseFgActivity {
    private TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        this.setContentView(R.layout.activity_necessary);

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
