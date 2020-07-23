package com.mxkj.yuanyintang.extraui.dialog
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment
import com.mxkj.yuanyintang.extraui.AgreeAnimationUtil
@SuppressLint("ValidFragment")
class AgreeAnimationDialog constructor(internal var rel_view: View) : BaseDialogFragment() {
    private var handler: Handler? = null

    override val contentViewLayoutID: Int
        get() = R.layout.agree_ani_dialog

    override val isBack: Boolean?
        get() = false

    override fun style(): Int {
        return 0
    }

    override fun initView() {
        val location = IntArray(2)
        rel_view.getLocationOnScreen(location)
        val x = location[0]
        val y = location[1]

        val imageView = rel_view.findViewById<ImageView>(R.id.imageView)
        imageView!!.x = x.toFloat()
        imageView!!.y = y.toFloat()
        handler = Handler()
        handler!!.postDelayed({
            imageView!!.visibility = View.VISIBLE
            AgreeAnimationUtil.setAnim(activity, imageView, imageView, AgreeAnimationUtil.COMMENT_AGREE)
            handler!!.postDelayed({
                //                        dismiss();
            }, 1500)
        }, 1500)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window
        val lp = window!!.attributes
        lp.dimAmount = 0.0f
        window.attributes = lp
    }
}


