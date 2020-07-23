package com.mxkj.yuanyintang.pay

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast

import com.alibaba.fastjson.JSON
import com.alipay.sdk.app.PayTask
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.pay.ali_pay.AliPayCallbackActivity
import com.mxkj.yuanyintang.pay.ali_pay.PayResult
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.mobileqq.openpay.api.OpenApiFactory
import com.tencent.mobileqq.openpay.constants.OpenConstants
import com.tencent.mobileqq.openpay.data.pay.PayApi

import okhttp3.Headers

object PayUtils {
    private const val SDK_PAY_FLAG = 1
    private const val AppID = "1106114082"
    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String, String>)
                    val resultStatus = payResult.resultStatus
                    val intent = Intent(MainApplication.context, AliPayCallbackActivity::class.java)
                    intent.putExtra(AliPayCallbackActivity.PAY_CODE, resultStatus)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    MainApplication.application!!.startActivity(intent)
                }
            }
        }
    }

    private fun useWxPay(context: Context, resultData: String) {
        val api = WXAPIFactory.createWXAPI(context, null)
        api.registerApp("wx67591dc5fc8029ac")
        val jObj = JSON.parseObject(resultData)
        val data = jObj.getJSONObject("data")
        val req = PayReq()
        req.appId = data.getString("appid")
        req.partnerId = data.getString("partnerid")
        req.prepayId = data.getString("prepayid")
        req.nonceStr = data.getString("noncestr")
        req.timeStamp = data.getString("timestamp")
        req.packageValue = data.getString("package")
        req.sign = data.getString("sign")
        req.extData = "app data"
        api.sendReq(req)
    }

    private fun useAliPay(context: Context, resultData: String) {
        val orderInfo = JSON.parseObject(resultData).getString("data")
        object : Thread() {
            override fun run() {
                val alipay = PayTask(context as Activity)
                val result = alipay.payV2(orderInfo, true)
                Log.i("msp", result.toString())
                val msg = Message()
                msg.what = SDK_PAY_FLAG
                msg.obj = result
                mHandler.sendMessage(msg)
            }
        }.start()
    }

    fun pay(context: Context, chargeId: Int, payment_type: Int) {
        if (payment_type == 0) return
        val params = HttpParams()
        params.put("payment_type", payment_type.toString() + "")
        params.put("order_cat_id", chargeId.toString() + "")
        NetWork.creatOrder(context, params, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val jObj = JSON.parseObject(resultData)
                val data = jObj.getJSONObject("data")
                val order_sn = data.getString("order_sn")
                val payurl = data.getString("pay_url")
                getOrderInfo(context, order_sn, payurl, payment_type)
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    private fun getOrderInfo(context: Context, ordercode: String?, payurl: String?, payment_type: Int) {
        payurl ?: return
        ordercode ?: return
        val params = HttpParams()
        params.put("ordercode", ordercode)
        params.put("logat", "3")
        NetWork.getOrderInfo(context, payurl, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                CacheUtils.setString(MainApplication.application, "giftStr", "")
                when (payment_type) {
                    1 -> useWxPay(context, resultData)
                    2 -> useQQPay(context, resultData)
                    3 -> useAliPay(context, resultData)
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {}
        })
    }

    private fun useQQPay(context: Context, resultData: String) {
        val jObj = JSON.parseObject(resultData)
        val data = jObj.getJSONObject("data")
        val openApi = OpenApiFactory.getInstance(context, AppID)
        val isInstalled = openApi.isMobileQQInstalled
        val isSupport = openApi.isMobileQQSupportApi(OpenConstants.API_NAME_PAY)
        if (!isInstalled) {
            Log.e("TAG", "没装qq: ")
            if (context is Activity) {
                Toast.makeText(context, "没安装QQ", Toast.LENGTH_SHORT).show()
            }
            return
        }
        if (!isSupport) {
            Log.e("TAG", "不支持QQ钱包: ")
            return
        }
        val api = PayApi()
        api.appId = AppID
        api.pubAccHint = ""
        api.serialNumber = data.getString("serialNumber")
        api.tokenId = data.getString("tokenId")
        api.pubAcc = ""
        api.nonce = data.getString("nonce")
        api.bargainorId = data.getString("bargainorId")
        api.sigType = "HMAC-SHA1"
        try {
            api.timeStamp = data.getLong("timeStamp")!!
            api.callbackScheme = "qwallet1106114082"
            api.sig = data.getString("sig")
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        if (api.checkParams()) {
            openApi.execApi(api)
        }
    }

}
