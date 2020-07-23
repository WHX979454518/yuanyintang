package com.mxkj.yuanyintang.mainui.newapp.pond

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.alibaba.fastjson.JSON
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.emotionkeyboard.utils.LogUtils
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.newapp.pond.PondInfo.*
import com.mxkj.yuanyintang.mainui.pond.activity.PublishPond
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.uiutils.Toast
import com.superrtc.call.DataChannel
import com.trello.rxlifecycle2.components.support.RxFragment
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.fragment_follow_pond.*
import java.util.ArrayList
import kotlinx.android.synthetic.main.fragment_hot_son_pond.view.*
import okhttp3.Headers

class HotPondSonFrg : RxFragment(), AppBarLayout.OnOffsetChangedListener {
    private var page: Int = 1
    private var pageCount=15;
    var tagClasId = "0"
    private lateinit var homeActivity: HomeActivity
    private var dataList = ArrayList<PondInfo.DataBean.DataListBean>()
    private var rootView: View? = null
    private var pondAdapter: PondAdapter? = null
    private var interRefresh: InterfaceRefreshLoadMore? = null
    internal var strings: MutableList<String> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeActivity = activity as HomeActivity
        if (rootView != null) {
            val parent = rootView!!.parent as ViewGroup
            parent?.removeView(rootView)
        } else {
            rootView = inflater.inflate(R.layout.fragment_hot_son_pond, container, false)
        }

        initView()
        initEvent()
        netData()
        return rootView
    }


    private fun initView() {
        rootView?.let {
            it.flab.setColorPressedResId(R.color.base_red)
            it.flab.setColorRippleResId(R.color.base_red)
            it.flab.setColorNormalResId(R.color.transparent)
            it.flab.setShadow(false)
            it.flab.attachToRecyclerView(it.recycler_hot_son)
            it.flab.setOnClickListener({

                MobclickAgent.onEvent(activity, "pond_publish");

                if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                    activity.startActivity(Intent(activity, PublishPond::class.java))
                } else {
                    activity.startActivity(Intent(activity, LoginRegMainPage::class.java))
                }
            })
            it.recycler_hot_son.layoutManager = LinearLayoutManager(activity)
            it.recycler_hot_son.addItemDecoration(MyRecyclerDetorration(activity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(activity, 5f), R.color.circleimageview_line_color, false))
            pondAdapter = PondAdapter(dataList)
            it.recycler_hot_son.adapter = pondAdapter

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
                    if(page>pageCount){
                        interRefresh!!.listNoData(it.swipe_refresh,activity)
                    }else{
                        netData(true)
                        if(page==pageCount){
                            interRefresh!!.listNoData(it.swipe_refresh,activity)
                        }
                    }
                }

            })
        }
    }

    private fun netData(isRefresh: Boolean = false) {
        tagClasId = arguments.getString("tag_clas")
        var cache_key = ""
        if (page == 1 && !isRefresh) {
            cache_key = "pondHot" + tagClasId;
        }
        val httpParams = HttpParams("p", page.toString())
        httpParams.put("tag_class", tagClasId)
        NetWork.getPond(cache_key, "", activity, httpParams, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                if (headers != null&&headers.get(" X-Pagination-Page-Count")!=null) {
                    pageCount= headers.get(" X-Pagination-Page-Count")!!.toInt()
                    Log.e("pondJson_pageCount","pageCount")
                }
                jsonPondData(resultData)
            }

            override fun doError(msg: String) {
                interRefresh?.setStopRefreshing()
                if (page > 1) {
                    page--
                }
            }

            override fun doResult() {

            }

        })
    }

    private fun jsonPondData(resultData: String) {
        interRefresh?.setStopRefreshing()
        val jsonObject = JSON.parseObject(resultData).getJSONObject("data")
        val jsonArray = jsonObject.getJSONArray("data_list")
        if (jsonArray.size > 0) {
            rootView?.view_nodata?.visibility = View.GONE
            val toJavaList = JSON.parseArray(jsonArray.toJSONString(), DataBean.DataListBean::class.java)
            if (page == 1) {
                dataList.clear()
            }
            dataList.addAll(toJavaList)
            pondAdapter?.setNewData(dataList)
        } else if (page == 1) {
            rootView?.view_nodata?.visibility = View.VISIBLE
        }
    }

    private fun initEvent() {

    }

    override fun onResume() {
        super.onResume()
        Log.e("tagClasId", "onResume" + tagClasId.toString())

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        swipe_refresh.isEnabled = verticalOffset == 0
    }
}

