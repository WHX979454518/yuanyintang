package com.mxkj.yuanyintang.base.share

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.bumptech.glide.Glide
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.ali_file_upload.FileUploadUtil
import com.mxkj.yuanyintang.utils.ali_file_upload.FileBean
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.string.StringUtils

import java.io.File
import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.alibaba.fastjson.JSONPath.eval
import com.google.gson.Gson
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.dialog.ActivityBean
import com.mxkj.yuanyintang.musicplayer.service.MediaService.bean
import com.mxkj.yuanyintang.video.SongMusicMvBean
import kotlinx.android.synthetic.main.activity_collect_song_sheet_details.*
import okhttp3.Headers
import kotlinx.android.synthetic.main.activity_share_to_yyt.*
import kotlinx.android.synthetic.main.uc_navigationbar.*

class ShareToYYT : StandardUiActivity() {

    private var music_id: Int = 0
    private var mv_id: Int = 0
    private var hashStr = ""
    private var topicId: Int = 0
    private var imgPath: String? = null
    private val handler = Handler()
    private var albumId: Int = 0
    private var web_id: Int = 0
    private var desc: String? = null
    //新加的mv的显示mv图标
    private var mv: String? = null
    private lateinit var forwar_mv :ImageView

    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun setLayoutId(): Int {
        return R.layout.activity_share_to_yyt
    }

    override fun initView() {
        setTitleText("分享")
        navigationBar.getLeftButton().text = "取消"
        navigationBar.getLeftButton().setTextColor(ContextCompat.getColor(this, R.color.gray_normal))
        setLeftButtonImageView(null)
        setRightButtonText("发送")
        navigationBar.getRightButton().setTextColor(ContextCompat.getColor(this, R.color.base_red))
        setLeftButtonImageView(null)

        forwar_mv = findViewById(R.id.forwar_mv);
    }

    lateinit var activityBean: ActivityBean

    lateinit var activityAlias : String
    lateinit var sinaDescription : String
    lateinit var title : String
    lateinit var imgpic : String
    lateinit var shareUrl : String
    lateinit var topicContent : String
    lateinit var nickname : String
    lateinit var activityType : String


    override fun initData() {
        val intent = intent
        imgPath = intent.getStringExtra("imgPath")
//        desc = intent.getStringExtra("desc")
//        if (!TextUtils.isEmpty(desc)) {
//            et_content.setText(desc)
//        }

        val intent1 =getIntent();
        //getXxxExtra方法获取Intent传递过来的数据
//        mv = intent.getStringExtra("mv")
        mv = intent1.getStringExtra("mv");
        Log.e("iiiiiii",""+mv)
        if(mv == null ||mv == ""||mv == "null"){
            forwar_mv.visibility = View.GONE
        }else{
            forwar_mv.visibility = View.VISIBLE
        }

        if (imgPath != null) {
            img_share.setVisibility(View.VISIBLE)
            Glide.with(this).load(imgPath).asBitmap().into(img_share)
        }
        music_id = intent.getIntExtra("music_id", 0)
        if (music_id != 0) {
            ll_music.setVisibility(View.VISIBLE)
            tv_songName.setText(intent.getStringExtra("title"))
            singer_song.setText(intent.getStringExtra("nickName"))

            if(null!= intent.getStringExtra("topicContent")){
                topicContent = intent.getStringExtra("topicContent")
                et_content.setText(topicContent)
            }


            ImageLoader.with(this)
                    .url(intent.getStringExtra("imgLink"))
                    .into(img_song)
            img_playmusic.setOnClickListener(View.OnClickListener { PlayCtrlUtil.play(this@ShareToYYT, music_id, 0) })
        }
        mv_id = intent.getIntExtra("mv_id", 0)
        if (mv_id != 0) {
            ll_music.setVisibility(View.VISIBLE)
            tv_songName.setText(intent.getStringExtra("title"))
            singer_song.setText(intent.getStringExtra("nickName"))
            ImageLoader.with(this)
                    .url(intent.getStringExtra("imgLink"))
                    .into(img_song)
            img_playmusic.setOnClickListener(View.OnClickListener { PlayCtrlUtil.play(this@ShareToYYT, music_id, 0) })
        }


        albumId = intent.getIntExtra("album_id", 0)
        if (albumId != 0) {
            item_type.setText("歌单")
            ll_music.setVisibility(View.VISIBLE)
            tv_songName.setText(intent.getStringExtra("title"))
            singer_song.setText(intent.getStringExtra("nickName"))
            ImageLoader.with(this)
                    .url(intent.getStringExtra("imgLink"))
                    .into(img_song)
            img_playmusic.setOnClickListener(View.OnClickListener { PlayCtrlUtil.playSheet(this@ShareToYYT, albumId.toString() + "") })
        }
        topicId = intent.getIntExtra("topic_id", 0)
        if (topicId != 0) {
            item_type.setText("池塘")
            img_playmusic.setVisibility(View.INVISIBLE)
            ll_music.setVisibility(View.VISIBLE)
            img_playmusic.setVisibility(View.GONE)
            tv_songName.setText(intent.getStringExtra("title"))
            singer_song.setText(intent.getStringExtra("nickName"))
            ImageLoader.with(this)
                    .url(intent.getStringExtra("imgLink"))
                    .into(img_song)
            img_playmusic.setOnClickListener(View.OnClickListener { PlayCtrlUtil.play(this@ShareToYYT, music_id, albumId) })
        }
        web_id = intent.getIntExtra("web_id", 0)
        if (web_id != 0) {
            activityAlias = intent.getStringExtra("activityAlias")
            sinaDescription = intent.getStringExtra("sinaDescription")
            title = intent.getStringExtra("title")
            imgpic = intent.getStringExtra("imgpic")
            shareUrl = intent.getStringExtra("shareUrl")
            topicContent = intent.getStringExtra("topicContent")
            nickname = intent.getStringExtra("nickname")
            activityType = intent.getStringExtra("activityType")


            activityBean  = ActivityBean()
            activityBean.activityAlias =activityAlias
            activityBean.sinaDescription =sinaDescription
            activityBean.title =title
            activityBean.imgpic =imgpic
            activityBean.url =shareUrl
            activityBean.topicContent =topicContent
            activityBean.nickname = nickname
            activityBean.activityType = activityType
            activityBean.sub_title = nickname


            item_type.setText("链接")
            img_playmusic.setVisibility(View.INVISIBLE)
            ll_music.setVisibility(View.VISIBLE)
            img_playmusic.setVisibility(View.GONE)
            tv_songName.setText(intent.getStringExtra("title"))
            singer_song.setText(intent.getStringExtra("nickname"))
            ImageLoader.with(this)
                    .url(intent.getStringExtra("imgpic"))
                    .into(img_song)

        }

    }

