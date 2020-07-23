package com.mxkj.yuanyintang.mainui.comment

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.bean.UserInfo
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.extraui.adapter.TitleOperationAdapter
import com.mxkj.yuanyintang.extraui.bean.TitleOperationBean
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import okhttp3.Headers
import kotlinx.android.synthetic.main.dialog_title_operation.*
@SuppressLint("ValidFragment")
class CommentDetialOperationDialog : BaseDialogFragment {
    private var parent_bean: Comment.DataBean? = null
    private var titleOperationBeanList: MutableList<TitleOperationBean> = ArrayList()
    override val contentViewLayoutID: Int
        get() = R.layout.dialog_title_operation

    override val isBack: Boolean?
        get() = false

    constructor()

    constructor(parent_bean: Comment.DataBean) {
        this.parent_bean = parent_bean
    }

    override fun style(): Int {
        return 0
    }

    override fun initView() {
        recyclerview!!.layoutManager = LinearLayoutManager(activity)
        recyclerview!!.addItemDecoration(MyRecyclerDetorration(activity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(activity, 0.6f), R.color.dividing_line_color, true))
        val string = CacheUtils.getString(MainApplication.application, Constants.User.USER_JSON)
        val userInfo = JSON.parseObject(string, UserInfo::class.java)
        if (userInfo?.data != null && userInfo.data!!.id != 0) {
            if (userInfo.data!!.id == parent_bean!!.uid) {
                titleOperationBeanList.add(TitleOperationBean("删除"))
            } else {
                titleOperationBeanList.add(TitleOperationBean("举报"))
            }
        }

        val titleOperationAdapter = TitleOperationAdapter(titleOperationBeanList, activity)
        recyclerview!!.adapter = titleOperationAdapter
        titleOperationAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            dismiss()
            if (null == parent_bean) {
                return@OnItemClickListener
            }
            when (titleOperationBeanList[position].title) {
                "删除" -> NetWork.deleComment(activity, parent_bean!!.id, object : NetWork.TokenCallBack {
                    override fun doNext(resultData: String, headers: Headers?) {
                        try {
                            if (activity != null) {
                                activity.finish()
                            }
                        } catch (e: RuntimeException) {
                        }
                    }
                    override fun doError(msg: String) {
                    }
                    override fun doResult() {
                    }
                })
                "举报" -> {
                }
            }
        }
        RxView.clicks(btn_cancel!!)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { dismiss() }
    }
}
