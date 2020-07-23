package com.mxkj.yuanyintang.base.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter


/**
 * Created by zwj on 2017/10/21.
 * 基于BaseAdapter封装的一个适配器
 * 对于一些很简单的列表页面（比如音乐列表、搜索列表、帮助中心这些地方用到）
 * 需要使用dataBinding
 *
 */

abstract class BaseBindingAdapter<T, W : ViewDataBinding> : BaseAdapter {
    private lateinit var list: List<T>
    protected var context: Context
    private var itemId: Int = 0

    constructor(context: Context, list: List<T>, itemId: Int) {
        this.list = list
        this.context = context
        this.itemId = itemId
    }

    constructor(context: Context) {
        this.context = context
    }

    fun getList(): List<T>? {
        return this.list
    }

    fun setList(list: List<T>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return if (list == null) 0 else list!!.size
    }

    override fun getItem(position: Int): T {
        return list!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var w: W
        if (convertView == null) {
            convertView = View.inflate(context, itemId, null)
            w = DataBindingUtil.bind(convertView)!!
            convertView!!.tag = w
        }
        if (list != null && list[position] != null) {
            w = convertView.tag as W
            val t = list[position]
            bindObj(w, t)
            operateView(convertView, t)
        }

        return convertView
    }

    abstract fun bindObj(w: W, t: T)
    open fun operateView(view: View, t: T) {}

}
