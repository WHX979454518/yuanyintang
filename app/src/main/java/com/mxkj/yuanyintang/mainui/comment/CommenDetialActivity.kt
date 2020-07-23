package com.mxkj.yuanyintang.mainui.comment

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import com.alibaba.fastjson.JSON
import com.jakewharton.rxbinding2.view.RxView
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.mainui.comment.CommentSuccessReceiver.SuccessCallBack
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.dynamic.activity.DynamicDetial
import com.mxkj.yuanyintang.mainui.dynamic.activity.DynamicDetial.Companion.DYNAMIC_ID
import com.mxkj.yuanyintang.mainui.dynamic.bean.DynamicBean
import com.mxkj.yuanyintang.mainui.emotionkeyboard.fragment.EmotionMainFragment
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.musicplayer.play_control.GetMusicInfo
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.net.ApiAddress
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.RichTextutils.ImageTextUtil
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.string.StringUtils

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import okhttp3.Headers

import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_FID
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_ITEM_ID
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_PID
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_SUCCESS
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_TYPE
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity.SONG_SHEET_ID
import com.mxkj.yuanyintang.musicplayer.util.SheetDetialBean
import kotlinx.android.synthetic.main.activity_commen_detial.*
import kotlinx.android.synthetic.main.comment_detial_head.view.*

