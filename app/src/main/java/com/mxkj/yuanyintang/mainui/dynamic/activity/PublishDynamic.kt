package com.mxkj.yuanyintang.mainui.dynamic.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast

import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.flyco.dialog.listener.OnBtnClickL
import com.flyco.dialog.widget.MaterialDialog
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.ali_file_upload.FileUploadUtil
import com.mxkj.yuanyintang.utils.ali_file_upload.FileBean
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerActivity
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerPreviewActivity
import com.mxkj.yuanyintang.utils.photopicker.widget.BGASortableNinePhotoLayout
import com.mxkj.yuanyintang.mainui.emotionkeyboard.fragment.EmotionMainFragment
import com.mxkj.yuanyintang.mainui.emotionkeyboard.utils.LogUtils
import com.mxkj.yuanyintang.mainui.pond.activity.AddMusicNew
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.uiutils.SoftKeyBoardListener
import com.mxkj.yuanyintang.utils.image.IMGCompressUtils

import java.io.File
import java.io.IOException
import java.util.ArrayList

import com.jakewharton.rxbinding2.view.RxView
import okhttp3.Headers
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

import com.mxkj.yuanyintang.mainui.pond.activity.AddMusicNew.REQUEST_CODE_SELECTED_MUSIC_BEAN
import kotlinx.android.synthetic.main.activity_publish_dynamic.*

class PublishDynamic : StandardUiActivity(), EasyPermissions.PermissionCallbacks, BGASortableNinePhotoLayout.Delegate {
    private var mInputManager: InputMethodManager? = null//软键盘管理类
    private var maxNumPhoto = 9//最多可以选多少张图片,默认9
    private val path = ArrayList<String>()//压前的路径
    private val filesUp = ArrayList<File>()//压缩后要上传的图片
    private val selectedList = ArrayList<String>()//选择的本地的图片
    private var musicId: Int = 0
    private var selectedBean: MusicInfo.DataBean? = null
    private var needCompress = true
    private var hashStr = ""//图片hash
    private var emotionMainFragment: EmotionMainFragment? = null
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    /**
     * 是否显示软件盘
     *
     * @return
     */
    private val isSoftInputShown: Boolean
        get() = supportSoftInputHeight != 0

    private val supportSoftInputHeight: Int
        get() {
            val r = Rect()
            window.decorView.getWindowVisibleDisplayFrame(r)
            val screenHeight = window.decorView.rootView.height
            var softInputHeight = screenHeight - r.bottom
            if (Build.VERSION.SDK_INT >= 20) {
                softInputHeight = softInputHeight
            }
            if (softInputHeight < 0) {
                LogUtils.w("EmotionKeyboard--Warning: value of softInputHeight is below zero!")
            }
            return softInputHeight
        }

    public override fun setLayoutId(): Int {
        return R.layout.activity_publish_dynamic
    }
    override fun initView() {
        hideTitle(true)
        initEmotionMainFragment()
        et_content.isFocusable = true
        et_content.isFocusableInTouchMode = true
        et_content.requestFocus()
    }

    override fun initData() {
    }

