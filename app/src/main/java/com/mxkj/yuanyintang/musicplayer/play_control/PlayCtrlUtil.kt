package com.mxkj.yuanyintang.musicplayer.play_control

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.text.TextUtils
import android.util.Log

import com.alibaba.fastjson.JSON
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.database.DBManager
import com.mxkj.yuanyintang.database.updatafilesql.UpDataFileBean
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.home.music_charts.bean.ChartsListBean
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean
import com.mxkj.yuanyintang.musicplayer.playcache.HttpProxyCacheServer
import com.mxkj.yuanyintang.musicplayer.util.SheetDetialBean
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils
import com.mxkj.yuanyintang.utils.uiutils.Toast
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils

import java.io.File

import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers

import com.mxkj.yuanyintang.musicplayer.service.MediaService.MEDIA_PLAY_IS_PAUSE
import com.mxkj.yuanyintang.musicplayer.service.MediaService.alreadyPlayList
import com.mxkj.yuanyintang.musicplayer.service.MediaService.bean
import com.mxkj.yuanyintang.musicplayer.service.MediaService.playList
import com.mxkj.yuanyintang.musicplayer.service.MediaService.tempList
import io.reactivex.*

/**
 * 播放点击的歌
 */

object PlayCtrlUtil {
    private val TAG = "PlayCtrlUtil"
    fun play(context: Context, musicId: Int, songId: Int) {
        Observable.create(ObservableOnSubscribe<Any> { musicPlay(context, musicId, songId) }).observeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe {}
    }

    private fun toSnackBar(context: Context, s: String, icon: Int) {
        if (context is BaseActivity) {
            context.setSnackBar(s, "", icon)
        }
    }

    private fun musicPlay(context: Context, musicId: Int, songId: Int) {
        if (NetConnectedUtils.isNetConnected(context)) {
            GetMusicInfo.playById(context, musicId, songId, object : GetMusicInfo.SetBeanData {
                override fun setBeanData(dataBean: MusicInfo.DataBean?) {
                    Observable.create(ObservableOnSubscribe<Any> {
                        if (dataBean != null) {
                            bean = dataBean
                            startServiceToPlay(context, dataBean)
                        }
                    }).observeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe {
                    }

                }
            })

        } else {
            toSnackBar(context, "没有连接网络", R.drawable.icon_fails)
        }
    }

    fun nextPlay(context: Context?, musicId: Int) {
        GetMusicInfo.playById(context!!, musicId, 0, object : GetMusicInfo.SetBeanData {
            override fun setBeanData(dataBean: MusicInfo.DataBean?) {
                if (null != playList && dataBean != null) {
                    for (i in playList.indices) {
                        if (playList[i].id == dataBean.id) {
                            val dataBean1 = playList[i]
                            dataBean1.comment = dataBean.comment
                            dataBean1.collection = dataBean.collection
                        }
                    }
                }
                MediaService.bean = dataBean
                context.sendBroadcast(Intent("startplay"))
                PlayList.addToList(context, dataBean)
                MediaService.getNextPlayList().add(dataBean)
                if (!MediaService.getMediaPlayer().isPlaying) {
                    startServiceToPlay(context, dataBean)
                }
                if (null != context) {
                    Toast.create(MainApplication.context).show("添加下一首至列表成功")
                }
            }
        })
    }

