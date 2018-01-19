package cn.ngame.store.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import cn.ngame.store.R;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.UMEventNameConstant;
import cn.ngame.store.user.view.LoginActivity;
import cn.ngame.store.util.ToastUtil;

/**
 * 自定义下载进度条
 * Created by zeng on 2016/5/18.
 */
public class GameLoadProgressBar extends View {

    private static final String TAG = GameLoadProgressBar.class.getSimpleName();
    private final int mRadius;
    private final int openColor;
    private final int mDownloadHeight;
    private final float mStrokeWidth;
    private final Context context;

    private GameFileStatus gameFileStatus;
    private FileLoadInfo fileLoadInfo;
    private OnStateChangeListener listener;

    //private int progress = 0;           //当前进度


    private String text;                //进度条上的文字
    private float textSize;             //文字大小
    private float textColor;            //文字色彩
    private int nomalColor = 0;         //进度条前景色
    private int downloadedColor = 0;    //进度条背景色
    private float width;                //控件宽度
    private float height;               //控件长度

    private Paint paint;
    private RectF mRadiusRect;

    public GameLoadProgressBar(Context context) {
        this(context, null);
    }

    public GameLoadProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        downloadedColor = ContextCompat.getColor(context, R.color.download_bg);
        mStrokeWidth = getResources().getDimensionPixelOffset(R.dimen.stroke_line_width);
        mDownloadHeight = CommonUtil.dip2px(context, 1.5f);
        //获取布局文件中的值
        text = context.getResources().getString(attrs.getAttributeResourceValue("http://schemas" +
                        ".android.com/apk/res/android",
                "text", 0));
        String tempSize = attrs.getAttributeValue("http://schemas.android.com/apk/res/android",
                "textSize");
        textSize = CommonUtil.dip2px(context, Float.valueOf(tempSize.substring(0, tempSize.length
                () - 2)));
        String tempWidth = attrs.getAttributeValue("http://schemas.android.com/apk/res/android",
                "layout_width");
        width = CommonUtil.dip2px(context, Float.valueOf(tempWidth.substring(0, tempWidth.length
                () - 3)));
        //圆角
        mRadius = CommonUtil.dip2px(context, 4);

        String tempHeight = attrs.getAttributeValue("http://schemas.android.com/apk/res/android",
                "layout_height");
        height = CommonUtil.dip2px(context, Float.valueOf(tempHeight.substring(0, tempHeight
                .length() - 3)));

