package com.mxkj.yuanyintang.mainui.login_regist;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.login_regist.widget.FailGetCodeDialog;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.app.CountDownTimerUtils;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.string.IsMobilieNum;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.utils.constant.Constants.Other.EM_LOGIN;

public class NewDeviceAuthActivity extends StandardUiActivity {

    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_code_introuble)
    TextView tvCodeIntrouble;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_getcode)
    TextView tvGetcode;
    @BindView(R.id.bt_sure)
    TextView btSure;
    @BindView(R.id.tv_use_email)
    TextView tvUseEmail;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;


    private CountDownTimerUtils countDownTimerUtils;
    private String email, monile, key;
    private String sendtype = "mobile";
    private FailGetCodeDialog failGetCodeDialog;
    private String etInput;


    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_new_device_auth;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        countDownTimerUtils = new CountDownTimerUtils(tvGetcode, 60000, 1000);
        setTitleText("新设备登录验证");
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        monile = intent.getStringExtra("mobile");
        email = intent.getStringExtra("email");
        etInput = intent.getStringExtra("etInput");

        /*没有邮箱 隐藏底部使用邮箱  显示手机号*/
        if (TextUtils.isEmpty(email) && !TextUtils.isEmpty(monile)) {
            tvUseEmail.setVisibility(View.GONE);
            tvMobile.setText(StringUtils.isEmpty(monile));
            sendtype = "mobile";
        } else if (TextUtils.isEmpty(monile) && !TextUtils.isEmpty(email)) {
            tvUseEmail.setVisibility(View.GONE);
            tvMobile.setText(StringUtils.isEmpty(email));
            sendtype = "email";
        }

//        补充
        if (!TextUtils.isEmpty(etInput)) {
            tvMobile.setText(StringUtils.isEmpty(etInput));
            if (IsMobilieNum.isEmail(etInput)) {
                sendtype = "email";
                tvUseEmail.setText("使用邮箱验证");
            } else if (IsMobilieNum.isMobileNumber(etInput)) {
                sendtype = "mobile";
                tvUseEmail.setText("使用手机验证");
            }
        }


    }

    @OnClick({R.id.tv_code_introuble, R.id.tv_getcode, R.id.bt_sure, R.id.tv_use_email})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_code_introuble:
                failGetCodeDialog = new FailGetCodeDialog(sendtype.equals("mobile") ? 0 : 1);
                failGetCodeDialog.show(getSupportFragmentManager(), "fail_get_code");
                break;
            case R.id.tv_getcode:
                HttpParams params = new HttpParams();
                params.put("type", "checklogin");
                params.put("key", StringUtils.isEmpty(key));
                params.put("sendtype", sendtype);
                NetWork.INSTANCE.getCodeNoLogin(params, this, new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
                        countDownTimerUtils.start();

                    }

                    @Override
                    public void doError(String msg) {
                    }

                    @Override
                    public void doResult() {

                    }
                });
                break;
            case R.id.bt_sure:
                if (TextUtils.isEmpty(etCode.getText())) {
                    setSnackBar("请输入验证码", "", R.drawable.icon_tips_bad);
                } else {
                    HttpParams paramsAuth = new HttpParams();
                    paramsAuth.put("checklogin_code", etCode.getText().toString());
                    paramsAuth.put("key", StringUtils.isEmpty(key));
                    paramsAuth.put("sendtype", sendtype);
                    NetWork.INSTANCE.checklogin(this, paramsAuth, new NetWork.TokenCallBack() {
                        @Override
                        public void doNext(String resultData, Headers headers) {
                            hideLoadingView();
                            EMClient.getInstance().chatManager().loadAllConversations();
                            EMClient.getInstance().groupManager().loadAllGroups();
                            Log.e(TAG, "密码登录 " + resultData);
                            CacheUtils.INSTANCE.setBoolean(NewDeviceAuthActivity.this, Constants.User.IS_LOGIN, true);
                            CacheUtils.INSTANCE.setString(NewDeviceAuthActivity.this, Constants.User.USER_JSON, resultData);
                            sendBroadcast(new Intent(EM_LOGIN));
                            ActivityCollector.INSTANCE.finishAll();
                        }

                        @Override
                        public void doError(String msg) {

                        }

                        @Override
                        public void doResult() {

                        }
                    });
                }
                break;
            case R.id.tv_use_email:
                if (sendtype.equals("mobile")) {
                    sendtype = "email";
                    tvMobile.setText(StringUtils.isEmpty(email));

                    tvUseEmail.setText("使用手机验证");

                } else {
                    sendtype = "mobile";
                    tvUseEmail.setText("使用邮箱验证");
                    tvMobile.setText(StringUtils.isEmpty(monile));
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityCollector.INSTANCE.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.INSTANCE.removeActivity(this);
    }
}
