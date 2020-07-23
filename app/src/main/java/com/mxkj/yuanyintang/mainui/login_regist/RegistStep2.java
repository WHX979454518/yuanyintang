package com.mxkj.yuanyintang.mainui.login_regist;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.app.CountDownTimerUtils;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.login_regist.ForgetStepNew1.byWhat;

public class RegistStep2 extends StandardUiActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.getCode)
    TextView getCode;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.ck_show)
    CheckBox ckShow;
    @BindView(R.id.et_pwd2)
    EditText etPwd2;
    @BindView(R.id.ck_show2)
    CheckBox ckShow2;
    @BindView(R.id.bt_sure)
    TextView btSure;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    private CountDownTimerUtils countDownTimerUtils;
    private String mobile = "";
    private String inviteCode;

    @Override
    public int setLayoutId() {
        return R.layout.activity_regist_step2;
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
    }

    @Override
    protected void initEvent() {
        ckShow.setOnCheckedChangeListener(this);
        ckShow2.setOnCheckedChangeListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            mobile = intent.getStringExtra("mobile");
            inviteCode = intent.getStringExtra("inCode");
            if (mobile != null) {
                tvPhone.setText(mobile);
                countDownTimerUtils = new CountDownTimerUtils(getCode, 60000, 1000);
                countDownTimerUtils.start();
            }
        }
    }

    @Override
    protected void initData() {

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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.ck_show:
                if (b) {
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.ck_show2:
                if (b) {
                    etPwd2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPwd2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
        }
    }

    @OnClick({R.id.finish, R.id.getCode, R.id.bt_sure})
    public void onClick(View view) {
        String code = etCode.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        String pwd2 = etPwd2.getText().toString().trim();
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.getCode:
                countDownTimerUtils.start();
                NetWork.INSTANCE.getCode(null,"reg", byWhat, mobile, this, new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {

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
                if (TextUtils.isEmpty(code)) {
                    setSnackBar("验证码不能为空", "", R.drawable.icon_fails);
                } else if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd2)) {
                    setSnackBar("请输入密码", "", R.drawable.icon_fails);
                } else if (!TextUtils.equals(pwd, pwd2)) {
                    setSnackBar("两次密码输入不一致", "", R.drawable.icon_fails);
                } else {
                    showLoadingView();
                    HttpParams params = new HttpParams();
                    params.put("mobile", mobile);
                    params.put("password", pwd);
                    params.put("code", code);
                    params.put("reg_at", "android");
                    if (!TextUtils.isEmpty(inviteCode)) {
                        params.put("incode", inviteCode);
                    }
                    NetWork.INSTANCE.Regist(this, params, new NetWork.TokenCallBack() {
                        @Override
                        public void doNext(String resultData, Headers headers) {
                            hideLoadingView();
                            CacheUtils.INSTANCE.setString(RegistStep2.this,Constants.User.USER_JSON, resultData);
                            CacheUtils.INSTANCE.setBoolean(RegistStep2.this,Constants.User.IS_LOGIN, true);
                            Intent intent = new Intent(RegistStep2.this, AddSelfInfo.class);
                            intent.putExtra("mobile", mobile);
                            intent.putExtra("pwd", etPwd.getText().toString());
                            startActivity(intent);
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
        }
    }
}
