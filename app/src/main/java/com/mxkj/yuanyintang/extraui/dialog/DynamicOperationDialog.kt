package com.mxkj.yuanyintang.extraui.dialog

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager

import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.dynamic.bean.DynamicBean
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil
import com.mxkj.yuanyintang.extraui.adapter.TitleOperationAdapter
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.bean.TitleOperationBean
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import okhttp3.Headers
import kotlinx.android.synthetic.main.dialog_title_operation.*
@SuppressLint("ValidFragment")
class DynamicOperationDialog private constructor() : BaseDialogFragment() {
    private var bean: DynamicBean.DataBean? = null
    private var titleOperationBeanList: MutableList<TitleOperationBean> = ArrayList()
    override val contentViewLayoutID: Int
        get() = R.layout.dialog_title_operation

    override val isBack: Boolean?
        get() = false

    override fun style(): Int {
        return 0
    }

    override fun initView() {
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.addItemDecoration(MyRecyclerDetorration(activity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(activity, 0.6f), R.color.dividing_line_color, true))
        UserInfoUtil.getUserInfoByJson(CacheUtils.getString(activity, Constants.User.USER_JSON)) { infoBean ->
            if (infoBean?.data != null) {
                if (infoBean.data!!.id == bean!!.uid) {
                    titleOperationBeanList.add(TitleOperationBean("删除"))
                } else {
                    titleOperationBeanList.add(TitleOperationBean("举报"))
                }
            }
        }
        val titleOperationAdapter = TitleOperationAdapter(titleOperationBeanList, activity)
        recyclerview.adapter = titleOperationAdapter
        titleOperationAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            dismiss()
            if (null == bean) {
                return@OnItemClickListener
            }
            when (titleOperationBeanList[position].title) {
                "删除" -> NetWork.deleDynamic(activity, bean!!.id, object : NetWork.TokenCallBack {

                    override fun doNext(resultData: String, headers: Headers?) {
                        activity.finish()
                    }

                    override fun doError(msg: String) {

                    }

                    override fun doResult() {

                    }
                })
                "举报" -> {
                    val musicBean = MusicBean()
                    musicBean.setReportType(3).setType(3).setReportId(bean!!.id)
                    val reportOperationDialog = ReportOperationDialog(musicBean)
                    reportOperationDialog.show(activity.supportFragmentManager, "mReportOperationDialog")
                }
            }
        }
        RxView.clicks(btn_cancel)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { dismiss() }
    }
}
