package com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import kotlinx.android.synthetic.main.activity_add_lyric.*

class AddLyricActivity : StandardUiActivity() {
    private var mInputManager: InputMethodManager? = null//软键盘管理类

    override val isVisibilityBottomPlayer: Boolean
        get() = false

    override fun setLayoutId(): Int {
        return R.layout.activity_add_lyric
    }

    override fun initView() {
        mInputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        var lyricStr = intent.getStringExtra("lyric")
        if (!TextUtils.isEmpty(lyricStr)) {
            etLyric.setText(lyricStr)
        }
        setTitleText("添加歌词")
        setRightButton("保存", null, View.OnClickListener {
            mInputManager?.hideSoftInputFromWindow(etLyric.windowToken, 0)
            if (!TextUtils.isEmpty(etLyric.text)) {
                val intent = Intent().putExtra("lyric", etLyric.text.toString())
                setResult(Activity.RESULT_OK, intent)
                finish()

            }
        })
    }

    override fun initData() {

    }

    override fun initEvent() {
    }

    override fun onPause() {
        super.onPause()


    }

    object Const {
        const val ADDLYRIC = 110
    }

}
