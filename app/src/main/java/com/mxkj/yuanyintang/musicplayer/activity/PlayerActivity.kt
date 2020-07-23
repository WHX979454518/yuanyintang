package com.mxkj.yuanyintang.musicplayer.activity

import android.animation.AnimatorSet
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.widget.RadioButton
import android.widget.SeekBar
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import com.linsh.lshutils.others.NetUtils
import com.liulishuo.filedownloader.util.FileDownloadUtils
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.base.activity.GiiGiftSuccessActivity
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.base.adapter.FrgPagerAdapter
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.database.updatafilesql.TasksManager
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.dialog.MusicOperationDialog
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog
import com.mxkj.yuanyintang.extraui.gift.BotomGiftListDialog
import com.mxkj.yuanyintang.extraui.gift.CheckBean
import com.mxkj.yuanyintang.extraui.gift.ConfirmGiveGiftDialog
import com.mxkj.yuanyintang.extraui.gift.FirstChargeDialog
import com.mxkj.yuanyintang.mainui.comment.CommentActivity
import com.mxkj.yuanyintang.mainui.emotionkeyboard.activity.MainActivity
import com.mxkj.yuanyintang.mainui.home.adapter.SwitcherAdapter
import com.mxkj.yuanyintang.mainui.home.bean.MemberGiftListBean
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.myself.doughnut.ChargeDoughnutActivity
import com.mxkj.yuanyintang.musicplayer.fragment.*
import com.mxkj.yuanyintang.musicplayer.lyric_remusic.LrcRow
import com.mxkj.yuanyintang.musicplayer.play_control.GetMusicInfo
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.musicplayer.service.MediaService.*
import com.mxkj.yuanyintang.musicplayer.view.PopPlayList
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.pay.PayUtils
import com.mxkj.yuanyintang.utils.app.TimeUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.uiutils.Toast
import com.mxkj.yuanyintang.video.MvDetails
import com.mxkj.yuanyintang.video.MvVideoActivitykt
import com.mxkj.yuanyintang.widget.Panel
import com.umeng.analytics.MobclickAgent
import com.xiao.nicevideoplayer.NiceVideoPlayerController
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_playing.*
import okhttp3.Headers
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import kotlinx.android.synthetic.main.player_activity.*
import kotlinx.android.synthetic.main.right_switcher_list.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File


/**
 * 全屏播放器
 */
