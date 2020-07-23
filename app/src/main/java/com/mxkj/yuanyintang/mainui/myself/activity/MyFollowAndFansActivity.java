package com.mxkj.yuanyintang.mainui.myself.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.home.activity.MusicListActivity;
import com.mxkj.yuanyintang.mainui.myself.apdater.MyFollowAndFansAdapter;
import com.mxkj.yuanyintang.mainui.myself.bean.MyFollowAndFansBean;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.event.SelectTabChangeEvent;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.home.activity.MusicListActivity.MUSIC_LIST_CATEGORY;
import static com.mxkj.yuanyintang.mainui.home.activity.MusicListActivity.MUSIC_LIST_TITLE;

/**
 * Created by LiuJie on 2017/10/9.
 */

public class MyFollowAndFansActivity extends StandardUiActivity {
    public static final String TITLE_NAME = "_title";
    public static final String TYPE = "_type";
    public static final String NOT_DATA_TEXT = "not_data_text";
    public static final String TO_CLICK_TEXT = "to_click_text";
    public static final String COUNT = "_count";

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.tv_to_click)
    TextView tv_to_click;
    @BindView(R.id.layout_not_data)
    LinearLayout layout_not_data;
    @BindView(R.id.swipeMenuRecyclerView)
    SuperSwipeRefreshLayout superSwipeRefreshLayout;

    private MyFollowAndFansAdapter myFollowAndFansAdapter;
    List<MyFollowAndFansBean.DataBean> myFollowAndFansBeanList = new ArrayList<>();
    String type;
    int page = 1;
    private String titleName;

    InterfaceRefreshLoadMore interfaceRefreshLoadMore;

    @Override
    public int setLayoutId() {
        return R.layout.activity_my_follow;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(MyFollowAndFansActivity.this);
        titleName = getIntent().getStringExtra(TITLE_NAME);
        type = getIntent().getStringExtra(TYPE);
        String notDataText = getIntent().getStringExtra(NOT_DATA_TEXT);
        String toClickText = getIntent().getStringExtra(TO_CLICK_TEXT);
        int count = getIntent().getIntExtra(COUNT, 0);
        setTitleText(titleName + "（" + count + "）");
        tv_no_data.setText(StringUtils.isEmpty(notDataText));
        tv_to_click.setText(StringUtils.isEmpty(toClickText));
        RxView.clicks(tv_to_click)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (TextUtils.equals("follow", type)) {
                            Bundle bundle = new Bundle();
                            bundle.putString(MUSIC_LIST_TITLE, "音乐人列表");
                            bundle.putInt(MUSIC_LIST_CATEGORY, 0);
                            bundle.putInt(MusicListActivity.MUSIC_GET_TYPE, 2);
                            goActivity(MusicListActivity.class, bundle);
                        } else if (TextUtils.equals("fans", type)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    RxBus.getDefault().post(new SelectTabChangeEvent(3));
                                }
                            }, 500);
                        }
                        finish();
                    }
                });
        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        finish();
                    }
                });
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        myFollowAndFansAdapter = new MyFollowAndFansAdapter(myFollowAndFansBeanList);
        recyclerview.setAdapter(myFollowAndFansAdapter);
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

    @Override
    protected void initData() {
        if (TextUtils.equals("follow", type)) {
            NetWork.INSTANCE.getMemberAttention(this, page, new NetWork.TokenCallBack() {
                @Override
                public void doNext(String resultData, Headers headers) {
                    if (headers!=null){
                        String s = headers.get("X-Pagination-Total-Count");
                        setTitleText(titleName + "（" + s + "）");
                    }
                    MyFollowAndFansBean myFollowAndFansBean = JSON.parseObject(resultData, MyFollowAndFansBean.class);
                    refreshData(myFollowAndFansBean);
                }

                @Override
                public void doError(String msg) {
                    if (page > 1) {
                        page--;
                    }
                }

                @Override
                public void doResult() {

                }
            });
        } else if (TextUtils.equals("fans", type)) {
            NetWork.INSTANCE.getMemberFans(this, page, new NetWork.TokenCallBack() {
                @Override
                public void doNext(String resultData, Headers headers) {
                    if (headers!=null){
                        String s = headers.get("X-Pagination-Total-Count");
                        setTitleText(titleName + "（" + s + "）");
                    }
                    MyFollowAndFansBean myFollowAndFansBean = JSON.parseObject(resultData, MyFollowAndFansBean.class);
                    refreshData(myFollowAndFansBean);
                }

                @Override
                public void doError(String msg) {
                    if (page > 1) {
                        page--;
                    }
                    interfaceRefreshLoadMore.setStopRefreshing();
                }

                @Override
                public void doResult() {

                }
            });
        }
    }

    private void refreshData(MyFollowAndFansBean myFollowAndFansBean) {
        if (page == 1) {
            if (myFollowAndFansBean.getData().size() > 0) {
                layout_not_data.setVisibility(View.GONE);
            }
            myFollowAndFansBeanList.clear();
        }
        myFollowAndFansBeanList.addAll(myFollowAndFansBean.getData());
        myFollowAndFansAdapter.notifyDataSetChanged();
        interfaceRefreshLoadMore.setStopRefreshing();
    }

    @Override
    protected void initEvent() {

    }
}
