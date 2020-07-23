package com.mxkj.yuanyintang.extraui.adapter

import android.content.Context
import android.view.View

import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.extraui.bean.MusicOperationBean
import com.mxkj.yuanyintang.utils.string.StringUtils
import java.util.concurrent.TimeUnit

class MusicOperationAdapter(data: List<MusicOperationBean>, private val context: Context) : BaseQuickAdapter<MusicOperationBean, BaseViewHolder>(R.layout.item_music_operation, data) {
    override fun convert(helper: BaseViewHolder, item: MusicOperationBean, position: Int) {
        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.title))
        helper.setImageResource(R.id.iv_icon, item.drawable)
        RxView.clicks(helper.getView<View>(R.id.layout_click))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { }
    }
}
