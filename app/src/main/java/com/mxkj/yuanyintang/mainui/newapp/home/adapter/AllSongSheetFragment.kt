package com.mxkj.yuanyintang.mainui.newapp.home.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import kotlinx.android.synthetic.main.activity_all_song_sheet.*
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.R.id.rootView
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.base.bean.UserInfo
import com.mxkj.yuanyintang.extraui.activity.PicturePagerDetailsActivity
import com.mxkj.yuanyintang.mainui.comment.Comment
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.home.adapter.SlidingFragmentViewPager
import com.mxkj.yuanyintang.mainui.home.adapter.SongSheetRecommendListAdapter
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.home.bean.MusicListSongBean
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.CHARTS_TYPE
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.EXPEN_CLASS_ID
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsAllFragment
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsFragment
import com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity
import com.mxkj.yuanyintang.mainui.home.utils.GridSpacingItemDecoration
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.myself.activity.EditPersonalProfileActivity
import com.mxkj.yuanyintang.mainui.newapp.home.AllMusicanFragment
import com.mxkj.yuanyintang.mainui.newapp.home.MusicTypeCkView
import com.mxkj.yuanyintang.mainui.newapp.home.OrderTypeBean
import com.mxkj.yuanyintang.mainui.newapp.pond.FollowPondFrg
import com.mxkj.yuanyintang.mainui.newapp.pond.HotPondMainFrg
import com.mxkj.yuanyintang.mainui.newapp.pond.NearPondFrg
import com.mxkj.yuanyintang.mainui.newapp.pond.PondFrgNew
import com.mxkj.yuanyintang.mainui.newapp.weidget.BigTxtTabLayout
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.trello.rxlifecycle2.components.support.RxFragment
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_all_song_sheet_fragment.*
import kotlinx.android.synthetic.main.activity_all_song_sheet_fragment.view.*
import kotlinx.android.synthetic.main.fragment_pond_new.view.*
import okhttp3.Headers
//StandardUiActivity()
class AllSongSheetFragment : RxFragment() {
    private var adapter: SongSheetRecommendListAdapter? = null
    val dataList = ArrayList<MusicIndex.ItemInfoListBean.RecommendBean>()
    private var whereBeanList = ArrayList<OrderTypeBean.DataBean.WhereBean>()
    private var page: Int = 1
    var interfaceRefreshLoadMore: InterfaceRefreshLoadMore? = null

    fun initView() {
        tabLayout = rootView!!.findViewById(R.id.tabLayout)
        music_app_bar = rootView!!.findViewById(R.id.music_app_bar)
        music_viewpager = rootView!!.findViewById(R.id.music_viewpager)
        tvCate = rootView!!.findViewById(R.id.tvCate)
    }

    private var rootView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.activity_all_song_sheet_fragment, container, false)
        return rootView
    }
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (null == rootView) {
            return
        }
        initView()
        initData()
        initEvent()
    }

    internal var titleList: ArrayList<OrderTypeBean.DataBean.WhereBean> = ArrayList()
    lateinit var titleone: String
    private var fragments: ArrayList<Fragment> = ArrayList()
    private lateinit var slidingFragmentViewPager: SlidingFragmentViewPager
    var titlesList = ArrayList<String>()
    lateinit var tabLayout:BigTxtTabLayout
    lateinit var music_app_bar: AppBarLayout
    lateinit var music_viewpager: ViewPager
    lateinit var tvCate: TextView






    private var thisId: Int = 0
    var OneId: Int = 0
    var TwoId: Int = 0
    var ThreeId: Int = 0

    fun initData() {
        NetWork.getOrderType(activity, "song", object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val parseObject = JSON.parseObject(resultData, OrderTypeBean::class.java)
                whereBeanList = parseObject.data.where as ArrayList<OrderTypeBean.DataBean.WhereBean>
                whereBeanList.forEach {
                    titleone = it.title

                    val whereBean = OrderTypeBean.DataBean.WhereBean()
                    whereBean.title = titleone
                    titleList.add(whereBean)
                    titlesList.add(it.title)

                    if(titleone.equals("主题")){
                        OneId = it.id
                    }
                    if(titleone.equals("情感")){
                        TwoId = it.id
                    }
                    if(titleone.equals("场景")){
                        ThreeId = it.id
                    }


                }

                tabLayout?.init(activity, titleList, 1, object : BigTxtTabLayout.TabSelectListener {
                    override fun onSelect(position: Int) {
                        music_viewpager.currentItem = position
//                        netPublicMusicSong(whereBeanList[position].list[0].id)
//                        if (position == 0) {
//                            if (!CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
//                                startActivity(Intent(activity, LoginRegMainPage::class.java))
//                                music_viewpager.currentItem = 1
//
//                            }
//                        }
                    }
                })
                tabLayout.setCurrSelectItem(0)
                tabLayout?.setColor("#ffffff", "#4d1a1a1a")

                initViewPager()
            }

            override fun doError(msg: String) {
            }

            override fun doResult() {
            }

        })
    }

    private fun initViewPager() {
        val bundle: Bundle
        fragments.clear()

        bundle = Bundle()
        bundle.putInt("one", OneId)
        val oneSongSheetFragment = OneSongSheetFragment()
        fragments.add(oneSongSheetFragment)
        oneSongSheetFragment.setArguments(bundle)

        bundle.putInt("two", TwoId)
        val twoSongSheetFragment = TwoSongSheetFragment()
        fragments.add(twoSongSheetFragment)
        twoSongSheetFragment.setArguments(bundle)

        bundle.putInt("three", ThreeId)
        val threeSongSheetFragment = ThreeSongSheetFragment()
        fragments.add(threeSongSheetFragment)
        threeSongSheetFragment.setArguments(bundle)

        slidingFragmentViewPager = SlidingFragmentViewPager(childFragmentManager, titlesList, fragments, activity)
        music_viewpager.adapter = slidingFragmentViewPager
        music_viewpager.offscreenPageLimit = titleList.size
//        music_viewpager.currentItem = 1


        music_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                tabLayout?.setCurrSelectItem(position)
                when (position) {
                    0 -> {

                    }
                    1 ->{

                    }
                    2 ->{

                    }

                }
            }
        })
    }

    fun initEvent() {
//        music_app_bar?.addOnOffsetChangedListener({ appBarLayout, verticalOffset ->
//            val scrollRangle = appBarLayout.totalScrollRange
//            if (verticalOffset == 0) {
//                tvCate?.alpha = 1.0f
//            } else {
//                val alpha = (Math.abs(Math.round(1.0f * verticalOffset / scrollRangle) * 10) / 10).toFloat()
//                tvCate?.alpha = 1.0f - alpha
//            }
//        })
    }



}
