package com.android.demo.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.android.demo.R;
import com.android.demo.view.fragment.FuncsFragment;
import com.android.demo.view.fragment.InfoListFragment;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
//    private TextView mTextMessage;
//    private CircleButton mCircleButton;
    private BottomNavigationView navigation;
    private InfoListFragment mInfoListFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final FragmentManager fm = getFragmentManager();
            final FragmentTransaction fTransaction = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (mInfoListFragment == null) {
                        mInfoListFragment = new InfoListFragment();
                        fTransaction.add(R.id.main_frame_layout, mInfoListFragment,
                                InfoListFragment.class.getSimpleName()).commit();
                    }else {
                        if (mInfoListFragment.isHidden()) {
                            fTransaction.show(mInfoListFragment).commit();
                        }
                    }
//                    if (!mInfoListFragment.isAdded()) {
//
//                    }
                    return true;
                case R.id.navigation_dashboard:
                    if (mInfoListFragment != null) {
                        if (mInfoListFragment.isAdded() && mInfoListFragment.isVisible()) {
                            Log.e(TAG, "isVisible():" + mInfoListFragment.isVisible());
                            getFragmentManager().beginTransaction().hide(mInfoListFragment).commit();
                        }
                    }
                    FuncsFragment funcsFragment = new FuncsFragment();
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListener();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //设置支持Toolbar
        setSupportActionBar(mToolbar);
        //隐藏标题栏
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //设置toolbar //设置导航图标一定要设置在setsupportactionbar后面才有用不然他会显示小箭头

//        mCircleButton = (CircleButton) findViewById(R.id.button);
//        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    private void setListener() {
        //底部导航栏的事件监听
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        mTextMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG, "mTextMessage onClick()");
////                View headView = getLayoutInflater().inflate(R.layout.header_view, (ViewGroup) recyclerView.getParent(), false);
////                adapter.addHeader(headView);
//
////                Intent i = new Intent(MainActivity.this, SecondActivity.class);
////                startActivity(i);
//
////                Intent i = new Intent(MainActivity.this, EditActivity.class);
////                startActivity(i);
//
//                Intent i = new Intent(MainActivity.this, DialogActivity.class);
//                startActivity(i);
//
////                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
////                builder.setTitle("转啊转啊转");
////                builder.setMessage("这是 android.support.v7.app.AlertDialog 中的样式");
////                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        Log.d(TAG, "");
////                    }
////                });
////                AlertDialog dialog = builder.create();
////                Window dialogWindow = dialog.getWindow();
////                dialog.show();
//
//
//            }
//        });
//
//        mCircleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCircleButton.startAnimationInOrder();
//            }
//        });
    }
}
