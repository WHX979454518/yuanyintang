package com.mxkj.yuanyintang.base.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.text.TextUtils
import android.util.Log

import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.database.DBManager
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.LocalMusicListActivity
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.MusicLoader
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.SongDao
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.widget.emoji.FaceBean

import java.util.ArrayList

import okhttp3.Headers

import com.mxkj.yuanyintang.utils.constant.Constants.Other.EM_LOGIN
import com.mxkj.yuanyintang.utils.file.SdUtil
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import java.io.File

class BackgroundService : Service() {
    override fun onCreate() {
        Log.i(TAG, "onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Thread(Runnable {
            val extSDCardPath = SdUtil.getExtSDCardPathList()
            if (extSDCardPath != null && extSDCardPath.size > 0) {
                for (i in extSDCardPath.indices) {
                    Log.e(TAG, extSDCardPath[i])
                    val sdPath = File(extSDCardPath[i])
//                    reloadAdapter(sdPath)
                }
            }
        }).start()
        getEmoji()
        sendBroadcast(Intent(EM_LOGIN))
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 表情
     */
    private fun getEmoji() {
        NetWork.getFace(this, null, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                Thread(Runnable {
                    var x_end_time: String? = ""
                    if (headers != null) {
                        x_end_time = headers.get("X_End_Time")
                    }
                    CacheUtils.setString(MainApplication.Companion.application, "emojiEndTime", if (TextUtils.isEmpty(x_end_time)) "" else x_end_time)
                    val faceBean = JSON.parseObject(resultData, FaceBean::class.java)
                    val faceList = faceBean.data
                    val faceListLocal = ArrayList<FaceBean.DataBean>()
                    for (face in faceList) {
                        try {
                            if (!TextUtils.isEmpty(face.imgpic_link)) {
                                val bean = FaceBean.DataBean()
                                bean.imgpic_link = face.imgpic_link
                                bean.id = face.id
                                bean.title = face.title
                                bean.emoji = face.emoji
                                faceListLocal.add(bean)
                                val dbManager = DBManager(applicationContext)
                                dbManager.saveEmoji(faceListLocal)
                            }

                        } catch (e: RuntimeException) {
                        }
                    }
                }).start()
            }

            override fun doError(msg: String) {}

            override fun doResult() {

            }
        })
    }

    private fun reloadAdapter(folders: File) {
        var songDao = SongDao(this)
        songDao.deleteAllHelper()
        Observable.just(folders).flatMap { file -> listFile(file) }.filter { file ->
            val filename = file.name
            val j = filename.lastIndexOf(".")
            val suf = filename.substring(j + 1)//得到文件后缀
            if (suf.equals("mp3", ignoreCase = true)) {
                val songInfo = MusicLoader.getSongInfo(file)
                if (songInfo != null) {
                    var songDao = SongDao(this)
                    if (songInfo.duration > 60) {
                        songDao.add(songInfo)
                    }
                }
            }
            file.exists()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                object : Observer<File> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(file: File) {
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {
                    }
                })
    }

    private fun listFile(file: File): Observable<File> {
        return if (file.isDirectory) {
            Observable.fromArray(*file.listFiles())
                    .flatMap { file -> listFile(file) }
        } else {
            Observable.just(file)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {
        private val TAG = BackgroundService::class.java.simpleName
    }
}
