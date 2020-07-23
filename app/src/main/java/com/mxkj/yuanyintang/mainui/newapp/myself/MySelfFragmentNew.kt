package com.mxkj.yuanyintang.mainui.newapp.myself

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.WindowManager
import android.widget.*
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import com.linsh.lshutils.utils.LshContextUtils
import com.liulishuo.filedownloader.util.FileDownloadUtils
import com.lzy.okgo.model.HttpParams
import com.lzy.okgo.utils.HttpUtils.runOnUiThread
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.R.layout.dialog
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.base.bean.UserInfo
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog
import com.mxkj.yuanyintang.base.dialog.BaseVerificationCodeDialog
import com.mxkj.yuanyintang.base.fragment.BaseFragment
import com.mxkj.yuanyintang.database.updatafilesql.UpDataFileBean
import com.mxkj.yuanyintang.database.updatafilesql.UpDataFileDao
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.data.Constant
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil
import com.mxkj.yuanyintang.mainui.myself.activity.*
import com.mxkj.yuanyintang.mainui.myself.bean.MyDoughnutBean
import com.mxkj.yuanyintang.mainui.myself.bean.MySongListBean
import com.mxkj.yuanyintang.mainui.myself.celebrity.RealInfoAuthStep1
import com.mxkj.yuanyintang.mainui.myself.doughnut.CountDownView
import com.mxkj.yuanyintang.mainui.myself.doughnut.MyDoughnutActivityNew
import com.mxkj.yuanyintang.mainui.myself.helpcenter.HelpCenterActivity
import com.mxkj.yuanyintang.mainui.myself.my_income.activity.MyIncomeActivity
import com.mxkj.yuanyintang.mainui.myself.my_release.MyReleaseActivity
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.Song
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.SongDao
import com.mxkj.yuanyintang.mainui.myself.settings.SettingActivity
import com.mxkj.yuanyintang.mainui.myself.settings.activity.NoMobile_goBind_Activity
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.app.CountDownTimerUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.constant.Constants.Other.EM_LOGIN
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.rxbus.RxBus
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import com.mxkj.yuanyintang.utils.uiutils.Toast
import com.umeng.analytics.MobclickAgent
import com.umeng.socialize.UmengTool.showDialog
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.frag_myself_head.*
import kotlinx.android.synthetic.main.frag_myself_head.view.*
import kotlinx.android.synthetic.main.frag_myself_menue.view.*
import okhttp3.Headers
import java.io.File
import java.util.concurrent.TimeUnit

open class MySelfFragmentNew : BaseFragment() {
    private var userInfos: UserInfo? = null
    private var homeActivity: HomeActivity? = null
    private var header: View? = null



    internal var llCharge: RelativeLayout?=null
    internal var countDown: CountDownView? = null
    private var page = 1
    internal var h = 0
    internal var d = 0
    internal var s = 0
    internal lateinit var interfaceRefreshLoadMore: InterfaceRefreshLoadMore
    private var firstDataMonth = ""
    private var isNotify: Boolean = false
    private var dataBean: TimeLimitPreferential.DataBean? = null



    override val layoutId: Int
        get() {
            StatusBarUtil.baseTransparentStatusBar(activity)
            return R.layout.fragment_my_self_new
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeActivity = activity as HomeActivity
        initView()
    }

