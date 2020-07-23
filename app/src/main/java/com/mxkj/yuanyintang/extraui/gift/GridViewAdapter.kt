package com.mxkj.yuanyintang.extraui.gift

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.bumptech.glide.Glide
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.utils.string.StringUtils

class GridViewAdapter(private val mContext: Context, private val mDatas: List<GiftListsBean.DataBean.GiftListBean>,
                      /**
                       * 页数下标,从0开始(当前是第几页)
                       */
                      private val curIndex: Int) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(mContext)
    /**
     * 每一页显示的个数
     */
    private val pageSize = 8

    /**
     * 先判断数据集的大小是否足够显示满本页？mDatas.size() > (curIndex+1)*pageSize,
     * 如果够，则直接返回每一页显示的最大条目个数pageSize,
     * 如果不够，则有几项返回几,(mDatas.size() - curIndex * pageSize);(也就是最后一页的时候就显示剩余item)
     */
    override fun getCount(): Int {
        return if (mDatas.size > (curIndex + 1) * pageSize) pageSize else mDatas.size - curIndex * pageSize
    }

    override fun getItem(position: Int): GiftListsBean.DataBean.GiftListBean? {
        return mDatas[position + curIndex * pageSize]
    }

    override fun getItemId(position: Int): Long {
        return (position + curIndex * pageSize).toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var viewHolder: ViewHolder? = null
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gift_gridview, parent, false)
            viewHolder = ViewHolder()
            viewHolder.tv = convertView!!.findViewById<View>(R.id.textView) as TextView
            viewHolder.price = convertView.findViewById<View>(R.id.price) as TextView
            viewHolder.iv = convertView.findViewById<View>(R.id.imageView) as ImageView
            viewHolder.item_layout = convertView.findViewById<View>(R.id.item_layout) as RelativeLayout
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        /**
         * 在给View绑定显示的数据时，计算点击的position = position + curIndex * pageSize，
         */
        val giftListsBean = getItem(position)
        viewHolder.tv!!.text = giftListsBean!!.name
        viewHolder.price!!.text = giftListsBean.coin_num.toString() + "甜甜圈"
        if (giftListsBean?.icon_info != null)
            Glide.with(mContext).load(StringUtils.isEmpty(giftListsBean.icon_info!!.link)).into(viewHolder.iv!!)
        if (giftListsBean.isSelected) {//被选中
            viewHolder.item_layout!!.setBackgroundColor(Color.parseColor("#ebebeb"))
        } else {
            viewHolder.item_layout!!.setBackgroundColor(Color.parseColor("#ffffff"))
        }
        return convertView
    }


    internal inner class ViewHolder {
        var item_layout: RelativeLayout? = null
        var tv: TextView? = null
        var price: TextView? = null
        var iv: ImageView? = null
    }

}