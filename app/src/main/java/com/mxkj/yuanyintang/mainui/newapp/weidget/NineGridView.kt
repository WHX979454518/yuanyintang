package com.mxkj.yuanyintang.mainui.newapp.weidget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast

import com.linsh.lshutils.view.RatioImageView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.extraui.activity.PicturePagerDetailsActivity
import com.mxkj.yuanyintang.extraui.bean.PictureDataBean
import com.mxkj.yuanyintang.mainui.newapp.pond.PondInfo
import com.mxkj.yuanyintang.utils.image.imageloader.config.SingleConfig
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader

class NineGridView : NineGridLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun displayOneImage(imageView: RoundCornorImageView, url: String, parentWidth: Int): Boolean {

        ImageLoader.with(mContext)
                .rectRoundCorner(5)
                .url(url)
                .placeHolder(R.drawable.nothing)
                .error(R.drawable.nothing)
                .asBitmap(object : SingleConfig.BitmapListener {
                    override fun onSuccess(bitmap: Bitmap) {
                        val w = bitmap.width
                        val h = bitmap.height
                        imageView.setImageBitmap(bitmap)
                    }

                    override fun onFail() {

                    }
                })

        return false
    }

    override fun displayImage(imageView: RoundCornorImageView, url: String) {
        ImageLoader.with(mContext)
                .getSize(400, 400)
                .rectRoundCorner(5)
                .url(url)
                .placeHolder(R.drawable.nothing)
                .error(R.drawable.nothing)
                .asBitmap(object : SingleConfig.BitmapListener {
                    override fun onSuccess(bitmap: Bitmap) {
                        imageView.setImageBitmap(bitmap)
                    }

                    override fun onFail() {

                    }
                })

    }

    override fun onClickImage(position: Int, url: String, urlList: List<String>) {
        if (dataBean == null) return
        val bundle = Bundle()
        val pictureDataBean = PictureDataBean()
                .setId(dataBean.id.toString())
                .setCommentNum(dataBean.hits)
                .setPhotoList(urlList)
                .setTitle(dataBean.title)
                .setNickname(dataBean.nickname!!)
                .setPosition(position)
                .setHits(dataBean.hits)
                .setType("dynamic")
        bundle.putSerializable(PicturePagerDetailsActivity.PICTURE_DATA, pictureDataBean)
        val intent = Intent(mContext, PicturePagerDetailsActivity::class.java)
        intent.putExtras(bundle)
        mContext.startActivity(intent)
    }


    companion object {
        protected val MAX_W_H_RATIO = 3
    }

    private lateinit var dataBean: PondInfo.DataBean.DataListBean

    fun setDataBean(dataBean: PondInfo.DataBean.DataListBean) {
        this.dataBean = dataBean
    }
}