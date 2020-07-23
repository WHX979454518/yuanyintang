package com.mxkj.yuanyintang.base.dialog

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.trello.rxlifecycle2.components.support.RxDialogFragment
import butterknife.ButterKnife
import butterknife.Unbinder
import android.app.Activity
import com.mxkj.yuanyintang.base.bean.UserInfo


abstract class BaseDialogFragment : RxDialogFragment() {
    protected abstract val contentViewLayoutID: Int
    protected abstract val isBack: Boolean?
    //在其他继承了此类的dialog里面直接用这个作为上下文使用，不要用getActivity方法，否则容易空指针
    open var mActivity: Activity? = null
    internal var view: View? = null
    private var unbinder: Unbinder? = null
    protected var userInfo: UserInfo? = null
        get() {
            userInfo = JSON.parseObject(CacheUtils.getString(MainApplication.application, Constants.User.USER_JSON, ""), UserInfo::class.java)
            return if (null == userInfo) {
                null
            } else userInfo
        }

    protected abstract fun style(): Int
    protected abstract fun initView()
    @Override

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivity = activity
    }

    override fun show(manager: FragmentManager, tag: String) {
        val ft = manager.beginTransaction()
        if (this.isAdded) {
            ft.remove(this)
        }
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    override fun show(transaction: FragmentTransaction, tag: String): Int {
        transaction.add(this, tag)
        transaction.commitAllowingStateLoss()
        return 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, if (style() == 0) R.style.Theme_Dialog_Bottom else style())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (view == null) {
            val mInflater = LayoutInflater.from(activity)
            view = mInflater.inflate(contentViewLayoutID, null, false)
            view!!.setOnClickListener {
                if (!isDetached) {
                    dismiss()
                }
            }
            unbinder = ButterKnife.bind(this, view!!)
        }
        Log.d(TAG, "onCreateView: ")
        return view
    }

    override fun onActivityCreated(bundle: Bundle?) {
        super.onActivityCreated(bundle)
        val window = this.dialog.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                isBack!!
            } else false
        }
        Log.d(TAG, "onActivityCreated: ")
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (null != unbinder) {
            unbinder!!.unbind()
        }
    }

    /**
     * 跳转页面带参数
     *
     * @param mClass 目标页面
     * @param bundle 参数
     */
    @JvmOverloads
    protected fun goActivity(mClass: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(activity, mClass)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    protected fun setSnackBar(msg: String, action: String, iconId: Int) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).setSnackBar(msg, action, iconId)
        }
    }

    companion object {

        private val TAG = "BaseDialogFragment"
    }
}