class CommenDetialActivity : StandardUiActivity() {
    private var page = 1
    private var mid: Int = 0
    private var fid: Int = 0
    private val dataList = ArrayList<Comment.DataBean>()
    private var adapter: CommentDetialAdapterNew? = null
    private var parentBean: Comment.DataBean? = null
    private var itemtype: Int = 0// 1:音乐 2:歌单 3:圈子 4:评论
    private var pid: Int = 0
    private var emotionMainFragment: EmotionMainFragment? = null
    private var commentSuccessReceiver: CommentSuccessReceiver? = null
    private var filter: IntentFilter? = null
    internal lateinit var interfaceRefreshLoadMore: InterfaceRefreshLoadMore
    override val isVisibilityBottomPlayer: Boolean
        get() = false
    public override fun setLayoutId(): Int {
        return R.layout.activity_commen_detial
    }
    private lateinit var headview: View
    override fun initView() {
        hideTitle(true)
        registerCommentReceiver()
        adapter = CommentDetialAdapterNew(supportFragmentManager, dataList)
        headview = LayoutInflater.from(this@CommenDetialActivity).inflate(R.layout.comment_detial_head, null)
        adapter!!.addHeaderView(headview)
        recycler_comment!!.layoutManager = LinearLayoutManager(this)
        recycler_comment!!.adapter = adapter

    }
    public override fun initData() {
        val bundle = intent.extras
        if (bundle != null) {
            //被评论音乐的id或者动态id
            mid = bundle.getInt("item_id", 0)
            //父评论id
            fid = bundle.getInt("fid", 0)
            pid = bundle.getInt("pid", 0)
            //评论类型
            itemtype = bundle.getInt("type", 0)
            initItemInfo(itemtype, mid)
            //父评论详情
            parentBean = if (bundle != null) bundle.getSerializable("parentbean") as Comment.DataBean else null
        }
        initHeadViewData()
        initEmotionMainFragment()
        val params = HttpParams()
        params.put("type", itemtype.toString() + "")
        params.put("item_id", mid.toString() + "")
        params.put("fid", fid.toString() + "")
        params.put("p", page.toString() + "")
        params.put("row", 20.toString() + "")
        params.put("order", "create_time-desc")
        NetWork.getCommentList(page == 1, this, params, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                interfaceRefreshLoadMore.setStopRefreshing()
                try {
                    val `object` = JSONObject(resultData)
                    if (`object`.opt("data") != "") {
                        val comment = JSON.parseObject(resultData, Comment::class.java)
                        val data = comment.data
                        if (page == 1) {
                            dataList.clear()
                        }
                        dataList.addAll(data!!)
                        if (dataList.size > 0) {
                            for (i in 0 until dataList.size - 1) {
                                for (j in dataList.size - 1 downTo i + 1) {
                                    if (dataList[j].id == dataList[i].id) {
                                        dataList.removeAt(j)
                                    }
                                }
                            }
                        }
                        adapter!!.notifyDataSetChanged()
                    } else {

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }

            }

            override fun doError(msg: String) {
                interfaceRefreshLoadMore.setStopRefreshing()


            }

            override fun doResult() {

            }
        })
    }
    private fun initItemInfo(type: Int, item_id: Int) {
        when (type) {
            1 -> {
                tv_itemType.text = "单曲"
                Log.e(TAG, "initView: $item_id")
                GetMusicInfo.getMusicDetial(this, item_id, object : GetMusicInfo.SetBeanData {
                    override fun setBeanData(dataBean: MusicInfo.DataBean?) {
                        if (dataBean != null) {
                            tv_songName.text = dataBean.title
                            singer_song.text = dataBean.nickname
                            try {
                                ImageLoader.with(MainApplication.application)
                                        .getSize(200, 200)
                                        .override(40, 40)
                                        .url(dataBean.imgpic_info!!.link)
                                        .placeHolder(R.drawable.nothing)
                                        .error(R.drawable.nothing)
                                        .into(img_song)
                            } catch (e: RuntimeException) {
                            }

                        }
                    }
                })

                RxView.clicks(ll_music).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                    PlayCtrlUtil.play(this@CommenDetialActivity,item_id,0)
                }
            }
            2 -> {
                tv_itemType.text = "歌单"
                RxView.clicks(ll_music).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                    val intent = Intent(this@CommenDetialActivity, SongSheetDetailsActivity::class.java)
                    intent.putExtra(SONG_SHEET_ID, item_id.toString() + "")
                    startActivity(intent)
                }

                PlayCtrlUtil.getSheetDetail(this,item_id.toString(),object :PlayCtrlUtil.SheetDetialCallBack{
                    override fun getSheetDetial(sheetDetialBean: SheetDetialBean) {
                        if (sheetDetialBean != null && sheetDetialBean.data.size > 0) {
                            tv_songName.text = sheetDetialBean.data[0].title
                            singer_song.text = sheetDetialBean.data[0].nickname
                            try {
                                ImageLoader.with(this@CommenDetialActivity)
                                        .override(40, 40)
                                        .getSize(200, 200)
                                        .url(sheetDetialBean.data[0].imgpic_info.link)
                                        .placeHolder(R.drawable.nothing)
                                        .error(R.drawable.nothing)
                                        .into(img_song)
                            } catch (e: RuntimeException) {
                            }

                        }
                    }

                })
            }
            3 -> {
                img_song.visibility = View.GONE
                tv_itemType.text = "动态"
                RxView.clicks(ll_music).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                    val intent = Intent(this@CommenDetialActivity, DynamicDetial::class.java)
                    intent.putExtra(DYNAMIC_ID, item_id)
                    startActivity(intent)
                }
                NetWork.getDynamicDetial(this, ApiAddress.CIRCLE_DETIAL_DATA + item_id, object : NetWork.TokenCallBack {
                    override fun doNext(resultData: String, headers: Headers?) {
                        hideLoadingView()
                        val jsonObject = JSON.parseObject(resultData)
                        val dataObj = jsonObject.getJSONObject("data")
                        if (dataObj != null) {
                            val s = dataObj.toJSONString()
                            val detialBean = JSON.parseObject(s, DynamicBean.DataBean::class.java)
                            ImageTextUtil.setImageText(tv_songName, detialBean.depict + "")
                            singer_song.text = detialBean.nickname + "-" + detialBean.create_time
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

    override fun onDestroy() {
        super.onDestroy()
        interfaceRefreshLoadMore.resetRefreshView()
        unregisterReceiver(commentSuccessReceiver)
    }

    private fun initHeadViewData() {
        if (parentBean != null) {
            if (parentBean!!.head_link != null) {
                ImageLoader.with(this)
                        .getSize(200, 200)
                        .override(35, 35)
                        .url(parentBean!!.head_link)
                        .error(R.drawable.default_head_img)
                        .placeHolder(R.drawable.default_head_img)
                        .into( headview.img_icon)
            }
            if (parentBean!!.is_music == 3) {
                headview.v_rz!!.visibility = View.VISIBLE
            } else {
                headview. v_rz!!.visibility = View.INVISIBLE
            }
            headview.username!!.text = if (parentBean!!.nickname != null) parentBean!!.nickname else ""
            headview.tv_time!!.text = StringUtils.isEmpty(parentBean!!.create_time)
            if (parentBean!!.content != null) {
                Log.e(TAG, "initHeadViewData: " + parentBean!!.content)
                ImageTextUtil.setImageText( headview.tv_content, parentBean!!.content + "")
            }
            if (parentBean!!.is_agree == 0) {
                headview.img_agree!!.setImageResource(R.drawable.disagree)
            } else {
                headview.img_agree!!.setImageResource(R.drawable.agreed)
            }

            headview.img_agree!!.setOnClickListener {
                val params = HttpParams()
                params.put("type", "4")
                params.put("item_id", parentBean!!.id.toString() + "")
                NetWork.agree(this@CommenDetialActivity, params, object : NetWork.TokenCallBack {
                    override fun doNext(resultData: String, headers: Headers?) {
                    }
                    override fun doError(msg: String) {
                    }
                    override fun doResult() {

                    }
                })
            }
        }
    }

    override fun initEvent() {
        RxView.clicks(finish).subscribe{finish()}
        RxView.clicks(more_menu).subscribe{
            val dynamicOperationDialog = CommentDetialOperationDialog(parentBean!!)
            dynamicOperationDialog.show(supportFragmentManager, "mDynamicDialog")
        }
        interfaceRefreshLoadMore = InterfaceRefreshLoadMore(swipe_refresh, this, object : InterfaceRefreshLoadMore.RefreshLoadMoreCallback {
            override fun onRefresh() {
                page = 1
                initData()
            }

            override fun onLoadMore() {
                page++
                initData()
            }

            override fun onPushDistance(distance: Int) {

            }

            override fun onPullDistance(distance: Int) {

            }
        })
    }

    fun initEmotionMainFragment() {
        //构建传递参数
        val bundle = Bundle()
        //绑定主内容编辑框
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, true)
        //隐藏控件
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, false)
        bundle.putInt(COMMENT_TYPE, 0)
        bundle.putInt(COMMENT_ITEM_ID, pid)
        bundle.putInt(COMMENT_PID, parentBean!!.id)
        bundle.putInt(COMMENT_FID, fid)
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment::class.java, bundle)
        emotionMainFragment!!.bindToContentView(findViewById(R.id.root_layout))
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment)
        transaction.addToBackStack(null)
        transaction.commit()
        if (emotionMainFragment!!.bar_edit_text != null) {
            emotionMainFragment!!.bar_edit_text.hint = "说点什么吧~~"
        }
    }

    override fun onBackPressed() {
        if (!emotionMainFragment!!.isInterceptBackPress) {
            finish()
        }
    }

    private fun registerCommentReceiver() {
        commentSuccessReceiver = CommentSuccessReceiver(object:SuccessCallBack {
            override fun callBack(json: String, intent: Intent) {
                setSnackBar("发表成功", "", R.drawable.icon_success)
                if (intent != null) {
                    val pid = intent.getIntExtra(COMMENT_PID, 0)
                    val jsonObject1 = JSON.parseObject(json)
                    val dataBean = jsonObject1.getObject("data", Comment.DataBean::class.java)
                    dataList.add(0, dataBean)
                    adapter!!.notifyDataSetChanged()
                }
            }
        })
        filter = IntentFilter(COMMENT_SUCCESS)
        registerReceiver(commentSuccessReceiver, filter)
    }
}
