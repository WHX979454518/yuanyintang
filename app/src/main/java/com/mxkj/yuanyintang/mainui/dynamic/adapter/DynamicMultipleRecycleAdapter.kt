package com.mxkj.yuanyintang.mainui.dynamic.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.LinearLayout

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.flyco.dialog.listener.OnBtnClickL
import com.flyco.dialog.widget.MaterialDialog
import com.hyphenate.chat.EMClient
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.mainui.dynamic.circle_msg.CircleMsgListActivity
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.extraui.AgreeAnimationUtil
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.dynamic.activity.DynamicDetial
import com.mxkj.yuanyintang.mainui.dynamic.bean.DynamicBean
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.mainui.home.data.Constant
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew
import com.mxkj.yuanyintang.mainui.pond.activity.PondReplyDetialActivity
import com.mxkj.yuanyintang.extraui.activity.PicturePagerDetailsActivity
import com.mxkj.yuanyintang.extraui.bean.PictureDataBean
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.widget.NoScrollGridview
import com.mxkj.yuanyintang.widget.StretchyTextView

import java.util.ArrayList

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import okhttp3.Headers

import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.Companion.MUSICIAN_ID
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity.SONG_SHEET_ID
import com.mxkj.yuanyintang.mainui.home.data.Constant.DynamicItemType.TYPE_NEW_MSG
import com.mxkj.yuanyintang.mainui.pond.activity.PondReplyDetialActivity.Companion.POND_COMMENTID

class DynamicMultipleRecycleAdapter(private val dataBeans: List<DynamicBean.DataBean>, isUserZone: Boolean) : BaseMultiItemQuickAdapter<DynamicBean.DataBean, BaseViewHolder>(dataBeans) {
    private var isUserZone = false
    private var gridView: NoScrollGridview? = null
    private var adapterQuanzi: DynamicImgGridAdapter? = null

    init {
        this.isUserZone = isUserZone
        addItemType(Constant.DynamicItemType.TYPE_TEXT_IMG, R.layout.dynamic_item_image)
        addItemType(Constant.DynamicItemType.TYPE_TEXT_MUSIC, R.layout.dynamic_item_text_music)
        addItemType(Constant.DynamicItemType.TYPE_MUSIC_IMG, R.layout.dynamic_item_music_img)
        addItemType(Constant.DynamicItemType.TYPE_TOPIC_NOIMG, R.layout.dynamic_item_text_topic)
        addItemType(Constant.DynamicItemType.TYPE_TOPIC_IMG, R.layout.dynamic_item_topic_img)
        addItemType(Constant.DynamicItemType.TYPE_TEXT, R.layout.dynamic_item_circie_text)
        addItemType(Constant.DynamicItemType.TYPE_TEXT_ALBUM, R.layout.dynamic_item_album_text)
        addItemType(Constant.DynamicItemType.TYPE_IMG_ALBUM, R.layout.dynamic_item_album_img)
        addItemType(TYPE_NEW_MSG, R.layout.circle_head_msg_item)
    }

