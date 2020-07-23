package com.mxkj.yuanyintang.base.activity

import android.graphics.Bitmap
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.extraui.gift.CheckBean
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import com.sina.weibo.sdk.utils.UIUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_gii_gift_success.*
import java.util.concurrent.TimeUnit

class GiiGiftSuccessActivity : AppCompatActivity() {
    var cCount = 1f
    var realWidth = 0
    private var giftRes: Bitmap? = null
    lateinit var dataBean: CheckBean.DataBean
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.baseTransparentStatusBar(this)
        setContentView(R.layout.activity_gii_gift_success)
        realWidth = ll_gift_bck.width
        realWidth = UIUtils.dip2px(210, this@GiiGiftSuccessActivity)
        dataBean = intent.getSerializableExtra("GIFT_BEAN") as CheckBean.DataBean
        playAni(this.dataBean)
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(0, 0)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    /**
     * 播放赠送完成动画
     */
    private fun playAni(dataBean: CheckBean.DataBean) {
        val type = dataBean.type
        Log.e("TAG", "礼物类型$type")
        when (type) {
            2 -> {
                tv_my_gift_name.text = "我打赏了" + dataBean.name + ""
                if (dataBean.icon_info != null) {
                    smallAniOpen(dataBean)
                }
            }
            1 -> {
                ll_gift_success.visibility = View.VISIBLE
                when {
                    dataBean.imgpic_info != null -> {
                        val controllerListener = object : BaseControllerListener<ImageInfo>() {
                            override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, anim: Animatable?) {
                                anim?.start()
                            }
                        }
                        val builder = Fresco.newDraweeControllerBuilder()
                        builder.setUri(dataBean.imgpic_info?.link)
                        val controller = builder.setControllerListener(controllerListener).build()
                        img_my_gift.controller = controller
                    }
                }
                img_dismiss.setOnClickListener({ finish() })
            }
        }
        Handler().postDelayed({ finish() }, 5000)
    }

    /**
     * 打开小礼物效果
     */
    private fun smallAniOpen(dataBean: CheckBean.DataBean) {

        Glide.with(MainApplication.application).load(dataBean.imgpic_info?.link).asBitmap().into(img_gift_ani)

        Glide.with(MainApplication.application).load(dataBean.icon_info?.link).asBitmap().into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                img_head_icon_gift_ani.setImageBitmap(resource)
                img_head_icon_gift_ani.visibility = View.VISIBLE
                val startWidth = realWidth
                val i = (startWidth / 5.0f)
                val layoutParams = ll_gift_bck.layoutParams
                layoutParams.width = 0
                ll_gift_bck.layoutParams = layoutParams
                rl_gift_ani.visibility = View.VISIBLE
                Observable.interval(0, 100, TimeUnit.MILLISECONDS)
                        .take(5)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<Long> {
                            override fun onSubscribe(@NonNull d: Disposable) {}

                            override fun onNext(t: Long) {
                                layoutParams.width = (cCount * i).toInt()
                                if (cCount == 1f) {
                                    ll_gift_bck.background.alpha = 10
                                } else {
                                    ll_gift_bck.background.alpha = (255 / 5 * cCount).toInt()
                                }
                                ll_gift_bck.layoutParams = layoutParams
                                cCount++
                            }

                            override fun onError(@NonNull e: Throwable) {}

                            override fun onComplete() {
                                cCount = 0f
                                img_gift_ani.visibility = View.VISIBLE
                                Handler().postDelayed({ smallAniClose() }, 800)
                            }
                        })
            }

            override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {}
        })
    }

    /**
     * 关闭小礼物动画
     */
    private fun smallAniClose() {
        img_gift_ani.visibility = View.GONE
        val i = (realWidth / 10.0).toFloat()
        val layoutParams = ll_gift_bck.layoutParams
        Observable.interval(0, 100, TimeUnit.MILLISECONDS)
                .take(5)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Long> {
                    override fun onSubscribe(@NonNull d: Disposable) {}

                    override fun onNext(t: Long) {
                        val currWidth = (realWidth - cCount * i).toInt()
                        layoutParams.width = if (currWidth > 0) currWidth else 0
                        ll_gift_bck.layoutParams = layoutParams
                        cCount++
                        if (cCount >= 5) {
                            ll_gift_bck.background.alpha = 10
                        } else {
                            ll_gift_bck.background.alpha = (255 - 255 / 5 * cCount).toInt()
                        }
                    }

                    override fun onError(@NonNull e: Throwable) {}
                    override fun onComplete() {
                        cCount = 0f
                        rl_gift_ani.visibility = View.GONE
                        val layoutParams = ll_gift_bck.layoutParams
                        layoutParams.width = realWidth
                        ll_gift_bck.layoutParams = layoutParams
                        finish()
                    }
                })
    }

}
