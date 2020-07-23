package com.mxkj.yuanyintang.base.bean

import java.io.Serializable

/**
 * Created by LiuJie on 2017/12/29.
 */

class StartImgBean : Serializable {
    var code: Int = 0
    var msg: String? = null
    lateinit var data: DataBean

    class DataBean : Serializable {
        var id: Int = 0
        var title: String? = null
        var type: String? = null
        var imgpic: String? = null
        var target_url: String? = null
        var item_type: Int = 0
        var show_time: Int = 0
        var start_time: Int = 0
        var end_time: Int = 0
        //        private String imgpic_link;


        lateinit var imgpic_info: ImgpicInfoBean

        class ImgpicInfoBean : Serializable {
            var ext: String? = null
            var w: String? = null
            var h: String? = null
            var size: String? = null
            var is_long: String? = null
            var md5: String? = null
            var link: String? = null
        }
    }
}
