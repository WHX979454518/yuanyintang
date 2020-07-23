package com.mxkj.yuanyintang.mainui.myself.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import com.alibaba.fastjson.JSON
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.base.bean.UserInfo
import com.mxkj.yuanyintang.mainui.login_regist.RegSuccessRecommend
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.app.ActivityCollector
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import okhttp3.Headers
import kotlinx.android.synthetic.main.activity_music_like_tag.*

class MusicLikeTagActivity : StandardUiActivity() {
    var tagList = ArrayList<UserInfo.DataBean.InterestTagBean>()
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    override fun setLayoutId(): Int {
        return R.layout.activity_music_like_tag
    }

    override fun initView() {
        hideTitle(true)
        StatusBarUtil.baseTransparentStatusBar(this)
    }

    override fun initData() {
        UserInfoUtil.getUserInfoById(0, this@MusicLikeTagActivity) { infoBean ->
            if (infoBean?.data != null && infoBean?.data?.interest_tag != null) {
                ImageLoader.with(this@MusicLikeTagActivity).url(infoBean?.data?.head_link).placeHolder(R.drawable.default_head_img).into(civ_headimg)
                tagList = infoBean?.data?.interest_tag as ArrayList<UserInfo.DataBean.InterestTagBean>
                for (i in tagList.indices) {
                    mrb_recommendTag.insert(i, tagList[i].title)
                    val checkBox = mrb_recommendTag.getChildAt(i).findViewById<CheckBox>(R.id.ckTag)
                    checkBox.isChecked = tagList[i].selected == 1
                }
                mrb_recommendTag.setOnCheckChangedListener { group, position, checked ->
                    tagList[position].selected = when (checked) {
                        true -> 1
                        false -> 0
                    }
                }


            }
        }
    }

    override fun initEvent() {
        tvSkip.setOnClickListener { finish() }
        val intent = Intent()
        tvSure.setOnClickListener {
            showLoadingView()
            var b = Bundle()


            var tags = ArrayList<UserInfo.DataBean.InterestTagBean>()
            tagList.forEach {
                if (it.selected == 1) {
                    tags.add(it)
                }
            }

            b.putString("tagJson", JSON.toJSONString(tags))
            intent.putExtra("musicianLikeTag", JSON.toJSONString(tags))
            setResult(Activity.RESULT_OK, intent)
            var params = HttpParams()
            params.put("interest_tag", JSON.toJSONString(tags))
            NetWork.saveInteTag(this, params, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    hideLoadingView()
                    if (intent.getBooleanExtra("edit", false)) {
                        goActivity(RegSuccessRecommend::class.java, b)
                    } else {
                        finish()
                    }
                }
                override fun doError(msg: String) {
                    hideLoadingView()
                }

                override fun doResult() {
                }
            })
        }
        tvSkip.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        ActivityCollector.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)

    }
}
