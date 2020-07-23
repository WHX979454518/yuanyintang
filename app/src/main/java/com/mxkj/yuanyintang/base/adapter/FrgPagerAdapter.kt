package com.mxkj.yuanyintang.base.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.PagerAdapter

import java.util.ArrayList

class FrgPagerAdapter(private val mFragmentManager: FragmentManager, mFragmentList: ArrayList<Fragment>) : FragmentStatePagerAdapter(mFragmentManager) {

    private var mFragmentList: ArrayList<Fragment>? = null

    init {
        setFragments(mFragmentList)
    }

    fun updateData(mFragmentList: ArrayList<Fragment>) {
        setFragments(mFragmentList)
    }

    private fun setFragments(mFragmentList: ArrayList<Fragment>) {
        if (this.mFragmentList != null) {
            val fragmentTransaction = mFragmentManager.beginTransaction()
            for (f in this.mFragmentList!!) {
                fragmentTransaction.remove(f)
            }
            fragmentTransaction.commit()
            mFragmentManager.executePendingTransactions()
        }
        this.mFragmentList = mFragmentList
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return this.mFragmentList!!.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList!![position]
    }
}
