package com.mxkj.yuanyintang.mainui.home.adapter

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.home.activity.LikesMusicActivity
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.activity.MusicListActivity
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.mainui.home.bean.HomeIndex
import com.mxkj.yuanyintang.mainui.home.data.Constant
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseMultiItemQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsHomeActivity
import com.mxkj.yuanyintang.mainui.home.utils.BetterRecyclerView
import com.mxkj.yuanyintang.mainui.home.utils.GridSpacingItemDecoration
import com.mxkj.yuanyintang.mainui.pond.TagDetialPondList
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew
import com.mxkj.yuanyintang.mainui.web.CommonWebview
import com.mxkj.yuanyintang.extraui.activity.PicturePagerDetailsActivity
import com.mxkj.yuanyintang.extraui.bean.PictureDataBean
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.image.imageloader.utils.ImageUtil
import com.mxkj.yuanyintang.utils.rxbus.RxBus
import com.mxkj.yuanyintang.utils.rxbus.event.SelectTabChangeEvent
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.widget.MarqueTextView
import com.mxkj.yuanyintang.widget.bannerLayout.BannerLayout
import com.mxkj.yuanyintang.widget.bannerLayout.RecyclingUnlimitedPagerAdapter
import com.mxkj.yuanyintang.widget.bannerLayout.util.ViewHolder
import java.util.concurrent.TimeUnit

import de.hdodenhof.circleimageview.CircleImageView

import com.mxkj.yuanyintang.mainui.home.fragment.HomeFragment.Companion.SHOW_SYS_MSG
import com.mxkj.yuanyintang.mainui.home.fragment.HomeFragment.Companion.STR_SYS_MSG
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil

class HomeMultipleRecycleAdapter(data: List<HomeIndex.ItemInfoListBean>) : BaseMultiItemQuickAdapter<HomeIndex.ItemInfoListBean, BaseViewHolder>(data), BaseQuickAdapter.SpanSizeLookup, BaseQuickAdapter.OnItemChildClickListener {
    private var maxHasLoadPosition: Int = 0
    private var musicAdapter: RecommendMusicDataAdapter? = null
    private var musicianAdapter: RecommendMusicianDataAdapter? = null
    private var chartsAdapter: ChartsDataAdapter? = null
    private var unlimitedPagerAdapter: RecyclingUnlimitedPagerAdapter<*>? = null

    init {
        setSpanSizeLookup(this)
        addItemType(Constant.TYPE_TOP_BANNER, R.layout.homerecycle_item_top_banner)
        addItemType(Constant.TYPE_LOTTERY, R.layout.homerecycle_item_lottery)
        addItemType(Constant.TYPE_SYSYEM_MSG, R.layout.homerecycle_item_system_msg)
        addItemType(Constant.TYPE_CHARTS, R.layout.homerecycle_item_charts)
        addItemType(Constant.TYPE_RECOMMEND_MUSIC, R.layout.homerecycle_item_recommend_music)
        addItemType(Constant.TYPE_RECOMMEND_MUSICIAN, R.layout.homerecycle_item_recommend_musician)
        addItemType(Constant.TYPE_VOCALOID, R.layout.homerecycle_item_vocaloid)
        addItemType(Constant.TYPE_POND, R.layout.homerecycle_item_pond)

    }

    override fun getSpanSize(gridLayoutManager: GridLayoutManager, position: Int): Int {
        return 1
    }

