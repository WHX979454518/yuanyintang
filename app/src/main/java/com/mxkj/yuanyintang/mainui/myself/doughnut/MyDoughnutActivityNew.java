package com.mxkj.yuanyintang.mainui.myself.doughnut;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.home.data.Constant;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.mainui.myself.bean.MyDoughnutBean;
import com.mxkj.yuanyintang.mainui.myself.doughnut.adapter.DoughtListAdapter;
import com.mxkj.yuanyintang.mainui.myself.my_income.dialog.BotomFishTips;
import com.mxkj.yuanyintang.mainui.myself.settings.activity.NoMobile_goBind_Activity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.extraui.view.sticky.StickyItemDecoration;
import com.mxkj.yuanyintang.utils.app.CountDownTimerUtils;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.VpSuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class MyDoughnutActivityNew extends StandardUiActivity {
    @BindView(R.id.imageview)
    ImageView imageview;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.dought_num)
    TextView doughtNum;
    @BindView(R.id.tv_charge_now)
    TextView tvChargeNow;
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
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.superSwipeRefreshLayout)
    VpSuperSwipeRefreshLayout superSwipeRefreshLayout;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    List<MyDoughnutBean.DataBeanX.DataBean.MonthDataBean> dataBeanList = new ArrayList<>();
    DoughtListAdapter adapter;
    @BindView(R.id.count_down)
    CountDownView countDown;
    @BindView(R.id.ll_charge)
    LinearLayout llCharge;
    @BindView(R.id.tv_tips)
    TextView tv_tips;
    private int page = 1;
    private MyDoughnutBean myDoughnutBean;
    int h = 0;
    int d = 0;
    int s = 0;

    InterfaceRefreshLoadMore interfaceRefreshLoadMore;
    private String firstDataMonth = "";
    private boolean isNotify;
    private MyDoughnutBean.DataBeanX dataBeanX;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        StatusBarUtil.baseTransparentStatusBar(this);
        return R.layout.activity_my_doughnut_new;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        hideTitle(true);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.e(TAG, "onOffsetChanged: " + verticalOffset);
                if (verticalOffset == 0) {
                    isNotify = true;
                    layoutHead.setVisibility(View.VISIBLE);
                    if (dataBeanList != null && dataBeanList.size() > 0) {
                        if (isNotify) {
                            dataBeanList.get(0).setMonth("甜甜圈记录");
                            adapter.notifyItemChanged(0);
                        }
                    }
                } else {
                    if (dataBeanList != null && dataBeanList.size() > 0) {
                        dataBeanList.get(0).setMonth(firstDataMonth);
                        if (isNotify) {
                            isNotify = false;
                            adapter.notifyItemChanged(0);
                        }
                    }
                    if (Math.abs(verticalOffset) > layoutHead.getHeight() / 2) {
                        layoutHead.setVisibility(View.GONE);
                    }
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new StickyItemDecoration());
        if (mRecyclerView.getAdapter() == null) {
            adapter = new DoughtListAdapter(this, dataBeanList);
            mRecyclerView.setAdapter(adapter);
        }


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        initMemberData();
    }

    private void initMemberData() {
        HttpParams params = new HttpParams();
        params.put("p", String.valueOf(page));
        NetWork.INSTANCE.getMemberCoin(this, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                interfaceRefreshLoadMore.setStopRefreshing();
                myDoughnutBean = JSON.parseObject(resultData, MyDoughnutBean.class);
                dataBeanX = myDoughnutBean.getData();
                if (dataBeanX == null) return;
                doughtNum.setText(dataBeanX.getMy_coin_counts() + "");
                List<MyDoughnutBean.DataBeanX.DataBean> data = dataBeanX.getData();

                int discount_surplus_second = 1561737599;
                if (discount_surplus_second != 0) {
                    tvChargeNow.setVisibility(View.GONE);
                    llCharge.setVisibility(View.VISIBLE);
                    change(discount_surplus_second);
                    countDown.initTime(h, d, s);
                    countDown.start();
                    countDown.setOnTimeCompleteListener(new CountDownView.OnTimeCompleteListener() {
                        @Override
                        public void onTimeComplete() {
                            llCharge.setVisibility(View.GONE);
                            tvChargeNow.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    tvChargeNow.setVisibility(View.VISIBLE);
                    llCharge.setVisibility(View.GONE);
                }
                if (page == 1) {
                    dataBeanList.clear();
                }
                for (int i = 0; i < data.size(); i++) {
                    if (data.size() == 0) return;
                    firstDataMonth = data.get(0).getMonth();
                    MyDoughnutBean.DataBeanX.DataBean dataBean = data.get(i);
                    MyDoughnutBean.DataBeanX.DataBean.MonthDataBean monthDataBean = new MyDoughnutBean.DataBeanX.DataBean.MonthDataBean();
                    monthDataBean.setItemType(Constant.IncomeListItemType.TITLE);
                    monthDataBean.setMonth(dataBean.getMonth());
//                    monthDataBean.setMonth("甜甜圈记录");
                    if (i == 0) {
                        monthDataBean.setMonth("甜甜圈记录");
                    }
                    if (!dataBeanList.contains(monthDataBean)) {
                        dataBeanList.add(monthDataBean);
                    }
                    List<MyDoughnutBean.DataBeanX.DataBean.MonthDataBean> month_data = dataBean.getMonth_data();
                    if (month_data != null && month_data.size() > 0) {
                        for (int j = 0; j < month_data.size(); j++) {
                            MyDoughnutBean.DataBeanX.DataBean.MonthDataBean monthDataBeanIn = month_data.get(j);
                            monthDataBeanIn.setItemType(Constant.IncomeListItemType.CONTENT);
                            dataBeanList.add(monthDataBeanIn);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void doError(String msg) {
                interfaceRefreshLoadMore.setStopRefreshing();
                if (page > 1) {
                    page--;
                }
            }

            @Override
            public void doResult() {

            }
        });
        UserInfoUtil.getUserInfoById(0, this, new UserInfoUtil.UserCallBack() {
            @Override
            public void doNext(UserInfo infoBean) {
                if (infoBean != null && infoBean.getData() != null) {
                   nickName.setText(infoBean.getData().getNickname());
                }
            }
        });
    }

    @Override
    protected void initEvent() {
        interfaceRefreshLoadMore = new InterfaceRefreshLoadMore(superSwipeRefreshLayout, this, new InterfaceRefreshLoadMore.RefreshLoadMoreCallback() {
            @Override
            public void onRefresh() {
                page = 1;
                initMemberData();
            }

            @Override
            public void onLoadMore() {
                page++;
                initMemberData();

            }

            @Override
            public void onPushDistance(int distance) {

            }

            @Override
            public void onPullDistance(int distance) {

            }
        });

    }

    @OnClick({R.id.tv_charge_now, R.id.tv_tips, R.id.ll_charge, R.id.iv_back})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isShowFirstDialog",true);
        switch (view.getId()) {
            case R.id.tv_charge_now:
                goActivity(ChargeDoughnutActivity.class,bundle);
                break;
            case R.id.tv_tips:
                if (dataBeanX != null) {
                    BotomFishTips botomFishTips = new BotomFishTips(1, dataBeanX.getRemark(), dataBeanX.getHelp_url());
                    botomFishTips.show(getSupportFragmentManager(), "DOUGHT");
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_charge:
                goActivity(ChargeDoughnutActivity.class,bundle);
                break;
        }
    }


    public void change(int second) {
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
    }
}
