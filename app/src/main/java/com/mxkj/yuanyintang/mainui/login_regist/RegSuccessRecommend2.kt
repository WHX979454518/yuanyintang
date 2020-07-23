package com.mxkj.yuanyintang.mainui.login_regist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.mxkj.yuanyintang.widget.MultiLineRadioGroup
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_reg_success_recommend.*
import okhttp3.Headers

class RegSuccessRecommend2 : StandardUiActivity() {
    var tagList = ArrayList<UserInfo.DataBean.InterestTagBean>()

    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun setLayoutId(): Int {
        return R.layout.activity_reg_success_recommend2
    }

    override fun initView() {
        hideTitle(true)
    }

    override fun initData() {
//        /v2/api/member.tag/save_interest_tag
        UserInfoUtil.getUserInfoById(0,this@RegSuccessRecommend2) { infoBean ->
            if (infoBean?.data != null && infoBean.data?.interest_tag != null) {
                ImageLoader.with(this@RegSuccessRecommend2).url(infoBean?.data?.head_link).placeHolder(R.drawable.default_head_img).into(civ_headimg)
                tagList = infoBean?.data?.interest_tag as ArrayList<UserInfo.DataBean.InterestTagBean>
                for (i in tagList.indices) {
                    mrb_recommendTag.insert(i, tagList[i].title)
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
        tvSkip.setOnClickListener {
            MobclickAgent.onEvent(this,"intre_skip")

            finish() }
        val intent = Intent()
        tvSure.setOnClickListener {
            MobclickAgent.onEvent(this,"intre_next")

            var tags = ArrayList<UserInfo.DataBean.InterestTagBean>()
            tagList.forEach {
                if (it.selected==1){
                    tags.add(it)
                }
            }



            var b = Bundle()
            b.putString("tagJson", JSON.toJSONString(tags))
            goActivity(RegSuccessRecommend::class.java, b)
            var params = HttpParams()
            params.put("interest_tag", JSON.toJSONString(tags))
            intent.putExtra("musicianLikeTag",JSON.toJSONString(tags))
            setResult(Activity.RESULT_OK,intent)
            NetWork.saveInteTag(this, params, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {

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
