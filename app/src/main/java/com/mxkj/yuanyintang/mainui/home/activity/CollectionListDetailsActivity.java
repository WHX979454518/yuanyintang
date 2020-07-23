package com.mxkj.yuanyintang.mainui.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.database.updatafilesql.TasksManager;
import com.mxkj.yuanyintang.mainui.home.adapter.MusicDetailsRecyclerViewAdapter;
import com.mxkj.yuanyintang.mainui.home.adapter.SongSheetMusicListAdapter;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.mainui.home.utils.FeedRootRecyclerView;
import com.mxkj.yuanyintang.mainui.message.activity.MyCollectionSongListActivity;
import com.mxkj.yuanyintang.musicplayer.play_control.GetMusicInfo;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayList;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.TitleOperationDialog;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber;
import com.mxkj.yuanyintang.utils.rxbus.event.PlayerMusicRefreshDataEvent;
import com.mxkj.yuanyintang.utils.rxbus.event.RefreshDataEvent;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;


/**
 * 歌单详情
 * Created by LiuJie on 2017/9/27.
 */
//TODO 获取他的收藏列表  接口
public class CollectionListDetailsActivity extends StandardUiActivity {
    public static final String SONG_SHEET_ID = "_id";
    MusicDetailsRecyclerViewAdapter detial_adapter;
    SongSheetMusicListAdapter songSheetMusicAdapter;
    String id;
    MusicIndex.ItemInfoListBean itemInfoBean;
    @BindView(R.id.imageview)
    ImageView imageview;
    @BindView(R.id.iv_music_cover)
    ImageView ivMusicCover;
    @BindView(R.id.layout_music_cover)
    RelativeLayout layoutMusicCover;
    @BindView(R.id.tv_music_name)
    TextView tvMusicName;
    @BindView(R.id.riv_head_img)
    ImageView rivHeadImg;
    @BindView(R.id.iv_is_vip)
    ImageView ivIsVip;
    @BindView(R.id.tv_music_nickname)
    TextView tvMusicNickname;
    @BindView(R.id.layout_musician)
    LinearLayout layoutMusician;
    @BindView(R.id.img_play)
    ImageView imgPlay;
    @BindView(R.id.layout_head)
    LinearLayout layoutHead;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.img_all_playing)
    ImageView imgAllPlaying;
    @BindView(R.id.tv_song_list_label)
    TextView tvSongListLabel;
    @BindView(R.id.ll_play_all)
    LinearLayout llPlayAll;
    @BindView(R.id.check_song)
    CheckBox checkSong;
    @BindView(R.id.tv_edit_more)
    TextView tvEditMore;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.bottom_recyclerview)
    FeedRootRecyclerView bottomRecyclerview;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.tv_add_song)
    TextView tvAddSong;
    @BindView(R.id.tv_add_musicList)
    TextView tvAddMusicList;
    @BindView(R.id.tv_cancle_collection)
    TextView tvCancleCollection;
    @BindView(R.id.tv_download_music)
    TextView tvDownloadMusic;
    @BindView(R.id.layout_footer)
    LinearLayout layoutFooter;


    private Disposable mPlayerMusicRefreshData;
    private Disposable mSongDetailsRefreshData;

    int playCounts = 0;
    private boolean isEdit = false;
    private int count;
    private boolean isClickable = false;
    private List<MusicIndex.ItemInfoListBean.MusicBean> musicBeanList = new ArrayList<>();
    private Handler handler;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    public int setLayoutId() {
        StatusBarUtil.baseTransparentStatusBar(this);
        return R.layout.activity_collect_song_sheet_details;
    }

    @Override
    protected void initView() {
        handler = new Handler();
        id = getIntent().getStringExtra(SONG_SHEET_ID);
        ButterKnife.bind(CollectionListDetailsActivity.this);
        hideTitle(true);
        initViewHeight();
        initRecyclerView();
    }

    private void refreshData() {
        detial_adapter.setSongName(itemInfoBean.getTitle());
        songSheetMusicAdapter = detial_adapter.getMusiListAdapter();
        if (songSheetMusicAdapter != null) {
            musicBeanList = songSheetMusicAdapter.getData();
        }
        if (songSheetMusicAdapter != null) {
            songSheetMusicAdapter.setCheckedSongListener(new SongSheetMusicListAdapter.ClickCheckedSongListener() {
                @Override
                public void onChecked(Boolean aBoolean, int position) {
                    if (musicBeanList.size() > position) {
                        musicBeanList.get(position).setSingle_selection(aBoolean);
                        songSheetMusicAdapter.notifyItemChanged(position);
                        countMusic();
                    }
                }

                @Override
                public void onRefreshData() {
                }
            });
        }
        if (itemInfoBean.getIs_music() == 3) {
            ivIsVip.setVisibility(View.VISIBLE);
        } else {
            ivIsVip.setVisibility(View.GONE);
        }

        tvTitle.setText(StringUtils.isEmpty(itemInfoBean.getTitle()));
        tvMusicName.setText(StringUtils.isEmpty(itemInfoBean.getTitle()));
        tvMusicNickname.setText(StringUtils.isEmpty(itemInfoBean.getNickname()));
        playCounts = itemInfoBean.getCounts();
        try {
            ImageLoader.with(this)
                    .override(CacheUtils.INSTANCE.getInt(CollectionListDetailsActivity.this, Constants.Other.WIDTH, 0), 250)
                    .url(itemInfoBean.getImgpic_info().getLink())
                    .scale(ScaleMode.CENTER_CROP)
                    .blur(25)
                    .into(imageview);
            ImageLoader.with(this)
                    .override(120, 120)
                    .url(itemInfoBean.getImgpic_info().getLink())
                    .scale(ScaleMode.CENTER_CROP)
                    .into(ivMusicCover);
            ImageLoader.with(this)
                    .override(40, 40)
                    .asCircle()
                    .url(itemInfoBean.getHead_link())
                    .scale(ScaleMode.CENTER_CROP)
                    .into(rivHeadImg);
        } catch (RuntimeException e) {
        }
        detial_adapter.notifyDataSetChanged();

    }

    private void initRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        bottomRecyclerview.setLayoutManager(layoutManager);
        detial_adapter = new MusicDetailsRecyclerViewAdapter(this, getSupportFragmentManager(), isEdit);
        bottomRecyclerview.setAdapter(detial_adapter);
