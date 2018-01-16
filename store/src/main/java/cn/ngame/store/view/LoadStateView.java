package cn.ngame.store.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ngame.store.R;

/**
 * 加载动画控件
 * Created by zeng on 2016/6/15.
 */
public class LoadStateView extends FrameLayout {

    /**
     * 正在加载
     */
    public static final int STATE_ING = 0;
    /**
     * 加载失败
     */
    public static final int STATE_END = 1;
    private final Context content;
    private ImageView loadingIv;

    private FrameLayout lay_loading, lay_load_bad;
    private TextView tv_summary;
    private Button bt_reload;
    private int state = STATE_ING;

    private String text_ing, text_end;

    private boolean isShowReload = false;

    private OnClickListener listener;
    private long ANIMATION_DURATION = 400;

    public LoadStateView(Context context) {
        this(context, null);
    }

    public LoadStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        content = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadStateView);
        text_ing = typedArray.getString(R.styleable.LoadStateView_ing);
        text_end = typedArray.getString(R.styleable.LoadStateView_end);
        isShowReload = typedArray.getBoolean(R.styleable.LoadStateView_visible, true);
        typedArray.recycle();

        inflate(context, R.layout.layout_load_state, this);

        lay_loading = (FrameLayout) findViewById(R.id.loading);
        lay_load_bad = (FrameLayout) findViewById(R.id.load_bad);

        tv_summary = (TextView) findViewById(R.id.tv_summary);
        loadingIv = (ImageView) findViewById(R.id.img_1);
        bt_reload = (Button) findViewById(R.id.but1);

        if (state == STATE_END) {

            lay_load_bad.setVisibility(VISIBLE);
            lay_loading.setVisibility(GONE);
            if (isShowReload) {
                bt_reload.setVisibility(VISIBLE);
            } else {
                bt_reload.setVisibility(GONE);
            }

            tv_summary.setText(text_end);

           /*  左右摇摆
           Animation animation = new TranslateAnimation(0, 10, 0, 0);
            animation.setFillAfter(false);//True:图片停在动画结束位置
            animation.setDuration(100);    //设置动画完成时间
            animation.setRepeatMode(Animation.REVERSE);
            animation.setRepeatCount(Integer.MAX_VALUE);
            img_smoke.startAnimation(animation);*/

        } else {
            lay_load_bad.setVisibility(GONE);
            lay_loading.setVisibility(VISIBLE);
            bt_reload.setVisibility(GONE);
            tv_summary.setText(text_ing);

            Animation animation = AnimationUtils.loadAnimation(content, R.anim.tip);
            //LinearInterpolator为匀速效果，Accelerateinterpolator为加速效果、DecelerateInterpolator为减速效果
           LinearInterpolator interpolator = new LinearInterpolator();
            animation.setInterpolator(interpolator);
            //animation.setFillAfter(false);//True:图片停在动画结束位置
            animation.setDuration(ANIMATION_DURATION);    //设置动画完成时间
            animation.setRepeatCount(Integer.MAX_VALUE);
            loadingIv.startAnimation(animation);
        }
    }

    /**
     * 是否显示重新加载的按钮
     *
     * @param isShowReload
     */
    public void isShowLoadBut(boolean isShowReload) {
        this.isShowReload = isShowReload;
    }

    /**
     * 设置重新加载监听器
     *
     * @param listener
     */
    public void setReLoadListener(OnClickListener listener) {

        this.listener = listener;
        if (bt_reload != null) {
            bt_reload.setOnClickListener(listener);
        }
    }

    /**
     * 设置加载状态
     *
     * @param state
     */
    public void setState(int state) {
        this.state = state;
        if (state == STATE_END) {
            lay_load_bad.setVisibility(VISIBLE);
            if (isShowReload) {
                bt_reload.setVisibility(VISIBLE);
            } else {
                bt_reload.setVisibility(GONE);
            }
            tv_summary.setText(text_end);
            lay_loading.setVisibility(GONE);

            /*Animation animation = new TranslateAnimation(0, 10, 0, 0);
            animation.setFillAfter(false);//True:图片停在动画结束位置
            animation.setDuration(100);    //设置动画完成时间
            animation.setRepeatMode(Animation.REVERSE);
            animation.setRepeatCount(Integer.MAX_VALUE);
            img_smoke.startAnimation(animation);*/

        } else {
            lay_load_bad.setVisibility(GONE);
            lay_loading.setVisibility(VISIBLE);
            bt_reload.setVisibility(GONE);
            tv_summary.setText(text_ing);
        }
    }

    /**
     * 设置加载状态
     *
     * @param state 加载状态
     * @param text  加载结果提示信息
     */
    public void setState(int state, String text) {
        this.state = state;

        if (state == STATE_END) {
            lay_load_bad.setVisibility(VISIBLE);
            if (isShowReload) {
                bt_reload.setVisibility(VISIBLE);
            } else {
                bt_reload.setVisibility(GONE);
            }
            tv_summary.setText(text);
            lay_loading.setVisibility(GONE);

           /* Animation animation = new TranslateAnimation(0, 10, 0, 0);
            animation.setFillAfter(false);//True:图片停在动画结束位置
            animation.setDuration(100);    //设置动画完成时间
            animation.setRepeatMode(Animation.REVERSE);
            animation.setRepeatCount(Integer.MAX_VALUE);
            img_smoke.startAnimation(animation);*/

        } else {

            lay_load_bad.setVisibility(GONE);
            lay_loading.setVisibility(VISIBLE);
            bt_reload.setVisibility(GONE);
            tv_summary.setText(text);

           /* Animation animation = new TranslateAnimation(0, 0, 0, 20);
            animation.setFillAfter(false);//True:图片停在动画结束位置
            animation.setDuration(ANIMATION_DURATION);    //设置动画完成时间
            animation.setRepeatMode(Animation.REVERSE);
            animation.setRepeatCount(Integer.MAX_VALUE);
            //img_hand.startAnimation(animation);*/
        }
    }
}
