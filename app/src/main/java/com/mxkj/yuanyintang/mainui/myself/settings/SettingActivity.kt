package com.mxkj.yuanyintang.mainui.myself.settings

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog
import com.mxkj.yuanyintang.mainui.myself.activity.FeedBackActivity
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.app.UpDataApkTools
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage
import com.mxkj.yuanyintang.mainui.message.activity.MessageSettingActivity
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil
import com.mxkj.yuanyintang.mainui.myself.settings.activity.About
import com.mxkj.yuanyintang.mainui.myself.settings.activity.ChangeCoutActivity
import com.mxkj.yuanyintang.mainui.myself.settings.activity.ChangePwdActivity
import com.mxkj.yuanyintang.mainui.myself.settings.activity.CleanCache
import com.mxkj.yuanyintang.mainui.myself.settings.activity.InviteOthersActivity
import com.mxkj.yuanyintang.mainui.myself.settings.activity.PlaySettingActivity
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.rxbus.RxBus
import com.mxkj.yuanyintang.utils.rxbus.event.EMECodeEvent

import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.base.bean.UserInfo
import com.mxkj.yuanyintang.mainui.myself.activity.EditPersonalProfileActivity
import com.mxkj.yuanyintang.mainui.myself.activity.SuggestActivity
import com.mxkj.yuanyintang.utils.SaveMessage
import com.umeng.analytics.MobclickAgent
import okhttp3.Headers
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.uc_navigationbar.*

class SettingActivity : StandardUiActivity() {
    override val isVisibilityBottomPlayer: Boolean
        get() = false

    public override fun setLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initView() {
        setTitleText("设置")
        onViewClick()
    }

    override fun initData() {
        val userInfoJson = CacheUtils.getString(this@SettingActivity, Constants.User.USER_JSON)
        if (userInfoJson != null && userInfoJson.length > 10) {
            Log.e(TAG, "initData: $userInfoJson")
            UserInfoUtil.getUserInfoByJson(userInfoJson) { infoBean ->
                if (infoBean != null) {
                    userInfoBean = infoBean
                    if (infoBean.data != null) {
                        if (TextUtils.isEmpty(infoBean.data!!.mobile) && TextUtils.isEmpty(infoBean.data!!.email)) {
                            change_pwd.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun onViewClick() {
        RxView.clicks(selfInfo).subscribe {
            MobclickAgent.onEvent(this, "setting_edit")
            when {
                CacheUtils.getBoolean(this@SettingActivity, Constants.User.IS_LOGIN) -> startActivity(Intent(this, EditPersonalProfileActivity::class.java))
                else -> startActivity(Intent(this, LoginRegMainPage::class.java))
            }
        }
        RxView.clicks(feed_back).subscribe { startActivity(Intent(this, SuggestActivity::class.java)) }
        RxView.clicks(invite_others).subscribe {
            MobclickAgent.onEvent(this, "setting_friend")

            when {
                CacheUtils.getBoolean(this@SettingActivity, Constants.User.IS_LOGIN) -> startActivity(Intent(this, InviteOthersActivity::class.java))
                else -> startActivity(Intent(this, LoginRegMainPage::class.java))
            }
        }
        RxView.clicks(change_cout).subscribe {
            MobclickAgent.onEvent(this, "setting_couts")

            when {
                CacheUtils.getBoolean(this@SettingActivity, Constants.User.IS_LOGIN) -> startActivity(Intent(this, ChangeCoutActivity::class.java))
                else -> startActivity(Intent(this, LoginRegMainPage::class.java))
            }
        }
        RxView.clicks(leftButton).subscribe { finish() }
        RxView.clicks(change_pwd).subscribe {
            MobclickAgent.onEvent(this, "setting_pwd")
            when {
                CacheUtils.getBoolean(this@SettingActivity, Constants.User.IS_LOGIN) -> startActivity(Intent(this, ChangePwdActivity::class.java))
                else -> startActivity(Intent(this, LoginRegMainPage::class.java))
            }
        }
        RxView.clicks(play_setting).subscribe {
            MobclickAgent.onEvent(this, "setting_play")
            startActivity(Intent(this, PlaySettingActivity::class.java))
        }
        RxView.clicks(msg_setting).subscribe {
            MobclickAgent.onEvent(this, "setting_msg")
            when {
                CacheUtils.getBoolean(this@SettingActivity, Constants.User.IS_LOGIN) -> startActivity(Intent(this, MessageSettingActivity::class.java))
                else -> startActivity(Intent(this, LoginRegMainPage::class.java))
            }
        }
        RxView.clicks(clean_data).subscribe {
            MobclickAgent.onEvent(this, "setting_cache")
            startActivity(Intent(this, CleanCache::class.java))
        }
        RxView.clicks(version).subscribe {
            MobclickAgent.onEvent(this, "setting_update")
            UpDataApkTools.getInstance().setContext(this).upDataApkData(true, "")
        }
        RxView.clicks(about).subscribe {
            MobclickAgent.onEvent(this, "setting_about")
            startActivity(Intent(this, About::class.java))
        }
        RxView.clicks(login_out).subscribe {
            MobclickAgent.onEvent(this, "setting_outlogin")
            when {
                !CacheUtils.getBoolean(this@SettingActivity, Constants.User.IS_LOGIN) -> login_out.visibility = View.GONE
            }
            BaseConfirmDialog.newInstance().cancleText("手滑了").confirmText("退出").content("退出登录？").title("提示").showDialog(this, object : BaseConfirmDialog.onBtClick {
                override fun onConfirm() {
                    showLoadingView()
                    NetWork.outLogin(this@SettingActivity, object : NetWork.TokenCallBack {
                        override fun doNext(resultData: String, headers: Headers?) {
                            sendBroadcast(Intent("publishDynamic"))
                            hideLoadingView()
                            CacheUtils.setBoolean(this@SettingActivity, Constants.User.IS_LOGIN, false)
                            CacheUtils.setString(this@SettingActivity, Constants.User.USER_JSON, "")
                            CacheUtils.setString(this@SettingActivity, Constants.User.USER_LOGIN_TOKEN, "")
                            RxBus.getDefault().post(EMECodeEvent(203))
                            RxBus.getDefault().post("resetData")
                            finish()


                            SaveMessage.clearLogintoken(this@SettingActivity)
                        }

                        override fun doError(msg: String) {

                        }

                        override fun doResult() {

                        }
                    })
                }

                override fun onCancle() {

                }
            })
        }


    }

    override fun onResume() {
        super.onResume()
        login_out.visibility = if (CacheUtils.getBoolean(this@SettingActivity, Constants.User.IS_LOGIN)) View.VISIBLE else View.GONE
    }

    companion object {
        var userInfoBean: UserInfo? = null
    }

    override fun initEvent() {
    }
}
