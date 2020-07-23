package com.mxkj.yuanyintang.mainui.newapp.pond

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.RequiresApi
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.alibaba.fastjson.JSON
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.poisearch.PoiSearch
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.home.adapter.SlidingFragmentViewPager
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.myself.activity.EditPersonalProfileActivity
import com.mxkj.yuanyintang.mainui.newapp.pond.PondInfo.*
import com.mxkj.yuanyintang.mainui.pond.activity.PublishPond
import com.mxkj.yuanyintang.mainui.pond.bean.PondBean
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.uiutils.Toast
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.components.support.RxFragment
import com.umeng.analytics.MobclickAgent
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_follow_pond.*
import kotlinx.android.synthetic.main.fragment_follow_pond.view.*
import okhttp3.Headers
import java.util.*

class NearPondFrg : RxFragment(), AMapLocationListener, AppBarLayout.OnOffsetChangedListener {
    private var latitude = 0.0
    private var longitude = 0.0
    private var interRefresh: InterfaceRefreshLoadMore? = null
    private lateinit var mLocationClient: AMapLocationClient
    override fun onLocationChanged(amapLocation: AMapLocation?) {
        when {
            amapLocation != null -> when {
                amapLocation.errorCode == 0 -> {
                    amapLocation.locationType
                    latitude = amapLocation.latitude
                    longitude = amapLocation.longitude
                    amapLocation.accuracy
                    netData()
                }
                amapLocation.errorCode == 1->{

                }
            }
        }
    }

    private var page = 1
    private lateinit var homeActivity: HomeActivity
    private var dataList = ArrayList<PondInfo.DataBean.DataListBean>()
    private var rootView: View? = null
    internal var strings: MutableList<String> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_follow_pond, container, false)
        return rootView
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (null == rootView) {
            return
        }
        homeActivity = activity as HomeActivity
        getLocalPosition()
        initView()
        initEvent()
    }

    private var pondAdapter: PondAdapter? = null

    private fun initView() {
        rootView?.let {
            it.flab.setColorPressedResId(R.color.base_red)
            it.flab.setColorRippleResId(R.color.base_red)
            it.flab.setColorNormalResId(R.color.transparent)
            it.flab.setShadow(false)
            it.flab.attachToRecyclerView(it.recycler_pond)
            it.flab.setOnClickListener {
                MobclickAgent.onEvent(activity, "pond_publish");
                if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                    activity.startActivity(Intent(activity, PublishPond::class.java))
                } else {
                    activity.startActivity(Intent(activity, LoginRegMainPage::class.java))
                }
            }
            it.recycler_pond.layoutManager = LinearLayoutManager(activity)
            it.recycler_pond.addItemDecoration(MyRecyclerDetorration(activity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(activity, 5f), R.color.circleimageview_line_color, false))
            pondAdapter = PondAdapter(dataList)
            it.recycler_pond.adapter = pondAdapter
        }
    }

    private fun netData(isRefresh: Boolean = false) {
        when {
            latitude == 0.0 || longitude == 0.0 -> {
                Toast.create(activity).show("请开启定位权限！")
                interRefresh?.setStopRefreshing()
                return
            }
            else -> {
                val httpParams = HttpParams("p", page.toString())
                httpParams.put("x", longitude)
                httpParams.put("y", latitude)
                var cacheKey = ""
                if (page == 1 && !isRefresh) {
                    cacheKey = "pongNear"
                }
                NetWork.getPond(cacheKey, "near", activity, httpParams, object : NetWork.TokenCallBack {
                    override fun doNext(resultData: String, headers: Headers?) {

                        jsonPondData(resultData)
                    }

                    override fun doError(msg: String) {
                        if (page > 1) {
                            page--
                        }
                        interRefresh?.setStopRefreshing()
                    }

                    override fun doResult() {

                    }

                })
            }
        }
    }

    private fun jsonPondData(resultData: String) {
        interRefresh?.setStopRefreshing()
        val toJavaList = JSON.parseArray(JSON.parseObject(resultData).getJSONObject("data").getJSONArray("data_list").toJSONString(), DataBean.DataListBean::class.java)
        when {
            toJavaList != null && toJavaList.size > 0 -> rootView?.view_nodata?.visibility = GONE
            else -> return
        }
        when (page) {
            1 -> dataList.clear()
        }
        dataList.addAll(toJavaList)
        pondAdapter?.setNewData(dataList)
    }

    private fun initEvent() {
        rootView?.let {
            interRefresh = InterfaceRefreshLoadMore(it.swipe_refresh, activity, object : InterfaceRefreshLoadMore.RefreshLoadMoreCallback {
                override fun onPushDistance(distance: Int) {
                }

                override fun onPullDistance(distance: Int) {
                }

                override fun onRefresh() {
                    page = 1
                    netData(true)
                }

                override fun onLoadMore() {
                    page++
                    netData(true)
                }

            })
        }
    }

    private fun getLocalPosition() {
        RxPermissions(activity).requestEach(Manifest.permission.ACCESS_FINE_LOCATION).subscribeOn(Schedulers.newThread()).subscribe { permission ->
            when {
                permission.granted -> {
                    var mLocationOption: AMapLocationClientOption? = null
                    mLocationClient = AMapLocationClient(activity)
                    mLocationOption = AMapLocationClientOption()
                    mLocationClient.setLocationListener(this)
                    mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
                    mLocationOption.interval = 2000
                    mLocationOption.isOnceLocationLatest = true
                    mLocationClient.setLocationOption(mLocationOption)
                    mLocationClient.startLocation()
                }
                permission.shouldShowRequestPermissionRationale -> {
                    Toast.create(activity).show("请开启定位权限！")
                    interRefresh?.setStopRefreshing()
                }
                else -> {
                    Toast.create(activity).show("请开启定位权限！")
                    interRefresh?.setStopRefreshing()
                }
            }
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        swipe_refresh.isEnabled = verticalOffset == 0
    }
}

