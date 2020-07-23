package com.mxkj.yuanyintang.mainui.home.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View

import com.alibaba.fastjson.JSON
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.mainui.home.bean.MemberGiftListBean
import com.mxkj.yuanyintang.mainui.home.bean.MusicGiftListBean
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.home.data.Constant
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseMultiItemQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.mainui.home.utils.GridSpacingItemDecoration
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.message.activity.MyCollectionSongListActivity
import com.mxkj.yuanyintang.mainui.search.SearchResultActivity
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.photopicker.widget.NoScrollRecyclerView
import com.mxkj.yuanyintang.extraui.gift.BotomGiftListDialog
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.uiutils.Toast
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.widget.ExpandableTextView
import com.mxkj.yuanyintang.widget.MultiLineRadioGroup
import com.mxkj.yuanyintang.widget.recyclerview.AutoPollRecyclerView

import java.util.ArrayList

import okhttp3.Headers

import com.mxkj.yuanyintang.mainui.home.fragment.HomeFragment.Companion.SEARCH_WORDS
import com.mxkj.yuanyintang.mainui.newapp.pond.PondAdapter
import com.mxkj.yuanyintang.mainui.search.SearchResultActivity.TO_SHEET_RESULT

class MusicDetailsRecyclerViewAdapter : BaseMultiItemQuickAdapter<MusicIndex.ItemInfoListBean, BaseViewHolder>, BaseQuickAdapter.SpanSizeLookup, BaseQuickAdapter.OnItemChildClickListener {
    private var isEdit: Boolean = false
    private var maxHasLoadPosition: Int = 0
    var song_recyclerview: RecyclerView? = null
    private var autoPollAdapter: AutoPollAdapter? = null
    private var profit_billboard_config: MemberGiftListBean.DataBean.ProfitBillboardConfigBean? = null
    var likesSongSheet: List<MusicIndex.ItemInfoListBean.LikesBean> = ArrayList()

    private var context: Context? = null
    private var songName: String? = null
    private var fragmentManager: FragmentManager? = null
    internal var musicDetailsLikeAdapter: MusicDetailsLikeAdapter? = null//包含该歌曲的歌单
    internal var musicDetailsSongAdapter: MusicDetailsSongAdapter? = null//音乐列表
    var songSheetMusicAdapter: SongSheetMusicListAdapter? = null//音乐列表
    internal var songSheetRecommendListAdapter: SongSheetRecommendListAdapter? = null//推荐歌单
    var multilineGroup: MultiLineRadioGroup? = null
        internal set

    private var musicId = "0"
    private var musicIanId = 0

    val musiListAdapter: SongSheetMusicListAdapter?
        get() = if (null != songSheetMusicAdapter) {
            songSheetMusicAdapter
        } else null

    fun resetMaxHasLoadPosition() {
        maxHasLoadPosition = 0
    }

    fun setSongName(songName: String) {
        this.songName = songName
    }

    constructor(context: Context, fragmentManager: FragmentManager, id: String, musicIanId: Int) {
        this.context = context
        this.musicId = id
        this.musicIanId = musicIanId
        this.fragmentManager = fragmentManager
        initView()
    }

    constructor(context: Context, fragmentManager: FragmentManager, isEdit: Boolean) {
        this.context = context
        this.fragmentManager = fragmentManager
        this.isEdit = isEdit
        initView()
    }

    private fun initView() {
        setSpanSizeLookup(this)
        addItemType(Constant.MUSIC_TYPE_LISTENING, R.layout.musicrecycle_item_listening)
        addItemType(Constant.MUSIC_TYPE_GIFT_LIST, R.layout.musicrecycle_item_gift_list)
        addItemType(Constant.MUSIC_TYPE_RELATED_PARTNER, R.layout.musicrecycle_item_partner_list)
        addItemType(Constant.MUSIC_TYPE_LABEL, R.layout.musicrecycle_item_label)
        addItemType(Constant.MUSIC_TYPE_SYNOPSIS, R.layout.musicrecycle_item_synopsis)
        addItemType(Constant.MUSIC_TYPE_SONG, R.layout.musicrecycle_item_song)
        addItemType(Constant.MUSIC_TYPE_RECOMMEND_SONG, R.layout.musicrecycle_item_recommend_song)
        addItemType(Constant.MUSIC_TYPE_POND, R.layout.musicrecycle_item_pond)
        addItemType(Constant.MUSIC_TYPE_MUSIC, R.layout.musicrecycle_item_music)//歌单详情歌单
        addItemType(Constant.MUSIC_TYPE_RECOMMEND, R.layout.musicrecycle_item_recommend)//歌单详情推荐频道
    }

