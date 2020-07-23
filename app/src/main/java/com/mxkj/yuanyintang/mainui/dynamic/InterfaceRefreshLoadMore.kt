package com.mxkj.yuanyintang.mainui.dynamic

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.widget.ImageView

import com.linsh.lshutils.others.NetUtils
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout

import pl.droidsonroids.gif.GifImageView

class InterfaceRefreshLoadMore(s: SuperSwipeRefreshLayout, context: Context, callback: RefreshLoadMoreCallback) {
    private lateinit var loadMoreView: View
    private lateinit var refreshView: View
    private lateinit var swipeRefreshLayout: SuperSwipeRefreshLayout
    private lateinit var imageView: GifImageView
    private lateinit var animationIV: ImageView
    private lateinit var noDataView: View

    init {
        init(s, context)

        swipeRefreshLayout.setOnPullRefreshListener(object : SuperSwipeRefreshLayout.OnPullRefreshListener {
            override fun onRefresh() {
                init(s, context)
                if (NetUtils.isConnected(context)) {
                    setOnRefreshAni()
                    callback.onRefresh()
                } else {
                    setStopRefreshing()
                }

            }
            override fun onPullDistance(distance: Int) {
                callback.onPullDistance(distance)
                setOnpullAni(distance)
            }

            override fun onPullEnable(enable: Boolean) {

            }
        })
        swipeRefreshLayout.setOnPushLoadMoreListener(object : SuperSwipeRefreshLayout.OnPushLoadMoreListener {
            override fun onLoadMore() {
                if (NetUtils.isConnected(context)) {
                    callback.onLoadMore()
                } else {
                    setStopRefreshing()
                }
            }

            override fun onPushDistance(distance: Int) {
                callback.onPushDistance(distance)
            }

            override fun onPushEnable(enable: Boolean) {

            }
        })
    }

    private fun init(s: SuperSwipeRefreshLayout, context: Context) {
        loadMoreView = View.inflate(context, R.layout.view_load_more, null)
        refreshView = View.inflate(context, R.layout.view_refresh, null)
        imageView = loadMoreView.findViewById(R.id.img)
        animationIV = refreshView.findViewById(R.id.img_refresh)
        swipeRefreshLayout = s
        swipeRefreshLayout?.isTargetScrollWithLayout = true
        when {
            swipeRefreshLayout.viewType == SuperSwipeRefreshLayout.AddViewType.HEAD_AND_FOOTER -> {
                swipeRefreshLayout.setFooterView(loadMoreView)
                swipeRefreshLayout.setHeaderView(refreshView)
            }
            swipeRefreshLayout.viewType == SuperSwipeRefreshLayout.AddViewType.HEAD -> swipeRefreshLayout.setHeaderView(refreshView)
            swipeRefreshLayout.viewType == SuperSwipeRefreshLayout.AddViewType.FOOTER -> swipeRefreshLayout.setFooterView(loadMoreView)
        }
        imageView.setImageResource(R.drawable.loading)
    }

    fun listNoData(s: SuperSwipeRefreshLayout, context: Context) {
        noDataView = View.inflate(context, R.layout.include_list_nodata_view, null)
        swipeRefreshLayout = s
        swipeRefreshLayout?.isTargetScrollWithLayout = true
        when {
            swipeRefreshLayout.viewType == SuperSwipeRefreshLayout.AddViewType.HEAD_AND_FOOTER -> {
                swipeRefreshLayout.setFooterView(noDataView)
            }
            swipeRefreshLayout.viewType == SuperSwipeRefreshLayout.AddViewType.FOOTER -> swipeRefreshLayout.setFooterView(noDataView)
        }
        setStopRefreshing()
    }

    fun setOnpullAni(distance: Int) {
        if (animationIV != null) {
            when {
                distance < 5 -> animationIV.setImageResource(R.drawable.ani1)
                distance < 10 -> animationIV.setImageResource(R.drawable.ani2)
                distance < 15 -> animationIV.setImageResource(R.drawable.ani3)
                distance < 20 -> animationIV.setImageResource(R.drawable.ani4)
                distance < 25 -> animationIV.setImageResource(R.drawable.ani5)
                distance < 30 -> animationIV.setImageResource(R.drawable.ani6)
                distance < 35 -> animationIV.setImageResource(R.drawable.ani7)
                distance < 40 -> animationIV.setImageResource(R.drawable.ani8)
                distance < 45 -> animationIV.setImageResource(R.drawable.ani9)
                distance < 50 -> animationIV.setImageResource(R.drawable.ani10)
                distance < 55 -> animationIV.setImageResource(R.drawable.ani11)
                distance < 60 -> animationIV.setImageResource(R.drawable.ani12)
                distance < 65 -> animationIV.setImageResource(R.drawable.ani13)
                distance < 70 -> animationIV.setImageResource(R.drawable.ani14)
                distance < 75 -> animationIV.setImageResource(R.drawable.ani15)
                distance < 80 -> animationIV.setImageResource(R.drawable.ani16)
                distance < 85 -> animationIV.setImageResource(R.drawable.ani17)
                distance < 90 -> animationIV.setImageResource(R.drawable.ani18)
                distance < 95 -> animationIV.setImageResource(R.drawable.ani19)
                distance < 100 -> animationIV.setImageResource(R.drawable.ani20)
            }
        }
    }

    fun setOnRefreshAni() {
        val animationDrawable: AnimationDrawable
        if (animationIV != null) {
            animationIV.setImageResource(R.drawable.animation)
            animationDrawable = animationIV.drawable as AnimationDrawable
            animationDrawable.start()
        }
    }

    interface RefreshLoadMoreCallback {
        fun onRefresh()

        fun onLoadMore()

        fun onPushDistance(distance: Int)

        fun onPullDistance(distance: Int)

    }

    fun setStopRefreshing() {
        swipeRefreshLayout?.setLoadMore(false)
        swipeRefreshLayout?.isRefreshing = false
    }

    fun resetRefreshView() {
        System.gc()
    }

}
