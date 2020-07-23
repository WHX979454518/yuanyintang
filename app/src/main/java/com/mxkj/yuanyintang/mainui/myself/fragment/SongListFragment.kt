//package com.mxkj.yuanyintang.mainui.myself.fragment
//
//import android.graphics.Color
//import android.os.Bundle
//import android.support.v4.content.ContextCompat
//import android.support.v7.widget.LinearLayoutManager
//import android.text.TextUtils
//import android.view.ViewGroup
//
//import com.alibaba.fastjson.JSON
//import com.mxkj.yuanyintang.R
//import com.mxkj.yuanyintang.base.activity.BaseActivity
//import com.mxkj.yuanyintang.base.fragment.BaseFragment
//import com.mxkj.yuanyintang.net.NetWork
//import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
//import com.mxkj.yuanyintang.mainui.myself.activity.EditSongActivity
//import com.mxkj.yuanyintang.mainui.myself.apdater.MySongListAdapter
//import com.mxkj.yuanyintang.mainui.myself.bean.MySongListBean
//import com.mxkj.yuanyintang.utils.rxbus.RxBus
//import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber
//import com.mxkj.yuanyintang.utils.rxbus.event.RefreshSongListEvent
//import com.yanzhenjie.recyclerview.swipe.SwipeMenu
//import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
//import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
//
//import java.util.ArrayList
//
//import io.reactivex.disposables.Disposable
//import okhttp3.Headers
//import kotlinx.android.synthetic.main.fragment_song_list.view.*
//
//class SongListFragment : BaseFragment() {
//
//
//    override val layoutId: Int
//        get() = R.layout.fragment_song_list
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        type = arguments.getString("type")
//        initSwipeRecyclerView()
//        netData()
//        initEvent()
//    }
//
//    private fun initEvent() {
//        mRefreshSongData = RxBus.getDefault().toObservable(RefreshSongListEvent::class.java)
//                .subscribeWith(object : RxBusSubscriber<RefreshSongListEvent>() {
//                    @Throws(Exception::class)
//                    override fun onEvent(refreshSongListEvent: RefreshSongListEvent) {
//                        if (TextUtils.equals("1", refreshSongListEvent.type)) {
//                            page = 1
//                            netData()
//                        } else {
//                            page = 1
//                            netData()
//                        }
//                    }
//                })
//        RxBus.getDefault().add(mRefreshSongData)
//    }
//
//
//
//
//
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        interfaceRefreshLoadMore.resetRefreshView()
//        RxBus.getDefault().remove(mRefreshSongData)
//    }
//
//    fun refreshNetData() {
//        page = 1
//        netData()
//    }
//
//    companion object {
//        private const val TAG = "SongListFragment"
//    }
//}
