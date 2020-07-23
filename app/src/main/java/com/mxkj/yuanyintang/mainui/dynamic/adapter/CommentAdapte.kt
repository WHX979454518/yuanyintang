package com.mxkj.yuanyintang.mainui.dynamic.adapter

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.alibaba.fastjson.JSONException
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.extraui.AgreeAnimationUtil
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.comment.CommenDetialActivity
import com.mxkj.yuanyintang.mainui.comment.Comment

import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.extraui.dialog.Del_or_reply_CommentDialog
import com.mxkj.yuanyintang.utils.RichTextutils.ImageTextUtil
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.widget.CommentEditDialogFrag
import com.mxkj.yuanyintang.widget.soncomment.SonCommentItemView

import okhttp3.Headers

import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.Companion.MUSICIAN_ID
import kotlinx.android.synthetic.main.activity_comment.*

class CommentAdapte(internal var type: Int, internal var dataList: MutableList<Comment.DataBean>, private val supportFragmentManager: FragmentManager) : BaseMultiItemQuickAdapter<Comment.DataBean, BaseViewHolder>(dataList) {
    internal var dialogForComment_son: CommentEditDialogFrag? = null

    init {
        addItemType(0, R.layout.comment_item)
        addItemType(1, R.layout.comment_item_self)
    }

    override fun convert(helper: BaseViewHolder, dataBean: Comment.DataBean?) {
        if (dataBean!!.itemType != -1) {
            helper.setOnClickListener(R.id.img_icon) {
                val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
                intent.putExtra(MUSICIAN_ID, dataBean.uid.toString() + "")
                mContext.startActivity(intent)
            }
            helper.setOnClickListener(R.id.nickname) {
                val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
                intent.putExtra(MUSICIAN_ID, dataBean.uid.toString() + "")
                mContext.startActivity(intent)
            }

            /**楼层 */
            helper.setText(R.id.tv_floor, dataBean.floor)

            /**
             * 评论点赞
             */
            if (dataBean.is_agree == 1) {
                helper.setImageResource(R.id.img_agree, R.drawable.icon_bottomg_zan_pink)
            } else {
                helper.setImageResource(R.id.img_agree, R.drawable.icon_bottom_zan_gray)
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
                            if (dataBean.is_agree == 1) {
                                helper.setImageResource(R.id.img_agree, R.drawable.icon_bottom_zan_gray)
                                helper.setText(R.id.tv_agree_num, (dataBean.agrees - 1).toString() + "")
                                dataBean.is_agree = 0
                                dataBean.agrees = dataBean.agrees - 1
                            } else {
                                helper.setImageResource(R.id.img_agree, R.drawable.icon_bottomg_zan_pink)
                                helper.setText(R.id.tv_agree_num, (dataBean.agrees + 1).toString() + "")
                                dataBean.is_agree = 1
                                dataBean.agrees = 1 + dataBean.agrees
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
            helper.setOnClickListener(R.id.iv_more_menu, View.OnClickListener {
                if (!CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN, false)) {
                    mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
                    return@OnClickListener
                }
                dataBean.type = 3
                val titleOperationDialog = Del_or_reply_CommentDialog(supportFragmentManager, false, mContext, dataBean, dataList, object : Del_or_reply_CommentDialog.ReplySuccessCallback {
                    override fun onSuccess(dataList: List<Comment.DataBean>) {
                        setNewData(dataList)
                    }
                })
                titleOperationDialog.show(supportFragmentManager, "mTitleOperationDialog")
            }
                //点击头像跳转到个人详情
            )
            if (dataBean.sex == 0) {
                helper.setTextColor(R.id.nickname, Color.parseColor("#ff8585"))
            } else {
                helper.setTextColor(R.id.nickname, Color.parseColor("#4bb6f3"))
            }
            helper.setText(R.id.nickname, dataBean.nickname)
                    .setText(R.id.comment_createtime, dataBean.create_time + "")
                    .setText(R.id.tv_agree_num, dataBean.agrees.toString() + "")
            helper.setText(R.id.tv_comment_content, dataBean.content + "")

            val view = helper.getView<TextView>(R.id.tv_comment_content)
            ImageTextUtil.setImageText(view, dataBean.content + "")
            /**
             * 删除自己的评论
             */
            val dele_my_comment = helper.getView<TextView>(R.id.dele_my_comment)
            dele_my_comment?.setOnClickListener {
                NetWork.deleComment(mContext, dataBean.id, object : NetWork.TokenCallBack {
                    override fun doNext(resultData: String, headers: Headers?) {
                        if (mContext is BaseActivity) {
                            (mContext as BaseActivity).setSnackBar("删除成功", "", R.drawable.icon_success)
                        }
                        for (i in dataList.indices) {
                            if (dataList[i].id == dataBean.id) {
                                dataList.removeAt(i)
                                break
                            }
                        }
                        setNewData(dataList)
                        if (dataList.size==0&&footerLayoutCount == 0) {
                           addFooterView(LayoutInflater.from(mContext).inflate(R.layout.no_comment_layout, null))
                        }
                    }

                    override fun doError(msg: String) {

                    }

                    override fun doResult() {

                    }
                })
            }
            ImageLoader.with(mContext)
                    .getSize(200, 200)
                    .override(40, 40)
                    .url(dataBean.head_link)
                    .placeHolder(R.drawable.default_head_img)
                    .error(R.drawable.default_head_img)
                    .scale(ScaleMode.CENTER_CROP)
                    .asCircle()
                    .into(helper.getView(R.id.img_icon))
            if (dataBean.is_music == 3) {
                helper.setVisible(R.id.img_v_renzheng, true)
            } else {
                helper.setVisible(R.id.img_v_renzheng, false)
            }
            /**
             * 子评论
             */
            val son = dataBean.son
            if (son != null && son.size > 0) {
                val sonCommentItemView = helper.getView<SonCommentItemView>(R.id.son_comment_item)
                sonCommentItemView.removeAllViews()
                sonCommentItemView.setSonViews(supportFragmentManager, son)
                helper.setOnClickListener(R.id.son_comment_item) {
                    if (dataBean != null) {
                        val intent = Intent(mContext, CommenDetialActivity::class.java)
                        val bundle = Bundle()
                        bundle.putInt("item_id", dataBean.item_id)
                        bundle.putInt("fid", dataBean.id)
                        bundle.putInt("type", type)
                        bundle.putInt("pid", dataBean.id)
                        bundle.putSerializable("parentbean", dataBean)
                        intent.putExtras(bundle)
                        mContext.startActivity(intent)
                    }
                }

            } else {
                helper.getView<View>(R.id.ll_son_comment).visibility = View.GONE
            }
        }
    }

}