    override fun convert(baseViewHolder: com.chad.library.adapter.base.BaseViewHolder, dataBean: DynamicBean.DataBean) {
        if (dataBean.itemType == TYPE_NEW_MSG) {
            //新消息
            val newMsgBean = dataBean.newMsgBean
            baseViewHolder.setText(R.id.tv_msg_count, newMsgBean!!.msgNum.toString() + "条新消息")
            if (!TextUtils.isEmpty(newMsgBean!!.userIcon)) {
                ImageLoader.with(mContext).getSize(100, 100).url(newMsgBean.userIcon).asCircle().into(baseViewHolder.getView<View>(R.id.img_circle_icon))
            }
            val data = data
//            if (mContext is HomeActivity) {
//                (mContext as HomeActivity).showDynamicMsgCount(0)
//            }
            baseViewHolder.setOnClickListener(R.id.ll_to_circle_msg_center) {
                EMClient.getInstance().chatManager().markAllConversationsAsRead()
                mContext.sendBroadcast(Intent("clearMsgNum"))
                mContext.startActivity(Intent(mContext, CircleMsgListActivity::class.java))
                Handler().postDelayed({
                    if (data != null && data.size > 0) {
                        data.removeAt(0)
                        setNewData(data)
//                        (mContext as HomeActivity).hideMsg()
                    }
                }, 2000)
            }


            //
        } else {
            if (isUserZone) {
                baseViewHolder.setVisible(R.id.ll_follow, false)
            } else {
                baseViewHolder.setVisible(R.id.ll_follow, true)
            }
            //文本和图片
            if (dataBean.itemType == Constant.DynamicItemType.TYPE_TEXT_IMG) {
                aboutCommon(baseViewHolder, dataBean)
                aboutImage(baseViewHolder, dataBean)
            }
            //文本和音乐
            if (dataBean.itemType == Constant.DynamicItemType.TYPE_TEXT_MUSIC) {
                aboutCommon(baseViewHolder, dataBean)
                aboutMusic(baseViewHolder, dataBean)
            }
            //文本音乐图片
            if (dataBean.itemType == Constant.DynamicItemType.TYPE_MUSIC_IMG) {
                aboutCommon(baseViewHolder, dataBean)
                aboutMusic(baseViewHolder, dataBean)
                aboutImage(baseViewHolder, dataBean)
            }
            //文本话题
            if (dataBean.itemType == Constant.DynamicItemType.TYPE_TOPIC_NOIMG) {
                if (dataBean.topic != null && dataBean.topic!!.content != null) {
                    aboutCommon(baseViewHolder, dataBean)
                    aboutTopic(baseViewHolder, dataBean)
                }
                if (dataBean.topic_reply != null && dataBean.topic_reply!!.content != null) {
                    aboutCommon(baseViewHolder, dataBean)
                    aboutTopic(baseViewHolder, dataBean)
                }
            }
            //文本图片话题
            if (dataBean.itemType == Constant.DynamicItemType.TYPE_TOPIC_IMG) {
                if ((dataBean.topic != null && dataBean.topic!!.id != 0)) {
                    aboutCommon(baseViewHolder, dataBean)
                    aboutTopic(baseViewHolder, dataBean)
                    aboutImage(baseViewHolder, dataBean)
                }
                if ((dataBean.topic_reply != null && dataBean.topic_reply!!.id != 0)) {
                    aboutCommon(baseViewHolder, dataBean)
                    aboutTopic(baseViewHolder, dataBean)
                    aboutImage(baseViewHolder, dataBean)
                }
            }

            //文本和歌单
            if (dataBean.itemType == Constant.DynamicItemType.TYPE_TEXT_ALBUM) {
                aboutCommon(baseViewHolder, dataBean)
                aboutAlbum(baseViewHolder, dataBean)
            }
            //文本歌单图片
            if (dataBean.itemType == Constant.DynamicItemType.TYPE_IMG_ALBUM) {
                aboutCommon(baseViewHolder, dataBean)
                aboutAlbum(baseViewHolder, dataBean)
                aboutImage(baseViewHolder, dataBean)
            }

            //只有文字
            if (dataBean.itemType == Constant.DynamicItemType.TYPE_TEXT) {
                aboutCommon(baseViewHolder, dataBean)
            }
        }
    }

