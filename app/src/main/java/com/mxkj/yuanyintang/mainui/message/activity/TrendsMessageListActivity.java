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
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.message.adapter.TrendsMessageListAdapter;
import com.mxkj.yuanyintang.mainui.message.bean.SystemMessageListBean;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.event.EMECodeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

/**
 * 动态消息
 * Created by LiuJie on 2017/10/14.
 */

public class TrendsMessageListActivity extends StandardUiActivity {

    @BindView(R.id.layout_not_data_view)
    LinearLayout layout_not_data_view;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_head)
    TextView tv_head;
    @BindView(R.id.tv_add_song)
    TextView tv_add_song;
    TrendsMessageListAdapter trendsMessageListAdapter;

    List<SystemMessageListBean.DataBeanXX.DataBeanX> systemMessageListBeanList = new ArrayList<>();

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
        setTitleText("动态消息");
        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        finish();
                    }
                });

        tv_head.setText("还没有新的动态哦~");
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
        trendsMessageListAdapter = new TrendsMessageListAdapter(systemMessageListBeanList, this);
        recyclerview.setAdapter(trendsMessageListAdapter);
        trendsMessageListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    @Override
    protected void initData() {
        NetWork.INSTANCE.getMessage(this, "2", new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                SystemMessageListBean systemMessageBean = JSON.parseObject(resultData, SystemMessageListBean.class);
                refreshData(systemMessageBean);
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    private void refreshData(SystemMessageListBean systemMessageBean) {
        systemMessageListBeanList.clear();
        systemMessageListBeanList.addAll(systemMessageBean.getData().getData());
        trendsMessageListAdapter.notifyDataSetChanged();
        if (systemMessageListBeanList.size() > 0) {
            layout_not_data_view.setVisibility(View.GONE);
        } else {
            layout_not_data_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initEvent() {

    }
}
