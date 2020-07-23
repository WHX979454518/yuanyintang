package com.mxkj.yuanyintang.mainui.dynamic.adapter

import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.CheckBox

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.dynamic.bean.RecommendUser

import de.hdodenhof.circleimageview.CircleImageView

class RecommendUserAdapter(data: List<RecommendUser.DataBean>) : BaseMultiItemQuickAdapter<RecommendUser.DataBean, BaseViewHolder>(data) {
    var list: List<RecommendUser.DataBean>
        internal set

    override fun getItemCount(): Int {
        return if (list.size > 15) {
            15
        } else super.getItemCount()

    }

    init {
        this.list = data
        addItemType(0, R.layout.item_recommend_user)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun convert(helper: BaseViewHolder, item: RecommendUser.DataBean) {
        val box = helper.getView<CheckBox>(R.id.box)
        helper.setText(R.id.tv_nickname, item.nickname)
        Glide.with(mContext).load(item.head_link).asBitmap().placeholder(R.drawable.nothing).into(helper.getView(R.id.img_icon) as CircleImageView)
        box.isChecked = item.isChecked

        box.setOnCheckedChangeListener { _, b ->
            val position = helper.position
            for (i in list.indices) {
                if (position == i) {
                    list[i].isChecked = b
                }
            }
        }
    }
}
