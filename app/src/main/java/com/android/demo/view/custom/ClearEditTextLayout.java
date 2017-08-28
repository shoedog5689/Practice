package com.android.demo.view.custom;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.demo.R;

/**
 * Created by hewei on 2017/8/25.
 */

public class ClearEditTextLayout extends LinearLayout {
    private static final String TAG = ClearEditTextLayout.class.getSimpleName();

    private EditText editText;
    private ImageView clearIv;
    private TextView msgTv;

    public ClearEditTextLayout(Context context) {
        super(context);
    }

    public ClearEditTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        init();
        initListener();
    }

    private void init() {
        LinearLayout customInputLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.custom_edittext_layout, this);

        if (customInputLayout != null) {
            editText = (EditText) customInputLayout.findViewById(R.id.edit_text);
            clearIv = (ImageView) customInputLayout.findViewById(R.id.clear_iv);
            msgTv = (TextView) customInputLayout.findViewById(R.id.edit_msg_tv);
        }
    }

    private void initListener() {
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    clearIv.setVisibility(View.VISIBLE);
                    showMsg("提示信息！");
                }else {
                    clearIv.setVisibility(View.GONE);
                    hideMsg();
                }
            }
        });

        clearIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    public void showMsg(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            msgTv.setVisibility(VISIBLE);
            msgTv.setText(msg);
        }else {  // 传入的Msg为空，则隐藏改布局
            msgTv.setVisibility(GONE);
        }
    }

    public void hideMsg() {
        msgTv.setText(""); //先清空文字
        msgTv.setVisibility(GONE);
    }

}
