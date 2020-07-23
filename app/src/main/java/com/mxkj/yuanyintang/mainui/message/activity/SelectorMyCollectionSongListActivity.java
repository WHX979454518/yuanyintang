package com.mxkj.yuanyintang.mainui.message.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.im.ui.EaseChatFragment;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.message.adapter.MyCollectionSongListAdapter;
import com.mxkj.yuanyintang.mainui.myself.bean.MySongListBean;
import com.mxkj.yuanyintang.utils.uiutils.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

/**
 * Created by LiuJie on 2017/10/16.
 */

public class SelectorMyCollectionSongListActivity extends StandardUiActivity {
    public static final String IS_MULTI_SELECT = "_is_multi_select";//是否多选
    public static final String IS_NEW_MUSIC = "_is_new_music";//是否有新建歌单

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.layout_new_song)
    View layout_new_song;
    private int page = 1;
    private Boolean isMultiSelect = false;
    MyCollectionSongListAdapter myColloectionSongListAdapter;
    List<MySongListBean.DataBeanX.DataBean> dataBeanList = new ArrayList<>();

    @Override
    public int setLayoutId() {
        return R.layout.activity_my_collection_song_list;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        isMultiSelect = getIntent().getBooleanExtra(IS_MULTI_SELECT, false);
        Boolean isNewMusic = getIntent().getBooleanExtra(IS_NEW_MUSIC, false);
        setTitleText("选择歌单");
        setLeftButtonImageView(ContextCompat.getDrawable(this, R.drawable.login_close));
        setRightButtonText("确定");
        getNavigationBar().getRightButton().setTextColor(ContextCompat.getColor(this, R.color.base_red));
        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        finish();
                    }
                });
        RxView.clicks(getNavigationBar().getRightButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MySongListBean.DataBeanX.DataBean dataBean = null;
                        for (int i = 0; i < dataBeanList.size(); i++) {
                            if (dataBeanList.get(i).getCheck()) {
                                dataBean = dataBeanList.get(i);
                                break;
                            }
                        }
                        if (null != dataBean&&null!=dataBean.getImgpic_info()) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString(EaseChatFragment._ID, dataBean.getId() + "");
                            bundle.putString(EaseChatFragment.TITLE, dataBean.getTitle());
                            bundle.putString(EaseChatFragment.NICKNAME, dataBean.getNickname());
                            bundle.putString(EaseChatFragment.IMGPIC_LINK, dataBean.getImgpic_info().getLink());
                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.create(SelectorMyCollectionSongListActivity.this).show("请选择歌单");
                        }
                    }
                });
        if (isNewMusic) {
            layout_new_song.setVisibility(View.GONE);
        }
        RxView.clicks(layout_new_song)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {

                    }
                });
        initRecylcerView();
    }

    private void initRecylcerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        myColloectionSongListAdapter = new MyCollectionSongListAdapter(dataBeanList, isMultiSelect, "");
        recyclerview.setAdapter(myColloectionSongListAdapter);
        myColloectionSongListAdapter.setCheckedSongListener(new MyCollectionSongListAdapter.ClickCheckedSongListener() {
            @Override
            public void onChecked(int position) {
                for (int i = 0; i < dataBeanList.size(); i++) {
                    if (i == position) {
                        dataBeanList.get(i).setCheck(true);
                    } else {
                        dataBeanList.get(i).setCheck(false);
                    }
                }
                myColloectionSongListAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void initData() {
        NetWork.INSTANCE.getMemberScollection(this, page, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                MySongListBean mySongListBean = JSON.parseObject(resultData, MySongListBean.class);
                refreshData(mySongListBean);
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    private void refreshData(MySongListBean mySongListBean) {
        if (page == 1) {
            dataBeanList.clear();
        }
        dataBeanList.addAll(mySongListBean.getData().getData());
        myColloectionSongListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initEvent() {

    }
}