//        bottom_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (detial_adapter.song_recyclerview!=null&&recyclerView.canScrollVertically(-1)&&detial_adapter.song_recyclerview.canScrollVertically(-1)) {
//                    appBar.setExpanded(true,true);
//                }
//            }
//        });
    }

    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    private void initViewHeight() {
        Drawable leftDrawable = getResources().getDrawable(R.drawable.music_detail_agree_false);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRangle = appBarLayout.getTotalScrollRange();
                toolbar.setBackgroundColor(changeAlpha(getResources().getColor(R.color.white), Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));
                if (verticalOffset == 0) {
                    tvTitle.setAlpha(0.0f);
                } else {
                    //保留一位小数
                    float alpha = Math.abs(Math.round(1.0f * verticalOffset / scrollRangle) * 10) / 10;
                    tvTitle.setAlpha(alpha);
//                    layout_head.setAlpha(1.0f - alpha);
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (TextUtils.isEmpty(id)) {
            return;
        }


    }

    @Override
    protected void initEvent() {
        mPlayerMusicRefreshData = RxBus.getDefault().toObservable(PlayerMusicRefreshDataEvent.class)
                .subscribeWith(new RxBusSubscriber<PlayerMusicRefreshDataEvent>() {
                    @Override
                    protected void onEvent(PlayerMusicRefreshDataEvent playerMusicRefreshDataEvent) throws Exception {
                        if (null != detial_adapter) {
                            detial_adapter.notifyData();
                        }
                        if (null != MediaService.bean && null != itemInfoBean) {
                            GetMusicInfo.INSTANCE.getMusicDetial(CollectionListDetailsActivity.this, MediaService.bean.getId(), new GetMusicInfo.SetBeanData() {
                                @Override
                                public void setBeanData(MusicInfo.DataBean dataBean) {
                                }
                            });
                        }
                    }
                });
        mSongDetailsRefreshData = RxBus.getDefault().toObservable(RefreshDataEvent.class)
                .subscribeWith(new RxBusSubscriber<RefreshDataEvent>() {
                    @Override
                    protected void onEvent(RefreshDataEvent refreshDataEvent) throws Exception {
                        if (refreshDataEvent.getCode() == 1) {//TODO，编辑成功后返回1刷新界面
                            initData();
                        }
                    }
                });
        RxBus.getDefault().add(mPlayerMusicRefreshData);
        RxBus.getDefault().add(mSongDetailsRefreshData);

        clickEvent();
    }

    private void clickEvent() {
        RxView.clicks(ivBack)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        backBtnEvent();
                    }
                });
        /**
         * 全选按钮
         * */
        RxView.clicks(checkSong)
                .throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        CheckAll();
                    }
                });
        RxView.clicks(tvEditMore)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isEdit = true;
                                checkView(false);
