package com.mxkj.yuanyintang.pay.ali_pay

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil

class AliPayCallbackActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.baseTransparentStatusBar(this)
        setContentView(R.layout.activity_qqpay_callback)
        val status = findViewById<TextView>(R.id.tv_status)
        val pay_code = intent.getStringExtra(PAY_CODE)
        when (pay_code) {
            "9000" -> status.text = "充值成功~"
            "4000" -> status.text = "充值失败~"
            "6001" -> status.text = "取消充值~"
        }
        Handler().postDelayed({ finish() }, 1000)

    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(0, 0)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    companion object {
        const val PAY_CODE = "PAY_CODE"
    }
}
