package com.mxkj.yuanyintang.mainui.home.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.fastjson.JSON
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.R.attr.type
import com.mxkj.yuanyintang.base.fragment.BaseLazyFragment
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.home.adapter.MusicianSongsAdapter
import com.mxkj.yuanyintang.mainui.home.bean.MusicIanSongSheetBean
import com.mxkj.yuanyintang.mainui.home.bean.MyCollectionSongBean
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.myself.activity.MySongListActivity
import com.mxkj.yuanyintang.mainui.myself.bean.MySongListBean
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.umeng.analytics.MobclickAgent
import com.umeng.socialize.utils.Log
import java.util.ArrayList
import okhttp3.Headers
import kotlinx.android.synthetic.main.fragment_musician_song_sheet.*
import java.util.concurrent.TimeUnit

class SongSheetListFragment : BaseLazyFragment() {
    private var id: String = "0"
    internal var page = 1
    private var createAdapter: MusicianSongsAdapter? = null
    private var likeAdapter: MusicianSongsAdapter? = null
    internal var createList: MutableList<MusicIanSongSheetBean.DataBean> = ArrayList()
    internal var likeList: MutableList<MusicIanSongSheetBean.DataBean> = ArrayList()
    internal var interfaceRefreshLoadMore: InterfaceRefreshLoadMore? = null
    override val contentViewLayoutID: Int
        get() = R.layout.fragment_musician_song_sheet
    internal var verticalOffset: Int = 0


    //操作是删除和公开操作在适配器中，无法刷新数据，通过适配器吧potion穿过去（主要是操作再要activity中进行，potion不穿都行），在通过activity中更新数据
    internal var myHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> {
                    val bundle = msg.data
                    val po = bundle.getInt("position")
                    Log.e("rrrr", "" + po)
                    netData()
//                    mySongListAdapter.notifyDataSetChanged()
                }
            }
        }
    }




    override fun onFirstVisibleToUser() {
        id = arguments.getString("id")
        netData()
        /*我创建的歌单*/
        RxView.clicks(tvCreate)
                .subscribe {
                    if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                        val bundle = Bundle()
                        bundle.putString("type", "1")
                        bundle.putInt("uid", id.toInt())
                        goActivity(MySongListActivity::class.java, bundle)
                    } else {
                        goActivity(LoginRegMainPage::class.java)
                    }
                }
        /*我收藏的歌单*/
        RxView.clicks(tvCollect).subscribe {
            if (CacheUtils.getBoolean(activity, Constants.User.IS_LOGIN)) {
                val bundle = Bundle()
                bundle.putString("type", "2")
                bundle.putInt("uid", id.toInt())
                goActivity(MySongListActivity::class.java, bundle)
            } else {
                goActivity(LoginRegMainPage::class.java)
            }
        }
    }

    private fun netData() {
//      产品要求这里歌单有多少显示多少，所以row传了很大的值，歌单过多估计会卡死。
        NetWork.getMusicianSong(activity, 1, 1000000, id, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                if (headers != null) {
                    tvCreate.text = "创建的歌单(${headers.get("X-Pagination-Total-Count")})"
                }
                interfaceRefreshLoadMore?.setStopRefreshing()
                val musicIanSongSheetBean = JSON.parseObject(resultData, MusicIanSongSheetBean::class.java)
                val dataList = JSON.toJSONString(musicIanSongSheetBean.data)
                val list = JSON.parseArray(dataList, MusicIanSongSheetBean.DataBean::class.java)
                if (list.isNotEmpty()) {
                    if (page == 1) {
                        createList.clear()
                    }
                    createList.addAll(list)
                    initCreateRecyclerView()
                }
                isNotDataView()
            }

            override fun doError(msg: String) {
                if (page > 1) {
                    page--
                }
                interfaceRefreshLoadMore?.setStopRefreshing()
            }

            override fun doResult() {

            }
        })
        NetWork.getMusicianSongCollection(activity, page, StringUtils.isEmpty(id), object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                if (resultData.contains("html")) {
                    return
                }

                if (headers != null) {
                    tvCollect.text = "收藏的歌单(${headers.get("X-Pagination-Total-Count")})"
                }

                val parseObject = JSON.parseObject(resultData)
                val jsonArray = parseObject.getJSONArray("data")
                val list = JSON.parseArray(jsonArray.toJSONString(), MusicIanSongSheetBean.DataBean::class.java)
                if (list.isNotEmpty()) {
                    if (page == 1) {
                        likeList.clear()
                    }
                    likeList.addAll(list)
                    initLikeRecyclerView()
                }
                isNotDataView()
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    private fun initLikeRecyclerView() {
        if (recyCollect.adapter == null) {
            val linearLayoutManager = LinearLayoutManager(activity)
            linearLayoutManager.isSmoothScrollbarEnabled = false
            recyCollect.layoutManager = linearLayoutManager
            recyCollect.addItemDecoration(MyRecyclerDetorration(activity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(activity, 0.6f), R.color.dividing_line_color, true))
            likeAdapter = MusicianSongsAdapter(likeList,"")
            recyCollect.adapter = likeAdapter
        } else {
            likeAdapter?.setNewData(likeList)
        }
    }

    private fun initCreateRecyclerView() {
        if (recyCerate.adapter == null) {
            val linearLayoutManager = LinearLayoutManager(activity)
            linearLayoutManager.isSmoothScrollbarEnabled = false
            recyCerate.layoutManager = linearLayoutManager
            recyCerate.addItemDecoration(MyRecyclerDetorration(activity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(activity, 0.6f), R.color.dividing_line_color, true))
            createAdapter = MusicianSongsAdapter(createList,"",myHandler)
            recyCerate.adapter = createAdapter
        } else {
            createAdapter?.setNewData(createList)
        }
    }

    override fun onVisibleToUser() {
    }

    override fun onInvisibleToUser() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        interfaceRefreshLoadMore?.resetRefreshView()
    }

    private fun isNotDataView() {
        if (createList.size > 0 && likeList.size > 0) {
            tv_no_data.visibility = View.GONE
        } else {
            tv_no_data.visibility = View.VISIBLE
        }
    }
    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart("musician_album")
        netData()
    }

    override fun onDestroy() {
        super.onDestroy()
        MobclickAgent.onPageEnd("musician_album");

    }
}
