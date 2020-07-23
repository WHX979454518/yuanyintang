package com.mxkj.yuanyintang.mainui.login_regist

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast

import com.alibaba.fastjson.JSON
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.kevin.crop.UCrop
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.utils.ali_file_upload.FileUploadUtil
import com.mxkj.yuanyintang.utils.ali_file_upload.FileBean
import com.mxkj.yuanyintang.utils.image.CropImgActivity
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerActivity
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil
import com.mxkj.yuanyintang.utils.app.ActivityCollector
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.widget.choosepic_popuowindow.ChoosePicPopWindow
import java.io.File
import java.io.IOException
import java.util.ArrayList
import com.mxkj.yuanyintang.mainui.dynamic.activity.PublishDynamic.Companion.REQUEST_CODE_CHOOSE_PHOTO
import okhttp3.Headers
import pub.devrel.easypermissions.EasyPermissions
import com.mxkj.yuanyintang.utils.constant.Constants.Other.EM_LOGIN
import com.mxkj.yuanyintang.widget.choosepic_popuowindow.ChoosePicPopWindow.tempFile
import kotlinx.android.synthetic.main.activity_add_self_info.*

class AddSelfInfo : StandardUiActivity(), EasyPermissions.PermissionCallbacks {
    private var hash: String? = null//头像hash
    private var mDestinationUri: Uri? = null
    /**
     * 图片选择的监听回调
     */
    private var mOnPictureSelectedListener: OnPictureSelectedListener? = null
    private var sex = -1

    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun setLayoutId(): Int {
        return R.layout.activity_add_self_info
    }

    override fun initView() {
        hideTitle(true)
        title = "关于我们"
        StatusBarUtil.baseTransparentStatusBar(this)
    }

    override fun initEvent() {
        onClicked()
        val string = CacheUtils.getString(this@AddSelfInfo, Constants.User.USER_JSON)
        if (string != null) {
            UserInfoUtil.getUserInfoByJson(string) { infoBean ->
                if (infoBean != null && infoBean.data != null) {
                    if (infoBean.data!!.head_link != null) {
                        ImageLoader.with(this@AddSelfInfo)
                                .override(100, 100)
                                .url(infoBean.data!!.head_link)
                                .scale(ScaleMode.CENTER_CROP)
                                .asCircle()
                                .into(icon)
                    }
                    et_nick!!.setText(infoBean.data!!.nickname)
                }
            }
        }
        setOnPictureSelectedListener(object : OnPictureSelectedListener {
            override fun onPictureSelected(fileUri: Uri, bitmap: Bitmap?) {
                changeIcon(fileUri)
                icon!!.setImageBitmap(bitmap)
            }
        })

        rgrop!!.setOnCheckedChangeListener { radioGroup, i ->
            val checkedRadioButtonId = radioGroup.checkedRadioButtonId
            when (checkedRadioButtonId) {
                R.id.rb_boy -> sex = 1
                R.id.rb_girl -> sex = 0
            }
        }

    }

    override fun initData() {

    }

    fun onClicked() {
        val intent = Intent(this, UserNoticeActivity::class.java)
        finish.setOnClickListener { finish() }
        skip.setOnClickListener { autoLogin() }
        icon.setOnClickListener {
            ChoosePicPopWindow.showPopupWindow(this, findViewById(R.id.main), false, object : ChoosePicPopWindow.ChoosePicCallback {
                override fun chooseFromLocal(pickIntent: Intent) {}

                override fun tackPhoto(takeIntent: Intent) {
                    startActivityForResult(takeIntent, CAMERA_REQUEST_CODE)
                }
            })
            val lp = window.attributes
            lp.alpha = 0.7f
            window.attributes = lp
        }
        bt_sure.setOnClickListener {
            if (!TextUtils.isEmpty(hash)) {
                showLoadingView()
                val params = HttpParams()
                params.put("head", hash)
                params.put("nickname", et_nick!!.text.toString())
                if (sex != -1) {
                    params.put("sex", sex.toString() + "")
                }
                Log.e(TAG, "onViewClicked: $params")
                NetWork.addSelfInfo(this, params, object : NetWork.TokenCallBack {
                    override fun doNext(resultData: String, headers: Headers?) {
                        hideLoadingView()
                        val intent = Intent(this@AddSelfInfo, HotUserRecommend::class.java)
                        intent.putExtra("isToHome", true)
                        startActivity(intent)
                        ActivityCollector.finishAll()
                    }

                    override fun doError(msg: String) {
                        hideLoadingView()
                    }

                    override fun doResult() {

                    }
                })
            } else {
                setSnackBar("请添加头像", "", R.drawable.icon_fails)
            }
        }
    }