    /**
     * 歌单（专辑）
     */
    private fun aboutAlbum(baseViewHolder: BaseViewHolder, dataBean: DynamicBean.DataBean) {
        baseViewHolder.setText(R.id.tv_songName, dataBean.song!!.title).setText(R.id.singer_song, dataBean.song!!.nickname)
        baseViewHolder.setVisible(R.id.ll_music, true)
        try {
            ImageLoader.with(mContext)
                    .getSize(100, 100)
                    .scale(ScaleMode.CENTER_CROP)
                    .url(dataBean.song!!.imgpic_info!!.link)

                    .asCircle()
                    .placeHolder(R.drawable.nothing)
                    .error(R.drawable.nothing)
                    .into(baseViewHolder.getView<View>(R.id.img_song))

        } catch (e: RuntimeException) {
        }

        baseViewHolder.setOnClickListener(R.id.ll_music) {
            if (dataBean.song != null && dataBean.song!!.id != 0) {
                val intentDetial = Intent(mContext, SongSheetDetailsActivity::class.java)
                intentDetial.putExtra(SONG_SHEET_ID, (dataBean.song!!.id).toShort())
                mContext.startActivity(intentDetial)
            }
        }
        //播放歌单列表
        baseViewHolder.setOnClickListener(R.id.img_playmusic) {
            if (dataBean.song != null) {
                PlayCtrlUtil.playSheet(mContext, (dataBean.song!!.id).toString())
            }
        }
    }

