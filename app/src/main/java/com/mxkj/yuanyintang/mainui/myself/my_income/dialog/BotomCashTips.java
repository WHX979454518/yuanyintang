package com.mxkj.yuanyintang.mainui.myself.my_income.dialog;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * Created by zuixia on 2018/5/14.
 */
@SuppressLint("ValidFragment")
public class BotomCashTips extends BaseDialogFragment {
    @BindView(R.id.tv_reason)
    TextView tvReason;
    Unbinder unbinder;
    private String reason = "";

    @SuppressLint("ValidFragment")
    public BotomCashTips(String reason) {
        this.reason = reason;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.botom_cash_dialog;
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
        tvReason.setText(reason);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}


