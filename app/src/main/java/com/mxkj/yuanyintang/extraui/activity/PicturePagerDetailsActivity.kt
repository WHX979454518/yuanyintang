package com.mxkj.yuanyintang.extraui.activity

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.github.chrisbanes.photoview.PhotoView
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.image.FileType
import com.mxkj.yuanyintang.utils.image.imageloader.config.SingleConfig
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.net.ApiAddress
import com.mxkj.yuanyintang.mainui.dynamic.activity.DynamicDetial
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.bean.PictureDataBean
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder
import com.mxkj.yuanyintang.utils.RichTextutils.ImageTextUtil
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.image.ImageUtils
import com.mxkj.yuanyintang.widget.CircleProgressBar
import com.mxkj.yuanyintang.widget.largeImage.LargeImageView
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.FileCallBack

import java.io.File
import java.util.ArrayList
import java.util.concurrent.TimeUnit

import butterknife.ButterKnife
import com.mxkj.yuanyintang.utils.app.GetUserAgent
import com.zhy.http.okhttp.builder.GetBuilder
import io.reactivex.functions.Consumer
import okhttp3.Call
import kotlinx.android.synthetic.main.activity_picture_pager_details.*
class PicturePagerDetailsActivity : StandardUiActivity() {
    internal var isShowNavigationBar = true
    private lateinit var pictureDataBean: PictureDataBean
    private var pictureDetailsAdapter: PictureDetailsAdapter? = null
    internal var imgList: MutableList<String> = ArrayList()
    private var picPosition = -1
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun setLayoutId(): Int {
        StatusBarUtil.baseTransparentStatusBar(this)
        return R.layout.activity_picture_pager_details
    }

