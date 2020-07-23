package com.mxkj.yuanyintang.base.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean

import java.io.Serializable

class MusicInfo : Serializable {

    var data: List<DataBean>? = null

    class DataBean : Serializable, MultiItemEntity {

        var buffer_progress: Int = 0
        var id: Int = 0
        var item_id: Int = 0
        var title: String? = null
        var video: String? = null
        var counts: Int = 0
        var shares: Int = 0
        var music_type: Int = 0
        var downloads: Int = 0
        var up_time: Int = 0
        var nickname: String=""
        var catename: String=""
        var playtime: String=""
        var song_id: Int = 0
        var head_link: String=""
        var status: Int = 0
        var category: Int = 0
        var comment: Int = 0
        var collects: Int = 0
        var tag: String=""
        var uid: Int = 0
        var create_time: Int = 0
        var lyrics: String = ""
        var intro: String = ""
        var tag_xs: Int = 0
        var tag_yz: Int = 0
        var tag_fg: Int = 0
        var tag_zt: Int = 0
        var tag_sx: Int = 0
        var imgpic: String=""
        var original: String=""
        var isdown: Int = 0
        var isCurrentPlaying = false
        var collection: Int = 0
        var loadType: String=""//加载模式：locality（本地加载）
        var localityId: Int = 0
        var fileName: String = ""
        var song_title: String=""
        var lottery_num: Int = 0
        var imgpic_info:  ImgpicInfoBean? = null
        var video_info: VideoInfoBean? = null

        var mv: String? = null
        var mv_info: MvInfoBean? = null



        var isChecked = false//选择音乐时是否被选中
        var isLocalMusic = false

        var share_url: String=""

        var is_relation: Int = 0

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
            var link: String = ""
        }

        object MvInfoBean : Serializable {

            var ext: String? = null
            var size: Int? = null
            var playtime: String? = null
            var bitrate: String? = null
            var height: String? = null
            var width: String? = null
            var fps: Int? = null
            var md5: String? = null
            var link: String = ""
        }
        override fun getItemType(): Int {
            return 0
        }
    }
}
