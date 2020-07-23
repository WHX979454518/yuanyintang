package com.mxkj.yuanyintang.mainui.comment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_SUCCESS
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_SUCCESS_JSON

class CommentSuccessReceiver( private val callBack: SuccessCallBack) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action != null && action == COMMENT_SUCCESS) {
            val json = intent.getStringExtra(COMMENT_SUCCESS_JSON)
            callBack.callBack(json, intent)
        }
    }

    interface SuccessCallBack {
        fun callBack(json: String, intent: Intent)
    }
}