        textColor = context.getResources().getColor(attrs.getAttributeResourceValue
                ("http://schemas.android" +
                        ".com/apk/res/android", "textColor", -1));
        //nomalColor = context.getResources().getColor(attrs.getAttributeResourceValue
        // ("http://schemas.android
        // .com/apk/res/android","background",-1));
        nomalColor = ContextCompat.getColor(context, R.color.mainColor);
        openColor = ContextCompat.getColor(context, R.color.mainColor);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable
                .GameLoadProgressBar2);
        //progress = typedArray.getInt(R.styleable.DownLoadProgressBar_progress, 0);
        typedArray.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);

    }

    /**
     * 设置进度条状态
     *
     * @param status
     */
    public void setLoadState(GameFileStatus status) {

        this.gameFileStatus = status;
        if (gameFileStatus == null || gameFileStatus.getStatus() == GameFileStatus
                .STATE_UN_INSTALL) {
            text = "下载";
        } else if (gameFileStatus.getStatus() == GameFileStatus.STATE_DOWNLOAD) {         //下载中
            text = "暂停";
        } else if (gameFileStatus.getStatus() == GameFileStatus.STATE_PAUSE) {        //已经暂停
            text = "继续";
        } else if (gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_DOWNLOAD) { //已经下载完成
            //progress = 100;
            text = "安装";
        } else if (gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_INSTALL ||
                gameFileStatus.getStatus() ==
                        GameFileStatus.STATE_HAS_INSTALL_OLD) {     //已经安装
            text = "打开";
        }
//        else if(gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_INSTALL_OLD){
//            text = "更新";
//        }
        invalidate();
    }

    public void setFileLoadInfo(FileLoadInfo info) {
        this.fileLoadInfo = info;
    }

    /**
     * 根据进度条被点击后状态发生的变化，做响应处理
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
    public void toggle() {
        if (gameFileStatus == null || gameFileStatus.getStatus() == GameFileStatus
                .STATE_UN_INSTALL) {
            if (CommonUtil.isLogined()) {
                int permission = ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ToastUtil.show(context,"检测到手机下载存储权限被拒绝,请打开");
                    CommonUtil.verifyStoragePermissions((Activity) context);
                } else {
                    startDownload();
                }
            } else {
                showUnLoginDialog();
            }
        } else if (gameFileStatus.getStatus() == GameFileStatus.STATE_DOWNLOAD) { //下载中，点击后暂停
            pauseDownload();
        } else if (gameFileStatus.getStatus() == GameFileStatus.STATE_PAUSE) {    //暂停中，点击后下载
            restartDownload();
        } else if (gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_DOWNLOAD) { //已经下载完成，点击后安装
            Log.d(TAG, "已经下载完成，点击后安装");
            installApp();
        } else if (gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_INSTALL ||
                gameFileStatus.getStatus() ==
                        GameFileStatus.STATE_HAS_INSTALL_OLD) { //已经安装，点击后打开APP
            openApp();
        }
//        else if(gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_INSTALL_OLD){   //已经安装了旧版本
//            updateApp();
//        }

    }

    private void showUnLoginDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //填充对话框的布局
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_dialog_one_bt, null);

        TextView sureBt = (TextView) inflate.findViewById(R.id.right_tv);
        sureBt.setText(R.string.login_now);
        sureBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        ((TextView) inflate.findViewById(R.id.title)).setText(getResources().getString(R.string
                .unlogin_msg));
        dialog.setContentView(inflate);//将布局设置给Dialog

        Window dialogWindow = dialog.getWindow(); //获取当前Activity所在的窗体
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL);//设置Dialog从窗体底部弹出
        WindowManager.LayoutParams params = dialogWindow.getAttributes();   //获得窗体的属性
        //params.y = 20;  Dialog距离底部的距离
        params.width = getResources().getDimensionPixelSize(R.dimen.unlogin_dialog_width);
        //设置Dialog距离底部的距离
        dialogWindow.setAttributes(params); //将属性设置给窗体
        dialog.show();//显示对话框
    }

    /**
     * 进入下载状态
     */
    private void startDownload() {
        //埋点
        HashMap<String, String> map = new HashMap<>();
        map.put(KeyConstant.game_Name, fileLoadInfo.getTitle());
        MobclickAgent.onEvent(context, UMEventNameConstant.gameDownloadButton, map);
        if (gameFileStatus == null) {
            return;
        }
        gameFileStatus.setStatus(GameFileStatus.STATE_DOWNLOAD);
        this.text = "暂停";
        this.invalidate();
        if (listener != null) {

            listener.onStartDownload(fileLoadInfo);
        }
    }

    /**
     * 更新App
     */
    private void updateApp() {
        gameFileStatus.setStatus(GameFileStatus.STATE_DOWNLOAD);
        this.text = "暂停";
        this.invalidate();
        if (listener != null) {
            listener.updateApp(fileLoadInfo);
        }
    }

    /**
     * 由暂停重新开始下载
     */
    private void restartDownload() {
        gameFileStatus.setStatus(GameFileStatus.STATE_DOWNLOAD);
        this.text = "暂停";
        this.invalidate();
        if (listener != null) {
            listener.onRestartDownload(fileLoadInfo);
        }
    }

    /**
     * 暂停下载
     */
    private void pauseDownload() {
        gameFileStatus.setStatus(GameFileStatus.STATE_PAUSE);
        this.text = "继续";
        this.invalidate();
        if (listener != null) {
            listener.onPauseDownload(fileLoadInfo);
        }
    }

    /**
     * 安装下载文件
     */
    private void installApp() {
        gameFileStatus.setStatus(GameFileStatus.STATE_HAS_INSTALL);
        text = "安装";
        invalidate();
        if (listener != null) {
            listener.onInstallApp(fileLoadInfo);
        }
    }

    private void openApp() {
        gameFileStatus.setStatus(GameFileStatus.STATE_HAS_INSTALL);
        text = "打开";
        if (listener != null) {
            listener.onOpenApp(fileLoadInfo);
            invalidate();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put(KeyConstant.game_Name, fileLoadInfo.getTitle());
        MobclickAgent.onEvent(context, UMEventNameConstant.gameOpenButton,
                map);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //RectF对象
        mRadiusRect = new RectF();
        mRadiusRect.left = mStrokeWidth / 2;                                 //左边
        mRadiusRect.top = mStrokeWidth / 2;                                  //上边
        mRadiusRect.right = width - mStrokeWidth / 2;                       //右边
        mRadiusRect.bottom = height - mStrokeWidth / 2;                     //下边*/
        if (gameFileStatus == null || gameFileStatus.getStatus() == GameFileStatus
                .STATE_UN_INSTALL) {
            //绘制背景色
            paint.setColor(nomalColor);
            paint.setAntiAlias(true);
            canvas.drawRoundRect(mRadiusRect, mRadius, mRadius, paint);
            //canvas.drawRect(0,0,width,height,paint); //绘制矩形

            //绘制字体
            paint.setColor((int) textColor);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2;   //设置字体基线
            canvas.drawText(text, width / 2, baseline, paint);

            //} else if (gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_INSTALL_OLD) {
            //绘制背景色
        /*    paint.setColor(nomalColor);
            paint.setColor(downloadedColor);
            canvas.drawRoundRect(mRadiusRect, mRadius, mRadius, paint);
            // canvas.drawRect(0, 0, width, height, paint);

            //绘制字体
            paint.setColor((int) textColor);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2;   //设置字体基线
            canvas.drawText(text, width / 2, baseline, paint);*/


        } else if (gameFileStatus.getStatus() == GameFileStatus.STATE_DOWNLOAD || gameFileStatus
                .getStatus() == GameFileStatus
                .STATE_HAS_DOWNLOAD) {
            mRadiusRect = new RectF();
            mRadiusRect.left = 0;                                 //左边
            mRadiusRect.right = width;                       //右边
            mRadiusRect.top = 0;
            mRadiusRect.bottom = height;
            //绘制背景色
            paint.setColor(downloadedColor);
            canvas.drawRoundRect(mRadiusRect, mRadius, mRadius, paint);
            //canvas.drawRect(0, 0, width, height, paint);

            //绘 前景  ----深色的进度//下边*/
            paint.setColor(nomalColor);
            int downingWidth = (int) (width / gameFileStatus.getLength() * gameFileStatus
                    .getFinished());
            mRadiusRect.right = downingWidth;
            canvas.drawRoundRect(mRadiusRect, mRadius, mRadius, paint);
            //canvas.drawRect(0, 0,  downingWidth, height, paint);
            //绘前景

            //绘制字体
            paint.setColor((int) textColor);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2;   //设置字体基线

            canvas.drawText(text, width / 2, baseline, paint);

        } else if (gameFileStatus.getStatus() == GameFileStatus.STATE_PAUSE) {

            //绘制背景色
            paint.setColor(downloadedColor);
            canvas.drawRoundRect(mRadiusRect, mRadius, mRadius, paint);
            //canvas.drawRect(0, 0, width, height, paint);    //绘背景


            mRadiusRect = new RectF();
            mRadiusRect.left = 0;                                 //左边
            mRadiusRect.right = (int) width;                       //右边
            mRadiusRect.top = 0;           //绘 前景  ----深色的进度
            mRadiusRect.bottom = height;   //下边*/
            paint.setColor(nomalColor);
            int pauseWidth = (int) (width / gameFileStatus.getLength() * gameFileStatus
                    .getFinished());
            mRadiusRect.right = pauseWidth;
            canvas.drawRoundRect(mRadiusRect, mRadius, mRadius, paint);
            // canvas.drawRect(0, height / 2 - mDownloadHeight,  pauseWidth, height / 2 +
            // mDownloadHeight, paint);
            //绘前景

            //绘制字体
            paint.setColor((int) textColor);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2;   //设置字体基线
            canvas.drawText(text, width / 2, baseline, paint);

        } else if (gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_INSTALL ||
                gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_INSTALL_OLD) {  //如以下载完成
            // 或已成功安装
          /*  Paint.Style.FILL :填充内部
            Paint.Style.FILL_AND_STROKE ：填充内部和描边
            Paint.Style.STROKE ：仅描边*/
            //绘制背景色
            Paint paintStoke = new Paint();
            paintStoke.setAntiAlias(true);
            paintStoke.setColor(openColor);
            paintStoke.setStrokeWidth(mStrokeWidth);
            paintStoke.setStyle(Paint.Style.STROKE);

            canvas.drawRoundRect(mRadiusRect, mRadius, mRadius, paintStoke);//圆角
            //canvas.drawRect(0, 0, width, height, paint);

            //绘制字体
            paint.setColor(openColor);
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
        void onStartDownload(FileLoadInfo info);

        /**
         * 已安装了旧版本，执行更新操作
         */
        void updateApp(FileLoadInfo info);

        /**
         * 处理暂停操作
         */
        void onPauseDownload(FileLoadInfo info);

        /**
         * 处理暂停后重新下载操作
         */
        void onRestartDownload(FileLoadInfo info);

        /**
         * 处理下载完成后安装APP的操作
         */
        void onInstallApp(FileLoadInfo info);

        /**
         * 处理APP已安装成功后的打开操作
         */
        void onOpenApp(FileLoadInfo info);
    }

}
