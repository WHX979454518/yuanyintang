package com.mxkj.yuanyintang.mainui.newapp.home.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import kotlinx.android.synthetic.main.activity_all_song_sheet.*
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.R.id.rootView
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.home.adapter.SlidingFragmentViewPager
import com.mxkj.yuanyintang.mainui.home.adapter.SongSheetRecommendListAdapter
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.home.bean.MusicListSongBean
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.CHARTS_TYPE
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.EXPEN_CLASS_ID
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsAllFragment
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsFragment
import com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity
import com.mxkj.yuanyintang.mainui.home.utils.GridSpacingItemDecoration
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.newapp.home.AllMusicanFragment
import com.mxkj.yuanyintang.mainui.newapp.home.MusicTypeCkView
import com.mxkj.yuanyintang.mainui.newapp.home.OrderTypeBean
import com.mxkj.yuanyintang.mainui.newapp.pond.FollowPondFrg
import com.mxkj.yuanyintang.mainui.newapp.pond.HotPondMainFrg
import com.mxkj.yuanyintang.mainui.newapp.pond.NearPondFrg
import com.mxkj.yuanyintang.mainui.newapp.pond.PondFrgNew
import com.mxkj.yuanyintang.mainui.newapp.weidget.BigTxtTabLayout
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlinx.android.synthetic.main.activity_all_song_sheet_fragment.*
import kotlinx.android.synthetic.main.activity_all_song_sheet_fragment.view.*
import kotlinx.android.synthetic.main.fragment_hot_pond.view.*
import kotlinx.android.synthetic.main.fragment_pond_new.view.*
import okhttp3.Headers
//StandardUiActivity()
class FourSongSheetFragment : RxFragment() {
    private var adapter: SongSheetRecommendListAdapter? = null
    val dataList = ArrayList<MusicIndex.ItemInfoListBean.RecommendBean>()
    private var list_id: Int = 0
    private var page: Int = 1
    var interfaceRefreshLoadMore: InterfaceRefreshLoadMore? = null

    lateinit var recycler:RecyclerView
    lateinit var swipe_refresh: SuperSwipeRefreshLayout

    private var rootView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.four_song_sheet_fragment, container, false)
        return rootView
    }
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (null == rootView) {
            return
        }
        initView()
        initData()
        initEvent()
    }

    private fun initView() {
        list_id = arguments.getInt("list_id", 0)
        recycler = rootView!!.findViewById(R.id.recycler)
        swipe_refresh = rootView!!.findViewById(R.id.swipe_refresh)
    }

    private fun initData() {
        netPublicMusicSong(list_id)
    }

    private fun initEvent() {
        recycler.layoutManager = GridLayoutManager(activity, 3)
        if (recycler.adapter == null) {
            recycler.addItemDecoration(GridSpacingItemDecoration(3, CommonUtils.dip2px(activity, 10f), CommonUtils.dip2px(activity, 20f), false))
            adapter = SongSheetRecommendListAdapter(dataList)
            recycler.adapter = adapter
        } else {
            adapter?.setNewData(dataList)
        }
        interfaceRefreshLoadMore = InterfaceRefreshLoadMore(swipe_refresh, activity, object : InterfaceRefreshLoadMore.RefreshLoadMoreCallback {
            override fun onRefresh() {
                page = 1
                netPublicMusicSong(list_id)
            }

            override fun onLoadMore() {
                page++
                netPublicMusicSong(list_id)
            }

            override fun onPushDistance(distance: Int) {
            }

            override fun onPullDistance(distance: Int) {
            }

        })
    }

    private fun netPublicMusicSong(tag: Int) {
        NetWork.getPublicMusicSong(tag, page, activity, "", object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
//                hideLoadingView()
                interfaceRefreshLoadMore?.setStopRefreshing()
                val obj = JSON.parseObject(resultData)
                val jObj = obj.getJSONObject("data")
                val array = jObj.getJSONArray("data_list")
                val musicListSongBeen = JSON.parseArray(array.toString(), MusicIndex.ItemInfoListBean.RecommendBean::class.java)
                if (musicListSongBeen.size > 0) {
                    nothing.visibility = GONE
                } else {
                    if (page == 1) {
                        nothing.visibility = VISIBLE
                    }
                }
                if (page == 1) {
                    dataList.clear()
                }
                dataList.addAll(musicListSongBeen)
                adapter?.setNewData(dataList)
            }

            override fun doError(msg: String) {
//                hideLoadingView()
                if (page > 1) {
                    page--
                }
                interfaceRefreshLoadMore?.setStopRefreshing()
            }

            override fun doResult() {

            }
        })
    }


}
