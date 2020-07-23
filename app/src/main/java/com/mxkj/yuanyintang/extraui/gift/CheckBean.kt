package com.mxkj.yuanyintang.extraui.gift

import com.mxkj.yuanyintang.musicplayer.util.SheetDetialBean

import java.io.Serializable

class CheckBean : Serializable {

    var code: Int = 0
    var msg: String? = null
    var data: DataBean? = null
    class DataBean : Serializable {
        var music_id: Int = 0
        var gift_id: Int = 0
        var order_cate: OrderCateBean? = null
        var last_pay_type: Int = 0
        var music_title: String? = null
        var icon: String? = null
        var name: String? = null
        var imgpic: String? = null
        var my_coin: Int = 0
        var icon_link: String? = null
        var type: Int = 0
        var icon_info: IconInfoBean? = null
        var imgpic_info: SheetDetialBean.DataBean.ImgpicInfoBean? = null

        class ImgpicInfoBean : Serializable {
            var ext: String? = null
            var w: String? = null
            var h: String? = null
            var size: String? = null
            var is_long: String? = null
            var md5: String? = null
            var link: String? = null
        }

        class OrderCateBean : Serializable {
            var lists: ListsBean? = null
            var setting: SettingBean? = null

            class ListsBean : Serializable {
                var id: Int = 0
                var type: Int = 0
                var coin_num: Int = 0
                var price: Float = 0.toFloat()
                var discount_price: Float = 0.toFloat()
                var title: String? = null
                var remark: String? = null
                var end_time: String? = null
                var start_time: String? = null
            }

            class SettingBean : Serializable {
                var first_charge: Int = 0
                var first_remark: String? = null
            }
        }

        class IconInfoBean : Serializable {
            var ext: String? = null
            var w: String? = null
            var h: String? = null
            var size: String? = null
            var is_long: String? = null
            var md5: String? = null
           lateinit var link: String
        }
    }
}
