package com.mxkj.yuanyintang.extraui.bean

import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean
import com.mxkj.yuanyintang.utils.string.StringUtils

import java.io.Serializable

class MusicBean : Serializable {

    internal var music_id: String? = null//音乐id
    internal var commentNum: Int = 0//评论数
    internal var music_name: String? = null//音乐名字
    internal var musician_name: String? = null//音乐人名字
    internal var uid: Int = 0
    internal var canEdit: Boolean = true
    internal var song_id: Int = 0//歌单id
    internal var collection: Int = 0//是否收藏
    internal var statu: String ?=""//是否收藏
    internal var position: Int = 0//Adapter的Postion
    internal var reportType: Int = 0//1音乐，2歌单，3圈子，4话题or话题一级评论，5评论，8举报反馈，29话题二级评论
    internal var reportId: Int = 0
    internal var type: Int = 0//评论类型 1:音乐 2:歌单 3:圈子 4:评论 5:池塘, 6下载,7已收藏歌曲 8音乐人详情 9来源 10有mv的
    //    internal String imgpic_link;
    var imgpic_info: ImgpicInfoBean? = null

    internal var mv: String ?=""//是否有mv


    private var isMultiSelect: Boolean = false
    //    internal String video_link;
    internal var shareDataBean: ShareDataBean? = null
    internal var sourceType: String? = null//来源状态，判断跳转到哪个页面
    var ext: String? = null
    internal var playTime: String? = null
    internal var songName: String? = null
    internal var commentType: Int = 0
    internal var musicIanId: String? = null
    internal var imgpic: String? = null
    internal var signature: String? = null
    var video_info: MusicInfo.DataBean.VideoInfoBean? = null

    var mvInfoBean : MvInfoBean? = null

    class MvInfoBean : Serializable{
        /**
         * ext : m4a
         * size : 445069
         * playtime : 00:05
         * bitrate : 597
         * height :640
         * width:360
         * fps :25
         * md5 : 3707d269505bc28be2864f5d87e7343b
         * link : http:\/\/testapi.imxkj.com\/\/video\/3707d269505bc28be2864f5d87e7343b.mp4?log_at=3
         */
        private var ext: String? = null
        private var size: Int = 0
        private var playtime: String? = null
        private var bitrate: String? = null
        private var height: Int = 0
        private var width: Int = 0
        private var fps: Int = 0
        private var md5: String? = null
        var link: String? = null
        override fun toString(): String {
            return "MvInfoBean(ext=$ext, size=$size, playtime=$playtime, bitrate=$bitrate, height=$height, width=$width, fps=$fps, md5=$md5, link=$link)"
        }

    }

    class ImgpicInfoBean : Serializable {
        /**
         * ext :
         * w :
         * h :
         * size : 330492
         * is_long : 0
         * md5 : c7adcb987e5224301258c6f7efb2d53e
         * link : http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3
         */

        var ext: String? = null
        var w: String? = null
        var h: String? = null
        var size: String? = null
        var is_long: String? = null
        var md5: String? = null
        var link: String? = null
    }




    class VideoInfoBean : Serializable {

        /**
         * ext : mp3
         * size : 18302306
         * playtime : 07:37
         * bitrate : 320
         * md5 : 4b1b917652c542247565aa5ace5a7a5c
         * link : http://api.demo.com//music/4b1b917652c542247565aa5ace5a7a5c/1
         */

        var ext: String? = null
        var size: Int = 0
        var playtime: String? = null
        var bitrate: String? = null
        var md5: String? = null
        var link: String? = null
        var type: String? = null
    }

    fun getSignature(): String? {
        return signature
    }

    fun setSignature(signature: String): MusicBean {
        this.signature = signature
        return this
    }

    fun getImgpic(): String? {
        return imgpic
    }

    fun setImgpic(imgpic: String): MusicBean {
        this.imgpic = imgpic
        return this
    }

    fun getMusicIanId(): String? {
        return musicIanId
    }

    fun setMusicIanId(musicIanId: String): MusicBean {
        this.musicIanId = musicIanId
        return this
    }

    fun getCommentType(): Int {
        return commentType
    }

    fun setCommentType(commentType: Int): MusicBean {
        this.commentType = commentType
        return this
    }

    fun getSongName(): String? {
        return songName
    }

