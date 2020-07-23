package com.mxkj.yuanyintang.mainui.myself.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.bean.TabEntity
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder
import com.mxkj.yuanyintang.utils.uiutils.Toast
import com.mxkj.yuanyintang.utils.rxbus.RxBus
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber
import com.mxkj.yuanyintang.utils.rxbus.event.RefreshDataEvent

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import butterknife.BindView
import butterknife.ButterKnife
import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog
import com.mxkj.yuanyintang.base.dialog.BaseThreeConfirmDialog
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore
import com.mxkj.yuanyintang.mainui.home.adapter.MusicianSongsAdapter
import com.mxkj.yuanyintang.mainui.home.bean.MusicIanSongSheetBean
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.myself.activity.EditSongActivity
import com.mxkj.yuanyintang.mainui.myself.apdater.MySongListAdapter
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.umeng.socialize.utils.Log
import com.yanzhenjie.recyclerview.swipe.SwipeMenu
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import okhttp3.Headers
import kotlinx.android.synthetic.main.activity_my_song_list.*
import kotlinx.android.synthetic.main.dialog_new_song.*
import kotlinx.android.synthetic.main.fragment_musician_song_sheet.*

class MySongListActivity : StandardUiActivity() {
    var count=1
    private var dBuilder: DiaLogBuilder? = null
    private lateinit var mySongListAdapter: MusicianSongsAdapter
    private var MusicIanSongSheetBeanList: MutableList<MusicIanSongSheetBean.DataBean> = ArrayList()
    private lateinit var mRefreshSongData: Disposable
    var type = "1"
        private set
    internal var page = 1
    lateinit var interfaceRefreshLoadMore: InterfaceRefreshLoadMore
    private lateinit var new_song_rl: RelativeLayout
    private val swipeMenuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(swipeLeftMenu: SwipeMenu, swipeRightMenu: SwipeMenu, viewType: Int) {
            val width = resources.getDimensionPixelSize(R.dimen.dimen_56)
            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            run {

            }
            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            run {
                val editItem = SwipeMenuItem(this@MySongListActivity)
                        .setBackgroundColor(ContextCompat.getColor(this@MySongListActivity, R.color.grey_cc_text))
                        .setText("") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setTextSize(12)
                        .setWidth(0)
                        .setHeight(height)

                val deleteItem = SwipeMenuItem(this@MySongListActivity)
                        .setBackgroundColor(ContextCompat.getColor(this@MySongListActivity, R.color.base_red))
                        .setText("  删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setTextSize(12)
                        .setWidth(width)
                        .setHeight(height)
                if (TextUtils.equals("1", type)) {
                    swipeRightMenu.addMenuItem(editItem)// 添加一个按钮到右侧侧菜单。
                    swipeRightMenu.addMenuItem(deleteItem)// 添加一个按钮到右侧侧菜单。
                } else {
                    swipeRightMenu.addMenuItem(deleteItem)// 添加一个按钮到右侧侧菜单。
                }
            }
        }
    }
    override val isVisibilityBottomPlayer: Boolean
        get() = false


