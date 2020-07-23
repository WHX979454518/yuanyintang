package com.mxkj.yuanyintang.mainui.pond.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.comment.Comment

import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.pond.activity.PondReplyDetialActivity
import com.mxkj.yuanyintang.extraui.dialog.Del_or_reply_CommentDialog
import com.mxkj.yuanyintang.utils.RichTextutils.ImageTextUtil
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils

import okhttp3.Headers

import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.Companion.MUSICIAN_ID

class PondReplySonCommentAdapte(internal var pondCommentId: Int, internal var data: MutableList<Comment.DataBean>, private val fragmentManager: FragmentManager) : BaseMultiItemQuickAdapter<Comment.DataBean, BaseViewHolder>(data) {

    init {
        addItemType(0, R.layout.comment_detial_item)
        addItemType(1, R.layout.comment_detial_item_my)
    }

    override fun convert(helper: BaseViewHolder, dataBean: Comment.DataBean?) {
        if (dataBean!!.itemType != -1) {
            helper.setText(R.id.nickname, dataBean.nickname)
                    .setText(R.id.comment_createtime, dataBean.create_time + "")
                    .setText(R.id.tv_agree_num, dataBean.agrees.toString() + "")
                    .setText(R.id.comment_floor, dataBean.floor + "")
            val textView = helper.getView<TextView>(R.id.tv_comment_content)
            textView.text = ""
            val startFrom2: Int
            val endFrom1: Int
            val str_1: String
            val length: Int
            val endFrom2: Int
            if (dataBean != null) {
                if (dataBean.to_nickname == null) {
                    dataBean.to_nickname = ""
                }
                if (dataBean.nickname == null) {
                    dataBean.nickname = ""
                }
                if (TextUtils.isEmpty(dataBean.to_nickname) || dataBean.to_nickname == dataBean.nickname) {
                    //总长度
                    str_1 = dataBean.nickname + " : "
                    length = str_1.length
                    //第一个昵称结束
                    endFrom1 = dataBean.nickname!!.length
                    val ssb = SpannableStringBuilder(str_1)
                    val clickSpan = MyClickableSpan(mContext, dataBean.nickname, dataBean)
                    ssb.setSpan(clickSpan, 0, endFrom1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    val clickSpan3 = MyClickableSpan(mContext, dataBean.content, dataBean)
                    ssb.setSpan(clickSpan3, endFrom1 + 1, length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    textView.append(ssb)
                    //                必须加这一句，否则就无法被点击
                    textView.movementMethod = LinkMovementMethod.getInstance()
                } else {
                    //总长度
                    str_1 = dataBean.nickname + " 回复 " + dataBean.to_nickname + " : "
                    length = str_1.length
                    //第一个昵称结束
                    endFrom1 = dataBean.nickname!!.length
                    //第二个昵称开始的位置
                    startFrom2 = endFrom1 + 4//回复的前后加了一个空格
                    //第二个昵称结束的位置
                    endFrom2 = startFrom2 + dataBean.to_nickname!!.length
                    val ssb = SpannableStringBuilder(str_1)
                    /**文字的点击事件 消灭重复代码 */
                    val clickSpan = MyClickableSpan(mContext, dataBean.nickname, dataBean)
                    ssb.setSpan(clickSpan, 0, endFrom1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    val clickSpan2 = MyClickableSpan(mContext, dataBean.to_nickname, dataBean)
                    ssb.setSpan(clickSpan2, startFrom2, endFrom2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    val clickSpan3 = MyClickableSpan(mContext, dataBean.content, dataBean)
                    ssb.setSpan(clickSpan3, endFrom2 + 1, length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    textView.append(ssb)
                    //必须加这一句，否则就无法被点击
                    textView.movementMethod = LinkMovementMethod.getInstance()
                }


                ImageTextUtil.setImageText(mContext, textView, dataBean.content, 1)
                /**
                 * 用户详情
                 */
                helper.setOnClickListener(R.id.img_icon) {
                    val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
                    intent.putExtra(MUSICIAN_ID, dataBean.uid.toString() + "")
                    mContext.startActivity(intent)
                }
                ImageLoader.with(mContext)
                        .override(40, 40)
                        .url(dataBean.head_link)
                        .asCircle()
                        .scale(ScaleMode.CENTER_CROP)
                        .error(R.drawable.default_head_img)
                        .placeHolder(R.drawable.default_head_img)
                        .into(helper.getView(R.id.img_icon))
                /**
                 * 加V
                 */
                if (dataBean.is_music == 3) {
                    helper.setVisible(R.id.img_v_renzheng, true)
                } else {
                    helper.setVisible(R.id.img_v_renzheng, false)
                }

                /**
                 * 评论点赞
                 */
                if (dataBean.is_agree == 1) {
                    helper.setImageResource(R.id.img_agree, R.mipmap.icon_chitang_zan_pink)
                    helper.setTextColor(R.id.tv_agree_num, ContextCompat.getColor(mContext, R.color.base_red))
                } else {
                    helper.setImageResource(R.id.img_agree, R.mipmap.icon_chitang_zan_gray)
                    helper.setTextColor(R.id.tv_agree_num, ContextCompat.getColor(mContext, R.color.grey_a6_text))
                }
                helper.setOnClickListener(R.id.ll_agree) {
                    val params = HttpParams()
                    params.put("type", "6")
                    params.put("item_id", dataBean.id.toString())
                    NetWork.agree(mContext, params, object : NetWork.TokenCallBack {
                        override fun doNext(resultData: String, headers: Headers?) {
                            val jsonObject = JSON.parseObject(resultData)
                            val code = jsonObject.getInteger("code")!!
                            if (code != 200) return
                            if (dataBean.is_agree == 1) {
                                helper.setImageResource(R.id.img_agree, R.mipmap.icon_chitang_zan_gray)
                                helper.setText(R.id.tv_agree_num, (dataBean.agrees - 1).toString() + "")
                                helper.setTextColor(R.id.tv_agree_num, ContextCompat.getColor(mContext, R.color.grey_a6_text))
                                dataBean.is_agree = 0
                                dataBean.agrees = dataBean.agrees - 1
                            } else {
                                helper.setImageResource(R.id.img_agree, R.mipmap.icon_chitang_zan_pink)
                                helper.setText(R.id.tv_agree_num, (dataBean.agrees + 1).toString() + "")
                                helper.setTextColor(R.id.tv_agree_num, ContextCompat.getColor(mContext, R.color.base_red))
                                dataBean.is_agree = 1
                                dataBean.agrees = 1 + dataBean.agrees
                            }
                        }

                        override fun doError(msg: String) {

                        }

                        override fun doResult() {

                        }
                    })
                }
                /**
                 * 举报\删除评论\回复评论的弹窗
                 */
                helper.setOnClickListener(R.id.ll_comment_item_more_menu, View.OnClickListener {
                    if (!CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN, false)) {
                        mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
                        return@OnClickListener
                    }
                    dataBean.type = 3
                    dataBean.pid = pondCommentId
                    val titleOperationDialog = Del_or_reply_CommentDialog(fragmentManager, true, mContext, dataBean, data, object : Del_or_reply_CommentDialog.ReplySuccessCallback {
                        override fun onSuccess(dataList: List<Comment.DataBean>) {
                            setNewData(dataList)
                        }
                    })
                    titleOperationDialog.show(fragmentManager, "mTitleOperationDialog")
                }
                        //点击头像跳转到个人详情
                )

                /**
                 * 删除自己的评论(子评论)
                 */

                val dele_my_comment = helper.getView<TextView>(R.id.dele_my_comment)
                dele_my_comment?.setOnClickListener {
                    NetWork.deleTopicSonComment(mContext, dataBean.id, object : NetWork.TokenCallBack {
                        override fun doNext(resultData: String, headers: Headers?) {
                            (mContext as PondReplyDetialActivity).setSnackBar("删除成功", "", R.drawable.icon_success)
                            if (data.contains(dataBean)) {
                                data.remove(dataBean)
                                setNewData(data)
                            }
                        }

                        override fun doError(msg: String) {

                        }

                        override fun doResult() {

                        }
                    })
                }
            }
        }
    }

    internal inner class MyClickableSpan(private val context: Context, private val text: String?, private val dataBean: Comment.DataBean?) : ClickableSpan() {

        //在这里设置字体的大小，等待各种属性
        override fun updateDrawState(ds: TextPaint) {
            if (text != null && dataBean!!.nickname != null && dataBean.to_nickname != null) {
                if (text == dataBean.nickname) {
                    if (dataBean.sex == 0) {
                        ds.color = Color.parseColor("#ff4c55")
                    } else {
                        ds.color = Color.parseColor("#5bc3f3")
                    }
                }
                if (text == dataBean.to_nickname) {
                    if (dataBean.to_sex == 0) {
                        ds.color = Color.parseColor("#ff4c55")
                    } else {
                        ds.color = Color.parseColor("#5bc3f3")
                    }
                }
            }
        }

        override fun onClick(widget: View) {
            if (text == dataBean!!.nickname) {
                val intent = Intent(context, MusicIanDetailsActivity::class.java)
                intent.putExtra(MUSICIAN_ID, dataBean.uid.toString() + "")
                context.startActivity(intent)
            } else if (text == dataBean!!.to_nickname) {
                val intent = Intent(context, MusicIanDetailsActivity::class.java)
                intent.putExtra(MUSICIAN_ID, dataBean.to_uid.toString() + "")
                context.startActivity(intent)
            }
        }
    }

}
