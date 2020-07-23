package com.mxkj.yuanyintang.mainui.login_regist

import com.alibaba.fastjson.JSON
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.base.bean.UserInfo
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.app.ActivityCollector
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_reg_success_recommend.*
import kotlinx.android.synthetic.main.layout_identifying_code.*
import okhttp3.Headers

class RegSuccessRecommend : StandardUiActivity() {
    var tagList = ArrayList<UserInfo.DataBean.IdentityTagBean>()
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun setLayoutId(): Int {
        return R.layout.activity_reg_success_recommend
    }

    override fun initView() {
        hideTitle(true)
    }

    override fun initData() {
        UserInfoUtil.getUserInfoById(0, this@RegSuccessRecommend) { infoBean ->
            if (infoBean?.data != null && infoBean?.data?.identity_tag != null) {
                ImageLoader.with(this@RegSuccessRecommend).url(infoBean?.data?.head_link).placeHolder(R.drawable.default_head_img).into(civ_headimg)
                tagList = infoBean?.data?.identity_tag as ArrayList<UserInfo.DataBean.IdentityTagBean>
                for (i in tagList.indices) {
                    mrb_recommendTag.insert(i, tagList[i].title)
                }
                mrb_recommendTag.setOnCheckChangedListener { _, position, checked ->
                    tagList[position].selected = when (checked) {
                        true -> 1
                        false -> 0
                    }
                }
            }
        }
    }

    override fun initEvent() {
        tvFinish.setOnClickListener {
            MobclickAgent.onEvent(this, "indi_pre")
            finish()
        }
        tvSkip.setOnClickListener {
            MobclickAgent.onEvent(this, "indi_skip")
            ActivityCollector.finishAll()
        }
        tvSure.setOnClickListener {
            MobclickAgent.onEvent(this, "indi_sure")

            var params = HttpParams()
            var tags = ArrayList<UserInfo.DataBean.IdentityTagBean>()
            tagList.forEach {
                if (it.selected == 1) {
                    tags.add(it)
                }
            }

            params.put("identity_tag", JSON.toJSONString(tags))

            NetWork.saveIdenTag(this, params, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    ActivityCollector.finishAll()
                }

                override fun doError(msg: String) {
                }

                override fun doResult() {
                }
            })

        }
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
