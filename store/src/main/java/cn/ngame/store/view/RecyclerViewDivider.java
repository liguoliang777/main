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
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = leftRightSpace;
            outRect.right = centerSpace;
        } else if (size - 1 == parent.getChildLayoutPosition(view)) {
            outRect.right = leftRightSpace;
        } else {
            outRect.right = centerSpace;
        }
    }
}
