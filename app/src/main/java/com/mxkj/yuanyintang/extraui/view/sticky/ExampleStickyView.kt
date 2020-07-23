package com.mxkj.yuanyintang.extraui.view.sticky

import android.view.View

class ExampleStickyView : StickyView {
    override val stickViewType: Int
        get() = 11

    override fun isStickyView(view: View): Boolean {
        return view.tag as Boolean
    }
}
