package com.mxkj.yuanyintang.base.activity

import android.Manifest
import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager

import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.hyphenate.chat.EMClient
import com.jakewharton.rxbinding2.view.RxView
import com.luck.picture.lib.tools.ScreenUtils
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.base.bean.StartImgBean
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew
import com.mxkj.yuanyintang.mainui.web.CommonWebview
import com.mxkj.yuanyintang.musicplayer.activity.PlayerActivity
import com.mxkj.yuanyintang.musicplayer.play_control.GetMusicInfo
import com.mxkj.yuanyintang.musicplayer.service.MediaService.bean
import com.mxkj.yuanyintang.net.HttpUtils
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.TransformationUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.image.imageloader.utils.ImageUtil
import com.mxkj.yuanyintang.utils.threadpool.ThreadPoolManager
import com.mxkj.yuanyintang.utils.uiutils.Srceen
import com.tbruyelle.rxpermissions2.RxPermissions

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_picture_details.*
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.dialog.*
import okhttp3.Headers

class WelComeActivity : BaseActivity() {
    private val countTimeDisposable: Disposable? = null
    private var disposable: Disposable? = null
    //    private var animSet: AnimatorSet? = null
    private var startImgBean: StartImgBean? = null
    private var w: Int = 0
    private var startImgJson: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        cacheClear()
//        cacheHome()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_welcome)
        // 如果登录过，APP 长期在后台再进的时候也可能会导致加载到内存的群组和会话为空，
        // 可以在主页面的oncreate 里也加上这两句代码，当然，更好的办法应该是放在程序的开屏页
        //如果不提前载入，会导致首次点击消息中心进入消息中心后页面空白（环信消息记录必须在进入聊天界面之前加入到内存中）
        EMClient.getInstance().chatManager().loadAllConversations()
        EMClient.getInstance().groupManager().loadAllGroups()

        HttpUtils.getToken(this@WelComeActivity, object : HttpUtils.DoNext() {
            override fun doResult() {

            }
        })

        initData()
        RxView.clicks(tv_skip).throttleFirst(1, TimeUnit.SECONDS).subscribe {
                    disposable()
                    isFirst()
                }
        RxView.clicks(img_ad).throttleFirst(1, TimeUnit.SECONDS).subscribe(Consumer {
                    isFirst()

                    NetWork.getStartAddCount(startImgBean!!.data.id.toString(), this@WelComeActivity, object : NetWork.TokenCallBack {
                        override fun doNext(resultData: String, headers: Headers?) {
                        }

                        override fun doError(msg: String) {

                        }

                        override fun doResult() {
                        }
                    })

                    if (null == startImgBean) {
                        return@Consumer
                    }
                    when {
                        startImgBean?.data?.type == "web" -> {
                            val url = startImgBean?.data?.target_url
                            val intent = Intent(this@WelComeActivity, CommonWebview::class.java)
                            intent.putExtra("url", url)
                            intent.putExtra("title", startImgBean?.data?.title)
                            startActivity(intent)
                        }
                        startImgBean?.data?.type == "topic" -> {
                            val url = startImgBean?.data?.target_url
                            val intent = Intent(this@WelComeActivity, PondDetialActivityNew::class.java)
                            val bundle = Bundle()
                            bundle.putInt(PondDetialActivityNew.PID, Integer.parseInt(url))
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                        startImgBean?.data?.type == "activity" -> {
                            val url = startImgBean?.data?.target_url
                            val intent = Intent(this@WelComeActivity, CommonWebview::class.java)
                            intent.putExtra("url", url)
                            intent.putExtra("activity", "activity")
                            intent.putExtra("title", startImgBean?.data?.title)
                            intent.putExtra("img", startImgBean?.data?.title)
                            startActivity(intent)
                        }
                        startImgBean?.data?.type == "music" -> {
                            val target_url = startImgBean?.data?.target_url ?: "0"
                            GetMusicInfo.playById(this, target_url?.toInt(), 0, object : GetMusicInfo.SetBeanData {
                                override fun setBeanData(dataBean: MusicInfo.DataBean?) {
                                    if (dataBean == null) return
                                    bean = JSON.parseObject(JSON.toJSONString(dataBean), MusicInfo.DataBean::class.java)
                                    startActivity(Intent(this@WelComeActivity, PlayerActivity::class.java))
                                }
                            })
                        }
                        startImgBean?.data?.type == "musician" -> {
                            val intent = Intent(this@WelComeActivity, MusicIanDetailsActivity::class.java)
                            val bundle = Bundle()
                            bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, startImgBean?.data?.target_url + "")
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                        startImgBean?.data?.type == "song" -> {
                            val intent = Intent(this@WelComeActivity, SongSheetDetailsActivity::class.java)
                            val bundle = Bundle()
                            bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, startImgBean!!.data.target_url + "")
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                    }
                })
        val screen = Srceen.getScreen(this)
        w = screen[0]
        CacheUtils.setInt(this, Constants.Other.WIDTH, ImageUtil.px2dip(this, w))
    }

    private fun cacheHome() {
//        NetWork.getHome(false, this@WelComeActivity, object : NetWork.TokenCallBack {
//            override fun doNext(resultData: String, headers: Headers?) {
//                Log.e("homeJson---",resultData);
//                CacheUtils.setString(this@WelComeActivity, "homeJson", resultData)
//            }
//
//            override fun doError(msg: String) {
//
//            }
//
//            override fun doResult() {
//            }
//        })
    }

