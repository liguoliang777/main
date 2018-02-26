package com.ngame.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaRecorder;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import com.lx.pad.R;
import com.lx.pad.util.LLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */

public class PointerLocationView extends View {
    private static float ptRadius;  //半径
    private final ViewConfiguration viewConfiguration;
    private final Paint paintBlack = new Paint();
    private final Paint paintHalfWhite = new Paint();
    private final Paint paintRed = new Paint();
    private final Paint paintWhile = new Paint();
    private final Paint paintBlue = new Paint();
    private final Paint.FontMetricsInt fontMetricsInt;
    private final List<PointerInfo> ptInfoList;
    private int txHeight;       //文本高度一般 descent-asscent
    private boolean hasPoint;   //是否存在任意触点
    private int ptCurCount;     //当前触点个数
    private int maxCount;       //使用的最大触点数
    private boolean isShowLog;

    class PointerInfo{
        final List<Float> listX;
        final List<Float> listY;
        boolean pressed;
        int x;
        int y;
        float pressure;
        float size;
        VelocityTracker velocityTracker;

        PointerInfo(){
            super();
            listX = new ArrayList<Float>();
            listY = new ArrayList<Float>();
        }
    }

    public PointerLocationView(Context context) {
        super(context);
        ptRadius = getContext().getResources().getDimension(R.dimen.pointer_location_view_circle_radius);
        fontMetricsInt = new Paint.FontMetricsInt();
        ptInfoList = new ArrayList();
        viewConfiguration = ViewConfiguration.get(context);
        init();
    }

