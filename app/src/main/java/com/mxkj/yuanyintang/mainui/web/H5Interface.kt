package com.mxkj.yuanyintang.mainui.web

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.base.activity.GiiGiftSuccessActivity
import com.mxkj.yuanyintang.base.bean.MusicInfo.DataBean.MvInfoBean.link
import com.mxkj.yuanyintang.base.share.ShareUtil
import com.mxkj.yuanyintang.extraui.activity.PictureDetailsActivity
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog
import com.mxkj.yuanyintang.extraui.gift.BotomGiftListDialog
import com.mxkj.yuanyintang.extraui.gift.CheckBean
import com.mxkj.yuanyintang.extraui.gift.ConfirmGiveGiftDialog
import com.mxkj.yuanyintang.extraui.gift.FirstChargeDialog
import com.mxkj.yuanyintang.mainui.comment.CommentActivity
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.utils.file.SdUtil
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.net.HttpUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.ImageUtils
import com.mxkj.yuanyintang.utils.image.imageloader.config.SingleConfig
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.tbruyelle.rxpermissions2.RxPermissions

import org.json.JSONException

import java.util.ArrayList

import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.Companion.MUSICIAN_ID
import com.mxkj.yuanyintang.mainui.home.bean.MemberGiftListBean
import com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity
import com.mxkj.yuanyintang.mainui.myself.activity.EditSongActivity
import com.mxkj.yuanyintang.mainui.myself.doughnut.ChargeDoughnutActivity
import com.mxkj.yuanyintang.musicplayer.activity.PlayerActivity
import com.mxkj.yuanyintang.musicplayer.service.MediaService.ACTION_PAUSE
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.pay.PayUtils
import com.mxkj.yuanyintang.upush.UpushService.Companion.goActivity
import com.umeng.analytics.MobclickAgent
import com.umeng.socialize.utils.Log.toast
import kotlinx.android.synthetic.main.player_activity.*
import okhttp3.Headers

class H5Interface : BaseActivity(){
    internal var context: Context? = null
    private val tokenUrl = ""
    private var funName = ""

    fun funAndroid(context: Context, paraJs: String) {
        this.context = context
        val param = paraJs.replace("\\", "")
        var params = "{}"
        val jsobj = getJsobj(param)
        if (jsobj != null) {
            funName = jsobj.getString("funcName")
            val jObj = jsobj.getJSONObject("params")
            if (TextUtils.isEmpty(funName)) {
                return
            }
            if (jObj != null) {
                params = jObj.toJSONString()
            }
            when (funName) {
                "goUploadMusic"//去发布音乐
                -> goUploadMusic(context, params)
                "setTitleWithGoto" -> {
                }
                "showShareWindow"//分享弹框
                -> showShareWindow(context, params)
                "gotoUser"//用户详情
                -> gotoUser(context, params)
                "reloadPage"//
                -> (context as CommonWebview).webView.reload()
                "gotoMusic"//音乐详情
                -> gotoMusic(context, params)
                "playMusic"//播放音乐
                -> playMusic(context, params)

                "hide"//隐藏键盘
                -> hidddenKeyboard(context, params)


                "giftGiving"//送礼物
                -> gotogift(context, params)
                "replyComent"//回复
                -> gotoreply(context, params)




                "previewer"//图片预览
                -> showBigImg(context, params)
                "saveSharePic"//保存网页图片
                -> saveSharePic(context, params)
                "gotoLogin"//登录
                -> gotoLogin(context,params)
                "getTokens"//
                -> getTokens(context, params)
                "setTokens" -> setTokens(context, params)
                "toast" -> toast(context, params)
                "share" -> share(context, params)
                "go" -> go(context, params)
                "setTitle"//只需要换title
                -> setTitle(context, params)
                "clearCache"
                -> (context as CommonWebview).webView.clearCache(true)
                "goBackPage"//返回上级页面（h5内部返回）
                -> if ((context as CommonWebview).webView.canGoBack()) {
                    context.webView.goBack()
                    context.showDialog = false
                }
                "goBack"//返回上级页面（打开新的webview的情况下调用，销毁新webview）
                -> (context as CommonWebview).finish()
                "closeLotteryDialog" -> context.sendBroadcast(Intent("hideNewYearDialog"))
                "playOrpause" -> playOrpause(context, params)
                "setGoBackStatus" -> setGoBackStatus(context, params)
            }//                    ((CommonWebview) context).setWebTitle(title, null);
        }
    }

