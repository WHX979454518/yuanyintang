package com.mxkj.yuanyintang.mainui.myself.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.database.updatafilesql.TasksManager;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayList;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.message.activity.MyCollectionSongListActivity;
import com.mxkj.yuanyintang.mainui.myself.apdater.MyCollectionAdapter;
import com.mxkj.yuanyintang.mainui.myself.bean.MyCollectionBean;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber;
import com.mxkj.yuanyintang.utils.rxbus.event.MyCollectionRefreshEvent;
import com.mxkj.yuanyintang.utils.rxbus.event.SelectTabChangeEvent;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

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
 * Created by LiuJie on 2017/10/9.
 */

public class MyCollectionActivity extends StandardUiActivity {

    @BindView(R.id.tv_batch_operation)
    TextView tv_batch_operation;
    @BindView(R.id.recyclerview)
    SwipeMenuRecyclerView recyclerview;
    @BindView(R.id.layout_head)
    RelativeLayout layout_head;
    @BindView(R.id.check_song)
    CheckBox check_song;
    @BindView(R.id.tv_select_song_num)
    TextView tv_select_song_num;
    @BindView(R.id.tv_all_playing)
    RelativeLayout tv_all_playing;
    @BindView(R.id.play)
    ImageView play;
    @BindView(R.id.tv_all_playing_tv)
    TextView tv_all_playing_tv;
    @BindView(R.id.layout_footer)
    LinearLayout layout_footer;
    @BindView(R.id.tv_cancle_collection)
    TextView tv_cancle_collection;
    @BindView(R.id.layout_not_data)
    LinearLayout layout_not_data;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.tv_to_click)
    TextView tv_to_click;
    @BindView(R.id.tv_add_song)
    TextView tv_add_song;
    @BindView(R.id.tv_download_music)
    TextView tv_download_music;
    @BindView(R.id.tv_add_musicList)
    TextView tv_add_musicList;
    @BindView(R.id.superSwipeRefreshLayout)
    SuperSwipeRefreshLayout superSwipeRefreshLayout;

    MyCollectionAdapter myCollectionAdapter;
    List<MyCollectionBean.DataBean> dataBeanList = new ArrayList<>();

    int page = 1;
    private Boolean isAllCheck = false;
    private Boolean isClickable = false;

    private Disposable mRefreshEvent;
    InterfaceRefreshLoadMore interfaceRefreshLoadMore;


    private boolean isPlaying;
    Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {//接收到完整的包裹
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
//                    Bundle bundle = msg.getData();
//                    int po = bundle.getInt("position");
//                    TextView tv_music_name= (TextView) recyclerview.getChildAt(po).findViewById(R.id.tv_music_name);
//                    TextView tv_music_nickname= (TextView) recyclerview.getChildAt(po).findViewById(R.id.tv_music_nickname);
//                    tv_music_name.setTextColor(Color.parseColor("#ff6699"));
//                    tv_music_nickname.setTextColor(Color.parseColor("#ff6699"));
                    initData();
                    myCollectionAdapter = new MyCollectionAdapter(dataBeanList, getSupportFragmentManager(),myhandler) {
                        @Override
                        public int getItemViewType(int position) {
                            return position;
                        }
                    };
                    recyclerview.setAdapter(myCollectionAdapter);
                    break;
                case 2:
                    break;
            }


        }
    };

    @Override
    public int setLayoutId() {
        return R.layout.activity_my_collection;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("我喜欢的");
        tv_no_data.setText("您还没有收藏过歌哦~");
        tv_to_click.setText("去首页看看");
        /**
         * 批量编辑
         * */
        RxView.clicks(tv_batch_operation)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        isAllCheck = true;
                        checkView();
                    }
                });

        /**
         * 返回键
         * */
        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (isAllCheck) {
                            isAllCheck = false;
                            checkView();
                        } else {
                            finish();
                        }
                    }
                });

        /**
         * 全选按钮
         * */
        RxView.clicks(check_song)
                .throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        singleCheck();
                    }
                });

        /**
         * 取消收藏
         * */
        RxView.clicks(tv_cancle_collection)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MaterialDialog("是否取消选中收藏？");
                    }
                });

        RxView.clicks(tv_to_click)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        finish();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                RxBus.getDefault().post(new SelectTabChangeEvent(0));
                            }
                        }, 500);
                    }
                });

        /**
         * 添加到歌单
         * */
        RxView.clicks(tv_add_song)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        getMusicID("addSong");
                    }
                });

        /**
         * 全部播放
         * */
        RxView.clicks(tv_all_playing)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (dataBeanList.size() <= 0) {
                            return;
                        }
