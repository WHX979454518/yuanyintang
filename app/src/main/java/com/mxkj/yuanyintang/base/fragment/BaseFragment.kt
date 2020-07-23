package com.mxkj.yuanyintang.base.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.base.bean.UserInfo
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.trello.rxlifecycle2.components.support.RxFragment

abstract class BaseFragment : RxFragment() {
    abstract val layoutId: Int
    lateinit var rootView: View
    private var context: Context? = null
    protected var userInfo: UserInfo? = null
        get() {
            var userInfo: UserInfo? = JSON.parseObject(CacheUtils.getString(activity, Constants.User.USER_JSON, ""), UserInfo::class.java)
            if (null == userInfo) {
                goActivity(LoginRegMainPage::class.java)
            }
            return userInfo
        }

    override fun getContext(): Context? {
        return context
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        context = activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(layoutId, container, false)
        rootView.parent?.let {
            val parent = rootView.parent as ViewGroup
            parent?.removeView(rootView)
        }
        return rootView
    }

    /**
     * 查找	View
     *
     * @param paramInt
     * @return
     */
    fun <T : View> findViewById(paramInt: Int): T {

        return view!!.findViewById<View>(paramInt) as T
    }

    /**
     * 跳转页面带参数
     *
     * @param mClass 目标页面
     * @param bundle 参数
     */
    @JvmOverloads
    protected fun goActivity(mClass: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(context, mClass)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    protected fun goActivity(mClass: Class<*>, isLogin: Boolean?) {
        if (isLogin!!) {
            if (CacheUtils.getBoolean(context, Constants.User.IS_LOGIN)) {
                goActivity(mClass, null, isLogin)
            } else {
                goActivity(LoginRegMainPage::class.java, null, isLogin)
            }
        } else {
            goActivity(mClass, null, isLogin)
        }
    }

    protected fun goActivity(mClass: Class<*>, bundle: Bundle?, isLogin: Boolean?) {
        val intent = Intent(context, mClass)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        context?.startActivity(intent)
    }

    /**
     * 带页面回调跳转，不带参数
     *
     * @param mClass      目标页面
     * @param requestCode 请求code
     */
    protected fun goActivityForResult(mClass: Class<*>, requestCode: Int) {
        goActivityForResult(mClass, null, requestCode)
    }

    /**
     * 带页面回调跳转，带参数
     *
     * @param mClass      目标页面
     * @param bundle      参数
     * @param requestCode 请求码
     */
    protected fun goActivityForResult(mClass: Class<*>, bundle: Bundle?, requestCode: Int) {
        val intent = Intent(activity, mClass)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }
}
/**
 * 跳转页面
 *
 * @param mClass 目标页面
 */
