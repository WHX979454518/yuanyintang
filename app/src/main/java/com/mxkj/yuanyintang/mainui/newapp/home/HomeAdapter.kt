package com.mxkj.yuanyintang.mainui.newapp.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.mainui.home.adapter.SongSheetRecommendListAdapter
import com.mxkj.yuanyintang.mainui.home.bean.HomeIndex
import com.mxkj.yuanyintang.mainui.home.data.Constant.NewHomeType.*
import com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsHomeActivity

import com.mxkj.yuanyintang.mainui.home.utils.GridSpacingItemDecoration
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.myself.activity.NearbyPeopleActivity
import com.mxkj.yuanyintang.mainui.newapp.OriginalMusicListAdapter
import com.mxkj.yuanyintang.mainui.newapp.home.adapter.HomeMusicianAdapter
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew
import com.mxkj.yuanyintang.mainui.web.CommonWebview
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.photopicker.widget.NoScrollRecyclerView
import com.mxkj.yuanyintang.widget.bannerLayout.HomeTopBannerLayout
import com.mxkj.yuanyintang.widget.bannerLayout.RecyclingUnlimitedPagerAdapter
import com.mxkj.yuanyintang.widget.bannerLayout.util.ViewHolder
import java.util.concurrent.TimeUnit
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.View
import com.alibaba.fastjson.JSON
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.im.widget.chatrow.EaseChatRowVoicePlayClickListener.isPlaying
import com.mxkj.yuanyintang.mainui.home.activity.LikesMusicActivity
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.musicplayer.activity.PlayerActivity
import com.mxkj.yuanyintang.musicplayer.service.MediaService.*
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.uiutils.Toast
import com.tbruyelle.rxpermissions2.RxPermissions
import com.umeng.analytics.MobclickAgent
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import java.io.File

/**
 * 首页数据
 */
class HomeAdapter(data: List<HomeBean>, var fragmentManager: FragmentManager) : BaseMultiItemQuickAdapter<HomeBean, BaseViewHolder>(data) {
    private var songSheetRecommendListAdapter: SongSheetRecommendListAdapter? = null
    private var guessLikeAdapter: SongSheetRecommendListAdapter? = null
    private var homeMusicianAdapter: HomeMusicianAdapter? = null
    private var originalAdapter: OriginalMusicListAdapter? = null
    private var coverAdapter: OriginalMusicListAdapter? = null

    init {
        addItemType(BANNERTYPE, R.layout.new_home_banner)
        addItemType(FOURTYPE, R.layout.new_home_fourtype)
        addItemType(SysMsgType, R.layout.new_home_sys_msg)
        addItemType(AlbumBeanTYPE, R.layout.new_home_album)
        addItemType(RecomendBeanTYPE, R.layout.new_home_recomend)
        addItemType(OriginalBeanTYPE, R.layout.new_home_original)
        addItemType(CoverBeanTYPE, R.layout.new_home_cover)
        addItemType(MusicianBeanTYPE, R.layout.new_home_musician)
        addItemType(MusicianBeanTYPENew, R.layout.new_home_newmusician)
        addItemType(GuessBeanTYPE, R.layout.new_home_guess)
    }

    override fun convert(helper: BaseViewHolder, homeBean: HomeBean) {
        val itemType = homeBean.itemType

        when (itemType) {
            -1 -> return
//            TODO 系统消息
            SysMsgType -> sysMsg(helper, homeBean)
            //banner
            BANNERTYPE -> bannerData(helper, homeBean)
            //附近、分类、今日推荐
            FOURTYPE -> fourtypeData(helper, homeBean)
            //推荐歌单
            AlbumBeanTYPE -> albumData(helper, homeBean)
            //源小伊
            RecomendBeanTYPE -> recomendData(helper, homeBean)
            //原创
            OriginalBeanTYPE -> oriMusicData(helper, homeBean)
            //翻唱
            CoverBeanTYPE -> coverMusicData(helper, homeBean)
            //熱門音樂人
            MusicianBeanTYPE -> musicianData(helper, homeBean)
            //最新入住
            MusicianBeanTYPENew -> musicianData(helper, homeBean)
            //
            GuessBeanTYPE -> guessMusicData(helper, homeBean)
        }
    }

