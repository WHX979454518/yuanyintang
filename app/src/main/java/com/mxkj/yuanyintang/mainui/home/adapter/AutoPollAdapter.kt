package com.mxkj.yuanyintang.mainui.home.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseViewHolder
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.home.bean.MusicGiftListBean
import com.mxkj.yuanyintang.utils.string.StringUtils

import de.hdodenhof.circleimageview.CircleImageView

class AutoPollAdapter(private val mContext: Context, private val mData: List<MusicGiftListBean.DataBean>) : RecyclerView.Adapter<BaseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_music_gift_list, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (mData[position % mData.size] == null) return
        val dataBean = mData[position % mData.size]
        holder.setText(R.id.nick_name, StringUtils.isEmpty(dataBean.nickname))
        holder.setText(R.id.gift_name, StringUtils.isEmpty(dataBean.gift_name))
        val img_gift = holder.getView<CircleImageView>(R.id.img_gift)
        if (dataBean.icon_info != null && dataBean.icon_info.link != null) {
            Glide.with(MainApplication.application).load(dataBean.icon_info.link).into(img_gift)
        }
    }

    override fun getItemCount(): Int {
        return if (mData.size > 2) {
            Integer.MAX_VALUE
        } else {
            mData.size
        }
    }
}