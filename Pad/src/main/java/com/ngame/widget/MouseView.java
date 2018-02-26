package com.ngame.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import com.lx.pad.util.LLog;

/**
 * Created by Administrator on 2017/12/6.
 */

public class MouseView extends View {
    public float m_x;
    public float m_y;
    private Bitmap m_bitmap;

    public MouseView(Context context, Bitmap bmp) {
        super(context);
        m_bitmap = bmp;
    }

    public float getPointerX(){return m_x;}
    public float getPointerY(){return m_y;}
    public float getXmax(){return (float)(getWidth() - m_bitmap.getWidth());}
    public float getYmax(){return (float)(getHeight() - m_bitmap.getHeight());}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LLog.d("MouseView->onDraw m_x:" + m_x + " m_y:" + m_y + " ..............................");
        if(m_x < 0f && m_y < 0f){
            m_x = (float)((canvas.getWidth() - m_bitmap.getWidth())/2);
            m_y = (float)((canvas.getHeight() - m_bitmap.getHeight())/2);
        }
        canvas.drawBitmap(m_bitmap, m_x, m_y, null);
    }
}
