package com.mxkj.yuanyintang.mainui.message.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.message.adapter.BroadcastMsgListListAdapter;
import com.mxkj.yuanyintang.mainui.message.bean.MsgListean;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

public class BroadcastMsgListActivity extends StandardUiActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_head)
    TextView tv_head;
    @BindView(R.id.layout_not_data_view)
    LinearLayout layout_not_data_view;
    @BindView(R.id.tv_add_song)
    TextView tv_add_song;
    BroadcastMsgListListAdapter recyclerAdapter;
    List<MsgListean.DataBeanX.DataBean> dataBeanList = new ArrayList<>();
    private int page = 1;
    InterfaceRefreshLoadMore interfaceRefreshLoadMore;
    @BindView(R.id.swipe_refresh)
    SuperSwipeRefreshLayout swipeRefresh;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_broadcast_msg_list;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("公告消息");
    }

    @Override
    protected void initData() {
        initMsgData();
    }

    @Override
    protected void initEvent() {
        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        finish();
                    }
                });
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new BroadcastMsgListListAdapter(dataBeanList);
        recyclerview.setAdapter(recyclerAdapter);
        interfaceRefreshLoadMore = new InterfaceRefreshLoadMore(swipeRefresh, this, new InterfaceRefreshLoadMore.RefreshLoadMoreCallback() {
            @Override
            public void onRefresh() {
                page = 1;
                initMsgData();
            }

            @Override
            public void onLoadMore() {
                page++;
                initMsgData();
            }

            @Override
            public void onPushDistance(int distance) {

            }

            @Override
            public void onPullDistance(int distance) {

            }

        });

    }

    public void initMsgData() {
        NetWork.INSTANCE.getMsgList(this, page, 1, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                interfaceRefreshLoadMore.setStopRefreshing();
                MsgListean msgListean = JSON.parseObject(resultData, MsgListean.class);
                MsgListean.DataBeanX data = msgListean.getData();
                if (data != null) {
                    List<MsgListean.DataBeanX.DataBean> msgList = data.getData();
                    if (msgList != null && msgList.size() > 0) {
                        if (page == 1) {
                            dataBeanList.clear();
                            dataBeanList.addAll(msgList);
                        } else {
                            dataBeanList.addAll(msgList);
                        }
                        recyclerAdapter.setNewData(dataBeanList);
                    }
                    isNotData();
                }
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    private void isNotData() {
        if (dataBeanList.size() > 0) {
            layout_not_data_view.setVisibility(View.GONE);
        } else {
            layout_not_data_view.setVisibility(View.VISIBLE);
        }
    }

}
