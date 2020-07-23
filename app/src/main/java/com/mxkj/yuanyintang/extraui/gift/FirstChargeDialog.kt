package com.mxkj.yuanyintang.extraui.gift

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder

class FirstChargeDialog {
    private var dialog: DiaLogBuilder? = null
    fun showDialog(mContext: Context, onBtClick: onBtClick): FirstChargeDialog {
        val view = LayoutInflater.from(mContext).inflate(R.layout.dialog_first_charge, null)
        dialog = DiaLogBuilder(mContext)
                .setContentView(view)
                .setAniMo(R.anim.popup_in)
                .setFullScreen()
                .setCanceledOnTouchOutside(false)
                .setGrvier(Gravity.CENTER)
        val tv_get_gift = view.findViewById<TextView>(R.id.tv_get_gift)
        val disMiss = view.findViewById<ImageView>(R.id.disMiss)
        tv_get_gift.setOnClickListener {
            onBtClick.onConfirm()
            dismiss()
        }

        disMiss.setOnClickListener {
            dismiss()
            onBtClick.onCancle()
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
        fun newInstance(): FirstChargeDialog {
            return FirstChargeDialog()
        }
    }

}
