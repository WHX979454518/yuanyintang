package com.mxkj.yuanyintang.extraui.view.sticky

import android.view.View

/**
 *
 * 获取吸附View相关的信息
 */

interface StickyView {

    /**
     * 得到吸附view的itemType
     * @return
     */
    val stickViewType: Int

    /**
     * 是否是吸附view
     * @param view
     * @return
     */
    fun isStickyView(view: View): Boolean
}