    public PointerLocationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ptRadius = getContext().getResources().getDimension(R.dimen.pointer_location_view_circle_radius);
        fontMetricsInt = new Paint.FontMetricsInt();
        ptInfoList = new ArrayList();
        viewConfiguration = ViewConfiguration.get(context);
        init();
    }

    private void init(){
        setFocusable(true);
        paintBlack.setAntiAlias(true);
        paintBlack.setTextSize(10f * getResources().getDisplayMetrics().density);
        paintBlack.setARGB(255, 0, 0, 0);
        paintHalfWhite.setAntiAlias(false);
        paintHalfWhite.setARGB(128, 255, 255, 255);
        paintRed.setAntiAlias(false);
        paintRed.setARGB(192, 255, 0, 0);
        paintWhile.setAntiAlias(true);
        paintWhile.setARGB(255, 255, 255, 255);
        paintBlue.setAntiAlias(false);
        paintBlue.setARGB(255, 0, 0, 192);
        for(int i = 0; i < 10; i++){
            PointerInfo pointerInfo = new PointerInfo();
            pointerInfo.velocityTracker = VelocityTracker.obtain();
            ptInfoList.add(pointerInfo);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        synchronized (ptInfoList){
            try{
                float width = (float)getWidth();
                float segment = (float)(width / 7.0);
                float ascent = -fontMetricsInt.ascent + 1;
                float fontHeight = (float)txHeight;
                int listSize = ptInfoList.size();
                if(listSize > 0){
                    PointerInfo ptInfo = ptInfoList.get(0);
                    canvas.drawRect(0f, 0f, segment - 1, fontHeight, paintHalfWhite);
                    canvas.drawText("P: " + ptCurCount + " / " + maxCount, 1f, ascent, paintBlack);
                    int listXSize = ptInfo.listX.size();
                    if(hasPoint && ptInfo.pressed || listXSize == 0){
                        canvas.drawRect(segment, 0f, segment * 2 - 1, fontHeight, paintHalfWhite);
                        canvas.drawText("X: " + ptInfo.x, segment + 1, ascent, paintBlack);
                        canvas.drawRect(segment*2, 0f, segment * 3 - 1, fontHeight, paintHalfWhite);
                        canvas.drawText("Y: " + ptInfo.y, segment * 2 + 1, ascent, paintBlack);
                    }else{
                        float xSeg = ptInfo.listX.get(listSize - 1).floatValue() - ptInfo.listY.get(0).floatValue();
                        float ySeg = ptInfo.listY.get(listSize - 1).floatValue() - ptInfo.listY.get(0).floatValue();
                        Paint paint = (Math.abs(xSeg) < viewConfiguration.getScaledTouchSlop()) ? paintHalfWhite : paintRed;
                        canvas.drawRect(segment, 0, (segment * 3 - 1), fontHeight, paint);
                        canvas.drawText("dX: " + String.format("%.1f", Float.valueOf(xSeg)), segment + 1, ascent, paintBlack);
                        paint = (Math.abs(ySeg) < viewConfiguration.getScaledTouchSlop()) ? paintHalfWhite : paintRed;
                        canvas.drawRect(segment * 2, 0f, segment * 3 - 1, fontHeight, paint);
                        canvas.drawText("dY: " + String.format("%.1f", Float.valueOf(ySeg)), segment * 2 + 1, ascent, paintBlack);
                    }

                    canvas.drawRect(segment * 3, 0f, segment * 4 - 1, fontHeight, paintHalfWhite);
                    int xV = (ptInfo.velocityTracker == null) ? 0 : (int)(ptInfo.velocityTracker.getXVelocity() * 1000f);
                    canvas.drawText("Xv: " + xV, segment * 3 + 1, ascent, paintBlack);
                    canvas.drawRect(segment * 4, 0f, segment * 5 - 1, fontHeight, paintHalfWhite);
                    int yV = (ptInfo.velocityTracker == null) ? 0 : (int)(ptInfo.velocityTracker.getYVelocity() * 1000f);
                    canvas.drawText("Yv: " + yV, segment * 4 + 1, ascent, paintBlack);
                    canvas.drawRect(segment * 5, 0f, segment * 6 - 1, fontHeight, paintHalfWhite);
                    canvas.drawRect(segment * 5, 0f, segment * 5 + ptInfo.pressure * segment - 1f, fontHeight, paintRed);
                    canvas.drawText("Prs: " + String.format("%.2f", Float.valueOf(ptInfo.pressure)), segment * 5 + 1, ascent, paintRed);
                    canvas.drawRect(segment * 6, 0f, width, fontHeight, paintHalfWhite);
                    canvas.drawRect(segment * 6, 0f, segment * 6 + ptInfo.size * segment - 1f, fontHeight, paintRed);
                    canvas.drawText("Size: " + String.format("%.2f", Float.valueOf(ptInfo.size)), segment * 6 + 1, ascent, paintBlack);
                }

                for(int i = 0; i < listSize; i++){
                    PointerInfo ptInfo = ptInfoList.get(i);
                    if(hasPoint && ptInfo.pressed){
                        canvas.drawLine(0f, ptInfo.y, getWidth(), ptInfo.y, paintBlue);
                        canvas.drawLine(ptInfo.x, 0f, ptInfo.x, getHeight(), paintBlue);
                        paintWhile.setARGB(255, 250, 255, 240);
                        canvas.drawPoint(ptInfo.x, ptInfo.y, paintWhile);
                        canvas.drawCircle(ptInfo.x, ptInfo.y, ptRadius, paintWhile);
                    }
                }
            }catch (Exception e){
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        paintBlack.getFontMetricsInt(fontMetricsInt);
        txHeight = fontMetricsInt.descent - fontMetricsInt.ascent + 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        synchronized (ptInfoList){
            try {
                int action = event.getAction();
                int listSize = ptInfoList.size();
                if (action == MotionEvent.ACTION_DOWN) {
                    for (int i = 0; i < listSize; i++) {
                        PointerInfo pointerInfo = ptInfoList.get(i);
                        pointerInfo.listX.clear();
                        pointerInfo.listY.clear();
                        pointerInfo.velocityTracker = VelocityTracker.obtain();
                        pointerInfo.pressed = false;
                    }

                    ptInfoList.get(event.getPointerId(0)).pressed = true;
                    maxCount = 0;
                    if (isShowLog) {
                        LLog.d("PointerLocationView->onTouchEvent Pointer 1:DOWN");
                    }
                }

                if ((action & 0xFF) == MotionEvent.ACTION_POINTER_DOWN) {
                    int id = event.getPointerId((action & 0xFF00) >> 8);
                    for (int i = listSize; i <= id; i++) {
                        PointerInfo pointerInfo = new PointerInfo();
                        pointerInfo.velocityTracker = VelocityTracker.obtain();
                        ptInfoList.add(pointerInfo);
                    }
                    PointerInfo ptInfo = ptInfoList.get(id);
                    ptInfo.velocityTracker = VelocityTracker.obtain();
                    ptInfo.pressed = true;
                    if (isShowLog) {
                        LLog.d("PointerLocationView->onTouchEvent Pointer " + (id + 1) + ": DOWN");
                    }
                }

                int ptCount = event.getPointerCount();
                hasPoint = (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) ? false : true;
                ptCurCount = hasPoint ? ptCount : 0;
                if (maxCount < ptCurCount) {
                    maxCount = ptCurCount;
                }

                for (int i = 0; i < ptCount; i++) {
                    int id = event.getPointerId(i);
                    PointerInfo ptInfo = ptInfoList.get(id);
                    ptInfo.velocityTracker.addMovement(event);
                    ptInfo.velocityTracker.computeCurrentVelocity(1);
                    int historySize = event.getHistorySize();
                    for (int j = 0; j < historySize; j++) {
                        if (isShowLog) {
                            LLog.d("PointerLocationView->onTouchEvent Pointer " + (id + 1) + ": (" + event.getHistoricalX(i, j) + ", " +
                                    event.getHistoricalY(i, j) + ") Prs=" + event.getHistoricalPressure(i, j) + " Size=" + event.getHistoricalSize(i, j));
                        }
                        ptInfo.listX.add(Float.valueOf(event.getHistoricalX(i, j)));
                        ptInfo.listY.add(Float.valueOf(event.getHistoricalY(i, j)));
                    }

                    if (isShowLog) {
                        LLog.d("PointerLocationView->onTouchEvent Pointer " + (id + 1) +
                                ": (" + event.getX(i) + ", " + event.getY(id) + ") Prs=" + event.getPressure(id) + " Size=" + event.getSize(id));
                    }

                    ptInfo.listX.add(Float.valueOf(event.getX(id)));
                    ptInfo.listY.add(Float.valueOf(event.getY(id)));
                    ptInfo.x = (int) event.getX(id);
                    ptInfo.y = (int) event.getY(id);
                    ptInfo.pressure = event.getPressure(id);
                    ptInfo.size = event.getSize(id);
                }

                if ((action & 0xFF) == MotionEvent.ACTION_POINTER_UP) {
                    int id = event.getPointerId((action & 0xFF00) >> 8);
                    PointerInfo ptInfo = ptInfoList.get(id);
                    ptInfo.listX.add(Float.valueOf(Float.NaN));
                    ptInfo.listY.add(Float.valueOf(Float.NaN));
                    ptInfo.pressed = false;
                    if (isShowLog) {
                        LLog.d("PointerLocationView->onTouchEvent Pointer " + (id + 1) + ": UP");
                    }
                }

                if (action == MotionEvent.ACTION_UP) {
                    for (int i = 0; i < ptCount; i++) {
                        int id = event.getPointerId(i);
                        PointerInfo ptInfo = ptInfoList.get(id);
                        if (ptInfo.pressed) {
                            ptInfo.pressed = false;
                            if (isShowLog) {
                                LLog.d("PointerLocationView->onTouchEvent Pointer " + (id + 1) + ": UP");
                            }
                        }
                    }
                }

                postInvalidate();
                return true;
            }catch(Exception e){
            }
        }
        return false;
    }
}
