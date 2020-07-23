package com.mxkj.yuanyintang.extraui.gift

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

class ViewPagerAdapter(private val mViewList: List<View>?, private val mContext: Context) : PagerAdapter() {

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(mViewList!![position])
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = mViewList!![position]
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return mViewList?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}