//                        if (MediaService.mediaPlayer != null && MediaService.mediaPlayer.isPlaying()) {
//                            sendBroadcast(new Intent(MediaService.ACTION_PAUSE));
//                            play.setImageDrawable(getResources().getDrawable(R.mipmap.icon_index_songlist_play));
//                        }else {
//                            PlayCtrlUtil.INSTANCE.playSheet(MyCollectionActivity.this, dataBeanList.get(0).getSong_id() + "");
//                            play.setImageDrawable(getResources().getDrawable(R.mipmap.icon_index_songlist_play2));
//                        }
                        if (MediaService.mediaPlayer != null && MediaService.bean != null && MediaService.mediaPlayer.isPlaying()) {
//                        if (isPlaying) {
                            play.setImageResource(R.drawable.song_detail_play_false);
                            isPlaying = false;
                            sendBroadcast(new Intent(MediaService.ACTION_PAUSE));
                            for (int i = 0; i < recyclerview.getChildCount(); i++) {
                                if(dataBeanList.size()>0){
                                    TextView tv_music_name= (TextView) recyclerview.getChildAt(i).findViewById(R.id.tv_music_name);
                                    TextView tv_music_nickname= (TextView) recyclerview.getChildAt(i).findViewById(R.id.tv_music_nickname);
                                    tv_music_name.setTextColor(Color.parseColor("#2b2b2b"));
                                    tv_music_nickname.setTextColor(Color.parseColor("#9da2a6"));
                                }else {}
                            }
                        } else if (MediaService.mediaPlayer != null && MediaService.bean != null) {
                            PlayCtrlUtil.INSTANCE.playSheet(MyCollectionActivity.this, dataBeanList.get(0).getSong_id() + "");
                            play.setImageResource(R.drawable.song_detail_play_true);
                            isPlaying = true;
                            Message message = Message.obtain();
                            message.what = 1;
                            myhandler.sendMessage(message);
                        }
                        else {
                            MobclickAgent.onEvent(MyCollectionActivity.this, "songlists");
                            PlayCtrlUtil.INSTANCE.playSheet(MyCollectionActivity.this, dataBeanList.get(0).getSong_id() + "");
                            play.setImageResource(R.drawable.song_detail_play_true);
                            isPlaying = true;
                            Message message = Message.obtain();
                            message.what = 1;
                            myhandler.sendMessage(message);
                        }


                    }
                });

        /**
         * 添加到播放列表
         * */
        RxView.clicks(tv_add_musicList)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (!isClickable) {
                            setSnackBar("你还没有选择音乐", "", R.drawable.icon_good);
                            return;
                        }
                        Observable.fromArray(dataBeanList)
                                .flatMap(new Function<List<MyCollectionBean.DataBean>, ObservableSource<MyCollectionBean.DataBean>>() {
                                    @Override
                                    public ObservableSource<MyCollectionBean.DataBean> apply(@NonNull List<MyCollectionBean.DataBean> dataBeen) throws Exception {
                                        return Observable.fromIterable(dataBeen)
                                                .filter(new Predicate<MyCollectionBean.DataBean>() {
                                                    @Override
                                                    public boolean test(@NonNull MyCollectionBean.DataBean dataBeen) throws Exception {
                                                        return dataBeen.getSingle_selection();
                                                    }
                                                });
                                    }
                                })
                                .subscribe(new Observer<MyCollectionBean.DataBean>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onNext(@NonNull MyCollectionBean.DataBean data) {
                                        MusicInfo.DataBean dataBean = sheetBeanResultData(data, 0);
                                        PlayList.addToList(MyCollectionActivity.this, dataBean);
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        setSnackBar("添加到播放列表成功", "", R.drawable.icon_success);
                                        if (!MediaService.getMediaPlayer().isPlaying()) {
                                            List<MusicInfo.DataBean> dataBeanList = PlayList.getList(MyCollectionActivity.this);
                                            PlayCtrlUtil.INSTANCE.startServiceToPlay(MyCollectionActivity.this, dataBeanList.get(dataBeanList.size() - 1));
                                        }
                                    }
                                });
                    }
                });

        /**
         * 下载
         * */
        RxView.clicks(tv_download_music)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (!isClickable) {
                            setSnackBar("你还没有选择音乐", "", R.drawable.icon_good);
                            return;
                        }
                        Observable.fromArray(dataBeanList)
                                .flatMap(new Function<List<MyCollectionBean.DataBean>, ObservableSource<MyCollectionBean.DataBean>>() {
                                    @Override
                                    public ObservableSource<MyCollectionBean.DataBean> apply(@NonNull List<MyCollectionBean.DataBean> dataBeen) throws Exception {
                                        return Observable.fromIterable(dataBeen)
                                                .filter(new Predicate<MyCollectionBean.DataBean>() {
                                                    @Override
                                                    public boolean test(@NonNull MyCollectionBean.DataBean dataBeen) throws Exception {
                                                        return dataBeen.getSingle_selection();
                                                    }
                                                });
                                    }
                                })
                                .subscribe(new Observer<MyCollectionBean.DataBean>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onNext(@NonNull final MyCollectionBean.DataBean dataBean) {
                                        if (!dataBean.getSingle_selection()) {
                                            return;
                                        }
                                        String kbps = CacheUtils.INSTANCE.getString(MyCollectionActivity.this, Constants.User.MUSIC_KBP, "128");
                                        NetWork.INSTANCE.getMusicDown(MyCollectionActivity.this, dataBean.getId() + "", TextUtils.equals("128", kbps) ? "1" : "2", new NetWork.TokenCallBack() {

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
                                                    TasksManager.getImpl().downLoadFile(musicBean, data, MyCollectionActivity.this);
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
                                        if (null != check_song) {
                                            check_song.setChecked(false);
                                            isAllCheck = check_song.isChecked();
                                            checkView();
                                        }
                                    }
                                });
                        getMusicID("download");
                    }
                });
        initSwipeRecyclerView();
    }

    private void singleCheck() {
        Observable.fromArray(dataBeanList)
                .flatMap(new Function<List<MyCollectionBean.DataBean>, ObservableSource<MyCollectionBean.DataBean>>() {
                    @Override
                    public ObservableSource<MyCollectionBean.DataBean> apply(@NonNull List<MyCollectionBean.DataBean> dataBeen) throws Exception {
                        return Observable.fromIterable(dataBeen);
                    }
                })
                .subscribe(new Observer<MyCollectionBean.DataBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull MyCollectionBean.DataBean dataBean) {
                        dataBean.setSingle_selection(check_song.isChecked());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        notifyDataAdapter();
                        countMusic();
                    }
                });
    }

    private void getMusicID(final String type) {
        if (!isClickable) {
            setSnackBar("你还没有选择音乐", "", R.drawable.icon_good);
            return;
        }
        Observable.fromArray(dataBeanList)
                .flatMap(new Function<List<MyCollectionBean.DataBean>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull List<MyCollectionBean.DataBean> dataBeen) throws Exception {
                        return Observable.fromIterable(dataBeen)
                                .filter(new Predicate<MyCollectionBean.DataBean>() {
                                    @Override
                                    public boolean test(@NonNull MyCollectionBean.DataBean dataBean) throws Exception {
                                        return dataBean.getSingle_selection();
                                    }
                                })
                                .map(new Function<MyCollectionBean.DataBean, String>() {
                                    @Override
                                    public String apply(@NonNull MyCollectionBean.DataBean dataBean) throws Exception {
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
                                netCancelCollection(s);
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

    private void netCancelCollection(String s) {
        if (TextUtils.isEmpty(s)) {
            return;
        }
        NetWork.INSTANCE.getMemberDelCollect(this, s, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                if (null == myCollectionAdapter) {
                    return;
                }
                for (int i = 0; i < dataBeanList.size(); i++) {
                    if (dataBeanList.get(i).getSingle_selection()) {
                        dataBeanList.remove(i);
                        myCollectionAdapter.notifyItemRemoved(i);
                        i--;
                    }
                }
                myCollectionAdapter.notifyDataSetChanged();
                countMusic();
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    private void initSwipeRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        // 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：

        myCollectionAdapter = new MyCollectionAdapter(dataBeanList, getSupportFragmentManager(),myhandler) {
            @Override
            public int getItemViewType(int position) {
                return position;
            }
        };
        recyclerview.setAdapter(myCollectionAdapter);
        recyclerview.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        // 设置菜单创建器。
        recyclerview.setSwipeMenuCreator(swipeMenuCreator);
        recyclerview.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                int adapterPos = menuBridge.getAdapterPosition();
                PlayCtrlUtil.INSTANCE.nextPlay(MyCollectionActivity.this, dataBeanList.get(adapterPos).getId());
            }
        });
        myCollectionAdapter.setCheckedSongListener(new MyCollectionAdapter.ClickCheckedSongListener() {
            @Override
            public void onChecked(Boolean aBoolean, int position) {
                if (dataBeanList.size() > position) {
                    dataBeanList.get(position).setSingle_selection(aBoolean);
                    myCollectionAdapter.notifyItemChanged(position);
                    countMusic();
                }
            }

            @Override
            public void onRefreshData() {
                page = 1;
                initData();
            }
        });
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
        NetWork.INSTANCE.getMemberCollection(this, page, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                MyCollectionBean myCollectionBean = JSON.parseObject(resultData, MyCollectionBean.class);
                refreshData(headers,myCollectionBean);
            }

            @Override
            public void doError(String msg) {
                if (page > 1) {
                    page--;
                }
            }

            @Override
            public void doResult() {
                interfaceRefreshLoadMore.setStopRefreshing();
            }
        });
    }

    private void refreshData(Headers headers, MyCollectionBean myCollectionBean) {
        if (myCollectionBean != null && null != myCollectionBean.getData() && myCollectionBean.getData().size() > 0) {
            if (page == 1) {
                dataBeanList.clear();
            }
            dataBeanList.addAll(myCollectionBean.getData());
            notifyDataAdapter();
            if (headers!=null){
                String s = headers.get("X-Pagination-Total-Count");
                tv_all_playing_tv.setText("全部播放（共" + s + "首）");
            }
        } else if (page == 1){
            layout_not_data.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initEvent() {
        mRefreshEvent = RxBus.getDefault().toObservable(MyCollectionRefreshEvent.class)
                .subscribeWith(new RxBusSubscriber<MyCollectionRefreshEvent>() {
                    @Override
                    protected void onEvent(MyCollectionRefreshEvent myCollectionRefreshEvent) throws Exception {
                        if (myCollectionRefreshEvent.getCode() == 0) {
                            if (null != check_song) {
                                check_song.setChecked(myCollectionRefreshEvent.getAllSelect());
                                singleCheck();
                            }
                        } else {
                            page = 1;
                            initData();
                        }
                    }
                });
        RxBus.getDefault().add(mRefreshEvent);
    }

    /**
     * 编辑和收藏切换
     */
    private void checkView() {
        if (isAllCheck) {
            tv_all_playing.setVisibility(View.GONE);
            tv_batch_operation.setVisibility(View.GONE);
            check_song.setVisibility(View.VISIBLE);
            tv_select_song_num.setVisibility(View.VISIBLE);
            setLeftButtonImageView(ContextCompat.getDrawable(this, R.drawable.cancle_edit_song));
            layout_footer.setVisibility(View.VISIBLE);
            //TODO和我的歌单中一样，先把加入到播放列表注释了
            tv_add_musicList.setVisibility(View.GONE);
            tv_select_song_num.setText("已选" + count + "/" + dataBeanList.size() + "首歌曲");
            layout_head.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            superSwipeRefreshLayout.setEnabled(false);
        } else {
            tv_all_playing.setVisibility(View.VISIBLE);
            tv_batch_operation.setVisibility(View.VISIBLE);
            check_song.setVisibility(View.GONE);
            tv_select_song_num.setVisibility(View.GONE);
            layout_footer.setVisibility(View.GONE);
            setLeftButtonImageView(ContextCompat.getDrawable(this, R.drawable.back));
            layout_head.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
            superSwipeRefreshLayout.setEnabled(true);
        }
        Observable.just(dataBeanList)
                .flatMap(new Function<List<MyCollectionBean.DataBean>, ObservableSource<MyCollectionBean.DataBean>>() {
                    @Override
                    public ObservableSource<MyCollectionBean.DataBean> apply(@NonNull List<MyCollectionBean.DataBean> dataBeen) throws Exception {
                        return Observable.fromIterable(dataBeen);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyCollectionBean.DataBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull MyCollectionBean.DataBean dataBean) {
                        dataBean.setCheck(isAllCheck);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        notifyDataAdapter();
                    }
                });
    }

    private void notifyDataAdapter() {
        if (null == myCollectionAdapter) {
            return;
        }
        myCollectionAdapter.notifyDataSetChanged();
        if (myCollectionAdapter.getData().size() > 0) {
            layout_not_data.setVisibility(View.GONE);
        } else {
            layout_not_data.setVisibility(View.VISIBLE);
        }
    }

    int count = 0;

    /**
     * 循环计数
     */
    private void countMusic() {
        Observable.fromArray(dataBeanList)
                .flatMap(new Function<List<MyCollectionBean.DataBean>, ObservableSource<MyCollectionBean.DataBean>>() {
                    @Override
                    public ObservableSource<MyCollectionBean.DataBean> apply(@NonNull List<MyCollectionBean.DataBean> dataBeen) throws Exception {
                        return Observable.fromIterable(dataBeen)
                                .filter(new Predicate<MyCollectionBean.DataBean>() {
                                    @Override
                                    public boolean test(@NonNull MyCollectionBean.DataBean dataBean) throws Exception {
                                        return dataBean.getSingle_selection();
                                    }
                                });
                    }
                })
                .subscribe(new Observer<MyCollectionBean.DataBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        count = 0;

                    }

                    @Override
                    public void onNext(@NonNull MyCollectionBean.DataBean dataBean) {
                        Log.d(TAG, "onNext: ");
                        count++;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        tv_select_song_num.setText("已选" + count + "/" + dataBeanList.size() + "首歌曲");
                        checkSelectClickView(count);
                        if (dataBeanList.size() == 0) {
                            check_song.setChecked(false);
                            return;
                        }
                        if (count == dataBeanList.size()) {
                            check_song.setChecked(true);
                        } else {
                            check_song.setChecked(false);
                        }
                    }
                });
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
                SwipeMenuItem deleteItem = new SwipeMenuItem(MyCollectionActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(MyCollectionActivity.this, R.color.base_red))
                        .setText("  下一首播放") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setTextSize(12)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
            }
        }
    };

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (isAllCheck) {
            isAllCheck = false;
            checkView();
        } else {
            finish();
        }
    }

    private void MaterialDialog(String content) {
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.content(
                content)
                .btnText("取消", "确定")
                .titleTextSize(16)
                .titleTextColor(ContextCompat.getColor(this, R.color.color_14_text))
                .contentTextColor(ContextCompat.getColor(this, R.color.color_66_text))
                .contentTextSize(14)
                .btnTextSize(14)
                .btnTextColor(ContextCompat.getColor(this, R.color.base_red)
                        , ContextCompat.getColor(this, R.color.base_red))
                .showAnim(null)
                .dismissAnim(null)
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        getMusicID("cancel");
                    }
                }
        );
    }

    private static MusicInfo.DataBean sheetBeanResultData(MyCollectionBean.DataBean data, int song_id) {
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
        if (data.getImgpic_info() != null) {
            dataBean.setOriginal(data.getImgpic_info().getLink());
        }


        dataBean.setCounts(data.getCounts());
        dataBean.setComment(data.getComment());
        return dataBean;
    }

    private void checkSelectClickView(int count) {
        if (count > 0) {
            tv_cancle_collection.setTextColor(ContextCompat.getColor(this, R.color.color_17_text));
            tv_add_song.setTextColor(ContextCompat.getColor(this, R.color.color_17_text));
            tv_add_musicList.setTextColor(ContextCompat.getColor(this, R.color.color_17_text));
            tv_download_music.setTextColor(ContextCompat.getColor(this, R.color.color_17_text));
            isClickable = true;
        } else {
            tv_cancle_collection.setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text));
            tv_add_song.setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text));
            tv_add_musicList.setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text));
            tv_download_music.setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text));
            isClickable = false;
        }
    }

    /**
     * service播放
     */
    public static void startServiceToPlay(Context context, MusicInfo.DataBean dataBean) {
        Intent intent = new Intent(context, MediaService.class);
        intent.putExtra("bean", dataBean);
        context.startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        interfaceRefreshLoadMore.resetRefreshView();
        RxBus.getDefault().remove(mRefreshEvent);
    }
}
