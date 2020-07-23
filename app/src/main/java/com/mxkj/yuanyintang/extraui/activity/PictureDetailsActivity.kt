package com.mxkj.yuanyintang.extraui.activity

import android.Manifest
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.utils.image.imageloader.config.SingleConfig
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.photopicker.photoview.PhotoViewAttacher
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.concurrent.TimeUnit
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.mxkj.yuanyintang.base.MainApplication
import kotlinx.android.synthetic.main.activity_picture_details.*

class PictureDetailsActivity : BaseActivity() {
    private var url: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_picture_details)
        StatusBarUtil.baseTransparentStatusBar(this)
        ButterKnife.bind(this)
        showLoadingView()
        url = intent.getStringExtra("url")
        if (!TextUtils.isEmpty(url)) {
            Glide.with(this).load(url).asBitmap().into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                    RxPermissions(this@PictureDetailsActivity).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe { permission ->
                                if (permission.granted) {
                                    hideLoadingView()
                                    iv_photoView!!.setImageBitmap(bitmap)
                                } else {
                                    hideLoadingView()
                                    setSnackBar("没有权限", "", R.drawable.icon_success)
                                }
                            }
                }

                override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {}
            })

            iv_photoView?.setOnClickListener {
                finish()
                false
            }

            iv_photoView?.setOnLongClickListener {
                diaLogBuilder?.show()
                false
            }
            iv_photoView?.onViewTapListener = PhotoViewAttacher.OnViewTapListener { _, _, _ -> finish() }
            iv_photoView?.onPhotoTapListener = object : PhotoViewAttacher.OnPhotoTapListener {
                override fun onPhotoTap(view: View, x: Float, y: Float) {
                    finish()
                }

                override fun onOutsidePhotoTap() {

                }
            }
//            fram.setOnClickListener { finish() }
            initDialogBuild()
        }
    }
    private fun initDialogBuild() {
        val view = View.inflate(this, R.layout.dialog_pic_details_operation, null)
        diaLogBuilder = DiaLogBuilder(this)
                .setContentView(view)
                .setFullScreen()
                .setGrvier(Gravity.CENTER)
                .setCanceledOnTouchOutside(true)
                .setAniMo(R.anim.popup_in)
        val tv_download_pic = view.findViewById<TextView>(R.id.tv_download_pic)
        val tv_share = view.findViewById<TextView>(R.id.tv_share)
        val v_line = view.findViewById<View>(R.id.v_line)
        tv_share.visibility = View.GONE
        v_line.visibility = View.GONE
        RxView.clicks(tv_download_pic)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    RxPermissions(this@PictureDetailsActivity).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe { permission ->
                                if (permission.granted) {
                                    setSnackBar("保存成功", "", R.drawable.icon_success)
                                }
                            }
                    diaLogBuilder?.setDismiss()
                }
    }



    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.drop_down, R.anim.drop_down)
    }
    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    var x1 = 0f
    var x2 = 0f
    var y1 = 0f
    var y2 = 0f
    override fun  onTouchEvent (event: MotionEvent?): Boolean {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if(event!!.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if(y1 - y2 > 50) { } else if(y2 - y1 > 5) {
                finish()
                overridePendingTransition(R.anim.drop_down, R.anim.drop_down)
            } else if(x1 - x2 > 50) {} else if(x2 - x1 > 5) {}
        }
        return super.onTouchEvent(event);
    }
}
