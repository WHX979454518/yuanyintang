package com.mxkj.yuanyintang.mainui.dynamic.activity

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.jakewharton.rxbinding2.view.RxView
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.net.ApiAddress
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.comment.CommenDetialActivity
import com.mxkj.yuanyintang.mainui.comment.Comment
import com.mxkj.yuanyintang.mainui.comment.CommentSuccessReceiver
import com.mxkj.yuanyintang.mainui.dynamic.DynamicOperationDialog
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.dynamic.adapter.CommentAdapte
import com.mxkj.yuanyintang.mainui.dynamic.adapter.DynamicImgGridAdapter
import com.mxkj.yuanyintang.mainui.dynamic.bean.DynamicBean
import com.mxkj.yuanyintang.mainui.emotionkeyboard.fragment.EmotionMainFragment

import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew
import com.mxkj.yuanyintang.mainui.pond.activity.PondReplyDetialActivity
import com.mxkj.yuanyintang.extraui.activity.PicturePagerDetailsActivity
import com.mxkj.yuanyintang.extraui.bean.PictureDataBean
import com.mxkj.yuanyintang.utils.RichTextutils.ImageTextUtil
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.widget.CommentEditDialogFrag
import com.mxkj.yuanyintang.widget.NoScrollGridview

import java.util.ArrayList
import java.util.concurrent.TimeUnit
import okhttp3.Headers
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_FID
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_ITEM_ID
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_PID
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_SUCCESS
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_TYPE
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.Companion.MUSICIAN_ID
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity.SONG_SHEET_ID
import com.mxkj.yuanyintang.mainui.pond.activity.PondReplyDetialActivity.Companion.POND_COMMENTID
import kotlinx.android.synthetic.main.activity_dynamic_detial.*
import kotlinx.android.synthetic.main.dynamic_common_top.view.*
import kotlinx.android.synthetic.main.dynamic_detial_head.view.*
import kotlinx.android.synthetic.main.uc_navigationbar.*

class DynamicDetial : StandardUiActivity() {
    private var dynamicId: Int = 0
    private lateinit var detialBean: DynamicBean.DataBean
    private lateinit var headView: View
    private var dynamic_img_grid: NoScrollGridview? = null
    private val list_grid = ArrayList<String>()
    private val dataList = ArrayList<Comment.DataBean>()
    private var adapterDynamic: DynamicImgGridAdapter? = null
    private var page = 1
    lateinit var commentAdapte: CommentAdapte
    //    val supportFragmentManager: FragmentManager? = null
    private var emotionMainFragment: EmotionMainFragment? = null
    private val dialogForComment: CommentEditDialogFrag? = null
    private var commentSuccessReceiver: CommentSuccessReceiver? = null
    private var filter: IntentFilter? = null
    internal lateinit var interfaceRefreshLoadMore: InterfaceRefreshLoadMore
    private var nothing_view: View? = null
    lateinit var imgList: ArrayList<String>

    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun setLayoutId(): Int {
        return R.layout.activity_dynamic_detial
    }

    override fun initView() {
        setTitleText("动态详情")
        setRightButtonImageView(ContextCompat.getDrawable(this, R.drawable.icon_more_black))
        nothing_view = LayoutInflater.from(this@DynamicDetial).inflate(R.layout.no_comment_layout, null)
        registerCommentReceiver()
        val intent = intent
        if (intent != null) {
            dynamicId = intent.getIntExtra(DYNAMIC_ID, 0)
        }
        Log.e("TAG", "传来的动态id==" + dynamicId.toString())
        showLoadingView()
    }

