package com.mxkj.yuanyintang.database.songhistorysql

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

import java.io.Serializable
@DatabaseTable(tableName = "yyt_songhistroy")//声明表名
class SongHistoryBean {
    @DatabaseField(generatedId = true)//generatedId   id为主键且自动生成
    private var id: Int = 0
    @DatabaseField(columnName = "contents")//columnName   数据中的列名
    private var contents: String? = null
    @DatabaseField(columnName = "time")
    private var time: Int = 0
    @DatabaseField(columnName = "music_id")//音乐id
    private var music_id: Int = 0
    @DatabaseField(columnName = "music_name")//音乐名称
    private var music_name: String? = null
    @DatabaseField(columnName = "imgpic_link")//音乐封面
    private var imgpic_link: String? = null
    var imgpic_info: ImgpicInfoBean? = null

    @DatabaseField(columnName = "video_link")//音乐地址
    private var video_link: String? = null
    @DatabaseField(columnName = "song_id")//歌单id
    private var song_id: Int = 0
    @DatabaseField(columnName = "playtime")//播放时长
    private var playtime: String? = null
    @DatabaseField(columnName = "uid")//音乐人id
    private var uid: Int = 0
    @DatabaseField(columnName = "music_nickname")//音乐人昵称
    private var music_nickname: String? = null
    @DatabaseField(columnName = "catename")//音乐分类名称
    private var catename: String? = null
    @DatabaseField(columnName = "lyrics")//歌词
    private var lyrics: String? = null

    private var check: Boolean? = false
    private var single_selection: Boolean? = false

    class ImgpicInfoBean : Serializable {

        var ext: String? = null
        var w: String? = null
        var h: String? = null
        var size: String? = null
        var is_long: String? = null
        var md5: String? = null
        var link: String? = null
    }

    fun getId(): Int {
        return id
    }

    fun getTime(): Int {
        return time
    }

    fun setTime(time: Int): SongHistoryBean {
        this.time = time
        return this
    }

    fun setId(id: Int): SongHistoryBean {
        this.id = id
        return this
    }

    fun getContents(): String? {
        return contents
    }

    fun setContents(contents: String): SongHistoryBean {
        this.contents = contents
        return this
    }

    fun getMusic_id(): Int {
        return music_id
    }

    fun setMusic_id(music_id: Int): SongHistoryBean {
        this.music_id = music_id
        return this
    }

    fun getMusic_name(): String? {
        return music_name
    }

    fun setMusic_name(music_name: String): SongHistoryBean {
        this.music_name = music_name
        return this
    }

    fun getImgpic_link(): String? {
        return imgpic_link
    }

    fun setImgpic_link(imgpic_link: String): SongHistoryBean {
        this.imgpic_link = imgpic_link
        return this
    }

    fun getVideo_link(): String? {
        return video_link
    }

    fun setVideo_link(video_link: String): SongHistoryBean {
        this.video_link = video_link
        return this
    }

    fun getSong_id(): Int {
        return song_id
    }

    fun setSong_id(song_id: Int): SongHistoryBean {
        this.song_id = song_id
        return this
    }

    fun getPlaytime(): String? {
        return playtime
    }

    fun setPlaytime(playtime: String): SongHistoryBean {
        this.playtime = playtime
        return this
    }

    fun getUid(): Int {
        return uid
    }

    fun setUid(uid: Int): SongHistoryBean {
        this.uid = uid
        return this
    }

    fun getMusic_nickname(): String? {
        return music_nickname
    }

    fun setMusic_nickname(music_nickname: String): SongHistoryBean {
        this.music_nickname = music_nickname
        return this
    }

    fun getCatename(): String? {
        return catename
    }

    fun setCatename(catename: String?): SongHistoryBean {
        this.catename = catename
        return this
    }

    fun getLyrics(): String? {
        return lyrics
    }

    fun setLyrics(lyrics: String): SongHistoryBean {
        this.lyrics = lyrics
        return this
    }

    fun getCheck(): Boolean? {
        return check
    }

    fun setCheck(check: Boolean?): SongHistoryBean {
        this.check = check
        return this
    }

    fun getSingle_selection(): Boolean? {
        return single_selection
    }

    fun setSingle_selection(single_selection: Boolean?): SongHistoryBean {
        this.single_selection = single_selection
        return this
    }
}
