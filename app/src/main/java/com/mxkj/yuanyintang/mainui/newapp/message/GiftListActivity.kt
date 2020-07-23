package com.mxkj.yuanyintang.mainui.newapp.message

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View.GONE
import android.view.View.VISIBLE
import com.alibaba.fastjson.JSON
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.mainui.newapp.home.MusicTypeCkView
import com.mxkj.yuanyintang.mainui.newapp.home.OrderTypeBean
import com.mxkj.yuanyintang.mainui.newapp.weidget.BigTxtTabLayout
import com.mxkj.yuanyintang.net.ApiAddress
import com.mxkj.yuanyintang.net.NetWork
import kotlinx.android.synthetic.main.activity_gift_list.*
import okhttp3.Headers

class GiftListActivity : StandardUiActivity() {
    private var dataList: ArrayList<MygiftListBean.DataBean> = ArrayList()
    private var page=1
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    override fun setLayoutId(): Int {
        return R.layout.activity_gift_list
    }

    private var adapter: MyGiftListAdapter?=null

    override fun initView() {
        setTitleText("礼物")
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = MyGiftListAdapter(dataList)
        recycler.adapter = adapter
        var whereBeanList = ArrayList<OrderTypeBean.DataBean.WhereBean>()
        var whereBean = OrderTypeBean.DataBean.WhereBean()
        whereBean.title = "收到的"
        whereBean.isChecked = true
        whereBean.id = 1
        var whereBean2 = OrderTypeBean.DataBean.WhereBean()
        whereBean2.title = "送出的"
        whereBean2.id = 2
        whereBean2.isChecked = false
        whereBeanList.add(whereBean)
        whereBeanList.add(whereBean2)
        tabLayout.init(this@GiftListActivity, whereBeanList, 0, object : BigTxtTabLayout.TabSelectListener {
            override fun onSelect(position: Int) {
                dataList.clear()
                if(position == 0){
                    gift_show.setText("你还没有收到礼物呢~")
                }else{
                    gift_show.setText("你还没有送出过礼物呢~")
                }
                initDataList(whereBeanList[position].id.toString())
            }
        })

    }


    private fun initDataList(typeId: String) {
        NetWork.getMyGiftList(this@GiftListActivity,HttpParams("type",typeId),object :NetWork.TokenCallBack{
            override fun doNext(resultData: String, headers: Headers?) {
                val parseObject = JSON.parseObject(resultData, MygiftListBean::class.java)
                val data = parseObject.data
                if (data!=null&&data.size>0){
                    rl_nothing.visibility = GONE

                    if (page==1){
                        dataList.clear()
                    }
                    dataList.addAll(data)
                    adapter?.setNewData(dataList)
                }else{
                    if (page==1){
                        rl_nothing.visibility = VISIBLE
                    }
                }

            }

            override fun doError(msg: String) {
            }

            override fun doResult() {
            }
        })
    }

    override fun initData() {
        initDataList("1")
    }

    override fun initEvent() {
    }
}
