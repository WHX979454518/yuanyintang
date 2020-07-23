package com.mxkj.yuanyintang.extraui.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log

import com.alibaba.fastjson.JSON
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.BaseActivity
import com.mxkj.yuanyintang.base.bean.UserInfo
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment
import com.mxkj.yuanyintang.database.updatafilesql.TasksManager
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.UploadMusicActivity
import com.mxkj.yuanyintang.musicplayer.util.SheetDetialBean
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.mainui.home.bean.DownLoadFileBean
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.message.activity.MyCollectionSongListActivity
import com.mxkj.yuanyintang.mainui.myself.activity.EditSongActivity
import com.mxkj.yuanyintang.mainui.myself.bean.MySongListBean
import com.mxkj.yuanyintang.extraui.adapter.TitleOperationAdapter
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.bean.TitleOperationBean
import com.mxkj.yuanyintang.mainui.home.bean.MusicIanSongSheetBean
import com.mxkj.yuanyintang.upush.UpushService.Companion.goActivity
import com.mxkj.yuanyintang.utils.app.CommonUtils
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.uiutils.Toast
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import kotlinx.android.synthetic.main.dialog_title_operation.*

@SuppressLint("ValidFragment")
class TitleOperationDialog : BaseDialogFragment {
    internal var musicBean: MusicBean? = null
    private var titleOperationBeanList: MutableList<TitleOperationBean> = ArrayList()
    override val contentViewLayoutID: Int
        get() = R.layout.dialog_title_operation

    override val isBack: Boolean?
        get() = false

    constructor(musicBean: MusicBean) {
        this.musicBean = musicBean
    }

    constructor()

    override fun style(): Int {
        return 0
    }

