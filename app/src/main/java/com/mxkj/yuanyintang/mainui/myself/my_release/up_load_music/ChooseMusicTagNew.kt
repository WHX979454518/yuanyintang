package com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import com.alibaba.fastjson.JSON
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.R.id.etTag
import com.mxkj.yuanyintang.R.id.mulrg_tag_type
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.mainui.myself.bean.MusicTypeBean
import com.mxkj.yuanyintang.mainui.pond.bean.PondHotTagBean
import com.mxkj.yuanyintang.mainui.pond.bean.PondTagListBean
import com.mxkj.yuanyintang.mainui.pond.bean.PondTagListBean.*
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_add_lyric.*
import kotlinx.android.synthetic.main.activity_choose_pond_tag_new.*
import kotlinx.android.synthetic.main.activity_choose_pond_tag_new.view.*
import java.util.ArrayList
import okhttp3.Headers
import java.io.Serializable
import android.support.v4.app.NotificationCompat.getExtras



class ChooseMusicTagNew : StandardUiActivity() {
    private var mInputManager: InputMethodManager? = null
    var typeTitle = ""
    var typeId = 0
    private var selectedTagList = ArrayList<PondTagListBean.DataBean.TagBean>()
    private var selectedTagTagList = ArrayList<MusicTypeBean.DataBean>()


    private var sources = ArrayList<String>()
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun setLayoutId(): Int {
        return R.layout.activity_choose_pond_tag_new
    }

    override fun initView() {
        StatusBarUtil.baseTransparentStatusBar(this)
        hideTitle(true)
        mInputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val selectMusicTag = CacheUtils.getString(this@ChooseMusicTagNew, "selectMusicTag", "")
        if (!TextUtils.isEmpty(selectMusicTag)) {
            if(null!=JSON.parseArray(selectMusicTag, DataBean.TagBean::class.java)){
                selectedTagList = JSON.parseArray(selectMusicTag, DataBean.TagBean::class.java) as ArrayList<DataBean.TagBean>
            }

        }
        val selectMusicTagTag = CacheUtils.getString(this@ChooseMusicTagNew, "selectMusicTagTag", "")
        if (!TextUtils.isEmpty(selectMusicTag)) {
            if(null!=JSON.parseArray(selectMusicTagTag, MusicTypeBean.DataBean::class.java)){
                selectedTagTagList = JSON.parseArray(selectMusicTagTag, MusicTypeBean.DataBean::class.java) as ArrayList<MusicTypeBean.DataBean>
            }

        }
    }

    override fun initEvent() {
        save.setOnClickListener { saveTag() }
        finish.setOnClickListener { finish() }
        etTag.setonAddListener { _ ->
            val ok = etTag.ok(this@ChooseMusicTagNew)
            if (selectedTagList.size + ok.size < 9) {
                sources = ok as ArrayList<String>
            } else {
                etTag.isAdd = false
                setSnackBar("最多添加9个标签哦！", "", R.drawable.icon_tips_bad)
            }
        }
    }

