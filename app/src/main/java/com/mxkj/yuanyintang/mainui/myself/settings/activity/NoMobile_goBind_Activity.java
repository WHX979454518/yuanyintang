package com.mxkj.yuanyintang.mainui.myself.settings.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.login_regist.EmailAutoCompleteTextView;
import com.mxkj.yuanyintang.mainui.myself.my_release.MyReleaseActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.app.CountDownTimerUtils;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.utils.constant.Constants.Other.EM_LOGIN;

public class NoMobile_goBind_Activity extends StandardUiActivity {

    @BindView(R.id.tv_type_name)
    TextView tvTypeName;
    @BindView(R.id.et_cout)
    EditText etCout;
    @BindView(R.id.et_cout_act)
    EmailAutoCompleteTextView etCoutAct;
    @BindView(R.id.tv_hint_code)
    TextView tvHintCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.getCode)
    TextView getCode;
    private int cgType;
    private CountDownTimerUtils countDownTimerUtils;


    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_no_mobile_go_bind_;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("账号绑定");
        cgType = getIntent().getIntExtra("cgType", 0);
        if (cgType == 1) {
            tvTypeName.setText("输入邮箱地址");
        }
        showRightButton();
        setRightButton("绑定", null, null);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.INSTANCE.removeActivity(this);
    }

    @OnClick({R.id.tv_type_name, R.id.leftButton, R.id.rightButton, R.id.et_cout, R.id.et_cout_act, R.id.tv_hint_code, R.id.getCode})
    public void onClick(View view) {
        HttpParams params;
        switch (view.getId()) {
            case R.id.leftButton:
                finish();
                break;
            case R.id.rightButton:
                if (TextUtils.isEmpty(etCout.getText()) || TextUtils.isEmpty(etCode.getText())) {
                    return;
                }
                params = new HttpParams();
                params.put("code", etCode.getText().toString());
                params.put("key", etCout.getText().toString());
                NetWork.INSTANCE.noMoile_GoBind(params, this, new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
                        setSnackBar("手机绑定成功!", "", R.drawable.icon_good);
                        Log.e("zzzzzz",""+resultData.toString());
                        sendBroadcast(new Intent(EM_LOGIN));
                        CacheUtils.INSTANCE.setString(NoMobile_goBind_Activity.this, Constants.User.USER_JSON, resultData);
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
                break;
            case R.id.tv_type_name:
                break;
            case R.id.et_cout:
                break;
            case R.id.et_cout_act:
                break;
            case R.id.tv_hint_code:
                break;
            case R.id.getCode:
                if (TextUtils.isEmpty(etCout.getText())) {
                    return;
                }
                params = new HttpParams();
                params.put("type", "addbind");
                params.put("key", etCout.getText().toString());
                NetWork.INSTANCE.getCodeNoLogin(params, this, new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
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
                break;
        }
    }
}
