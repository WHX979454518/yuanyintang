package com.mxkj.yuanyintang.mainui.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.home.adapter.LikesMusicAdapter;
import com.mxkj.yuanyintang.mainui.home.bean.LikeMusicDetailsBean;
import com.mxkj.yuanyintang.mainui.home.bean.LikesMusicBean;
import com.mxkj.yuanyintang.extraui.activity.PictureDetailsActivity;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

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

public class LikesMusicActivity extends StandardUiActivity {

    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView recyclerView;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_more)
    ImageView iv_more;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_music_name)
    TextView tv_music_name;
    @BindView(R.id.tv_player_num)
    TextView tv_player_num;
    @BindView(R.id.iv_music_cover)
    ImageView iv_music_cover;
    @BindView(R.id.iv_bg)
    ImageView iv_bg;
    @BindView(R.id.tv_song_list_label)
    TextView tv_song_list_label;
    int page = 1;
    String musicLink;

    LikesMusicAdapter likesMusicAdapter;
    private String musicianID;
    List<LikesMusicBean.DataBean> dataBeanList = new ArrayList<>();
    public static final String MUSICIAN_ID = "_musician_id";

    @Override
    public int setLayoutId() {
        return R.layout.activity_likes_music;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        hideTitle(true);
        musicianID = getIntent().getStringExtra(MUSICIAN_ID);
        RxView.clicks(iv_back)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        finish();
                    }
                });
        RxView.clicks(tv_title)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {

                    }
                });
        initRecyclerView();
        initHeadView();
    }

    private void initHeadView() {
        RxView.clicks(iv_music_cover)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(LikesMusicActivity.this, PictureDetailsActivity.class);
                        intent.putExtra("url", musicLink);
                        startActivity(intent);
                    }
                });

    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        likesMusicAdapter = new LikesMusicAdapter(dataBeanList, getSupportFragmentManager()) {
            @Override
            public int getItemViewType(int position) {
                return position;
            }
        };
        recyclerView.setAdapter(likesMusicAdapter);
        recyclerView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);
        recyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            }
        });
        getInfo();
    }

    private void getInfo() {
        if (TextUtils.isEmpty(musicianID)) {
            return;
        }
        NetWork.INSTANCE.getMusicIanInfo(this, musicianID, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                LikeMusicDetailsBean likeMusicDetails = JSON.parseObject(resultData, LikeMusicDetailsBean.class);
                if (null != likeMusicDetails) {
                    refreshData(likeMusicDetails);
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

    @Override
    protected void initData() {
        if (TextUtils.isEmpty(musicianID)) {
            return;
        }
        NetWork.INSTANCE.getMusicianMusicCollection(this, page, musicianID, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                LikesMusicBean likesMusicBean = JSON.parseObject(resultData, LikesMusicBean.class);
                if (likesMusicBean != null && likesMusicBean.getData() != null) {
                    refreshData(likesMusicBean);
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

    private void refreshData(LikeMusicDetailsBean likeMusicDetails) {
        ImageLoader.with(this)
                .getSize(800,400)
                .url(likeMusicDetails.getData().getHead_link())
                .scale(ScaleMode.CENTER_CROP)
                .blur(25)
                .into(iv_bg);
        ImageLoader.with(this)
                .getSize(400,400)

                .url(likeMusicDetails.getData().getHead_link())
                .scale(ScaleMode.CENTER_CROP)
                .into(iv_music_cover);
        musicLink = likeMusicDetails.getData().getHead_link();
        tv_music_name.setText(StringUtils.isEmpty(likeMusicDetails.getData().getNickname()) + "的相关歌曲");
        tv_player_num.setText(StringUtils.isEmpty(likeMusicDetails.getData().getCount().getPlaylogCount() + ""));
    }

    private void refreshData(LikesMusicBean likesMusicBean) {
        if (page == 1) {
            dataBeanList.clear();
        }
        dataBeanList.addAll(likesMusicBean.getData());
        tv_song_list_label.setText("歌曲列表（共" + dataBeanList.size() + "首）");
        likesMusicAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initEvent() {

    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dimen_80);
            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            {

            }
            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(LikesMusicActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(LikesMusicActivity.this, R.color.base_red))
                        .setText("  下一首") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setTextSize(12)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
            }
        }
    };


}
