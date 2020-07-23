package com.mxkj.yuanyintang.mainui.pond.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.jakewharton.rxbinding2.view.RxView
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.mainui.comment.Comment
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.dynamic.adapter.ReplyImgAdapter
import com.mxkj.yuanyintang.mainui.dynamic.widget.NoScrollListview
import com.mxkj.yuanyintang.mainui.emotionkeyboard.fragment.EmotionMainFragment
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.pond.adapter.PondReplySonCommentAdapte
import com.mxkj.yuanyintang.mainui.pond.bean.PondCommentBean
import com.mxkj.yuanyintang.mainui.pond.bean.PondDetialBean
import com.mxkj.yuanyintang.mainui.pond.bean.PondReplyDetialBean
import com.mxkj.yuanyintang.musicplayer.play_control.GetMusicInfo
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.extraui.dialog.Del_or_reply_Dialog_PongDetial
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.widget.NoScrollGridview
import com.mxkj.yuanyintang.widget.StretchyTextView

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import butterknife.ButterKnife
import butterknife.OnClick
import okhttp3.Headers

import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_FID
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_PID
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_SUCCESS
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_SUCCESS_JSON
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_TYPE
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.IS_POND
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_pond_reply_detial.*
import kotlinx.android.synthetic.main.pond_reply_detial_head.*

class PondReplyDetialActivity : StandardUiActivity() {
    internal lateinit var imgPond: ImageView

    internal lateinit var tvPondName: TextView
    internal lateinit var tvPondContent: TextView
    internal lateinit var ll_to_pond: LinearLayout
    private var finishfinish : ImageView? =null
    private var headView: View? = null
    private var tv_time: TextView? = null
    private var tv_agree_num: TextView? = null
    private var item_type: TextView? = null
    private var singer_song: TextView? = null
    private var tv_songName: TextView? = null
    private var username: TextView? = null
    private var tv_from_share: TextView? = null
    private val tv_follow: TextView? = null
    private var img_agree: ImageView? = null
    private var img_playmusic: ImageView? = null
    private var img_icon: ImageView? = null
    private var v_rz: ImageView? = null
    private var img_song: ImageView? = null
    private var ll_agree: LinearLayout? = null
    private var music_rl: RelativeLayout? = null
    private var ll_music: LinearLayout? = null
    private var content: StretchyTextView? = null
    private var dynamic_img_grid: NoScrollGridview? = null
    private val list_grid = ArrayList<String>()
    private val dataList = ArrayList<Comment.DataBean>()
    //    private DynamicImgGridAdapter adapterDynamic;
    private var page = 1
    private var commentAdapte: PondReplySonCommentAdapte? = null
    private var dataBean: PondCommentBean.DataBean? = null
    private var emotionMainFragment: EmotionMainFragment? = null
    private var pondCommentId: Int = 0
    internal lateinit var interfaceRefreshLoadMore: InterfaceRefreshLoadMore
    private var pondReceiver: UpdateCommentReceiver? = null
    private var pondFilter: IntentFilter? = null
    private var imglist_link: ArrayList<String>? = null
    private var lv_img: NoScrollListview? = null
    private var pondId: Int = 0
    private var tv_num: TextView? = null

    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun setLayoutId(): Int {
        return R.layout.activity_pond_reply_detial
    }

    override fun initView() {
//        ButterKnife.bind(this@PondReplyDetialActivity)
        hideTitle(true)
        val intent = intent
        if (intent != null) {
            //            dataBean = (PondCommentBean.DataBean) intent.getSerializableExtra("pond_commentBean");
            pondCommentId = intent.getIntExtra(POND_COMMENTID, 0)
        }
        initHeadView()


        finishfinish = findViewById(R.id.finishfinish)
        finishfinish!!.setOnClickListener {
            finish()
        }

    }

