package com.mxkj.yuanyintang.mainui.login_regist.widget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.HelpCenterActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by LiuJie on 2018/4/18.
 */
@SuppressLint("ValidFragment")
public class FailGetCodeDialog extends BaseDialogFragment {

    @BindView(R.id.tv_see_more)
    TextView tv_see_more;
    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.tv_fail_getcode_tips)
    TextView tvFailGetcodeTips;
    int type;
    String tipsText = "";

    public FailGetCodeDialog(int type) {
        this.type = type;
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.dialog_title_fail_getcode;
    }

    @Override
    protected Boolean isBack() {
        return false;
    }

    @Override
    protected int style() {
        return 0;
    }

    @Override
    protected void initView() {
        if (type == 0) {
            tipsText = "1、请查看验证码是否被广告过滤软件过滤\n2、请将手机开关一次飞行模式再试\n3、一部手机每天只能请求6次验证码\n4、若手机号有误，请联系客服处理";
        } else {
            tipsText = "1、请查看验证码是否被广告过滤软件过滤\n2、请刷新邮箱再试\n3、若邮箱号有误，请联系客服处理";
        }
        tvFailGetcodeTips.setText(tipsText);

        RxView.clicks(tvClose)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        dismiss();
                    }
                });
        RxView.clicks(tv_see_more)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                       getActivity().startActivity(new Intent(getActivity(), HelpCenterActivity.class));
                    }
                });
    }

}
