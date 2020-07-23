package com.mxkj.yuanyintang.mainui.pond.widget

import android.widget.CheckBox
import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.mainui.pond.bean.PondTagListBean
import com.mxkj.yuanyintang.mainui.pond.bean.PondTagListBean.*
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_choose_pond_tag_new.*
import java.util.ArrayList
import okhttp3.Headers

class ChoosePondTagNew : StandardUiActivity() {
    var maxNum = 3
    private var selectedTagList = ArrayList<PondTagListBean.DataBean.TagBean>()
    private var sources = ArrayList<String>()//自定义ET添加的view
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun setLayoutId(): Int {
        return R.layout.activity_choose_pond_tag_new
    }

    override fun initView() {
        StatusBarUtil.baseTransparentStatusBar(this)
        hideTitle(true)
        val selectTopicTag = CacheUtils.getString(this@ChoosePondTagNew, "selectTopicTag", "")
        if (selectTopicTag == null || selectTopicTag.length < 5) return
        selectedTagList = JSON.parseArray(selectTopicTag, DataBean.TagBean::class.java) as ArrayList<DataBean.TagBean>
    }

    override fun initEvent() {
        save.setOnClickListener { saveTag() }
        finish.setOnClickListener { finish() }
        etTag.setonAddListener { _ ->
            val ok = etTag.ok(this@ChoosePondTagNew)
            if (selectedTagList.size + ok.size < 3) {
                sources = ok as ArrayList<String>
            } else {
                if (selectedTagList.size >= 3) {
                    etTag.isAdd = false
                }
                setSnackBar("最多添加3个标签哦！", "", R.drawable.icon_tips_bad)
            }
        }
    }

    override fun initData() {
        NetWork.getPondTags(this, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                initTags(resultData)
            }

            override fun doError(msg: String) {
            }

            override fun doResult() {
            }

        })
    }

    private fun initTags(resultData: String) {
        mulrg_tag_type.removeAllViews()
        mulrg_tag.removeAllViews()
        mulrg_tag.setChoiceMode(false)
        val pondTagListBean = JSON.parseObject(resultData, PondTagListBean::class.java)
        //        TODO  要遍历已选择列表，将已选择的tag匹配的view置为true
//        val string = CacheUtils.getString(this@ChoosePondTagNew, "selectTopicTag")
//        var array = ArrayList<PondTagListBean.DataBean.TagBean>()
        pondTagListBean.data?.let {
            it.indices.forEach {
                mulrg_tag_type.insert(it, pondTagListBean.data[it].title)
            }
            it[0].tag?.let {
                it.indices.forEach {
                    mulrg_tag.insert(it, pondTagListBean.data[0].tag[it].title)
                }
            }
            mulrg_tag_type.setOnCheckChangedListener { _, position1, _ ->
                mulrg_tag.removeAllViews()
                pondTagListBean.data[position1]?.tag?.let {
                    it.indices.forEach {
                        mulrg_tag.insert(it, pondTagListBean.data[position1].tag[it].title)
                    }
                    mulrg_tag.setOnCheckChangedListener { view, position, checked ->
                        if (checked) {
                            if (selectedTagList.size < 3) {
                                val tagBean = pondTagListBean.data[position1].tag[position]
                                selectedTagList.forEach {
                                    if (it.id == tagBean.id) {
                                        selectedTagList.remove(it)
                                    }
                                }
                                selectedTagList.add(pondTagListBean.data[position1].tag[position])
                            } else {
                                (view.getChildAt(position) as CheckBox).isChecked = false
                                setSnackBar("最多添加3个标签哦！", "", R.drawable.icon_tips_bad)
                            }
                        } else {
                            selectedTagList.remove(pondTagListBean.data[position1].tag[position])
                        }
                    }

                }
            }

        }
    }

    private fun saveTag() {
        sources.forEach {
            var sBean = DataBean.TagBean()
            sBean.title = it
            sBean.id = 0
            selectedTagList.add(sBean)
        }
        CacheUtils.setString(this@ChoosePondTagNew, "selectTopicTag", JSON.toJSONString(selectedTagList))
        finish()
    }
}
