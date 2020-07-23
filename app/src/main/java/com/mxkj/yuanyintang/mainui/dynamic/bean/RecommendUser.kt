package com.mxkj.yuanyintang.mainui.dynamic.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

import java.io.Serializable


class RecommendUser : Serializable {
    var code: Int = 0
    var msg: String? = null
    var data: List<DataBean>? = null

    class DataBean : MultiItemEntity {
        var id: Int = 0
        var fans_num: Int = 0
        var nickname: String? = null
        var mobile: String? = null
        var sex: Int = 0
        var head: String? = null
        var signature: String? = null
        var head_link: String? = null


        var isChecked = true

        override fun getItemType(): Int {
            return 0
        }
    }
}
