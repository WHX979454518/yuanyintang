package com.mxkj.yuanyintang.extraui.gift

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.pay.ChoosePayWayDialog
import com.mxkj.yuanyintang.pay.PayUtils
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder
import com.mxkj.yuanyintang.utils.uiutils.Toast

class ConfirmGiveGiftDialog {
    private var confirmTxt = "确认"
    private var cancleTxt = "取消"
    private var title = "赠送确认"
    private var content = ""
    private var tips = ""
    private var giftUrl = ""
    private var giftName = ""
    private var isRecharge: Boolean = false
    private var oldPrice: String? = null
    private var price = "0"
    private var last_payWay: Int = 0
    private var gift_id: Int = 0
    private var chargeId: Int = 0
    private var type: Int = 0//1标注2限时
    private var realPrice: String? = null

    private var dialog: DiaLogBuilder? = null
    fun isRecharge(isRecharge: Boolean): ConfirmGiveGiftDialog {
        this.isRecharge = isRecharge
        return this
    }

    fun payWay(payway: Int): ConfirmGiveGiftDialog {
        this.last_payWay = payway
        return this
    }

    fun type(type: Int): ConfirmGiveGiftDialog {
        this.type = type
        return this
    }

    fun confirmText(confirmTxt: String): ConfirmGiveGiftDialog {
        this.confirmTxt = confirmTxt
        return this
    }

    fun cancleText(cancleTxt: String): ConfirmGiveGiftDialog {
        this.cancleTxt = cancleTxt
        return this
    }

    fun title(title: String): ConfirmGiveGiftDialog {
        this.title = title
        return this
    }

    fun oldPrice(oldPrice: String): ConfirmGiveGiftDialog {
        this.oldPrice = oldPrice
        return this
    }

    fun price(price: String): ConfirmGiveGiftDialog {
        this.price = price
        return this
    }

    fun content(content: String): ConfirmGiveGiftDialog {
        this.content = content
        return this
    }

    fun tips(tips: String): ConfirmGiveGiftDialog {
        this.tips = tips
        return this
    }

    fun giftUrl(giftUrl: String?): ConfirmGiveGiftDialog {
        giftUrl?.let { this.giftUrl = giftUrl }
        return this
    }

    fun giftName(giftName: String?): ConfirmGiveGiftDialog {
        giftName?.let { this.giftName = giftName }
        return this
    }

