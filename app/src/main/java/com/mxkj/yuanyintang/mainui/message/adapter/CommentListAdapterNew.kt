package com.mxkj.yuanyintang.mainui.message.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.R.id.mv_update
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.mainui.comment.CommentActivity
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.message.bean.MsgListean
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew
import com.mxkj.yuanyintang.mainui.pond.activity.PondReplyDetialActivity
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.app.TimeUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder
import java.util.concurrent.TimeUnit

import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.Headers

import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.Companion.MUSICIAN_ID
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity.SONG_SHEET_ID
import com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.AgreeItemType.AGREE_MY_COMMENT_FOR_ALBUM
import com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.AgreeItemType.AGREE_MY_COMMENT_FOR_MUSIC
import com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.AgreeItemType.AGREE_MY_COMMENT_FOR_POND
import com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.CommentMsgItemType.REPLY_MYMUSIC
import com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.CommentMsgItemType.REPLY_MY_ALBUM
import com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.CommentMsgItemType.REPLY_MY_COMMENT_FOR_ALBUM
import com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.CommentMsgItemType.REPLY_MY_COMMENT_FOR_MUSIC
import com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.CommentMsgItemType.REPLY_MY_COMMENT_FOR_POND
import com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.CommentMsgItemType.REPLY_MY_POND
import com.mxkj.yuanyintang.mainui.pond.activity.PondReplyDetialActivity.Companion.POND_COMMENTID

/**
 * Created by Liujie 2018/1/20.
 */

class CommentListAdapterNew(data: List<MsgListean.DataBeanX.DataBean>) : BaseMultiItemQuickAdapter<MsgListean.DataBeanX.DataBean, BaseViewHolder>(data) {
    private var dialogView: View? = null
    private var diaLogBuilder: DiaLogBuilder? = null

    init {
        addItemType(REPLY_MYMUSIC, R.layout.msg_center_comment_msg_reply_music)//
        addItemType(REPLY_MY_COMMENT_FOR_MUSIC, R.layout.msg_center_reply_music_comment)


        addItemType(REPLY_MY_ALBUM, R.layout.msg_center_reply_album)
        addItemType(REPLY_MY_COMMENT_FOR_ALBUM, R.layout.msg_center_reply_album_comment)
        addItemType(REPLY_MY_POND, R.layout.msg_center_comment_pond)//
        addItemType(REPLY_MY_COMMENT_FOR_POND, R.layout.msg_center_comment_msg_reply_pond)//


        addItemType(AGREE_MY_COMMENT_FOR_MUSIC, R.layout.msg_center_agree_my_comment_for_music)
        addItemType(AGREE_MY_COMMENT_FOR_ALBUM, R.layout.msg_center_agree_my_comment_for_album)
        addItemType(AGREE_MY_COMMENT_FOR_POND, R.layout.msg_center_agree_pond_comment)
    }

    override fun convert(helper: BaseViewHolder, item: MsgListean.DataBeanX.DataBean) {
        try {
            val itemType = item.itemType
            if (item.type == 4 || item.type == 3) {
                if (itemType != 0) {
                    common_data(helper, item)
                    when (itemType) {
                        REPLY_MYMUSIC//评论了我的音乐
                        -> comment_music(helper, item)
                        REPLY_MY_COMMENT_FOR_MUSIC//回复了我对音乐的评论
                        -> reply_comment_for_music(helper, item)
                        REPLY_MY_ALBUM//评论了我的歌单
                        -> comment_album(helper, item)
                        REPLY_MY_COMMENT_FOR_ALBUM//回复了我对歌单的评论
                        -> reply_comment_for_album(helper, item)
                        REPLY_MY_POND//评论了我的池塘
                        -> comment_pond(helper, item)
                        REPLY_MY_COMMENT_FOR_POND//回复了我对池塘的评论
                        -> reply_comment_for_pond(helper, item)
                        AGREE_MY_COMMENT_FOR_MUSIC//对音乐评论点赞
                        -> agree_my_comment_for_music(helper, item)
                        AGREE_MY_COMMENT_FOR_ALBUM//对歌单评论点赞
                        -> agree_my_comment_for_album(helper, item)
                        AGREE_MY_COMMENT_FOR_POND//对池塘评论点赞
                        -> agree_my_comment_for_pond(helper, item)
                    }
                }
            }
        } catch (e: RuntimeException) {
        }

    }

