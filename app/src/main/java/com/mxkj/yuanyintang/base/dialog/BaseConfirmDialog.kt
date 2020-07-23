package com.mxkj.yuanyintang.base.dialog

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder

/**
 * 全局通用的一个二次确认，dialog（标题，提示语，确认，取消）
 */

class BaseConfirmDialog {
    private var confirmTxt: String? = null
    private var cancleTxt: String? = null
    private var title: String? = null
    private var content: String? = null
    private var tips: String? = null
    private var isShowTips = true
    private var isShowEdittext: Boolean = false//这个默认不显示，是一个输入框
    private var isOnrBtn: Boolean = false
    private var isShowYxy: Boolean = false
    open lateinit var et_reason: EditText
    private var dialog: DiaLogBuilder? = null

    fun confirmText(confirmTxt: String): BaseConfirmDialog {
        this.confirmTxt = confirmTxt
        return this
    }

    fun isShowYxy(isShow: Boolean): BaseConfirmDialog {
        this.isShowYxy = isShow
        return this
    }

    fun cancleText(cancleTxt: String): BaseConfirmDialog {
        this.cancleTxt = cancleTxt
        return this
    }

    fun title(title: String): BaseConfirmDialog {
        this.title = title
        return this
    }

    fun content(content: String): BaseConfirmDialog {
        this.content = content
        return this
    }

    fun tips(tips: String): BaseConfirmDialog {
        this.tips = tips
        return this
    }

    fun title(isShowTips: Boolean): BaseConfirmDialog {
        this.isShowTips = isShowTips
        return this
    }

    fun isOneBtn(isOnrBtn: Boolean): BaseConfirmDialog {
        this.isOnrBtn = isOnrBtn
        return this
    }

    fun isShowEdittext(isShowEdittext: Boolean): BaseConfirmDialog {
        this.isShowEdittext = isShowEdittext
        return this
    }

    fun showDialog(mContext: Context, onBtClick: onBtClick): BaseConfirmDialog {
        val view = View.inflate(mContext, R.layout.dialog_confirm, null)
        dialog = DiaLogBuilder(mContext)
                .setContentView(view)
                .setAniMo(R.anim.popup_in)
                .setFullScreen()
                .setCanceledOnTouchOutside(false)
                .setGrvier(Gravity.CENTER)

        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val iv_yxy = view.findViewById<ImageView>(R.id.iv_yxy)
        val tv_content = view.findViewById<TextView>(R.id.tv_content)
        val tv_tips = view.findViewById<TextView>(R.id.tv_tips)
        val tv_confirm = view.findViewById<TextView>(R.id.tv_confirm)
        val tv_cancle = view.findViewById<TextView>(R.id.tv_cancle)
        et_reason = view.findViewById(R.id.et_reason)
        val img_close = view.findViewById<ImageView>(R.id.img_close)
        val tv_confirm_oneBtn = view.findViewById<TextView>(R.id.tv_confirm_oneBtn)
        val ll_confirm_btn = view.findViewById<LinearLayout>(R.id.ll_confirm_btn)
        tvTitle.text = title
        tv_content.text = content
        tips?.let {
            tv_tips.text = tips
        }
        tv_cancle.text = cancleTxt
        tv_confirm.text = confirmTxt
        tv_confirm_oneBtn.text = confirmTxt
        if (!isShowTips || tips == null) {
            tv_tips.visibility = View.GONE
        } else {
            tv_tips.visibility = View.VISIBLE
        }
        if (isShowEdittext == true) {
            et_reason.visibility = View.VISIBLE
        }
        if (isOnrBtn) {
            ll_confirm_btn.visibility = View.GONE
            tv_confirm_oneBtn.visibility = View.VISIBLE
            img_close.visibility = View.VISIBLE
        }
        if (isShowYxy) {
            iv_yxy.visibility=VISIBLE
        }else{
            iv_yxy.visibility= GONE
        }

        tv_cancle.setOnClickListener {
            onBtClick.onCancle()
            dialog!!.setDismiss()
        }
        img_close.setOnClickListener {
            onBtClick.onCancle()
            dialog!!.setDismiss()
        }
        tv_confirm.setOnClickListener(View.OnClickListener {
            if (isShowEdittext) {
                if (TextUtils.isEmpty(et_reason.text)) {
                    return@OnClickListener
                } else {
                    dialog!!.setDismiss()
                    onBtClick.onConfirm()
                }
            } else {
                dialog!!.setDismiss()
                onBtClick.onConfirm()
            }
        })
        tv_confirm_oneBtn.setOnClickListener {
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

    interface onBtClick {
        fun onConfirm()

        fun onCancle()

    }

    companion object {

        fun newInstance(): BaseConfirmDialog {
            return BaseConfirmDialog()
        }
    }

}
