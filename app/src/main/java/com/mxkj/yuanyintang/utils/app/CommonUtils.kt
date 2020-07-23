package com.mxkj.yuanyintang.utils.app

import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.TypedValue

import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R

import java.io.IOException
import java.lang.reflect.Method
import java.util.UUID

object CommonUtils {
    val MUSIC_ONLY_SELECTION = (MediaStore.Audio.AudioColumns.IS_MUSIC + "=1"
            + " AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''")
    private const val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"
    private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private const val KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage"

    val deviceInfo: String
        get() {
            val builder = StringBuilder()
            builder.append("MODEL = " + Build.MODEL + "\n")
            builder.append("PRODUCT = " + Build.PRODUCT + "\n")
            builder.append("TAGS = " + Build.TAGS + "\n")
            builder.append("CPU_ABI" + Build.CPU_ABI + "\n")
            builder.append("BOARD = " + Build.BOARD + "\n")
            builder.append("BRAND = " + Build.BRAND + "\n")
            builder.append("DEVICE = " + Build.DEVICE + "\n")
            builder.append("DISPLAY = " + Build.DISPLAY + "\n")
            builder.append("ID = " + Build.ID + "\n")
            builder.append("VERSION.RELEASE = " + Build.VERSION.RELEASE + "\n")
            builder.append("Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT + "\n")
            builder.append("VERSION.BASE_OS = " + Build.VERSION.BASE_OS + "\n")
            builder.append("Build.VERSION.SDK = " + Build.VERSION.SDK + "\n")
            builder.append("APP.VERSION = " + getAPPVersionCode(MainApplication.context) + "\n")
            builder.append("\n" + "log:" + "\n")

            return builder.toString()
        }

    val isMarshmallow: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    val isLollipop: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    // 检测MIUI

    val isJellyBeanMR2: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2

    val isJellyBean: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN

    val isJellyBeanMR1: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1

    val isMIUI: Boolean
        get() {
            return try {
                val prop = BuildProperties.newInstance()
                (prop.getProperty(KEY_MIUI_VERSION_CODE, null!!) != null
                        || prop.getProperty(KEY_MIUI_VERSION_NAME, null!!) != null
                        || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null!!) != null)
            } catch (e: IOException) {
                false
            }

        }


    // 检测Flyme

    // Invoke Build.hasSmartBar()
    val isFlyme: Boolean
        get() {
            return try {
                val method = Build::class.java.getMethod("hasSmartBar")
                method != null
            } catch (e: Exception) {
                false
            }

        }

    fun getStatusHeight(context: Context): Int {

        var statusHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(`object`).toString())
            statusHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusHeight
    }

    /**
     * 获取apk的版本号 currentVersionCode
     *
     * @param ctx
     * @return
     */
    fun getAPPVersionCode(ctx: Context?): Int {
        var currentVersionCode = 0
        val manager = ctx!!.packageManager
        try {
            val info = manager.getPackageInfo(ctx.packageName, 0)
            val appVersionName = info.versionName // 版本名
            currentVersionCode = info.versionCode // 版本号
            println(currentVersionCode.toString() + " " + appVersionName)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return currentVersionCode
    }

    /**
     * 获取apk的版本号 currentVersionCode
     *
     * @param ctx
     * @return
     */
    fun getAPPVersionName(ctx: Context?): String {
        var appVersionName = ""
        if (null != ctx) {
            var currentVersionCode = 0
            val manager = ctx.packageManager
            try {
                val info = manager.getPackageInfo(ctx.packageName, 0)
                appVersionName = info.versionName // 版本名
                currentVersionCode = info.versionCode // 版本号
                println(currentVersionCode.toString() + " " + appVersionName)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return appVersionName
    }


    fun getAlbumArtUri(paramInt: Long): Uri {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), paramInt)
    }


    fun makeLabel(context: Context, pluralInt: Int, number: Int): String {
        return context.resources.getQuantityString(pluralInt, number, number)
    }

    fun getActionBarHeight(context: Context): Int {
        val mActionBarHeight: Int
        val mTypedValue = TypedValue()

        context.theme.resolveAttribute(R.attr.actionBarSize, mTypedValue, true)

        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, context.resources.displayMetrics)

        return mActionBarHeight
    }

    fun getBlackWhiteColor(color: Int): Int {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return if (darkness >= 0.5) {
            Color.WHITE
        } else
            Color.BLACK
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    //4.sp转px
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }


}