    private fun comment_music(helper: BaseViewHolder, item: MsgListean.DataBeanX.DataBean) {
        val item_info = item.item_info
        val from_info = item.from_info
        val user_info = item.user_info
        if (item_info != null) {
            helper.setText(R.id.music_name, item_info.title)
            helper.setText(R.id.listen_num, item_info.counts.toString() + "")
            if (item_info.imgpic_info != null) {
                ImageLoader.with(mContext).getSize(100, 100).format(0).url(item_info.imgpic_info.link).error(R.drawable.nothing).into(helper.getView(R.id.img_song))
            }else{
                helper.setImageResource(R.id.img_song,R.drawable.nothing);
            }

            helper.setOnClickListener(R.id.ll_todetial) { toMusic(item_info.id) }
            helper.setOnClickListener(R.id.rl_content) {
                toComment(from_info!!.id, 1)//传音乐id
            }
        }
        if (from_info != null) {
            helper.setText(R.id.content, item.from_info.content)
            if (from_info.is_delete == 1) {
                helper.setText(R.id.content, "该条评论已被删除")
                helper.getView<View>(R.id.content).setBackgroundResource(R.drawable.shape_msg_delete)
                helper.getView<View>(R.id.rl_to_comment).visibility = View.GONE
                helper.getView<View>(R.id.showline1).visibility = View.GONE
                helper.getView<View>(R.id.showline2).visibility = View.VISIBLE
            }
        }
        if (user_info != null) {
            if (!TextUtils.isEmpty(user_info.head_link)) {
                ImageLoader.with(mContext).getSize(100, 100).format(0).url(user_info.head_link).error(R.drawable.nothing).into(helper.getView(R.id.img_head_icon))
            }
            helper.setText(R.id.nickname, user_info.nickname)
            helper.setOnClickListener(R.id.img_head_icon) { toUser(from_info!!.uid) }
        }
        val to_agree = helper.getView<ImageView>(R.id.to_agree)
        val ll_agree = helper.getView<LinearLayout>(R.id.ll_agree)

        val agree_info = item.agree_info
        if (agree_info != null) {
            val status = agree_info.status
            to_agree.setImageResource(if (status == 0) R.drawable.icon_chitang_zan_gray_3x else R.drawable.icon_chitang_zan_pink_3x)
            if (status != 0) {
                helper.setText(R.id.tv_agreenum, (item.from_info.agrees + 1).toString())
                helper.setTextColor(R.id.tv_agreenum, Color.parseColor("#ff6699"))
            }
        }
        RxView.clicks(ll_agree)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (from_info != null && from_info.is_delete != 1) {
                        NetWork.postAgreeAdd(mContext, agree_info.agree_type.toString(), from_info.id.toString() + "", object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jOjb = JSON.parseObject(resultData)
                                val data = jOjb["data"] as Int
                                if (data == 1) {
                                    if (agree_info != null) {
                                        agree_info.status = 1
                                    }
                                } else {
                                    if (agree_info != null) {
                                        agree_info.status = 0
                                    }
                                }
                                if (agree_info != null) {
                                    val status = agree_info.status
                                    to_agree.setImageResource(if (status == 0) R.drawable.icon_chitang_zan_gray_3x else R.drawable.icon_chitang_zan_pink_3x)
                                    if (status != 0) {
                                        helper.setText(R.id.tv_agreenum, (from_info.agrees + 1).toString())
                                        helper.setTextColor(R.id.tv_agreenum, Color.parseColor("#ff6699"))
                                    }
                                }
                            }

                            override fun doError(msg: String) {

                            }

                            override fun doResult() {

                            }
                        })
                    }
                }
        RxView.clicks(helper.getView(R.id.to_comment))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN)) {
                        showCommentDialog(helper.getView(R.id.to_comment), item)
                    } else {
                        mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
                    }
                }
    }

    private fun agree_my_comment_for_album(helper: BaseViewHolder, item: MsgListean.DataBeanX.DataBean) {
        val tv_my_reply = helper.getView<TextView>(R.id.tv_i_reply_others)
        val from_info = item.from_info
        val item_info = item.item_info
        val user_info = item.user_info
        val reply_api_desc = item.reply_api_desc
        val reply_info: MsgListean.DataBeanX.DataBean.FromInfoBean.ReplyInfoBean? = null
        if (from_info != null) {
            helper.setOnClickListener(R.id.img_head_icon) { toUser(from_info.uid) }

        }
        if (item_info != null) {
            helper.setText(R.id.listen_num, item_info.counts.toString() + "")
            helper.setText(R.id.music_name, item_info.title)
            if (item_info.imgpic_info != null) {
                ImageLoader.with(mContext).getSize(200, 200).url(item_info.imgpic_info.link).error(R.drawable.nothing).into(helper.getView(R.id.img_album))
            }
            helper.setOnClickListener(R.id.tv_i_reply_others) { toComment(from_info!!.id, 2) }
            helper.setOnClickListener(R.id.ll_todetial) { toAlbum(item_info.id) }
        }
        if (user_info != null) {
            helper.setText(R.id.nickname, user_info.nickname)
            if (user_info.head_info != null && !TextUtils.isEmpty(user_info.head_link)) {
                ImageLoader.with(mContext).getSize(200, 200).url(user_info.head_link).error(R.drawable.nothing).into(helper.getView(R.id.img_head_icon))
            }
        }

        if (from_info != null) {
            val content = from_info.content
            val nickname = from_info.nickname
            val uid = from_info.uid
            var spText = "我回复$nickname:$content"
            if (from_info.is_delete == 1) {
                spText = "我回复$nickname:该条评论已被删除"
                tv_my_reply.setBackgroundResource(R.drawable.shape_msg_delete)
            }
            val ssb = SpannableStringBuilder(spText)
            /*我*/
            val colorSpan1 = ForegroundColorSpan(Color.parseColor("#333333"))
            ssb.setSpan(colorSpan1, 0, 0, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            /*回复*/
            val colorSpan = ForegroundColorSpan(Color.parseColor("#666666"))
            ssb.setSpan(colorSpan, 1, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            /*昵称*/
            if (nickname.isNotEmpty()) {
                val spToNick = MyClickableSpan(mContext, nickname, uid)
                ssb.setSpan(spToNick, 3, 3 + nickname.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            /*内容*/
            val colorSpan2 = ForegroundColorSpan(Color.parseColor("#666666"))
            ssb.setSpan(colorSpan2, nickname.length + 4, spText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            tv_my_reply.text = ssb
            //                必须加这一句，否则就无法被点击
            tv_my_reply.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun agree_my_comment_for_pond(helper: BaseViewHolder, item: MsgListean.DataBeanX.DataBean) {
        val tv_my_reply = helper.getView<TextView>(R.id.tv_i_reply_others)
        val from_info = item.from_info
        val item_info = item.item_info
        val user_info = item.user_info
        val reply_api_desc = item.reply_api_desc
        val reply_info: MsgListean.DataBeanX.DataBean.FromInfoBean.ReplyInfoBean? = null

        if (item_info != null) {
            if (item_info.imglist_info != null && item_info.imglist_info.size > 0) {
                ImageLoader.with(mContext).getSize(200, 200).url(item_info.imglist_info[0].link).error(R.drawable.nothing).into(helper.getView(R.id.img_album))
            } else {
                helper.getView<View>(R.id.img_album).visibility = View.GONE
            }
            helper.setText(R.id.listen_num, item_info.counts.toString() + "")
            helper.setText(R.id.music_name, item_info.title)
            helper.setOnClickListener(R.id.ll_todetial) { toPond(item_info.id) }

        }
        if (user_info != null) {
            helper.setText(R.id.nickname, user_info.nickname)
            if (user_info.head_info != null && !TextUtils.isEmpty(user_info.head_link)) {
                ImageLoader.with(mContext).getSize(200, 200).url(user_info.head_link).error(R.drawable.nothing).into(helper.getView(R.id.img_head_icon))
            }
        }
        if (from_info != null) {
            helper.setOnClickListener(R.id.img_head_icon) { toUser(from_info.uid) }

            helper.setOnClickListener(R.id.tv_i_reply_others) { toPondComment(from_info.id) }


            val content = from_info.content
            val nickname = from_info.nickname
            val uid = from_info.uid
            var spText = "我回复$nickname:$content"
            if (from_info.is_delete == 1) {
                spText = "我回复$nickname:该条评论已被删除"
                tv_my_reply.setBackgroundResource(R.drawable.shape_msg_delete)
            }
            val ssb = SpannableStringBuilder(spText)
            /*回复*/
            val colorSpan = ForegroundColorSpan(Color.parseColor("#666666"))
            ssb.setSpan(colorSpan, 1, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            /*昵称*/
            val spToNick = MyClickableSpan(mContext, nickname, uid)
            if (nickname.length > 0) {
                ssb.setSpan(spToNick, 3, 2 + nickname.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            /*内容*/
            val colorSpan2 = ForegroundColorSpan(Color.parseColor("#666666"))
            ssb.setSpan(colorSpan2, nickname.length + 4, spText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            tv_my_reply.text = ssb
            //                必须加这一句，否则就无法被点击
            tv_my_reply.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun agree_my_comment_for_music(helper: BaseViewHolder, item: MsgListean.DataBeanX.DataBean) {
        val tv_my_reply = helper.getView<TextView>(R.id.tv_i_reply_others)
        val from_info = item.from_info
        val item_info = item.item_info
        val user_info = item.user_info
        val reply_api_desc = item.reply_api_desc
        val reply_info: MsgListean.DataBeanX.DataBean.FromInfoBean.ReplyInfoBean? = null

        if (item_info != null) {
            helper.setText(R.id.listen_num, item_info.counts.toString() + "")
            helper.setText(R.id.music_name, item_info.title)
            if (item_info.imgpic_info != null) {
                ImageLoader.with(mContext).getSize(200, 200).url(item_info.imgpic_info.link).error(R.drawable.nothing).into(helper.getView(R.id.img_album))
            }

            helper.setOnClickListener(R.id.ll_todetial) { toMusic(item_info.id) }
        }
        if (user_info != null) {
            helper.setText(R.id.nickname, user_info.nickname)
            if (user_info.head_info != null && !TextUtils.isEmpty(user_info.head_link)) {
                ImageLoader.with(mContext).getSize(100, 100).format(0).url(user_info.head_link).error(R.drawable.nothing).into(helper.getView(R.id.img_head_icon))
            }
        }

        if (from_info != null) {
            helper.setOnClickListener(R.id.img_head_icon) { toUser(from_info.uid) }
            val content = from_info.content
            val nickname = from_info.nickname
            val uid = from_info.uid
            var spText = "我回复$nickname:$content"
            if (from_info.is_delete == 1) {
                spText = "我回复$nickname:该条评论已被删除"
                tv_my_reply.setBackgroundResource(R.drawable.shape_msg_delete)
            }
            val ssb = SpannableStringBuilder(spText)
            /*我*/
            val colorSpan1 = ForegroundColorSpan(Color.parseColor("#333333"))
            ssb.setSpan(colorSpan1, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            /*回复*/
            val colorSpan = ForegroundColorSpan(Color.parseColor("#666666"))
            ssb.setSpan(colorSpan, 1, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            /*昵称*/
            if (nickname.isNotEmpty()) {
                val spToNick = MyClickableSpan(mContext, nickname, uid)
                ssb.setSpan(spToNick, 3, 3 + nickname.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            /*内容*/
            val colorSpan2 = ForegroundColorSpan(Color.parseColor("#666666"))
            ssb.setSpan(colorSpan2, nickname.length + 4, spText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            tv_my_reply.text = ssb
            //                必须加这一句，否则就无法被点击
            tv_my_reply.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun reply_comment_for_pond(helper: BaseViewHolder, item: MsgListean.DataBeanX.DataBean) {
        val item_info = item.item_info
        val fromInfoBean = item.from_info
        if (item_info != null) {
            if (fromInfoBean != null) {
                helper.setText(R.id.content, item.from_info.content)
                if (fromInfoBean.is_delete == 1) {
                    helper.setText(R.id.content, "该条评论已被删除")
                    helper.getView<View>(R.id.content).setBackgroundResource(R.drawable.shape_msg_delete)
                    helper.getView<View>(R.id.rl_to_comment).visibility = View.GONE
                    helper.getView<View>(R.id.showline1).visibility = View.GONE
                    helper.getView<View>(R.id.showline2).visibility = View.VISIBLE

                }
            }
            helper.setText(R.id.pond_name, item_info.title)
            helper.setText(R.id.listen_num, item_info.counts.toString() + "")
            if (item_info.imglist_info != null && item_info.imglist_info.size > 0) {
                ImageLoader.with(mContext).getSize(200, 200).url(item_info.imglist_info[0].link).error(R.drawable.nothing).into(helper.getView(R.id.img_song))
            } else {
                //helper.getView<View>(R.id.img_song).visibility = View.GONE
                helper.setImageResource(R.id.img_song,R.drawable.nothing);
            }
            helper.setOnClickListener(R.id.img_head_icon) { toUser(fromInfoBean!!.uid) }
            helper.setOnClickListener(R.id.content) { toPondComment(fromInfoBean!!.id) }
            helper.setOnClickListener(R.id.ll_todetial) { toPond(item_info.id) }
        }
        val reply_info = item.from_info.reply_info
        if (reply_info != null && reply_info.content != null) {
            val tv_my_comment = helper.getView<TextView>(R.id.tv_my_comment)
            var spannText = "我的评论:" + reply_info.content

            if (reply_info.is_delete == 1) {
                spannText = "我的评论:" + "该条评论已被删除"
                tv_my_comment.setBackgroundResource(R.drawable.shape_msg_delete)
            }

            val spannableString = SpannableString(spannText)
            val colorSpan = ForegroundColorSpan(Color.parseColor("#666666"))
            spannableString.setSpan(colorSpan, 5, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            tv_my_comment.text = spannableString
        }
        val agree_info = item.agree_info
        val to_agree = helper.getView<ImageView>(R.id.to_agree)
        val ll_agree = helper.getView<LinearLayout>(R.id.ll_agree)

        if (agree_info != null) {
            val status = agree_info.status
            to_agree.setImageResource(if (status == 0) R.drawable.icon_chitang_zan_gray_3x else R.drawable.icon_chitang_zan_pink_3x)
            if (status != 0) {
                helper.setText(R.id.tv_agreenum, (item.from_info.agrees + 1).toString())
                helper.setTextColor(R.id.tv_agreenum, Color.parseColor("#ff6699"))
            }
        }
        RxView.clicks(ll_agree)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (fromInfoBean != null && fromInfoBean.is_delete != 1) {
                        NetWork.postAgreeAdd(mContext, agree_info.agree_type.toString(), fromInfoBean.id.toString() + "", object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jOjb = JSON.parseObject(resultData)
                                val data = jOjb["data"] as Int
                                if (data == 1) {
                                    if (agree_info != null) {
                                        agree_info.status = 1
                                    }
                                } else {
                                    if (agree_info != null) {
                                        agree_info.status = 0
                                    }
                                }
                                if (agree_info != null) {
                                    val status = agree_info.status
                                    to_agree.setImageResource(if (status == 0) R.drawable.icon_chitang_zan_gray_3x else R.drawable.icon_chitang_zan_pink_3x)
                                    if (status != 0) {
                                        helper.setText(R.id.tv_agreenum, (item.from_info.agrees + 1).toString())
                                        helper.setTextColor(R.id.tv_agreenum, Color.parseColor("#ff6699"))
                                    }
                                }
                            }

                            override fun doError(msg: String) {

                            }

                            override fun doResult() {

                            }
                        })
                    }
                }
        RxView.clicks(helper.getView(R.id.to_comment))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN)) {
                        showCommentDialog(helper.getView(R.id.to_comment), item)
                    } else {
                        mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
                    }
                }


    }

    private fun showCommentDialog(view: View, dataBean: MsgListean.DataBeanX.DataBean?) {
        RxView.clicks(view)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN)) {
                        if (dataBean != null && dataBean.from_info != null && dataBean.from_info.is_delete != 1) {
                            initCommentDialog(dataBean)
                        }
                    } else {
                        mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
                    }
                }
    }

    private fun initCommentDialog(dataBean: MsgListean.DataBeanX.DataBean?) {
        val from_info = dataBean!!.from_info
        val itemType = dataBean.itemType
        val from_type = dataBean.from_type
        val item_info = dataBean.item_info
        val reply_api_desc = dataBean.reply_api_desc
        /* 1.单曲的评论
        2.歌单的评价
        3.池塘的评论 （1级评论）
        4.池塘的回复（2级评论）*/
        if (from_info != null) {
            dialogView = View.inflate(mContext, R.layout.dialog_msgcenter_comment, null)
            if (diaLogBuilder == null) {
                diaLogBuilder = DiaLogBuilder(mContext)
                        .setContentView(dialogView)
                        .setFullScreen()
                        .setGrvier(Gravity.CENTER)
                diaLogBuilder!!.setCanceledOnTouchOutside(false)
            }
            diaLogBuilder!!.show()
            val cancle = dialogView!!.findViewById<TextView>(R.id.tv_cancle)
            val sure = dialogView!!.findViewById<TextView>(R.id.tv_sure)
            val to_nickname = dialogView!!.findViewById<TextView>(R.id.to_nickname)
            to_nickname.text = "回复： " + from_info.nickname + ""
            val et_reply = dialogView!!.findViewById<EditText>(R.id.et_reply)


            cancle.setOnClickListener { diaLogBuilder!!.setDismiss() }
            sure.setOnClickListener {
                when (from_type) {
                    1//回复歌曲的评论
                        , 2//回复歌单评论
                    -> if (item_info != null && reply_api_desc != null) {
                        NetWork.addComment(mContext, 0, item_info.id, et_reply.text.toString(), reply_api_desc.pid, reply_api_desc.fid, object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                (mContext as BaseActivity).setSnackBar("回复成功", "", R.drawable.icon_success)

                                diaLogBuilder!!.setDismiss()

                            }

                            override fun doError(msg: String) {
                                diaLogBuilder!!.setDismiss()

                            }

                            override fun doResult() {

                            }
                        })
                    }
                    3//回复池塘评论
                        , 4//回复池塘二级评论
                    -> if (item_info != null && reply_api_desc != null) {
                        NetWork.addPondCommentReply(mContext, reply_api_desc.fid, reply_api_desc.pid, et_reply.text.toString(), object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                (mContext as BaseActivity).setSnackBar("回复成功", "", R.drawable.icon_success)
                                diaLogBuilder!!.setDismiss()
                            }

                            override fun doError(msg: String) {
                                diaLogBuilder!!.setDismiss()

                            }

                            override fun doResult() {

                            }
                        })
                    }
                }
            }
        }
    }

    private fun reply_comment_for_album(helper: BaseViewHolder, item: MsgListean.DataBeanX.DataBean) {
        val item_info = item.item_info
        val fromInfoBean = item.from_info
        if (fromInfoBean != null) {
            helper.setOnClickListener(R.id.img_head_icon) { toUser(fromInfoBean.uid) }
        }
        if (item_info != null) {
            if (item.from_info != null) {
                helper.setText(R.id.content, item.from_info.content)
                if (item.from_info.is_delete == 1) {
                    helper.setText(R.id.content, "该条评论已被删除")
                    helper.getView<View>(R.id.content).setBackgroundResource(R.drawable.shape_msg_delete)
                    helper.getView<View>(R.id.rl_to_comment).visibility = View.GONE
                }
            }
            helper.setText(R.id.album_name, item_info.title)
            helper.setText(R.id.listen_num, item_info.counts.toString() + "")
            if (item_info.imgpic_info != null) {
                Glide.with(mContext).load(item_info.imgpic_info.link).asBitmap().into(helper.getView<View>(R.id.img_song) as CircleImageView)
            }
            helper.setOnClickListener(R.id.ll_todetial) { toAlbum(item_info.id) }

            helper.setOnClickListener(R.id.rl_content) {
                toComment(fromInfoBean!!.id, 2)//传音乐id
            }
        }
        val reply_info = item.from_info.reply_info
        if (reply_info != null && reply_info.content != null) {
            val tv_my_comment = helper.getView<TextView>(R.id.tv_my_comment)
            var spannText = "我的评论:" + reply_info.content
            if (reply_info.is_delete == 1) {
                spannText = "我的评论:" + "该条评论已被删除"
                tv_my_comment.setBackgroundResource(R.drawable.shape_msg_delete)
            }
            val spannableString = SpannableString(spannText)
            val colorSpan = ForegroundColorSpan(Color.parseColor("#666666"))
            spannableString.setSpan(colorSpan, 5, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            tv_my_comment.text = spannableString
        }
        val agree_info = item.agree_info
        val to_agree = helper.getView<ImageView>(R.id.to_agree)
        if (agree_info != null) {
            val status = agree_info.status
            to_agree.setImageResource(if (status == 0) R.drawable.icon_chitang_zan_gray_3x else R.drawable.icon_chitang_zan_pink_3x)
            if (status != 0) {
                helper.setText(R.id.tv_agreenum, (item.from_info.agrees + 1).toString())
                helper.setTextColor(R.id.tv_agreenum, Color.parseColor("#ff6699"))
            }
        }
        val ll_agree = helper.getView<LinearLayout>(R.id.ll_agree)
        RxView.clicks(ll_agree)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (fromInfoBean != null && fromInfoBean.is_delete != 1) {
                        NetWork.postAgreeAdd(mContext, agree_info.agree_type.toString(), fromInfoBean.id.toString() + "", object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jOjb = JSON.parseObject(resultData)
                                val data = jOjb["data"] as Int
                                if (data == 1) {
                                    if (agree_info != null) {
                                        agree_info.status = 1
                                    }
                                } else {
                                    if (agree_info != null) {
                                        agree_info.status = 0
                                    }
                                }
                                if (agree_info != null) {
                                    val status = agree_info.status
                                    to_agree.setImageResource(if (status == 0) R.drawable.icon_chitang_zan_gray_3x else R.drawable.icon_chitang_zan_pink_3x)
                                    if (status != 0) {
                                        helper.setText(R.id.tv_agreenum, (item.from_info.agrees + 1).toString())
                                        helper.setTextColor(R.id.tv_agreenum, Color.parseColor("#ff6699"))
                                    }
                                }
                            }

                            override fun doError(msg: String) {

                            }

                            override fun doResult() {

                            }
                        })
                    }
                }
        RxView.clicks(helper.getView(R.id.to_comment))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN)) {
                        showCommentDialog(helper.getView(R.id.to_comment), item)
                    } else {
                        mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
                    }
                }

    }

    private fun comment_album(helper: BaseViewHolder, item: MsgListean.DataBeanX.DataBean) {
        val item_info = item.item_info
        val from_info = item.from_info
        if (from_info != null) {
            helper.setText(R.id.content, item.from_info.content)
            helper.setOnClickListener(R.id.img_head_icon) { toUser(from_info.uid) }

            helper.setOnClickListener(R.id.rl_content) {
                toComment(from_info.id, 2)//传歌单id
            }

            helper.setOnClickListener(R.id.ll_todetial) {
                if (item_info != null) {
                    toAlbum(item_info.id)
                }
            }
            if (from_info.is_delete == 1) {
                helper.setText(R.id.content, "该条评论已被删除")
                helper.getView<View>(R.id.content).setBackgroundResource(R.drawable.shape_msg_delete)
                helper.getView<View>(R.id.rl_to_comment).visibility = View.GONE
                helper.getView<View>(R.id.showline1).visibility = View.GONE
                helper.getView<View>(R.id.showline2).visibility = View.VISIBLE
            }

        }
        if (item_info != null) {
            helper.setText(R.id.music_name, item_info.title)
            helper.setText(R.id.listen_num, item_info.counts.toString() + "")
            if (item_info.imgpic_info != null) {
                Glide.with(mContext).load(item_info.imgpic_info.link).asBitmap().into(helper.getView<View>(R.id.img_album) as CircleImageView)
            }
        }
        val agree_info = item.agree_info
        val to_agree = helper.getView<ImageView>(R.id.to_agree)
        val ll_agree = helper.getView<LinearLayout>(R.id.ll_agree)
        if (agree_info != null) {
            val status = agree_info.status
            to_agree.setImageResource(if (status == 0) R.drawable.icon_chitang_zan_gray_3x else R.drawable.icon_chitang_zan_pink_3x)
            if (status != 0) {
                helper.setText(R.id.tv_agreenum, (item.from_info.agrees + 1).toString())
                helper.setTextColor(R.id.tv_agreenum, Color.parseColor("#ff6699"))
            }
        }
        RxView.clicks(ll_agree)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (from_info != null && from_info.is_delete != 1) {
                        NetWork.postAgreeAdd(mContext, agree_info.agree_type.toString(), from_info.id.toString() + "", object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jOjb = JSON.parseObject(resultData)
                                val data = jOjb["data"] as Int
                                if (data == 1) {
                                    if (agree_info != null) {
                                        agree_info.status = 1
                                    }
                                } else {
                                    if (agree_info != null) {
                                        agree_info.status = 0
                                    }
                                }
                                if (agree_info != null) {
                                    val status = agree_info.status
                                    to_agree.setImageResource(if (status == 0) R.drawable.icon_chitang_zan_gray_3x else R.drawable.icon_chitang_zan_pink_3x)
                                    if (status != 0) {
                                        helper.setText(R.id.tv_agreenum, (item.from_info.agrees + 1).toString())
                                        helper.setTextColor(R.id.tv_agreenum, Color.parseColor("#ff6699"))
                                    }
                                }
                            }

                            override fun doError(msg: String) {

                            }

                            override fun doResult() {

                            }
                        })
                    }
                }
        RxView.clicks(helper.getView(R.id.to_comment))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN)) {
                        showCommentDialog(helper.getView(R.id.to_comment), item)
                    } else {
                        mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
                    }
                }


    }

    private fun reply_comment_for_music(helper: BaseViewHolder, item: MsgListean.DataBeanX.DataBean) {
        val item_info = item.item_info
        val fromInfoBean = item.from_info
        val reply_info = item.from_info.reply_info
        val from_info = item.from_info
        if (from_info != null) {
            helper.setText(R.id.content, from_info.content)
            if (from_info.is_delete == 1) {
                helper.setText(R.id.content, "该条评论已被删除")
                helper.getView<View>(R.id.rl_to_comment).visibility = View.GONE
                helper.getView<View>(R.id.showline1).visibility = View.GONE
                helper.getView<View>(R.id.showline2).visibility = View.VISIBLE

                helper.getView<View>(R.id.content).setBackgroundResource(R.drawable.shape_msg_delete)
            }
            helper.setText(R.id.nickname, from_info.nickname)
            if (item.user_info != null && item.user_info.head_link != null) {
                ImageLoader.with(mContext).getSize(100, 100).url(item.user_info.head_link).into(helper.getView(R.id.img_head_icon))
            }
            helper.setOnClickListener(R.id.img_head_icon) { toUser(from_info.uid) }
        }
        if (reply_info != null && reply_info.content != null) {
            val tv_my_comment = helper.getView<TextView>(R.id.tv_my_comment)
            var spannText = "我的评论:" + reply_info.content
            if (reply_info.is_delete == 1) {
                spannText = "我的评论:" + "该条评论已被删除"
                tv_my_comment.setBackgroundResource(R.drawable.shape_msg_delete)

            }
            val spannableString = SpannableString(spannText)
            val colorSpan = ForegroundColorSpan(Color.parseColor("#666666"))
            spannableString.setSpan(colorSpan, 5, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            tv_my_comment.text = spannableString
        }
        if (item_info != null) {
            helper.setText(R.id.music_name, item_info.title)
            helper.setText(R.id.listen_num, item_info.counts.toString() + "")
            if (item_info.imgpic_info != null) {
                ImageLoader.with(mContext).getSize(200, 200).url(item_info.imgpic_info.link).into(helper.getView(R.id.img_song))
            }
            helper.setOnClickListener(R.id.ll_todetial) { toMusic(item_info.id) }
            helper.setOnClickListener(R.id.rl_content) {
                toComment(from_info!!.id, 1)//传音乐id
            }
        }
        val agree_info = item.agree_info
        val to_agree = helper.getView<ImageView>(R.id.to_agree)
        if (agree_info != null) {
            val status = agree_info.status
            to_agree.setImageResource(if (status == 0) R.drawable.icon_chitang_zan_gray_3x else R.drawable.icon_chitang_zan_pink_3x)
            if (status != 0) {
                helper.setText(R.id.tv_agreenum, (item.from_info.agrees + 1).toString())
                helper.setTextColor(R.id.tv_agreenum, Color.parseColor("#ff6699"))
            }
            if (status != 0) {
                helper.setText(R.id.tv_agreenum, (item.from_info.agrees + 1).toString())
                helper.setTextColor(R.id.tv_agreenum, Color.parseColor("#ff6699"))
            }
        }
        RxView.clicks(to_agree)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (from_info != null && from_info.is_delete != 1) {
                        NetWork.postAgreeAdd(mContext, "4", from_info.id.toString() + "", object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jOjb = JSON.parseObject(resultData)
                                val data = jOjb["data"] as Int
                                if (data == 1) {
                                    if (agree_info != null) {
                                        agree_info.status = 1
                                    }
                                } else {
                                    if (agree_info != null) {
                                        agree_info.status = 0
                                    }
                                }
                                if (agree_info != null) {
                                    val status = agree_info.status
                                    to_agree.setImageResource(if (status == 0) R.drawable.icon_chitang_zan_gray_3x else R.drawable.icon_chitang_zan_pink_3x)
                                    if (status != 0) {
                                        helper.setText(R.id.tv_agreenum, (item.from_info.agrees + 1).toString())
                                        helper.setTextColor(R.id.tv_agreenum, Color.parseColor("#ff6699"))
                                    }
                                }
                            }

                            override fun doError(msg: String) {

                            }

                            override fun doResult() {

                            }
                        })
                    }
                }
        RxView.clicks(helper.getView(R.id.to_comment))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN)) {
                        showCommentDialog(helper.getView(R.id.to_comment), item)
                    } else {
                        mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
                    }
                }


    }

    private fun comment_pond(helper: BaseViewHolder, item: MsgListean.DataBeanX.DataBean) {
        val item_info = item.item_info
        val from_info = item.from_info
        if (item_info != null) {
            if (from_info != null) {
                helper.setText(R.id.content, item.from_info.content)
                if (item_info.imglist_info != null && item_info.imglist_info.size > 0) {
                    helper.getView<View>(R.id.img_song).visibility = View.VISIBLE
                    ImageLoader.with(mContext).getSize(200, 200).url(item_info.imglist_info[0].link).error(R.drawable.nothing).into(helper.getView(R.id.img_song))
                } else {
//                    helper.getView<View>(R.id.img_song).visibility = View.GONE
                    helper.setImageResource(R.id.img_song,R.drawable.nothing);
                }
                helper.setOnClickListener(R.id.img_head_icon) { toUser(from_info.uid) }

                helper.setOnClickListener(R.id.content) { toPondComment(from_info.id) }
                if (from_info.is_delete == 1) {
                    helper.getView<View>(R.id.rl_to_comment).visibility = View.GONE
                    helper.getView<View>(R.id.showline1).visibility = View.GONE
                    helper.getView<View>(R.id.showline2).visibility = View.VISIBLE

                    helper.setText(R.id.content, "该条评论已被删除")
                    helper.getView<View>(R.id.content).setBackgroundResource(R.drawable.shape_msg_delete)
                }
            }
            helper.setOnClickListener(R.id.ll_todetial) { toPond(item_info.id) }
            helper.setText(R.id.pond_name, item_info.title)
            helper.setText(R.id.pond_read_count, item_info.counts.toString() + "")
        }
        val agree_info = item.agree_info
        val to_agree = helper.getView<ImageView>(R.id.to_agree)
        if (agree_info != null) {
            val status = agree_info.status
            to_agree.setImageResource(if (status == 0) R.drawable.icon_chitang_zan_gray_3x else R.drawable.icon_chitang_zan_pink_3x)
            if (status != 0) {
                helper.setText(R.id.tv_agreenum, (item.from_info.agrees + 1).toString())
                helper.setTextColor(R.id.tv_agreenum, Color.parseColor("#ff6699"))
            }
        }
        RxView.clicks(to_agree)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (from_info != null && from_info.is_delete != 1) {
                        NetWork.postAgreeAdd(mContext, "4", from_info.id.toString() + "", object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jOjb = JSON.parseObject(resultData)
                                val data = jOjb["data"] as Int
                                if (data == 1) {
                                    if (agree_info != null) {
                                        agree_info.status = 1
                                    }
                                } else {
                                    if (agree_info != null) {
                                        agree_info.status = 0
                                    }
                                }
                                if (agree_info != null) {
                                    val status = agree_info.status
                                    to_agree.setImageResource(if (status == 0) R.drawable.icon_chitang_zan_gray_3x else R.drawable.icon_chitang_zan_pink_3x)
                                    if (status != 0) {
                                        helper.setText(R.id.tv_agreenum, (item.from_info.agrees + 1).toString())
                                        helper.setTextColor(R.id.tv_agreenum, Color.parseColor("#ff6699"))
                                    }
                                }
                            }

                            override fun doError(msg: String) {

                            }

                            override fun doResult() {

                            }
                        })
                    }
                }
        RxView.clicks(helper.getView(R.id.to_comment))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN)) {
                        showCommentDialog(helper.getView(R.id.to_comment), item)
                    } else {
                        mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
                    }
                }
    }

    private fun common_data(helper: BaseViewHolder, dataBean: MsgListean.DataBeanX.DataBean) {
        helper.setText(R.id.nickname, dataBean.from_info.nickname)
//        helper.setText(R.id.tv_time, TimeUtils.timestampToDateChn(dataBean.from_info.create_time.toLong()))
        helper.setText(R.id.tv_time, TimeUtils.getFetureDate(dataBean.from_info.create_time.toLong()))
        if (!TextUtils.isEmpty(dataBean.user_info.head_link)) {
            ImageLoader.with(mContext).getSize(200, 200).url(dataBean.user_info.head_link).error(R.drawable.nothing).into(helper.getView(R.id.img_head_icon))
        }
    }

    internal inner class MyClickableSpan(private val context: Context, private val text: String, private val uid: Int) : ClickableSpan() {
        //在这里设置字体的大小，等待各种属性
        override fun updateDrawState(ds: TextPaint) {
            ds.color = Color.parseColor("#1a1717")
        }

        override fun onClick(widget: View) {
            toUser(uid)
        }
    }

    //跳转用户
    private fun toUser(uid: Int) {
        if (uid > 0) {
            val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
            intent.putExtra(MUSICIAN_ID, uid.toString() + "")
            mContext.startActivity(intent)
        }
    }

    //跳转音乐
    private fun toMusic(musicId: Int) {
//        if (musicId > 0) {
//            val intent = Intent(mContext, MusicDetailsActivity::class.java)
//            intent.putExtra(MUSIC_ID, musicId.toString() + "")
//            mContext.startActivity(intent)
//        }
    }

    //跳转歌单
    private fun toAlbum(albumId: Int) {
        if (albumId > 0) {
            val intent_detial = Intent(mContext, SongSheetDetailsActivity::class.java)
            intent_detial.putExtra(SONG_SHEET_ID, albumId.toString() + "")
            mContext.startActivity(intent_detial)
        }

    }

    //跳转池塘
    private fun toPond(pondId: Int) {
        if (pondId > 0) {
            val intent = Intent(mContext, PondDetialActivityNew::class.java)
            val bundle = Bundle()
            bundle.putInt(PondDetialActivityNew.PID, pondId)
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        }
    }

    //跳转评论（歌单、歌曲）
    private fun toComment(itemId: Int, type: Int) {
        if (itemId > 0) {
            val intent = Intent(mContext, CommentActivity::class.java)
            intent.putExtra(CommentActivity.TYPE, type)
            intent.putExtra(CommentActivity.ITEM_ID, itemId)
            mContext.startActivity(intent)
        }
    }

    //跳转评论（池塘）
    private fun toPondComment(topicReplyId: Int) {
        if (topicReplyId > 0) {
            val intent = Intent(mContext, PondReplyDetialActivity::class.java)
            intent.putExtra(POND_COMMENTID, topicReplyId)
            mContext.startActivity(intent)
        }
    }
}