    override fun initView() {
        if (null == musicBean) {
            return
        }
        recyclerview?.layoutManager = LinearLayoutManager(mActivity)
        recyclerview?.addItemDecoration(MyRecyclerDetorration(mActivity, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(mActivity!!, 0.6f), R.color.dividing_line_color, true))
        if (musicBean?.getType() == 8) {//音乐人詳情更多
//            titleOperationBeanList.add(TitleOperationBean("分享"))
            dismiss()
            val shareBottomDialog = ShareBottomDialog(mActivity!!, musicBean!!)
            shareBottomDialog.show()
        } else if (musicBean?.getType() == 2) {


            val string = CacheUtils.getString(MainApplication.application, Constants.User.USER_JSON, "")
            string?.let {
                val userInfo = JSON.parseObject(string, UserInfo::class.java)
                userInfo?.let {

                    if (TextUtils.equals(if (null != userInfo) userInfo.data?.id.toString() + "" else "", musicBean?.getUid().toString() + "")&& musicBean?.canEdit ==true) {
                        titleOperationBeanList.add(TitleOperationBean("编辑"));
                        titleOperationBeanList.add(TitleOperationBean("删除"))

                    } else {
                        if (musicBean?.getCollection()==1){
                            titleOperationBeanList.add(TitleOperationBean("删除"))
                        }
                        titleOperationBeanList.add(TitleOperationBean("举报"))
                    }
                }
            }
        } else {
            titleOperationBeanList.add(TitleOperationBean("添加歌单"))
            titleOperationBeanList.add(TitleOperationBean("下载"))
            titleOperationBeanList.add(TitleOperationBean("分享"))
            if (TextUtils.equals(CacheUtils.getString(mActivity, Constants.User.USER_ID), musicBean?.getUid().toString() + "")&& musicBean?.canEdit ==true) {
                titleOperationBeanList.add(TitleOperationBean("编辑"))
            } else {
                titleOperationBeanList.add(TitleOperationBean("举报"))
            }
        }
        val titleOperationAdapter = TitleOperationAdapter(titleOperationBeanList, mActivity!!)
        recyclerview?.adapter = titleOperationAdapter
        titleOperationAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            if (null == musicBean) {
                return@OnItemClickListener
            }
            when (titleOperationBeanList[position].title) {
                "添加歌单" -> {
                    dismiss()
                    if (CacheUtils.getBoolean(mActivity, Constants.User.IS_LOGIN, false)) {
                        addSong()
                    } else {
                        goActivity(LoginRegMainPage::class.java)
                    }
                }
                "下载" -> if (CacheUtils.getBoolean(mActivity, Constants.User.IS_LOGIN)) {
                    validateDownLoadFile()
                } else {
                    Toast.create(mActivity).show("登录后才能下载")
                    goActivity(LoginRegMainPage::class.java)
                }
                "分享" -> {
                    dismiss()
                    val shareBottomDialog = ShareBottomDialog(mActivity!!, musicBean!!)
                    shareBottomDialog.show()
                }
                "编辑" -> {
                    dismiss()
                    if (CacheUtils.getBoolean(mActivity, Constants.User.IS_LOGIN, false)) {
                        if (musicBean?.getType() == 2) {
                            val dataBean = MySongListBean.DataBeanX.DataBean()
                            dataBean.id = musicBean?.getSong_id()!!
                            dataBean.title = musicBean?.getSongName()
                            val imgpicInfoBean = MySongListBean.DataBeanX.DataBean.ImgpicInfoBean()
                            if (musicBean?.imgpic_info != null) {
                                imgpicInfoBean.link = musicBean?.imgpic_info?.link
                                dataBean.imgpic_info = imgpicInfoBean
                                dataBean.imgpic = musicBean?.getImgpic()
                            }
                            val bundle = Bundle()
                            bundle.putString("biaoshi", 1.toString())
                            bundle.putSerializable("data", dataBean)
                            goActivity(EditSongActivity::class.java, bundle)
                        } else if (musicBean?.getType() == 1) {
                            val bundle = Bundle()
                            bundle.putSerializable(UploadMusicActivity.DATA, Integer.valueOf(musicBean?.getMusic_id()))
                            goActivity(UploadMusicActivity::class.java, bundle)
                        }
                    } else {
                        goActivity(LoginRegMainPage::class.java)
                    }
                }
                "举报" -> {
                    dismiss()
                    if (CacheUtils.getBoolean(mActivity, Constants.User.IS_LOGIN, false)) {
                        val reportOperationDialog = ReportOperationDialog(musicBean!!)
                        reportOperationDialog.show(activity.supportFragmentManager, "mReportOperationDialog")
                    } else {
                        goActivity(LoginRegMainPage::class.java)
                    }
                }"删除" -> {

                BaseConfirmDialog.newInstance().title("提示").content("歌单中所有歌曲都将被移除").confirmText("移除").cancleText("取消").showDialog(activity, object : BaseConfirmDialog.onBtClick {

                    override fun onConfirm() {

                        if(musicBean?.getCollection() != 0){
                            Log.i("msg","收藏的")
                            NetWork.getSongCollect(mActivity!!, musicBean!!.getSong_id().toString() + "", object : NetWork.TokenCallBack {

                                override fun doNext(resultData: String, headers: Headers?) {
//                                Log.i("ttt",""+resultData.toString())
                                    mActivity!!.finish()
                                    Toast.create(mActivity).show("如果收藏了该歌单请再次删除哦！")
                                    //page = 1
                                    //netData()
                                }
                                override fun doError(msg: String) {
                                }
                                override fun doResult() {
                                }
                            })
                        }else{
                            //如果是新建的z直接就会删除，如果是新建的并且收藏了，是应该先取消收藏，然后删除歌单
                            Log.i("msg","新建的")
//                            NetWork.getSongCollect(mActivity!!, musicBean!!.getSong_id().toString() + "", object : NetWork.TokenCallBack {
//                                override fun doNext(resultData: String, headers: Headers?) {
////                                Log.i("ttt",""+resultData.toString())
//                                    mActivity!!.finish()
//                                    //page = 1
//                                    //netData()
//                                }
//                                override fun doError(msg: String) {
//                                }
//                                override fun doResult() {
//                                }
//                            })
                            NetWork.delCollectionSong(mActivity!!, musicBean!!.getSong_id().toString() + "", object : NetWork.TokenCallBack {

                                override fun doNext(resultData: String, headers: Headers?) {
                                    mActivity!!.finish()
                                }

                                override fun doError(msg: String) {

                                }

                                override fun doResult() {

                                }
                            })


                        }



                    }

                    override fun onCancle() {
                        //Toast.makeText(getActivity(),"取消",Toast.LENGTH_SHORT).show();
                    }
                })

            }
            }
        }

