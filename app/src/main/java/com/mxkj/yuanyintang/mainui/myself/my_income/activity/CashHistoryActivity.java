package com.mxkj.yuanyintang.mainui.myself.my_income.activity;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.myself.my_income.adapter.CashListAdapter;
import com.mxkj.yuanyintang.mainui.myself.my_income.bean.CashHistoryBean;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.VpSuperSwipeRefreshLayout;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;

public class CashHistoryActivity extends StandardUiActivity {
    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.recycler)
    SwipeMenuRecyclerView recyclerview;
    @BindView(R.id.superSwipeRefreshLayout)
    VpSuperSwipeRefreshLayout superSwipeRefreshLayout;
    @BindView(R.id.ll_nodata)
    LinearLayout ll_nodata;
    @BindView(R.id.back_nodata)
    TextView back_nodata;
    private List<CashHistoryBean.DataBean> dataList = new ArrayList<>();
    private CashListAdapter cashListAdapter;
    InterfaceRefreshLoadMore interfaceRefreshLoadMore;
    private int page = 1;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        StatusBarUtil.baseTransparentStatusBar(this);
        return R.layout.activity_cash_history;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        hideTitle(true);
        initSwipeRecyclerView();
        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        back_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dimen_56);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            SwipeMenuItem deleteItem = new SwipeMenuItem(CashHistoryActivity.this)
                    .setBackgroundColor(ContextCompat.getColor(CashHistoryActivity.this, R.color.base_red))
                    .setText("  取消")
                    .setTextColor(Color.WHITE)
                    .setTextSize(12)
                    .setWidth(width)
                    .setHeight(height);
            if (viewType == 0) {
                swipeRightMenu.addMenuItem(deleteItem);
            }
        }
    };

    private void initSwipeRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        cashListAdapter = new CashListAdapter(dataList);
        recyclerview.setAdapter(cashListAdapter);
        recyclerview.setSwipeMenuCreator(swipeMenuCreator);
        recyclerview.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                final int adapterPosition = menuBridge.getAdapterPosition();

                BaseConfirmDialog.Companion.newInstance().title("确定取消提现吗？").content("取消后，您的金额将返回至小鱼干").confirmText("确定").cancleText("取消").showDialog(CashHistoryActivity.this, new BaseConfirmDialog.onBtClick() {
                    @Override
                    public void onConfirm() {
                        HttpParams params = new HttpParams();
                        params.put("cash_log_id",dataList.get(adapterPosition).getId()+"");
                        NetWork.INSTANCE.cancelCash(CashHistoryActivity.this,params, new NetWork.TokenCallBack() {
                            @Override
                            public void doNext(String resultData, Headers headers) {
                               page=1;
                               initData();
                            }

                            @Override
                            public void doError(String msg) {

                            }

                            @Override
                            public void doResult() {

                            }
                        });

                    }

                    @Override
                    public void onCancle() {

                    }
                });



                    }
        });
    }


    @Override
    protected void initData() {
        HttpParams params = new HttpParams();
        params.put("p",page);
        NetWork.INSTANCE.getCashHistory(this, params,new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                interfaceRefreshLoadMore.setStopRefreshing();
                CashHistoryBean cashHistoryBean = JSON.parseObject(resultData, CashHistoryBean.class);
                List<CashHistoryBean.DataBean> data = cashHistoryBean.getData();
                if (data != null && data.size() > 0) {
                    if (page == 1) {
                        dataList.clear();
                    }
                    dataList.addAll(data);
                    cashListAdapter.setNewData(dataList);
                }else{
                    if (page==1){
                    ll_nodata.setVisibility(View.VISIBLE);
                }
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
