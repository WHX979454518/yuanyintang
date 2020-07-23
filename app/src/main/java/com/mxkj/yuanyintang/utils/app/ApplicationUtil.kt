package com.mxkj.yuanyintang.utils.app

import android.app.ActivityManager
import android.app.ActivityManager.RunningTaskInfo
import android.content.ComponentName
import android.content.Context

object ApplicationUtil {

    /**
     * 判断应用是否已经打开
     * @return
     */
    fun isOpenMain(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = manager.getRunningTasks(manager.runningAppProcesses.size) //获取当前正在运行的应用任务集合
        for (i in runningTasks.indices) {
            val taskInfo = runningTasks[i]
            val name = taskInfo.baseActivity  //
            val aname = name.className
            if (aname.contains("com.mxkj.yuanyintang")) {
                return true
            }
        }
        return false
    }

    /**
     * 获取本应用当前activity的名字
     * @param context
     * @return
     */
    fun theTopActivityName(context: Context): String {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //ComponentName cn = manager.getRunningTasks(1).get(0).topActivity;

        val runningTasks = manager.getRunningTasks(1000)
        val iterator = runningTasks.iterator()
        while (iterator.hasNext()) {
            val runningTaskInfo = iterator.next()
            val componentName = runningTaskInfo.topActivity
            val packageName = componentName.packageName
            if (packageName == "com.mxkj.yuanyintang") {
                return componentName.className
            }
        }
        //return cn.getClassName();
        return ""
    }

    /**
     * 把后台已经打开的应用切换回前台
     * @param context
     */
    fun switchBackAppToFront(context: Context) {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = manager.getRunningTasks(1000)
        val iterator = runningTasks.iterator()
        while (iterator.hasNext()) {
            val runningTaskInfo = iterator.next()
            val componentName = runningTaskInfo.topActivity
            val packageName = componentName.packageName
            if (packageName == context.packageName) {
                manager.moveTaskToFront(runningTaskInfo.id, 0)
            }
        }
    }

    /**
     * 判断应用是否在前台显示
     * @param context
     * @return
     */
    fun isAppFront(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = manager.getRunningTasks(1)
        if (runningTasks != null && !runningTasks.isEmpty()) {
            val componentName = runningTasks[0].topActivity
            val packageName = componentName.packageName
            if (packageName == context.packageName) {
                return true
            }
        }
        return false

    }
}