    /**
     * 播放本地音乐
     * */
    fun localityPlay(context: Context, upDataFileBean: UpDataFileBean, isNext: Boolean, isToPlay: Boolean) {
        val dataBean = MusicInfo.DataBean()
        dataBean.loadType = "locality"
        dataBean.up_time = upDataFileBean.upTime
        if (upDataFileBean.path != null) {
            val videoInfoBean = MusicInfo.DataBean.VideoInfoBean()
            videoInfoBean.link = upDataFileBean.path
            dataBean.video_info = videoInfoBean
        }
        dataBean.nickname = upDataFileBean.nickname
        dataBean.item_id = Integer.parseInt(if (TextUtils.isEmpty(upDataFileBean.music_id)) "0" else upDataFileBean.music_id)
        dataBean.collection = upDataFileBean.collection
        dataBean.localityId = upDataFileBean.id


        val imgpicInfoBean = MusicInfo.DataBean.ImgpicInfoBean()//HomeBean.ImgpicInfoBean();//
        if (upDataFileBean?.img_url != null) {
            imgpicInfoBean.link = upDataFileBean.img_url
            dataBean.imgpic_info = imgpicInfoBean
        }
        dataBean.uid = upDataFileBean.uid
        dataBean.id = Integer.valueOf(upDataFileBean.music_id)
        dataBean.song_id = upDataFileBean.song_id
        dataBean.fileName = upDataFileBean.fileName ?: ""
        dataBean.title = upDataFileBean.music_name
        dataBean.playtime = upDataFileBean.playTime ?: ""
        PlayList.addToList(context, dataBean)
        if (isToPlay) {
            startServiceToPlay(context, dataBean)
        }
        if (isNext) {
            MediaService.getNextPlayList().add(dataBean)
        }
    }

