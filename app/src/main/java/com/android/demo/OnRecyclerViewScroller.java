package com.android.demo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by hewei on 2017/8/21.
 */

public abstract class OnRecyclerViewScroller extends RecyclerView.OnScrollListener {
    private final String TAG = OnRecyclerViewScroller.class.getSimpleName();

    private RecyclerView.LayoutManager mLayoutManager;

    private int totalCount;  //总数据量
    private int visibleCount;  //屏幕可见数据量

    private boolean scrollState = false; //是否滚动

    /**
     * 提供一个抽闲方法，在Activity中监听到这个EndLessOnScrollListener
     * 并且实现这个方法
     * */
    public abstract void onLoadMore();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleCount = recyclerView.getChildCount();
        totalCount = mLayoutManager.getItemCount();
        int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();

//        if (lastVisibleItem >= totalCount - 4 && dy > 0) {
//            onLoadMore();
//        }

        if (!recyclerView.canScrollVertically(1)) { //滚动到底部
            onLoadMore();
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        switch (newState) {
            case 0:
                scrollState = false;
                break;
            case 1:
                scrollState = true;
                break;
            case 2:
                scrollState = true;
                break;
        }
    }

    public OnRecyclerViewScroller(RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }



}
