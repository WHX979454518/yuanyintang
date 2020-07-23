package com.mxkj.yuanyintang.extraui.adapter

import android.content.Context

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.extraui.bean.TitleOperationBean
import com.mxkj.yuanyintang.utils.string.StringUtils

class TitleOperationAdapter(data: List<TitleOperationBean>, private val context: Context) : BaseQuickAdapter<TitleOperationBean, BaseViewHolder>(R.layout.item_title_operation, data) {
    override fun convert(helper: BaseViewHolder, item: TitleOperationBean, position: Int) {
        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.title))

    }
}
