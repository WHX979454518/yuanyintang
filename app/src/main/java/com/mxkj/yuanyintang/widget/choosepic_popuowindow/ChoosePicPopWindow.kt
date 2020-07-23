package com.mxkj.yuanyintang.widget.choosepic_popuowindow

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerActivity
import com.mxkj.yuanyintang.utils.photopicker.util.BGAPhotoPickerUtil
import com.tbruyelle.rxpermissions2.RxPermissions

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import com.mxkj.yuanyintang.mainui.dynamic.activity.PublishDynamic.Companion.REQUEST_CODE_CHOOSE_PHOTO

object ChoosePicPopWindow {
    private const val TAG = "ChoosePicPopWindow"
    lateinit var popupWindow: PopupWindow
    private val imgType = "image/jpeg"
    const val FILE_NAME = "temp_%s.jpeg"
    const val GALLERY_REQUEST_CODE = 0    // 相册选图标记
    const val CAMERA_REQUEST_CODE = 1    // 相机拍照标记
    open lateinit var tempFile: File
    fun showPopupWindow(context: Context, view: View, isBGAPhoto: Boolean?, callback: ChoosePicCallback) {
        val contentView = LayoutInflater.from(context).inflate(
                R.layout.layout_popwindow_choose_img, null)
        popupWindow = PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popupWindow.setOnDismissListener {
            val lp = (context as Activity).window.attributes
            lp.alpha = 1f
            context.window.attributes = lp
        }
        val tkPhoto = contentView.findViewById<View>(R.id.tv_tkpic) as TextView
        val tv_choose = contentView.findViewById<View>(R.id.tv_choose) as TextView
        val cancle = contentView.findViewById<View>(R.id.cancle) as TextView
        cancle.setOnClickListener { popupWindow.dismiss() }
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
        val filename = String.format(FILE_NAME, dateFormat.format(Date()))
        val mImageStoreDir = File(Environment.getExternalStorageDirectory(), "/YYT/IMGCA/")
        if (!mImageStoreDir.exists()) {
            mImageStoreDir.mkdirs()
        }
        tempFile = File(mImageStoreDir, filename)
        tkPhoto.setOnClickListener {
            popupWindow.dismiss()
            RxPermissions(context as Activity).requestEach(Manifest.permission.CAMERA).subscribe { permission ->
                if (permission.granted) {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (takePictureIntent.resolveActivity(BGAPhotoPickerUtil.sApp!!.packageManager) != null) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile))
                        } else {
                            Log.d(TAG, "accept: ------->" + tempFile.absolutePath)
                            val contentValues = ContentValues(1)
                            contentValues.put(MediaStore.Images.Media.DATA, tempFile.absolutePath)
                            contentValues.put(MediaStore.Images.Media.MIME_TYPE, imgType)
                            val uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        }
                    }
                    callback.tackPhoto(takePictureIntent)
                } else if (permission.shouldShowRequestPermissionRationale) {
                    (context as BaseActivity).setSnackBar("没有拍照权限,请到系统设置开启源音塘的拍照权限", "去设置", R.drawable.icon_fails)
                }
            }
        }
        tv_choose.setOnClickListener {
            popupWindow.dismiss()
            val pickIntent = Intent(Intent.ACTION_PICK, null)
            RxPermissions(context as Activity).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe { permission ->
                if (permission.granted) {
                    val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    if (isBGAPhoto!!) {
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                        callback.chooseFromLocal(pickIntent)
                    } else {
                        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
                        context.startActivityForResult(BGAPhotoPickerActivity.newIntent(context, tempFile, 1, null, false), REQUEST_CODE_CHOOSE_PHOTO)
                        callback.chooseFromLocal(pickIntent)
                    }
                } else if (permission.shouldShowRequestPermissionRationale) {
                } else {
                    (context as BaseActivity).setSnackBar("没有相册权限,请到系统设置开启权限", "去设置", R.drawable.icon_fails)
                }
            }
        }
        popupWindow.isTouchable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.showAtLocation(view, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
    }

    interface ChoosePicCallback {
        fun chooseFromLocal(pickIntent: Intent)

        fun tackPhoto(takeIntent: Intent)
    }
}
