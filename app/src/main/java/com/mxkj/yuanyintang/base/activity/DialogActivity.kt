package com.mxkj.yuanyintang.base.activity

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.WindowManager

import com.mxkj.yuanyintang.R
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

open class DialogActivity : RxAppCompatActivity() {

    /**
     * 退出动画
     * @return 动画资源id
     */
    protected open fun exitAnim(): Int {
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置竖屏显示
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        if (Build.VERSION.SDK_INT >= 11) {
            setFinishOnTouchOutside(true)
        }
    }

    /**
     * 是否可点击空白区域消失，要求ApiLevel 11
     * @param cancel 是否取消
     * @see .setFinishOnTouchOutside
     */
    protected fun setCancelOnTouchOutside(cancel: Boolean) {
        if (Build.VERSION.SDK_INT >= 11) {
            setFinishOnTouchOutside(cancel)
        }
    }

    override fun finish() {
        super.finish()
        var exitAnim = exitAnim()
        if (exitAnim == 0)
            exitAnim = R.anim.popup_out
        //设置退出动画，xml设置的无效
        overridePendingTransition(0, exitAnim)
    }
}
