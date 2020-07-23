package com.mxkj.yuanyintang.mainui.newapp.pond

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.alibaba.fastjson.JSON
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.dynamic.bean.RecommendUser
import com.mxkj.yuanyintang.mainui.home.adapter.SlidingFragmentViewPager
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.newapp.pond.PondInfo.*
import com.mxkj.yuanyintang.mainui.pond.activity.PublishPond
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.layoutmanager.decoration.SpacesItemDecoration
import com.trello.rxlifecycle2.components.support.RxFragment
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.fragment_follow_pond.*
import java.util.ArrayList
import kotlinx.android.synthetic.main.fragment_follow_pond.view.*
import okhttp3.Headers

/**
 * 关注池塘
 */
class FollowPondFrg : RxFragment(), AppBarLayout.OnOffsetChangedListener {
    private var page: Int = 1
    private lateinit var homeActivity: HomeActivity
    private var dataList = ArrayList<PondInfo.DataBean.DataListBean>()
    private var rootView: View? = null
    internal var strings: MutableList<String> = ArrayList()
    private var fragments: MutableList<Fragment> = ArrayList()
    private var interRefresh: InterfaceRefreshLoadMore? = null

    private lateinit var slidingFragmentViewPager: SlidingFragmentViewPager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_follow_pond, container, false)
        homeActivity = activity as HomeActivity
        initView()
        initEvent()
        return rootView
    }

    private var pondAdapter: PondAdapter? = null

    private fun initView() {
//        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
//
//        }
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
            pondAdapter?.isFollowPage = true
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

    /**
     *
     */
    private fun netData(isRefresh: Boolean = false) {
//        if (!CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
//            interRefresh?.setStopRefreshing()
//            dataList.clear()
//            pondAdapter?.setNewData(dataList)
//            return
//        }
        var cacheKey = "";
        if (page == 1 && !isRefresh) {
            cacheKey = "pondFollow"
        }
        NetWork.getPond(cacheKey, "follow", activity, HttpParams("p", page.toString()), object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                CacheUtils.setString(activity, "pondFollow", resultData);
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
        val toJavaList = JSON.parseArray(JSON.parseObject(resultData).getJSONObject("data").getJSONArray("data_list").toJSONString(), DataBean.DataListBean::class.java)
        if (page == 1) {
            dataList.clear()
        }
        if (toJavaList == null || toJavaList.size == 0) {
            if (page == 1) {
                view_nodata.visibility = View.VISIBLE
            }
//            显示推荐关注
            if (page == 1) {
                ll_recommend.visibility = VISIBLE
                initRecommend()
            } else {
                ll_recommend.visibility = GONE
            }
        } else {
            view_nodata.visibility = View.GONE
        }
        dataList.addAll(toJavaList)
        pondAdapter?.setNewData(dataList)
    }

    private fun initEvent() {

    }

    override fun onResume() {
        super.onResume()
        netData()
    }

    private fun initRecommend() {
        NetWork.getRecommendUser(activity, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val recommendUser = JSON.parseObject(resultData, RecommendUser::class.java)
                val data = recommendUser.data
                if (data != null) {
                    var recAdapter = RecUserAdapter(data)
                    recycler_recommend.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    recycler_recommend.addItemDecoration(SpacesItemDecoration(CommonUtils.dip2px(activity, 10f), CommonUtils.dip2px(activity, 10f)))
                    recycler_recommend.adapter = recAdapter
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        swipe_refresh.isEnabled = verticalOffset == 0
    }
}

