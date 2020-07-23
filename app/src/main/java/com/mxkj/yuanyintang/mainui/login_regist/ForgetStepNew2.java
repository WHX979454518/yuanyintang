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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.app.CountDownTimerUtils;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.login_regist.ForgetStepNew1.byWhat;


public class ForgetStepNew2 extends StandardUiActivity {
    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.bt_sure)
    TextView btSure;
    private String mobile = "";
    private String code = "";

    @Override
    public int setLayoutId() {
        return R.layout.activity_forget_step_new2;
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
        Intent intent = getIntent();
        if (intent != null) {
            mobile = intent.getStringExtra("mobile");
            code = intent.getStringExtra("code");
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.finish, R.id.bt_sure})
    public void onViewClicked(View view) {
        String pwd = etPwd.getText().toString().trim();
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.bt_sure:
                if (TextUtils.isEmpty(code)) {
                    setSnackBar("验证码不能为空", "", R.drawable.icon_fails);
                } else if (TextUtils.isEmpty(pwd)) {
                    setSnackBar("请输入密码", "", R.drawable.icon_fails);
                } else {
                    showLoadingView();
                    final HttpParams params = new HttpParams();
                    params.put("loginname", mobile);
                    params.put("findcode", code);
                    NetWork.INSTANCE.authChgPwd(ForgetStepNew2.this, params, new NetWork.TokenCallBack() {
                        @Override
                        public void doNext(String resultData, Headers headers) {
                            hideLoadingView();
                            JSONObject jsonObject = JSON.parseObject(resultData);
                            String data = jsonObject.getString("data");
                            if (data != null) {
                                HttpParams params2 = new HttpParams();
                                params2.put("auth", data);
                                params2.put("password", etPwd.getText().toString());
                                params2.put("repassword", etPwd.getText().toString());
                                NetWork.INSTANCE.changePwdAuth(ForgetStepNew2.this, params2, new NetWork.TokenCallBack() {

                                    @Override
                                    public void doNext(String resultData, Headers headers) {
                                        setSnackBar("密码重置成功", "", R.drawable.icon_success);
                                        startActivity(new Intent(ForgetStepNew2.this, LoginRegMainPage.class));
                                        ActivityCollector.INSTANCE.finishAll();
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
