package com.mxkj.yuanyintang.mainui.newapp.weidget

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.TextView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.newapp.home.OrderTypeBean
/*简单自定义的一个水平滚动选项，暂时没有做和viewpager联动以及文字逐渐放大和缩小
* 后面产品有这需求再加也很方便
* */
class BigTxtTabLayout : RecyclerView {
    private var layoutManager: LinearLayoutManager? = null
    var ckedPosition = 0
    var listener: TabSelectListener? = null
    var ckedColor: String? = null
    var unCkedColor: String? = null

    fun init(context: Context, titleList: List<OrderTypeBean.DataBean.WhereBean>, defaultCkIndex: Int, listener: TabSelectListener) {
        this.listener = listener
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        if (titleList.size > defaultCkIndex) {
            titleList[defaultCkIndex].isChecked = true
        }
        setLayoutManager(layoutManager)
        val adapter = Adapter(titleList)
        setAdapter(adapter)
    }

    fun setColor(ckedColor: String, unCkedColor: String) {
        this.ckedColor = ckedColor
        this.unCkedColor = unCkedColor
    }

    fun setCurrSelectItem(index:Int){
        ckedPosition = index
        if (adapter!=null){
            val data = (adapter as Adapter).data
            for (i in data.indices) {
                if (index == i) {
                    ckedPosition = index
                    data[i].isChecked = true
                } else {
                    data[i].isChecked = false
                }
            }
            (adapter as Adapter).setNewData(data)
        }
    }

    interface TabSelectListener {
        fun onSelect(position: Int)
    }

    internal inner class Adapter(data: List<OrderTypeBean.DataBean.WhereBean>) : BaseQuickAdapter<OrderTypeBean.DataBean.WhereBean, BaseViewHolder>(R.layout.text_item, data) {
        override fun convert(helper: BaseViewHolder, musicTypeBean: OrderTypeBean.DataBean.WhereBean) {
            val ckTitle = helper.getView<TextView>(R.id.title)
            ckTitle.text = musicTypeBean.title
            if (musicTypeBean.isChecked) {
                if (ckedColor != null) {
//                    ckTitle.setTextColor(Color.parseColor(ckedColor))
                    ckTitle.setTextColor(Color.parseColor("#2b2b2b"))
                } else {
                    ckTitle.setTextColor(Color.parseColor("#2b2b2b"))
                }
                ckTitle.textSize = 24f
            } else {
                ckTitle.textSize = 15f
                if (unCkedColor != null) {
//                    ckTitle.setTextColor(Color.parseColor(unCkedColor))
                    ckTitle.setTextColor(Color.parseColor("#616366"))
                } else {
                    ckTitle.setTextColor(Color.parseColor("#616366"))
                }
            }
            val position = helper.position
            ckTitle.setOnClickListener {
                val data = data
                for (i in data.indices) {
                    if (position == i) {
                        ckedPosition = position
                        data[i].isChecked = true
                    } else {
                        data[i].isChecked = false
                    }
                }
                notifyDataSetChanged()
                listener?.onSelect(ckedPosition)
            }
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}
