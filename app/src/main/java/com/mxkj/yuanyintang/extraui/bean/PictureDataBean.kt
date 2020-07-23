package com.mxkj.yuanyintang.extraui.bean

import java.io.Serializable

class PictureDataBean : Serializable {
    internal var title: String? = null
    internal var nickname: String? = null
    internal var photoList: List<String>? = null
    private var commentNum: Int = 0
    internal var id: String? = null
    internal var position: Int = 0
    internal var hits: Int = 0
    internal var type: String? = null

    fun getType(): String? {
        return type
    }

    fun setType(type: String): PictureDataBean {
        this.type = type
        return this
    }

    fun getHits(): Int {
        return hits
    }

    fun setHits(hits: Int): PictureDataBean {
        this.hits = hits
        return this
    }

    fun getPosition(): Int {
        return position
    }

    fun setPosition(position: Int): PictureDataBean {
        this.position = position
        return this
    }


    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String): PictureDataBean {
        this.title = title
        return this
    }

    fun getNickname(): String? {
        return nickname
    }

    fun setNickname(nickname: String): PictureDataBean {
        this.nickname = nickname
        return this
    }

    fun getPhotoList(): List<String>? {
        return photoList
    }

    fun setPhotoList(photoList: List<String>): PictureDataBean {
        this.photoList = photoList
        return this
    }

    fun getCommentNum(): Int {
        return commentNum
    }

    fun setCommentNum(commentNum: Int): PictureDataBean {
        this.commentNum = commentNum
        return this
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String): PictureDataBean {
        this.id = id
        return this
    }
}
