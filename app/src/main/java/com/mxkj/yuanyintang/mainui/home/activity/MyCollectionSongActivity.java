package com.mxkj.yuanyintang.mainui.home.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.home.adapter.MyCollectionSongAdapter;
import com.mxkj.yuanyintang.mainui.home.bean.MyCollectionSongBean;
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder;
import com.mxkj.yuanyintang.utils.string.StringUtils;
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
 * Created by LiuJie on 2017/10/23.
 */

public class MyCollectionSongActivity extends StandardUiActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private int page = 1;
    private String uid;
    public static final String MUSIC_ID = "music_id";
    public static final String MUSICIAN_ID = "musician_id";
    private String music_id;
    MyCollectionSongAdapter myCollectionSongAdapter;
    List<MyCollectionSongBean.DataBean> dataBeanList;

    private DiaLogBuilder diaLogBuilder;

    @Override
    public int setLayoutId() {
        return R.layout.activity_my_collection_song;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        uid = getIntent().getStringExtra(MUSICIAN_ID);
        music_id = getIntent().getStringExtra(MUSIC_ID);

        ButterKnife.bind(this);
        setTitleText("TA收藏的歌单");
        setRightButtonText("新建歌单");
        setTitleTextColor(ContextCompat.getColor(this, R.color.base_red));
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
                        if (null != diaLogBuilder) {
                            diaLogBuilder.show();
                        }
                    }
                });

        initRecyclerView();
        initNewSongDialog();
        if (TextUtils.isEmpty(uid)) {
            setSnackBar("未找到ID", "", R.drawable.icon_fails);
            return;
        }
    }

    private void initRecyclerView() {
        dataBeanList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myCollectionSongAdapter = new MyCollectionSongAdapter(dataBeanList);
        recyclerView.setAdapter(myCollectionSongAdapter);
    }

    @Override
    protected void initData() {
        NetWork.INSTANCE.getMusicianSongCollection(this, page, StringUtils.isEmpty(uid), new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                MyCollectionSongBean myCollectionSongBean = JSON.parseObject(resultData, MyCollectionSongBean.class);
                if (null != myCollectionSongBean) {
                    refreshData(myCollectionSongBean);
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

    private void initNewSongDialog() {
        View view = View.inflate(this, R.layout.dialog_new_song, null);
        TextView tv_cancel = view.findViewById(R.id.tv_cancle);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        final EditText et_song_content = view.findViewById(R.id.et_song_content);
        diaLogBuilder = new DiaLogBuilder(this)
                .setContentView(view)
                .setFullScreen()
                .setGrvier(Gravity.CENTER)
                .setCanceledOnTouchOutside(true)
                .setAniMo(R.anim.popup_in);
        RxView.clicks(tv_confirm)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (TextUtils.isEmpty(et_song_content.getText().toString())) {
                            Toast.create(MyCollectionSongActivity.this).show("请输入歌单名字");
                            return;
                        }
                        newSong(et_song_content.getText().toString());
                        diaLogBuilder.setDismiss();
                    }
                });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diaLogBuilder.setDismiss();
            }
        });
    }

    private void refreshData(MyCollectionSongBean myCollectionSongBean) {
        if (page == 1) {
            dataBeanList.clear();
        }
        dataBeanList.addAll(myCollectionSongBean.getData());
        myCollectionSongAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initEvent() {

    }

    private void newSong(final String s) {
        NetWork.INSTANCE.getNewSong(this, s, "", StringUtils.isEmpty(music_id), new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                page = 1;
                initData();
//                Bundle bundle = new Bundle();
//                bundle.putString("title", s);
//                goActivity(EditSongActivity.class, bundle);
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

}
