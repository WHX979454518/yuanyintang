package com.mxkj.yuanyintang.mainui.comment

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jakewharton.rxbinding2.view.RxView
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.musicplayer.play_control.GetMusicInfo
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.dynamic.adapter.CommentAdapte
import com.mxkj.yuanyintang.mainui.emotionkeyboard.fragment.EmotionMainFragment

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
import kotlinx.android.synthetic.main.activity_comment.*

class CommentActivity : StandardUiActivity() {
    internal lateinit var adapter: CommentAdapte
    private val dataList = ArrayList<Comment.DataBean>()
    private var screenHeight = 0
    private var keyHeight = 0
    private var page = 1
    private val mid: Int = 0
    private var type: Int = 0
    private var item_id: Int = 0
    private var activityRootView: View? = null
    private var emotionMainFragment: EmotionMainFragment? = null
    private var commentSuccessReceiver: CommentSuccessReceiver? = null
    private var filter: IntentFilter? = null
    internal lateinit var interfaceRefreshLoadMore: InterfaceRefreshLoadMore
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun initData() {
        val params = HttpParams()
        params.put("type", type)
        params.put("item_id", item_id.toString() + "")
        params.put("p", page.toString() + "")
        params.put("row", 20.toString() + "")
        params.put("order", "create_time-desc")
        NetWork.getCommentList(page == 1, this, params, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                swipe_refresh.isRefreshing = false
                interfaceRefreshLoadMore.setStopRefreshing()
                val comment = JSON.parseObject(resultData, Comment::class.java)
                if (comment != null) {
                    val data = comment.data
                    if (data != null && data.isNotEmpty()) {
                        if (recycler_comment.scrollState === RecyclerView.SCROLL_STATE_IDLE && recycler_comment.isComputingLayout === false) {
                            adapter.removeAllFooterView()
                        }
                        if (page == 1) {
                            dataList.clear()
                        }
                        dataList.addAll(data)
                        adapter.setNewData(dataList)
//                        if (recycler_comment.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (recycler_comment.isComputingLayout() == false)) {
//                            adapter.notifyDataSetChanged()
//                            adapter.setNewData(dataList)
//                        }
                        adapter.loadMoreComplete()
                    } else {
                        if (page == 1) {
                            if (adapter.footerLayoutCount == 0) {
                                adapter.addFooterView(LayoutInflater.from(this@CommentActivity).inflate(R.layout.no_comment_layout, null))
                                swipe_refresh.setLoadMore(false)
                            }
                        } else {
                            adapter.loadMoreEnd()
                            adapter.setEnableLoadMore(false)
                            if (adapter.footerLayoutCount == 0) {
                                adapter.addFooterView(LayoutInflater.from(this@CommentActivity).inflate(R.layout.no_more_data_text, null))
                            }
                        }
                    }
                }
            }

            override fun doError(msg: String) {
                interfaceRefreshLoadMore.setStopRefreshing()
                hideLoadingView()
            }

            override fun doResult() {

            }
        })
    }

    public override fun setLayoutId(): Int {
        return R.layout.activity_comment
    }

    private var biaoshi: String? = ""
    public override fun initView() {
        registerCommentReceiver()
        //1:音乐 2:歌单 3:圈子 4:评论
        hideTitle(true)
        val intent = intent
        if (intent != null) {
            type = intent.getIntExtra(TYPE, 0)
            item_id = intent.getIntExtra(ITEM_ID, 0)

            if (null != getIntent().extras) {
                val i = getIntent().extras
                if (null != i!!.getString("biaoshi")) {
                    biaoshi = i.getString("biaoshi")//获取数据包
                }
            }

            initEmotionMainFragment()
            if (type == 1) {
                tv_itemType.text = "单曲"
                Log.e(TAG, "initView: $item_id")
                GetMusicInfo.getMusicDetial(this, item_id, object : GetMusicInfo.SetBeanData {
                    override fun setBeanData(dataBean: MusicInfo.DataBean?) {
                        if (dataBean != null) {
                            tv_songName.text = dataBean.title
                            singer_song.setText(dataBean.nickname)
                            try {
                                ImageLoader.with(this@CommentActivity)
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
                    PlayCtrlUtil.play(this@CommentActivity, item_id, 0)

                }
//                RxView.clicks(finish).throttleFirst(2, TimeUnit.SECONDS).subscribe {
//                    finish()
//                }
            } else if (type == 2) {
                tv_itemType.text = "歌单"
                RxView.clicks(ll_music).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                    val intent = Intent(this@CommentActivity, SongSheetDetailsActivity::class.java)
                    intent.putExtra(SONG_SHEET_ID, item_id.toString() + "")
                    startActivity(intent)
                }
                PlayCtrlUtil.getSheetDetail(this, item_id.toString(), object : PlayCtrlUtil.SheetDetialCallBack {
                    override fun getSheetDetial(sheetDetialBean: SheetDetialBean) {
                        if (sheetDetialBean != null && sheetDetialBean.data.size > 0) {
                            tv_songName.text = sheetDetialBean.data[0].title
                            singer_song.text = sheetDetialBean.data[0].nickname
                            try {
                                ImageLoader.with(this@CommentActivity)
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
        }
        RxView.clicks(finish).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            finish()
        }
        activityRootView = findViewById(R.id.root_layout)
        //获取屏幕高度
        screenHeight = windowManager.defaultDisplay.height
        keyHeight = screenHeight / 3
    }

    public override fun initEvent() {
        adapter = CommentAdapte(1, dataList, getSupportFragmentManager())
        recycler_comment.layoutManager = LinearLayoutManager(this)
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        adapter.setEnableLoadMore(true)
        recycler_comment.adapter = adapter
        adapter.setOnItemClickListener { _, _, position ->
            val detialBean = dataList[position]
            if (detialBean != null) {
                Log.e(TAG, "onItemClick: " + detialBean.nickname!!)
                val intent = Intent(this@CommentActivity, CommenDetialActivity::class.java)
                val bundle = Bundle()
                bundle.putInt("item_id", detialBean.item_id)
                bundle.putInt("fid", detialBean.id)
                bundle.putInt("type", type)
                bundle.putInt("pid", detialBean.id)
                bundle.putSerializable("parentbean", detialBean)
                intent.putExtras(bundle)
                startActivity(intent)
            }
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

        RxView.clicks(btn_comment).throttleFirst(3, TimeUnit.SECONDS).subscribe {
            Log.e(TAG, "onViewClicked: $item_id")
            NetWork.addComment(this@CommentActivity, 1, item_id, et_pinglun.getText().toString(), 0, 0, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    setSnackBar("评论成功", "", R.drawable.icon_success)
                    val commentBean: Comment.DataBean
                    val jsonObject1 = JSON.parseObject(resultData)
                    commentBean = jsonObject1.getObject("data", Comment.DataBean::class.java)
                    et_pinglun.setText("")
                    //                        detialBean.setComment(detialBean.getComment() + 1);
                    dataList.add(0, commentBean)
                    adapter.notifyDataSetChanged()
                    if (recycler_comment.getScrollState() === RecyclerView.SCROLL_STATE_IDLE && recycler_comment.isComputingLayout() === false) {
                        adapter.removeAllFooterView()
                    }
                }

                override fun doError(msg: String) {

                }

                override fun doResult() {

                }
            })
        }
    }

    public override fun onResume() {
        super.onResume()
    }

    public override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        interfaceRefreshLoadMore.resetRefreshView()
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
        bundle.putInt(COMMENT_TYPE, type)
        bundle.putInt(COMMENT_ITEM_ID, item_id)
        bundle.putInt(COMMENT_PID, 0)
        bundle.putInt(COMMENT_FID, 0)
        //替换fragment
        //创建修改实例
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment::class.java, bundle)
        emotionMainFragment!!.bindToContentView(et_pinglun)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment)
        transaction.addToBackStack(null)
        //提交修改
        transaction.commit()
    }

    override fun onBackPressed() {
        /**
         * 判断是否拦截返回键操作
         */
        if (!emotionMainFragment!!.isInterceptBackPress) {
            finish()
        }
    }

    private fun registerCommentReceiver() {
        commentSuccessReceiver = CommentSuccessReceiver(object : CommentSuccessReceiver.SuccessCallBack {
            override fun callBack(json: String, intent: Intent) {
                adapter.removeAllFooterView()
                setSnackBar("发表成功", "", R.drawable.icon_success)
                if (intent.getIntExtra(COMMENT_PID, 0) == 0) {
                    val commentBean: Comment.DataBean
                    val jsonObject1 = JSON.parseObject(json)
                    commentBean = jsonObject1.getObject("data", Comment.DataBean::class.java)
                    dataList.add(0, commentBean)
                    adapter.setNewData(dataList)
                } else {
                    val pid = intent.getIntExtra(COMMENT_PID, 0)
                    val jsonObject1 = JSON.parseObject(json)
                    if (jsonObject1 != null) {
                        val data = jsonObject1.getJSONObject("data")
                        val jsonString = data.toJSONString()
                        val sonBean = JSON.parseObject(jsonString, Comment.DataBean.SonBean::class.java)
                        for (i in dataList.indices) {
                            val dateBean = dataList[i]
                            if (dateBean.id == sonBean!!.fid) {
                                var son: ArrayList<Comment.DataBean.SonBean>? = dateBean.son
                                if (son == null) {
                                    son = ArrayList<Comment.DataBean.SonBean>()
                                }
                                if (sonBean != null) {
                                    son.add(0, sonBean)
                                    for (i1 in 0 until son.size - 1) {
                                        for (j in son.size - 1 downTo i1 + 1) {
                                            if (son[j].id == son[i1].id) {
                                                son.removeAt(j)
                                            }
                                        }
                                    }
                                }
                                dateBean.son = son
                                dataList[i] = dateBean
                                adapter.notifyDataSetChanged()
                                break
                            }
                        }
                    }
                }
            }
        })
        filter = IntentFilter(COMMENT_SUCCESS)
        registerReceiver(commentSuccessReceiver, filter)
    }

    companion object {
        const val TYPE = "type"
        const val ITEM_ID = "item_id"
    }
}
