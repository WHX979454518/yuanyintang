package com.mxkj.yuanyintang.mainui.newapp.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import kotlinx.android.synthetic.main.activity_all_song_sheet.*
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.alibaba.fastjson.JSON
import com.flyco.tablayout.SlidingTabLayout
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.home.adapter.SlidingFragmentViewPager
import com.mxkj.yuanyintang.mainui.home.adapter.SongSheetRecommendListAdapter
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.home.bean.MusicListSongBean
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.CHARTS_TYPE
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.EXPEN_CLASS_ID
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsAllFragment
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsFragment
import com.mxkj.yuanyintang.mainui.home.utils.GridSpacingItemDecoration
import com.mxkj.yuanyintang.mainui.newapp.home.adapter.AllSongSheetFragment
import com.mxkj.yuanyintang.mainui.newapp.pond.PondFrgNew
import com.mxkj.yuanyintang.mainui.newapp.weidget.BigTxtTabLayout
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.app.CommonUtils
import okhttp3.Headers

class AllSongSheetActivity : StandardUiActivity() {
    var interfaceRefreshLoadMore: InterfaceRefreshLoadMore? = null
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    override fun setLayoutId(): Int {
        return R.layout.activity_all_song_sheet
    }

    private lateinit var vp_charts: ViewPager
    private lateinit var tab_charts: SlidingTabLayout
    private var slidingFragmentViewPager: SlidingFragmentViewPager? = null
    internal var fragmentList = java.util.ArrayList<Fragment>()
    private val strings = ArrayList<String>()
    override fun initView() {
        setTitleText("全部歌单")
        vp_charts = findViewById(R.id.vp_charts)
        tab_charts = findViewById(R.id.tab_charts)
        strings.add("")
        val weekFrag = AllSongSheetFragment()
        fragmentList.add(weekFrag)
        slidingFragmentViewPager = SlidingFragmentViewPager(supportFragmentManager, strings, fragmentList, this@AllSongSheetActivity)
        vp_charts.setAdapter(slidingFragmentViewPager)
        tab_charts.setViewPager(vp_charts)
        vp_charts.setOffscreenPageLimit(1)
    }

    override fun initData() {

    }

    override fun initEvent() {

    }
}
