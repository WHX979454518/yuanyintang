package com.mxkj.yuanyintang.extraui.bean

import java.io.Serializable

class CommentPictureDataBean : Serializable {

    internal var dataBeanList: ArrayList<DataBean>? = null
    internal var position: Int = 0

    fun getDataBeanList(): List<DataBean>? {
        return dataBeanList
    }

    fun setDataBeanList(dataBeanList: ArrayList<DataBean>): CommentPictureDataBean {
        this.dataBeanList = dataBeanList
        return this
    }

    fun getPosition(): Int {
        return position
    }

    fun setPosition(position: Int): CommentPictureDataBean {
        this.position = position
        return this
    }

    class DataBean : Serializable {

        internal var img: String? = null
        internal var title: String? = null
        internal var nickname: String? = null
        internal var commentNum: Int = 0
        internal var size: Int = 0
        internal var tag: Int = 0


        fun getImg(): String? {
            return img
        }

        fun setImg(img: String): DataBean {
            this.img = img
            return this
        }

        fun getTitle(): String? {
            return title
        }

        fun setTitle(title: String): DataBean {
            this.title = title
            return this
        }

        fun getNickname(): String? {
            return nickname
        }

        fun setNickname(nickname: String): DataBean {
            this.nickname = nickname
            return this
        }

        fun getCommentNum(): Int {
            return commentNum
        }

        fun setCommentNum(commentNum: Int): DataBean {
            this.commentNum = commentNum
            return this
        }

        fun getSize(): Int {
            return size
        }

        fun setSize(size: Int): DataBean {
            this.size = size
            return this
        }

        fun getTag(): Int {
            return tag
        }

        fun setTag(tag: Int): DataBean {
            this.tag = tag
            return this
        }
    }
}
