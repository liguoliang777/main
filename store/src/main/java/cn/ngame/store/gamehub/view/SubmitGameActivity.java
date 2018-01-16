package cn.ngame.store.gamehub.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.gamehub.AddGameBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.NormalDataBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.gamehub.AddGameClient;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.utils.APIErrorUtils;
import cn.ngame.store.util.StringUtil;
import cn.ngame.store.util.ToastUtil;

/**
 * 提交游戏
 * Created by gp on 2017/3/3 0003.
 */

public class SubmitGameActivity extends BaseFgActivity implements View.OnClickListener {

    private LinearLayout ll_back;
    private TextView tv_title, tv_rightTxt;
    private EditText et_title, et_content;
    String title, content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_game_activity);
        init();
    }

    private void init() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_rightTxt = (TextView) findViewById(R.id.tv_rightTxt);
        tv_title.setText("提交游戏");
        tv_rightTxt.setText("提交");
        ll_back.setOnClickListener(this);
        tv_rightTxt.setOnClickListener(this);
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                this.finish();
                break;
            case R.id.tv_rightTxt:
                title = et_title.getText().toString().trim();
                content = et_content.getText().toString().trim();
                if(StringUtil.isEmpty(title)){
                    ToastUtil.show(this, "名字不能为空");
                    return;
                }
                if(StringUtil.isEmpty(title)){
                    ToastUtil.show(this, "推荐理由不能为空");
                    return;
                }
                submitGame();
                break;
        }
    }

    private void submitGame() {
        AddGameBodyBean bodyBean = new AddGameBodyBean();
        bodyBean.setGameName(title);
        bodyBean.setRecommendReason(content);
        new AddGameClient(this, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<NormalDataBean>() {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(SubmitGameActivity.this, APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(NormalDataBean result) {
                        if (result != null && result.getCode() == 0) {
                            ToastUtil.show(SubmitGameActivity.this, "提交成功");
                            SubmitGameActivity.this.finish();
                        } else {
                            ToastUtil.show(SubmitGameActivity.this, result.getMsg());
                        }
                    }
                });
    }
}
