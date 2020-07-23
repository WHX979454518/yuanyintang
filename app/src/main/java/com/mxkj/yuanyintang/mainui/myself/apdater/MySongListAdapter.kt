package com.mxkj.yuanyintang.mainui.myself.apdater


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View

import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder
import com.mxkj.yuanyintang.mainui.myself.bean.MySongListBean
import com.mxkj.yuanyintang.utils.string.StringUtils
import java.util.concurrent.TimeUnit

import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer

open class MySongListAdapter(data: List<MySongListBean.DataBeanX.DataBean>) : BaseQuickAdapter<MySongListBean.DataBeanX.DataBean, BaseViewHolder>(R.layout.item_my_song_list, data) {
    override fun convert(helper: BaseViewHolder, item: MySongListBean.DataBeanX.DataBean, position: Int) {
        try {
            if (!TextUtils.isEmpty(item.imgpic_info.link)) {
                ImageLoader.with(mContext)
                        .override(80, 80)
                        .url(item.imgpic_info.link)
                        .rectRoundCorner(5)
                        .scale(ScaleMode.CENTER_CROP)
                        .into(helper.getView<CircleImageView>(R.id.civ_headimg))
            } else {
                ImageLoader.with(mContext)
                        .override(80, 80)
                        .res(R.drawable.logo_defaulft)
                        .rectRoundCorner(5)
                        .scale(ScaleMode.CENTER_CROP)
                        .into(helper.getView<CircleImageView>(R.id.civ_headimg))
            }

        } catch (e: RuntimeException) {
        }
        helper.setText(R.id.tv_name, StringUtils.isEmpty(item.title))
        helper.setText(R.id.tv_play_num, StringUtils.setNum(item.counts))
        helper.setText(R.id.tv_total_num, StringUtils.setNum(item.total))
        RxView.clicks(helper.getView(R.id.layout_click))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    val intent = Intent(mContext, SongSheetDetailsActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, item.id.toString() + "")
                    intent.putExtras(bundle)
                    mContext.startActivity(intent)
                }

    }
}
