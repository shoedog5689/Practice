package com.android.demo.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

/**
 * Created by hewei on 2017/8/28.
 */

public class CommonPromptDialog extends Dialog {
    private static final String TAG = CommonPromptDialog.class.getSimpleName();

    public CommonPromptDialog(@NonNull Context context) {
        super(context);
    }

    public CommonPromptDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected CommonPromptDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
