package com.mxkj.yuanyintang.base.activity

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.IntentFilter
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.linsh.lshutils.utils.LshIntentUtils
import com.mxkj.yuanyintang.base.EaseHelper
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.bean.StartImgBean
import com.mxkj.yuanyintang.base.bean.TabEntity
import com.mxkj.yuanyintang.utils.app.NotificationsUtils
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.config.SingleConfig
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.app.UpDataApkTools
import com.mxkj.yuanyintang.mainui.home.bean.SearchRecommendBean
import com.mxkj.yuanyintang.mainui.myself.activity.NearbyPeopleActivity
import com.mxkj.yuanyintang.utils.rxbus.event.EMECodeEvent
import com.mxkj.yuanyintang.utils.rxbus.event.SearchRecommendEvent
import com.mxkj.yuanyintang.utils.rxbus.event.SelectTabChangeEvent
import com.mxkj.yuanyintang.base.service.BackgroundService
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder
import com.mxkj.yuanyintang.utils.uiutils.Toast
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.rxbus.RxBus
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber
import com.taobao.sophix.SophixManager
import com.tbruyelle.rxpermissions2.RxPermissions

import butterknife.ButterKnife
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.base.dialog.CoinDialog
import com.mxkj.yuanyintang.mainui.home.fragment.HomeFragment.Companion.SHOW_SYS_MSG
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.newapp.home.HomeFragmentNew
import com.mxkj.yuanyintang.mainui.newapp.message.MsgCenterFragNew
import com.mxkj.yuanyintang.mainui.newapp.pond.PondFrgNew
import com.mxkj.yuanyintang.mainui.newapp.myself.MySelfFragmentNew
import com.mxkj.yuanyintang.musicplayer.service.MediaService.*
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.constant.Constants.*
import io.reactivex.disposables.Disposable
import okhttp3.Headers

import com.mxkj.yuanyintang.utils.constant.Constants.Other.EM_LOGIN
import com.mxkj.yuanyintang.utils.tab.FragmentSwitchTool
import com.mxkj.yuanyintang.utils.threadpool.ThreadPoolManager
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

open class HomeActivity : BaseActivity() {
    private var mIconUnselectedIds = intArrayOf(R.drawable.icon_index_false, R.drawable.icon_index_chityang_false, R.color.transparent, R.drawable.icon_index_message_false, R.drawable.icon_infex_mine_false)
    private var mIconSelectIds = intArrayOf(R.drawable.icon_index_true, R.drawable.icon_index_chityang_true, R.color.transparent, R.drawable.icon_index_message_true, R.drawable.icon_infex_mine_true)
    private var mTitles = arrayOfNulls<String>(5)
    private var mFragments = ArrayList<Fragment>(5)
    private var mTabEntities = ArrayList<CustomTabEntity>()
    var msgUnReadNum = 0
    var lastTab = 0
    private var re: ImgReceiver? = null
    private var filter: IntentFilter? = null
    private var mSelectTabChangeEvent: Disposable? = null
    private var resetData: Disposable? = null
    private var mEMEEvent: Disposable? = null
    var searchRecommendEvent: SearchRecommendEvent? = null
    private lateinit var homeFragment: HomeFragmentNew
    private lateinit var pondFragment: PondFrgNew
    private lateinit var mySelfFragment: MySelfFragmentNew
    private var pushdialog: View? = null
    private var appExit = false
    private val handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> appExit = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ButterKnife.bind(this)
        initView()
        initEvent()
        startService(Intent(this, BackgroundService::class.java))
        startService(Intent(this, MediaService::class.java))
        //检查新版本更新下载
        UpDataApkTools.getInstance().setContext(this).upDataApkData(true, "")
        searchRecommendData()
        CacheUtils.setBoolean(this@HomeActivity, MediaService.MEDIA_PLAY_IS_PAUSE, false)
        getStartImg(true)//获取启动图
        requestPermissions()