    private fun setGoBackStatus(context: Context, params: String) {
        val jsobj = getJsobj(params)
        (context as CommonWebview).showDialog = jsobj!!.getBoolean("show")!!
        context.dialogTitle = jsobj.getString("title")
        context.dialogContent = jsobj.getString("content")
        context.sureBtnText = jsobj.getString("sureBtnText")
        context.cancelBtnText = jsobj.getString("cancelBtnText")
    }

    private fun playOrpause(context: Context, params: String) {
        val jsobj = getJsobj(params)
        if (jsobj != null) {
            val status = jsobj.getString("status")
            if (status != null) {
                when (status) {
                    "play" -> if (MediaService.mediaPlayer != null && !MediaService.mediaPlayer.isPlaying) {
                        context.sendBroadcast(Intent(ACTION_PAUSE))
                    }
                    "pause" -> if (MediaService.mediaPlayer != null && MediaService.mediaPlayer.isPlaying) {
                        context.sendBroadcast(Intent(ACTION_PAUSE))
                        CacheUtils.setBoolean(context, "continuePlay", true)
                    }
                }
            }
        }
    }

    /**
     * 开一个新的webview打开目标页面
     */
    private fun go(context: Context, params: String) {
        val jsobj = getJsobj(params)
        val url = jsobj!!.getString("url")
        val title = jsobj.getString("title")
        val activity = jsobj.getString("activity")
        val headerType = jsobj.getString("headerType")
        val intent = Intent(context, CommonWebview::class.java)
        intent.putExtra("url", url)
        intent.putExtra("title", title)
        intent.putExtra("headerType", headerType)
        if (activity != null) {
            intent.putExtra("activity", activity)
        }
        context.startActivity(intent)
    }

    /**
     * 设置标题以及右边按钮的点击事件类型（前端要求）
     */
    private fun setTitle(context: Context, params: String) {
        val jsobj = getJsobj(params)
        val title = jsobj!!.getString("title")
        val headerType = jsobj.getString("headerType")
        (context as CommonWebview).setWebTitle(title, headerType)
    }

    /**
     * 前端调用，传相关参数过来配置当前web页面的分享信息,点击app右上角分享出去)
     */
    private fun share(context: Context, params: String) {
        (context as CommonWebview).setShareInfo(initShareBean(params))
    }



    /**
     * 回复
     */
    var pid = ""
    fun gotoreply(context: Context, params: String) {
        val jsobj = getJsobj(params)
        pid = jsobj!!.getString("pid")
        (context as CommonWebview).initEmotionMainFragment(Integer.parseInt(pid))

    }

    /**
     * 送礼物
     */
    fun gotogift(context: Context, params: String) {
        val jsobj = getJsobj(params)
        music_id = jsobj!!.getString("music_id")
        (context as CommonWebview).showBotomGiftDialog(music_id)
    }
    /**
     * 送礼物
     */
    var music_id = ""





    //分享音乐、网页、图片，具体分享啥由前端自己传参
    private fun initShareBean(params: String): MusicBean? {
        Log.e("TAG", params)
        val jsobj = getJsobj(params)
        var musicBean: MusicBean? = null
        if (jsobj != null && !TextUtils.isEmpty(jsobj.getString("link"))) {
            val activityAlias = jsobj.getString("alias")
            val sinaDescription = jsobj.getString("sinaDescription")
            val title = jsobj.getString("title")
            val imgpic = jsobj.getString("imgpic")
            val inputText = jsobj.getString("inputText")
            val link = jsobj.getString("link")
            val desc = jsobj.getString("desc")
            val imgUrl = jsobj.getString("imgUrl")
            val sub_desc = jsobj.getString("sub_desc")
            val activityType = jsobj.getString("type")
//            val mediaType = jsobj.getString("mediaType")//web,music,img



            //            int songId = jsobj.getInteger("songId");
            musicBean = MusicBean()
            val shareDataBean = MusicBean.ShareDataBean()
            shareDataBean.type = "web"
            shareDataBean.activityAlias = StringUtils.isEmpty(activityAlias)
            shareDataBean.sinaDescription = StringUtils.isEmpty(sinaDescription)
            shareDataBean.title = StringUtils.isEmpty(title)
            shareDataBean.webImgUrl = StringUtils.isEmpty(imgpic)
            shareDataBean.shareUrl = StringUtils.isEmpty(link)
            shareDataBean.topicContent = StringUtils.isEmpty(sub_desc)
            shareDataBean.nickname = StringUtils.isEmpty(desc)
            shareDataBean.activityType = StringUtils.isEmpty(activityType)
            musicBean.setShareDataBean(shareDataBean)
        }
        return musicBean
    }

