package com.mxkj.yuanyintang.mainui.dynamic.adapter

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.pond.bean.PondCommentBean
import com.mxkj.yuanyintang.extraui.activity.PicturePagerDetailsActivity
import com.mxkj.yuanyintang.extraui.bean.PictureDataBean
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.sina.weibo.sdk.utils.UIUtils

import butterknife.BindView
import butterknife.ButterKnife

class ReplyImgAdapter(internal var context: Context, internal var dataBean: PondCommentBean.DataBean?, internal var lists: List<String>) : BaseAdapter() {
    private var imglist_info: List<PondCommentBean.DataBean.ImglistInfoBean>? = null

    init {
        if (dataBean != null) {
            imglist_info = dataBean!!.imglist_info
        }
    }

    override fun getCount(): Int {
        return lists.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View, viewGroup: ViewGroup): View {
        var view: View = LayoutInflater.from(context).inflate(R.layout.pongreply_img_item, null)
        var holder: ViewHolder?
        holder = ViewHolder(view)
        val finalHolder = holder
        val anInt = CacheUtils.getInt(context, Constants.Other.WIDTH, 0)
        var imgUrl = lists[position]
        imgUrl += if (imgUrl.contains("?")) {
            "&w=" + UIUtils.dip2px(anInt, context) + "&h=0"
        } else {
            "?log_at=3&w=" + UIUtils.dip2px(anInt, context) + "&h=0"
        }
        if (imglist_info != null && imglist_info!!.isNotEmpty()) {
            val ext = imglist_info!![position].ext
            if (ext == "gif") {
                imgUrl = lists[position] + "/" + UIUtils.dip2px(anInt, context) + "/0/1/0/1/?format=0"
                finalHolder.imgReply!!.setImageResource(R.drawable.nothing)
                val finalImgUrl = imgUrl
                Glide.with(context).load(imgUrl).asBitmap().into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                        val lp = getLayoutParams(context, resource, finalHolder.imgReply)
                        finalHolder.imgReply!!.layoutParams = lp
                        Glide.with(context).load(finalImgUrl).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(finalHolder.imgReply!!)

                    }

                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {}
                })
            } else {
                Glide.with(context).load(imgUrl).asBitmap().into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                        setBitmap(resource, finalHolder.imgReply)
                    }

                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {}
                })
            }
        }
        holder.imgReply.setOnClickListener {
            val bundle = Bundle()
            if (dataBean != null) {
                val pictureDataBean = PictureDataBean()
                        .setId(dataBean!!.id.toString() + "")
                        .setCommentNum(dataBean!!.com_count)
                        .setPhotoList(lists)
                        .setTitle(dataBean!!.content)
                        .setNickname(dataBean!!.nickname)
                        .setPosition(position)
                        .setHits(dataBean!!.hits)
                        .setType("")
                bundle.putSerializable(PicturePagerDetailsActivity.PICTURE_DATA, pictureDataBean)
                val intent = Intent(context, PicturePagerDetailsActivity::class.java)
                intent.putExtras(bundle)
                context.startActivity(intent)
            }
        }
        return view
    }

    internal class ViewHolder(view: View) {
        @BindView(R.id.img_reply)
        lateinit var imgReply: ImageView

        init {
            ButterKnife.bind(this, view)
        }
    }

    private fun setBitmap(file: Bitmap?, imgReply: ImageView?) {
        if (file != null) {
            val width = file.width.toFloat()
            val height = file.height.toFloat()
            if (width > 0 && height > 0) {
                val lp = getLayoutParams(context, file, imgReply)
                imgReply!!.setImageBitmap(file)
                imgReply.layoutParams = lp
            }
        }
    }

    companion object {
        fun getLayoutParams(context: Context, bitmap: Bitmap, imageView: ImageView?): ViewGroup.LayoutParams {
            val m = context.getSystemService(WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            m.defaultDisplay.getMetrics(outMetrics)
            val screenWidth = outMetrics.widthPixels
            val rawWidth = bitmap.width.toFloat()
            val rawHeight = bitmap.height.toFloat()
            var width: Float
            var height: Float
            if (rawWidth > screenWidth) {
                height = screenWidth / rawWidth * rawHeight - UIUtils.dip2px(57, context)
                width = (screenWidth - UIUtils.dip2px(57, context)).toFloat()
            } else {
                width = (screenWidth - UIUtils.dip2px(57, context)).toFloat()
                height = rawHeight * (screenWidth / rawWidth) - UIUtils.dip2px(57, context)
            }
            val params = imageView!!.layoutParams
            params.width = width.toInt()
            params.height = height.toInt()
            return params
        }
    }
}
