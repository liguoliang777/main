package cn.ngame.store.ota.view;

import android.os.Bundle;
import android.view.View;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.view.SimpleTitleBar;

/**
 * 用于显示OTA升级帮助
 * Created by zeng on 2016/8/17.
 */
public class OtaHelpActivity extends BaseFgActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ota_help);
        SimpleTitleBar title_bar = (SimpleTitleBar) findViewById(R.id.title_bar);
        title_bar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
