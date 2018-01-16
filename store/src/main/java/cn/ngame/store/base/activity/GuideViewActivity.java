package cn.ngame.store.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.activity.main.MainHomeActivity;
import cn.ngame.store.adapter.GuideViewAdapter;
import cn.ngame.store.core.utils.FileUtil;
import cn.ngame.store.push.model.PushMessage;

public class GuideViewActivity extends BaseFgActivity {
    private ViewPager viewPage;
    // 图片
    private int[] imgs = {R.drawable.guide_0, R.drawable.guide_1, R.drawable.guide_2};
    private ArrayList<ImageView> list;
    // 底部小点的图片
    private LinearLayout llPoint;
    //立即进入按钮
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_view_acitivyt);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 隐藏状态栏
        initview();
        initoper();
        addView();
        addPoint();
    }

    private void initoper() {
        // 进入按钮
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                long pushMsgId = getIntent().getLongExtra("msgId", 0);
                int pushMsgType = getIntent().getIntExtra("type", 0);
                PushMessage msg = (PushMessage) getIntent().getSerializableExtra("msg");
                Intent msgIntent = new Intent(GuideViewActivity.this, MainHomeActivity.class);
                if (pushMsgId > 0) {
                    msgIntent.putExtra("msgId", pushMsgId);
                    msgIntent.putExtra("type", pushMsgType);
                    msgIntent.putExtra("msg", msg);
                }
                startActivity(msgIntent);
                GuideViewActivity.this.finish();
            }
        });

        // 2.监听当前显示的页面，将对应的小圆点设置为选中状态，其它设置为未选中状态
        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                monitorPoint(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    private void initview() {
        viewPage = (ViewPager) findViewById(R.id.viewpage);
        llPoint = (LinearLayout) findViewById(R.id.llPoint);
        textView = (TextView) findViewById(R.id.guideTv);
    }

    /**
     * 添加图片到view
     */
    private void addView() {
        list = new ArrayList<ImageView>();
        // 将imageview添加到view
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < imgs.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(params);
//            iv.setScaleType(ImageView.ScaleType.FIT_XY);
//            iv.setImageResource(imgs[i]);
            FileUtil.scaleImage(this, iv, imgs[i]);
            list.add(iv);
        }
        // 加入适配器
        viewPage.setAdapter(new GuideViewAdapter(list));

    }

    /**
     * 添加小圆点
     */
    private void addPoint() {
        // 1.根据图片多少，添加多少小圆点
        for (int i = 0; i < imgs.length; i++) {
            LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(
                    ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);
            if (i < 1) {
                pointParams.setMargins(0, 0, 0, 0);
            } else {
                pointParams.setMargins(20, 0, 0, 0);
            }
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(pointParams);
            iv.setBackgroundResource(R.drawable.white_radius);
            llPoint.addView(iv);
        }
        llPoint.getChildAt(0).setBackgroundResource(R.drawable.choosen_radius);

    }

    /**
     * 判断小圆点
     *
     * @param position
     */
    private void monitorPoint(int position) {
        for (int i = 0; i < imgs.length; i++) {
            if (i == position) {
                llPoint.getChildAt(position).setBackgroundResource(
                        R.drawable.choosen_radius);
            } else {
                llPoint.getChildAt(i).setBackgroundResource(
                        R.drawable.white_radius);
            }
        }
        // 3.当滑动到最后一个添加按钮点击进入，
        if (position == imgs.length - 1) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}