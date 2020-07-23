package com.mxkj.yuanyintang.extraui.dialog

import android.app.Dialog
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.alibaba.fastjson.JSON

import com.flyco.dialog.widget.base.BottomBaseDialog
import com.linsh.lshutils.utils.LshContextUtils
import com.linsh.lshutils.utils.LshContextUtils.getSystemService
import com.luck.picture.lib.PictureVideoPlayActivity
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.R.id.layout_dismiss
import com.mxkj.yuanyintang.R.id.tv_cancel
import com.mxkj.yuanyintang.R.layout.dialog
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.mainui.web.CommonWebview
import com.mxkj.yuanyintang.musicplayer.activity.PlayerActivity
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.base.share.ShareToYYT
import com.mxkj.yuanyintang.extraui.activity.ReportActivity
import com.mxkj.yuanyintang.extraui.adapter.ReportOperationAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.bean.ReportOperationBean
import com.mxkj.yuanyintang.extraui.bean.ShareBean
import com.mxkj.yuanyintang.mainui.emotionkeyboard.activity.MainActivity
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.upush.UpushService.Companion.goActivity
import com.mxkj.yuanyintang.utils.PlatformUtil
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.tencent.connect.share.QQShare
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMMin
import com.umeng.socialize.media.UMWeb
import com.umeng.socialize.media.UMusic
import kotlinx.android.synthetic.main.dialog_report_operation.*

import java.io.File
import java.util.ArrayList

import okhttp3.Headers

class ShareBottomDialog(context: Context, private var musicBean: MusicBean) : BottomBaseDialog<ShareBottomDialog>(context) {
    private var tv_cancel: TextView? = null
    private var layout_dismiss: RelativeLayout? = null
    private var recyclerView: RecyclerView? = null
    private var shareBeanList: MutableList<ShareBean>? = null
    private var shareBottomAdapter: ShareBottomAdapter? = null
    private val umShareListener = object : UMShareListener {
        override fun onStart(share_media: SHARE_MEDIA) {

        }

        override fun onResult(platform: SHARE_MEDIA) {
            com.umeng.socialize.utils.Log.d("plat", "platform$platform")
            dismiss()
        }

        override fun onError(platform: SHARE_MEDIA, t: Throwable?) {

            //这里是判断了分享的时候如果是纯文字，友盟分享不支持，只能自己通过判断以后用系统的分享分享出去
            var errorMsg = t.toString()
            if(errorMsg.contains("该平台不支持纯文本分享")){
                val topicTitle = "分享歌词："
                val topicContent = musicBean.getShareDataBean()!!.topicContent
                val shareUrl = "https://wap.yuanyintang.com/music/" + musicBean.getShareDataBean()!!.muisic_id
                shareQQ(getContext(), topicTitle+"\n"+topicContent!!.toString().trim()+"\n"+shareUrl+"（@源音塘音乐）")
                val cbm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cbm.setText(topicTitle+"\n"+topicContent!!.toString().trim()+"\n"+shareUrl+"（@源音塘音乐）")
            }else{
                com.umeng.socialize.utils.Log.d("throw", "throw:" + t!!.message)
            }
            dismiss()
        }

        override fun onCancel(platform: SHARE_MEDIA) {
            dismiss()
        }
    }

    override fun onCreateView(): View {
        showAnim(null)
        dismissAnim(null)
        val inflate = View.inflate(mContext, R.layout.dialog_share, null)
        tv_cancel = inflate.findViewById(R.id.tv_cancel)
        recyclerView = inflate.findViewById(R.id.recyclerView)
        layout_dismiss = inflate.findViewById(R.id.layout_dismiss)
        getShareContents(musicBean)
        return inflate
    }


    internal lateinit var wm:WindowManager;
    internal lateinit var progressbar: ProgressBar
    internal var inflate:View? = null
    internal var dialog:Dialog? = null
    private var reportOperationBeanList: MutableList<ReportOperationBean.DataBean> = ArrayList()
    private lateinit var reportOperationAdapter: ReportOperationAdapter
    internal lateinit var recyclerview:RecyclerView
    internal lateinit var tv_cancle:TextView

