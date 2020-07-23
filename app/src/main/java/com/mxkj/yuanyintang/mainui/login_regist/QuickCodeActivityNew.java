package com.mxkj.yuanyintang.mainui.login_regist;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.mainui.login_regist.widget.FailGetCodeDialog;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.app.CountDownTimerUtils;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.widget.IdentifyingCodeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.utils.constant.Constants.Other.EM_LOGIN;

public class QuickCodeActivityNew extends StandardUiActivity {
    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.fail_get_code)
    TextView fail_get_code;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.reget_code)
    TextView regetCode;
    @BindView(R.id.codeInput)
    IdentifyingCodeView codeInput;
    private String mobile = "";
    private CountDownTimerUtils countDownTimerUtils;
    private String TAG = "login_regist";
    private boolean must;
    private FailGetCodeDialog failGetCodeDialog;

    @Override
    public int setLayoutId() {
        return R.layout.activity_quick_code_new;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        hideTitle(true);
        ButterKnife.bind(this);
        initEvent();
        StatusBarUtil.baseTransparentStatusBar(this);
    }

    @Override
    protected void initEvent() {
        Intent intent = getIntent();
        if (intent != null) {
            mobile = intent.getStringExtra("mobile");
            must = intent.getBooleanExtra("must", false);
            if (mobile != null) {
                tvPhone.setText(mobile);
                countDownTimerUtils = new CountDownTimerUtils(regetCode, 60000, 1000, R.drawable.time_tiker_bck_ontick);
                countDownTimerUtils.start();
            }
        }

        codeInput.setInputCompleteListener(new IdentifyingCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                MobileBind();
            }

            @Override
            public void deleteContent() {
            }
        });
    }

    @Override
    protected void initData() {
    }

    private void MobileBind() {
        String code = codeInput.getTextContent().toString().trim();
        Log.e(TAG, code + "===" + mobile);
        if (codeInput.getTextContent().length() == 4) {
            showLoadingView();
            NetWork.INSTANCE.bindMobile(must, "", mobile, code, this, new NetWork.TokenCallBack() {
                @Override
                public void doNext(final String resultData, Headers headers) {
                    CacheUtils.INSTANCE.setBoolean(QuickCodeActivityNew.this, Constants.User.IS_LOGIN, true);
                    CacheUtils.INSTANCE.setString(QuickCodeActivityNew.this, Constants.User.USER_JSON, resultData);
                    hideLoadingView();
                    UserInfoUtil.getUserInfoByJson(resultData, new UserInfoUtil.UserCallBack() {
                        @Override
                        public void doNext(UserInfo infoBean) {
                            if (infoBean != null) {
                                int code1 = infoBean.getData().getCode();
                                if (code1 == 1) {
                                    startActivity(new Intent(QuickCodeActivityNew.this, HotUserRecommend.class));
                                }
                            }
                        }
                    });
                    CacheUtils.INSTANCE.setString(QuickCodeActivityNew.this, Constants.User.USER_JSON, resultData);
                    CacheUtils.INSTANCE.setBoolean(QuickCodeActivityNew.this, Constants.User.IS_LOGIN, true);
                    if (must) {
                        sendBroadcast(new Intent(EM_LOGIN));

                    }
                    ActivityCollector.INSTANCE.finishAll();
                }

                @Override
                public void doError(String msg) {
                    codeInput.clearAllText();
                    hideLoadingView();
                }

                @Override
                public void doResult() {

                }
            });
        }
    }

    @OnClick({R.id.finish, R.id.reget_code, R.id.fail_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.fail_get_code:
                failGetCodeDialog = new FailGetCodeDialog(0);
                failGetCodeDialog.show(getSupportFragmentManager(), "fail_get_code");
                break;
            case R.id.reget_code:
                showLoadingView();
                countDownTimerUtils.start();
                String codeType;
                if (must == true) {
                    codeType = "bind_mobile";
                } else {
                    codeType = "fast_log";
                }
                NetWork.INSTANCE.getCode("", codeType, "1", mobile, this, new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
                        hideLoadingView();
                        CacheUtils.INSTANCE.setBoolean(QuickCodeActivityNew.this, Constants.User.IS_LOGIN, true);
                        CacheUtils.INSTANCE.setString(QuickCodeActivityNew.this, Constants.User.USER_JSON, resultData);
                    }

                    @Override
                    public void doError(String msg) {
                    }

                    @Override
                    public void doResult() {

                    }


                });
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
