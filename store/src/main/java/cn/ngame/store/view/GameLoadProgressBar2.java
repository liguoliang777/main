package cn.ngame.store.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.CommonUtil;

/**
 * 自定义下载进度条
 * Created by zeng on 2016/5/18.
 */
public class GameLoadProgressBar2 extends View {

    /**
     * 未安装
     */
    public static final int STATE_UN_INSTALL = 0;
    /**
     * 已安装
     */
    public static final int STATE_INSTALLED = 1;
    /**
     * 下载中
     */
    public static final int STATE_DOWNLOAD = 2;
    /**
     * 暂停中
     */
    public static final int STATE_PAUSE = 3;
    /**
     * 已下载完成
     */
    public static final int STATE_HAS_DOWNLOAD = 4;
    /**
     * 解压中
     */
    public static final int STATE_UNZIP_ING = 5;
    /**
     * 解压完成
     */
    public static final int STATE_HAS_UNZIP = 6;
    private final int heightDownloading;

    private int state = STATE_UN_INSTALL;    //按钮当前状态


    private OnStateChangeListener listener;

    private String text;                //进度条上的文字
    private float textSize;             //文字大小
    private float textColor;            //文字色彩
    private int frontColor = 0;         //进度条前景色
    private int backgroundColor = 0;    //进度条背景色

    private float width;                //控件宽度
    private float height;               //控件长度

    private int progress = 0;           //当前进度

    private Paint paint;


    public GameLoadProgressBar2(Context context) {
        this(context, null);
    }

    public GameLoadProgressBar2(Context context, AttributeSet attrs) {
        super(context, attrs);

        backgroundColor = context.getResources().getColor(R.color.progress_bar_background);
        //获取布局文件中的值
        text = context.getResources().getString(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android",
                "text", 0));
        String tempSize = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textSize");
        textSize = CommonUtil.dip2px(context, Float.valueOf(tempSize.substring(0, tempSize.length() - 2)));
        String tempWidth = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
        width = CommonUtil.dip2px(context, Float.valueOf(tempWidth.substring(0, tempWidth.length() - 3)));
        String tempHeight = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
        height = CommonUtil.dip2px(context, Float.valueOf(tempHeight.substring(0, tempHeight.length() - 3)));
        heightDownloading = CommonUtil.dip2px(context, 5);

