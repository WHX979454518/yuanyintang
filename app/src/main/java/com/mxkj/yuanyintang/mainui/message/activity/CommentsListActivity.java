package com.mxkj.yuanyintang.mainui.message.activity;

import android.graphics.Color;
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
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration;
import com.mxkj.yuanyintang.mainui.message.adapter.CommentListAdapterNew;
import com.mxkj.yuanyintang.mainui.message.bean.MsgListean;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.app.CommonUtils;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.event.EMECodeEvent;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

public class CommentsListActivity extends StandardUiActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_head)
    TextView tv_head;
    @BindView(R.id.layout_not_data_view)
    LinearLayout layout_not_data_view;
    @BindView(R.id.tv_add_song)
    TextView tv_add_song;
    CommentListAdapterNew commentListAdapter;
    List<MsgListean.DataBeanX.DataBean> dataBeanList = new ArrayList<>();
    private int page = 1;
    InterfaceRefreshLoadMore interfaceRefreshLoadMore;
    @BindView(R.id.swipe_refresh)
    SuperSwipeRefreshLayout swipeRefresh;

    @Override
    public int setLayoutId() {
        return R.layout.activity_message_list;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("评论消息");
        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        finish();
                    }
                });
        tv_head.setText("会有人评论你的，别放在心上~");
        RxView.clicks(tv_add_song)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        RxBus.getDefault().post(new EMECodeEvent(997));
                        finish();
                    }
                });
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        recyclerview.setItemViewCacheSize(200);
        recyclerview.addItemDecoration(new MyRecyclerDetorration(this, LinearLayoutManager.VERTICAL, CommonUtils.INSTANCE.dip2px(this, 5), Color.parseColor("#F5F5F5"), true));
        commentListAdapter = new CommentListAdapterNew(dataBeanList);
        recyclerview.setAdapter(commentListAdapter);
    }

    @Override
    protected void initData() {
        initMsgData();
    }

    @Override
    protected void initEvent() {
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
        NetWork.INSTANCE.getMsgList(this, page, 3, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                if (interfaceRefreshLoadMore != null) {
                    interfaceRefreshLoadMore.setStopRefreshing();
                }
                try {
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
                            commentListAdapter.setNewData(dataBeanList);
                        }
                    }
                    isNotData();
                } catch (RuntimeException e) {
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
