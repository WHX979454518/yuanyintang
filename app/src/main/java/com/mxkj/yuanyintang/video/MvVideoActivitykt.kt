package com.mxkj.yuanyintang.video

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*

import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.home.bean.MemberGiftListBean
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity
import com.mxkj.yuanyintang.mainui.myself.bean.MyReleaseBean
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.widget.Panel
import com.umeng.analytics.MobclickAgent
import com.xiao.nicevideoplayer.NiceVideoPlayer
import com.xiao.nicevideoplayer.NiceVideoPlayerController
import com.xiao.nicevideoplayer.NiceVideoPlayerManager

import java.util.concurrent.TimeUnit

import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.linsh.lshutils.utils.LshContextUtils
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R.attr.type
import com.mxkj.yuanyintang.R.layout.dialog
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.base.activity.GiiGiftSuccessActivity
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.extraui.activity.ReportActivity
import com.mxkj.yuanyintang.extraui.adapter.ReportOperationAdapter
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.bean.ReportOperationBean
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog
import com.mxkj.yuanyintang.extraui.gift.BotomGiftListDialog
import com.mxkj.yuanyintang.extraui.gift.CheckBean
import com.mxkj.yuanyintang.extraui.gift.ConfirmGiveGiftDialog
import com.mxkj.yuanyintang.extraui.gift.FirstChargeDialog
import com.mxkj.yuanyintang.mainui.comment.CommenDetialActivity
import com.mxkj.yuanyintang.mainui.comment.Comment
import com.mxkj.yuanyintang.mainui.comment.CommentSuccessReceiver
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.dynamic.adapter.CommentAdapte
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard
import com.mxkj.yuanyintang.mainui.emotionkeyboard.fragment.EmotionMainFragment
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.adapter.SwitcherAdapter
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.myself.doughnut.ChargeDoughnutActivity
import com.mxkj.yuanyintang.musicplayer.play_control.GetMusicInfo
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.net.NetWork.mvdetails
import com.mxkj.yuanyintang.pay.PayUtils
import com.mxkj.yuanyintang.utils.app.TimeUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.xiao.nicevideoplayer.NiceVideoPlayer.PLAYER_FULL_SCREEN
import com.xiao.nicevideoplayer.NiceVideoPlayer.PLAYER_NORMAL
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.player_activity.*
import kotlinx.android.synthetic.main.right_switcher_list.*
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.mvvideoavtivity.view.*
import kotlinx.android.synthetic.main.notification.view.*
import okhttp3.Headers
import java.util.ArrayList

class MvVideoActivitykt : BaseActivity() {
    private var bioashi: String? = null

    private var giftListDialog: BotomGiftListDialog? = null
    private var isReGiveGift: Boolean = false
    private var gift_id: Int = 0
    private var profit_billboard_config: MemberGiftListBean.DataBean.ProfitBillboardConfigBean? = null


    private var mvDetails : MvDetails.DataBean? = null
//    private var fragmentList = ArrayList<Fragment>()
//    private var seekbarMum: Int = 0
//    var isUpdateProUi = true
//    private val animatorSet: AnimatorSet? = null
//    private var LrcRowlist: List<LrcRow> = ArrayList()
//    internal var isFull: Boolean = false
//    private var isPause: Boolean = false
//    private var popwindow: PopPlayList? = null
//    private lateinit var fragMoreSong: FragMoreSong
//    private lateinit var fragPlaying: FragPlaying
//    private lateinit var fragMusicDetail: FragMusicDetail
//    private lateinit var filter_duration: IntentFilter

    private lateinit var mNiceVideoPlayer: NiceVideoPlayer
    private lateinit var back: ImageView
    private lateinit var date: MyReleaseBean.DataBean
//    private lateinit var datedate: HomeBean.TypeMusicListBean.TypeMusicBean.MusicBean



