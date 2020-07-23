package com.mxkj.yuanyintang.base.service

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

import com.mxkj.yuanyintang.base.service.receiver.WakeReceiver


/**
 * 灰色保活手法创建的Service进程
 */
class GrayService : Service() {

    override fun onCreate() {
        super.onCreate()
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, Notification())//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            val innerIntent = Intent(this, GrayInnerService::class.java)
            startService(innerIntent)
            startForeground(GRAY_SERVICE_ID, Notification())
        }
        //发送唤醒广播来促使挂掉的UI进程重新启动起来
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent()
        alarmIntent.action = WakeReceiver.GRAY_WAKE_ACTION
        val operation = PendingIntent.getBroadcast(this, WAKE_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ALARM_INTERVAL.toLong(), operation)

        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    class GrayInnerService : Service() {

        override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
            startForeground(GRAY_SERVICE_ID, Notification())
            stopSelf()
            return super.onStartCommand(intent, flags, startId)
        }

        override fun onBind(intent: Intent): IBinder? {
            throw UnsupportedOperationException("Not yet implemented")
        }

        override fun onDestroy() {
            super.onDestroy()
        }
    }

    companion object {

        private val TAG = GrayService::class.java.simpleName
        /**
         * 定时唤醒的时间间隔，5分钟
         */
        private val ALARM_INTERVAL = 5 * 60 * 1000
        private val WAKE_REQUEST_CODE = 6666

        private val GRAY_SERVICE_ID = -1001
    }
}
