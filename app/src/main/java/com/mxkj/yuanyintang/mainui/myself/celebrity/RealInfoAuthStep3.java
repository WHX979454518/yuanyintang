package com.mxkj.yuanyintang.mainui.myself.celebrity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.login_regist.UserNoticeActivity;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class RealInfoAuthStep3 extends StandardUiActivity {
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.step_text)
    TextView stepText;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.bt_next)
    TextView btNext;
    @BindView(R.id.navigationBarRl)
    RelativeLayout navigationBar;
    @BindView(R.id.et_realName)
    EditText etRealName;
    @BindView(R.id.et_IdCard)
    EditText etIdCard;
    @BindView(R.id.etAdress)
    EditText etAdress;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.ll_authStatus)
    LinearLayout llAuthStatus;
    @BindView(R.id.tv_real_notice)
    TextView tvRealNotice;
    @BindView(R.id.img_status)
    ImageView imgStatus;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.toNotice)
    TextView toNotice;
    private int authType;
    private boolean finishAll;

    @Override
    public int setLayoutId() {
        return R.layout.activity_real_info_auth_step3;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        hideTitle(true);
        authType = getIntent().getIntExtra("authType", 0);
        if (authType == 0) {
            etIdCard.setMaxEms(18);
        } else {
            etIdCard.setMaxEms(100);
        }
        btNext.setVisibility(View.VISIBLE);
        title.setText("填写信息");
        btNext.setText("完成");
        stepText.setText("(3/3)");
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {
    }

    @OnClick({R.id.finish, R.id.bt_next, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish:
                if (finishAll==true){
                    ActivityCollector.INSTANCE.finishAll();
                }else {
                    finish();
                }
                break;
            case R.id.bt_next:
                Auth();
                break;
            case R.id.tv_sure:
                ActivityCollector.INSTANCE.finishAll();
                break;
        }
    }

    private void Auth() {
        String hash_card1, hash_card2;
        hash_card1 = getIntent().getStringExtra("hash1");
        hash_card2 = getIntent().getStringExtra("hash2");
        Log.e(TAG, "Auth: ----"+hash_card1+"----"+hash_card2 );
        if (hash_card1 == null || hash_card2 == null) {
            setSnackBar("未添加证件照片", "", R.drawable.icon_fails);
        } else {
            if (TextUtils.isEmpty(etRealName.getText().toString().trim())) {
                setSnackBar("请填写真实姓名", "", R.drawable.icon_fails);
            } else {
                if (TextUtils.isEmpty(etAdress.getText().toString().trim())) {
                    setSnackBar("请填写通讯地址", "", R.drawable.icon_fails);
                } else {
                    if (TextUtils.isEmpty(etIdCard.getText().toString().trim())) {
                        setSnackBar("请填写证件号码", "", R.drawable.icon_fails);
                    } else {
                        HttpParams params = new HttpParams();
                        params.put("distinction", "1");
                        params.put("front", hash_card1);
                        params.put("back", hash_card2);
                        params.put("realname", etRealName.getText().toString().trim());
                        params.put("number", etIdCard.getText().toString().trim());
                        params.put("address", etAdress.getText().toString().trim());
                        params.put("type", authType);
                        NetWork.INSTANCE.RealInfoAuth(RealInfoAuthStep3.this, params, new NetWork.TokenCallBack() {
                            @Override
                            public void doNext(String resultData, Headers headers) {
//                                更新本地保存的用户信息数据
                                UserInfoUtil.getUserInfoById(0, RealInfoAuthStep3.this, new UserInfoUtil.UserCallBack() {
                                    @Override
                                    public void doNext(UserInfo infoBean) {

                                    }
                                });
                                scrollView.setVisibility(View.GONE);
                                llAuthStatus.setVisibility(View.VISIBLE);
                                finishAll=true;
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
            }
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

    @OnClick(R.id.toNotice)
    public void onViewClicked() {
        Intent intent = new Intent(this, UserNoticeActivity.class);
        intent.putExtra("code", "realname");
        startActivity(intent);
    }
    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (finishAll == true) {
                ActivityCollector.INSTANCE.finishAll();
                return true;
            } else {
               finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
