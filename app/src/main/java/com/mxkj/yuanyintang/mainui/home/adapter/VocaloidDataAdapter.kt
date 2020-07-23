package com.mxkj.yuanyintang.mainui.home.adapter

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.ImageView

import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.mainui.home.bean.HomeIndex
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.utils.string.StringUtils

import java.util.ArrayList
import java.util.concurrent.TimeUnit

class VocaloidDataAdapter(data: List<HomeIndex.ItemInfoListBean.CatemusicBean.CatemusicBeanX>) : BaseQuickAdapter<HomeIndex.ItemInfoListBean.CatemusicBean.CatemusicBeanX, BaseViewHolder>(R.layout.home_item_vocaloid, data) {

    internal var data: List<HomeIndex.ItemInfoListBean.CatemusicBean.CatemusicBeanX> = ArrayList()

    init {
        this.data = data
    }

    override fun getData(): List<HomeIndex.ItemInfoListBean.CatemusicBean.CatemusicBeanX> {
        return data
    }

    override fun convert(helper: BaseViewHolder, item: HomeIndex.ItemInfoListBean.CatemusicBean.CatemusicBeanX, position: Int) {
        val iv_music_cover = helper.getView<ImageView>(R.id.iv_music_cover)
        if (!TextUtils.isEmpty(item.imgpic_info?.link)) {
            ImageLoader.with(mContext)
                    .getSize(200, 200)
                    .override(120, 120)
                    .url(StringUtils.isEmpty(item.imgpic_info.link))
                    .into(iv_music_cover)
        } else {
            ImageLoader.with(mContext)
                    .res(R.drawable.logo_defaulft)
                    .into(iv_music_cover)
        }

        if (item.isPlaying) {
            helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_true))
        } else {
            helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_false))
        }

        if (null != MediaService.bean) {
            if (MediaService.bean!!.id == item.id) {
                if (MediaService.getMediaPlayer().isPlaying) {
                    helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_true))
                } else {
                    helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_false))
                }
            } else {
                helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_false))
            }
        } else {
            helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_false))
        }







        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.title))
        helper.setText(R.id.tv_audience_num, StringUtils.setNum(item.counts))
        helper.setText(R.id.tv_company_name, StringUtils.isEmpty(item.nickname))

        RxView.clicks(helper.getView<View>(R.id.layout_click))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe {
                   PlayCtrlUtil.play(mContext,item.id,0)
                }
        RxView.clicks(helper.getView<View>(R.id.iv_player))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe { PlayCtrlUtil.play(mContext, item.id, 0) }
    }
}
