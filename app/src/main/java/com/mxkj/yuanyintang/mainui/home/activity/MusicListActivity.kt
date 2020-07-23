package com.mxkj.yuanyintang.mainui.home.activity

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.flyco.dialog.widget.popup.BasePopup
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.home.adapter.MusicListMusicAdapter
import com.mxkj.yuanyintang.mainui.home.adapter.MusicListMusicIanAdapter
import com.mxkj.yuanyintang.mainui.home.adapter.MusicListSongAdapter
import com.mxkj.yuanyintang.mainui.home.adapter.MusicListTagAdapter
import com.mxkj.yuanyintang.mainui.home.bean.MusicListMusicBean
import com.mxkj.yuanyintang.mainui.home.bean.MusicListMusicIanBean
import com.mxkj.yuanyintang.mainui.home.bean.MusicListSongBean
import com.mxkj.yuanyintang.mainui.home.bean.MusicListTabBean
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.utils.GridSpacingItemDecoration
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.rxbus.RxBus
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber
import com.mxkj.yuanyintang.utils.rxbus.event.PlayerMusicRefreshDataEvent
import com.mxkj.yuanyintang.utils.rxbus.event.RefreshIsPlayerEvent
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout

import org.json.JSONArray
import org.json.JSONException

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import kotlinx.android.synthetic.main.activity_music_list.*
/*
*
* 这个类要删（2.5之后的版本估计用不到这个页面了）
*改动太大，音乐人列表歌单列表等页面2.5重写过
*
* */
class MusicListActivity : StandardUiActivity() {
    private var musicListTagAdapter: MusicListTagAdapter? = null
    private var musicListMusicAdapter: MusicListMusicAdapter? = null
    private var musicListSongAdapter: MusicListSongAdapter? = null
    private var musicListMusicIanAdapter: MusicListMusicIanAdapter? = null
    internal var pid: Int = 0//1.音乐，2.音乐人 其他歌單
    internal var item_id: Int = 0
    private var category_id: Int = 0
    internal var tagPage = 1
    internal var page = 1
    internal var tag = 0
    internal var musicListTabBeanList: MutableList<MusicListTabBean> = ArrayList()
    internal var musicListMusicBeanList: MutableList<MusicListMusicBean> = ArrayList()
    internal var musicListSongBeanList: MutableList<MusicListSongBean> = ArrayList()
    internal var musicListMusicIanBeanList: MutableList<MusicListMusicIanBean.DataBean> = ArrayList()
    private lateinit var mRefreshIsPlayerEvent: Disposable
    private lateinit var mPlayerMusicRefreshData: Disposable
    private var selectTab: String? = null
    internal lateinit var interfaceRefreshLoadMore: InterfaceRefreshLoadMore

    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun setLayoutId(): Int {
        return R.layout.activity_music_list
    }