    override fun initEvent() {
        onViewClick()

    }

    fun onViewClick() {
        leftButton.setOnClickListener { finish() }
        rightButton.setOnClickListener {publishDynamic() }
    }

    private fun publishDynamic() {
        //这里是判断到底有没有5个是汉字
        var n = 0
        var nnum = 0
        for (i in 0 until et_content.getText().toString().length) {
            n = et_content.getText().toString().get(i).toInt()
            if (19968 <= n && n < 40623) {
                nnum++
            }
        }

        if (imgPath != null) {//图片
            val file = File(imgPath)
            val files = ArrayList<File>()
            files.add(file)

            val fileUploadUtil = FileUploadUtil()
            fileUploadUtil.setFileList(files)
            fileUploadUtil.upload(this, 1, object : FileUploadUtil.UpLoadCallback {
                override fun upLoadSuccess(finishBeans: List<FileBean.DataBean>) {
                    if (finishBeans != null && finishBeans.isNotEmpty()) {
                        hashStr = finishBeans[0].imgpic!!
                        if(nnum<5){
                            et_content.setFocusable(true)
                            setSnackBar("内容不能少于5个字", "", R.drawable.icon_fails)
                        }else{
                            showLoadingView()
                            publish(hashStr)
                        }
                    }                }

                override fun upLoadFailure(request: PutObjectRequest, serviceException: ServiceException?) {

                }
            })
        } else {//没图片
            if(nnum<5){
                et_content.setFocusable(true)
                setSnackBar("内容不能少于5个字", "", R.drawable.icon_fails)
            }else{
                showLoadingView()
                publish(null)
            }
        }
    }

    /*发布*/
    private fun publish(hashStr: String?) {
        val params = HttpParams()
        params.put("push", 2.toString() + "")
        if (hashStr != null) {
            Log.e(TAG, "publish: $hashStr")
            params.put("imglist", hashStr)
        }
        params.put("depict", et_content.text.toString())
        params.put("content", et_content.text.toString())
        when {
            music_id != 0 -> {
                ll_music.visibility = View.VISIBLE
                params.put("item_id", music_id.toString() + "")//音乐id
                params.put("item_type", 1.toString() + "")
            }mv_id != 0 -> {
                ll_music.visibility = View.VISIBLE
                params.put("item_id", mv_id.toString() + "")//MV id
                params.put("item_type", 4.toString() + "")
            }
            topicId != 0 -> {
                ll_music.visibility = View.VISIBLE
                img_playmusic.visibility = View.GONE
                params.put("item_id", topicId.toString() + "")//池塘id
                params.put("item_type", 3.toString() + "")//池塘id
            }
            albumId != 0 -> {
                params.put("item_id", albumId.toString() + "")
                params.put("item_type", 2.toString() + "")//歌单id
            }web_id !=0 ->{
                params.put("item_id", "")
                params.put("item_type", 6.toString() + "")//链接
                var gson:Gson
                gson = Gson()
//                var jsonStr:String = gson.toJson(activityBean);
                params.put("item_url", gson.toJson(activityBean).toString())//链接
            }
            else -> params.put("item_type", 5.toString() + "")
        }

        NetWork.publishDynamic(this, params, object : NetWork.TokenCallBack {

            override fun doNext(resultData: String, headers: Headers?) {
                setSnackBar("发布成功", "", R.drawable.icon_success)
                sendBroadcast(Intent("publishDynamic"))
                finish()
            }

            override fun doError(msg: String) {
                setSnackBar(StringUtils.isEmpty(msg), "", R.drawable.icon_success)
            }

            override fun doResult() {
                hideLoadingView()
                navigationBar.getRightButton().isClickable = true
            }
        })
    }
}
