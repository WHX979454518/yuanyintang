package com.mxkj.yuanyintang.utils.app

import android.app.Activity

import java.util.ArrayList

object ActivityCollector {
    var activities: MutableList<Activity> = ArrayList()

    fun addActivity(activity: Activity) {
        if (!activities.contains(activity)) {
            activities.add(activity)
            if (activities.size > 5) {
                for (i in 0..3) {
                    activities[i].finish()
                }
            }
        }
    }

    fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    fun finishAll() {
        for (activity in activities) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
    }
}
