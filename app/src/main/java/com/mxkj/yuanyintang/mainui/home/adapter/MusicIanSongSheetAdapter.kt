package com.mxkj.yuanyintang.mainui.home.adapter

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View

import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.mainui.home.activity.LikesMusicActivity
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.mainui.home.bean.MusicIanSongSheetBean
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.utils.string.StringUtils
import java.util.concurrent.TimeUnit

import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer

class MusicIanSongSheetAdapter(data: List<MusicIanSongSheetBean.DataBean>, private val id: String) : BaseQuickAdapter<MusicIanSongSheetBean.DataBean, BaseViewHolder>(R.layout.home_item_recommend_music, data) {
    private var isAddCount: Boolean = true
    override fun convert(helper: BaseViewHolder, item: MusicIanSongSheetBean.DataBean, position: Int) {
        if (item.collection!!) {
            helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_collection_white))
        } else {
            if (null != MediaService.bean) {
                if (MediaService.bean!!.song_id == item.id) {
                    if (MediaService.getMediaPlayer().isPlaying) {
                        helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_big_true))
                    } else {
                        helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_big_false))
                    }
                } else {
                    helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_big_false))
                }
            } else {
                helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_big_false))
            }
        }
        ImageLoader.with(mContext)
                .override(120, 120)
                .getSize(400, 400)
                .url(item.imgpic_info.link)
                .asCircle()
                .scale(ScaleMode.CENTER_CROP)
                .into(helper.getView<CircleImageView>(R.id.iv_music_cover))
        helper.setText(R.id.tv_music_name, StringUtils.isEmpty(item.title))
        helper.setText(R.id.tv_player_num, StringUtils.setNum(item.counts))
        RxView.clicks(helper.getView<View>(R.id.layout_click))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    if (item.collection!!) {
                        val intent = Intent(mContext, LikesMusicActivity::class.java)
                        val bundle = Bundle()
                        bundle.putString(LikesMusicActivity.MUSICIAN_ID, id)
                        intent.putExtras(bundle)
                        mContext.startActivity(intent)
                    } else {
                        val intent = Intent(mContext, SongSheetDetailsActivity::class.java)
                        val bundle = Bundle()
                        bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, item.id.toString() + "")
                        intent.putExtras(bundle)
                        mContext.startActivity(intent)
                    }
                }
        RxView.clicks(helper.getView<View>(R.id.iv_player))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    if (isAddCount) {
                        isAddCount = false
                        helper.setText(R.id.tv_player_num, StringUtils.setNum(item.counts++))
                    }
                    PlayCtrlUtil.playSheet(mContext, item.id.toString() + "")
                }
    }
}
