package com.mxkj.yuanyintang.base.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.ListView

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.widget.NavigationBar

abstract class TitleBarActivity : BaseActivity() {
    protected lateinit var navigationBar: NavigationBar
        private set
    private var rootView: LinearLayout? = null
    private var line: View? = null
    private var titleHeight: Int = 0
    abstract fun setContentLayout(): View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_bar)
        init()
    }

    private fun init() {
        rootView = findViewById(R.id.rootView) as LinearLayout
        navigationBar = findViewById(R.id.navigationBar) as NavigationBar
        rootView = findViewById(R.id.rootView) as LinearLayout
        line = findViewById(R.id.line)

        val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        navigationBar!!.measure(w, h)
        titleHeight = navigationBar!!.measuredHeight
        navigationBar!!.init()
        val v = setContentLayout()
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        layoutParams.weight = 1f
        rootView!!.addView(v, 2, layoutParams)
        showTitleBarLine(true)
    }

    protected fun addView(view: View) {
        rootView!!.addView(view)
    }

    protected fun addView(position: Int, view: View) {
        rootView!!.addView(view, position)
    }

    protected fun addView(view: View, params: ViewGroup.LayoutParams) {
        rootView!!.addView(view, params)
    }

    protected fun addView(position: Int, view: View, params: ViewGroup.LayoutParams) {
        rootView!!.addView(view, position, params)
    }

    protected fun toggleTip(show: Boolean) {

        findViewById<ListView>(R.id.tv_tip).visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * 是否隐藏标题
     *
     * @param hide
     */
    protected fun hideTitle(hide: Boolean) {
        navigationBar!!.visibility = if (!hide) View.VISIBLE else View.GONE
        line!!.visibility = if (!hide) View.VISIBLE else View.GONE
    }

    /**
     * 设置标题栏背景颜色
     *
     * @param color
     */
    fun setTitleBarBackgroundColor(color: Int) {
        navigationBar!!.setBackgroundColor(color)
    }

    /**
     * 设置标题文字背景颜色
     *
     * @param color
     */
    protected fun setTitleTextColor(color: Int) {
        navigationBar!!.setTitleColor(color)
    }

    /**
     * 设置标题文字
     *
     * @param titleText
     */
    protected fun setTitleText(titleText: String) {
        navigationBar!!.setTitle(titleText)
    }

    /**
     * 不显示左边按钮
     */
    protected fun hideLeftButton() {
        navigationBar!!.hideLeftButton()
    }
    /**
     * 显示左边按钮
     */
    protected fun showLeftButton() {
        navigationBar!!.showLeftButton()
    }
    /**
     * 不显示右边按钮
     */
    protected fun hideRightButton() {
        navigationBar!!.hideRightButton()
    }

    /**
     * 显示右边按钮
     */
    protected fun showRightButton() {
        navigationBar!!.showRightButton()
    }

    /**
     * 是否显示标题栏下一根线
     *
     * @param show
     */
    fun showTitleBarLine(show: Boolean) {
        line!!.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * 设置右边按钮是否可用
     *
     * @param enabled
     */
    protected fun enabledRightButton(enabled: Boolean) {
        navigationBar!!.enableRightButton(enabled)
    }

    /**
     * 左边按钮事件
     *
     * @param listener
     */
    protected fun setOnLeftClick(listener: View.OnClickListener) {
        navigationBar!!.setOnLeftClick(listener)
    }

    /**
     * 左边按钮事件
     *
     * @param listener
     */
    protected fun setOnRightClick(listener: View.OnClickListener) {
        navigationBar!!.setOnRightClick(listener)
    }

    /**
     * 左边按钮
     *
     * @param text     文字
     * @param left     图标
     * @param listener onclick事件回调
     */
    protected fun setLeftButton(text: String, left: Drawable?, listener: View.OnClickListener) {
        navigationBar!!.setLeftButton(text, listener, left)
    }

    protected fun setLeftButton(text: String, color:Int,left: Drawable?, listener: View.OnClickListener) {
        navigationBar!!.setLeftButton(text,color,listener, left)
    }

    /**
     * 设置左边边按钮
     *
     * @param left
     */
    protected fun setLeftButtonImageView(left: Drawable?) {
        navigationBar.setLeftButtonDrawable(left)
    }

    /**
     * 设置左按钮事件图片
     *
     * @param left            图片
     * @param onClickListener onclick事件回调
     */
    protected fun setLeftDrawable(left: Drawable?, onClickListener: View.OnClickListener) {
        navigationBar!!.setLeftButtonDrawable(left)
        navigationBar!!.setOnLeftClick(onClickListener)
    }

    /**
     * 设置右边按钮
     *
     * @param text
     * @param right
     * @param listener
     */
    protected fun setRightButton(text: String, right: Drawable?, listener: View.OnClickListener?) {
        navigationBar!!.setRightButton(text, null, right, null, null, listener)
    }

    /**
     * 设置右边按钮的图标（）
     *
     * @param right
     */
    protected fun setRightButtonImageView(right: Drawable?) {
        navigationBar!!.setRightButtonImageView(right)
    }

    /**
     * 设置右边按钮文字
     */
    protected fun setRightButtonText(text: String?) {
        navigationBar!!.setRightButtonText(text)
    }

    /**
     * 设置左边点击状态
     */
    protected fun setLeftButtonClickable(i: Boolean) {
        navigationBar!!.isClickable = i
    }

    protected fun setRightButtonClickable(i: Boolean) {
        navigationBar!!.isClickable = i
    }
}
