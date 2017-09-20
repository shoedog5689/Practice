package com.android.demo.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.demo.R;
import com.android.demo.view.adapter.MyRecyclerViewAdapter;
import com.android.demo.view.callback.OnRecyclerViewScroller;
import com.android.demo.view.custom.CustomScrollRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hewei on 2017/9/19.
 */

public class RefreshFragment extends Fragment {
    private static final String TAG = RefreshFragment.class.getSimpleName();

    private View rootView;
    private CustomScrollRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private MyRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_refresh, container, false);
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

    private void initView() {
        refreshLayout = (CustomScrollRefreshLayout) rootView.findViewById(R.id.custom_refresh_layout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        /**
         * use this setting to improve performance if you know that changes
         * in content do not change the layout size of the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);
        //set LayoutManager
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        //set Adapter
        adapter = new MyRecyclerViewAdapter(getData(), getActivity());
        mRecyclerView.setAdapter(adapter);
    }

    private void initListener() {
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mRecyclerView.addOnScrollListener(new OnRecyclerViewScroller(mLinearLayoutManager) {
            @Override
            public void onLoadMore() {
                Log.e(TAG, "onLoadMore()");

                setCanLoad(false);

                /*
                 * 加载更多时候，先ItemInsert一个loading item，加载成功后再去除，然后刷新adapter
                 */
                final View loadingView = getActivity().getLayoutInflater().inflate(R.layout.loading_view,
                        (ViewGroup) mRecyclerView.getParent(), false);
                mRecyclerView.post(new Runnable() {  // Avoid warning when update while measure.
                    public void run() {
                        adapter.addLoadingItem(loadingView);
                        mRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);  //自动弹出底部加载框
                    }
                });

                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        mRecyclerView.post(new Runnable() {
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

    public List getData() {
        List list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        return list;
    }

}
