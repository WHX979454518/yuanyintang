package com.mxkj.yuanyintang.extraui.dialog
import android.annotation.SuppressLint
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment
import kotlinx.android.synthetic.main.activity_select_sex.*
import kotlinx.android.synthetic.main.include_select_view_head.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit
@SuppressLint("ValidFragment")
class SelectSexDialog : BaseDialogFragment() {

    override val contentViewLayoutID: Int
        get() = R.layout.activity_select_sex

    override val isBack: Boolean?
        get() = false

    private var myDialogFragmentListener: MyDialogFragmentListener? = null

    override fun style(): Int {
        return 0
    }

    override fun initView() {
        val stringList = ArrayList<String>()
        stringList.add("男")
        stringList.add("女")
        type_loopview!!.setItems(stringList)
        type_loopview!!.setNotLoop()
        type_loopview!!.setItemsVisibleCount(7)
        type_loopview!!.setTextSize(15f)
        RxView.clicks(tv_confirm!!)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    if (stringList.size > type_loopview!!.selectedItem && type_loopview!!.selectedItem != -1) {
                        dismiss()
                        if (null != myDialogFragmentListener) {
                            myDialogFragmentListener!!.getDataFrom_DialogFragment(stringList[type_loopview!!.selectedItem])
                        }
                    }
                }
        RxView.clicks(iv_left!!)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { dismiss() }
        RxView.clicks(iv_right!!)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { dismiss() }

    }

    interface MyDialogFragmentListener {
        fun getDataFrom_DialogFragment(sex: String)
    }

    fun setMyDialogFragmentListener(myDialogFragmentListener: MyDialogFragmentListener) {
        this.myDialogFragmentListener = myDialogFragmentListener
    }
}