    /**
     * 获取歌单详情
     */
    fun getSheetDetail(context: Context, sheetId: String, callBack: SheetDetialCallBack) {
        NetWork.getSongSheetDetails("songDetails_"+sheetId,context, sheetId, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val sheetBean = JSON.parseObject(resultData, SheetDetialBean::class.java)
                if (sheetBean.data != null) {
                    callBack.getSheetDetial(sheetBean)
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    /**
     * 获取歌单详情
     */
    fun playSheet(context: Context, sheetId: String) {
        if (!TextUtils.isEmpty(sheetId)) {
            if (null != MediaService.bean) {
                if (MediaService.bean!!.song_id == Integer.parseInt(sheetId)) {
                    if (MediaService.getMediaPlayer().isPlaying) {
                        context.sendBroadcast(Intent(MediaService.ACTION_PAUSE))
                    } else {
                        if (!CacheUtils.getBoolean(context, MEDIA_PLAY_IS_PAUSE, true)) {
                            playSongList(context, sheetId)
                        } else {
                            context.sendBroadcast(Intent(MediaService.ACTION_PAUSE))
                        }
                    }
                } else {
                    playSongList(context, sheetId)
                }
            } else {
                playSongList(context, sheetId)
            }
        }

    }

    /*播放歌单歌曲*/
    private fun playSongList(context: Context, sheetId: String) {
        NetWork.getSongList(context, sheetId, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                if (resultData == null) {
                    return
                }
                if (!resultData.startsWith("{")) {
                    return
                }

                val dbManager = DBManager(context)
                dbManager.deleteAllInfo(0)
                playList.clear()
                tempList.clear()
                alreadyPlayList.clear()

                val sheetBean = JSON.parseObject(resultData, SheetDetialBean::class.java)
                if (null != sheetBean && sheetBean.data != null) {
                    val data = sheetBean.data
                    if (data == null || data.size == 0) return
                    Observable.fromArray(data)
                            .flatMap { dataBeen -> Observable.fromIterable(dataBeen) }
                            .subscribeOn(Schedulers.io())
                            .subscribe(object : Observer<SheetDetialBean.DataBean> {
                                override fun onSubscribe(@NonNull d: Disposable) {
//                                    val dbManager = DBManager(context)
//                                    dbManager.deleteAllInfo(0)
//                                    playList.clear()
//                                    tempList.clear()
//                                    alreadyPlayList.clear()
                                }

                                override fun onNext(@NonNull data: SheetDetialBean.DataBean) {
                                    val dataBean = sheetBeanResultData(data, sheetId)
                                    PlayList.addToList(context, dataBean)
                                }

                                override fun onError(@NonNull e: Throwable) {

                                }

                                override fun onComplete() {
                                    if (null != sheetBean.data && sheetBean.data.size > 0) {
                                        MediaService.setRandomPlayList()
                                        for (i in playList.indices) {
                                            if (playList[i].id == sheetBeanResultData(sheetBean.data[0], sheetId).id) {
                                                alreadyPlayList.add(playList[i])
                                            }
                                        }
                                        play(context, sheetBeanResultData(sheetBean.data[0], sheetId).id, Integer.valueOf(sheetId)!!)
                                    }
                                }
                            })
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }


    private fun sheetBeanResultData(data: SheetDetialBean.DataBean, song_id: String): MusicInfo.DataBean {
        val dataBean = MusicInfo.DataBean()
        dataBean.video = data.video
        //        dataBean.setVideo_link(data.getVideo_link());
        if (data.video_info != null) {
            val videoInfoBean = MusicInfo.DataBean.VideoInfoBean()
            videoInfoBean.link = data.video_info.link
            dataBean.video_info = videoInfoBean
        }
        dataBean.id = data.id
        dataBean.song_id = if (!TextUtils.isEmpty(song_id)) Integer.parseInt(song_id) else 0
        dataBean.uid = data.uid
        dataBean.title = data.title
        dataBean.collection = data.collection

        val imgpicInfoBean = MusicInfo.DataBean.ImgpicInfoBean()//HomeBean.ImgpicInfoBean();//
        try {
            imgpicInfoBean.link = data.imgpic_info.link
        } catch (e: RuntimeException) {
        }

        dataBean.imgpic_info = imgpicInfoBean

        dataBean.collection = data.collection
        dataBean.comment = data.comment
        dataBean.imgpic = data.imgpic
        dataBean.lyrics = data.lyrics
        dataBean.nickname = data.nickname
        dataBean.playtime = data.playtime
        dataBean.song_id = data.song_id
        dataBean.up_time = data.up_time
        dataBean.original = data.original
        data.music_count = data.music_count
        return dataBean
    }

    /**
     * 收藏歌曲、取消收藏
     */
    fun collectSong(context: Context, dataBean: MusicInfo.DataBean?, callBack: ChgCollectCallBack) {//position
        if (dataBean == null) return
        if (CacheUtils.getBoolean(context, Constants.User.IS_LOGIN, false)) {
            NetWork.collectMusic(context, dataBean.id.toString() + "", object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    context.sendBroadcast(Intent("updateData"))
                    if (dataBean.collection == 1) {
                        dataBean.collection = 0
                    } else {
                        dataBean.collection = 1
                    }
                    val dbManager = DBManager(context)
                    dbManager.upDateMusicInfo(dataBean)
                    playList = PlayList.getList(context)
                    callBack.chgCollect(dataBean)
                }

                override fun doError(msg: String) {
                    callBack.onError()
                }

                override fun doResult() {

                }
            })

        } else {
            context.startActivity(Intent(context, LoginRegMainPage::class.java))
        }
    }

    fun startServiceToPlay(context: Context?, dataBean: MusicInfo.DataBean?) {
        PlayList.addToList(context, dataBean)
        val intent = Intent(context, MediaService::class.java)
        intent.putExtra("bean", dataBean)
        context?.startService(intent)
        CacheUtils.setString(context, Constants.LAST_PLAY.BEAN_STR, JSON.toJSONString(dataBean))
        CacheUtils.setBoolean(context, MediaService.MEDIA_PLAY_IS_PAUSE, false)
    }

    /*播放排行榜歌曲*/
    fun playChartsList(context: Context, id: Int, type: Int) {
        val params = HttpParams()
        params.put("class_id", id.toString() + "")
        params.put("type", type.toString() + "")
        NetWork.getChartsList(context, 1, params, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val chartsListBean = JSON.parseObject(resultData, ChartsListBean::class.java)
                        ?: return
                val data = chartsListBean.data ?: return
                val dataList = data.data_list ?: return
                Observable.fromArray(dataList)
                        .flatMap { dataBeen -> Observable.fromIterable(dataBeen) }.subscribeOn(Schedulers.io()).subscribe(object : Observer<ChartsListBean.DataBean.DataListBean> {
                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onNext(data: ChartsListBean.DataBean.DataListBean) {
                                val dataBean = MusicInfo.DataBean()
                                dataBean.video = data.video
                                if (data.video_info != null) {
                                    val videoInfoBean = MusicInfo.DataBean.VideoInfoBean()
                                    videoInfoBean.link = data.video_info.link
                                    dataBean.video_info = videoInfoBean
                                }
                                dataBean.id = data.music_id
                                dataBean.song_id = 0
                                dataBean.uid = data.uid
                                dataBean.title = data.title
                                //                        dataBean.setCollection(data.getc);
                                val imgpicInfoBean = MusicInfo.DataBean.ImgpicInfoBean()//HomeBean.ImgpicInfoBean();//
                                try {
                                    imgpicInfoBean.link = data.imgpic_info.link
                                } catch (e: RuntimeException) {
                                }

                                dataBean.imgpic_info = imgpicInfoBean
                                dataBean.comment = data.comment
                                dataBean.imgpic = data.imgpic
                                //                        dataBean.setLyrics(data.getmu());
                                dataBean.nickname = data.nickname
                                //                        dataBean.setPlaytime(data.getPlaytime());
                                PlayList.addToList(context, dataBean)
                            }

                            override fun onError(e: Throwable) {
                                Log.e(TAG, "onError: ")
                            }

                            override fun onComplete() {
                                if (dataList != null && dataList.size > 0) {
                                    if (MediaService.playModel < 0) {
                                        MediaService.playModel = 2
                                    }
                                    play(context, dataList[0].music_id, 0)
                                }
                            }
                        })
            }

            override fun doError(msg: String) {}

            override fun doResult() {

            }
        })

    }

    /*播放今日热门歌曲列表*/
    fun playTodayList(context: Context) {
        NetWork.getTodatRecommend("todayHotToday",context, null, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val jObj = JSON.parseObject(resultData)
                val dataObj = jObj.getJSONObject("data")
                val musicList = dataObj.getJSONArray("musics").toJavaList(MusicIndex.ItemInfoListBean.MusicBean::class.java)


                val dbManager = DBManager(context)
//                dbManager.deleteAllInfo(0)
                playList.clear()
                tempList.clear()
                alreadyPlayList.clear()

                PlayCtrlUtil.playTypeList(context, musicList as ArrayList<MusicIndex.ItemInfoListBean.MusicBean>)

//                Observable.fromArray(musicList)
//                        .flatMap { dataBeen -> Observable.fromIterable(dataBeen) }.subscribeOn(Schedulers.io()).subscribe(object : Observer<MusicIndex.ItemInfoListBean.MusicBean> {
//                            override fun onSubscribe(d: Disposable) {
//
//                            }
//
//                            override fun onNext(data: MusicIndex.ItemInfoListBean.MusicBean) {
//                                val dataBean = MusicInfo.DataBean()
//                                dataBean.video = data.video
//                                if (data.video_info != null) {
//                                    val videoInfoBean = MusicInfo.DataBean.VideoInfoBean()
//                                    videoInfoBean.link = data.video_info.link
//                                    dataBean.video_info = videoInfoBean
//                                }
//                                dataBean.id = data.id
//                                dataBean.song_id = 0
//                                dataBean.uid = data.uid
//                                dataBean.title = data.title
//                                //                        dataBean.setCollection(data.getc);
//                                val imgpicInfoBean = MusicInfo.DataBean.ImgpicInfoBean()//HomeBean.ImgpicInfoBean();
//                                try {
//                                    imgpicInfoBean.link = data.imgpic_info.link
//                                } catch (e: RuntimeException) {
//                                }
//
//                                dataBean.imgpic_info = imgpicInfoBean
//                                dataBean.comment = data.comment
//                                dataBean.imgpic = data.imgpic
//                                //                        dataBean.setLyrics(data.getmu());
//                                dataBean.nickname = data.nickname
//                                //                        dataBean.setPlaytime(data.getPlaytime());
////                                PlayList.addToList(context, dataBean)
//                            }
//
//                            override fun onError(e: Throwable) {
//                                Log.e(TAG, "onError: ")
//                            }
//
//                            override fun onComplete() {
//                                if (musicList != null && musicList.size > 0) {
//                                    if (MediaService.playModel < 0) {
//                                        MediaService.playModel = 2
//                                    }
//                                    play(context, musicList[0].id, 0)
//                                }
//                            }
//                        })
            }

            override fun doError(msg: String) {
            }

            override fun doResult() {
            }

        })
    }

    fun playTypeList(context: Context, daatList: ArrayList<MusicIndex.ItemInfoListBean.MusicBean>) {
        if (daatList == null) return


        val dbManager = DBManager(context)
//        dbManager.deleteAllInfo(0)
        MediaService.playList.clear()
        MediaService.tempList.clear()
        MediaService.alreadyPlayList.clear()
        Log.e("daatList",""+daatList.size)

        Observable.fromArray(daatList).flatMap { dataBeen -> Observable.fromIterable(dataBeen) }.subscribeOn(Schedulers.io()).subscribe(object : Observer<MusicIndex.ItemInfoListBean.MusicBean> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(data: MusicIndex.ItemInfoListBean.MusicBean) {
                val dataBean = MusicInfo.DataBean()
                dataBean.video = data.video
                if (data.video_info != null) {
                    val videoInfoBean = MusicInfo.DataBean.VideoInfoBean()
                    videoInfoBean.link = data.video_info.link
                    dataBean.video_info = videoInfoBean
                }
                dataBean.id = data.id
                dataBean.song_id = 0
                dataBean.uid = data.uid
                dataBean.title = data.title
                //                        dataBean.setCollection(data.getc);
                val imgpicInfoBean = MusicInfo.DataBean.ImgpicInfoBean()//HomeBean.ImgpicInfoBean();//
                try {
                    imgpicInfoBean.link = data.imgpic_info.link
                } catch (e: RuntimeException) {
                }

                dataBean.imgpic_info = imgpicInfoBean
                dataBean.comment = data.comment
                dataBean.imgpic = data.imgpic
                //                        dataBean.setLyrics(data.getmu());
                dataBean.nickname = data.nickname
                //                        dataBean.setPlaytime(data.getPlaytime());
                PlayList.addToList(context, dataBean)
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError: ")
            }

            override fun onComplete() {
                if (daatList != null && daatList.size > 0) {
                    if (MediaService.playModel < 0) {
                        MediaService.playModel = 2
                    }
                    play(context, daatList[0].id, 0)
                }
            }
        })


    }

    interface ChgCollectCallBack {
        fun chgCollect(dataBean: MusicInfo.DataBean)

        fun onError()
    }

    interface SheetDetialCallBack {
        fun getSheetDetial(sheetDetialBean: SheetDetialBean)
    }

    private fun isCachedMusic(bean: MusicInfo.DataBean?, context: Context): Boolean {
        if (bean?.video_info != null) {
            if (bean.video_info!!.link != null) {
                val proxy = MainApplication.getProxy(context)
                val proxyUrl = proxy.getProxyUrl(bean.video_info!!.link)
                val file = File(proxyUrl)
                val file2 = File(Environment.getExternalStorageDirectory().absolutePath + "/yyt_music/" + bean.title + ".mp3")
                return file.exists() || file2.exists()
            }
        }
        return false
    }

    @JvmStatic
    fun nextPlayIntent(mContext: Context, i: Int) {
        GetMusicInfo.playById(mContext!!, i, 0, object : GetMusicInfo.SetBeanData {
            override fun setBeanData(dataBean: MusicInfo.DataBean?) {
                if (null != playList && dataBean != null) {
                    for (i in playList.indices) {
                        if (playList[i].id == dataBean.id) {
                            val dataBean1 = playList[i]
                            dataBean1.comment = dataBean.comment
                            dataBean1.collection = dataBean.collection
                        }
                    }
                }
                MediaService.bean = dataBean
                mContext.sendBroadcast(Intent("startplay"))
                PlayList.addToList(mContext, dataBean)
                MediaService.getNextPlayList().add(dataBean)
                if (!MediaService.getMediaPlayer().isPlaying) {
                    startServiceToPlay(mContext, dataBean)
                }
                if (null != mContext) {
                    Toast.create(MainApplication.context).show("添加下一首至列表成功")
                }
            }
        })
    }
}
