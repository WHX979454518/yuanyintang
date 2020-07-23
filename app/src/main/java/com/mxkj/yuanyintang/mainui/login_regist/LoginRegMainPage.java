package com.mxkj.yuanyintang.mainui.login_regist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyphenate.chat.EMClient;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.login_regist.widget.EmailpopUpWindow;
import com.mxkj.yuanyintang.utils.SaveMessage;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.Serializable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.utils.constant.Constants.Other.EM_LOGIN;

public class LoginRegMainPage extends StandardUiActivity {
    private UMShareAPI mShareAPI;
    public static final String MOBILE = "U_MOBILE";
    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.user_notice)
    TextView userNotice;
    @BindView(R.id.copyright)
    TextView copyright;
    @BindView(R.id.ll_notice)
    LinearLayout llNotice;
    @BindView(R.id.et_cout)
    EditText etCout;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.ck_show)
    CheckBox ckShow;
    @BindView(R.id.bt_sure)
    TextView btSure;
    @BindView(R.id.loginByCode)
    TextView loginByCode;
    @BindView(R.id.forget_pwd)
    TextView forgetPwd;
    @BindView(R.id.tvLine)
    TextView tvLine;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.deleTxt)
    ImageView deleTxt;
    private PopupWindow popupWindow;
    private EmailpopUpWindow emailpopUpWindow;
    private InputMethodManager mInputManager;//软键盘管理类


    private String data = "";

    @Override
    public int setLayoutId() {
        return R.layout.activity_pwd_login_new;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mShareAPI = UMShareAPI.get(this);
        hideTitle(true);
        StatusBarUtil.baseTransparentStatusBar(this);


        if (null!=getIntent().getExtras()){
            Bundle i=getIntent().getExtras();
            if (null!=i.getString("params")){
                data=i.getString("params");//获取数据包
            }
        }

    }

    @Override
    protected void initEvent() {
        if (emailpopUpWindow == null) {
            emailpopUpWindow = new EmailpopUpWindow();
        }
        ckShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etPwd.setSelection(etPwd.getText().length());
            }
        });
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        etCout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (s.endsWith("@")) {
                    showEmailPop();
                }
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityCollector.INSTANCE.addActivity(this);
        MobclickAgent.onEvent(this,"pwd_count");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.INSTANCE.removeActivity(this);
    }

    @OnClick({R.id.finish, R.id.deleTxt, R.id.qq, R.id.wechat, R.id.sina, R.id.user_notice, R.id.copyright, R.id.bt_sure, R.id.loginByCode, R.id.forget_pwd})
    public void onClick(View view) {
        Intent intent = new Intent(this, UserNoticeActivity.class);
        switch (view.getId()) {
            case R.id.finish:
                MobclickAgent.onEvent(this,"pwd_back");
                finish();
                break;
            case R.id.user_notice:
                intent.putExtra("code", "agreement");
                startActivity(intent);
                break;
            case R.id.copyright:
                intent.putExtra("code", "copyrightpolicy");
                startActivity(intent);
                break;
            case R.id.bt_sure:
                if (TextUtils.isEmpty(etCout.getText().toString())) {
                    setSnackBar("请输入账号", "", R.drawable.icon_fails);
                } else {
                    showLoadingView();
                    NetWork.INSTANCE.PwdLogin(this, etCout.getText().toString().trim(), etPwd.getText().toString().trim(),data.toString(), new NetWork.TokenCallBack() {
                        @Override
                        public void doNext(final String resultData, Headers headers) {
                            MobclickAgent.onEvent(LoginRegMainPage.this,"pwd_log_success");

                            JSONObject jsonObject = JSON.parseObject(resultData);
                            Integer code = jsonObject.getInteger("code");
                            if (code == 0) {
                                hideLoadingView();
                                JSONObject checkObj = jsonObject.getJSONObject("data");
                                String key = checkObj.getString("key");
                                String mobile = checkObj.getString("mobile");
                                String email = checkObj.getString("email");
                                Intent intentAuth = new Intent(LoginRegMainPage.this, NewDeviceAuthActivity.class);
                                intentAuth.putExtra("key", StringUtils.isEmpty(key));
                                intentAuth.putExtra("mobile", StringUtils.isEmpty(mobile));
                                intentAuth.putExtra("email", StringUtils.isEmpty(email));
                                intentAuth.putExtra("etInput", StringUtils.isEmpty(etCout.getText().toString()));
                                startActivity(intentAuth);
                            } else {
                                if (jsonObject.getJSONObject("data").getInteger("log_num") == 0) {
                                    LoginSuccessToGo.goNext(LoginRegMainPage.this);
                                }
                                hideLoadingView();
                                EMClient.getInstance().chatManager().loadAllConversations();
                                EMClient.getInstance().groupManager().loadAllGroups();
                                CacheUtils.INSTANCE.setBoolean(LoginRegMainPage.this, Constants.User.IS_LOGIN, true);
                                CacheUtils.INSTANCE.setString(LoginRegMainPage.this, Constants.User.USER_JSON, resultData);

                                JSONObject checkObj = jsonObject.getJSONObject("data");
                                String logintoken = checkObj.getString("logintoken");
                                Log.e("logintoken",""+logintoken);
                                SaveMessage.SaveLogintoken(logintoken,LoginRegMainPage.this);

                                sendBroadcast(new Intent(EM_LOGIN));
                                ActivityCollector.INSTANCE.finishAll();
                            }


                        }

                        @Override
                        public void doError(String msg) {
                            hideLoadingView();
                        }

                        @Override
                        public void doResult() {

                        }
                    });
                }
                break;
            case R.id.loginByCode:
                MobclickAgent.onEvent(this,"pwd_quick");
                Intent intent1 = new Intent(this, QuickLoginActivityNew.class);
                if (!TextUtils.isEmpty(etCout.getText())) {
                    intent1.putExtra(MOBILE, etCout.getText().toString());
                }
                startActivity(intent1);
                break;
            case R.id.forget_pwd:
                MobclickAgent.onEvent(this,"pwd_forget");
                Intent intent2 = new Intent(this, ForgetStepNew1.class);
                if (!TextUtils.isEmpty(etCout.getText())) {
                    intent2.putExtra(MOBILE, etCout.getText().toString());
                }
                startActivity(intent2);
                break;
            case R.id.deleTxt:
                etCout.getText().clear();
                break;
            case R.id.qq:
                UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.QQ, authListener);
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, authListener);
                break;
            case R.id.wechat:
                UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.WEIXIN, authListener);
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener);
                break;
            case R.id.sina:
