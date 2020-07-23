package com.mxkj.yuanyintang.mainui.home.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.View

import com.alibaba.fastjson.JSON
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.extraui.activity.PictureDetailsActivity
import com.mxkj.yuanyintang.im.EaseConstant
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.home.adapter.SlidingFragmentViewPager
import com.mxkj.yuanyintang.mainui.home.bean.MusicIanDetailsBean
import com.mxkj.yuanyintang.mainui.home.fragment.HomepageFragment
import com.mxkj.yuanyintang.mainui.home.fragment.MusicFragment
import com.mxkj.yuanyintang.mainui.home.fragment.PondFragment
import com.mxkj.yuanyintang.mainui.home.fragment.SongSheetListFragment
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.message.ChatActivity
import com.mxkj.yuanyintang.mainui.myself.activity.EditPersonalProfileActivity
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.dialog.TitleOperationDialog
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.rxbus.RxBus
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber
import com.mxkj.yuanyintang.utils.rxbus.event.MusicIanEvent
import com.mxkj.yuanyintang.utils.rxbus.event.PlayerMusicRefreshDataEvent
import com.mxkj.yuanyintang.widget.Panel

import org.json.JSONArray
import org.json.JSONObject

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import butterknife.ButterKnife
import com.mxkj.yuanyintang.base.bean.UserInfo
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil
import com.umeng.analytics.MobclickAgent
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import okhttp3.Headers
import kotlinx.android.synthetic.main.activity_musician_details.*

/**
 * 音乐人详情~
 */
class MusicIanDetailsActivity : StandardUiActivity(), Panel.OnPanelListener {
    var is_self:String = null.toString()
    var uInfo: UserInfo? = null
    private lateinit var musicianId: String
    private var currentItem: Int = 0
    internal var strings: MutableList<String> = ArrayList()
    private var fragments: MutableList<Fragment> = ArrayList()
    lateinit var musicIanDetailsBean: MusicIanDetailsBean
    private lateinit var homepageFragment: HomepageFragment
    private lateinit var musicFragment: MusicFragment
    private lateinit var pondFragment: PondFragment
    private lateinit var songSheetListFragment: SongSheetListFragment
    private var musicIanEvent: Disposable? = null
    private var playerMusicEvent: Disposable? = null
    private lateinit var slidingFragmentViewPager: SlidingFragmentViewPager
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun setLayoutId(): Int {
        StatusBarUtil.baseTransparentStatusBar(this)
        return R.layout.activity_musician_details
    }

    override fun initView() {
        musicianId = intent.getStringExtra(MUSICIAN_ID)
        currentItem = intent.getIntExtra(MUSICIAN_CURRENT_ITEM, 0)
        ButterKnife.bind(this@MusicIanDetailsActivity)
        hideTitle(true)
        initViewPager()
        initViewHeight()
    }

    private fun initViewHeight() {
        app_bar.addOnOffsetChangedListener({ appBarLayout, verticalOffset ->
            val scrollRangle = appBarLayout.totalScrollRange
            //初始verticalOffset为0，不能参与计算。
            if (verticalOffset == 0) {
                tv_title.alpha = 0.0f
                layout_head.alpha = 1.0f
            } else {
                //保留一位小数
                val alpha = (Math.abs(Math.round(1.0f * verticalOffset / scrollRangle) * 10) / 10).toFloat()
                tv_title.alpha = alpha
                layout_head.alpha = 1.0f - alpha
            }
        })
        RxView.clicks(iv_back).throttleFirst(2, TimeUnit.SECONDS).subscribe { finish() }
        RxView.clicks(iv_more).throttleFirst(2, TimeUnit.SECONDS).subscribe({
            MobclickAgent.onEvent(this@MusicIanDetailsActivity, "musician_more");
            uInfo?.let {
                val shareDataBean = MusicBean.ShareDataBean()
                shareDataBean.type = "musician"
                shareDataBean.nickname = uInfo?.data?.nickname


                shareDataBean.is_self = uInfo?.data?.is_self.toString()

                shareDataBean.topicContent = if (it.data?.signature == null) "这个人很懒，什么都没有留下哦" else it.data?.signature
                shareDataBean.title = it.data?.nickname + ""
                shareDataBean.muisic_id = it.data?.id!!
                shareDataBean.webImgUrl = it.data?.head_link
                val shareUrl = "https://www.yuanyintang.com/singer/" + it.data?.id//第三方分享的歌单链接
                shareDataBean.shareUrl = shareUrl
                val titleOperationDialog = TitleOperationDialog(MusicBean().setMusic_id(it.data?.id.toString() + "").setMusician_name(it.data?.nickname).setMusic_name(it.data?.nickname).setUid(it.data?.id!!).setCommentNum(0).setShareDataBean(shareDataBean).setReportType(2).setReportId(it.data?.id!!).setType(8))
                titleOperationDialog.show(supportFragmentManager, "mTitleOperationDialog")
            }
        })
        RxView.clicks(tv_edit_material).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            MobclickAgent.onEvent(this@MusicIanDetailsActivity, "musician_edit");
            if (null != uInfo) {
                val bundle = Bundle()
                bundle.putSerializable(EditPersonalProfileActivity.DATA, uInfo)
                goActivity(EditPersonalProfileActivity::class.java, bundle)
            }
        }
        RxView.clicks(tv_mail).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            MobclickAgent.onEvent(this@MusicIanDetailsActivity, "musician_chat");

