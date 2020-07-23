package com.mxkj.yuanyintang.mainui.login_regist;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.login_regist.widget.EmailpopUpWindow;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.app.CountDownTimerUtils;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage.MOBILE;

public class ForgetStepNew1 extends StandardUiActivity {

    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.getCode)
    TextView getCode;
    @BindView(R.id.bt_sure)
    TextView btSure;
    @BindView(R.id.use_email)
    TextView useEmail;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText et_code;
    private boolean isPhone = true;
    public static String byWhat = "1";
    private InputMethodManager mInputManager;
    private EmailpopUpWindow emailpopUpWindow;
    private CountDownTimerUtils countDownTimerUtils;

    @Override
    public int setLayoutId() {
        return R.layout.activity_forget_step_new1;
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
        String stringExtra = getIntent().getStringExtra(MOBILE);
        if (!TextUtils.isEmpty(stringExtra)) {
            etPhone.setText(stringExtra);
            etPhone.setCursorVisible(false);
        }
    }

    @Override
    protected void initEvent() {
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (emailpopUpWindow == null) {
            emailpopUpWindow = new EmailpopUpWindow();
        }
        etPhone.addTextChangedListener(new TextWatcher() {
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

    @OnClick({R.id.finish, R.id.bt_sure,R.id.getCode, R.id.use_email})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish:
                MobclickAgent.onEvent(this,"forget_back");
                finish();
                break;
            case R.id.getCode:
                if (!TextUtils.isEmpty(etPhone.getText())) {
                    MobclickAgent.onEvent(this,"forget_get_code");
                    NetWork.INSTANCE.getCode("", "find_pwd", byWhat, etPhone.getText().toString(), this, new NetWork.TokenCallBack() {
                        @Override
                        public void doNext(String resultData, Headers headers) {

                        }

                        @Override
                        public void doError(String msg) {
                            if (countDownTimerUtils != null) {
                                countDownTimerUtils.onFinish();
                            }                        }

                        @Override
                        public void doResult() {

                        }
                    });
                    countDownTimerUtils = new CountDownTimerUtils(getCode, 60000, 1000);
                    countDownTimerUtils.start();
                } else {
                    setSnackBar("账号不能为空", "", R.drawable.icon_fails);
                }
                break;
            case R.id.bt_sure:
                if (TextUtils.isEmpty(etPhone.getText())) {
                    setSnackBar("账号不能为空", "", R.drawable.icon_fails);
                } else if (TextUtils.isEmpty(et_code.getText())) {
                    setSnackBar("验证码不能为空", "", R.drawable.icon_fails);
                } else {
                    MobclickAgent.onEvent(this,"forget_next");
                    Intent intent = new Intent(ForgetStepNew1.this, ForgetStepNew2.class);
                    intent.putExtra("mobile", etPhone.getText().toString());
                    intent.putExtra("code", et_code.getText().toString());
                    startActivity(intent);
                }
                break;
            case R.id.use_email:
                MobclickAgent.onEvent(this,"forget_email");
                if (countDownTimerUtils!=null){
                countDownTimerUtils.onFinish();}
                countDownTimerUtils = new CountDownTimerUtils(getCode, 60000, 1000);
                if (isPhone == true) {
                    isPhone = false;
                    byWhat = "2";
                    btSure.setText("验证邮箱");
                    useEmail.setText("使用手机找回密码");
                    etPhone.setHint("请输入邮箱地址");
                    etPhone.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    etPhone.getText().clear();
                } else {
                    etPhone.getText().clear();
                    byWhat = "1";
                    etPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
                    isPhone = true;
                    btSure.setText("验证手机");
                    useEmail.setText("使用邮箱找回密码");
                    etPhone.setHint("请输入手机号码");
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityCollector.INSTANCE.addActivity(this);
        MobclickAgent.onEvent(this,"forget_count");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.INSTANCE.removeActivity(this);
    }

    public void showEmailPop() {
        mInputManager.hideSoftInputFromWindow(etPhone.getWindowToken(), 0);
        if (emailpopUpWindow.isShowing()) {
            emailpopUpWindow.dismiss();
        }
        emailpopUpWindow.showPop(this, etPhone, etPhone.getText().toString(), new EmailpopUpWindow.EmailPopCallBack() {
            @Override
            public void popEmailItemClick(String cout) {
                etPhone.setText(cout);
                etPhone.setSelection(etPhone.getText().length());
                emailpopUpWindow.dismissPop();
            }
        });
    }
}
