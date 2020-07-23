package com.mxkj.yuanyintang.mainui.pond.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.mainui.pond.adapter.LocateRecyclerAdapter
import com.mxkj.yuanyintang.mainui.pond.bean.LocationInfo
import com.mxkj.yuanyintang.mainui.search.SearchResultActivity
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils
import com.mxkj.yuanyintang.utils.uiutils.Toast
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.activity_location_list.*

class LocationListActivity : StandardUiActivity(), AMapLocationListener, LocateRecyclerAdapter.OnLocationItemClick, PoiSearch.OnPoiSearchListener {

    private lateinit var mList: MutableList<LocationInfo>
    private var mAdapter: LocateRecyclerAdapter? = null
    private var searchKey = ""


    override fun OnLocationClick(parent: RecyclerView?, view: View?, position: Int, locationInfo: LocationInfo?) {
        if (position != 0) {
            val intent = Intent()
            intent.putExtra("LOCATION_DATA", locationInfo)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }

    override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
    }

    override fun onPoiSearched(result: PoiResult?, p1: Int) {
        hideLoadingView()
        if (result != null) {
            mList.clear()
            val noneAd = LocationInfo()
            noneAd.address = "不显示地理位置"
            mList.add(noneAd)
            val pois = result.pois
            for (poi in pois) {
                val info = LocationInfo()
                info.address = poi.snippet
                info.addressDesc = poi.title
                val point = poi.latLonPoint
                info.latitude = point.latitude
                info.lonTitude = point.longitude
                mList.add(info)
            }
            mAdapter?.notifyDataSetChanged()
        }

    }


    override fun onLocationChanged(amapLocation: AMapLocation?) {
        hideLoadingView()
        if (amapLocation != null) {
            if (amapLocation.errorCode == 0) {
                amapLocation.locationType
                val latitude = amapLocation.latitude
                val longitude = amapLocation.longitude
                amapLocation.accuracy
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = Date(amapLocation.time)
                df.format(date)//定位时间
                val query = PoiSearch.Query("", searchKey, "")
                query.pageSize = 200
                val search = PoiSearch(this, query)
                search.bound = PoiSearch.SearchBound(LatLonPoint(latitude, longitude), 10000)
                search.setOnPoiSearchListener(this)
                search.searchPOIAsyn()

            }
        }
    }
    private lateinit var mLocationClient: AMapLocationClient
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    override fun setLayoutId(): Int {
        return R.layout.activity_location_list
    }

    override fun initView() {
        hideTitle(true)
        back.setOnClickListener { finish() }
        mList = ArrayList()
        mAdapter = LocateRecyclerAdapter(this, mList)
        val layoutManager = LinearLayoutManager(this)
        locate_recycler.layoutManager = layoutManager
        locate_recycler.adapter = mAdapter
        mAdapter?.setLocationItemClick(this)
        getLocalPosition()

    }

    override fun initData() {
    }

    override fun initEvent() {
        et_search_info.setOnKeyListener({ _, keyCode, event ->
            if (NetConnectedUtils.isNetConnected(applicationContext)) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                            .hideSoftInputFromWindow(this@LocationListActivity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    if (!TextUtils.isEmpty(et_search_info.text.toString().trim({ it <= ' ' }))) {
                        searchKey = et_search_info.text.toString()
                        getLocalPosition()
                    } else {
                        //ToastUtil.showToast(getApplicationContext(), "你还没有输入搜索内容哦~~");
                    }
                }
            }
            false
        })

    }

    private fun getLocalPosition() {
        showLoadingView()
        RxPermissions(this@LocationListActivity).requestEach(Manifest.permission.ACCESS_FINE_LOCATION).subscribe { permission ->
            when {
                permission.granted -> {
                    var mLocationOption: AMapLocationClientOption? = null
                    mLocationClient = AMapLocationClient(this)
                    mLocationOption = AMapLocationClientOption()
                    mLocationClient.setLocationListener(this)
                    mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
                    mLocationOption.interval = 2000
                    mLocationOption.isOnceLocationLatest = true
                    mLocationClient.setLocationOption(mLocationOption)
                    mLocationClient.startLocation()
                }
                permission.shouldShowRequestPermissionRationale -> {
                    hideLoadingView()
                }
                else -> {
                    Toast.create(this).show("请开启定位权限！")
                    hideLoadingView()
                }
            }
        }
    }
}
