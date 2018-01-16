package cn.ngame.store.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import cn.ngame.store.R;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.utils.CommonUtil;

/**
 * 映射精灵下载页面进度条
 * Created by zeng on 2016/11/17.
 */
public class SpriteLoadProgressBar extends View {

    private static final String TAG = SpriteLoadProgressBar.class.getSimpleName();

    private GameFileStatus gameFileStatus;
    private FileLoadInfo fileLoadInfo;
    private OnStateChangeListener listener;

    //private int progress = 0;           //当前进度


    private String text;                //进度条上的文字
    private float textSize;             //文字大小
    private float textColor;            //文字色彩
    private int frontColor = 0;         //进度条前景色
    private int backgroundColor = 0;    //进度条背景色
    private float width;                //控件宽度
    private float height;               //控件长度

    private Paint paint;

    public SpriteLoadProgressBar(Context context) {
        this(context,null);
    }

    public SpriteLoadProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        backgroundColor = context.getResources().getColor(R.color.progress_bar_background);
        //获取布局文件中的值
        text = context.getResources().getString(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android","text",0));
        String tempSize = attrs.getAttributeValue("http://schemas.android.com/apk/res/android","textSize");
        textSize = CommonUtil.dip2px(context,Float.valueOf(tempSize.substring(0,tempSize.length()-2)));
        String tempWidth = attrs.getAttributeValue("http://schemas.android.com/apk/res/android","layout_width");
        width = CommonUtil.dip2px(context,Float.valueOf(tempWidth.substring(0,tempWidth.length()-3)));
        String tempHeight = attrs.getAttributeValue("http://schemas.android.com/apk/res/android","layout_height");
        height = CommonUtil.dip2px(context,Float.valueOf(tempHeight.substring(0,tempHeight.length()-3)));

        textColor = context.getResources().getColor(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android","textColor",-1));
        //frontColor = context.getResources().getColor(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android","background",-1));
        frontColor = context.getResources().getColor(R.color.mainColor);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GameLoadProgressBar2);
        //progress = typedArray.getInt(R.styleable.DownLoadProgressBar_progress, 0);
        typedArray.recycle();

        paint = new Paint();

    }

    /**
     * 设置进度条状态
     * @param status
     */
    public void setLoadState(GameFileStatus status){

        this.gameFileStatus = status;

        if (gameFileStatus == null || gameFileStatus.getStatus() == GameFileStatus.STATE_UN_INSTALL){
            text = "立即下载";
        } else if(gameFileStatus.getStatus() == GameFileStatus.STATE_DOWNLOAD){         //下载中
            text = "暂停";
        }else if (gameFileStatus.getStatus() == GameFileStatus.STATE_PAUSE){        //已经暂停
            text = "继续";
        }else if (gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_DOWNLOAD){ //已经下载完成
            //progress = 100;
            text = "安装";
        }else if(gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_INSTALL){     //已经安装
            text = "打开";
        }else if(gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_INSTALL_OLD){
            text = "更新";
        }
        invalidate();
    }

    public void setFileLoadInfo(FileLoadInfo info){
        this.fileLoadInfo = info;
    }

    /**
     * 根据进度条被点击后状态发生的变化，做响应处理
     * @param listener
     */
    public void setOnStateChangeListener(OnStateChangeListener listener) {
        this.listener = listener;
    }

    /**
     * 切换ProgressBar状态
     * @return state 进度条当前的状态
     */
    public void toggle() {

        if (gameFileStatus == null || gameFileStatus.getStatus() == GameFileStatus.STATE_UN_INSTALL){
            startDownload();
        } else if(gameFileStatus.getStatus() == GameFileStatus.STATE_DOWNLOAD){ //下载中，点击后暂停
            pauseDownload();
        }else if (gameFileStatus.getStatus() == GameFileStatus.STATE_PAUSE){    //暂停中，点击后下载
            restartDownload();
        }else if (gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_DOWNLOAD){ //已经下载完成，点击后安装
            installApp();
        }else if(gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_INSTALL){ //已经安装，点击后打开APP
            openApp();
        }else if(gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_INSTALL_OLD){   //已经安装了旧版本
            updateApp();
        }

    }

    /**
     * 进入下载状态
     */
    private void startDownload(){
        gameFileStatus.setStatus(GameFileStatus.STATE_DOWNLOAD);
        this.text = "暂停";
        this.invalidate();
        if(listener != null){
            listener.onStartDownload(fileLoadInfo);
        }
    }

    /**
     * 更新App
     */
    private void updateApp(){

        gameFileStatus.setStatus(GameFileStatus.STATE_DOWNLOAD);
        this.text = "暂停";
        this.invalidate();
        if(listener != null){
            listener.updateApp(fileLoadInfo);
        }
    }

    /**
     * 由暂停重新开始下载
     */
    private void restartDownload(){
        gameFileStatus.setStatus(GameFileStatus.STATE_DOWNLOAD);
        this.text = "暂停";
        this.invalidate();
        if(listener != null){
            listener.onRestartDownload(fileLoadInfo);
        }
    }

    /**
     * 暂停下载
     */
    private void pauseDownload(){
        gameFileStatus.setStatus(GameFileStatus.STATE_PAUSE);
        this.text = "继续";
        this.invalidate();
        if(listener != null){
            listener.onPauseDownload(fileLoadInfo);
        }
    }

