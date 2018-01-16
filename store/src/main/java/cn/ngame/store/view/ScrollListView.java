package cn.ngame.store.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义listView处理 scrollview嵌套listview的冲突
 * Created by zeng on 2016/5/30.
 */
public class ScrollListView extends ListView{

    public ScrollListView(Context context) {
        super(context);
    }

    public ScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
