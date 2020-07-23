package com.mxkj.yuanyintang.extraui

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.view.View
import android.widget.ImageView

import com.mxkj.yuanyintang.R
object AgreeAnimationUtil {
    const val ALBUM_AGREE = 0
    const val COMMENT_AGREE = 1
    private var animationDrawable: AnimationDrawable? = null
    fun setAnim(context: Context, aniAgreeView: ImageView, realView: View, agreeType: Int) {
        when (agreeType) {
            0 -> aniAgreeView.setImageResource(R.drawable.ani_song_sheet_agree)
            1 -> aniAgreeView.setImageResource(R.drawable.ani_comment_agree)
        }
        realView.visibility = View.INVISIBLE
        aniAgreeView.visibility = View.VISIBLE
        animationDrawable = aniAgreeView.drawable as AnimationDrawable
        animationDrawable!!.start()
        val handler = Handler()
        handler.postDelayed({
            aniAgreeView.clearAnimation()
            realView.visibility = View.VISIBLE
            aniAgreeView.visibility = View.INVISIBLE
        }, 1500)
    }


    interface AniCallback {
        fun onStart()
        fun onEnd()
    }
}
