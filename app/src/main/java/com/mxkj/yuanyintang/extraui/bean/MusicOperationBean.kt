package com.mxkj.yuanyintang.extraui.bean


class MusicOperationBean {

    var drawable: Int = 0
    var title: String? = null
    var type: Int = 0

    constructor(drawable: Int, title: String, type: Int) {
        this.drawable = drawable
        this.title = title
        this.type = type
    }

}
