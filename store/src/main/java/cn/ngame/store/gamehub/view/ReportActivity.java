package cn.ngame.store.gamehub.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.gamehub.NormalDataBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.ReportPostBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.gamehub.ReportPostClient;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.utils.APIErrorUtils;
import cn.ngame.store.util.ToastUtil;

/**
 * 举报
 * Created by gp on 2017/2/15 0015.
 */

public class ReportActivity extends BaseFgActivity implements View.OnClickListener {

    private LinearLayout ll_back;
    private TextView tv_title;
    private LinearLayout ll_eygs, ll_ggzp, ll_yhsq, ll_other;
    private ImageView iv_eygs, iv_ggzp, iv_yhsq, iv_other;
    private Button btn_submit;
    int postId; //帖子id
    int isReport = 0; //是否举报 1已举报
    int type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_layout);
        init();
    }

    private void init() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("举报");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportActivity.this.finish();
            }
        });
        ll_eygs = (LinearLayout) findViewById(R.id.ll_eygs);
        ll_ggzp = (LinearLayout) findViewById(R.id.ll_ggzp);
        ll_yhsq = (LinearLayout) findViewById(R.id.ll_yhsq);
        ll_other = (LinearLayout) findViewById(R.id.ll_other);
        iv_eygs = (ImageView) findViewById(R.id.iv_eygs);
        iv_ggzp = (ImageView) findViewById(R.id.iv_ggzp);
        iv_other = (ImageView) findViewById(R.id.iv_other);
        iv_yhsq = (ImageView) findViewById(R.id.iv_yhsq);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        ll_eygs.setOnClickListener(this);
        ll_ggzp.setOnClickListener(this);
        ll_yhsq.setOnClickListener(this);
        ll_other.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        postId = getIntent().getIntExtra("postId", 0);
        isReport = getIntent().getIntExtra("isReport", 0);
        btn_submit.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_eygs:
                type = 1;
                iv_eygs.setBackgroundResource(R.drawable.report_select);
                iv_ggzp.setBackgroundResource(R.drawable.report_yuan);
                iv_other.setBackgroundResource(R.drawable.report_yuan);
                iv_yhsq.setBackgroundResource(R.drawable.report_yuan);
                btn_submit.setEnabled(true);
                break;
            case R.id.ll_ggzp:
                type = 2;
                iv_eygs.setBackgroundResource(R.drawable.report_yuan);
                iv_ggzp.setBackgroundResource(R.drawable.report_select);
                iv_other.setBackgroundResource(R.drawable.report_yuan);
                iv_yhsq.setBackgroundResource(R.drawable.report_yuan);
                btn_submit.setEnabled(true);
                break;
            case R.id.ll_yhsq:
                type = 3;
                iv_eygs.setBackgroundResource(R.drawable.report_yuan);
                iv_ggzp.setBackgroundResource(R.drawable.report_yuan);
                iv_other.setBackgroundResource(R.drawable.report_yuan);
                iv_yhsq.setBackgroundResource(R.drawable.report_select);
                btn_submit.setEnabled(true);
                break;
            case R.id.ll_other:
                type = 4;
                iv_eygs.setBackgroundResource(R.drawable.report_yuan);
                iv_ggzp.setBackgroundResource(R.drawable.report_yuan);
                iv_other.setBackgroundResource(R.drawable.report_select);
                iv_yhsq.setBackgroundResource(R.drawable.report_yuan);
                btn_submit.setEnabled(true);
                break;
            case R.id.btn_submit:
                if (isReport == 0) {
                    runService();
                } else {
                    ToastUtil.show(this, "举报成功");
                    this.finish();
                }
                break;
        }
    }

    private void runService() {
        ReportPostBodyBean bodyBean = new ReportPostBodyBean();
        bodyBean.setPostId(postId);
        bodyBean.setReportTypeId(type);
        new ReportPostClient(this, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<NormalDataBean>() {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(ReportActivity.this, APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(NormalDataBean result) {
                        if (result != null && result.getCode() == 0) {
                            ToastUtil.show(ReportActivity.this, "举报成功");
                            ReportActivity.this.finish();
                        } else {
                            ToastUtil.show(ReportActivity.this, result.getMsg());
                        }
                    }
                });
    }
}