    /**
     * 显示底部分享弹框，分享出去的东西也由前端配置，如果params里面前端没传东西过来，默认分享share方法配置的信息
     */
    private fun showShareWindow(context: Context, params: String) {
        //      前端调用这个方法之前需要先调用share()方法配置分享参数
        val musicBean = initShareBean(params)
        if (musicBean != null) {
            val shareBottomDialog = ShareBottomDialog(context, musicBean)
            shareBottomDialog.show()
        } else {
            if ((context as CommonWebview).musicBean != null) {
                val shareBottomDialog = ShareBottomDialog(context, context.musicBean!!)
                shareBottomDialog.show()
            }
        }
    }

    /**
     * 谈提示
     */
    fun toast(context: Context, params: String) {
        val jsobj = getJsobj(params)
        val text = jsobj!!.getString("text")
        val type = jsobj.getString("type")
        if (text != null) {
            when (type) {
                "success" -> (context as CommonWebview).setSnackBar(text, "", R.drawable.icon_success)
                "fail" -> (context as CommonWebview).setSnackBar(text, "", R.drawable.icon_fails)
                "tip" -> (context as CommonWebview).setSnackBar(text, "", R.drawable.icon_tips_bad)
            }
        }
    }

    companion object {
        private var musicBean: MusicBean? = null
        private var shareDataBean: MusicBean.ShareDataBean? = null

        /**
         * 登录
         */
        private fun gotoLogin(context: Context,params: String) {
            if (CacheUtils.getBoolean(context, Constants.User.IS_LOGIN)) {
                (context as CommonWebview).webView.reload()
                CacheUtils.setBoolean(context, "needReload", false)
            } else {
                val bundle = Bundle()
                bundle.putString("params", params)
                goActivity(LoginRegMainPage::class.java, bundle,context)
//                context.startActivity(Intent(context, LoginRegMainPage::class.java))
//                CacheUtils.setBoolean(context, "needReload", true)

            }
        }

        /**
         * 音乐详情
         */

        private fun gotoMusic(context: Context, params: String) {
//            val jsobj = getJsobj(params)
//            val songID = jsobj!!["songID"]//前端有时候返个int有时候返个string。。。
//            val intent_detial = Intent(context, MusicDetailsActivity::class.java)
//            intent_detial.putExtra(MUSIC_ID, songID.toString())
//            context.startActivity(intent_detial)
        }

        /**
         * 图片预览
         */
        private fun showBigImg(context: Context, params: String) {
            val jsobj = getJsobj(params)
            val s = jsobj!!.toJSONString()
            val index = jsobj.getInteger("index")!!
            val urlList = ArrayList<String>()
            try {
                val jsonObject = org.json.JSONObject(s)
                val list = jsonObject.optJSONArray("list")
                for (i in 0 until list.length()) {
                    val o = list.get(i) as org.json.JSONObject
                    val src = o.optString("src")
                    urlList.add(src)
                }
                val intent = Intent(context, PictureDetailsActivity::class.java)
                if (urlList.size > index) {
                    intent.putExtra("url", urlList[index])
                    context.startActivity(intent)
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        /**
         * 保存、分享图片（通过前端传来的url或者base64）
         */
        private fun saveSharePic(context: Context, params: String) {
            val jsobj = getJsobj(params)
            val imgUrl = jsobj!!.getString("imgUrl")
            val base64 = jsobj.getString("base64")
            val isSave = jsobj.getInteger("isSave")!!
            val shareInfoObj = jsobj.getJSONObject("shareInfo")
            RxPermissions(context as Activity).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe { permission ->
                        if (permission.granted) {
                            musicBean = null
                            if (shareInfoObj != null) {//要分享图片出去
                                val title = shareInfoObj.getString("title")
                                val desc = shareInfoObj.getString("desc")
                                musicBean = MusicBean()
                                shareDataBean = MusicBean.ShareDataBean()
                                shareDataBean!!.type = "img"
                                shareDataBean!!.activityAlias = "activity"
                                if (!TextUtils.isEmpty(imgUrl)) {
                                    shareDataBean!!.webImgUrl = imgUrl
                                } else if (!TextUtils.isEmpty(base64)) {
                                    val b = Base64.decode(base64.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1], Base64.DEFAULT)
                                    shareDataBean!!.imgByte = b
                                }
                                shareDataBean!!.nickname = desc
                                shareDataBean!!.topicContent = desc
                                shareDataBean!!.title = title
                            }
                            if (!TextUtils.isEmpty(imgUrl)) {
                                ImageLoader.with(context)
                                        .url(imgUrl)
                                        .asBitmap(object : SingleConfig.BitmapListener {
                                            override fun onSuccess(bitmap: Bitmap) {
                                                val file = ImageUtils.saveImageToGallery(context, bitmap)
                                                if (file != null) {
                                                    shareDataBean!!.imgFilePath = file.absolutePath
                                                    musicBean!!.setShareDataBean(shareDataBean!!)
                                                }
                                                if (isSave == 1) {
                                                    ShareUtil.showSavePicDialog(context, musicBean!!)
                                                } else if (shareInfoObj != null) {
                                                    val shareBottomDialog = ShareBottomDialog(context, musicBean!!)
                                                    shareBottomDialog.show()
                                                }
                                            }

                                            override fun onFail() {
                                                //                                                    ((CommonWebview) context).setSnackBar("保存失败，请重试！", "", R.drawable.icon_success);
                                            }
                                        })
                            } else if (!TextUtils.isEmpty(base64)) {
                                val b = Base64.decode(base64.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1], Base64.DEFAULT)
                                val file = SdUtil.writeToSd("yytPicture", System.currentTimeMillis().toString() + ".jpg", b)
                                if (file != null) {
                                    shareDataBean!!.imgFilePath = file.absolutePath
                                    musicBean!!.setShareDataBean(shareDataBean!!)
                                }
                                if (isSave == 1) {
                                    ShareUtil.showSavePicDialog(context, musicBean!!)
                                } else if (shareInfoObj != null) {
                                    val shareBottomDialog = ShareBottomDialog(context, musicBean!!)
                                    shareBottomDialog.show()
                                }
                            }
                        }
                    }
            //        }

        }

        /**
         * 跳转用户详情页
         */
        fun gotoUser(context: Context, params: String) {
            val jsobj = getJsobj(params)
            val userID = jsobj!!.getInteger("userID")!!
            val intent = Intent(context, MusicIanDetailsActivity::class.java)
            intent.putExtra(MUSICIAN_ID, userID.toString() + "")
//            context.startActivity(intent)
        }

        /**
         * 播放音乐
         */
        fun playMusic(context: Context, params: String) {
            val jsobj = getJsobj(params)
            val songID = jsobj!!.getString("songID")
            if (!TextUtils.isEmpty(songID)) {
                PlayCtrlUtil.play(context, Integer.parseInt(songID), 0)
            }
            //跳转到播放器
            val intent = Intent(context, PlayerActivity::class.java)
//            context.startActivity(intent)
        }

        /**
         * 隐藏键盘
         */
        fun hidddenKeyboard(context: Context, params: String) {
            val jsobj = getJsobj(params)
            val hide = jsobj!!.getInteger("hide")
            if(hide==1){

            }
        }

        /**
         * 请求token  前端调用，app自己请求，替换已经缓存的token
         */
        fun getTokens(context: Context, params: String) {
            HttpUtils.getToken(context, null)
        }

        /**
         * 更改token ，前端请求token，传给app保存
         */
        fun setTokens(context: Context, params: String) {
            val jsobj = getJsobj(params)
            val token = jsobj!!.getString("token")
            CacheUtils.setString(context, "token", token)
            (context as CommonWebview).webView.reload()
        }

        /**
         * 去发布音乐
         */
        fun goUploadMusic(context: Context, params: String) {
            val uid = CacheUtils.getInt(context, "uid", 0)
//           跳转发布音乐
        }

        fun getJsobj(params: String): JSONObject? {
            return JSON.parseObject(params)
        }
    }
}