    private fun initHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.pond_reply_detial_head, null)
        imgPond = headView!!.findViewById(R.id.img_pond)
        tv_num = headView!!.findViewById(R.id.tv_num)
        tvPondName = headView!!.findViewById(R.id.tv_pondname)
        tvPondContent = headView!!.findViewById(R.id.tv_pond_content)
        ll_to_pond = headView!!.findViewById(R.id.ll_to_pond)
        username = headView!!.findViewById(R.id.username)
        singer_song = headView!!.findViewById(R.id.singer_song)
        item_type = headView!!.findViewById(R.id.item_type)
        tv_time = headView!!.findViewById(R.id.tv_time)
        tv_agree_num = headView!!.findViewById(R.id.tv_agree_num)
        tv_songName = headView!!.findViewById(R.id.tv_songName)
        tv_from_share = headView!!.findViewById(R.id.tv_from_share)
        img_icon = headView!!.findViewById(R.id.img_icon)
        img_agree = headView!!.findViewById(R.id.img_agree)
        img_playmusic = headView!!.findViewById(R.id.img_playmusic)
        img_song = headView!!.findViewById(R.id.img_song)
        v_rz = headView!!.findViewById(R.id.v_rz)
        ll_agree = headView!!.findViewById(R.id.ll_agree)
        music_rl = headView!!.findViewById(R.id.music_rl)
        ll_music = headView!!.findViewById(R.id.ll_music)
        content = headView!!.findViewById(R.id.content)
        dynamic_img_grid = headView!!.findViewById(R.id.dynamic_img_grid)
    }

    override fun initData() {
        initBaseInfo()
    }

    private fun registePondCommentReceiver() {
        pondFilter = IntentFilter()
        pondFilter!!.addAction(COMMENT_SUCCESS)
        pondReceiver = UpdateCommentReceiver()
        registerReceiver(pondReceiver, pondFilter)
    }

    override fun initEvent() {
        registePondCommentReceiver()
        commentAdapte = PondReplySonCommentAdapte(pondCommentId, dataList, supportFragmentManager)
        commentAdapte!!.addHeaderView(headView)
        recycler_pinglun!!.setItemViewCacheSize(50)
        recycler_pinglun!!.layoutManager = LinearLayoutManager(this)
        recycler_pinglun!!.adapter = commentAdapte
        interfaceRefreshLoadMore = InterfaceRefreshLoadMore(swipe_refresh, this@PondReplyDetialActivity, object : InterfaceRefreshLoadMore.RefreshLoadMoreCallback {
            override fun onRefresh() {
                page = 1
                getSonCommentList()
            }

            override fun onLoadMore() {
                page++
                getSonCommentList()
            }

            override fun onPushDistance(distance: Int) {

            }

            override fun onPullDistance(distance: Int) {

            }
        })

    }

    @OnClick(R.id.finishfinish, R.id.more_menu, R.id.et_pinglun, R.id.img_show_emoji, R.id.btn_comment)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.finishfinish -> finish()
            R.id.more_menu -> {
                if (!CacheUtils.getBoolean(this@PondReplyDetialActivity, Constants.User.IS_LOGIN, false)) {
                    goActivity(LoginRegMainPage::class.java)
                    return
                }
                val titleOperationDialog = Del_or_reply_Dialog_PongDetial(this, dataBean!!, null, object : Del_or_reply_Dialog_PongDetial.ReplySuccessCallback {
                    override fun onSuccess(data: List<PondCommentBean.DataBean>?) {

                    }
                })
                val supportFragmentManager = supportFragmentManager
                titleOperationDialog.show(supportFragmentManager, "mTitleOperationDialog")
            }
            R.id.et_pinglun -> {
            }
            R.id.img_show_emoji -> {
            }
            R.id.btn_comment -> {
            }
        }
    }

    /**
     * 池塘主要信息
     */
    private fun initBaseInfo() {
        NetWork.getPondReplyDetialInfo(this@PondReplyDetialActivity, pondCommentId, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                Log.e(TAG, "池塘回复详情: $resultData")
                val pondReplyDetialBean = JSON.parseObject(resultData, PondReplyDetialBean::class.java)
                val data = pondReplyDetialBean.data
                val s = JSON.toJSONString(data)
                dataBean = JSON.parseObject(s, PondCommentBean.DataBean::class.java)
                pondId = dataBean!!.pid
                initPondDetial()
                initEmotionMainFragment()
                initHeaderEvent()
                getSonCommentList()
            }

            override fun doError(msg: String) {}

            override fun doResult() {}
        })
    }

    private fun initPondDetial() {
        NetWork.getPondDetialInfo(this@PondReplyDetialActivity, pondId, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                Log.e(TAG, "池塘详情: $resultData")
                hideLoadingView()
                val pondDetialBeans = JSON.parseObject(resultData, PondDetialBean::class.java)
                val pondDetialBean = pondDetialBeans.data ?: return
                if (pondDetialBean.imglist_info != null && pondDetialBean.imglist_info.size > 0) {
                    ImageLoader.with(MainApplication.application)
                            .getSize(200, 200)
                            .override(40, 40)
                            .url(pondDetialBean.imglist_info[0].link)
                            .placeHolder(R.drawable.nothing)
                            .error(R.drawable.nothing)
                            .into(imgPond)
                } else {
                    imgPond.visibility = View.GONE
                }
                tvPondName.text = pondDetialBean.title
                tvPondContent.text = pondDetialBean.content
                ll_to_pond.setOnClickListener {
                    val intent = Intent(this@PondReplyDetialActivity, PondDetialActivityNew::class.java)
                    val bundle = Bundle()
                    bundle.putInt(PondDetialActivityNew.PID, pondId)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }

            }

            override fun doError(msg: String) {}

            override fun doResult() {

            }
        })
    }

    private fun initHeaderEvent() {
        if (dataBean != null) {
            if (dataBean!!.sex == 0) {
                username!!.setTextColor(Color.parseColor("#ff8585"))
            } else {
                username!!.setTextColor(Color.parseColor("#4bb6f3"))
            }

            RxView.clicks(img_icon!!).throttleFirst(3, TimeUnit.SECONDS).subscribe {
                val intent = Intent(this@PondReplyDetialActivity, MusicIanDetailsActivity::class.java)
                val bundle = Bundle()
                bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, dataBean!!.uid.toString() + "")
                intent.putExtras(bundle)
                startActivity(intent)
            }
            RxView.clicks(username!!).throttleFirst(3, TimeUnit.SECONDS).subscribe {
                val intent = Intent(this@PondReplyDetialActivity, MusicIanDetailsActivity::class.java)
                val bundle = Bundle()
                bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, dataBean!!.uid.toString() + "")
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tv_time!!.text = dataBean!!.create_time
            username!!.text = if (dataBean!!.nickname == null) "" else dataBean!!.nickname
            tv_agree_num!!.text = dataBean!!.agrees.toString() + ""
            img_agree!!.setImageResource(if (dataBean!!.is_agree == 1) R.mipmap.icon_chitang_zan_pink else R.mipmap.icon_chitang_zan_gray)
            tv_agree_num!!.setTextColor(if (dataBean!!.is_agree == 1)
                ContextCompat.getColor(this@PondReplyDetialActivity, R.color.base_red)
            else
                ContextCompat.getColor(this@PondReplyDetialActivity, R.color.grey_a6_text))
            ll_agree!!.setOnClickListener {
                val params = HttpParams()
                params.put("type", 5.toString() + "")
                params.put("item_id", dataBean!!.id.toString() + "")
                NetWork.agree(this@PondReplyDetialActivity, params, object : NetWork.TokenCallBack {

                    override fun doNext(resultData: String, headers: Headers?) {
                        try {
                            val jsonObject = JSON.parseObject(resultData)
                            val code = jsonObject.getInteger("code")!!
                            if (code == 200) {
                                if (dataBean!!.is_agree == 1) {
                                    img_agree!!.setImageResource(R.mipmap.icon_chitang_zan_gray)
                                    tv_agree_num!!.text = (dataBean!!.agrees - 1).toString() + ""
                                    tv_agree_num!!.setTextColor(ContextCompat.getColor(this@PondReplyDetialActivity, R.color.grey_a6_text))
                                    dataBean!!.is_agree = 0
                                    dataBean!!.agrees = dataBean!!.agrees - 1
                                } else {
                                    img_agree!!.setImageResource(R.mipmap.icon_chitang_zan_pink)
                                    tv_agree_num!!.text = (dataBean!!.agrees + 1).toString() + ""
                                    tv_agree_num!!.setTextColor(ContextCompat.getColor(this@PondReplyDetialActivity, R.color.base_red))
                                    dataBean!!.is_agree = 1
                                    dataBean!!.agrees = 1 + dataBean!!.agrees
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

            /*图片集合*/
//            imglist_link = ArrayList()
//            val imglist_info = dataBean!!.imglist_info
//            if (imglist_info != null && imglist_info.size > 0) {
//                for (i in imglist_info.indices) {
//                    imglist_link!!.add(imglist_info[i].link)
//                }
//            }
//            if (imglist_link!!.size > 0) {
//                lv_img = headView!!.findViewById(R.id.list_img)
//                val adapter = ReplyImgAdapter(this, dataBean, imglist_link!!)
//                lv_img!!.adapter = adapter
//            } else {
//            }
            /*
                * 音乐
                * */
            val music_id = dataBean!!.music_id
            if (music_id != 0) {
                GetMusicInfo.getMusicDetial(this, music_id, object : GetMusicInfo.SetBeanData {
                    override fun setBeanData(dataBean: MusicInfo.DataBean?) {
                        val music_nickname = dataBean!!.nickname
                        val music_title = dataBean.title
                        tv_songName!!.text = music_title
                        singer_song!!.text = music_nickname
                        try {
                            ImageLoader.with(MainApplication.application)
                                    .format(1)
                                    .getSize(200, 200)
                                    .url(dataBean.imgpic_info!!.link)
                                    .into(img_song)
                        } catch (e: RuntimeException) {
                        }


                        ll_music!!.setOnClickListener {
                            PlayCtrlUtil.play(this@PondReplyDetialActivity,music_id,0)
                        }

                        img_playmusic!!.setOnClickListener {
                            PlayCtrlUtil.play(this@PondReplyDetialActivity, music_id, 0)
                        }
                    }
                })
            } else {
                music_rl!!.visibility = View.GONE
                ll_music!!.visibility = View.GONE
            }
            if (dataBean!!.content != null) {
                content!!.setContent(dataBean!!.content)
            }
            ImageLoader.with(this)
                    .override(35, 35)
                    .url(dataBean!!.head_link)
                    .scale(ScaleMode.CENTER_CROP)
                    .asCircle()
                    .into(img_icon)
            if (dataBean!!.is_music == 3) {
                v_rz!!.visibility = View.VISIBLE
            } else {
                v_rz!!.visibility = View.GONE
            }
        }
    }

    private fun getSonCommentList() {
        if (dataBean == null) {
            interfaceRefreshLoadMore.setStopRefreshing()
            return
        }
        val params = HttpParams()
        params.put("p", page.toString() + "")
        params.put("fid", dataBean!!.id.toString() + "")
        params.put("row", 60.toString() + "")
        NetWork.getPondReplySon(this, params, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                if (headers != null) {
                    val s = headers.get("X-Pagination-Total-Count")
                    if (s != null) {
                        tv_num!!.text = "全部评论($s)"
                    }
                }
                interfaceRefreshLoadMore.setStopRefreshing()
                Log.e(TAG, "回复列表: $resultData")
                val comment = JSON.parseObject(resultData, Comment::class.java)
                if (comment != null) {
                    if (page == 1) {
                        dataList.clear()
                    }
                    val data = comment.data
                    dataList.addAll(data!!)
                    //防止刷新的时候奔溃
                    if(recycler_pinglun.getScrollState() === RecyclerView.SCROLL_STATE_IDLE && recycler_pinglun.isComputingLayout === false){
                        commentAdapte!!.setNewData(dataList)
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
        bundle.putInt(COMMENT_PID, 0)
        bundle.putBoolean(IS_POND, true)
        bundle.putInt(COMMENT_FID, dataBean!!.id)
        //替换fragment
        //创建修改实例
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment::class.java, bundle)
        emotionMainFragment!!.bindToContentView(et_pinglun)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment)
        transaction.addToBackStack(null)
        //提交修改
        //       异常处理： https://blog.csdn.net/rflyee/article/details/74723891
        transaction.commit()
    }

    override fun onBackPressed() {
        /**
         * 判断是否拦截返回键操作
         */
        if (emotionMainFragment != null) {
            if (!emotionMainFragment!!.isInterceptBackPress) {
                finish()
            }
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        interfaceRefreshLoadMore.resetRefreshView()
        unregisterReceiver(pondReceiver)
        System.gc()

    }

    private inner class UpdateCommentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null && intent.action != null) {
                when (intent.action) {
                    COMMENT_SUCCESS//池塘评论（pid为0时为评论的回复,否则为评论回复的回复）
                    -> {
                        val stringExtra = intent.getStringExtra(COMMENT_SUCCESS_JSON)
                        val jsonObject = JSON.parseObject(stringExtra)
                        val dataStr = jsonObject.getString("data")
                        val booleanExtra = intent.getBooleanExtra(IS_POND, false)
                        if (booleanExtra) {
                            Log.e(TAG, "onReceive: 评论更新$stringExtra")
                            val dataBean = JSON.parseObject(dataStr, Comment.DataBean::class.java)
                            dataList.add(0, dataBean)
                            //防止刷新的时候奔溃
                            if(recycler_pinglun.getScrollState() === RecyclerView.SCROLL_STATE_IDLE && recycler_pinglun.isComputingLayout === false){
                                commentAdapte!!.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val POND_COMMENTID = "com.mxkj.yuanyintang.ui.pond.comment_id"
    }
}