        textColor = context.getResources().getColor(attrs.getAttributeResourceValue("http://schemas.android" +
                ".com/apk/res/android", "textColor", -1));
        frontColor = context.getResources().getColor(attrs.getAttributeResourceValue("http://schemas.android" +
                ".com/apk/res/android", "background", -1));

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GameLoadProgressBar2);
        progress = typedArray.getInt(R.styleable.GameLoadProgressBar2_progress, 0);
        typedArray.recycle();

        paint = new Paint();

    }

    /**
     * 设置进度条状态
     *
     * @param state
     */
    public void setState(int state) {
        this.state = state;
        if (state == STATE_UN_INSTALL) {
            text = "下载";
        } else if (state == STATE_DOWNLOAD) {
            text = "暂停";
        } else if (state == STATE_PAUSE) {        //已经暂停
            text = "继续";
        } else if (state == STATE_HAS_DOWNLOAD || state == STATE_HAS_UNZIP) { //已经下载完成
            progress = 100;
            text = "安装";
        } else if (state == STATE_INSTALLED) {     //已经安装
            text = "打开";
        } else if (state == STATE_UNZIP_ING) {     //已经安装
            text = "解压中";
        }
        invalidate();
    }

    /**
     * 设置进度条进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * 更新进度条进度
     *
     * @param progress
     */
    public void updateProgress(int progress) {
        //Log.d(TAG,"updateProgress : "+progress);
        this.progress = progress;
        if (progress == 100) {
            state = STATE_HAS_DOWNLOAD;
            text = "安装";
        }
        this.invalidate();
    }

    /**
     * 设置
     *
     * @param listener
     */
    public void setOnStateChangeListener(OnStateChangeListener listener) {
        this.listener = listener;
    }

    /**
     * 切换ProgressBar状态
     *
     * @return state 进度条当前的状态
     */
    public int toggle() {

        if (state == STATE_UN_INSTALL) {
            startDownload();
        } else if (state == STATE_DOWNLOAD) {
            pauseDownload();
        } else if (state == STATE_PAUSE) {    //已经下载完成
            restartDownload();
        } else if (state == STATE_HAS_DOWNLOAD) { //已经下载完成
            if (listener != null) {
                listener.onInstallApp();
            }
            state = STATE_INSTALLED;
            text = "打开";
            invalidate();
        } else if (state == STATE_INSTALLED) { //已经安装
            if (listener != null) {
                listener.onOpenApp();
                invalidate();
            }
        }

        return state;
    }

    /**
     * 进入下载状态
     */
    private void startDownload() {
        this.state = STATE_DOWNLOAD;
        this.text = "暂停";
        this.invalidate();
        if (listener != null) {
            listener.onStartDownload();
        }
    }

    /**
     * 由暂停重新开始下载
     */
    private void restartDownload() {
        this.state = STATE_DOWNLOAD;
        this.text = "暂停";
        this.invalidate();
        if (listener != null) {
            listener.onRestartDownload();
        }
    }

    /**
     * 暂停下载
     */
    private void pauseDownload() {
        this.state = STATE_PAUSE;
        this.text = "继续";
        this.invalidate();
        if (listener != null) {
            listener.onPauseDownload();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (state == STATE_UN_INSTALL || state == STATE_UNZIP_ING || state == STATE_HAS_UNZIP) {

            //绘制背景色
            paint.setColor(frontColor);
            canvas.drawRect(0, 0, width, height, paint);

            //绘制字体
            paint.setColor((int) textColor);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2;   //设置字体基线
            canvas.drawText(text, width / 2, baseline, paint);

        } else if (state == STATE_DOWNLOAD) {//下载中

            //绘制背景色
            paint.setColor(backgroundColor);
            canvas.drawRect(0, 0, width, height, paint);    //绘背景
            paint.setColor(frontColor);
            canvas.drawRect(0, 0, width / 100 * progress, height, paint);   //绘前景

            //绘制字体
            paint.setColor((int) textColor);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2;   //设置字体基线

            canvas.drawText(text, width / 2, baseline, paint);

        } else if (state == STATE_PAUSE) {//继续

            //绘制背景色
            paint.setColor(backgroundColor);
            canvas.drawRect(0, 0, width, height, paint);    //绘背景
            paint.setColor(frontColor);
            canvas.drawRect(0, 0, width / 100 * progress, height, paint);   //绘前景

            //绘制字体
            paint.setColor((int) textColor);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2;   //设置字体基线
            canvas.drawText(text, width / 2, baseline, paint);

        } else if (state == STATE_HAS_DOWNLOAD || state == STATE_INSTALLED) {  //如以下载完成 或已成功安装

            //绘制背景色
            paint.setColor(frontColor);
            canvas.drawRect(0, 0, width, height, paint);

            //绘制字体
            paint.setColor((int) textColor);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2;   //设置字体基线
            canvas.drawText(text, width / 2, baseline, paint);
        }

    }

    /**
     * 进度条完成后执行的操作
     */
    public interface OnStateChangeListener {

        /**
         * 处理开始下载任务
         */
        void onStartDownload();

        /**
         * 处理暂停操作
         */
        void onPauseDownload();

        /**
         * 处理暂停后重新下载操作
         */
        void onRestartDownload();

        /**
         * 处理下载完成后安装APP的操作
         */
        void onInstallApp();

        /**
         * 处理APP已安装成功后的打开操作
         */
        void onOpenApp();
    }
}
