package com.mxkj.yuanyintang.mainui.myself.my_income.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.login_regist.UserNoticeActivity;
import com.mxkj.yuanyintang.mainui.myself.my_income.EdittextUtils;
import com.mxkj.yuanyintang.mainui.myself.my_income.bean.BankListBean;
import com.mxkj.yuanyintang.mainui.myself.my_income.bean.IncomeHomeBean;
import com.mxkj.yuanyintang.mainui.myself.my_income.dialog.CashAuthDialog;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.myself.my_income.activity.AddCreditCardActivity.CARD_BEAN;
import static com.mxkj.yuanyintang.mainui.myself.my_income.activity.MyIncomeActivity.APPLY_DESC;
import static com.mxkj.yuanyintang.mainui.myself.my_income.activity.MyIncomeActivity.BANK_INFO;
import static com.mxkj.yuanyintang.mainui.myself.my_income.activity.MyIncomeActivity.FISH_TO_MONRY_RATE;
import static com.mxkj.yuanyintang.mainui.myself.my_income.activity.MyIncomeActivity.MY_FISH_NUM;

public class ApplyCashActivity extends StandardUiActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_no_card)
    LinearLayout llNoCard;
    @BindView(R.id.img_card)
    CircleImageView imgCard;
    @BindView(R.id.bank_name)
    TextView bankName;
    @BindView(R.id.card_number)
    TextView cardNumber;
    @BindView(R.id.ll_card_view)
    LinearLayout llCardView;
    @BindView(R.id.tv_fish_num)
    TextView tvFishNum;
    @BindView(R.id.tv_cash_all)
    TextView tvCashAll;
    @BindView(R.id.et_fish_num)
    EditText etFishNum;
    @BindView(R.id.cash_tips)
    TextView cashTips;
    @BindView(R.id.tv_ensure_cash)
    TextView tvEnsureCash;
    @BindView(R.id.ll_data_view)
    LinearLayout llDataView;
    @BindView(R.id.user_notice)
    TextView userNotice;
    @BindView(R.id.ll_notice)
    LinearLayout llNotice;
    @BindView(R.id.tv_bank_desc)
    TextView tvBankDesc;
    @BindView(R.id.money_desc)
    TextView moneyDesc;
    @BindView(R.id.msg_desc)
    TextView msgDesc;
    @BindView(R.id.tv_cash_history)
    TextView tvCashHistory;
    @BindView(R.id.back_botom)
    TextView backBotom;
    @BindView(R.id.ll_cash_success)
    LinearLayout llCashSuccess; @BindView(R.id.tv_desc)
    TextView tv_desc;
    private float myFishNum;
    private float fish_to_money_rate;
    public static final int ADD_CARD_REQUEST_CODE = 0x0001;
    private BankListBean.DataBean bankBean;
    private IncomeHomeBean.DataBean.BankInfoBean bankInfo;
    private CashAuthDialog cashAuthDialog;
    private DecimalFormat df;
    private String apply_desc;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        StatusBarUtil.baseTransparentStatusBar(this);
        return R.layout.activity_apply_cash;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        hideTitle(true);
    }

    @Override
    protected void initData() {
        myFishNum = getIntent().getFloatExtra(MY_FISH_NUM, 0);
        apply_desc = getIntent().getStringExtra(APPLY_DESC);
        if (apply_desc!=null){
            tv_desc.setText(apply_desc);
        }
        fish_to_money_rate = getIntent().getFloatExtra(FISH_TO_MONRY_RATE, 0f);
        df = new DecimalFormat("#.##");
        float money = myFishNum * fish_to_money_rate;
        cashTips.setText("可到账" + df.format(money) + "元");
        bankInfo = (IncomeHomeBean.DataBean.BankInfoBean) getIntent().getSerializableExtra(BANK_INFO);
        tvFishNum.setText(df.format(myFishNum) + "");
        if (bankInfo == null) return;
        llCardView.setVisibility(View.VISIBLE);
        llNoCard.setVisibility(View.GONE);
        if (bankInfo.getIcon_info() != null && bankInfo.getIcon_info().getLink() != null) {
            ImageLoader.with(ApplyCashActivity.this).url(bankInfo.getIcon_info().getLink()).into(imgCard);
        }
        bankName.setText(StringUtils.isEmpty(bankInfo.getBank_name()));
        cardNumber.setText(StringUtils.isEmpty(bankInfo.getCard_num()));

        bankBean = new BankListBean.DataBean();
        bankBean.setUser_card_id(bankInfo.getId());
        BankListBean.DataBean.IconInfoBean iconInfoBean = new BankListBean.DataBean.IconInfoBean();
        iconInfoBean.setLink(bankInfo.getIcon_info().getLink());
        bankBean.setIcon_info(iconInfoBean);
        bankBean.setCard_number(bankInfo.getCard_num());
        bankBean.setName(bankInfo.getBank_name());

        if(etFishNum.getText().toString().equals("") || etFishNum.getText().toString() == ""){
            tvEnsureCash.setBackgroundResource(R.drawable.shape_un_add_notcard);
        }

    }

    @Override
    protected void initEvent() {
        if (bankBean == null) {
            tvEnsureCash.setBackgroundResource(R.drawable.shape_un_add_card);
            tvEnsureCash.setClickable(false);
        } else {
            tvEnsureCash.setBackgroundResource(R.drawable.shape_button_red_incode);
            tvEnsureCash.setClickable(true);
        }
        etFishNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence != null && charSequence.length() > 0) {
                    float et_num = Float.valueOf(charSequence.toString());
                    if (et_num > myFishNum) {
                        cashTips.setText("你没有那么多小鱼干");
                        tvEnsureCash.setBackgroundResource(R.drawable.shape_un_add_card);
                        tvEnsureCash.setClickable(false);
                        return;
                    } else {
                        if (et_num < 10) {
                            cashTips.setText("至少提现10个小鱼干哦");
                            tvEnsureCash.setBackgroundResource(R.drawable.shape_un_add_card);
                            tvEnsureCash.setClickable(false);
                        } else {
                            tvEnsureCash.setBackgroundResource(R.drawable.shape_button_red_incode);
                            float money = et_num * fish_to_money_rate;
                            cashTips.setText("可到账" + money + "元");
                            tvEnsureCash.setClickable(true);
                        }
                    }
                } else {
                    cashTips.setText("");
                    tvEnsureCash.setBackgroundResource(R.drawable.shape_un_add_notcard);
//                    tvEnsureCash.setBackgroundResource(R.drawable.shape_un_add_card);
                    tvEnsureCash.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                EdittextUtils.judgeNumber(editable,etFishNum);
                if (editable!=null) {
                    etFishNum.setSelection(editable.length());
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.ll_no_card, R.id.ll_notice, R.id.tv_cash_history, R.id.back_botom, R.id.ll_card_view, R.id.tv_cash_all, R.id.tv_ensure_cash})
    public void onClick(View view) {
        Intent intent = new Intent(this, AddCreditCardActivity.class);
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_notice:
                Intent intentNotice = new Intent(this, UserNoticeActivity.class);
                intentNotice.putExtra("code", "fish_use_protocol");
                startActivity(intentNotice);
                break;
            case R.id.ll_no_card:
                startActivityForResult(intent, ADD_CARD_REQUEST_CODE);
                break;
            case R.id.ll_card_view:
                startActivityForResult(intent, ADD_CARD_REQUEST_CODE);
                break;
            case R.id.tv_cash_all:
                etFishNum.setText(myFishNum + "");
                break;
            case R.id.tv_ensure_cash:
                applyCash();
                break;
            case R.id.tv_cash_history:
                goActivity(CashHistoryActivity.class);
                break;
            case R.id.back_botom:
                finish();
                break;
        }
    }

    private void applyCash() {
        if (bankBean == null || TextUtils.isEmpty(etFishNum.getText())) return;
        cashAuthDialog = CashAuthDialog.newInstance().showDialog(this, new CashAuthDialog.onBtClick() {
            @Override
            public void onConfirm(String code) {
                HttpParams params = new HttpParams();
                params.put("fish_num", etFishNum.getText().toString().trim());
                params.put("u_bank_id", bankBean.getUser_card_id() + "");
                params.put("code", code);
                Log.e(TAG, "onConfirm: "+params );
                NetWork.INSTANCE.applyCash(ApplyCashActivity.this, params, new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
                        cashAuthDialog.setDismiss();
                        llCashSuccess.setVisibility(View.VISIBLE);
                        JSONObject object = JSON.parseObject(resultData);
                        JSONObject dataObj = object.getJSONObject("data");
                        if (dataObj == null) return;
                        String bank_desc = dataObj.getString("bank_desc");
                        String cash_success_desc = dataObj.getString("cash_success_desc");
                        float cash_money = dataObj.getFloat("cash_money");
                        tvBankDesc.setText(StringUtils.isEmpty(bank_desc));
                        msgDesc.setText(StringUtils.isEmpty(cash_success_desc));
                        moneyDesc.setText("￥" + cash_money);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CARD_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            bankBean = (BankListBean.DataBean) data.getSerializableExtra(CARD_BEAN);
            llCardView.setVisibility(View.VISIBLE);
            llNoCard.setVisibility(View.GONE);
            if (bankBean.getIcon_info() != null) {
                ImageLoader.with(ApplyCashActivity.this).url(bankBean.getIcon_info().getLink()).into(imgCard);
            }
            bankName.setText(StringUtils.isEmpty(bankBean.getName()));
            cardNumber.setText(StringUtils.isEmpty(bankBean.getCard_number()));
        }
    }

}
