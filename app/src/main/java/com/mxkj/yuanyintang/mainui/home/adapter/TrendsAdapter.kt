package com.mxkj.yuanyintang.mainui.home.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.jakewharton.rxbinding2.view.RxView
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.dynamic.activity.DynamicDetial
import com.mxkj.yuanyintang.mainui.dynamic.bean.DynamicBean

import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.mainui.home.utils.GridSpacingItemDecoration
import com.mxkj.yuanyintang.extraui.activity.PicturePagerDetailsActivity
import com.mxkj.yuanyintang.extraui.bean.PictureDataBean
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.RichTextutils.ImageTextUtil
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import okhttp3.Headers

class TrendsAdapter(data: List<DynamicBean.DataBean>, private val context: Context) : BaseQuickAdapter<DynamicBean.DataBean, BaseViewHolder>(R.layout.musician_item_trends, data) {

    override fun convert(helper: BaseViewHolder, item: DynamicBean.DataBean, p: Int) {
        if (p == 0) {
            helper.setVisible(R.id.tv_ta_dynamic, true)
        } else {
            helper.setVisible(R.id.tv_ta_dynamic, false)
        }

        UserInfoUtil.getUserInfoByJson(CacheUtils.getString(mContext, Constants.User.USER_JSON)) { infoBean ->
            if (infoBean?.data != null) {
                if (infoBean.data!!.id == item.uid) {
                    helper.setText(R.id.tv_ta_dynamic, "我的动态")
                } else {
                    helper.setText(R.id.tv_ta_dynamic, "TA的动态")
                }
            }
        }

        ImageLoader.with(mContext)
                .override(40, 40)
                .url(item.head_link)
                .scale(ScaleMode.CENTER_CROP)
                .asCircle()
                .into(helper.getView<ImageView>(R.id.civ_headimg))
        if (item.is_music == 3) {
            helper.setVisible(R.id.iv_is_vip, true)
        } else {
            helper.setVisible(R.id.iv_is_vip, false)
        }
        helper.setVisible(R.id.layout_source, false)

        if (null != item.topic && item.topic!!.id > 0) {
            helper.setVisible(R.id.layout_source, true)
            try {
                ImageLoader.with(mContext)
                        .override(60, 60)
                        .error(R.drawable.defualt_img)
                        .url(item.topic!!.imgpic_info!!.link)
                        .scale(ScaleMode.CENTER_CROP)
                        .asSquare()
                        .into(helper.getView<ImageView>(R.id.iv_cover))
            } catch (e: RuntimeException) {
            }


            helper.setText(R.id.tv_label, StringUtils.isEmpty("池塘"))
            helper.setText(R.id.tv_source_nickname, StringUtils.isEmpty(item.topic!!.content))
            helper.setText(R.id.tv_source_title, StringUtils.isEmpty(item.topic!!.title))
            helper.setVisible(R.id.iv_player_img, false)
            helper.setImageDrawable(R.id.iv_player_img, ContextCompat.getDrawable(mContext, R.drawable.dynamic_unplaying))
        }

        if (null != item.music && item.music!!.id > 0) {
            helper.setVisible(R.id.layout_source, true)
            try {
                ImageLoader.with(mContext)
                        .override(60, 60)
                        .error(R.drawable.defualt_img)
                        .url(item.music!!.imgpic_info!!.link)
                        .scale(ScaleMode.CENTER_CROP)
                        .asSquare()
                        .into(helper.getView<ImageView>(R.id.iv_cover))
            } catch (e: RuntimeException) {
            }


            helper.setText(R.id.tv_label, StringUtils.isEmpty("音乐"))
            helper.setText(R.id.tv_source_nickname, StringUtils.isEmpty(item.music!!.nickname))
            helper.setText(R.id.tv_source_title, StringUtils.isEmpty(item.music!!.title))
            helper.setVisible(R.id.iv_player_img, true)
            if (null != MediaService.bean) {
                if (null != item.music && MediaService.bean!!.id == item.music!!.id) {
                    if (MediaService.getMediaPlayer().isPlaying) {
                        helper.setImageDrawable(R.id.iv_player_img, ContextCompat.getDrawable(mContext, R.drawable.icon_music_list_player_player))
                    } else {
                        helper.setImageDrawable(R.id.iv_player_img, ContextCompat.getDrawable(mContext, R.drawable.dynamic_unplaying))
                    }
                } else {
                    helper.setImageDrawable(R.id.iv_player_img, ContextCompat.getDrawable(mContext, R.drawable.dynamic_unplaying))
                }
            } else {
                helper.setImageDrawable(R.id.iv_player_img, ContextCompat.getDrawable(mContext, R.drawable.dynamic_unplaying))
            }
        }
        if (null != item.song && item.song!!.id > 0) {
            helper.setVisible(R.id.layout_source, true)
            try {
                ImageLoader.with(mContext)
                        .override(60, 60)
                        .error(R.drawable.defualt_img)
                        .url(item.song!!.imgpic_info!!.link)
                        .scale(ScaleMode.CENTER_CROP)
                        .asCircle()
                        .into(helper.getView<ImageView>(R.id.iv_cover))
            } catch (e: RuntimeException) {
            }
            helper.setText(R.id.tv_label, StringUtils.isEmpty("歌单"))
            helper.setText(R.id.tv_source_nickname, StringUtils.isEmpty(item.song!!.nickname))
            helper.setText(R.id.tv_source_title, StringUtils.isEmpty(item.song!!.title))
            helper.setVisible(R.id.iv_player_img, true)
            helper.setImageDrawable(R.id.iv_player_img, ContextCompat.getDrawable(mContext, R.drawable.dynamic_unplaying))
        }
        helper.setText(R.id.tv_nickname, StringUtils.isEmpty(item.nickname))
        ImageTextUtil.setImageText(helper.getView<View>(R.id.tv_contents) as TextView, item.depict!! + "")
        helper.setText(R.id.tv_time, StringUtils.isEmpty(item.create_time))
        helper.setText(R.id.tv_disagree_num, StringUtils.setNum(item.agrees))
        helper.setText(R.id.tv_read_num, StringUtils.setNum(item.comment))
        if (item.is_agree == 0) {
            setAgreesDrawable(helper.getView(R.id.tv_disagree_num), R.drawable.disagree)
        } else {
            setAgreesDrawable(helper.getView(R.id.tv_disagree_num), R.drawable.agreed)
        }
        val img_recycierview = helper.getView<RecyclerView>(R.id.img_recycierview)
        if (item.imglist_info == null || item.imglist_info != null && item.imglist_info!!.size == 0) {
            img_recycierview.visibility = View.GONE
        } else {
            if (null != item.imglist_info) {
                val layoutManager: GridLayoutManager = if (item.imglist_info!!.size == 4) {
                    GridLayoutManager(context, 2)
                } else {
                    GridLayoutManager(context, 3)
                }
                val imgList = ArrayList<String>()
                val imglist_info = item.imglist_info
                for (i in imglist_info!!.indices) {
                    imgList.add(imglist_info[i].link!!)
                }
                img_recycierview.layoutManager = layoutManager
                img_recycierview.addItemDecoration(GridSpacingItemDecoration(3, 0, CommonUtils.dip2px(context, 5f), true))
                val imageAdapter = ImageAdapter(imgList)
                img_recycierview.adapter = imageAdapter
                imageAdapter.setOnItemClickListener { adapter, view, position ->
                    val bundle = Bundle()
                    val pictureDataBean = PictureDataBean()
                            .setId(item.id.toString() + "")
                            .setPhotoList(imgList)
                            .setTitle(item.topic!!.title!!)
                            .setNickname(item.nickname!!)
                            .setPosition(position)
                            .setHits(item.hits)
                    bundle.putSerializable(PicturePagerDetailsActivity.PICTURE_DATA, pictureDataBean)
                    val intent = Intent(mContext, PicturePagerDetailsActivity::class.java)
                    intent.putExtras(bundle)
                    mContext.startActivity(intent)
                }
            }
        }


        helper.getView<View>(R.id.tv_disagree_num).setOnClickListener { agree(item, p) }

        RxView.clicks(helper.getView(R.id.layout_click))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val intent = Intent(mContext, DynamicDetial::class.java)
                    val bundle = Bundle()
                    bundle.putInt(DynamicDetial.DYNAMIC_ID, item.id)
                    intent.putExtras(bundle)
                    mContext.startActivity(intent)
                }

