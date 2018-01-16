package cn.ngame.store.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import cn.ngame.store.core.utils.Log;

/**
 * 处理与scrollview嵌套
 * Created by zeng on 2016/6/7.
 */
public class ScrollGridView extends GridView {

    public ScrollGridView(Context context) {
        super(context);
    }

    public ScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.e("ScrollGridView","===================>>> 我在测量");
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
