package com.mxkj.yuanyintang.mainui.login_regist;//package com.mxkj.yuanyintang.main_ui.login_regist;
//
//import android.content.Intent;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.mxkj.yuanyintang.R;
//import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
//import com.mxkj.yuanyintang.net.NetWork;
//import com.mxkj.yuanyintang.utils.app.ActivityCollector;
//import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import okhttp3.Headers;
//
//public class BindMobileActivity extends StandardUiActivity {
//    @BindView(R.id.finish)
//    ImageView finish;
//    @BindView(R.id.rl_title)
//    RelativeLayout rlTitle;
//    @BindView(R.id.tv_type)
//    TextView tvType;
//    @BindView(R.id.et_phone)
//    EditText etPhone;
//    @BindView(R.id.bt_sure)
//    TextView btSure;
//    @BindView(R.id.ll_phone)
//    LinearLayout llPhone;
//
//    @Override
//    public int setLayoutId() {
//        return R.layout.activity_bind_mobile;
//    }
//
//    @Override
//    public boolean isVisibilityBottomPlayer() {
//        return false;
//    }
//
//    @Override
//    protected void initView() {
//        hideTitle(true);
//        ButterKnife.bind(this);
//        StatusBarUtil.baseTransparentStatusBar(this);
//    }
//
//    @Override
//    protected void initEvent() {
//
//    }
//
//    @Override
//    protected void initData() {
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        ActivityCollector.addActivity(this);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ActivityCollector.removeActivity(this);
//    }
//
//    @OnClick({R.id.finish, R.id.bt_sure})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.finish:
//                finish();
//                break;
//            case R.id.bt_sure:
//                if (TextUtils.isEmpty(etPhone.getText())) {
//                    setSnackBar("账号不能为空", "", R.drawable.icon_fails);
//                } else {
//                    NetWork.getCode(null,"bind_mobile", "1", etPhone.getText().toString(), this, new NetWork.TokenCallBack() {
//                        @Override
//                        public void doNext(String resultData, Headers headers) {
//                            Intent intent = new Intent(BindMobileActivity.this, QuickCodeActivityNew.class);
//                            intent.putExtra("must",true);
//                            intent.putExtra("mobile",etPhone.getText().toString());
//                            startActivity(intent);
//                        }
//
//                        @Override
//                        public void doError(String msg) {
//
//                        }
//
//                        @Override
//                        public void doResult() {
//
//                        }
//                    });
//                }
//                break;
//        }
//    }
//}
