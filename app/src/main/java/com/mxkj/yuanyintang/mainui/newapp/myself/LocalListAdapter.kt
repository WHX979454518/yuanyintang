package com.mxkj.yuanyintang.mainui.newapp.myself

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Environment
import android.support.v4.app.FragmentManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView

import com.alibaba.fastjson.JSON
import com.jakewharton.rxbinding2.view.RxView
import com.liulishuo.filedownloader.util.FileDownloadUtils
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.R.id.jinrituijian_img
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.mainui.newapp.home.TodayHotSongActivity
import com.mxkj.yuanyintang.mainui.search.adapter.Music
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.dialog.MusicOperationDialog
import com.mxkj.yuanyintang.musicplayer.play_control.PlayList
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.string.StringUtils

import java.io.File
import java.util.concurrent.TimeUnit

import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer

import com.mxkj.yuanyintang.musicplayer.service.MediaService.ACTION_PAUSE
import com.mxkj.yuanyintang.musicplayer.service.MediaService.bean
import com.mxkj.yuanyintang.utils.constant.Constants.User.MUSIC_DIR_PRIVATE

class LocalListAdapter(data: List<MusicIndex.ItemInfoListBean.MusicBean>, private val fragmentManager: FragmentManager?, private val songName: String?, var isEdit: Boolean) : BaseQuickAdapter<MusicIndex.ItemInfoListBean.MusicBean, BaseViewHolder>(R.layout.musicrecycle_item_music_list, data) {
    private var checkedSongListener: ClickCheckedSongListener? = null
    private var filePath: String? = null

    override fun convert(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean.MusicBean, position: Int) {
        helper.setText(R.id.tv_music_name, StringUtils.isEmpty(item.title))
        helper.setText(R.id.tv_music_nickname, StringUtils.isEmpty(item.nickname))
        helper.getView<View>(R.id.ivIsdown).visibility = View.GONE
        helper.getView<View>(R.id.jinrituijian_img).visibility = View.GONE
        when {
            item.imgpic_info != null -> ImageLoader.with(mContext).getSize(200,200).setUrl(item.imgpic_info).into(helper.getView(R.id.iv_music))
        }
        val imageView = helper.getView<ImageView>(R.id.iv_more)
        when {
            isEdit -> helper.getView<View>(R.id.check_song).visibility = View.VISIBLE
            else -> helper.getView<View>(R.id.check_song).visibility = View.GONE
        }
        when {
            isEdit -> helper.getView<View>(R.id.check_song).visibility = View.VISIBLE
        //编辑模式下，隐藏除了歌曲列表外的其他布局
            else -> helper.getView<View>(R.id.check_song).visibility = View.GONE
        }
        when {
            item.localMusic!! -> helper.getView<View>(R.id.iv_more).visibility = View.GONE
            else -> helper.getView<View>(R.id.iv_more).visibility = View.VISIBLE
        }
        RxView.clicks(imageView)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    val musicBean = MusicBean()
                            .setMusic_name(item.title)
                            .setCommentNum(item.comment)
                            .setUid(item.uid)
                            .setMusic_id(item.id.toString() + "")
                            .setMusician_name(item.nickname)
                            .setSong_id(item.song_id)
                            .setSongName(songName)
                            .setCollection(item.collection)
                            .setPosition(position)
                            .setType(1)
                            .setReportType(1)
                            .setReportId(item.id)
                            .setCommentType(1)
                            .setMultiSelect(false)
                            .setPlayTime(item.playtime + "")
                    if (item.imgpic_info != null) {
                        val link = item.imgpic_info.link
                        val imgpicInfoBean = MusicBean.ImgpicInfoBean()
                        imgpicInfoBean.link = link
                        musicBean.imgpic_info = imgpicInfoBean
                    }
                    fragmentManager?.let {
                        val musicOperationDialog = MusicOperationDialog(
                                musicBean, it)
                        musicOperationDialog.show(it, position.toString() + "")
                        musicOperationDialog.setSetMusicOperationListener(object : MusicOperationDialog.SetMusicOperationListener {
                            override fun onCollection(collection: Int, position: Int) {
                                item.collection = collection
                                notifyDataSetChanged()
                                musicOperationDialog.dismiss()
                            }

                            override fun getType(type: Int) {

                            }
                        })

                    }
                }

        val checkBox = helper.getView<CheckBox>(R.id.check_song)
        checkBox.isChecked = item.isIscheck == true
        val tvType = helper.getView<TextView>(R.id.tvType)
        when {
            item.music_type == 1 -> tvType.visibility = View.VISIBLE
            else -> tvType.visibility = View.GONE
        }
        val musicNane = helper.getView<TextView>(R.id.tv_music_name)
        val nickName = helper.getView<TextView>(R.id.tv_music_nickname)
        when {
            item.isPlaying -> {
                musicNane.setTextColor(Color.parseColor("#ff6699"))
                nickName.setTextColor(Color.parseColor("#ff6699"))
            }
            else -> {
                musicNane.setTextColor(Color.parseColor("#ff333333"))
                nickName.setTextColor(Color.parseColor("#ff333333"))
            }
        }
        RxView.clicks(helper.getView(R.id.llItem)).subscribe {
            when {
                isEdit -> {
                    checkBox.isChecked = !checkBox.isChecked
                    item.isIscheck = checkBox.isChecked
                    checkedSongListener?.onChecked(item.isIscheck, position)
                }
                else -> {
                    filePath = data[position].video_info.link
                    when {
                        bean != null && bean!!.video_info?.link == data[position].video_info.link -> //点击了正在播放的歌
                            when {
                                MediaService.mediaPlayer != null -> {
                                    val playing = MediaService.mediaPlayer.isPlaying
                                    data[position].isPlaying = !playing
                                    mContext.sendBroadcast(Intent(ACTION_PAUSE))
                                }
                                else -> {
                                    data[position].isPlaying = true
                                    when {
                                        data[position] != null && data[position].video_info != null && data[position].video_info.link != null -> {
                                            val file = File(filePath)
                                            when {
                                                file.exists() -> {
                                                    bean = JSON.parseObject(JSON.toJSONString(data[position]), MusicInfo.DataBean::class.java)
                                                    mContext.sendBroadcast(Intent(ACTION_PAUSE))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        else -> {//点击了正在播放的歌之外的歌
                            for (i in 0 until data.size) {
                                when (i) {
                                    position -> {
                                        data[i].isPlaying = true
                                        bean = null
                                    }
                                    else -> data[i].isPlaying = false
                                }
                            }
                            val file = File(filePath)
                            when {
                                file.exists() -> {
                                    bean = JSON.parseObject(JSON.toJSONString(data[position]), MusicInfo.DataBean::class.java)
                                    MediaService.mediaPlayer?.stop()
                                    MediaService.mediaPlayer = null
                                    mContext.sendBroadcast(Intent(ACTION_PAUSE))
                                }
                            }
                        }
                    }
                    setNewData(data)
                    PlayList.addToList(mContext, bean)
                }
            }
        }
    }

    fun setCheckedSongListener(checkedSongListener: ClickCheckedSongListener?) {
        this.checkedSongListener = checkedSongListener
    }

    interface ClickCheckedSongListener {
        fun onChecked(aBoolean: Boolean?, position: Int)

        fun onRefreshData()
    }
}