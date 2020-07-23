package com.mxkj.yuanyintang.base

import android.app.Activity
import android.content.Context
import android.os.Looper

import com.facebook.drawee.backends.pipeline.Fresco
import com.hyphenate.chat.EMClient
import com.linsh.lshutils.LshUtils
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection
import com.liulishuo.filedownloader.util.FileDownloadUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheEntity
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.interceptor.HttpLoggingInterceptor
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.database.updatafilesql.TasksManager
import com.mxkj.yuanyintang.musicplayer.playcache.HttpProxyCacheServer
import com.mxkj.yuanyintang.net.HttpUtils
import com.mxkj.yuanyintang.upush.UpushService
import com.mxkj.yuanyintang.utils.app.RxDeviceTool
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.threadpool.ThreadPoolManager
import com.nanchen.crashmanager.CrashApplication
import com.nanchen.crashmanager.UncaughtExceptionHandlerImpl
import com.taobao.sophix.SophixManager
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.umeng.socialize.Config
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI

import org.android.agoo.huawei.HuaWeiRegister
import org.android.agoo.mezu.MeizuRegister
import org.android.agoo.xiaomi.MiPushRegistar

import java.net.Proxy
import java.util.concurrent.TimeUnit
import java.util.logging.Level

import okhttp3.OkHttpClient

class MainApplication : CrashApplication() {
    var currentActivity: Activity? = null

    private fun newProxy(): HttpProxyCacheServer {
        return HttpProxyCacheServer(this)
    }

    override fun onCreate() {
        super.onCreate()
        SophixManager.getInstance().queryAndLoadNewPatch()
        application = this
        context = this
        Fresco.initialize(this)
        //图片加载初始化
        ImageLoader.init(context)
        //环信初始化
        EaseHelper.getInstance().init(context!!)
        initOkGo()
        initUmeng()
        LshUtils.init(context)
        /**
         * 初始化线程池对象保证只有一个对象
         */
        ThreadPoolManager.getInstance()
        FileDownloader.setupOnApplicationOnCreate(application).connectionCreator(FileDownloadUrlConnection.Creator(FileDownloadUrlConnection.Configuration().connectTimeout(20000) // set connection timeout.
                .readTimeout(20000) // set read timeout.
                .proxy(Proxy.NO_PROXY) // set proxy
        )).commit()
        FileDownloadUtils.setDefaultSaveRootPath(TasksManager.getFilePath(applicationContext))
//        ThreadPoolManager.getInstance().execute(object : Runnable {
//            override fun run() {
//
//            }
//        })


//      LeakCanary.install(this);
        // 设置崩溃后自动重启 APP
      UncaughtExceptionHandlerImpl.getInstance().init(this, true, true, 0, HomeActivity.javaClass)
    }

    private fun initOkGo() {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor("OkGo")
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)
        loggingInterceptor.setColorLevel(Level.INFO)
        builder.addInterceptor(loggingInterceptor)
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        OkGo.getInstance().init(this).setCacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST).setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE).retryCount = 3
    }

    private fun initUmeng() {
        //debug模式，上线关掉
//       UMConfigure.setLogEnabled(true);
//       Config.DEBUG = true;
//        禁用友盟自己的统计，自定义统计策略
        MobclickAgent.openActivityDurationTrack(false)
        UMConfigure.init(this, "58743b0df5ade44882001702", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "5374abde979407c1823534f796e4e5d9")
        initUmengShare()
        initUmengPush()
        initUmenAnalytics()
    }

    private fun initUmenAnalytics() {
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL)
        MobclickAgent.setSecret(this, "5374abde979407c1823534f796e4e5d9")
    }

    private fun initUmengShare() {
        PlatformConfig.setWeixin("wx67591dc5fc8029ac", "a109a24ddf8361624521b887b82b476d")
        PlatformConfig.setSinaWeibo("2295422391", "aa7752e423e1e709fd1ac7fcbe89ff9d", "http://www.yuanyintang.com")
        PlatformConfig.setQQZone("101387828", "cd07b8e2f0443bad431e11242d0cbc1e")
        UMShareAPI.get(this)
        Config.isJumptoAppStore = true
        val deviceKeyToken = RxDeviceTool.getUniqueSerialNumber() + ""
        CacheUtils.setString(this, Constants.User.USER_DEVICE_TOKEN, deviceKeyToken)
    }

    private fun initUmengPush() {
        pushAgent = PushAgent.getInstance(this)
        pushAgent.setPushIntentServiceClass(UpushService::class.java)
        MiPushRegistar.register(this, "2882303761517569365", "5611756958365")
        HuaWeiRegister.register(this)
        MeizuRegister.register(this, "112324", "bff4a50591cb4b469c76a2e9b6c21220")
        pushAgent.register(object : IUmengRegisterCallback {
            override fun onSuccess(deviceToken: String) {
                CacheUtils.setString(applicationContext, Constants.User.USER_DEVICE_TOKEN, deviceToken)
            }

            override fun onFailure(s: String, s1: String) {}
        })
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        ImageLoader.trimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        ImageLoader.clearAllMemoryCaches()
    }

    companion object {
        var isTop: Boolean = false
        var isScrollY: Boolean = false
        var context: Context? = null
            private set
        private var proxy: HttpProxyCacheServer? = null
        var application: MainApplication? = null
            private set
        lateinit var pushAgent: PushAgent

        fun getProxy(context: Context): HttpProxyCacheServer {
            val app = context.applicationContext as MainApplication
            return if (proxy == null) app.newProxy() else proxy!!
        }
    }
}
