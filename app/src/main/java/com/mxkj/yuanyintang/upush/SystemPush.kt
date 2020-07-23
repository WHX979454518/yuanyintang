package com.mxkj.yuanyintang.upush

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils

import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.mainui.home.activity.LikesMusicActivity
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.mainui.message.activity.MessageCenterActivity
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew
import com.mxkj.yuanyintang.mainui.web.CommonWebview
import com.umeng.message.UmengNotifyClickActivity

import org.android.agoo.common.AgooConstants

class SystemPush : UmengNotifyClickActivity() {
    private var url: String? = null
    private var type: String? = null
    private var id: String? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_welcome)
    }

    override fun onMessage(intent: Intent) {
        super.onMessage(intent)
        val body = intent.getStringExtra(AgooConstants.MESSAGE_BODY)
        val jsonObject = JSON.parseObject(body)
        val customObj = jsonObject.getJSONObject("body").getJSONObject("custom")
        id = customObj.getString("id")
        type = customObj.getString("type")
        url = customObj.getString("url")
        msgClick()
    }

    private fun msgClick() {
        startActivity(Intent(this, HomeActivity::class.java))
        if (type != null && type == "page") {
            val bundle = Bundle()
            if (TextUtils.isEmpty(url)) {
                return
            }
            when (url) {
                "msgList" -> goActivity(MessageCenterActivity::class.java, null)
                "topicDetails" -> if (!TextUtils.isEmpty(id)) {
                    bundle.putInt(PondDetialActivityNew.PID, Integer.parseInt(id))
                    goActivity(PondDetialActivityNew::class.java, bundle)
                }
                "musicDetails" -> {
//                    bundle.putString(MusicDetailsActivity.MUSIC_ID, id)
//                    goActivity(MusicDetailsActivity::class.java, bundle)
                }
                "musicianDetailHome" -> {
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 0)
                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                }
                "musicianDetailMusic" -> {
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 1)
                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                }
                "musicianDetailSongSheet" -> {
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 2)
                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                }
                "musicianDetailTopic" -> {
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 3)
                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                }
                "songSheetDetails" -> {
                    bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, id)
                    goActivity(SongSheetDetailsActivity::class.java, bundle)
                }
                "likesSongSheetDetails" -> {
                    bundle.putString(LikesMusicActivity.MUSICIAN_ID, id)
                    goActivity(LikesMusicActivity::class.java, bundle)
                }
            }
        } else if (type != null && type == "activity") {
            val inten = Intent(this, CommonWebview::class.java)
            inten.putExtra("url", url)
            inten.putExtra("activity", "activity")
            startActivity(inten)
        }
        finish()
    }

    fun goActivity(mClass: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, mClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

}