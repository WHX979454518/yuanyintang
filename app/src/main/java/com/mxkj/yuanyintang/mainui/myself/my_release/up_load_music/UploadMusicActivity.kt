package com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.annotation.IdRes
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.kevin.crop.UCrop
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog
import com.mxkj.yuanyintang.base.bean.DegreeBean
import com.mxkj.yuanyintang.mainui.home.bean.MyReleaseDetial
import com.mxkj.yuanyintang.mainui.login_regist.UserNoticeActivity
import com.mxkj.yuanyintang.mainui.myself.activity.ChooseMusicTag
import com.mxkj.yuanyintang.mainui.myself.bean.MusicTypeBean
import com.mxkj.yuanyintang.mainui.myself.bean.MyReleaseBean
import com.mxkj.yuanyintang.mainui.myself.helpcenter.HelpCenterActivity
import com.mxkj.yuanyintang.mainui.myself.my_release.BindPartnerActivity
import com.mxkj.yuanyintang.mainui.myself.my_release.MusicTypeActivity
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.MusicLoader
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.Song
import com.mxkj.yuanyintang.mainui.pond.bean.PondHotTagBean
import com.mxkj.yuanyintang.mainui.pond.widget.FlowLayout
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerActivity
import com.mxkj.yuanyintang.utils.ali_file_upload.FileBean
import com.mxkj.yuanyintang.utils.ali_file_upload.FileUploadUtil
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.CropImgActivity
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.rxbus.RxBus
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.widget.NavigationBar
import com.mxkj.yuanyintang.widget.SearTextView
import com.suke.widget.SwitchButton
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions

import java.io.File
import java.io.IOException
import java.io.Serializable
import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

import com.kevin.crop.UCrop.RESULT_ERROR
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureConfig.REQUEST_PICTURE
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.mainui.dynamic.activity.PublishDynamic.Companion.REQUEST_CODE_CHOOSE_PHOTO
import com.mxkj.yuanyintang.mainui.dynamic.activity.PublishDynamic.Companion.REQUEST_CODE_PERMISSION_PHOTO_PICKER
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.AddLyricActivity.Const.ADDLYRIC
import com.mxkj.yuanyintang.mainui.pond.bean.PondTagListBean
//import com.mxkj.yuanyintang.upvideo.MainActivity
import com.mxkj.yuanyintang.utils.file.CacheUtils.String2SceneList
import com.mxkj.yuanyintang.utils.image.IMGCompressUtils.count
import com.mxkj.yuanyintang.utils.image.IMGCompressUtils.files
import com.mxkj.yuanyintang.widget.choosepic_popuowindow.ChoosePicPopWindow.GALLERY_REQUEST_CODE
import kotlinx.android.synthetic.main.activity_upload_music.*
import kotlinx.android.synthetic.main.uc_navigationbar.*
import java.lang.RuntimeException

class UploadMusicActivity : StandardUiActivity(), EasyPermissions.PermissionCallbacks {
    private var fileMusi: File? = null//选择的音乐文件
    private var fileVideo: File? = null//选择的視頻文件
    private var selectList: List<LocalMedia> = ArrayList()
    private var mOnPictureSelectedListener: OnPictureSelectedListener? = null
    var catId: Int = 0//分类ID
    private var lyricStr: String = ""//歌词
    private var hashTagJson: String? = null//标签的hash
    private var hashTagJsonTag: String? = null//主题标签的hash
    private var fileHash: String? = null//文件的hash
    private var mvfileHash: String? = null//视频的hash

    private var imgHash: String? = null//图片的hash
    var imgpic_link: String? = null
    var imgpic_info: ImgpicInfoBean? = null
    private var rxEvent: RxBusSubscriber<Song>? = null
    private var isUploaded: Boolean = false
    private var partnerJson: String? = null
    private var admin_status = "1"

    private var filePath: String? = null//
    private var imagePath: String? = null


    private var canDownload = 1
    private var singType = 1
    private var list = ArrayList<PondTagListBean.DataBean.TagBean>()

    private val selectedTagList = ArrayList<PondHotTagBean.DataBean>()

    private var selectedTag = ArrayList<MusicTypeBean.DataBean>()

    internal var handler = Handler()
    private var musicId: Int = 0
    private var disposable: Disposable? = null

