package com.mxkj.yuanyintang.mainui.login_regist;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.app.CountDownTimerUtils;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.utils.constant.Constants.Other.EM_LOGIN;

public class BindThird extends StandardUiActivity {
    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.icon)
    CircleImageView icon;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.getCode)
    TextView getCode;
    @BindView(R.id.bt_sure)
    TextView btSure;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.user_notice)
    TextView userNotice;
    @BindView(R.id.copyright)
    TextView copyright;
    @BindView(R.id.ll_notice)
    LinearLayout llNotice;
    private CountDownTimerUtils countDownTimerUtils;
    private String mobile;
    private Intent intent;

    @Override
    public int setLayoutId() {
        return R.layout.activity_bind_third;
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
        intent = getIntent();
        nickname.setText(intent.getStringExtra("nickname"));
        Log.e(TAG, "initEvent: " + intent.getStringExtra("iconurl"));
        Glide.with(this).load(intent.getStringExtra("iconurl")).asBitmap().error(R.drawable.nothing).into(icon);

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.finish, R.id.getCode, R.id.bt_sure, R.id.user_notice, R.id.copyright})
    public void onViewClicked(View view) {
        Intent intentNotice = new Intent(this, UserNoticeActivity.class);

        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.bt_sure:
                if (TextUtils.isEmpty(etPhone.getText())) {
                    setSnackBar("手机号不能为空", "", R.drawable.icon_fails);
                } else if (TextUtils.isEmpty(etCode.getText())) {
                    setSnackBar("验证码不能为空", "", R.drawable.icon_fails);
                } else {
                    HttpParams params = new HttpParams();
                    params.put("openid", intent.getStringExtra("uid"));
                    params.put("nickname", intent.getStringExtra("nickname"));
                    params.put("head", intent.getStringExtra("iconurl"));
                    params.put("type", intent.getStringExtra("type"));
                    params.put("thirdcode", etCode.getText().toString().trim());
                    params.put("sex", intent.getStringExtra("sex"));
                    params.put("mobile", etPhone.getText().toString().trim());
                    Log.e(TAG, "绑定参数: ======" + params);
                    NetWork.INSTANCE.bindThird(BindThird.this, params, new NetWork.TokenCallBack() {
                        @Override
                        public void doNext(final String resultData, Headers headers) {
                            CacheUtils.INSTANCE.setString(BindThird.this, Constants.User.USER_JSON, resultData);
                            showLoadingView();
                            CacheUtils.INSTANCE.setString(BindThird.this, Constants.User.USER_JSON, resultData);
                            CacheUtils.INSTANCE.setBoolean(BindThird.this, Constants.User.IS_LOGIN, true);
//                            startService(new Intent(BindThird.this, BackgroundService.class));
                            sendBroadcast(new Intent(EM_LOGIN));

                            setSnackBar("绑定成功", "", R.drawable.icon_success);
                            hideLoadingView();
                            UserInfoUtil.getUserInfoByJson(resultData, new UserInfoUtil.UserCallBack() {
                                @Override
                                public void doNext(UserInfo infoBean) {
                                    if (infoBean != null) {
                                        int code1 = infoBean.getData().getCode();
                                        if (code1 == 1) {
                                            startActivity(new Intent(BindThird.this, HotUserRecommend.class));
                                        }
                                    }
                                }
                            });

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
            case R.id.getCode:
                if (!TextUtils.isEmpty(etPhone.getText())) {
                    countDownTimerUtils = new CountDownTimerUtils(getCode, 60000, 1000);
                    countDownTimerUtils.start();
                    NetWork.INSTANCE.getCode(intent.getStringExtra("type"),"third_log", null, etPhone.getText().toString(), this, new NetWork.TokenCallBack() {

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
                }

                break;
            case R.id.user_notice:
                intentNotice.putExtra("code", "agreement");
                startActivity(intentNotice);
                break;
            case R.id.copyright:
                intentNotice.putExtra("code", "copyrightpolicy");
                startActivity(intentNotice);
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
