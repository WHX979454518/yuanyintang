package com.mxkj.yuanyintang.base.service.receiver

import android.app.Notification
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

class WakeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (GRAY_WAKE_ACTION == action) {
            val wakeIntent = Intent(context, WakeNotifyService::class.java)
            context.startService(wakeIntent)
        }
    }

    /**
     * 用于其他进程来唤醒UI进程用的Service
     */
    class WakeNotifyService : Service() {
        override fun onCreate() {
            Log.i(TAG, "WakeNotifyService->onCreate")
            super.onCreate()
        }

        override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
            Log.i(TAG, "WakeNotifyService->onStartCommand")
            if (Build.VERSION.SDK_INT < 18) {
                startForeground(WAKE_SERVICE_ID, Notification())//API < 18 ，此方法能有效隐藏Notification上的图标
            } else {
                val innerIntent = Intent(this, WakeGrayInnerService::class.java)
                startService(innerIntent)
                startForeground(WAKE_SERVICE_ID, Notification())
            }
            return Service.START_STICKY
        }

        override fun onBind(intent: Intent): IBinder? {
            throw UnsupportedOperationException("Not yet implemented")
        }

        override fun onDestroy() {
            Log.i(TAG, "WakeNotifyService->onDestroy")
            super.onDestroy()
        }
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    class WakeGrayInnerService : Service() {

        override fun onCreate() {
            Log.i(TAG, "InnerService -> onCreate")
            super.onCreate()
        }

        override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
            Log.i(TAG, "InnerService -> onStartCommand")
            startForeground(WAKE_SERVICE_ID, Notification())
            //stopForeground(true);
            stopSelf()
            return super.onStartCommand(intent, flags, startId)
        }

        override fun onBind(intent: Intent): IBinder? {
            throw UnsupportedOperationException("Not yet implemented")
        }

        override fun onDestroy() {
            Log.i(TAG, "InnerService -> onDestroy")
            super.onDestroy()
        }
    }

    companion object {

        private val TAG = WakeReceiver::class.java.simpleName
        private val WAKE_SERVICE_ID = -1111
        val GRAY_WAKE_ACTION = "com.wake.gray"
    }
}
