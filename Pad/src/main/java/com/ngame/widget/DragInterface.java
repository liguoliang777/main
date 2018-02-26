package com.ngame.widget;

import android.widget.ImageView;

/**
 * Created by Administrator on 2017/12/5.
 */

public interface DragInterface {
    void onDragFinish(ImageView v);
    void onDragMove(ImageView v);
    void onDragStart(ImageView v);
}