    lateinit var check_mv : CheckBox
    lateinit var mvrl : LinearLayout

    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun setLayoutId(): Int {
        return R.layout.activity_upload_music
    }

    override fun initView() {
        onViewClick()
        musicId = intent.getIntExtra(DATA, 0)
        intent.getSerializableExtra("BEAN")?.let {
            var dataBean = it as MyReleaseBean.DataBean
            setOnLeftClick(View.OnClickListener {
                var status = 0
                if (dataBean != null) {
                    status = dataBean.status
                }
                if (status == 1) {
                    BaseConfirmDialog.newInstance().cancleText("取消").confirmText("退出").content("是否退出发布？").title("退出发布").showDialog(this@UploadMusicActivity, object : BaseConfirmDialog.onBtClick {
                        override fun onConfirm() {
                            finish()
                        }

                        override fun onCancle() {

                        }
                    })
                } else {
                    BaseConfirmDialog.newInstance().cancleText("退出").confirmText("保存").content("是否保存草稿？").title("退出发布").showDialog(this@UploadMusicActivity, object : BaseConfirmDialog.onBtClick {
                        override fun onConfirm() {
                            admin_status = "4"
                            release()
                        }

                        override fun onCancle() {
                            finish()
                        }
                    })
                }
            })
        }
        if (musicId != 0) {
            showLoadingView()
            NetWork.getMyReleaseDetial(this@UploadMusicActivity, musicId, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    hideLoadingView()
                    Log.e(BaseActivity.TAG, "doNext: $resultData")
                    val myReleaseDetial = JSON.parseObject(resultData, MyReleaseDetial::class.java)
                    val data = myReleaseDetial.data
                    if (data != null) {
                        val member = data.member
                        if (member != null && member.size > 0) {
                            partnerJson = "{\"member\":" + JSON.toJSONString(member) + "}"
                        }
                        singType = data.music_type
                        canDownload = data.isdown
                        catId = data.category
                        imgHash = data.imgpic
                        imgpic_link = data.imgpic_info?.link
                        data.title?.let {
                            et_musicName.setText(it)
                            tv_songName.text = data.filename
                            tv_progress.text = "上传完成"
                            progress_uoload.max = 100
                            progress_uoload.progress = 100
                            beforeUpload.visibility = GONE
                            llUploading.visibility = VISIBLE

                            if(data.mv_filename.equals("")|| data.mv_filename == null){
                                mvrl.visibility = View.GONE
                                beforeUploadvideo.visibility = VISIBLE
                                llUploadingvideo.visibility = GONE
                            }else{
                                check_mv.setChecked(true);
                                count =2
                                mvrl.visibility = View.VISIBLE
                                private_songlist.setTextColor(Color.parseColor("#ff6699"))
                                //mv的名字
                                tv_videoName.text = data.mv_filename
                                //mv的大小
//                            mv_size
                                //mv上传完成的进度
                                tv_videoprogress.text = "上传完成"
                                progress_uoloadvideo.max = 100
                                progress_uoloadvideo.progress = 100
                                beforeUploadvideo.visibility = GONE
                                llUploadingvideo.visibility = VISIBLE
                            }

                        }

                        et_intro.setText(data.intro ?: "")
                        lyricStr = data.lyrics ?: ""
                        fileHash = data.video
                        mvfileHash = data.mv
                        if (null != imgpic_link) {
                            add_albumPic.visibility = VISIBLE
                            ImageLoader.with(this@UploadMusicActivity).url(imgpic_link).into(add_albumPic)
                        }
                        rb_firstSing.isChecked = data.music_type == 1
                        rb_ReSing.isChecked = data.music_type == 0
                        rb_can_down.isChecked = data.isdown == 1
                        rb_not_down.isChecked = data.isdown == 0

                        tv_musicType.text = data.category_title


//                        val tagtagBeanList = data.category_title
//                        hashTagJsonTag  = JSON.toJSONString(tagtagBeanList)
//                        CacheUtils.setString(this@UploadMusicActivity, "selectMusicTagTag", hashTagJsonTag)

                        //这里吧原来的数据没有传送过去，传送获取会报错
                        val tagBeanList = data.tag
                        hashTagJson = JSON.toJSONString(tagBeanList)
//                        CacheUtils.setString(this@UploadMusicActivity, "selectMusicTag", hashTagJson)
                        selected_tag.removeAllViews()
                        list = JSON.parseArray(hashTagJson, PondTagListBean.DataBean.TagBean::class.java) as ArrayList<PondTagListBean.DataBean.TagBean>
                        for (i in list.indices) {
                            if (i < 9) {
                                val tv = LayoutInflater.from(this@UploadMusicActivity).inflate(R.layout.search_label_tv, selected_tag, false) as SearTextView
                                tv.text = list[i].title
                                selected_tag.addView(tv)
                            }
                        }

//                        setMusicTagHash()
                    }
                }

                override fun doError(msg: String) {
                    hideLoadingView()
                }

                override fun doResult() {

                }
            })
        }
        navigationBar.init()
        setTitleText("上传作品")
        val drawable = resources.getDrawable(R.drawable.icon_fabu_help)
        setRightButton("", drawable, View.OnClickListener { startActivity(Intent(this@UploadMusicActivity, HelpCenterActivity::class.java)) })
    }

    override fun initData() {
        //        获取正在进行的活动列表
        getActivitiesList()
        //        获取作品类型
        workstype()
    }

    private fun workstype(){
        val params = HttpParams()
//        params.put("category", catId.toString() + "")
        NetWork.worksType(this@UploadMusicActivity, params, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {

            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    private fun getActivitiesList() {
        NetWork.getActivity(this, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                Log.e(BaseActivity.TAG, "getActivity-----------: $resultData")
                val array = JSON.parseObject(resultData).getJSONArray("data")
                if (array != null && array.size > 0) {
                } else {
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    override fun initEvent() {
        disposable = RxBus.getDefault().toObservable(ProBean::class.java).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : RxBusSubscriber<ProBean>() {
                    override fun onEvent(pro: ProBean) {

                        if(pro.biaoshi.equals("1")){
                            progress_uoloadvideo.progress = pro.pro


                            if(delectflag == true && delectcount == 2){
                                tv_videoprogress.text = "${pro.totalSize / 1000000}M  ${25}%"
                                tv_videoprogress.text = "${pro.totalSize / 1000000}M  ${50}%"
                                tv_videoprogress.text = "${pro.totalSize / 1000000}M  ${75}%"
                                progress_uoloadvideo.progress = 25
                                progress_uoloadvideo.progress = 50
                                progress_uoloadvideo.progress = 75
                            }else{
                                tv_videoprogress.text = "${pro.totalSize / 1000000}M  ${pro.pro}%"
                                progress_uoloadvideo.progress = pro.pro
                            }


                            Log.e("progre", pro.pro.toString())
                            mvrl = findViewById(R.id.mvrl)
                            check_mv = findViewById(R.id.check_mv)
                            check_mv.setChecked(true);
                            count =2
                        }else if(pro.biaoshi.equals("2")){
                            progress_uoload.progress = pro.pro
                            tv_progress.text = "${pro.totalSize / 1000000}M  ${pro.pro}%"
                            progress_uoload.progress = pro.pro
                            Log.e("progre", pro.pro.toString())
                            mvrl = findViewById(R.id.mvrl)
                            check_mv = findViewById(R.id.check_mv)
                            check_mv.setChecked(true);
                            count =2
                            mvrl.visibility = View.VISIBLE
                        }else{
                            progress_uoload.progress = pro.pro
                            tv_progress.text = "${pro.totalSize / 1000000}M  ${pro.pro}%"
                            progress_uoload.progress = pro.pro
                            Log.e("progre", pro.pro.toString())
                        }


                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        Log.e(BaseActivity.TAG, "接收到---onError: $e")
//                        setSnackBar("该歌曲上传失败,请重新选择哟~", "", R.drawable.icon_fails)

                    }
                })
        RxBus.getDefault().add(disposable)


        rxEvent = RxBus.getDefault().toObservable(Song::class.java)
                .subscribeWith(object : RxBusSubscriber<Song>() {
                    override fun onEvent(song: Song) {
                        filePath = song.path
                        if (filePath != null) {
                            fileMusi = File(filePath)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        Log.e(BaseActivity.TAG, "接收到---onError: $e")
//                        setSnackBar("该歌曲无法上传,请重新选择哟~", "", R.drawable.icon_fails)

                    }
                })
        RxBus.getDefault().add(rxEvent)

        rxEvent = RxBus.getDefault().toObservable(Song::class.java)
                .subscribeWith(object : RxBusSubscriber<Song>() {
                    override fun onEvent(song: Song) {
                        filePath = song.path
                        if (filePath != null) {
                            fileVideo = File(filePath)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        Log.e(BaseActivity.TAG, "接收到---onError: $e")
//                        setSnackBar("该mv无法上传,请重新选择哟~", "", R.drawable.icon_fails)

                    }
                })
        RxBus.getDefault().add(rxEvent)

        /**
         * 1原创 0翻唱 2.伴奏
         */
        rg_musicType.setOnCheckedChangeListener({ radioGroup, _ ->
            val radioButtonId = radioGroup.checkedRadioButtonId
            singType = if (radioButtonId == R.id.rb_firstSing) {
                1
            } else if(radioButtonId == R.id.rb_Accompaniment){
                2
            }else{
                0
            }

            Log.e(BaseActivity.TAG, "onCheckedChanged: $singType")
        })
        rg_can_down.setOnCheckedChangeListener({ radioGroup, _ ->
            val radioButtonId = radioGroup.checkedRadioButtonId
            canDownload = if (radioButtonId == R.id.rb_can_down) {
                1
            } else {
                0
            }

            Log.e(BaseActivity.TAG, "onCheckedChanged: $singType")
        })


        setOnPictureSelectedListener(object : OnPictureSelectedListener {
            override fun onPictureSelected(fileUri: Uri, bitmap: Bitmap?) {
                filePath = fileUri.encodedPath
                imagePath = Uri.decode(filePath)
                Log.e("TAG", "imagePath: $imagePath")
                Log.e("TAG", "filePath: $filePath")
                add_albumPic.setImageBitmap(BitmapFactory.decodeFile(filePath))
            }
        })


    }

    //进入相册显示图片
    private fun picture(){
        PictureSelector.create(this@UploadMusicActivity)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(false)// 是否裁剪
                .compress(false)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(true)// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                //                        .videoMaxSecond(15)
                //                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.REQUEST_PICTURE)//结果回调onActivityResult code
    }
    //进入相册显示视频
    private fun viedoChosed() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(this@UploadMusicActivity)
                .openGallery(PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(false)// 是否裁剪
                .compress(false)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(true)// 是否开启点击声音
//                .selectionMedia(selectList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                //                        .videoMaxSecond(15)
                //                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
    }

    var delectcount = 1
    var delectflag:Boolean = false



    var count=1
    private fun onViewClick() {
        mvrl = findViewById(R.id.mvrl)
        check_mv = findViewById(R.id.check_mv)
        if(check_mv.isChecked==true){
            mvrl.visibility = View.VISIBLE
        }else{
            mvrl.visibility = View.GONE
        }
        check_mv.setOnClickListener {
            if(count==1){
                check_mv.setChecked(true);
                count =2
                mvrl.visibility = View.VISIBLE
                private_songlist.setTextColor(Color.parseColor("#ff6699"))
            }else{
                check_mv.setChecked(false);
                count = 1
                mvrl.visibility = View.GONE
                private_songlist.setTextColor(Color.parseColor("#1a1919"))
//                private_songlist.setText("上传MV关闭")
            }

        }

        mv.setOnClickListener {
            Log.i("qqqqqq", "5")
            viedoChosed()
//            val n = Intent(this, MainActivity::class.java)
//            //n.putExtra("code", "typeofworks")
//            startActivity(n)
        }


        leftButton.setOnClickListener { finish() }
        bt_sure.setOnClickListener { release() }
        iv_cancle.setOnClickListener {
            beforeUpload.visibility = VISIBLE
            llUploading.visibility = GONE
        }
        iv_canclevideo.setOnClickListener {
            beforeUploadvideo.visibility = VISIBLE
            llUploadingvideo.visibility = GONE
            delectcount = 2
        }
        tv_musicType_tips.setOnClickListener {
            //现在不做跳转了
            val iN = Intent(this, UserNoticeActivity::class.java)
            iN.putExtra("code", "typeofworks")
//            startActivity(iN)
        }
        deleImg.setOnClickListener {
            add_albumPic.visibility = GONE
            deleImg.visibility = GONE
        }
        llLyric.setOnClickListener { goActivityForResult(AddLyricActivity::class.java, ADDLYRIC) }
        rl_musictype.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("selectMusicTag", selectedTagList as Serializable)
            bundle.putString("type", "song")
            bundle.putInt("num", 6)
            bundle.putString("title", "选择歌单标签")
//            goActivityForResult(ChooseMusicTagNew::class.java, GET_MUSIC_TYPE_REQ_CODE)
            goActivityForResult(ChooseMusicTagNew::class.java, bundle, GET_MUSIC_TYPE_REQ_CODE)
        }
        beforeUpload.setOnClickListener { showFileChooser() }
//        add_albumPic.setOnClickListener { choicePhotoWrapper() }
//        tv_addPic.setOnClickListener { choicePhotoWrapper() }
        add_albumPic.setOnClickListener {  picture() }
        tv_addPic.setOnClickListener {  picture() }

        tv_bind_status.setOnClickListener {
            val intent = Intent(this, BindPartnerActivity::class.java)
            if (!TextUtils.isEmpty(partnerJson)) {
                intent.putExtra(PARTNER_JSON, partnerJson)
            }
            startActivityForResult(intent, GET_PARTNER_REQU_CODE)
        }
    }
    private var catTitle: String? = ""//
    @AfterPermissionGranted(1)
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        when (intent) {
            null -> return
            else -> when {

                requestCode == 121 -> {
                    if (fileMusi == null) return
                    if (fileMusi!!.exists()) {
                        tv_songName.text = fileMusi?.name
                        val songInfo = MusicLoader.getSongInfo(fileMusi)
                        when {
                            songInfo != null -> {
                                val bitrate = songInfo.bitrate
                                when {
                                    bitrate != null -> {
                                        val integer = Integer.valueOf(bitrate) / 1000
                                        when {
                                            integer >= 128 -> startUploadMusic()
                                            else -> setSnackBar("请选择码率128kbs以上的歌曲上传哦~~", "", R.drawable.icon_fails)
                                        }
                                    }
                                    else -> setSnackBar("请选择码率128kbs以上的歌曲上传哦~~", "", R.drawable.icon_fails)
                                }

                            }
                        }
                    }
                }




//                GALLERY_REQUEST_CODE == requestCode -> startCropActivity(intent.data)
//                UCrop.REQUEST_CROP == requestCode -> handleCropResult(intent)
//                RESULT_ERROR == requestCode -> handleCropError(intent)
//                resultCode == Activity.RESULT_OK && requestCode == REQUEST_PICTURE -> {
                requestCode == PictureConfig.REQUEST_PICTURE -> {

                    selectList = PictureSelector.obtainMultipleResult(intent)
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    add_albumPic.visibility = View.VISIBLE
                    for (media in selectList) {
                        Log.i("图片-----》", media.path)
                        Glide.with(this@UploadMusicActivity)
                                .load(media.path)
                                .into(add_albumPic)
                        Log.i("eeeeeeeee",""+media.path)
                        filePath = media.path
                    }

                    imagePath = Uri.decode(filePath)
                    Log.e("TAG", "imagePath: $imagePath")
                    Log.e("TAG", "filePath: $filePath")
//                        add_albumPic.setImageBitmap(BitmapFactory.decodeFile(filePath))
                    val file = File(imagePath)
                    files.add(file)
                    val fileUploadUtil = FileUploadUtil()
                    fileUploadUtil.setFileList(files)
                    fileUploadUtil.upload(this, 1, object : FileUploadUtil.UpLoadCallback {
                        override fun upLoadSuccess(finishBeans: List<FileBean.DataBean>) {
                            if (finishBeans != null && finishBeans.isNotEmpty()) {
                                if(finishBeans.size == 1){
                                    imgHash = finishBeans[0].imgpic
                                }else if(finishBeans.size == 2){
                                    imgHash = finishBeans[1].imgpic
                                }else if(finishBeans.size == 3){
                                    imgHash = finishBeans[2].imgpic
                                }else if(finishBeans.size == 4){
                                    imgHash = finishBeans[3].imgpic
                                }else if(finishBeans.size == 5){
                                    imgHash = finishBeans[4].imgpic
                                }else if(finishBeans.size == 6){
                                    imgHash = finishBeans[5].imgpic
                                }else if(finishBeans.size == 7){
                                    imgHash = finishBeans[6].imgpic
                                }
                                isUploaded = true
                                tv_videoprogress.text = "上传完成"
                                progress_uoloadvideo.progress = 100
                            }
                        }




                        override fun upLoadFailure(request: PutObjectRequest, serviceException: ServiceException?) {
//                            llUploadingvideo.visibility = View.GONE
//                            beforeUploadvideo.visibility = View.VISIBLE
                            hideLoadingView()

                        }
                    })

//                    val selectedImages = BGAPhotoPickerActivity.getSelectedImages(intent)
//                    if (selectedImages != null && selectedImages.size > 0) {
//                        val path = selectedImages[0]
//                        startCropActivity(Uri.fromFile(File(path)))
//                    } else {
//                        setSnackBar("请重新选择图片", "", R.drawable.icon_tips_bad)
//                    }
                }





                requestCode == PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(intent)
                    fileVideo = File(selectList[0].path)


                    if (fileVideo == null) return
                    if (fileVideo!!.exists()) {
                        tv_videoName.text = fileVideo?.name
                        Log.e("ttt",""+fileVideo?.name)
                        val mvInfo = MusicLoader.getSongInfo(fileMusi)
                        when {
                            mvInfo != null -> {
                                val bitrate = mvInfo.bitrate
                                when {
                                    bitrate != null -> {
                                        val integer = Integer.valueOf(bitrate) / 1000
                                        when {
                                            integer >= 128 -> startUploadMusic()
                                            else -> setSnackBar("请选择码率128kbs以上的mv上传哦~~", "", R.drawable.icon_fails)
                                        }
                                    }
                                    else -> setSnackBar("请选择码率128kbs以上的mv上传哦~~", "", R.drawable.icon_fails)
                                }

                            }
                        }
                    }
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的

                    //这里是为了区分是否是视频
                    delectflag = true

                    val files = ArrayList<File>()
                    files.add(fileVideo!!)
                    val fileUploadUtil = FileUploadUtil()
                    fileUploadUtil.setFileList(files)
                    llUploadingvideo.visibility = View.VISIBLE
                    beforeUploadvideo.visibility = View.GONE
                    fileUploadUtil.upload(this, 3, object : FileUploadUtil.UpLoadCallback {
                        override fun upLoadSuccess(finishBeans: List<FileBean.DataBean>) {
                            if (finishBeans != null && finishBeans.isNotEmpty()) {
                                mvfileHash = finishBeans[0].mv
                                isUploaded = true
                                tv_videoprogress.text = "上传完成"
                                progress_uoloadvideo.progress = 100
                                tv_videoName.visibility = View.VISIBLE
//                                tv_videoName.text =selectList[0].path//获取文件名字

                                delectcount =1
                            }
                        }




                        override fun upLoadFailure(request: PutObjectRequest, serviceException: ServiceException?) {
                            llUploadingvideo.visibility = View.GONE
                            beforeUploadvideo.visibility = View.VISIBLE
                            hideLoadingView()

                        }
                    })
                }



                //音乐分类和标签这是那个回调
                requestCode == GET_MUSIC_TYPE_REQ_CODE -> {
//                    catId = intent.getIntExtra("typeId", 0)
                    var catTitle = intent.getStringExtra("typeTitle")
                    val selectMusicTagTag = CacheUtils.getString(this@UploadMusicActivity, "selectMusicTagTag", "")
                    if (null!=(JSON.parseArray(selectMusicTagTag, MusicTypeBean.DataBean::class.java) as ArrayList<MusicTypeBean.DataBean>)&&(JSON.parseArray(selectMusicTagTag, MusicTypeBean.DataBean::class.java) as ArrayList<MusicTypeBean.DataBean>).size>0){
                        tv_musicType.text = (JSON.parseArray(selectMusicTagTag, MusicTypeBean.DataBean::class.java) as ArrayList<MusicTypeBean.DataBean>).get(0).title
                        catId = (JSON.parseArray(selectMusicTagTag, MusicTypeBean.DataBean::class.java) as ArrayList<MusicTypeBean.DataBean>).get(0).id
                    }else{
                        tv_musicType.text = "";
                    }
                    setMusicTagHash()
                }
                requestCode == GET_PARTNER_REQU_CODE && resultCode == Activity.RESULT_OK -> when {intent != null -> {
                    val stringExtra = intent.getStringExtra(PARTNER_JSON)
                    when {
                        !TextUtils.isEmpty(stringExtra) -> {
                            Log.e(BaseActivity.TAG, "onActivityResult: $stringExtra")
                            partnerJson = stringExtra
                            tv_bind_status.setText("已关联")
                        }
                        else -> tv_bind_status.setText("")
                    }
                }
                }
                requestCode == ADDLYRIC && resultCode == RESULT_OK -> {
                    lyricStr = intent.getStringExtra("lyric")
                    when {
                        lyricStr != null -> tv_lyric_status.text = "已添加"
                        else -> tv_lyric_status.text = ""
                    }
                }
            }
        }
    }

    /**
     * 上传歌曲文件
     */
    @AfterPermissionGranted(1)
    private fun startUploadMusic() {
        if (fileMusi == null) return
        beforeUpload.visibility = View.GONE
        llUploading.visibility = View.VISIBLE
        val files = ArrayList<File>()
        files.add(fileMusi!!)
        val fileUploadUtil = FileUploadUtil()
        fileUploadUtil.setFileList(files)
        fileUploadUtil.upload(this, 2, object : FileUploadUtil.UpLoadCallback {
            override fun upLoadSuccess(finishBeans: List<FileBean.DataBean>) {
                if (finishBeans != null && finishBeans.isNotEmpty()) {
                    fileHash = finishBeans[0].video
                    isUploaded = true
                    tv_progress.text = "上传完成"
                    progress_uoload.progress = 100
                }
            }

            override fun upLoadFailure(request: PutObjectRequest, serviceException: ServiceException?) {
                llUploading.visibility = View.GONE
                beforeUpload.visibility = View.VISIBLE
                hideLoadingView()

            }
        })
    }

    /**
     * 发布
     */
    private fun release() {
//        showLoadingView()
        val files = ArrayList<File>()
        if (TextUtils.isEmpty(et_intro.text) || TextUtils.isEmpty(lyricStr) || TextUtils.isEmpty(et_musicName.getText())) {
            hideLoadingView()
            setSnackBar("请完善上传信息", "", R.drawable.icon_fails)
            return
        }
        val params = HttpParams()
        params.put("category", catId.toString() + "")
        params.put("title", et_musicName.text.toString())
        params.put("isdown", canDownload.toString() + "")
        params.put("intro", et_intro.text.toString())
        params.put("video", fileHash)
        params.put("mv", mvfileHash)
        params.put("music_type", singType.toString() + "")
        params.put("imgpic", imgHash)
        params.put("tag", hashTagJson)
        params.put("admin_status", admin_status)
        params.put("lyrics", StringUtils.isEmpty(lyricStr))
        if (!TextUtils.isEmpty(partnerJson)) {
            val uploadJsonBean = JSON.parseObject(partnerJson, BindPartnerActivity.UploadJsonBean::class.java)
            val member = uploadJsonBean.member
            val jsonString = JSON.toJSONString(member)
            params.put("member", jsonString)
        }
        if (list != null && list.size > 0) {
            for (typebean in list) {
                params.put(typebean.title, typebean.id.toString() + "")
            }
        }
        NetWork.releaseMusic(this@UploadMusicActivity, musicId, params, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                if (admin_status == "1") {
                    setSnackBar("你的作品已成功上传，工作人员将在12h内进行审核~~", "", R.drawable.icon_success)
                }
                finish()
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    @AfterPermissionGranted(1)
    private fun showFileChooser() {
        RxPermissions(this).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { permission ->
                    if (permission.granted) {
                        isUploaded = false
                        val intent = Intent(this@UploadMusicActivity, BeforeScanActivity::class.java)
                        startActivity(intent)
                    } else {
                        setSnackBar("未获得存储权限", "", R.drawable.icon_fails)
                    }
                }


    }

    @AfterPermissionGranted(1)
    private fun choicePhotoWrapper() {//去选择照片
        val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            val takePhotoDir = File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto")
            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, 1, null, false), REQUEST_CODE_CHOOSE_PHOTO)
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:  1.访问设备上的照片  2.拍照", 1, *perms)
        }
    }

    private fun startCropActivity(uri: Uri) {
        UCrop.of(uri, uri)
                .withAspectRatio(1f, 1f)
                .withTargetActivity(CropImgActivity::class.java)
                .start(this)
    }

    private fun setOnPictureSelectedListener(l: OnPictureSelectedListener) {
        this.mOnPictureSelectedListener = l
    }

    override fun onPermissionsGranted(i: Int, list: List<String>) {

    }

    override fun onPermissionsDenied(i: Int, list: List<String>) {
        setSnackBar("你拒绝了源音塘访问SD卡的权限", "", R.drawable.icon_fails)
    }

    interface OnPictureSelectedListener {
        fun onPictureSelected(fileUri: Uri, bitmap: Bitmap?)
    }

    private fun handleCropResult(result: Intent) {
        if (result == null) return
        val resultUri = UCrop.getOutput(result)
        if (null != resultUri && null != mOnPictureSelectedListener) {
            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, resultUri)
                add_albumPic.visibility = VISIBLE
                deleImg.visibility = VISIBLE
                add_albumPic.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mOnPictureSelectedListener?.onPictureSelected(resultUri, bitmap)

        } else {
            //ToastUtil.showToast(getApplicationContext(), "无法剪切选择图片");
        }
    }

    private fun handleCropError(result: Intent) {
        val cropError = UCrop.getError(result)
        if (cropError != null) {
            Log.e("TAG", "handleCropError: ", cropError)
            //ToastUtil.showToast(getApplicationContext(), cropError.getMessage());
        } else {
            //ToastUtil.showToast(getApplicationContext(), "无法剪切选择图片");
        }
    }

    override fun onResume() {
        super.onResume()
        fileMusi?.let {
            if (!isUploaded && it?.exists()) {
                tv_songName.text = it?.name
                val songInfo = MusicLoader.getSongInfo(it)
                if (songInfo != null) {
                    val bitrate = songInfo.bitrate
                    if (bitrate != null) {
                        val integer = Integer.valueOf(bitrate) / 1000
                        if (integer < 128) {
                            setSnackBar("请选择码率128kbs以上的歌曲上传哦~~", "", R.drawable.icon_fails)
                        } else {
                            startUploadMusic()
                        }
                    } else {
                        setSnackBar("请选择码率128kbs以上的歌曲上传哦~~", "", R.drawable.icon_fails)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.getDefault().remove(disposable)
        RxBus.getDefault().remove(rxEvent)
        CacheUtils.setString(this@UploadMusicActivity, "selectMusicTag", "")
    }

    private fun setMusicTagHash() {
        hashTagJson = CacheUtils.getString(this@UploadMusicActivity, "selectMusicTag", "[]") ?: "[]"
        list = JSON.parseArray(hashTagJson, PondTagListBean.DataBean.TagBean::class.java) as ArrayList<PondTagListBean.DataBean.TagBean>
        selected_tag.removeAllViews()
        for (i in list.indices) {
            if (i < 9) {
                val tv = LayoutInflater.from(this@UploadMusicActivity).inflate(R.layout.search_label_tv, selected_tag, false) as SearTextView
                tv.text = list[i].title
                selected_tag.addView(tv)
            }
        }
    }

    class ImgpicInfoBean : Serializable {
        /**
         * ext :
         * w :
         * h :
         * size : 330492
         * is_long : 0
         * md5 : c7adcb987e5224301258c6f7efb2d53e
         * link : http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3
         */

        var ext: String? = null
        var w: String? = null
        var h: String? = null
        var size: String? = null
        var is_long: String? = null
        var md5: String? = null
        var link: String? = null
    }

    companion object {
        const val GET_MUSIC_TYPE_REQ_CODE = 12
        const val GET_MUSIC_TYPE_RESU_CODE = 13
        const val GET_PARTNER_REQU_CODE = 14
        const val PARTNER_JSON = "PARTNER_JSON"
        const val DATA = "data"
    }

}