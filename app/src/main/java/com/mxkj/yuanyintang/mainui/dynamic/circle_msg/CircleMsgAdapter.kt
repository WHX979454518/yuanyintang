package com.mxkj.yuanyintang.mainui.dynamic.circle_msg

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.string.StringUtils

import com.mxkj.yuanyintang.mainui.home.data.Constant.CircleMsgItemType.AGREE_MY_COMMENT
import com.mxkj.yuanyintang.mainui.home.data.Constant.CircleMsgItemType.AGREE_MY_DYNAMIC
import com.mxkj.yuanyintang.mainui.home.data.Constant.CircleMsgItemType.AGREE_MY_REPLY
import com.mxkj.yuanyintang.mainui.home.data.Constant.CircleMsgItemType.COMMENT_DYNAMIC
import com.mxkj.yuanyintang.mainui.home.data.Constant.CircleMsgItemType.REPLY_COMMENT_FOR_DYNAMIC
import com.mxkj.yuanyintang.mainui.home.data.Constant.CircleMsgItemType.TV_HISTORY_MSG

class CircleMsgAdapter(data: List<CircleMsgBean.DataBeanX.DataBean>) : BaseMultiItemQuickAdapter<CircleMsgBean.DataBeanX.DataBean, BaseViewHolder>(data) {
    init {
        addItemType(COMMENT_DYNAMIC, R.layout.circle_msg_with_pic)
        addItemType(REPLY_COMMENT_FOR_DYNAMIC, R.layout.circle_msg_with_pic)
        addItemType(AGREE_MY_DYNAMIC, R.layout.circle_msg_with_pic)
        addItemType(AGREE_MY_COMMENT, R.layout.circle_msg_with_pic)
        addItemType(AGREE_MY_REPLY, R.layout.circle_msg_with_pic)
        addItemType(TV_HISTORY_MSG, R.layout.circle_msg_load_history)

    }

    override fun convert(helper: BaseViewHolder, dateBean: CircleMsgBean.DataBeanX.DataBean?) {
        if (dateBean != null && dateBean.itemType != 0) {
            val fromInfo = dateBean.from_info//评论或点赞的内容
            val user_info = dateBean.user_info
            var reply_info: CircleMsgBean.DataBeanX.DataBean.FromInfoBean.ReplyInfoBean? = null
            if (fromInfo != null) {
                reply_info = fromInfo.reply_info
            }
            when (dateBean.itemType) {
                COMMENT_DYNAMIC -> {
                    commonData(helper, dateBean)
                    if (fromInfo != null) {
                        helper.setText(R.id.tv_event, fromInfo.content + "")
                    }
                }
                REPLY_COMMENT_FOR_DYNAMIC -> {
                    commonData(helper, dateBean)
                    if (reply_info != null) {
                        helper.setText(R.id.tv_event, reply_info.content)
                    }
                }
                AGREE_MY_DYNAMIC -> {
                    commonData(helper, dateBean)
                    if (fromInfo != null) {
                        helper.setText(R.id.tv_event, dateBean.body)
                    }
                }
                AGREE_MY_REPLY -> {
                    commonData(helper, dateBean)
                    helper.setText(R.id.tv_event, dateBean.body)
                }
                AGREE_MY_COMMENT -> {
                    commonData(helper, dateBean)
                    helper.setText(R.id.tv_event, dateBean.body)
                }
                TV_HISTORY_MSG -> helper.setText(R.id.tv_load_history, if (TextUtils.isEmpty(dateBean.title)) "点击加载历史消息" else dateBean.title)
            }
        }
    }

    private fun commonData(helper: BaseViewHolder, dateBean: CircleMsgBean.DataBeanX.DataBean) {
        val user_info = dateBean.user_info
        val from_info = dateBean.from_info//评论或点赞的内容
        val item_info = dateBean.item_info//你的动态或者你被别人回复的评论的内容
        val reply_info: CircleMsgBean.DataBeanX.DataBean.FromInfoBean.ReplyInfoBean
        if (user_info == null || from_info == null || item_info == null) {
            return
        }

        helper.setVisible(R.id.iv_is_vip, Integer.valueOf(user_info.is_music) == 3)

        helper.setOnClickListener(R.id.circle_msg_icon) {
            val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
            val bundle = Bundle()
            bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, user_info.id + "")
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        }
        helper.setOnClickListener(R.id.tv_name) {
            val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
            val bundle = Bundle()
            bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, user_info.id + "")
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        }

        helper.setText(R.id.tv_name, StringUtils.isEmpty(user_info.nickname))
        helper.setText(R.id.tv_time, StringUtils.isEmpty(dateBean.create_time_desc))
        ImageLoader.with(mContext).override(100, 100).getSize(100, 100).url(user_info.head_link).asCircle().into(helper.getView(R.id.circle_msg_icon))
        val imglist_info = item_info.imglist_info
        if (imglist_info != null && imglist_info.size > 0) {//显示图片
            helper.setVisible(R.id.img_item, true)
            helper.setVisible(R.id.tv_item, false)
            if (imglist_info != null && imglist_info.size > 0) {
                val link = imglist_info[0].link
                ImageLoader.with(mContext).override(60, 60).getSize(200, 200).url(link).rectRoundCorner(3).into(helper.getView(R.id.img_item))
            }
        } else {
            helper.setVisible(R.id.img_item, false)
            helper.setVisible(R.id.tv_item, true)
            helper.setText(R.id.tv_item, StringUtils.isEmpty(item_info.depict))
        }
    }
}
