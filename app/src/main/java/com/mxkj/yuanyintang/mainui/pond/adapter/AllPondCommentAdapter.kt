package com.mxkj.yuanyintang.mainui.pond.adapter

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.R.id.tv_comment_content
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.extraui.AgreeAnimationUtil
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.net.NetWork

import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew
import com.mxkj.yuanyintang.mainui.pond.activity.PondReplyDetialActivity
import com.mxkj.yuanyintang.mainui.pond.bean.PondCommentBean
import com.mxkj.yuanyintang.extraui.dialog.Del_or_reply_Dialog_PongDetial
import com.mxkj.yuanyintang.utils.RichTextutils.ImageTextUtil
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.widget.soncomment.PondSonCommentItemView

import okhttp3.Headers

import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.Companion.MUSICIAN_ID
import com.mxkj.yuanyintang.mainui.home.data.Constant.PondAllCommentItemType.*
import com.mxkj.yuanyintang.mainui.pond.activity.PondReplyDetialActivity.Companion.POND_COMMENTID
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.musicplayer.service.MediaService.ACTION_PAUSE

class AllPondCommentAdapter(internal var data: MutableList<PondCommentBean.DataBean>, private val supportFragmentManager: FragmentManager, internal var commentType: String//hot热门评论，all全部评论
) : BaseMultiItemQuickAdapter<PondCommentBean.DataBean, BaseViewHolder>(data) {

    init {
        addItemType(MUSIC_IMG, R.layout.pond_comment_music_img)
        addItemType(MUSIC, R.layout.pond_comment_music)
        addItemType(IMG, R.layout.pond_comment_img)
        addItemType(TEXT, R.layout.pond_comment_text)
//        addItemType(ISSELF, R.layout.comment_item_self)

    }

    override fun convert(helper: BaseViewHolder, dataBean: PondCommentBean.DataBean) {
        if (dataBean.itemType != -1) {
            if (dataBean.itemType == MUSIC_IMG) {
                commonData(helper, dataBean)
                setImgData(helper, dataBean)
                setMusicData(helper, dataBean)
            } else if (dataBean.itemType == MUSIC) {
                commonData(helper, dataBean)
                setMusicData(helper, dataBean)
            } else if (dataBean.itemType == IMG) {
                commonData(helper, dataBean)
                setImgData(helper, dataBean)
            } else if (dataBean.itemType == TEXT) {
                commonData(helper, dataBean)
            }
//            else if (dataBean.itemType == ISSELF) {
//                commonData(helper, dataBean)
//                deleteData(helper, dataBean)
//            }
        }
    }

    /**
     * 公共的部分
     */
    fun commonData(helper: BaseViewHolder, dataBean: PondCommentBean.DataBean?) {
        try {
            helper.setOnClickListener(R.id.ll_to) {
                if (dataBean != null) {
                    val intent = Intent(mContext, PondReplyDetialActivity::class.java)
                    intent.putExtra("pond_commentBean", dataBean)
                    intent.putExtra(POND_COMMENTID, dataBean.id)
                    mContext.startActivity(intent)
                }
            }
            helper.setOnClickListener(R.id.tv_comment_content) {
                if (dataBean != null) {
                    val intent = Intent(mContext, PondReplyDetialActivity::class.java)
                    intent.putExtra("pond_commentBean", dataBean)
                    intent.putExtra(POND_COMMENTID, dataBean.id)
                    mContext.startActivity(intent)
                }
            }

            helper.setOnClickListener(R.id.img_icon) {
                if (dataBean != null) {
                    val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
                    intent.putExtra(MUSICIAN_ID, dataBean.uid.toString() + "")
                    //                intent.putExtra("clickPosition", baseViewHolder.getPosition());
                    mContext.startActivity(intent)
                }
            }
            helper.setOnClickListener(R.id.username) {
                if (dataBean != null) {
                    val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
                    intent.putExtra(MUSICIAN_ID, dataBean.uid.toString() + "")
                    //                intent.putExtra("clickPosition", baseViewHolder.getPosition());
                    mContext.startActivity(intent)
                }
            }

            helper.setText(R.id.username, dataBean!!.nickname)
                    .setText(R.id.create_time, dataBean.create_time + "")
                    .setText(R.id.agree_num, dataBean.agrees.toString() + "")
                    .setText(R.id.create_floor, dataBean.floor + "")

            val textView = helper.getView<TextView>(R.id.tv_comment_content)
            ImageTextUtil.setImageText(textView, dataBean.content + "")

            if (dataBean.head_link != null) {
                ImageLoader.with(mContext)
                        .override(55, 55)
                        .scale(ScaleMode.CENTER_CROP)
                        .asCircle()
                        .url(dataBean.head_link)
                        .into(helper.getView(R.id.img_icon))
            }
            if (dataBean.is_music == 3) {
                helper.setVisible(R.id.v_rz, true)
            } else {
                helper.setVisible(R.id.v_rz, false)
            }
            val view = helper.getView<TextView>(R.id.username)
            if (dataBean.sex == 0) {
                view.setTextColor(Color.parseColor("#ff6699"))
            } else {
                view.setTextColor(Color.parseColor("#2fabf1"))
            }
            val img_agree = helper.getView<ImageView>(R.id.img_agree)
            val tv_agree_num = helper.getView<TextView>(R.id.agree_num)
            if (dataBean.is_agree == 0) {
                img_agree.setImageResource(R.mipmap.icon_chitang_zan_gray)
            } else {
                img_agree.setImageResource(R.mipmap.icon_chitang_zan_pink)
            }

            helper.setOnClickListener(R.id.ll_agree) {
                if (dataBean.is_agree == 0) {
                    AgreeAnimationUtil.setAnim(mContext, helper.getView<View>(R.id.ani_agree) as ImageView, img_agree, AgreeAnimationUtil.COMMENT_AGREE)
                }
                val params = HttpParams()
                params.put("type", 5.toString() + "")
                params.put("item_id", dataBean.id.toString() + "")
                NetWork.agree(mContext, params, object : NetWork.TokenCallBack {
                    override fun doNext(resultData: String, headers: Headers?) {
                        try {
                            val jsonObject = JSON.parseObject(resultData)
                            val code = jsonObject.getInteger("code")!!
                            if (code == 200) {
                                if (dataBean.is_agree == 1) {
                                    img_agree.setImageResource(R.mipmap.icon_chitang_zan_gray)
                                    tv_agree_num.text = (dataBean.agrees - 1).toString() + ""
                                    dataBean.is_agree = 0
                                    dataBean.agrees = dataBean.agrees - 1
                                } else {
                                    img_agree.setImageResource(R.mipmap.icon_chitang_zan_pink)
                                    tv_agree_num.text = (dataBean.agrees + 1).toString() + ""
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

            helper.setOnClickListener(R.id.comment_menu, View.OnClickListener {
                if (!CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN, false)) {
                    mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
                    return@OnClickListener
                }
                (mContext as PondDetialActivityNew).setEventPosition(helper.position, commentType)
                val titleOperationDialog = Del_or_reply_Dialog_PongDetial(mContext, commentType, helper.position, dataBean, data, object : Del_or_reply_Dialog_PongDetial.ReplySuccessCallback {
                    override fun onSuccess(dataList: List<PondCommentBean.DataBean>?) {
                        setNewData(dataList)
                    }
                })
                val supportFragmentManager = (mContext as PondDetialActivityNew).supportFragmentManager
                titleOperationDialog.show(supportFragmentManager, "mTitleOperationDialog")
            })

            /**
             * 子评论
             */
            val pondSonCommentItemView = helper.getView<PondSonCommentItemView>(R.id.pond_son_item_view)
            if (dataBean.com_lists != null && dataBean.com_lists.size > 0) {
                pondSonCommentItemView.visibility = View.VISIBLE
                val com_lists = dataBean.com_lists
                pondSonCommentItemView.removeAllViews()
                pondSonCommentItemView.setSonViews(dataBean, supportFragmentManager, com_lists)
            } else {
                pondSonCommentItemView.visibility = View.GONE
            }

        } catch (e: RuntimeException) {
        }

    }

    private fun setImgData(helper: BaseViewHolder, dataBean: PondCommentBean.DataBean) {
        val recycler_img = helper.getView<RecyclerView>(R.id.recycler_pond_comment_img)
        val adapter = Hori_ImgListAdapter_Pond(mContext, dataBean)
        recycler_img.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        recycler_img.adapter = adapter
    }

    private fun setMusicData(helper: BaseViewHolder, dataBean: PondCommentBean.DataBean?) {
        helper.setOnClickListener(R.id.ll_music) {
            if (dataBean != null) {
                PlayCtrlUtil.play(mContext,dataBean.music_id,0)
            }
        }
        if (dataBean != null) {

        }

        helper.setOnClickListener(R.id.img_playmusic) {
            if (dataBean != null) {
//                PlayCtrlUtil.play(mContext, dataBean.music_id, 0)

                if (MediaService.mediaPlayer != null && MediaService.bean != null && MediaService.bean!!.id == dataBean.music_id) {
                    dataBean.setPlaying(!MediaService.mediaPlayer.isPlaying)
                    if (dataBean.isPlaying() == true) {
                        helper.getView<ImageView>(R.id.img_playmusic).setImageResource(R.drawable.icon_pond_play_true)
                        mContext.sendBroadcast(Intent(ACTION_PAUSE))
                    } else {
                        helper.getView<ImageView>(R.id.img_playmusic).setImageResource(R.drawable.dynamic_unplaying)
                        mContext.sendBroadcast(Intent(ACTION_PAUSE))
                    }
                } else {
                    PlayCtrlUtil.play(mContext, dataBean.music_id, 0)
                    dataBean.setPlaying(true)
                    helper.getView<ImageView>(R.id.img_playmusic).setImageResource(R.drawable.icon_pond_play_true)
                }
            }
        }

        helper.setText(R.id.tv_songName, dataBean!!.music_title)
                .setText(R.id.singer_song, dataBean.nickname)
        if (dataBean != null && dataBean.imgpic_info != null && dataBean.imgpic_info.link != null) {
            ImageLoader.with(mContext)
                    .override(40, 40)
                    .url(dataBean.imgpic_info.link)
                    .scale(ScaleMode.CENTER_CROP)
                    .into(helper.getView(R.id.img_song))
        }
    }
//    private fun deleteData(helper: BaseViewHolder, dataBean: PondCommentBean.DataBean) {
//        helper.setOnClickListener(R.id.ll_to_commentdetial) {
//            if (dataBean != null) {
//                val intent = Intent(mContext, PondReplyDetialActivity::class.java)
//                intent.putExtra("pond_commentBean", dataBean)
//                intent.putExtra(POND_COMMENTID, dataBean.id)
//                mContext.startActivity(intent)
//            }
//        }
//
//        helper.setOnClickListener(R.id.img_icon) {
//            if (dataBean != null) {
//                val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
//                intent.putExtra(MUSICIAN_ID, dataBean.uid.toString() + "")
//                //                intent.putExtra("clickPosition", baseViewHolder.getPosition());
//                mContext.startActivity(intent)
//            }
//        }
//
//        helper.setText(R.id.nickname, dataBean!!.nickname)
//                .setText(R.id.comment_createtime, dataBean.create_time + "")
//                .setText(R.id.tv_agree_num, dataBean.agrees.toString() + "")
//                .setText(R.id.tv_floor, dataBean.floor + "")
//
//        val textView = helper.getView<TextView>(R.id.tv_comment_content)
//        ImageTextUtil.setImageText(textView, dataBean.content + "")
//        if (dataBean.head_link != null) {
//            ImageLoader.with(mContext)
//                    .override(55, 55)
//                    .scale(ScaleMode.CENTER_CROP)
//                    .asCircle()
//                    .url(dataBean.head_link)
//                    .into(helper.getView(R.id.img_icon))
//        }
//        if (dataBean.is_music == 3) {
//            helper.setVisible(R.id.img_v_renzheng, true)
//        } else {
//            helper.setVisible(R.id.img_v_renzheng, false)
//        }
//        val view = helper.getView<TextView>(R.id.nickname)
//        if (dataBean.sex == 0) {
//            view.setTextColor(Color.parseColor("#ff8585"))
//        } else {
//            view.setTextColor(Color.parseColor("#4bb6f3"))
//        }
//        val img_agree = helper.getView<ImageView>(R.id.img_agree)
//        val tv_agree_num = helper.getView<TextView>(R.id.tv_agree_num)
//        if (dataBean.is_agree == 0) {
//            img_agree.setImageResource(R.drawable.disagree)
//        } else {
//            img_agree.setImageResource(R.drawable.agreed)
//        }
//        helper.setOnClickListener(R.id.ll_agree) {
//            if (dataBean.is_agree == 0) {
//                AgreeAnimationUtil.setAnim(mContext, helper.getView<View>(R.id.ani_agree) as ImageView, img_agree, AgreeAnimationUtil.COMMENT_AGREE)
//            }
//            val params = HttpParams()
//            params.put("type", 5.toString() + "")
//            params.put("item_id", dataBean.id.toString() + "")
//            NetWork.agree(mContext, params, object : NetWork.TokenCallBack {
//                override fun doNext(resultData: String, headers: Headers?) {
//                    try {
//                        val jsonObject = JSON.parseObject(resultData)
//                        val code = jsonObject.getInteger("code")!!
//                        if (code == 200) {
//                            if (dataBean.is_agree == 1) {
//                                img_agree.setImageResource(R.drawable.disagree)
//                                tv_agree_num.text = (dataBean.agrees - 1).toString() + ""
//                                dataBean.is_agree = 0
//                                dataBean.agrees = dataBean.agrees - 1
//                            } else {
//                                img_agree.setImageResource(R.drawable.agreed)
//                                tv_agree_num.text = (dataBean.agrees + 1).toString() + ""
//                                dataBean.is_agree = 1
//                                dataBean.agrees = 1 + dataBean.agrees
//                            }
//                        }
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//
//                }
//
//                override fun doError(msg: String) {
//
//                }
//
//                override fun doResult() {
//
//                }
//            })
//        }
//        helper.setOnClickListener(R.id.iv_more_menu, View.OnClickListener {
//            if (!CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN, false)) {
//                mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
//                return@OnClickListener
//            }
//            (mContext as PondDetialActivityNew).setEventPosition(helper.position, commentType)
//            val titleOperationDialog = Del_or_reply_Dialog_PongDetial(mContext, commentType, helper.position, dataBean, data, object : Del_or_reply_Dialog_PongDetial.ReplySuccessCallback {
//                override fun onSuccess(dataList: List<PondCommentBean.DataBean>?) {
//                    setNewData(dataList)
//                }
//            })
//            val supportFragmentManager = (mContext as PondDetialActivityNew).supportFragmentManager
//            titleOperationDialog.show(supportFragmentManager, "mTitleOperationDialog")
//        })
//        /**
//         * 删除自己的评论（池塘）
//         */
//        val dele_my_comment = helper.getView<TextView>(R.id.dele_my_comment)
//        dele_my_comment?.setOnClickListener {
//            NetWork.deleTopicComment(mContext, dataBean.id, object : NetWork.TokenCallBack {
//                override fun doNext(resultData: String, headers: Headers?) {
//                    if (mContext is BaseActivity) {
//                        (mContext as BaseActivity).setSnackBar("删除成功", "", R.drawable.icon_success)
//                    }
//                    for (i in data.indices) {
//                        if (data[i].id == dataBean.id) {
//                            data.removeAt(i)
//                            break
//                        }
//                    }
//                    setNewData(data)
//                    if (data.size==0&&footerLayoutCount == 0) {
//                        addFooterView(LayoutInflater.from(mContext).inflate(R.layout.no_comment_layout, null))
//                    }
//                }
//
//                override fun doError(msg: String) {
//
//                }
//
//                override fun doResult() {
//
//                }
//            })
//        }
//
//    }
}
