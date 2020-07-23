package com.mxkj.yuanyintang.mainui.dynamic.fragment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.database.cachemusiciansql.CacheMusicIanDataBean
import com.mxkj.yuanyintang.database.cachemusiciansql.CacheMusicIanDataDao
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.dynamic.activity.PublishDynamic
import com.mxkj.yuanyintang.mainui.dynamic.adapter.DynamicMultipleRecycleAdapter
import com.mxkj.yuanyintang.mainui.dynamic.bean.DynamicBean
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import java.util.ArrayList
import butterknife.ButterKnife
import butterknife.Unbinder
import okhttp3.Headers
import kotlinx.android.synthetic.main.world_fragment_layout.*
import kotlinx.android.synthetic.main.world_fragment_layout.view.*

@SuppressLint("ValidFragment")
class WorldFragment : Fragment() {
    private var msgNum: Int = 0//顶部显示的消息条数
    internal lateinit var rootView: View
    internal var adapter: DynamicMultipleRecycleAdapter? = null
    private val dataBeans = ArrayList<DynamicBean.DataBean>()
    internal var page = 1
    private var unbinder: Unbinder? = null
    private var dataUpdateReceiver: DataUpdateReceiver? = null
    private var filterBroadcast: IntentFilter? = null
    internal lateinit var interfaceRefreshLoadMore: InterfaceRefreshLoadMore
    private var jsonDynamicData: CacheMusicIanDataBean? = null
    private var isRefresh = false
    private var dataBeanNewMsg: DynamicBean.DataBean? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.world_fragment_layout, null)
        unbinder = ButterKnife.bind(this, rootView!!)
        initEvent()
        return rootView
    }

    private fun initData() {
        NetWork.getDynamicWorld(page, isRefresh, activity, if (null == jsonDynamicData) 0 else if (null == jsonDynamicData!!.endtime) 0 else jsonDynamicData!!.endtime, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val dynamicBean = JSON.parseObject(resultData, DynamicBean::class.java)
                if (page == 1) {
                    if (headers != null && null != dynamicBean && null != dynamicBean.data && dynamicBean.data!!.size > 0) {
                        CacheMusicIanDataDao(activity).setCacheData(resultData, Constants.DataManager.DYNAMIC_DATA_JSON, if (TextUtils.isEmpty(headers.get("X_End_Time")))
                            0
                        else
                            java.lang.Long.valueOf(headers.get("X_End_Time")))
                    }
                }
                jsonData(dynamicBean)
                interfaceRefreshLoadMore.setStopRefreshing()
            }

            override fun doError(msg: String) {
                interfaceRefreshLoadMore.setStopRefreshing()
            }

            override fun doResult() {

            }
        })
    }

    private fun jsonData(dynamicBean: DynamicBean?) {
        val list = dynamicBean!!.data
        if (list != null && list.isNotEmpty()) {
            if (page == 1) {
                dataBeans.clear()
                if (msgNum > 0) {
                    dataBeans.add(dataBeanNewMsg!!)
                }
            }
            dataBeans.addAll(list)
            if (world_recycler.scrollState === RecyclerView.SCROLL_STATE_IDLE && world_recycler.isComputingLayout === false) {
                adapter!!.setNewData(dataBeans)
            }
        }
    }

    private fun initEvent() {
        dataUpdateReceiver = DataUpdateReceiver()
        filterBroadcast = IntentFilter()
        filterBroadcast!!.addAction("publishDynamic")
        filterBroadcast!!.addAction("receive_new_msg")
        filterBroadcast!!.addAction("clearMsgNum")
        activity.registerReceiver(dataUpdateReceiver, filterBroadcast)
        initAdapter()
        interfaceRefreshLoadMore = InterfaceRefreshLoadMore(rootView.swipe_refresh, activity, object : InterfaceRefreshLoadMore.RefreshLoadMoreCallback {
            override fun onRefresh() {
                page = 1
                isRefresh = true
                initData()
                isRefresh = false
            }

            override fun onLoadMore() {
                page++
                initData()
            }

            override fun onPushDistance(distance: Int) {

            }

            override fun onPullDistance(distance: Int) {

            }
        })
        rootView.flab.attachToRecyclerView(rootView.world_recycler)
        rootView.flab.setOnClickListener({
            if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN, false)) {
                activity.startActivity(Intent(activity, PublishDynamic::class.java))
            } else {
                activity.startActivity(Intent(activity, LoginRegMainPage::class.java))
            }
        })
        rootView.flab.setColorPressedResId(R.color.base_red)
        rootView.flab.setColorRippleResId(R.color.base_red)
        rootView.flab.setColorNormalResId(R.color.transparent)
        rootView.flab.setShadow(false)
        initData()
    }

    private fun initAdapter() {
        if (adapter == null) {
            adapter = DynamicMultipleRecycleAdapter(dataBeans, false)
            rootView.world_recycler.layoutManager = LinearLayoutManager(activity)
            rootView.world_recycler.addItemDecoration(MyRecyclerDetorration(activity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(activity, 5f), R.color.bg_color, true))
            rootView.world_recycler.setHasFixedSize(true)
            rootView.world_recycler.adapter = adapter
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        interfaceRefreshLoadMore.resetRefreshView()
        if (null != unbinder) {
            unbinder!!.unbind()
        }
        activity.unregisterReceiver(dataUpdateReceiver)
    }

    private inner class DataUpdateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when (action) {
                "publishDynamic" -> {
                    page = 1
                    initData()
                }
                "clearMsgNum" -> msgNum = 0
                "receive_new_msg" -> {
                    msgNum++
                    val userIcon = intent.getStringExtra("userIcon")
                    dataBeanNewMsg = DynamicBean.DataBean()
                    val newMsgBean = DynamicBean.DataBean.NewMsgBean()
                    newMsgBean.msgNum = msgNum
                    newMsgBean.userIcon = userIcon
                    dataBeanNewMsg!!.newMsgBean = newMsgBean
                    if (dataBeans[0].newMsgBean != null) {
                        dataBeans.removeAt(0)
                    }
                    dataBeans.add(0, dataBeanNewMsg!!)

                    adapter!!.setNewData(dataBeans)
                }
            }
        }
    }
}
