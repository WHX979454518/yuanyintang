package com.mxkj.yuanyintang.mainui.myself.helpcenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.base.adapter.BaseBindingAdapter
import com.mxkj.yuanyintang.databinding.ItemHelpListsBinding
import com.mxkj.yuanyintang.mainui.home.activity.LikesMusicActivity
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.mainui.myself.activity.FeedBackActivity
import com.mxkj.yuanyintang.mainui.myself.activity.SuggestActivity
import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.HelperListBean
import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.ProblemListBean
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew
import com.mxkj.yuanyintang.mainui.web.CommonWebview
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.uiutils.SetGridViewViewHeightBasedOnChildren
import com.mxkj.yuanyintang.utils.uiutils.SetListViewHeightBasedOnChildren
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import okhttp3.Headers
import kotlinx.android.synthetic.main.activity_help_center.*

class HelpCenterActivity : StandardUiActivity() {
//    internal lateinit var adapter: BaseBindingAdapter<HelperListBean.DataBean, ItemHelpListsBinding>
    var dataList: ArrayList<HelperListBean.DataBean> = ArrayList()
    private val page = 1
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun setLayoutId(): Int {
        return R.layout.activity_help_center
    }

    override fun initView() {
        hideTitle(true)
        StatusBarUtil.baseTransparentStatusBar(this)
    }

    internal var hotQuestionsActivityGridviewAdapter: HotQuestionsActivityGridviewAdapter? = null
    internal var gridviewList: List<ProblemListBean.DataBean>? = null

