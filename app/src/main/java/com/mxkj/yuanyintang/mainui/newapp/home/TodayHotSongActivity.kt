package com.mxkj.yuanyintang.mainui.newapp.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.INVISIBLE
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.database.DBManager
import com.mxkj.yuanyintang.database.updatafilesql.TasksManager
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.mainui.home.adapter.SongSheetMusicListAdapter
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.message.activity.MyCollectionSongListActivity
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.musicplayer.play_control.PlayList
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.layoutmanager.FullyLinearLayoutManager
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.threadpool.ThreadPoolManager
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import kotlinx.android.synthetic.main.activity_today_hot_song.*
import okhttp3.Headers
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * 新歌速递/今日推荐
 */
class TodayHotSongActivity : StandardUiActivity() {
    private var adapter: SongSheetMusicListAdapter? = null
    var isEdit = false
    var count = 0
    var type = ""
    var titleStr: String = ""
    private var isClickable = false
    var dataList = ArrayList<MusicIndex.ItemInfoListBean.MusicBean>()

    lateinit var dataBeans: MusicIndex.ItemInfoListBean.MusicBean
    private var handler: Handler? = null
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    override fun setLayoutId(): Int {
        StatusBarUtil.baseTransparentStatusBar(this)
        return R.layout.activity_today_hot_song
    }

    override fun initView() {
        handler = Handler()
        hideTitle(true)
        app_bar.addOnOffsetChangedListener({ appBarLayout, verticalOffset ->
            val scrollRangle = appBarLayout.totalScrollRange
            if (verticalOffset == 0) {
                layout_head.alpha = 1.0f
            } else {
                val alpha = (Math.abs(Math.round(1.0f * verticalOffset / scrollRangle) * 10) / 10).toFloat()
                layout_head.alpha = 1.0f - alpha
            }
        })
        iv_back.setOnClickListener {
            backEvent()
        }
        RxView.clicks(tv_edit_more).throttleFirst(1, TimeUnit.SECONDS).subscribe {
                    if (CacheUtils.getBoolean(this@TodayHotSongActivity, Constants.User.IS_LOGIN)) {
                        isEdit = true
                        adapter?.notifyDataSetChanged()
                        checkView()
                    } else {
                        goActivity(LoginRegMainPage::class.java)
                    }
                }

        check_song.setOnTouchListener { view, motionEvent ->
//            check_song.isChecked = !check_song.isChecked
//            CheckAll(check_song.isChecked)
//            true
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                check_song.isChecked = !check_song.isChecked
                CheckAll(check_song.isChecked)
            }
            true

        }
        /**
         * 添加到歌单
         * */
        RxView.clicks(tv_add_song).throttleFirst(1, TimeUnit.SECONDS).subscribe {
                    getMusicID("addSong")
                    isEdit = false
                    checkView()
                    if (type == "new") {
                        titleStr = "新歌速递"
                    } else if (type == "today") {
                        titleStr = "今日推荐"
                    }
                    tv_title.text = titleStr
                }
        /**
         * 添加到播放列表
         * */
        RxView.clicks(tv_add_musicList).throttleFirst(1, TimeUnit.SECONDS).subscribe(Consumer {
                    if (!isClickable) {
                        setSnackBar("你还没有选择音乐", "", R.drawable.icon_good)
                        return@Consumer
                    }
                    Observable.fromArray(dataList).flatMap<MusicIndex.ItemInfoListBean.MusicBean> { dataBeen ->
                        Observable.fromIterable<MusicIndex.ItemInfoListBean.MusicBean>(dataBeen).filter { dataBean ->
                            dataBean.isIscheck
                        }
                    }.subscribe(object : Observer<MusicIndex.ItemInfoListBean.MusicBean> {
                                override fun onSubscribe(@NonNull d: Disposable) {

                                }

                                override fun onNext(@NonNull data: MusicIndex.ItemInfoListBean.MusicBean) {
                                    val dataBean = sheetBeanResultData(data, 0)
                                    PlayList.addToList(this@TodayHotSongActivity, dataBean)
                                }

                                override fun onError(@NonNull e: Throwable) {

                                }

                                override fun onComplete() {
                                    setSnackBar("添加到播放列表成功", "", R.drawable.icon_success)
                                    if (!MediaService.getMediaPlayer().isPlaying) {
                                        val dataBeanList = PlayList.getList(this@TodayHotSongActivity)
                                        if (dataBeanList != null && dataBeanList.size > 0) {
                                            PlayCtrlUtil.startServiceToPlay(this@TodayHotSongActivity, dataBeanList[dataBeanList.size - 1])
                                        }
                                    }
                                    checkView()
                                }
                            })
                    isEdit = false
                    checkView()
                    if (type == "new") {
                        titleStr = "新歌速递"
                    } else if (type == "today") {
                        titleStr = "今日推荐"
                    }
                    tv_title.text = titleStr
                })

