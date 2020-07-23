package com.mxkj.yuanyintang.extraui.activity

import android.os.Handler
import android.support.v4.content.ContextCompat
import android.text.TextUtils

import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.net.NetWork

import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import okhttp3.Headers
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity : StandardUiActivity() {
    override val isVisibilityBottomPlayer: Boolean = false
    private var item_id: Int = 0
    private var pid: Int = 0
    private  var isFinsh: Boolean ?= false
    public override fun setLayoutId(): Int {
        return R.layout.activity_report
    }
    override fun initView() {
        item_id = intent.getIntExtra(REPORT_ITEM_ID, -1)
        pid = intent.getIntExtra(REPORT_PID, -1)//这里的pid传过来的就已经是要举报的那个类型的id，比如音乐id或者歌单id，池塘id'等
        setTitleText("举报")
        setTitleBarBackgroundColor(ContextCompat.getColor(this, R.color.bg_color))
        setRightButtonText("提交")
        navigationBar.getRightButton().setTextColor(ContextCompat.getColor(this, R.color.base_red))
        RxView.clicks(navigationBar.getRightButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    Observable.just(et_report_contents!!.text.toString())
                            .filter { s ->
                                if (TextUtils.isEmpty(s))
                                    contentsVaild()
                                else if (item_id <= 0) itemIdVaild() else true
                            }
                            .subscribe { sendReport() }
                }
        RxView.clicks(navigationBar.getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { onBackPressed() }
    }

    private fun itemIdVaild(): Boolean {
        setSnackBar("未获取到ItemID", "", R.drawable.icon_fails)
        return false
    }

    private fun contentsVaild(): Boolean {
        setSnackBar("举报内容不能为空", "", R.drawable.icon_fails)
        return false
    }

    private fun sendReport() {
        NetWork.postFeedback(this, item_id.toString() + "", "", et_report_contents!!.text.toString(), "", "", pid, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                isFinsh = true
                setSnackBar("举报成功", "", R.drawable.icon_success)
                Handler().postDelayed({ finish() }, 1000)
            }

            override fun doError(msg: String) {
                if (!TextUtils.isEmpty(msg)) {
                    setSnackBar(msg, "", R.drawable.icon_fails)
                }
            }

            override fun doResult() {

            }
        })
    }

    override fun initData() {

    }

    override fun initEvent() {

    }

    override fun onBackPressed() {
        if ((!isFinsh!!)) {
            finish()
        }
    }

    companion object {
        val REPORT_ITEM_ID = "item_id"
        val REPORT_PID = "report_pid"
    }
}
