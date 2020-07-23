package com.mxkj.yuanyintang.extraui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*

import com.alibaba.fastjson.JSON
import com.jakewharton.rxbinding2.view.RxView
import com.linsh.lshutils.utils.LshContextUtils.getSystemService
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.R.id.recyclerviewlist
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment
import com.mxkj.yuanyintang.database.updatafilesql.TasksManager
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.database.updatafilesql.UpDataFileDao
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.comment.CommentActivity
import com.mxkj.yuanyintang.mainui.home.bean.DownLoadFileBean
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.bean.MusicOperationBean
import com.mxkj.yuanyintang.im.ui.EaseChatFragment
import com.mxkj.yuanyintang.mainui.home.activity.ContainSongListActivity
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.mainui.message.activity.MyCollectionSongListActivity
import com.mxkj.yuanyintang.mainui.message.activity.MyCollectionSongListActivity.MUSIC_ID
import com.mxkj.yuanyintang.mainui.message.adapter.MyCollectionSongListAdapter
import com.mxkj.yuanyintang.mainui.myself.bean.MySongListBean
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.uiutils.Toast
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.rxbus.RxBus
import com.mxkj.yuanyintang.utils.rxbus.event.MusicOpenationEvent

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import io.reactivex.functions.Consumer
import okhttp3.Headers

import com.mxkj.yuanyintang.musicplayer.service.MediaService.bean
import com.mxkj.yuanyintang.utils.image.IMGCompressUtils.count
import com.mxkj.yuanyintang.utils.rxbus.event.MyCollectionRefreshEvent
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder
import com.mxkj.yuanyintang.video.MikyouCommonDialog
import com.mxkj.yuanyintang.video.MvVideoActivity
import com.mxkj.yuanyintang.video.MvVideoActivitykt
import com.umeng.message.proguard.T
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.activity_regist.*
import kotlinx.android.synthetic.main.dialog_music_operation.*

@SuppressLint("ValidFragment")
class MusicOperationDialog : BaseDialogFragment {
    internal var musicBean: MusicBean? = null
    private lateinit var musicOperationBeanList: ArrayList<MusicOperationBean>
    private var musicOperationAdapter: MusicOperationAdapter? = null
    internal var fragmentManager: FragmentManager? = null


    internal lateinit var tv_canclel:TextView
    internal lateinit var recyclerviewlist:RecyclerView
    internal lateinit var layout_new_songlist:LinearLayout


    internal lateinit var wm:WindowManager;
    internal lateinit var progressbar: ProgressBar
    internal var inflate:View? = null
    internal var dialog:Dialog? = null

    //    internal var caijiAllFileFireEquipmentActivityadapter: CaijiAllFileFireEquipmentActivityadapter

    val IS_MULTI_SELECT = "_is_multi_select"//是否多选
    val IS_NEW_MUSIC = "_is_new_music"//是否有新建歌单
    internal var layout_new_song: View? = null
    private val page = 1
    private var isMultiSelect: Boolean? = false
    internal lateinit var myColloectionSongListAdapter: MyCollectionSongListAdapter
    internal var dataBeanList: MutableList<MySongListBean.DataBeanX.DataBean> = ArrayList()

    private var isConfirm: Boolean? = false
    private var music_id: String? = null
    private var view_type: String? = null


    private var diaLogBuilder: DiaLogBuilder? = null




    var count=1



    override val contentViewLayoutID: Int
        get() = R.layout.dialog_music_operation

    override val isBack: Boolean?
        get() = false

    private var setMusicOperationListener: SetMusicOperationListener? = null

    constructor()

    constructor(infoBean: MusicInfo.DataBean) {
        val musicBean = MusicBean()
        musicBean.setMusic_id(infoBean.id.toString() + "")
        musicBean.setMusic_name(infoBean.title)
    }

    constructor(musicBean: MusicBean, fragmentManager: FragmentManager) {
        this.musicBean = musicBean
        this.fragmentManager = fragmentManager
    }

    constructor(musicBean: MusicBean, fragmentManager: FragmentManager, c: Context) {
        this.musicBean = musicBean
        this.fragmentManager = fragmentManager
    }

    override fun style(): Int {
        return 0
    }

