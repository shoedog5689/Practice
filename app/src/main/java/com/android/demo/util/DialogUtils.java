package com.android.demo.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by hewei on 2017/8/30.
 */

public class DialogUtils {

    public static void showSingleTitleBtnDialog(Context context, String title, final OnDialogBtnClickListener callback) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onClick(DialogInterface.BUTTON_POSITIVE);
            }
        });
        dialog.show();
    }

    /**
     * status 为 {@link DialogInterface.BUTTON_POSITIVE}
     *           {@link DialogInterface.BUTTON_NEGATIVE}
     *           {@link DialogInterface.BUTTON_NEUTRAL}
     * 之一，用于标识点击某个Button
     */
    public interface OnDialogBtnClickListener {
        void onClick(int status);
    }
}
