package com.mxkj.yuanyintang.mainui.newapp.home.adapter

import android.content.Intent
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.dynamic_common_top.view.*
import okhttp3.Headers

/**
 * 首页音乐人
 *
 */
class HomeMusicianAdapter(dataList: List<HomeBean.HomeMusicianBean.MusicianBean>) : BaseMultiItemQuickAdapter<HomeBean.HomeMusicianBean.MusicianBean, BaseViewHolder>(dataList) {
    init {
        addItemType(0, R.layout.home_musician_item)
    }

    override fun convert(helper: BaseViewHolder, musicianBean: HomeBean.HomeMusicianBean.MusicianBean) {
        musicianBean?.let {
            ImageLoader.with(mContext).getSize(170, 170).setUrl(it.head_info).into(helper.getView(R.id.civ_headimg))
            helper.setText(R.id.title, StringUtils.isEmpty(it.nickname))
            helper.setText(R.id.tv_fans_num, it.fans_num?.toString()+" 粉丝")
            val ckFollow = helper.getView<CheckBox>(R.id.ck_follow)
            ckFollow.isChecked = it.relation == 1
            ckFollow.text = if (it.relation == 1) {
                "已关注"
            } else {
                "+关注"
            }
            helper.setOnClickListener(R.id.layout_click, {
                toUser(0, musicianBean.id)
            })

            ckFollow.setOnTouchListener { view, motionEvent ->
                if (motionEvent.action==MotionEvent.ACTION_DOWN){
                    MobclickAgent.onEvent(mContext,"hot_musician_follow");

                    if (CacheUtils.getBoolean(mContext, Constants.User.IS_LOGIN)) {
                        ckFollow.isChecked =!ckFollow.isChecked
                        if (ckFollow.isChecked) {
                            ckFollow.text = "已关注"
                        } else {
                            ckFollow.text = "+关注"
                        }
                        val params = HttpParams()
                        params.put("id", musicianBean.id.toString())
                        NetWork.follow(mContext, params, object : NetWork.TokenCallBack {
                            override fun doNext(resultData: String, headers: Headers?) {
                                val jObj = JSON.parseObject(resultData)
                                val code = jObj.getInteger("code")
                                if (code == 200) {
                                    if (musicianBean.relation == 0) {
                                        ckFollow.text = "已关注"
                                        musicianBean.fans_num +=1
                                        helper.setText(R.id.tv_fans_num, (it.fans_num)?.toString()+" 粉丝")
                                        musicianBean.relation = 1
                                    } else if (musicianBean.relation == 1) {
                                        ckFollow.text = "+关注"
                                        musicianBean.relation = 0
                                        musicianBean.fans_num -=1
                                        helper.setText(R.id.tv_fans_num, (it.fans_num)?.toString()+" 粉丝")

                                    }
                                }
                            }

                            override fun doError(msg: String) {
                            }

                            override fun doResult() {

                            }
                        })
                    } else {
                        mContext.startActivity(Intent(mContext, LoginRegMainPage::class.java))
                        ckFollow.isChecked = false
                        ckFollow.text = "+关注"
                    }
                }



                true
            }

            helper.setVisible(R.id.iv_is_vip, it.is_music == 3)
        }
    }

    //跳转用户
    private fun toUser(pageIndex: Int, uid: Int) {
        MobclickAgent.onEvent(mContext,"hot_musician_home");
        val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
        intent.putExtra(MusicIanDetailsActivity.MUSICIAN_ID, uid.toString() + "")
        intent.putExtra(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, pageIndex)
        mContext.startActivity(intent)
    }
}