    internal lateinit var adapter: CommentAdapte
    private val dataList = ArrayList<Comment.DataBean>()
    internal lateinit var interfaceRefreshLoadMore: InterfaceRefreshLoadMore
    private var page = 1
    private var emotionMainFragment: EmotionMainFragment? = null


    private lateinit var share: ImageView
    private lateinit var report: ImageView
    private lateinit var btn_send_mvcomment:Button
    private lateinit var et_pinglun:TextView
    private lateinit var mv_music_name :TextView
    private lateinit var mv_update :TextView
    private lateinit var mv_playnumber :TextView
    private lateinit var mv_user_photo :CircleImageView
    private lateinit var mv_user_name :TextView
    private lateinit var mv_gift_rl :RelativeLayout


    private var urlid:Int = 0
    private lateinit var mvurl:String
    private var uid:Int = 0
    private lateinit var title:String
    private lateinit var nickname:String
    private lateinit var imgpic_link:String


    private lateinit var wm:WindowManager;
    private lateinit var progressbar: ProgressBar
    private var inflate:View? = null
    private var dialog: Dialog? = null
    private var reportOperationBeanList: MutableList<ReportOperationBean.DataBean> = ArrayList()
    private lateinit var reportOperationAdapter: ReportOperationAdapter
    private lateinit var recyclerview:RecyclerView
    private lateinit var tv_cancle:TextView


