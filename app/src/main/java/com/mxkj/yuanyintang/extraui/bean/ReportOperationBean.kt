package com.mxkj.yuanyintang.extraui.bean

class ReportOperationBean {

    var code: Int = 0
    var msg: String? = null
    var data: List<DataBean>? = null

    class DataBean {
        var id: Int = 0
        var code: String? = null
        var title: String? = null
        var state: Int = 0
        var required: Int = 0
        var pid: Int = 0
        var count: Int = 0
        var sort: Int = 0
        var check: Boolean? = null
            get() = if (null == field) {
                false
            } else field
        var create_time: Int = 0
    }
}
