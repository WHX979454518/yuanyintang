package com.mxkj.yuanyintang.mainui.myself.settings.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jakewharton.rxbinding2.view.RxView;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.myself.settings.SettingActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.app.CountDownTimerUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;
public class ChangePhoneActivity extends StandardUiActivity {
    @BindView(R.id.headerViewGroup)
    FrameLayout headerViewGroup;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_reget)
    TextView tvReget;
    @BindView(R.id.ByEmail)
    TextView ByEmail;
    @BindView(R.id.tv_coutType)
    TextView tvCoutType;
    @BindView(R.id.tv_error)
    TextView tv_error;
    @BindView(R.id.ll_byEmail)
    LinearLayout ll_byEmail;
    @BindView(R.id.tvSure)
    TextView tvSure;
    @BindView(R.id.iv_finish)
    ImageView iv_finish;

    @BindView(R.id.show_tv)
    TextView show_tv;
    @BindView(R.id.show_image)
    ImageView show_image;
    @BindView(R.id.show_phone_rl)
    RelativeLayout show_phone_rl;
    @BindView(R.id.show_email_rl)
    RelativeLayout show_email_rl;
    @BindView(R.id.number_or_email_tv)
    TextView number_or_email_tv;




    public static String byWhat = "1";//通过手机还是邮箱（后台规定1为手机2为邮箱）
    public static String bind_type = "bind_mobile";

    private CountDownTimerUtils countDownTimerUtils;
    private String mobile;
    private int cgType = -1;
    private int cgBy = -1;

    @Override
    public int setLayoutId() {
        return R.layout.activity_change_phone;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        hideTitle(true);
        ButterKnife.bind(this);
        getNavigationBar().getRightButton().setTextColor(Color.parseColor("#ff6666"));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        RxView.clicks(iv_finish)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        finish();
                    }
                });

        Intent intent = getIntent();
        cgType = intent.getIntExtra("cgType", -1);
        cgBy = intent.getIntExtra("cgBy", -1);
//        if ((cgType == 0 && cgBy == 1) || (cgType == 1 && cgBy == 0)) {
        if ((cgType == 0 && cgBy == 1)) {
            ll_byEmail.setVisibility(View.GONE);
        }

        if (cgType==0){
            bind_type = "bind_mobile";
        }else if (cgType==1){
            bind_type = "bind_email";
        }

        if (cgBy == 0) {
            show_tv.setText("修改手机号");
            show_image.setImageResource(R.drawable.img_tel_update);
            tvCoutType.setText("更换后请用新手机号登录");
        } else if (cgBy == 1) {
            show_tv.setText("修改邮箱");
            show_image.setImageResource(R.drawable.img_mail_update3x);
//            show_phone_rl.setVisibility(View.GONE);
//            show_email_rl.setVisibility(View.VISIBLE);
            number_or_email_tv.setText("你当前的邮箱号为");
            tvCoutType.setText("更换后请用新邮箱号登录");
        }

        if (SettingActivity.Companion.getUserInfoBean() != null && SettingActivity.Companion.getUserInfoBean().getData() != null) {
            //更改邮箱账号
            if (cgType == 1) {
                if (cgBy == 0) {//通过手机更改邮箱
                    mobile = SettingActivity.Companion.getUserInfoBean().getData().getMobile();
                    tvPhone.setText(mobile);
                } else if (cgBy == 1) {
                    mobile = SettingActivity.Companion.getUserInfoBean().getData().getEmail();
                    tvPhone.setText(mobile);
                    if (!TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getMobile())) {
                        tv_error.setText("收不到验证码，");
                        ByEmail.setText("手机验证");
                    }

                }
                //更改手机
            } else if (cgType == 0) {
                if (cgBy == 0) {//通过手机
                    mobile = SettingActivity.Companion.getUserInfoBean().getData().getMobile();
                    tvPhone.setText(mobile);
                    if (!TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getEmail())) {
                        tv_error.setText("收不到验证码，");
                        ByEmail.setText("邮箱验证");
                    }
                } else if (cgBy == 1) {
                    mobile = SettingActivity.Companion.getUserInfoBean().getData().getEmail();
                    tvPhone.setText(mobile);
                }
            }
        }
    }

    private void getCode(String mobile) {
        if (cgBy == 0) {
            byWhat = "1";
        } else {
            byWhat = "2";
        }
        NetWork.INSTANCE.getCode("", "bind_email", byWhat, null, this, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                setSnackBar("验证码发送成功", "", R.drawable.icon_success);
                if (countDownTimerUtils != null) {
                    countDownTimerUtils.onFinish();
                }
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

    @OnClick({R.id.leftButton, R.id.tvSure, R.id.ByEmail, R.id.tv_reget})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leftButton:
                finish();
                break;
            case R.id.tv_reget:
                if (!TextUtils.isEmpty(tvPhone.getText())) {
                    getCode(tvPhone.getText().toString());
                }
                break;
            case R.id.tvSure:
                if (!TextUtils.isEmpty(etCode.getText().toString())) {
                    HttpParams params = new HttpParams();
                    params.put("findcode", etCode.getText().toString());
                    params.put("choose", byWhat);
                    Log.e(TAG, "onViewClicked: " + params);
                    NetWork.INSTANCE.bindPhoneAuth(this, params, new NetWork.TokenCallBack() {

                        @Override
                        public void doNext(String resultData, Headers headers) {
                            JSONObject jsonObject = JSON.parseObject(resultData);
                            String data = jsonObject.getString("data");
                            Intent intent = new Intent(ChangePhoneActivity.this, ChangePhoneStep2.class);
                            intent.putExtra("auth", data);
                            intent.putExtra("cgType", cgType);
                            intent.putExtra("cgBy", cgBy);
                            startActivity(intent);
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
            case R.id.ByEmail:
                if (cgBy == 0) {//通过手机更改
                    cgBy = 1;
                    byWhat = "2";

                    //这里做判断，邮箱为空的时候显示那种布局，邮箱不为空的时候显示那种布局
//                    1.不为空的情况
                    number_or_email_tv.setText("你当前的邮箱号为");
                    tvCoutType.setText("更换后请用新邮箱号登录");
////                    2.为空的情况
//                        show_tv.setText("绑定新邮箱");
//                        show_image.setImageResource(R.drawable.img_mail_update3x);
//                        show_phone_rl.setVisibility(View.GONE);
//                        show_email_rl.setVisibility(View.VISIBLE);

                    setTitleText("更改账号绑定");
                    tvPhone.setText(SettingActivity.Companion.getUserInfoBean().getData().getEmail());
                    ByEmail.setText("手机验证");
                } else if (cgBy == 1) {
                    cgBy = 0;
                    byWhat = "1";

                    show_image.setImageResource(R.drawable.img_tel_update);
                    show_phone_rl.setVisibility(View.VISIBLE);
                    show_email_rl.setVisibility(View.GONE);


                    number_or_email_tv.setText("你当前的手机号为");
                    tvCoutType.setText("更换后请用新手机号登录");
                    setTitleText("更改账号绑定");
                    ByEmail.setText("邮箱验证");
                    tvPhone.setText(SettingActivity.Companion.getUserInfoBean().getData().getMobile());
                }
                if (!TextUtils.isEmpty(tvPhone.getText())) {
                    getCode(tvPhone.getText().toString());
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
