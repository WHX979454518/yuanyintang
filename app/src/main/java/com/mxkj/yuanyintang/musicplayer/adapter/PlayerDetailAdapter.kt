package com.mxkj.yuanyintang.musicplayer.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.View.GONE
import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.home.adapter.*
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.home.data.Constant
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseMultiItemQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.mainui.home.utils.GridSpacingItemDecoration
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.message.activity.MyCollectionSongListActivity
import com.mxkj.yuanyintang.mainui.newapp.OriginalMusicListAdapter
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean
import com.mxkj.yuanyintang.mainui.newapp.pond.PondAdapter
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils

import com.mxkj.yuanyintang.utils.photopicker.widget.NoScrollRecyclerView
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.uiutils.Toast
import com.mxkj.yuanyintang.widget.ExpandableTextView
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.fragment_more_song.*
import java.util.ArrayList

class PlayerDetailAdapter : BaseMultiItemQuickAdapter<MusicIndex.ItemInfoListBean, BaseViewHolder> {
    private var context: Context? = null
    private var fragmentManager: FragmentManager? = null
    private var musicDetailsLikeAdapter: SongSheetRecommendListAdapter? = null
    private var musicId = "0"
    var likesSongSheet: List<MusicIndex.ItemInfoListBean.MusicBean> = ArrayList()
    private var musicDetailsSongAdapter: OriginalMusicListAdapter? = null


    constructor(context: Context, fragmentManager: FragmentManager, id: String, musicIanId: Int) {
        this.context = context
        this.musicId = id
        this.fragmentManager = fragmentManager
        initView()
    }

    constructor(context: Context, fragmentManager: FragmentManager, isEdit: Boolean) {
        this.context = context
        this.fragmentManager = fragmentManager
        initView()
    }

    private fun initView() {
        addItemType(Constant.MUSIC_TYPE_SYNOPSIS, R.layout.musicrecycle_item_synopsis)
        addItemType(Constant.MUSIC_TYPE_SONG, R.layout.musicrecycle_item_song)
        addItemType(Constant.MUSIC_TYPE_POND, R.layout.musicrecycle_item_pond)
        addItemType(Constant.MUSIC_TYPE_RECOMMEND_SONG, R.layout.musicrecycle_item_recommend)
        addItemType(Constant.MUSIC_TYPE_RELATED_PARTNER, R.layout.musicrecycle_item_partner_list)
    }

    override fun convert(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        /*初始化一次数据，统计一次*/
        MobclickAgent.onEvent(mContext,"player_left_album")
        MobclickAgent.onEvent(mContext,"player_left_music")
        MobclickAgent.onEvent(mContext,"player_left_pond")

        when {
            "synopsis" == item.itemType -> bindSynopsisData(helper, item, position)
            "relatedPartnerView" == item.itemType -> bindPartnerList(helper, item, position)
            "song" == item.itemType -> bindSongData(helper, item, position)
            "recommendSong" == item.itemType -> bindRecommendSongData(helper, item, position)
            "pond" == item.itemType -> bindPondData(helper, item, position)
        }
    }

    private fun bindSynopsisData(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        helper.getView<ExpandableTextView>(R.id.expand_text_view).text = StringUtils.isEmpty(item.intro)
    }

    private fun bindPartnerList(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        val member = item.member
        val partnerView = helper.getView<NoScrollRecyclerView>(R.id.parterView)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        partnerView.layoutManager = linearLayoutManager
        val partnerDataAdapter = PartnerDataAdapter(member)
        partnerView.adapter = partnerDataAdapter
    }

    private fun bindSongData(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        helper.getView<View>(R.id.tv_add_song).setOnClickListener(View.OnClickListener {
            val intent = Intent()
            if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN, false)) {
                if (TextUtils.isEmpty(item.id.toString() + "")) {
                    Toast.create(mContext).show("没有音乐ID~")
                    return@OnClickListener
                }
                val bundle = Bundle()
                bundle.putString("music_id", musicId)
                bundle.putBoolean(MyCollectionSongListActivity.IS_MULTI_SELECT, false)
                bundle.putString(MyCollectionSongListActivity.VIEW_TYPE, "addMusic")
                intent.putExtras(bundle)
                intent.setClass(mContext, MyCollectionSongListActivity::class.java)
            } else {
                intent.setClass(mContext, LoginRegMainPage::class.java)
            }
            mContext.startActivity(intent)
        })
        val recyclerView = helper.getView<RecyclerView>(R.id.song_recyclerview) as NoScrollRecyclerView
        recyclerView.isNestedScrollingEnabled = false
        if (item.song_all.size > 0) {
            helper.setVisible(R.id.tv_icon, false)
            helper.setVisible(R.id.tv_add_song, false)
            helper.setVisible(R.id.song_recyclerview, true)
        } else {
            helper.getView<View>(R.id.ll_like).visibility = GONE
            helper.setVisible(R.id.tv_icon, false)
            helper.setVisible(R.id.tv_add_song, false)
            helper.setVisible(R.id.song_recyclerview, false)
            return
        }
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        if (null == recyclerView.adapter) {
            val array = JSON.parseArray(JSON.toJSONString(item.song_all), MusicIndex.ItemInfoListBean.RecommendBean::class.java)
            musicDetailsLikeAdapter = SongSheetRecommendListAdapter(array)
            recyclerView.addItemDecoration(GridSpacingItemDecoration(3, CommonUtils.dip2px(mContext, 10f), CommonUtils.dip2px(mContext, 10f), false))
            recyclerView.adapter = musicDetailsLikeAdapter
        } else {
            musicDetailsLikeAdapter?.notifyDataSetChanged()
        }

    }

    private fun bindPondData(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        val recyclerView = helper.getView<RecyclerView>(R.id.pond_recyclerview) as NoScrollRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val musicDetailsPondAdapter = PondAdapter(item.pondList)
        recyclerView.adapter = musicDetailsPondAdapter
    }

    private fun bindRecommendSongData(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        likesSongSheet = if (item.music == null) ArrayList() else item.music
        val array = JSON.parseArray(JSON.toJSONString(likesSongSheet), HomeBean.TypeMusicListBean.TypeMusicBean.MusicBean::class.java)
        val recyclerView = helper.getView<RecyclerView>(R.id.song_sheet_recyclerview) as NoScrollRecyclerView
        val myRecyclerDetorration = MyRecyclerDetorration(mContext, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(mContext, 5f), R.color.white, false)
        recyclerView.addItemDecoration(myRecyclerDetorration)
        recyclerView.layoutManager = LinearLayoutManager(context)
        if (null == recyclerView.adapter) {
            musicDetailsSongAdapter = OriginalMusicListAdapter(array, fragmentManager!!)
            recyclerView.adapter = musicDetailsSongAdapter
        } else {
            recyclerView.adapter.notifyDataSetChanged()
        }
    }

}