    /**
     * 安装下载文件
     */
    private void installApp(){
        gameFileStatus.setStatus(GameFileStatus.STATE_HAS_INSTALL);
        text = "安装";
        invalidate();
        if(listener != null){
            listener.onInstallApp(fileLoadInfo);
        }
    }

    private void openApp(){

        gameFileStatus.setStatus(GameFileStatus.STATE_HAS_INSTALL);
        text = "打开";
        if(listener != null){
            listener.onOpenApp(fileLoadInfo);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(gameFileStatus == null || gameFileStatus.getStatus() == GameFileStatus.STATE_UN_INSTALL){

            //绘制背景色
            paint.setColor(frontColor);
            paint.setAntiAlias(true);
            /*RectF r1=new RectF();                      //RectF对象
            r1.left=0;                                 //左边
            r1.top=0;                                  //上边
            r1.right=(int)width;                       //右边
            r1.bottom=(int)height;                     //下边*/
            canvas.drawRect(0,0,width,height,paint);
            //canvas.drawRoundRect(r1,15,15,paint);

            //绘制字体
            paint.setColor((int)textColor);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = (height-fontMetrics.bottom-fontMetrics.top)/2;   //设置字体基线
            canvas.drawText(text, width/2, baseline, paint);

        }else if(gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_INSTALL_OLD){

            //绘制背景色
            paint.setColor(frontColor);
            paint.setColor(backgroundColor);
            /*RectF r1=new RectF();                      //RectF对象
            r1.left=0;                                 //左边
            r1.top=0;                                  //上边
            r1.right=(int)width;                       //右边
            r1.bottom=(int)height;                     //下边*/
            canvas.drawRect(0,0,width,height,paint);
            //canvas.drawRoundRect(r1,15,15,paint);

            //绘制字体
            paint.setColor((int)textColor);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = (height-fontMetrics.bottom-fontMetrics.top)/2;   //设置字体基线
            canvas.drawText(text, width/2, baseline, paint);

        }else if(gameFileStatus.getStatus() == GameFileStatus.STATE_DOWNLOAD) {

            /*RectF r1=new RectF();                      //RectF对象
            r1.left=0;                                 //左边
            r1.top=0;                                  //上边
            r1.right=(int)width;                       //右边
            r1.bottom=(int)height;                     //下边*/
            //绘制背景色
            paint.setColor(backgroundColor);
            canvas.drawRect(0,0,width,height,paint);    //绘背景
            //canvas.drawRoundRect(r1,15,15,paint);    //绘背景
            paint.setColor(frontColor);

            /*RectF r2=new RectF();                      //RectF对象
            r2.left=0;                                 //左边
            r2.top=0;                                  //上边
            r2.right=(int)(width/gameFileStatus.getLength()*gameFileStatus.getFinished());                       //右边
            r2.bottom=(int)height;                     //下边
            canvas.drawRoundRect(r2,15,15,paint);   //绘前景*/
            canvas.drawRect(0,0,(int)(width/gameFileStatus.getLength()*gameFileStatus.getFinished()),height,paint);   //绘前景

            //绘制字体
            paint.setColor((int)textColor);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = (height-fontMetrics.bottom-fontMetrics.top)/2;   //设置字体基线

            canvas.drawText(text, width/2, baseline, paint);

        }else if(gameFileStatus.getStatus() == GameFileStatus.STATE_PAUSE){

            //绘制背景色
            paint.setColor(backgroundColor);
            /*RectF r1=new RectF();                      //RectF对象
            r1.left=0;                                 //左边
            r1.top=0;                                  //上边
            r1.right=(int)width;                       //右边
            r1.bottom=(int)height;                     //下边*/
            canvas.drawRect(0,0,width,height,paint);    //绘背景
            //canvas.drawRoundRect(r1,15,15,paint);    //绘背景
            paint.setColor(frontColor);

            /*RectF r2=new RectF();                      //RectF对象
            r2.left=0;                                 //左边
            r2.top=0;                                  //上边
            r2.right=(int)(width/gameFileStatus.getLength()*gameFileStatus.getFinished());                       //右边
            r2.bottom=(int)height;                     //下边*/
            canvas.drawRect(0,0,(int)(width/gameFileStatus.getLength()*gameFileStatus.getFinished()),height,paint);   //绘前景
            //canvas.drawRoundRect(r2,15,15,paint);   //绘前景

            //绘制字体
            paint.setColor((int)textColor);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = (height-fontMetrics.bottom-fontMetrics.top)/2;   //设置字体基线
            canvas.drawText(text, width/2, baseline, paint);

        }else if(gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_DOWNLOAD || gameFileStatus.getStatus() == GameFileStatus.STATE_HAS_INSTALL){  //如以下载完成 或已成功安装

            //绘制背景色
            paint.setColor(frontColor);
            paint.setColor(backgroundColor);
            /*RectF r1=new RectF();                      //RectF对象
            r1.left=0;                                 //左边
            r1.top=0;                                  //上边
            r1.right=(int)width;                       //右边
            r1.bottom=(int)height;                     //下边*/
            canvas.drawRect(0,0,width,height,paint);
            //canvas.drawRoundRect(r1,15,15,paint);

            //绘制字体
            paint.setColor((int)textColor);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            float baseline = (height-fontMetrics.bottom-fontMetrics.top)/2;   //设置字体基线
            canvas.drawText(text, width/2, baseline, paint);
        }
    }

    /**
     * 进度条完成后执行的操作
     */
    public interface OnStateChangeListener{

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
