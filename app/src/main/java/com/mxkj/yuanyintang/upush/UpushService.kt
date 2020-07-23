package com.mxkj.yuanyintang.upush

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.RemoteViews

import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.home.activity.LikesMusicActivity
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.mainui.message.activity.MessageCenterActivity
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew
import com.mxkj.yuanyintang.mainui.web.CommonWebview
import com.mxkj.yuanyintang.net.NetWork
import com.umeng.message.UmengMessageService

import org.android.agoo.common.AgooConstants

import okhttp3.Headers

class UpushService : UmengMessageService() {
    private val message: String? = null//推送来的json数据
    private var notification: Notification? = null
    private var receiver: NotificationReceiver? = null
    private var filter: IntentFilter? = null
    private var text: String? = null
    private var title: String? = null
    private var contentView: RemoteViews? = null

    override fun onMessage(context: Context, intent: Intent) {
        receiver = NotificationReceiver()
        filter = IntentFilter("uPushClick")
        val message = intent.getStringExtra(AgooConstants.MESSAGE_BODY)
        Log.e(TAG, "onMessage: $message")
        showMessageNotification(context, message)
    }

    /**
     * 处理收到的推送  创建通知
     */
    private fun showMessageNotification(context: Context, messageReceiver: String?) {
        if (!TextUtils.isEmpty(messageReceiver)) {
            try {
                if (messageReceiver != null) {
                    val jsonObject = JSON.parseObject(messageReceiver)
                    msgId = jsonObject.getString("msg_id")
                    val customObj = jsonObject.getJSONObject("body").getJSONObject("custom")
                    id = customObj.getString("id")
                    text = customObj.getString("text")
                    type = customObj.getString("type")
                    title = customObj.getString("title")
                    url = customObj.getString("url")
                    nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.logo)
                    contentView = RemoteViews(packageName,
                            R.layout.notification_upush)
                    val mNotifyBuilder = Notification.Builder(applicationContext)
                    val intentClick = Intent("uPushClick")
                    val clickPendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intentClick, 0)
                    notification = mNotifyBuilder
                            .setSmallIcon(R.drawable.logo)
                            .setLargeIcon(bitmap)
                            .setContentTitle(title)
                            .setContentText(text)
                            .setAutoCancel(true)
                            .setContentIntent(clickPendingIntent)
                            .build()
                    val contentView = RemoteViews(packageName, R.layout.notification_upush)
                    notification!!.bigContentView = contentView
                    notification!!.contentView = contentView
                    contentView.setOnClickPendingIntent(R.id.up_notification_layout, clickPendingIntent)
                    contentView.setTextViewText(R.id.notification_title, title)
                    contentView.setTextViewText(R.id.notification_text, text)
                    nm!!.notify(10010, notification)
                }
            } catch (e: RuntimeException) {
            }

        }
    }

    /**
     * 点击通知栏后的动作
     */
    class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            Log.e(TAG, "通知栏点击1: ")
            if (!TextUtils.isEmpty(msgId)) {
                NetWork.pushCount(context, msgId!!, object : NetWork.TokenCallBack {
                    override fun doNext(resultData: String, headers: Headers?) {

                    }

                    override fun doError(msg: String) {

                    }

                    override fun doResult() {

                    }
                })
            }
            if (intent != null) {
                if (intent.action == "uPushClick") {
                    Log.e(TAG, "通知栏点击: ")
                    //判断app进程是否存活
                    msgClickGo(context)
                }
            }
        }
    }

    companion object {
        private val TAG = UpushService::class.java.name
        private var url: String? = null
        private var type: String? = null
        private var id: String? = null
        private var nm: NotificationManager? = null
        private var msgId: String? = null

        /**
         * TODO  抽出来，banner、app唤醒后跳转、弹窗点击的跳转、启动广告跳转、CommentsListActivity跳转
         */
        private fun msgClickGo(context: Context) {
            if (nm != null) {
                nm!!.cancel(10010)
            }
            if (type != null && type == "page") {
                val bundle = Bundle()
                when (url) {
                    "msgList" -> goActivity(MessageCenterActivity::class.java, null, context)
                    "home" -> {
                    }
                    "topicDetails" -> if (!TextUtils.isEmpty(id)) {
                        bundle.putInt(PondDetialActivityNew.PID, Integer.parseInt(id))
                        goActivity(PondDetialActivityNew::class.java, bundle, context)
                    }
                    "musicDetails" -> {
//                        bundle.putString(MusicDetailsActivity.MUSIC_ID, id)
//                        goActivity(MusicDetailsActivity::class.java, bundle, context)
                    }
                    "musicianDetailHome" -> {
                        bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                        bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 0)
                        goActivity(MusicIanDetailsActivity::class.java, bundle, context)
                    }
                    "musicianDetailMusic" -> {
                        bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                        bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 1)
                        goActivity(MusicIanDetailsActivity::class.java, bundle, context)
                    }
                    "musicianDetailSongSheet" -> {
                        bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                        bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 2)
                        goActivity(MusicIanDetailsActivity::class.java, bundle, context)
                    }
                    "musicianDetailTopic" -> {
                        bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                        bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 3)
                        goActivity(MusicIanDetailsActivity::class.java, bundle, context)
                    }
                    "songSheetDetails" -> {
                        bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, id)
                        goActivity(SongSheetDetailsActivity::class.java, bundle, context)
                    }
                    "likesSongSheetDetails" -> {
                        bundle.putString(LikesMusicActivity.MUSICIAN_ID, id)
                        goActivity(LikesMusicActivity::class.java, bundle, context)
                    }
                }
            } else if (type != null && type == "activity") {
                val intent = Intent(context, CommonWebview::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("url", url)
                intent.putExtra("activity", "activity")
                context.startActivity(intent)
            }
        }

        fun goActivity(mClass: Class<*>, bundle: Bundle?, context: Context) {
            val intent = Intent(context, mClass)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }

}