    internal var hotQuestionsActivityListviewAdapter: HotQuestionsActivityListviewAdapter? = null
    override fun initData() {
        NetWork.getHelpList(this, page, 6, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val helperListBean = JSON.parseObject(resultData, HelperListBean::class.java)
                if (helperListBean != null) {
                    val data = helperListBean.data
                    if (data != null && data.size > 0) {
                        dataList.addAll(data)
//                        adapter.setList(dataList)
                        hotQuestionsActivityListviewAdapter = HotQuestionsActivityListviewAdapter(this@HelpCenterActivity, dataList)
                        lv_help_list.adapter = hotQuestionsActivityListviewAdapter
                        SetListViewHeightBasedOnChildren.setListViewHeightBasedOnChildren(lv_help_list)
                        hotQuestionsActivityListviewAdapter!!.notifyDataSetChanged()
                    }

                }
            }

            override fun doError(msg: String) {
            }

            override fun doResult() {

            }
        })
        NetWork.getHelpMsg(this, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val jsonObject = JSON.parseObject(resultData)
                val jObj = jsonObject.getJSONObject("data")
                val configArray = jObj.getJSONArray("config")
                if (configArray != null && configArray.size > 0) {
                    val conObj: JSONObject = configArray[0] as JSONObject
                    tv_system_msg.text = conObj?.getString("title")
                    val type = conObj?.getInteger("type")
                    val app_url = conObj?.getString("app_url")
                    val url = conObj?.getString("url")
                    val urlmodel = conObj?.getString("urlmodel")
                    val from_id = conObj?.getInteger("from_id")
                    RxView.clicks(tv_system_msg).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                        if (type == 1) {
                            val intent = Intent(this@HelpCenterActivity, CommonWebview::class.java)
                            intent.putExtra("url", url)
                            intent.putExtra("activity", "activity")
                            startActivity(intent)
                        } else if (type == 2) {
                            val bundle = Bundle()
                            when (urlmodel) {
                                "home" -> {
                                }
                                "topicDetails" -> if (from_id != 0) {
                                    bundle.putInt(PondDetialActivityNew.PID, from_id)
                                    goActivity(PondDetialActivityNew::class.java, bundle)
                                }
                                "musicDetails" -> {
//                                    bundle.putString(MusicDetailsActivity.MUSIC_ID, from_id.toString() + "")
//                                    goActivity(MusicDetailsActivity::class.java, bundle)
                                }
                                "musicianDetailHome" -> {
                                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, from_id.toString() + "")
                                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 0)
                                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                                }
                                "musicianDetailMusic" -> {
                                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, from_id.toString() + "")
                                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 1)
                                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                                }
                                "musicianDetailSongSheet" -> {
                                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, from_id.toString() + "")
                                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 2)
                                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                                }
                                "musicianDetailTopic" -> {
                                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, from_id.toString() + "")
                                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 3)
                                    goActivity(MusicIanDetailsActivity::class.java, bundle)
                                }
                                "songSheetDetails" -> {
                                    bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, from_id.toString() + "")
                                    goActivity(SongSheetDetailsActivity::class.java, bundle)
                                }
                                "likesSongSheetDetails" -> {
                                    bundle.putString(LikesMusicActivity.MUSICIAN_ID, from_id.toString() + "")
                                    goActivity(LikesMusicActivity::class.java, bundle)
                                }
                                "activity" -> {
                                    val intent = Intent(this@HelpCenterActivity, CommonWebview::class.java)
                                    intent.putExtra("url", app_url)
                                    intent.putExtra("activity", "activity")
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                } else {
                    tv_system_msg.visibility = View.GONE
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
        ProblemList()//问题分类列表
//        SetListViewHeightBasedOnChildren.setListViewHeightBasedOnChildren(lvHotSearchList)
//        lvHotSearchList.setOnTouchListener(View.OnTouchListener { v, event ->
//            // TODO Auto-generated method stub
//            //吧状态改为先true后false以后listview嵌套在ScrollView中划不动的问题就解决了
//            if (event.action == MotionEvent.ACTION_UP) {
//                hot_scroll_view.requestDisallowInterceptTouchEvent(true)
//            } else {
//                hot_scroll_view.requestDisallowInterceptTouchEvent(false)
//            }
//            false
//        })
    }
    override fun initEvent() {
        //这里的适配器使用的事数据绑定
//        adapter = object : BaseBindingAdapter<HelperListBean.DataBean, ItemHelpListsBinding>(this@HelpCenterActivity, dataList, R.layout.item_help_lists) {
//            override fun bindObj(itemHelpListsBinding: ItemHelpListsBinding, dataBean: HelperListBean.DataBean) {
//                itemHelpListsBinding.obj = dataBean
//            }
//        }
        hotQuestionsActivityListviewAdapter = HotQuestionsActivityListviewAdapter(this@HelpCenterActivity, dataList)
        lv_help_list.adapter = hotQuestionsActivityListviewAdapter
        SetListViewHeightBasedOnChildren.setListViewHeightBasedOnChildren(lv_help_list)
        hotQuestionsActivityListviewAdapter!!.notifyDataSetChanged()
//        lv_help_list.adapter = adapter
        lv_help_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            val dataBean = dataList[i]
            val intent = Intent(this@HelpCenterActivity, CommonWebview::class.java)
            intent.putExtra("url", dataBean.share_url)
            intent.putExtra("title", dataBean.title)
            startActivity(intent)
        }
        RxView.clicks(search_more).throttleFirst(1, TimeUnit.SECONDS).subscribe { goActivity(HotQuestionsActivity::class.java) }
        RxView.clicks(tv_to_advice).throttleFirst(1, TimeUnit.SECONDS).subscribe { goActivity(SuggestActivity::class.java) }
        RxView.clicks(tv_call).throttleFirst(1, TimeUnit.SECONDS).subscribe { goActivity(ChatRobotActivity::class.java) }
        RxView.clicks(iv_finish).throttleFirst(1, TimeUnit.SECONDS).subscribe { finish() }
    }


    fun ProblemList() {
        NetWork.getHelpProblemList(this, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                Log.e("kkkkkk", "" + resultData)
                val problemListBean = JSON.parseObject(resultData, ProblemListBean::class.java)
                if (problemListBean != null) {
                    gridviewList = problemListBean.data
                    if (gridviewList != null && gridviewList!!.size > 0) {
                        //                        adapter.setList(dataList);
                        hotQuestionsActivityGridviewAdapter = HotQuestionsActivityGridviewAdapter(this@HelpCenterActivity, gridviewList)
                        problem_list.adapter = hotQuestionsActivityGridviewAdapter
                        SetGridViewViewHeightBasedOnChildren.setListViewHeightBasedOnChildren(problem_list)
                        hotQuestionsActivityGridviewAdapter!!.notifyDataSetChanged()
                    }
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }
}
