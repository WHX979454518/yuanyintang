package com.mxkj.yuanyintang.mainui.newapp.pond

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jakewharton.rxbinding2.view.RxView
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.R.id.*
import com.mxkj.yuanyintang.extraui.AgreeAnimationUtil
import com.mxkj.yuanyintang.extraui.activity.PicturePagerDetailsActivity
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.bean.PictureDataBean
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog
import com.mxkj.yuanyintang.mainui.dynamic.bean.DynamicBean
import com.mxkj.yuanyintang.mainui.newapp.ExpandTextView
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.mainui.emotionkeyboard.utils.ScreenUtils
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.home.bean.SongListDetails
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.newapp.weidget.NineGridView
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew
import com.mxkj.yuanyintang.mainui.web.CommonWebview
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.musicplayer.service.MediaService.ACTION_PAUSE
import com.mxkj.yuanyintang.net.ApiAddress
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.upush.UpushService.Companion.goActivity
import com.mxkj.yuanyintang.utils.RichTextutils.ImageTextUtil
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.photopicker.widget.NoScrollRecyclerView
import com.mxkj.yuanyintang.utils.threadpool.ThreadPoolManager
import com.mxkj.yuanyintang.video.MvVideoActivitykt
import com.umeng.analytics.MobclickAgent
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dynamic_common_top.view.*
import kotlinx.android.synthetic.main.right_switcher_list.*
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class PondAdapter(data: List<PondInfo.DataBean.DataListBean>) : BaseMultiItemQuickAdapter<PondInfo.DataBean.DataListBean, BaseViewHolder>(data) {
    init {
        addItemType(0, R.layout.pond_new_item)

    }

    var isFollowPage = false
    override fun convert(helper: BaseViewHolder, dataBean: PondInfo.DataBean.DataListBean) {
        baseEvent(helper, dataBean)
        setItemInfo(helper, dataBean)
        follow(helper, dataBean)
        agree(helper, dataBean)
    }

    private fun baseEvent(helper: BaseViewHolder, dataBean: PondInfo.DataBean.DataListBean) {
        helper.getView<View>(R.id.exTvContent).setOnClickListener {
            val intent = Intent(mContext, PondDetialActivityNew::class.java)
            val bundle = Bundle()
            bundle.putInt(PondDetialActivityNew.PID, dataBean.id)
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        }

        RxView.clicks(helper.getView(R.id.layout_click)).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            val intent = Intent(mContext, PondDetialActivityNew::class.java)
            val bundle = Bundle()
            bundle.putInt(PondDetialActivityNew.PID, dataBean.id)
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        }
        helper.setText(R.id.tvNickname, dataBean.nickname)
        ImageLoader.with(mContext).getSize(200, 200).setUrl(dataBean.head_info).into(helper.getView(R.id.civ_headimg))
        helper.getView<ImageView>(R.id.iv_isvip).visibility = if (dataBean.is_music == 3) {
            View.VISIBLE
        } else {
            View.GONE
        }
        helper.setText(R.id.tvTime, dataBean.create_time?.toString())
        helper.getView<CheckBox>(R.id.ck_follow).isChecked = dataBean.is_relation == 1
        val expandTextView = helper.getView<ExpandTextView>(R.id.exTvContent)
        // 设置TextView可展示的宽度 ( 父控件宽度 - 左右margin - 左右padding）
        val whidth = ScreenUtils.getScreenWidth(mContext) - CommonUtils.dip2px(mContext, 20f)
        expandTextView.initWidth(whidth)
        expandTextView.setExpandText("收起")
        expandTextView.setCloseText("展开")
        expandTextView.maxLines = 3

        if (!TextUtils.isEmpty(dataBean.getContent())) {
            if (dataBean.getContent().contains("<img") || dataBean.getContent().contains("<br>")) {
                ImageTextUtil.setImageText(expandTextView, dataBean.getContent())
            } else {
                expandTextView.setCloseText(dataBean.getContent())
            }
        }
//        expandTextView.setCloseText(dataBean.content)
//        图片九宫格
        aboutImage(helper, dataBean)
        if (!TextUtils.isEmpty(dataBean.place_desc)) {
            helper.setText(R.id.tvLocation, StringUtils.isEmpty(dataBean.place_desc))
        } else {
            helper.getView<TextView>(R.id.tvLocation).visibility = GONE
        }
        if (!TextUtils.isEmpty(dataBean.distance)) {
            helper.setText(R.id.tvFaraway, StringUtils.isEmpty(dataBean.distance?.toString()))
        } else {
            helper.getView<TextView>(R.id.tvFaraway).visibility = GONE
            helper.getView<ImageView>(R.id.iv_far).visibility = GONE
        }
        helper.setText(R.id.tvShareNum, StringUtils.isEmpty(dataBean.share_counts_text))
        helper.setText(R.id.tvCommentNum, StringUtils.isEmpty(dataBean.thcount_text))
        helper.setText(R.id.tvAgreeNum, StringUtils.isEmpty(dataBean.agrees_text))
        var shaarenumber = helper.getView<TextView>(R.id.tvShareNum)
        var commentsnumber = helper.getView<TextView>(R.id.tvCommentNum)
        var agreenumber = helper.getView<TextView>(R.id.tvAgreeNum)
        if(shaarenumber.text.equals("0") || shaarenumber.text == "0"){
            shaarenumber.setText("分享")
        }
        if(commentsnumber.text.equals("0") || commentsnumber.text == "0"){
            commentsnumber.setText("评论")
        }
        if(agreenumber.text.equals("0") || agreenumber.text == "0"){
            agreenumber.setText("点赞")
        }


        /**
         * 点击头像和昵称
         * */
        helper.setOnClickListener(R.id.tvNickname, {
            val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
            val bundle = Bundle()
            bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, dataBean.uid.toString() + "")
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        })
        helper.setOnClickListener(R.id.civ_headimg, {
            val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
            val bundle = Bundle()
            bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, dataBean.uid.toString() + "")
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        })
        helper.setOnClickListener(R.id.llShare, {
            MobclickAgent.onEvent(mContext,"pond_share");

            if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN, false)) {
                Handler().postDelayed({
                    helper.setText(R.id.tvShareNum, (dataBean.share_counts + 1).toString())
                    dataBean.share_counts++
                }, 4000)

                val musicBean = MusicBean()
                val shareDataBean = MusicBean.ShareDataBean()
                shareDataBean.type = "pond"

                shareDataBean.nickname = dataBean.nickname
                shareDataBean.topicContent = dataBean.content
                shareDataBean.title = dataBean.title + ""
                //新加的有mv的显示mv
                shareDataBean.mv = dataBean.item_info.mv + ""
                Log.e("yyyyy",""+dataBean.item_info.mv)
                shareDataBean.is_self = dataBean.is_self.toString()
                Log.e("yyyyy",""+dataBean.is_self)


                if (dataBean.item_type == 1) {
                    shareDataBean.muisic_id = dataBean.id
                }else{
                    shareDataBean.muisic_id = dataBean.id
                }
                if (dataBean.imglist_info != null && dataBean.imglist_info.size > 0) {
                    shareDataBean.webImgUrl = dataBean.imglist_info[0].link
                    shareDataBean.image_link = dataBean.imglist_info[0].link
                }
//                val shareUrl = ApiAddress.H5_BASE_URL + "topic/detail?id=" + dataBean.id
                val shareUrl = dataBean.share_url
                shareDataBean.shareUrl = shareUrl
                musicBean.setShareDataBean(shareDataBean)
                val shareBottomDialog = ShareBottomDialog(mContext, musicBean)
                shareBottomDialog.show()
            } else {
                mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
            }
        })
    }

    private fun agree(helper: BaseViewHolder, dataBean: PondInfo.DataBean.DataListBean) {
        val img_agree = helper.getView<ImageView>(R.id.img_agree)
        val tv_agree_num = helper.getView<TextView>(R.id.tvAgreeNum)
        if (dataBean.is_agree == 1) {
            img_agree.setImageResource(R.drawable.icon_agreed)
            tv_agree_num.text = (dataBean.agrees).toString() + ""
        } else {
            img_agree.setImageResource(R.drawable.icon_disagree)
        }
        helper.setOnClickListener(R.id.llAgree) {
            MobclickAgent.onEvent(mContext,"pond_agree");

            if (dataBean.is_agree == 0) {
                AgreeAnimationUtil.setAnim(mContext, helper.getView<View>(R.id.ani_agree) as ImageView, img_agree, AgreeAnimationUtil.COMMENT_AGREE)
            }
            val params = HttpParams()
            params.put("type", 5.toString())
            params.put("item_id", dataBean.id.toString() + "")
            NetWork.agree(mContext, params, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    try {
                        val jsonObject = JSON.parseObject(resultData)
                        val code = jsonObject.getInteger("code")!!
                        if (code == 200) {
                            if (dataBean.is_agree == 1) {
                                img_agree.setImageResource(R.drawable.icon_disagree)
                                tv_agree_num.text = (dataBean.agrees - 1).toString() + ""
                                dataBean.is_agree = 0
                                dataBean.agrees = dataBean.agrees - 1
                                tv_agree_num.setTextColor(Color.parseColor("#616366"))
                            } else {
                                img_agree.setImageResource(R.drawable.icon_agreed)
                                tv_agree_num.text = (dataBean.agrees + 1).toString() + ""
                                dataBean.is_agree = 1
                                dataBean.agrees = 1 + dataBean.agrees
                                tv_agree_num.setTextColor(Color.parseColor("#ff6699"))
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun doError(msg: String) {

                }

                override fun doResult() {

                }
            })
        }
    }

    private fun follow(helper: BaseViewHolder, detialBean: PondInfo.DataBean.DataListBean) {
        val checkBox = helper.getView<CheckBox>(R.id.ck_follow)
        if (isFollowPage) {
            checkBox.visibility = GONE
        } else {
            checkBox.visibility = VISIBLE
        }
        checkBox.isChecked = detialBean.is_relation == 1
        checkBox.text = if (detialBean.is_relation == 1) {
            "已关注"
        } else {
            "+关注"
        }

        checkBox.setOnTouchListener { view, motionEvent ->
            if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN, false)) {
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN)) {
                        checkBox.isChecked = !checkBox.isChecked
                        if (checkBox.isChecked) {
                            checkBox.text = "已关注"
                        } else {
                            checkBox.text = "+关注"
                        }
                        val params = HttpParams()
                        params.put("id", detialBean.uid.toString() + "")
                        NetWork.follow(mContext, params, object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jObj = JSON.parseObject(resultData)
                                val code = jObj.getInteger("code")
                                if (code == 200) {
                                    if (detialBean.is_relation == 0 && detialBean.is_relation == 0) {
                                        checkBox.text = "已关注"
                                        detialBean.is_relation = 1
                                    } else if (detialBean.is_relation == 1) {
                                        checkBox.text = "+关注"
                                        detialBean.is_relation = 0
                                    }
                                }
                            }

                            override fun doError(msg: String) {
//                        checkBox.isChecked = !checkBox.isChecked
                                notifyItemChanged(helper.adapterPosition)
                            }

                            override fun doResult() {

                            }
                        })
                    } else {
                        mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
                    }
                }
            }else{
                val intent = Intent(mContext, LoginRegMainPage::class.java)
                mContext.startActivity(intent)
            }


            true
        }
    }

    private fun setItemInfo(helper: BaseViewHolder, dataBean: PondInfo.DataBean.DataListBean) {
        if (dataBean.item_info == null) return
        Log.e("hhhh",""+dataBean.item_type)
        if (dataBean.item_type == 1 || dataBean.item_type == 2 || dataBean.item_type == 3 || dataBean.item_type == 6 || dataBean.item_type == 4) {
            helper.getView<View>(R.id.card_item).visibility = VISIBLE
            if(null == dataBean.item_info.mv || dataBean.item_info.mv.equals("")){
                helper.getView<ImageView>(R.id.forwarding_mv).visibility = GONE
            }else{
                helper.getView<ImageView>(R.id.forwarding_mv).visibility = VISIBLE
            }
        } else {
            helper.getView<View>(R.id.card_item).visibility = GONE
        }
        ImageLoader.with(mContext).getSize(200,200).setUrl(dataBean.item_info.imgpic_info).into(helper.getView(R.id.img_item))
        ImageLoader.with(mContext).getSize(200,200).setUrl(dataBean.item_info.imgpic_info).into(helper.getView(R.id.img_item2))


        helper.setText(R.id.tv_itemTitle, dataBean.item_info.title)
        helper.setText(R.id.tv_item_nick, dataBean.item_info.nickname)
        helper.setText(R.id.tv_itemTitle2, dataBean.item_info.title)
        helper.setText(R.id.tv_item_nick2, dataBean.item_info.nickname)
        if(dataBean.item_info.status == 0){
            helper.getView<LinearLayout>(R.id.show_ll).visibility = GONE
            helper.getView<LinearLayout>(R.id.hide_ll).visibility = VISIBLE
            helper.getView<ImageView>(R.id.img_item).visibility = GONE
            helper.getView<ImageView>(R.id.img_item2).visibility = VISIBLE
        }else{
            helper.getView<LinearLayout>(R.id.show_ll).visibility = VISIBLE
            helper.getView<LinearLayout>(R.id.hide_ll).visibility = GONE
            helper.getView<ImageView>(R.id.img_item).visibility = VISIBLE
            helper.getView<ImageView>(R.id.img_item2).visibility = GONE
        }


        helper.setText(R.id.tv_item_type, when (dataBean.item_type) {
            1 -> "单曲"
            2 -> "歌单"
            3 -> "池塘"
            4 -> "MV"
            6 -> "活动"
            else -> {
                ""
            }
        })

        when (dataBean.item_type) {
            1 -> {
                helper.setText(R.id.tv_item_type2,"单曲")
                setMusicItem(helper, dataBean)
            }
            2 -> {
                helper.setText(R.id.tv_item_type2,"歌单")
                setAlbumItem(helper, dataBean)
            }
            3 -> setPondItem(helper, dataBean)
            4 -> {
                helper.setText(R.id.tv_item_type2,"MV")
                setMvItem(helper, dataBean)
            }
            6 -> {
                helper.setText(R.id.tv_item_type,"活动")
                helper.getView<LinearLayout>(R.id.show_ll).visibility = VISIBLE
                helper.getView<LinearLayout>(R.id.hide_ll).visibility = GONE
                helper.getView<ImageView>(R.id.img_item).visibility = VISIBLE
                helper.getView<ImageView>(R.id.img_item2).visibility = GONE
//                helper.setText(R.id.tv_item_type2,"活动")
//                helper.setTextColor(R.id.tv_itemTitle2,Color.parseColor("#2b2b2b"))
//                helper.setTextColor(R.id.tv_item_nick2,Color.parseColor("#9da2a6"))
//                helper.setVisible(R.id.img_playmusic2,false)
                setActivityItem(helper, dataBean)
            }

        }

    }

    private fun setPondItem(helper: BaseViewHolder, dataBean: PondInfo.DataBean.DataListBean) {
        helper.getView<ImageView>(R.id.img_playmusic).visibility = GONE
        if(null == dataBean.item_info.mv || dataBean.item_info.mv.equals("")){
            helper.getView<ImageView>(R.id.forwarding_mv).visibility = GONE
        }else{
            helper.getView<ImageView>(R.id.forwarding_mv).visibility = VISIBLE
        }

        helper.setOnClickListener(R.id.ll_item) {
                val intent = Intent(mContext, PondDetialActivityNew::class.java)
                intent.putExtra("pid", dataBean.item_id)
                mContext.startActivity(intent)

        }

    }

    private fun setAlbumItem(helper: BaseViewHolder, dataBean: PondInfo.DataBean.DataListBean) {

        if((dataBean.item_info.status == 1 && dataBean.item_info.is_private == 1) || dataBean.item_info.status == 0) {
            helper.getView<LinearLayout>(R.id.show_ll).visibility = GONE
            helper.getView<LinearLayout>(R.id.hide_ll).visibility = VISIBLE
            helper.getView<ImageView>(R.id.img_playmusic2).visibility = GONE
        }else{
            helper.getView<LinearLayout>(R.id.show_ll).visibility = VISIBLE
            helper.getView<LinearLayout>(R.id.hide_ll).visibility = GONE
        }

        helper.getView<ImageView>(R.id.img_playmusic).visibility = GONE

        if(null == dataBean.item_info.mv || dataBean.item_info.mv.equals("")){
            helper.getView<ImageView>(R.id.forwarding_mv).visibility = GONE
        }else{
            helper.getView<ImageView>(R.id.forwarding_mv).visibility = VISIBLE
        }

        helper.setOnClickListener(R.id.ll_item) {
            if((dataBean.item_info.status == 1 && dataBean.item_info.is_private == 1) || dataBean.item_info.status == 0){
                NetWork.getSongSheetDetails("songDetails_${dataBean.item_id}", mContext, dataBean.item_id.toString(), object : NetWork.TokenCallBack {
                    override fun doNext(resultData: String, headers: Headers?) {

                    }
                    override fun doError(msg: String) {

                    }

                    override fun doResult() {

                    }
                })
            }else{
                val intent_detial = Intent(mContext, SongSheetDetailsActivity::class.java)
                intent_detial.putExtra(SongSheetDetailsActivity.SONG_SHEET_ID, dataBean.item_id.toString() + "")
                mContext.startActivity(intent_detial)
            }

        }
    }

    private fun setMusicItem(helper: BaseViewHolder, dataBean: PondInfo.DataBean.DataListBean) {
        if (dataBean.item_info == null) return
        val item_info = dataBean.item_info

        if (item_info.music_type == 1) {
            helper.getView<View>(R.id.tv_type).visibility = VISIBLE
        } else {
            helper.getView<View>(R.id.tv_type).visibility = GONE
        }
        helper.getView<ImageView>(R.id.img_playmusic).visibility = VISIBLE
        if(null == dataBean.item_info.mv || dataBean.item_info.mv.equals("")){
            helper.getView<ImageView>(R.id.forwarding_mv).visibility = GONE
        }else{
            helper.getView<ImageView>(R.id.forwarding_mv).visibility = VISIBLE
        }
        val img_playmusic = helper.getView<ImageView>(R.id.img_playmusic)
        if (dataBean.isPlaying) {
            img_playmusic.setImageResource(R.drawable.icon_pond_play_true)
        } else {
            img_playmusic.setImageResource(R.drawable.icon_pond_play_false)
        }
        helper.setOnClickListener(R.id.ll_item) {

            if(dataBean.item_info.status == 0){
                var toast = Toast(mContext);
                var view = LayoutInflater.from(mContext).inflate(R.layout.customtoast, null);
                toast.setView(view);
                toast.setGravity(Gravity.TOP, 0, 30);
                toast.show();
            }else{
                if (MediaService.mediaPlayer != null && MediaService.bean != null && MediaService.bean?.id == dataBean.item_id) {
                    dataBean.isPlaying = !MediaService.mediaPlayer.isPlaying
                    mContext.sendBroadcast(Intent(ACTION_PAUSE))
                } else {
                    dataBean.isPlaying = true
                    PlayCtrlUtil.play(mContext, item_info.id, 0)
                }
                Observable.fromArray(data)
                        .flatMap<PondInfo.DataBean.DataListBean> { dataBeen -> Observable.fromIterable<PondInfo.DataBean.DataListBean>(dataBeen) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<PondInfo.DataBean.DataListBean> {
                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onNext(dataBean1: PondInfo.DataBean.DataListBean) {
                                if (dataBean1.item_id == dataBean.item_id) {
                                    dataBean1.isPlaying = dataBean.isPlaying
                                } else {
                                    dataBean1.isPlaying = false
                                }
                            }

                            override fun onError(e: Throwable) {

                            }

                            override fun onComplete() {
                                setNewData(data)//重置状态
                            }
                        })
            }


        }
    }

    private fun setMvItem(helper: BaseViewHolder, dataBean: PondInfo.DataBean.DataListBean) {
        if (dataBean.item_info == null) return
        val item_info = dataBean.item_info
        helper.getView<ImageView>(R.id.img_playmusic).visibility = GONE
        helper.getView<ImageView>(R.id.forwarding_mv).visibility = GONE
//        if(null == dataBean.item_info.mv || dataBean.item_info.mv.equals("")){
//            helper.getView<ImageView>(R.id.forwarding_mv).visibility = GONE
//        }else{
//            helper.getView<ImageView>(R.id.forwarding_mv).visibility = VISIBLE
//        }
        helper.setOnClickListener(R.id.ll_item) {
            if(dataBean.item_info.status == 0){
                var toast = Toast(mContext);
                var view = LayoutInflater.from(mContext).inflate(R.layout.customtoast, null);
                toast.setView(view);
                toast.setGravity(Gravity.TOP, 0, 30);
                toast.show();
            }else{
                toMV(dataBean.item_info.id,dataBean.item_info.mv_info.link,dataBean.item_info.uid,dataBean.item_info.title,dataBean.item_info.nickname,dataBean.item_info.imgpic_link);
            }


        }
    }

    private fun aboutImage(baseViewHolder: BaseViewHolder, dataBean: PondInfo.DataBean.DataListBean) {
        val nineGridView = baseViewHolder.getView<NineGridView>(R.id.nineGridView)
        if (dataBean.imglist_info == null || dataBean.imglist_info.size == 0) {
            nineGridView.visibility = GONE
        } else {
            nineGridView.visibility = VISIBLE
            var imgList = ArrayList<String>()
//            data[baseViewHolder.adapterPosition].imglist_info?.forEach {
//                imgList.add(it.link)
//            }
//            nineGridView.setUrlList(imgList)
//            nineGridView.setDataBean(dataBean)
            data[baseViewHolder.adapterPosition].imglist_info?.forEach {

                val lvImgList = baseViewHolder.getView<NoScrollRecyclerView>(R.id.pond_gif_img)
                val ext = it.ext
                nineGridView.visibility = View.VISIBLE
                lvImgList.visibility = View.VISIBLE
                if (ext == "gif") {
                    nineGridView.visibility = View.GONE
                    imgList.add(it.link)
                    val imglist_link = ArrayList<String>()
                    val imglist_info = dataBean.getImglist_info()
                    if (imglist_info != null) {
                        for (i in imglist_info.indices) {
                            imglist_link.add(imglist_info.get(i).getLink())
                        }
                        if (imgList.size > 0) {
                            val adapter = PondAdapterAdapterImgListAdapter(R.layout.pong_reply_img_item, imglist_link, imglist_info)
                            lvImgList.setLayoutManager(LinearLayoutManager(mContext))
                            lvImgList.setAdapter(adapter)
                            if (lvImgList.getRecycledViewPool() != null) {
                                lvImgList.getRecycledViewPool().setMaxRecycledViews(0, 10)
                            }
                            adapter.setOnItemClickListener { adapter, view, position ->
                                val bundle = Bundle()
                                val pictureDataBean = PictureDataBean()
                                        .setId(dataBean.getId().toString())
                                        .setCommentNum(dataBean.getHits())
                                        .setPhotoList(imglist_link)
                                        .setTitle(dataBean.getTitle())
                                        .setNickname(dataBean.getNickname())
                                        .setType("pond")
                                        .setPosition(position)
                                        .setHits(dataBean.getHits())
                                bundle.putSerializable(PicturePagerDetailsActivity.PICTURE_DATA, pictureDataBean)
                                val intent = Intent(mContext, PicturePagerDetailsActivity::class.java)
                                intent.putExtras(bundle)
                                mContext.startActivity(intent)
                            }
                        }
                    }
                } else {
                    lvImgList.visibility = View.GONE
                    imgList.add(it.link)
                    nineGridView.setUrlList(imgList)
                    nineGridView.setDataBean(dataBean)
                }
            }


        }
    }

    private fun setActivityItem(helper: BaseViewHolder, dataBean: PondInfo.DataBean.DataListBean) {
        if (dataBean.item_info == null) return
        val item_info = dataBean.item_info

        if (item_info.music_type == 6) {
            helper.getView<View>(R.id.tv_type).visibility = VISIBLE
        } else {
            helper.getView<View>(R.id.tv_type).visibility = GONE
        }
        helper.getView<ImageView>(R.id.img_playmusic).visibility = VISIBLE
        helper.setText(R.id.tv_item_nick, dataBean.item_info.sub_title)

        val img_playmusic = helper.getView<ImageView>(R.id.img_playmusic)
        img_playmusic.visibility = View.GONE
        Log.e("ggggg",""+item_info.imgpic)
        Log.e("ggggg",""+item_info.url)
        val img_item = helper.getView<ImageView>(R.id.img_item)
        Glide.with(mContext).load(item_info.imgpic).into(img_item)

//        ImageLoader.with(mContext).getSize(200,200).setUrl(dataBean.item_info.imgpic_info).into(helper.getView(R.id.img_item))
        helper.setOnClickListener(R.id.ll_item) {
            if (null!=item_info.url||item_info.url != "") {
                Log.e("ggggg",""+item_info.url)
                val intent = Intent(mContext, CommonWebview::class.java)
                intent.putExtra("url", item_info.url)
                intent.putExtra("activity", "activity")
                intent.putExtra("title", item_info.title)
                intent.putExtra("content", item_info.content)
                intent.putExtra("img", item_info.imgpic)
                mContext.startActivity(intent)
//                try {
//                    intent.putExtra("img", item_info.imgpic_info.link)
//                    mContext.startActivity(intent)
//                } catch (e: RuntimeException) {
//                }
            } else {
                Log.e("ggggg","ggggg"+item_info.url)
            }
        }
    }

    //跳转MV
    private fun toMV(id :Int,mvurl :String,uid:Int,title :String,nickname :String,imgpic_link :String) {
        val intent = Intent(mContext, MvVideoActivitykt::class.java)
        var bundle = Bundle()
        bundle.putInt("mv", id);
        bundle.putString("mvurl", mvurl);
        bundle.putInt("uid", uid);
        bundle.putString("title", title);
        bundle.putString("nickname", nickname);
        bundle.putString("imgpic_link", imgpic_link);
        bundle.putString("bioashi", 1.toString() + "")
        intent.putExtra("mvdate",bundle);
        mContext.startActivity(intent)
    }
}
