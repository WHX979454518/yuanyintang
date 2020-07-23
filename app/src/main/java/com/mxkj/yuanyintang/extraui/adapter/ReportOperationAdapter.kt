package com.mxkj.yuanyintang.extraui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.TextView

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.extraui.bean.ReportOperationBean
import com.mxkj.yuanyintang.utils.string.StringUtils
class ReportOperationAdapter(data: List<ReportOperationBean.DataBean>, private val context: Context) : BaseQuickAdapter<ReportOperationBean.DataBean, BaseViewHolder>(R.layout.item_report_operation, data) {
    override fun convert(helper: BaseViewHolder, item: ReportOperationBean.DataBean, position: Int) {
        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.title))
        val drawable = ContextCompat.getDrawable(context, R.drawable.home_index_more)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        helper.getView<TextView>(R.id.tv_title).setCompoundDrawables(null, null, drawable, null) //设置右图标
    }
}