    private fun autoLogin() {
        showLoadingView()
        val mobile = intent.getStringExtra("mobile")
        val pwd = intent.getStringExtra("pwd")
        NetWork.PwdLogin(this, mobile, pwd,"", object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val jsonObject = JSON.parseObject(resultData)
                val code = jsonObject.getInteger("code")
                if (code == 0) {
                    val checkObj = jsonObject.getJSONObject("data")
                    val key = checkObj.getString("key")
                    val mobile = checkObj.getString("mobile")
                    val email = checkObj.getString("email")
                    val intentAuth = Intent(this@AddSelfInfo, NewDeviceAuthActivity::class.java)
                    intentAuth.putExtra("key", StringUtils.isEmpty(key))
                    intentAuth.putExtra("mobile", StringUtils.isEmpty(mobile))
                    intentAuth.putExtra("email", StringUtils.isEmpty(email))
                    startActivity(intentAuth)
                } else {
                    hideLoadingView()
                    CacheUtils.setString(this@AddSelfInfo, Constants.User.USER_JSON, resultData)
                    CacheUtils.setBoolean(this@AddSelfInfo, Constants.User.IS_LOGIN, true)
                    hideLoadingView()
                    val intent = Intent(this@AddSelfInfo, HotUserRecommend::class.java)
                    intent.putExtra("isToHome", true)
                    startActivity(intent)
                    //                startService(new Intent(AddSelfInfo.this, BackgroundService.class));
                    sendBroadcast(Intent(EM_LOGIN))

                    ActivityCollector.finishAll()
                }


            }
            override fun doError(msg: String) {
                hideLoadingView()
            }

            override fun doResult() {

            }
        })
    }

    fun changeIcon(fileUri: Uri) {
        val filePath = fileUri.encodedPath
        val imagePath = Uri.decode(filePath)
        val file = File(imagePath)
        val add = ArrayList<File>()
        add.add(file)
        val fileUploadUtil = FileUploadUtil()
        fileUploadUtil.setFileList(add)
        fileUploadUtil.upload(this, 1, object : FileUploadUtil.UpLoadCallback {
            override fun upLoadSuccess(finishBeans: List<FileBean.DataBean>) {
                hash = finishBeans[0].imgpic
            }

            override fun upLoadFailure(request: PutObjectRequest, serviceException: ServiceException?) {

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            Log.e(TAG, "onActivityResult: ")
            when (requestCode) {
                UCrop.REQUEST_CROP    // 裁剪图片结果
                -> handleCropResult(data)
                UCrop.RESULT_ERROR    // 裁剪图片错误
                -> handleCropError(data)
                CAMERA_REQUEST_CODE -> startCropActivity(Uri.fromFile(tempFile))
                REQUEST_CODE_CHOOSE_PHOTO -> {
                    val selectedImages = BGAPhotoPickerActivity.getSelectedImages(data)
                    if (selectedImages.size > 0) {
                        val path = selectedImages[0]
                        startCropActivity(Uri.fromFile(File(path)))
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startCropActivity(uri: Uri) {
        mDestinationUri = Uri.fromFile(File(cacheDir, "cropImage.jpeg"))
        val uCrop = UCrop.of(uri, mDestinationUri!!).withTargetActivity(CropImgActivity::class.java)
        uCrop.withAspectRatio(3f, 3f)
        uCrop.withMaxResultSize(300, 300)
        uCrop.start(this)
    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private fun handleCropResult(result: Intent) {
        deleteTempPhotoFile()
        val resultUri = UCrop.getOutput(result)
        if (null != resultUri && null != mOnPictureSelectedListener) {
            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, resultUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            mOnPictureSelectedListener!!.onPictureSelected(resultUri, bitmap)
        } else {
            Toast.makeText(applicationContext, "无法剪切选择图片", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    private fun handleCropError(result: Intent) {
        deleteTempPhotoFile()
        val cropError = UCrop.getError(result)
        if (cropError != null) {
            Toast.makeText(applicationContext, cropError.message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, "无法剪切选择图片,请重试", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 删除拍照临时文件
     */
    private fun deleteTempPhotoFile() {
        val tempFile = File(ChoosePicPopWindow.tempFile.absolutePath)
        if (tempFile.exists() && tempFile.isFile) {
            tempFile.delete()
        }
    }

    /**
     * 设置图片选择的回调监听
     *
     * @param l
     */
    fun setOnPictureSelectedListener(l: OnPictureSelectedListener) {
        this.mOnPictureSelectedListener = l
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {

    }

    /**
     * 图片选择的回调接口
     */
    interface OnPictureSelectedListener {
        /**
         * 图片选择的监听回调
         *
         * @param fileUri
         * @param bitmap
         */
        fun onPictureSelected(fileUri: Uri, bitmap: Bitmap?)
    }

    override fun onResume() {
        super.onResume()
        ActivityCollector.addActivity(this)
    }

    companion object {
        const val GALLERY_REQUEST_CODE = 0    // 相册选图标记
        const val CAMERA_REQUEST_CODE = 1    // 相机拍照标记
    }
}
