package com.android.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.demo.view.custom.CircleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    private TextView mTextMessage;
    private CircleButton mCircleButton;

    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCircleButton = (CircleButton) findViewById(R.id.button);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        /**
         * use this setting to improve performance if you know that changes
         * in content do not change the layout size of the RecyclerView
         */
        recyclerView.setHasFixedSize(true);
        //set LayoutManager
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        //set Adapter
        adapter = new MyRecyclerViewAdapter(getData(), this);
        recyclerView.setAdapter(adapter);

        //SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.grid_swipe_refresh);
        //调整SwipeRefreshLayout的位置
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        setListener();
    }

    private void setSwipeRefreshing(final boolean status) {
        runOnUiThread(new Runnable() {
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

    private void setListener() {
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

        mTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "mTextMessage onClick()");
//                View headView = getLayoutInflater().inflate(R.layout.header_view, (ViewGroup) recyclerView.getParent(), false);
//                adapter.addHeader(headView);

                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(i);

//                Intent i = new Intent(MainActivity.this, EditActivity.class);
//                startActivity(i);

//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("转啊转啊转");
//                builder.setMessage("这是 android.support.v7.app.AlertDialog 中的样式");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d(TAG, "");
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                Window dialogWindow = dialog.getWindow();
//                dialog.show();


            }
        });

        mCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCircleButton.startAnimationInOrder();
            }
        });

        recyclerView.addOnScrollListener(new OnRecyclerViewScroller(mLinearLayoutManager) {
            @Override
            public void onLoadMore() {
                Log.e(TAG, "onLoadMore()");

                setCanLoad(false);

                /**
                 * 加载更多时候，先ItemInsert一个loading item，加载成功后再去除，然后刷新adapter
                 */
                final View loadingView = getLayoutInflater().inflate(R.layout.loading_view, (ViewGroup) recyclerView.getParent(), false);
                recyclerView.post(new Runnable() {  // Avoid warning when update while measure.
                    public void run() {
                        adapter.addLoadingItem(loadingView);
//                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);  //自动弹出底部加载框
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
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

}
