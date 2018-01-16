package cn.ngame.store.activity.manager;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;

/**
 * 意见反馈成功
 * Created by gp on 2017/4/12 0012.
 */

public class FeedbackSucceedActivity extends BaseFgActivity{

    private RelativeLayout rl_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_succeed);
        init();
    }

    private void init() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackSucceedActivity.this.finish();
            }
        });
    }
}