    override fun initView() {




        initDialogBuilder()





        RxView.clicks(tv_cancel)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { dismiss() }
        musicOperationBeanList = ArrayList()
        var type = 0
        if (null != musicBean) {
            type = musicBean?.getType()!!
        }
        if (type == 6) {
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_includ_list_black, "包含这首歌的歌单", 0))
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_nextsong_list_black, "下一曲播放", 1))
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_addsong_list_black, "添加到歌单", 2))
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_musician_list_black, if (null != musicBean) "音乐人：" + StringUtils.isEmpty(musicBean?.getMusician_name()) else "音乐人：", 3))
//            musicOperationBeanList.add(MusicOperationBean(R.drawable.icon_operation_collection, "收藏", 4))//（" + ((null != musicBean) ? ((musicBean.getCollection() == 0) ? "未收藏）" : "取消收藏）") : "收藏")
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_comment_list_black, "评论", 6))
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_share_list_black, "分享", 7))
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_delect, "删除", 8))


            if (musicBean?.getSong_id()!! > 0 && !TextUtils.isEmpty(musicBean?.getSongName())) {
                musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_xiaxian_list_black, "来源：" + if (TextUtils.isEmpty(musicBean?.getSongName())) "未知" else musicBean?.getSongName(), 9))
            }
        }else if(type == 11){
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_delect, "删除", 16))
        }



        else if(type == 10){


            var status = ""
            if(null!=musicBean?.getStatu()!!){
                status = musicBean?.getStatu()!!
            }
            if(status.equals("0") || status == "0"){
//                musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_delect, "删除", 8))
                musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_delect, "删除", 16))
            }else{



                musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_includ_list_black, "包含这首歌的歌单", 0))
                musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_nextsong_list_black, "下一曲播放", 1))
                musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_addsong_list_black, "添加到歌单", 2))
                musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_musician_list_black, if (null != musicBean) "音乐人：" + StringUtils.isEmpty(musicBean?.getMusician_name()) else "音乐人：", 3))
                musicOperationBeanList.add(MusicOperationBean(R.drawable.icon_operation_collection, "取消收藏", 4))
                musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_share_list_black, "分享", 7))
                //新添加mv
                Log.e("pppp",""+musicBean!!.mv)
                Log.e("oooo",""+ musicBean!!.mvInfoBean.toString())
                if (!musicBean!!.mv.equals("")) {
                    musicOperationBeanList.add(MusicOperationBean(R.drawable.icon_addsong_mv_black, "播放MV", 10))
                }
//            musicOperationBeanList.add(MusicOperationBean(R.drawable.icon_operation_updata, "下载", 5))
                if(!UpDataFileDao(mActivity).isQueryData("musicId", musicBean?.getMusic_id())){
                    musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_down_list_black, "下载", 5))
                }
                musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_comment_list_black, "评论（" + (if (null != musicBean) musicBean?.getCommentNum() else "评论") + "）", 6))

                if (musicBean?.getSong_id()!! > 0 && !TextUtils.isEmpty(musicBean?.getSongName())) {
                    musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_xiaxian_list_black, "来源：" + if (TextUtils.isEmpty(musicBean?.getSongName())) "未知" else musicBean?.getSongName(), 9))
                }
            }

        }else if(type == 15){
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_includ_list_black, "包含这首歌的歌单", 0))
//            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_nextsong_list_black, "下一曲播放", 1))
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_addsong_list_black, "添加到歌单", 2))
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_musician_list_black, if (null != musicBean) "音乐人：" + StringUtils.isEmpty(musicBean?.getMusician_name()) else "音乐人：", 3))
            if (type != 7) {
//                musicOperationBeanList.add(MusicOperationBean(R.drawable.icon_operation_collection, "收藏", 4))
            }
            if (!musicBean!!.mv.equals("")) {
                musicOperationBeanList.add(MusicOperationBean(R.drawable.icon_addsong_mv_black, "播放MV", 10))
            }
            if(!UpDataFileDao(mActivity).isQueryData("musicId", musicBean?.getMusic_id())){
                musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_down_list_black, "下载", 5))
            }

            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_comment_list_black, "评论（" + (if (null != musicBean) musicBean?.getCommentNum() else "评论") + "）", 6))

            if (musicBean?.getSong_id()!! > 0 && !TextUtils.isEmpty(musicBean?.getSongName())) {
                musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_xiaxian_list_black, "来源：" + if (TextUtils.isEmpty(musicBean?.getSongName())) "未知" else musicBean?.getSongName(), 9))
            }
        }

        else {
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_includ_list_black, "包含这首歌的歌单", 0))
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_nextsong_list_black, "下一曲播放", 1))
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_addsong_list_black, "添加到歌单", 2))
            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_musician_list_black, if (null != musicBean) "音乐人：" + StringUtils.isEmpty(musicBean?.getMusician_name()) else "音乐人：", 3))
            if (type != 7) {
//                musicOperationBeanList.add(MusicOperationBean(R.drawable.icon_operation_collection, "收藏", 4))
            }
            //新添加mv
            Log.e("pppp",""+musicBean!!.mv)
            Log.e("oooo",""+ musicBean!!.mvInfoBean.toString())
            if (!musicBean!!.mv.equals("")) {
                musicOperationBeanList.add(MusicOperationBean(R.drawable.icon_addsong_mv_black, "播放MV", 10))
            }
