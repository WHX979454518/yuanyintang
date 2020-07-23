package com.mxkj.yuanyintang.mainui.home.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.mainui.message.activity.MessageCenterActivity
import com.mxkj.yuanyintang.net.ApiAddress
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.home.adapter.HomeMultipleRecycleAdapter
import com.mxkj.yuanyintang.mainui.home.bean.HomeIndex
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.search.SearchActivity
import com.mxkj.yuanyintang.extraui.dialog.GuideDialog
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.uiutils.Srceen
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.rxbus.RxBus
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber
import com.mxkj.yuanyintang.utils.rxbus.event.PlayerMusicRefreshDataEvent
import com.mxkj.yuanyintang.utils.rxbus.event.SearchRecommendEvent
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout
import com.trello.rxlifecycle2.components.support.RxFragment

import java.util.ArrayList

import butterknife.OnClick
import butterknife.Unbinder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.*

/*
* 2.4以及之前版本的首页  没用了可以删除
*
* */
class HomeFragment : RxFragment(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.rl_msg_center -> activity.startActivity(Intent(activity, MessageCenterActivity::class.java))
            R.id.layout_top_search -> if (searchRecommend?.title != null) {
                val intent = Intent(activity, SearchActivity::class.java)
                intent.putExtra(SEARCH_WORDS, searchRecommend?.title)
                startActivity(intent)
            }
        }
    }

    var unbinder: Unbinder? = null
    lateinit var rootView: View
    var homeMultipleRecycleAdapter: HomeMultipleRecycleAdapter? = null
    var mDistance = 0
    private var fristEndTime: Long = 0L
    var mPlayerMusicRefreshData: Disposable? = null
    private var itemInfoListBeenList: MutableList<HomeIndex.ItemInfoListBean> = ArrayList()
    private lateinit var homeActivity: HomeActivity
    lateinit var interfaceRefreshLoadMore: InterfaceRefreshLoadMore
    private var searchRecommend: SearchRecommendEvent? = null
    var disposable: Disposable? = null
    private var isAddMsg: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
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
        initHeightView()
        initView()
        initEvent()
//        netData()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun initEvent() {
        rl_msg_center.setOnClickListener(this)
        layout_top_search.setOnClickListener(this)
        sysMsgEvent()
        RxBus.getDefault().add(disposable)
        mPlayerMusicRefreshData = RxBus.getDefault().toObservable(PlayerMusicRefreshDataEvent::class.java)
                .subscribeWith(object : RxBusSubscriber<PlayerMusicRefreshDataEvent>() {
                    @Throws(Exception::class)
                    override fun onEvent(playerMusicRefreshDataEvent: PlayerMusicRefreshDataEvent) {
                        if (null != homeMultipleRecycleAdapter) {
                            homeMultipleRecycleAdapter?.notifyData()//收到开始播放的消息，更新首页每一个子adapter里面播放按钮的状态
                        }
                    }
                })
        RxBus.getDefault().add(mPlayerMusicRefreshData)
        when {
            TextUtils.equals("http://demoapi.yuanyintang.com", ApiAddress.BASE_URL) -> tv_test.visibility = View.VISIBLE
            TextUtils.equals("https://api.yuanyintang.com", ApiAddress.BASE_URL) -> tv_test.visibility = View.GONE
            else -> tv_test.visibility = View.VISIBLE
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private fun initHeightView() {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN) {
            rootView.layout_top_search.background = ContextCompat.getDrawable(activity, R.drawable.gradient_search_bg)
            rootView.layout_top_search.background.alpha = 255
            val height = Srceen.getScreen(activity)[1] / 2
            rootView.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    mDistance += dy
                    val percent = mDistance * 1f / height//百分比
                    val alpha = (percent * 255).toInt()
                    if (alpha >= 255) {
                        rootView.layout_top_search.background.alpha = 255
                    } else {
                        rootView.layout_top_search.background.alpha = alpha
                    }
                }
            })
            StatusBarUtil.baseTransparentStatusBar(activity)
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StatusBarUtil.getStatusBarHeight(activity))
            rootView.v_statusbar.layoutParams = params
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                rootView.v_statusbar.visibility = View.GONE
            }
        }

    }

    fun initView() {
        superSwipeRefreshLayout.viewType = SuperSwipeRefreshLayout.AddViewType.HEAD
        interfaceRefreshLoadMore = InterfaceRefreshLoadMore(superSwipeRefreshLayout, activity, object : InterfaceRefreshLoadMore.RefreshLoadMoreCallback {
            override fun onRefresh() {
//                netData()
            }

            override fun onLoadMore() {
                interfaceRefreshLoadMore.setStopRefreshing()
            }

            override fun onPushDistance(distance: Int) {

            }

            override fun onPullDistance(distance: Int) {
                if (null != rootView.layout_head_search) {
                    rootView.layout_head_search.visibility = if (distance > 0) View.GONE else View.VISIBLE
                }
            }
        })
        setMsgCount(homeActivity.msgUnReadNum)
        itemInfoListBeenList = ArrayList()
//        netData()
    }

    fun setMsgCount(msgCount: Int) {
        if (null != tv_msg_count) {
            if (!CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                tv_msg_count.visibility = View.GONE
                return
            }
            if (msgCount <= 0) {
                tv_msg_count.visibility = View.GONE
            } else {
                tv_msg_count.visibility = View.VISIBLE
                tv_msg_count.text = if (msgCount > 9) "9+" else msgCount.toString() + ""
            }
        }
    }
