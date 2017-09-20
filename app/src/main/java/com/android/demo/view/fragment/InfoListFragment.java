package com.android.demo.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.demo.R;
import com.android.demo.view.adapter.MyRecyclerViewAdapter;
import com.android.demo.view.callback.OnRecyclerViewScroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hewei on 2017/9/5.
 */

public class InfoListFragment extends Fragment {
    private static final String TAG = InfoListFragment.class.getSimpleName();

    private View rootView;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyRecyclerViewAdapter adapter;

    private void initView() {
        //RecyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        /**
         * use this setting to improve performance if you know that changes
         * in content do not change the layout size of the RecyclerView
         */
        recyclerView.setHasFixedSize(true);
        //set LayoutManager
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        //set Adapter
        adapter = new MyRecyclerViewAdapter(getData(), getActivity());
        recyclerView.setAdapter(adapter);
        //SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.grid_swipe_refresh);
        //调整SwipeRefreshLayout的位置
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    private void initListener() {
        //swipeRefreshLayout刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh()");
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        setSwipeRefreshing(false);
                    }
                };
                Timer timer = new Timer(true);
                timer.schedule(timerTask, 2000);
            }
        });

        recyclerView.addOnScrollListener(new OnRecyclerViewScroller(mLinearLayoutManager) {
            @Override
            public void onLoadMore() {
                Log.e(TAG, "onLoadMore()");

                setCanLoad(false);

                /*
                 * 加载更多时候，先ItemInsert一个loading item，加载成功后再去除，然后刷新adapter
                 */
                final View loadingView = getActivity().getLayoutInflater().inflate(R.layout.loading_view,
                        (ViewGroup) recyclerView.getParent(), false);
                recyclerView.post(new Runnable() {  // Avoid warning when update while measure.
                    public void run() {
                        adapter.addLoadingItem(loadingView);
                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);  //自动弹出底部加载框
                    }
                });

                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {  //两秒后删除loading item
                                adapter.removeLoadingItem();
                                adapter.updateData();
                                setCanLoad(true);   //设置可以Loading
                            }
                        });
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask, 2000);
            }

            @Override
            public void onGetTop(boolean isTop) {

            }
        });
    }

    private void setSwipeRefreshing(final boolean status) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(status);
            }
        });
    }

    public List getData() {
        List list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        return list;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_info_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (rootView != null) {
            initView();
            initListener();
        }
    }
}
