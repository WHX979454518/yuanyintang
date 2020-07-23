package com.mxkj.yuanyintang.mainui.newapp.pond

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.CheckBox
import com.alibaba.fastjson.JSON

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.dynamic.bean.RecommendUser
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew
import com.mxkj.yuanyintang.mainui.pond.bean.PondDetialBean
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import okhttp3.Headers

class RecUserAdapter(data: List<RecommendUser.DataBean>) : BaseMultiItemQuickAdapter<RecommendUser.DataBean, BaseViewHolder>(data) {
    init {
        addItemType(0, R.layout.item_rec_pond)
    }
    override fun convert(helper: BaseViewHolder, dataBean: RecommendUser.DataBean) {
        if (dataBean.nickname != null) {
            helper.setText(R.id.nickname, dataBean.nickname)
        }
        helper.setText(R.id.tv_fans_num, "粉丝:"+dataBean.fans_num.toString())
        if (dataBean.head_link != null) {
            ImageLoader.with(mContext)
                    .url(dataBean.head_link)
                    .into(helper.getView(R.id.civ_head))
        }
        val ck_follow = helper.getView<CheckBox>(R.id.ck_follow)
        ck_follow?.setOnCheckedChangeListener { _, isChecked ->
            if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN)) {
                if (isChecked) {
                    ck_follow?.text = "已关注"
                } else {
                    ck_follow?.text = "+关注"
                }
                val params = HttpParams()
                params.put("id", dataBean.id.toString())
                NetWork.follow(mContext, params, object : NetWork.TokenCallBack {
                    override fun doNext(resultData: String, headers: Headers?) {
                        val jObj = JSON.parseObject(resultData)
                        val code = jObj.getInteger("code")
                        if (code == 200) {
                            if (MediaService.bean?.is_relation == 0) {
                                ck_follow?.text = "已关注"
                                MediaService.bean?.is_relation = 1
                            } else if (MediaService.bean?.is_relation == 1) {
                                ck_follow?.text = "+关注"
                                MediaService.bean?.is_relation = 0
                            }
                        }
                    }

                    override fun doError(msg: String) {
                    }

                    override fun doResult() {

                    }
                })
            } else {
                ck_follow.isChecked = false
                mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
            }
        }
    }
}