    override fun getSpanSize(gridLayoutManager: GridLayoutManager, position: Int): Int {
        return 1
    }

    override fun convert(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        if (maxHasLoadPosition < position) {
            maxHasLoadPosition = position
        }
        if ("song" == item.itemType && maxHasLoadPosition <= position) {
            bindSongData(helper, item, position)
        }

        if (isEdit) {
            return
        }
        if ("listening" == item.itemType && maxHasLoadPosition <= position) {
            bindListeningData(helper, item, position)
        } else if ("label" == item.itemType && maxHasLoadPosition <= position) {
            bindLabelData(helper, item, position)
        } else if ("synopsis" == item.itemType && maxHasLoadPosition <= position) {
            bindSynopsisData(helper, item, position)
        } else if ("recommendSong" == item.itemType) {
            bindRecommendSongData(helper, item, position)
        } else if ("pond" == item.itemType) {
            bindPondData(helper, item, position)
        } else if ("music" == item.itemType) {
            bindMusicData(helper, item, position)
        } else if ("recommend" == item.itemType) {//推荐歌单
            bindRecommendData(helper, item, position)
        } else if ("giftItem" == item.itemType) {
            bindGiftList(helper, item, position)
        } else if ("relatedPartnerView" == item.itemType) {
            bindPartnerList(helper, item, position)
        }
    }

    private fun bindPartnerList(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        val member = item.member
        val partnerView = helper.getView<NoScrollRecyclerView>(R.id.parterView)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        partnerView.layoutManager = linearLayoutManager
        val partnerDataAdapter = PartnerDataAdapter(member)
        partnerView.adapter = partnerDataAdapter
    }

    private fun bindGiftList(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        val autoPollRecyclerView = helper.getView<AutoPollRecyclerView>(R.id.auto_pull_recycler)
        helper.setOnClickListener(R.id.tv_give_gift) {
            val giftListDialog = BotomGiftListDialog()
            giftListDialog.music_id = Integer.valueOf(musicId)
            if (CacheUtils.getBoolean(MainApplication.application, Constants.User.IS_LOGIN)) {
                giftListDialog.show((mContext as BaseActivity).supportFragmentManager, "giftListDialog")
            } else {
                mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
            }
        }

//        helper.setOnClickListener(R.id.tv_to_gift_list) { (mContext as MusicDetailsActivity).getRightPanel2().setOpen(true, true) }

        NetWork.getMusiGiftList(mContext, HttpParams("id", item.uid.toString() + ""), object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val musicGiftListBean = JSON.parseObject(resultData, MusicGiftListBean::class.java)
                val data = musicGiftListBean.data
                if (data != null && data.size > 0) {
                    helper.setVisible(R.id.tv_icon, false)
                    helper.setVisible(R.id.auto_pull_recycler, true)
                    if (autoPollRecyclerView.adapter == null) {
                        autoPollAdapter = AutoPollAdapter(mContext, data)
                        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                        layoutManager.isSmoothScrollbarEnabled = true
                        layoutManager.isAutoMeasureEnabled = true
                        autoPollRecyclerView.layoutManager = layoutManager
                        autoPollRecyclerView.setHasFixedSize(true)
                        autoPollRecyclerView.isNestedScrollingEnabled = false
                        autoPollRecyclerView.addItemDecoration(MyRecyclerDetorration(mContext, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(mContext, 5f), Color.WHITE, true))
                        autoPollRecyclerView.adapter = autoPollAdapter
                    }
                    if (data.size > 2) {//保证itemCount的总个数宽度超过屏幕宽度
                        autoPollRecyclerView.start()
                    }
                } else {
                    helper.setVisible(R.id.tv_icon, true)
                    helper.setVisible(R.id.auto_pull_recycler, false)
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    private fun bindListeningData(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        val listening_recyclerview = helper.getView<NoScrollRecyclerView>(R.id.listening_recyclerview)
        listening_recyclerview.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.left = -CommonUtils.dip2px(context!!, 10f)
            }
        })

        listening_recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val musicDetailsListeningAdapter = MusicDetailsListeningAdapter(item.related, context)
        listening_recyclerview.adapter = musicDetailsListeningAdapter
    }

    private fun bindLabelData(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        if (TextUtils.isEmpty(songName)) {
            helper.setText(R.id.tv_table_name, "音乐标签")
        } else {
            helper.setText(R.id.tv_table_name, "歌单标签")
        }
        if (null == multilineGroup) {
            multilineGroup = helper.getView(R.id.multiline_group)
            val strings = ArrayList<String>()
            strings.clear()
            for (i in 0 until item.tags.size) {
                strings.add(item.tags[i].title)
            }
            multilineGroup!!.addAll(strings)
            multilineGroup!!.setOnCheckChangedListener { group, position, checked ->
                val intent = Intent(mContext, SearchResultActivity::class.java)
                val bundle = Bundle()
                bundle.putString(SEARCH_WORDS, strings[position])
                if (!TextUtils.isEmpty(songName)) {
                    intent.putExtra(TO_SHEET_RESULT, "sheetResult")
                }
                intent.putExtras(bundle)
                mContext.startActivity(intent)
            }
        }
    }

