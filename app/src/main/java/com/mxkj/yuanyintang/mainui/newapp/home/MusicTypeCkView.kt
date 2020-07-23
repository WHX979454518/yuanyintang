package com.mxkj.yuanyintang.mainui.newapp.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.utils.string.StringUtils

import java.util.ArrayList

class MusicTypeCkView : LinearLayout {
    internal var context: Context
    private var recycler: RecyclerView? = null
    internal var handler: Handler
    var adapter: MtypeAdapter? = null
    val datas: List<OrderTypeBean.DataBean.WhereBean.ListBean>?
        get() = adapter?.data


    constructor(context: Context) : super(context) {
        this.context = context
        handler = Handler()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.context = context
        handler = Handler()

    }

    //传入layoutId，布局应该是这样
    /*
    *
    * <CheckBox xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/tv_typeName"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@drawable/bck_music_type"
        android:button="@null"
        android:gravity="center"
        android:layout_marginLeft="@dimen/dimen_10"
        android:text=""
        android:paddingBottom="@dimen/dimen_5"
        android:paddingLeft="@dimen/dimen_10"
        android:paddingRight="@dimen/dimen_10"
        android:paddingTop="@dimen/dimen_5"
        android:textColor="@drawable/music_type_text_color"
        android:textSize="@dimen/size_14">

    </CheckBox>
    *
    * */
    fun setDatas(isShowTitle: Boolean, whereBean: OrderTypeBean.DataBean.WhereBean, resId: Int, listener: OnCheckedListener) {
        removeAllViews()
        val itemLayout = LayoutInflater.from(getContext()).inflate(R.layout.music_type_ck_item, null)
        val tv_typeName = itemLayout.findViewById<TextView>(R.id.tv_typeName)
        recycler = itemLayout.findViewById(R.id.recycler)
        if (isShowTitle) {
            tv_typeName.text = whereBean.title
        } else {
            tv_typeName.visibility = View.GONE
        }
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recycler?.layoutManager = layoutManager
        if (recycler?.adapter == null) {
            adapter = MtypeAdapter(whereBean.list, resId, listener)
            recycler?.adapter = adapter
        } else {
            adapter?.setNewData(whereBean.list)
        }
        addView(itemLayout)
    }

    inner class MtypeAdapter(data: List<OrderTypeBean.DataBean.WhereBean.ListBean>?, resId: Int, private val listener: OnCheckedListener) : BaseQuickAdapter<OrderTypeBean.DataBean.WhereBean.ListBean, BaseViewHolder>(resId, data) {
        @SuppressLint("ClickableViewAccessibility")
        override fun convert(helper: BaseViewHolder, musicTypeBean: OrderTypeBean.DataBean.WhereBean.ListBean) {
            helper.setText(R.id.tv_typeName, StringUtils.isEmpty(musicTypeBean.title))
            val checkBox = helper.getView<CheckBox>(R.id.tv_typeName)
            checkBox.isChecked = musicTypeBean.isChecked
            checkBox.isClickable = false
            checkBox.setOnTouchListener { _, motionEvent ->
                if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
                    for (i in 0 until itemCount) {
                        if (helper.position == i) {
                            data[i]?.isChecked = !checkBox.isChecked
                        } else {
                            data[i]?.isChecked = false
                        }
                    }
                    listener.onChecked(getItem(helper.position), helper.position)
                    handler.post { setNewData(data) }

                }
                true
            }
        }
    }

    interface OnCheckedListener {
        fun onChecked(id: OrderTypeBean.DataBean.WhereBean.ListBean?, position: Int)
    }
}
