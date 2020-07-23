package com.mxkj.yuanyintang.mainui.comment

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver

import java.util.LinkedList

class SoftKeyboardStateHelper @JvmOverloads constructor(private val activityRootView: View, var isSoftKeyboardOpened: Boolean = false) : ViewTreeObserver.OnGlobalLayoutListener {

    private val listeners = LinkedList<SoftKeyboardStateListener>()
    var lastSoftKeyboardHeightInPx: Int = 0
        private set

    interface SoftKeyboardStateListener {
        fun onSoftKeyboardOpened(keyboardHeightInPx: Int)
        fun onSoftKeyboardClosed()
    }

    init {
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        val r = Rect()
        activityRootView.getWindowVisibleDisplayFrame(r)
        val heightDiff = activityRootView.rootView.height - (r.bottom - r.top)
        if (!isSoftKeyboardOpened && heightDiff > 100) {
            isSoftKeyboardOpened = true
            notifyOnSoftKeyboardOpened(heightDiff)
        } else if (isSoftKeyboardOpened && heightDiff < 100) {
            isSoftKeyboardOpened = false
            notifyOnSoftKeyboardClosed()
        }
    }

    fun addSoftKeyboardStateListener(listener: SoftKeyboardStateListener) {
        listeners.add(listener)
    }

    private fun notifyOnSoftKeyboardOpened(keyboardHeightInPx: Int) {
        this.lastSoftKeyboardHeightInPx = keyboardHeightInPx

        for (listener in listeners) {
            listener?.onSoftKeyboardOpened(keyboardHeightInPx)
        }
    }

    private fun notifyOnSoftKeyboardClosed() {
        for (listener in listeners) {
            listener?.onSoftKeyboardClosed()
        }
    }
}