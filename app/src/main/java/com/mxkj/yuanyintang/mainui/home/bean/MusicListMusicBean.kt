package com.mxkj.yuanyintang.mainui.home.bean

import com.mxkj.yuanyintang.base.bean.MusicInfo

import java.io.Serializable

/**
 * Created by LiuJie on 2017/9/22.
 */

class MusicListMusicBean {

    var id: Int = 0
    var title: String? = null
    var imgpic: String? = null
    var comment: Int = 0
    var counts: Int = 0
    var uid: Int = 0
    var nickname: String? = null
    var catename: String? = null
    var video: String? = null
    var playtime: String? = null
    var dayhits: Int = 0
    var weekhits: Int = 0
    var monthhits: Int = 0
    var collection: Int = 0
    var song_id: Int = 0
    //    private String imgpic_link;
    //    private String video_link;
    var check: Boolean? = false
    var video_info: MusicInfo.DataBean.VideoInfoBean? = null


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
