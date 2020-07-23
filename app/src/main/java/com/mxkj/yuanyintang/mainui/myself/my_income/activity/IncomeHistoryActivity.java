package com.mxkj.yuanyintang.mainui.myself.my_income.activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.home.data.Constant;
import com.mxkj.yuanyintang.mainui.myself.my_income.adapter.IncomeListAdapter;
import com.mxkj.yuanyintang.mainui.myself.my_income.bean.IncomeListBean;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.extraui.view.sticky.StickyItemDecoration;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.VpSuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;
public class IncomeHistoryActivity extends StandardUiActivity {
    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.superSwipeRefreshLayout)
    VpSuperSwipeRefreshLayout superSwipeRefreshLayout;
    @BindView(R.id.ll_no_data_view)
    LinearLayout llNoDataView;
    @BindView(R.id.back_nodata)
    TextView backNodata;
    @BindView(R.id.ll_nodata)
    LinearLayout llNodata;
    private List<IncomeListBean.DataBean.MonthDataBean> dataList = new ArrayList<>();
    private IncomeListAdapter incomeListAdapter;
    InterfaceRefreshLoadMore interfaceRefreshLoadMore;
    private int page = 1;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        StatusBarUtil.baseTransparentStatusBar(this);
        return R.layout.activity_income_history;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        hideTitle(true);
        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backNodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new StickyItemDecoration());
        if (mRecyclerView.getAdapter() == null) {
            incomeListAdapter = new IncomeListAdapter(this, dataList);
            RecyclerView.RecycledViewPool pool = mRecyclerView.getRecycledViewPool();
            pool.setMaxRecycledViews(Constant.IncomeListItemType.CONTENT, 20);
            mRecyclerView.setRecycledViewPool(pool);
            mRecyclerView.setAdapter(incomeListAdapter);
        }
    }

    @Override
    protected void initData() {
        HttpParams params = new HttpParams();
        params.put("p", page + "");
        NetWork.INSTANCE.getIncomehistoryList(this, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                interfaceRefreshLoadMore.setStopRefreshing();
                IncomeListBean incomeListBean = JSON.parseObject(resultData, IncomeListBean.class);
                List<IncomeListBean.DataBean> data = incomeListBean.getData();
                if (data == null || data.size() == 0) {
                    if (page == 1) {
                        llNodata.setVisibility(View.VISIBLE);
                    }
                    return;
                } else {
                    if (page == 1) {
                        dataList.clear();
                    }
                }

                for (int i = 0; i < data.size(); i++) {
                    IncomeListBean.DataBean dataBean = data.get(i);
                    // 如果下一页新数据第一条和上一页最后一条月份一样，就不加title
                    if (dataList != null && dataList.size() > 0) {
                        IncomeListBean.DataBean.MonthDataBean lastBean = dataList.get(dataList.size() - 1);
                        String month = lastBean.getMonth();
                        if (!month.equals(dataBean.getMonth())) {
                            IncomeListBean.DataBean.MonthDataBean monthDataBean = new IncomeListBean.DataBean.MonthDataBean();
                            monthDataBean.setMonth_total(dataBean.getMonth_total());
                            monthDataBean.setMonth(dataBean.getMonth());
                            monthDataBean.setItemType(Constant.IncomeListItemType.TITLE);
                            if (!dataList.contains(monthDataBean)) {
                                dataList.add(monthDataBean);
                            }
                        }

                    } else {
                        IncomeListBean.DataBean.MonthDataBean monthDataBean = new IncomeListBean.DataBean.MonthDataBean();
                        monthDataBean.setMonth_total(dataBean.getMonth_total());
                        monthDataBean.setMonth(dataBean.getMonth());
                        monthDataBean.setItemType(Constant.IncomeListItemType.TITLE);
                        if (!dataList.contains(monthDataBean)) {
                            dataList.add(monthDataBean);
                        }
                    }
                    List<IncomeListBean.DataBean.MonthDataBean> month_data = dataBean.getMonth_data();
                    if (month_data != null && month_data.size() > 0) {
                        for (int j = 0; j < month_data.size(); j++) {
                            IncomeListBean.DataBean.MonthDataBean monthDataBeanIn = month_data.get(j);
                            monthDataBeanIn.setItemType(Constant.IncomeListItemType.CONTENT);
                            dataList.add(monthDataBeanIn);
                        }
                    }
                }

                if (mRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE || (mRecyclerView.isComputingLayout() == false)) {
                    incomeListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void doError(String msg) {
                interfaceRefreshLoadMore.setStopRefreshing();
            }

            @Override
            public void doResult() {
            }
        });
    }

    @Override
    protected void initEvent() {
        interfaceRefreshLoadMore = new InterfaceRefreshLoadMore(superSwipeRefreshLayout, this, new InterfaceRefreshLoadMore.RefreshLoadMoreCallback() {
            @Override
            public void onRefresh() {
                page = 1;
                initData();
            }

            @Override
            public void onLoadMore() {
                page++;
                initData();
            }

            @Override
            public void onPushDistance(int distance) {

            }

            @Override
            public void onPullDistance(int distance) {
            }
        });
    }

}
