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
import com.flyco.tablayout.SlidingTabLayout
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
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlinx.android.synthetic.main.activity_all_song_sheet_fragment.*
import kotlinx.android.synthetic.main.activity_all_song_sheet_fragment.view.*
import kotlinx.android.synthetic.main.fragment_hot_pond.view.*
import kotlinx.android.synthetic.main.fragment_pond_new.view.*
import okhttp3.Headers
//StandardUiActivity()
class ThreeSongSheetFragment : RxFragment() {
    private var rootView: View? = null
    private var class_id: Int = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.one_song_sheet_fragment, container, false)
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
        class_id = arguments.getInt("three", 0)
        Log.e("oooooooo",""+class_id)
        one_viewpager = rootView!!.findViewById(R.id.one_viewpager)
        tabs = rootView!!.findViewById(R.id.tabs)
    }
    private var whereBeanList = ArrayList<OrderTypeBean.DataBean.WhereBean>()
    internal var strings: MutableList<String> = ArrayList()
    private var fragments: MutableList<Fragment> = ArrayList()
    private lateinit var slidingFragmentViewPager: SlidingFragmentViewPager
    lateinit var one_viewpager: ViewPager
    lateinit var tabs: SlidingTabLayout
    lateinit var list:ArrayList<OrderTypeBean.DataBean.WhereBean.ListBean>
    private fun initData() {
        NetWork.getOrderType(activity, "song", object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val parseObject = JSON.parseObject(resultData, OrderTypeBean::class.java)
                whereBeanList = parseObject.data.where as ArrayList<OrderTypeBean.DataBean.WhereBean>
                whereBeanList.forEach {
                    if(class_id == it.id){
                        list = it.list as ArrayList<OrderTypeBean.DataBean.WhereBean.ListBean>
                        list.forEach { listBean ->
                            var fourSongSheetFragment = FourSongSheetFragment()
                            var bundle = Bundle()
                            bundle.putInt("list_id", listBean.id)
                            fourSongSheetFragment.arguments = bundle
                            fragments.add(fourSongSheetFragment)
                            strings.add(listBean.title)
                        }
                    }
                }
                slidingFragmentViewPager = SlidingFragmentViewPager(childFragmentManager, strings, fragments, activity)
                one_viewpager.adapter = slidingFragmentViewPager
                tabs.setViewPager(one_viewpager)
                one_viewpager.offscreenPageLimit = strings.size
                tabs.updateTabSelection(0)
            }

            override fun doError(msg: String) {
            }

            override fun doResult() {
            }

        })
    }

    private fun initEvent() {

    }

}