    private fun bindSynopsisData(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        helper.getView<ExpandableTextView>(R.id.expand_text_view).text = StringUtils.isEmpty(item.intro)
    }

    private fun bindSongData(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        val recyclerView = helper.getView<RecyclerView>(R.id.song_recyclerview) as NoScrollRecyclerView
        recyclerView.isNestedScrollingEnabled = false
        if (item.song_all.size > 0) {
            helper.setVisible(R.id.tv_icon, false)
            helper.setVisible(R.id.tv_add_song, false)
            helper.setVisible(R.id.song_recyclerview, true)
        } else {
            helper.setVisible(R.id.tv_icon, true)
            helper.setVisible(R.id.tv_add_song, true)
            helper.setVisible(R.id.song_recyclerview, false)
        }
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        if (null == recyclerView.adapter) {
            musicDetailsLikeAdapter = MusicDetailsLikeAdapter(item.song_all)
            recyclerView.adapter = musicDetailsLikeAdapter
        } else {
            musicDetailsLikeAdapter!!.notifyDataSetChanged()
        }
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
    }

    private fun bindRecommendSongData(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        likesSongSheet = if (item.likes == null) ArrayList() else item.likes
        val recyclerView = helper.getView<RecyclerView>(R.id.song_recyclerview) as NoScrollRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3, CommonUtils.dip2px(context!!, 6f), CommonUtils.dip2px(context!!, 10f), true))
        if (null == recyclerView.adapter) {
            musicDetailsSongAdapter = MusicDetailsSongAdapter(likesSongSheet)
            recyclerView.adapter = musicDetailsSongAdapter
        } else {
            musicDetailsSongAdapter!!.notifyDataSetChanged()
        }
    }

    private fun bindPondData(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        val recyclerView = helper.getView<RecyclerView>(R.id.pond_recyclerview) as NoScrollRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val musicDetailsPondAdapter = PondAdapter(item.pondList)
        recyclerView.adapter = musicDetailsPondAdapter
    }

    private fun bindMusicData(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        val musics = item.music ?: return
        helper.setText(R.id.tv_song_list_label, "全部播放（共" + item.music.size + "首）")
        song_recyclerview = helper.getView(R.id.song_recyclerview)
        song_recyclerview?.isNestedScrollingEnabled = false
        song_recyclerview?.layoutManager = LinearLayoutManager(context)
        songSheetMusicAdapter = SongSheetMusicListAdapter(musics, fragmentManager, songName,isEdit)
        song_recyclerview?.adapter = songSheetMusicAdapter
    }

    private fun bindRecommendData(helper: BaseViewHolder, item: MusicIndex.ItemInfoListBean, position: Int) {
        val song_sheet_recyclerview = helper.getView<NoScrollRecyclerView>(R.id.song_sheet_recyclerview)
        song_sheet_recyclerview.isNestedScrollingEnabled = false
        song_sheet_recyclerview.layoutManager = GridLayoutManager(context, 3)
        song_sheet_recyclerview.addItemDecoration(GridSpacingItemDecoration(3, 0, CommonUtils.dip2px(context!!, 6f), true))
        songSheetRecommendListAdapter = SongSheetRecommendListAdapter(item.recommend)
        song_sheet_recyclerview.adapter = songSheetRecommendListAdapter
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int): Boolean {
        return false
    }

    fun notifyData() {
        if (null != musicDetailsLikeAdapter) {
            musicDetailsLikeAdapter!!.notifyDataSetChanged()
        }
        if (null != musicDetailsSongAdapter) {
            musicDetailsSongAdapter!!.notifyDataSetChanged()
        }
        if (null != songSheetMusicAdapter) {
            songSheetMusicAdapter!!.notifyDataSetChanged()
        }
        if (null != songSheetRecommendListAdapter) {
            songSheetRecommendListAdapter!!.notifyDataSetChanged()
        }
    }

    fun setBillboardConfig(profit_billboard_config: MemberGiftListBean.DataBean.ProfitBillboardConfigBean) {
        this.profit_billboard_config = profit_billboard_config
        data?.let {
            for (i in data.indices) {
                if (data[i].itemType === "giftItem") {
                    notifyItemChanged(i)
                }
            }
        }

    }
}
