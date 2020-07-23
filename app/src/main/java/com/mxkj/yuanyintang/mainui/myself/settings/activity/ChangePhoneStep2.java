package com.mxkj.yuanyintang.mainui.myself.settings.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.myself.settings.SettingActivity;
import com.mxkj.yuanyintang.mainui.myself.settings.utils.GetSafeNum;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.login_regist.EmailAutoCompleteTextView;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.app.CountDownTimerUtils;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.utils.constant.Constants.Other.EM_LOGIN;

public class ChangePhoneStep2 extends StandardUiActivity {
    @BindView(R.id.headerViewGroup)
    FrameLayout headerViewGroup;
    @BindView(R.id.llmain)
    LinearLayout llmain;
    @BindView(R.id.ll_bind_success)
    LinearLayout ll_bind_success;
    @BindView(R.id.tv_binded)
    TextView tv_binded;
    @BindView(R.id.tv_type_name)
    TextView tvTypeName;
    @BindView(R.id.tv_hint_code)
    TextView tv_hint_code;
    @BindView(R.id.et_cout)
    EditText etCout;
    @BindView(R.id.et_cout_line)
    TextView etCout_line;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.getCode)
    TextView getCode;
    @BindView(R.id.et_cout_act)
    EmailAutoCompleteTextView etCoutAct;


    @BindView(R.id.show_tv)
    TextView show_tv;
    @BindView(R.id.show_image)
    ImageView show_image;
    @BindView(R.id.show_email_or_phone)
    TextView show_email_or_phone;
    @BindView(R.id.show_email_or_phone_image)
    ImageView show_email_or_phone_image;
    @BindView(R.id.success_back)
    ImageView success_back;




    private String auth;
    private CountDownTimerUtils countDownTimerUtils;
    private int cgType;

    @Override
    public int setLayoutId() {
        return R.layout.activity_change_phone_step2;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        auth = getIntent().getStringExtra("auth");
        cgType = getIntent().getIntExtra("cgType", -1);
        hideTitle(true);
        getNavigationBar().getRightButton().setTextColor(ContextCompat.getColor(this, R.color.base_red));
        if (cgType == 0) {
            show_tv.setText("绑定新手机号");
            show_image.setImageResource(R.drawable.img_tel_update);
            tvTypeName.setText("输入新的手机");
            tv_hint_code.setText("请填写手机收到的4位数字验证码");
        } else if (cgType == 1) {
            show_tv.setText("绑定新邮箱号");
            show_image.setImageResource(R.drawable.img_mail_update3x);
            tvTypeName.setText("输入新的邮箱");
            tv_hint_code.setText("请填写邮箱收到的4位数字验证码");
            etCout.setVisibility(View.GONE);
            etCout_line.setVisibility(View.GONE);
            etCoutAct.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
    }

    @OnClick({R.id.leftButton, R.id.tvSure, R.id.tvSure2, R.id.getCode, R.id.et_cout_act,R.id.success_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_cout_act:
                Log.e(TAG, "onViewClicked:===act ");
                etCoutAct.setFocusable(true);
                etCoutAct.setFocusableInTouchMode(true);
                etCoutAct.requestFocus();
                break;
            case R.id.leftButton:
                finish();
                break;
            case R.id.tvSure2:
                ActivityCollector.INSTANCE.finishAll();
                break;
            case R.id.success_back:
                ActivityCollector.INSTANCE.finishAll();
                break;
            case R.id.tvSure:
                HttpParams params = new HttpParams();
                params.put("auth", auth + "");
                if (cgType == 0) {
                    if (!TextUtils.isEmpty(etCout.getText())) {
                        params.put("thirdname", etCout.getText().toString() + "");
                    }
                } else if (cgType == 1) {
                    if (!TextUtils.isEmpty(etCoutAct.getText())) {
                        params.put("thirdname", etCoutAct.getText().toString() + "");
                    }
                }
                if (!TextUtils.isEmpty(etCode.getText())) {
                    params.put("thirdcode", etCode.getText().toString() + "");
                }
                if(null!=auth){
                    NetWork.INSTANCE.bindNewMobile(this, params, new NetWork.TokenCallBack() {
                        @Override
                        public void doNext(String resultData, Headers headers) {
//                        setSnackBar("更改成功", "", R.drawable.icon_success);
                            ll_bind_success.setVisibility(View.VISIBLE);
                            llmain.setVisibility(View.GONE);

                            if(cgType == 0){
                                show_email_or_phone.setText("手机绑定成功");
                                show_email_or_phone_image.setImageResource(R.drawable.img_tel_success3x);
                            }else if(cgType == 1){
                                show_email_or_phone.setText("邮箱绑定成功");
                                show_email_or_phone_image.setImageResource(R.drawable.img_mail_success3x);
                            }
                            tv_binded.setText(GetSafeNum.getSafeNumStr(SettingActivity.Companion.getUserInfoBean().getData().getEmail()));
                        }

                        @Override
                        public void doError(String msg) {

                        }

                        @Override
                        public void doResult() {

                        }
                    });
                }else {
                    bindemail();
                }

                break;
            case R.id.getCode:
                if (cgType == 0) {
                    if (!TextUtils.isEmpty(etCout.getText())) {
                        getCode(etCout.getText().toString());
                    }
                } else {
                    if (!TextUtils.isEmpty(etCoutAct.getText())) {
                        getCode(etCoutAct.getText().toString());
                    }
                }
                break;
        }
    }

    private void getCode(String mobile) {
        NetWork.INSTANCE.getCode("", "bind", ChangePhoneActivity.byWhat, mobile, this, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                setSnackBar("验证码发送成功", "", R.drawable.icon_success);
                countDownTimerUtils = new CountDownTimerUtils(getCode, 60000, 1000, "s后重新获取");
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

    private void bindemail(){
        if (TextUtils.isEmpty(etCoutAct.getText()) || TextUtils.isEmpty(etCode.getText())) {
            return;
        }
        HttpParams params = new HttpParams();
        params.put("code", etCode.getText().toString());
        params.put("key", etCoutAct.getText().toString());
        NetWork.INSTANCE.noMoile_GoBind(params, this, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                setSnackBar("邮箱绑定成功!", "", R.drawable.icon_good);
                Log.e("zzzzzz",""+resultData.toString());
                sendBroadcast(new Intent(EM_LOGIN));
                CacheUtils.INSTANCE.setString(ChangePhoneStep2.this, Constants.User.USER_JSON, resultData);
//                        Intent intent = new Intent(NoMobile_goBind_Activity.this,MyReleaseActivity.class);
//                        startActivity(intent);
                finish();
//                        ActivityCollector.INSTANCE.finishAll();
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