    override fun convert(helper: BaseViewHolder, item: HomeIndex.ItemInfoListBean, position: Int) {
        if (maxHasLoadPosition < position) {
            maxHasLoadPosition = position
        }
        if ("topBanner" == item.itemType && maxHasLoadPosition <= position) {
            bindTopBannerData(helper, item, position)
        } else if (item.systemMsgBeanList != null && item.systemMsgBeanList.size > 0 && "systemMsg" == item.itemType && maxHasLoadPosition <= position) {
            //bindSystemMsgData(helper, item, position);
        } else if (item.lotteryList != null && item.lotteryList.size > 0 && "lottery" == item.itemType && maxHasLoadPosition <= position) {
            bindLotteryData(helper, item, position)
        } else if ("charts" == item.itemType && maxHasLoadPosition <= position) {
            bindChartsData(helper, item, position)
        } else if ("recommendMusic" == item.itemType && maxHasLoadPosition <= position) {
            bindRecommendMusicData(helper, item, position)
        } else if ("recommendMusicIan" == item.itemType && maxHasLoadPosition <= position) {
            bindRecommendMusicIanData(helper, item, position)
        } else if ("vocaloid" == item.itemType) {
            bindVocaloidData(helper, item, position)
        } else if ("pond" == item.itemType) {
            bindPondData(helper, item, position)
        }
    }

    private fun bindChartsData(helper: BaseViewHolder, item: HomeIndex.ItemInfoListBean?, position: Int) {
        if (item == null) {
            return
        }

        val billboard = item.billboardBean
        helper.getView<View>(R.id.layout_click_label).setOnClickListener { goActivity(ChartsHomeActivity::class.java) }

        if (billboard != null && billboard.cate_imgX != null) {
            helper.setText(R.id.tv_label, StringUtils.isEmpty(billboard.cate_imgX.title))
            ImageLoader.with(mContext)
                    .url(StringUtils.isEmpty(billboard.cate_imgX.imgpic_link))
                    .into(helper.getView<ImageView>(R.id.iv_label_img))
        }
        val recyclerView = helper.getView<BetterRecyclerView>(R.id.recommend_charts_recyclerview)
        if (null == recyclerView.adapter) {
            recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            chartsAdapter = ChartsDataAdapter(billboard!!.billboards)
            recyclerView.adapter = chartsAdapter
        } else {
            chartsAdapter!!.setNewData(billboard!!.billboards)
        }
    }

    private fun bindLotteryData(helper: BaseViewHolder, item: HomeIndex.ItemInfoListBean, position: Int) {
        val img_lottery = helper.getView<ImageView>(R.id.img_lottery)
        if (item.lotteryList != null && item.lotteryList.size > 0) {
            val lotteryBean = item.lotteryList[0]
            img_lottery.visibility = View.VISIBLE
            try {
                ImageLoader.with(mContext).url(lotteryBean.imgpic_info.link).error(R.drawable.nothing).into(img_lottery)

            } catch (e: RuntimeException) {
            }

            helper.setOnClickListener(R.id.img_lottery) { msgClickGo(lotteryBean.type, lotteryBean.url, lotteryBean.id.toString() + "") }
        } else {
            img_lottery.visibility = View.GONE

        }
    }

