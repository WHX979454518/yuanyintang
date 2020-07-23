package com.mxkj.yuanyintang.base.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.lang.reflect.Field

abstract class BaseLazyFragment : BaseFragment() {
    protected abstract val contentViewLayoutID: Int
    override val layoutId: Int
        get() = contentViewLayoutID
    private var isFirstResume = true
    private var isFirstVisible = true
    private var isFirstInvisible = true
    private var isPrepared: Boolean = false

    protected abstract fun onFirstVisibleToUser() //第一次进入

    protected abstract fun onVisibleToUser() //第二次后进入

    protected abstract fun onInvisibleToUser() //页面休眠

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPrepare()
    }

    override fun onResume() {
        super.onResume()
        if (isFirstResume) {
            isFirstResume = false
            return
        }
        if (userVisibleHint) {
            onVisibleToUser()
        }
    }

    override fun onPause() {
        super.onPause()
        if (userVisibleHint) {
            onInvisibleToUser()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false
                initPrepare()
            } else {
                onVisibleToUser()
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false
            } else {
                onInvisibleToUser()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        //  bug ---> java.lang.IllegalStateException: Activity has been destroyed
        try {
            val childFragmentManager = Fragment::class.java.getDeclaredField("mChildFragmentManager")
            childFragmentManager.isAccessible = true
            childFragmentManager.set(this, null)

        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }

    }

    @Synchronized
    private fun initPrepare() {
        if (isPrepared) {
            onFirstVisibleToUser()
        } else {
            isPrepared = true
        }
    }

}
