package cn.ngame.store.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import cn.ngame.store.adapter.VrVideoRvAdapter;

/**
 * 上滑加载更多
 * Created by zeng on 2016/7/18.
 */
public class LoadMoreRecyclerView extends RecyclerView {

    /**
     * 加载更多的监听
     */
    private LoadMoreListener mListener;
    /**
     * 标记是否正在加载更多，防止再次调用加载更多接口
     */
    private boolean mIsLoadingMore = false;
    /**
     * 是否允许加载更多
     */
    private boolean mIsFooterEnable = true;
    /**
     * 标记加载更多的position
     */
    private int mLoadMorePosition;

    private VrVideoRvAdapter mAdapter;

    public LoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始化-添加滚动监听
     * <p/>
     * 回调加载更多方法，前提是
     * <pre>
     *    1、有监听并且支持加载更多：null != mListener && mIsFooterEnable
     *    2、目前没有在加载，正在上拉（dy>0），当前最后一条可见的view是否是当前数据列表的最好一条--及加载更多
     * </pre>
     */
    private void init() {

        super.addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null != mListener && mIsFooterEnable && !mIsLoadingMore && dy > 0) {
                    int lastVisiblePosition = getLastVisiblePosition();
                    if (lastVisiblePosition + 1 == mAdapter.getItemCount()) {
                        setLoadingMore(true);
                        mLoadMorePosition = lastVisiblePosition;
                        mListener.onLoadMore();
                    }
                }
            }
        });
    }

    /**
     * 获取最后一条展示的位置
     * @return
     */
    private int getLastVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {

            position = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最大的位置
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }

    /**
     * 设置正在加载更多
     * @param loadingMore
     */
    public void setLoadingMore(boolean loadingMore) {
        this.mIsLoadingMore = loadingMore;
    }

    public void setmAdapter(VrVideoRvAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    /**
     * 设置加载更多的监听
     * @param listener
     */
    public void setLoadMoreListener(LoadMoreListener listener) {
        mListener = listener;
    }

    /**
     * 通知更多的数据已经加载
     * 每次加载完成之后添加了Data数据，用notifyItemRemoved来刷新列表展示，
     * 而不是用notifyDataSetChanged来刷新列表
     */
    public void notifyMoreFinish() {
        getAdapter().notifyItemRemoved(mLoadMorePosition);
        getAdapter().notifyDataSetChanged();
        mIsLoadingMore = false;
    }

    /**
     * 加载更多监听
     */
    public interface LoadMoreListener {
        void onLoadMore();
    }

}
