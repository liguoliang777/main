package cn.ngame.store.activity.sm;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.utils.CommonUtil;

/**
 * 显示 关于信息的 界面
 * Created by zeng on 2016/5/23.
 */
public class AboutNgameZoneActivity extends BaseFgActivity {
    private TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_about_ngame_zone);

        findViewById(R.id.left_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView centerTv = (TextView) findViewById(R.id.center_tv);
        centerTv.setText("关于NgameZone");

        tv_version = (TextView) findViewById(R.id.text1);
        tv_version.setText("版本号：" + CommonUtil.getVersionName(this));

    }
}
