package cn.ngame.store.base.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.AppCarouselBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.BrowseHistoryBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.gamehub.AppCarouselClient;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.activity.main.MainHomeActivity;
import cn.ngame.store.core.utils.APIErrorUtils;
import cn.ngame.store.core.utils.SystemUtil;
import cn.ngame.store.push.model.PushMessage;
import cn.ngame.store.util.ToastUtil;

/**
 * 广告页面
 * Created by gp on 2017/2/16 0016.
 */

public class AdvertisingPageActivity extends BaseFgActivity {

    private SimpleDraweeView sdv_img;
    private TextView tv_jump;
    String imgUrl;
    static Handler handler = new Handler();
    private static final int WAIT_TIME = 5;
    private int second = 4;
    private Intent msgIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertising_page_activity);
        init();
    }

    private void init() {
        sdv_img = (SimpleDraweeView) findViewById(R.id.sdv_img);
        tv_jump = (TextView) findViewById(R.id.tv_jump);

        tv_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpHome();
            }
        });
        long pushMsgId = getIntent().getLongExtra("msgId", 0);
        int pushMsgType = getIntent().getIntExtra("type", 0);
        PushMessage msg = (PushMessage) getIntent().getSerializableExtra("msg");
        msgIntent = new Intent(AdvertisingPageActivity.this, MainHomeActivity.class);
        if (pushMsgId > 0) {
            msgIntent.putExtra("msgId", pushMsgId);
            msgIntent.putExtra("type", pushMsgType);
            msgIntent.putExtra("msg", msg);
        }
        try {
            if (SystemUtil.checkNet(this)) {
                getCarousel();
            } else {
                jumpHome();
            }
        } catch (Exception e) {
            jumpHome();
        }
    }

    private void getCarousel() {
        BrowseHistoryBodyBean bean = new BrowseHistoryBodyBean();
        bean.setType(0);
        new AppCarouselClient(this, bean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<AppCarouselBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(AdvertisingPageActivity.this, APIErrorUtils.getMessage(e));
                        Log.d(TAG, "onError: "+ APIErrorUtils.getMessage(e));
                        jumpHome();
                    }

                    @Override
                    public void onNext(AppCarouselBean result) {
                        if (result != null && result.getCode() == 0) {
                            tv_jump.setVisibility(View.VISIBLE);
                            imgUrl = result.getData().get(0).getAdvImageLink();
                            if (imgUrl.contains("gif")) {
                                Uri uri = Uri.parse(imgUrl);
                                DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                                        .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                                        .setUri(uri)
                                        .build();
                                sdv_img.setController(draweeController);
                            } else {
                                sdv_img.setImageURI(imgUrl);
                            }
                            if (sdv_img.getDrawable() != null) { //图片是否加载成功
                                new Thread(runnable).start();
                            } else {
                                jumpHome();
                            }
                        } else {
                            ToastUtil.show(AdvertisingPageActivity.this, result.getMsg());
                            //// TODO: 2017/7/17 0017 移除
                            jumpHome();
                        }
                    }
                });
    }

    // 去首页
    public void jumpHome() {
        //将runnable从线程队列移除
        handler.removeCallbacks(runnable);

        startActivity(msgIntent);
        AdvertisingPageActivity.this.finish();
    }

    /**
     * 执行倒计时操作
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            for (int i = 0; i < WAIT_TIME; i++) {
//                if (second <= 0) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            jumpHome();
//                            return;
//                        }
//                    });
//                } else {
//                    second--;
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            tv_jump.setText("跳过" + second);
//                        }
//                    });
//                }
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

            second--;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tv_jump.setText("跳过" + second);
                    if (second == 0) {
                        jumpHome();
                    }
                }
            });
            handler.postDelayed(runnable, 1000);
//            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将runnable从线程队列移除
        handler.removeCallbacks(runnable);
    }
}
