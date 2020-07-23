package com.mxkj.yuanyintang.mainui.newapp.pond

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.mainui.home.adapter.SlidingFragmentViewPager
import com.mxkj.yuanyintang.mainui.newapp.home.OrderTypeBean
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.uiutils.Toast
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlinx.android.synthetic.main.em_activity_chat.*
import java.util.ArrayList
import kotlinx.android.synthetic.main.fragment_hot_pond.view.*
import okhttp3.Headers

class HotPondMainFrg : RxFragment() {
    private lateinit var homeActivity: HomeActivity
    private var rootView: View? = null
    internal var strings: MutableList<String> = ArrayList()
    private var fragments: MutableList<Fragment> = ArrayList()
    private lateinit var slidingFragmentViewPager: SlidingFragmentViewPager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_hot_pond, container, false)
        homeActivity = activity as HomeActivity
        initView()
        initEvent()
        netData()
        return rootView
    }

    private fun initView() {
        initViewPager()
    }

    private fun netData() {

    }

    private fun initEvent() {

    }

    /**
     * 初始化池塘分类（二级）
     */
    private fun jsonPondData(resultData: String) {
        rootView!!.postDelayed(object :Runnable{
            override fun run() {
                strings = ArrayList()
                fragments = ArrayList()
                val parseObject = JSON.parseObject(resultData, OrderTypeBean::class.java)
                var whereBeanList = parseObject.data.where as ArrayList<OrderTypeBean.DataBean.WhereBean>
                whereBeanList.forEach { i ->
                    if (i.field == "tag_class") {
                        val list = i.list
                        list.forEach { listBean ->
                            var hotPondSonFrg = HotPondSonFrg()
                            var bundle = Bundle()
                            bundle.putString("tag_clas", listBean.id.toString())
                            hotPondSonFrg.arguments = bundle
                            fragments.add(hotPondSonFrg)
                            strings.add(listBean.title)

                        }
                    }
                }
                slidingFragmentViewPager = SlidingFragmentViewPager(childFragmentManager, strings, fragments, activity)
                rootView?.viewpager?.adapter = slidingFragmentViewPager
                rootView?.tabs?.setViewPager(rootView?.viewpager)
                rootView?.viewpager?.offscreenPageLimit = strings.size
                rootView?.tabs?.updateTabSelection(0)
            }
        },500)

    }

    private fun initViewPager() {
        val jsondata = CacheUtils.getString(activity, "pondClassSon")
        if (!jsondata.isNullOrEmpty()) {
            jsonPondData(jsondata!!)
            return
        }
        NetWork.getOrderType(activity, "topic", object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                CacheUtils.setString(activity,"pondClassSon",resultData)
                jsonPondData(resultData);
            }

            override fun doError(msg: String) {
            }

            override fun doResult() {
            }

        })
        rootView?.viewpager?.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
            }

        })

    }

}