    //操作是删除和公开操作在适配器中，无法刷新数据，通过适配器吧potion穿过去（主要是操作再要activity中进行，potion不穿都行），在通过activity中更新数据
    internal var myHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> {
                    val bundle = msg.data
                    val po = bundle.getInt("position")
                    netData()
//                    mySongListAdapter.notifyDataSetChanged()
                }
                2 -> {
                    val bundle = msg.data
                    val po = bundle.getInt("size")
                    if (TextUtils.equals("1", type)) {
                        setTitleText("创建的歌单("+po+")")
                    } else if (TextUtils.equals("2", type)) {
                        setTitleText("收藏的歌单("+po+")")
                    }
                }
            }
        }
    }




    public override fun setLayoutId(): Int {
        return R.layout.activity_my_song_list
    }

    override fun initView() {
        type = intent.getStringExtra("type")
        initSwipeRecyclerView()
        new_song_rl = findViewById(R.id.new_song_rl)
        if (TextUtils.equals("1", type)) {
            setTitleText("创建的歌单")
//            setRightButtonText("新建歌单")
        } else if (TextUtils.equals("2", type)) {
            setTitleText("收藏的歌单")
            new_song_rl.visibility = View.GONE
        }

        navigationBar.getRightButton().setTextColor(ContextCompat.getColor(this, R.color.color_17_text))
        RxView.clicks(navigationBar.getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { finish() }

        initNewSongDialog()

//        RxView.clicks(navigationBar.getRightButton())
        RxView.clicks(new_song_rl)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    if (null != dBuilder) {
                        dBuilder!!.show()
                    }
                }

    }

    private fun initNewSongDialog() {
        val view = View.inflate(this, R.layout.dialog_new_song, null)
        val tv_cancle = view.findViewById<TextView>(R.id.tv_cancle)
        val tv_confirm = view.findViewById<TextView>(R.id.tv_confirm)
        val et_song_content = view.findViewById<EditText>(R.id.et_song_content)

        val private_songlist = view.findViewById<TextView>(R.id.private_songlist)
        val check_newsonglist = view.findViewById<CheckBox>(R.id.check_newsonglist)

        dBuilder = DiaLogBuilder(this)
                .setContentView(view)
                .setFullScreen()
                .setGrvier(Gravity.CENTER)
                .setCanceledOnTouchOutside(true)
                .setAniMo(R.anim.popup_in)
        RxView.clicks(tv_confirm)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(Consumer {
                    if (TextUtils.isEmpty(et_song_content.text.toString())) {
                        Toast.create(this@MySongListActivity).show("请输入歌单名字")
                        return@Consumer
                    }

                    if(check_newsonglist.isChecked==true){
                        newSong(et_song_content.text.toString(), 1.toString())
                    }else{
                        newSong(et_song_content.text.toString(),0.toString())
                    }

                    et_song_content.setText("")
                    dBuilder!!.setDismiss()
                })
        tv_cancle.setOnClickListener { dBuilder!!.setDismiss() }

        check_newsonglist.setOnClickListener {
            if(count==1){
                check_newsonglist.setChecked(true);
                count =2
                private_songlist.setText("仅自己可见")
            }else{
                check_newsonglist.setChecked(false);
                count = 1
                private_songlist.setText("仅自己可见")
            }

        }

    }

    private fun delCollection(dataBean: MusicIanSongSheetBean.DataBean) {
        if (this is BaseActivity) {
            (this as BaseActivity).MaterialDialog("确定要删除这个歌单吗？", null, null) { code ->
                if (code == 1) {
                    NetWork.delCollectionSong(this, dataBean.id.toString() + "", object : NetWork.TokenCallBack {

                        override fun doNext(resultData: String, headers: Headers?) {
                            page = 1
                            netData()
                        }

                        override fun doError(msg: String) {

                        }

                        override fun doResult() {

                        }
                    })
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        netData()
    }

    private fun cancleCollection(dataBean: MusicIanSongSheetBean.DataBean) {
        NetWork.getSongCollect(this, dataBean.id.toString() + "", object : NetWork.TokenCallBack {

            override fun doNext(resultData: String, headers: Headers?) {
                page = 1
                netData()
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    private fun netData() {
//        别人的
        when {
            intent.getIntExtra("uid", 0) != 0 -> //            创建的
                when {
                    TextUtils.equals("1", type) -> NetWork.getMusicianSong(this, 1, 5, intent.getIntExtra("uid", 0).toString(), object : NetWork.TokenCallBack {
                        override fun doNext(resultData: String, headers: Headers?) {
                            val musicIanSongSheetBean = JSON.parseObject(resultData, MusicIanSongSheetBean::class.java)
                            val dataList = JSON.toJSONString(musicIanSongSheetBean.data)
                            val list = JSON.parseArray(dataList, MusicIanSongSheetBean.DataBean::class.java)
                            //通过handler来统计刷新标题上歌单的数量
                            val message = Message.obtain()
                            message.what = 2
                            val bundle = Bundle()
                            bundle.putInt("size", list.size)
                            message.data = bundle
                            myHandler.sendMessage(message)
                            if (page == 1) {
                                MusicIanSongSheetBeanList.clear()
                            }
                            MusicIanSongSheetBeanList.addAll(list)
                            mySongListAdapter.setNewData(MusicIanSongSheetBeanList)
                            interfaceRefreshLoadMore.setStopRefreshing()
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
                    TextUtils.equals("2", type) -> NetWork.getMusicianSongCollection(this, page, intent.getIntExtra("uid", 0).toString(), object : NetWork.TokenCallBack {
                        override fun doNext(resultData: String, headers: Headers?) {

                            val musicIanSongSheetBean = JSON.parseObject(resultData, MusicIanSongSheetBean::class.java)
                            val dataList = JSON.toJSONString(musicIanSongSheetBean.data)
                            val list = JSON.parseArray(dataList, MusicIanSongSheetBean.DataBean::class.java)
//                            val parseObject = JSON.parseObject(resultData)
//                            val jsonObject = parseObject.getJSONObject("data")
//                            val jsonArray = jsonObject.getJSONArray("data")
//                            val dataList = JSON.toJSONString(jsonArray)
//                            val list = JSON.parseArray(dataList, MusicIanSongSheetBean.DataBean::class.java)
                            //通过handler来统计刷新标题上歌单的数量
                            val message = Message.obtain()
                            message.what = 2
                            val bundle = Bundle()
                            bundle.putInt("size", list.size)
                            message.data = bundle
                            myHandler.sendMessage(message)
                            if (list.isNotEmpty()) {
                                if (page == 1) {
                                    MusicIanSongSheetBeanList.clear()
                                }
                                MusicIanSongSheetBeanList.addAll(list)
                                mySongListAdapter.setNewData(MusicIanSongSheetBeanList)
                                interfaceRefreshLoadMore.setStopRefreshing()
                            }
                        }

                        override fun doError(msg: String) {

                        }

                        override fun doResult() {

                        }
                    })
                }
            else -> //        自己的
                if (TextUtils.equals("1", type)) {
                    NetWork.getMemberSong(this, page, object : NetWork.TokenCallBack {
                        override fun doNext(resultData: String, headers: Headers?) {
//                            val MusicIanSongSheetBean = JSON.parseObject(resultData, MusicIanSongSheetBean::class.java)
//                            refreshData(MusicIanSongSheetBean)

                            val parseObject = JSON.parseObject(resultData)
                            val jsonObject = parseObject.getJSONObject("data")
                            val jsonArray = jsonObject.getJSONArray("data")
                            val dataList = JSON.toJSONString(jsonArray)
                            val list = JSON.parseArray(dataList, MusicIanSongSheetBean.DataBean::class.java)
                            //通过handler来统计刷新标题上歌单的数量
                            val message = Message.obtain()
                            message.what = 2
                            val bundle = Bundle()
                            bundle.putInt("size", list.size)
                            message.data = bundle
                            myHandler.sendMessage(message)

                            if (page == 1) {
                                MusicIanSongSheetBeanList.clear()
                            }
                            MusicIanSongSheetBeanList.addAll(list)
                            mySongListAdapter.setNewData(MusicIanSongSheetBeanList)
                            interfaceRefreshLoadMore.setStopRefreshing()
                        }

                        override fun doError(msg: String) {
                            if (page > 1) {
                                page--
                            }
                            interfaceRefreshLoadMore.setStopRefreshing()
                        }

                        override fun doResult() {

                        }
                    })
                }
                else if (TextUtils.equals("2", type)) {
                    NetWork.getMemberScollection(this, page, object : NetWork.TokenCallBack {
                        override fun doNext(resultData: String, headers: Headers?) {
                            val parseObject = JSON.parseObject(resultData)
                            val jsonObject = parseObject.getJSONObject("data")
                            val jsonArray = jsonObject.getJSONArray("data")
                            val dataList = JSON.toJSONString(jsonArray)
                            val list = JSON.parseArray(dataList, MusicIanSongSheetBean.DataBean::class.java)
                            //通过handler来统计刷新标题上歌单的数量
                            val message = Message.obtain()
                            message.what = 2
                            val bundle = Bundle()
                            bundle.putInt("size", list.size)
                            message.data = bundle
                            myHandler.sendMessage(message)
                            if (page == 1) {
                                MusicIanSongSheetBeanList.clear()
                            }
                            MusicIanSongSheetBeanList.addAll(list)
                            mySongListAdapter.setNewData(MusicIanSongSheetBeanList)
                            interfaceRefreshLoadMore.setStopRefreshing()
                        }

                        override fun doError(msg: String) {

                        }

                        override fun doResult() {

                        }
                    })
                }
        }

    }

    private fun newSong(s: String) {
        NetWork.getNewSong(this, s, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                setSnackBar("新建成功", "", R.drawable.icon_success)
                netData()
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    private fun newSong(s: String,is_private :String) {
//        NetWork.getNewSong(this, s, object : NetWork.TokenCallBack {
//            override fun doNext(resultData: String, headers: Headers?) {
//                setSnackBar("新建成功", "", R.drawable.icon_success)
//                netData()
//            }
//
//            override fun doError(msg: String) {
//
//            }
//
//            override fun doResult() {
//
//            }
//        })
        NetWork.getNewSongList(this, s,is_private, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                setSnackBar("新建成功", "", R.drawable.icon_success)
                netData()
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    private fun initSwipeRecyclerView() {
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setSwipeMenuCreator(swipeMenuCreator)
//        mySongListAdapter = MusicianSongsAdapter(MusicIanSongSheetBeanList,type)
        mySongListAdapter = MusicianSongsAdapter(MusicIanSongSheetBeanList,type,myHandler)
        recyclerview.addItemDecoration(MyRecyclerDetorration(this, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(this, 0.6f), R.color.dividing_line_color, true))
        recyclerview.adapter = mySongListAdapter
        recyclerview.setSwipeItemClickListener { _, _ -> }
        recyclerview.setSwipeMenuItemClickListener { menuBridge ->
            menuBridge.closeMenu()
            val menuPosition = menuBridge.position
            when (menuPosition) {
                0 -> if (TextUtils.equals("1", type)) {
                    val bundle = Bundle()
                    bundle.putSerializable("data", MusicIanSongSheetBeanList[menuBridge.adapterPosition])
                    goActivity(EditSongActivity::class.java, bundle)
                } else if (TextUtils.equals("2", type)) {
                    cancleCollection(MusicIanSongSheetBeanList[menuBridge.adapterPosition])
                }
                1 -> delCollection(MusicIanSongSheetBeanList[menuBridge.adapterPosition])
            }
        }
        interfaceRefreshLoadMore = InterfaceRefreshLoadMore(superSwipeRefreshLayout, this, object : InterfaceRefreshLoadMore.RefreshLoadMoreCallback {
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
    }

    override fun initData() {

    }
    override fun initEvent() {}

    override fun onDestroy() {
        super.onDestroy()
    }
}
