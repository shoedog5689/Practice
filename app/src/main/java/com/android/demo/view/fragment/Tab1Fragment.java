package com.android.demo.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.demo.R;
import com.android.demo.view.custom.CommonDialogFragment;

/**
 * Created by hewei on 2017/9/4.
 */

public class Tab1Fragment extends Fragment {
    private static final String TAG = Tab1Fragment.class.getSimpleName();

    private View rootView;
    private Button singleBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_1_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initListener();
    }

    private void initView() {
        singleBtn = (Button) rootView.findViewById(R.id.single_btn_dialog);
    }

    private void initListener() {
        singleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialogFragment dialog = CommonDialogFragment.create();
                dialog.show(getFragmentManager(), "single");
            }
        });
    }
}
