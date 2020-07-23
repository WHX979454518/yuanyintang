package com.mxkj.yuanyintang.utils.tab


import android.app.FragmentTransaction
import android.graphics.Color

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.HomeActivity


class FragmentSwitchTool : View.OnClickListener {

    private var fragmentManager: FragmentManager? = null
    private var currentFragment: Fragment? = null

    private var containerId: Int = 0

    private var selectMap: MutableMap<String, TabViewMap>? = null // 传入一个集合  Any 包含  LinearLayout  TextView ImageView  Fragment
    private var currSelectMap: TabViewMap? = null
    private var key: String = "home"
    private var homeActivity: HomeActivity? = null

    constructor(fragmentManager: FragmentManager, containerId: Int, homeActivity: HomeActivity) {
        this.fragmentManager = fragmentManager
        this.containerId = containerId
        this.homeActivity = homeActivity
    }

    /**
     * 添加一组按钮到集合
     * @param key
     * @param value  包含 view->LinearLayout  text->TextView  img->ImageView  frg->Fragment
     */
    fun setSelectMap(key: String, tabViewMap: TabViewMap): FragmentSwitchTool {
        if (this.selectMap == null) {
            this.selectMap = mutableMapOf()
        }
        this.selectMap?.put(key, tabViewMap)
        tabViewMap.view.setOnClickListener(this) //设置点击事件
        return this
    }

    fun changeSelectd(v: View) {
        val fragmentTransaction = fragmentManager?.beginTransaction()
        var fragment: Fragment? = fragmentManager?.findFragmentByTag(v.id.toString()) //获取当前 Fragment 是否已经加载
        for ((key, item) in selectMap!!) {
            if (item.view.id != v.id) {
                continue
            }
            val fragments = fragmentManager?.fragments
            if (fragments != null) {
                for (frg in fragments) {
                    if (!frg.isHidden) {
                        fragmentTransaction?.hide(frg)
                    }
                }
            }
            if (fragment == null) {
                fragment = item.fragment?.newInstance()
                fragmentTransaction?.add(containerId, fragment, v.id.toString())?.commit()
            } else if (fragment == currentFragment) {
                return
            } else {
                fragmentTransaction?.hide(currentFragment)?.show(fragment)?.commit()
            }
            if (currSelectMap != null) {
                currSelectMap?.imageView?.setImageLevel(0)
                currSelectMap?.textView?.setTextColor(Color.parseColor("#ff96d7"))
            }

            currentFragment = fragment //必须采用fragmentManager获取到的fragment 否隐藏会失败~
            currSelectMap = item
            this.key = key

            when (v.id) {
                R.id.line_home_home, R.id.line_home_topic, R.id.line_home_message, R.id.line_home_mine -> {
                    item.imageView?.setImageLevel(1)
                    item.textView?.setTextColor(Color.parseColor("#ff6699"))
                }
            }
            break
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.line_home_message -> {
                if (homeActivity?.isLogin()!!) {
                    changeSelectd(v!!)
                }
            }
            else -> {
                changeSelectd(v!!)
            }
        }

    }

    inner class TabViewMap(view: View, imageView: ImageView?, textView: TextView?, fragment: Class<out Fragment>) {
        val view: View = view
        val imageView: ImageView? = imageView
        val textView: TextView? = textView
        val fragment: Class<out Fragment>? = fragment
    }

}