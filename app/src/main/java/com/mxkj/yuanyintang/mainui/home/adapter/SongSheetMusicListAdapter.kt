package com.mxkj.yuanyintang.mainui.home.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.os.Message
import android.support.v4.app.FragmentManager
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.alibaba.fastjson.JSON
import com.jakewharton.rxbinding2.view.RxView
import com.liulishuo.filedownloader.util.FileDownloadUtils
import com.lzy.okgo.model.HttpParams
import com.lzy.okgo.utils.HttpUtils.runOnUiThread
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.R.attr.type
import com.mxkj.yuanyintang.R.id.*
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
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.bean.SongListDetails
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean
import com.mxkj.yuanyintang.mainui.newapp.pond.PondInfo
import com.mxkj.yuanyintang.musicplayer.activity.PlayerActivity
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
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.constant.Constants.User.MUSIC_DIR_PRIVATE
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.threadpool.ThreadPoolManager
import com.mxkj.yuanyintang.video.MusicClassification
import com.mxkj.yuanyintang.video.MvVideoActivitykt
import com.mxkj.yuanyintang.video.NewMusicDean
import com.mxkj.yuanyintang.video.SongMusicMvBean
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_music_type_new.*
import kotlinx.android.synthetic.main.activity_today_hot_song.*
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class SongSheetMusicListAdapter(data: List<MusicIndex.ItemInfoListBean.MusicBean>, private val fragmentManager: FragmentManager?, private val songName: String?, var isEdit: Boolean) : BaseQuickAdapter<MusicIndex.ItemInfoListBean.MusicBean, BaseViewHolder>(R.layout.musicrecycle_item_music_list, data) {
    private var checkedSongListener: ClickCheckedSongListener? = null
    override fun convert(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean.MusicBean, position: Int) {

        val musicNane = helper.getView<TextView>(R.id.tv_music_name)
        val nickName = helper.getView<TextView>(R.id.tv_music_nickname)
        if (MediaService.mediaPlayer != null && MediaService.mediaPlayer.isPlaying) {
            if (bean != null && bean?.id == item.id) {
                item.isPlaying = true

                musicNane.setTextColor(Color.parseColor("#ff6699"))
                nickName.setTextColor(Color.parseColor("#ff6699"))
            }
        }

        helper.setText(R.id.tv_music_name, StringUtils.isEmpty(item.title))
        helper.setText(R.id.tv_music_nickname, StringUtils.isEmpty(item.nickname))
        helper.setText(R.id.tv_music_name_gray, StringUtils.isEmpty(item.title))
        helper.setText(R.id.tv_music_nickname_gray, StringUtils.isEmpty(item.nickname))
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
        Log.e("lllll",""+item.mv);
        if (item.mv==null||item.mv.equals("")) {
            helper.getView<ImageView>(R.id.jinrituijian_img).visibility = View.GONE
            Log.e("position",""+songName)
        } else {
            helper.getView<ImageView>(R.id.jinrituijian_img).visibility = View.VISIBLE
            Log.e("item",""+item.toString())

        }
        Log.e("songName",""+songName)
        if(songName == "1" || songName == "2" || songName == "3"){
            helper.getView<ImageView>(R.id.jinrituijian_img).visibility = View.GONE
            Log.e("item",""+item.toString())
        }
        if( songName == "home_page_music"){
            helper.getView<ImageView>(R.id.jinrituijian_img).visibility = View.GONE
            Log.e("item",""+item.toString())
        }

        //看歌曲是否有下架
        if(songName.equals("today") || songName == "today" || songName.equals("new") ||songName == "new"){

        }else{
            if (item.status == 0) {
                helper.getView<TextView>(R.id.tv_music_name).visibility = View.GONE
                helper.getView<TextView>(R.id.tv_music_name_gray).visibility = View.VISIBLE
                helper.getView<TextView>(R.id.tv_music_nickname).visibility = View.GONE
                helper.getView<TextView>(R.id.tv_music_nickname_gray).visibility = View.VISIBLE
//            val musicNane = helper.getView<TextView>(R.id.tv_music_name)
//            val nickName = helper.getView<TextView>(R.id.tv_music_nickname)
//            helper.getView<TextView>(R.id.tv_music_name).setTextColor(Color.parseColor("#9da2a6"))
//            helper.getView<TextView>(R.id.tv_music_nickname).setTextColor(Color.parseColor("#9da2a6"))
            }
        }


        RxView.clicks(helper.getView(R.id.jinrituijian_img))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    Log.e("position",""+position)
                    var mvuri:String? = null
                    if (songName == "new") {
                        NetWork.getNewRecommend("todayHotNew",mContext, null, object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jObj = JSON.parseObject(resultData)
                                val dataObj = jObj.getJSONObject("data")
                                val musicList = dataObj.getJSONArray("list").toJavaList(NewMusicDean.DataBean.ListBean::class.java)
//                                Log.e("musicList",""+musicList.get(position).mv_info.link)
                                mvuri = musicList.get(position).mv_info.link
                            }
                            override fun doError(msg: String) {
                            }

                            override fun doResult() {
                            }
                        })
                        toMV(item.id, mvuri!!,item.uid,item.title,item.nickname,item.imgpic_link);
                    } else if (songName == "today") {
                        NetWork.getTodatRecommend("todayHotToday",mContext, null, object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jObj = JSON.parseObject(resultData)
                                val dataObj = jObj.getJSONObject("data")
                                val musicList = dataObj.getJSONArray("musics").toJavaList(NewMusicDean.DataBean.ListBean::class.java)
//                                Log.e("musicList",""+musicList.get(position).mv_info.link)
                                mvuri = musicList.get(position).mv_info.link
                            }
                            override fun doError(msg: String) {
                            }
                            override fun doResult() {
                            }
                        })
                        toMV(item.id, mvuri!!,item.uid,item.title,item.nickname,item.imgpic_link);
                    }else if(songName == "1"||songName == "2"||songName == "3"||songName == "4"||songName == "5"){
                        var params: HttpParams = HttpParams()
                        if (params == null) {
                            params = HttpParams()
                        }
                        params?.put("p", songName.toString())
                        NetWork.getPublicMusic(params, mContext, object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val obj = JSON.parseObject(resultData)
                                val jObj = obj.getJSONObject("data")
                                val array = jObj.getJSONArray("data_list")
                                val musicclassificationListBean = JSON.parseArray(array.toString(), MusicClassification.DataBean.DataListBean::class.java)
                                Log.e("ppppp",""+musicclassificationListBean.size)
//                                if(songName == "3"){
//                                    mvuri = musicclassificationListBean.get(position-40).mv_info.link
//                                }else if(songName == "2"){
//                                    mvuri = musicclassificationListBean.get(position-20).mv_info.link
//                                }else{
                                    mvuri = musicclassificationListBean.get(position).mv_info.link
//                                }
                                toMV(item.id, mvuri!!,item.uid,item.title,item.nickname,item.imgpic_link);
                            }

                            override fun doError(msg: String) {
                            }

                            override fun doResult() {

                            }
                        })

