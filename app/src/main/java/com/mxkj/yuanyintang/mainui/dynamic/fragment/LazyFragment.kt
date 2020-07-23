package com.mxkj.yuanyintang.mainui.dynamic.fragment

import android.support.v4.app.Fragment

abstract class LazyFragment : Fragment() {
    private var isVisible: Boolean?=false
    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isVisible = true
            onVisible()
        } else {
            isVisible = false
            onInvisible()
        }
    }

    fun onVisible() {
        lazyLoad()
    }

    protected abstract fun lazyLoad()

    fun onInvisible() {}
}
