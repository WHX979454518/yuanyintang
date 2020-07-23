package com.mxkj.yuanyintang.mainui.home.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.home.adapter.ListeningToSongPeopleAdapter;
import com.mxkj.yuanyintang.mainui.home.bean.ListeningToSongPeopleBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

public class ListeningToSongPeopleActivity extends StandardUiActivity {
    public static final String MUSIC_ID = "_music_id";
    public static final String MUSIC_DURATION = "_music_duration";
    private int music_id;
    private int duration;
    private int page = 1;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    ListeningToSongPeopleAdapter listeningToSongPeopleAdapter;
    List<ListeningToSongPeopleBean.DataBean> dataBeanList = new ArrayList<>();

    @Override
    public int setLayoutId() {
        return R.layout.activity_listening_to_song_people;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        music_id = getIntent().getIntExtra(MUSIC_ID, -1);
        duration = getIntent().getIntExtra(MUSIC_DURATION, -1);
        setTitleText("正在听歌的人");
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listeningToSongPeopleAdapter = new ListeningToSongPeopleAdapter(dataBeanList);
        recyclerView.setAdapter(listeningToSongPeopleAdapter);
    }

    @Override
    protected void initData() {
        NetWork.INSTANCE.getMusicRelated(this, music_id, duration, page, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                ListeningToSongPeopleBean listeningToSongPeopleBean = JSON.parseObject(resultData, ListeningToSongPeopleBean.class);
                if (null != listeningToSongPeopleBean && listeningToSongPeopleBean.getData().size() > 0) {
                    refreshData(listeningToSongPeopleBean);
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

    private void refreshData(ListeningToSongPeopleBean listeningToSongPeopleBean) {
        if (1 == page) {
            dataBeanList.clear();
        }
        dataBeanList.addAll(listeningToSongPeopleBean.getData());
        if (null != listeningToSongPeopleAdapter) {
            listeningToSongPeopleAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void initEvent() {

    }
}
