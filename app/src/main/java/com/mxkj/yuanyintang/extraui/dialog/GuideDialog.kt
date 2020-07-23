package com.mxkj.yuanyintang.extraui.dialog

import android.annotation.SuppressLint
import android.util.Log
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_guide.*
/**
 *
 * 这个是旧版本的引导页，估计用不到了
 *
 * */
@SuppressLint("ValidFragment")
class GuideDialog : BaseDialogFragment {
    internal var num = 0
    private lateinit var activityName: String //首页引导
    override val contentViewLayoutID: Int
        get() = R.layout.dialog_guide

    override val isBack: Boolean?
        get() = false

    constructor()

    constructor(activityName: String) {
        this.activityName = activityName
    }

    override fun style(): Int {
        return R.style.Theme_Dialog_Default
    }

    override fun initView() {
        try {
            when (activityName) {
//                "home" -> iv_guide_img!!.setImageResource(R.drawable.img_home_guide)
//                "playing" -> iv_guide_img!!.setImageResource(R.drawable.img_play2_guide)
//                "mySelf" -> iv_guide_img!!.setImageResource(R.drawable.img_mine_guide)
            }
            iv_guide_img!!.setOnClickListener {
                when (activityName) {
//                    "home" -> if (activity is HomeActivity) {
//                        when (num) {
//                            0 -> {
//                                iv_guide_img!!.setImageResource(R.drawable.img_play_guide)
//                                num++
//                            }
//                            1 -> dismiss()
//                        }
//                    }
//                    "playing" -> dismiss()
//                    "mySelf" -> dismiss()
                }
            }
        } catch (e: RuntimeException) {
        }
    }

    companion object {
        private const val TAG = "GuideDialog"
    }


}