    private var commentSuccessReceiver: CommentSuccessReceiver? = null
    private var filter: IntentFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mvvideoavtivity)

        init()
        initPannel()

        initEmotionMainFragment()

        initData();
        initEvent();
    }

    private fun init() {
        val intent = intent
        val bundle = intent.getBundleExtra("mvdate")


        share = findViewById(R.id.share) as ImageView
        report = findViewById(R.id.report) as ImageView
        btn_send_mvcomment = findViewById(R.id.btn_send_mvcomment)as Button
        et_pinglun = findViewById(R.id.et_pinglun) as TextView

        mv_music_name = findViewById(R.id.mv_music_name) as TextView
        mv_update = findViewById(R.id.mv_update) as TextView
        mv_playnumber = findViewById(R.id.mv_playnumber) as TextView
        mv_user_photo = findViewById(R.id.mv_user_photo) as CircleImageView
        mv_user_name = findViewById(R.id.mv_user_name) as TextView
        //礼物
        mv_gift_rl = findViewById(R.id.mv_gift_rl) as RelativeLayout

        mNiceVideoPlayer = findViewById(R.id.nice_video_player) as NiceVideoPlayer

        registerCommentReceiver()


        bioashi = bundle.getString("bioashi") as String
        if (bioashi == "2") {
            urlid = bundle.getInt("mymv") as Int
            mvurl = bundle.getString("mvurl") as String
            uid = bundle.getInt("uid") as Int

            title = bundle.getString("title") as String
            nickname = bundle.getString("nickname") as String
            imgpic_link = bundle.getString("imgpic_link") as String
            mvdetails(urlid)
            Log.i("mmmmm", "" + mvurl.toString())
            if(mvurl==null||mvurl.equals("")){

            }else{
                if(null==mvurl||mvurl.equals("")){

                }else{
                    mNiceVideoPlayer!!.setUp(mvurl, null)
                }

            }
            //  进入用户中心
            mv_user_photo.setOnClickListener {
                val intent = Intent(this@MvVideoActivitykt, MusicIanDetailsActivity::class.java)
                intent.putExtra(MusicIanDetailsActivity.MUSICIAN_ID, uid.toString() + "")
                startActivity(intent)
            }


        } else if(bioashi == "1"){
            urlid = bundle.getInt("mv") as Int
            mvurl = bundle.getString("mvurl") as String
            uid = bundle.getInt("uid") as Int

            title = bundle.getString("title") as String
            nickname = bundle.getString("nickname") as String
            imgpic_link = bundle.getString("imgpic_link") as String
            mvdetails(urlid)
            Log.i("mmmmm", "" + mvurl.toString())
            if(mvurl==null||mvurl.equals("")){

            }else{
                if(null==mvurl||mvurl.equals("")){

                }else{
                    mNiceVideoPlayer!!.setUp(mvurl, null)
                }

            }
            //  进入用户中心
            mv_user_photo.setOnClickListener {
                val intent = Intent(this@MvVideoActivitykt, MusicIanDetailsActivity::class.java)
                intent.putExtra(MusicIanDetailsActivity.MUSICIAN_ID, uid.toString() + "")
                startActivity(intent)
            }
            //如果视频正在播放，就暂停音乐请求增加播放数量的接口
            NetWork.getMvnumber(this@MvVideoActivitykt, urlid, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    Log.e("kkkk",""+resultData)
                }

                override fun doError(msg: String) {

                }

                override fun doResult() {

                }
            })

        }
        //获取数据


        //mNiceVideoPlayer.setUp("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4", null);
        val controller = NiceVideoPlayerController(this)
        controller.setTitle("")
        //controller.setImage("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg");//封面图
        mNiceVideoPlayer!!.setController(controller)
        mNiceVideoPlayer!!.start()


        if (MediaService.mediaPlayer != null) {
            MediaService.mediaPlayer.pause()
        } else {

        }

        back = findViewById(R.id.back) as ImageView
        back = findViewById(R.id.back) as ImageView
        back.setOnClickListener {
            if (mNiceVideoPlayer!!.isFullScreen) {
                mNiceVideoPlayer!!.exitFullScreen()
            } else if (mNiceVideoPlayer!!.isTinyWindow) {
                mNiceVideoPlayer!!.exitTinyWindow()
            } else {
                this@MvVideoActivitykt.finish()
            }
        }
        //分享和举报
        share.setOnClickListener {
            Log.i("qqqqq","111")
            MobclickAgent.onEvent(this@MvVideoActivitykt, "player_share");
            shareMusic()
        }
        report.setOnClickListener {
            Log.i("qqqqq","22")
            if (CacheUtils.getBoolean(this@MvVideoActivitykt, Constants.User.IS_LOGIN, false)) {
//                val reportOperationDialog = ReportOperationDialog(musicBean!!)
//                reportOperationDialog.show(this@MvVideoActivitykt.supportFragmentManager, "mReportOperationDialog")
                 if (CacheUtils.getBoolean(this@MvVideoActivitykt, Constants.User.IS_LOGIN, false)) {
//                            val reportOperationDialog = ReportOperationDialog(musicBean!!)
//                            reportOperationDialog.show(mContext.FragmentTransaction, "mReportOperationDialog")

//                            if (null == musicBean) {
//                                return@setOnClickListener
//                            }
                            NetWork.getFeedback(this@MvVideoActivitykt, 1.toString() + "", object : NetWork.TokenCallBack {
                                override fun doNext(resultData: String, headers: Headers?) {
                                    val reportOperationBean = JSON.parseObject(resultData, ReportOperationBean::class.java)
//                                    refreshData(reportOperationBean)
                                    reportOperationBeanList.clear()
                                    reportOperationBeanList.addAll(reportOperationBean.data!!)
                                    reportOperationAdapter.notifyDataSetChanged()
                                }

                                override fun doError(msg: String) {

                                }

                                override fun doResult() {

                                }
                            })

                            dialog = Dialog(this@MvVideoActivitykt, R.style.ActionSheetDialogStyle)
                            //填充对话框的布局
                            inflate = LayoutInflater.from(this@MvVideoActivitykt).inflate(R.layout.dialog_report_operation, null)
                            //初始化控件
                            recyclerview = inflate.run { this!!.findViewById<RecyclerView>(R.id.recyclerview) as RecyclerView }
                            tv_cancle = inflate.run { this!!.findViewById<TextView>(R.id.tv_cancle) as TextView }
                            recyclerview!!.layoutManager = LinearLayoutManager(this@MvVideoActivitykt)
                            reportOperationAdapter = ReportOperationAdapter(reportOperationBeanList, this@MvVideoActivitykt)
                            recyclerview!!.adapter = reportOperationAdapter
                            reportOperationAdapter.onItemClickListener = com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
//                                 if (null == musicBean) {
//                                     return@OnItemClickListener
//                                 }
                                 if (reportOperationBeanList[position].id == 0) {
                                 } else {
                                     var intent = Intent(this@MvVideoActivitykt, ReportActivity::class.java)
                                     val bundle = Bundle()
                                     bundle.putInt(ReportActivity.REPORT_ITEM_ID, urlid)
                                     bundle.putInt(ReportActivity.REPORT_PID, reportOperationBeanList[position].id)
//                                     bundle.putInt(ReportActivity.REPORT_PID, 1)
                                     intent.putExtras(bundle);
                                     this@MvVideoActivitykt.startActivity(intent)
        //                                    goActivity(ReportActivity::class.java, bundle)
                                 }
                             }
                            //将布局设置给Dialog
                            dialog!!.setContentView(inflate)
                            //获取当前Activity所在的窗体
                            val dialogWindow1 = dialog!!.window
                            //设置Dialog从窗体底部弹出
                            dialogWindow1!!.setGravity(Gravity.BOTTOM)
                            //获得窗体的属性
                            val lp1 = dialogWindow1.attributes
                            lp1.y = 5//设置Dialog距离底部的距离
                            wm = this@MvVideoActivitykt.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                            val m1 = wm;
                            val d1 = m1.getDefaultDisplay() // 获取屏幕宽、高度
                            val p1 = dialogWindow1.attributes // 获取对话框当前的参数值
                            p1.height = (d1.getHeight() * 0.6).toInt() // 高度设置为屏幕的0.6，根据实际情况调整
                            p1.width = (d1.getWidth() * 1).toInt() // 宽度设置为屏幕的0.65，根据实际情况调整
                            //    将属性设置给窗体
                            dialogWindow1.attributes = lp1
                            dialog!!.show()//显示对话框
                            tv_cancle.setOnClickListener { dialog!!.cancel() }

                        } else {
                            var intent = Intent(this@MvVideoActivitykt, LoginRegMainPage::class.java)
                            this@MvVideoActivitykt.startActivity(intent)
                        }


            } else {
                val intent = Intent(this@MvVideoActivitykt, LoginRegMainPage::class.java)
                startActivity(intent)
            }
        }

        btn_send_mvcomment.setOnClickListener {
            NetWork.addMvComment(this@MvVideoActivitykt, urlid, 1, et_pinglun.getText().toString(), object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
//                    setSnackBar("评论成功", "", R.drawable.icon_success)
                    val commentBean: Comment.DataBean
                    val jsonObject1 = JSON.parseObject(resultData)
                    commentBean = jsonObject1.getObject("data", Comment.DataBean::class.java)
                    et_pinglun.setText("")
                    //                        detialBean.setComment(detialBean.getComment() + 1);
                    dataList.add(0, commentBean)
                    adapter.notifyDataSetChanged()
                    if (recycler_comment.getScrollState() === RecyclerView.SCROLL_STATE_IDLE && recycler_comment.isComputingLayout() === false) {
                        adapter.removeAllFooterView()
                    }
                }

                override fun doError(msg: String) {

                }

                override fun doResult() {

                }
            })
        }