//            musicOperationBeanList.add(MusicOperationBean(R.drawable.icon_operation_updata, "下载", 5))
            if(!UpDataFileDao(mActivity).isQueryData("musicId", musicBean?.getMusic_id())){
                musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_down_list_black, "下载", 5))
            }

            musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_comment_list_black, "评论（" + (if (null != musicBean) musicBean?.getCommentNum() else "评论") + "）", 6))

            if (musicBean?.getSong_id()!! > 0 && !TextUtils.isEmpty(musicBean?.getSongName())) {
                musicOperationBeanList.add(MusicOperationBean(R.mipmap.icon_xiaxian_list_black, "来源：" + if (TextUtils.isEmpty(musicBean?.getSongName())) "未知" else musicBean?.getSongName(), 9))
            }
        }



        musicOperationAdapter = MusicOperationAdapter(musicOperationBeanList)
        recyclerview.layoutManager = LinearLayoutManager(mActivity)
        recyclerview.adapter = musicOperationAdapter
        musicOperationAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, _ -> }
        if (null != musicBean) {
            if(musicBean!!.type==2){
                tv_music_name2.visibility = View.VISIBLE
                tv_music_name.visibility = View.GONE
                tv_music_name2?.text = "单曲：" + StringUtils.isEmpty(musicBean?.getMusic_name())
            }else{
//                tv_music_name?.text = "歌曲：" + StringUtils.isEmpty(musicBean?.getMusic_name())
                tv_music_name2.visibility = View.VISIBLE
                tv_music_name.visibility = View.GONE
                tv_music_name2?.text = "单曲：" + StringUtils.isEmpty(musicBean?.getMusic_name())
            }

            RxView.clicks(tv_music_name)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        musicBean?.let {
                            if (!TextUtils.isEmpty(it.getMusic_id())) {
                                PlayCtrlUtil.play(mActivity!!, it.getMusic_id()!!.toInt(), 0)
                            }
                        }
                    }
            RxView.clicks(tv_music_name2)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        musicBean?.let {
                            if (!TextUtils.isEmpty(it.getMusic_id())) {
                                PlayCtrlUtil.play(mActivity!!, it.getMusic_id()!!.toInt(), 0)
                            }
                        }
                    }
        }
    }

    private fun validateDownLoadFile() {
        if (null == musicBean) {
            return
        }
        if (null != mActivity) {
            NetWork.getDownBit(mActivity!!, musicBean?.getMusic_id()!!, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    val downLoadFileBit = JSON.parseObject(resultData, DownLoadFileBean.DownLoadFileBitBean::class.java)
                    if (null != downLoadFileBit) {
                        downLoadFile(downLoadFileBit.data.isSd_sdl, downLoadFileBit.data.isHigh_sdl)
                    } else {
                        Toast.create(mActivity).show("没有音乐可以下载~")
                    }
                    dismiss()
                }

                override fun doError(msg: String) {
                    if (null != MainApplication.application?.currentActivity) {
                        Toast.create(MainApplication.application?.currentActivity).show("" + msg)
                        dismiss()
                    }
                }

                override fun doResult() {

                }
            })
        }
    }

    private fun downLoadFile(sd_sdl: Boolean, high_sdl: Boolean) {
        val kbps = CacheUtils.getString(mActivity, Constants.User.MUSIC_KBP, "128")
        var type: String? = null
        when (kbps) {
            "128" -> type = "2"
            "320" -> type = when {
                high_sdl -> "1"
                sd_sdl -> "2"
                else -> "2"
            }
        }

        NetWork.getMusicDown(MainApplication.application!!, musicBean?.getMusic_id()!!, type!!, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                var data: String? = null
                try {
                    val `object` = JSONObject(resultData)
                    data = `object`.optString("data")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                if (!TextUtils.isEmpty(data)) {
                    musicBean?.ext = "." + StringUtils.parseSuffix(data)
                    TasksManager.getImpl().downLoadFile(musicBean!!, data!!, mActivity!!)
                }
                dismiss()
            }

            override fun doError(msg: String) {
                if (null != MainApplication.application!!.currentActivity) {
                    Toast.create(MainApplication.application!!.currentActivity).show("" + msg)
                    dismiss()
                }
                dismiss()
            }

            override fun doResult() {

            }
        })
    }

    internal inner class MusicOperationAdapter(data: List<MusicOperationBean>) : BaseQuickAdapter<MusicOperationBean, BaseViewHolder>(R.layout.item_music_operation, data) {

        override fun convert(helper: BaseViewHolder, item: MusicOperationBean, position: Int) {
            helper.setText(R.id.tv_title, StringUtils.isEmpty(item.title))
            helper.setImageResource(R.id.iv_icon, item.drawable)
            RxView.clicks(helper.getView(R.id.layout_click))
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe(Consumer {
                        val bundle = Bundle()
                        when (item.type) {
                            0 -> {
                                if (null == musicBean) {
                                    return@Consumer
                                }
                                bundle.putString(ContainSongListActivity.MUSIC_ID, StringUtils.isEmpty(musicBean?.getMusic_id()))
                                goActivity(ContainSongListActivity::class.java, bundle)
                            }
                            1 -> if (!TextUtils.isEmpty(musicBean?.getMusic_id())) {
                                PlayCtrlUtil.nextPlay(mActivity, Integer.parseInt(musicBean?.getMusic_id()))
                            }
                            2 -> {
                                if (null == musicBean) {
                                    return@Consumer
                                }
                                if (CacheUtils.getBoolean(mActivity, Constants.User.IS_LOGIN)) {
//                                    bundle.putBoolean(MyCollectionSongListActivity.IS_MULTI_SELECT, musicBean?.getMultiSelect()!!)
//                                    bundle.putString(MyCollectionSongListActivity.MUSIC_ID, musicBean?.getMusic_id())
//                                    bundle.putString(MyCollectionSongListActivity.VIEW_TYPE, "add")
//                                    goActivity(MyCollectionSongListActivity::class.java, bundle)


                                    dialog = Dialog(context, R.style.ActionSheetDialogStyle)
                                    //填充对话框的布局
                                    inflate = LayoutInflater.from(context).inflate(R.layout.dialog_newsonglist, null)
                                    //初始化控件

                                    //var recyclerviewlist= dialog!!.findViewById<RecyclerView>(R.id.recyclerviewlist)as RecyclerView
                                    recyclerviewlist = inflate.run { this!!.findViewById<RecyclerView>(R.id.recyclerviewlist) as RecyclerView }
                                    tv_canclel = inflate.run { this!!.findViewById<TextView>(R.id.tv_canclel) as TextView }

                                    layout_new_songlist = inflate.run { this!!.findViewById<LinearLayout>(R.id.layout_new_songlist) as LinearLayout }
                                    tv_canclel.setText("确定")


                                    //将布局设置给Dialog
                                    dialog!!.setContentView(inflate)
                                    //获取当前Activity所在的窗体
                                    val dialogWindow1 = dialog!!.window
                                    //设置Dialog从窗体底部弹出
                                    dialogWindow1!!.setGravity(Gravity.BOTTOM)
                                    //获得窗体的属性
                                    val lp1 = dialogWindow1.attributes
                                    lp1.y = 0//设置Dialog距离底部的距离
                                    wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager;
                                    val m1 = wm;
                                    val d1 = m1.getDefaultDisplay() // 获取屏幕宽、高度
                                    val p1 = dialogWindow1.attributes // 获取对话框当前的参数值
                                    p1.height = (d1.getHeight() * 0.6).toInt() // 高度设置为屏幕的0.6，根据实际情况调整
                                    p1.width = (d1.getWidth() * 0.9).toInt() // 宽度设置为屏幕的0.65，根据实际情况调整
                                    //    将属性设置给窗体
                                    dialogWindow1.attributes = lp1
                                    dialog!!.show()//显示对话框


                                    initData();
                                    //用recyclerview排列的

                                    tv_canclel.setOnClickListener {
                                        var dataBean: MySongListBean.DataBeanX.DataBean? = null
                                        for (i in dataBeanList.indices) {
                                            if (dataBeanList[i].check!!) {
                                                dataBean = dataBeanList[i]
                                                break
                                            }
                                        }
//                                        if (null != dataBean && null != dataBean.imgpic_info) {
                                        if (null != dataBean) {
                                            addMusicSong();
                                            dialog!!.hide()
                                        } else {
                                            Toast.create(mActivity).show("请选择歌单")
                                        }
                                    }


                                    layout_new_songlist.setOnClickListener {
                                        if (null != diaLogBuilder) {
                                            diaLogBuilder!!.show()
                                        }
                                    }



                                } else {
                                    goActivity(LoginRegMainPage::class.java)
                                }
                            }
                            3 -> {
                                bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, musicBean?.getUid().toString() + "")
                                goActivity(MusicIanDetailsActivity::class.java, bundle)
                            }
                            4 -> if (CacheUtils.getBoolean(mActivity, Constants.User.IS_LOGIN)) {
                                getCollection()
                            } else {
                                goActivity(LoginRegMainPage::class.java)
                            }
                            5 -> if (CacheUtils.getBoolean(mActivity, Constants.User.IS_LOGIN)) {
                                validateDownLoadFile()
                            } else {
                                Toast.create(mActivity).show("登录后才能下载")
                                goActivity(LoginRegMainPage::class.java)
                            }
                            6 -> {
                                if (TextUtils.isEmpty(musicBean?.getMusic_id())) {
                                    return@Consumer
                                }
                                bundle.putInt(CommentActivity.TYPE, musicBean?.getCommentType()!!)
                                bundle.putInt(CommentActivity.ITEM_ID, Integer.parseInt(musicBean?.getMusic_id()))
                                goActivity(CommentActivity::class.java, bundle)
                            }
                            7//分享
                            -> {
                                val shareBottomDialog = ShareBottomDialog(mContext, musicBean!!)
                                shareBottomDialog.show()
                            }
                            8//删除
                            -> {
                                if (null == musicBean) {
                                    return@Consumer
                                }
                                RxBus.getDefault().post(MusicOpenationEvent(musicBean?.getPosition()!!, musicBean?.getType()!!))
                            }
                            9//来源
                            -> {
                                bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, musicBean?.getSong_id().toString() + "")
                                goActivity(SongSheetDetailsActivity::class.java, bundle)
                            }
                            10//mv跳转
                            -> {
//                                val intent = Intent(mActivity, MvVideoActivitykt::class.java)
//                                var bundle = Bundle()
//                                Log.e("mmmmmmm",""+ musicBean!!.imgpic_info!!.link)
//                                Log.e("mmmmmmm",""+ musicBean!!.imgpic_info!!.link)
//                                bundle.putInt("mv", Integer.parseInt(musicBean?.getMusic_id()));
//                                bundle.putString("mvurl", musicBean!!.imgpic_info!!.link);
//                                bundle.putInt("uid", musicBean?.uid!!);
//                                bundle.putString("title", musicBean?.getMusician_name());
//                                bundle.putString("nickname", musicBean?.getMusic_name());
//                                bundle.putString("imgpic_link", musicBean!!.imgpic_info!!.link);
//                                bundle.putString("bioashi", 1.toString() + "")
//                                intent.putExtra("mvdate",bundle);
//                                mActivity!!.startActivity(intent)
                                toMV(musicBean?.getMusic_id()!!, musicBean!!.mvInfoBean!!.link!!,musicBean?.uid!!, musicBean?.getMusician_name()!!, musicBean?.getMusic_name()!!, musicBean!!.imgpic_info!!.link!!)
                            }
                            16//删除已经下线的歌曲
                            -> {
                                if (null == musicBean) {
                                    return@Consumer
                                }
                                val params = HttpParams()
                                params.put("id", musicBean?.getReportId()!!)
                                NetWork.delFromSongSheet(mContext, params, object : NetWork.TokenCallBack {
                                    override fun doNext(resultData: String, headers: Headers?) {
                                        setSnackBar("删除成功", "", R.drawable.icon_success)
                                        //这里删除以后仿取消收藏做的
                                        if (null != bean) {
                                            if (TextUtils.equals(musicBean?.getMusic_id(), bean?.id.toString() + "")) {
                                                bean?.collection = Integer.valueOf(musicBean?.getMusic_id())
                                                if (mActivity != null) {
                                                    mActivity?.sendBroadcast(Intent("cgCollect"))
                                                }
                                            }
                                        }
                                        if (null != setMusicOperationListener) {
                                            setMusicOperationListener?.onCollection(musicBean?.getCollection()!!, musicBean?.getType()!!)
                                        }
                                    }

                                    override fun doError(msg: String) {
                                        Log.e(BaseActivity.TAG, "doError: $msg")
                                        Log.e(BaseActivity.TAG, "从歌单删除！doError！！！: ")
                                        dismiss()
                                    }

                                    override fun doResult() {

                                    }
                                })
                            }
                        }
                        if (item.type != 4 || item.type != 5) {
                            dismiss()
                        }
                    })
        }
    }

    private fun getCollection() {
        NetWork.collectMusic(mActivity!!, musicBean?.getMusic_id()!!, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                if (musicBean?.getCollection() != 1) {
                    musicBean?.setCollection(1)
                    setSnackBar("收藏成功", "", R.drawable.icon_success)
                } else {
                    musicBean?.setCollection(0)
                    setSnackBar("取消收藏成功", "", R.drawable.icon_success)
                }
                if (null != bean) {
                    if (TextUtils.equals(musicBean?.getMusic_id(), bean?.id.toString() + "")) {
                        bean?.collection = Integer.valueOf(musicBean?.getMusic_id())
                        if (mActivity != null) {
                            mActivity?.sendBroadcast(Intent("cgCollect"))
                        }
                    }
                }
                if (null != setMusicOperationListener) {
                    setMusicOperationListener?.onCollection(musicBean?.getCollection()!!, musicBean?.getType()!!)
                }
            }

            override fun doError(msg: String) {
                dismiss()
            }

            override fun doResult() {

            }
        })
    }

    interface SetMusicOperationListener {
        fun onCollection(collection: Int, position: Int)
        fun getType(type: Int)
    }

    fun setSetMusicOperationListener(setMusicOperationListener: MusicOperationDialog.SetMusicOperationListener) {
        this.setMusicOperationListener = setMusicOperationListener
    }


    protected fun initData() {
        NetWork.getMemberSong(context, page, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val mySongListBean = JSON.parseObject(resultData, MySongListBean::class.java)
                //refreshData(mySongListBean)
                dataBeanList.addAll(mySongListBean.data.data)
                //myColloectionSongListAdapter.notifyDataSetChanged()
                recyclerviewlist.layoutManager = LinearLayoutManager(context)
                myColloectionSongListAdapter = MyCollectionSongListAdapter(dataBeanList, isMultiSelect, "")
                recyclerviewlist.adapter = myColloectionSongListAdapter;
                myColloectionSongListAdapter.setCheckedSongListener { position ->
                    for (i in dataBeanList.indices) {
                        if (i == position) {
                            dataBeanList[i].check = true
                        } else {
                            dataBeanList[i].check = false
                        }
                    }
                    myColloectionSongListAdapter.notifyDataSetChanged()
                }

            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })

    }

    private fun refreshData(mySongListBean: MySongListBean) {
//        if (page == 1) {
//            dataBeanList.clear()
//        }
        dataBeanList.addAll(mySongListBean.data.data)
        myColloectionSongListAdapter.notifyDataSetChanged()
    }


    private fun addMusicSong() {
        if (TextUtils.isEmpty(musicBean?.getMusic_id()!!)) {
            Toast.create(mActivity).show("没有音乐ID哦~")
            isConfirm = false
            return
        }
        val sb = StringBuffer()
        for (i in dataBeanList.indices) {
            if (dataBeanList[i].check!!) {
                sb.append(dataBeanList[i].id).append(",")
            }
        }
        if (TextUtils.isEmpty(sb)) {
            Toast.create(mActivity).show("请选择歌单")
            isConfirm = false
            return
        }



        sb.deleteCharAt(sb.length - 1)
        NetWork.postAddSong(this!!.mActivity!!, sb.toString(), musicBean?.getMusic_id()!!, musicBean?.getMultiSelect()!!, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val `object` = JSON.parseObject(resultData)
                val msg = `object`.getString("msg")
                Toast.create(mActivity).show(msg)
                RxBus.getDefault().post(MyCollectionRefreshEvent(false, 0))
                //finish()
            }

            override fun doError(msg: String) {
                isConfirm = false
            }

            override fun doResult() {

            }
        })
    }


    private fun initDialogBuilder() {
        val view = View.inflate(mActivity, R.layout.dialog_new_song, null)
        val tv_cancle = view.findViewById<TextView>(R.id.tv_cancle)
        val tv_confirm = view.findViewById<TextView>(R.id.tv_confirm)
        val et_song_content = view.findViewById<EditText>(R.id.et_song_content)

        val private_songlist = view.findViewById<TextView>(R.id.private_songlist)
        val check_newsonglist = view.findViewById<CheckBox>(R.id.check_newsonglist)

        diaLogBuilder = DiaLogBuilder(mActivity)
                .setContentView(view)
                .setFullScreen()
                .setGrvier(Gravity.CENTER)
                .setCanceledOnTouchOutside(true)
                .setAniMo(R.anim.popup_in)
        RxView.clicks(tv_confirm)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(Consumer {
                    if (TextUtils.isEmpty(et_song_content.text.toString())) {
                        Toast.create(mActivity).show("请输入歌单名字")
                        return@Consumer
                    }

                    if(check_newsonglist.isChecked==true){
                        newSong(et_song_content.text.toString(), 1.toString())
                    }else{
                        newSong(et_song_content.text.toString(),0.toString())
                    }

                    et_song_content.setText("")
                    diaLogBuilder!!.setDismiss()
                })
        tv_cancle.setOnClickListener {
            diaLogBuilder!!.setDismiss()
        }
        check_newsonglist.setOnClickListener {
            if (count == 1) {
                check_newsonglist.setChecked(true);
                count = 2
                private_songlist.setText("仅自己可见")
            } else {
                check_newsonglist.setChecked(false);
                count = 1
                private_songlist.setText("仅自己可见")
            }
        }
    }
    /*新建歌单
    * */
    private fun newSong(s: String,is_private :String) {
        NetWork.gethomeNewSong(this!!.mActivity!!, s, musicBean?.getMusic_id()!!, is_private,object : NetWork.TokenCallBack {

            override fun doNext(resultData: String, headers: Headers?) {
                Toast.create(mActivity).show("已加入到新歌单")
                RxBus.getDefault().post(MyCollectionRefreshEvent(false, 0))
                //如果dialog存在的，就在新建了歌单的弹窗之后关闭
                if(dialog!=null){
                    dialog!!.hide()
                }

//                finish()
                //                page = 1;
                //                initData();
            }
            override fun doError(msg: String) {

            }
            override fun doResult() {

            }
        })
    }

    //跳转MV
    private fun toMV(id :String,mvurl :String,uid:Int,title :String,nickname :String,imgpic_link :String) {
        val intent = Intent(mActivity, MvVideoActivitykt::class.java)
        var bundle = Bundle()
        bundle.putInt("mv", Integer.parseInt(id));
        bundle.putString("mvurl", mvurl);
        bundle.putInt("uid", uid);
        bundle.putString("title", title);
        bundle.putString("nickname", nickname);
        bundle.putString("imgpic_link", imgpic_link);
        bundle.putString("bioashi", 1.toString() + "")
        intent.putExtra("mvdate",bundle);
        mActivity!!.startActivity(intent)


//        var mv:Int? = 0
//        var mvurl:String?=""
//        if (musicBean!!.musicIanId != null && !musicBean!!.musicIanId!!.isEmpty()) {
//            mv = Integer.parseInt(musicBean!!.musicIanId);
//        } else {
//            mv = 0;
//        }
//        Log.e("bbbb",""+musicBean.toString())
//        bundle.putInt("mv", Integer.parseInt(mv.toString()));
//        if (musicBean!!.mvInfoBean?.link != null && musicBean!!.mvInfoBean?.link!!.isEmpty()) {
//            mvurl = musicBean!!.mvInfoBean?.link
//        } else {
//            mvurl = "";
//        }
    }
}
