package com.mxkj.yuanyintang.base.bean

class DegreeBean {
    var code: Int = 0
    var msg: String? = null
    var data: DataBean? = null

    class DataBean {
        var id: Int = 0
        var alias: String? = null
        var status: Int = 0
        var wait_time: Int = 0
        var remark: String? = null
        var wait_time_desc: String? = null
    }
}
