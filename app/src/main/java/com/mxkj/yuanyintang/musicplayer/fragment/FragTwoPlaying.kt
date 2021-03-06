package com.mxkj.yuanyintang.musicplayer.fragment

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.linsh.lshutils.utils.LshContextUtils.getSystemService
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.base.fragment.BaseFragment
import com.mxkj.yuanyintang.musicplayer.activity.ChooseLyricActivity
import com.mxkj.yuanyintang.musicplayer.activity.PlayerActivity
import com.mxkj.yuanyintang.musicplayer.lyric_remusic.DefaultLrcParser
import com.mxkj.yuanyintang.musicplayer.lyric_remusic.LrcRow
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.musicplayer.service.MediaService.bean
import com.mxkj.yuanyintang.musicplayer.util.GetColorFromBitmap
import com.mxkj.yuanyintang.utils.app.TimeUtils
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.file.SdUtil
import com.mxkj.yuanyintang.utils.image.ImageUtils
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.config.SingleConfig
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import com.umeng.analytics.MobclickAgent
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_playing.*
import kotlinx.android.synthetic.main.fragment_playing.view.*
import kotlinx.android.synthetic.main.player_activity.*
import java.io.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class FragTwoPlaying : BaseFragment() {
    private var isFull: Boolean = false
    private var lrcRowlist: List<LrcRow>? = ArrayList()
    var isShowImg = false
    private lateinit var filter_duration: IntentFilter
    private var receiver: PlayCtrlReceiver? = null
    private var diaLogBuilder: DiaLogBuilder? = null
    override val layoutId: Int
        get() = R.layout.fragment_twoplaying

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        setBaseInfo()
        initDialogBuild()
    }

    private fun setBaseInfo() {
        filter_duration = IntentFilter()
        filter_duration.addAction("dur")
        receiver = PlayCtrlReceiver()
        activity.registerReceiver(receiver, filter_duration)
        bean?.let {
            wv.setUseRing(false)
            wv?.count = 6
            wv.start()
            setAlbumImgPic()
            lyricEvent()
            updateLrc()
        }
    }

    /**
     * 设置封面
     */
    fun setAlbumImgPic() {
        if (bean == null || bean?.imgpic_info == null || bean?.imgpic_info?.link == null) {
            //这里可以设置默认的图片
//            img_music.setBackgroundResource(R.drawable.play_none)
            return
        }

        ll_album.setOnClickListener {
            if (isFull) {
                isFull = false
                rl_default_lrcView.visibility = View.VISIBLE
            } else {
                isFull = true
//                rl_default_lrcView.visibility = View.GONE
            }
            isTimerLyric()
        }


        ImageLoader.with(MainApplication.context)
                .getSize(400, 400)
                .override(180, 180)
                .url(bean?.imgpic_info?.link)
                .scale(ScaleMode.CENTER_CROP)
                .asCircle()
                .asBitmap(object : SingleConfig.BitmapListener {
                    override fun onSuccess(bitmap: Bitmap) {
                        img_music.setImageBitmap(bitmap)
                        GetColorFromBitmap.getColor(bitmap) { color ->
                            color?.let {
                                wv?.setColor(color)
                            }
                        }
                    }

                    override fun onFail() {

                    }
                })
    }

    fun updateLrc() {
        isTimerLyric()
        lrcRowlist = getLrcRows()
        if (lrcRowlist != null && lrcRowlist?.isNotEmpty()!!) {
            lrcview.setLrcRows(lrcRowlist)
        } else {
            lrcview.reset()
        }
    }

    /**
     * 判断是不是带时间的歌词
     */
    private fun isTimerLyric() {
        bean?.let {
            if (TextUtils.isEmpty(it.lyrics)) {
                small_srcoll.visibility = View.VISIBLE
                lrcview.visibility = View.GONE
                tv_small_lyric.text = ""
            } else {
                val lyrics = it.lyrics
                lyrics?.let {
                    if (it.contains("[")) {
                        small_srcoll.visibility = View.GONE
                        lrcview.visibility = View.VISIBLE
                    } else {
                        small_srcoll.visibility = View.VISIBLE
                        lrcview.visibility = View.GONE
                        val replace = it.replace("\\n", "\n")
                        tv_small_lyric.text = replace
                    }
                }

            }
        }

    }

    /**
     * 歌词相关的事件
     */
    private fun lyricEvent() {
        /**
         * 点击小歌词  图片缩小淡出，小歌词向上动画，同时小时，大歌词显示
         */
        tv_small_lyric.setOnClickListener({
            if (isFull) {
                isFull = false
                rl_default_lrcView.visibility = View.VISIBLE
            } else {
                isFull = true
//                rl_default_lrcView.visibility = View.GONE
            }
            isTimerLyric()
        })

        lrcview.setOnLrcClickListener({
            if (isFull) {
                isFull = false
                rl_default_lrcView.visibility = View.VISIBLE
            } else {
                isFull = true
//                rl_default_lrcView.visibility = View.GONE
            }
            isTimerLyric()
            isTimerLyric()
        })

        lrcview.setOnLrcLongClickListener({
            val lrcRows = getLrcRows()
            val strings = ArrayList<String>()
            if (lrcRows != null) {
                for (lrc in lrcRows!!) {
                    strings.add(lrc.content)
                }
                val intent = Intent(context, ChooseLyricActivity::class.java)
                intent.putStringArrayListExtra("lyrics", strings)
                startActivity(intent)
            }
        })

        tv_small_lyric.setOnLongClickListener({
            val intent = Intent(context, ChooseLyricActivity::class.java)
            var bre: BufferedReader?
            val file = File(Environment.getExternalStorageDirectory().absolutePath +
                    "/yytmusic/lyrics/" + bean!!.title + ".lrc")
            if (file.exists()) {
                try {
                    bre = BufferedReader(FileReader(file))//此时获取到的bre就是整个文件的缓存流
                    var str: String
                    val strings = ArrayList<String>()
                    while ((bre.readLine()) != null) {
                        str = bre.readLine()
                        strings.add(str)
                    }
                    intent.putStringArrayListExtra("lyrics", strings)
                    startActivity(intent)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            true
        })
    }

    private fun getLrcRows(): List<LrcRow>? {
        var rows: List<LrcRow>? = null
        var inputStr: InputStream? = null
        val file = File(Environment.getExternalStorageDirectory().absolutePath +
                "/yytmusic/lyrics/" + bean!!.title + ".lrc")
        val arr = bean!!.lyrics
        if (arr != null) {
            SdUtil.writeToSd("/yytmusic/lyrics", bean!!.title!! + ".lrc", arr.toByteArray())
        }
        try {
            inputStr = FileInputStream(Environment.getExternalStorageDirectory().absolutePath +
                    "/yytmusic/lyrics/" + bean!!.title + ".lrc")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            if (inputStr == null) {
                return null
            }
        }
        val br = BufferedReader(InputStreamReader(inputStr))
        var line: String?
        val sb = StringBuilder()
        try {
            while ((br?.readLine()) != null) {
                line = br.readLine()
                sb.append(line + "\n")
            }
            rows = DefaultLrcParser.getIstance().getLrcRows(sb.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return rows
    }

    private fun showBigImg() {
        if (bean != null && bean!!.imgpic_info != null) {
            isShowImg = true
            rl_big_img.setVisibility(View.VISIBLE)
            try {
                ImageLoader.with(context)
                        .override(0, 0)
                        .url(bean!!.imgpic_info!!.link)
                        .placeHolder(R.drawable.defualt_img)
                        .error(R.drawable.defualt_img)
                        .asBitmap(object : SingleConfig.BitmapListener {
                            override fun onSuccess(bitmap: Bitmap) {
                                val width = bitmap.width
                                val height = bitmap.height
                                if (width > 0 && height > 0) {
                                    val m = getSystemService(Context.WINDOW_SERVICE) as WindowManager?
                                    val outMetrics = DisplayMetrics()
                                    m!!.defaultDisplay.getMetrics(outMetrics)
                                    val widthPixels = outMetrics.widthPixels
                                    var scaleWidth = 0f
                                    if (width > widthPixels) {
                                        scaleWidth = widthPixels.toFloat() / width
                                        val matrix = Matrix()
                                        matrix.postScale(scaleWidth, scaleWidth)
                                        val newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
                                        big_img_song.setImageBitmap(newbm)
                                    } else {
                                        big_img_song.setImageBitmap(bitmap)
                                    }
                                }
                            }

                            override fun onFail() {

                            }
                        })
            } catch (e: RuntimeException) {
            }


            big_img_song.setOnClickListener({
                big_img_song.visibility = View.GONE
            })
            rl_big_img.setOnClickListener(View.OnClickListener {
                rl_big_img.visibility = View.GONE
            })

            big_img_song.setOnLongClickListener({
                diaLogBuilder?.show()
                false
            })
        }
    }

    private fun initDialogBuild() {
        val view = View.inflate(activity, R.layout.dialog_pic_details_operation, null)
        diaLogBuilder = DiaLogBuilder(activity)
                .setContentView(view)
                .setFullScreen()
                .setGrvier(Gravity.CENTER)
                .setCanceledOnTouchOutside(true)
                .setAniMo(R.anim.popup_in)
        val tv_download_pic = view.findViewById<TextView>(R.id.tv_download_pic)
        val tv_share = view.findViewById<TextView>(R.id.tv_share)
        val v_line = view.findViewById<View>(R.id.v_line)
        tv_share.visibility = View.GONE
        v_line.visibility = View.GONE
        RxView.clicks(tv_download_pic)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    RxPermissions(activity).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe { permission ->
                                if (permission.granted) {
                                    try {

                                        ImageLoader.with(context)
                                                .override(0, 0)
                                                .url(MediaService.bean!!.imgpic_info!!.link)
                                                .asBitmap(object : SingleConfig.BitmapListener {
                                                    override fun onSuccess(bitmap: Bitmap) {
                                                        ImageUtils.saveImageToGallery(context, bitmap)
                                                    }

                                                    override fun onFail() {

                                                    }
                                                })
                                    } catch (e: RuntimeException) {
                                    }


                                }
                            }
                    diaLogBuilder?.setDismiss()
                }
    }

    private inner class PlayCtrlReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            Log.d(BaseActivity.TAG, "onReceive: ---------->" + action!!)
            when (action) {
                "dur" -> if (MediaService.getMediaPlayer() != null && MediaService.getMediaPlayer().isPlaying && (activity as PlayerActivity).isUpdateProUi) {
                    lrcview.seekTo(MediaService.getMediaPlayer().currentPosition.toInt(), true, false,null,"")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity.unregisterReceiver(receiver)

    }


    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart("frg_playing")

    }

    override fun onDestroy() {
        super.onDestroy()
        MobclickAgent.onPageEnd("frg_playing")

    }


}