    /**
     * 公共数据（标题、内容、时间、头像等）
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun aboutCommon(baseViewHolder: BaseViewHolder, dataBean: DynamicBean.DataBean) {
        baseViewHolder.setOnClickListener(R.id.ll_item_music) {
            val intent = Intent(mContext, DynamicDetial::class.java)
            intent.putExtra("dynamicId", dataBean.id)
            mContext.startActivity(intent)
        }
        baseViewHolder.getView<View>(R.id.content).isFocusable = false
        when {
            dataBean.push == 2 -> {
                baseViewHolder.setVisible(R.id.tv_from_share, true)
                baseViewHolder.setText(R.id.tv_from_share, "分享")
            }
            dataBean.push == 1 -> {
                baseViewHolder.setVisible(R.id.tv_from_share, true)
                baseViewHolder.setText(R.id.tv_from_share, "发布")
            }
            else -> baseViewHolder.setVisible(R.id.tv_from_share, false)
        }

        //点赞,评论,昵称,时间,是否点赞了
        baseViewHolder
                .setText(R.id.username, dataBean.nickname)
                .setText(R.id.tv_time, dataBean.create_time)
                .setText(R.id.tv_comment_num, dataBean.comment.toString())
                .setText(R.id.tv_agree_num, dataBean.agrees.toString())
        //头像
        ImageLoader.with(mContext)
                .override(35, 35)
                .getSize(100, 100)
                .url(dataBean.head_link)
                .scale(ScaleMode.CENTER_CROP)
                .placeHolder(R.drawable.default_head_img)
                .error(R.drawable.default_head_img)
                .asCircle()
                .into(baseViewHolder.getView<View>(R.id.img_icon))
        //vrenz
        if (dataBean.is_music == 3) {
            baseViewHolder.setVisible(R.id.v_rz, true)
        } else {
            baseViewHolder.setVisible(R.id.v_rz, false)
        }
        //说说文字
        val contentTextView = baseViewHolder.getView<StretchyTextView>(R.id.content)
        contentTextView.setMaxLineCount(6)
        if (dataBean.depict != null) {
            contentTextView.setContent(dataBean.depict!!.trim({ it <= ' ' }))
        }
        contentTextView.setOnTouchListener { _, _ ->
            val intent = Intent(mContext, DynamicDetial::class.java)
            intent.putExtra("dynamicId", dataBean.id)
            mContext.startActivity(intent)
            false
        }

        //性别
        if (dataBean.sex == 0) {
            baseViewHolder.setTextColor(R.id.username, Color.parseColor("#ff8585"))
        } else if (dataBean.sex == 1) {
            baseViewHolder.setTextColor(R.id.username, Color.parseColor("#4bb6f3"))
        }

        //点赞
        if (dataBean.is_agree == 1) {
            baseViewHolder.setImageResource(R.id.img_agree, R.drawable.agreed)
            baseViewHolder.setTextColor(R.id.tv_agree_num, ContextCompat.getColor(mContext, R.color.base_red))
        } else {
            baseViewHolder.setImageResource(R.id.img_agree, R.drawable.disagree)
            baseViewHolder.setTextColor(R.id.tv_agree_num, ContextCompat.getColor(mContext, R.color.dynamic_agree_num))
        }

        baseViewHolder.setOnClickListener(R.id.ll_agree
                //点击头像跳转到个人详情
        ) {
            if (dataBean.is_agree == 0) {
                AgreeAnimationUtil.setAnim(mContext, baseViewHolder.getView<View>(R.id.ani_img_agree) as ImageView, baseViewHolder.getView<View>(R.id.img_agree) as ImageView, AgreeAnimationUtil.COMMENT_AGREE)
            }
            val params = HttpParams()
            params.put("type", "3")
            params.put("item_id", dataBean.id.toString() + "")
            NetWork.agree(mContext, params, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    try {
                        val jsonObject = JSON.parseObject(resultData)
                        val code = jsonObject.getInteger("code")!!
                        if (code == 200) {
                            if (dataBean.is_agree == 1) {
                                baseViewHolder.setImageResource(R.id.img_agree, R.drawable.disagree)
                                baseViewHolder.setText(R.id.tv_agree_num, (dataBean.agrees - 1).toString())
                                baseViewHolder.setTextColor(R.id.tv_agree_num, ContextCompat.getColor(mContext, R.color.dynamic_agree_num))
                                dataBean.is_agree = 0
                                dataBean.agrees = dataBean.agrees - 1
                            } else {
                                baseViewHolder.setImageResource(R.id.img_agree, R.drawable.agreed)
                                baseViewHolder.setText(R.id.tv_agree_num, (dataBean.agrees + 1).toString())
                                baseViewHolder.setTextColor(R.id.tv_agree_num, ContextCompat.getColor(mContext, R.color.base_red))
                                dataBean.is_agree = 1
                                dataBean.agrees = 1 + dataBean.agrees
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

        //  进入用户中心
        baseViewHolder.setOnClickListener(R.id.img_icon) {
            val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
            intent.putExtra(MUSICIAN_ID, dataBean.uid.toString() + "")
            mContext.startActivity(intent)
        }

        baseViewHolder.setOnClickListener(R.id.username) {
            val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
            intent.putExtra(MUSICIAN_ID, dataBean.uid.toString() + "")
            mContext.startActivity(intent)
        }

        //点击关注以及首次进入页面时判断关注状态
        baseViewHolder.setOnClickListener(R.id.ll_follow) {
            if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN, false)) {
                if (dataBean.is_relation == 1 || dataBean.is_relation == 2) {
                    val dialog = MaterialDialog(mContext)
                    dialog.content(
                            "是否取消关注？")//
                            .btnText("取消", "确定")//
                            .titleTextSize(16f)
                            .titleTextColor(ContextCompat.getColor(mContext, R.color.color_14_text))
                            .contentTextColor(ContextCompat.getColor(mContext, R.color.color_66_text))
                            .contentTextSize(14f)
                            .btnTextSize(14f)
                            .btnTextColor(ContextCompat.getColor(mContext, R.color.base_red), ContextCompat.getColor(mContext, R.color.base_red))
                            .showAnim(null)//
                            .dismissAnim(null)//
                            .show()

                    dialog.setOnBtnClickL(
                            OnBtnClickL //left btn click listener
                            { dialog.dismiss() },
                            OnBtnClickL //right btn click listener
                            {
                                follow(baseViewHolder, dataBean)
                                dialog.dismiss()
                            }
                    )
                    dialog.show()
                } else {
                    follow(baseViewHolder, dataBean)
                }

            } else {
                mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
            }
        }
        when {
            dataBean.is_relation == 1 -> {
                baseViewHolder.setText(R.id.tv_follow, "已关注")
                baseViewHolder.setTextColor(R.id.tv_follow, Color.parseColor("#666666"))
                baseViewHolder.setBackgroundRes(R.id.tv_follow, R.drawable.shape_followed)
            }
            dataBean.is_relation == 2 -> {
                baseViewHolder.setText(R.id.tv_follow, "相互关注")
                baseViewHolder.setTextColor(R.id.tv_follow, Color.parseColor("#666666"))
                baseViewHolder.setBackgroundRes(R.id.tv_follow, R.drawable.shape_followed)
            }
            dataBean.is_relation == 0 -> {
                baseViewHolder.setText(R.id.tv_follow, "+关注")
                baseViewHolder.setTextColor(R.id.tv_follow, Color.parseColor("#f65f6c"))
                baseViewHolder.setBackgroundRes(R.id.tv_follow, R.drawable.shape_disfollowed)
            }
            dataBean.is_relation == 4 -> baseViewHolder.setVisible(R.id.ll_follow, false)
        }
    }

    /**
     * 关注、取关
     */
    private fun follow(baseViewHolder: BaseViewHolder, dataBean: DynamicBean.DataBean) {
        val params = HttpParams()
        params.put("id", dataBean.uid.toString() + "")
        NetWork.follow(mContext, params, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val `object` = JSON.parseObject(resultData)
                val code = `object`.getInteger("code")!!
                if (code == 200) {
                    if (dataBean.is_relation == 0 && dataBean.is_relation == 0) {
                        baseViewHolder.setText(R.id.tv_follow, "已关注")
                        baseViewHolder.setTextColor(R.id.tv_follow, Color.parseColor("#666666"))
                        baseViewHolder.setBackgroundRes(R.id.tv_follow, R.drawable.shape_followed)
                        dataBean.is_relation = 1
                        //                                        dataBean.setIs_relation(0);
                    } else if (dataBean.is_relation == 1) {
                        baseViewHolder.setText(R.id.tv_follow, "+关注")
                        baseViewHolder.setTextColor(R.id.tv_follow, Color.parseColor("#f65f6c"))
                        baseViewHolder.setBackgroundRes(R.id.tv_follow, R.drawable.shape_disfollowed)
                        dataBean.is_relation = 0
                        //                                        dataBean.setIs_relation(0);
                    } else if (dataBean.is_relation == 0 && dataBean.is_relation == 1) {
                        baseViewHolder.setText(R.id.tv_follow, "相互关注")
                        baseViewHolder.setTextColor(R.id.tv_follow, Color.parseColor("#666666"))
                        baseViewHolder.setBackgroundRes(R.id.tv_follow, R.drawable.shape_followed)
                        dataBean.is_relation = 2
                    } else if (dataBean.is_relation == 1 && dataBean.is_relation == 1) {
                        baseViewHolder.setText(R.id.tv_follow, "+关注")
                        baseViewHolder.setTextColor(R.id.tv_follow, Color.parseColor("#f65f6c"))
                        baseViewHolder.setBackgroundRes(R.id.tv_follow, R.drawable.shape_disfollowed)
                        dataBean.is_relation = 0
                        //                                        dataBean.setIs_relation(1);
                    } else if (dataBean.is_relation == 2) {
                        baseViewHolder.setText(R.id.tv_follow, "+关注")
                        baseViewHolder.setTextColor(R.id.tv_follow, Color.parseColor("#f65f6c"))
                        baseViewHolder.setBackgroundRes(R.id.tv_follow, R.drawable.shape_disfollowed)
                        dataBean.is_relation = 0
                    }
                    for (i in dataBeans.indices) {
                        if (dataBeans[i].uid == dataBean.uid) {
                            dataBeans[i].is_relation = dataBean.is_relation
                            notifyItemChanged(i)
                        }
                    }
                }
            }

            public override fun doError(msg: String) {

            }

            public override fun doResult() {

            }
        })
    }

    /**
     * 话题（池塘）
     */
    private fun aboutTopic(baseViewHolder: BaseViewHolder, dataBean: DynamicBean.DataBean) {
        if (baseViewHolder != null && dataBean.topic != null && dataBean.topic!!.title != null) {//池塘
            if (null != dataBean.topic!!.imgpic_info && !TextUtils.isEmpty(dataBean.topic!!.imgpic_info!!.link)) {
                ImageLoader.with(mContext)
                        .override(55, 55)
                        .getSize(100, 100)
                        .url(dataBean.topic!!.imgpic_info!!.link)

                        .scale(ScaleMode.CENTER_CROP)
                        .placeHolder(R.drawable.nothing)
                        .error(R.drawable.nothing)
                        .into(baseViewHolder.getView<View>(R.id.img_song))
            }
            if (!TextUtils.isEmpty(dataBean.topic!!.title)) {
                baseViewHolder.setText(R.id.tv_songName, dataBean.topic!!.title)
            }
            if (!TextUtils.isEmpty(dataBean.topic!!.content)) {
                baseViewHolder.setText(R.id.singer_song, dataBean.topic!!.content)
            }
            val llTopic = baseViewHolder.getView<LinearLayout>(R.id.ll_music)
            llTopic.isClickable = true
            llTopic.setOnClickListener {
                val intent = Intent(mContext, PondDetialActivityNew::class.java)
                val bundle = Bundle()
                bundle.putInt(PondDetialActivityNew.PID, dataBean.topic!!.id)
                intent.putExtras(bundle)
                mContext.startActivity(intent)
            }
        } else if (baseViewHolder != null && dataBean.topic_reply != null && dataBean.topic_reply!!.id != 0) {//池塘回复
            if (dataBean.topic_reply!!.imgpic_info != null && !TextUtils.isEmpty(dataBean.topic_reply!!.imgpic_info!!.link)) {
                ImageLoader.with(mContext)
                        .getSize(100, 100)
                        .override(55, 55)
                        .url(dataBean.topic_reply!!.imgpic_info!!.link)
                        .scale(ScaleMode.CENTER_CROP)
                        .placeHolder(R.drawable.nothing)
                        .error(R.drawable.nothing)
                        .into(baseViewHolder.getView<View>(R.id.img_song))
            }
            if (!TextUtils.isEmpty(dataBean.nickname)) {
                baseViewHolder.setText(R.id.tv_songName, dataBean.nickname)
            }
            if (!TextUtils.isEmpty(dataBean.topic_reply!!.content)) {
                baseViewHolder.setText(R.id.singer_song, dataBean.topic_reply!!.content)
            }
            val ll_topic = baseViewHolder.getView<LinearLayout>(R.id.ll_music)
            ll_topic.isClickable = true
            ll_topic.setOnClickListener {
                val intent = Intent(mContext, PondReplyDetialActivity::class.java)
                intent.putExtra(POND_COMMENTID, dataBean.topic_reply!!.id)
                mContext.startActivity(intent)
            }
        }
    }

    private fun aboutMusic(baseViewHolder: com.chad.library.adapter.base.BaseViewHolder, dataBean: DynamicBean.DataBean) {
        if (dataBean.music != null && dataBean.music!!.video_info != null) {
            baseViewHolder.setVisible(R.id.ll_music, true)
            baseViewHolder.setText(R.id.tv_songName, dataBean.music!!.title).setText(R.id.singer_song, dataBean.music!!.nickname)
            try {
                ImageLoader.with(mContext)
                        .override(55, 55)
                        .getSize(100, 100)
                        .format(1)
                        .url(dataBean.music!!.imgpic_info!!.link)

                        .scale(ScaleMode.CENTER_CROP)
                        .placeHolder(R.drawable.nothing)
                        .error(R.drawable.nothing)
                        .into(baseViewHolder.getView<View>(R.id.img_song))
            } catch (e: RuntimeException) {
            }

            //音乐详情
            baseViewHolder.setOnClickListener(R.id.ll_music) {
                PlayCtrlUtil.play(mContext,dataBean.music!!.id,0)

            }
            //播放音乐
            val img_playmusic = baseViewHolder.getView<ImageView>(R.id.img_playmusic)
            if (dataBean.music != null) {
                val music = dataBean.music
                if (!music!!.isPlaying) {
                    img_playmusic.setImageResource(R.drawable.dynamic_unplaying)
                } else {
                    img_playmusic.setImageResource(R.drawable.dynamic_playing)
                }
            }
            img_playmusic.setOnClickListener {
                //                    点击之后，正在播放就暂停，没有播放就不管
                val music = dataBean.music
                if (music != null) {
                    if (MediaService.mediaPlayer != null && MediaService.bean != null) {
                        if (MediaService.mediaPlayer.isPlaying) {//正在播放
                            music!!.isPlaying = music!!.id != MediaService.bean!!.id
                        } else {//当前没放歌
                            music!!.isPlaying = true
                        }
                    } else {//当前没放歌，直接播放点击的
                        music!!.isPlaying = true
                    }
                    Observable.fromArray<List<DynamicBean.DataBean>>(dataBeans)
                            .flatMap<DynamicBean.DataBean> { dataBeen -> Observable.fromIterable<DynamicBean.DataBean>(dataBeen) }
                            .subscribeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<DynamicBean.DataBean> {
                                override fun onSubscribe(d: Disposable) {

                                }

                                override fun onNext(dataBean1: DynamicBean.DataBean) {
                                    val music1 = dataBean1.music
                                    if (music1 != null && music1!!.id == music!!.id) {
                                        dataBean1.music = music
                                    } else {
                                        music1!!.isPlaying = false
                                        dataBean1.music = music1
                                    }
                                }

                                override fun onError(e: Throwable) {

                                }

                                override fun onComplete() {
                                    PlayCtrlUtil.play(mContext, dataBean.music!!.id, dataBean.music!!.song_id)
                                    setNewData(dataBeans)//重置状态
                                }
                            })
                }
            }
        }
    }

    private fun aboutImage(baseViewHolder: com.chad.library.adapter.base.BaseViewHolder, dataBean: DynamicBean.DataBean) {
        val listGrid = ArrayList<String>()
        baseViewHolder.getView<View>(R.id.dynamic_img_grid).isFocusable = false
        gridView = baseViewHolder.getView(R.id.dynamic_img_grid)
        if (dataBean.imglist_info!!.size == 4 || dataBean.imglist_info!!.size == 2) {
            gridView!!.numColumns = 2
        } else if (dataBean.imglist_info!!.size == 1) {
            gridView!!.numColumns = 1
        } else {
            gridView!!.numColumns = 3
        }

        val imglist_info = dataBean.imglist_info
        if (imglist_info != null) {
            for (i in imglist_info!!.indices) {
                listGrid.add(imglist_info!![i].link!!)
            }
            adapterQuanzi = DynamicImgGridAdapter(imglist_info!!, imglist_info!!.size, mContext)
            gridView!!.adapter = adapterQuanzi
            gridView!!.onItemClickListener = OnItemClickListener { _, _, position2, _ ->
                //点击查看图片
                val bundle = Bundle()
                val pictureDataBean = PictureDataBean()
                        .setId(dataBean.id.toString())
                        .setCommentNum(dataBean.hits)
                        .setPhotoList(listGrid)
                        .setTitle(dataBean.depict!!)
                        .setNickname(dataBean.nickname!!)
                        .setPosition(position2)
                        .setHits(dataBean.hits)
                        .setType("dynamic")
                bundle.putSerializable(PicturePagerDetailsActivity.PICTURE_DATA, pictureDataBean)
                val intent = Intent(mContext, PicturePagerDetailsActivity::class.java)
                intent.putExtras(bundle)
                mContext.startActivity(intent)
            }
        }
    }

}
