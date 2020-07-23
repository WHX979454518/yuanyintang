package com.mxkj.yuanyintang.mainui.home.adapter

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.INVISIBLE
import android.widget.CheckBox
import android.widget.TextView
import com.alibaba.fastjson.JSON

import com.jakewharton.rxbinding2.view.RxView
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.R.id.nickName
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.bean.MusicListMusicIanBean
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.newapp.pond.PondInfo
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.musicplayer.service.MediaService.bean
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.string.StringUtils
import java.util.concurrent.TimeUnit

import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer
import okhttp3.Headers
import java.io.File

class MusicListMusicIanAdapter(data: List<MusicListMusicIanBean.DataBean>) : BaseQuickAdapter<MusicListMusicIanBean.DataBean, BaseViewHolder>(R.layout.item_musiclist_musician, data) {
    override fun convert(helper: BaseViewHolder, item: MusicListMusicIanBean.DataBean, position: Int) {
        isPlayView(helper, item)
//        if (MediaService.mediaPlayer != null && MediaService.mediaPlayer.isPlaying) {
//            if (bean != null && bean?.id == item.id) {
//                item.isPlaying = true
//                val tv_musicname = helper.getView<TextView>(R.id.tv_musicname)
//                tv_musicname.setTextColor(Color.parseColor("#ff6699"))
//            }
//        }
        ImageLoader.with(mContext)
                .getSize(220, 220)
                .url(item.head_link)
                .format(1)
                .scale(ScaleMode.CENTER_CROP)
                .into(helper.getView<CircleImageView>(R.id.cimg_cover))
        helper.getView<TextView>(R.id.tv_musicname).text = StringUtils.isEmpty("《"+item.music.title+"》")
        helper.setOnClickListener(R.id.tv_musicname, {
            if (MediaService.bean != null && MediaService.bean?.id == item.id) {
                if (MediaService.mediaPlayer != null) {
                    val playing = MediaService.mediaPlayer.isPlaying
                    data[position].isPlaying = !playing
                    mContext.sendBroadcast(Intent(MediaService.ACTION_PAUSE))
                } else {
                    data[position].isPlaying = true
                    PlayCtrlUtil.play(mContext, item.music.item_id, 0)
                }
            } else {
                for (i in 0 until data.size) {
                    if (i == position) {
                        data[i].isPlaying = true
                        MediaService.bean = null
                    } else {
                        data[i].isPlaying = false
                    }
                }
                PlayCtrlUtil.play(mContext, item.music.item_id, 0)
            }
            setNewData(data)

        })
        if (item.music == null || item.music.title == null) {
            helper.getView<TextView>(R.id.tv_musicname).visibility = INVISIBLE
        }
        helper.setText(R.id.tv_music_nickname, StringUtils.isEmpty(item.nickname))
        helper.setText(R.id.tv_fans_num, item.fans_num.toString() + "粉丝")
//        when {
//            item.sex == 1 -> helper.setTextColor(R.id.tv_musicname, ContextCompat.getColor(mContext, R.color.male_blue))
//            item.sex == 0 -> helper.setTextColor(R.id.tv_musicname, ContextCompat.getColor(mContext, R.color.female_pink))
//            else -> helper.setTextColor(R.id.tv_music_nickname, ContextCompat.getColor(mContext, R.color.color_17_text))
//        }
        if (item.is_music == 3) {
            helper.setVisible(R.id.iv_is_vip, true)
        } else {
            helper.setVisible(R.id.iv_is_vip, false)
        }

        RxView.clicks(helper.getView<View>(R.id.layout_click))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, item.id.toString() + "")
                    intent.putExtras(bundle)
                    mContext.startActivity(intent)
                }
        follow(helper, item)
    }

    private fun isPlayView(helper: BaseViewHolder, item: MusicListMusicIanBean.DataBean) {
        val tv_musicname = helper.getView<TextView>(R.id.tv_musicname)
        if (item.isPlaying) {
//            tv_musicname.setTextColor(Color.parseColor("#f65f6c"))
            tv_musicname.setTextColor(Color.parseColor("#FF6699"))
        } else {
//            tv_musicname.setTextColor(Color.parseColor("#1a1717"))
            tv_musicname.setTextColor(Color.parseColor("#4CBEFF"))
        }
    }

    private fun follow(helper: BaseViewHolder, detialBean: MusicListMusicIanBean.DataBean) {
        val checkBox = helper.getView<CheckBox>(R.id.ck_follow)
        if (detialBean.is_relation == 1 || detialBean.is_relation == 2) {
            checkBox.isChecked = true
            checkBox.text = "已关注"
        } else {
            checkBox.text = "+关注"
            checkBox.isChecked = false

        }
        checkBox.setOnTouchListener { _, event ->
            when {
                event.action == MotionEvent.ACTION_DOWN -> if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN)) {
                    checkBox.isChecked = !checkBox.isChecked
                    when {
                        checkBox.isChecked -> checkBox.text = "已关注"
                        else -> checkBox.text = "+关注"
                    }
                    val params = HttpParams()
                    params.put("id", detialBean.id.toString() + "")
                    NetWork.follow(mContext, params, object : NetWork.TokenCallBack {
                        override fun doNext(resultData: String, headers: Headers?) {
                            val jObj = JSON.parseObject(resultData)
                            val code = jObj.getInteger("code")
                            when (code) {
                                200 -> when {
                                    detialBean.is_relation == 0 && detialBean.is_relation == 0 -> {
                                        checkBox.text = "已关注"
                                        detialBean.is_relation = 1
                                        helper.setText(R.id.tv_fans_num, (detialBean.fans_num + 1).toString() + "粉丝")

                                    }
                                    detialBean.is_relation == 1 -> {
                                        checkBox.text = "+关注"
                                        detialBean.is_relation = 0
                                        helper.setText(R.id.tv_fans_num, (detialBean.fans_num - 1).toString() + "粉丝")
                                    }
                                }
                            }
                        }

                        override fun doError(msg: String) {
                            notifyItemChanged(helper.adapterPosition)
                        }

                        override fun doResult() {

                        }
                    })
                } else {
                    mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
                }
            }
            true
        }




        checkBox.setOnCheckedChangeListener { _, isChecked ->

        }
    }
}