    override fun initView() {
        ButterKnife.bind(this)
        selectTab = "id-desc"
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 122)
        val title = intent.getStringExtra(MUSIC_LIST_TITLE)
        pid = intent.getIntExtra(MUSIC_GET_TYPE, -1)
        category_id = intent.getIntExtra(MUSIC_LIST_CATEGORY, -1)
        when (pid) {
            1 -> {
                item_id = category_id
                recyclerview.layoutManager = GridLayoutManager(this, 3)
                recyclerview.addItemDecoration(GridSpacingItemDecoration(3, CommonUtils.dip2px(this, 6f), CommonUtils.dip2px(this, 10f), true))
                musicListMusicAdapter = MusicListMusicAdapter(musicListMusicBeanList)
                recyclerview.adapter = musicListMusicAdapter
            }
            2 -> {
                recyclerview.layoutManager = LinearLayoutManager(this)
                musicListMusicIanAdapter = MusicListMusicIanAdapter(musicListMusicIanBeanList)
                recyclerview.adapter = musicListMusicIanAdapter
            }
            else -> {
                recyclerview.layoutManager = GridLayoutManager(this, 3)
                recyclerview.addItemDecoration(GridSpacingItemDecoration(3, CommonUtils.dip2px(this, 6f), CommonUtils.dip2px(this, 0f), true))
                musicListSongAdapter = MusicListSongAdapter(musicListSongBeanList)
                recyclerview.adapter = musicListSongAdapter
            }
        }
        setTitleText(StringUtils.isEmpty(title))
        setTitleTextColor(ContextCompat.getColor(this, R.color.color_17_text))
        setRightButtonImageView(ContextCompat.getDrawable(this, R.drawable.icon_music_sort))
        setOnLeftClick(View.OnClickListener { finish() })
        RxView.clicks(navigationBar.getRightButton())
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val mQuickCustomPopup = SimpleCustomPop(this@MusicListActivity)
                    mQuickCustomPopup.anchorView(navigationBar.getRightButton())
                            .offset(-10f, 0f)
                            .gravity(Gravity.BOTTOM)
                            .showAnim(null)
                            .dismissAnim(null)
                            .dimEnabled(true)
                            .show()
                }
        label_recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        musicListTagAdapter = MusicListTagAdapter(musicListTabBeanList, this)
        label_recyclerview.adapter = musicListTagAdapter
        musicListTagAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            for (i in musicListTabBeanList.indices) {
                if (i == position) {
                    if (musicListTabBeanList[i].isCheck) {
                        tag = 0
                        musicListTabBeanList[i].isCheck = false
                    } else {
                        tag = musicListTabBeanList[position].id
                        musicListTabBeanList[i].isCheck = true
                    }
                } else {
                    musicListTabBeanList[i].isCheck = false
                }
            }
            selectTab = "id-desc"
            page = 1
            netData(selectTab)
            musicListTagAdapter?.notifyDataSetChanged()
        }

        interfaceRefreshLoadMore = InterfaceRefreshLoadMore(superSwipeRefreshLayout, this, object : InterfaceRefreshLoadMore.RefreshLoadMoreCallback {
            override fun onRefresh() {
                page = 1
                netData(selectTab)
            }

            override fun onLoadMore() {
                page++
                netData(selectTab)
            }

            override fun onPushDistance(distance: Int) {

            }

            override fun onPullDistance(distance: Int) {

            }

        })
    }

    override fun initData() {
        netData(selectTab)
        if (pid == 2) {
            label_recyclerview.visibility = View.GONE
            v_line.visibility = View.GONE
            setTitleText("音乐人列表")
            return
        }
        NetWork.getPublicTag(pid, item_id, tagPage, this, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val jObj = JSON.parseObject(resultData)
                val data = jObj.getJSONArray("data")
                val musicTabList = JSON.parseArray(data.toString(), MusicListTabBean::class.java)
                if (tagPage == 1) {
                    musicListTabBeanList.clear()
                }
                if (null != musicTabList && musicTabList.size > 0) {
                    musicListTabBeanList.addAll(musicTabList)
                }
                musicListTagAdapter?.notifyDataSetChanged()
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    private fun netData(order: String?) {
        when (pid) {
            1 -> netPublicMusic(tag, order)
            2 -> netPublicMusicIan(tag, order)
            else -> netPublicMusicSong(tag, order)
        }
    }

    private fun netPublicMusicIan(tag: Int, order: String?) {
//        order?.let {
//            NetWork.getPublicMusicIan(tag, page, this, it, object : NetWork.TokenCallBack {
//                override fun doNext(resultData: String, headers: Headers?) {
//                    try {
//                        val musicListMusicIanBean = MusicListMusicIanBean()
//                        val jObj = org.json.JSONObject(resultData)
//                        val dataArray = if (jObj.optJSONArray("data") == null) JSONArray() else jObj.optJSONArray("data")
//                        musicListMusicIanBean.data = parserData(dataArray)
//                        notifyAdapter()
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//
//                }
//
//                override fun doError(msg: String) {
//                    if (page > 1) {
//                        page--
//                    }
//                }
//
//                override fun doResult() {
//                    interfaceRefreshLoadMore.setStopRefreshing()
//                }
//            })
//        }
    }

    private fun parserData(dataArray: JSONArray): List<MusicListMusicIanBean.DataBean> {
        if (page == 1) {
            musicListMusicIanBeanList.clear()
        }
        for (i in 0 until dataArray.length()) {
            val jObj = dataArray.optJSONObject(i)
            val dataBean = MusicListMusicIanBean.DataBean()
            dataBean.check = false
            dataBean.sex = jObj.optInt("sex")
            dataBean.id = jObj.optInt("id")
            dataBean.nickname = jObj.optString("nickname")
            dataBean.head_link = jObj.optString("head_link")
            dataBean.is_music = jObj.optInt("is_musice")
            val musicObject = if (jObj.optJSONObject("music") == null) org.json.JSONObject() else jObj.optJSONObject("music")
            val musicBean = MusicListMusicIanBean.DataBean.MusicBean()
            if (TextUtils.isEmpty(musicObject.toString()) || TextUtils.equals("{}", musicObject.toString())) {
                musicBean.title = ""
                musicBean.item_id = -1
            } else {
                musicBean.title = musicObject.optString("title")
                musicBean.item_id = musicObject.optInt("item_id")
            }
            dataBean.music = musicBean
            musicListMusicIanBeanList.add(dataBean)
        }
        return musicListMusicIanBeanList
    }

    private fun netPublicMusicSong(tag: Int, order: String?) {
        order?.let {
            NetWork.getPublicMusicSong(tag, page, this, it, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    val obj = JSON.parseObject(resultData)
                    val jObj = obj.getJSONObject("data")
                    val array = jObj.getJSONArray("data_list")
                    val musicListSongBeen = JSON.parseArray(array.toString(), MusicListSongBean::class.java)
                    if (page == 1) {
                        musicListSongBeanList.clear()
                    }
                    musicListSongBeanList.addAll(musicListSongBeen)
                    notifyAdapter()
                    interfaceRefreshLoadMore.setStopRefreshing()
                }

                override fun doError(msg: String) {
                    if (page > 1) {
                        page--
                    }
                    interfaceRefreshLoadMore.setStopRefreshing()
                }

                override fun doResult() {

                }
            })
        }
    }

    private fun netPublicMusic(tag: Int, order: String?) {
        order?.let {
//            NetWork.getPublicMusic(tag, category_id, page, it, this, object : NetWork.TokenCallBack {
//                override fun doNext(resultData: String, headers: Headers?) {
//                    interfaceRefreshLoadMore.setStopRefreshing()
//                    val obj = JSON.parseObject(resultData)
//                    val jObj = obj.getJSONObject("data")
//                    val array = jObj.getJSONArray("data_list")
//                    val musicListBean = JSON.parseArray(array.toString(), MusicListMusicBean::class.java)
//                    if (page == 1) {
//                        musicListMusicBeanList.clear()
//                    }
//                    musicListMusicBeanList.addAll(musicListBean)
//                    notifyAdapter()
//                }
//
//                override fun doError(msg: String) {
//                    if (page > 1) {
//                        page--
//                    }
//                    interfaceRefreshLoadMore.setStopRefreshing()
//                }
//
//                override fun doResult() {
//
//                }
//            })
        }
    }

    private fun notifyAdapter() {
        musicListMusicAdapter?.notifyDataSetChanged()
        musicListSongAdapter?.notifyDataSetChanged()
        musicListMusicIanAdapter?.notifyDataSetChanged()
    }

    override fun initEvent() {
        mRefreshIsPlayerEvent = RxBus.getDefault().toObservable(RefreshIsPlayerEvent::class.java)
                .subscribeWith(object : RxBusSubscriber<RefreshIsPlayerEvent>() {
                    @Throws(Exception::class)
                    override fun onEvent(refreshIsPlayerEvent: RefreshIsPlayerEvent) {
                        when (refreshIsPlayerEvent.type) {
                            "music/list/music" -> Observable.fromArray<List<MusicListMusicBean>>(musicListMusicBeanList)
                                    .flatMap { musicListMusicBeen -> Observable.fromIterable(musicListMusicBeen) }
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(object : Observer<MusicListMusicBean> {
                                        override fun onSubscribe(@NonNull d: Disposable) {
                                        }

                                        override fun onNext(@NonNull musicListMusicBean: MusicListMusicBean) {
                                            musicListMusicBean.check = false
                                        }

                                        override fun onError(@NonNull e: Throwable) {

                                        }

                                        override fun onComplete() {
                                            if (musicListMusicBeanList.size > refreshIsPlayerEvent.position) {
                                                musicListMusicBeanList[refreshIsPlayerEvent.position].check = refreshIsPlayerEvent.player
                                                notifyAdapter()
                                            }
                                        }
                                    })
                            "music/list/song" -> Observable.fromArray<List<MusicListSongBean>>(musicListSongBeanList)
                                    .flatMap { musicListMusicBeen -> Observable.fromIterable(musicListMusicBeen) }
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(object : Observer<MusicListSongBean> {
                                        override fun onSubscribe(@NonNull d: Disposable) {

                                        }

                                        override fun onNext(@NonNull musicListSongBean: MusicListSongBean) {
                                            musicListSongBean.check = false
                                        }

                                        override fun onError(@NonNull e: Throwable) {

                                        }

                                        override fun onComplete() {
                                            if (musicListSongBeanList.size > refreshIsPlayerEvent.position) {
                                                musicListSongBeanList[refreshIsPlayerEvent.position].check = refreshIsPlayerEvent.player
                                                notifyAdapter()
                                            }
                                        }
                                    })
                            "music/list/musician" -> Observable.fromArray<List<MusicListMusicIanBean.DataBean>>(musicListMusicIanBeanList)
                                    .flatMap<MusicListMusicIanBean.DataBean> { musicListMusicBeen -> Observable.fromIterable<MusicListMusicIanBean.DataBean>(musicListMusicBeen) }
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(object : Observer<MusicListMusicIanBean.DataBean> {
                                        override fun onSubscribe(@NonNull d: Disposable) {

                                        }

                                        override fun onNext(@NonNull dataBean: MusicListMusicIanBean.DataBean) {
                                            dataBean.check = false
                                        }

                                        override fun onError(@NonNull e: Throwable) {

                                        }

                                        override fun onComplete() {
                                            if (musicListMusicIanBeanList.size > refreshIsPlayerEvent.position) {
                                                musicListMusicIanBeanList[refreshIsPlayerEvent.position].check = refreshIsPlayerEvent.player
                                                notifyAdapter()
                                            }
                                        }
                                    })
                        }
                    }
                })
        RxBus.getDefault().add(mRefreshIsPlayerEvent)
        mPlayerMusicRefreshData = RxBus.getDefault().toObservable(PlayerMusicRefreshDataEvent::class.java)
                .subscribeWith(object : RxBusSubscriber<PlayerMusicRefreshDataEvent>() {
                    @Throws(Exception::class)
                    override fun onEvent(playerMusicRefreshDataEvent: PlayerMusicRefreshDataEvent) {
                        notifyAdapter()
                    }
                })
        RxBus.getDefault().add(mPlayerMusicRefreshData)
    }

    internal inner class SimpleCustomPop(context: Context) : BasePopup<SimpleCustomPop>(context) {
        @BindView(R.id.tv_default)
        lateinit var tv_default: TextView
        @BindView(R.id.tv_play_at_most)
        lateinit var tv_play_at_most: TextView
        @BindView(R.id.tv_newest)
        lateinit var tv_newest: TextView
        @BindView(R.id.tv_at_most_comment)
        lateinit var tv_at_most_comment: TextView

        init {
            setCanceledOnTouchOutside(true)
        }

        override fun onCreatePopupView(): View {
            val inflate = View.inflate(mContext, R.layout.pop_layout_sort, null)
            ButterKnife.bind(this, inflate)
            tv_default.setTextColor(ContextCompat.getColor(this@MusicListActivity, R.color.color_17_text))
            tv_play_at_most.setTextColor(ContextCompat.getColor(this@MusicListActivity, R.color.color_17_text))
            tv_newest.setTextColor(ContextCompat.getColor(this@MusicListActivity, R.color.color_17_text))
            tv_at_most_comment.setTextColor(ContextCompat.getColor(this@MusicListActivity, R.color.color_17_text))
            //默认，人气最多，音乐最多，最新动态
            if (pid == 2) {
                tv_play_at_most.text = "人气最多"
                tv_newest.text = "音乐最多"
                tv_at_most_comment.text = "最新动态"
                when (selectTab) {
                    "id-desc" -> tv_default.setTextColor(ContextCompat.getColor(this@MusicListActivity, R.color.base_red))
                    "ips_num-desc" -> tv_play_at_most.setTextColor(ContextCompat.getColor(this@MusicListActivity, R.color.base_red))
                    "publish-desc" -> tv_newest.setTextColor(ContextCompat.getColor(this@MusicListActivity, R.color.base_red))
                    "dynamic_time-desc" -> tv_at_most_comment.setTextColor(ContextCompat.getColor(this@MusicListActivity, R.color.base_red))
                }
            } else {
                when (selectTab) {
                    "id-desc" -> tv_default.setTextColor(ContextCompat.getColor(this@MusicListActivity, R.color.base_red))
                    "counts-desc" -> tv_play_at_most.setTextColor(ContextCompat.getColor(this@MusicListActivity, R.color.base_red))
                    "create_time-desc" -> tv_newest.setTextColor(ContextCompat.getColor(this@MusicListActivity, R.color.base_red))
                    "comment-desc" -> tv_at_most_comment.setTextColor(ContextCompat.getColor(this@MusicListActivity, R.color.base_red))
                }
            }

            return inflate
        }

        override fun setUiBeforShow() {
            RxView.clicks(tv_default)
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe {
                        selectTab = "id-desc"
                        page = 1
                        netData(selectTab)
                        dismiss()
                    }
            RxView.clicks(tv_play_at_most)
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe {
                        selectTab = if (pid == 2) {
                            "ips_num-desc"
                        } else {
                            "counts-desc"
                        }
                        page = 1
                        netData(selectTab)
                        dismiss()
                    }
            RxView.clicks(tv_newest)
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe {
                        selectTab = if (pid == 2) {
                            "publish-desc"
                        } else {
                            "create_time-desc"
                        }
                        page = 1
                        netData(selectTab)
                        dismiss()
                    }
            RxView.clicks(tv_at_most_comment)
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe {
                        selectTab = if (pid == 2) {
                            "dynamic_time-desc"
                        } else {
                            "comment-desc"
                        }
                        page = 1
                        netData(selectTab)
                        dismiss()
                    }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        interfaceRefreshLoadMore.resetRefreshView()
        RxBus.getDefault().remove(mRefreshIsPlayerEvent)
        RxBus.getDefault().add(mPlayerMusicRefreshData)
    }

    companion object {
        const val MUSIC_GET_TYPE = "get_type"
        const val MUSIC_LIST_TITLE = "title"
        const val MUSIC_LIST_CATEGORY = "category_id"
        private val TAG = "MusicListActivity"
    }
}