        lotterFilter = IntentFilter()
        lotterFilter.addAction("hideCoinDialog")
        lotterFilter.addAction("showCoinDialog")
        lotterRecriver = LotterRecriver()
        registerReceiver(lotterRecriver, lotterFilter)

    }

    override fun showCoinDialog() {
        coinDialog = CoinDialog.newInstance()
        coinDialog.showDialog(this)
    }
    /**
     * 获取搜索推荐数据
     */
    private fun searchRecommendData() {
        NetWork.getSearchRecommend(this, 0L, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val searchRecommendBean = JSON.parseObject(resultData, SearchRecommendBean::class.java)
                jsonSearchHotData(searchRecommendBean)
            }

            override fun doError(msg: String) {}

            override fun doResult() {}
        })
    }

    private fun jsonSearchHotData(searchRecommendBean: SearchRecommendBean?) {
        if (null != searchRecommendBean) {
            if (null != searchRecommendBean.data && searchRecommendBean.data.size > 0) {
                var searchRecommendEvents = SearchRecommendEvent(searchRecommendBean.data[0].id, searchRecommendBean.data[0].title)
                searchRecommendEvent = searchRecommendEvents
            }
        }
    }

    private fun initEvent() {

        if (!NotificationsUtils.isNotificationEnabled(this)) {
            initPushDialog()
        }
        re = ImgReceiver()
        filter = IntentFilter()
        filter!!.addAction(CHANGE_PLAYING_IMG)
        filter!!.addAction(EM_LOGIN)
        registerReceiver(re, filter)
//        mSelectTabChangeEvent = RxBus.getDefault().toObservable(SelectTabChangeEvent::class.java).subscribeWith(object : RxBusSubscriber<SelectTabChangeEvent>() {
//            @Throws(Exception::class)
//            override fun onEvent(selectTabChangeEvent: SelectTabChangeEvent) {
//                if (selectTabChangeEvent.tab > 100) {
//                    when (selectTabChangeEvent.tab) {
//                        101 -> getLocation(101)
//                    }
//                } else {
//                    if (null != commonTabLayout) {
////                        commonTabLayout!!.currentTab = selectTabChangeEvent.tab
//                    }
//                }
//            }
//        })

//        RxBus.getDefault().add(mSelectTabChangeEvent)
//        mEMEEvent = RxBus.getDefault().toObservable(EMECodeEvent::class.java).subscribeWith(object : RxBusSubscriber<EMECodeEvent>() {
//            @Throws(Exception::class)
//            override fun onEvent(emeCodeEvent: EMECodeEvent) {
//                //                        showDynamicMsgCount();
//                when (emeCodeEvent.code) {
//                    201 -> mySelfFragment!!.outLoginView()
//                    202 -> mySelfFragment!!.outLoginView()
//                    203 -> {
//                        EMClient.getInstance().logout(true, object : EMCallBack {
//                            override fun onSuccess() {
//
//                            }
//
//                            override fun onProgress(progress: Int, status: String) {
//                                // TODO Auto-generated method stub
//                            }
//
//                            override fun onError(code: Int, message: String) {
//                                // TODO Auto-generated method stub
//                            }
//                        })
//                        mySelfFragment!!.outLoginView()
//                    }
//                    100 -> {
//                    }
//                }
//            }
//        })
//        RxBus.getDefault().add(mEMEEvent)


        val string = CacheUtils.getString(this@HomeActivity, LAST_PLAY_SONG)
        if (!TextUtils.isEmpty(string)) {
            bean = JSON.parseObject(string, MusicInfo.DataBean::class.java)
            if (bean == null) {
                return
                setBotomCircleImg()
            }
        }
    }

    private fun initView() {

        val fragmentSwitchTool = FragmentSwitchTool(supportFragmentManager, R.id.layout_container, this)

        val homeMap = fragmentSwitchTool.TabViewMap(findViewById(R.id.line_home_home), findViewById(R.id.line_home_home_image), findViewById(R.id.line_home_home_text), HomeFragmentNew::class.java)
        val pondMap = fragmentSwitchTool.TabViewMap(findViewById(R.id.line_home_topic), findViewById(R.id.line_home_topic_img), findViewById(R.id.line_home_topic_text), PondFrgNew::class.java)

        val mesageMap = fragmentSwitchTool.TabViewMap(findViewById(R.id.line_home_message), findViewById(R.id.line_home_message_img), findViewById(R.id.line_home_message_text), MsgCenterFragNew::class.java)
        val mineMap = fragmentSwitchTool.TabViewMap(findViewById(R.id.line_home_mine), findViewById(R.id.line_home_mine_img), findViewById(R.id.line_home_mine_text), MySelfFragmentNew::class.java)
        fragmentSwitchTool.setSelectMap("home", homeMap).setSelectMap("topic", pondMap).setSelectMap("msg", mesageMap).setSelectMap("mine", mineMap)
        fragmentSwitchTool.changeSelectd(findViewById(R.id.line_home_home))
//        commonTabLayout.setBackgroundColor(Color.TRANSPARENT)
//        commonTabLayout.setTabData(mTabEntities, this, R.id.layout_container, mFragments)
//        commonTabLayout.setOnTabSelectListener(object : OnTabSelectListener {
//            override fun onTabSelect(position: Int) {
//                try {
//                    when (position) {
//                        1 -> MobclickAgent.onEvent(this@HomeActivity, "click_btom_pond")
//                        2 -> MobclickAgent.onEvent(this@HomeActivity, "click_btom_player")
//                        3 -> {
//                            MobclickAgent.onEvent(this@HomeActivity, "click_btom_msg")
//                            if (!CacheUtils.getBoolean(this@HomeActivity, Constants.User.IS_LOGIN)) {
//                                goActivity(LoginRegMainPage::class.java)
//                                commonTabLayout.currentTab = lastTab
//                            } else {
//                                RxBus.getDefault().post(EMECodeEvent(100))
//                            }
//                        }
//                        4 -> MobclickAgent.onEvent(this@HomeActivity, "click_btom_mine")
//                    }
//                } catch (e: RuntimeException) {
//
//                }
//                if (position != 3) {
//                    lastTab = position
//                }
//
//            }
//
//            override fun onTabReselect(position: Int) {
//
//            }
//        })
//        commonTabLayout.tabsContainer.getChildAt(2).isClickable = false
    }

    //    fun showDynamicMsgCount(count: Int) {
