package com.mxkj.yuanyintang.mainui.newapp.myself

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.CheckBox
import com.alibaba.fastjson.JSON
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.base.bean.UserInfo
import com.mxkj.yuanyintang.mainui.login_regist.RegSuccessRecommend
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil
import com.mxkj.yuanyintang.net.NetWork
import kotlinx.android.synthetic.main.activity_add_musician_tag.*
import kotlinx.android.synthetic.main.activity_music_like_tag.*
import okhttp3.Headers
import java.util.ArrayList

class AddMusicianTagActivity : StandardUiActivity() {
    var identity_tag: ArrayList<UserInfo.DataBean.IdentityTagBean> = ArrayList()
    var upDateBeanList: ArrayList<UserInfo.DataBean.IdentityTagBean> = ArrayList()
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    override fun setLayoutId(): Int {
        return R.layout.activity_add_musician_tag
    }

    override fun initView() {
        setTitleText("身份标签")
        setRightButton("保存", null, View.OnClickListener {
            showLoadingView()
            upDateBeanList.clear()
            val list = etTag.ok(this)
            if (list != null && list.size > 0) {
                list.indices.forEach { i ->
                    var sBean = UserInfo.DataBean.IdentityTagBean()
                    sBean.code = "custom"
                    sBean.title = list[i]
                    Log.e("TAG", list[i])
                    sBean.id = 1
                    upDateBeanList.add(sBean)
                }
            }
            identity_tag.forEach {
                if (it.selected == 1 && it.code != "custom") {
                    upDateBeanList.add(it)
                }
            }
            val intent = Intent().putExtra("musicianTag", JSON.toJSONString(upDateBeanList))
            setResult(Activity.RESULT_OK, intent)
            var params = HttpParams()
            params.put("identity_tag", JSON.toJSONString(upDateBeanList))
            NetWork.saveIdenTag(this, params, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    hideLoadingView()
                    finish()
                }

                override fun doError(msg: String) {
                    hideLoadingView()
                }

                override fun doResult() {
                }
            })
        })
    }

    override fun initData() {
        UserInfoUtil.getUserInfoById(0, this) { infoBean ->
            infoBean?.data?.let {
                identity_tag = it.identity_tag as ArrayList<UserInfo.DataBean.IdentityTagBean>
                identity_tag?.let {
                    it.indices.forEach { i ->
                        val identityTagBean = it[i]
                        if (identityTagBean.code == "identity") {
                            selfTag.insert(selfTag.childCount, identityTagBean.title)
                            val checkBox = selfTag?.getChildAt(selfTag.childCount - 1)?.findViewById<CheckBox>(R.id.ckTag)
                            checkBox?.isChecked = identityTagBean.selected == 1
                        } else if (identityTagBean.code == "musician") {
                            musicianTag.insert(musicianTag.childCount, identityTagBean.title)
                            val checkBox = musicianTag?.getChildAt(musicianTag.childCount - 1)?.findViewById<CheckBox>(R.id.ckTag)
                            checkBox?.isChecked = identityTagBean.selected == 1
                        } else if (identityTagBean.code == "custom") {
                            etTag.append(identityTagBean.title)
                            if (etTag.isAdd) {
                                etTag.initTags(this@AddMusicianTagActivity)
                            }
                            if (etTag.listener != null) {
                                etTag.listener.onAdd(etTag.sources)
                            }
                        }
                    }
//选中事件
                    selfTag.setOnCheckChangedListener { group, position, checked ->
                        val text = group.getChildAt(position).findViewById<CheckBox>(R.id.ckTag).text
                        for (i in identity_tag.indices) {
                            if (text == it[i].title) {
                                it[i].selected = when (checked) {true -> 1
                                    false -> 0
                                }
                            }
                        }
                    }


                    musicianTag.setOnCheckChangedListener { group, position, checked ->
                        val text = group.getChildAt(position).findViewById<CheckBox>(R.id.ckTag).text
                        for (i in identity_tag.indices) {
                            if (text == it[i].title) {
                                it[i].selected = when (checked) {true -> 1
                                    false -> 0
                                }
                            }
                        }
                    }
                }


            }
        }

    }

    override fun initEvent() {
    }
}
