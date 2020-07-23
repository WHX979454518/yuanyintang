package com.mxkj.yuanyintang.mainui.myself.my_income.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.mxkj.yuanyintang.mainui.myself.my_income.NumSpaceTextWatcher;
import com.mxkj.yuanyintang.mainui.myself.my_income.bean.BankListBean;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class AddCreditCardActivity extends StandardUiActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.tv_choose_bank)
    TextView tvChooseBank;
    @BindView(R.id.et_card_number)
    EditText etCardNumber;
    @BindView(R.id.tv_ensure_cash)
    TextView tvEnsureCash;
    @BindView(R.id.ll_data_view)
    LinearLayout llDataView;
    @BindView(R.id.img_bank)
    ImageView img_bank;
    @BindView(R.id.bankname)
    TextView bankname;
    Intent resultIntent;
    @BindView(R.id.et_card_city)
    EditText et_card_city;
    @BindView(R.id.et_card_bank)
    EditText et_card_bank;

    private final int CHOOSE_CARD_REQUEST_CODE = 0x0003;
    public static final String CARD_BEAN = "CARD_BEAN";
    private BankListBean.DataBean bankBean;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        StatusBarUtil.baseTransparentStatusBar(this);
        return R.layout.activity_add_credit_card;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        hideTitle(true);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        NumSpaceTextWatcher numSpaceTextWatcher = new NumSpaceTextWatcher(etCardNumber);
        etCardNumber.addTextChangedListener(numSpaceTextWatcher);
    }

    @OnClick({R.id.iv_back, R.id.tv_choose_bank, R.id.tv_ensure_cash})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_choose_bank:
                Intent intent = new Intent(this, AddCard_ListActivity.class);
                startActivityForResult(intent, CHOOSE_CARD_REQUEST_CODE);
                break;
            case R.id.tv_ensure_cash:
                if (bankBean == null || TextUtils.isEmpty(etCardNumber.getText()) || TextUtils.isEmpty(etUserName.getText()))
                    return;
//                if (!IsCreditCard.checkBankCard(tvEnsureCash.getText().toString().trim())){
//                    setSnackBar("请输入正确银行卡号","",R.drawable.icon_tips_bad);
//                    return;
//                }

                showLoadingView();
                HttpParams params = new HttpParams();
                params.put("bank_id", bankBean.getId());
                params.put("card_user", etUserName.getText().toString().trim());
                params.put("card_num", etCardNumber.getText().toString().trim());
                //后台添加了这俩个字段以后在放开TODO
//                params.put("card_city", et_card_city.getText().toString().trim());
//                params.put("card_bank", et_card_bank.getText().toString().trim());
                Log.e(TAG, "onClick: "+params );
                NetWork.INSTANCE.addCard(AddCreditCardActivity.this,params, new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
                        JSONObject object = JSON.parseObject(resultData);
                        JSONObject data = object.getJSONObject("data");
                        if (data==null){
                            return;
                        }
                        Integer id = data.getInteger("id");
                        resultIntent = new Intent();
                        bankBean.setUser_card_id(id);
                        bankBean.setBank_user_name(etUserName.getText().toString());
                        bankBean.setCard_number(etCardNumber.getText().toString());
                        resultIntent.putExtra(CARD_BEAN, bankBean);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }

                    @Override
                    public void doError(String msg) {
                        hideLoadingView();
                    }

                    @Override
                    public void doResult() {

                    }
                });
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_CARD_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            bankBean = (BankListBean.DataBean) data.getSerializableExtra(CARD_BEAN);
            if (bankBean == null) return;
            tvChooseBank.setText(bankBean.getName());
            tvChooseBank.setTextColor(Color.BLACK);
            if (bankBean.getIcon_info() != null) {
//                img_bank.setVisibility(View.VISIBLE);
//                bankname.setVisibility(View.GONE);
                ImageLoader.with(AddCreditCardActivity.this).url(bankBean.getIcon_info().getLink()).into(img_bank);
            }
        }
    }
}
