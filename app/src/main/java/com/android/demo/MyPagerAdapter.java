package com.android.demo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by hewei on 2017/9/4.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = MyPagerAdapter.class.getSimpleName();

    private List<Fragment> dataList;

    public MyPagerAdapter(FragmentManager fm, List<Fragment> dataList) {
        super(fm);
        this.dataList = dataList;
    }

    @Override
    public CharSequence getPageTitle(int position) {    //选择性实现
        return dataList.get(position).getClass().getSimpleName();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return dataList.get(position);
    }
}
