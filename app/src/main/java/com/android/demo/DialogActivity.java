package com.android.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hewei on 2017/8/30.
 */

public class DialogActivity extends AppCompatActivity {
    private static final String TAG = DialogActivity.class.getSimpleName();

    private Button singleBtn;
    private Button doubleBtn;
    private Button singleCallbackBtn;
    private Button testBtn;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> dataList;

    {
        Tab1Fragment fragment1 = new Tab1Fragment(); //新建Fragment
        Tab2Fragment fragment2 = new Tab2Fragment();
        dataList = new ArrayList<>();
        dataList.add(fragment1);
        dataList.add(fragment2);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        getFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        initView();
        initListener();
    }

    private void initView() {
//        singleBtn = (Button) findViewById(R.id.single_btn_dialog);
//        doubleBtn = (Button) findViewById(R.id.double_btn_dialog);
//        singleCallbackBtn = (Button) findViewById(R.id.single_callback_btn_dialog);
//        testBtn = (Button) findViewById(R.id.test_btn);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), dataList));

        //TabLayout绑定ViewPager
        mTabLayout.setupWithViewPager(mViewPager);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //设置支持Toolbar
        setSupportActionBar(mToolbar);
        //隐藏标题栏
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //设置toolabar //设置导航图标一定要设置在setsupportactionbar后面才有用不然他会显示小箭头
    }

    private void initListener() {
//        singleBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogUtils.showSingleTitleBtnDialog(DialogActivity.this, "单个按钮带标题", new DialogUtils.OnDialogBtnClickListener() {
//
//                    @Override
//                    public void onClick(int status) {
//                        switch (status) {
//                            case DialogInterface.BUTTON_POSITIVE:
//                                Log.d(TAG, "Clicked POSITIVE.");
//                                Toast.makeText(DialogActivity.this, "Clicked POSITIVE.", Toast.LENGTH_LONG).show();
//                                break;
//                        }
//                    }
//                });
//            }
//        });
//
//        testBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Snackbar.make().show();
//            }
//        });
    }

}
