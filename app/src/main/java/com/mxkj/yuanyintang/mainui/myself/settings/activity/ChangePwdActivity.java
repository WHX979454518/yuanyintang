package com.mxkj.yuanyintang.mainui.myself.settings.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.mainui.myself.settings.SettingActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.myself.settings.utils.GetSafeNum;
import com.mxkj.yuanyintang.utils.app.CountDownTimerUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class ChangePwdActivity extends StandardUiActivity {
    @BindView(R.id.headerViewGroup)
    FrameLayout headerViewGroup;
    @BindView(R.id.tv_coutType)
    TextView tvCoutType;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.t)
    TextView t;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_reget)
    TextView tvReget;
    @BindView(R.id.t2)
    TextView t2;
    @BindView(R.id.et_pwd1)
    EditText etPwd1;
    @BindView(R.id.t3)
    TextView t3;
    @BindView(R.id.et_pwd2)
    EditText etPwd2;
    @BindView(R.id.ByEmail)
    TextView ByEmail;
    @BindView(R.id.ll_byEmail)
    LinearLayout llByEmail;
    private CountDownTimerUtils countDownTimerUtils;
    private String mobile;
    private String pwd1, pwd2;
    private String byWhat = "1";

    @Override
    public int setLayoutId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("修改密码");
        setRightButtonText("保存");
        getNavigationBar().getRightButton().setTextColor(Color.parseColor("#ff6699"));
    }

    @Override
    protected void initData() {
        if (SettingActivity.Companion.getUserInfoBean() != null && SettingActivity.Companion.getUserInfoBean().getData() != null) {
            if (TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getMobile())) {
                llByEmail.setVisibility(View.GONE);
                tvCoutType.setText("邮箱号");
                if (!TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getEmail())) {
                    mobile = SettingActivity.Companion.getUserInfoBean().getData().getEmail();
                    tvPhone.setText(GetSafeNum.getSafeNumStr(mobile));
                }
            }
        }
    }

    @Override
    protected void initEvent() {
        if (SettingActivity.Companion.getUserInfoBean() != null && SettingActivity.Companion.getUserInfoBean().getData() != null) {
            if (!TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getMobile())) {
                byWhat = "1";
                mobile = SettingActivity.Companion.getUserInfoBean().getData().getMobile();
                if (mobile != null) {
                    tvPhone.setText(GetSafeNum.getSafeNumStr(mobile));
                }
            } else {
                mobile = SettingActivity.Companion.getUserInfoBean().getData().getEmail();
                byWhat = "2";
                if (mobile != null) {
                    tvPhone.setText(GetSafeNum.getSafeNumStr(mobile));
                }
            }
        }
    }

    @OnClick({R.id.leftButton, R.id.rightButton, R.id.tv_reget, R.id.ByEmail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leftButton:
                finish();
                break;
            case R.id.rightButton:
                if (!TextUtils.isEmpty(etPwd1.getText()) && !TextUtils.isEmpty(etPwd2.getText())) {
                    pwd1 = etPwd1.getText().toString().trim();
                    pwd2 = etPwd2.getText().toString().trim();
                    if (pwd1.equals(pwd2)) {
                        if (!TextUtils.isEmpty(etCode.getText())) {
                            changePwd();
                        } else {
                            setSnackBar("请输入验证码！", "", R.drawable.icon_fails);
                        }
                    } else {
                        setSnackBar("两次密码不一致！", "", R.drawable.icon_fails);
                    }
                } else {
                    setSnackBar("请输入密码！", "", R.drawable.icon_fails);
                }
                break;
            case R.id.tv_reget:
                getCode(mobile);
                break;
            case R.id.ByEmail:
                llByEmail.setVisibility(View.GONE);
                tvCoutType.setText("邮箱号");
                byWhat = "2";
                if (SettingActivity.Companion.getUserInfoBean() != null && SettingActivity.Companion.getUserInfoBean().getData() != null) {
                    if (!TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getEmail())) {
                        mobile = SettingActivity.Companion.getUserInfoBean().getData().getEmail();
                        if (mobile != null) {
                            tvPhone.setText(GetSafeNum.getSafeNumStr(mobile));
                        }
                    }
                }

                break;
        }
    }

    private void changePwd() {
        HttpParams params = new HttpParams();
//        params.put("name", mobile);
        params.put("code", etCode.getText().toString());
        params.put("password", pwd1);
        params.put("repassword", pwd2);
        params.put("choose", byWhat);
        NetWork.INSTANCE.changePwd(this, params, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                setSnackBar("密码修改成功", "", R.drawable.icon_success);
                startActivity(new Intent(ChangePwdActivity.this, LoginRegMainPage.class));
                finish();
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    private void getCode(String mobile) {
        NetWork.INSTANCE.getCode("","update_pwd", byWhat, "", this, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                countDownTimerUtils = new CountDownTimerUtils(tvReget, 60000, 1000, "s后重新获取");
                countDownTimerUtils.start();
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