    private fun initData() {
        startImgJson = CacheUtils.getString(this, Constants.Other.SPLASH_PIC_DATA)
        if (!TextUtils.isEmpty(startImgJson)) {
            try {
                ImageLoader.with(this@WelComeActivity).res(R.drawable.bg_welcome).into(img_ad)

                startImgBean = JSON.parseObject(startImgJson, StartImgBean::class.java)
//                ImageLoader.with(this@WelComeActivity).url(startImgBean!!.data.imgpic_info.link).into(img_ad)
                Glide.with(this@WelComeActivity).load(startImgBean!!.data.imgpic_info.link).into(img_ad)
                Log.e("immmmmmm",""+startImgBean!!.data.imgpic_info.link)
//                Glide.with(this)
//                        .load(startImgBean!!.data.imgpic_info.link)
//                        .asBitmap()
//                        .into(TransformationUtils(img_ad));
//                Glide.with(this).load(startImgBean!!.data.imgpic_info.link).asBitmap().into(object : SimpleTarget<Bitmap>() {
//                    override fun onResourceReady(bitmap: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
//                        RxPermissions(this@WelComeActivity).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                                .subscribe { permission ->
//                                    if (permission.granted) {
////                                        hideLoadingView()
//                                        img_ad!!.setImageBitmap(bitmap)
//                                    } else {
////                                        hideLoadingView()
//                                    }
//                                }
//                    }
//
//                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {}
//                })



                val show_time = startImgBean!!.data.show_time
                startCountDownTime(show_time)
            } catch (e: RuntimeException) {
                isFirst()
            }

            tv_skip!!.visibility = View.VISIBLE
            img_ad!!.visibility = View.VISIBLE
            iv_bg_img!!.visibility = View.GONE

        } else {
            img_ad!!.visibility = View.GONE
            tv_skip!!.visibility = View.GONE
            giftEffect()
        }
    }

    private fun startCountDownTime(show_time: Int) {
        Observable.interval(0, 1, TimeUnit.SECONDS).take(show_time.toLong()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<Long> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: Long) {
                        tv_skip!!.text = "跳过" + (show_time - t!! - 1)
                    }

                    override fun onError(@NonNull e: Throwable) {
                        Log.d(TAG, "onError: --------->")
                        isFirst()
                    }

                    override fun onComplete() {
                        isFirst()
                    }
                })

    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun giftEffect() {
        iv_bg_img!!.setImageResource(R.drawable.bg_welcome)
        isFirst()
//        启动的时候源小伊的放大动画
//        animSet = AnimatorSet()
//        animSet!!.addListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator) {
//
//            }
//
//            override fun onAnimationEnd(animation: Animator) {
//                isFirst()
//            }
//
//            override fun onAnimationCancel(animation: Animator) {}
//
//            override fun onAnimationRepeat(animation: Animator) {}
//        })
//        val animatorList = ArrayList<Animator>()
//        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        val pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.4f)
//        val pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.4f)
//        val animator = ObjectAnimator.ofPropertyValuesHolder(iv_bg_img, pvhX, pvhY)
//        val pvhX1 = PropertyValuesHolder.ofFloat("scaleX", 1.4f, 1f)
//        val pvhY1 = PropertyValuesHolder.ofFloat("scaleY", 1.4f, 1f)
//        val animator1 = ObjectAnimator.ofPropertyValuesHolder(iv_bg_img, pvhX1, pvhY1)
//        animatorList.add(animator)
//        animatorList.add(animator1)
//        animSet!!.playSequentially(animatorList)
//        animSet!!.duration = 300
//        if (!animSet!!.isRunning) {
//            animSet!!.start()
//        }
    }

    private fun isFirst() {
        val preferences = getSharedPreferences("yyt_app", Context.MODE_PRIVATE)
        val isFirst = preferences.getBoolean("app_start", true)
        val intent = Intent()
        if (isFirst) {
            intent.setClass(this, StartActivity::class.java)
            startActivity(intent)
            preferences.edit().putBoolean("app_start", false).commit()
            CacheUtils.setBoolean(this, Constants.Other.IS_SETTING_MSG_SOUND, true)
            CacheUtils.setBoolean(this, Constants.Other.IS_SETTING_MSG_VIBRATE, true)
            CacheUtils.setBoolean(this, Constants.Other.IS_SETTING_MSG_SPEAKER, true)
            CacheUtils.setBoolean(this, Constants.Other.IS_SETTING_MSG_NOTIFICATION, true)
            CacheUtils.setLong(this, Constants.User.FIRST_TIME, System.currentTimeMillis())
        } else {
            intent.setClass(this, HomeActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

    private fun disposable() {
        disposable?.let {
            if (!disposable?.isDisposed!!) {
                disposable?.dispose()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
//        if (null != animSet) {
//            animSet!!.cancel()
//        }
//        if (null != animSet) {
//            animSet = null
//        }
        disposable()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    companion object {
        private const val DELAY_TIME = 3
        private const val COUNT_TIME = 4
        private const val TAG = "WelComeActivity"
    }
}
