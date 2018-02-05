package cn.ngame.store.activity.sm;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.activity.manager.LikeFragment;
import cn.ngame.store.activity.manager.NecessaryFragment;
import cn.ngame.store.core.utils.KeyConstant;

/**
 * 显示 关于信息的 界面
 * Created by zeng on 2016/5/23.
 */
public class NecessaryOrLikeActivity extends BaseFgActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_necessary);

        findViewById(R.id.left_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView centerTv =(TextView) findViewById(R.id.center_tv);


        int EXTRA = getIntent().getIntExtra(KeyConstant.likeOrNecessaryExtraKey, KeyConstant
                .EXTRA_LIKE);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();



        if (EXTRA == KeyConstant.EXTRA_LIKE) {
            transaction.replace(R.id.necessary_frameLayout, new LikeFragment(this));
            centerTv.setText("收藏");
        } else {
            transaction.replace(R.id.necessary_frameLayout, new NecessaryFragment(this));
            centerTv.setText("必备工具");
        }

        transaction.commit();
    }
}
