package cn.ngame.store.push.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.push.model.MessageDetail;
import cn.ngame.store.push.model.PushMessage;
import cn.ngame.store.push.presenter.PushMsgDetailPresenter;

/**
 * 显示通知详情的页面
 * Created by zeng on 2016/12/3.
 */
public class NotifyMsgDetailActivity extends BaseFgActivity implements IPushMsgDetailView{

    private TextView tv_title,tv_time,tv_summary;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private PushMsgDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        this.setContentView(R.layout.activity_msg_detail1);

        Button viewById =(Button) findViewById(R.id.left_bt);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewById.setText("通知");
        ((TextView) findViewById(R.id.center_tv)).setText("");
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_summary = (TextView) findViewById(R.id.tv_summary);

        final PushMessage msg = (PushMessage) getIntent().getSerializableExtra("msg");
        if(msg != null){
            tv_title.setText(msg.getTitle());
            tv_time.setText(df.format(new Date(msg.getReceiveDate())));
            tv_summary.setText(msg.getDescription());
        }

        presenter = new PushMsgDetailPresenter(this,this);

        //将消息标记为已读
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.markHasRead(msg.getType(),msg.getMsgId());
            }
        }).start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        final PushMessage msg = (PushMessage) intent.getSerializableExtra("msg");
        if(msg != null){
            tv_title.setText(msg.getTitle());
            tv_time.setText(df.format(new Date(msg.getReceiveDate())));
            tv_summary.setText(msg.getDescription());
        }

        //将消息标记为已读
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.markHasRead(msg.getType(),msg.getMsgId());
            }
        }).start();
    }

    @Override
    public void showMsgDetail(MessageDetail detailMsg) {}
}
