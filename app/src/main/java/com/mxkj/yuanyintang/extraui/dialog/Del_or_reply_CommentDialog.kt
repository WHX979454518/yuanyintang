package com.mxkj.yuanyintang.extraui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment
import com.mxkj.yuanyintang.mainui.comment.Comment
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil
import com.mxkj.yuanyintang.extraui.adapter.TitleOperationAdapter
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.bean.TitleOperationBean
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.widget.CommentEditDialogFrag

import java.util.ArrayList
import java.util.concurrent.TimeUnit
import kotlinx.android.synthetic.main.dialog_title_operation.*
@SuppressLint("ValidFragment")
class Del_or_reply_CommentDialog : BaseDialogFragment {
    internal var dataBean: Comment.DataBean? = null
    private var titleOperationBeanList: MutableList<TitleOperationBean> = ArrayList()
    private var dialogForComment: CommentEditDialogFrag? = null
    private var bean: Comment.DataBean.SonBean? = null
    private var context: Activity? = null
    private var dataList: List<Comment.DataBean>? = null
    internal var callback: ReplySuccessCallback? = null
    private var isPond = false
    internal var fragmentManager: FragmentManager? = null
    override val contentViewLayoutID: Int
        get() = R.layout.dialog_title_operation
    override val isBack: Boolean?
        get() = false

    constructor(fragmentManager: FragmentManager, isPond: Boolean, mContext: Context, dataBean: Comment.DataBean, dataList: List<Comment.DataBean>, callback: ReplySuccessCallback) {
        this.dataBean = dataBean
        this.context = mContext as Activity
        this.dataList = dataList
        this.callback = callback
        this.isPond = isPond
        this.fragmentManager = fragmentManager
    }

    constructor()

    override fun style(): Int {
        return 0
    }

    override fun initView() {
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.addItemDecoration(MyRecyclerDetorration(activity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(activity, 0.6f), R.color.dividing_line_color, true))
        titleOperationBeanList.add(TitleOperationBean("回复"))
        UserInfoUtil.getUserInfoByJson(CacheUtils.getString(context, Constants.User.USER_JSON)) { infoBean ->
            if (infoBean != null && infoBean.data != null) {
                if (infoBean.data!!.id == dataBean!!.uid) {
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
            if (null == dataBean) {
                return@OnItemClickListener
            }
            when (titleOperationBeanList[position].title) {
                "回复" -> {
                    dialogForComment = CommentEditDialogFrag(dataBean!!.nickname, 0, 0, dataBean!!.id, dataBean!!.fid, isPond, CommentEditDialogFrag.successCallBack { json ->
                        bean = dialogForComment!!.setSonBean(json)
                        val son = ArrayList<Comment.DataBean.SonBean>()
                        if (dataBean!!.son != null) {
                            son.addAll(dataBean!!.son!!)
                        }
                        son.add(0, bean!!)
                        for (i in dataList!!.indices) {
                            if (dataList!![i].id == dataBean!!.id) {
                                dataList!![i].son = son
                                callback!!.onSuccess(dataList!!)
                                break
                            }
                        }
                    })
                    if (dialogForComment != null) {
                        if (dialogForComment!!.isAdded) {
                            dialogForComment!!.dismiss()
                        }
                    }
                    dialogForComment!!.show(fragmentManager, "")
                    dialogForComment!!.setEtHint(dataBean!!.nickname)
                }
                "删除" -> { }
                "举报" -> {
                    val musicBean = MusicBean()
                    musicBean.setReportType(5)
                            .setReportId(dataBean!!.id)
                    val reportOperationDialog = ReportOperationDialog(musicBean)
                    reportOperationDialog.show(activity.supportFragmentManager, "mReportOperationDialog")
                }
            }
        }
        RxView.clicks(btn_cancel)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { dismiss() }
    }

    interface ReplySuccessCallback {
        fun onSuccess(dataList: List<Comment.DataBean>)
    }
}