    override fun initView() {
        hideTitle(true)
        pictureDataBean = intent.getSerializableExtra(PICTURE_DATA) as PictureDataBean
        ButterKnife.bind(this)
        tv_title_num.text = "0/0"
        leftBT.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.close_back_white), null, null, null)
        rightBT.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.icon_more_white), null)
        RxView.clicks(leftBT)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { finish() }
        RxView.clicks(rightBT)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    diaLogBuilder.show()
                }
        RxView.clicks(layout_click_pond)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(Consumer {
                    if (!TextUtils.isEmpty(pictureDataBean.type)) {
                        //根据type判断跳转的页面
                        if (pictureDataBean.type == "pond") {
                            if (null == pictureDataBean || TextUtils.isEmpty(pictureDataBean.id)) {
                                return@Consumer
                            }
                            val bundle = Bundle()
                            bundle.putInt(PondDetialActivityNew.PID, Integer.parseInt(pictureDataBean.id))
                            goActivity(PondDetialActivityNew::class.java, bundle)
                        } else if (pictureDataBean.type == "dynamic") {
                            if (null == pictureDataBean || TextUtils.isEmpty(pictureDataBean.id)) {
                                return@Consumer
                            }
                            val intent = Intent(this@PicturePagerDetailsActivity, DynamicDetial::class.java)
                            intent.putExtra("dynamicId", Integer.parseInt(pictureDataBean.id))
                            startActivity(intent)
                        }
                    }
                })
        if (null == pictureDataBean) {
            return
        }

        tv_read_num.visibility = if (null != pictureDataBean.type) if (pictureDataBean.type == "dynamic") View.GONE else View.VISIBLE else View.GONE
        tv_name.text = StringUtils.isEmpty(pictureDataBean.nickname)
        tv_read_num.text = StringUtils.setNum(pictureDataBean.hits)
        ImageTextUtil.setImageText(tv_title, StringUtils.isEmpty(pictureDataBean.title))
        initViewPager()
        initDialogBuild()
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

        if (null != pictureDataBean.type && pictureDataBean.type == "pond") {
            tv_share.text = "分享池塘"
        } else {
            tv_share.visibility = View.GONE
        }

        RxView.clicks(tv_download_pic)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (picPosition == -1) {
                        setSnackBar("当前没有图片", "", R.drawable.icon_fails)
                    } else {
                        RxPermissions(this@PicturePagerDetailsActivity).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .subscribe { permission ->
                                    if (permission.granted) {
                                        val imgPic = pictureDataBean.photoList?.get(picPosition)

                                        ImageLoader.with(this@PicturePagerDetailsActivity).url(imgPic).asBitmap(object : SingleConfig.BitmapListener {
                                            override fun onSuccess(bitmap: Bitmap) {
                                                ImageUtils.saveImageToGallery(this@PicturePagerDetailsActivity, bitmap)
                                            }

                                            override fun onFail() {

                                            }
                                        })
                                    }
                                }
                    }
                    diaLogBuilder.setDismiss()
                }

        /**
         * 分享话题
         */
        RxView.clicks(tv_share)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val sharePond = MusicBean()
                    val shareDataBean = MusicBean.ShareDataBean()
                    shareDataBean.nickname = pictureDataBean.nickname
                    shareDataBean.title = pictureDataBean.title
                    shareDataBean.type = "pond"
                    shareDataBean.muisic_id = Integer.valueOf(pictureDataBean.id)
                    shareDataBean.nickname = pictureDataBean.nickname
                    shareDataBean.title = pictureDataBean.nickname
                    if (pictureDataBean?.photoList != null && pictureDataBean.photoList!!.isNotEmpty()) {
                        shareDataBean.webImgUrl = pictureDataBean.photoList!![0]
                    }
                    val shareUrl = ApiAddress.H5_BASE_URL + "topic/detail?id=" + pictureDataBean.id
                    shareDataBean.shareUrl = shareUrl
                    sharePond.shareDataBean = shareDataBean
                    val shareBottomDialog = ShareBottomDialog(this@PicturePagerDetailsActivity, sharePond)
                    shareBottomDialog.show()
                    diaLogBuilder.setDismiss()
                }
    }

    private fun initViewPager() {
        imgList.clear()
        pictureDataBean.photoList?.let { imgList.addAll(it) }
        if (pictureDataBean.photoList?.isNotEmpty()!!) {
            picPosition = pictureDataBean.position
        }
        pictureDetailsAdapter = PictureDetailsAdapter()
        viewPager.adapter = pictureDetailsAdapter
        tv_title_num.text = (pictureDataBean.position + 1).toString() + "/" + imgList.size
        viewPager.currentItem = pictureDataBean.position
        viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                picPosition = position
                tv_title_num.text = (position + 1).toString() + "/" + pictureDataBean.photoList!!.size
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    override fun initData() {

    }

    override fun initEvent() {

    }

    inner class PictureDetailsAdapter : PagerAdapter() {


        override fun getCount(): Int {
            return imgList.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val loadingView = LayoutInflater.from(this@PicturePagerDetailsActivity).inflate(R.layout.layout_img_loading, null)
            val progressbar = loadingView.findViewById<CircleProgressBar>(R.id.progressbar)
            val photoView = loadingView.findViewById<LargeImageView>(R.id.photo_view)
            val imageView = loadingView.findViewById<PhotoView>(R.id.image_view)
            val bck = loadingView.findViewById<TextView>(R.id.bck)
            val s = imgList[position] + "?format=0"
            val replace = s.replace("/", "")
            if (pictureDataBean != null) {
                val file = File(Constants.Path.APP_CACHE_PATH + File.separator + "gif", replace)
                if (!file.exists()) {

                    OkHttpUtils.get()
                            .addHeader("Accept","image/webp,image/apng,image/*,*/*;q=0.8")
                            .addHeader("Accept-Encoding","gzip, deflate, br")
                            .addHeader("Accept-Language","zh-CN,zh;q=0.9")
                            .addHeader("User-Agent",GetUserAgent().userAgent)
                            .url(s).build().execute(object : FileCallBack(Constants.Path.APP_CACHE_PATH + File.separator + "gif", replace) {
                        override fun onError(call: Call, e: Exception, id: Int) {
                            Log.e(TAG, "onError: $e")
                        }
                        override fun onResponse(response: File, id: Int) {
                            progressbar.visibility = View.GONE
                            bck.visibility = View.GONE
                            val fileType = FileType.getFileType(response.absolutePath)
                            if (fileType != null && fileType.contains("gif")) {
                                imageView.visibility = View.VISIBLE
                                photoView.visibility = View.GONE
                                Glide.with(this@PicturePagerDetailsActivity).load(response).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView)
                            } else if (fileType != null && !fileType.contains("gif")) {
                                imageView.visibility = View.GONE
                                photoView.visibility = View.VISIBLE
                                Glide.with(this@PicturePagerDetailsActivity).load(response).asBitmap().into(object : SimpleTarget<Bitmap>() {
                                    override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                                        photoView.setImage(resource)
                                    }
                                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {}
                                })
                            } else {
                                imageView.visibility = View.VISIBLE
                                photoView.visibility = View.GONE
                                Glide.with(this@PicturePagerDetailsActivity).load(response).into(imageView)
                            }
                        }

                        override fun inProgress(progress: Float, total: Long, id: Int) {
                            super.inProgress(progress, total, id)
                            progressbar.setProgress((progress * 100).toInt())

                        }
                    })
                } else {
                    progressbar.visibility = View.GONE
                    val fileType = FileType.getFileType(file.absolutePath)
                    if (fileType != null && fileType.contains("gif")) {
                        imageView.visibility = View.VISIBLE
                        photoView.visibility = View.GONE
                        Glide.with(this@PicturePagerDetailsActivity).load(file).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView)
                    } else if (fileType != null && !fileType.contains("gif")) {
                        imageView.visibility = View.GONE
                        photoView.visibility = View.VISIBLE
                        Glide.with(this@PicturePagerDetailsActivity).load(file).asBitmap().into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                                photoView.setImage(resource)
                            }

                            override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {}
                        })
                    } else {
                        imageView.visibility = View.VISIBLE
                        photoView.visibility = View.GONE
                        Glide.with(this@PicturePagerDetailsActivity).load(file).into(imageView)
                    }
                }

                photoView.setOnClickListener {
                    isShowNavigationBar = if (isShowNavigationBar == true) {
                        rl_NavigationBar.visibility = View.GONE
                        layout_click_pond.visibility = View.GONE
                        false
                    } else {
                        rl_NavigationBar.visibility = View.VISIBLE
                        layout_click_pond.visibility = View.VISIBLE
                        true
                    }
                }


                imageView.setOnPhotoTapListener { _, _, _ ->
                    isShowNavigationBar = if (isShowNavigationBar == true) {
                        rl_NavigationBar.visibility = View.GONE
                        layout_click_pond.visibility = View.GONE
                        false
                    } else {
                        rl_NavigationBar.visibility = View.VISIBLE
                        layout_click_pond.visibility = View.VISIBLE
                        true
                    }
                }

                photoView.setOnLongClickListener {
                    if (null != diaLogBuilder) {
                        diaLogBuilder.show()
                    }
                    false
                }
                container.addView(loadingView)
            }

            return loadingView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        imgList.clear()
        viewPager.removeAllViews()
        System.gc()
        pictureDetailsAdapter?.notifyDataSetChanged()
    }

    companion object {

        const val PICTURE_DATA = "_picture_data"
    }
}