    override fun initEvent() {
        RxView.clicks(delet_seletmusic).subscribe { MaterialDialog("取消发布") }
        RxView.clicks(finishtv).subscribe {
            musicId = 0
            ll_selectmusic.visibility = View.GONE
            finish()
        }
        RxView.clicks(publish).subscribe {
            if (!TextUtils.isEmpty(et_content.text)) {
                publish.isClickable = false
                publishDynamic()
            } else {
                setSnackBar("您还什么都没写呢", "", R.drawable.icon_tips_bad)
            }
        }
        RxView.clicks(emoji).subscribe { emotionMainFragment!!.mEmotionKeyboard.showEmotionLayout() }
        RxView.clicks(ll_hideSoft).subscribe {
            if (isSoftInputShown) {
                hideSoftInput()
            } else {
                showSoftInput()
            }
        }
        RxView.clicks(picture).subscribe { choicePhotoWrapper() }
        RxView.clicks(music).subscribe {
            val intent = Intent(this, AddMusicNew::class.java)
            startActivityForResult(intent, REQUEST_CODE_SELECTED_MUSIC_BEAN)
        }
        mInputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        nineimg_layout.maxItemCount = maxNumPhoto
        nineimg_layout.isSortable = true
        // 设置拖拽排序控件的代理
        nineimg_layout.setDelegate(this)
        if (musicId == 0) {
            ll_selectmusic.visibility = View.GONE
        }
        SoftKeyBoardListener.setListener(this, object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
                nineimg_layout.visibility = View.GONE
            }

            override fun keyBoardHide(height: Int) {
                if (selectedList != null && selectedList.size > 0) {
                    nineimg_layout.visibility = View.VISIBLE
                }
            }
        })
        et_content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                tv_total_textnum!!.text = (500 - editable.length).toString() + ""
            }
        })

    }

    private fun showSoftInput() {
        img_softKeytag?.setImageResource(R.drawable.icon_soft_down)
        et_content.requestFocus()
        et_content.post({ mInputManager!!.showSoftInput(et_content, 0) })
    }

    /**
     * 隐藏软件盘
     */
    private fun hideSoftInput() {
        img_softKeytag?.setImageResource(R.drawable.icon_soft_up)
        mInputManager?.hideSoftInputFromWindow(et_content.windowToken, 0)
    }


    private fun publishDynamic() {
        path.clear()
        showLoadingView()
        nineimg_layout.setItemSpanCount(4)
        path.addAll(nineimg_layout.data!!)
        if (path.size > 0) {//有图片
            for (i in path.indices) {
                if (path.size == 1 || path[i].contains("gif")) {
                    needCompress = false
                }
            }
            if (needCompress) {
                try {
                    IMGCompressUtils.CompressorImage1(path.size, applicationContext, path) { files ->
                        filesUp.clear()
                        filesUp.addAll(files)
                        //上传图片
                        upLoadImgs()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            } else {
                filesUp.clear()
                for (i in path.indices) {
                    val file = File(path[i])
                    filesUp.add(file)
                }
                //上传图片
                upLoadImgs()
            }
        } else {//没图片
            publish(null)
        }
    }

    private fun upLoadImgs() {
        val fileUploadUtil = FileUploadUtil()
        fileUploadUtil.setFileList(filesUp)
        fileUploadUtil.upload(this, 1, object : FileUploadUtil.UpLoadCallback {
            override fun upLoadSuccess(finishBeans: List<FileBean.DataBean>) {
                if (finishBeans != null) {
                    for (i in finishBeans.indices) {
                        var hash: String = finishBeans[i].imgpic as String
                        hashStr += if (i != finishBeans.size - 1) {
                            "$hash,"
                        } else {
                            hash
                        }
                    }
                    publish(hashStr)
                }
            }

            override fun upLoadFailure(request: PutObjectRequest, serviceException: ServiceException?) {

            }
        })
    }

    private fun publish(hashStr: String?) {
        val params = HttpParams()
        params.put("push", 1.toString() + "")
        if (hashStr != null) {
            Log.e(TAG, "publish: $hashStr")
            params.put("imglist", hashStr)
        }
        params.put("depict", et_content.text.toString())
        if (musicId != 0) {
            params.put("musicId", musicId.toString() + "")//音乐id
            params.put("type", 1.toString() + "")
        } else {
            params.put("type", 3.toString() + "")
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
                publish.isClickable = true
                hideLoadingView()
            }
        })
    }

    /**
     * 九宫格控件的监听事件
     */
    override fun onClickAddNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout, view: View, position: Int, models: ArrayList<String>) {
        choicePhotoWrapper()
    }

    override fun onClickDeleteNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout, view: View, position: Int, model: String, models: ArrayList<String>) {
        nineimg_layout.removeItem(position)
        selectedList.clear()
        selectedList.addAll(nineimg_layout.data)
        maxNumPhoto = 9 - nineimg_layout.data.size
        Log.e("TAG", "onClickDeleteNinePhotoItem: $maxNumPhoto")
        nineimg_layout.isPlusEnable = nineimg_layout.data.size > 0
    }

    override fun onClickNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout, view: View, position: Int, model: String, models: ArrayList<String>) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, nineimg_layout.maxItemCount, models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW)
    }

    /**
     * 去相册选择照片
     */
    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private fun choicePhotoWrapper() {//去选择照片
        val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            val takePhotoDir = File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto")
            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, maxNumPhoto, null, false), REQUEST_CODE_CHOOSE_PHOTO)
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:  1.访问设备上的照片  2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, *perms)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (requestCode == REQUEST_CODE_PERMISSION_PHOTO_PICKER) {
            Toast.makeText(this@PublishDynamic, "您拒绝了选取照片所需的权限!", Toast.LENGTH_SHORT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
                nineimg_layout.addMoreData(BGAPhotoPickerActivity.getSelectedImages(data))
                val selectedImages = BGAPhotoPickerActivity.getSelectedImages(data)
                onPicResult(selectedImages)
            } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
                nineimg_layout.data = BGAPhotoPickerPreviewActivity.getSelectedImages(data)
                val selectedImages = BGAPhotoPickerActivity.getSelectedImages(data)
                onPicResult(selectedImages)
            } else if (requestCode == REQUEST_CODE_SELECTED_MUSIC_BEAN && resultCode == REQUEST_CODE_SELECTED_MUSIC_BEAN) {
                ll_selectmusic.visibility = View.VISIBLE
                selectedBean = data.getSerializableExtra("selectedBean") as MusicInfo.DataBean
                if (selectedBean != null) {
                    musicId = selectedBean!!.id
                    tv_selectmusic.text = selectedBean!!.title
                    tv_selectsinger.text = selectedBean!!.nickname
                    if (selectedBean!!.imgpic_info != null) {
                        ImageLoader.with(this)
                                .url(selectedBean!!.imgpic_info!!.link)
                                .placeHolder(R.drawable.nothing)
                                .error(R.drawable.nothing)
                                .into(img_selected)
                    }

                }
            }
            if (BGAPhotoPickerActivity.getSelectedImages(data) != null && BGAPhotoPickerActivity.getSelectedImages(data).size > 0) {
                maxNumPhoto = 9 - BGAPhotoPickerActivity.getSelectedImages(data).size
            }
        }

    }

    private fun onPicResult(selectedImages: ArrayList<String>) {
        selectedList.clear()
        selectedList.addAll(selectedImages)
        if (selectedImages.size > 0) {
            nineimg_layout.visibility = View.VISIBLE
        } else {
            nineimg_layout.visibility = View.GONE
        }
    }

    /**
     * 初始化表情面板
     */
    fun initEmotionMainFragment() {
        //构建传递参数
        val bundle = Bundle()
        //绑定主内容编辑框   true  表示使用表情键盘的输入框，false使用键盘之外的
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, false)
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, true)//隐藏表情键盘的输入框和发送按钮
        //        //隐藏控件
        //替换fragment
        //创建修改实例
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment::class.java, bundle)
        emotionMainFragment!!.bindToContentView(et_content as EditText)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onResume() {
        super.onResume()
        if (selectedList != null && selectedList.size > 0) {
            nineimg_layout.visibility = View.VISIBLE
        } else {
            nineimg_layout.visibility = View.GONE
        }
    }

    private fun MaterialDialog(content: String) {
        val dialog = MaterialDialog(this)
        dialog.content(content)
                .btnText("取消", "确定")//
                .titleTextSize(16f)
                .titleTextColor(ContextCompat.getColor(this, R.color.color_14_text))
                .contentTextColor(ContextCompat.getColor(this, R.color.color_66_text))
                .contentTextSize(14f)
                .btnTextSize(14F)
                .btnTextColor(ContextCompat.getColor(this, R.color.base_red), ContextCompat.getColor(this, R.color.base_red))
                .showAnim(null)//
                .dismissAnim(null)//
                .show()

        dialog.setOnBtnClickL(
                OnBtnClickL //left btn click listener
                {
                    dialog.dismiss()
                },
                OnBtnClickL //right btn click listener
                {
                    dialog.dismiss()
                    finish()
                }
        )
    }

    companion object {
        const val REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1
        const val REQUEST_CODE_CHOOSE_PHOTO = 2
        const val REQUEST_CODE_PHOTO_PREVIEW = 3
    }
}
