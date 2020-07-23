package com.mxkj.yuanyintang.extraui.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.extraui.activity.ReportActivity
import com.mxkj.yuanyintang.extraui.adapter.ReportOperationAdapter
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.bean.ReportOperationBean
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import okhttp3.Headers
import kotlinx.android.synthetic.main.dialog_report_operation.*
@SuppressLint("ValidFragment")
class ReportOperationDialog : BaseDialogFragment {
    private var reportOperationBeanList: MutableList<ReportOperationBean.DataBean> = ArrayList()
    private lateinit var reportOperationAdapter: ReportOperationAdapter
    internal var musicBean: MusicBean? = null
    override val contentViewLayoutID: Int
        get() = R.layout.dialog_report_operation

    override val isBack: Boolean?
        get() = false

    constructor(musicBean: MusicBean) {
        this.musicBean = musicBean
    }

    constructor()

    override fun style(): Int {
        return 0
    }

    override fun initView() {
        recyclerview!!.layoutManager = LinearLayoutManager(activity)
        reportOperationAdapter = ReportOperationAdapter(reportOperationBeanList, activity)
        recyclerview!!.adapter = reportOperationAdapter
        reportOperationAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            if (null == musicBean) {
                return@OnItemClickListener
            }
            if (reportOperationBeanList[position].id == 0) {
            } else {
                val bundle = Bundle()
                bundle.putInt(ReportActivity.REPORT_ITEM_ID, musicBean!!.getReportId())
                bundle.putInt(ReportActivity.REPORT_PID, reportOperationBeanList[position].id)
                goActivity(ReportActivity::class.java, bundle)
                dismiss()
            }
        }
        RxView.clicks(tv_cancle!!)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { dismiss() }

        netData()
    }

    private fun netData() {

        if (null == musicBean) {
            return
        }
        NetWork.getFeedback(activity, musicBean!!.getReportType().toString() + "", object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val reportOperationBean = JSON.parseObject(resultData, ReportOperationBean::class.java)
                refreshData(reportOperationBean)

            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    private fun refreshData(reportOperationBean: ReportOperationBean) {
        reportOperationBeanList.clear()
        reportOperationBeanList.addAll(reportOperationBean.data!!)
        reportOperationAdapter.notifyDataSetChanged()
    }
}