            if (CacheUtils.getBoolean(this@MusicIanDetailsActivity, Constants.User.IS_LOGIN)) {
                uInfo?.let {
                    val bundle = Bundle()
                    val dataBean = it.data
                    bundle.putString(Constants.EaseConstant.EXTRA_USER_ID, dataBean?.id.toString() + "")
                    bundle.putString(Constants.EaseConstant.EXTRA_OTHER_NAME, dataBean?.nickname)
                    bundle.putString(Constants.EaseConstant.EXTRA_OTHER_AVATAR, dataBean?.head_link)
                    bundle.putString(EaseConstant.EXTRA_OTHER_UID, dataBean?.id.toString() + "")
                    bundle.putString(EaseConstant.EXTRA_IS_MUSIC, dataBean?.is_music.toString() + "")
                    bundle.putString(EaseConstant.EXTRA_IS_RELATION, dataBean?.is_relation.toString() + "")
                    bundle.putString(EaseConstant.EXTRA_TO_RELATION, dataBean?.to_relation.toString() + "")
                    bundle.putString(EaseConstant.EXTRA_TO_MUSIC, dataBean?.is_music.toString() + "")
                    goActivity(ChatActivity::class.java, bundle)
                }

            } else {
                goActivity(LoginRegMainPage::class.java)
            }
        }
        RxView.clicks(tv_follow).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            MobclickAgent.onEvent(this@MusicIanDetailsActivity, "musician_follow");

            if (CacheUtils.getBoolean(this@MusicIanDetailsActivity, Constants.User.IS_LOGIN)) {
                getFollow()
            } else {
                goActivity(LoginRegMainPage::class.java)
            }
        }
        RxView.clicks(circle_headimg).throttleFirst(1, TimeUnit.SECONDS).subscribe(Consumer {
            if (null == uInfo) {
                return@Consumer
            }
            val intent = Intent(this@MusicIanDetailsActivity, PictureDetailsActivity::class.java)
            intent.putExtra("url", uInfo?.data?.head_link)
            startActivity(intent)
        })
    }

    private fun initViewPager() {
        strings.clear()
        strings.add("主页")
        strings.add("作品")
        strings.add("歌单")
        strings.add("池塘")

        val bundle = Bundle()
        bundle.putString("id", musicianId)

        homepageFragment = HomepageFragment()
        homepageFragment.arguments = bundle

        musicFragment = MusicFragment()
        musicFragment.arguments = bundle

        songSheetListFragment = SongSheetListFragment()
        songSheetListFragment.arguments = bundle

        pondFragment = PondFragment()
        pondFragment.arguments = bundle
        fragments.clear()
        fragments.add(homepageFragment)
        fragments.add(musicFragment)
        fragments.add(songSheetListFragment)
        fragments.add(pondFragment)
        slidingFragmentViewPager = SlidingFragmentViewPager(supportFragmentManager, strings, fragments, this)
        viewpager.adapter = slidingFragmentViewPager
        tabs.setViewPager(viewpager)
        viewpager.currentItem = currentItem
        viewpager.offscreenPageLimit = 4
        viewpager.currentItem = currentItem
        tabs.updateTabSelection(currentItem)

    }

    override fun initData() {
//        Log.e("musicianId", musicianId)
//        TODO  相似功能的接口太多了
        if (CacheUtils.getBoolean(this, Constants.User.IS_LOGIN)) {
            UserInfoUtil.getUserInfoById(0, this) { selfInfo ->
                if (selfInfo?.data?.id == musicianId.toInt()) {
                    layout_follow.visibility = View.GONE
                    tv_edit_material.visibility = View.VISIBLE
                } else {
                    layout_follow.visibility = View.VISIBLE
                    tv_edit_material.visibility = View.GONE
                }
            }
        } else {
            layout_follow.visibility = View.VISIBLE
            tv_edit_material.visibility = View.GONE
        }
        NetWork.getMusicIanInfo("musiclanDetail_" + musicianId, this, musicianId, 0L, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                uInfo = JSON.parseObject(resultData, UserInfo::class.java)
                refreshDataInfo()
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })

//        NetWork.getMusicianDetails(this, musicianId, 15, object : NetWork.TokenCallBack {
//            override fun doNext(resultData: String, headers: Headers?) {
//                var jObj = JSONObject(resultData)
//                val dataObject = if (jObj?.optJSONObject("data") == null) JSONObject() else jObj.optJSONObject("data")
//                musicIanDetailsBean = JSON.parseObject(dataObject.toString(), MusicIanDetailsBean::class.java)
//
//                val songArray = if (dataObject.optJSONArray("song") == null) JSONArray() else dataObject.optJSONArray("song")
//                val songBeanList = JSON.parseArray(songArray.toString(), MusicIanDetailsBean.SongBean::class.java)
//                musicIanDetailsBean.song = songBeanList
//
//                val releaseArray = if (dataObject.optJSONArray("release") == null) JSONArray() else dataObject.optJSONArray("release")
//                val releaseBeanList = JSON.parseArray(releaseArray.toString(), MusicIanDetailsBean.ReleaseBean::class.java)
//                musicIanDetailsBean.release = releaseBeanList
//
//
//                val collectionArray = if (dataObject.optJSONArray("collection") == null) JSONArray() else dataObject.optJSONArray("collection")
//                val collectionBeanList = JSON.parseArray(collectionArray.toString(), MusicIanDetailsBean.CollectionBean::class.java)
//                musicIanDetailsBean.collection = collectionBeanList
//
//                val topicArray = if (dataObject.optJSONArray("topic") == null) JSONArray() else dataObject.optJSONArray("topic")
//                val topicBeanList = JSON.parseArray(topicArray.toString(), MusicIanDetailsBean.TopicBean::class.java)
//                musicIanDetailsBean.topic = topicBeanList
//
//                val boxObject = if (dataObject.optJSONObject("box") == null) JSONObject() else dataObject.optJSONObject("box")
//                val boxBean = JSON.parseObject(boxObject.toString(), MusicIanDetailsBean.BoxBean::class.java)
//                musicIanDetailsBean.box = boxBean
//            }
//
//            override fun doError(msg: String) {}
//
//            override fun doResult() {}
//        })
    }

    @SuppressLint("SetTextI18n")
    private fun refreshDataInfo() {
        uInfo?.let {
            tv_title.text = StringUtils.isEmpty(it.data?.nickname)
            tv_name.text = StringUtils.isEmpty(it?.data?.nickname)
//            tabs.getTitleView(1).text = "音乐("+it?.data.release_num+"首)"

            when {
                it.data?.sex == 1 -> {
                    iv_sex.setImageResource(R.drawable.icon_male)
                    iv_sex.background = ContextCompat.getDrawable(this, R.drawable.oval_3dp_blue_60_bg)
                    iv_sex.visibility = View.VISIBLE
                }
                it.data?.sex == 0 -> {
                    iv_sex.setImageResource(R.drawable.icon_female)
                    iv_sex.background = ContextCompat.getDrawable(this, R.drawable.oval_3dp_pink_99_bg)
                    iv_sex.visibility = View.VISIBLE
                }
                else -> iv_sex.visibility = View.GONE
            }

            iv_is_vip.visibility = if (it.data?.is_music == 3) View.VISIBLE else View.GONE
            tv_follow_num.text = it.data?.attention_num?.let { StringUtils.setNum(it) } + "关注"
            tv_fans_num.text = it.data?.fans_num?.let { StringUtils.setNum(it) } + "粉丝"
            tv_popularity_num.text = it.data?.ips_num?.let { StringUtils.setNum(it) } + "人气"
            getFollowView()
            it.data?.head_link?.let {
                ImageLoader.with(this).override(80, 80).getSize(140, 140).url(uInfo?.data?.head_link).scale(ScaleMode.CENTER_CROP).asCircle().into(circle_headimg)
                ImageLoader.with(this).getSize(750, 445).url(uInfo?.data?.head_link).blur(25).scale(ScaleMode.CENTER_CROP).into(imageview)
            }


            Handler().postDelayed({ homepageFragment.setData(it.data) }, 500)
        }
    }

    override fun initEvent() {
        musicIanEvent = RxBus.getDefault().toObservable(MusicIanEvent::class.java).subscribeWith(object : RxBusSubscriber<MusicIanEvent>() {
            @Throws(Exception::class)
            override fun onEvent(musicIanEvent: MusicIanEvent) {
                when (musicIanEvent.type) {
                    "refresh" -> initData()
                }
            }
        })
        RxBus.getDefault().add(musicIanEvent)
        playerMusicEvent = RxBus.getDefault().toObservable(PlayerMusicRefreshDataEvent::class.java).subscribeWith(object : RxBusSubscriber<PlayerMusicRefreshDataEvent>() {
            @Throws(Exception::class)
            override fun onEvent(playerMusicRefreshDataEvent: PlayerMusicRefreshDataEvent) {
                musicFragment.refreshData()
            }
        })
        RxBus.getDefault().add(playerMusicEvent)
    }

    override fun onDestroy() {
        super.onDestroy()
        fragments.clear()
        MobclickAgent.onEvent(this@MusicIanDetailsActivity, "musician_back");

        RxBus.getDefault().remove(musicIanEvent)
        RxBus.getDefault().remove(playerMusicEvent)
    }

    override fun onResume() {
        super.onResume()
        slidingFragmentViewPager.notifyDataSetChanged()
    }

    private fun getFollow() {
        showLoadingView()
        NetWork.getRelationFollow(this, musicianId + "", object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                hideLoadingView()
                uInfo?.let {
                    if (it.data?.is_relation == 0) {
                        it.data?.is_relation = 1
                        it.data?.fans_num = it.data?.fans_num!! + 1
                        sendHelloMsg()
                    } else {
                        it.data?.is_relation = 0
                        it.data?.fans_num = it.data?.fans_num!! - 1
                    }
                    getFollowView()
                }

            }

            override fun doError(msg: String) {
                hideLoadingView()
            }

            override fun doResult() {

            }
        })
    }

    private fun sendHelloMsg() {
    }

    private fun getFollowView() {
        if (tv_follow == null || uInfo == null || null == tv_follow_num) {
            return
        }
        var followDrawable: Drawable? = if (uInfo?.data?.is_relation == 0) {
            tv_follow.text = "    关注"
            ContextCompat.getDrawable(this, R.drawable.icon_musician_add)
        } else {
            tv_follow.text = "已关注"
            ContextCompat.getDrawable(this, R.drawable.icon_follow_concern)
        }
        if (null != followDrawable) {
            followDrawable.setBounds(0, 0, followDrawable.minimumWidth, followDrawable.minimumHeight)
            tv_follow.setCompoundDrawables(followDrawable, null, null, null)
        }
        tv_fans_num.text = StringUtils.setNum(uInfo?.data?.fans_num!!) + "粉丝"
    }

    fun addListener(listener: AppBarLayout.OnOffsetChangedListener) {
        if (app_bar != null) app_bar.addOnOffsetChangedListener(listener)
    }

    fun removeListener(listener: AppBarLayout.OnOffsetChangedListener) {
        if (app_bar != null) app_bar.removeOnOffsetChangedListener(listener)
    }


    override fun onPanelClosed(panel: Panel) {

    }

    override fun onPanelOpened(panel: Panel) {

    }

    companion object {
        const val MUSICIAN_ID = "_uid"
        const val MUSICIAN_CURRENT_ITEM = "_musician_current_item"
    }
}
