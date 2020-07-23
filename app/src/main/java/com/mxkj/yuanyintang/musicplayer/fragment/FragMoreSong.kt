package com.mxkj.yuanyintang.musicplayer.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.fragment.BaseFragment
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.bean.MusicIanMusicBean
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil
import com.mxkj.yuanyintang.mainui.newapp.OriginalMusicListAdapter
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean
import com.mxkj.yuanyintang.musicplayer.service.MediaService.bean
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.fragment_more_song.*
import kotlinx.android.synthetic.main.fragment_more_song.view.*
import kotlinx.android.synthetic.main.more_music_header.view.*
import okhttp3.Headers

class FragMoreSong : BaseFragment() {
    private var coverAdapter: OriginalMusicListAdapter? = null
    private var musics: ArrayList<HomeBean.TypeMusicListBean.TypeMusicBean.MusicBean> = ArrayList()
    private var page = 1
    private var headView: View? = null
    override val layoutId: Int
        get() = R.layout.fragment_more_song

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initEvent()
        initData()
    }

    fun initData() {
        if (bean == null) return
        NetWork.getMusicianMusic(activity, page, bean?.uid.toString(), object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                if (coverAdapter == null) return
                val musicIanMusicBean = JSON.parseObject(resultData, MusicIanMusicBean::class.java)
                        ?: return
                val dataBean = JSON.toJSONString(musicIanMusicBean.data)
                val dataList = JSON.parseArray(dataBean, HomeBean.TypeMusicListBean.TypeMusicBean.MusicBean::class.java)
                if (dataList != null && dataList.size > 0) {
                    if (dataList.size<10){
                        tv_load_more?.text = "~没有更多啦~"
                    }
                    rootView?.tvLoading.visibility = GONE
                    if (page == 1) {
                        musics.clear()
                    }
                    musics.addAll(dataList)
                    coverAdapter?.setNewData(musics)
                } else {
                    tv_load_more?.text = "~没有更多啦~"
                }
            }

            override fun doError(msg: String) {
                if (page > 1) {
                    page--
                }
            }

            override fun doResult() {

            }
        })
        setHeaderInfo()
    }

    private var tv_load_more: TextView? = null

    private fun initEvent() {
        recycler.layoutManager = LinearLayoutManager(activity)
        if (coverAdapter == null) {
            val myRecyclerDetorration = MyRecyclerDetorration(activity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(activity, 5f), R.color.white, false)
            recycler.addItemDecoration(myRecyclerDetorration)
            coverAdapter = OriginalMusicListAdapter(musics, fragmentManager)
            headView = LayoutInflater.from(activity).inflate(R.layout.more_music_header, null)
            coverAdapter?.addHeaderView(headView)
            if (coverAdapter?.footerLayoutCount == 0) {
                val footView = LayoutInflater.from(activity).inflate(R.layout.no_more_data_text, null)
                tv_load_more = footView.findViewById(R.id.tv_load_more)
                tv_load_more?.text = "~点击加载更多~"
                tv_load_more?.setOnClickListener {
                    page++
                    initData()
                }
                coverAdapter?.addFooterView(footView)
            }
            recycler.adapter = coverAdapter
            recycler.setHasFixedSize(true)
            recycler.isFocusableInTouchMode = false
            recycler.isFocusable = false

        } else {
            coverAdapter?.setNewData(musics)
        }
        headView?.let {
        }
    }

    private fun setHeaderInfo() {
        if (bean == null || headView == null || bean?.uid == 0) return
        UserInfoUtil.getUserInfoById(bean!!.uid, activity) { infoBean ->
            infoBean?.let {
                headView?.tv_music_nickname?.text = it.data?.nickname
                headView?.tv_desc?.text = it.data?.signature
                headView?.ck_follow?.text = if (it.data?.is_relation == 1) {
                    "已关注"
                } else {
                    "+关注"
                }
                headView?.ck_follow?.isChecked = it.data?.is_relation == 1
                ImageLoader.with(activity).url(it.data?.head_link).getSize(150, 150).into(headView?.cimg_cover)
                headView?.iv_is_vip?.visibility = if (it.data?.is_music == 3) {
                    View.VISIBLE
                } else {
                    GONE
                }
            }
        }
        headView?.ck_follow?.setOnTouchListener { view, motionEvent ->

            if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN, false)) {
                if (motionEvent.action==MotionEvent.ACTION_DOWN){
                    headView?.ck_follow?.isChecked = !(headView?.ck_follow?.isChecked?:false)
                    if (headView?.ck_follow?.isChecked == true) {
                        headView?.ck_follow?.text = "已关注"
                    } else {
                        headView?.ck_follow?.text = "+关注"
                    }
                    val params = HttpParams()
                    params.put("id", bean!!.uid.toString())
                    if (activity != null) {
                        NetWork.follow(activity, params, object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jObj = JSON.parseObject(resultData)
                                val code = jObj.getInteger("code")
                                if (code == 200) {
                                    if (bean?.is_relation == 0) {
                                        headView?.ck_follow?.text = "已关注"
                                        bean?.is_relation = 1
                                    } else if (bean?.is_relation == 1) {
                                        headView?.ck_follow?.text = "+关注"
                                        bean?.is_relation = 0
                                    }
                                }
                            }

                            override fun doError(msg: String) {
                            }

                            override fun doResult() {

                            }
                        })
                    }
                }
            }else{
                val intent = Intent(activity, LoginRegMainPage::class.java)
                startActivity(intent)
            }


            true
        }
        headView?.ll_todetial?.setOnClickListener({
            val intent = Intent(activity, MusicIanDetailsActivity::class.java)
            intent.putExtra(MusicIanDetailsActivity.MUSICIAN_ID, bean?.uid.toString())
            activity.startActivity(intent)
        })
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart("frg_more_song")

    }

    override fun onDestroy() {
        super.onDestroy()
        MobclickAgent.onPageEnd("frg_more_song")

    }
}