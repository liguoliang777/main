package cn.ngame.store.activity.rank;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.HubAdapter;
import cn.ngame.store.bean.PostsInfo;

/**
 * 圈子
 * Created by liguoliang on 2017/11/23 0023.
 */
@SuppressLint("WrongConstant")
public class RankActivity extends BaseFgActivity {
    protected static final String TAG = RankActivity.class.getSimpleName();
    private LinearLayout ll_back;
    private RankActivity mContext;
    private TextView titleTv;
    private RecyclerView mRecyclerView;
    private HubAdapter mAdapter;
    private TextView headerLastUpdateTv;
    private List<PostsInfo.DataBean> mDatas=new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_rank);
        mContext = this;
        init();
    }

    private void init() {
        ll_back = findViewById(R.id.ll_back);
        titleTv = findViewById(R.id.tv_title);

        titleTv.setText("排行榜");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}