package com.mxkj.yuanyintang.utils.ali_file_upload

import com.mxkj.yuanyintang.base.bean.MusicInfo

import java.io.Serializable

class FileBean {
    var code: Int = 0
    var msg: String? = null
    var data: DataBean? = null

    class DataBean {
        var endpoint: String? = null
        var bucket: String? = null
        var callbackUrl: String? = null
        var callbackBody: String? = null
        var imgpic: String? = null
        var video: String? = null
        var mv:String?=null
        var uptype: Int = 0
        var video_info: MusicInfo.DataBean.VideoInfoBean? = null

        var imgpic_link: String? = null
        var dir: String? = null


        var imgpic_info: ImgpicInfoBean? = null

        class VideoInfoBean : Serializable {

            var ext: String? = null
            var size: Int = 0
            var playtime: String? = null
            var bitrate: String? = null
            var md5: String? = null
            var link: String? = null
        }

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