    fun showDialog(mContext: Context, onBtClick: onBtClick): ConfirmGiveGiftDialog {
        val view = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm_give_gift, null)
        dialog = DiaLogBuilder(mContext)
                .setContentView(view)
                .setAniMo(R.anim.popup_in)
                .setFullScreen()
                .setCanceledOnTouchOutside(false)
                .setGrvier(Gravity.CENTER)

        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val tv_content = view.findViewById<TextView>(R.id.tv_content)
        val tv_tips = view.findViewById<TextView>(R.id.tv_tips)
        val tv_old_price = view.findViewById<TextView>(R.id.tv_old_price)
        val tv_price = view.findViewById<TextView>(R.id.tv_price)
        val tv_pay = view.findViewById<TextView>(R.id.tv_pay)
        val tv_confirm = view.findViewById<TextView>(R.id.tv_confirm)
        val tv_cancle = view.findViewById<TextView>(R.id.tv_cancle)
        val ll_recharge = view.findViewById<LinearLayout>(R.id.ll_recharge)
        val ll_give_gift = view.findViewById<LinearLayout>(R.id.ll_give_gift)
        if (isRecharge) {//充值
            when (last_payWay) {
                0 -> tv_pay.text = "使用支付宝支付"
                1 -> tv_pay.text = "使用微信支付"
                2 -> tv_pay.text = "使用QQ支付"
                3 -> tv_pay.text = "使用支付宝支付"
            }
            ll_recharge.visibility = View.VISIBLE
            ll_give_gift.visibility = View.GONE
            tv_tips.visibility = View.GONE
            tv_pay.visibility = View.VISIBLE

            if (type == 1) {//标准
                tv_old_price.visibility = View.GONE
                tv_price.text = "￥" + StringUtils.isEmpty(oldPrice)
                realPrice = oldPrice
            } else if (type == 2) {
                tv_old_price.visibility = View.VISIBLE
                tv_price.text = "￥" + StringUtils.isEmpty(price)
                tv_old_price.text = "￥" + oldPrice!!
                tv_old_price.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                tv_old_price.paint.isAntiAlias = true
                realPrice = price
            }
            tv_pay.setOnClickListener {
                dismiss()
                val payWayDialog = ChoosePayWayDialog(realPrice!!, object : ChoosePayWayDialog.PayWayCallback {
                    override fun payWay(pay_way: ChoosePayWayDialog.payWay) {
                        if (pay_way == null) {
                            return
                        }
                        when (pay_way) {
                            ChoosePayWayDialog.payWay.QQ_PAY -> PayUtils.pay(mContext, chargeId, 2)
                            ChoosePayWayDialog.payWay.ALI_PAY -> PayUtils.pay(mContext, chargeId, 3)
                            ChoosePayWayDialog.payWay.WX_PAY -> if (isWeixinAvilible(mContext)) {
                                PayUtils.pay(mContext, chargeId, 1)
                            } else {
                                if (mContext is Activity) {
                                    Toast.create(mContext).show("你还没安装微信")
                                }
                            }
                        }
                        dismiss()
                        if (mContext is BaseActivity) {
                            mContext.hideLoadingView()
                        }

                    }
                })
                payWayDialog.show((mContext as AppCompatActivity).supportFragmentManager, "")
            }
        } else {
            ll_recharge.visibility = View.GONE
            ll_give_gift.visibility = View.VISIBLE
            tv_tips.visibility = View.VISIBLE
            tv_pay.visibility = View.GONE
        }

        val img_gift = view.findViewById<ImageView>(R.id.img_gift)
        val tv_gift_name = view.findViewById<TextView>(R.id.tv_gift_name)
        val img_close = view.findViewById<ImageView>(R.id.img_close)
        if (!TextUtils.isEmpty(giftUrl)) {
            ImageLoader.with(mContext).getSize(100, 100).url(giftUrl).into(img_gift)
        }
        if (!TextUtils.isEmpty(giftName)) {
            tv_gift_name.text = giftName
        }

        tvTitle.text = title
        tv_content.text = content
        tv_tips.text = tips
        tv_cancle.text = cancleTxt
        tv_confirm.text = confirmTxt
        tv_cancle.setOnClickListener {
            onBtClick.onCancle()
            dialog!!.setDismiss()
        }
        img_close.setOnClickListener {
            onBtClick.onCancle()
            dialog!!.setDismiss()
        }
        tv_confirm.setOnClickListener {
            dialog!!.setDismiss()
            onBtClick.onConfirm()
        }
        dialog!!.show()
        return this
    }

    fun dismiss() {
        if (dialog != null) {
            dialog!!.setDismiss()
        }
    }

    fun giftId(gift_id: Int): ConfirmGiveGiftDialog {
        this.gift_id = gift_id
        return this
    }

    fun chargeId(chargeId: Int): ConfirmGiveGiftDialog {
        this.chargeId = chargeId
        return this
    }

    interface onBtClick {
        fun onConfirm()

        fun onCancle()

    }

    companion object {
        fun newInstance(): ConfirmGiveGiftDialog {
            return ConfirmGiveGiftDialog()
        }

        fun isWeixinAvilible(context: Context): Boolean {
            val packageManager = context.packageManager
            val pinfo = packageManager.getInstalledPackages(0)
            if (pinfo != null) {
                for (i in pinfo.indices) {
                    val pn = pinfo[i].packageName
                    if (pn == "com.tencent.mm") {
                        return true
                    }
                }
            }
            return false
        }
    }


}