        RxView.clicks(btn_cancel)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { dismiss() }
    }

    private fun downLoadFile() {
        if (null == musicBean) {
            if (TextUtils.isEmpty(musicBean?.getMusic_id()) || 0 <= musicBean?.getSong_id()!!) {
                if (null != mActivity) {
                    Toast.create(mActivity).show("没有ID~")
                }
                return
            }
        }
        if (musicBean?.getType() == 1) {
            validateDownLoadFile()
        } else if (musicBean?.getType() == 2) {
            Toast.create(mActivity).show("已添加到下载队列")
            NetWork.getSongSheetDetails("songDetails_"+musicBean?.getMusic_id()!!,mActivity!!, musicBean?.getMusic_id()!!, object : NetWork.TokenCallBack {
                override fun doNext(resultData: String, headers: Headers?) {
                    val sheetBean = JSON.parseObject(resultData, SheetDetialBean::class.java)
                    Observable.fromArray(sheetBean.data)
                            .flatMap { dataBeen -> Observable.fromIterable(dataBeen) }
                            .observeOn(Schedulers.io())
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe { dataBean ->
                                val kbps = CacheUtils.getString(mActivity, Constants.User.MUSIC_KBP, "128")
                                NetWork.getMusicDown(mActivity!!, dataBean.id.toString() + "", if (TextUtils.equals("128", kbps)) "1" else "2", object : NetWork.TokenCallBack {
                                    override fun doNext(resultData: String, headers: Headers?) {
                                        var data: String? = null
                                        try {
                                            val `object` = JSONObject(resultData)
                                            data = `object`.optString("data")
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }

                                        if (!TextUtils.isEmpty(data)) {
                                            val musicBean = MusicBean()
                                            musicBean.ext = "." + StringUtils.parseSuffix(data)
//                                            musicBean.setFileName(musicBean.getMusic_name()?)
                                            musicBean.setCollection(1)
                                            musicBean.setMusic_name(dataBean.title)
                                            val imgpicInfoBean = MusicBean.ImgpicInfoBean()
                                            imgpicInfoBean.link = dataBean.imgpic_info.link
                                            musicBean.imgpic_info = imgpicInfoBean
                                            musicBean.setMusic_name(dataBean.title)
                                            musicBean.setMusician_name(dataBean.nickname)
                                            musicBean.ext = "." + StringUtils.parseSuffix(data)
                                            musicBean.setMusic_id(dataBean.id.toString() + "")
                                            musicBean.setUid(dataBean.uid)
                                            musicBean.setSong_id(dataBean.song_id)
                                            TasksManager.getImpl().downLoadFile(musicBean, data, mActivity)
                                        }
                                    }

                                    override fun doError(msg: String) {

                                    }

                                    override fun doResult() {

                                    }
                                })
                            }
                }

                override fun doError(msg: String) {

                }

                override fun doResult() {

                }
            })
        }
    }

    private fun validateDownLoadFile() {
        if (null == musicBean) {
            return
        }
        NetWork.getDownBit(mActivity!!, musicBean?.getMusic_id()!!, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val downLoadFileBit = JSON.parseObject(resultData, DownLoadFileBean.DownLoadFileBitBean::class.java)
                if (null != downLoadFileBit) {
                    downLoadFile(downLoadFileBit.data.isSd_sdl, downLoadFileBit.data.isHigh_sdl)
                } else {
                    if (null != mActivity) {
                        Toast.create(mActivity).show("没有音乐可以下载~")
                    }
                    dismiss()
                }
            }

            override fun doError(msg: String) {
                dismiss()
            }

            override fun doResult() {

            }
        })
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
                    musicBean?.setMusic_name(musicBean?.getMusic_name())
                    TasksManager.getImpl().downLoadFile(musicBean, data, mActivity)
                }
                dismiss()
            }

            override fun doError(msg: String) {
                dismiss()
            }

            override fun doResult() {

            }
        })
    }


    private fun addSong() {
        if (null == musicBean && TextUtils.isEmpty(musicBean?.getMusic_id())) {
            if (null != mActivity) {
                Toast.create(mActivity).show("没有音乐ID~")
            }
            return
        }
        val bundle = Bundle()
        bundle.putString("music_id", musicBean?.getMusic_id())
        bundle.putBoolean(MyCollectionSongListActivity.IS_MULTI_SELECT, if (musicBean?.getType() == 1) false else musicBean?.getType() == 2)
        bundle.putString(MyCollectionSongListActivity.VIEW_TYPE, "addMusic")
        goActivity(MyCollectionSongListActivity::class.java, bundle)
    }
}
