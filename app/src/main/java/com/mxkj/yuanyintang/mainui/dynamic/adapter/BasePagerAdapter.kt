package com.mxkj.yuanyintang.mainui.dynamic.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

class BasePagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>?, private val list_Title: List<String>) : FragmentPagerAdapter(fm) {
    lateinit var currentFragment: Fragment
    override fun getItem(position: Int): Fragment? {
        return if (fragments != null && fragments.isNotEmpty()) {
            fragments[position]
        } else null
    }


    override fun getCount(): Int {
        return fragments!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (list_Title != null && list_Title!!.size > position) {
            list_Title!!.get(position)
        } else null
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        this.currentFragment = `object` as Fragment
        super.setPrimaryItem(container, position, `object`)
    }
}
