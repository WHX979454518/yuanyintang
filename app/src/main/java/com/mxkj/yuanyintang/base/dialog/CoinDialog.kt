package com.mxkj.yuanyintang.base.dialog

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder

class CoinDialog {
    private var dialog: DiaLogBuilder? = null
    fun showDialog(mContext: Context): CoinDialog {
        val view = LayoutInflater.from(mContext).inflate(R.layout.dialog_coin, null)
        dialog = DiaLogBuilder(mContext)
                .setContentView(view)
                .setAniMo(R.anim.popup_in)
                .setFullScreen()
                .setCanceledOnTouchOutside(false)
                .setGrvier(Gravity.CENTER)
        val disMiss = view.findViewById<ImageView>(R.id.disMiss)
        disMiss.setOnClickListener {
            dismiss()
            mContext.sendBroadcast(Intent("hideCoinDialog"))
        }
        dialog!!.show()
        return this
    }

    fun dismiss() {
        dialog?.setDismiss()
    }

    companion object {
        fun newInstance(): CoinDialog {
            return CoinDialog()
        }
    }

}
