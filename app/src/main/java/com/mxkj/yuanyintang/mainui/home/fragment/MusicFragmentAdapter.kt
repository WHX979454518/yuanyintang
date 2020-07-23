package com.mxkj.yuanyintang.mainui.home.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.dialog.MusicOperationDialog
import com.mxkj.yuanyintang.musicplayer.activity.PlayerActivity
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.string.StringUtils

import java.io.File
import java.util.concurrent.TimeUnit

import io.reactivex.functions.Consumer

import com.mxkj.yuanyintang.musicplayer.service.MediaService.ACTION_PAUSE
import com.mxkj.yuanyintang.musicplayer.service.MediaService.bean
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.constant.Constants.User.MUSIC_DIR_PRIVATE
import com.mxkj.yuanyintang.video.MvVideoActivitykt
import com.mxkj.yuanyintang.video.NewMusicDean
import com.umeng.analytics.MobclickAgent
import okhttp3.Headers

class MusicFragmentAdapter(data: List<MusicIndex.ItemInfoListBean.MusicBean>, private val fragmentManager: FragmentManager?, private val biaoshi: String?, private val page: Int, private val id: String?, var isEdit: Boolean) : BaseQuickAdapter<MusicIndex.ItemInfoListBean.MusicBean, BaseViewHolder>(R.layout.musicrecycle_item_music_list, data) {
    private var checkedSongListener: ClickCheckedSongListener? = null
    override fun convert(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean.MusicBean, position: Int) {



        if (MediaService.mediaPlayer != null && MediaService.mediaPlayer.isPlaying) {
            if (bean != null && bean?.id == item.id) {
                item.isPlaying = true
                val musicNane = helper.getView<TextView>(R.id.tv_music_name)
                val nickName = helper.getView<TextView>(R.id.tv_music_nickname)
                musicNane.setTextColor(Color.parseColor("#ff6699"))
                nickName.setTextColor(Color.parseColor("#ff6699"))
            }
        }

        helper.setText(R.id.tv_music_name, StringUtils.isEmpty(item.title))
        helper.setText(R.id.tv_music_nickname, StringUtils.isEmpty(item.nickname))
        val filePath = FileDownloadUtils.generateFilePath(getFilePath(mContext), if (TextUtils.isEmpty(item.title))
            System.currentTimeMillis().toString() + "" + item.title
        else
            StringUtils.isEmpty(item.title))
        when {
            File(filePath).exists() -> helper.getView<View>(R.id.ivIsdown).visibility = View.VISIBLE
            else -> helper.getView<View>(R.id.ivIsdown).visibility = View.GONE
        }
        when {
            item.imgpic_info != null -> ImageLoader.with(mContext).getSize(200, 200).setUrl(item.imgpic_info).into(helper.getView(R.id.iv_music))
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
        //看是否有mv，如果有显示mv小标识
        if (item.mv==null||item.mv.equals("")) {
            helper.getView<ImageView>(R.id.jinrituijian_img).visibility = View.GONE
        } else {
            helper.getView<ImageView>(R.id.jinrituijian_img).visibility = View.VISIBLE
        }
        Log.e("songName",""+biaoshi)
        Log.e("songName",""+page)
        Log.e("songName",""+id)
        //啥都没改


        RxView.clicks(helper.getView(R.id.jinrituijian_img))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    Log.e("position",""+position)
                    var mvuri:String? = null
                    if (biaoshi == "myhomepagemusic") {
                        NetWork.getMusicianMusic(mContext, page, id.toString(), object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jObj = JSON.parseObject(resultData)
                                val musicList = jObj.getJSONArray("data").toJavaList(MusicFragmentBean.DataBean::class.java)
                                mvuri = musicList.get(position).mv_info.link
                                toMV(item.id, mvuri!!,item.uid,item.title,item.nickname,item.imgpic_link);
                            }
                            override fun doError(msg: String) {
                            }

                            override fun doResult() {
                            }
                        })
                    }else{
                        toMV(item.id,item.mv_info.link,item.uid,item.title,item.nickname,item.imgpic_link);
                    }
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
//                            .setSongName(songName)
                            .setCollection(item.collection)
                            .setPosition(position)
                            .setType(1)
                            .setReportType(1)
                            .setReportId(item.id)
                            .setCommentType(1)
                            .setMultiSelect(false)
                            .setPlayTime(item.playtime + "")
                            .setMv(item.mv)
                    if (item.imgpic_info != null) {
                        val link = item.imgpic_info.link
                        val imgpicInfoBean = MusicBean.ImgpicInfoBean()
                        imgpicInfoBean.link = link
                        musicBean.imgpic_info = imgpicInfoBean
                    }
                    //吧mv里面的东西也传过去
                    Log.e("position",""+position)
                    var mvuri:String? = null