    private fun getShareContents(musicBean: MusicBean) {
        shareBeanList = ArrayList()
        val shareDataBean = musicBean.getShareDataBean()
        val type = shareDataBean!!.type
        val mv = shareDataBean!!.mv
//        if (type != "musician" && type != "web") {
//            shareBeanList!!.add(ShareBean(R.drawable.share_yyt, "yyt", "源音塘"))
//        }
        if (type != "musician") {
            shareBeanList!!.add(ShareBean(R.drawable.share_yyt, "yyt", "源音塘"))
        }

        shareBeanList!!.add(ShareBean(R.drawable.login_wechat, "wechat", "微信"))
        shareBeanList!!.add(ShareBean(R.drawable.wechat_circle, "pwechat", "朋友圈"))
        shareBeanList!!.add(ShareBean(R.drawable.share_qq, "qq", "QQ"))
        shareBeanList!!.add(ShareBean(R.drawable.icon_share_weibo, "sina", "新浪"))
        shareBeanList!!.add(ShareBean(R.drawable.share_qzone, "qzone", "QQ空间"))
        if (type != "img") {
            shareBeanList!!.add(ShareBean(R.drawable.share_copy, "copy", "复制链接"))
        }
        val is_self = shareDataBean!!.is_self
        if(is_self.equals("0")||is_self == "0"){
            shareBeanList!!.add(ShareBean(R.drawable.icon_mv_jubao, "report", "举报"))
        }

        recyclerView!!.layoutManager = GridLayoutManager(mContext, 5) as RecyclerView.LayoutManager?
        shareBottomAdapter = ShareBottomAdapter()
        recyclerView!!.adapter = shareBottomAdapter
        shareBottomAdapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            when (shareBeanList!![position].type) {
                "yyt" -> if (CacheUtils.getBoolean(context, Constants.User.IS_LOGIN, false)) {
                    shareToDynamic()
                } else {
                    val intent = Intent(getContext(), LoginRegMainPage::class.java)
                    getContext().startActivity(intent)
                }
                "wechat" -> share(SHARE_MEDIA.WEIXIN, 4)
                "pwechat" -> share(SHARE_MEDIA.WEIXIN_CIRCLE, 5)
                "qq" -> share(SHARE_MEDIA.QQ, 2)
                "sina" -> share(SHARE_MEDIA.SINA, 6)
                "qzone" -> share(SHARE_MEDIA.QZONE, 3)
                "copy" -> shareLink()
                "report" -> {
                        if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN, false)) {
//                            val reportOperationDialog = ReportOperationDialog(musicBean!!)
//                            reportOperationDialog.show(mContext.FragmentTransaction, "mReportOperationDialog")

                            if (null == musicBean) {
                                return@OnItemClickListener
                            }

                            var leixing :String ?= null
                            if(type == "musician"){
                                Log.e("yyyy","1");
                                leixing = "6";
                            }else{
                                leixing = "4";
                            }
                            NetWork.getFeedback(mContext, leixing.toString() + "", object : NetWork.TokenCallBack {
                                override fun doNext(resultData: String, headers: Headers?) {
                                    val reportOperationBean = JSON.parseObject(resultData, ReportOperationBean::class.java)
//                                    refreshData(reportOperationBean)
                                    reportOperationBeanList.clear()
                                    reportOperationBeanList.addAll(reportOperationBean.data!!)
                                    reportOperationAdapter.notifyDataSetChanged()
                                }

                                override fun doError(msg: String) {

                                }

                                override fun doResult() {

                                }
                            })

                            dialog = Dialog(context, R.style.ActionSheetDialogStyle)
                            //填充对话框的布局
                            inflate = LayoutInflater.from(context).inflate(R.layout.dialog_report_operation, null)
                            //初始化控件
                            recyclerview = inflate.run { this!!.findViewById<RecyclerView>(R.id.recyclerview) as RecyclerView }
                            tv_cancle = inflate.run { this!!.findViewById<TextView>(R.id.tv_cancle) as TextView }
                            recyclerview!!.layoutManager = LinearLayoutManager(mContext)
                            reportOperationAdapter = ReportOperationAdapter(reportOperationBeanList, mContext)
                            recyclerview!!.adapter = reportOperationAdapter
                            reportOperationAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                                if (null == musicBean) {
                                    return@OnItemClickListener
                                }
                                if (reportOperationBeanList[position].id == 0) {
                                } else {
                                    var intent = Intent(mContext, ReportActivity::class.java)
                                    val bundle = Bundle()
                                    if(type == "musician"){
                                        Log.e("yyyy","1");
                                        bundle.putInt(ReportActivity.REPORT_ITEM_ID, shareDataBean.muisic_id)
//                                        id = "6";
                                    }else{
//                                        id = "4";
                                        bundle.putInt(ReportActivity.REPORT_ITEM_ID, shareDataBean.muisic_id)
                                    }

                                    bundle.putInt(ReportActivity.REPORT_PID, reportOperationBeanList[position].id)
//                                    bundle.putInt(ReportActivity.REPORT_PID, 5)
                                    intent.putExtras(bundle);
                                    mContext.startActivity(intent)
//                                    goActivity(ReportActivity::class.java, bundle)
                                    dismiss()
                                }
                            }
                            //将布局设置给Dialog
                            dialog!!.setContentView(inflate)
                            //获取当前Activity所在的窗体
                            val dialogWindow1 = dialog!!.window
                            //设置Dialog从窗体底部弹出
                            dialogWindow1!!.setGravity(Gravity.BOTTOM)
                            //获得窗体的属性
                            val lp1 = dialogWindow1.attributes
                            lp1.y = 5//设置Dialog距离底部的距离
                            wm = LshContextUtils.getSystemService(Context.WINDOW_SERVICE) as WindowManager;
                            val m1 = wm;
                            val d1 = m1.getDefaultDisplay() // 获取屏幕宽、高度
                            val p1 = dialogWindow1.attributes // 获取对话框当前的参数值
                            p1.height = (d1.getHeight() * 0.6).toInt() // 高度设置为屏幕的0.6，根据实际情况调整
                            p1.width = (d1.getWidth() * 1).toInt() // 宽度设置为屏幕的0.65，根据实际情况调整
                            //    将属性设置给窗体
                            dialogWindow1.attributes = lp1
                            dialog!!.show()//显示对话框

                            tv_cancle.setOnClickListener { dialog!!.cancel() }

                        } else {
                            var intent = Intent(mContext, LoginRegMainPage::class.java)
                            mContext.startActivity(intent)
                        }
                }
            }
            dismiss()
        }
    }

    /**
     * 复制链接
     */
    private fun shareLink() {
        if (null == musicBean.getShareDataBean()) {
            return
        }
        val shareDataBean = musicBean.getShareDataBean()
        val type = shareDataBean!!.type
        when (type) {
            "img" -> {
            }
            "music" -> {
                val copy = mContext
                        .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                if (shareDataBean != null) {
                    copy.text = shareDataBean.share_link
                    Toast.makeText(mContext, "歌曲链接复制成功", Toast.LENGTH_SHORT).show()
                }
            }
            "pond" -> {
                val copypond = mContext
                        .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                if (shareDataBean != null) {
                    copypond.text = shareDataBean.shareUrl
                    Toast.makeText(mContext, "池塘链接复制成功", Toast.LENGTH_SHORT).show()
                }
            }
            "web" -> {
                val copypondWeb = mContext
                        .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                if (shareDataBean != null) {
                    copypondWeb.text = shareDataBean.shareUrl
                    Toast.makeText(mContext, "网页链接复制成功", Toast.LENGTH_SHORT).show()
                }
            }
            "album" -> {
                val copypondalbum = mContext
                        .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                if (shareDataBean != null) {
                    copypondalbum.text = shareDataBean.shareUrl
                    Toast.makeText(mContext, "歌单链接复制成功", Toast.LENGTH_SHORT).show()
                }
            }
            "musician" -> {
                val copypondMusician = mContext
                        .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                if (shareDataBean != null) {
                    copypondMusician.text = shareDataBean.shareUrl
                    Toast.makeText(mContext, "音乐人链接复制成功", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 分享到动态
     */
    private fun shareToDynamic() {
        if (null == musicBean.getShareDataBean()) {
            return
        }
        val intent = Intent(mContext, ShareToYYT::class.java)
        val shareDataBean = musicBean.getShareDataBean()
        val params = HttpParams()
        if (shareDataBean!!.muisic_id != 0) {
            params.put("from_id", shareDataBean.muisic_id.toString() + "")
        }
        if (!TextUtils.isEmpty(shareDataBean.topicContent)) {
            intent.putExtra("desc", shareDataBean.topicContent)
        } else if (!TextUtils.isEmpty(shareDataBean.nickname)) {
            intent.putExtra("desc", shareDataBean.nickname)
        }

        intent.putExtra("mv", shareDataBean.mv)
        Log.e("uuuu",""+shareDataBean.type)
        Log.e("uuuu",""+shareDataBean.title)
        Log.e("uuuu",""+shareDataBean.nickname)
        Log.e("uuuu",""+shareDataBean.webImgUrl)
        Log.e("uuuu",""+shareDataBean.mv)

        //能拿的到pond，但是不走里面，只能在外面判断
        if(shareDataBean.type.equals("pond")||shareDataBean.type == "pond"){
            params.put("type", "3")
            params.put("share_type", "1")
            shareCount(params)
            intent.putExtra("topic_id", shareDataBean.muisic_id)
            intent.putExtra("title", shareDataBean.title)
            intent.putExtra("nickName", shareDataBean.nickname)
//            intent.putExtra("imgLink", shareDataBean.webImgUrl)
            //加了一个mv的，如果转发了有mv的池塘，成功以后应该吧mv带过去
            intent.putExtra("mv", "")
            mContext.startActivity(intent)
        }else if(shareDataBean.type.equals("web")||shareDataBean.type == "web"){
            params.put("type", "6")
            params.put("share_type", "1")
            params.put("from_id", shareDataBean.activityAlias)
            shareCount(params)
            intent.putExtra("web_id", 6)

            intent.putExtra("activityAlias", shareDataBean.activityAlias)
            intent.putExtra("sinaDescription", shareDataBean.sinaDescription)
            intent.putExtra("title", shareDataBean.title)
            intent.putExtra("imgpic", shareDataBean.webImgUrl)
            intent.putExtra("shareUrl", shareDataBean.shareUrl)
            intent.putExtra("topicContent", shareDataBean.topicContent)
            intent.putExtra("nickname", shareDataBean.nickname)
            intent.putExtra("activityType", shareDataBean.activityType)

            //加了一个mv的，如果转发了有mv的池塘，成功以后应该显示mv标示
            intent.putExtra("mv", "")
            mContext.startActivity(intent)
        }
        else{
            when (shareDataBean.type) {
                "img" -> {
                    val imgFilePath = musicBean.getShareDataBean()!!.imgFilePath
                    intent.putExtra("imgPath", imgFilePath)
                    mContext.startActivity(intent)
                }
                "music" -> {
                    //设置分享音乐
                    params.put("type", "1")
                    params.put("share_type", "1")
                    shareCount(params)
                    intent.putExtra("music_id", shareDataBean.muisic_id)
                    intent.putExtra("title", shareDataBean.title)
                    intent.putExtra("nickName", shareDataBean.nickname)
                    intent.putExtra("imgLink", shareDataBean.image_link)
                    intent.putExtra("mv", shareDataBean.mv)
                    intent.putExtra("topicContent", shareDataBean.topicContent)
                    mContext.startActivity(intent)
                }
                "pond" -> {
                    params.put("type", "3")
                    params.put("share_type", "1")
                    shareCount(params)
                    intent.putExtra("topic_id", shareDataBean.muisic_id)
                    intent.putExtra("title", shareDataBean.title)
                    intent.putExtra("nickName", shareDataBean.nickname)
                    intent.putExtra("imgLink", shareDataBean.webImgUrl)
                    //加了一个mv的，如果转发了有mv的池塘，成功以后应该显示mv标示
                    intent.putExtra("mv", shareDataBean.mv)
                    mContext.startActivity(intent)
                }
                "album" -> {
                    params.put("type", "4")
                    params.put("share_type", "1")

                    shareCount(params)
                    intent.putExtra("album_id", shareDataBean.muisic_id)
                    intent.putExtra("title", shareDataBean.title)
                    intent.putExtra("nickName", shareDataBean.nickname)
                    intent.putExtra("imgLink", shareDataBean.webImgUrl)
                    mContext.startActivity(intent)
                }
                "mv" -> {
                    //设置分享mv
                    params.put("type", "4")
                    params.put("share_type", "1")
                    shareCount(params)
                    intent.putExtra("mv_id", shareDataBean.muisic_id)
                    intent.putExtra("title", shareDataBean.title)
                    intent.putExtra("nickName", shareDataBean.nickname)
                    intent.putExtra("imgLink", shareDataBean.image_link)
                    intent.putExtra("mv", shareDataBean.mv)
                    mContext.startActivity(intent)
                }
                "lyrics" -> {
                    //设置分享音乐
                    params.put("type", "1")
                    params.put("share_type", "1")
                    shareCount(params)
                    intent.putExtra("music_id", shareDataBean.muisic_id)
                    intent.putExtra("title", shareDataBean.title)
                    intent.putExtra("nickName", shareDataBean.nickname)
                    intent.putExtra("imgLink", shareDataBean.image_link)
                    intent.putExtra("mv", shareDataBean.mv)
                    intent.putExtra("topicContent", shareDataBean.topicContent)
                    mContext.startActivity(intent)
                }
            }
        }


    }

    override fun setUiBeforShow() {
        tv_cancel!!.setOnClickListener { dismiss() }
        layout_dismiss!!.setOnClickListener { dismiss() }
    }

    /**
     * 分享到三方
     */
    private fun share(i: SHARE_MEDIA, shareType: Int) {
        if (null == musicBean.getShareDataBean()) {
            return
        }
        val shareDataBean = musicBean.getShareDataBean()
        val image: UMImage
        val thumb: UMImage
        if (shareDataBean!!.webImgUrl != null) {
            image = UMImage(mContext, shareDataBean.webImgUrl!!)
            thumb = UMImage(mContext, shareDataBean.webImgUrl!!)
            thumb.compressStyle = UMImage.CompressStyle.SCALE
            image.setThumb(thumb)
        } else {
            image = UMImage(mContext, R.drawable.logo)
            thumb = UMImage(mContext, R.drawable.logo)
            thumb.compressStyle = UMImage.CompressStyle.SCALE
            image.setThumb(thumb)
        }
        val params = HttpParams()
        params.put("from_id", shareDataBean.muisic_id.toString() + "1")
        when (shareDataBean.type) {
            "img" -> {
                val imgFilePath = shareDataBean.imgFilePath
                val webImgUrl = shareDataBean.webImgUrl
                val imgByte = shareDataBean.imgByte
                var imageLyric: UMImage? = null
                if (!TextUtils.isEmpty(imgFilePath)) {
                    val file = File(imgFilePath!!)
                    imageLyric = UMImage(mContext, file)//本地文件,歌词
                    if (!file.exists()) {
                        if (!TextUtils.isEmpty(webImgUrl)) {
                            imageLyric = UMImage(mContext, webImgUrl!!)//网络图片
                        } else if (imgByte != null) {
                            imageLyric = UMImage(mContext, imgByte)//字节数组
                        }
                    }
                } else if (!TextUtils.isEmpty(webImgUrl)) {
                    imageLyric = UMImage(mContext, webImgUrl!!)//网络图片
                } else if (imgByte != null) {
                    imageLyric = UMImage(mContext, imgByte)//字节数组
                }
                if (imageLyric != null) {
                    ShareAction(mContext as BaseActivity)
                            .setPlatform(i)
                            .withMedia(imageLyric)
                            .setCallback(umShareListener)
                            .share()
                }
            }
            "music" -> {
                params.put("type", "1")
                params.put("share_type", shareType.toString() + "")
                shareCount(params)
                val music = UMusic(musicBean.getShareDataBean()!!.video_link)
                //                UMusic music = new UMusic(musicBean.getShareDataBean().getShare_link());
                music.title = musicBean.getShareDataBean()!!.title
                music.setmTargetUrl("https://wap.yuanyintang.com/music/" + musicBean.getShareDataBean()!!.muisic_id)//QQ好友微信好友可以设置跳转链接
                music.setThumb(thumb)
                music.description = musicBean.getShareDataBean()!!.nickname
                if (mContext is BaseActivity) {
                    ShareAction(mContext as BaseActivity)
                            .setPlatform(i)
                            .withMedia(music)
                            .setCallback(umShareListener)
                            .share()
                } else if (mContext is PlayerActivity) {
                    ShareAction(mContext as PlayerActivity)
                            .setPlatform(i)
                            .withMedia(music)
                            .setCallback(umShareListener)
                            .share()
                }
            }
            "pond" -> {
                params.put("type", "3")
                params.put("share_type", shareType.toString() + "")
                shareCount(params)
                shareWebUrl(image, i)
            }
            "web" -> {
                if (TextUtils.isEmpty(shareDataBean.activityType)) {
                    params.put("type", "7")
                } else {
                    params.put("type", shareDataBean.activityType)
                    params.put("from_id", shareDataBean.activityAlias)
                }
                params.put("share_type", shareType.toString() + "")
                shareWebUrl(image, i)
                if (!TextUtils.isEmpty(shareDataBean.activityAlias)) {
                    shareCount(params)
                }
            }
            "album" -> {
                params.put("type", "2")
                params.put("share_type", shareType.toString() + "")
                shareWebUrl(image, i)
                shareCount(params)
            }
            "musician" -> {
                params.put("type", "4")
                params.put("share_type", shareType.toString() + "")
                shareWebUrl(image, i)
                shareCount(params)
            }
            "mv" -> {
                params.put("type", "4")
                params.put("share_type", shareType.toString() + "")
                shareCount(params)
                val music = UMusic(musicBean.getShareDataBean()!!.mv_link)
                //                UMusic music = new UMusic(musicBean.getShareDataBean().getShare_link());
                music.title = musicBean.getShareDataBean()!!.title
                music.setmTargetUrl("https://wap.yuanyintang.com/music/" + musicBean.getShareDataBean()!!.muisic_id)//QQ好友微信好友可以设置跳转链接
                music.setThumb(thumb)
                music.description = musicBean.getShareDataBean()!!.nickname
                if (mContext is BaseActivity) {
                    ShareAction(mContext as BaseActivity)
                            .setPlatform(i)
                            .withMedia(music)
                            .setCallback(umShareListener)
                            .share()
                } else if (mContext is PlayerActivity) {
                    ShareAction(mContext as PlayerActivity)
                            .setPlatform(i)
                            .withMedia(music)
                            .setCallback(umShareListener)
                            .share()
                }
            }
            "lyrics" -> {
//                params.put(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);//分享的类型
//                params.put(QQShare.SHARE_TO_QQ_TITLE, "分享歌词");//分享标题
//                params.put(QQShare.SHARE_TO_QQ_SUMMARY,musicBean.getShareDataBean()!!.topicContent);//要分享的内容摘要
//                params.put(QQShare.SHARE_TO_QQ_TARGET_URL,"https://wap.yuanyintang.com/music/" + musicBean.getShareDataBean()!!.muisic_id);//内容地址
////                params.put(QQShare.SHARE_TO_QQ_IMAGE_URL,"https://avatar.csdn.net/6/9/D/1_k571039838k.jpg?1537239037");//分享的图片URL
//                params.put(QQShare.SHARE_TO_QQ_APP_NAME, "@源音塘音乐");//应用名称
                sharelyricsUrl(i)
                params.put("type", "1")
                params.put("share_type", shareType.toString() + "")
                shareCount(params)
            }
        }
    }

    /**
     * 分享链接的配置信息
     */
    private fun shareWebUrl(image: UMImage, i: SHARE_MEDIA) {
        var image = image
        val shareDataBean = musicBean.getShareDataBean()
        if (shareDataBean != null) {
            val topicTitle = shareDataBean.title
            val topicContent = shareDataBean.topicContent
            val sinaDescription = shareDataBean.sinaDescription
            val webImgUrl = shareDataBean.webImgUrl
            val shareUrl = shareDataBean.shareUrl
            val web = UMWeb(shareUrl)
            if (topicTitle != null) {
                web.title = topicTitle + ""
            }
            if (i == SHARE_MEDIA.SINA) {
                web.description = StringUtils.isEmpty(sinaDescription)
            } else {
//                web.description = StringUtils.isEmpty(topicContent)
                web.description = StringUtils.isEmpty(sinaDescription)
            }
            if (i == SHARE_MEDIA.QQ) {
                web.description = StringUtils.isEmpty(topicContent)
            } else {
                web.description = StringUtils.isEmpty(topicContent)
            }
            val thumbweb: UMImage
            if (!TextUtils.isEmpty(webImgUrl)) {
                image = UMImage(mContext, webImgUrl!!)
                thumbweb = UMImage(mContext, webImgUrl)
                image.setThumb(thumbweb)
            } else {
                if (shareDataBean.isShareMyIncode) {
                    image = UMImage(mContext, R.drawable.invate_share)
                    thumbweb = UMImage(mContext, R.drawable.invate_share)
                    thumbweb.compressStyle = UMImage.CompressStyle.SCALE
                    image.setThumb(thumbweb)
                } else {
                    image = UMImage(mContext, R.drawable.logo)
                    thumbweb = UMImage(mContext, R.drawable.logo)
                    thumbweb.compressStyle = UMImage.CompressStyle.SCALE
                    image.setThumb(thumbweb)
                }
            }
            web.setThumb(thumbweb)
            ShareAction(mContext as BaseActivity)
                    .setPlatform(i)
                    .withMedia(web)
                    .setCallback(umShareListener)
                    .share()
        }
    }
    /**
     * 分享歌词的配置信息
     */
    private fun sharelyricsUrl(i: SHARE_MEDIA) {
//        var image = image
        val shareDataBean = musicBean.getShareDataBean()
        if (shareDataBean != null) {
            val topicTitle = "分享歌词"
            val topicContent = musicBean.getShareDataBean()!!.topicContent
            val sinaDescription = musicBean.getShareDataBean()!!.topicContent
            val webImgUrl = shareDataBean.webImgUrl
            val shareUrl = "https://wap.yuanyintang.com/music/" + musicBean.getShareDataBean()!!.muisic_id
            val web = UMWeb(shareUrl)
            if (topicTitle != null) {
                web.title = "分享歌词"
            }
            if (i == SHARE_MEDIA.SINA) {
                web.description = StringUtils.isEmpty(topicContent)
            } else {
                web.description = StringUtils.isEmpty(topicContent)
            }

            ShareAction(mContext as BaseActivity)
                    .setPlatform(i)
                    .withText(StringUtils.isEmpty(topicContent)+shareUrl)
//                    .withMedia(web)
                    .setCallback(umShareListener)
                    .share()
        }
    }

    private inner class ShareBottomAdapter : BaseQuickAdapter<ShareBean, BaseViewHolder>(R.layout.item_share, shareBeanList) {

        override fun convert(helper: BaseViewHolder, item: ShareBean, position: Int) {
            helper.setImageResource(R.id.iv_img, item.drawable)
            helper.setText(R.id.tv_txt, item.txt)
        }
    }

    /**
     * 分享次数统计
     */
    private fun shareCount(params: HttpParams) {
        NetWork.shareCount(context, params, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                if (context is CommonWebview) {
                    (context as CommonWebview).reSetVoteNum()
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    /**
     * @param mContext 上下文
     * @param content 要分享的文本
     */
    fun shareQQ(mContext: Context,content: String) {
        if (PlatformUtil.isQQClientAvailable(mContext)) {
            val intent = Intent("android.intent.action.SEND")
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享")
            intent.putExtra(Intent.EXTRA_TEXT, content)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.component = ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity")
            mContext.startActivity(intent)
        } else {
            Toast.makeText(mContext, "您需要安装QQ客户端", Toast.LENGTH_LONG).show()
        }
    }
}
