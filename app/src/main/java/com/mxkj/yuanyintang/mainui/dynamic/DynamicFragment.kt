package com.mxkj.yuanyintang.mainui.dynamic

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.dynamic.fragment.ObservalFragment
import com.mxkj.yuanyintang.mainui.dynamic.fragment.WorldFragment
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import com.trello.rxlifecycle2.components.support.RxFragment
import java.util.ArrayList
import kotlinx.android.synthetic.main.fragment_dynamic.view.*

@SuppressLint("ValidFragment")
class DynamicFragment : RxFragment() {
    private lateinit var rootView: View
    private var fragmentList = ArrayList<Fragment>()
    private var titles = arrayOfNulls<String>(2)
    private var statusBarView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_dynamic, container!!, false)
        initEvent()
        if (rootView.parent != null) {
            val parent = rootView.parent as ViewGroup
            parent.removeView(rootView)
        }
        return rootView
    }

    private fun initEvent() {
        StatusBarUtil.baseTransparentStatusBar(activity)
        statusBarView = rootView.v_statusbar
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StatusBarUtil.getStatusBarHeight(activity))
        statusBarView!!.layoutParams = params
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            statusBarView!!.visibility = View.GONE
        }
        titles[0] = "世界"
        titles[1] = "观测者"
        fragmentList.add(WorldFragment())
        fragmentList.add(ObservalFragment())
        rootView.tab_dynamic.setViewPager(rootView.vp_dynamic, titles!!, activity!!, fragmentList)
    }

}
