package com.mxkj.yuanyintang.mainui.myself.my_income.dialog;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.mainui.myself.settings.activity.NoMobile_goBind_Activity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.app.CountDownTimerUtils;
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder;

import okhttp3.Headers;

import static com.linsh.lshutils.utils.LshContextUtils.getSystemService;

/**
 * Created by zuixia on 2018/5/14.
 */

public class CashAuthDialog {
    private DiaLogBuilder dialog;
    private CountDownTimerUtils countDownTimerUtils;
    private String sendtype = "mobile";

    public static CashAuthDialog newInstance() {
        return new CashAuthDialog();
    }

    public CashAuthDialog showDialog(final Context mContext, final CashAuthDialog.onBtClick onBtClick) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cash_ensure_dialog_layout, null);
        ImageView img_close = view.findViewById(R.id.img_close);
        final TextView tv_tips = view.findViewById(R.id.tv_tips);
        final TextView tv_reget = view.findViewById(R.id.tv_reget);
        TextView tv_sure = view.findViewById(R.id.tv_sure);
        final EditText et_code = view.findViewById(R.id.et_code);
        UserInfoUtil.getUserInfoById(0, mContext, new UserInfoUtil.UserCallBack() {
            @Override
            public void doNext(UserInfo infoBean) {
                if (infoBean != null && infoBean.getData() != null) {
                    String mobile = infoBean.getData().getMobile();
                    String email = infoBean.getData().getEmail();
                    if (!TextUtils.isEmpty(mobile)) {
                        tv_tips.setText("我们需向" + mobile + "发送验证码");
                        sendtype = "mobile";
                    } else if (!TextUtils.isEmpty(email)) {
                        tv_tips.setText("我们需向" + email + "发送验证码");
                        sendtype = "email";

                    } else {
                        dialog.setDismiss();
                        BaseConfirmDialog.Companion.newInstance().title("未绑定手机").content("您还没有绑定手机\n\n为了您的账号安全\n请绑定后再认证哟").confirmText("去绑定").isOneBtn(true).showDialog(mContext, new BaseConfirmDialog.onBtClick() {
                            @Override
                            public void onConfirm() {
                                mContext.startActivity(new Intent(mContext, NoMobile_goBind_Activity.class));
                            }

                            @Override
                            public void onCancle() {

                            }
                        });
                    }
                    countDownTimerUtils = new CountDownTimerUtils(tv_reget, 60000, 1000);
                }
            }
        });

        tv_reget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpParams params = new HttpParams();
                params.put("type", "apply_cash");
                params.put("sendtype", sendtype);
                NetWork.INSTANCE.getCodeNoLogin(params, mContext, new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
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
        });


        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et_code.getText())) {
                    InputMethodManager mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputManager.hideSoftInputFromWindow(et_code.getWindowToken(), 0);
                    onBtClick.onConfirm(et_code.getText().toString());
                }
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setDismiss();
            }
        });

        dialog = new DiaLogBuilder(mContext)
                .setContentView(view)
                .setAniMo(R.anim.popup_in)
                .setFullScreen()
                .setCanceledOnTouchOutside(false)
                .setGrvier(Gravity.CENTER);
        dialog.show();
        return this;

    }

    public void setDismiss() {
        dialog.setDismiss();
    }

    public interface onBtClick {
        void onConfirm(String code);
    }
}


