package com.mxkj.yuanyintang.extraui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment
import com.mxkj.yuanyintang.mainui.pond.activity.PondReplyDetialActivity
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialReplyDialogFrag
import com.mxkj.yuanyintang.mainui.pond.bean.PondCommentBean
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
class Del_or_reply_Dialog_PongDetial : BaseDialogFragment {
    internal var dataBean: PondCommentBean.DataBean? = null
    private var titleOperationBeanList: MutableList<TitleOperationBean> = ArrayList()
    private var dialogForComment: PondDetialReplyDialogFrag? = null
    private var context: Activity? = null
    private var dataList: MutableList<PondCommentBean.DataBean>? = null
    internal lateinit var callback: ReplySuccessCallback
    private lateinit var commentType: String
    internal var position: Int = 0
    override val contentViewLayoutID: Int
        get() = R.layout.dialog_title_operation

    override val isBack: Boolean?
        get() = false

    constructor(mContext: Context, commentType: String, position: Int, dataBean: PondCommentBean.DataBean, dataList: MutableList<PondCommentBean.DataBean>?, callback: ReplySuccessCallback) {
        this.dataBean = dataBean
        this.context = mContext as Activity
        this.commentType = commentType
        this.position = position
        if (dataList != null) {
            this.dataList = dataList
        }
        this.callback = callback
    }

    constructor(mContext: Context, dataBean: PondCommentBean.DataBean, dataList: MutableList<PondCommentBean.DataBean>?, callback: ReplySuccessCallback) {
        this.dataBean = dataBean
        this.context = mContext as Activity
        if (dataList != null) {
            this.dataList = dataList
        }
        this.callback = callback
    }

    constructor()

    override fun style(): Int {
        return 0
    }

    override fun initView() {
        recyclerview!!.layoutManager = LinearLayoutManager(activity)
        recyclerview!!.addItemDecoration(MyRecyclerDetorration(activity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(activity, 0.6f), R.color.dividing_line_color, true))
        if (dataList != null && dataList!!.size > 0) {
            titleOperationBeanList.add(TitleOperationBean("回复"))
        }
        UserInfoUtil.getUserInfoByJson(CacheUtils.getString(context, Constants.User.USER_JSON)) { infoBean ->
            if (infoBean!!.data!!.id == dataBean!!.uid) {
                titleOperationBeanList.add(TitleOperationBean("删除"))
            } else {
                titleOperationBeanList.add(TitleOperationBean("举报"))
            }
        }
        val titleOperationAdapter = TitleOperationAdapter(titleOperationBeanList, activity)
        recyclerview!!.adapter = titleOperationAdapter
        titleOperationAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            dismiss()
            if (null == dataBean) {
                return@OnItemClickListener
            }
            when (titleOperationBeanList[position].title) {
                "回复" -> {
                    dialogForComment = PondDetialReplyDialogFrag(dataBean!!.nickname, childFragmentManager, 0, dataBean!!.id, PondDetialReplyDialogFrag.successCallBack { json ->
                        val jsonObject = JSON.parseObject(json)
                        val comListsBean = jsonObject.getObject("data", PondCommentBean.DataBean.ComListsBean::class.java)
                        if (dataBean!!.com_lists != null) {
                            dataBean!!.com_lists.add(comListsBean)
                            if (dataList != null) {
                                for (i in dataList!!.indices) {
                                    if (dataList!![i].id == dataBean!!.id) {
                                        dataList!![i].com_lists = dataBean!!.com_lists
                                        callback.onSuccess(dataList)
                                        break
                                    }
                                }
                            }
                        }
                    })
                    if (dialogForComment != null) {
                        if (dialogForComment!!.isAdded) {
                            dialogForComment!!.dismiss()
                        }
                        dialogForComment!!.show(fragmentManager, "")
                    }
                }
                "删除" -> NetWork.deleTopicComment(context!!, dataBean!!.id, object : NetWork.TokenCallBack {
                    override fun doNext(resultData: String, headers: Headers?) {
                        try {
                            (context as BaseActivity).setSnackBar("删除成功", "", R.drawable.icon_success)
                            if (context is PondReplyDetialActivity) {
                                context!!.finish()
                            }
                        } catch (e: RuntimeException) {
                        }

                        if (dataList != null) {
                            for (i in dataList!!.indices) {
                                if (dataList!![i].id == dataBean!!.id) {
                                    dataList!!.removeAt(i)
                                    callback.onSuccess(dataList)
                                    break
                                }
                            }
                        }


                    }

                    override fun doError(msg: String) {

                    }

                    override fun doResult() {

                    }
                })
                "举报" -> {
                    val musicBean = MusicBean()
                    musicBean.setReportType(4).setReportId(dataBean!!.id)
                    val reportOperationDialog = ReportOperationDialog(musicBean)
                    reportOperationDialog.show(activity.supportFragmentManager, "mReportOperationDialog")
                }
            }
        }

        RxView.clicks(btn_cancel!!)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { dismiss() }
    }


    interface ReplySuccessCallback {
        fun onSuccess(data: List<PondCommentBean.DataBean>?)
    }
}
