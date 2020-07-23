package com.mxkj.yuanyintang.mainui.myself.doughnut;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.login_regist.UserNoticeActivity;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.mainui.myself.my_income.activity.MyIncomeActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.pay.PayUtils;
import com.mxkj.yuanyintang.extraui.gift.FirstChargeDialog;
import com.mxkj.yuanyintang.utils.layoutmanager.FullyGridLayoutManager;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class ChargeDoughnutActivity extends StandardUiActivity {
    @BindView(R.id.imageview)
    ImageView imageview;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.dought_num)
    TextView doughtNum;
    @BindView(R.id.layout_head)
    LinearLayout layoutHead;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.ex_linear_layou)
    ExpandableLinearLayout exLinearLayou;
    @BindView(R.id.tv_charge_title)
    TextView tvChargeTitle;
    @BindView(R.id.tv_charge_content)
    TextView tvChargeContent;
    @BindView(R.id.tv_go_first_charge)
    TextView tvGoFirstCharge;
    @BindView(R.id.ll_first_charge)
    RelativeLayout llFirstCharge;
    @BindView(R.id.tv_charge)
    TextView tvCharge;
    @BindView(R.id.user_notice)
    TextView userNotice;
    @BindView(R.id.ll_notice)
    LinearLayout llNotice;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    private ChargeItemAdapter chargeItemAdapter;
    private List<ChargeItemBean.DataBean.ListsBean> dataList = new ArrayList<>();
    private int payType;
    private int payId;

    boolean isNotify;
    private ChargeItemBean.DataBean.SettingBean setting;
    private boolean isShowFirstDialog;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        StatusBarUtil.baseTransparentStatusBar(this);
        return R.layout.activity_charge_doughnut;
    }

    @Override
    protected void initView() {
        isShowFirstDialog = getIntent().getBooleanExtra("isShowFirstDialog",false);
        hideTitle(true);
        ButterKnife.bind(this);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRangle = appBarLayout.getTotalScrollRange();
                toolbar.setBackgroundColor(changeAlpha(getResources().getColor(R.color.white), Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));
                if (verticalOffset == 0) {
//                    tvTitle.setAlpha(0.0f);
//                    ivBack.setAlpha(0.0f);
                } else {
                    float alpha = Math.abs(Math.round(1.0f * verticalOffset / scrollRangle) * 10) / 10;
//                    tvTitle.setAlpha(alpha);
//                    ivBack.setAlpha(alpha);
                }
            }
        });
    }

    private void initRecycler() {
        dataList.get(0).setChecked(true);
        FullyGridLayoutManager layoutManager = new FullyGridLayoutManager(this, 3);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setHasFixedSize(true);
        recyclerview.setNestedScrollingEnabled(false);
        chargeItemAdapter = new ChargeItemAdapter(dataList);
        recyclerview.setAdapter(chargeItemAdapter);
        chargeItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e(TAG, "onItemClick: " + position);
                for (int i = 0; i < dataList.size(); i++) {
                    if (position == i) {
                        chargeItemAdapter.getData().get(i).setChecked(true);
                        payId = chargeItemAdapter.getData().get(i).getId();
                    } else {
                        chargeItemAdapter.getData().get(i).setChecked(false);
                    }
                }
                chargeItemAdapter.notifyDataSetChanged();
            }
        });
    }

    public int changeAlpha(int color, float fraction) {
        Log.e(TAG, "changeAlpha: " + fraction);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);


        if (fraction > 0.4) {
            ivBack.setImageResource(R.drawable.icon_back_black);
            tvTitle.setTextColor(Color.BLACK);
        } else if (fraction <= 0.4) {
            ivBack.setImageResource(R.drawable.icon_back_white);
            tvTitle.setTextColor(Color.WHITE);
        }

        return Color.argb(alpha, red, green, blue);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private void initDatas() {
        NetWork.INSTANCE.getChargeItem(this, new HttpParams("row", "6"), new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                ChargeItemBean chargeItemBean = JSON.parseObject(resultData, ChargeItemBean.class);
                ChargeItemBean.DataBean data = chargeItemBean.getData();
                List<ChargeItemBean.DataBean.ListsBean> lists = data.getLists();setting = data.getSetting();
                if (setting != null && setting.getLast_pay_info() != null) {
                    List<ChargeItemBean.DataBean.SettingBean.LastPayInfoBean> last_pay_info = setting.getLast_pay_info();
                    if (last_pay_info != null && last_pay_info.size() > 0) {
                        initPayWayView(last_pay_info);
                    }
                }
                if (setting != null && setting.getFirst_charge() > 0) {
                    if (isShowFirstDialog){
                        isShowFirstDialog=false;
                        FirstChargeDialog.Companion.newInstance().showDialog(ChargeDoughnutActivity.this, new FirstChargeDialog.onBtClick() {
                            @Override
                            public void onConfirm() {

                            }

                            @Override
                            public void onCancle() {
                            }
                        });
                    }
                    llFirstCharge.setVisibility(View.VISIBLE);
                } else {
                    llFirstCharge.setVisibility(View.GONE);
                }
                if (lists != null && lists.size() > 0) {
                    dataList.clear();
                    dataList.addAll(lists);
                    payId = dataList.get(0).getId();
                    if (!isNotify) {
                        initRecycler();
                    }
                }
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
        UserInfoUtil.getUserInfoById(0, this, new UserInfoUtil.UserCallBack() {
            @Override
            public void doNext(UserInfo infoBean) {
                if (infoBean != null && infoBean.getData() != null) {
                    int id = infoBean.getData().getId();
                    int coin_counts = infoBean.getData().getCoin_counts();
                    String nickname = infoBean.getData().getNickname();
                    nickName.setText(nickname + ",您拥有甜甜圈");
                    doughtNum.setText("" + coin_counts);
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideLoadingView();
        isNotify = true;
        appBar.setExpanded(true,true);
    }

    @OnClick({R.id.tv_go_first_charge, R.id.iv_back, R.id.tv_charge, R.id.user_notice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_go_first_charge:
                if (payId != 0) {
                    PayUtils.INSTANCE.pay(this, payId, payType);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_charge:
                Log.e(TAG, "onClick:去支付----- " + payType);
                showLoadingView();
                if (payId != 0) {
                    PayUtils.INSTANCE.pay(this, payId, payType);
                }
                break;
            case R.id.user_notice:
                Intent intent = new Intent(this, UserNoticeActivity.class);
                intent.putExtra("code", "fish_use_protocol");
                startActivity(intent);
                break;
        }
    }

    private void initPayWayView(final List<ChargeItemBean.DataBean.SettingBean.LastPayInfoBean> last_pay_info) {
        if (exLinearLayou.getChildCount() > 2) return;
        exLinearLayou.removeAllViews();
        Log.e(TAG, "initPayWayView: " + last_pay_info.size());
        for (int i = 0; i < last_pay_info.size(); i++) {
            View pay_view = LayoutInflater.from(this).inflate(R.layout.pay_way_layout, null);
            TextView pay_name = pay_view.findViewById(R.id.pay_name);
            ImageView iv_pay = pay_view.findViewById(R.id.iv_pay);
            setDrawable(iv_pay, last_pay_info.get(i).getType());
            final CheckBox ck_pay_way = pay_view.findViewById(R.id.ck_pay_way);
            pay_name.setText(last_pay_info.get(i).getDesc());
            final int finalI = i;
            pay_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < exLinearLayou.getChildCount(); i++) {
                        View childAt = exLinearLayou.getChildAt(i);
                        final CheckBox ck_pay_way = childAt.findViewById(R.id.ck_pay_way);
                        if (ck_pay_way != null) {
                            ck_pay_way.setChecked(false);
                        }
                    }
                    ck_pay_way.setChecked(true);
                    payType = last_pay_info.get(finalI).getType();
                }
            });
            if (last_pay_info.get(i).getIs_last() == 1) {
                ck_pay_way.setChecked(true);
                payType = last_pay_info.get(i).getType();
                Log.e(TAG, "默认的---: " + payType);
                exLinearLayou.addItem(pay_view, 0);
            } else {
                ck_pay_way.setChecked(false);
                exLinearLayou.addItem(pay_view, -1);
            }
        }
        UserInfoUtil.getUserInfoById(0, this, new UserInfoUtil.UserCallBack() {
            @Override
            public void doNext(UserInfo infoBean) {
                if (infoBean != null && infoBean.getData() != null) {
                    int id = infoBean.getData().getId();
                    int coin_counts = infoBean.getData().getCoin_counts();
                    String nickname = infoBean.getData().getNickname();
                    nickName.setText(nickname + ",您拥有甜甜圈");
                    doughtNum.setText("" + coin_counts);
                    int is_auth = infoBean.getData().getIs_auth();
                    if (is_auth == 3) {
                        View pay_fsh = LayoutInflater.from(ChargeDoughnutActivity.this).inflate(R.layout.pay_from_fish, null);
                        exLinearLayou.addItem(pay_fsh, -1);
                        pay_fsh.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(ChargeDoughnutActivity.this, MyIncomeActivity.class));
                            }
                        });
                    }
                }
            }
        });

    }

    private void setDrawable(ImageView tv, int type) {
        switch (type) {
            case 3:
                tv.setImageResource(R.drawable.ali_pay);
                break;
            case 2:
                tv.setImageResource(R.drawable.qq_pay);
                break;
            case 1:
                tv.setImageResource(R.drawable.wx_pay);
                break;
        }

    }

}
