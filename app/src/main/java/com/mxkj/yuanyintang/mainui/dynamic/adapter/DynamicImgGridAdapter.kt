package com.mxkj.yuanyintang.mainui.dynamic.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.dynamic.bean.DynamicBean
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.config.SingleConfig
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.musicplayer.playcache.CacheListener
import com.mxkj.yuanyintang.utils.image.imageloader.utils.ImageUtil

import java.io.File

class DynamicImgGridAdapter(private val dataList: List<DynamicBean.DataBean.ImglistInfoBean>, private val itemNum: Int, private val mContext: Context) : BaseAdapter(), CacheListener {
    private val inflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = if (itemNum == 2 || itemNum == 4) {
                inflater.inflate(R.layout.dynamic_grid_num2, null)
            } else if (itemNum == 1) {
                inflater.inflate(R.layout.dynamic_grid_num1, null)
            } else {
                inflater.inflate(R.layout.dynamic_grid_num3, null)
            }
        }
        val imgDynamac = convertView!!.findView<ImageView>(R.id.imgDynamac)
        if (dataList.isNotEmpty()) {
            if (itemNum == 2 || itemNum == 4) {
                loadImg(itemNum, convertView, 160, dataList[position], imgDynamac)
            } else if (itemNum == 1) {

                loadImg(itemNum, convertView, 180, dataList[position], imgDynamac)
            } else {
                loadImg(itemNum, convertView, 110, dataList[position], imgDynamac)
            }
        }
        return convertView
    }

    private fun loadImg(itemNum: Int, convertView: View, i: Int, infoBean: DynamicBean.DataBean.ImglistInfoBean, v: ImageView) {
        val link = infoBean.link
        val is_long = infoBean.is_long
        val imgLayoutParams = v.layoutParams
        val anInt = CacheUtils.getInt(MainApplication.application, Constants.Other.WIDTH, 0)
        val width: Int
        val height: Int
        if (itemNum == 2 || itemNum == 4) {
            width = ImageUtil.dip2px(anInt.toFloat()) * 2 / 3
            height = ImageUtil.dip2px(anInt.toFloat()) / 2 - ImageUtil.dip2px(40f)
        } else if (itemNum == 1) {
            width = ImageUtil.dip2px(283f)
            height = ImageUtil.dip2px(214f)
        } else {
            width = ImageUtil.dip2px(anInt.toFloat()) / 3
            height = ImageUtil.dip2px(anInt.toFloat()) / 3
        }
        imgLayoutParams.height = height
        imgLayoutParams.width = width

        v.layoutParams = imgLayoutParams
        if (itemNum == 1) {
            convertView.findView<RelativeLayout>(R.id.ll_img_shuoshuo).layoutParams = imgLayoutParams
        }
        ImageLoader.with(mContext)
                .getSize(400, 400)
                .rectRoundCorner(5)
                .url(link)
                .placeHolder(R.drawable.nothing)
//                .error(R.drawable.nothing)
//                .into(v)
                .asBitmap(object : SingleConfig.BitmapListener {
                    override fun onSuccess(bitmap: Bitmap) {
                        v.setImageBitmap(bitmap)
                    }

                    override fun onFail() {

                    }
                })

        if (is_long == "1") {
            convertView.findView<TextView>(R.id.tv_long_pic).visibility = View.VISIBLE
        } else {
            convertView.findView<TextView>(R.id.tv_long_pic).visibility = View.GONE
        }
    }

    override fun onCacheAvailable(cacheFile: File, url: String, percentsAvailable: Int) {}
    private fun <T : View> View.findView(viewId: Int): T {
        var viewHolder: SparseArray<View> = tag as? SparseArray<View> ?: SparseArray()
        tag = viewHolder
        var childView = viewHolder.get(viewId)
        if (null == childView) {
            childView = findViewById(viewId)
            viewHolder.put(viewId, childView)
        }
        return childView as T
    }


}