                    if (biaoshi == "myhomepagemusic") {
                        NetWork.getMusicianMusic(mContext, page, id.toString(), object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jObj = JSON.parseObject(resultData)
                                val musicList = jObj.getJSONArray("data").toJavaList(MusicFragmentBean.DataBean::class.java)
                                mvuri = musicList.get(position).mv_info.link
                                val link = mvuri
                                val mvInfoBean = MusicBean.MvInfoBean()
                                mvInfoBean.link = link
                                musicBean.mvInfoBean = mvInfoBean

                            }
                            override fun doError(msg: String) {
                            }

                            override fun doResult() {
                            }
                        })
                    }else{
                        if (item.mv_info != null) {
                            val link = item.mv_info.link
                            val mvInfoBean = MusicBean.MvInfoBean()
                            mvInfoBean.link = link
                            musicBean.mvInfoBean = mvInfoBean
                        }
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
                musicNane.setTextColor(Color.parseColor("#2b2b2b"))
                nickName.setTextColor(Color.parseColor("#9da2a6"))
            }
        }
        RxView.clicks(helper.getView(R.id.llItem)).subscribe(object : Consumer<Any> {

            @Throws(Exception::class)
            override fun accept(o: Any) {
                MobclickAgent.onEvent(mContext,"musician_music_play");
                when {
                    isEdit -> {
                        checkBox.isChecked = !checkBox.isChecked
                        item.isIscheck = checkBox.isChecked
                        checkedSongListener?.onChecked(item.isIscheck, position)
                    }
                    else -> {
                        when {
                            bean != null && bean!!.id == data[position].id -> //点击了正在播放的歌
                                when {
                                    MediaService.mediaPlayer != null -> {//播放器正在播放中
                                        val playing = MediaService.mediaPlayer.isPlaying
                                        data[position].isPlaying = !playing
//                                        mContext.sendBroadcast(Intent(ACTION_PAUSE))//不发送广播，不让他暂停


                                        val intent = Intent(mContext, PlayerActivity::class.java)
                                        mContext.startActivity(intent)


                                    }
                                    else -> {//播放器暂停中或没有播放，但是播放资源不为空
                                        data[position].isPlaying = true
                                        when {
                                            data[position] != null && data[position].video_info != null && data[position].video_info.link != null -> {
                                                val file = File(filePath)
                                                when {
                                                    file.exists() -> {
                                                        bean = JSON.parseObject(JSON.toJSONString(data[position]), MusicInfo.DataBean::class.java)
                                                        mContext.sendBroadcast(Intent(ACTION_PAUSE))
                                                    }
                                                    else -> PlayCtrlUtil.play(mContext, data[position].id, data[position].song_id)
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
                                    file.exists() -> {//将要播放的歌已经被下载，播放本地文件
                                        bean = JSON.parseObject(JSON.toJSONString(data[position]), MusicInfo.DataBean::class.java)
                                        mContext.sendBroadcast(Intent(ACTION_PAUSE))
                                        //将要播放的歌没下载，请求网络获取
                                    }
                                    data[position] != null && data[position].video_info != null && data[position].video_info.link != null -> run { PlayCtrlUtil.play(mContext, data[position].id, data[position].song_id) }
                                }
                            }
                        }
                        setNewData(data)
                    }
                }
            }
        })
    }

    fun setCheckedSongListener(checkedSongListener: ClickCheckedSongListener?) {
        this.checkedSongListener = checkedSongListener
    }

    interface ClickCheckedSongListener {
        fun onChecked(aBoolean: Boolean?, position: Int)

        fun onRefreshData()
    }

    private fun getFilePath(context: Context): String {
        return when {
            CacheUtils.getBoolean(context, MUSIC_DIR_PRIVATE, false) -> {
                val path = (Environment.getDataDirectory().absolutePath
                        + File.separator + "Music")
                val file = File(path)
                if (!file.exists()) {
                    file.mkdirs()
                }
                path
            }
            else -> Constants.Path.APP_PATH + File.separator + "Music"
        }
    }


    //跳转MV
    private fun toMV(id :Int,mvurl :String,uid:Int,title :String,nickname :String,imgpic_link :String) {
        val intent = Intent(mContext, MvVideoActivitykt::class.java)
        var bundle = Bundle()
        bundle.putInt("mv", id);
        bundle.putString("mvurl", mvurl);
        bundle.putInt("uid", uid);
        bundle.putString("title", title);
        bundle.putString("nickname", nickname);
        bundle.putString("imgpic_link", imgpic_link);
        bundle.putString("bioashi", 1.toString() + "")
        intent.putExtra("mvdate",bundle);
        mContext.startActivity(intent)
    }
}