class PlayerActivity : BaseActivity() {
    private var giftListDialog: BotomGiftListDialog? = null
    private var isReGiveGift: Boolean = false
    private var gift_id: Int = 0
    private var profit_billboard_config: MemberGiftListBean.DataBean.ProfitBillboardConfigBean? = null
    private var fragmentList = ArrayList<Fragment>()
    private var seekbarMum: Int = 0
    var isUpdateProUi = true
    private val animatorSet: AnimatorSet? = null
    private var LrcRowlist: List<LrcRow> = ArrayList()
    internal var isFull: Boolean = false
    private var isPause: Boolean = false
    private var popwindow: PopPlayList? = null
    private lateinit var fragMoreSong: FragMoreSong
    private lateinit var fragPlaying: FragPlaying
    private lateinit var fragNotingPlaying: FragNotingPlaying
    private lateinit var fragtwoPlaying: FragTwoPlaying
    private lateinit var fragMusicDetail: FragMusicDetail
    private lateinit var filter_duration: IntentFilter
    private var receiver: PlayCtrlReceiver? = null
    var currVp = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)
        StatusBarUtil.baseTransparentStatusBar(this)
        setPlayerBaseInfo()
        onViewClicked()
        registReceiver()
        checkPlayStatue()
        playCtrl()
        initPannel()
    }

    private fun initPannel() {
        val animationDrawable = img_gift.drawable as AnimationDrawable
        animationDrawable.start()
        rightPanel2.setOnPanelListener(object : Panel.OnPanelListener {
            override fun onPanelClosed(panel: Panel) {
                bck.visibility = View.GONE
            }

            override fun onPanelOpened(panel: Panel) {
                MobclickAgent.onEvent(this@PlayerActivity, "player_gift");

                getGiftList()
                bck.visibility = View.VISIBLE
                bck.setOnClickListener {
                    rightPanel2.setOpen(false, true)
                    bck.visibility = View.GONE
                }
            }
        })

        RxView.clicks(tv_to_gift_list)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(Consumer {
                    if (profit_billboard_config == null) return@Consumer
                    val intent = Intent(this@PlayerActivity, GiftChartsActivity::class.java)
                    intent.putExtra(GiftChartsActivity.EXPEN_CLASS_ID, profit_billboard_config?.class_id)
                    intent.putExtra(GiftChartsActivity.INCOME_CLASS_ID, if (profit_billboard_config?.toggle_class == null) 0 else profit_billboard_config?.toggle_class?.id)
                    startActivity(intent)
                })

        RxView.clicks(tv_give_gift)
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe {
                    rightPanel2.setOpen(false, false)
                    showBotomGiftDialog()
                }

    }

    private var switcherList: ArrayList<MemberGiftListBean.DataBean.DataListBean> = ArrayList()

    private fun getGiftList() {
        if (bean != null && bean?.isLocalMusic == false) {
            rightPanel2.visibility = VISIBLE
            tv_musician_name.text = bean?.nickname
            bean?.head_link?.let {
                Glide.with(this@PlayerActivity).load(it).into(switcher_head_icon)
            }
            recycler_switcher.layoutManager = LinearLayoutManager(this)
            NetWork.getMemberGiftList(this, HttpParams("id", bean?.uid.toString()), object : NetWork.TokenCallBack {
                override fun doError(msg: String) {

                }

                override fun doResult() {
                }

                override fun doNext(resultData: String, headers: Headers?) {
                    var memberGiftListBean: MemberGiftListBean? = JSON.parseObject(resultData, MemberGiftListBean::class.java)
                            ?: return
                    var data: MemberGiftListBean.DataBean? = memberGiftListBean?.data ?: return
                    tv_gift_num.text = "共收到" + data?.counts + "件礼物"
                    profit_billboard_config = data?.profit_billboard_config
                    var dataList = data?.data_list
                    if (dataList != null && dataList.size > 0) {
                        recycler_switcher.visibility = View.VISIBLE
                        tv_switcher_no_gift.visibility = View.GONE
                        switcherList.addAll(dataList)
                        var switcherAdapter = SwitcherAdapter(this@PlayerActivity, dataList)
                        recycler_switcher.adapter = switcherAdapter
                    } else {
                        tv_switcher_no_gift.visibility = View.VISIBLE
                        recycler_switcher.visibility = View.GONE
                    }
                }

            })
            GetMusicInfo.getMusicDetial(this, bean!!.id, object : GetMusicInfo.SetBeanData {
                override fun setBeanData(dataBean: MusicInfo.DataBean?) {
                    if (dataBean != null) {
                        bean = dataBean
                    }
                    if(bean!!.comment!! >0){
                        comment.setImageResource(R.drawable.icon_music_pinlun3x)
                    }else{
                        comment.setImageResource(R.drawable.icon_music_pinlun_full_3x)
                    }
                    if (bean!!.comment!! > 99) {
                        num_comment.text = "99+"
                    } else if(bean!!.comment!! == 0){
                        num_comment.visibility = View.GONE
                    }else{
                        num_comment.text = bean!!.comment.toString() + ""
                    }
                    if (bean!!.collection == 0) {
                        collect.setImageResource(R.drawable.fullplay_like_normal)
                    } else {
                        collect.setImageResource(R.drawable.fullplay_like_red)
                    }
                }
            })
        } else {
            rightPanel2.visibility = GONE
        }
    }

    private fun playCtrl() {
        seekBarCtrl()
    }

    private fun seekBarCtrl() {
        play_seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                isUpdateProUi = true
                seekbarMum = progress
                tvCurrTime.text = TimeUtils.timeUtil(seekbarMum.toLong())
                seekBar.progress = seekbarMum
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
//                wvStop()
            }

            override fun onStopTrackingTouch(seekBar2: SeekBar) {
                play_seek.progress = seekbarMum
                tvCurrTime.text = TimeUtils.timeUtil(seekbarMum.toLong())
                play_seek.progress = seekbarMum
                val intentPause = Intent(ACTION_SEEK)
                intentPause.putExtra(ACTION_SEEK, seekbarMum.toLong())
                sendBroadcast(intentPause)
                val handler = Handler()
                handler.postDelayed({ isUpdateProUi = true }, 1000)
//                wvStart()
            }
        })
    }

    private fun initVp() {
        fragmentList.clear()

        //这里是判断播放的列表是否为空，如果为空，则清空，并且清空歌曲名和歌手
        if(MediaService.playList.size == 0){
            bean = null
            tv_songname.setText("")
            tv_singer.setText("")
        }

        if (bean == null || bean?.isLocalMusic == true) {
            var aaa = bean?.isLocalMusic
            rightPanel2.visibility = INVISIBLE
            fragPlaying = FragPlaying()
            fragmentList.add(fragPlaying)
            if(bean?.mv.equals("")|| bean?.mv == ""||null==bean?.mv){
                mv.visibility = View.GONE
                download.setImageResource(R.drawable.icon_play_updown_gray3x)
                collect.setImageResource(R.drawable.icon_play_collectsong_gray3x)
            }else{
                mv.visibility = View.VISIBLE
                mv.setImageResource(R.mipmap.icon_play_mv_black3x)
            }
            download.setImageResource(R.drawable.icon_play_updown_gray3x)
            collect.setImageResource(R.drawable.icon_play_collectsong_gray3x)
            comment.setImageResource(R.drawable.icon_music_pinlun_gray_3x)
            share.setImageResource(R.drawable.icon_play_sharesong_gray2)
            download.isClickable = false
            collect.isClickable = false
            comment.isClickable = false
            share.isClickable = false
            more.visibility = View.GONE

            fragtwoPlaying = FragTwoPlaying()
            fragmentList.add(fragtwoPlaying)
            fragNotingPlaying = FragNotingPlaying()
            fragmentList.add(fragNotingPlaying)
        } else {
            rightPanel2.visibility = VISIBLE
            download.isClickable = true
            collect.isClickable = true
            comment.isClickable = true
            share.isClickable = true
            fragMusicDetail = FragMusicDetail()
            fragPlaying = FragPlaying()
            fragMoreSong = FragMoreSong()
            fragmentList.add(fragMusicDetail)
            fragmentList.add(fragPlaying)
            fragmentList.add(fragMoreSong)
        }
        val pagerAdapter = FrgPagerAdapter(supportFragmentManager, fragmentList)
        vp_player.adapter = pagerAdapter
        vp_player.offscreenPageLimit = fragmentList.size
        vp_player.currentItem = currVp
        vp_player.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currVp = position
                if(currVp == 0 || currVp== 2){
                    tv_singer.visibility = View.GONE
                }else{
                    tv_singer.visibility = View.VISIBLE
                }
                val rBtn = rg_vp_indic.getChildAt(position) as RadioButton
                rBtn.isChecked = true
            }
        })
    }

    private fun onViewClicked() {
        RxView.clicks(play_mode).subscribe {
            MobclickAgent.onEvent(this@PlayerActivity, "player_model");
            when (playModel) {
                1 -> {
                    playModel = 0
                    playModelView()
                    Toast.create(this).show("随机播放")
                }
                2 -> {
                    playModel = 1
                    playModelView()
                    Toast.create(this).show("单曲循环")
                }
                0 -> {
                    playModel = 2
                    playModelView()
                    Toast.create(this).show("列表循环")
                }
            }
        }
        RxView.clicks(finish).subscribe {
            MobclickAgent.onEvent(this@PlayerActivity, "player_finish");
//            goActivity(HomeActivity::class.java)
            overridePendingTransition(R.anim.drop_down, android.R.anim.fade_out);
            finish()

        }
        RxView.clicks(play_pre).throttleWithTimeout(300, TimeUnit.MILLISECONDS).subscribe {
            MobclickAgent.onEvent(this@PlayerActivity, "player_last");

            if (MediaService.bean != null) {
                sendBroadcast(Intent(ACTION_PRE))
            } else {
                noMusicPlay()
            }
        }
        RxView.clicks(play).subscribe {
            MobclickAgent.onEvent(this@PlayerActivity, "player_pause");

            if (null != MediaService.bean) {
                if (MediaService.getMediaPlayer().isPlaying) {
                    play.setImageResource(R.drawable.fullplay_pause);
                    sendBroadcast(Intent(ACTION_PAUSE))
                } else {
                    play.setImageResource(R.drawable.fullplay_playing);
                    if (!CacheUtils.getBoolean(this@PlayerActivity, MEDIA_PLAY_IS_PAUSE, true)) {
                        var intent = Intent(this@PlayerActivity, MediaService::class.java)
                        intent.putExtra("bean", bean)
                        startService(intent)
                    } else {
                        sendBroadcast(Intent(ACTION_PAUSE))
                    }
                }
            } else {
                noMusicPlay()
            }
        }
        RxView.clicks(play_next).throttleWithTimeout(300, TimeUnit.MILLISECONDS).subscribe {
            MobclickAgent.onEvent(this@PlayerActivity, "player_next");

            when {
                MediaService.bean != null -> if (MediaService.getMediaPlayer().isPlaying) {
                    sendBroadcast(Intent(ACTION_NEXT))
                } else {
                    val intent = Intent(this, MediaService::class.java)
                    intent.putExtra("bean", bean)
                    startService(intent)
                }
                else -> noMusicPlay()
            }
        }
        RxView.clicks(play_list).subscribe {
            MobclickAgent.onEvent(this@PlayerActivity, "player_list");

            popwindow = null
            popwindow = PopPlayList()
            popwindow?.show(supportFragmentManager, "")
        }

        RxView.clicks(mv).subscribe {
            val intent = Intent(this@PlayerActivity, MvVideoActivitykt::class.java)
            var bundle = Bundle()
            bundle.putInt("mv", bean!!.id);
            bundle.putString("mvurl", bean!!.mv_info!!.link);
            bundle.putInt("uid", bean!!.uid);
            bundle.putString("title", bean!!.title);
            bundle.putString("nickname", bean!!.nickname);
            bundle.putString("imgpic_link", bean!!.imgpic);
            bundle.putString("bioashi", 1.toString() + "")
            intent.putExtra("mvdate",bundle);
            startActivity(intent)
        }

        RxView.clicks(download).subscribe {
            MobclickAgent.onEvent(this@PlayerActivity, "player_download");
            if (CacheUtils.getBoolean(this@PlayerActivity, Constants.User.IS_LOGIN)) {
                if (bean != null) {
//                   TODO  下载
                    val kbps = CacheUtils.getString(this@PlayerActivity, Constants.User.MUSIC_KBP, "128")
                    NetWork.getMusicDown(this@PlayerActivity, bean!!.id.toString(), if (TextUtils.equals("128", kbps)) "1" else "2", object : NetWork.TokenCallBack {
                        override fun doNext(resultData: String, headers: Headers?) {
                            setSnackBar("已加入下载列表", "", R.drawable.icon_success)
                            var data: String? = null
                            try {
                                val `object` = JSONObject(resultData)
                                data = `object`.optString("data")
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                            if (!TextUtils.isEmpty(data) && bean?.imgpic_info != null) {
                                val musicBean = MusicBean()
                                musicBean.ext = "." + StringUtils.parseSuffix(data)
                                musicBean.setCollection(1)
                                val link = bean?.imgpic_info?.link
                                val imgpicInfoBean = MusicBean.ImgpicInfoBean()
                                imgpicInfoBean.link = link
                                musicBean.imgpic_info = imgpicInfoBean

                                musicBean.setMusic_name(bean?.title)
                                musicBean.setMusician_name(bean?.nickname)
                                musicBean.ext = "." + StringUtils.parseSuffix(data)
                                musicBean.setMusic_id(bean?.id.toString())
                                musicBean.setUid(bean?.uid ?: 0)
                                musicBean.setSong_id(0)
                                TasksManager.getImpl().downLoadFile(musicBean, data, this@PlayerActivity)
                            }
                        }

                        override fun doError(msg: String) {

                        }

                        override fun doResult() {

                        }
                    })


                }
            } else {
                goActivity(LoginRegMainPage::class.java)
            }
        }
        RxView.clicks(collect).subscribe {
            MobclickAgent.onEvent(this@PlayerActivity, "player_collect");

            if (CacheUtils.getBoolean(this@PlayerActivity, Constants.User.IS_LOGIN)) {
                if (bean != null) {
                    showLoadingView()
                    PlayCtrlUtil.collectSong(this, bean, object : PlayCtrlUtil.ChgCollectCallBack {
                        override fun chgCollect(dataBean: MusicInfo.DataBean) {
                            bean = dataBean
                            hideLoadingView()
                            if (dataBean.collection == 0) {
                                collect.setImageResource(R.drawable.fullplay_like_normal)
                            } else {
                                collect.setImageResource(R.drawable.fullplay_like_red)
                            }
                        }

                        override fun onError() {
                            hideLoadingView()
                        }
                    })
                }
            } else {
                goActivity(LoginRegMainPage::class.java)
            }
        }
        RxView.clicks(comment).subscribe {
            MobclickAgent.onEvent(this@PlayerActivity, "player_comment");
            if (bean != null) {
                val intent = Intent(this, CommentActivity::class.java)
                intent.putExtra(CommentActivity.TYPE, 1)
                intent.putExtra(CommentActivity.ITEM_ID, bean!!.id)
                startActivity(intent)
            }
        }
        RxView.clicks(share).subscribe {
            MobclickAgent.onEvent(this@PlayerActivity, "player_share");
            shareMusic()
        }
        RxView.clicks(more).subscribe {
            if (bean != null) {
                if(null==bean!!.mv){
                    bean!!.mv = ""
                }
                val musicBean = MusicBean()
                        .setMusic_name(bean!!.title)
                        .setCommentNum(bean!!.comment)
                        .setUid(bean!!.uid)
                        .setMusic_id(bean!!.id.toString())
                        .setMusician_name(bean!!.nickname)
                        .setPosition(0)
                        .setCommentType(1)
//                        .setType(2)
                        .setType(15)
                        .setReportId(bean!!.id)
                        .setMultiSelect(false)
                        .setSong_id(bean!!.song_id)
                        .setSongName(bean!!.song_title + "")
                        .setCollection(bean!!.collection)
                        .setPlayTime(bean!!.playtime)
                        .setMv(bean!!.mv!!)

                try {
                    val link = bean!!.imgpic_info!!.link
                    val imgpicInfoBean = MusicBean.ImgpicInfoBean()
                    imgpicInfoBean.link = link
                    musicBean.imgpic_info = imgpicInfoBean
                } catch (e: RuntimeException) {
                }

                if (bean!!.mv_info != null) {
                    val link = bean!!.mv_info!!.link
                    val mvInfoBean = MusicBean.MvInfoBean()
                    mvInfoBean.link = link
                    musicBean.mvInfoBean = mvInfoBean
                }


                val musicOperationDialog = MusicOperationDialog(musicBean, supportFragmentManager, this)
                musicOperationDialog.show(supportFragmentManager, "fullPlay")
                musicOperationDialog.setSetMusicOperationListener(object : MusicOperationDialog.SetMusicOperationListener {
                    override fun onCollection(collection: Int, position: Int) {
                        MediaService.bean!!.collection = collection
                        if (MediaService.bean!!.collection == 0) {
                            collect.setImageResource(R.drawable.fullplay_like_normal)
                        } else {
                            collect.setImageResource(R.drawable.fullplay_like_red)
                        }
                        musicOperationDialog.dismiss()
                    }

                    override fun getType(type: Int) {

                    }
                })
            }
        }
    }

    private fun setPlayerBaseInfo() {
        Log.e("oooooo",""+bean)
        if (bean != null) {
            tv_songname.text = bean!!.title
            tv_singer.text = bean!!.nickname

            val filePath = FileDownloadUtils.generateFilePath(TasksManager.getFilePath(this@PlayerActivity), if (TextUtils.isEmpty(bean?.title))
                System.currentTimeMillis().toString() + "" + bean?.title
            else
                StringUtils.isEmpty(bean?.title))
            when {
                File(filePath).exists() ->
                    download.setImageResource(R.drawable.down_ed)
                else -> download.setImageResource(R.drawable.undown)
            }
            initVp()
            getGiftList()
            System.gc()
            if (MediaService.mediaPlayer != null) {
                tv_total_time.text = TimeUtils.timeUtil(MediaService.mediaPlayer.duration)
                tvCurrTime.text = TimeUtils.timeUtil(MediaService.mediaPlayer.currentPosition)
                play_seek.max = MediaService.getMediaPlayer().duration.toInt()
                play_seek.progress = MediaService.getMediaPlayer().currentPosition.toInt()
            }

        } else {
            val string = CacheUtils.getString(this@PlayerActivity, LAST_PLAY_SONG)
            if (!TextUtils.isEmpty(string)) {
                bean = JSON.parseObject(string, MusicInfo.DataBean::class.java)
                if (bean == null) {
                    return
                }
                if (bean!!.collection == 0) {
                    collect.setImageResource(R.drawable.fullplay_like_normal)
                } else {
                    collect.setImageResource(R.drawable.fullplay_like_red)
                }
                if(bean!!.comment!! >0){
                    comment.setImageResource(R.drawable.icon_music_pinlun3x)
                }else{
                    comment.setImageResource(R.drawable.icon_music_pinlun_full_3x)
                }
                if (bean!!.comment > 99) {
                    num_comment.text = "99+"
                }else if(bean!!.comment!! == 0){
                    num_comment.visibility = View.GONE
                } else {
                    num_comment.text = bean!!.comment.toString() + ""
                }
                tv_total_time.text = TimeUtils.timeUtil(CacheUtils.getLong(this@PlayerActivity, LAST_PLAY_SONG_TOTAL_TIME, 0.toLong()))
                tvCurrTime.text = TimeUtils.timeUtil(CacheUtils.getLong(this@PlayerActivity, LAST_PLAY_SONG_PLAY_TIME, 0.toLong()))
            }
        }
    }

    private fun playModelView() {
        when (playModel) {
            0 -> {
                play_mode.setImageResource(R.drawable.fullplay_radom_normal)
                MediaService.setRandomPlayList()
            }
            1 -> play_mode.setImageResource(R.drawable.fullplay_single_normal)
            2 -> play_mode.setImageResource(R.drawable.fullplay_list_normal)
        }
    }

    private fun noMusicPlay() {
        if (MediaService.playList.size > 0) {
            val intent = Intent(this, MediaService::class.java)
            intent.putExtra("bean", MediaService.playList[0])
            startService(intent)
        }
    }

    private fun shareMusic() {
        if (NetConnectedUtils.isNetConnected(applicationContext)) {
            if (CacheUtils.getBoolean(this@PlayerActivity, Constants.User.IS_LOGIN, false) && bean != null) {
                val musicBean = MusicBean()
                val shareDataBean = MusicBean.ShareDataBean()
                shareDataBean.type = "music"
                if (bean!!.video_info != null) {
                    shareDataBean.video_link = bean!!.video_info!!.link
                }
                try {
                    shareDataBean.image_link = bean!!.imgpic_info!!.link
                    shareDataBean.webImgUrl = bean!!.imgpic_info!!.link
                } catch (e: RuntimeException) {
                }

                shareDataBean.uid = bean!!.uid
                shareDataBean.muisic_id = bean!!.id
                shareDataBean.nickname = bean!!.nickname
                shareDataBean.title = bean!!.title
                shareDataBean.share_link = bean!!.share_url

                Log.e("jjjjj","jj"+bean!!.mv_info!!.link)
                shareDataBean.mv = bean!!.mv
                musicBean.setShareDataBean(shareDataBean)
                val shareBottomDialog = ShareBottomDialog(this, musicBean)
                shareBottomDialog.show()
            } else {
                goActivity(LoginRegMainPage::class.java)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fragPlaying.isShowImg) {//如果处于全屏播放状态
                fragPlaying.rl_big_img.visibility = View.GONE
                fragPlaying.isShowImg = false
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun checkPlayStatue() {
        if(bean?.mv.equals("")|| bean?.mv == ""||null==bean?.mv){
            mv.visibility = View.GONE
        }else{
            mv.visibility = View.VISIBLE
            mv.setImageResource(R.mipmap.icon_play_mv_black3x)
        }
        if (null == MediaService.bean || !NetUtils.isConnected(this) || bean?.isLocalMusic == true) {
            download.setImageResource(R.drawable.undown)
            collect.setImageResource(R.drawable.fullplay_like_default)
            comment.setImageResource(R.drawable.icon_music_pinlun_gray_3x)
            share.setImageResource(R.drawable.fullplay_share_default)
        } else {
            download.setImageResource(R.drawable.undown)
            collect.setImageResource(R.drawable.fullplay_like_normal)
            comment.setImageResource(R.drawable.icon_music_pinlun_full_3x)
            share.setImageResource(R.drawable.fullplay_share_normal)
        }
    }

    private fun registReceiver() {
        filter_duration = IntentFilter()
        filter_duration.addAction("dur")
        filter_duration.addAction("secondDur")//缓冲进度
        filter_duration.addAction("setpause")
        filter_duration.addAction("setplay")
        filter_duration.addAction("playNext")
        filter_duration.addAction("startplay")
        filter_duration.addAction("playPre")
        filter_duration.addAction("cgCollect")//更改收藏状态
        receiver = PlayCtrlReceiver()
        registerReceiver(receiver, filter_duration)
    }

    private inner class PlayCtrlReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            Log.d(BaseActivity.TAG, "onReceive: ---------->" + action!!)
            when (action) {
                "dur" -> if (MediaService.getMediaPlayer() != null && MediaService.getMediaPlayer().isPlaying && isUpdateProUi) {
                    play_seek.max = MediaService.getMediaPlayer().duration.toInt()
                    play_seek.progress = MediaService.getMediaPlayer().currentPosition.toInt()
                    tv_total_time.text = TimeUtils.timeUtil(MediaService.getMediaPlayer().duration)
                    tvCurrTime.text = TimeUtils.timeUtil(MediaService.getMediaPlayer().currentPosition)
                    val i = MediaService.getMediaPlayer().currentPosition.toInt()
                }
                "startplay" -> {
                    checkPlayStatue()
                    setPlayerBaseInfo()
                    if (bean != null) {
                        tv_singer.text = bean?.nickname
                        tv_songname.text = bean?.title
                    }
                }
                "setpause" -> {
                    CacheUtils.setBoolean(this@PlayerActivity, MediaService.MEDIA_PLAY_IS_PAUSE, true)
                    play.setImageResource(R.drawable.fullplay_pause)
//                    wvStop()
                }
                "setplay" -> {
                    CacheUtils.setBoolean(this@PlayerActivity, MediaService.MEDIA_PLAY_IS_PAUSE, false)
                    play.setImageResource(R.drawable.fullplay_playing)
                    if (!isPause) {
//                        wvStart()
                    }
                }
                "cgCollect" -> {
                    if (null == bean) {
                        return
                    }
                    val filePath = FileDownloadUtils.generateFilePath(TasksManager.getFilePath(this@PlayerActivity), if (TextUtils.isEmpty(bean?.title))
                        System.currentTimeMillis().toString() + "" + bean?.title
                    else
                        StringUtils.isEmpty(bean?.title))
                    when {
                        File(filePath).exists() -> {
                            download.setImageResource(R.drawable.down_ed)
                            download.isClickable = true
                        }
                        else -> {
                            download.setImageResource(R.drawable.undown)
                            download.isClickable = true

                        }
                    }
                    collect.setImageResource(if (bean!!.collection == 1) R.drawable.fullplay_like_red else R.drawable.fullplay_like_normal)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        unregisterReceiver(receiver)

//        wv = null
    }


    private fun showBotomGiftDialog() {
        giftListDialog = BotomGiftListDialog()
        giftListDialog?.music_id = if (bean == null) 0 else bean!!.id
        if (CacheUtils.getBoolean(this, Constants.User.IS_LOGIN)) {
            giftListDialog?.show(supportFragmentManager, "giftListDialog")
        } else {
            goActivity(LoginRegMainPage::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        initVp()
        giftListDialog?.let {
            if (giftListDialog!!.isAdded) {
                giftListDialog!!.dismiss()
            }
        }
        if (isReGiveGift && gift_id != 0) {
            checkStatus(gift_id)
        }


        if(bean != null){
            mvdetails(bean!!.id)
        }


    }

    /*检查能否送礼物*/
    fun checkStatus(gift_id: Int) {
        showLoadingView()
        //gift_id开始是0，当点击弹框里面的礼物，此方法第一次调用并且给gift_id赋值
        this.gift_id = gift_id
        val params = HttpParams()
        params.put("music_id", bean?.id.toString() + "")
        params.put("gift_id", gift_id.toString() + "")
        params.put("source", "app")
        NetWork.checkGift(this, params, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                hideLoadingView()
                val checkBean = JSON.parseObject(resultData, CheckBean::class.java)
                val code = checkBean.code
                val data = checkBean.data ?: return
                when (code) {
                    0 -> {
                        if (isReGiveGift) {
                            return
                        }
                        isReGiveGift = false
                        if (data.order_cate != null && data.order_cate?.setting != null && data.order_cate?.setting?.first_charge!! > 0) {
                            data.order_cate?.setting
                            FirstChargeDialog.newInstance().showDialog(this@PlayerActivity, object : FirstChargeDialog.onBtClick {
                                override fun onConfirm() {
                                    startActivity(Intent(this@PlayerActivity, ChargeDoughnutActivity::class.java))
                                }

                                override fun onCancle() {
                                    rechargeDialog(data)
                                }
                            })
                        } else {
                            rechargeDialog(data)
                        }
                    }
                    1 -> giveGiftDialog(data)
                }
            }

            override fun doError(msg: String) {
                hideLoadingView()
            }

            override fun doResult() {

            }
        })
    }

    /*充值*/
    fun rechargeDialog(dataBean: CheckBean.DataBean) {
        if (dataBean.order_cate == null || dataBean.order_cate?.lists == null) {
            return
        }
        val dLlists = dataBean.order_cate?.lists
        dLlists?.let {
            ConfirmGiveGiftDialog.newInstance()
                    .isRecharge(true)
                    .title("甜甜圈不够啦")
                    .chargeId(dLlists?.id)
                    .giftId(dataBean.gift_id)
                    .type(dLlists.type)
                    .payWay(dataBean.last_pay_type)
                    .content("已为您选择最佳档位" + dLlists.coin_num + "个")
                    .oldPrice(dLlists.price.toString() + "")
                    .price(dLlists.discount_price.toString() + "")
                    .showDialog(this@PlayerActivity, object : ConfirmGiveGiftDialog.onBtClick {
                        override fun onConfirm() {
                            showLoadingView()
                            val lastPayType = dataBean.last_pay_type
                            isReGiveGift = true
                            when (lastPayType) {
                                0 -> PayUtils.pay(this@PlayerActivity, dLlists.id, 3)
                                1 -> PayUtils.pay(this@PlayerActivity, dLlists.id, 1)
                                2 -> PayUtils.pay(this@PlayerActivity, dLlists.id, 2)
                                3 -> PayUtils.pay(this@PlayerActivity, dLlists.id, 3)
                            }
                        }

                        override fun onCancle() {

                        }
                    })
        }
    }

    /*确认赠送*/
    fun giveGiftDialog(dataBean: CheckBean.DataBean?) {
        if (dataBean?.icon_info == null) {
            return
        }
        var content = "为《" + StringUtils.isEmpty(dataBean.music_title) + "》送"
        if (isReGiveGift) {
            content = "继续为《" + StringUtils.isEmpty(dataBean.music_title) + "》送"
            isReGiveGift = false
        }
        ConfirmGiveGiftDialog.newInstance()
                .content(content)
                .giftName(dataBean.name)
                .giftUrl(dataBean.icon_info?.link)
                .tips("剩余：" + dataBean.my_coin + "甜甜圈")
                .showDialog(this@PlayerActivity, object : ConfirmGiveGiftDialog.onBtClick {
                    override fun onConfirm() {
                        val params = HttpParams()
                        params.put("music_id", bean?.id.toString())
                        params.put("gift_id", dataBean.gift_id.toString() + "")
                        params.put("source", "source")
                        NetWork.giveGift(this@PlayerActivity, params, object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val bundle = Bundle()
                                bundle.putSerializable("GIFT_BEAN", dataBean)
                                goActivity(GiiGiftSuccessActivity::class.java, bundle)
//                            TODO    通知音乐详情fragment更新礼物列表
                            }

                            override fun doError(msg: String) {

                            }

                            override fun doResult() {

                            }
                        })
                    }

                    override fun onCancle() {

                    }
                })
    }

    override fun onBackPressed() {
        if (rightPanel2.isOpen) {
            rightPanel2.setOpen(false, true)
        } else {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.drop_down, R.anim.drop_down)
    }
    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    var x1 = 0f
    var x2 = 0f
    var y1 = 0f
    var y2 = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if(event!!.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if(y1 - y2 > 50) { } else if(y2 - y1 > 50) {
                finish()
                overridePendingTransition(R.anim.drop_down, R.anim.drop_down)
            } else if(x1 - x2 > 50) {} else if(x2 - x1 > 50) {}
        }
        return super.onTouchEvent(event);
    }


    private var mvDetails : MvDetails.DataBean? = null
    private fun mvdetails(id :Int){
        NetWork.mvdetails(this@PlayerActivity, id, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val jsonObject1 = JSON.parseObject(resultData)
                mvDetails = jsonObject1.getObject("data", MvDetails.DataBean::class.java)
                Log.e("rrrrrr",""+ mvDetails.toString())
                Log.e("rrrrrr",""+ mvDetails!!.comment)
                num_comment.text = mvDetails!!.comment.toString()

            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }
}
