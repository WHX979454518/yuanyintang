package com.mxkj.yuanyintang.database.messagesql

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "yyt_message")//声明表名
class MessageBean {
    @DatabaseField(generatedId = true)//generatedId   id为主键且自动生成
    var id: Int = 0
    @DatabaseField(columnName = "contents")//columnName   数据中的列名
    var contents: String? = null
    @DatabaseField(columnName = "time")
    var time: Long? = null

    constructor() {

    }

    constructor(contents: String, time: Long?) {
        this.contents = contents
        this.time = time
    }


}