    /*系统消息*/
    private fun sysMsg(helper: BaseViewHolder, homeBean: HomeBean) {
        val sysMsgBean = homeBean.sysMsgBean
        if (sysMsgBean.system_affiche.isEmpty()) return
        for (systemAfficheBean in sysMsgBean.system_affiche) {
            helper.setText(R.id.sysMsg, systemAfficheBean.text)
        }
        helper.setOnClickListener(R.id.sysMsg, {
            val systemMsgBean = sysMsgBean.system_affiche[0]
            msgClickGo(systemMsgBean.type, systemMsgBean.url, systemMsgBean.id.toString() + "")
        })
    }

    /**
     * 猜你喜欢
     */
    private fun guessMusicData(helper: BaseViewHolder, homeBean: HomeBean) {
        var page = 1
        val guessRecycler = helper.getView<NoScrollRecyclerView>(R.id.guessRecycler)
        guessRecycler.isNestedScrollingEnabled = false
        guessRecycler.layoutManager = GridLayoutManager(mContext, 3)
        if (guessRecycler.adapter == null) {
            guessRecycler.addItemDecoration(GridSpacingItemDecoration(3, CommonUtils.dip2px(mContext, 10f), CommonUtils.dip2px(mContext, 10f), false))
            guessLikeAdapter = SongSheetRecommendListAdapter(homeBean.guessBean.guessList)
            guessRecycler.adapter = guessLikeAdapter
        } else {
            guessLikeAdapter?.setNewData(homeBean.guessBean.guessList)
        }
        helper.setOnClickListener(R.id.tv_more_sheet) {
            goActivity(AllSongSheetActivity::class.java)
        }
        helper.setOnClickListener(R.id.llCgData) {
            page++
            NetWork.getPublicMusicSong(0, page, mContext, "guess", object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    val array = JSON.parseObject(resultData).getJSONObject("data").getJSONArray("song")
                    val musicListSongBeen = JSON.parseArray(array.toString(), MusicIndex.ItemInfoListBean.RecommendBean::class.java)
                    guessLikeAdapter?.setNewData(musicListSongBeen)
                }

                override fun doError(msg: String) {
                }

                override fun doResult() {
                }
            })
        }

    }

    /**
     * 音乐人
     *热门音乐人-recommend_musician
     * 最新入驻-newest_musician
     */
    private fun musicianData(helper: BaseViewHolder, homeBean: HomeBean) {
        val type = homeBean.musicianBean.type
        when (type) {
            "recommend_musician" -> {
                helper.setText(R.id.tv_reco_song, "热门音乐人")
            }
            "newest_musician" -> {
                helper.setText(R.id.tv_reco_song, "最新入驻 ")
            }
        }
        val musicianRecyclerview = helper.getView<NoScrollRecyclerView>(R.id.hot_musician_recyclerview)
        musicianRecyclerview.isNestedScrollingEnabled = false
        musicianRecyclerview.layoutManager = GridLayoutManager(mContext, 3)
        if (musicianRecyclerview.adapter == null) {
            musicianRecyclerview.addItemDecoration(GridSpacingItemDecoration(3, CommonUtils.dip2px(mContext, 10f), CommonUtils.dip2px(mContext, 10f), false))
            if(null==homeBean.musicianBean.musician){

            }else{
                homeMusicianAdapter = HomeMusicianAdapter(homeBean.musicianBean.musician)
                musicianRecyclerview.adapter = homeMusicianAdapter
            }

        } else {
            homeMusicianAdapter?.setNewData(homeBean.musicianBean.musician)
        }

        helper.setOnClickListener(R.id.tv_more_musician) {
            MobclickAgent.onEvent(mContext, "hot_musician_more");
            goActivity(AllMusicianActivity::class.java)
        }
    }

    /**
     * 原创
     */
    private fun oriMusicData(helper: BaseViewHolder, homeBean: HomeBean) {
        var page = 1
        var typeMusicListBean = homeBean.typeOriListBean
        val type_music = typeMusicListBean.type_music
        if (type_music.size > typeMusicListBean.index) {
            val musicBean = type_music[typeMusicListBean.index]
            val title = musicBean.title
            val music = musicBean.music
            helper.setText(R.id.tv_title, title)
//            Glide.with(mContext).load(musicBean.imgpic_info?.link).placeholder(R.drawable.nothing_no_txt).error(R.drawable.nothing_no_txt).into(helper.getView(R.id.iv_type))
            ImageLoader.with(mContext).getSize(150,150).setUrl(musicBean.imgpic_info).placeHolder(R.drawable.nothing_no_txt).error(R.drawable.nothing_no_txt).into(helper.getView(R.id.iv_type));
            val original_recyclerview = helper.getView<NoScrollRecyclerView>(R.id.original_recyclerview)
            original_recyclerview.isNestedScrollingEnabled = false
            original_recyclerview.layoutManager = LinearLayoutManager(mContext)
            if (original_recyclerview.adapter == null) {
                val myRecyclerDetorration = MyRecyclerDetorration(mContext, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(mContext, 5f), R.color.white, false)
                original_recyclerview.addItemDecoration(myRecyclerDetorration)
                originalAdapter = OriginalMusicListAdapter(music, (mContext as HomeActivity).supportFragmentManager)
                original_recyclerview.adapter = originalAdapter
            } else {
                originalAdapter?.setNewData(music)
            }
        }
        helper.setOnClickListener(R.id.moreMusic, {

            MobclickAgent.onEvent(mContext, "original_more");

            goActivity(MusicTypeActivity::class.java)
        })

        helper.setOnClickListener(R.id.llCgData, {
            MobclickAgent.onEvent(mContext, "original_chg");
            page++
            var httpParams = HttpParams("music_type", "1")
            httpParams.put("row", "6")
            httpParams.put("p", page)
            NetWork.getPublicMusic(httpParams, mContext, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    val obj = JSON.parseObject(resultData)
                    val jObj = obj.getJSONObject("data")
                    val array = jObj.getJSONArray("data_list")
                    val musicListBean = JSON.parseArray(array.toString(), HomeBean.TypeMusicListBean.TypeMusicBean.MusicBean::class.java)
                    homeBean.typeOriListBean.type_music[typeMusicListBean.index].music = musicListBean
                    originalAdapter?.setNewData(musicListBean)
                }

                override fun doError(msg: String) {
                }

                override fun doResult() {
                }

            })
        })

        originalAdapter?.setOnItemClickListener { _, _, position ->
            MobclickAgent.onEvent(mContext, "original_play");
            coverAdapter?.resetPlayStatus()
            Observable.create(ObservableOnSubscribe<Any> { e ->
                if (bean != null && bean?.id == originalAdapter!!.data[position].id) {
                    if (MediaService.mediaPlayer != null && !MediaService.mediaPlayer.isPlaying) {
                        originalAdapter!!.data[position].counts++
                    }
                    if (MediaService.mediaPlayer != null) {
                        val playing = MediaService.mediaPlayer.isPlaying
                        originalAdapter!!.data[position].isPlaying = !playing
                        mContext.sendBroadcast(Intent(ACTION_PAUSE))//不发送广播，不让他暂停
                        originalAdapter!!.data[position].isPlaying = true
                        val intent = Intent(mContext, PlayerActivity::class.java)
                        mContext.startActivity(intent)

                    } else {
                        originalAdapter!!.data[position].isPlaying = true
                        if (originalAdapter!!.data[position] != null && originalAdapter!!.data[position].video_info != null && originalAdapter!!.data[position].video_info.link != null) {
                            val file = File(originalAdapter!!.data[position].video_info.link!!)
                            if (file.exists()) {
                                bean = JSON.parseObject(JSON.toJSONString(originalAdapter!!.data[position]), MusicInfo.DataBean::class.java)
                                PlayCtrlUtil.startServiceToPlay(mContext, bean)
                            } else {
                                PlayCtrlUtil.play(mContext, originalAdapter!!.data[position].id, originalAdapter!!.data[position].song_id)
                            }
                        }
                    }
                } else {
                    for (i in 0 until originalAdapter!!.data.size) {
                        if (i == position) {
                            originalAdapter!!.data[i].isPlaying = true
                            bean = null
                        } else {
                            originalAdapter!!.data[i].isPlaying = false
                        }
                    }
                    if (originalAdapter!!.data[position] != null && originalAdapter!!.data[position].video_info != null && originalAdapter!!.data[position].video_info.link != null) {
                        val file = File(originalAdapter!!.data[position].video_info.link!!)
                        if (file.exists()) {
                            bean = JSON.parseObject(JSON.toJSONString(originalAdapter!!.data[position]), MusicInfo.DataBean::class.java)
                            PlayCtrlUtil.startServiceToPlay(mContext, bean)
                        } else {
                            PlayCtrlUtil.play(mContext, originalAdapter!!.data[position].id, originalAdapter!!.data[position].song_id)
                        }
                    }

                }
                e.onNext(1)
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                originalAdapter?.setNewData(originalAdapter!!.data)
            }
        }
    }

    /**
     * 翻唱
     */
    private fun coverMusicData(helper: BaseViewHolder, homeBean: HomeBean) {
        var page = 1
        var typeMusicListBean = homeBean.typeCoverListBean
        val type_music = typeMusicListBean.type_music
        if (type_music.size > typeMusicListBean.index) {
            val musicBean = type_music[typeMusicListBean.index]
            val title = musicBean.title
            val music = musicBean.music
            helper.setText(R.id.tv_title, title)
//            Glide.with(mContext).load(musicBean.imgpic_info?.link).placeholder(R.drawable.nothing_no_txt).error(R.drawable.nothing_no_txt).into(helper.getView(R.id.iv_type))
            ImageLoader.with(mContext).getSize(150,150).setUrl(musicBean.imgpic_info).placeHolder(R.drawable.nothing_no_txt).error(R.drawable.nothing_no_txt).into(helper.getView(R.id.iv_type));

            val original_recyclerview = helper.getView<NoScrollRecyclerView>(R.id.original_recyclerview)
            original_recyclerview.isNestedScrollingEnabled = false
            original_recyclerview.layoutManager = LinearLayoutManager(mContext)
            if (original_recyclerview.adapter == null) {
                val myRecyclerDetorration = MyRecyclerDetorration(mContext, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(mContext, 5f), R.color.white, false)
                original_recyclerview.addItemDecoration(myRecyclerDetorration)
                coverAdapter = OriginalMusicListAdapter(music, fragmentManager)
                original_recyclerview.adapter = coverAdapter
            } else {
                coverAdapter?.setNewData(music)
            }
            coverAdapter?.setOnItemClickListener { _, _, position ->
                MobclickAgent.onEvent(mContext, "cover_play");

                coverAdapter?.resetPlayStatus()
                Observable.create(ObservableOnSubscribe<Any> { e ->
                    coverAdapter!!.data[position].counts++
                    if (bean != null && bean?.id == coverAdapter!!.data[position].id) {
                        if (MediaService.mediaPlayer != null) {
                            val playing = MediaService.mediaPlayer.isPlaying
                            coverAdapter!!.data[position].isPlaying = !playing
//                            mContext.sendBroadcast(Intent(ACTION_PAUSE))//不发送广播，不让他暂停

                            val intent = Intent(mContext, PlayerActivity::class.java)
                            mContext.startActivity(intent)
                        } else {
                            coverAdapter!!.data[position].isPlaying = true
                            if (coverAdapter!!.data[position] != null && coverAdapter!!.data[position].video_info != null && coverAdapter!!.data[position].video_info.link != null) {
                                val file = File(coverAdapter!!.data[position].video_info.link!!)
                                if (file.exists()) {
                                    bean = JSON.parseObject(JSON.toJSONString(coverAdapter!!.data[position]), MusicInfo.DataBean::class.java)
                                    PlayCtrlUtil.startServiceToPlay(mContext, bean)
                                } else {
                                    PlayCtrlUtil.play(mContext, coverAdapter!!.data[position].id, coverAdapter!!.data[position].song_id)
                                }
                            }
                        }
                    } else {
                        for (i in 0 until coverAdapter!!.data.size) {
                            if (i == position) {
                                coverAdapter!!.data[i].isPlaying = true
                                bean = null
                            } else {
                                coverAdapter!!.data[i].isPlaying = false
                            }
                        }
                        if (coverAdapter!!.data[position] != null && coverAdapter!!.data[position].video_info != null && coverAdapter!!.data[position].video_info.link != null) {
                            val file = File(coverAdapter!!.data[position].video_info.link!!)
                            if (file.exists()) {
                                bean = JSON.parseObject(JSON.toJSONString(coverAdapter!!.data[position]), MusicInfo.DataBean::class.java)
                                PlayCtrlUtil.startServiceToPlay(mContext, bean)
                            } else {
                                PlayCtrlUtil.play(mContext, coverAdapter!!.data[position].id, coverAdapter!!.data[position].song_id)
                            }
                        }

                    }
                    e.onNext(1)
                }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                    coverAdapter?.setNewData(coverAdapter!!.data)
                }
            }
        }
        helper.setOnClickListener(R.id.moreMusic, {
            MobclickAgent.onEvent(mContext, "cover_more");

            goActivity(MusicTypeActivity::class.java)
        })

        helper.setOnClickListener(R.id.llCgData, {
            MobclickAgent.onEvent(mContext, "cover_chg");

            var httpParams = HttpParams("music_type", "0")
            httpParams.put("row", "6")
            httpParams.put("p", page++)
            NetWork.getPublicMusic(httpParams, mContext, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    val obj = JSON.parseObject(resultData)
                    val jObj = obj.getJSONObject("data")
                    val array = jObj.getJSONArray("data_list")
                    val musicListBean = JSON.parseArray(array.toString(), HomeBean.TypeMusicListBean.TypeMusicBean.MusicBean::class.java)
                    if (homeBean.typeCoverListBean.type_music.size > typeMusicListBean.index) {
                        homeBean.typeCoverListBean.type_music[typeMusicListBean.index].music = musicListBean
                        coverAdapter?.setNewData(musicListBean)
                    }
                }

                override fun doError(msg: String) {
                }

                override fun doResult() {
                }

            })
        })
    }

    /**
     *源小伊
     *
     */
    private fun recomendData(helper: BaseViewHolder, homeBean: HomeBean) {
        val recomendBean = homeBean.recomendBean
        val yxy_notice = recomendBean.yxy_notice
        val home_ad = recomendBean.home_ad
        val index = recomendBean.index

        if (yxy_notice.size > index) {
            helper.setText(R.id.tv_yxy, yxy_notice[index].title)
            //nnd，到底取图片还是不取
//            ImageLoader.with(mContext).getSize(60,60).url(yxy_notice[index].imgpic_link).into(helper.getView(R.id.iv_yxy))
//            Log.e("iiiiiii",""+yxy_notice[index].imgpic_link.toString())
//            Log.e("iiiiiii",""+yxy_notice.toString())
        }
        if (home_ad.size > index) {
            val homeAdBean = home_ad[index]
//            Log.e("iiiiiii",home_ad[index].content)
//            Log.e("iiiiiii",home_ad[index].title)
//            Log.e("iiiiiii",home_ad[index].place)
            helper.setText(R.id.tv_ad_content, home_ad[index].title)
            if (homeAdBean.url.isNotEmpty()) {
                when (homeAdBean.alias) {
                    "web" -> {
                        helper.setText(R.id.tv_ad_type, "网页")
                    }
                    "topic" -> {
                        helper.setText(R.id.tv_ad_type, "池塘")
                    }
                    "music" -> {
                        helper.setText(R.id.tv_ad_type, "单曲")
                    }
                    "song" -> {
                        helper.setText(R.id.tv_ad_type, "歌单")
                    }
                    "activity"-> {
                        helper.setText(R.id.tv_ad_type, "网页")
                    }
                }
            }
            ImageLoader.with(mContext).getSize(690, 210).url(home_ad[index].imgpic_link).into(helper.getView(R.id.iv_ad_bck))

            RxView.clicks(helper.getView(R.id.iv_ad_bck))
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {

                        when (index) {
                            0 -> MobclickAgent.onEvent(mContext, "yxy_num_one")
                            1 -> MobclickAgent.onEvent(mContext, "yxy_num_two")
                        }
                        if (homeAdBean.url.isNotEmpty()) {
                            when {
                                homeAdBean.alias == "web" -> {
                                    val url = homeAdBean.url
                                    val intent = Intent(mContext, CommonWebview::class.java)
                                    intent.putExtra("url", url)
                                    intent.putExtra("title", homeAdBean.title)
                                    if (!TextUtils.isEmpty(homeAdBean.content)) {
                                        intent.putExtra("content", homeAdBean.content)
                                    }
                                    if (homeAdBean.imgpic_info != null) {
                                        if (!TextUtils.isEmpty(homeAdBean.imgpic_info.link)) {
                                            intent.putExtra("img", homeAdBean.imgpic_info.link)
                                        }
                                        mContext.startActivity(intent)

                                    }

                                }
                                homeAdBean.alias == "topic" -> {
                                    val url = homeAdBean.url
                                    val intent = Intent(mContext, PondDetialActivityNew::class.java)
                                    val bundle = Bundle()
                                    bundle.putInt(PondDetialActivityNew.PID, Integer.parseInt(url))
                                    intent.putExtras(bundle)
                                    mContext.startActivity(intent)
                                }
                                homeAdBean.alias == "activity" -> {
                                    val url = homeAdBean.url
                                    val intent = Intent(mContext, CommonWebview::class.java)
                                    intent.putExtra("url", url)
                                    intent.putExtra("activity", "activity")
                                    intent.putExtra("title", homeAdBean.title)
                                    intent.putExtra("content", homeAdBean.content)
                                    try {
                                        intent.putExtra("img", homeAdBean.imgpic_info.link)
                                        mContext.startActivity(intent)
                                    } catch (e: RuntimeException) {
                                    }

                                }
                                homeAdBean.alias == "music" -> {
//                                    PlayCtrlUtil.play(mContext, homeAdBean.id, 0)
                                    //单曲的情况下直接跳转到播放器
                                    val intent = Intent(mContext, PlayerActivity::class.java)
//                                    PlayCtrlUtil.play(mContext, originalAdapter!!.data[position].id, originalAdapter!!.data[position].song_id)
                                    PlayCtrlUtil.play(mContext, Integer.parseInt(homeAdBean.url), 0)
                                    mContext.startActivity(intent)
                                }
                                homeAdBean.alias == "musician" -> {
                                    val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
                                    val bundle = Bundle()
                                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, homeAdBean.url + "")
                                    intent.putExtras(bundle)
                                    mContext.startActivity(intent)
                                }
                                homeAdBean.alias == "song" -> {
                                    val intent = Intent(mContext, SongSheetDetailsActivity::class.java)
                                    val bundle = Bundle()
                                    bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, homeAdBean.url + "")
                                    intent.putExtras(bundle)
                                    mContext.startActivity(intent)
                                }
                            }
                        }
                    }
        }
    }

    /**
     * 推荐歌单
     */
    private fun albumData(helper: BaseViewHolder, homeBean: HomeBean) {
        var page = 1
        val song_sheet_recyclerview = helper.getView<NoScrollRecyclerView>(R.id.song_sheet_recyclerview)
        song_sheet_recyclerview.isNestedScrollingEnabled = false
        song_sheet_recyclerview.layoutManager = GridLayoutManager(mContext, 3)
        if (song_sheet_recyclerview.adapter == null) {
            song_sheet_recyclerview.addItemDecoration(GridSpacingItemDecoration(3, CommonUtils.dip2px(mContext, 10f), CommonUtils.dip2px(mContext, 10f), false))
            songSheetRecommendListAdapter = SongSheetRecommendListAdapter(homeBean.albumBean.recommend)
            song_sheet_recyclerview.adapter = songSheetRecommendListAdapter
        } else {
            songSheetRecommendListAdapter?.setNewData(homeBean.albumBean.recommend)
        }
        helper.setOnClickListener(R.id.tv_more_sheet) {
            MobclickAgent.onEvent(mContext, "home_more_album")
            goActivity(AllSongSheetActivity::class.java)
        }
        helper.setOnClickListener(R.id.llCgData) {
            MobclickAgent.onEvent(mContext, "home_chg_album")
            page++
            NetWork.getPublicMusicSong(0, page, mContext, "recommend", object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    val array = JSON.parseObject(resultData).getJSONArray("data")
                    val musicListSongBeen = JSON.parseArray(array.toString(), MusicIndex.ItemInfoListBean.RecommendBean::class.java)
                    musicListSongBeen.add(0, homeBean.albumBean.recommend[0])
                    songSheetRecommendListAdapter?.setNewData(musicListSongBeen)
                }

                override fun doError(msg: String) {
                }

                override fun doResult() {
                }
            })
        }
    }

    /**
     * 附近、分类、今日推荐
     */
    private fun fourtypeData(helper: BaseViewHolder, homeBean: HomeBean) {
        var isPlaying = false
        val typeBean = homeBean.typeBean
        val today_recommends = typeBean?.today_recommends
        val music_category = typeBean?.music_category
        val billboard = typeBean?.billboard
        val nearby = typeBean?.nearby
        ImageLoader.with(mContext).getSize(310, 250).setUrl(today_recommends?.cate_img?.imgpic_info).error(R.mipmap.today_recommends_img)
                .into(helper.getView(R.id.iv_bck))
        ImageLoader.with(mContext).getSize(386, 130).setUrl(music_category?.imgpic_info).error(R.mipmap.music_category_img)
                .into(helper.getView(R.id.iv_bck_type))
        ImageLoader.with(mContext).getSize(186, 106).setUrl(billboard?.imgpic_info).error(R.mipmap.billboard_img).
                into(helper.getView(R.id.iv_bck_chart))
        ImageLoader.with(mContext).getSize(186, 106).setUrl(nearby?.imgpic_info).error(R.mipmap.nearby_img)
                .into(helper.getView(R.id.iv_bck_near))
        helper.setOnClickListener(R.id.rlTodayRecommend, {
            MobclickAgent.onEvent(mContext, "home_today")
            val intent = Intent(mContext, TodayHotSongActivity::class.java)
            intent.putExtra("type", "today")
            mContext.startActivity(intent)
        })
        helper.setOnClickListener(R.id.rl_music_type, {
            MobclickAgent.onEvent(mContext, "home_music_type")
            goActivity(MusicTypeActivity::class.java)
        })
        helper.setOnClickListener(R.id.rlChart, {
            goActivity(ChartsHomeActivity::class.java)
        })
        helper.setOnClickListener(R.id.rl_nearby, {
            MobclickAgent.onEvent(mContext, "home_near")
            when {
                CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN) -> getLocalPosition()
                else -> goActivity(LoginRegMainPage::class.java)
            }
        })
        RxView.clicks(helper.getView(R.id.ivPlay)).throttleFirst(1, TimeUnit.SECONDS).subscribe {
//            MobclickAgent.onEvent(mContext, "home_today_play")
            MobclickAgent.onEvent(mContext, "songlists")
            if (isPlaying) {
                isPlaying = false
                mContext.sendBroadcast(Intent(ACTION_PAUSE))
                helper.getView<ImageView>(R.id.ivPlay).setImageResource(R.drawable.home_recommend_play_false)
            } else {
                isPlaying = true
                PlayCtrlUtil.playTodayList(mContext)
                helper.getView<ImageView>(R.id.ivPlay).setImageResource(R.drawable.home_recommend_play_true)
            }
        }
    }

    private fun bannerData(helper: BaseViewHolder, homeBean: HomeBean) {
        val bannerLayout = helper.getView<HomeTopBannerLayout>(R.id.banner)
        var unlimitedPagerAdapter = object : RecyclingUnlimitedPagerAdapter<HomeIndex.ItemInfoListBean.ShufflingBean>(bannerLayout.getAutoScrollViewPager(), mContext, homeBean.bannerBean.shuffling, R.layout.item_banner_imgae) {
            override fun onBind(position: Int, data: HomeIndex.ItemInfoListBean.ShufflingBean, holder: ViewHolder) {
//                try {
//                    val layoutParams = bannerLayout.layoutParams
                ImageLoader.with(mContext).getSize(710, 300).url(data.imgpic_link).into(holder.bind(R.id.img));
//                    Glide.with(mContext)
////                            .load(data.imgpic_info.link + "/" + width + "/" + height + "/3/80?format=0")
//                            .load(data.imgpic_link)
//                            .asBitmap()
//                            .centerCrop()
//                            .into(holder.bind(R.id.img))
//                } catch (e: RuntimeException) {
//                }

                RxView.clicks(holder.bind(R.id.img))
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe {
                            NetWork.getOnClicknum(data.id, mContext,object : NetWork.TokenCallBack {
                                override fun doNext(resultData: String, headers: Headers?) {

                                }

                                override fun doError(msg: String) {
                                }

                                override fun doResult() {
                                }
                            })


                            MobclickAgent.onEvent(mContext, "home_banner")
                            if (data.url.isNotEmpty()) {
                                if (data.alias == "web") {
                                    val url = data.url
                                    val intent = Intent(mContext, CommonWebview::class.java)
                                    intent.putExtra("url", url)
                                    intent.putExtra("title", data.title)
                                    if (!TextUtils.isEmpty(data.content)) {
                                        intent.putExtra("content", data.content)
                                    }
                                    if (data.imgpic_info != null) {
                                        if (!TextUtils.isEmpty(data.imgpic_info.link)) {
                                            intent.putExtra("img", data.imgpic_info.link)
                                        }
                                        mContext.startActivity(intent)

                                    }

                                } else if (data.alias == "topic") {
                                    val url = data.url
                                    val intent = Intent(mContext, PondDetialActivityNew::class.java)
                                    val bundle = Bundle()
                                    bundle.putInt(PondDetialActivityNew.PID, Integer.parseInt(url))
                                    intent.putExtras(bundle)
                                    mContext.startActivity(intent)
                                } else if (data.alias == "activity") {
                                    val url = data.url
                                    val intent = Intent(mContext, CommonWebview::class.java)
                                    intent.putExtra("url", url)
                                    intent.putExtra("activity", "activity")
                                    intent.putExtra("title", data.title)
                                    intent.putExtra("content", data.content)
                                    try {
                                        intent.putExtra("img", data.imgpic_info.link)
                                        mContext.startActivity(intent)
                                    } catch (e: RuntimeException) {
                                    }

                                } else if (data.alias == "music") {
                                     //单曲的情况下直接跳转到播放器
                                    val intent = Intent(mContext, PlayerActivity::class.java)
//                                    PlayCtrlUtil.play(mContext, originalAdapter!!.data[position].id, originalAdapter!!.data[position].song_id)
                                    PlayCtrlUtil.play(mContext, Integer.parseInt(data.url), 0)
                                    mContext.startActivity(intent)
                                    Log.e("ppppp",""+data.toString())
                                    Log.e("ppppp",""+data.url)
////                                    val bundle = Bundle()
////                                    Log.e("ppppp",""+data.toString())
////                                    bundle.putString(PlayerActivity.MUSIC_ID, data.url + "")
////                                    intent.putExtras(bundle)
////                                    val intent = Intent(mContext, MusicDetailsActivity::class.java)
////                                    val bundle = Bundle()
////                                    bundle.putString(MusicDetailsActivity.MUSIC_ID, data.url + "")
////                                    intent.putExtras(bundle)
////                                    mContext.startActivity(intent)
                                } else if (data.alias == "musician") {
                                    val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
                                    val bundle = Bundle()
                                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, data.url + "")
                                    intent.putExtras(bundle)
                                    mContext.startActivity(intent)
                                } else if (data.alias == "song") {
                                    val intent = Intent(mContext, SongSheetDetailsActivity::class.java)
                                    val bundle = Bundle()
                                    bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, data.url + "")
                                    intent.putExtras(bundle)
                                    mContext.startActivity(intent)
                                }
                            }
                        }
            }
        }
        bannerLayout.setAdapter(unlimitedPagerAdapter)
        bannerLayout.showIndicator(true)
        bannerLayout.startAutoScroll()
    }

    private fun goActivity(mClass: Class<*>) {
        goActivity(mClass, null)
    }

    fun goActivity(mClass: Class<*>, bundle: Bundle?) {
        val intent = Intent(mContext, mClass)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        mContext.startActivity(intent)
    }

    private fun msgClickGo(type: String?, url: String, id: String) {
        if (type != null && type == "page") {
            val bundle = Bundle()
            when (url) {
                "home" -> {
                }
                "topicDetails" -> if (!TextUtils.isEmpty(id)) {
                    bundle.putInt(PondDetialActivityNew.PID, Integer.parseInt(id))
                    goActivity(PondDetialActivityNew::class.java, bundle)
                }
                "musicDetails" -> {
//                    bundle.putString(MusicDetailsActivity.MUSIC_ID, id)
//                    goActivity(MusicDetailsActivity::class.java, bundle)
                }
                "musicianDetailHome" -> {
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 0)
                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                }
                "musicianDetailMusic" -> {
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 1)
                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                }
                "musicianDetailSongSheet" -> {
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 2)
                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                }
                "musicianDetailTopic" -> {
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 3)
                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                }
                "songSheetDetails" -> {
                    bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, id)
                    goActivity(SongSheetDetailsActivity::class.java, bundle)
                }
                "likesSongSheetDetails" -> {
                    bundle.putString(LikesMusicActivity.MUSICIAN_ID, id)
                    goActivity(LikesMusicActivity::class.java, bundle)
                }
            }
        } else if (type != null && type == "activity") {
            val intent = Intent(mContext, CommonWebview::class.java)
            intent.putExtra("url", url)
            intent.putExtra("activity", "activity")
            mContext.startActivity(intent)
        }
    }

    private fun getLocalPosition() {
        RxPermissions(mContext as Activity).requestEach(Manifest.permission.ACCESS_FINE_LOCATION).subscribeOn(Schedulers.newThread()).subscribe { permission ->
            when {
                permission.granted -> {
                    goActivity(NearbyPeopleActivity::class.java)
                }
                permission.shouldShowRequestPermissionRationale -> {

                }
                else -> {
                    Toast.create(mContext).show("请开启定位权限！")
                }
            }
        }
    }


}