    private fun bindTopBannerData(helper: BaseViewHolder, item: HomeIndex.ItemInfoListBean, position: Int) {
        val bannerLayout = helper.getView<BannerLayout>(R.id.banner)
        val ll_banner = helper.getView<LinearLayout>(R.id.ll_banner)

        val anInt = CacheUtils.getInt(MainApplication.application, Constants.Other.WIDTH, 0)
        val width = ImageUtil.dip2px(anInt.toFloat())
        val height = (width / 1.8).toInt()
        ll_banner.layoutParams = LinearLayout.LayoutParams(width, height)

        unlimitedPagerAdapter = object : RecyclingUnlimitedPagerAdapter<HomeIndex.ItemInfoListBean.ShufflingBean>(bannerLayout.getAutoScrollViewPager(), mContext, item.shuffling, R.layout.item_banner_imgae) {
            override fun onBind(position: Int, data: HomeIndex.ItemInfoListBean.ShufflingBean, holder: ViewHolder) {
                try {
                    val layoutParams = bannerLayout.layoutParams

                    Glide.with(mContext)
                            .load(data.imgpic_info.link + "/" + width + "/" + height + "/3/80?format=0")
                            .asBitmap()
                            .into(holder.bind(R.id.img))
                } catch (e: RuntimeException) {
                }

                RxView.clicks(holder.bind(R.id.img))
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe {
                            if (data.url.isNotEmpty()) {
                                if (data.alias == "web") {
                                    val url = data.url
                                    val intent = Intent(mContext, CommonWebview::class.java)
                                    intent.putExtra("url", url)
                                    intent.putExtra("title", data.title)
                                    if (!TextUtils.isEmpty(data.content)) {
                                        intent.putExtra("content", data.content)
                                    }
                                    if (data.imgpic_info != null) {
                                        if (!TextUtils.isEmpty(data.imgpic_info.link)) {
                                            intent.putExtra("img", data.imgpic_info.link)
                                        }
                                        mContext.startActivity(intent)

                                    }

                                } else if (data.alias == "topic") {
                                    val url = data.url
                                    val intent = Intent(mContext, PondDetialActivityNew::class.java)
                                    val bundle = Bundle()
                                    bundle.putInt(PondDetialActivityNew.PID, Integer.parseInt(url))
                                    intent.putExtras(bundle)
                                    mContext.startActivity(intent)
                                } else if (data.alias == "activity") {
                                    val url = data.url
                                    val intent = Intent(mContext, CommonWebview::class.java)
                                    intent.putExtra("url", url)
                                    intent.putExtra("activity", "activity")
                                    intent.putExtra("title", data.title)
                                    intent.putExtra("content", data.content)
                                    try {
                                        intent.putExtra("img", data.imgpic_info.link)
                                        mContext.startActivity(intent)
                                    } catch (e: RuntimeException) {
                                    }

                                } else if (data.alias == "music") {
                                    PlayCtrlUtil.play(mContext,data.url.toInt(),0)
                                } else if (data.alias == "musician") {
                                    val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
                                    val bundle = Bundle()
                                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, data.url + "")
                                    intent.putExtras(bundle)
                                    mContext.startActivity(intent)
                                } else if (data.alias == "song") {
                                    val intent = Intent(mContext, SongSheetDetailsActivity::class.java)
                                    val bundle = Bundle()
                                    bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, data.url + "")
                                    intent.putExtras(bundle)
                                    mContext.startActivity(intent)
                                }
                            }
                        }
            }
        }
        bannerLayout.setAdapter(unlimitedPagerAdapter)
        bannerLayout.showIndicator(true)
        bannerLayout.startAutoScroll()
    }

    private fun bindSystemMsgData(helper: BaseViewHolder, item: HomeIndex.ItemInfoListBean, position: Int) {
        if (CacheUtils.getBoolean(mContext, SHOW_SYS_MSG, true) && item.systemMsgBeanList != null && item.systemMsgBeanList.size > 0) {
            val systemMsgBean = item.systemMsgBeanList[0]
            helper.setVisible(R.id.layout_system_msg, true)
            helper.getView<MarqueTextView>(R.id.tv_system_msg).text = StringUtils.isEmpty(systemMsgBean.text)
            helper.setOnClickListener(R.id.layout_system_msg) {
                Log.e(BaseQuickAdapter.TAG, "onClick: " + systemMsgBean.type + "------" + systemMsgBean.url + "------" + systemMsgBean.id)
                msgClickGo(systemMsgBean.type, systemMsgBean.url, systemMsgBean.id.toString() + "")
            }
            helper.setOnClickListener(R.id.hide_sys_msg) {
                CacheUtils.setBoolean(mContext, SHOW_SYS_MSG, false)
                CacheUtils.setString(mContext, STR_SYS_MSG, null)
                RxBus.getDefault().post(systemMsgBean)
            }
        } else {
            helper.getView<View>(R.id.layout_system_msg).visibility = View.GONE
        }
    }

    private fun bindRecommendMusicData(helper: BaseViewHolder, item: HomeIndex.ItemInfoListBean, position: Int) {
        helper.getView<View>(R.id.layout_click_label).setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(MusicListActivity.MUSIC_GET_TYPE, 5)
            bundle.putString(MusicListActivity.MUSIC_LIST_TITLE, item.song.cate_img.title)
            bundle.putInt(MusicListActivity.MUSIC_LIST_CATEGORY, item.song.cate_img.id)
            goActivity(MusicListActivity::class.java, bundle)
        }
        helper.setText(R.id.tv_label, StringUtils.isEmpty(item.song.cate_img.title))
        try {
            ImageLoader.with(mContext)
                    .url(item.song.cate_img.imgpic_info.link)
                    .into(helper.getView<ImageView>(R.id.iv_label_img))

        } catch (e: NullPointerException) {
        }


        if (musicAdapter == null) {
            val recyclerView = helper.getView<BetterRecyclerView>(R.id.recommend_music_recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            musicAdapter = RecommendMusicDataAdapter(item.song.song)
            musicAdapter!!.setOnItemClickListener { _, _, position ->
                val bundle = Bundle()
                bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, item.song.song[position].id.toString() + "")
                goActivity(SongSheetDetailsActivity::class.java, bundle)
            }
            recyclerView.adapter = musicAdapter
        } else {
            if (null != item.song && null != item.song.song && item.song.song.size > 0) {
                musicAdapter!!.data.clear()
                musicAdapter!!.data.addAll(item.song.song)
                musicAdapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun bindRecommendMusicIanData(helper: BaseViewHolder, item: HomeIndex.ItemInfoListBean, position: Int) {
        helper.getView<View>(R.id.layout_click_label).setOnClickListener {
            val bundle = Bundle()
            bundle.putString(MusicListActivity.MUSIC_LIST_TITLE, item.musicianX.cate_img.title)
            bundle.putInt(MusicListActivity.MUSIC_LIST_CATEGORY, item.musicianX.cate_img.id)
            bundle.putInt(MusicListActivity.MUSIC_GET_TYPE, 2)
            goActivity(MusicListActivity::class.java, bundle)
        }

        helper.setText(R.id.tv_label, StringUtils.isEmpty(item.musicianX.cate_img.title))
        try {
            ImageLoader.with(mContext)
                    .url(item.musicianX.cate_img.imgpic_info.link)
                    .into(helper.getView<ImageView>(R.id.iv_label_img))
        } catch (e: RuntimeException) {
        }


        if (null == musicianAdapter) {
            val recyclerView = helper.getView<BetterRecyclerView>(R.id.recommend_musician_recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            musicianAdapter = RecommendMusicianDataAdapter(item.musicianX.musician)
            recyclerView.adapter = musicianAdapter
        } else {
            if (null != item.musicianX && null != item.musicianX.musician && item.musicianX.musician.size > 0) {
                musicianAdapter!!.data.clear()
                musicianAdapter!!.data.addAll(item.musicianX.musician)
                musicianAdapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun bindVocaloidData(helper: BaseViewHolder, item: HomeIndex.ItemInfoListBean, position: Int) {
        helper.getView<View>(R.id.layout_click_label).setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(MusicListActivity.MUSIC_GET_TYPE, 1)
            bundle.putString(MusicListActivity.MUSIC_LIST_TITLE, item.catemusic[item.tag].title)
            bundle.putInt(MusicListActivity.MUSIC_LIST_CATEGORY, item.catemusic[item.tag].id)
            goActivity(MusicListActivity::class.java, bundle)
        }
        if (item.tag >= item.catemusic.size) {
            return
        }
        helper.setText(R.id.tv_label, StringUtils.isEmpty(item.catemusic[item.tag].title))
        ImageLoader.with(mContext)
                .url(item.catemusic[item.tag]?.imgpic_info?.link)
                .into(helper.getView<ImageView>(R.id.iv_label_img))
        val recyclerView = helper.getView<BetterRecyclerView>(R.id.recommend_vocaloid_recyclerview)
        if (null == recyclerView.adapter) {
            recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            val cateMusicList = item.catemusic[item.tag].music
            val vocaloidAdapter = VocaloidDataAdapter(cateMusicList)
            recyclerView.adapter = vocaloidAdapter
        } else {
            val cateMusicList = item.catemusic[item.tag].music
            (recyclerView.adapter as VocaloidDataAdapter).setNewData(cateMusicList)
        }
    }

    private fun bindPondData(helper: BaseViewHolder, item: HomeIndex.ItemInfoListBean, position: Int) {
        if (item.tag == 0) {
            helper.setVisible(R.id.layout_cate, true)
            if (!TextUtils.isEmpty(item.cate_img.toString())) {
                val cateImgBean = JSON.parseObject(item.cate_img.toString(), HomeIndex.CateImgBean::class.java)
                helper.setText(R.id.tv_pond, StringUtils.isEmpty(cateImgBean.title))
                if (cateImgBean.imgpic_info != null) {
                    ImageLoader.with(mContext)
                            .url(cateImgBean.imgpic_info.link)
                            .into(helper.getView(R.id.iv_cate_img))
                }
            }
        } else {
            helper.setVisible(R.id.layout_cate, false)
        }


        if (item.topic.size <= item.tag) {
            return
        }


        if (item.topic[item.tag].is_music == 3) {
            helper.setVisible(R.id.iv_is_vip, true)
        } else {
            helper.setVisible(R.id.iv_is_vip, false)
        }
        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.topic[item.tag].title))
        helper.setText(R.id.tv_nickname, StringUtils.isEmpty(item.topic[item.tag].nickname))
        helper.setText(R.id.tv_read_num, StringUtils.setNum(item.topic[item.tag].hits))
        ImageLoader.with(mContext)
                .url(item.topic[item.tag].head_link + "/" + 100 + "/" + 100 + "/3/80?format=0")
                .scale(ScaleMode.CENTER_CROP)
                .asCircle()
                .into(helper.getView<CircleImageView>(R.id.cimg_head))


        val recyclerView = helper.getView<RecyclerView>(R.id.recyclerView)
        recyclerView.setItemViewCacheSize(10)
        if (item.topic != null && item.topic[item.tag] != null && item.topic[item.tag].imglist_info != null) {
            if (item.topic[item.tag].imglist_info.size == 0) {
                helper.setVisible(R.id.iv_alone_img, false)
                helper.setVisible(R.id.recyclerView, false)
            } else if (item.topic[item.tag].imglist_info.size == 1) {
                //                final List imglist_link = new ArrayList<>();
                val imglist_info = item.topic[item.tag].imglist_info
                if (imglist_info != null && imglist_info.size > 0) {
                    for (i in imglist_info.indices) {
                        //                        imglist_link.add(imglist_info.get(i).getLink());
                    }
                }
                helper.setVisible(R.id.iv_alone_img, true)
                helper.setVisible(R.id.recyclerView, false)
                ImageLoader.with(mContext)
                        .url(item.topic[item.tag].imglist_info[0].link)
                        .override(160, 160)
                        .asSquare()
                        .into(helper.getView<ImageView>(R.id.iv_alone_img))
                RxView.clicks(helper.getView(R.id.iv_alone_img))
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe {
                            val bundle = Bundle()
                            val pictureDataBean = PictureDataBean()
                                    .setId(item.topic[item.tag].id.toString() + "")
                                    .setCommentNum(item.topic[item.tag].thcount)
                                    .setPhotoList(item.topic[item.tag].imglist_link)
                                    .setTitle(item.topic[item.tag].title)
                                    .setNickname(item.topic[item.tag].nickname)
                                    .setType("pond")
                                    .setPosition(position)
                                    .setHits(item.topic[item.tag].hits)
                            bundle.putSerializable(PicturePagerDetailsActivity.PICTURE_DATA, pictureDataBean)
                            goActivity(PicturePagerDetailsActivity::class.java, bundle)
                        }

            } else if (item.topic[item.tag].imglist_info.size >= 2) {
                val imglist_info = item.topic[item.tag].imglist_info
                helper.setVisible(R.id.iv_alone_img, false)
                helper.setVisible(R.id.recyclerView, true)
                if (recyclerView.adapter == null) {
                    recyclerView.layoutManager = GridLayoutManager(mContext, 3)
                    recyclerView.addItemDecoration(GridSpacingItemDecoration(3, CommonUtils.dip2px(mContext, 5f), 0, true))
                    val pondPhotoAdapter = PondPhotoAdapter(imglist_info, item.topic[item.tag].imglist_link, mContext)
                    recyclerView.adapter = pondPhotoAdapter
                    pondPhotoAdapter.setOnItemClickListener { adapter, view, position ->
                        val bundle = Bundle()
                        val pictureDataBean = PictureDataBean()
                                .setId(item.topic[item.tag].id.toString() + "")
                                .setCommentNum(item.topic[item.tag].thcount)
                                .setPhotoList(item.topic[item.tag].imglist_link)
                                .setTitle(item.topic[item.tag].title)
                                .setNickname(item.topic[item.tag].nickname)
                                .setType("pond")
                                .setPosition(position)
                                .setHits(item.topic[item.tag].hits)
                        bundle.putSerializable(PicturePagerDetailsActivity.PICTURE_DATA, pictureDataBean)
                        goActivity(PicturePagerDetailsActivity::class.java, bundle)
                    }
                } else {
                    recyclerView.adapter.notifyDataSetChanged()
                }
            } else {
                helper.setVisible(R.id.iv_alone_img, false)
                helper.setVisible(R.id.recyclerView, false)
            }
        } else {
            helper.setVisible(R.id.iv_alone_img, false)
            helper.setVisible(R.id.recyclerView, false)
        }

        helper.setVisible(R.id.tv_label_one, false)
        helper.setVisible(R.id.tv_label_two, false)
        helper.setVisible(R.id.tv_label_three, false)
        when {
            item.topic[item.tag].hashtag.size == 1 -> {
                helper.setVisible(R.id.tv_label_one, true)
                helper.setText(R.id.tv_label_one, StringUtils.isEmpty(item.topic[item.tag].hashtag[0].title))
            }
            item.topic[item.tag].hashtag.size == 2 -> {
                helper.setVisible(R.id.tv_label_one, true)
                helper.setVisible(R.id.tv_label_two, true)
                helper.setText(R.id.tv_label_one, StringUtils.isEmpty(item.topic[item.tag].hashtag[0].title))
                helper.setText(R.id.tv_label_two, StringUtils.isEmpty(item.topic[item.tag].hashtag[1].title))
            }
            item.topic[item.tag].hashtag.size == 3 -> {
                helper.setVisible(R.id.tv_label_one, true)
                helper.setVisible(R.id.tv_label_two, true)
                helper.setVisible(R.id.tv_label_three, true)
                helper.setText(R.id.tv_label_one, StringUtils.isEmpty(item.topic[item.tag].hashtag[0].title))
                helper.setText(R.id.tv_label_two, StringUtils.isEmpty(item.topic[item.tag].hashtag[1].title))
                helper.setText(R.id.tv_label_three, StringUtils.isEmpty(item.topic[item.tag].hashtag[2].title))
            }
        }

        RxView.clicks(helper.getView(R.id.layout_click)).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            val bundle = Bundle()
            bundle.putInt(PondDetialActivityNew.PID, item.topic[item.tag].id)
            goActivity(PondDetialActivityNew::class.java, bundle)
        }

        RxView.clicks(helper.getView(R.id.tv_pond)).throttleFirst(1, TimeUnit.SECONDS).subscribe { RxBus.getDefault().post(SelectTabChangeEvent(1)) }

        RxView.clicks(helper.getView(R.id.tv_label_one)).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            val intent = Intent(mContext, TagDetialPondList::class.java)
            val bundle = Bundle()
            bundle.putInt(TagDetialPondList.TAG_ID, item.topic[item.tag].hashtag[0].id)
            bundle.putString(TagDetialPondList.TAG_TITLE, item.topic[item.tag].hashtag[0].title)
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        }

        RxView.clicks(helper.getView(R.id.tv_label_two)).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            val intent = Intent(mContext, TagDetialPondList::class.java)
            val bundle = Bundle()
            bundle.putInt(TagDetialPondList.TAG_ID, item.topic[item.tag].hashtag[1].id)
            bundle.putString(TagDetialPondList.TAG_TITLE, item.topic[item.tag].hashtag[1].title)
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        }

        RxView.clicks(helper.getView(R.id.tv_label_three))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val intent = Intent(mContext, TagDetialPondList::class.java)
                    val bundle = Bundle()
                    bundle.putInt(TagDetialPondList.TAG_ID, item.topic[item.tag].hashtag[2].id)
                    bundle.putString(TagDetialPondList.TAG_TITLE, item.topic[item.tag].hashtag[2].title)
                    intent.putExtras(bundle)
                    mContext.startActivity(intent)
                }

        RxView.clicks(helper.getView(R.id.layout_to_musician))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    Log.d(BaseQuickAdapter.TAG, "accept: ----------->" + item.topic[item.tag].uid)
                    val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, item.topic[item.tag].uid.toString() + "")
                    intent.putExtras(bundle)
                    mContext.startActivity(intent)
                }

    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int): Boolean {
        return false
    }

    /**
     * 跳转页面带参数
     *
     * @param mClass 目标页面
     * @param bundle 参数
     */
    @JvmOverloads
    private fun goActivity(mClass: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(mContext, mClass)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        mContext.startActivity(intent)
    }

    fun notifyData() {
        if (null != musicAdapter) {
            musicAdapter!!.notifyDataSetChanged()
        }
        if (null != musicianAdapter) {
            musicianAdapter!!.notifyDataSetChanged()
        }
        if (null != chartsAdapter) {
            chartsAdapter!!.notifyDataSetChanged()
        }
    }

    private fun msgClickGo(type: String?, url: String, id: String) {
        if (type != null && type == "page") {
            val bundle = Bundle()
            when (url) {
                "home" -> {
                }
                "topicDetails" -> if (!TextUtils.isEmpty(id)) {
                    bundle.putInt(PondDetialActivityNew.PID, Integer.parseInt(id))
                    goActivity(PondDetialActivityNew::class.java, bundle)
                }
                "musicDetails" -> {
//                    bundle.putString(MusicDetailsActivity.MUSIC_ID, id)
//                    goActivity(MusicDetailsActivity::class.java, bundle)
                    PlayCtrlUtil.play(mContext,id.toInt(),0)
                }
                "musicianDetailHome" -> {
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 0)
                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                }
                "musicianDetailMusic" -> {
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 1)
                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                }
                "musicianDetailSongSheet" -> {
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 2)
                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                }
                "musicianDetailTopic" -> {
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id)
                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 3)
                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                }
                "songSheetDetails" -> {
                    bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, id)
                    goActivity(SongSheetDetailsActivity::class.java, bundle)
                }
                "likesSongSheetDetails" -> {
                    bundle.putString(LikesMusicActivity.MUSICIAN_ID, id)
                    goActivity(LikesMusicActivity::class.java, bundle)
                }
            }
        } else if (type != null && type == "activity") {
            val intent = Intent(mContext, CommonWebview::class.java)
            intent.putExtra("url", url)
            intent.putExtra("activity", "activity")
            mContext.startActivity(intent)
        }
    }
}