//
//    fun netData() {
//        NetWork.getHome(!superSwipeRefreshLayout?.isRefreshing!!, activity, fristEndTime, object : NetWork.TokenCallBack {
//            override fun doNext(resultData: String, headers: Headers?) {
//                val jsonObject = JSON.parseObject(resultData)
//                val jObj = jsonObject.getJSONObject("data")
//                if (jObj != null) {
//                    //Banner解析
//                    val shufflingArray = if (jObj.getJSONArray("shuffling") == null) JSONArray() else jObj.getJSONArray("shuffling")
//                    bannerJsonArray(shufflingArray)
//                    //抽奖活动布局、数据
//                    initLottery(jObj)
//                    //系统消息
//                    if (CacheUtils.getBoolean(activity, SHOW_SYS_MSG, true)) {
//                        systemJson(jObj)
//                    } else {
//                        systemJson(null)
//                    }
//                    //榜单
//                    songCharts(jObj)
//
//                    //推荐音乐解析
//                    val songBeanObject = if (jObj.getJSONObject("song") == null) JSONObject() else jObj.getJSONObject("song")
//                    songJsonObject(songBeanObject)
//                    //推荐音乐人解析
//                    val musicianBeanObject = if (jObj.getJSONObject("musician") == null) JSONObject() else jObj.getJSONObject("musician")
//                    musicianJsonObject(musicianBeanObject)
//                    //人气音乐
//                    val cateMusicArray = if (jObj.getJSONArray("catemusic") == null) JSONArray() else jObj.getJSONArray("catemusic")
//                    musicJsonArray(cateMusicArray)
//                    // 池塘
//                    val topicObject = if (jObj.getJSONObject("topic") == null) JSONObject() else jObj.getJSONObject("topic")
//                    topicJsonObject(topicObject)
//                    refreshData()
//                }
//            }
//
//            override fun doError(msg: String) {
//
//            }
//
//            override fun doResult() {
//                interfaceRefreshLoadMore.setStopRefreshing()
//            }
//        })
//    }

    fun initLottery(jObj: JSONObject?) {
        var isEquals: Boolean? = false
        for (i in itemInfoListBeenList.indices) {
            if (TextUtils.equals("lottery", itemInfoListBeenList[i].itemType)) {
                isEquals = true
                break
            }
        }
        val itemInfoListBean = HomeIndex.ItemInfoListBean()
        itemInfoListBean.itemType = "lottery"
        val activityAffiche = jObj?.getJSONArray("activityAffiche")
        if (activityAffiche != null && activityAffiche.size > 0) {

            val lotteryBeans = activityAffiche.toJavaList(HomeIndex.ItemInfoListBean.LotteryBean::class.java)
            itemInfoListBean.lotteryList = lotteryBeans
            if (!isEquals!!) {
                itemInfoListBeenList.add(1, itemInfoListBean)
            }
        }
    }

    fun systemJson(jObj: JSONObject?) {
        var isEquals: Boolean? = false
        if (!isEquals!! && CacheUtils.getBoolean(activity, SHOW_SYS_MSG, false)) {
            val itemInfoListBean = HomeIndex.ItemInfoListBean()
            if (jObj != null) {
                itemInfoListBean.itemType = "systemMsg"
                val systemAffiche = jObj.getJSONArray("systemAffiche")
                if (systemAffiche != null && systemAffiche.size > 0 && !isAddMsg) {
                    val systemMsgBeans = systemAffiche.toJavaList(HomeIndex.ItemInfoListBean.SystemMsgBean::class.java)
                    itemInfoListBean.systemMsgBeanList = systemMsgBeans
                    CacheUtils.setBoolean(activity, SHOW_SYS_MSG, true)
                    if (itemInfoListBeenList.size > 2) {
                        if (itemInfoListBeenList[1].itemType == "systemMsg") {
                            itemInfoListBeenList.add(2, itemInfoListBean)
                        } else {
                            itemInfoListBeenList.add(1, itemInfoListBean)
                        }
                        val systemMsgBean = systemMsgBeans[0]
                        CacheUtils.setString(activity, STR_SYS_MSG, JSON.toJSONString(systemMsgBean))
                        RxBus.getDefault().post(systemMsgBean)//发送事件
                        isAddMsg = true
                    }
                }
            }
        }
    }

    /*
    * 排行榜
    * */
    fun songCharts(jObj: JSONObject?) {
        if (jObj == null) {
            return
        }
        val billboardObj = jObj.getJSONObject("billboard")
        if (billboardObj != null) {
            var isEquals: Boolean? = false
            for (i in itemInfoListBeenList.indices) {
                if (TextUtils.equals("charts", itemInfoListBeenList[i].itemType)) {
                    isEquals = true
                    break
                }
            }

            val itemInfoListBean = HomeIndex.ItemInfoListBean()
            itemInfoListBean.itemType = "charts"
            val jsonArray = billboardObj.getJSONArray("billboards")
            val billboardBean = HomeIndex.ItemInfoListBean.BillboardBean()
            if (jsonArray != null) {
                Log.e(TAG, "songCharts: " + jsonArray.toJSONString())
                val billboardsBeans = jsonArray.toJavaList(HomeIndex.ItemInfoListBean.BillboardBean.BillboardsBean::class.java)
                billboardBean.billboards = billboardsBeans
            }
            val cateImg = billboardObj.getObject("cateImg", HomeIndex.ItemInfoListBean.BillboardBean.CateImgBean::class.java)
            billboardBean.cate_imgX = cateImg
            itemInfoListBean.billboardBean = billboardBean
            if (!isEquals!!) {
                itemInfoListBeenList.add(itemInfoListBean)
            }
        }
    }

    fun topicJsonObject(topicObject: JSONObject) {
        val topicArray = if (topicObject.getJSONArray("topic") == null) JSONArray() else topicObject.getJSONArray("topic")
        val topicBeanList = JSON.parseArray(topicArray.toString(), HomeIndex.ItemInfoListBean.TopicBean::class.java)
        var isEquals: Boolean? = false
        for (i in itemInfoListBeenList.indices) {
            if (TextUtils.equals("pond", itemInfoListBeenList[i].itemType)) {
                isEquals = true
                break
            }
        }

        for (i in topicBeanList.indices) {
            val itemInfoListBean = HomeIndex.ItemInfoListBean()
            itemInfoListBean.itemType = "pond"
            itemInfoListBean.topic = topicBeanList
            itemInfoListBean.tag = i
            if (!isEquals!!) {
                if (i == 0) {
                    itemInfoListBean.cate_img = topicObject["cate_img"].toString() + ""
                }
                itemInfoListBeenList.add(itemInfoListBean)
            }
        }
    }

    fun musicJsonArray(cateMusicArray: JSONArray) {
        if (cateMusicArray.size > 0) {
            val cateMusicBean = JSON.parseArray(cateMusicArray.toString(), HomeIndex.ItemInfoListBean.CatemusicBean::class.java)
            var isEquals: Boolean? = false
            for (i in itemInfoListBeenList.indices) {
                if (TextUtils.equals("vocaloid", itemInfoListBeenList[i].itemType)) {
                    isEquals = true
                    break
                }
            }
            for (i in cateMusicBean.indices) {
                val itemInfoListBean = HomeIndex.ItemInfoListBean()
                itemInfoListBean.itemType = "vocaloid"
                itemInfoListBean.tag = i
                itemInfoListBean.catemusic = cateMusicBean
                if (!isEquals!!) {
                    itemInfoListBeenList.add(itemInfoListBean)
                } else {
                    for (j in itemInfoListBeenList.indices) {
                        if (TextUtils.equals("vocaloid", itemInfoListBeenList[j].itemType)) {
                            itemInfoListBeenList[j].catemusic = cateMusicBean
                        }
                    }
                }
            }
        }
    }

    fun musicianJsonObject(musicianBeanObject: JSONObject) {
        val musicianArray = if (musicianBeanObject.getJSONArray("musician") == null)
            JSONArray()
        else
            musicianBeanObject.getJSONArray("musician")
        val musicianObject = if (musicianBeanObject.getJSONObject("cate_img") == null)
            JSONObject()
        else
            musicianBeanObject.getJSONObject("cate_img")
        val musicianBeanX = JSON.parseObject(musicianBeanObject.toString(), HomeIndex.ItemInfoListBean.MusicianBeanX::class.java)
        if (null != musicianBeanX) {
            val cateImgBean = JSON.parseObject(musicianObject.toString(), HomeIndex.ItemInfoListBean.MusicianBeanX.CateImgBean::class.java)
            musicianBeanX.cate_img = cateImgBean
            if (musicianArray.size > 0) {
                val musicianBean = JSON.parseArray(musicianArray.toString(), HomeIndex.ItemInfoListBean.MusicianBeanX.MusicianBean::class.java)
                musicianBeanX.musician = musicianBean
            }
            val itemInfoListBean = HomeIndex.ItemInfoListBean()
            itemInfoListBean.itemType = "recommendMusicIan"
            itemInfoListBean.setMusician(musicianBeanX)

            var isEquals: Boolean? = false
            var a = -1
            for (i in itemInfoListBeenList.indices) {
                if (TextUtils.equals("recommendMusicIan", itemInfoListBeenList[i].itemType)) {
                    isEquals = true
                    a = i
                    break
                }
            }
            if (isEquals!!) {
                itemInfoListBeenList[a].setMusician(musicianBeanX)
            } else {
                itemInfoListBeenList.add(itemInfoListBean)
            }
        }
    }

    fun songJsonObject(songBeanObject: JSONObject) {
        val songArray = if (songBeanObject.getJSONArray("song") == null)
            JSONArray()
        else
            songBeanObject.getJSONArray("song")
        val songCateImgObject = if (songBeanObject.getJSONObject("cate_img") == null)
            JSONObject()
        else
            songBeanObject.getJSONObject("cate_img")
        val songBeanX = JSON.parseObject(songBeanObject.toString(), HomeIndex.ItemInfoListBean.SongBeanX::class.java)
        if (null != songBeanX) {
            val cateImgBean = JSON.parseObject(songCateImgObject.toString(), HomeIndex.ItemInfoListBean.SongBeanX.CateImgBeanX::class.java)
            songBeanX.cate_img = cateImgBean
            if (songArray.size > 0) {
                val musicianBean = JSON.parseArray(songArray.toString(), HomeIndex.ItemInfoListBean.SongBeanX.SongBean::class.java)
                songBeanX.song = musicianBean
            }
            val itemInfoListBean = HomeIndex.ItemInfoListBean()
            itemInfoListBean.itemType = "recommendMusic"
            itemInfoListBean.song = songBeanX
            var isEquals: Boolean? = false
            var a = -1
            for (i in itemInfoListBeenList.indices) {
                if (TextUtils.equals("recommendMusic", itemInfoListBeenList[i].itemType)) {
                    isEquals = true
                    a = i
                    break
                }
            }
            if (isEquals!!) {
                itemInfoListBeenList[a].song = songBeanX
            } else {
                itemInfoListBeenList.add(itemInfoListBean)
            }
        }
    }

    fun bannerJsonArray(shufflingArray: JSONArray) {
        if (shufflingArray.size > 0) {
            val itemInfoListBean = HomeIndex.ItemInfoListBean()
            itemInfoListBean.itemType = "topBanner"
            val shufflingDataList = JSON.parseArray(shufflingArray.toString(), HomeIndex.ItemInfoListBean.ShufflingBean::class.java)
            itemInfoListBean.shuffling = shufflingDataList
            var isEquals: Boolean? = false
            var a = -1
            for (i in itemInfoListBeenList.indices) {
                if (TextUtils.equals("topBanner", itemInfoListBeenList[i].itemType)) {
                    isEquals = true
                    a = i
                    break
                }
            }
            if (isEquals!!) {
                itemInfoListBeenList[a].shuffling = shufflingDataList
            } else {
                itemInfoListBeenList.add(itemInfoListBean)
            }
        }
    }

    fun refreshData() {
        rootView.recyclerview.setItemViewCacheSize(100)
        rootView.recyclerview.layoutManager = LinearLayoutManager(activity)
        val myRecyclerDetorration = MyRecyclerDetorration(activity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(activity, 5f), R.color.bg_color, false)
        if (homeMultipleRecycleAdapter == null) {
            rootView.recyclerview.addItemDecoration(myRecyclerDetorration)
            homeMultipleRecycleAdapter = HomeMultipleRecycleAdapter(itemInfoListBeenList)
            rootView.recyclerview.adapter = homeMultipleRecycleAdapter
        } else {
            homeMultipleRecycleAdapter?.setNewData(itemInfoListBeenList)
        }
        val homeActivity = activity as HomeActivity
        if (homeActivity?.searchRecommendEvent != null) {
            searchRecommend = homeActivity.searchRecommendEvent
            if (null != tv_hot_search) {
                tv_hot_search.text = StringUtils.isEmpty(searchRecommend?.title)
            }
        }
//        if (!CacheUtils.getBoolean(activity, "home", false)) {
//            CacheUtils.setBoolean(activity, "home", true)
//            val guideDialog = GuideDialog("home")
//            guideDialog.show(activity.supportFragmentManager, "mGuideDialog")
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        interfaceRefreshLoadMore.resetRefreshView()
        unbinder?.unbind()
        RxBus.getDefault().remove(mPlayerMusicRefreshData)
        RxBus.getDefault().remove(disposable)
    }

    private fun sysMsgEvent() {
        disposable = RxBus.getDefault().toObservable(HomeIndex.ItemInfoListBean.SystemMsgBean::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    if (CacheUtils.getBoolean(activity, SHOW_SYS_MSG, true) == false) {
                        val data = homeMultipleRecycleAdapter?.data
                        for (i in data?.indices!!) {
                            if (data[i].systemMsgBeanList != null && data[i].systemMsgBeanList.size > 0) {
                                data.removeAt(i)
                                homeMultipleRecycleAdapter?.setNewData(data)
                            }
                        }
                    }
                }
    }

    companion object {
        const val SEARCH_WORDS = "search_key_word"
        const val SHOW_SYS_MSG = "show_system_msg"
        const val STR_SYS_MSG = "str_system_msg"
        const val TAG = "HomeFragment"
    }
}
