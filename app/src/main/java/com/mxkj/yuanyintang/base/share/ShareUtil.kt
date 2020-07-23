package com.mxkj.yuanyintang.base.share
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder

object ShareUtil {
    private fun shareImg(context: Context, musicBean: MusicBean) {
        if (NetConnectedUtils.isNetConnected(context)) {
            if (CacheUtils.getBoolean(context, Constants.User.IS_LOGIN, false)) {
                val shareBottomDialog = ShareBottomDialog(context, musicBean)
                shareBottomDialog.show()
            }
        } else {
            context.startActivity(Intent(context, LoginRegMainPage::class.java))
        }
    }
    fun showSavePicDialog(context: Context, musicBean: MusicBean) {
        val view = View.inflate(context, R.layout.dialog_invite_share, null)
        val diaLogBuilder = DiaLogBuilder(context)
                .setContentView(view)
                .setFullScreen()
                .setGrvier(Gravity.CENTER)
                .show()
        diaLogBuilder.setCanceledOnTouchOutside(true)
        val tv_know = view.findViewById<TextView>(R.id.tv_know)
        val diss_dialog = view.findViewById<ImageView>(R.id.img_close_dialog)
        diss_dialog.visibility = View.VISIBLE
        tv_know.setOnClickListener {
            shareImg(context, musicBean)
            diaLogBuilder.setDismiss()
        }
        diss_dialog.setOnClickListener { diaLogBuilder.setDismiss() }
    }
}
