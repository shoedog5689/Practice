package com.android.demo.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.demo.R;
import com.android.demo.util.FragmentUtils;
import com.android.demo.util.ShortCutUtils;
import com.android.demo.view.fragment.InfoListFragment;
import com.android.demo.view.fragment.TabsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
//    private TextView mTextMessage;
//    private CircleButton mCircleButton;
    private BottomNavigationView navigation;
    private List<Fragment> fragmentList = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final FragmentManager fm = getFragmentManager();
            final FragmentTransaction fTransaction = fm.beginTransaction();
            if (navigation.getSelectedItemId() != item.getItemId()) {  //重复点击当前选中项时，不做fragment切换
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        FragmentUtils.hideAllShowFragment(fragmentList.get(0));
                        return true;
                    case R.id.navigation_dashboard:
                        FragmentUtils.hideAllShowFragment(fragmentList.get(1));
                        return true;
                    case R.id.navigation_notifications:
                        ShortCutUtils.addShortcut(MainActivity.this, R.drawable.sticky_note);
                        return true;
                }
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
        initFirstFragment();
    }

    private void initFirstFragment() {
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
        //第一个fragment这里设置为InfoListFragment
        fragmentList.add((Fragment) new InfoListFragment());
        fragmentList.add((Fragment) new TabsFragment());
        // TODO: 2017/9/6 Add ohter fragments here.
        if (fragmentList.size() > 0) {
            if (!fragmentList.get(0).isAdded()) {
                FragmentUtils.addFragments(getSupportFragmentManager(), fragmentList, R.id.main_frame_layout, 0);
//                FragmentUtils.addFragment(getSupportFragmentManager(), fragmentList.get(0), R.id.main_frame_layout);
            }
        }
        setNavigationItemSelected(R.id.navigation_home);
    }

    private void setNavigationItemSelected(int resId) {
        if (navigation != null) {
            navigation.setSelectedItemId(resId);
        }
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