        /**
         * 下载
         * */
        RxView.clicks(tv_download_music).throttleFirst(1, TimeUnit.SECONDS).subscribe(Consumer {
                    //getMusicID("download")
                    //isEdit = false
                    //isEdit = true//需要調整，这个下载并没有真真的下载
                    //checkView()
                    if (type == "new") {
                        titleStr = "新歌速递"
                    } else if (type == "today") {
                        titleStr = "今日推荐"
                    }
                    tv_title.text = titleStr
                    if (!isClickable) {
                        setSnackBar("你还没有选择音乐", "", R.drawable.icon_good)
                        return@Consumer
                    }
                    downLoad()

                })
    }

    override fun initData() {
        type = intent.getStringExtra("type")
        if (type == "new") {
            tv_title.text = "新歌速递"
            rl_weather.visibility = INVISIBLE
            today.visibility = View.GONE
            newsong.visibility = View.VISIBLE

            NetWork.getNewRecommend("todayHotNew",this, null, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    jsonParse(resultData, "list")
                }
                override fun doError(msg: String) {
                }

                override fun doResult() {
                }
            })
        } else if (type == "today") {
            tv_title.text = "今日推荐"
            today.visibility = View.VISIBLE
            newsong.visibility = View.GONE
            NetWork.getTodatRecommend("todayHotToday",this, null, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    jsonParse(resultData, "musics")
                }
                override fun doError(msg: String) {
                }
                override fun doResult() {
                }
            })
        }

    }

    private fun jsonParse(resultData: String, musicArrayKey: String) {
        dataList.clear()
        val jObj = JSON.parseObject(resultData)
        val dataObj = jObj.getJSONObject("data")
        val cateImgBean = dataObj.getObject("cate_img", HomeBean.CateImgBean::class.java)
        if (cateImgBean != null) {
            ImageLoader.with(this).getSize(750,348).setUrl(cateImgBean?.imgpic_info).blur(25).scale(ScaleMode.CENTER_CROP).into(imageview)

            tv_day.text = cateImgBean.day.toString()
            mTitle.text = cateImgBean.title
            mContent.text = cateImgBean.content
            mTitle1.text = cateImgBean.title
            mContent1.text = cateImgBean.content

        }
        val musicList = dataObj.getJSONArray(musicArrayKey).toJavaList(MusicIndex.ItemInfoListBean.MusicBean::class.java)
        dataList.addAll(musicList)
        adapter?.setNewData(dataList)
        Log.e("dataList",""+dataList.toString())
        tv_song_list_label.text = "全部播放  (共" + dataList.size + "首)"
    }

    override fun initEvent() {
        type = intent.getStringExtra("type")
        val layoutManager = FullyLinearLayoutManager(this)
        recycler.layoutManager = layoutManager
        adapter = SongSheetMusicListAdapter(dataList, supportFragmentManager, type, isEdit)
        recycler?.adapter = adapter
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var firstVisibleItemPosition = (recyclerView?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (dy < 0 && firstVisibleItemPosition == 0) {
                    app_bar.setExpanded(true, true)
                }
            }
        })
        adapter?.setCheckedSongListener(object : SongSheetMusicListAdapter.ClickCheckedSongListener {
            override fun onChecked(aBoolean: Boolean?, position: Int) {
                if (dataList.size > position) {
                    dataList[position].isIscheck = aBoolean ?: false
                    countMusic()
                }
            }

            override fun onRefreshData() {}
        })
//     批量编辑
        RxView.clicks(tv_edit_more).subscribe {
            if (CacheUtils.getBoolean(this@TodayHotSongActivity, Constants.User.IS_LOGIN)) {
                isEdit = true
                checkView()
            } else {
                goActivity(LoginRegMainPage::class.java)
            }
        }