    var list = ArrayList<Song>()
    private fun initView() {

        rootView?.let {
            /*设置*/
            RxView.clicks(it.ivSetting).throttleFirst(1, TimeUnit.SECONDS).subscribe {
                MobclickAgent.onEvent(activity,"mine_setting");
                goActivity(SettingActivity::class.java) }
            /*个人中心*/
            RxView.clicks(it.layout_personal)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        MobclickAgent.onEvent(activity, "mine_icon");

                        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                            if (null != userInfo && null != userInfos?.data) {
                                val bundle = Bundle()
                                bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, userInfos?.data!!.id.toString() + "")
                                goActivity(MusicIanDetailsActivity::class.java, bundle)
                            }
                        } else {
                            goActivity(LoginRegMainPage::class.java)
                        }
                    }
            /*动态*/
            RxView.clicks(it.ll_topic)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        MobclickAgent.onEvent(activity, "mine_dynamic");

                        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                            if (null != userInfo && null != userInfos?.data) {
                                val bundle = Bundle()
                                bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 3)
                                bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, userInfos?.data!!.id.toString() + "")
                                goActivity(MusicIanDetailsActivity::class.java, bundle)
                            }
                        } else {
                            goActivity(LoginRegMainPage::class.java)
                        }
                    }
            /*关注*/
            RxView.clicks(it.tv_follow_num)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(Consumer {
                        MobclickAgent.onEvent(activity, "mine_follow");

                        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                            if (null == userInfo) {
                                return@Consumer
                            }

                            val bundle = Bundle()
                            bundle.putString(MyFollowAndFansActivity.TITLE_NAME, "我的关注")
                            bundle.putString(MyFollowAndFansActivity.TYPE, "follow")
                            bundle.putString(MyFollowAndFansActivity.NOT_DATA_TEXT, "您还没有关注哦~")
                            bundle.putString(MyFollowAndFansActivity.TO_CLICK_TEXT, "去和音乐人互动")
                            bundle.putInt(MyFollowAndFansActivity.COUNT, userInfos?.data!!.count!!.attention)
                            goActivity(MyFollowAndFansActivity::class.java, bundle)
                        } else {
                            goActivity(LoginRegMainPage::class.java)
                        }
                    })
            /*粉丝*/
            RxView.clicks(it.tv_fans_num)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(Consumer {
                        MobclickAgent.onEvent(activity, "mine_fans");

                        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                            if (null == userInfo) {
                                return@Consumer
                            }
                            val bundle = Bundle()
                            bundle.putString(MyFollowAndFansActivity.TITLE_NAME, "我的粉丝")
                            bundle.putString(MyFollowAndFansActivity.TYPE, "fans")
                            bundle.putString(MyFollowAndFansActivity.NOT_DATA_TEXT, "您还没有粉丝哦~")
                            bundle.putString(MyFollowAndFansActivity.TO_CLICK_TEXT, "去冒泡")
                            bundle.putInt(MyFollowAndFansActivity.COUNT, userInfos?.data!!.count!!.fans)
                            goActivity(MyFollowAndFansActivity::class.java, bundle)

                        } else {
                            goActivity(LoginRegMainPage::class.java)
                        }
                    })
            /*甜甜圈*/
            RxView.clicks(it.layout_doughnut)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        MobclickAgent.onEvent(activity,"mine_doughnut");
                        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                            goActivity(MyDoughnutActivityNew::class.java)
                        } else {
                            goActivity(LoginRegMainPage::class.java)
                        }
                    }
            /*我喜欢的*/
            RxView.clicks(it.myCollection)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe({
                        MobclickAgent.onEvent(activity,"mine_collection");

                        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                            goActivity(MyCollectionActivity::class.java, true)
                        } else {
                            goActivity(LoginRegMainPage::class.java)
                        }
                    })

            /*下载*/
            RxView.clicks(it.download_layout)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        MobclickAgent.onEvent(activity,"mine_download");
                        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                            goActivity(DownLoadFileListActivity::class.java)
                        } else {
                            goActivity(LoginRegMainPage::class.java)
                        }
                    }
            /*播放历史*/
            RxView.clicks(it.playHistory)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        MobclickAgent.onEvent(activity,"mine_play_histroy");
                        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                            goActivity(PlayerHistoryActivity::class.java, true)
                        } else {
                            goActivity(LoginRegMainPage::class.java)
                        }
                    }
            /*我创建的歌单*/
            RxView.clicks(it.mySongList)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        MobclickAgent.onEvent(activity,"mine_create_album");
                        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                            val bundle = Bundle()
                            bundle.putString("type", "1")
                            goActivity(MySongListActivity::class.java, bundle)
                        } else {
                            goActivity(LoginRegMainPage::class.java)
                        }
                    }
            /*我收藏的歌单*/
            RxView.clicks(it.myLikeSongList)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        MobclickAgent.onEvent(activity,"mine_collect_album");

                        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                            val bundle = Bundle()
                            bundle.putString("type", "2")
                            goActivity(MySongListActivity::class.java, bundle)
                        } else {
                            goActivity(LoginRegMainPage::class.java)
                        }
                    }

            /*小鱼干*/
            RxView.clicks(it.myFish)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        MobclickAgent.onEvent(activity,"mine_fish");

                        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                            goActivity(MyIncomeActivity::class.java, true)
                        } else {
                            goActivity(LoginRegMainPage::class.java)
                        }
                    }

            /*实名认证*/
            RxView.clicks(it.toAuth)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        MobclickAgent.onEvent(activity,"mine_realname");
                        val json = CacheUtils.getString(context, Constants.User.USER_JSON)
                        if (CacheUtils.getBoolean(context, Constants.User.IS_LOGIN)) {
                            UserInfoUtil.getUserInfoByJson(json) { infoBean ->
                                if (infoBean?.data != null) {
                                    isUserEvent(infoBean, 0)
                                }
                            }
                        } else {
                            goActivity(LoginRegMainPage::class.java)
                        }
                    }
            /*本地音乐*/
            RxView.clicks(it.llLocalMusic)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        MobclickAgent.onEvent(activity,"mine_local_music");

                        goActivity(LocalMusicActivity::class.java)
                    }

            /*我的发布*/
            RxView.clicks(it.toRelease)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {

                        MobclickAgent.onEvent(activity,"mine_realname");
                        val json1 = CacheUtils.getString(context, Constants.User.USER_JSON)
                        if (CacheUtils.getBoolean(context, Constants.User.IS_LOGIN)) {
                            goActivity(MyReleaseActivity::class.java)
//                            UserInfoUtil.getUserInfoByJson(json1) { infoBean ->
//                                if (infoBean?.data != null) {
//                                    if (infoBean != null) {
//                                        if (infoBean.data != null && TextUtils.isEmpty(infoBean.data!!.mobile)) {
//                                            BaseConfirmDialog.newInstance().title("未绑定手机").content("您还没有绑定手机\n\n为了您的账号安全\n请绑定后再认证哟").confirmText("去绑定").isOneBtn(true).showDialog(activity, object : BaseConfirmDialog.onBtClick {
//                                                override fun onConfirm() {
//                                                    goActivity(NoMobile_goBind_Activity::class.java)
//                                                }
//
//                                                override fun onCancle() {
//
//                                                }
//                                            })
//
//                                        }
//                                    }
//                                    val is_auth = infoBean!!.data!!.is_auth
//                                    if (is_auth == 0) {
//                                        BaseConfirmDialog.newInstance().title("未完善资料").content("您还没有完善账号资料\n\n为了您的账号安全\n请完成后再实名认证哟").confirmText("去完善").isOneBtn(true).showDialog(activity, object : BaseConfirmDialog.onBtClick {
//                                            override fun onConfirm() {
//                                                val userInfo = getUserInfos()
//                                                if (null == userInfo) {
//                                                    goActivity(LoginRegMainPage::class.java)
//                                                    return
//                                                }
//                                                val bundle2 = Bundle()
//                                                bundle2.putSerializable(EditPersonalProfileActivity.DATA, userInfo)
//                                                goActivity(EditPersonalProfileActivity::class.java, bundle2)
//                                            }
//
//                                            override fun onCancle() {
//
//                                            }
//                                        })
//                                    } else {
////            goActivity(RealInfoAuthStep1::class.java)
//                                        goActivity(MyReleaseActivity::class.java)
//                                    }
//                                }
//                            }
                        } else {
                            goActivity(LoginRegMainPage::class.java)
                        }

//                        MobclickAgent.onEvent(activity,"mine_release");
//                        val json = CacheUtils.getString(context, Constants.User.USER_JSON)
//                        if (CacheUtils.getBoolean(context, Constants.User.IS_LOGIN)) {
//                            UserInfoUtil.getUserInfoByJson(json) { infoBean ->
//                                if (infoBean?.data != null) {
//                                    if (infoBean.data!!.is_auth != 3) {
//                                        BaseConfirmDialog.newInstance().title("未实名认证").content("您还没有实名认证\n\n发布音乐需要先完成认证哦~~").confirmText("知道了").isOneBtn(true).showDialog(activity, object : BaseConfirmDialog.onBtClick {
//                                            override fun onConfirm() {
//
//                                            }
//
//                                            override fun onCancle() {}
//                                        })
//                                    } else {
//                                        goActivity(MyReleaseActivity::class.java)
//                                    }
//                                }
//                            }
//                        } else {
//                            goActivity(LoginRegMainPage::class.java)
//                        }
                    }

            /*帮助中心*/
            RxView.clicks(it.helpCenter)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        MobclickAgent.onEvent(activity,"mine_help_center");

                        goActivity(HelpCenterActivity::class.java)
                    }
            /*意见反馈*/
            RxView.clicks(it.feedBack)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        MobclickAgent.onEvent(activity,"mine_feedback");
                        goActivity(SuggestActivity::class.java)
                    }

        }

        countDown = findViewById(R.id.count_down)
        llCharge = findViewById(R.id.ll_charge)
        initMemberData()
    }

    private var upDataFileBeanList = ArrayList<UpDataFileBean>()
    private fun getData() {
        if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
            Observable.create(ObservableOnSubscribe<Any> { e ->
                upDataFileBeanList = UpDataFileDao(MainApplication.context).isQueryDataList("success", true) as ArrayList<UpDataFileBean>
                if (upDataFileBeanList != null) {
                    e.onNext(1)
                }
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                rootView.download_num.text = upDataFileBeanList.size.toString()
            }
        }
    }

    private fun dirSize(dir: File): Long {
        if (dir.exists()) {
            var result: Long = 0
            val fileList = dir.listFiles()
            for (i in fileList.indices) {
                result += when {
                    fileList[i].isDirectory -> dirSize(fileList[i])
                    else -> fileList[i].length()
                }
            }
            return result
        }
        return 0
    }

    override fun onResume() {
        super.onResume()
        netData()
        getData()
        Observable.create(ObservableOnSubscribe<Any> { e ->
            var songDao = SongDao(activity)
            list = songDao.listHelper() as ArrayList<Song>
            if (list != null) {
                e.onNext(1)
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            rootView.local_num.text = list.size.toString()
        }
    }

    private fun netData() {
        if (!CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
            outLoginView()
            return
        }
        ll_topic.visibility = VISIBLE
        rootView.ll_topic.visibility = VISIBLE
        rootView.play_log_counts.visibility = VISIBLE
        rootView.like_num.visibility = VISIBLE
        rootView.download_num.visibility = VISIBLE
        rootView.local_num.visibility = VISIBLE
        UserInfoUtil.getUserInfoById(0, activity) { infoBean ->
            if (infoBean?.data != null) {
                RxBus.getDefault().post("resetData")
                userInfos = infoBean
                userInfos?.let {
                    ImageLoader.with(activity)
                            .override(80, 80)
                            .url(it.data?.head_link)
                            .into(rootView.civ_headimg)
                    rootView.tv_name.text = StringUtils.isEmpty(it.data?.nickname)
                    if (it.data?.count != null) {
                        rootView.tv_follow_num.text = "关注" + StringUtils.setNum(it.data?.count!!.attention)
                        rootView.tv_fans_num.text = "粉丝" + StringUtils.setNum(it.data?.count!!.fans)
                        rootView.tv_pondnum.text = "动态" + StringUtils.setNum(it.data?.count!!.topicCount)
                        rootView.iv_is_vip.visibility = if (it.data?.is_music == 3) {
                            VISIBLE
                        } else {
                            GONE
                        }
                    }
                    rootView.tv_doughnut_num.text = StringUtils.setNum(it.data?.coin_counts!!)
                    rootView.tv_fish_num.text = StringUtils.setNum(it.data?.fish!!)
                    if (it.data?.count?.playlogCount!! > 100) {
                        rootView.play_log_counts.text = StringUtils.setNum(100)
                    } else {
                        rootView.play_log_counts.text = StringUtils.setNum(it.data?.count?.playlogCount!!)
                    }
                    rootView.like_num.text = StringUtils.setNum(it.data?.count?.collection!!)
                }
            }
        }

    }

    fun outLoginView() {
        activity.runOnUiThread {
            rootView.tv_name.text = "登录源音塘"
            rootView.tv_follow_num.text = ""
            rootView.tv_pondnum.text = ""
            rootView.tv_fans_num.text = ""
            rootView.tv_doughnut_num.text = ""
            rootView.tv_fish_num.text = ""
            rootView.ll_topic.visibility = GONE
            rootView.play_log_counts.visibility = GONE
            rootView.like_num.visibility = GONE
            rootView.download_num.visibility = GONE
            rootView.local_num.visibility = GONE
            rootView.iv_is_vip.visibility = INVISIBLE
            ImageLoader.with(activity)
                    .res(R.drawable.img_mine_default)
                    .into(rootView.civ_headimg)
        }
    }


    internal lateinit var wm:WindowManager;
    internal lateinit var progressbar: ProgressBar
    internal var inflate:View? = null
    internal var dialog:Dialog? = null
    private var countDownTimerUtils: CountDownTimerUtils? = null
    private fun isUserEvent(infoBean: UserInfo?, type: Int) {
        if (infoBean != null) {
            if (infoBean.data != null && TextUtils.isEmpty(infoBean.data!!.mobile)) {
                BaseConfirmDialog.newInstance().title("未绑定手机").content("您还没有绑定手机\n\n为了您的账号安全\n请绑定后再认证哟").confirmText("去绑定").isOneBtn(true).showDialog(activity, object : BaseConfirmDialog.onBtClick {
                    override fun onConfirm() {
                        dialog = Dialog(context, R.style.ActionSheetDialogStyle)
                        //填充对话框的布局
                        inflate = LayoutInflater.from(context).inflate(R.layout.dialog_verificationcode, null)
                        //初始化控件
                        val iv_yxy = inflate.run { this!!.findViewById<ImageView>(R.id.iv_yxy) as ImageView }
                        val tv_title = inflate.run { this!!.findViewById<TextView>(R.id.tv_title) as TextView }
                        val et_cout = inflate.run { this!!.findViewById<TextView>(R.id.et_cout) as EditText }//手机号
                        val et_code = inflate.run { this!!.findViewById<TextView>(R.id.et_code) as EditText }//验证码
                        val getCode = inflate.run { this!!.findViewById<TextView>(R.id.getCode) as TextView }//发送验证码
                        val tv_confirm = inflate.run { this!!.findViewById<TextView>(R.id.tv_confirm) as TextView }
                        val tv_cancle = inflate.run { this!!.findViewById<TextView>(R.id.tv_cancle) as TextView }
                        val img_close = inflate.run { this!!.findViewById<ImageView>(R.id.img_close) as ImageView }
//                        val tv_content = inflate.run { this!!.findViewById<TextView>(R.id.tv_content) as TextView }
//                        val tv_tips = inflate.run { this!!.findViewById<TextView>(R.id.tv_tips) as TextView }
                        val tv_confirm_oneBtn = inflate.run { this!!.findViewById<TextView>(R.id.tv_confirm_oneBtn) as TextView }
                        val ll_confirm_btn = inflate.run { this!!.findViewById<LinearLayout>(R.id.ll_confirm_btn) as LinearLayout }
                        tv_title.setText("绑定手机号")
                        tv_confirm_oneBtn.setText("下一步")
                        //将布局设置给Dialog
                        dialog!!.setContentView(inflate)
                        //获取当前Activity所在的窗体
                        val dialogWindow1 = dialog!!.window
                        //设置Dialog从窗体底部弹出
                        dialogWindow1!!.setGravity(Gravity.BOTTOM)
                        //获得窗体的属性
                        val lp1 = dialogWindow1.attributes
                        lp1.y = 200//设置Dialog距离底部的距离
                        wm = LshContextUtils.getSystemService(Context.WINDOW_SERVICE) as WindowManager;
                        val m1 = wm;
                        val d1 = m1.getDefaultDisplay() // 获取屏幕宽、高度
                        val p1 = dialogWindow1.attributes // 获取对话框当前的参数值
                        p1.height = (d1.getHeight() * 0.6).toInt() // 高度设置为屏幕的0.6，根据实际情况调整
                        p1.width = (d1.getWidth() * 1).toInt() // 宽度设置为屏幕的0.65，根据实际情况调整
                        //    将属性设置给窗体
                        dialogWindow1.attributes = lp1
                        dialog!!.show()//显示对话框
//                        initData();
                        var params: HttpParams
                        getCode.setOnClickListener {
                            if (TextUtils.isEmpty(et_cout.getText())) {
                                return@setOnClickListener
                            }
                            params = HttpParams()
                            params.put("type", "addbind")
                            params.put("key", et_cout.getText().toString())
                            NetWork.getCodeNoLogin(params, activity, object : NetWork.TokenCallBack {
                                override fun doNext(resultData: String, headers: Headers?) {
                                    countDownTimerUtils = CountDownTimerUtils(getCode, 60000, 1000, "s后重新获取")
                                    countDownTimerUtils!!.start()
                                }
                                override fun doError(msg: String) {

                                }
                                override fun doResult() {

                                }
                            })
                        }
                        tv_confirm_oneBtn.setOnClickListener {
                            if (TextUtils.isEmpty(et_cout.getText()) || TextUtils.isEmpty(et_code.getText())) {
                                return@setOnClickListener
                            }
                            params = HttpParams()
                            params.put("code", et_code.getText().toString())
                            params.put("key", et_cout.getText().toString())
                            NetWork.noMoile_GoBind(params, activity, object : NetWork.TokenCallBack {
                                override fun doNext(resultData: String, headers: Headers?) {
//                                    setSnackBar("手机绑定成功!", "", R.drawable.icon_good)
                                    Log.e("zzzzzz", "" + resultData)
//                                    sendBroadcast(Intent(EM_LOGIN))
//                                    CacheUtils.setString(this@NoMobile_goBind_Activity, Constants.User.USER_JSON, resultData)
                                    dialog!!.dismiss()
                                }
                                override fun doError(msg: String) {

                                }
                                override fun doResult() {

                                }
                            })
                        }



                    }

                    override fun onCancle() {

                    }
                })
                return
            }
        }
        val is_auth = infoBean!!.data!!.is_auth
        if (is_auth == 0) {
            BaseConfirmDialog.newInstance().title("未完善资料").content("您还没有完善账号资料\n\n为了您的账号安全\n请完成后再实名认证哟").confirmText("去完善").isOneBtn(true).showDialog(activity, object : BaseConfirmDialog.onBtClick {
                override fun onConfirm() {
                    val userInfo = getUserInfos()
                    if (null == userInfo) {
                        goActivity(LoginRegMainPage::class.java)
                        return
                    }
                    val bundle2 = Bundle()
                    bundle2.putSerializable(EditPersonalProfileActivity.DATA, userInfo)
                    goActivity(EditPersonalProfileActivity::class.java, bundle2)
                }

                override fun onCancle() {

                }
            })
        } else {
            goActivity(RealInfoAuthStep1::class.java)
        }
    }

    protected fun getUserInfos(): UserInfo? {
        var userInfo: UserInfo? = null
        userInfo = JSON.parseObject(CacheUtils.getString(context, Constants.User.USER_JSON, ""), UserInfo::class.java)
        return if (null == userInfo) {
            null
        } else userInfo
    }


    private fun initMemberData() {
        val params = HttpParams()
        params.put("p", page.toString())
        NetWork.getTimeLimit(activity, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                var timeLimitPreferential: TimeLimitPreferential? = null
                timeLimitPreferential = JSON.parseObject(resultData, TimeLimitPreferential::class.java)
                dataBean = timeLimitPreferential.getData()
                if (dataBean == null) return
////                doughtNum.setText(dataBeanX.getMy_coin_counts() + "")
                val discount_surplus_second = dataBean!!.surplus_time
                if (discount_surplus_second != 0) {
                    llCharge!!.setVisibility(View.VISIBLE)
                    change(discount_surplus_second)
                    countDown!!.initTime(h.toLong(), d.toLong(), s.toLong())
                    countDown!!.start()
                    countDown!!.setOnTimeCompleteListener(CountDownView.OnTimeCompleteListener {
                        llCharge!!.setVisibility(View.GONE)
                    })
                } else {
                    llCharge!!.setVisibility(View.GONE)
                }

            }

            override fun doError(msg: String) {
//                interfaceRefreshLoadMore.setStopRefreshing()
//                if (page > 1) {
//                    page--
//                }
            }

            override fun doResult() {

            }
        })

    }

    fun change(second: Int) {
        val temp = second % 3600
        if (second > 3600) {
            h = second / 3600
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60
                    if (temp % 60 != 0) {
                        s = temp % 60
                    }
                } else {
                    s = temp
                }
            }
        } else {
            d = second / 60
            if (second % 60 != 0) {
                s = second % 60
            }
        }
    }
}
