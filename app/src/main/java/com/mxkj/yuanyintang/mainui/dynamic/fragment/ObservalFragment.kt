package com.mxkj.yuanyintang.mainui.dynamic.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.login_regist.HotUserRecommend
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil
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
class ObservalFragment : LazyFragment() {
    internal lateinit var rootView: View
    internal lateinit var adapter: DynamicMultipleRecycleAdapter
    private val dataBeans = ArrayList<DynamicBean.DataBean>()
    internal var page = 1
    private var unbinder: Unbinder? = null
    private var isFirst = true
    private var isLoad = false
    internal lateinit var interfaceRefreshLoadMore: InterfaceRefreshLoadMore
    private var isRefresh = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.world_fragment_layout, null)
        unbinder = ButterKnife.bind(this, rootView)
        initEvent()
        return rootView
    }

    private fun initData() {
        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
            NetWork.getDynamicObserval(page, isRefresh, activity, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    interfaceRefreshLoadMore.setStopRefreshing()
                    val dynamicBean = JSON.parseObject(resultData, DynamicBean::class.java)
                    val list = dynamicBean.data
                    if (list != null && list.isNotEmpty()) {
                        ll_noUser.visibility = View.GONE
                        if (page == 1) {
                            dataBeans.clear()
                        }
                        dataBeans.addAll(list)
                        adapter.setNewData(dataBeans)
                    } else {
                        val inflate = LayoutInflater.from(activity).inflate(R.layout.no_more_data_text, null)
                        if (adapter.footerLayoutCount == 0) {
                            adapter.addFooterView(inflate)
                        }
                    }
                }

                override fun doError(msg: String) {
                    interfaceRefreshLoadMore.setStopRefreshing()
                }

                override fun doResult() {

                }
            })
        }
    }

    private fun initEvent() {
        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
            val string = CacheUtils.getString(activity, Constants.User.USER_JSON)
            if (!TextUtils.isEmpty(string)) {
                UserInfoUtil.getUserInfoByJson(string) { infoBean ->
                    if (infoBean?.data != null && isFirst && infoBean.data!!.attention_num < 2) {
                        rootView.ll_noUser.visibility = View.VISIBLE
                        startActivity(Intent(activity, HotUserRecommend::class.java))
                        isFirst = false
                    }
                }
            }
        }

        if (rootView.world_recycler.adapter == null) {
            adapter = DynamicMultipleRecycleAdapter(dataBeans, false)
            rootView.world_recycler.layoutManager = LinearLayoutManager(activity)
            rootView.world_recycler.addItemDecoration(MyRecyclerDetorration(activity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(activity, 5f), R.color.bg_color, true))
            rootView.world_recycler.adapter = adapter
            rootView.world_recycler.setHasFixedSize(true)
        }
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        interfaceRefreshLoadMore.resetRefreshView()
        unbinder!!.unbind()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        page = 1
        initData()
    }

    override fun lazyLoad() {
        if (!isLoad) {
            initData()
            isLoad = true
        }
    }

    companion object {
        const val NEED_REFRESH = 97162
    }
}
