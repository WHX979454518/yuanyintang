package com.mxkj.yuanyintang.mainui.comment

import android.content.Intent
import android.graphics.Color
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.extraui.AgreeAnimationUtil
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.extraui.dialog.Del_or_reply_CommentDialog
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.Companion.MUSICIAN_ID
import com.mxkj.yuanyintang.utils.RichTextutils.ImageTextUtil
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils

import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.Headers

class CommentDetialAdapterNew(private val fragmentManager: FragmentManager, internal var data: MutableList<Comment.DataBean>) : BaseMultiItemQuickAdapter<Comment.DataBean, BaseViewHolder>(data) {
    init {
        addItemType(0, R.layout.comment_detial_item)
        addItemType(1, R.layout.comment_detial_item_my)
    }
    override fun convert(helper: BaseViewHolder, dataBean: Comment.DataBean) {
        helper.setText(R.id.nickname, dataBean.nickname + "")
                .setText(R.id.comment_createtime, dataBean.create_time + "")
                .setText(R.id.tv_agree_num, dataBean.agrees.toString() + "")
        val view = helper.getView<TextView>(R.id.tv_comment_content)
        ImageTextUtil.setImageText(view, dataBean.content + "")
        /**
         * 用户详情
         */
        helper.setOnClickListener(R.id.img_icon) {
            val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
            intent.putExtra(MUSICIAN_ID, dataBean.uid.toString() + "")
            mContext.startActivity(intent)
        }
        ImageLoader.with(mContext)
                .getSize(200, 200)
                .url(dataBean.head_link)
                .scale(ScaleMode.CENTER_CROP)
                .into(helper.getView<View>(R.id.img_icon) as CircleImageView)
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
            helper.setImageResource(R.id.img_agree, R.drawable.agreed)
            (helper.getView<View>(R.id.tv_agree_num) as TextView).setTextColor(Color.parseColor("#ff4c55"))
        } else {
            helper.setImageResource(R.id.img_agree, R.drawable.disagree)
        }
        helper.setOnClickListener(R.id.ll_agree) {
            if (dataBean.is_agree == 0) {
                AgreeAnimationUtil.setAnim(mContext, helper.getView<View>(R.id.ani_agree) as ImageView, helper.getView(R.id.img_agree), AgreeAnimationUtil.COMMENT_AGREE)
            }
            val params = HttpParams()
            params.put("type", "4")
            params.put("item_id", dataBean.id.toString())
            NetWork.agree(mContext, params, object : NetWork.TokenCallBack {

                override fun doNext(resultData: String, headers: Headers?) {
                    try {
                        val jsonObject = JSON.parseObject(resultData)
                        val code = jsonObject.getInteger("code")!!
                        if (code == 200) {
                            if (dataBean.is_agree == 1) {
                                helper.setImageResource(R.id.img_agree, R.drawable.disagree)
                                helper.setText(R.id.tv_agree_num, (dataBean.agrees - 1).toString() + "")
                                dataBean.is_agree = 0
                                dataBean.agrees = dataBean.agrees - 1
                            } else {
                                helper.setImageResource(R.id.img_agree, R.drawable.agreed)
                                helper.setText(R.id.tv_agree_num, (dataBean.agrees + 1).toString() + "")
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
            //点击头像跳转到个人详情
        /**
         * 举报\删除评论\回复评论的弹窗
         */
        helper.setOnClickListener(R.id.ll_comment_item_more_menu, View.OnClickListener {
            if (!CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN, false)) {
                mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
                return@OnClickListener
            }
            dataBean.type = 3
            val titleOperationDialog = Del_or_reply_CommentDialog(fragmentManager, false, mContext, dataBean, data, object : Del_or_reply_CommentDialog.ReplySuccessCallback {
                override fun onSuccess(dataList: List<Comment.DataBean>) {
                    setNewData(dataList)
                }
            })
            titleOperationDialog.show(fragmentManager, "mTitleOperationDialog")
        }
            //点击头像跳转到个人详情
        )


        /**
         * 删除自己的评论
         *
         */
        val dele_my_comment = helper.getView<TextView>(R.id.dele_my_comment)
        dele_my_comment?.setOnClickListener {
            NetWork.deleComment(mContext, dataBean.id, object : NetWork.TokenCallBack {

                override fun doNext(resultData: String, headers: Headers?) {
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
