package com.mxkj.yuanyintang.mainui.newapp.pond

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.R.id.rootView
import com.mxkj.yuanyintang.base.activity.HomeActivity
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.adapter.SlidingFragmentViewPager
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.login_regist.QuickLoginActivityNew
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil
import com.mxkj.yuanyintang.mainui.newapp.home.OrderTypeBean
import com.mxkj.yuanyintang.mainui.newapp.myself.TimeLimitPreferential
import com.mxkj.yuanyintang.mainui.newapp.weidget.PondBigTxtTabLayout
import com.mxkj.yuanyintang.mainui.search.SearchActivity
import com.mxkj.yuanyintang.musicplayer.lyric_remusic.LrcView.biaoshi
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.trello.rxlifecycle2.components.support.RxFragment
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_choose_pond_tag_new.*
import kotlinx.android.synthetic.main.fragment_pond_new.view.*
import okhttp3.Headers
import java.util.ArrayList

class PondFrgNew : RxFragment() {
    private lateinit var homeActivity: HomeActivity
    private var rootView: View? = null
    internal var strings: ArrayList<String> = ArrayList()
    internal var titleList: ArrayList<OrderTypeBean.DataBean.WhereBean> = ArrayList()
    private var fragments: ArrayList<Fragment> = ArrayList()
    private lateinit var slidingFragmentViewPager: SlidingFragmentViewPager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_pond_new, container, false)
        return rootView
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (null == rootView) {
            return
        }
        homeActivity = activity as HomeActivity
        initView()
        initEvent()
        netData()
        initHead()
    }

    /**
     * 初始化池塘分类
     */
    private fun jsonPondData(resultData: String) {
        val parseObject = JSON.parseObject(resultData, OrderTypeBean::class.java)
        var whereBeanList = parseObject.data.where as ArrayList<OrderTypeBean.DataBean.WhereBean>
        var bgBeanList = parseObject.data.bg_img as ArrayList<OrderTypeBean.DataBean.BgImgBean>
        val cate_img = parseObject.data.cate_img
//        setHeadInfo(cate_img)
        strings = ArrayList()
        titleList = ArrayList()
        val whereBean = OrderTypeBean.DataBean.WhereBean()
        whereBean.title = "关注"
        titleList.add(whereBean)
        strings.add("关注")
        val whereBean2 = OrderTypeBean.DataBean.WhereBean()
        whereBean2.title = "热门"
        titleList.add(whereBean2)
        strings.add("热门")
        val whereBean3 = OrderTypeBean.DataBean.WhereBean()
        whereBean3.title = "附近"
        titleList.add(whereBean3)
        strings.add("附近")
        initViewPager()


        bgBeanList.indices.forEach {
            var alias:String? =bgBeanList.get(it).alias
            if(alias.equals("follow") || alias == "follow"){
                ImageLoader.with(activity).url(bgBeanList.get(it).imgpic_info.link).into(rootView?.iv_bcg)
                rootView?.tvCate?.text = bgBeanList.get(it).content
            }
        }



        rootView?.bigTxtTab?.init(activity, titleList, 1, object : PondBigTxtTabLayout.TabSelectListener {
            override fun onSelect(position: Int) {
                rootView?.viewpager?.currentItem = position
//                if (position == 0) {
//                    if (!CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
//                        startActivity(Intent(activity, LoginRegMainPage::class.java))
//                        rootView?.viewpager?.currentItem = 1
//
//                    }
//                }
            }
        })
        rootView?.bigTxtTab?.setColor("#ffffff", "#4d1a1a1a")
        rootView?.app_bar?.addOnOffsetChangedListener({ appBarLayout, verticalOffset ->
            val scrollRangle = appBarLayout.totalScrollRange
            if (verticalOffset == 0) {
//                rootView?.layout_head?.alpha = 1.0f
            } else {
//                val alpha = (Math.abs(Math.round(1.0f * verticalOffset / scrollRangle) * 10) / 10).toFloat()
//                rootView?.layout_head?.alpha = 1.0f - alpha
            }
        })
    }

    private fun initView() {
//        val jsondata = CacheUtils.getString(activity, "pondClass")
//        if (jsondata != null && jsondata.length > 10) {
//            jsonPondData(jsondata)
//            return
//        }
        NetWork.getOrderType(activity, "topic", object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                Log.e("pondClass", resultData)
                CacheUtils.setString(activity, "pondClass", resultData);
                jsonPondData(resultData);

                //加载池塘头部右侧大图用
                val parseObject = JSON.parseObject(resultData, PondImageBean::class.java)
                bgBeanList = parseObject.data.bg_img as ArrayList<PondImageBean.DataBean.BgImgBean>
            }

            override fun doError(msg: String) {
            }

            override fun doResult() {
            }
        })
    }

    private fun initHead() {
        rootView?.search?.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)

            MobclickAgent.onEvent(activity, "pond_search");

        }
        if (!CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {

            rootView?.civ_headimg?.setOnClickListener {
                MobclickAgent.onEvent(activity, "pond_musician_home");
                activity.startActivity(Intent(activity, QuickLoginActivityNew::class.java))
            }
            ImageLoader.with(activity).res(R.drawable.default_head_img).into(rootView?.civ_headimg)
            rootView?.civ_headimg?.isClickable = false
            return
        }
        UserInfoUtil.getUserInfoById(0, activity) { infoBean ->
            if (infoBean != null && infoBean?.data != null) {
                rootView?.civ_headimg?.isClickable = true
                Glide.with(activity) //上下文
                        .load(infoBean.data?.head_link) //图片地址
//                        .placeholder(R.drawable.default_head_img) //占位图
                        .error(R.drawable.default_head_img) //出错的占位图
                        .override(80,80) //图片显示的分辨率 ，像素值 可以转化为DP再设置
                        .into(rootView?.civ_headimg); //显示在哪个控件中
//                ImageLoader.with(activity).override(80, 80).getSize(88, 88).url(infoBean.data?.head_link)
//                        .placeHolder(R.drawable.default_head_img).error(R.drawable.default_head_img).asCircle()
//                        .scale(ScaleMode.CENTER_CROP).into(rootView?.civ_headimg)
                rootView?.civ_headimg?.setOnClickListener {
                    val intent = Intent(activity, MusicIanDetailsActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, infoBean?.data?.id.toString())
                    intent.putExtras(bundle)
                    activity.startActivity(intent)
                }
            }
        }
    }

    private fun setHeadInfo(cate_img: OrderTypeBean.DataBean.CateImgBean?) {
        cate_img?.let {
            ImageLoader.with(activity).url(it.imgpic_info?.link).into(rootView?.iv_bcg)
            rootView?.tvCate?.text = it.content
        }
    }

    private fun netData() {

    }

    private fun initEvent() {
        rootView?.app_bar?.addOnOffsetChangedListener({ appBarLayout, verticalOffset ->
            val scrollRangle = appBarLayout.totalScrollRange
            if (verticalOffset == 0) {
                rootView?.tvCate?.alpha = 1.0f
            } else {
                val alpha = (Math.abs(Math.round(1.0f * verticalOffset / scrollRangle) * 10) / 10).toFloat()
                rootView?.tvCate?.alpha = 1.0f - alpha
            }
        })
    }

    private fun initViewPager() {
        Log.e("strings", strings.size.toString())
        fragments.clear()
        fragments.add(FollowPondFrg())
        fragments.add(HotPondMainFrg())
        fragments.add(NearPondFrg())
        slidingFragmentViewPager = SlidingFragmentViewPager(childFragmentManager, strings, fragments, activity)
        rootView?.viewpager?.adapter = slidingFragmentViewPager
        rootView?.viewpager?.offscreenPageLimit = 3
        rootView?.viewpager?.currentItem = 1

        rootView?.viewpager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                rootView?.bigTxtTab?.setCurrSelectItem(position)
                when (position) {
                    0 -> {
                        MobclickAgent.onEvent(activity, "pond_follow");
//                        if (!CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
//                            startActivity(Intent(activity, LoginRegMainPage::class.java))
//                            rootView?.viewpager?.currentItem = 1
//                        }
                        titleList?.let {
                            if(it.get(0).title.equals("关注")){
                                bgBeanList?.let {
                                    ImageLoader.with(activity).url(it.get(0).imgpic_info.link).into(rootView?.iv_bcg)
                                    rootView?.tvCate?.text = it.get(0).content
                                }
                            }
                        }
                    }
                    1 ->{
                        MobclickAgent.onEvent(activity, "pond_hot")
                        titleList?.let {
                            if(it.get(1).title.equals("热门")){
                                bgBeanList?.let {
                                    ImageLoader.with(activity).url(it.get(1).imgpic_info.link).into(rootView?.iv_bcg)
                                    rootView?.tvCate?.text = it.get(1).content
                                }
                            }
                        }
                    }
                    2 ->{
                        MobclickAgent.onEvent(activity, "pond_near")
                        titleList?.let {
                            if(it.get(2).title.equals("附近")){
                                bgBeanList?.let {
                                    ImageLoader.with(activity).url(it.get(2).imgpic_info.link).into(rootView?.iv_bcg)
                                    rootView?.tvCate?.text = it.get(2).content
                                }
                            }
                        }
                    }

                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
//        initView()
        initHead()
    }

    internal var bgBeanList: ArrayList<PondImageBean.DataBean.BgImgBean> = ArrayList()
}

