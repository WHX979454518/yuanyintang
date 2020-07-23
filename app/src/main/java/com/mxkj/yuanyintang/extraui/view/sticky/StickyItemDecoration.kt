package com.mxkj.yuanyintang.extraui.view.sticky

import android.graphics.Canvas
import android.graphics.Paint
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

import java.util.ArrayList

class StickyItemDecoration : RecyclerView.ItemDecoration() {

    /**
     * 吸附的itemView
     */
    private var mStickyItemView: View? = null

    /**
     * 吸附itemView 距离顶部
     */
    private var mStickyItemViewMarginTop: Int = 0

    /**
     * 吸附itemView 高度
     */
    private var mStickyItemViewHeight: Int = 0

    /**
     * 通过它获取到需要吸附view的相关信息
     */
    private val mStickyView: StickyView

    /**
     * 滚动过程中当前的UI是否可以找到吸附的view
     */
    private var mCurrentUIFindStickView: Boolean = false

    /**
     * adapter
     */
    private var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null

    /**
     * viewHolder
     */
    private var mViewHolder: RecyclerView.ViewHolder? = null

    /**
     * position list
     */
    private val mStickyPositionList = ArrayList<Int>()

    /**
     * layout manager
     */
    private var mLayoutManager: LinearLayoutManager? = null

    /**
     * 绑定数据的position
     */
    private var mBindDataPosition = -1

    /**
     * paint
     */
    private var mPaint: Paint? = null

    init {
        mStickyView = ExampleStickyView()
        initPaint()
    }

    /**
     * init paint
     */
    private fun initPaint() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
    }


    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)

        if (parent.adapter.itemCount <= 0) return

        mLayoutManager = parent.layoutManager as LinearLayoutManager
        mCurrentUIFindStickView = false

        var m = 0
        val size = parent.childCount
        while (m < size) {
            val view = parent.getChildAt(m)

            /**
             * 如果是吸附的view
             */
            if (mStickyView.isStickyView(view)) {
                mCurrentUIFindStickView = true
                getStickyViewHolder(parent)
                cacheStickyViewPosition(m)

                if (view.top <= 0) {
                    bindDataForStickyView(mLayoutManager!!.findFirstVisibleItemPosition(), parent.measuredWidth)
                } else {
                    if (mStickyPositionList.size > 0) {
                        if (mStickyPositionList.size == 1) {
                            bindDataForStickyView(mStickyPositionList[0], parent.measuredWidth)
                        } else {
                            val currentPosition = getStickyViewPositionOfRecyclerView(m)
                            val indexOfCurrentPosition = mStickyPositionList.lastIndexOf(currentPosition)
                            bindDataForStickyView(mStickyPositionList[indexOfCurrentPosition - 1], parent.measuredWidth)
                        }
                    }
                }

                if (view.top > 0 && view.top <= mStickyItemViewHeight) {
                    mStickyItemViewMarginTop = mStickyItemViewHeight - view.top
                } else {
                    mStickyItemViewMarginTop = 0

                    val nextStickyView = getNextStickyView(parent)
                    if (nextStickyView != null && nextStickyView.top <= mStickyItemViewHeight) {
                        mStickyItemViewMarginTop = mStickyItemViewHeight - nextStickyView.top
                    }
                }

                drawStickyItemView(c)
                break
            }
            m++
        }

        if (!mCurrentUIFindStickView) {
            mStickyItemViewMarginTop = 0
            if (mLayoutManager!!.findFirstVisibleItemPosition() + parent.childCount == parent.adapter.itemCount && mStickyPositionList.size > 0) {
                bindDataForStickyView(mStickyPositionList[mStickyPositionList.size - 1], parent.measuredWidth)
            }
            drawStickyItemView(c)
        }
    }

    /**
     * 得到下一个吸附View
     * @param parent
     * @return
     */
    private fun getNextStickyView(parent: RecyclerView): View? {
        var num = 0
        var nextStickyView: View? = null
        var m = 0
        val size = parent.childCount
        while (m < size) {
            val view = parent.getChildAt(m)
            if (mStickyView.isStickyView(view)) {
                nextStickyView = view
                num++
            }
            if (num == 2) break
            m++
        }
        return nextStickyView
    }

    /**
     * 给StickyView绑定数据
     * @param position
     */
    private fun bindDataForStickyView(position: Int, width: Int) {
        if (mBindDataPosition == position || mViewHolder == null) return

        mBindDataPosition = position
        mAdapter!!.onBindViewHolder(mViewHolder, mBindDataPosition)
        measureLayoutStickyItemView(width)
        mStickyItemViewHeight = mViewHolder!!.itemView.bottom - mViewHolder!!.itemView.top
    }

    /**
     * 缓存吸附的view position
     * @param m
     */
    private fun cacheStickyViewPosition(m: Int) {
        val position = getStickyViewPositionOfRecyclerView(m)
        if (!mStickyPositionList.contains(position)) {
            mStickyPositionList.add(position)
        }
    }

    /**
     * 得到吸附view在RecyclerView中 的position
     * @param m
     * @return
     */
    private fun getStickyViewPositionOfRecyclerView(m: Int): Int {
        return mLayoutManager!!.findFirstVisibleItemPosition() + m
    }

    /**
     * 得到吸附viewHolder
     * @param recyclerView
     */
    private fun getStickyViewHolder(recyclerView: RecyclerView) {
        if (mAdapter != null) return

        mAdapter = recyclerView.adapter
        mViewHolder = mAdapter!!.onCreateViewHolder(recyclerView, mStickyView.stickViewType)
        mStickyItemView = mViewHolder!!.itemView
    }

    /**
     * 计算布局吸附的itemView
     * @param parentWidth
     */
    private fun measureLayoutStickyItemView(parentWidth: Int) {
        if (mStickyItemView == null || !mStickyItemView!!.isLayoutRequested) return

        val widthSpec = View.MeasureSpec.makeMeasureSpec(parentWidth, View.MeasureSpec.EXACTLY)
        val heightSpec: Int

        val layoutParams = mStickyItemView!!.layoutParams
        if (layoutParams != null && layoutParams.height > 0) {
            heightSpec = View.MeasureSpec.makeMeasureSpec(layoutParams.height, View.MeasureSpec.EXACTLY)
        } else {
            heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        }

        mStickyItemView!!.measure(widthSpec, heightSpec)
        mStickyItemView!!.layout(0, 0, mStickyItemView!!.measuredWidth, mStickyItemView!!.measuredHeight)
    }

    /**
     * 绘制吸附的itemView
     * @param canvas
     */
    private fun drawStickyItemView(canvas: Canvas) {
        if (mStickyItemView == null) return

        val saveCount = canvas.save()
        canvas.translate(0f, (-mStickyItemViewMarginTop).toFloat())
        mStickyItemView!!.draw(canvas)
        canvas.restoreToCount(saveCount)
    }
}
