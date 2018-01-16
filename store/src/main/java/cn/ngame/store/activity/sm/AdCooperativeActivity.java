package cn.ngame.store.activity.sm;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;

/**
 * App设置页面
 * Created by zeng on 2016/12/7.
 */
public class AdCooperativeActivity extends BaseFgActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ad_cooperative);

        findViewById(R.id.left_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView centerTv = (TextView) findViewById(R.id.center_tv);
        centerTv.setText("广告与合作");
    }
}
