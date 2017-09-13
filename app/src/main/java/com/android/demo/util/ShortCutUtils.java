package com.android.demo.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;

import com.android.demo.view.EditActivity;

/**
 * Created by hewei on 2017/9/8.
 */

public class ShortCutUtils {
    private static final String TAG = ShortCutUtils.class.getSimpleName();

    /**
     * 添加当前应用的桌面快捷方式
     *
     * Android ShortCut 快捷图标的创建，是根据
     *     BroadcastReceiver收到的Intent中
     *        getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT)
     *        getStringExtra(Intent.EXTRA_SHORTCUT_NAME)
     *        (通过Cursor来检索这两个值)
     *  || Launcher.EXTRA_SHORTCUT_DUPLICATE (即代码中的“duplicate”)
     *  这两个值来决定的
     *  （具体可以查看InstallShortcutReceiver和LauncherModel中的源码）
     *
     *
     * @param context
     */
    public static void addShortcut(Context context, int appIcon) {
        Intent shortcut = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");

//        Intent shortcutIntent = context.getPackageManager()
//                .getLaunchIntentForPackage(context.getPackageName());
        Intent i = new Intent();
        i.setAction("com.short.cut.action");
        i.addCategory("com.short.cut.category");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);
        // 获取当前应用名称
        String title = null;
        try {
            final PackageManager pm = context.getPackageManager();
            title = pm.getApplicationLabel(
                    pm.getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 快捷方式名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "T0T2");
        // 不允许重复创建（不一定有效）
        // {@link Launcher.EXTRA_SHORTCUT_DUPLICATE}
        shortcut.putExtra("duplicate", false);
        // 快捷方式的图标
        Parcelable iconResource = Intent.ShortcutIconResource.fromContext(context,
                appIcon);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);

        context.sendBroadcast(shortcut);
    }

    /**
     * 删除当前应用的桌面快捷方式
     *
     * @param context
     */
    public static void delShortcut(Context context) {
        Intent shortcut = new Intent(
                "com.android.launcher.action.UNINSTALL_SHORTCUT");

        // 获取当前应用名称
        String title = null;
        try {
            final PackageManager pm = context.getPackageManager();
            title = pm.getApplicationLabel(
                    pm.getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
        }
        // 快捷方式名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        Intent shortcutIntent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        context.sendBroadcast(shortcut);
    }

    /**
     * 判断当前应用在桌面是否有桌面快捷方式
     *
     * @param context
     */
    public static boolean hasShortcut(Context context) {
        boolean result = false;
        String title = null;
        try {
            final PackageManager pm = context.getPackageManager();
            title = pm.getApplicationLabel(
                    pm.getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String uriStr;
        if (android.os.Build.VERSION.SDK_INT < 8) {
            uriStr = "content://com.android.launcher.settings/favorites?notify=true";
        } else if (android.os.Build.VERSION.SDK_INT < 19) {
            uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
        } else {
            uriStr = "content://com.android.launcher3.settings/favorites?notify=true";
        }
        final Uri CONTENT_URI = Uri.parse(uriStr);
        final Cursor c = context.getContentResolver().query(CONTENT_URI, null,
                "title=?", new String[]{title}, null);
        if (c != null) {
            if (c.getCount() > 0) {
                result = true;
            }
            c.close();
        }
        return result;
    }

}