//                        隐藏除了歌曲列表外的其他布局
                                int itemCount = detial_adapter.getItemCount();
                                for (int i = 0; i < itemCount; i++) {
                                    MusicIndex.ItemInfoListBean item = detial_adapter.getItem(i);
                                    if (!item.itemType.equals("music")) {
                                        detial_adapter.remove(i);
                                    }
                                }
                            }
                        }, 100);

                    }
                });

        RxView.clicks(ivMore)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        rightBtnEvent();
                    }
                });
        RxView.clicks(layoutMusician)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (null != itemInfoBean) {
                            Bundle bundle = new Bundle();
                            bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, itemInfoBean.getUid() + "");
                            goActivity(MusicIanDetailsActivity.class, bundle);
                        }
                    }
                });
        RxView.clicks(ivMusicCover)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (null == itemInfoBean) {
                            return;
                        }

                        Intent intent = new Intent(CollectionListDetailsActivity.this, SongSheetSynopsisActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", itemInfoBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });


        /**
         * 添加到歌单
         * */
        RxView.clicks(tvAddSong)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        getMusicID("addSong");
                    }
                });
        /**
         * 添加到播放列表
         * */
        RxView.clicks(tvAddMusicList)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (!isClickable) {
                            setSnackBar("你还没有选择音乐", "", R.drawable.icon_good);
                            return;
                        }
                        Observable.fromArray(musicBeanList)
                                .flatMap(new Function<List<MusicIndex.ItemInfoListBean.MusicBean>, ObservableSource<MusicIndex.ItemInfoListBean.MusicBean>>() {
                                    @Override
                                    public ObservableSource<MusicIndex.ItemInfoListBean.MusicBean> apply(@NonNull List<MusicIndex.ItemInfoListBean.MusicBean> dataBeen) throws Exception {
                                        return Observable.fromIterable(dataBeen)
                                                .filter(new Predicate<MusicIndex.ItemInfoListBean.MusicBean>() {
                                                    @Override
                                                    public boolean test(@NonNull MusicIndex.ItemInfoListBean.MusicBean dataBeen) throws Exception {
                                                        return dataBeen.getSingle_selection();
                                                    }
                                                });
                                    }
                                })
                                .subscribe(new Observer<MusicIndex.ItemInfoListBean.MusicBean>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onNext(@NonNull MusicIndex.ItemInfoListBean.MusicBean data) {
                                        MusicInfo.DataBean dataBean = sheetBeanResultData(data, 0);
                                        PlayList.addToList(CollectionListDetailsActivity.this, dataBean);
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        setSnackBar("添加到播放列表成功", "", R.drawable.icon_success);
                                        if (!MediaService.getMediaPlayer().isPlaying()) {
                                            List<MusicInfo.DataBean> dataBeanList = PlayList.getList(CollectionListDetailsActivity.this);
                                            PlayCtrlUtil.INSTANCE.startServiceToPlay(CollectionListDetailsActivity.this, dataBeanList.get(dataBeanList.size() - 1));
                                        }
                                    }
                                });
                    }
                });

        /**
         * 下载
         * */
        RxView.clicks(tvDownloadMusic)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (!isClickable) {
                            setSnackBar("你还没有选择音乐", "", R.drawable.icon_good);
                            return;
                        }
                        Observable.fromArray(musicBeanList)
                                .flatMap(new Function<List<MusicIndex.ItemInfoListBean.MusicBean>, ObservableSource<MusicIndex.ItemInfoListBean.MusicBean>>() {
                                    @Override
                                    public ObservableSource<MusicIndex.ItemInfoListBean.MusicBean> apply(@NonNull List<MusicIndex.ItemInfoListBean.MusicBean> dataBeen) throws Exception {
                                        return Observable.fromIterable(dataBeen)
                                                .filter(new Predicate<MusicIndex.ItemInfoListBean.MusicBean>() {
                                                    @Override
                                                    public boolean test(@NonNull MusicIndex.ItemInfoListBean.MusicBean dataBeen) throws Exception {
                                                        return dataBeen.getSingle_selection();
                                                    }
                                                });
                                    }
                                })
                                .subscribe(new Observer<MusicIndex.ItemInfoListBean.MusicBean>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onNext(@NonNull final MusicIndex.ItemInfoListBean.MusicBean dataBean) {
                                        if (!dataBean.getSingle_selection()) {
                                            return;
                                        }
                                        String kbps = CacheUtils.INSTANCE.getString(CollectionListDetailsActivity.this, Constants.User.MUSIC_KBP, "128");
                                        NetWork.INSTANCE.getMusicDown(CollectionListDetailsActivity.this, dataBean.getId() + "", TextUtils.equals("128", kbps) ? "1" : "2", new NetWork.TokenCallBack() {

                                            @Override
                                            public void doNext(String resultData, Headers headers) {
                                                String data = null;
                                                try {
                                                    JSONObject object = new JSONObject(resultData);
                                                    data = object.optString("data");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                if (!TextUtils.isEmpty(data) && dataBean.getImgpic_info() != null) {
                                                    MusicBean musicBean = new MusicBean();
                                                    musicBean.setExt("." + StringUtils.parseSuffix(data));
                                                    musicBean.setCollection(1);
                                                    musicBean.setMusic_name(dataBean.getTitle());
                                                    String link = dataBean.getImgpic_info().getLink();
                                                    MusicBean.ImgpicInfoBean imgpicInfoBean = new MusicBean.ImgpicInfoBean();
                                                    imgpicInfoBean.setLink(link);
                                                    musicBean.setImgpic_info(imgpicInfoBean);

                                                    musicBean.setMusician_name(dataBean.getNickname());
                                                    musicBean.setExt("." + StringUtils.parseSuffix(data));
                                                    musicBean.setMusic_id(dataBean.getId() + "");
                                                    musicBean.setUid(dataBean.getUid());
                                                    musicBean.setSong_id(dataBean.getSong_id());
                                                    TasksManager.getImpl().downLoadFile(musicBean, data, CollectionListDetailsActivity.this);
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
                                    public void onError(@NonNull Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        setSnackBar("添加到下载列表成功", "", R.drawable.icon_success);
                                        if (null != checkSong) {
                                            checkSong.setChecked(false);
                                            isEdit = checkSong.isChecked();
                                            checkView(true);
                                        }
                                    }
                                });
                        getMusicID("download");
                    }
                });
    }

    private static MusicInfo.DataBean sheetBeanResultData(MusicIndex.ItemInfoListBean.MusicBean data, int song_id) {
        MusicInfo.DataBean dataBean = new MusicInfo.DataBean();
        dataBean.setVideo(data.getVideo());
        if (data.getVideo_info() != null) {
            MusicInfo.DataBean.VideoInfoBean videoInfoBean = new MusicInfo.DataBean.VideoInfoBean();
            videoInfoBean.setLink(data.getVideo_info().getLink());
            data.setVideo_info(videoInfoBean);
        }
        dataBean.setId(data.getId());
        dataBean.setSong_id(song_id);
        dataBean.setUid(data.getUid());
        dataBean.setTitle(data.getTitle());
        dataBean.setCollection(data.getCollection());
        dataBean.setImgpic(data.getImgpic());
        dataBean.setLyrics("");
        dataBean.setNickname(data.getNickname());
        dataBean.setPlaytime(data.getPlaytime());
        dataBean.setUp_time(0);
        try {
            dataBean.setOriginal(data.getImgpic_info().getLink());
        } catch (RuntimeException e) {
        }

        dataBean.setCounts(data.getCounts());
        dataBean.setComment(data.getComment());
        return dataBean;
    }

    private void getMusicID(final String type) {
        if (!isClickable) {
            setSnackBar("你还没有选择音乐", "", R.drawable.icon_good);
            return;
        }
        Observable.fromArray(musicBeanList)
                .flatMap(new Function<List<MusicIndex.ItemInfoListBean.MusicBean>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull List<MusicIndex.ItemInfoListBean.MusicBean> dataBeen) throws Exception {
                        return Observable.fromIterable(dataBeen)
                                .filter(new Predicate<MusicIndex.ItemInfoListBean.MusicBean>() {
                                    @Override
                                    public boolean test(@NonNull MusicIndex.ItemInfoListBean.MusicBean dataBean) throws Exception {
                                        return dataBean.getSingle_selection();
                                    }
                                })
                                .map(new Function<MusicIndex.ItemInfoListBean.MusicBean, String>() {
                                    @Override
                                    public String apply(@NonNull MusicIndex.ItemInfoListBean.MusicBean dataBean) throws Exception {
                                        return dataBean.getId() + "";
                                    }
                                });
                    }
                })
                .reduce(new BiFunction<String, String, String>() {
                    @Override
                    public String apply(@NonNull String s, @NonNull String s2) throws Exception {
                        return s + "," + s2;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        switch (type) {
                            case "cancel":
//                                deleFromSongSheet(s);
                                break;
                            case "addSong":
                                Bundle bundle = new Bundle();
                                bundle.putString(MyCollectionSongListActivity.MUSIC_ID, s);
                                bundle.putString(MyCollectionSongListActivity.VIEW_TYPE, "collectionAddSong");
                                bundle.putBoolean(MyCollectionSongListActivity.IS_MULTI_SELECT, false);
                                goActivity(MyCollectionSongListActivity.class, bundle);
                                break;
                        }

                    }
                });
    }

    private void rightBtnEvent() {
        if (null == itemInfoBean) {
            return;
        }
        MusicBean.ShareDataBean shareDataBean = new MusicBean.ShareDataBean();
        shareDataBean.setType("album");
        shareDataBean.setNickname(itemInfoBean.getNickname());
        shareDataBean.setTopicContent(itemInfoBean.getNickname());
        shareDataBean.setTitle(itemInfoBean.getTitle() + "");
        shareDataBean.setMuisic_id(itemInfoBean.getId());
        try {
            shareDataBean.setWebImgUrl(itemInfoBean.getImgpic_info().getLink());
        } catch (RuntimeException e) {
        }

        String shareUrl = "https://www.yuanyintang.com/mlist/" + itemInfoBean.getId();//第三方分享的歌单链接
        shareDataBean.setShareUrl(shareUrl);
        MusicBean musicBean = new MusicBean()
                .setMusic_id(itemInfoBean.getId() + "")
                .setMusician_name(itemInfoBean.getNickname())
                .setMusic_name(itemInfoBean.getTitle())
                .setSongName(itemInfoBean.getTitle())
                .setUid(itemInfoBean.getUid())
                .setCommentNum(itemInfoBean.getComment())
                .setImgpic(itemInfoBean.getImgpic())
                .setSignature(itemInfoBean.getSignature())
                .setSong_id(itemInfoBean.getId())
                .setReportType(2)
                .setReportId(itemInfoBean.getId())
                .setShareDataBean(shareDataBean)//歌单分享bean
                .setType(2);
        try {
            String link = itemInfoBean.getImgpic_info().getLink();
            MusicBean.ImgpicInfoBean imgpicInfoBean = new MusicBean.ImgpicInfoBean();
            imgpicInfoBean.setLink(link);
            musicBean.setImgpic_info(imgpicInfoBean);

        } catch (RuntimeException e) {
        }
        TitleOperationDialog titleOperationDialog = new TitleOperationDialog(musicBean);
        titleOperationDialog.show(getSupportFragmentManager(), "mTitleOperationDialog");

    }

    private void checkView(boolean expanded) {
        if (isEdit == true) {
            llPlayAll.setVisibility(View.GONE);
            tvEditMore.setVisibility(View.GONE);
            checkSong.setVisibility(View.VISIBLE);
            layoutFooter.setVisibility(View.VISIBLE);
            checkSong.setText("全选(" + count + "/" + musicBeanList.size() + ")");
        } else {
            llPlayAll.setVisibility(View.VISIBLE);
            tvEditMore.setVisibility(View.VISIBLE);
            checkSong.setVisibility(View.GONE);
            layoutFooter.setVisibility(View.GONE);
        }
        appBar.setExpanded(expanded, true);
        if (musicBeanList == null) {
            return;
        }
        Observable.just(musicBeanList)
                .flatMap(new Function<List<MusicIndex.ItemInfoListBean.MusicBean>, ObservableSource<MusicIndex.ItemInfoListBean.MusicBean>>() {
                    @Override
                    public ObservableSource<MusicIndex.ItemInfoListBean.MusicBean> apply(@NonNull List<MusicIndex.ItemInfoListBean.MusicBean> dataBeen) throws Exception {
                        return Observable.fromIterable(dataBeen);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MusicIndex.ItemInfoListBean.MusicBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull MusicIndex.ItemInfoListBean.MusicBean dataBean) {
                        dataBean.setCheck(isEdit);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        notifyDataAdapter(isEdit);
                    }
                });
    }

    /**
     * 循环计数
     */
    private void countMusic() {
        Observable.fromArray(musicBeanList)
                .flatMap(new Function<List<MusicIndex.ItemInfoListBean.MusicBean>, ObservableSource<MusicIndex.ItemInfoListBean.MusicBean>>() {
                    @Override
                    public ObservableSource<MusicIndex.ItemInfoListBean.MusicBean> apply(@NonNull List<MusicIndex.ItemInfoListBean.MusicBean> dataBeen) throws Exception {
                        return Observable.fromIterable(dataBeen)
                                .filter(new Predicate<MusicIndex.ItemInfoListBean.MusicBean>() {
                                    @Override
                                    public boolean test(@NonNull MusicIndex.ItemInfoListBean.MusicBean dataBean) throws Exception {
                                        return dataBean.getSingle_selection();
                                    }
                                });
                    }
                })
                .subscribe(new Observer<MusicIndex.ItemInfoListBean.MusicBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        count = 0;

                    }

                    @Override
                    public void onNext(@NonNull MusicIndex.ItemInfoListBean.MusicBean dataBean) {
                        Log.d(TAG, "onNext: ");
                        count++;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        checkSong.setText("全选(" + count + "/" + musicBeanList.size() + ")");
                        checkSelectClickView(count);
                        if (musicBeanList.size() == 0) {
                            checkSong.setChecked(false);
                            return;
                        }
                        if (count == musicBeanList.size()) {
                            checkSong.setChecked(true);
                        } else {
                            checkSong.setChecked(false);
                        }
                    }
                });
    }

    private void checkSelectClickView(int count) {
        if (count > 0) {
            tvCancleCollection.setTextColor(ContextCompat.getColor(this, R.color.color_17_text));
            tvAddSong.setTextColor(ContextCompat.getColor(this, R.color.color_17_text));
            tvAddMusicList.setTextColor(ContextCompat.getColor(this, R.color.color_17_text));
            tvDownloadMusic.setTextColor(ContextCompat.getColor(this, R.color.color_17_text));
            isClickable = true;
        } else {
            tvCancleCollection.setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text));
            tvAddSong.setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text));
            tvAddMusicList.setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text));
            tvDownloadMusic.setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text));
            isClickable = false;
        }
    }

    private void CheckAll() {
        Observable.fromArray(musicBeanList)
                .flatMap(new Function<List<MusicIndex.ItemInfoListBean.MusicBean>, ObservableSource<MusicIndex.ItemInfoListBean.MusicBean>>() {
                    @Override
                    public ObservableSource<MusicIndex.ItemInfoListBean.MusicBean> apply(@NonNull List<MusicIndex.ItemInfoListBean.MusicBean> dataBeen) throws Exception {
                        return Observable.fromIterable(dataBeen);
                    }
                })
                .subscribe(new Observer<MusicIndex.ItemInfoListBean.MusicBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull MusicIndex.ItemInfoListBean.MusicBean dataBean) {
                        dataBean.setSingle_selection(checkSong.isChecked());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        notifyDataAdapter(isEdit);
                        countMusic();
                    }
                });
    }

    private void notifyDataAdapter(boolean isEdit) {
        if (null == songSheetMusicAdapter) {
            return;
        }
        songSheetMusicAdapter.notifyDataSetChanged();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (detial_adapter.getSong_recyclerview() == null) {
                    return;
                }
                detial_adapter.getSong_recyclerview().scrollToPosition(0);
            }
        }, 100);
    }

    @Override
    public void onBackPressed() {
        backBtnEvent();
    }

    private void backBtnEvent() {
        if (isEdit) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isEdit = false;
                    detial_adapter.setSongSheetMusicAdapter(null);
                    initData();
                    checkView(true);
                }
            }, 100);
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        itemInfoBean = null;
        if (null != detial_adapter.getMultilineGroup()) {
            detial_adapter.getMultilineGroup().removeAllViews();
            bottomRecyclerview.removeAllViews();
            detial_adapter.getData().clear();
            detial_adapter.notifyDataSetChanged();
            bottomRecyclerview = null;
        }
        RxBus.getDefault().remove(mPlayerMusicRefreshData);
        RxBus.getDefault().remove(mSongDetailsRefreshData);
    }

}