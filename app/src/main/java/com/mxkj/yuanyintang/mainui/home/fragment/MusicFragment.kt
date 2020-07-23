package com.mxkj.yuanyintang.mainui.home.fragment

import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.fragment.BaseLazyFragment
import com.mxkj.yuanyintang.utils.image.imageloader.utils.ImageUtil
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.bean.MusicIanMusicBean
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.layoutmanager.FullyLinearLayoutManager
import com.umeng.analytics.MobclickAgent

import java.util.ArrayList

import io.reactivex.disposables.Disposable
import okhttp3.Headers
import kotlinx.android.synthetic.main.fragment_musician_music.view.*

class MusicFragment : BaseLazyFragment(), AppBarLayout.OnOffsetChangedListener {
    internal var id: String = "0"
    internal var page = 1
    private var musicIanMusicAdapter: MusicFragmentAdapter? = null
    internal var musicListMusicBeanList: MutableList<MusicIndex.ItemInfoListBean.MusicBean> = ArrayList()
    internal var interfaceRefreshLoadMore: InterfaceRefreshLoadMore? = null
    private var mPlayerMusicRefreshData: Disposable? = null
    override val contentViewLayoutID: Int
        get() = R.layout.fragment_musician_music

    internal var verticalOffset: Int = 0

    override fun onFirstVisibleToUser() {
        id = arguments.getString("id")
        initmRecyclerview()
        addListener()
        netData()
    }

    private fun netData() {
        if (TextUtils.isEmpty(id)) {
            return
        }
        NetWork.getMusicianMusic(activity, page, id, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                Log.e("TAG", headers.toString())
                if (headers != null) {
                    rootView.tv_release_num.text = "TA的作品(" + headers.get("X-Pagination-Total-Count") + "首)"
                }
                interfaceRefreshLoadMore?.setStopRefreshing()
                val musicIanMusicBean = JSON.parseObject(resultData, MusicIanMusicBean::class.java)
                        ?: return
                val dataBean = JSON.toJSONString(musicIanMusicBean.data)
                val dataList = JSON.parseArray(dataBean, MusicIndex.ItemInfoListBean.MusicBean::class.java)
                if (dataList != null && dataList.size > 0) {
                    if (page == 1) {
                        musicListMusicBeanList.clear()
                    }
                    musicListMusicBeanList.addAll(dataList)
                    rootView.mRecyclerview.post({
                        if (rootView.mRecyclerview.adapter == null) {
                            musicIanMusicAdapter = MusicFragmentAdapter(musicListMusicBeanList, fragmentManager, "myhomepagemusic", page, id, false)
                            rootView.mRecyclerview.adapter = musicIanMusicAdapter
                        } else {
                            musicIanMusicAdapter!!.setNewData(musicListMusicBeanList)
                        }
                    })
                } else {
                    if (page == 1) {
                        isNotDataView()
                    } else {
                        if (musicIanMusicAdapter!!.footerLayoutCount == 0) {
                            musicIanMusicAdapter!!.addFooterView(LayoutInflater.from(activity).inflate(R.layout.no_more_data_text, null))
                        }
                    }
                }
            }

            override fun doError(msg: String) {
                if (page > 1) {
                    page--
                }
                interfaceRefreshLoadMore!!.setStopRefreshing()
            }

            override fun doResult() {

            }
        })
    }

    private fun initmRecyclerview() {
        rootView.mRecyclerview.layoutManager = FullyLinearLayoutManager(activity)
        val myRecyclerDetorration = MyRecyclerDetorration(activity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(activity, 5f), R.color.white, false)
        rootView.mRecyclerview.addItemDecoration(myRecyclerDetorration)
        interfaceRefreshLoadMore = InterfaceRefreshLoadMore(rootView.superSwipeRefreshLayout, activity, object : InterfaceRefreshLoadMore.RefreshLoadMoreCallback {
            override fun onRefresh() {
                page = 1
                netData()
            }

            override fun onLoadMore() {
                page++
                netData()
            }

            override fun onPushDistance(distance: Int) {

            }

            override fun onPullDistance(distance: Int) {

            }
        })
        rootView.mRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(mRecyclerview: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(mRecyclerview, dx, dy)
                val h = ImageUtil.dip2px(182f)
                if (Math.abs(verticalOffset) >= h) {
                    rootView.superSwipeRefreshLayout.isEnabled = dy > 0
                } else {
                    rootView.superSwipeRefreshLayout.isEnabled = false
                }
            }
        })
    }

    private fun isNotDataView() {
        if (null != rootView.tv_no_data) {
            if (musicListMusicBeanList.size > 0) {
                rootView.tv_no_data.visibility = View.GONE
            } else {
                rootView.tv_no_data.visibility = View.VISIBLE
            }
        }
    }

    override fun onVisibleToUser() {
        addListener()
    }

    override fun onInvisibleToUser() {
        removeListener()
    }

    fun refreshData() {
        if (null != musicIanMusicAdapter) {
            musicIanMusicAdapter?.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (null != interfaceRefreshLoadMore) {
            interfaceRefreshLoadMore?.resetRefreshView()
        }
    }

    private fun addListener() {
        val activity = activity as MusicIanDetailsActivity
        if (activity != null && !activity.isFinishing && activity is MusicIanDetailsActivity) {
            activity.addListener(this)
        }
    }

    private fun removeListener() {
        val activity = activity as MusicIanDetailsActivity
        if (activity != null && !activity.isFinishing && activity is MusicIanDetailsActivity) {
            activity.removeListener(this)
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        this.verticalOffset = verticalOffset
        if (musicListMusicBeanList.size > 9) {
            if (verticalOffset == 0) {
                rootView.superSwipeRefreshLayout.isEnabled = true
            }
        } else {
            rootView.superSwipeRefreshLayout.isEnabled = verticalOffset == 0
        }
    }

    companion object {
        private const val TAG = "MusicFragment"
    }
    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart("musician_music")

    }

    override fun onDestroy() {
        super.onDestroy()
        MobclickAgent.onPageEnd("musician_music");

    }

}