    fun setSongName(songName: String?): MusicBean {
        this.songName = songName
        return this
    }

    fun getPlayTime(): String? {
        return playTime
    }

    fun setPlayTime(playTime: String): MusicBean {
        this.playTime = playTime
        return this
    }

 fun getStatu(): String? {
        return statu
    }

    fun setStatu(statu: String): MusicBean {
        this.statu = statu
        return this
    }

    //新添加的mv
    fun getMv(): String? {
        return mv
    }

    fun setMv(mv: String): MusicBean {
        this.mv = mv
        return this
    }

    fun getShareDataBean(): ShareDataBean? {
        return shareDataBean
    }

    fun setShareDataBean(shareDataBean: ShareDataBean): MusicBean {
        this.shareDataBean = shareDataBean
        return this
    }

    fun getMultiSelect(): Boolean? {
        return isMultiSelect
    }

    fun setMultiSelect(multiSelect: Boolean): MusicBean {
        isMultiSelect = multiSelect
        return this
    }

    fun getPosition(): Int {
        return position
    }

    fun setPosition(position: Int): MusicBean {
        this.position = position
        return this
    }

    fun getCollection(): Int {
        return collection
    }

    fun setCollection(collection: Int): MusicBean {
        this.collection = collection
        return this
    }

    fun getSong_id(): Int {
        return song_id
    }

    fun setSong_id(song_id: Int): MusicBean {
        this.song_id = song_id
        return this
    }

    fun getUid(): Int {
        return uid
    }

    fun setUid(uid: Int): MusicBean {
        this.uid = uid
        return this
    }

    fun getCommentNum(): Int {
        return commentNum
    }

    fun setCommentNum(commentNum: Int): MusicBean {
        this.commentNum = commentNum
        return this
    }

    fun getMusic_name(): String? {
        return music_name
    }

    fun setMusic_name(music_name: String?): MusicBean {
        this.music_name = music_name+""
        return this
    }

    fun getMusician_name(): String? {
        return musician_name
    }

    fun setMusician_name(musician_name: String?): MusicBean {
        this.musician_name = musician_name+""
        return this
    }

    fun getMusic_id(): String? {
        return music_id
    }

    fun setMusic_id(music_id: String): MusicBean {
        this.music_id = music_id
        return this
    }

    fun setCanEdit(canEdit: Boolean): MusicBean {
        this.canEdit = canEdit
        return this
    }
     fun getCanEdit(): Boolean {
        return canEdit
    }





    fun getReportType(): Int {
        return reportType
    }

    fun setReportType(reportType: Int): MusicBean {
        this.reportType = reportType
        return this
    }

    fun getReportId(): Int {
        return reportId
    }

    fun setReportId(reportId: Int): MusicBean {
        this.reportId = reportId
        return this
    }

    fun getType(): Int {
        return type
    }

    fun setType(type: Int): MusicBean {
        this.type = type
        return this
    }

    fun getSourceType(): String? {
        return sourceType
    }

    fun setSourceType(sourceType: String): MusicBean {
        this.sourceType = sourceType
        return this
    }

    class ShareDataBean : Serializable {
        var uid: Int = 0
        var image_link: String? = null//歌单，音乐，池塘图片，
        var video_link: String? = null//音乐播放地址
        var mv_link: String? = null//mv播放地址
        var share_link: String? = null//音乐分享¬地址
        var title: String? = null//歌单，音乐，池塘标题，
        var mv: String? = null//mv(用来看是否有mv这个东西)，
        var is_self: String? = null//mv(用来看是否有mv这个东西)，
        var nickname: String? = null//歌单，音乐，池塘作者
        var type: String? = null//img,music,pond ,album
        var imgFilePath: String? = null//分享图片  本地地址
        var imgByte: ByteArray? = null//分享图片 字节数组
        var webImgUrl: String? = null//第三方分享要的缩略图地址(分享图片时做为网络图片地址)


        var topicContent: String? = null//池塘content
        var sinaDescription: String? = null
        var shareUrl: String? = null//池塘或者网页的地址
        var activityAlias: String? = null
            get() = StringUtils.isEmpty(field)
        var activityType: String? = null
            get() = StringUtils.isEmpty(field)// 5:抽奖活动,6:投票活动
        var muisic_id: Int = 0//歌单，音乐，池塘id
        var isShareMyIncode: Boolean = false
    }
}
