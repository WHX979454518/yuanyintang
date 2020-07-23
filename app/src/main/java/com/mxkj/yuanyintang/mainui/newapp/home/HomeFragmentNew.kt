package com.mxkj.yuanyintang.mainui.newapp.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.mainui.home.bean.HomeIndex
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.trello.rxlifecycle2.components.support.RxFragment

import java.util.ArrayList

import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean.BannerBean
import com.mxkj.yuanyintang.mainui.search.SearchActivity
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.rxbus.RxBus
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber
import com.mxkj.yuanyintang.utils.threadpool.ThreadPoolManager
import com.umeng.analytics.MobclickAgent
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home_new.view.*
import okhttp3.Headers
import java.lang.RuntimeException

/**
 * 首页
 */
class HomeFragmentNew : RxFragment() {
    private lateinit var homeActivity: HomeActivity
    private var dataList: ArrayList<HomeBean> = ArrayList()
    private var homeAdapter: HomeAdapter? = null
    private var resetData: Disposable? = null
    private var rootView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_home_new, container, false)
        return rootView
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (null == rootView) {
            return
        }
        homeActivity = activity as HomeActivity
        initView()
        initEvent()
        netData()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun initEvent() {
        rootView?.layout_top_search?.setOnClickListener {
            MobclickAgent.onEvent(activity, "home_search")
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }
        resetData = RxBus.getDefault().toObservable(String::class.java).subscribeWith(object : RxBusSubscriber<String>() {
            @Throws(Exception::class)
            override fun onEvent(str: String) {
                if (str == "resetData") {
//                    Log.e("resetData", "");
                    CacheUtils.setString(activity, "homeJson", "")
                    netData()
                }
            }
        })

        RxBus.getDefault().add(resetData)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun initView() {
        val linearLayoutManager = LinearLayoutManager(activity)
        rootView?.let {
            linearLayoutManager.isSmoothScrollbarEnabled = false
            it.newHomeRecycler.isNestedScrollingEnabled = false
            it.newHomeRecycler.setHasFixedSize(true)
            it.newHomeRecycler.layoutManager = linearLayoutManager
            val myRecyclerDetorration = MyRecyclerDetorration(activity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(activity, 5f), R.color.white, false)
            if (homeAdapter == null) {
                it.newHomeRecycler.addItemDecoration(myRecyclerDetorration)
                homeAdapter = HomeAdapter(dataList, activity.supportFragmentManager)
                it.newHomeRecycler.adapter = homeAdapter
            } else {
                homeAdapter?.setNewData(dataList)
            }
            it.scroll_view.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                val layoutParams = it.tvBck.layoutParams
                var height = it.tvBck.height
                if (oldScrollY - scrollY < 0) {
                    if (height > CommonUtils.dip2px(activity, 70f)) {
                        layoutParams.height = height - Math.abs(oldScrollY - scrollY)
                    }
                } else {//下滑
                    val l = it.newHomeRecycler.layoutManager as LinearLayoutManager
                    var adapterNowPos = l.findFirstVisibleItemPosition()

                    if (adapterNowPos == 0 && height < CommonUtils.dip2px(activity, 200f)) {
                        layoutParams.height = height + Math.abs(oldScrollY - scrollY)
                    }
                }
                it.tvBck.layoutParams = layoutParams
            }
        }
    }

    private var needCache: Boolean = true

    fun netData() {

        NetWork.getHome("homeJson", activity, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
//                CacheUtils.setString(activity, "homeJson", resultData);
                jsonData(resultData)
            }
            override fun doError(msg: String) {

            }

            override fun doResult() {
            }
        })
    }

    private fun jsonData(resultData: String) {
        dataList.clear()
        var jsonObj: JSONObject?
        var dataObj: JSONObject? = null
        try {
            jsonObj = JSON.parseObject(resultData)
            dataObj = jsonObj.getJSONObject("data")
        } catch (e: RuntimeException) {
            CacheUtils.setString(activity, "homeJson", "")
        }
        Observable.create(ObservableOnSubscribe<List<HomeBean>> { e ->
            val shufflingArray = if (dataObj?.getJSONArray("shuffling") == null) JSONArray() else dataObj.getJSONArray("shuffling")
            bannerJsonArray(shufflingArray)
            fourTypeJsonObj(dataObj)
            sysMsgJsonObj(dataObj)
            recommendAlbumJsonObj(dataObj)
            yxyRecommJsonObj(dataObj, 0)
            oriMusicJsonObj(dataObj, 0)
            yxyRecommJsonObj(dataObj, 1)
            coverMusicJsonObj(dataObj, 1)
            musicIanJsonObj(dataObj, "recommend_musician")
            guessLikeJsonObj(dataObj)
            musicIanJsonObj(dataObj, "newest_musician")
            e.onNext(dataList)
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            homeAdapter?.setNewData(it)
        }
    }

    /**
     * 猜你喜欢
     */
    private fun guessLikeJsonObj(dataObj: JSONObject?) {
        dataObj?.let {
            var guessHomeBean = HomeBean()
            val guessBean = HomeBean.GuessBean()
            val recObj = it.getJSONObject("guess_you_like")
            val cateImgBean = recObj?.getObject("cate_img", HomeBean.CateImgBean::class.java)
            val guessList = JSON.parseArray(recObj?.getJSONArray("song")?.toJSONString(), MusicIndex.ItemInfoListBean.RecommendBean::class.java)
            guessBean.guessList = guessList
            guessBean.cate_img = cateImgBean
            guessHomeBean.guessBean = guessBean
            dataList.add(guessHomeBean)
        }
    }

    private fun musicIanJsonObj(dataObj: JSONObject?, mType: String) {
        var musicianHomeBean = HomeBean()
        val homeMusicianBean = HomeBean.HomeMusicianBean()
        val rcmObj = dataObj?.getJSONObject(mType)
        val cateImgBean = rcmObj?.getObject("cate_img", HomeBean.HomeMusicianBean.CateImgBean::class.java)
        val musicianList = rcmObj?.getJSONArray("musician")?.toJavaList(HomeBean.HomeMusicianBean.MusicianBean::class.java)
        homeMusicianBean.musician = musicianList
        homeMusicianBean.cate_img = cateImgBean
        homeMusicianBean.type = mType
        musicianHomeBean.musicianBean = homeMusicianBean
        dataList.add(musicianHomeBean)
    }

    private fun oriMusicJsonObj(dataObj: JSONObject?, index: Int) {
        dataObj?.let {
            var originalHomeBean = HomeBean()
            var typeMusicListBean = HomeBean.TypeMusicListBean()
            val jsonArray = it.getJSONArray("type_music")
            val typeList = jsonArray?.toJavaList(HomeBean.TypeMusicListBean.TypeMusicBean::class.java)
            typeMusicListBean.type_music = typeList
            typeMusicListBean.index = index
            originalHomeBean.typeOriListBean = typeMusicListBean
            dataList.add(originalHomeBean)
        }
    }

    private fun coverMusicJsonObj(dataObj: JSONObject?, index: Int) {
        dataObj?.let {
            var originalHomeBean = HomeBean()
            var typeMusicListBean = HomeBean.TypeMusicListBean()
            val jsonArray = it.getJSONArray("type_music")
            val typeList = jsonArray?.toJavaList(HomeBean.TypeMusicListBean.TypeMusicBean::class.java)
            typeMusicListBean.type_music = typeList
            typeMusicListBean.index = index
            originalHomeBean.typeCoverListBean = typeMusicListBean
            dataList.add(originalHomeBean)
        }
    }

    private fun yxyRecommJsonObj(dataObj: JSONObject?, index: Int) {
        dataObj?.let {
            var recomendHomeBeanPond = HomeBean()
            val yxyRecomendBean = HomeBean.YxyRecomendBean()
            val jsonArray = it.getJSONArray("yxy_notice") ?: return
            val yxyNoticeList = JSON.parseArray(jsonArray.toJSONString(), HomeBean.YxyRecomendBean.YxyNoticeBean::class.java)
         var home_ad=   it.getJSONArray("home_ad")?:return

            val homeAdList = JSON.parseArray(home_ad.toJSONString(), HomeBean.YxyRecomendBean.HomeAdBean::class.java)
            if (homeAdList.size > index) {
                yxyRecomendBean.yxy_notice = yxyNoticeList
                yxyRecomendBean.home_ad = homeAdList
                yxyRecomendBean.index = index
                recomendHomeBeanPond.recomendBean = yxyRecomendBean
                dataList.add(recomendHomeBeanPond)
            }
        }
    }

    /**
     * 新歌速递、推荐歌单
     */
    private fun recommendAlbumJsonObj(dataObj: JSONObject?) {
        dataObj?.let {
            var albumHomeBean = HomeBean()
            val albumBean = HomeBean.AlbumBean()
            val recObj = it.getJSONObject("recommend_song")
            val cateImgBean = recObj?.getObject("cate_img", HomeBean.AlbumBean.CateImgBean::class.java)
            val albumList = JSON.parseArray(recObj?.getJSONArray("song")?.toJSONString(), MusicIndex.ItemInfoListBean.RecommendBean::class.java)
            albumBean.recommend = albumList
            albumBean.cate_img = cateImgBean
            albumHomeBean.albumBean = albumBean
            dataList.add(albumHomeBean)
        }


    }

    private fun sysMsgJsonObj(dataObj: JSONObject?) {
        dataObj?.let {
            var sysMsgpeHomeBean = HomeBean()
            val toJavaList = it.getJSONArray("system_affiche").toJavaList(HomeBean.SysMsgBean.SystemAfficheBean::class.java)
            if (toJavaList.isNotEmpty()) {
                val sysMsgBean = HomeBean.SysMsgBean()
                sysMsgBean.system_affiche = toJavaList
                sysMsgpeHomeBean.sysMsgBean = sysMsgBean
                dataList.add(sysMsgpeHomeBean)
            }
        }
    }

    /**
     * 今日推荐、音乐分类、排行、附近
     */
    private fun fourTypeJsonObj(dataObj: JSONObject?) {
        dataObj?.let {
            var fourtypeHomeBean = HomeBean()
            val typeBean = HomeBean.TypeBean()
            val todayRecommendsBean = it.getObject("today_recommends", HomeBean.TypeBean.TodayRecommendsBean::class.java)
            val musicCateBean = it.getObject("music_category", HomeBean.TypeBean.MusicCategoryBean::class.java)
            val billboardBean = it.getObject("billboard", HomeBean.TypeBean.BillboardBean::class.java)
            val nearbyBean = it.getObject("nearby", HomeBean.TypeBean.NearbyBean::class.java)
            typeBean.today_recommends = todayRecommendsBean
            typeBean.music_category = musicCateBean
            typeBean.billboard = billboardBean
            typeBean.nearby = nearbyBean
            fourtypeHomeBean.typeBean = typeBean
            dataList.add(fourtypeHomeBean)
        }
    }

    /**
     * banner
     */
    private fun bannerJsonArray(shufflingArray: JSONArray?): HomeBean {
        var bannerHomeBean = HomeBean()
        var bannerBean = BannerBean()
        val shufflingDataList = JSON.parseArray(shufflingArray.toString(), HomeIndex.ItemInfoListBean.ShufflingBean::class.java)
        bannerBean.shuffling = shufflingDataList
        bannerHomeBean.bannerBean = bannerBean
        dataList.add(bannerHomeBean)
        return bannerHomeBean
    }

    /**
     * 退出应用或者进程被kill
     */
    override fun onDestroyView() {
        super.onDestroyView()
//        CacheUtils.setString(activity, "homeJson", "{}")
        RxBus.getDefault().remove(resetData)
    }

}
