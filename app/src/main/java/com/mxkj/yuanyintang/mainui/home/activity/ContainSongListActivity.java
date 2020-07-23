package com.mxkj.yuanyintang.mainui.home.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.home.adapter.ContainSongListAdapter;
import com.mxkj.yuanyintang.mainui.home.bean.ContainSongBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

/**
 * Created by LiuJie on 2017/10/20.
 */

public class ContainSongListActivity extends StandardUiActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.noting_date)
    RelativeLayout noting_date;


    ContainSongListAdapter containSongListAdapter;
    List<ContainSongBean.DataBean> dataList = new ArrayList<>();

    public static final String MUSIC_ID = "_music_id";
    private String musicId;
    private int page = 1;

    @Override
    public int setLayoutId() {
        return R.layout.activity_contain_song_list;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        musicId = getIntent().getStringExtra(MUSIC_ID);
        setTitleText("包含这首歌的歌单");
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
        containSongListAdapter = new ContainSongListAdapter(dataList);
        recyclerView.setAdapter(containSongListAdapter);
        containSongListAdapter.setIsSongLisListener(new ContainSongListAdapter.IsSongLisListener() {
            @Override
            public void isSong(final int position) {
                NetWork.INSTANCE.getSongCollect(ContainSongListActivity.this, dataList.get(position).getId() + "", new NetWork.TokenCallBack() {

                    @Override
                    public void doNext(String resultData, Headers headers) {
                        if (dataList.get(position).getIs_song() == 0) {
                            dataList.get(position).setIs_song(1);
                        } else {
                            dataList.get(position).setIs_song(0);
                        }
                        if (null != containSongListAdapter) {
                            containSongListAdapter.notifyDataSetChanged();
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

        });
    }

    @Override
    protected void initData() {
        if (TextUtils.isEmpty(musicId)) {
            return;
        }
        NetWork.INSTANCE.getMusicSong(this, musicId, page, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                ContainSongBean containSongBean = JSON.parseObject(resultData, ContainSongBean.class);
                if (null != containSongBean) {
                    if(containSongBean.getData().size()<1){
                        noting_date.setVisibility(View.VISIBLE);
                    }else {
                        refreshData(containSongBean);
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
    }

    private void refreshData(ContainSongBean containSongBean) {
        if (null != containSongBean.getData()) {
            if (page == 1) {
                dataList.clear();
            }
            dataList.addAll(containSongBean.getData());
            containSongListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initEvent() {

    }
}
