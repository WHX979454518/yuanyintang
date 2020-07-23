package com.mxkj.yuanyintang.mainui.dynamic

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager

import com.flyco.dialog.listener.OnBtnClickL
import com.flyco.dialog.widget.MaterialDialog
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
import com.mxkj.yuanyintang.extraui.dialog.ReportOperationDialog
import com.mxkj.yuanyintang.mainui.dynamic.activity.DynamicDetial
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import okhttp3.Headers
import kotlinx.android.synthetic.main.dialog_title_operation.*

@SuppressLint("ValidFragment")
class DynamicOperationDialog : BaseDialogFragment {
    private val bean: DynamicBean.DataBean
    private var titleOperationBeanList: MutableList<TitleOperationBean> = ArrayList()
    override val contentViewLayoutID: Int
        get() = R.layout.dialog_title_operation

    override val isBack: Boolean?
        get() = false

    constructor(context: Context, bean: DynamicBean.DataBean) {
        this.bean = bean
    }

    override fun style(): Int {
        return 0
    }

    override fun initView() {
        mActivity?.let {
            recyclerview.layoutManager = LinearLayoutManager(mActivity)
            recyclerview.addItemDecoration(MyRecyclerDetorration(mActivity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(it, 0.6f), R.color.dividing_line_color, true))
            val string = CacheUtils.getString(context, Constants.User.USER_JSON)
            UserInfoUtil.getUserInfoByJson(string) { infoBean ->
                if (infoBean != null && infoBean.data?.id == bean.uid) {
                    titleOperationBeanList.add(TitleOperationBean("删除"))
                } else {
                    titleOperationBeanList.add(TitleOperationBean("举报"))
                }
            }
            val titleOperationAdapter = TitleOperationAdapter(titleOperationBeanList, it)
            recyclerview.adapter = titleOperationAdapter
            titleOperationAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
                dismiss()
                if (null == bean) {
                    return@OnItemClickListener
                }
                when (titleOperationBeanList[position].title) {
                    "删除" -> MaterialDialog("删除动态？")
                    "举报" -> {
                        val musicBean = MusicBean()
                        musicBean.setReportType(3).setReportId(bean.id)
                        val reportOperationDialog = ReportOperationDialog(musicBean)
                        if(mActivity is FragmentActivity){
                            reportOperationDialog.show((mActivity as FragmentActivity).supportFragmentManager, "mReportOperationDialog")
                        }
                    }
                }
            }

            RxView.clicks(btn_cancel)
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe { dismiss() }
        }

    }


    private fun MaterialDialog(content: String) {
        val dialog = MaterialDialog(mActivity)
        dialog.content(
                content)//
                .btnText("取消", "确定")//
                .titleTextSize(16f)
                .titleTextColor(ContextCompat.getColor(mActivity, R.color.color_14_text))
                .contentTextColor(ContextCompat.getColor(mActivity, R.color.color_66_text))
                .contentTextSize(14f)
                .btnTextSize(14f)
                .btnTextColor(ContextCompat.getColor(mActivity, R.color.base_red), ContextCompat.getColor(mActivity, R.color.base_red))
                .showAnim(null)//
                .dismissAnim(null)//
                .show()

        dialog.setOnBtnClickL(
                OnBtnClickL
                {
                    dialog.dismiss()
                },
                OnBtnClickL
                {
                    dialog.dismiss()
                    mActivity?.let {
                        NetWork.deleDynamic(it, bean?.id, object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                (mActivity as DynamicDetial).finish()
                                it.sendBroadcast(Intent("publishDynamic"))
                            }

                            override fun doError(msg: String) {

                            }

                            override fun doResult() {

                            }
                        })
                    }
                }
        )
    }
}