//        RxView.clicks(comment).subscribe {
//            MobclickAgent.onEvent(this@Mv, "player_comment");
//            if (MediaService.bean != null) {
//                val intent = Intent(this, CommentActivity::class.java)
//                intent.putExtra(CommentActivity.TYPE, 1)
//                intent.putExtra(CommentActivity.ITEM_ID, MediaService.bean!!.id)
//                startActivity(intent)
//            }
//        }
    }

    override fun onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) {
            return
        }

        /**
         * 判断是否拦截返回键操作
         */
        if (!emotionMainFragment!!.isInterceptBackPress) {
            finish()
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        mNiceVideoPlayer!!.pause()
    }

    override fun onRestart() {
        super.onRestart()
        mNiceVideoPlayer!!.restart()
    }

    override fun onResume() {
        super.onResume()
        giftListDialog?.let {
            if (giftListDialog!!.isAdded) {
                giftListDialog!!.dismiss()
            }
        }
        if (isReGiveGift && gift_id != 0) {
            checkStatus(gift_id)
        }


    }

    private fun initPannel() {
        val animationDrawable = img_gift.drawable as AnimationDrawable
        animationDrawable.start()
        rightPanel2.setOnPanelListener(object : Panel.OnPanelListener {
            override fun onPanelClosed(panel: Panel) {
                bck.visibility = View.GONE
            }

            override fun onPanelOpened(panel: Panel) {
                MobclickAgent.onEvent(this@MvVideoActivitykt, "player_gift");

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
                    val intent = Intent(this@MvVideoActivitykt, GiftChartsActivity::class.java)
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
        mv_gift_rl.setOnClickListener {
            rightPanel2.setOpen(false, false)
            showBotomGiftDialog()
        }

    }

    private var switcherList: ArrayList<MemberGiftListBean.DataBean.DataListBean> = ArrayList()

    private fun getGiftList() {
//        if (MediaService.bean != null && MediaService.bean?.isLocalMusic == false) {
            rightPanel2.visibility = View.VISIBLE
            tv_musician_name.text = MediaService.bean?.nickname
            MediaService.bean?.head_link?.let {
                Glide.with(this@MvVideoActivitykt).load(it).into(switcher_head_icon)
            }
            recycler_switcher.layoutManager = LinearLayoutManager(this)
            NetWork.getMemberGiftList(this, HttpParams("id", MediaService.bean?.uid.toString()), object : NetWork.TokenCallBack {
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
                        var switcherAdapter = SwitcherAdapter(this@MvVideoActivitykt, dataList)
                        recycler_switcher.adapter = switcherAdapter
                    } else {
                        tv_switcher_no_gift.visibility = View.VISIBLE
                        recycler_switcher.visibility = View.GONE
                    }
                }

            })
            GetMusicInfo.getMusicDetial(this, MediaService.bean!!.id, object : GetMusicInfo.SetBeanData {
                override fun setBeanData(dataBean: MusicInfo.DataBean?) {
                    if (dataBean != null) {
                        MediaService.bean = dataBean
                    }
                    if (MediaService.bean!!.comment!! > 99) {
                        num_comment.text = "99+"
                    } else {
//                        num_comment.text = MediaService.bean!!.comment.toString() + ""
                    }
                    if (MediaService.bean!!.collection == 0) {
//                        collect.setImageResource(R.drawable.fullplay_like_normal)
                    } else {
                        collect.setImageResource(R.drawable.fullplay_like_red)
                    }
                }
            })
//        } else {
//            rightPanel2.visibility = View.GONE
//        }
    }

    private fun showBotomGiftDialog() {
        giftListDialog = BotomGiftListDialog()
        giftListDialog?.music_id = if (MediaService.bean == null) 0 else MediaService.bean!!.id
        if (CacheUtils.getBoolean(this, Constants.User.IS_LOGIN)) {
            giftListDialog?.show(supportFragmentManager, "giftListDialog")
        } else {
            val intent = Intent(this@MvVideoActivitykt, LoginRegMainPage::class.java)
            startActivity(intent)
        }
    }

    private fun initData() {
        val params = HttpParams()
        params.put("type", 1.toString())
        params.put("item_id", urlid.toString() + "")
//        params.put("p", page.toString() + "")
//        params.put("row", 20.toString() + "")
//        params.put("order", "create_time-desc")
        NetWork.getCommentList(page == 1, this, params, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                swipe_refresh.isRefreshing = false
                interfaceRefreshLoadMore.setStopRefreshing()
                val comment = JSON.parseObject(resultData, Comment::class.java)
                if (comment != null) {
                    val data = comment.data
                    if (data != null && data.isNotEmpty()) {
                        if (recycler_comment.scrollState === RecyclerView.SCROLL_STATE_IDLE && recycler_comment.isComputingLayout === false) {
                            adapter.removeAllFooterView()
                        }
                        if (page == 1) {
                            dataList.clear()
                        }
                        dataList.addAll(data)
                        adapter.setNewData(dataList)
                        adapter.loadMoreComplete()
                    } else {
                        if (page == 1) {
                            if (adapter.footerLayoutCount == 0) {
                                adapter.addFooterView(LayoutInflater.from(this@MvVideoActivitykt).inflate(R.layout.no_comment_layout, null))
                                swipe_refresh.setLoadMore(false)
                            }
                        } else {
                            adapter.loadMoreEnd()
                            adapter.setEnableLoadMore(false)
                            if (adapter.footerLayoutCount == 0) {
                                adapter.addFooterView(LayoutInflater.from(this@MvVideoActivitykt).inflate(R.layout.no_more_data_text, null))
                            }
                        }
                    }
                }
            }

            override fun doError(msg: String) {
                interfaceRefreshLoadMore.setStopRefreshing()
                //hideLoadingView()
            }

            override fun doResult() {

            }
        })
    }

    private  fun initEvent() {
        adapter = CommentAdapte(1, dataList, getSupportFragmentManager())
        recycler_comment.layoutManager = LinearLayoutManager(this)
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        adapter.setEnableLoadMore(true)
        recycler_comment.adapter = adapter
        adapter.setOnItemClickListener { _, _, position ->
            val detialBean = dataList[position]
            if (detialBean != null) {
                Log.e(BaseActivity.TAG, "onItemClick: " + detialBean.nickname!!)
                val intent = Intent(this@MvVideoActivitykt, CommenDetialActivity::class.java)
                val bundle = Bundle()
                bundle.putInt("item_id", detialBean.item_id)
                bundle.putInt("fid", detialBean.id)
                bundle.putInt("type", type)
                bundle.putInt("pid", detialBean.id)
                bundle.putSerializable("parentbean", detialBean)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }

        interfaceRefreshLoadMore = InterfaceRefreshLoadMore(swipe_refresh, this, object : InterfaceRefreshLoadMore.RefreshLoadMoreCallback {
            override fun onRefresh() {
                page = 1
                initData()
            }

            override fun onLoadMore() {
                page++
                initData()

            }

            override fun onPushDistance(distance: Int) {

            }

            override fun onPullDistance(distance: Int) {

            }
        })

//        RxView.clicks(btn_comment).throttleFirst(3, TimeUnit.SECONDS).subscribe {
//            Log.e(BaseActivity.TAG, "onViewClicked: $item_id")
//            NetWork.addComment(this@MvVideoActivitykt, 1, item_id, et_pinglun.getText().toString(), 0, 0, object : NetWork.TokenCallBack {
//                override fun doNext(resultData: String, headers: Headers?) {
//                    setSnackBar("评论成功", "", R.drawable.icon_success)
//                    val commentBean: Comment.DataBean
//                    val jsonObject1 = JSON.parseObject(resultData)
//                    commentBean = jsonObject1.getObject("data", Comment.DataBean::class.java)
//                    et_pinglun.setText("")
//                    //                        detialBean.setComment(detialBean.getComment() + 1);
//                    dataList.add(0, commentBean)
//                    adapter.notifyDataSetChanged()
//                    if (recycler_comment.getScrollState() === RecyclerView.SCROLL_STATE_IDLE && recycler_comment.isComputingLayout() === false) {
//                        adapter.removeAllFooterView()
//                    }
//                }
//
//                override fun doError(msg: String) {
//
//                }
//
//                override fun doResult() {
//
//                }
//            })
//        }
    }
    /**
     * 初始化表情面板
     */
    fun initEmotionMainFragment() {
        //构建传递参数
        val bundle = Bundle()
        //绑定主内容编辑框
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, true)
        //隐藏控件
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, false)
        bundle.putInt(EmotionKeyboard.COMMENT_TYPE, 1)
