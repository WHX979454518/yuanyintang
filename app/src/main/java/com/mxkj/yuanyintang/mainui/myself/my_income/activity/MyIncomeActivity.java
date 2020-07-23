package com.mxkj.yuanyintang.mainui.myself.my_income.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.mainui.myself.my_income.bean.IncomeHomeBean;
import com.mxkj.yuanyintang.mainui.myself.my_income.dialog.BotomFishTips;
import com.mxkj.yuanyintang.mainui.myself.my_income.dialog.ExchangeDoughtDialog;
import com.mxkj.yuanyintang.mainui.myself.my_release.BindPartnerActivity;
import com.mxkj.yuanyintang.mainui.myself.my_release.MyReleaseActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class MyIncomeActivity extends StandardUiActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_income_history)
    TextView tvIncomeHistory;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_week_num)
    TextView tvWeekNum;
    @BindView(R.id.money_history)
    TextView moneyHistory;
    @BindView(R.id.tv_total_num)
    TextView tvTotalNum;
    @BindView(R.id.tv_get_money)
    TextView tvGetMoney;
    @BindView(R.id.tv_to_dought)
    TextView tvToDought;
    @BindView(R.id.tv_money_desc)
    TextView tvMoneyDesc;
    @BindView(R.id.tv_dought_desc)
    TextView tvDoughtDesc;
    @BindView(R.id.ll_data_view)
    LinearLayout llDataView;
    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_income_history_nodata)
    TextView tvIncomeHistoryNodata;
    @BindView(R.id.ll_no_data_view)
    LinearLayout llNoDataView;
    @BindView(R.id.tv_tips)
    TextView tv_tips;
    IncomeHomeBean.DataBean data;
    public static final String MY_FISH_NUM = "MY_FISH_NUM";
    public static final String FISH_TO_MONRY_RATE = "FISH_TO_MONRY_RATE";
    public static final String BANK_INFO = "BANK_INFO";
    public static final String APPLY_DESC = "APPLY_DESC";

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        StatusBarUtil.baseTransparentStatusBar(this);
        return R.layout.activity_my_income;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        hideTitle(true);
    }

    @Override
    protected void initData() {
    }

    protected void initHomeData() {
        showLoadingView();
        NetWork.INSTANCE.getMyIncomeHome(this, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                hideLoadingView();
                IncomeHomeBean incomeHomeBean = JSON.parseObject(resultData, IncomeHomeBean.class);
                data = incomeHomeBean.getData();
                initPageData();
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });

    }

    private String s;
    private void initPageData() {
        if (data == null) {
            return;
        }
        NumberFormat nf = new DecimalFormat("#.##");
//        if (data.getIs_get_fish() == 0) {
//            llNoDataView.setVisibility(View.VISIBLE);
//            llDataView.setVisibility(View.GONE);
//        } else {
//            llNoDataView.setVisibility(View.GONE);
//            llDataView.setVisibility(View.VISIBLE);
//        }

        String week = nf.format(data.getWeek_fish());
        tvWeekNum.setText(data.getWeek_fish() == 0 ? "暂无收益" : week + "");

        s = nf.format(data.getFish_counts());
        tvTotalNum.setText("账户余额" + s + "(小鱼干)");
        if (data.getCash_counts() > 0) {
            moneyHistory.setText(data.getCash_counts() + "笔金额提现中");
        }else{
            moneyHistory.setText("提现记录");
        }

        if (data.getCan_apply_cash() == 0) {
            tvGetMoney.setText("满" + data.getMin_cash_money_num() + "个小鱼干才能提现");
            tvGetMoney.setTextColor(Color.parseColor("#80ffffff"));
            tvGetMoney.setClickable(false);
        } else {
            tvGetMoney.setText("申请提现");
            tvGetMoney.setTextColor(Color.parseColor("#ffffff"));
            tvGetMoney.setClickable(true);
        }

        tvMoneyDesc.setText(StringUtils.isEmpty(data.getApply_cash_desc()));
        tvDoughtDesc.setText(StringUtils.isEmpty(data.getExchange_coin_desc()));


    }

    @Override
    protected void initEvent() {

    }

    @OnClick({R.id.iv_back, R.id.tv_tips_fish, R.id.tv_tips, R.id.tv_income_history, R.id.iv_finish, R.id.tv_income_history_nodata, R.id.money_history, R.id.tv_get_money, R.id.tv_to_dought})
    public void onClick(View view) {
        BotomFishTips botomFishTips = new BotomFishTips(0,data.getFish_desc(),data.getHelp_url());
        switch (view.getId()) {
            case R.id.tv_tips_fish:
                if (data==null)return;
                botomFishTips.show(getSupportFragmentManager(), "FISH");
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_tips:
                if (data==null)return;
                botomFishTips.show(getSupportFragmentManager(), "FISH");
                break;
            case R.id.tv_income_history:
                goActivity(IncomeHistoryActivity.class);
                break;
            case R.id.money_history:
                goActivity(CashHistoryActivity.class);
                break;
            case R.id.tv_get_money:
                int idx = s.lastIndexOf(".");//查找小数点的位置
                String strNum = s.substring(0,idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
                int num = Integer.valueOf(strNum);//把整数部分通过Integer.valueof方法转换为数字
                if(null != s){
                    if(num >= 10){
                        if (data == null) {
                            return;
                        }
                        Intent intent = new Intent(this, ApplyCashActivity.class);
                        intent.putExtra(MY_FISH_NUM, data.getFish_counts());
                        intent.putExtra(FISH_TO_MONRY_RATE, data.getFish_to_money_rate());
                        intent.putExtra(BANK_INFO, data.getBank_info());
                        intent.putExtra(APPLY_DESC, data.getApply_cash_desc());
                        startActivity(intent);
                    }else {
                        BaseConfirmDialog.Companion.newInstance()
                                .title("温馨提示")
                                .content("至少提现10个小鱼干哦~")
                                .confirmText("好的")
                                .isOneBtn(true)
                                .showDialog(MyIncomeActivity.this, new BaseConfirmDialog.onBtClick() {
                                    @Override
                                    public void onConfirm() {

                                    }

                                    @Override
                                    public void onCancle() {

                                    }
                                });
                    }
                }

                break;
            case R.id.tv_to_dought:
                ExchangeDoughtDialog.newInstance().fish_to_coin_rate(data.getFish_to_coin_rate()).my_fish_num(data.getFish_counts()).showDialog(this, new ExchangeDoughtDialog.onBtClick() {
                    @Override
                    public void onConfirm(float num) {
                        NetWork.INSTANCE.exchange(MyIncomeActivity.this, num, new NetWork.TokenCallBack() {
                            @Override
                            public void doNext(String resultData, Headers headers) {
                                setSnackBar("兑换成功~", "", R.drawable.icon_success);
                                initHomeData();
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
                break;
            case R.id.iv_finish:
                finish();
                break;
            case R.id.tv_income_history_nodata:
                goActivity(IncomeHistoryActivity.class);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHomeData();
    }
}
