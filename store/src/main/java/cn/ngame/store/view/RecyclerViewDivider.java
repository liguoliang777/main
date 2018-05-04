package cn.ngame.store.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * 图片轮播控件
 *
 * @author liguoliang
 * @since 2017/7/21
 */
public class RecyclerViewDivider extends RecyclerView.ItemDecoration {

    private int centerSpace;
    private int leftRightSpace;
    private int size;

    public RecyclerViewDivider(Context context, int leftRightSpace, int centerSpace, int size) {
        Resources resources = context.getResources();
        this.leftRightSpace = resources.getDimensionPixelSize(leftRightSpace);
        this.centerSpace = resources.getDimensionPixelSize(centerSpace);
        this.size = size;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        //由于每行都只有2个，所以第一个都是2的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.right = 0;
            outRect.left = leftRightSpace;
        } else if (parent.getChildLayoutPosition(view) % 2 == 1) {
            outRect.left = centerSpace;
            outRect.right = leftRightSpace;
        }
    }
}
