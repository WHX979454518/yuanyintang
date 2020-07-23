package com.mxkj.yuanyintang.pay.qq_pay

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import com.tencent.mobileqq.openpay.api.IOpenApi
import com.tencent.mobileqq.openpay.api.IOpenApiListener
import com.tencent.mobileqq.openpay.api.OpenApiFactory
import com.tencent.mobileqq.openpay.data.base.BaseResponse
import com.tencent.mobileqq.openpay.data.pay.PayResponse

class QQPayCallbackActivity : Activity(), IOpenApiListener {
    private var appId = "1106114082"
    private lateinit var openApi: IOpenApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.baseTransparentStatusBar(this)
        setContentView(R.layout.activity_qqpay_callback)
        openApi = OpenApiFactory.getInstance(this, appId)
        openApi.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        openApi.handleIntent(intent, this)
    }

    override fun onOpenResponse(response: BaseResponse?) {
        val title = "Callback from mqq"
        val message: String
        if (response == null) {
            message = "response is null."
            return
        } else {
            if (response is PayResponse) {
                val payResponse = response as PayResponse?
                val retCode = payResponse!!.retCode
                val tv_status = findViewById<TextView>(R.id.tv_status)
                if (retCode == 0) {
                    tv_status.text = "充值成功~"
                } else {
                    tv_status.text = "充值失败~"
                }
                Handler().postDelayed({ finish() }, 2000)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(0, 0)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}
