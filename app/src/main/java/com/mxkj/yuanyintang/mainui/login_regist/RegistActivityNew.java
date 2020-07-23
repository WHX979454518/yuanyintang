package com.mxkj.yuanyintang.mainui.login_regist;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.HelpCenterActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class RegistActivityNew extends StandardUiActivity {
    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.bt_sure)
    TextView btSure;
    @BindView(R.id.tv_haveKey)
    TextView tvHaveKey;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.user_notice)
    TextView userNotice;
    @BindView(R.id.copyright)
    TextView copyright;
    @BindView(R.id.tv_in_trouble)
    TextView tv_in_trouble;
    @BindView(R.id.ll_notice)
    LinearLayout llNotice;
    String inviteCode;

    @Override
    public int setLayoutId() {
        return R.layout.activity_regist;
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

    @OnClick({R.id.finish, R.id.tv_in_trouble, R.id.bt_sure, R.id.tv_haveKey, R.id.user_notice, R.id.copyright})
    public void onClick(View view) {
        Intent intent = new Intent(this, UserNoticeActivity.class);
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.tv_in_trouble:
                startActivity(new Intent(this, HelpCenterActivity.class));
                break;
            case R.id.bt_sure:
                if (TextUtils.isEmpty(etPhone.getText())) {
                    setSnackBar("账号不能为空", "", R.drawable.icon_fails);
                } else {
                    NetWork.INSTANCE.getCode("", "reg", "", etPhone.getText().toString(), this, new NetWork.TokenCallBack() {
                        @Override
                        public void doNext(String resultData, Headers headers) {
                            Intent intent = new Intent(RegistActivityNew.this, RegistStep2.class);
                            intent.putExtra("mobile", etPhone.getText().toString());
                            if (!TextUtils.isEmpty(inviteCode)) {
                                intent.putExtra("inCode", inviteCode);
                            }
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
            case R.id.tv_haveKey:
                initInviteCodeDialog();
                break;
            case R.id.user_notice:
                intent.putExtra("code", "agreement");
                startActivity(intent);
                break;
            case R.id.copyright:
                intent.putExtra("code", "copyrightpolicy");
                startActivity(intent);
                break;
        }
    }

    public void initInviteCodeDialog() {
        View view = View.inflate(this, R.layout.dialog_invite_code, null);
        diaLogBuilder = new DiaLogBuilder(this)
                .setContentView(view)
                .setFullScreen()
                .setGrvier(Gravity.CENTER)
                .show();
        TextView tv_know = view.findViewById(R.id.tv_know);
        final EditText et_inviteCode = view.findViewById(R.id.et_inviteCode);
        et_inviteCode.setTransformationMethod(new UpperCaseTransform());
        ImageView diss_dialog = view.findViewById(R.id.img_close_dialog);
        diss_dialog.setVisibility(View.VISIBLE);
        tv_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: " );
                inviteCode = et_inviteCode.getText().toString();
                tvHaveKey.setText("已输入邀请码：" + inviteCode);
                diaLogBuilder.setDismiss();

                NetWork.INSTANCE.authIncode(RegistActivityNew.this, et_inviteCode.getText().toString(), new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
//                        inviteCode = et_inviteCode.getText().toString();
//                        tvHaveKey.setText("已输入邀请码：" + inviteCode);
//                        diaLogBuilder.setDismiss();
                    }

                    @Override
                    public void doError(String msg) {
                        setSnackBar(StringUtils.isEmpty(msg),"",R.drawable.icon_tips_bad);
                    }

                    @Override
                    public void doResult() {

                    }
                });
            }
        });
        diss_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diaLogBuilder.setDismiss();
            }
        });
    }

    public class UpperCaseTransform extends ReplacementTransformationMethod {
        @Override
        protected char[] getOriginal() {
            char[] aa = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            return aa;
        }

        @Override
        protected char[] getReplacement() {
            char[] cc = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            return cc;
        }
    }
}
