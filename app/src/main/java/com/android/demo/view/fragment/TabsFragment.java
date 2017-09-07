package com.android.demo.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.demo.R;
import com.android.demo.view.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hewei on 2017/9/5.
 */

public class TabsFragment extends Fragment {
    private static final String TAG = TabsFragment.class.getSimpleName();

    private View rootView;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tabs, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        mViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), dataList));

        //TabLayout绑定ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