    private fun initHotTagData() {
        NetWork.getPublicTag(1, 1, 1, this, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val pondHotTagBean = JSON.parseObject(resultData, PondHotTagBean::class.java)
                val dataArray = pondHotTagBean?.data
                val toJSONString = JSON.toJSONString(dataArray)
                val parseArray = JSON.parseArray(toJSONString, PondTagListBean.DataBean.TagBean::class.java)
                if (parseArray != null && parseArray.size > 0) {
                    mulrg_tag.removeAllViews()
                    mulrg_tag.setChoiceMode(false)

                    parseArray.indices.forEach {
                        mulrg_tag.insert(it, parseArray[it].title)
                        var biaoshi:Int? =parseArray.get(it).id
                        var pos:Int? =it
                        if(null!=selectedTagList&&selectedTagList.size>0){
                                selectedTagList.indices.forEach {
                                    if(selectedTagList.get(it).id==biaoshi){
                                    mulrg_tag.setItemChecked(pos!!)
                                }
                            }
                        }
                    }
                    mulrg_tag.setOnCheckChangedListener { view, position, checked ->
                        val tagBean = parseArray[position]

                        //加载完成标签以后吧原来存的标签清空
//                        selectedTagList.clear()

                        if (checked) {
                            if (selectedTagList.size < 9) {
                                selectedTagList.forEach {
                                    if (it.id == tagBean.id) {
                                        selectedTagList.remove(it)
                                    }
                                }
                                selectedTagList.add(tagBean)
                            } else {
                                (view.getChildAt(position) as CheckBox).isChecked = false
                                setSnackBar("最多添加9个标签哦！", "", R.drawable.icon_tips_bad)
                            }
                            CacheUtils.setString(this@ChooseMusicTagNew, "selectMusicTag", JSON.toJSONString(selectedTagList))
                        } else {

                            selectedTagList.forEach {
                                if (it.id == tagBean.id) {
                                    selectedTagList.remove(it)
                                }
                            }
                            CacheUtils.setString(this@ChooseMusicTagNew, "selectMusicTag", JSON.toJSONString(selectedTagList))
                        }
                    }
                }
            }

            override fun doError(msg: String) {
            }

            override fun doResult() {

            }
        })
    }

    //这个是
    override fun initData() {
        NetWork.getMusicType(this, null, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val musicTypeBean = JSON.parseObject(resultData, MusicTypeBean::class.java)
                val typeBeanList = musicTypeBean?.data
                if (typeBeanList != null) {
                    mulrg_tag_type.removeAllViews()


                    typeBeanList.indices.forEach {
                        mulrg_tag_type.insert(it, typeBeanList[it].title)

                        if(null!=selectedTagTagList&&selectedTagTagList.size>0){
                            if(selectedTagTagList.get(0).id==typeBeanList.get(it).id){
                                mulrg_tag_type.setItemChecked(it)
                                CacheUtils.setString(this@ChooseMusicTagNew, "selectMusicTagTag", JSON.toJSONString(selectedTagTagList))
                            }
                        }



                    }
                    mulrg_tag_type.setOnCheckChangedListener { view, position, checked ->
                        typeTitle = typeBeanList[position].title
                        val musicType = typeBeanList[position]
                        selectedTagTagList.clear()
                        if (checked) {
                            if (selectedTagTagList.size < 6) {
                                selectedTagTagList.forEach {
                                    if (it.id == musicType.id) {
                                        selectedTagTagList.remove(it)
                                    }
                                }
                                selectedTagTagList.add(musicType)
                            } else {
                                (view.getChildAt(position) as CheckBox).isChecked = false
                                setSnackBar("最多添加5个标签哦！", "", R.drawable.icon_tips_bad)
                            }
                            CacheUtils.setString(this@ChooseMusicTagNew, "selectMusicTagTag", JSON.toJSONString(selectedTagTagList))
                        } else {
                            CacheUtils.setString(this@ChooseMusicTagNew, "selectMusicTagTag", JSON.toJSONString(selectedTagTagList))
                        }
                    }


                }


            }

            override fun doError(msg: String) {
            }

            override fun doResult() {

            }

        })

        initHotTagData()
    }

    override fun onPause() {
        super.onPause()
        mInputManager?.hideSoftInputFromWindow(etTag.windowToken, 0)
    }

    //标签保存方法
    private fun saveTag() {
        sources = etTag.ok(this) as ArrayList<String>
        sources.forEach {
            var sBean = DataBean.TagBean()
            sBean.title = it
            sBean.id = 0
            selectedTagList.add(sBean)
        }
        CacheUtils.setString(this@ChooseMusicTagNew, "selectMusicTag", JSON.toJSONString(selectedTagList))
        CacheUtils.setString(this@ChooseMusicTagNew, "selectMusicTagTag", JSON.toJSONString(selectedTagTagList))

        val intent = Intent()
        intent.putExtra("typeTitle", typeTitle)
        intent.putExtra("typeId", typeId)
        intent.putExtra("selectMusicTag", JSON.toJSONString(selectedTagList))
        intent.putExtra("selectMusicTagTag", JSON.toJSONString(selectedTagTagList))
        setResult(Activity.RESULT_OK, intent)//带返回参数的
        finish()
    }
}
