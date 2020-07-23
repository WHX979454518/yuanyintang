package com.mxkj.yuanyintang.mainui.newapp.home

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import kotlinx.android.synthetic.main.activity_music_type_new.*
import com.alibaba.fastjson.JSON
import com.jakewharton.rxbinding2.view.RxView
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.database.updatafilesql.TasksManager
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.message.activity.MyCollectionSongListActivity
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.musicplayer.play_control.PlayList
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.musicplayer.service.MediaService.ACTION_PAUSE
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.video.MusicClassification
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.music_type_head.*
import kotlinx.android.synthetic.main.music_type_head_item.view.*
import kotlinx.android.synthetic.main.uc_navigationbar.*
import okhttp3.Headers
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class MusicTypeActivity : StandardUiActivity(), AppBarLayout.OnOffsetChangedListener {
//    private var adapter: MusicFragmentAdapter? = null
    private var newadapter: MusicTypeActivityAdapter?=null
    private var isEdit: Boolean = false
    private var isPlaying: Boolean = false
    var count = 0
//    var dataList = ArrayList<MusicIndex.ItemInfoListBean.MusicBean>()
    var newdataList = ArrayList<MusicClassification.DataBean.DataListBean>()

    var selectedText: String = ""
    private var isClickable = false
    internal var verticalOffset: Int = 0

    private var params: HttpParams = HttpParams()
    private var page: Int = 1
    //    swipe_refresh
    internal lateinit var interfaceRefreshLoadMore: InterfaceRefreshLoadMore
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    override fun setLayoutId(): Int {
        return R.layout.activity_music_type_new
    }

    override fun initView() {
        setTitleText("音乐分类")
        app_bar.addOnOffsetChangedListener({ appBarLayout, verticalOffset ->
            tv_select_type.visibility = View.GONE
            val scrollRangle = appBarLayout.totalScrollRange
            if (Math.abs(verticalOffset) == scrollRangle && selectedText.isNotEmpty()) {
                tv_select_type.visibility = View.VISIBLE
            } else {
                tv_select_type.visibility = View.GONE
            }
        })
        leftButton.setOnClickListener {
            if (isEdit) {
                isEdit = false
                checkView()
            } else {
                finish()
            }
            setTitleText("音乐分类")
        }
        tv_close.setOnClickListener {
            if (isEdit) {
                isEdit = false
                checkView()
            } else {
                finish()
            }
            setTitleText("音乐分类")
        }
        RxView.clicks(tv_edit_more)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (CacheUtils.getBoolean(this@MusicTypeActivity, Constants.User.IS_LOGIN)) {
                        isEdit = true
                        newadapter?.notifyDataSetChanged()
                        checkView()
                    } else {
                        goActivity(LoginRegMainPage::class.java)
                    }
                }
        check_song.setOnCheckedChangeListener { _, p1 -> CheckAll(p1) }
        RxView.clicks(ll_play_all).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            Log.e("dataList",""+newdataList.size)
            if (isPlaying) {
                img_all_playing.setImageResource(R.drawable.song_detail_play_false)
                sendBroadcast(Intent(ACTION_PAUSE))
                isPlaying = false
            } else {
//                PlayCtrlUtil.playTypeList(this@MusicTypeActivity, newdataList)
                img_all_playing.setImageResource(R.drawable.song_detail_play_true)
                isPlaying = true
            }
        }

        /**
         * 添加到歌单
         * */
        RxView.clicks(tv_add_song)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe { getMusicID("addSong") }
        /**
         * 添加到播放列表
         * */
        RxView.clicks(tv_add_musicList)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(Consumer {
                    if (!isClickable) {
                        setSnackBar("你还没有选择音乐", "", R.drawable.icon_good)
                        return@Consumer
                    }
                    Observable.fromArray(newdataList)
                            .flatMap<MusicClassification.DataBean.DataListBean> { dataBeen ->
                                Observable.fromIterable<MusicClassification.DataBean.DataListBean>(dataBeen).filter { dataBean ->
                                    dataBean.isIscheck
                                }
                            }
                            .subscribe(object : Observer<MusicClassification.DataBean.DataListBean> {
                                override fun onSubscribe(@NonNull d: Disposable) {

                                }

                                override fun onNext(@NonNull data: MusicClassification.DataBean.DataListBean) {
                                    val dataBean = sheetBeanResultData(data, 0)
                                    PlayList.addToList(this@MusicTypeActivity, dataBean)
                                }

                                override fun onError(@NonNull e: Throwable) {

                                }

                                override fun onComplete() {
                                    setSnackBar("添加到播放列表成功", "", R.drawable.icon_success)
                                    if (!MediaService.getMediaPlayer().isPlaying) {
                                        val dataBeanList = PlayList.getList(this@MusicTypeActivity)
                                        PlayCtrlUtil.startServiceToPlay(this@MusicTypeActivity, dataBeanList[dataBeanList.size - 1])
                                    }
                                    checkView()
                                }
                            })
                })

        /**
         * 下载
         * */
        RxView.clicks(tv_download_music)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(Consumer {
                    if (!isClickable) {
                        setSnackBar("你还没有选择音乐", "", R.drawable.icon_good)
                        return@Consumer
                    }
                    Observable.fromArray(newdataList)
                            .flatMap<MusicClassification.DataBean.DataListBean> { dataBeen ->
                                Observable.fromIterable<MusicClassification.DataBean.DataListBean>(dataBeen).filter { dataBean ->
                                    dataBean.isIscheck
                                }
                            }.subscribe(object : Observer<MusicClassification.DataBean.DataListBean> {
                                override fun onSubscribe(@NonNull d: Disposable) {

                                }

                                override fun onNext(@NonNull dataBean: MusicClassification.DataBean.DataListBean) {
//                                    if (!dataBean.single_selection) {
//                                        return
//                                    }
                                    val kbps = CacheUtils.getString(this@MusicTypeActivity, Constants.User.MUSIC_KBP, "128")
                                    NetWork.getMusicDown(this@MusicTypeActivity, dataBean.id.toString() + "", if (TextUtils.equals("128", kbps)) "1" else "2", object : NetWork.TokenCallBack {

                                        override fun doNext(resultData: String, headers: Headers?) {
                                            var data: String? = null
                                            try {
                                                val `object` = JSONObject(resultData)
                                                data = `object`.optString("data")
                                            } catch (e: JSONException) {
                                                e.printStackTrace()
                                            }

                                            if (!TextUtils.isEmpty(data) && dataBean.imgpic_info != null) {
                                                val musicBean = MusicBean()
                                                musicBean.ext = "." + StringUtils.parseSuffix(data)
                                                musicBean.setMusic_name(musicBean.getMusic_name())
                                                musicBean.setCollection(1)
                                                musicBean.setMusic_name(dataBean.title)
                                                val link = dataBean.imgpic_info.link
                                                val imgpicInfoBean = MusicBean.ImgpicInfoBean()
                                                imgpicInfoBean.link = link
                                                musicBean.imgpic_info = imgpicInfoBean
                                                musicBean.setMusician_name(dataBean.nickname)
                                                musicBean.ext = "." + StringUtils.parseSuffix(data)
                                                musicBean.setMusic_id(dataBean.id.toString() + "")
                                                musicBean.setUid(dataBean.uid)
                                                musicBean.setSong_id(dataBean.song_id)
                                                TasksManager.getImpl().downLoadFile(musicBean, data!!, this@MusicTypeActivity)
                                            }
                                        }

                                        override fun doError(msg: String) {

                                        }

                                        override fun doResult() {

                                        }
                                    })
                                }

                                override fun onError(@NonNull e: Throwable) {

                                }

                                override fun onComplete() {
                                    setSnackBar("添加到下载列表成功", "", R.drawable.icon_success)
                                    if (null != check_song) {
                                        check_song.isChecked = false
                                        isEdit = check_song.isChecked
                                        checkView()
                                    }
                                }
                            })
                    getMusicID("download")
                })
    }

    override fun initData() {
        getMusicList()
        NetWork.getOrderType(this, "music", object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                llTypeHead.removeAllViews()
                var orderTypeBean = JSON.parseObject(resultData, OrderTypeBean::class.java)
                for (whereBean in orderTypeBean.data.where) {
                    val view = LayoutInflater.from(this@MusicTypeActivity).inflate(R.layout.music_type_head_item, null)
                    val typeCkView = view.type_make as MusicTypeCkView
                    typeCkView.setDatas(true, whereBean, R.layout.type_item, object : MusicTypeCkView.OnCheckedListener {
                        override fun onChecked(id: OrderTypeBean.DataBean.WhereBean.ListBean?, position: Int) {
                            if (typeCkView.datas != null && typeCkView.datas!!.isNotEmpty()) {
                                whereBean.list = typeCkView.datas
                            }
                            setSelectedInfo(orderTypeBean)
                        }

                    })
                    llTypeHead.addView(view)
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {
            }

        })
    }

    private fun getMusicList() {
        if (params == null) {
            params = HttpParams()
        }
        params?.put("p", page.toString())
        NetWork.getPublicMusic(params, this, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                interfaceRefreshLoadMore.setStopRefreshing()
                val obj = JSON.parseObject(resultData)
                val jObj = obj.getJSONObject("data")
                val array = jObj.getJSONArray("data_list")
                val musicListBean = JSON.parseArray(array.toString(), MusicIndex.ItemInfoListBean.MusicBean::class.java)





                val musicclassificationListBean = JSON.parseArray(array.toString(), MusicClassification.DataBean.DataListBean::class.java)
                if (musicListBean != null && musicListBean.size > 0) {
                    tv_no_data.visibility = GONE
                    if (page == 1) {
                        newdataList.clear()
                    }
                    newdataList.addAll(musicclassificationListBean)
                    newadapter?.setNewData(newdataList)
                } else if (page == 1) {
                    newdataList.clear()
                    newadapter?.setNewData(newdataList)
                    tv_no_data.visibility = VISIBLE
                }





//                if (musicListBean != null && musicListBean.size > 0) {
//                    tv_no_data.visibility = GONE
//                    if (page == 1) {
//                        dataList.clear()
//                    }
//                    dataList.addAll(musicListBean)
//                    adapter?.setNewData(dataList)
//                } else if (page == 1) {
//                    dataList.clear()
//                    adapter?.setNewData(dataList)
//                    tv_no_data.visibility = VISIBLE
//                }
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

    //配置已选中的信息，以及请求数据
    private fun setSelectedInfo(orderTypeBean: OrderTypeBean) {
        newdataList.clear()
        selectedText = ""
        params = HttpParams()
        for (whereBean in orderTypeBean.data.where) {
            for (listBean in whereBean.list) {
                if (listBean.isChecked) {
                    params?.put(whereBean.field, listBean.id.toString())
                    selectedText += (listBean.title + "·")
                    selectedText.replace(selectedText[selectedText.length - 1].toString(), "")
                    tv_select_type.text = selectedText
                } else {
                    newdataList.clear()
                }
            }
        }
        getMusicList()

    }

    override fun initEvent() {
        val layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager
//        adapter = MusicFragmentAdapter(dataList, supportFragmentManager, "musicclassification",page,"", isEdit)
        newadapter = MusicTypeActivityAdapter(newdataList, supportFragmentManager, "musicclassification", page, "", isEdit)
        recycler?.adapter = newadapter
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var firstVisibleItemPosition = (recyclerView?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (dy < 0 && firstVisibleItemPosition == 0) {
                    app_bar.setExpanded(true, true)
                }
            }
        })

        newadapter?.setCheckedSongListener(object : MusicTypeActivityAdapter.ClickCheckedSongListener {
            override fun onChecked(aBoolean: Boolean?, position: Int) {
                if (newdataList.size > position) {
                    newdataList[position].isIscheck = aBoolean ?: false
//                    adapter?.notifyItemChanged(position)
                    countMusic()
                }
            }

            override fun onRefreshData() {}
        })
//     批量编辑
        RxView.clicks(tv_edit_more).subscribe {
            if (CacheUtils.getBoolean(this@MusicTypeActivity, Constants.User.IS_LOGIN)) {
                isEdit = true
                checkView()
            } else {
                goActivity(LoginRegMainPage::class.java)
            }
        }
        interfaceRefreshLoadMore = InterfaceRefreshLoadMore(swipe_refresh, this, object : InterfaceRefreshLoadMore.RefreshLoadMoreCallback {
            override fun onRefresh() {
                page = 1
                getMusicList()
            }

            override fun onLoadMore() {
                page++
//                initEvent()
                getMusicList()
            }

            override fun onPushDistance(distance: Int) {

            }

            override fun onPullDistance(distance: Int) {

            }
        })
    }

    private fun countMusic() {
        Observable.fromArray<List<MusicClassification.DataBean.DataListBean>>(newdataList)
                .flatMap<MusicClassification.DataBean.DataListBean> { dataBeen ->
                    Observable.fromIterable<MusicClassification.DataBean.DataListBean>(dataBeen).filter { dataBean ->
                        dataBean.isIscheck
                    }
                }
                .subscribe(object : Observer<MusicClassification.DataBean.DataListBean> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        count = 0

                    }

                    override fun onNext(@NonNull dataBean: MusicClassification.DataBean.DataListBean) {
                        count++
                    }

                    override fun onError(@NonNull e: Throwable) {

                    }

                    override fun onComplete() {
                        setTitleText("已选(" + count + "/" + newdataList.size + ")")
                        title_tv.setText("已选择" + count + "项")
                        checkSelectClickView(count)
                        if (newdataList.size == 0) {
                            check_song.isChecked = false
                            return
                        }
                        check_song.isChecked = count == newdataList.size
                    }
                })
    }

    private fun checkView() {
        if (isEdit) {
            app_bar.setExpanded(false, true)
            tv_close.visibility = View.VISIBLE
            ll_play_all.visibility = View.GONE
            tv_edit_more.visibility = View.GONE
            check_song.visibility = View.VISIBLE
            tv_close.visibility = View.VISIBLE
            title_tv.visibility = View.VISIBLE
            hideLeftButton()
            hideRightButton()
            hideTitle(true)
            title_ll.visibility = View.VISIBLE
            layout_footer.visibility = View.VISIBLE
            tv_add_musicList.visibility = View.GONE

            if (newadapter != null) {
                setTitleText("已选(" + count + "/" + newadapter?.data?.size + ")")
                title_tv.setText("已选择" + count + "项")
            }
        } else {
            app_bar.setExpanded(true, true)
            ll_play_all.visibility = View.VISIBLE
            tv_edit_more.visibility = View.VISIBLE
            check_song.visibility = View.GONE
            tv_close.visibility = View.GONE
            title_tv.visibility = View.GONE

            showLeftButton()
            hideTitle(false)

            title_ll.visibility = View.GONE
            layout_footer.visibility = View.GONE
            CheckAll(false)
        }
        newadapter?.isEdit = isEdit
        newadapter?.notifyDataSetChanged()
    }

    private fun CheckAll(check: Boolean) {
        Observable.fromArray<List<MusicClassification.DataBean.DataListBean>>(newdataList)
                .flatMap<MusicClassification.DataBean.DataListBean> { dataBeen ->
                    Observable.fromIterable<MusicClassification.DataBean.DataListBean>(dataBeen) }
                .subscribe(object : Observer<MusicClassification.DataBean.DataListBean> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                    }

                    override fun onNext(@NonNull dataBean: MusicClassification.DataBean.DataListBean) {
                        dataBean.isIscheck = check
                    }

                    override fun onError(@NonNull e: Throwable) {

                    }

                    override fun onComplete() {
                        newadapter?.notifyDataSetChanged()
                        countMusic()
                    }
                })
    }

    override fun onBackPressed() {
        if (isEdit) {
            isEdit = false
            checkView()
        } else {
            finish()
        }
        setTitleText("音乐分类")

    }

    private fun sheetBeanResultData(data: MusicClassification.DataBean.DataListBean, song_id: Int): MusicInfo.DataBean {
        val dataBean = MusicInfo.DataBean()
        dataBean.video = data.video
        if (data.video_info != null) {
            val videoInfoBean = MusicClassification.DataBean.DataListBean.VideoInfoBean()
            videoInfoBean.link = data.video_info.link
            data.video_info = videoInfoBean
        }
        dataBean.id = data.id
        dataBean.song_id = song_id
        dataBean.uid = data.uid
        dataBean.title = data.title
        dataBean.collection = data.collection
        dataBean.imgpic = data.imgpic
        dataBean.lyrics = ""
        dataBean.nickname = data.nickname
        dataBean.playtime = data.playtime
        dataBean.up_time = 0
        try {
            dataBean.original = data.imgpic_info.link
        } catch (e: RuntimeException) {
        }

        dataBean.counts = data.counts
        dataBean.comment = data.comment
        return dataBean
    }

    private fun getMusicID(type: String) {
        if (!isClickable) {
            setSnackBar("你还没有选择音乐", "", R.drawable.icon_good)
            return
        }
        Observable.fromArray(newdataList)
                .flatMap({ dataBeen ->
                    Observable.fromIterable<MusicClassification.DataBean.DataListBean>(dataBeen)
                            .filter({ dataBean -> dataBean.isIscheck!! })
                            .map({ dataBean -> dataBean.id.toString() + "" })
                })
                .reduce { s, s2 -> "$s,$s2" }
                .subscribe { s ->
                    when (type) {
                        "cancel" -> {
                        }
                        "addSong" -> {
                            val bundle = Bundle()
                            bundle.putString(MyCollectionSongListActivity.MUSIC_ID, s)
                            bundle.putString(MyCollectionSongListActivity.VIEW_TYPE, "collectionAddSong")
                            bundle.putBoolean(MyCollectionSongListActivity.IS_MULTI_SELECT, false)
                            goActivity(MyCollectionSongListActivity::class.java, bundle)
                        }
                    }//                                deleFromSongSheet(s);
                }
    }

    private fun checkSelectClickView(count: Int) {
        isClickable = if (count > 0) {
            tv_add_song.setTextColor(ContextCompat.getColor(this, R.color.color_17_text))
            tv_add_musicList.setTextColor(ContextCompat.getColor(this, R.color.color_17_text))
            tv_download_music.setTextColor(ContextCompat.getColor(this, R.color.color_17_text))
            true
        } else {
            tv_add_song.setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text))
            tv_add_musicList.setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text))
            tv_download_music.setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text))
            false
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        swipe_refresh.isEnabled = verticalOffset == 0
    }


}
