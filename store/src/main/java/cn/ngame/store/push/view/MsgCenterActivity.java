package cn.ngame.store.push.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.VideoLabel;
import cn.ngame.store.push.model.PushMessage;
import cn.ngame.store.view.VideoTabView;


/**
 * 显示消息列表
 * Created by zeng on 2016/11/23.
 */
public class MsgCenterActivity extends BaseFgActivity {

    private VideoTabView videoTabView;       //分类标签控件
    private ViewPager viewPager;
    private PushMsgFgAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        this.setContentView(R.layout.activity_push_message);

        Button leftBt = (Button) findViewById(R.id.left_bt);
        leftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.center_tv)).setText("消息");
        adapter = new PushMsgFgAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        videoTabView = (VideoTabView) findViewById(R.id.auto_tab);
        videoTabView.setViewPager(viewPager);
        createFragmentData();

    }

    private void createFragmentData() {

        ArrayList<VideoLabel> labels = new ArrayList<>();
        VideoLabel labelYG = new VideoLabel(PushMessage.MSG_TYPE_TZ, "通知");
        VideoLabel labelHD = new VideoLabel(PushMessage.MSG_TYPE_HD, "活动");
        labels.add(labelYG);
        labels.add(labelHD);
        videoTabView.setVideoLabels(labels);
        videoTabView.setCurrentTab(0);

        ArrayList<Fragment> fragments = new ArrayList<>();

        //通知
        MessageListFragment msgYGFragment = new MessageListFragment();
        Bundle bundleYG = new Bundle();
        bundleYG.putLong("labelId", PushMessage.MSG_TYPE_TZ);
        msgYGFragment.setArguments(bundleYG);
        //活动
        MessageListFragment msgHDFragment = new MessageListFragment();
        Bundle bundleHD = new Bundle();
        bundleHD.putLong("labelId", PushMessage.MSG_TYPE_HD);
        msgHDFragment.setArguments(bundleHD);

        fragments.add(msgYGFragment);
        fragments.add(msgHDFragment);

        adapter.setDate(fragments);
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0);

    }
}
