package com.mxkj.yuanyintang.musicplayer.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.GONE
import com.alibaba.fastjson.JSON
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.AbsCallback
import com.lzy.okgo.callback.Callback
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.base.fragment.BaseFragment
import com.mxkj.yuanyintang.base.fragment.BaseLazyFragment
import com.mxkj.yuanyintang.extraui.gift.BotomGiftListDialog
import com.mxkj.yuanyintang.mainui.home.adapter.MusicDetailsRecyclerViewAdapter
import com.mxkj.yuanyintang.mainui.home.adapter.SwitcherAdapter
import com.mxkj.yuanyintang.mainui.home.bean.MemberGiftListBean
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.newapp.pond.PondInfo
import com.mxkj.yuanyintang.musicplayer.adapter.PlayerDetailAdapter
import com.mxkj.yuanyintang.musicplayer.service.MediaService.bean
import com.mxkj.yuanyintang.net.ApiAddress
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.net.SetRequestHeaders
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.layoutmanager.FullyLinearLayoutManager
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.trello.rxlifecycle2.components.support.RxFragment
import com.umeng.analytics.MobclickAgent
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_music_details.*
import kotlinx.android.synthetic.main.frag_music_detail.view.*
import kotlinx.android.synthetic.main.right_switcher_list.*
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import kotlinx.android.synthetic.main.frag_music_detail.*

class FragMusicDetail : BaseFragment() {
    private var itemInfoBean: MusicIndex.ItemInfoListBean? = null
    private var musicListMusicAdapter: PlayerDetailAdapter? = null
    private var id: String = "0"
    private var uid: String = "0"
    private lateinit var mPlayerMusicRefreshData: Disposable
    private var switcherList: MutableList<MemberGiftListBean.DataBean.DataListBean> = ArrayList()
    private lateinit var switcherAdapter: SwitcherAdapter
    private lateinit var profit_billboard_config: MemberGiftListBean.DataBean.ProfitBillboardConfigBean
    override val layoutId: Int
        get() = R.layout.frag_music_detail

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        initData()
    }

    fun initData() {
        NetWork.getMusicDetails(activity, bean?.id.toString(), 0L, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                rootView.tvLoading.visibility=GONE
                try {
                    val jObj = JSONObject(resultData)
                    val dataObject = if (jObj.optJSONObject("data") == null) JSONObject() else jObj.optJSONObject("data")
                    jsonData(dataObject)
                } catch (e: JSONException) {
                    Log.e(BaseActivity.TAG, "doNext: $e")
                    e.printStackTrace()
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    private fun jsonData(dataObject: JSONObject) {
        if (musicListMusicAdapter == null) {
            return
        }
        itemInfoBean = JSON.parseObject(dataObject.toString(), MusicIndex.ItemInfoListBean::class.java)
        if (itemInfoBean == null) return
        musicListMusicAdapter?.data?.clear()
        //                  TODO：关联小伙伴
        val partnerArray = if (dataObject.optJSONArray("member") == null) JSONArray() else dataObject.optJSONArray("member")
        if (partnerArray != null) {
            val memberBeanXES = JSON.parseArray(StringUtils.isEmpty(partnerArray.toString()), MusicIndex.ItemInfoListBean.MemberBeanX::class.java)
            if (memberBeanXES != null && memberBeanXES.size > 0) {
                val relatedPartner = MusicIndex.ItemInfoListBean()
                relatedPartner.member = memberBeanXES
                relatedPartner.itemType = "relatedPartnerView"
                musicListMusicAdapter?.data?.add(relatedPartner)
            }
        }
        //                  TODO：音乐简介
        val synopsisInfoListBean = MusicIndex.ItemInfoListBean()
        synopsisInfoListBean.itemType = "synopsis"
        synopsisInfoListBean.intro = itemInfoBean?.intro
        musicListMusicAdapter?.data?.add(synopsisInfoListBean)

        //                  TODO：相关歌单
        val songAllArray = if (dataObject.optJSONArray("song_all") == null) JSONArray() else dataObject.optJSONArray("song_all")
        val songAllBeanList = JSON.parseArray(StringUtils.isEmpty(songAllArray.toString()), MusicIndex.ItemInfoListBean.SongAllBean::class.java)
        val songInfoListBean = MusicIndex.ItemInfoListBean()
        songInfoListBean.itemType = "song"
        songInfoListBean.song_all = songAllBeanList ?: ArrayList()
        musicListMusicAdapter?.data?.add(songInfoListBean)

        //                  TODO：猜你喜歡
        val likeArray = if (dataObject.optJSONArray("likes") == null) JSONArray() else dataObject.optJSONArray("likes")
        val likeBeanList = JSON.parseArray(StringUtils.isEmpty(likeArray.toString()), MusicIndex.ItemInfoListBean.MusicBean::class.java)
        if (null != likeBeanList && likeBeanList.size > 0) {
            val likeInfoListBean = MusicIndex.ItemInfoListBean()
            likeInfoListBean.itemType = "recommendSong"
            likeInfoListBean.music = likeBeanList
            musicListMusicAdapter?.data?.add(likeInfoListBean)
        }

        //                  TODO：相关池塘
        val topicArray = if (dataObject.optJSONArray("topic") == null) JSONArray() else dataObject.optJSONArray("topic")
        val topicBeanList = JSON.parseArray(StringUtils.isEmpty(topicArray.toString()), PondInfo.DataBean.DataListBean::class.java)
        if (null != topicBeanList && topicBeanList.size > 0) {
            val infoListBean = MusicIndex.ItemInfoListBean()
            infoListBean.itemType = "pond"
            infoListBean.pondList = topicBeanList
            musicListMusicAdapter?.data?.add(infoListBean)
        }
        refreshData(itemInfoBean!!)
    }

    private fun refreshData(itemInfoBean: MusicIndex.ItemInfoListBean) {
        uid = itemInfoBean.uid.toString() + ""
        musicListMusicAdapter?.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.isAutoMeasureEnabled = true
        recyclerview.layoutManager = layoutManager
        recyclerview.setHasFixedSize(true)
        musicListMusicAdapter = PlayerDetailAdapter(activity, activity.supportFragmentManager, id, if (itemInfoBean == null) 0 else itemInfoBean?.uid!!)
        recyclerview.adapter = musicListMusicAdapter
    }


    override fun onResume() {
        super.onResume()
        initData()
        MobclickAgent.onPageStart("frg_music_detail")

    }

    override fun onDestroy() {
        super.onDestroy()
        MobclickAgent.onPageEnd("frg_music_detail")

    }

}