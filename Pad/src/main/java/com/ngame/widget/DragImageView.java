package com.ngame.widget;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.lx.pad.util.LLog;

/**
 * Created by Administrator on 2017/12/5.
 */

public class DragImageView extends android.support.v7.widget.AppCompatImageView {
    private int disWidth;
    private int disHeight;
    private int x;
    private int y;
    private int rawX;
    private int rawY;
    private long startTime;
    private float pressLen; //两点按下时的间距
    private float moveLen;  //两点移动时的间距
    private float lenScale; //移动前和移动后的比
    int nTouchState;        //状态，单点还是多点
    DragInterface dragInterface;
    ScaleInterface scaleInterface;
    DragImgClickInterface dragImgClickInterface;

    public DragImageView(Context context) {
        super(context);
        nTouchState = TouchState.STATE_NONE;
        init();
    }

    public DragImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        nTouchState = TouchState.STATE_NONE;
        init();
    }

    private void init(){
        WindowManager windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        if(Build.VERSION.SDK_INT <= 12){
            disWidth = windowManager.getDefaultDisplay().getWidth();
            disHeight = windowManager.getDefaultDisplay().getHeight();
        }else{
            Point pt = new Point();
            windowManager.getDefaultDisplay().getSize(pt);
            disWidth = pt.x;
            disHeight = pt.y;
        }
    }

    //两点之前距离
    private static float moveLength(MotionEvent event){
        float w = event.getX(0) - event.getX(1);
        float h = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(w * w + h * h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        LLog.d("DragImageView->onTouchEvent action:" + event.getAction() + " nTouchState:" + nTouchState);
        ViewGroup.LayoutParams layoutParams;
        switch(event.getAction() & 0xFF){
            case MotionEvent.ACTION_DOWN:{
                nTouchState = TouchState.STATE_SINGLE_POINT;
                rawX = (int)event.getRawX();
                rawY = (int)event.getRawY();
                x = (int)event.getX();
                y = rawY - getTop();
                if(dragInterface != null){
                    dragInterface.onDragStart(this);
                }
                bringToFront();
                startTime = System.currentTimeMillis();
                break;
            }
            case MotionEvent.ACTION_UP:{
                if(nTouchState == TouchState.STATE_SINGLE_POINT && dragInterface != null){
                    dragInterface.onDragFinish(this);
                }
                if(System.currentTimeMillis() - startTime < 150 && dragImgClickInterface != null){
                    dragImgClickInterface.onDragImageViewClick(this);
                }
                nTouchState = TouchState.STATE_NONE;
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                if(dragInterface != null){
                    dragInterface.onDragMove(this);
                }
                if(nTouchState == TouchState.STATE_SINGLE_POINT){
                    int startX = rawX - x;
                    int endX = rawX - x + getWidth();
                    int startY = rawY - y;
                    int endY = rawY - y + getHeight();
                    //LLog.d("DragImageView->onTouchEvent [1] startX:" + startX + " startY:" + startY + " rawX:" + rawX + " rawY:" + rawY + " disWidth:" + disWidth + " disHeight:" + disHeight);
                    if(startX <= 0){
                        startX = 0;
                        endX = getWidth();
                    }
                    startX = (endX >= disWidth) ? (disWidth - getWidth()) : startX;
                    if(startY <= 0){
                        startY = 0;
                        endY = getHeight();
                    }
                    if(endY >= disHeight){
                        startY = disHeight - getHeight();
                    }

                    layoutParams = getLayoutParams();
                    if(layoutParams instanceof FrameLayout.LayoutParams){
                        ((FrameLayout.LayoutParams)layoutParams).leftMargin = startX;
                        ((FrameLayout.LayoutParams)layoutParams).topMargin = startY;
                    }
                    setLayoutParams(layoutParams);
                    rawX = (int)event.getRawX();
                    rawY = (int)event.getRawY();
                    //LLog.d("DragImageView->onTouchEvent [2] startX:" + startX + " startY:" + startY + " rawX:" + rawX + " rawY:" + rawY);
                    return true;
                }
                if(nTouchState != TouchState.STATE_MUTIL_POINT){
                    return true;
                }

                moveLen = moveLength(event);
//                LLog.d("DragImageView->onTouchEvent moveLen:" + moveLen + " pressLen:" + pressLen);
                if(Math.abs(moveLen - pressLen) <= 5f){
                    return true;
                }

                lenScale = moveLen / pressLen;
                layoutParams = getLayoutParams();
                layoutParams.width = (int)(getWidth() * lenScale);
                layoutParams.height = (int)(getHeight() * lenScale);
                if(layoutParams instanceof FrameLayout.LayoutParams){
                    ((FrameLayout.LayoutParams)layoutParams).leftMargin -= (layoutParams.width - getWidth())/2;
                    ((FrameLayout.LayoutParams)layoutParams).topMargin -= (layoutParams.height - getHeight())/2;
                }
                setLayoutParams(layoutParams);
                pressLen = moveLen;
//                LLog.d("DragImageView->onTouchEvent mutil point");
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN:{
//                LLog.d("DragImageView->onTouchEvent ACTION_POINTER_DOWN PointerCount:" + event.getPointerCount());
                if(event.getPointerCount() != 2){
                    return true;
                }
                nTouchState = TouchState.STATE_MUTIL_POINT;
                pressLen = moveLength(event);
                if(scaleInterface != null){
                    scaleInterface.onScaleStart(this);
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:{
//                LLog.d("DragImageView->onTouchEvent ACTION_POINTER_UP");
                if(nTouchState == TouchState.STATE_MUTIL_POINT && scaleInterface != null){
                    scaleInterface.onScaleFinish(this);
                }
                nTouchState = TouchState.STATE_NONE;
                break;
            }
        }
        return true;
    }

    public void setDragInterface(DragInterface i){dragInterface = i;}
    public void setScaleInterface(ScaleInterface i){scaleInterface = i;}
    public void setDragImgClickInterface(DragImgClickInterface i){dragImgClickInterface = i;}
}
