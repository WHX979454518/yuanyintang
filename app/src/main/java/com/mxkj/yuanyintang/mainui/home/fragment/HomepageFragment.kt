package com.mxkj.yuanyintang.mainui.home.fragment

import android.support.design.widget.AppBarLayout
import android.text.TextUtils
import android.util.Log
import android.view.View.GONE
import com.alibaba.fastjson.JSON

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.bean.UserInfo
import com.mxkj.yuanyintang.base.fragment.BaseLazyFragment
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.bean.LikeMusicDetailsBean
import com.mxkj.yuanyintang.mainui.home.bean.PersonalHomePageBean
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.fragment_musician_homepage.view.*
import okhttp3.Headers


class HomepageFragment : BaseLazyFragment(), AppBarLayout.OnOffsetChangedListener {
    internal var id: String? = null
    override val contentViewLayoutID: Int
        get() = R.layout.fragment_musician_homepage

    internal var verticalOffset: Int = 0
    fun setData(dataBean: UserInfo.DataBean?) {
        if (rootView == null) return
        if (null != dataBean) {
            rootView.tv_synopsis.text = StringUtils.isEmpty(dataBean.signature)
            if (dataBean.sex == 1) {
                rootView.tv_sex.text = "男"
            } else {
                rootView.tv_sex.text = "女"
            }
            rootView.tv_address.text =  StringUtils.isEmpty(dataBean.province_text) + "  " + StringUtils.isEmpty(dataBean.city_text)
            rootView.tv_age.text = dataBean.age.toString()
            val interest_tag = dataBean.interest_tag
            val identity_tag = dataBean.identity_tag
            when {
                interest_tag != null && interest_tag.size > 0 -> {
                    rootView.likeTag.removeAllViews()
                    interest_tag.indices.forEach {
                        rootView.likeTag.insert(it, interest_tag[it].title)
                    }
                }
                else -> {
                    rootView.tl.visibility = GONE
                    rootView.tv_d1.visibility = GONE
                    rootView.likeTag.visibility = GONE
                }
            }
            when {
                identity_tag != null && identity_tag.size > 0 -> {
                    rootView.musicianTag.removeAllViews()
                    identity_tag.indices.forEach {
                        rootView.musicianTag.insert(it, identity_tag[it].title)
                    }
                }
                else -> {
                    rootView.ht.visibility = GONE
                    rootView.tv_d2.visibility = GONE
                    rootView.musicianTag.visibility = GONE
                }
            }
        }
    }

    override fun onFirstVisibleToUser() {
        val bundle = arguments
        id = bundle.getString("id")
        initData()
        initEvent()
        addListener()
        getInfo()
    }

    private fun initEvent() {}

    private fun initData() {
        if (TextUtils.isEmpty(id)) {
            return
        }
    }

    override fun onVisibleToUser() {
        addListener()
    }

    override fun onInvisibleToUser() {
        removeListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart("musician_home")

    }

    override fun onDestroy() {
        super.onDestroy()
        MobclickAgent.onPageEnd("musician_home");

    }
    override fun onPause() {
        super.onPause()
    }

    private fun addListener() {
        val activity = activity as MusicIanDetailsActivity
        if (activity != null && !activity.isFinishing && activity is MusicIanDetailsActivity) {
            activity.addListener(this)
        }
    }

    private fun removeListener() {
        val activity = activity as MusicIanDetailsActivity
        if (activity != null && !activity.isFinishing && activity is MusicIanDetailsActivity) {
            activity.removeListener(this)
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        this.verticalOffset = verticalOffset
    }

    private fun getInfo() {
        if (TextUtils.isEmpty(id)) {
            return
        }
        NetWork.getMusicIanInfo(activity, this!!.id!!, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val personalHomePageBean = JSON.parseObject(resultData, PersonalHomePageBean::class.java)
                rootView.tv_xingzuo.text = personalHomePageBean.data.constellation
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }
}