    private fun initHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.dynamic_detial_head, null)
        /**
         * 关注
         */
        RxView.clicks(headView.ll_follow)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    if (CacheUtils.getBoolean(this@DynamicDetial, Constants.User.IS_LOGIN)) {
                        showLoadingView()
                        val params = HttpParams()
                        params.put("id", detialBean.uid.toString() + "")
                        NetWork.follow(this@DynamicDetial, params, object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                hideLoadingView()
                                val `object` = JSON.parseObject(resultData)
                                val code = `object`.getInteger("code")
                                if (code == 200) {
                                    if (detialBean.is_relation == 0 && detialBean.is_relation == 0) {
                                        headView.tv_follow.text = "已关注"
                                        headView.tv_follow.setTextColor(Color.parseColor("#666666"))
                                        headView.tv_follow.setBackgroundResource(R.drawable.shape_followed)
                                        detialBean.is_relation = 1
                                        //                                        detialBean.setIs_relation(0)
                                    } else if (detialBean.is_relation == 1) {
                                        headView.tv_follow.text = "+关注"
                                        headView.tv_follow.setTextColor(Color.parseColor("#f65f6c"))
                                        headView.tv_follow.setBackgroundResource(R.drawable.shape_disfollowed)
                                        detialBean.is_relation = 0
                                        //                                        detialBean.setIs_relation(0)
                                    } else if (detialBean.is_relation == 0 && detialBean.is_relation == 1) {
                                        headView.tv_follow.text = "相互关注"
                                        headView.tv_follow.setTextColor(Color.parseColor("#666666"))
                                        headView.tv_follow.setBackgroundResource(R.drawable.shape_followed)
                                        //                                        detialBean.setIs_relation(2)
                                        detialBean.is_relation = 2
                                    } else if (detialBean.is_relation == 1 && detialBean.is_relation == 1) {
                                        headView.tv_follow.text = "+关注"
                                        headView.tv_follow.setTextColor(Color.parseColor("#f65f6c"))
                                        headView.tv_follow.setBackgroundResource(R.drawable.shape_disfollowed)
                                        detialBean.is_relation = 0
                                        //                                        detialBean.setIs_relation(1)
                                    } else if (detialBean.is_relation == 2) {
                                        headView.tv_follow.text = "+关注"
                                        headView.tv_follow.setTextColor(Color.parseColor("#f65f6c"))
                                        headView.tv_follow.setBackgroundResource(R.drawable.shape_disfollowed)
                                        detialBean.is_relation = 0
                                    }
                                }
                            }

                            override fun doError(msg: String) {
                                hideLoadingView()
                            }

                            override fun doResult() {

                            }
                        })
                    } else {
                        startActivity(Intent(this@DynamicDetial, LoginRegMainPage::class.java))
                    }
                }
        /**
         * 点赞
         */
        RxView.clicks(headView.ll_agree).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            val params = HttpParams()
            params.put("type", 3.toString() + "")
            params.put("item_id", detialBean.id.toString() + "")
            NetWork.agree(this@DynamicDetial, params, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    try {
                        val jsonObject = JSON.parseObject(resultData)
                        val code = jsonObject.getInteger("code")
                        if (code == 200) {
                            if (detialBean.is_agree == 1) {
                                headView.img_agree.setImageResource(R.drawable.disagree)
                                headView.tv_agree_num.text = (detialBean.agrees - 1).toString() + ""
                                headView.tv_agree_num.setTextColor(ContextCompat.getColor(this@DynamicDetial, R.color.grey_a6_text))
                                detialBean.is_agree = 0
                                detialBean.agrees = detialBean.agrees - 1
                            } else {
                                headView.img_agree.setImageResource(R.drawable.agreed)
                                headView.tv_agree_num.text = (detialBean.agrees + 1).toString() + ""
                                headView.tv_agree_num.setTextColor(ContextCompat.getColor(this@DynamicDetial, R.color.base_red))
                                detialBean.is_agree = 1
                                detialBean.agrees = 1 + detialBean.agrees
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
        RxView.clicks(headView.rl_icon).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            val intent = Intent(this@DynamicDetial, MusicIanDetailsActivity::class.java)
            intent.putExtra(MUSICIAN_ID, detialBean.uid.toString() + "")
            startActivity(intent)
        }
        RxView.clicks(headView.username).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            val intent = Intent(this@DynamicDetial, MusicIanDetailsActivity::class.java)
            intent.putExtra(MUSICIAN_ID, detialBean.uid.toString() + "")
            startActivity(intent)
        }
        RxView.clicks(btn_comment).throttleFirst(3, TimeUnit.SECONDS).subscribe {
            NetWork.addComment(this@DynamicDetial, 3, detialBean.id, et_pinglun.getText().toString(), 0, 0, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    setSnackBar("评论成功", "", R.drawable.icon_success)
                    val commentBean: Comment.DataBean
                    val jsonObject1 = JSON.parseObject(resultData)
                    commentBean = jsonObject1.getObject("data", Comment.DataBean::class.java)
                    et_pinglun.text = ""
                    detialBean.comment = detialBean.comment + 1
                    dataList.add(0, commentBean)
                    commentAdapte.setNewData(dataList)
                    if (recycler_pinglun.scrollState === RecyclerView.SCROLL_STATE_IDLE && recycler_pinglun.isComputingLayout === false) {
                        commentAdapte.removeAllFooterView()
                    }
                }

                override fun doError(msg: String) {

                }

                override fun doResult() {

                }
            })
        }
    }

    override fun initData() {
        getCircleData()
    }

    override fun initEvent() {
        initHeadView()
        initEmotionMainFragment()
        recycler_pinglun.layoutManager = LinearLayoutManager(this)
        commentAdapte = CommentAdapte(3, dataList, supportFragmentManager)
        commentAdapte.addHeaderView(headView)
        recycler_pinglun.adapter = commentAdapte
        commentAdapte.setOnItemClickListener { _, _, position ->
            val detialBean = dataList[position]
            if (detialBean != null) {
                Log.e(TAG, "onItemClick: " + detialBean.nickname)
                val intent = Intent(this@DynamicDetial, CommenDetialActivity::class.java)
                val bundle = Bundle()
                bundle.putInt("item_id", detialBean.item_id)
                bundle.putInt("fid", detialBean.id)
                bundle.putInt("type", 3)
                bundle.putInt("pid", detialBean.id)
                bundle.putSerializable("parentbean", detialBean)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
        RxView.clicks(leftButton).subscribe { finish() }
        RxView.clicks(rightButton).subscribe {
            val dynamicOperationDialog = DynamicOperationDialog(this, detialBean)
            dynamicOperationDialog.show(supportFragmentManager, "mDynamicDialog")
        }
        RxView.clicks(img_show_emoji).subscribe { emotionMainFragment?.mEmotionKeyboard?.showEmotionLayout() }
        interfaceRefreshLoadMore = InterfaceRefreshLoadMore(swipe_refresh, this, object : InterfaceRefreshLoadMore.RefreshLoadMoreCallback {
            override fun onRefresh() {
                page = 1
                getDynamicComment()
            }

            override fun onLoadMore() {
                page++
                getDynamicComment()
            }

            override fun onPushDistance(distance: Int) {

            }

            override fun onPullDistance(distance: Int) {

            }

        })
    }

    /**
     * 获取动态
     *
     */
    private fun getCircleData() {
        val url = ApiAddress.CIRCLE_DETIAL_DATA + dynamicId
        NetWork.getDynamicDetial(this, url, object : NetWork.TokenCallBack {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            override fun doNext(resultData: String, headers: Headers?) {
                hideLoadingView()
                val jsonObject = JSON.parseObject(resultData)
                val dataObj = jsonObject.getJSONObject("data")
                val s = dataObj?.toJSONString()
                detialBean = JSON.parseObject(s, DynamicBean.DataBean::class.java)

                /**
                 * VIP
                 */
                if (detialBean?.is_music == 3) {
                    headView.v_rz.visibility = View.VISIBLE
                } else {
                    headView.v_rz.visibility = View.GONE
                }

                /**
                 * 动态评论
                 */
                dataList.clear()
                getDynamicComment()
                /**
                 * 动态配图
                 */
                if (detialBean.imglist_info != null && detialBean.imglist_info.isNotEmpty()) {
                    val imglist_info = detialBean.imglist_info
                    imgList = ArrayList()
                    if (imglist_info != null) {
                        for (i in imglist_info.indices) {
                            imgList.add(imglist_info[i].link)
                        }
                    }
                    list_grid.addAll(imgList)
                    if (detialBean.imglist_info.size == 4 || detialBean.imglist_info.size == 2) {
                        headView.dynamic_img_grid.numColumns = 2
                    } else if (detialBean.imglist_info.size == 1) {
                        headView.dynamic_img_grid.numColumns = 1
                    } else {
                        headView.dynamic_img_grid.numColumns = 3
                    }
                    if (adapterDynamic == null) {
                        adapterDynamic = DynamicImgGridAdapter(imglist_info, list_grid.size, this@DynamicDetial)
                        headView.dynamic_img_grid.adapter = adapterDynamic
                    }
                    headView.dynamic_img_grid.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                        val bundle = Bundle()
                        val pictureDataBean = PictureDataBean()
                                .setId(detialBean.id.toString() + "")
                                .setCommentNum(detialBean.hits)
                                .setPhotoList(list_grid)
                                .setTitle(detialBean.depict)
                                .setNickname(detialBean.nickname)
                                .setPosition(position)
                                .setHits(detialBean.hits)
                                .setType("dynamic")
                        bundle.putSerializable(PicturePagerDetailsActivity.PICTURE_DATA, pictureDataBean)
                        val intent = Intent(this@DynamicDetial, PicturePagerDetailsActivity::class.java)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                } else {
                    headView.dynamic_img_grid.visibility = View.GONE
                }
                if (detialBean.head_link != null) {
                    if (!isDestroyed) {
                        ImageLoader.with(this@DynamicDetial)
                                .getSize(200, 200)
                                .override(35, 35)
                                .url(detialBean.head_link)
                                .asCircle()
                                .placeHolder(R.drawable.default_head_img)
                                .error(R.drawable.default_head_img)
                                .into(headView.img_icon)
                    }
                }
                if (detialBean.sex == 0) {
                    headView.username.setTextColor(Color.parseColor("#ff6666"))
                } else if (detialBean.sex == 1) {
                    headView.username.setTextColor(Color.parseColor("#2fabf1"))
                }
                if (detialBean.is_music == 3) {
                    headView.v_rz.visibility = View.VISIBLE
                } else {
                    headView.v_rz.visibility = View.INVISIBLE
                }
                if (detialBean.nickname != null) {
                    headView.username.text = detialBean.nickname + ""
                }
                if (detialBean.create_time != null) {
                    headView.tv_time.text = detialBean.create_time + ""
                }
                if (detialBean.depict != null) {
                    ImageTextUtil.setImageText(headView.content, detialBean.depict + "")
                }
                headView.tv_agree_num.text = detialBean.agrees.toString() + ""
                if (detialBean.music != null && detialBean.music.title != null) {
                    headView.ll_music.visibility = View.VISIBLE
                    if (MediaService.mediaPlayer != null) {
                        if (MediaService.mediaPlayer.isPlaying) {
                            if (MediaService.bean != null && detialBean.music.id == MediaService.bean?.id) {
                                headView.img_playmusic.setImageResource(R.drawable.dynamic_playing)
                                detialBean.music.isPlaying = true
                            }
                        }
                    }
                    headView.ll_music.setOnClickListener {
                        PlayCtrlUtil.play(this@DynamicDetial,detialBean.music.id,0)
                    }

                    RxView.clicks(headView.img_playmusic).throttleFirst(1, TimeUnit.SECONDS)
                            .subscribe {
                                if (!detialBean.music.isPlaying) {
                                    headView.img_playmusic.setImageResource(R.drawable.dynamic_playing)
                                    detialBean.music.isPlaying = true
                                } else {
                                    headView.img_playmusic.setImageResource(R.drawable.dynamic_unplaying)
                                    detialBean.music.isPlaying = false
                                }
                                PlayCtrlUtil.play(this@DynamicDetial, detialBean.music.id, detialBean.music.song_id)
                            }
                    headView.singer_song.text = detialBean.music.nickname + ""
                    headView.tv_songName.text = detialBean.music.title + ""
                    if (detialBean.music.imgpic_info != null && detialBean.music.imgpic_info != null && detialBean.music.imgpic_info.link != null) {
                        ImageLoader.with(this@DynamicDetial)
                                .getSize(200, 200)
                                .override(55, 55)
                                .url(detialBean.music.imgpic_info.link)
                                .error(R.drawable.nothing)
                                .placeHolder(R.drawable.nothing)
                                .into(headView.img_song)
                    }
                } else if (detialBean.song != null && detialBean.song.title != null) {
                    headView.ll_music.visibility = View.VISIBLE
                    headView.item_type.text = "歌单"
                    headView.ll_music.setOnClickListener {
                        val intent = Intent(this@DynamicDetial, SongSheetDetailsActivity::class.java)
                        intent.putExtra(SONG_SHEET_ID, detialBean.song.id.toString() + "")
                        startActivity(intent)
                    }

                    RxView.clicks(headView.img_playmusic).throttleFirst(2, TimeUnit.SECONDS)
                            .subscribe { PlayCtrlUtil.playSheet(this@DynamicDetial, detialBean.song.id.toString() + "") }

                    headView.singer_song.text = detialBean.song.nickname + ""
                    headView.tv_songName.text = detialBean.song.title + ""
                    if (detialBean.song.imgpic_info != null && detialBean.song.imgpic_info.link != null) {
                        ImageLoader.with(this@DynamicDetial)
                                .getSize(200, 200)
                                .override(55, 55)
                                .url(detialBean.song.imgpic_info.link)
                                .error(R.drawable.nothing)
                                .placeHolder(R.drawable.nothing)
                                .into(headView.img_song)
                    }
                } else if (detialBean.topic != null && detialBean.topic.id != 0) {
                    headView.ll_music.visibility = View.VISIBLE
                    headView.img_playmusic.visibility = View.GONE
                    headView.item_type.text = "池塘"
                    headView.singer_song.text = detialBean.topic.content + ""
                    headView.tv_songName.text = detialBean.topic.title + ""
                    headView.ll_music.setOnClickListener {
                        val intent = Intent(this@DynamicDetial, PondDetialActivityNew::class.java)
                        val bundle = Bundle()
                        bundle.putInt(PondDetialActivityNew.PID, detialBean.topic.id)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                    if (detialBean.topic.imgpic_info != null && detialBean.topic.imgpic_info.link != null) {
                        ImageLoader.with(this@DynamicDetial)
                                .getSize(200, 200)
                                .override(55, 55)
                                .url(detialBean.topic.imgpic_info.link)
                                .error(R.drawable.nothing)
                                .placeHolder(R.drawable.nothing)
                                .into(headView.img_song)
                    }

                } else if (detialBean.topic_reply != null && detialBean.topic_reply.content != null) {
                    headView.ll_music.visibility = View.VISIBLE
                    headView.img_playmusic.visibility = View.GONE
                    headView.item_type.text = "池塘"
                    headView.singer_song.text = detialBean.topic_reply.content
                    headView.tv_songName.text = detialBean.nickname + ""
                    headView.ll_music.setOnClickListener {
                        val intent = Intent(this@DynamicDetial, PondReplyDetialActivity::class.java)
                        intent.putExtra(POND_COMMENTID, detialBean.topic_reply.id)
                        startActivity(intent)
                    }
                    ImageLoader.with(this@DynamicDetial)
                            .getSize(200, 200)
                            .override(55, 55)
                            .url(detialBean.topic_reply.imgpic_info.link)
                            .error(R.drawable.nothing)
                            .placeHolder(R.drawable.nothing)
                            .into(headView.img_song)
                }

                when {
                    detialBean.is_relation == 1 -> {
                        headView.tv_follow.text = "已关注"
                        headView.tv_follow.setTextColor(Color.parseColor("#666666"))
                        headView.tv_follow.setBackgroundResource(R.drawable.shape_followed)
                    }
                    detialBean.is_relation == 2 -> {
                        headView.tv_follow.text = "相互关注"
                        headView.tv_follow.setTextColor(Color.parseColor("#666666"))
                        headView.tv_follow.setBackgroundResource(R.drawable.shape_followed)
                    }
                    detialBean.is_relation == 0 -> {
                        headView.tv_follow.text = "+关注"
                        headView.tv_follow.setTextColor(Color.parseColor("#f65f6c"))
                        headView.tv_follow.setBackgroundResource(R.drawable.shape_disfollowed)
                    }
                    detialBean.is_relation == 4 -> headView.ll_follow.visibility = View.GONE
                }

                if (detialBean.is_agree == 0) {
                    headView.img_agree.setImageResource(R.drawable.disagree)
                } else if (detialBean.is_agree == 1) {
                    headView.img_agree.setImageResource(R.drawable.agreed)
                }
            }

            override fun doError(msg: String) {
                finish()
            }

            override fun doResult() {

            }
        })
    }

    /**
     * 获取评论
     */
    private fun getDynamicComment() {
        if (detialBean != null && detialBean.id != 0) {
            val params = HttpParams()
            params.put("type", 3.toString())
            params.put("item_id", detialBean.id.toString() + "")
            params.put("p", page.toString() + "")
            params.put("row", 20.toString() + "")
            params.put("order", "id-desc")
            NetWork.getCommentList(if (page == 1) true else false, this, params, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    interfaceRefreshLoadMore.setStopRefreshing()
                    val comment = JSON.parseObject(resultData, Comment::class.java)
                    if (comment != null) {
                        val data = comment.data
                        if (data != null && data.size != 0) {
                            if (recycler_pinglun.getScrollState() === RecyclerView.SCROLL_STATE_IDLE && recycler_pinglun.isComputingLayout() === false) {
                                commentAdapte.removeAllFooterView()
                            }
                            if (page == 1) {
                                dataList.clear()
                            }
                            dataList.addAll(data)
                            commentAdapte.setNewData(dataList)
                        } else {
                            if (page == 1) {
                                commentAdapte.setFooterView(nothing_view, 0)
                            }
                        }
                    }
                }

                override fun doError(msg: String) {
                    interfaceRefreshLoadMore.setStopRefreshing()
                }

                override fun doResult() {

                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(commentSuccessReceiver)
    }

    /**
     * 初始化表情面板
     */
    fun initEmotionMainFragment() {
        //构建传递参数
        val bundle = Bundle()
        //绑定主内容编辑框
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, true)
        //隐藏控件
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, false)
        bundle.putInt(COMMENT_TYPE, 3)
        bundle.putInt(COMMENT_ITEM_ID, dynamicId)
        bundle.putInt(COMMENT_PID, 0)
        bundle.putInt(COMMENT_FID, 0)
        //替换fragment
        //创建修改实例
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment::class.java, bundle)
        emotionMainFragment?.bindToContentView(et_pinglun)
        val transaction = supportFragmentManager.beginTransaction()
        // Replace whatever is in thefragment_container view with this fragment,
        // and add the transaction to the backstack
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment)
        transaction.addToBackStack(null)
        //提交修改
        transaction.commit()
    }

    override fun onBackPressed() {
        /**
         * 判断是否拦截返回键操作
         */
        if (!emotionMainFragment?.isInterceptBackPress!!) {
            finish()
        }
    }

    private fun registerCommentReceiver() {
        commentSuccessReceiver = CommentSuccessReceiver(object : CommentSuccessReceiver.SuccessCallBack {
            override fun callBack(json: String, intent: Intent) {
                setSnackBar("发表成功", "", R.drawable.icon_success)
                if (intent != null) {
                    val pid = intent.getIntExtra(COMMENT_PID, 0)
                    if (pid == 0) {
                        val sonBean: Comment.DataBean
                        val jsonObject1 = JSON.parseObject(json)
                        sonBean = jsonObject1.getObject("data", Comment.DataBean::class.java)
                        detialBean.comment = detialBean.comment + 1
                        dataList.add(0, sonBean)
                        commentAdapte.notifyDataSetChanged()
                        if (recycler_pinglun.scrollState === RecyclerView.SCROLL_STATE_IDLE && recycler_pinglun.isComputingLayout() === false) {

                            commentAdapte.removeAllFooterView()
                        }

                    } else {

                    }
                }
            }
        })
        filter = IntentFilter(COMMENT_SUCCESS)
        registerReceiver(commentSuccessReceiver, filter)
    }

    companion object {
        const val DYNAMIC_ID = "dynamicId"
    }


}
