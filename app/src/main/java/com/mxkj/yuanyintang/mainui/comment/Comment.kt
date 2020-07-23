package com.mxkj.yuanyintang.mainui.comment

import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.base.bean.UserInfo
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils

import java.io.Serializable

class Comment : Serializable {
    var code: Int = 0
    var msg: String? = null
    var data: List<DataBean>? = null

    class DataBean : Serializable, MultiItemEntity {
        var head: String? = null
        var nickname: String? = null
        var to_nickname: String? = null
        var floor = ""
            get() = "| # $field"
        var revert: String? = null
        var create_time: String? = null
        var vip: Int = 0
        var item_id: Int = 0
        var id: Int = 0
        var to_uid: Int = 0
        var sex: Int = 0
        var to_sex: Int = 0
        var type: Int = 0
        var content: String? = null
        var agrees: Int = 0
        var uid: Int = 0

        var p_uid: Int = 0
        var pid: Int = 0
        var member_type: Int = 0
        var is_auth: Int = 0
        var is_music: Int = 0
        var fid: Int = 0
        var comment: Int = 0
        var page_counts: Int = 0
        var page: Int = 0
        var is_agree: Int = 0
        var head_link: String? = null
        var son: ArrayList<SonBean>? = null

        override fun getItemType(): Int {
            val string = CacheUtils.getString(MainApplication.application, Constants.User.USER_JSON)
            val userInfoBean = JSON.parseObject(string, UserInfo::class.java)
            return if (userInfoBean?.data != null && userInfoBean.data!!.id != 0) {
                if (userInfoBean.data!!.id == uid) {
                    1
                } else {
                    0
                }
            } else 0
        }

        class SonBean : Serializable, MultiItemEntity {
            var head: String? = null
            var nickname: String? = null
            var create_time: String? = null
            var vip: Int = 0
            var item_id: Int = 0
            var id: Int = 0
            var sex: Int = 0
            var type: Int = 0
            var content: String? = null
            var agrees: Int = 0
            var uid: Int = 0
            var pid: Int = 0
            var member_type: Int = 0
            var is_auth: Int = 0
            var is_music: Int = 0
            var fid: Int = 0
            var comment: Int = 0
            var revert: String? = null
            var p_uid: Int = 0
            var to_uid: Int = 0
            var is_agree: Int = 0
            var head_link: String? = null
            var to_nickname: String? = null

            override fun getItemType(): Int {
                return 0
            }
        }
    }
}
