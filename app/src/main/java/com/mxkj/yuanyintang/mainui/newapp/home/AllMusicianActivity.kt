package com.mxkj.yuanyintang.mainui.newapp.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import com.alibaba.fastjson.JSON
import com.flyco.tablayout.SlidingTabLayout
import com.flyco.tablayout.SousuoSlidingTabLayout
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.mainui.home.adapter.SlidingFragmentViewPager
import com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity.CHART_ID
import com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity.CHART_TIME_TYPE
import com.mxkj.yuanyintang.net.NetWork
import okhttp3.Headers

/**
 * Created by Liujie 2017/11/29.
 */
class AllMusicianActivity : StandardUiActivity() {
    private var whereBeanList = ArrayList<OrderTypeBean.DataBean.WhereBean>()
    private var slidingFragmentViewPager: SlidingFragmentViewPager? = null
    internal var strings: ArrayList<String> = ArrayList()
    private val fragments = ArrayList<Fragment>()
    private var id: Int = 0
    private var fields: String? = null
    private var title: String? = null
    internal var tabs: SousuoSlidingTabLayout? = null
    internal var viewpager: ViewPager? = null
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    override fun setLayoutId(): Int {
        return R.layout.activity_all_musician_fragment
    }

    override fun initView() {
        setTitleText("音乐人")
        tabs = findViewById(R.id.tabs)
        viewpager = findViewById(R.id.viewpager)
    }

    override fun initData() {
//        水平筛选滚动条
        NetWork.getOrderType(this, "musician", object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val parseObject = JSON.parseObject(resultData, OrderTypeBean::class.java)
                whereBeanList = parseObject.data.where as ArrayList<OrderTypeBean.DataBean.WhereBean>
                var newList = ArrayList<OrderTypeBean.DataBean.WhereBean>()
                var field: String
                whereBeanList.forEach {
                    field = it.field
                    it.list.forEach {
                        var newBean = OrderTypeBean.DataBean.WhereBean()
                        newBean.title = it.title
                        newBean.id = it.id
                        newBean.field = field
                        newList.add(newBean)
                    }
                }

                newList.forEach {
                    title = it.title
                    strings.add(title.toString())
                }
                for (i in newList.indices) {
                    id = newList.get(i).id
                    fields = newList.get(i).field
                    val bundle = Bundle()
                    bundle.putInt(CHART_ID, id)
                    bundle.putString(CHART_TIME_TYPE, fields)
                    val fragment = AllMusicanFragment()
                    fragment.arguments = bundle
                    fragments.add(fragment)
                }
                slidingFragmentViewPager = SlidingFragmentViewPager(supportFragmentManager, strings, fragments, this@AllMusicianActivity)
                viewpager!!.adapter = slidingFragmentViewPager
                tabs!!.setViewPager(viewpager)
                tabs!!.updateTabSelection(0)
                viewpager!!.offscreenPageLimit = strings.size

            }

            override fun doError(msg: String) {
            }

            override fun doResult() {
            }

        })
    }



    override fun initEvent() {

    }
}