//                        toMV(item.id,item.mv_info.link,item.uid,item.title,item.nickname,item.imgpic_link);
                    }else if(songName == "home_page_music"){
                        toMV(item.id,item.mv_info.link,item.uid,item.title,item.nickname,item.imgpic_link);
                    }else{
                        NetWork.getSongSheetDetails("songDetails_$songName", mContext, songName.toString(), object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                try {
                                    val `object` = JSONObject(resultData)
                                    var dataObject: JSONObject? = null
                                    dataObject = if (`object`.optJSONObject("data") == null) JSONObject() else `object`.optJSONObject("data")
                                    val musicArray = if (dataObject!!.optJSONArray("music") == null) JSONArray() else dataObject.optJSONArray("music")
//                                    Log.d(TAG, "doNext: ------------>" + musicArray.toString());
                                    var musicBeanList: List<SongMusicMvBean.MusicBean> = ArrayList()
                                    musicBeanList = JSON.parseArray(StringUtils.isEmpty(musicArray.toString()), SongMusicMvBean.MusicBean::class.java)
                                    mvuri = musicBeanList.get(position).mv_info.link

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                            }

                            override fun doError(msg: String) {

                            }

                            override fun doResult() {

                            }
                        })
                        if(null==mvuri||mvuri.equals("")){

                        }else{
                            toMV(item.id, mvuri!!,item.uid,item.title,item.nickname,item.imgpic_link);
                        }

                    }

                }

        var biaoshitype: Int = 1
        if (songName == "new" || songName == "today") {
            biaoshitype =10
        }else{
            if(item.status == 0){
                biaoshitype =11
            }else{
                biaoshitype =1
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
                            .setSongName(songName)
                            .setCollection(item.collection)
                            .setPosition(position)
                            .setType(biaoshitype)
                            .setReportType(1)
                            .setReportId(item.sid)
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

                    if (songName == "new") {
                        NetWork.getNewRecommend("todayHotNew",mContext, null, object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jObj = JSON.parseObject(resultData)
                                val dataObj = jObj.getJSONObject("data")
                                val musicList = dataObj.getJSONArray("list").toJavaList(NewMusicDean.DataBean.ListBean::class.java)
//                                Log.e("musicList",""+musicList.get(position).mv_info.link)
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
                    } else if (songName == "today") {
                        NetWork.getTodatRecommend("todayHotToday",mContext, null, object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jObj = JSON.parseObject(resultData)
                                val dataObj = jObj.getJSONObject("data")
                                val musicList = dataObj.getJSONArray("musics").toJavaList(NewMusicDean.DataBean.ListBean::class.java)
//                                Log.e("musicList",""+musicList.get(position).mv_info.link)
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
                    }else if(songName == "1"||songName == "2"||songName == "3"||songName == "4"||songName == "5"){
                        var params: HttpParams = HttpParams()
                        if (params == null) {
                            params = HttpParams()
                        }
                        params?.put("p", songName.toString())
                        NetWork.getPublicMusic(params, mContext, object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val obj = JSON.parseObject(resultData)
                                val jObj = obj.getJSONObject("data")
                                val array = jObj.getJSONArray("data_list")
                                val musicclassificationListBean = JSON.parseArray(array.toString(), MusicClassification.DataBean.DataListBean::class.java)
//                                if(songName == "3"){
//                                    mvuri = musicclassificationListBean.get(position-40).mv_info.link
//                                }else if(songName == "2"){
//                                    mvuri = musicclassificationListBean.get(position-20).mv_info.link
//                                }else{
                                    mvuri = musicclassificationListBean.get(position).mv_info.link
//                                }
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
//                        if (item.mv_info != null) {
//                            val link = item.mv_info.link
//                            val mvInfoBean = MusicBean.MvInfoBean()
//                            mvInfoBean.link = link
//                            musicBean.mvInfoBean = mvInfoBean
//                        }
                        NetWork.getSongSheetDetails("songDetails_$songName", mContext, songName.toString(), object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                try {
                                    val `object` = JSONObject(resultData)
                                    var dataObject: JSONObject? = null
                                    dataObject = if (`object`.optJSONObject("data") == null) JSONObject() else `object`.optJSONObject("data")
                                    val musicArray = if (dataObject!!.optJSONArray("music") == null) JSONArray() else dataObject!!.optJSONArray("music")
//                                    Log.d(TAG, "doNext: ------------>" + musicArray.toString());
                                    var musicBeanList: List<SongMusicMvBean.MusicBean> = ArrayList()
                                    musicBeanList = JSON.parseArray(StringUtils.isEmpty(musicArray.toString()), SongMusicMvBean.MusicBean::class.java)
                                    mvuri = musicBeanList.get(position).mv_info.link
                                    val link = mvuri
                                    val mvInfoBean = MusicBean.MvInfoBean()
                                    mvInfoBean.link = link
                                    musicBean.mvInfoBean = mvInfoBean

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                            }

                            override fun doError(msg: String) {

                            }

                            override fun doResult() {

                            }
                        })
                    }
//                    if (item.mv_info != null) {
//                        val link = item.mv_info.link
//                        val mvInfoBean = MusicBean.MvInfoBean()
//                        mvInfoBean.link = link
//                        musicBean.mvInfoBean = mvInfoBean
//                    }


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
//        val musicNane = helper.getView<TextView>(R.id.tv_music_name)
//        val nickName = helper.getView<TextView>(R.id.tv_music_nickname)
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
//                if(songName.equals("today") || songName == "today" || songName.equals("new") ||songName == "new"){
//
//                }else {
                if(songName == "today" || songName == "new"){
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
                }else{
                    if (item.status == 0) {
                        var toast = Toast(mContext);
                        var view = LayoutInflater.from(mContext).inflate(R.layout.customtoast, null);
                        toast.setView(view);
//                        toast.setGravity(Gravity.FILL, 0, 30);
//                        toast.setGravity(Gravity.TOP, 0, 30);
                        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, -500)
                        toast.show();
                    }else{
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
                }

//                }
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