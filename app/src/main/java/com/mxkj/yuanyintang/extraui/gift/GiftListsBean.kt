package com.mxkj.yuanyintang.extraui.gift

import java.io.Serializable

class GiftListsBean : Serializable {
    var code: Int = 0
    var msg: String? = null
    var data: DataBean? = null

    class DataBean : Serializable {
        var my_coin: Int = 0
        var gift_list: List<GiftListBean>? = null
        var setting: SettingBean? = null
        class GiftListBean {
            var id: Int = 0
            var type: Int = 0
            var name: String? = null
            var coin_num: Int = 0
            var icon: String? = null
            var imgpic: String? = null
            var imgpic_link: String? = null
            var old_price: String? = null
            var price = "0"
            var imgpic_info: ImgpicInfoBean? = null
            var isSelected: Boolean = false
            var icon_info: ImgpicInfoBean? = null


            class ImgpicInfoBean {
                var ext: String? = null
                var w: String? = null
                var h: String? = null
                var size: String? = null
                var is_long: String? = null
                var md5: String? = null
                var link: String? = null
            }
        }


        class SettingBean {
            var first_charge: Int = 0
            var first_remark: String? = null
            var first_desc: String? = null
        }
    }
}
