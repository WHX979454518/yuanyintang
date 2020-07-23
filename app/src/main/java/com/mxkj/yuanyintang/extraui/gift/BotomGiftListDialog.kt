package com.mxkj.yuanyintang.extraui.gift

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView

import com.alibaba.fastjson.JSON
import com.jakewharton.rxbinding2.view.RxView
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.FileCallback
import com.lzy.okgo.model.Response
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment
import com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity
import com.mxkj.yuanyintang.mainui.myself.doughnut.ChargeDoughnutActivity
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils

import java.io.File
import java.util.ArrayList
import java.util.concurrent.TimeUnit

import butterknife.Unbinder
import com.mxkj.yuanyintang.mainui.web.CommonWebview
import com.mxkj.yuanyintang.musicplayer.activity.PlayerActivity
import com.mxkj.yuanyintang.video.MvVideoActivitykt
import kotlinx.android.synthetic.main.dot.view.*
import okhttp3.Headers
import kotlinx.android.synthetic.main.fragment_main_gift.*

/**
 * viewpager所在的fragment，出处理viewpaher和滑动切换
 */
class BotomGiftListDialog : BaseDialogFragment() {
    internal var unbinder: Unbinder? = null
    private var mPagerList: MutableList<View>? = null//页面集合
    private var mInflater: LayoutInflater? = null
    lateinit var clickBean: GiftListsBean.DataBean.GiftListBean
    private var pageCount: Int = 0
    private val pageSize = 8
    private var curIndex = 0
    private var arr: Array<GridViewAdapter?>? = null
    private var gift_list: List<GiftListsBean.DataBean.GiftListBean>? = null
    private var my_coin: Int = 0
    var music_id = 0
    override val contentViewLayoutID: Int
        get() = R.layout.fragment_main_gift

    override val isBack: Boolean?
        get() = false

    override fun style(): Int {
        return 0
    }

    private lateinit var mContext: Context

    override fun initView() {
        mContext = activity
        initValues()
    }

    private fun initValues() {
        NetWork.getGiftList(mContext!!, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                CacheUtils.setString(MainApplication.application, "giftStr", resultData)
                parseJson(resultData)
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    private fun parseJson(resultData: String?) {
        resultData?.let {
            val giftListsBean = JSON.parseObject(resultData, GiftListsBean::class.java)
            val data = giftListsBean.data ?: return
            gift_list = data?.gift_list
            if (gift_list != null && gift_list!!.isNotEmpty()) {
                my_coin = data.my_coin
                val setting = data.setting
                val firstCharge = setting!!.first_charge
                if (firstCharge > 0) {//首冲
                    if (img_tips == null) return
                    img_tips.visibility = View.VISIBLE
                }
                giftView()
            }
        }
    }

    private fun giftView() {
        if (tv_dought_num == null) return
        tv_dought_num.text = "甜甜圈余额：$my_coin"
        mInflater = LayoutInflater.from(mContext)
        pageCount = Math.ceil(gift_list!!.size * 1.0 / pageSize).toInt()
        arr = arrayOfNulls(pageCount)
        mPagerList = ArrayList()
        for (j in 0 until pageCount) {
            val gridview = mInflater!!.inflate(R.layout.bottom_vp_gridview, viewpager, false) as GiftGridview
            val gridAdapter = GridViewAdapter(mContext, gift_list!!, j)
            gridview.adapter = gridAdapter
            arr!![j] = gridAdapter
            gridview.selector = ColorDrawable(Color.parseColor("#ebebeb"))
            gridview.onItemClickListener = AdapterView.OnItemClickListener { _, _, position1, _ ->
                val position = position1 + 8 * j
                clickBean = gift_list!![position]
                val path = (Constants.Path.APP_PATH + File.separator + "gift_Ani")
                OkGo.get<File>(clickBean.imgpic).execute(object : FileCallback(path, clickBean.name + ".webp") {
                    override fun onSuccess(response: Response<File>) {
                    }
                })
                val activity = activity ?: return@OnItemClickListener
                if (activity is PlayerActivity) {
                    (getActivity() as PlayerActivity).checkStatus(clickBean.id)
                } else if (activity is ChartsListsActivity) {
                    (getActivity() as ChartsListsActivity).checkStatus(clickBean.id)
                }else if(activity is MvVideoActivitykt){
                    (getActivity() as MvVideoActivitykt).checkStatus(clickBean.id)
                }else if(activity is CommonWebview){
                    (getActivity() as CommonWebview).checkStatus(clickBean.id)
                }
                dismiss()
            }
            mPagerList!!.add(gridview)
        }
        viewpager.adapter = ViewPagerAdapter(mPagerList, mContext)
        RxView.clicks(rl_recharge)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe { mContext!!.startActivity(Intent(mContext, ChargeDoughnutActivity::class.java)) }
        setOvalLayout(pageCount)
    }

    private fun setOvalLayout(pageCount: Int) {
        if (ll_dot == null) return
        ll_dot.removeAllViews()
        for (i in 0 until this.pageCount) {
            ll_dot.addView(mInflater!!.inflate(R.layout.dot, null))
        }
        val childAt = ll_dot.getChildAt(0)
        if (childAt != null) {
            childAt!!.v_dot.setBackgroundResource(R.drawable.icon_gift_current)
            viewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageSelected(position: Int) {
                    for (i in 0 until pageCount) {
                        arr!![i]!!.notifyDataSetChanged()
                    }
                    ll_dot.getChildAt(curIndex).v_dot.setBackgroundResource(R.drawable.icon_gift_gray)
                    ll_dot.getChildAt(position).v_dot.setBackgroundResource(R.drawable.icon_gift_current)
                    curIndex = position
                }

                override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
                override fun onPageScrollStateChanged(arg0: Int) {}
            })
        }

    }
}