//        if (count > 0) {
//            commonTabLayout!!.showMsg(3, 0)
//            commonTabLayout!!.setMsgMargin(3, 0f, 0f)
//        } else {
//            commonTabLayout!!.hideMsg(3)
//        }
//    }
    fun isLogin():Boolean
    {
        if (!CacheUtils.getBoolean(this@HomeActivity, Constants.User.IS_LOGIN)) {
            goActivity(LoginRegMainPage::class.java)
            return false
        } else {
            RxBus.getDefault().post(EMECodeEvent(100))
        }
        return true
    }

    fun getLocation(type: Int) {
        if (type == 101) {
            val pm = packageManager
            val permission = PackageManager.PERMISSION_GRANTED == pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, packageName)
            if (!permission) {
                MaterialDialog("请设置定位权限哦~", null, null) { code ->
                    if (code == 1) {
                        LshIntentUtils.gotoAppDetailSetting(packageName)
                    }
                }
            } else {
                goActivity(NearbyPeopleActivity::class.java)
            }
        }
    }

//    fun hideMsg() {
//        commonTabLayout!!.hideMsg(3)
//    }

    private inner class ImgReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when (action) {
                CHANGE_PLAYING_IMG -> setBotomCircleImg()
                EM_LOGIN -> emLogin()
            }
        }
    }

    private fun setBotomCircleImg() {
        if (bean?.imgpic_info != null) {
            ImageLoader.with(this@HomeActivity).override(38, 38).getSize(110, 110).url(bean?.imgpic_info?.link).scale(ScaleMode.CENTER_CROP).asBitmap(object : SingleConfig.BitmapListener {
                override fun onSuccess(bitmap: Bitmap) {
                    aroundCircleImageView(bitmap)
                }

                override fun onFail() {

                }
            })
        }
    }

    private fun aroundCircleImageView(bitmap: Bitmap) {
        val ovalBitMap = getOvalBitmap(bitmap)
        val drawable = BitmapDrawable(ovalBitMap)
        autoBackView!!.background = drawable
        autoBackView!!.setImageResource(R.drawable.oval_img_src)
    }

    /**
     * 处理启动图
     */
    private fun jsonStartImg(resultData: String) {
        val parse = JSON.parse(resultData) as JSONObject
        val data = parse["data"]
        if (data is String) {
            CacheUtils.setString(this@HomeActivity, Other.SPLASH_PIC_DATA, "")
            return
        }
        val startImgBean = JSON.parseObject(resultData, StartImgBean::class.java)
        if (startImgBean?.data != null && startImgBean?.data.imgpic_info != null) {
            val s = JSON.toJSONString(startImgBean)
            CacheUtils.setString(this@HomeActivity, Other.SPLASH_PIC_DATA, s)
        } else {
            CacheUtils.setString(this@HomeActivity, Other.SPLASH_PIC_DATA, "")
        }
    }

    /**
     * 处理启动图请求
     */
    private fun getStartImg(is_cache: Boolean) {

        val startdata = CacheUtils.getString(this, "startImg")
        if (is_cache && !startdata.isNullOrEmpty()) {
            jsonStartImg(startdata!!)
            ThreadPoolManager.getInstance().execute(object : Runnable {
                override fun run() {
                    getStartImg(false)
                }
            })
            return
        }
        NetWork.getStartImg(this, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                jsonStartImg(resultData)
                CacheUtils.setString(this@HomeActivity, "startImg", resultData)
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    override fun onBackPressed() {
        if (appExit) {
            CacheUtils.setBoolean(this, SHOW_SYS_MSG, true)
            moveTaskToBack(false)
            // 热更新
            if (!MediaService.getMediaPlayer().isPlaying) {
                if (CacheUtils.getBoolean(MainApplication.application, Other.NEED_RE_LAUNCH, false)) {
                    CacheUtils.setBoolean(MainApplication.application, Other.NEED_RE_LAUNCH, false)
                    SophixManager.getInstance().killProcessSafely()
                }
            }
        } else {
            appExit = true
            Toast.create(this).show("再按一次返回桌面")
            handler.sendEmptyMessageDelayed(0, 2000)
        }
    }

    private fun initPushDialog() {
        pushdialog = View.inflate(this, R.layout.dialog_push, null)
        val pushDiaLogBuilder = DiaLogBuilder(this).setContentView(pushdialog).setFullScreen().setGrvier(Gravity.CENTER).show()
        pushDiaLogBuilder.setCanceledOnTouchOutside(false)
        val tv_know = pushdialog!!.findViewById<TextView>(R.id.tv_know)
        val tv_dialog_title = pushdialog!!.findViewById<TextView>(R.id.tv_dialog_title)
        val tv_contents = pushdialog!!.findViewById<TextView>(R.id.tv_contents)
        val diss_dialog = pushdialog!!.findViewById<ImageView>(R.id.diss_dialog)
        diss_dialog.visibility = View.VISIBLE
        tv_know.setOnClickListener {
            NotificationsUtils.requestPermission(0, this@HomeActivity)
            pushDiaLogBuilder.setDismiss()
        }
        diss_dialog.setOnClickListener { pushDiaLogBuilder.setDismiss() }
    }

    override fun onResume() {
        super.onResume()
        MainApplication.application?.currentActivity = this
//        emLogin()
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.getDefault().remove(mSelectTabChangeEvent)
        RxBus.getDefault().remove(mEMEEvent)
        unregisterReceiver(re)
        CacheUtils.setString(this, Other.YXY_CHAT_HISTOTY, "")
        SophixManager.getInstance().killProcessSafely()
        CacheUtils.setLong(this, LAST_PLAY.POSITION, MediaService.mediaPlayer?.currentPosition)
    }

    private fun emLogin() {
        val string = CacheUtils.getString(MainApplication.application, User.USER_JSON)
        if (!TextUtils.isEmpty(string) && !string.equals("null")) {
            EaseHelper.emLogin(string, object : EaseHelper.EmLoginListener {
                override fun onSuccess() {
                }

                override fun onError(message: String) {
                }
            })
        }
    }

    companion object {
        private val TAG = "HomeActivity"
        fun getOvalBitmap(bitmap: Bitmap): Bitmap {
            val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)

            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)

            val rectF = RectF(rect)

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)

            canvas.drawOval(rectF, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
            return output
        }
    }

    private fun requestPermissions() {
        val rxPermission = RxPermissions(this)
        rxPermission.requestEach(android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe { permission ->
            when {
                permission.granted -> {
                }
                permission.shouldShowRequestPermissionRationale -> {
                }
                else -> {
                }
            }
        }
    }
}