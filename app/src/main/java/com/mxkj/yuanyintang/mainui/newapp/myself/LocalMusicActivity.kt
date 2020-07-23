package com.mxkj.yuanyintang.mainui.newapp.myself

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.alibaba.fastjson.JSON
import com.flyco.dialog.widget.popup.BasePopup
import com.hyphenate.chat.EMClient
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.database.DBManager
import com.mxkj.yuanyintang.database.updatafilesql.TasksManager
import com.mxkj.yuanyintang.database.updatafilesql.UpDataFileBean
import com.mxkj.yuanyintang.database.updatafilesql.UpDataFileDao
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.mainui.home.adapter.SongSheetMusicListAdapter
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.home.fragment.HomeFragment.Companion.SEARCH_WORDS
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.message.activity.MessageSettingActivity
import com.mxkj.yuanyintang.mainui.message.activity.MyCollectionSongListActivity
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.BeforeScanActivity
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.Song
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.SongDao
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean
import com.mxkj.yuanyintang.mainui.search.SearchResultActivity
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.musicplayer.play_control.PlayList
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.layoutmanager.FullyLinearLayoutManager
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.frag_myself_head.view.*
import kotlinx.android.synthetic.main.local_music_list_activity.*
import okhttp3.Headers
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class LocalMusicActivity : StandardUiActivity() {
    private var searchWord: String? = null
    private var adapter: LocalListAdapter? = null
    var isEdit = false
    var isPlaying = false
    var count = 0
    var type = ""
    var titleStr: String = ""
    private var isClickable = false
    private var entities: ArrayList<Song> = ArrayList()
    var dataList = ArrayList<MusicIndex.ItemInfoListBean.MusicBean>()
    var searchList = ArrayList<MusicIndex.ItemInfoListBean.MusicBean>()
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    override fun setLayoutId(): Int {
        return R.layout.local_music_list_activity
    }

    override fun initView() {
        setTitleText("本地音乐")

        et_search.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (NetConnectedUtils.isNetConnected(applicationContext)) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                            .hideSoftInputFromWindow(this@LocalMusicActivity.getCurrentFocus()!!
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
                    if (!TextUtils.isEmpty(et_search.getText().toString().trim({ it <= ' ' }))) {
                       setSearch()
                    }
                }
            }
            false
        })
        RxView.clicks(delect_search)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    et_search.setText("")
                }


        val drawable = resources.getDrawable(R.drawable.more_charts_member)
        setRightButton("", drawable, View.OnClickListener {
            val mQuickCustomPopup = SimpleCustomPop(this@LocalMusicActivity)
            mQuickCustomPopup.anchorView(navigationBar.getRightButton())
                    .offset(0f, 0f)
                    .gravity(Gravity.BOTTOM)
                    .showAnim(null)
                    .dismissAnim(null)
                    .dimEnabled(true)
                    .show()
        })

        RxView.clicks(tv_edit_more)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (CacheUtils.getBoolean(this@LocalMusicActivity, Constants.User.IS_LOGIN)) {
                        isEdit = true
                        adapter?.notifyDataSetChanged()
                        checkView()
                    } else {
                        goActivity(LoginRegMainPage::class.java)
                    }
                }
        check_song.setOnCheckedChangeListener { _, p1 -> CheckAll(p1) }


        RxView.clicks(rl_search).subscribe {
            searchWord = et_search.text.toString()
            if (!TextUtils.isEmpty(searchWord)) {
                setSearch()
            }
        }

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
                    Observable.fromArray(dataList)
                            .flatMap<MusicIndex.ItemInfoListBean.MusicBean> { dataBeen ->
                                Observable.fromIterable<MusicIndex.ItemInfoListBean.MusicBean>(dataBeen).filter { dataBean ->
                                    dataBean.isIscheck
                                }
                            }
                            .subscribe(object : Observer<MusicIndex.ItemInfoListBean.MusicBean> {
                                override fun onSubscribe(@NonNull d: Disposable) {

                                }

                                override fun onNext(@NonNull data: MusicIndex.ItemInfoListBean.MusicBean) {
                                    val dataBean = sheetBeanResultData(data, 0)
                                    PlayList.addToList(this@LocalMusicActivity, dataBean)
                                }

                                override fun onError(@NonNull e: Throwable) {

                                }

                                override fun onComplete() {
                                    setSnackBar("添加到播放列表成功", "", R.drawable.icon_success)
                                    if (!MediaService.getMediaPlayer().isPlaying) {
                                        val dataBeanList = PlayList.getList(this@LocalMusicActivity)
                                        if (dataBeanList != null && dataBeanList.size > 0) {
                                            PlayCtrlUtil.startServiceToPlay(this@LocalMusicActivity, dataBeanList[dataBeanList.size - 1])
                                        }
                                    }
                                    checkView()
                                }
                            })
                })
    }

    var songDao: SongDao? = null
    override fun initData() {
        Observable.create(ObservableOnSubscribe<Any> { e ->
            songDao = SongDao(this@LocalMusicActivity)
            entities = songDao?.listHelper() as ArrayList<Song>
            e.onNext(1)
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (entities != null && entities.size > 0) {
                entities.forEach {
                    var musicBean = MusicIndex.ItemInfoListBean.MusicBean()
                    musicBean.title = it.songName
                    musicBean.nickname = it.singer
                    var videoInfo = MusicInfo.DataBean.VideoInfoBean()
                    videoInfo.link = it.path
                    musicBean.video_info = videoInfo
                    musicBean.setLocalMusic(true)
                    musicBean.id = it.id
                    dataList.add(musicBean)
                }
                tv_song_list_label.text = "播放全部 (共" + entities.size + "首)"
                adapter?.setNewData(dataList)
                if(entities.size==0){
                    tv_no_data.visibility = View.VISIBLE
                }
            } else {
                val intent = Intent(this@LocalMusicActivity, BeforeScanActivity::class.java)
                intent.putExtra("isList",true)
                intent.putExtra("biaoshi","1")
                startActivity(intent)
                finish()
            }
        }
    }

    override fun initEvent() {
        val layoutManager = FullyLinearLayoutManager(this)
        recycler.layoutManager = layoutManager
        adapter = LocalListAdapter(dataList, supportFragmentManager, "", isEdit)
        recycler?.adapter = adapter
        adapter?.setCheckedSongListener(object : LocalListAdapter.ClickCheckedSongListener {
            override fun onChecked(aBoolean: Boolean?, position: Int) {
                if (dataList.size > position) {
                    dataList[position].isIscheck = aBoolean ?: false
//                    adapter?.notifyItemChanged(position)
                    countMusic()
                }
            }

            override fun onRefreshData() {}
        })
//     批量编辑
        RxView.clicks(tv_edit_more).subscribe {
            if (CacheUtils.getBoolean(this@LocalMusicActivity, Constants.User.IS_LOGIN)) {
                isEdit = true
                checkView()
            } else {
                goActivity(LoginRegMainPage::class.java)
            }
        }
        RxView.clicks(ll_play_all).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            if (dataList != null) {
                if (isPlaying) {
                    sendBroadcast(Intent(MediaService.ACTION_PAUSE))
                    img_all_playing.setImageResource(R.drawable.song_detail_play_false)
                } else {

                    dataList[0].isPlaying=true
                    adapter?.setNewData(dataList)
                    MediaService.bean = JSON.parseObject(JSON.toJSONString(dataList[0]), MusicInfo.DataBean::class.java)
                    sendBroadcast(Intent(MediaService.ACTION_PAUSE))
                    img_all_playing.setImageResource(R.drawable.song_detail_play_true)
                }
                Observable.fromArray(dataList).flatMap { dataBeen -> Observable.fromIterable(dataBeen) }.subscribeOn(Schedulers.io()).subscribe(object : Observer<MusicIndex.ItemInfoListBean.MusicBean> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(data: MusicIndex.ItemInfoListBean.MusicBean) {
                        val dataBean = MusicInfo.DataBean()
                        dataBean.video = data.video
                        if (data.video_info != null) {
                            val videoInfoBean = MusicInfo.DataBean.VideoInfoBean()
                            videoInfoBean.link = data.video_info.link
                            dataBean.video_info = videoInfoBean
                        }
                        dataBean.id = data.id
                        dataBean.song_id = 0
                        dataBean.uid = data.uid
                        dataBean.title = data.title
                        val imgpicInfoBean =MusicInfo.DataBean.ImgpicInfoBean()//HomeBean.ImgpicInfoBean(); //
                        try {
                            imgpicInfoBean.link = data.imgpic_info.link
                        } catch (e: RuntimeException) {
                        }

                        dataBean.imgpic_info = imgpicInfoBean
                        dataBean.comment = data.comment
                        dataBean.imgpic_info = imgpicInfoBean
                        dataBean.nickname = StringUtils.isEmpty(data.nickname)
                        PlayList.addToList(this@LocalMusicActivity, dataBean)
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {

                    }
                })
            }
        }
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                if (editable?.length == 0) {
                    adapter?.setNewData(dataList)
                    sousuo_tv_no_data.visibility = View.GONE
                    playall.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    private fun setSearch() {
        searchWord = et_search.getText().toString()
        searchList.clear()
        Observable.fromArray(entities)
                .flatMap { songs -> Observable.fromIterable(songs) }
                .filter { song -> if (TextUtils.isEmpty(et_search.text)) false else song.songName.contains(et_search.text.toString()) || song.singer.contains(et_search.text.toString()) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Song> {
                    override fun onSubscribe(@io.reactivex.annotations.NonNull d: Disposable) {
                        searchList.clear()
                    }

                    override fun onNext(@io.reactivex.annotations.NonNull data: Song) {
                        val dataBean = MusicIndex.ItemInfoListBean.MusicBean()
                        if (data.path != null) {
                            val videoInfoBean = MusicInfo.DataBean.VideoInfoBean()
                            videoInfoBean.link = data.path
                            dataBean.video_info = videoInfoBean
                        }
                        dataBean.id = data.id
                        dataBean.title = data.songName
                        dataBean.nickname = data.singer
                        dataBean.playtime = ""
                        dataBean.up_time = 0
                        searchList.add(dataBean)
                    }

                    override fun onError(@io.reactivex.annotations.NonNull e: Throwable) {
                        adapter?.setNewData(searchList)
                        if(searchList.size<=0){
//                            playall.visibility = View.GONE
                            sousuo_tv_no_data.visibility = View.VISIBLE
                            sousuo_tv_no_data.setText("沒有找到与"+"“"+et_search.text.toString()+"”"+"相关的歌曲")
                        }else{
                            playall.visibility = View.VISIBLE
                            sousuo_tv_no_data.visibility = View.GONE
                        }
                    }

                    override fun onComplete() {
                        adapter?.setNewData(searchList)
                    }
                })
    }

    private fun countMusic() {
        Observable.fromArray<List<MusicIndex.ItemInfoListBean.MusicBean>>(dataList)
                .flatMap<MusicIndex.ItemInfoListBean.MusicBean> { dataBeen ->
                    Observable.fromIterable<MusicIndex.ItemInfoListBean.MusicBean>(dataBeen).filter { dataBean ->
                        dataBean.isIscheck
                    }
                }
                .subscribe(object : Observer<MusicIndex.ItemInfoListBean.MusicBean> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        count = 0

                    }

                    override fun onNext(@NonNull dataBean: MusicIndex.ItemInfoListBean.MusicBean) {
                        count++
                    }

                    override fun onError(@NonNull e: Throwable) {

                    }

                    override fun onComplete() {
                        setTitleText("已选(" + count + "/" + dataList.size + ")")
                        checkSelectClickView(count)
                        if (dataList.size == 0) {
                            check_song.isChecked = false
                            return
                        }
                        check_song.isChecked = count == dataList.size
                    }
                })
    }

    private fun checkView() {
        if (isEdit) {
            ll_play_all.visibility = View.GONE
            tv_edit_more.visibility = View.GONE
            check_song.visibility = View.VISIBLE
            layout_footer.visibility = View.VISIBLE
            if (adapter != null) {
                setTitleText("已选(" + count + "/" + dataList.size + ")")
            }
        } else {
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
        Observable.fromArray<List<MusicIndex.ItemInfoListBean.MusicBean>>(dataList)
                .flatMap<MusicIndex.ItemInfoListBean.MusicBean> { dataBeen -> Observable.fromIterable<MusicIndex.ItemInfoListBean.MusicBean>(dataBeen) }
                .subscribe(object : Observer<MusicIndex.ItemInfoListBean.MusicBean> {
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
        if (isEdit) {
            isEdit = false
            checkView()
        } else {
            finish()
        }
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

    internal inner class SimpleCustomPop(context: Context) : BasePopup<SimpleCustomPop>(context) {
        var toScana: TextView? = null

        init {
            setCanceledOnTouchOutside(true)
        }

        override fun onCreatePopupView(): View {
            val inflate = View.inflate(mContext, R.layout.pop_to_scan, null)
            toScana = inflate.findViewById(R.id.toScana)
            return inflate
        }

        override fun setUiBeforShow() {
            RxView.clicks(toScana!!)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        val songDao = SongDao(this@LocalMusicActivity)
                        songDao.deleteAllHelper()
                        startActivity(Intent(this@LocalMusicActivity, BeforeScanActivity::class.java))
                    }
        }
    }
}
