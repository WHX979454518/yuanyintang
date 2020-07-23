package com.mxkj.yuanyintang.utils.ali_file_upload

import android.content.Context
import android.util.Log

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSS
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask
import com.alibaba.sdk.android.oss.model.OSSRequest
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.ProBean
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.file.MD5Utils
import com.mxkj.yuanyintang.utils.image.FileType
import com.mxkj.yuanyintang.utils.rxbus.RxBus

import java.io.File
import java.util.ArrayList
import java.util.HashMap

import okhttp3.Headers

class FileUploadUtil {
    var mOss: OSS? = null
    var mBucket: String? = null
    var dirPath: String? = null
    var endpoint: String? = null
    var mCallbackAddress: String? = null
    var mCallbackBody: String? = null
    private lateinit var task: OSSAsyncTask<*>
    var TAG = "UPLOAD_UTIL"
    var credentialProvider: OSSStsTokenCredentialProvider? = null
    var count: Int = 0//多张图片上传的时候，用于递归计数（目前后台要求多张图片只能一张一张按顺序传）
    var totalSize: Int = 0
    var finishBeans: MutableList<FileBean.DataBean> = ArrayList()
    var fileLists: MutableList<File> = ArrayList()
    var callback: UpLoadCallback? = null

    fun setFileList(list: List<File>) {
        count = 0
        finishBeans.clear()
        fileLists.clear()
        fileLists.addAll(list)
        totalSize = list.size
    }

    fun upload(context: Context, upLoadType: Int, callback: UpLoadCallback) {
        isFileExist(upLoadType, context, callback)
    }

    private fun isFileExist(upLoadType: Int, context: Context, mCallback: UpLoadCallback) {
        val params = HttpParams()
        params.put("uptype", upLoadType.toString())
        if (totalSize == 0) {
            count = 0
            return
        }
        val file = fileLists[count]
        val fileMD5 = MD5Utils.getFileMD5(file)
        if (fileMD5 != null) {
            params.put("md5", fileMD5)
            Log.e(TAG, "MD5====: $fileMD5")
        }
        NetWork.isUploaded(context, params, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val fileBean = JSON.parseObject(resultData, FileBean::class.java)
                val data = fileBean.data
                val code = fileBean.code
                Log.e(TAG, "服务器文件是否存在:---code== $code")
                if (data != null) {
                    when (code) {
                        0 -> {
                            mBucket = data.bucket
                            dirPath = data.dir
                            mCallbackAddress = data.callbackUrl
                            mCallbackBody = data.callbackBody
                            endpoint = data.endpoint
                            goUpload(upLoadType, context, mCallback)
                        }
                        1 -> {
                            finishBeans.add(data)
                            //全部上传完成
                            //                                if (finishBeans.size() >= fileLists.size()) {
                            if (finishBeans.size == totalSize) {
                                Log.e(TAG, "全部上传完===: $count")
                                mCallback.upLoadSuccess(finishBeans)
                                count = 0
                                finishBeans.clear()
                            } else if (finishBeans.size < totalSize) {
                                count++
                                Log.e(TAG, "继续上传，序列===: $count")
                                upload(context, upLoadType, mCallback)
                            }
                        }
                    }
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    fun goUpload(upLoadType: Int, context: Context, callback: UpLoadCallback) {
        NetWork.getStsToken(context, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val jObj = JSON.parseObject(resultData)
                val jsonObjs = jObj.getJSONObject("data")
                val ak = jsonObjs.getString("AccessKeyId")
                val sk = jsonObjs.getString("AccessKeySecret")
                val token = jsonObjs.getString("SecurityToken")
                credentialProvider = OSSStsTokenCredentialProvider(ak, sk, token)
                initAliOSS(context)
                var path = ""

                when (upLoadType) {
                    1 -> if (fileLists.size > count) {
                        val fileType = FileType.getFileType(fileLists[count].absolutePath)
                        path = if (fileType != null && fileType == "gif") {
                            dirPath!! + ".gif"
                        } else {
                            dirPath!! + ".jpg"
                        }
                    }
                    2 -> {
                        val fileType = FileType.getFileType(fileLists[count].absolutePath)
                        path = dirPath!! + if (fileType == null) ".mp3" else ".$fileType"
                    }
                    3 -> {
//                        val fileType = FileType.getFileType(fileLists[count].absolutePath)
                        path = dirPath!! + ".mp4"
                    }
                }
                when {
                    totalSize > count && mOss != null -> asyncPutFile(context, upLoadType, path, fileLists[count].absolutePath, callback)
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    fun asyncPutFile(context: Context, upLoadType: Int, obj: String, filePath: String, mCallback: UpLoadCallback) {
        if (obj == "") {
            return
        }

        val file = File(filePath)
        if (!file.exists()) {
            return
        }
        val put = PutObjectRequest(mBucket, obj, filePath)
        if (mCallbackAddress != null) {
            put.callbackParam = object : HashMap<String, String>() {
                init {
                    put("callbackUrl", mCallbackAddress!!)
                    put("callbackBody", mCallbackBody!!)
                }
            }
        }
        put.progressCallback = OSSProgressCallback { _, currentSize, totalSize ->
            val progress: Int = ((100 * currentSize / totalSize).toInt())
            if(mBucket.equals("yyt-video")){
                RxBus.getDefault().post(ProBean(progress,currentSize,totalSize,"1"))
            }else if(mBucket.equals("yyt-file")){
                RxBus.getDefault().post(ProBean(progress,currentSize,totalSize,"2"))
            }
            else{
                RxBus.getDefault().post(ProBean(progress,currentSize,totalSize))
            }

        }
        task = mOss!!.asyncPutObject(put, object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
            override fun onSuccess(request: PutObjectRequest, result: PutObjectResult) {
                Log.e(TAG, "onSuccess: " + result.serverCallbackReturnBody)
                val serverCallbackReturnBody = result.serverCallbackReturnBody
                val fileBean = JSON.parseObject(serverCallbackReturnBody, FileBean::class.java)
                val data = fileBean.data
                val code = fileBean.code
                if (data != null && code == 200) {
                    count++
                    finishBeans.add(data)
                    //全部上传完成
                    if (finishBeans.size == totalSize) {
                        mCallback.upLoadSuccess(finishBeans)
                        count = 0
                        finishBeans.clear()
                    } else {
                        upload(context, upLoadType, mCallback)
                    }
                } else if (code == 203) {
                    mCallback.upLoadFailure(request, null)
                    val msg = fileBean.msg
                    if (context is BaseActivity) {
                        context.setSnackBar(msg, "", R.drawable.icon_fails)
                    }
                }
            }

            override fun onFailure(request: PutObjectRequest, clientExcepion: ClientException?, serviceException: ServiceException?) {
                mCallback.upLoadFailure(request, serviceException)
                clientExcepion?.printStackTrace()
                if (serviceException != null) {
                    // 服务异常
                }
            }
        })
    }

    fun initAliOSS(context: Context) {
        val conf = ClientConfiguration()
        conf.connectionTimeout = 15 * 1000
        conf.socketTimeout = 15 * 1000
        conf.maxConcurrentRequest = 5
        conf.maxErrorRetry = 2
        if (credentialProvider != null && endpoint != null) {
            mOss = OSSClient(context, endpoint!!, credentialProvider!!, conf)
        }
    }

    interface UpLoadCallback {
        fun upLoadSuccess(finishBeans: List<FileBean.DataBean>)

        fun upLoadFailure(request: PutObjectRequest, serviceException: ServiceException?)
    }
}