        RxView.clicks(helper.getView(R.id.iv_player_img))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    if (null != item.music && item.music!!.id > 0) {
                        if (null != item.music) {
                            PlayCtrlUtil.play(mContext, item.music!!.id, item.music!!.song_id)
                        }
                    }
                    if (null != item.song && item.song!!.id > 0) {
                        if (null != item.music) {
                            PlayCtrlUtil.playSheet(mContext, item.music!!.song_id.toString() + "")
                        }
                    }
                }
    }

    private fun agree(item: DynamicBean.DataBean, p: Int) {
        val params = HttpParams()
        params.put("type", "3")
        params.put("item_id", item.id.toString())
        NetWork.agree(mContext, params, object : NetWork.TokenCallBack {

            override fun doNext(resultData: String, headers: Headers?) {
                try {
                    val jsonObject = JSON.parseObject(resultData)
                    val code = jsonObject.getInteger("code")!!
                    if (code == 200) {
                        if (item.is_agree == 1) {
                            item.is_agree = 0
                            item.agrees = item.agrees - 1
                        } else {
                            item.is_agree = 1
                            item.agrees = 1 + item.agrees
                        }
                    }
                    notifyDataSetChanged()
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

    private fun setAgreesDrawable(view: TextView, drawable: Int) {
        view.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, drawable), null, null, null)
    }

    internal inner class ImageAdapter(data: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_trend_child_img, data) {

        override fun convert(helper: BaseViewHolder, item: String, position: Int) {
            ImageLoader.with(mContext)
                    .override(160, 160)
                    .url(item)
                    .placeHolder(R.drawable.defualt_img)
                    .scale(ScaleMode.CENTER_CROP)
                    .into(helper.getView<ImageView>(R.id.iv_img))
        }
    }
}
