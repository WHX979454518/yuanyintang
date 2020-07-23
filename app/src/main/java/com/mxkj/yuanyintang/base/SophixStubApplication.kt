//package com.mxkj.yuanyintang.base
//
//import android.content.Context
//import android.content.pm.PackageInfo
//import android.content.pm.PackageManager
//import android.os.Handler
//import android.support.annotation.Keep
//import android.support.multidex.MultiDex
//import android.util.Log
//
//import com.mxkj.yuanyintang.utils.constant.Constants
//import com.mxkj.yuanyintang.utils.file.CacheUtils
//import com.mxkj.yuanyintang.utils.luban.Luban
//import com.taobao.sophix.PatchStatus
//import com.taobao.sophix.SophixApplication
//import com.taobao.sophix.SophixEntry
//import com.taobao.sophix.SophixManager
//import com.taobao.sophix.listener.PatchLoadStatusListener
//
//class SophixStubApplication : SophixApplication() {
//    private var currentVersion: String? = null
//    private val handler: Handler? = null
//
//    @Keep
//    @SophixEntry(MainApplication::class)
//    internal class RealApplicationStub
//
//    override fun attachBaseContext(base: Context) {
//        super.attachBaseContext(base)
//        MultiDex.install(this)
//        initializeHotFix()
//    }
//
//    private fun initializeHotFix() {
//        val manager = packageManager
//        try {
//            val info = manager.getPackageInfo(packageName, 0)
//            currentVersion = info.versionName
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        SophixManager.getInstance().setContext(this)
//                .setAppVersion(currentVersion)
//                .setAesKey(null)
//                .setEnableDebug(true)
//                .setUsingEnhance()
//                .setPatchLoadStatusStub { _, code, _, _ ->
//                    when (code) {
//                        PatchStatus.CODE_LOAD_SUCCESS -> {
//                        }
//                        PatchStatus.CODE_LOAD_RELAUNCH -> {
//                        }
//                        PatchStatus.CODE_LOAD_FAIL -> SophixManager.getInstance().cleanPatches()
//                        else -> {
//                        }
//                    }
//                }.initialize()
//    }
//
//}