//                UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.SINA, authListener);
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.SINA, authListener);
        }
    }

    public void showEmailPop() {
        mInputManager.hideSoftInputFromWindow(etCout.getWindowToken(), 0);
        if (emailpopUpWindow.isShowing()) {
            emailpopUpWindow.dismiss();
        }
        emailpopUpWindow.showPop(this, tvLine, etCout.getText().toString(), new EmailpopUpWindow.EmailPopCallBack() {
            @Override
            public void popEmailItemClick(String cout) {
                etCout.setText(cout);
                etCout.setSelection(etCout.getText().length());
                emailpopUpWindow.dismissPop();
            }
        });
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            showLoadingView();
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(final SHARE_MEDIA platform, int action, final Map<String, String> data) {
            MobclickAgent.onEvent(LoginRegMainPage.this,"other_login");
            if (data != null && platform != null) {
                NetWork.INSTANCE.IsBind(LoginRegMainPage.this, platform, data, new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(final String resultData, Headers headers) {
                        JSONObject jsonObject = JSON.parseObject(resultData);
                        int code = jsonObject.getInteger("code");
                        if (code == 0) {//没有绑定，直接登录(以前是跳转绑定页面，暂时留着，后台说不定优惠改回去)
                            HttpParams params = new HttpParams();
                            params.put("openid", data.get("uid") + "");
                            params.put("nickname", data.get("name") + "");
                            params.put("head", data.get("iconurl") + "");
                            if (platform.equals(SHARE_MEDIA.QQ)) {
                                params.put("type", "2");
                            } else if (platform.equals(SHARE_MEDIA.WEIXIN)) {
                                params.put("type", "1");
                            } else if (platform.equals(SHARE_MEDIA.SINA)) {
                                params.put("type", "3");
                            }
                            params.put("device_token", MainApplication.Companion.getPushAgent().getRegistrationId());

                            NetWork.INSTANCE.loginByOpenId(LoginRegMainPage.this, params, new NetWork.TokenCallBack() {
                                @Override
                                public void doNext(String resultData, Headers headers) {
                                    loginSuccess(resultData);
                                }

                                @Override
                                public void doError(String msg) {
                                    setSnackBar("登录失败，请稍后再试", "", R.drawable.icon_tips_bad);
                                }

                                @Override
                                public void doResult() {

                                }
                            });
                        } else {
                            loginSuccess(resultData);
                        }
                    }

                    @Override
                    public void doError(String msg) {

                    }

                    @Override
                    public void doResult() {

                    }
                });
            }
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            hideLoadingView();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            hideLoadingView();
        }
    };

    private void loginSuccess(String resultData) {
        CacheUtils.INSTANCE.setString(LoginRegMainPage.this, Constants.User.USER_JSON, resultData);
        CacheUtils.INSTANCE.setBoolean(LoginRegMainPage.this, Constants.User.IS_LOGIN, true);
        UserInfoUtil.getUserInfoByJson(resultData, new UserInfoUtil.UserCallBack() {
            @Override
            public void doNext(UserInfo infoBean) {
                if (infoBean.getData().getLog_num() == 0) {
                    LoginSuccessToGo.goNext(LoginRegMainPage.this);
                }
            }
        });
        sendBroadcast(new Intent(EM_LOGIN));
        ActivityCollector.INSTANCE.finishAll();
    }


}