//        if (MediaService.mediaPlayer != null && MediaService.bean != null && MediaService.mediaPlayer.isPlaying) {
//            img_all_playing.setImageResource(R.drawable.song_detail_play_true)
//        } else {
//            img_all_playing.setImageResource(R.drawable.song_detail_play_false)
//        }
        RxView.clicks(ll_play_all).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            Log.e("dataList",""+dataList.size)
            if (MediaService.mediaPlayer != null && MediaService.bean != null && MediaService.mediaPlayer.isPlaying) {
                sendBroadcast(Intent(MediaService.ACTION_PAUSE))
                img_all_playing.setImageResource(R.drawable.song_detail_play_false)

//                PlayCtrlUtil.playTypeList(this@TodayHotSongActivity, dataList)
//                img_all_playing.setImageResource(R.drawable.song_detail_play_true)
                if (dataList.size > 0) {
                    for (i in 0 until recycler.getChildCount()) {
                        val layout = recycler.getChildAt(i) as LinearLayout// 获得子item的layout
                        val musicNane = layout.findViewById<View>(R.id.tv_music_name) as TextView// 从layout中获得控件,根据其id
                        val nickName = layout.findViewById<View>(R.id.tv_music_nickname) as TextView// 从layout中获得控件,根据其id
                        musicNane.setTextColor(Color.parseColor("#2b2b2b"))
                        nickName.setTextColor(Color.parseColor("#9da2a6"))
                    }
                } else {
                }

            } else if (MediaService.mediaPlayer != null && MediaService.bean != null) {
//                sendBroadcast(Intent(MediaService.ACTION_PAUSE))
//                PlayCtrlUtil.playTypeList(this@TodayHotSongActivity, dataList)
                PlayCtrlUtil.playTypeList(this@TodayHotSongActivity, dataList)
                img_all_playing.setImageResource(R.drawable.song_detail_play_true)

                if (dataList.size > 0) {
                    for (i in 0 until recycler.getChildCount()) {
                        val layout = recycler.getChildAt(0) as LinearLayout// 获得子item的layout
                        val musicNane = layout.findViewById<View>(R.id.tv_music_name) as TextView// 从layout中获得控件,根据其id
                        val nickName = layout.findViewById<View>(R.id.tv_music_nickname) as TextView// 从layout中获得控件,根据其id
                        musicNane.setTextColor(Color.parseColor("#ff6699"))
                        nickName.setTextColor(Color.parseColor("#ff6699"))
                    }
                } else {
                }
            } else {
                PlayCtrlUtil.playTypeList(this@TodayHotSongActivity, dataList)
                img_all_playing.setImageResource(R.drawable.song_detail_play_true)
                dataList[0].isPlaying = true
                adapter?.setNewData(dataList)

                if (dataList.size > 0) {
                    for (i in 0 until recycler.getChildCount()) {
                        val layout = recycler.getChildAt(0) as LinearLayout// 获得子item的layout
                        val musicNane = layout.findViewById<View>(R.id.tv_music_name) as TextView// 从layout中获得控件,根据其id
                        val nickName = layout.findViewById<View>(R.id.tv_music_nickname) as TextView// 从layout中获得控件,根据其id
                        musicNane.setTextColor(Color.parseColor("#ff6699"))
                        nickName.setTextColor(Color.parseColor("#ff6699"))
                    }
                } else {
                }

            }
        }
    }

    private fun countMusic() {
        Observable.fromArray<List<MusicIndex.ItemInfoListBean.MusicBean>>(dataList).flatMap<MusicIndex.ItemInfoListBean.MusicBean> { dataBeen ->
            Observable.fromIterable<MusicIndex.ItemInfoListBean.MusicBean>(dataBeen).filter { dataBean ->
                dataBean.isIscheck
            }
        }.subscribe(object : Observer<MusicIndex.ItemInfoListBean.MusicBean> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        count = 0

                    }

                    override fun onNext(@NonNull dataBean: MusicIndex.ItemInfoListBean.MusicBean) {
                        count++
                    }

                    override fun onError(@NonNull e: Throwable) {

                    }

                    override fun onComplete() {
                        tv_title.text = "已选(" + count + "/" + dataList.size + ")"
                        checkSelectClickView(count)
                        if (dataList.size == 0) {
                            check_song.isChecked = false
                            return
                        }
                        if (count == dataList.size) {
                            check_song.setChecked(false)
                        } else {
                            check_song.setChecked(true)
                        }
                        check_song.isChecked = count == dataList.size
                    }
                })
    }

    private fun checkView() {
        if (isEdit) {
            app_bar.setExpanded(false, true)
            ll_play_all.visibility = View.GONE
            tv_edit_more.visibility = View.GONE
            check_song.visibility = View.VISIBLE
            layout_footer.visibility = View.VISIBLE
            tv_add_musicList.visibility = View.GONE
            if (adapter != null) {
                tv_title.text = "已选(" + count + "/" + adapter?.data?.size + ")"
            }
        } else {
            app_bar.setExpanded(true, true)
            ll_play_all.visibility = View.VISIBLE
            tv_edit_more.visibility = View.VISIBLE
            check_song.visibility = View.GONE
            layout_footer.visibility = View.GONE
//            将所有未选择
            CheckAll(false)
        }
        adapter?.isEdit = isEdit
        adapter?.notifyDataSetChanged()
    }

    private fun CheckAll(check: Boolean) {
        Observable.fromArray<List<MusicIndex.ItemInfoListBean.MusicBean>>(dataList).flatMap<MusicIndex.ItemInfoListBean.MusicBean> { dataBeen -> Observable.fromIterable<MusicIndex.ItemInfoListBean.MusicBean>(dataBeen) }.subscribe(object : Observer<MusicIndex.ItemInfoListBean.MusicBean> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                    }

                    override fun onNext(@NonNull dataBean: MusicIndex.ItemInfoListBean.MusicBean) {
                        dataBean.isIscheck = check
                    }

                    override fun onError(@NonNull e: Throwable) {

                    }

                    override fun onComplete() {
                        adapter?.notifyDataSetChanged()
                        countMusic()
                    }
                })
    }

    override fun onBackPressed() {
        backEvent()
    }

    private fun backEvent() {
        if (isEdit) {
            isEdit = false
            checkView()
        } else {
            finish()
        }
        if (type == "new") {
            titleStr = "新歌速递"
        } else if (type == "today") {
            titleStr = "今日推荐"
        }
        tv_title.text = titleStr
    }

    private fun sheetBeanResultData(data: MusicIndex.ItemInfoListBean.MusicBean, song_id: Int): MusicInfo.DataBean {
        val dataBean = MusicInfo.DataBean()
        dataBean.video = data.video
        if (data.video_info != null) {
            val videoInfoBean = MusicInfo.DataBean.VideoInfoBean()
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

    private fun getMusicID(types: String) {
        if (!isClickable) {
            setSnackBar("你还没有选择音乐", "", R.drawable.icon_good)
            return
        }
        Observable.fromArray(dataList).flatMap({ dataBeen ->
            Observable.fromIterable<MusicIndex.ItemInfoListBean.MusicBean>(dataBeen).filter({ dataBean -> dataBean.isIscheck }).map({ dataBean -> dataBean.id.toString() + "" })
        }).reduce { s, s2 -> "$s,$s2" }.subscribe { s ->
                    when (types) {
                        "addSong" -> {
                            val bundle = Bundle()
                            bundle.putString(MyCollectionSongListActivity.MUSIC_ID, s)
                            bundle.putString(MyCollectionSongListActivity.VIEW_TYPE, "collectionAddSong")
                            bundle.putBoolean(MyCollectionSongListActivity.IS_MULTI_SELECT, false)
                            goActivity(MyCollectionSongListActivity::class.java, bundle)
                        }
                    }//  deleFromSongSheet(s);
                }
    }

    private fun checkSelectClickView(count: Int) {
        if (count > 0) {
            tv_add_song.setTextColor(ContextCompat.getColor(this, R.color.color_17_text))
            tv_add_musicList.setTextColor(ContextCompat.getColor(this, R.color.color_17_text))
            tv_download_music.setTextColor(ContextCompat.getColor(this, R.color.color_17_text))
            isClickable = true
        } else {
            tv_add_song.setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text))
            tv_add_musicList.setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text))
            tv_download_music.setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text))
            isClickable = true
        }
    }

    /**
     * （后台规定歌曲的下载链接必须用每首歌id去请求接口获得。。。。。。。）
     */
    private val runnable = Runnable {
        val kbps = CacheUtils.getString(this@TodayHotSongActivity, Constants.User.MUSIC_KBP, "128")
        NetWork.getMusicDown(this@TodayHotSongActivity, dataBeans.id.toString() + "", if (TextUtils.equals("128", kbps)) "1" else "2", object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                var data: String? = null
                try {
                    val `object` = JSONObject(resultData)
                    data = `object`.optString("data")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                if (!TextUtils.isEmpty(data) && dataBeans.imgpic_info != null) {
                    val musicBean = MusicBean()
                    musicBean.ext = "." + StringUtils.parseSuffix(data)
                    musicBean.setCollection(1)
                    val link = dataBeans.imgpic_info.link
                    val imgpicInfoBean = MusicBean.ImgpicInfoBean()
                    imgpicInfoBean.link = link
                    musicBean.imgpic_info = imgpicInfoBean

                    musicBean.setMusic_name(dataBeans.title)
                    musicBean.setMusician_name(dataBeans.nickname)
                    musicBean.ext = "." + StringUtils.parseSuffix(data)
                    musicBean.setMusic_id(dataBeans.id.toString() + "")
                    musicBean.setUid(dataBeans.uid)
                    musicBean.setSong_id(dataBeans.song_id)
                    TasksManager.getImpl().downLoadFile(musicBean, data, this@TodayHotSongActivity)
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    private fun downLoad(){
        Observable.fromArray(dataList).flatMap<MusicIndex.ItemInfoListBean.MusicBean> { dataBeen ->
            Observable.fromIterable<MusicIndex.ItemInfoListBean.MusicBean>(dataBeen).filter { dataBean ->
                dataBean.isIscheck
            }
        }
                .subscribe(object : Observer<MusicIndex.ItemInfoListBean.MusicBean> {
                    override fun onSubscribe(@NonNull d: Disposable) {

                    }

                    override fun onNext(@NonNull dataBean: MusicIndex.ItemInfoListBean.MusicBean) {
                        dataBeans = dataBean
                        handler!!.post(runnable)
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
    }
}
