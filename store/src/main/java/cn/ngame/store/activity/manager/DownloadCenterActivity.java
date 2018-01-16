package cn.ngame.store.activity.manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;

/**
 * 管理-下载更新
 * Created by gp on 2017/3/23 0023.
 */

public class DownloadCenterActivity extends BaseFgActivity {

    private LinearLayout ll_back;
    private TextView tv_title;
    private DownloadCenterActivity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_download_center);
        initStatusBar();
        mContext = this;
        init();
    }

    private void init() {
        //必需继承FragmentActivity,嵌套fragment只需要这行代码
        final FragmentManager fm = getSupportFragmentManager();
        final DownloadCenterFragment fragment = new DownloadCenterFragment();
        fm.beginTransaction().replace(R.id.download_center_fl, fragment).commitAllowingStateLoss();
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("下载中心");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
