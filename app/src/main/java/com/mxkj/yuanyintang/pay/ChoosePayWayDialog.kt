package com.mxkj.yuanyintang.pay

import android.annotation.SuppressLint
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment
import java.util.concurrent.TimeUnit
import com.mxkj.yuanyintang.pay.ChoosePayWayDialog.payWay.ALI_PAY
import com.mxkj.yuanyintang.pay.ChoosePayWayDialog.payWay.QQ_PAY
import com.mxkj.yuanyintang.pay.ChoosePayWayDialog.payWay.WX_PAY
import kotlinx.android.synthetic.main.choose_pay_way.*
@SuppressLint("ValidFragment")
class ChoosePayWayDialog(private var realPrice: String, private val callBack: PayWayCallback) : BaseDialogFragment() {
    override val contentViewLayoutID: Int
        get() = R.layout.choose_pay_way
    override val isBack: Boolean?
        get() = false

    override fun style(): Int {
        return 0
    }

    override fun initView() {
        tv_price.text = "选择其他支付方式充值  ￥" + realPrice + "元"
        RxView.clicks(img_close)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { dismiss() }
        RxView.clicks(tv_alipay)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    callBack.payWay(ALI_PAY)
                    dismiss()
                }
        RxView.clicks(tv_qq)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    callBack.payWay(QQ_PAY)
                    dismiss()
                }
        RxView.clicks(tv_wx_pay)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    callBack.payWay(WX_PAY)
                    dismiss()
                }
    }
    interface PayWayCallback {
        fun payWay(pay_way: payWay)
    }

    enum class payWay {
        QQ_PAY,
        ALI_PAY,
        WX_PAY
    }
}
