package com.mxkj.yuanyintang.mainui.login_regist;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.app.CountDownTimerUtils;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.utils.constant.Constants.Other.EM_LOGIN;

public class QuickLoginActivityNew extends StandardUiActivity {
    private CountDownTimerUtils countDownTimerUtils;
    private UMShareAPI mShareAPI;
    public String inviteCode = "";
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
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.bt_sure)
    TextView btSure;
    @BindView(R.id.tv_code)
    TextView tv_code;
    @BindView(R.id.tv_incode)
    TextView tv_incode;
    @BindView(R.id.loginByPwd)
    TextView loginByPwd;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;

    @Override
    public int setLayoutId() {
        return R.layout.activity_quick_login_new;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        hideTitle(true);
        ButterKnife.bind(this);
        StatusBarUtil.baseTransparentStatusBar(this);
        mShareAPI = UMShareAPI.get(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityCollector.INSTANCE.addActivity(this);
        MobclickAgent.onEvent(this, "quick_login");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.INSTANCE.removeActivity(this);
    }

    @OnClick({R.id.finish, R.id.qq, R.id.wechat, R.id.sina, R.id.user_notice, R.id.tv_incode, R.id.tv_code, R.id.loginByPwd, R.id.copyright, R.id.bt_sure})
    public void onClick(View view) {
        Intent intent = new Intent(this, UserNoticeActivity.class);
        switch (view.getId()) {
            case R.id.loginByPwd:
                MobclickAgent.onEvent(this, "exit_login");
                finish();
                break;
            case R.id.tv_incode:
                initInviteCodeDialog();
                break;
            case R.id.finish:
                MobclickAgent.onEvent(this, "exit_login");
                finish();
                break;
            case R.id.tv_code:
                if (TextUtils.isEmpty(etCout.getText())) return;
                MobclickAgent.onEvent(QuickLoginActivityNew.this, "getQuickCode");

                countDownTimerUtils = new CountDownTimerUtils(tv_code, 60000, 1000);
                countDownTimerUtils.start();
                countDownTimerUtils.start();
                NetWork.INSTANCE.getCode("", "fast_log", "1", etCout.getText().toString(), this, new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
                        hideLoadingView();
                    }

                    @Override
                    public void doError(String msg) {
                        if (countDownTimerUtils != null) {
                            countDownTimerUtils.onFinish();
                        }                    }

                    @Override
                    public void doResult() {

                    }


                });
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
                mobileBind();
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
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.SINA, authListener);
        }
    }

    private void mobileBind() {
        String code = et_code.getText().toString().trim();
        if (TextUtils.isEmpty(etCout.getText())) return;
        if (!TextUtils.isEmpty(code)) {
            NetWork.INSTANCE.bindMobile(false, inviteCode,etCout.getText().toString(), code, this, new NetWork.TokenCallBack() {
                @Override
                public void doNext(final String resultData, Headers headers) {
                    MobclickAgent.onEvent(QuickLoginActivityNew.this,"quick_log_success");
                    CacheUtils.INSTANCE.setBoolean(QuickLoginActivityNew.this, Constants.User.IS_LOGIN, true);
                    CacheUtils.INSTANCE.setString(QuickLoginActivityNew.this, Constants.User.USER_JSON, resultData);
                    hideLoadingView();
                    UserInfoUtil.getUserInfoByJson(resultData, new UserInfoUtil.UserCallBack() {
                        @Override
                        public void doNext(UserInfo infoBean) {
                            if (infoBean.getData().getLog_num() == 0) {
                                LoginSuccessToGo.goNext(QuickLoginActivityNew.this);
                            }
                        }
                    });
                    CacheUtils.INSTANCE.setString(QuickLoginActivityNew.this, Constants.User.USER_JSON, resultData);
                    CacheUtils.INSTANCE.setBoolean(QuickLoginActivityNew.this, Constants.User.IS_LOGIN, true);
                    sendBroadcast(new Intent(EM_LOGIN));
                    ActivityCollector.INSTANCE.finishAll();
                }

                @Override
                public void doError(String msg) {
                    if (countDownTimerUtils != null) {
                        countDownTimerUtils.onFinish();
                    }
                }

                @Override
                public void doResult() {

                }
            });
        }
    }

    public void initInviteCodeDialog() {
        View view = View.inflate(this, R.layout.dialog_invite_code, null);
        diaLogBuilder = new DiaLogBuilder(this)
                .setContentView(view)
                .setFullScreen()
                .setGrvier(Gravity.CENTER)
                .show();
        TextView tv_know = view.findViewById(R.id.tv_know);
        final TextView tv_error = view.findViewById(R.id.tv_error);
        final EditText et_inviteCode = view.findViewById(R.id.et_inviteCode);
        et_inviteCode.setTransformationMethod(new UpperCaseTransform());
        ImageView diss_dialog = view.findViewById(R.id.img_close_dialog);
        diss_dialog.setVisibility(View.VISIBLE);
        tv_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteCode = et_inviteCode.getText().toString();
                if (TextUtils.isEmpty(inviteCode)) {
                    setSnackBar("请输入邀请码", "", R.drawable.icon_fails);
                    return;
                }
                InputMethodManager mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputManager.hideSoftInputFromWindow(et_inviteCode.getWindowToken(), 0);
                NetWork.INSTANCE.authIncode(QuickLoginActivityNew.this, et_inviteCode.getText().toString(), new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
                        MobclickAgent.onEvent(QuickLoginActivityNew.this,"invite_code");

                        inviteCode = et_inviteCode.getText().toString();
                        tv_incode.setText("邀请码：" + inviteCode);
                        diaLogBuilder.setDismiss();
                    }

                    @Override
                    public void doError(String msg) {
                        tv_error.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void doResult() {

                    }
                });
            }
        });
        diss_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diaLogBuilder.setDismiss();
            }
        });
    }

    public class UpperCaseTransform extends ReplacementTransformationMethod {
        @Override
        protected char[] getOriginal() {
            char[] aa = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            return aa;
        }

        @Override
        protected char[] getReplacement() {
            char[] cc = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            return cc;
        }
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
            if (data != null && platform != null) {
                MobclickAgent.onEvent(QuickLoginActivityNew.this,"other_login");

                NetWork.INSTANCE.IsBind(QuickLoginActivityNew.this, platform, data, new NetWork.TokenCallBack() {
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
                            if (!TextUtils.isEmpty(inviteCode)){
                                params.put("incode", inviteCode);
                            }
                            params.put("device_token", MainApplication.Companion.getPushAgent().getRegistrationId());

                            NetWork.INSTANCE.loginByOpenId(QuickLoginActivityNew.this, params, new NetWork.TokenCallBack() {
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
        CacheUtils.INSTANCE.setString(QuickLoginActivityNew.this, Constants.User.USER_JSON, resultData);
        CacheUtils.INSTANCE.setBoolean(QuickLoginActivityNew.this, Constants.User.IS_LOGIN, true);
        UserInfoUtil.getUserInfoByJson(resultData, new UserInfoUtil.UserCallBack() {
            @Override
            public void doNext(UserInfo infoBean) {
                if (infoBean != null) {
                    if (infoBean.getData().getLog_num() == 0) {
                        LoginSuccessToGo.goNext(QuickLoginActivityNew.this);
                    }
                    int code1 = infoBean.getData().getCode();
                    if (code1 == 1) {
                        startActivity(new Intent(QuickLoginActivityNew.this, HotUserRecommend.class));
                    }
                }
            }
        });
        sendBroadcast(new Intent(EM_LOGIN));
        ActivityCollector.INSTANCE.finishAll();
    }

}
