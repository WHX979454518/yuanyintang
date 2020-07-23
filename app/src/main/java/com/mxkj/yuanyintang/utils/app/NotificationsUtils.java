package com.mxkj.yuanyintang.utils.app;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by LiuJie on 2018/3/13.
 */

public class NotificationsUtils {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    @SuppressLint("NewApi")
    public static boolean isNotificationEnabled(Context context) {
//        19以下没用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                // 进入设置系统应用权限界面
                AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                ApplicationInfo appInfo = context.getApplicationInfo();
                String pkg = context.getApplicationContext().getPackageName();
                int uid = appInfo.uid;

                Class appOpsClass = null;
      /* Context.APP_OPS_MANAGER */
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                        String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public static void requestPermission(int requestCode, Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
                // 进入设置系统应用权限界面
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
                return;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
                // 进入设置系统应用权限界面
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
                return;
            }
        } catch (RuntimeException e) {
        }

        return;
    }
}
