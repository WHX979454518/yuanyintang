package com.mxkj.yuanyintang.mainui.myself.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.mainui.myself.bean.MyPondBean
import kotlinx.android.synthetic.main.activity_edit_intro.*

class EditIntroActivity : StandardUiActivity() {
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    override fun setLayoutId(): Int {
        return R.layout.activity_edit_intro
    }

    override fun initView() {
        setTitleText("个人简介")
        setRightButton("保存",null, View.OnClickListener {
            val intent = Intent()
            intent.putExtra("musicianIntro",etIntro.text.toString())
            setResult(Activity.RESULT_OK,intent)
            finish()
        })

        val intent = intent
        val bundle = intent.extras
        if(null != bundle.getString("introduce")){
            var introduce = bundle.getString("introduce") as String
            etIntro.setText(introduce)
        }

    }

    override fun initData() {
    }

    override fun initEvent() {
    }
}
