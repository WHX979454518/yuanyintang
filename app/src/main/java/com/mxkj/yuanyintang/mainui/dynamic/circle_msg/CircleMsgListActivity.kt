package com.mxkj.yuanyintang.mainui.dynamic.circle_msg

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater

import com.alibaba.fastjson.JSON
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.dynamic.activity.DynamicDetial
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.app.CommonUtils

import java.util.ArrayList

import butterknife.ButterKnife
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import kotlinx.android.synthetic.main.activity_circle_msg_list.*
class CircleMsgListActivity : StandardUiActivity() {
    internal lateinit var interfaceRefreshLoadMore: InterfaceRefreshLoadMore
    internal var msgAdapter: CircleMsgAdapter? = null
    internal var dataList: MutableList<CircleMsgBean.DataBeanX.DataBean>? = ArrayList()
    private var page = 1
    internal var loadingHistory: Boolean = false//标志位，当前是不是加载历史消息的状态

    override val isVisibilityBottomPlayer: Boolean
        get() = false

    override fun setLayoutId(): Int {
        return R.layout.activity_circle_msg_list
    }

    override fun initView() {
        setTitleText("动态消息")
        initAdapter()
    }

    private fun initAdapter() {
        if (msgAdapter == null) {
            msgAdapter = CircleMsgAdapter(dataList!!)
            msg_recycler.layoutManager = LinearLayoutManager(this)
            msg_recycler.addItemDecoration(MyRecyclerDetorration(this, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(this, 2f), R.color.bg_color, true))
            msg_recycler.setHasFixedSize(true)
            msg_recycler.adapter = msgAdapter
            msgAdapter!!.setOnItemClickListener { _, _, i ->
                val dataBean = dataList!![i]
                Log.e(TAG, "onItemClick: ")
                if (dataBean != null) {
                    if (dataBean.from_type == TYPE_HISTORY) {
                        if (!loadingHistory) {
                            loadingHistory = true
                            dataList!![i].title = "以下为历史消息"
                            initData()
                        }
                    } else {
                        if (dataBean.item_info != null) {
                            val intent = Intent(this@CircleMsgListActivity, DynamicDetial::class.java)
                            intent.putExtra("dynamicId", dataBean.item_info!!.id)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    override fun initData() {
        NetWork.getMsgList(this, page, 5, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                interfaceRefreshLoadMore.setStopRefreshing()
                val msgListean = JSON.parseObject(resultData, CircleMsgBean::class.java)
                val dataX = msgListean.data
                val data = dataX!!.data
                if (data != null && data.isNotEmpty()) {
                    Observable.fromArray<List<CircleMsgBean.DataBeanX.DataBean>>(data)
                            .flatMap<CircleMsgBean.DataBeanX.DataBean> { musicListMusicBeen -> Observable.fromIterable<CircleMsgBean.DataBeanX.DataBean>(musicListMusicBeen) }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<CircleMsgBean.DataBeanX.DataBean> {
                                override fun onSubscribe(@NonNull d: Disposable) {
                                }
                                override fun onNext(@NonNull dataBean: CircleMsgBean.DataBeanX.DataBean) {
                                    if (!loadingHistory) {//当前加载的是未读消息
                                        if (dataBean.status == 0) {
                                            dataList!!.add(dataBean)
                                        }
                                    } else {//当前加载的是已读消息
                                        if (dataBean.status == 1) {
                                            dataList!!.add(dataBean)
                                        }
                                    }
                                }

                                override fun onError(@NonNull e: Throwable) {

                                }

                                override fun onComplete() {
                                    if (!loadingHistory) {//未读消息加载完
                                        val dataBean = CircleMsgBean.DataBeanX.DataBean()
                                        dataBean.from_type = TYPE_HISTORY
                                        dataList!!.add(dataBean)
                                    }
                                    if (msg_recycler.scrollState === RecyclerView.SCROLL_STATE_IDLE && msg_recycler.isComputingLayout === false) {
                                        msgAdapter!!.setNewData(dataList)
                                    }
                                }
                            })
                } else {
                    if (loadingHistory) {
                        if (dataList != null && dataList!!.size > 0) {
                            Observable.fromArray<List<CircleMsgBean.DataBeanX.DataBean>>(dataList!!)
                                    .flatMap<CircleMsgBean.DataBeanX.DataBean> { musicListMusicBeen -> Observable.fromIterable<CircleMsgBean.DataBeanX.DataBean>(musicListMusicBeen) }.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(object : Observer<CircleMsgBean.DataBeanX.DataBean> {
                                        override fun onSubscribe(@NonNull d: Disposable) {

                                        }

                                        override fun onNext(@NonNull dataBean: CircleMsgBean.DataBeanX.DataBean) {
                                            if (dataBean.from_type == TYPE_HISTORY) {
                                                dataList!!.remove(dataBean)
                                            }

                                        }

                                        override fun onError(@NonNull e: Throwable) {

                                        }

                                        override fun onComplete() {
                                            msgAdapter!!.setNewData(dataList)
                                            if (msgAdapter!!.footerLayoutCount == 0) {
                                                msgAdapter!!.addFooterView(LayoutInflater.from(this@CircleMsgListActivity).inflate(R.layout.no_more_data_text, null))
                                            }
                                        }
                                    })
                        }
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

    override fun initEvent() {
        interfaceRefreshLoadMore = InterfaceRefreshLoadMore(swipe_refresh, this, object : InterfaceRefreshLoadMore.RefreshLoadMoreCallback {
            override fun onRefresh() {
                page = 1
                loadingHistory = false
                dataList!!.clear()
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
    }

    override fun onDestroy() {
        super.onDestroy()
        NetWork.readAllMsg(this@CircleMsgListActivity, HttpParams("type", "5"), object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {

            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    companion object {
        var TYPE_HISTORY = 1024
    }
}