//        bundle.putInt(EmotionKeyboard.COMMENT_ITEM_ID, datedate.id)
        bundle.putInt(EmotionKeyboard.COMMENT_ITEM_ID, urlid)
        bundle.putInt(EmotionKeyboard.COMMENT_PID, 0)
        bundle.putInt(EmotionKeyboard.COMMENT_FID, 0)
        //替换fragment
        //创建修改实例
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment::class.java, bundle)
        emotionMainFragment!!.bindToContentView(et_pinglun)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment)
        transaction.addToBackStack(null)
        //提交修改
        transaction.commit()
    }


    companion object {
        const val TYPE = "type"
        const val ITEM_ID = "item_id"
    }


    /*检查能否送礼物*/
    fun checkStatus(gift_id: Int) {
//        showLoadingView()
        //gift_id开始是0，当点击弹框里面的礼物，此方法第一次调用并且给gift_id赋值
        this.gift_id = gift_id
        val params = HttpParams()
        Log.e("urlid",""+urlid)
        params.put("music_id", urlid.toString() + "")
        params.put("gift_id", gift_id.toString() + "")
//        params.put("source", "app")
        NetWork.checkGift(this, params, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
//                hideLoadingView()
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
                            FirstChargeDialog.newInstance().showDialog(this@MvVideoActivitykt, object : FirstChargeDialog.onBtClick {
                                override fun onConfirm() {
                                    startActivity(Intent(this@MvVideoActivitykt, ChargeDoughnutActivity::class.java))
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
                //hideLoadingView()
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
                    .showDialog(this@MvVideoActivitykt, object : ConfirmGiveGiftDialog.onBtClick {
                        override fun onConfirm() {
                            //showLoadingView()
                            val lastPayType = dataBean.last_pay_type
                            isReGiveGift = true
                            when (lastPayType) {
                                0 -> PayUtils.pay(this@MvVideoActivitykt, dLlists.id, 3)
                                1 -> PayUtils.pay(this@MvVideoActivitykt, dLlists.id, 1)
                                2 -> PayUtils.pay(this@MvVideoActivitykt, dLlists.id, 2)
                                3 -> PayUtils.pay(this@MvVideoActivitykt, dLlists.id, 3)
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
                .showDialog(this@MvVideoActivitykt, object : ConfirmGiveGiftDialog.onBtClick {
                    override fun onConfirm() {
                        val params = HttpParams()
                        params.put("music_id", urlid.toString())
                        params.put("gift_id", dataBean.gift_id.toString() + "")
                        params.put("source", "source")
                        NetWork.giveGift(this@MvVideoActivitykt, params, object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val bundle = Bundle()
                                bundle.putSerializable("GIFT_BEAN", dataBean)
                                val intent = Intent(this@MvVideoActivitykt, GiiGiftSuccessActivity::class.java)
                                intent.putExtras(bundle);
                                startActivity(intent)
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


    //分享和举报
    private fun shareMusic() {
        if (NetConnectedUtils.isNetConnected(applicationContext)) {
            if (CacheUtils.getBoolean(this@MvVideoActivitykt, Constants.User.IS_LOGIN, false)) {
//            if (CacheUtils.getBoolean(this@MvVideoActivitykt, Constants.User.IS_LOGIN, false) && MediaService.bean != null) {
                val musicBean = MusicBean()
                val shareDataBean = MusicBean.ShareDataBean()
                shareDataBean.type = "mv"
//                if (MediaService.bean!!.video_info != null) {
                    shareDataBean.mv_link = mvDetails!!.mv_info.link
//                }
                try {
                    shareDataBean.image_link = imgpic_link
                    shareDataBean.webImgUrl = imgpic_link
                } catch (e: RuntimeException) {
                }

                shareDataBean.uid = uid
                shareDataBean.muisic_id = urlid

                shareDataBean.nickname = nickname
                shareDataBean.title = title
                shareDataBean.mv = mvurl

                shareDataBean.share_link = mvDetails!!.mv_share_url
//                shareDataBean.share_link = ""
                musicBean.setShareDataBean(shareDataBean)
                val shareBottomDialog = ShareBottomDialog(this, musicBean)
                shareBottomDialog.show()
            } else {
                val intent = Intent(this@MvVideoActivitykt, LoginRegMainPage::class.java)
                startActivity(intent)
            }
        }
    }


    private fun mvdetails(id :Int){
        NetWork.mvdetails(this@MvVideoActivitykt, id, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {

                val jsonObject1 = JSON.parseObject(resultData)
                mvDetails = jsonObject1.getObject("data", MvDetails.DataBean::class.java)
                Log.i("rrrrrr",""+mvDetails.toString())
                val controlle = NiceVideoPlayerController(this@MvVideoActivitykt)
                controlle.setTitle(mvDetails!!.title)//标题栏的标题
                mv_music_name!!.text = mvDetails!!.title
                mv_user_name!!.text = mvDetails!!.nickname
                mv_playnumber!!.text = mvDetails!!.mv_counts.toString()//播放量
                mv_update!!.text = TimeUtils.getFetureDate(mvDetails!!.create_time.toLong())//发布时间

                //头像
                Glide.with(this@MvVideoActivitykt)
                        .load(mvDetails!!.head_link)
                        .into(mv_user_photo);
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

//    //返回键的监听
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (mNiceVideoPlayer.isFullScreen) {
//
//        } else if (mNiceVideoPlayer.isNormal) {
//            this@MvVideoActivitykt.finish()
//        } else {
//
//        }
//        return super.onKeyDown(keyCode, event)
//    }

    override fun onDestroy() {
        super.onDestroy()
        interfaceRefreshLoadMore.resetRefreshView()
        unregisterReceiver(commentSuccessReceiver)
    }
    private fun registerCommentReceiver() {
        commentSuccessReceiver = CommentSuccessReceiver(object : CommentSuccessReceiver.SuccessCallBack {
            override fun callBack(json: String, intent: Intent) {
                adapter.removeAllFooterView()
//                setSnackBar("发表成功", "", R.drawable.icon_success)
                if (intent.getIntExtra(EmotionKeyboard.COMMENT_PID, 0) == 0) {
                    val commentBean: Comment.DataBean
                    val jsonObject1 = JSON.parseObject(json)
                    commentBean = jsonObject1.getObject("data", Comment.DataBean::class.java)
                    dataList.add(0, commentBean)
                    adapter.setNewData(dataList)
                } else {
                    val pid = intent.getIntExtra(EmotionKeyboard.COMMENT_PID, 0)
                    val jsonObject1 = JSON.parseObject(json)
                    if (jsonObject1 != null) {
                        val data = jsonObject1.getJSONObject("data")
                        val jsonString = data.toJSONString()
                        val sonBean = JSON.parseObject(jsonString, Comment.DataBean.SonBean::class.java)
                        for (i in dataList.indices) {
                            val dateBean = dataList[i]
                            if (dateBean.id == sonBean!!.fid) {
                                var son: ArrayList<Comment.DataBean.SonBean>? = dateBean.son
                                if (son == null) {
                                    son = ArrayList<Comment.DataBean.SonBean>()
                                }
                                if (sonBean != null) {
                                    son.add(0, sonBean)
                                    for (i1 in 0 until son.size - 1) {
                                        for (j in son.size - 1 downTo i1 + 1) {
                                            if (son[j].id == son[i1].id) {
                                                son.removeAt(j)
                                            }
                                        }
                                    }
                                }
                                dateBean.son = son
                                dataList[i] = dateBean
                                adapter.notifyDataSetChanged()
                                break
                            }
                        }
                    }
                }
            }
        })
        filter = IntentFilter(EmotionKeyboard.COMMENT_SUCCESS)
        registerReceiver(commentSuccessReceiver, filter)
    }
}
