package com.mxkj.yuanyintang.mainui.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.BaseActivity;
import com.mxkj.yuanyintang.base.activity.HomeActivity;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.activity.WelComeActivity;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.database.updatafilesql.TasksManager;
import com.mxkj.yuanyintang.extraui.AgreeAnimationUtil;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog;
import com.mxkj.yuanyintang.extraui.dialog.TitleOperationDialog;
import com.mxkj.yuanyintang.mainui.comment.CommentActivity;
import com.mxkj.yuanyintang.mainui.home.adapter.SongSheetDetailsAdapter;
import com.mxkj.yuanyintang.mainui.home.adapter.SongSheetMusicListAdapter;
import com.mxkj.yuanyintang.mainui.home.adapter.SongSheetRecommendListAdapter;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.mainui.home.bean.SongListDetails;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.mainui.home.utils.GridSpacingItemDecoration;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.mainui.login_regist.QuickLoginActivityNew;
import com.mxkj.yuanyintang.mainui.message.activity.MyCollectionSongListActivity;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.mainui.myself.bean.MyCollectionBean;
import com.mxkj.yuanyintang.mainui.myself.my_release.MyReleaseActivity;
import com.mxkj.yuanyintang.mainui.myself.settings.activity.NoMobile_goBind_Activity;
import com.mxkj.yuanyintang.mainui.newapp.pond.PondAdapter;
import com.mxkj.yuanyintang.mainui.newapp.pond.PondInfo;
import com.mxkj.yuanyintang.musicplayer.play_control.GetMusicInfo;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayList;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.app.CommonUtils;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.layoutmanager.FullyLinearLayoutManager;
import com.mxkj.yuanyintang.utils.photopicker.widget.NoScrollRecyclerView;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber;
import com.mxkj.yuanyintang.utils.rxbus.event.PlayerMusicRefreshDataEvent;
import com.mxkj.yuanyintang.utils.rxbus.event.RefreshDataEvent;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.threadpool.ThreadPoolManager;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.mxkj.yuanyintang.widget.MultiLineRadioGroup;
import com.umeng.analytics.MobclickAgent;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
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

import static com.mxkj.yuanyintang.utils.constant.Constants.Other.EM_LOGIN;

/**
 * Created by Liujie 2017/10/13.
 * 歌单详情
 */
public class SongSheetDetailsActivity extends StandardUiActivity {
    public static final String SONG_SHEET_ID = "_id";
    @BindView(R.id.app_bar)
    public AppBarLayout app_bar;
    @BindView(R.id.tv_close)
    public TextView tv_close;
    @BindView(R.id.main_content)
    CoordinatorLayout main_content;

    @BindView(R.id.main_content_show)
    LinearLayout main_content_show;
    @BindView(R.id.back_home)
    ImageView back_home;


    @BindView(R.id.layout_head)
    LinearLayout layout_head;
    @BindView(R.id.imageview)
    ImageView imageview;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_music_name)
    TextView tv_music_name;
    @BindView(R.id.tv_music_nickname)
    TextView tv_music_nickname;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_more)
    ImageView iv_more;
    @BindView(R.id.iv_music_cover)
    ImageView iv_music_cover;
    @BindView(R.id.riv_head_img)
    ImageView riv_head_img;
    @BindView(R.id.dtv_comment_num)
    TextView dtv_comment_num;
    @BindView(R.id.dtv_collection_num)
    TextView dtv_collection_num;
    @BindView(R.id.dtv_doughnut_num)
    TextView dtv_doughnut_num;
    @BindView(R.id.layout_musician)
    LinearLayout layout_musician;
    @BindView(R.id.iv_is_vip)
    ImageView iv_is_vip;
    @BindView(R.id.layout_music_cover)
    RelativeLayout layout_music_cover;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    String id;
    String type;
    MusicIndex.ItemInfoListBean itemInfoBean;
    @BindView(R.id.tv_table_name)
    TextView tvTableName;
    @BindView(R.id.multiline_group)
    MultiLineRadioGroup multilineGroup;
    @BindView(R.id.img_all_playing)
    ImageView imgAllPlaying;
    @BindView(R.id.tv_song_list_label)
    TextView tvSongListLabel;
    @BindView(R.id.ll_play_all)
    LinearLayout llPlayAll;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.check_song)
    CheckBox checkSong;
    @BindView(R.id.tv_edit_more)
    TextView tvEditMore;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.tv_add_song)
    TextView tvAddSong;
    @BindView(R.id.tv_add_musicList)
    TextView tvAddMusicList;
    @BindView(R.id.tv_cancle_collection)
    TextView tvCancleCollection;
    @BindView(R.id.listen_num)
    TextView listen_num;
    @BindView(R.id.tv_download_music)
    TextView tvDownloadMusic;
    @BindView(R.id.layout_footer)
    LinearLayout layoutFooter;

    @BindView(R.id.pond_recyclerview)
    NoScrollRecyclerView pond_recyclerview;
    @BindView(R.id.song_sheet_recyclerview)
    NoScrollRecyclerView song_sheet_recyclerview;
    @BindView(R.id.song_recyclerview)
    NoScrollRecyclerView song_recyclerview;
    @BindView(R.id.ll_recommend)
    LinearLayout ll_recommend;
    @BindView(R.id.ll_other)
    LinearLayout ll_other;
    @BindView(R.id.tv_reco_song)
    TextView tv_reco_song;
    @BindView(R.id.layout_pond)
    View layout_pond;

    private Disposable mPlayerMusicRefreshData;
    private Disposable mSongDetailsRefreshData;

    MusicIndex.ItemInfoListBean.MusicBean dataBeans;
    int playCounts = 0;
    public boolean isEdit = false;

    private int count;
    private boolean isClickable = false;
    private List<MusicIndex.ItemInfoListBean.MusicBean> musicBeanList = new ArrayList<>();
    private Handler handler;
    private State mState = State.Expanded;
    private boolean isPlaying;
    private SongSheetRecommendListAdapter songSheetRecommendListAdapter;
    SongSheetMusicListAdapter songSheetMusicAdapter;
    private boolean canEdit = true;


    @BindView(R.id.notplaylist)
    RelativeLayout notplaylist;

    Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {//接收到完整的包裹
            super.handleMessage(msg);
            switch (msg.what){
                case 1://拆解包裹拿值
                    //处理消息
                    finish();
                    break;
                case 2:
                    break;
            }


        }
    };

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    public int setLayoutId() {
        StatusBarUtil.baseTransparentStatusBar(this);
        return R.layout.activity_song_sheet_details;
    }

    @Override
    protected void initView() {
        handler = new Handler();
        id = getIntent().getStringExtra(SONG_SHEET_ID);
        type = getIntent().getStringExtra("type");
        ButterKnife.bind(SongSheetDetailsActivity.this);
        hideTitle(true);
        initViewHeight();
        initRecyclerView();
    }

    private void refreshData() {
        if (itemInfoBean.getIs_music() == 3) {
            iv_is_vip.setVisibility(View.VISIBLE);
        } else {
            iv_is_vip.setVisibility(View.GONE);
        }
        tv_title.setText(StringUtils.isEmpty(itemInfoBean.getTitle()));
        listen_num.setText(StringUtils.isEmpty(itemInfoBean.getCounts_text()));
        tv_music_name.setText(StringUtils.isEmpty(itemInfoBean.getTitle()));
        tv_music_nickname.setText(StringUtils.isEmpty(itemInfoBean.getNickname()));

        if(itemInfoBean.getCollection()==0){
            dtv_collection_num.setText("收藏");
        }else {
            dtv_collection_num.setText(StringUtils.setNum(itemInfoBean.getCollection()));
        }
        if(itemInfoBean.getComment()==0){
            dtv_comment_num.setText("评论");
        }else {
            dtv_comment_num.setText(StringUtils.setNum(itemInfoBean.getComment()));
        }
        if(itemInfoBean.getAgrees()==0){
            dtv_doughnut_num.setText("分享");
        }else {
            dtv_doughnut_num.setText(StringUtils.setNum(itemInfoBean.getAgrees()));
        }
        Log.e("yyyyy",""+itemInfoBean.getCollection());
        Log.e("yyyyy",""+itemInfoBean.getComment());
        Log.e("yyyyy",""+itemInfoBean.getAgrees());

        playCounts = itemInfoBean.getCounts();
        collectionView(itemInfoBean.getIsCollection());
        agreeView(itemInfoBean.getIs_agree());
        try {
            ImageLoader.with(this)
                    .override(CacheUtils.INSTANCE.getInt(SongSheetDetailsActivity.this, Constants.Other.WIDTH, 0), 250)
                    .getSize(750,300)
                    .url(itemInfoBean.getImgpic_info().getLink())
                    .scale(ScaleMode.CENTER_CROP)
                    .blur(25)
                    .into(imageview);

            ImageLoader.with(this)
                    .override(120, 120)
                    .getSize(300,300)
                    .url(itemInfoBean.getImgpic_info().getLink())
                    .scale(ScaleMode.CENTER_CROP)
                    .into(iv_music_cover);

            ImageLoader.with(this)
                    .override(40, 40)
                    .asCircle()
                    .getSize(48,48)
                    .url(itemInfoBean.getHead_link())
                    .scale(ScaleMode.CENTER_CROP)
                    .into(riv_head_img);

        } catch (RuntimeException e) {
        }

    }

    private void agreeView(int is_agree) {
        MobclickAgent.onEvent(SongSheetDetailsActivity.this, "album_detail_praise");
        Drawable doughntDrawable = null;
        if (is_agree == 1) {
            doughntDrawable = getResources().getDrawable(R.drawable.fullplay_share_normal);
            dtv_doughnut_num.setTextColor(ContextCompat.getColor(this, R.color.base_red));
        } else {
            doughntDrawable = getResources().getDrawable(R.drawable.fullplay_share_normal);
            dtv_doughnut_num.setTextColor(Color.parseColor("#616665"));
        }
        doughntDrawable.setBounds(0, 0, doughntDrawable.getMinimumWidth(), doughntDrawable.getMinimumHeight());
        dtv_doughnut_num.setCompoundDrawables(null, doughntDrawable, null, null);

    }

    private void collectionView(int collection) {
        MobclickAgent.onEvent(this, "album_detail_collect");
        Drawable collectionDrawable = null;
        if (collection == 1) {
            collectionDrawable = getResources().getDrawable(R.drawable.icon_music_soucang2);
            dtv_collection_num.setTextColor(ContextCompat.getColor(this, R.color.base_red));
        } else {
            collectionDrawable = getResources().getDrawable(R.drawable.music_detail_collection_false);
            dtv_collection_num.setTextColor(Color.parseColor("#616665"));
        }
        collectionDrawable.setBounds(0, 0, collectionDrawable.getMinimumWidth(), collectionDrawable.getMinimumHeight());
        dtv_collection_num.setCompoundDrawables(null, collectionDrawable, null, null);

    }

    private void initRecyclerView() {
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                Log.e(TAG, "onOffsetChanged:verticalOffset== " + Math.abs(verticalOffset) + "----TotalScrollRange===" + appBarLayout.getTotalScrollRange());
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (mState != State.Collapsed)
                        mState = State.Collapsed;
                } else if (verticalOffset == 0) {
                    if (mState != State.Expanded) {
                        mState = State.Expanded;
                    }
                } else {
                    mState = State.Ide;
                }
                int scrollRangle = appBarLayout.getTotalScrollRange();
                toolbar.setBackgroundColor(changeAlpha(getResources().getColor(R.color.white), Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));
                if (verticalOffset == 0) {
                    tv_title.setAlpha(0.0f);
                } else {
                    //保留一位小数
                    float alpha = Math.abs(Math.round(1.0f * verticalOffset / scrollRangle) * 10) / 10;
                    tv_title.setAlpha(alpha);
//                    layout_head.setAlpha(1.0f - alpha);
                }
            }
        });
    }

    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    private void initViewHeight() {
        Drawable leftDrawable = getResources().getDrawable(R.drawable.fullplay_share_normal);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        dtv_doughnut_num.setCompoundDrawables(null, leftDrawable, null, null);
    }
    private JSONObject dataObject=null;

    @Override
    protected void initData() {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        Log.e("songDetails_"+id,"111111111111111");
        NetWork.INSTANCE.getSongSheetDetails("songDetails_"+id,this, id, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                hideLoadingView();
                try {
                    JSONObject object = new JSONObject(resultData);
                     dataObject = object.optJSONObject("data") == null ? new JSONObject() : object.optJSONObject("data");
                    itemInfoBean = JSON.parseObject(dataObject.toString(), MusicIndex.ItemInfoListBean.class);

                    final SongListDetails songListDetails = JSON.parseObject(dataObject.toString(), SongListDetails.class);

                    int uid = dataObject.getInt("uid");
                    canEdit = itemInfoBean.getType() != 1;
                    isMySongSheet(uid);


                    /**
                     * 通过子线程刷新UI
                     */
                    ThreadPoolManager.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //TODO：音乐列表
                                    JSONArray musicArray = dataObject.optJSONArray("music") == null ? new JSONArray() : dataObject.optJSONArray("music");
//                                    Log.d(TAG, "doNext: ------------>" + musicArray.toString());
                                    musicBeanList = JSON.parseArray(StringUtils.isEmpty(musicArray.toString()), MusicIndex.ItemInfoListBean.MusicBean.class);
                                    MusicIndex.ItemInfoListBean musicInfoListBean = new MusicIndex.ItemInfoListBean();
                                    musicInfoListBean.itemType = "music";
                                    musicInfoListBean.setMusic(musicBeanList);

                                    if(songListDetails.getTotal()<=0){
                                        Log.e("eeee","没有音乐，显示给的布局");
                                        notplaylist.setVisibility(View.VISIBLE);
                                        tvEditMore.setVisibility(View.GONE);
                                        rl.setVisibility(View.GONE);
                                        bindMusicData(musicInfoListBean);
                                        notplaylist.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Message message = Message.obtain();
                                                message.what = 1;
                                                myhandler.sendMessage(message);
//                                                goActivity(WelComeActivity.class);
                                            }
                                        });
                                    }else {
                                        notplaylist.setVisibility(View.GONE);
                                        bindMusicData(musicInfoListBean);
                                        tvEditMore.setVisibility(View.VISIBLE);
                                        rl.setVisibility(View.VISIBLE);
                                    }

                                    //TODO：猜你喜欢
                                    JSONArray recommendArray = dataObject.optJSONArray("recommend") == null ? new JSONArray() : dataObject.optJSONArray("recommend");
                                    List<MusicIndex.ItemInfoListBean.RecommendBean> recommendBeanList = JSON.parseArray(StringUtils.isEmpty(recommendArray.toString()), MusicIndex.ItemInfoListBean.RecommendBean.class);
                                    if (recommendBeanList.size() > 0) {
                                        MusicIndex.ItemInfoListBean recommendInfoListBean = new MusicIndex.ItemInfoListBean();
                                        recommendInfoListBean.itemType = "recommend";
                                        recommendInfoListBean.setRecommend(recommendBeanList);
                                        bindRecommendData(recommendInfoListBean);
                                    }
                                    //TODO  相关池塘
                                    JSONArray pondArray = dataObject.optJSONArray("topic") == null ? new JSONArray() : dataObject.optJSONArray("topic");
                                    List<PondInfo.DataBean.DataListBean> topicBeans = JSON.parseArray(StringUtils.isEmpty(pondArray.toString()), PondInfo.DataBean.DataListBean.class);
                                    if (topicBeans != null && topicBeans.size() > 0) {
                                        MusicIndex.ItemInfoListBean pondInfoListBean = new MusicIndex.ItemInfoListBean();
                                        pondInfoListBean.itemType = "pond";
                                        pondInfoListBean.setPondList(topicBeans);
                                        bindPondData(pondInfoListBean);
                                    } else {
                                        layout_pond.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    });

                    refreshData();
                    main_content.setVisibility(View.VISIBLE);
                    main_content_show.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void isMySongSheet(final int uid) {
        UserInfoUtil.getUserInfoByJson(CacheUtils.INSTANCE.getString(this, Constants.User.USER_JSON), new UserInfoUtil.UserCallBack() {
            @Override
            public void doNext(UserInfo infoBean) {
                if (infoBean != null && infoBean.getData() != null) {
                    int id = infoBean.getData().getId();
                    if (uid == id) {
                        tvCancleCollection.setVisibility(View.VISIBLE);
                        tvCancleCollection.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e(TAG, "从歌单删除！！！！: ");
                                deleFromSongSheet();
                            }
                        });

                    } else {
                        tvCancleCollection.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    protected void initEvent() {
        mPlayerMusicRefreshData = RxBus.getDefault().toObservable(PlayerMusicRefreshDataEvent.class)
                .subscribeWith(new RxBusSubscriber<PlayerMusicRefreshDataEvent>() {
                    @Override
                    protected void onEvent(PlayerMusicRefreshDataEvent playerMusicRefreshDataEvent) throws Exception {
                        if (null != MediaService.bean && null != itemInfoBean) {
                            GetMusicInfo.INSTANCE.getMusicDetial(SongSheetDetailsActivity.this, MediaService.bean.getId(), new GetMusicInfo.SetBeanData() {
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

        //静态页面的返回按钮
        RxView.clicks(back_home)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
//                        MobclickAgent.onEvent(SongSheetDetailsActivity.this, "album_detail_back");
//                        backBtnEvent();
                        finish();
                    }
                });




        RxView.clicks(iv_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MobclickAgent.onEvent(SongSheetDetailsActivity.this, "album_detail_back");
                        backBtnEvent();
                    }
                });

        checkSong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
        /**
         * 全选按钮
         * */
        checkSong.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    checkSong.setChecked(!checkSong.isChecked());
                    CheckAll();
                }
                return true;
            }
        });
        RxView.clicks(tvEditMore)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MobclickAgent.onEvent(SongSheetDetailsActivity.this, "album_detail_edit");
                        if (CacheUtils.INSTANCE.getBoolean(SongSheetDetailsActivity.this, Constants.User.IS_LOGIN)) {
                            isEdit = true;
                            checkView(false);
                            tv_close.setVisibility(View.VISIBLE);
                            iv_more.setVisibility(View.GONE);
                            tv_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    backBtnEvent();
                                }
                            });
                            ll_other.setVisibility(View.GONE);

                        } else {
//                            goActivity(QuickLoginActivityNew.class);
                            goActivity(LoginRegMainPage.class);
                        }
                    }
                });

        RxView.clicks(iv_more)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        rightBtnEvent();
                    }
                });
        RxView.clicks(dtv_doughnut_num)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
//                        if (itemInfoBean != null) {
//                            ImageView img_ani = (ImageView) findViewById(R.id.img_ani);
//                            if (itemInfoBean.getIs_agree() == 0) {
//                                AgreeAnimationUtil.INSTANCE.setAnim(SongSheetDetailsActivity.this, img_ani, dtv_doughnut_num, AgreeAnimationUtil.ALBUM_AGREE);
//                            }
//                            NetWork.INSTANCE.postAgreeAdd(SongSheetDetailsActivity.this, "2", itemInfoBean.getId() + "", new NetWork.TokenCallBack() {
//                                @Override
//                                public void doNext(String resultData, Headers headers) {
////
//                                    hideLoadingView();
//                                    com.alibaba.fastjson.JSONObject object = JSON.parseObject(resultData);
//                                    int data = (int) object.get("data");
//                                    int agrees = itemInfoBean.getAgrees();
//                                    if (data == 1) {
//                                        itemInfoBean.setAgrees((agrees + 1));
//                                        itemInfoBean.setIs_agree(1);
//                                    } else {
//                                        itemInfoBean.setAgrees((agrees - 1));
//                                        itemInfoBean.setIs_agree(0);
//                                    }
//                                    if (null != dtv_doughnut_num) {
//                                        dtv_doughnut_num.setText(StringUtils.setNum(itemInfoBean.getAgrees()));
//                                    }
//                                    agreeView(itemInfoBean.getIs_agree());
//                                }
//
//                                @Override
//                                public void doError(String msg) {
//                                    hideLoadingView();
//                                }
//
//                                @Override
//                                public void doResult() {
//
//                                }
//                            });
//                        }
                        MobclickAgent.onEvent(SongSheetDetailsActivity.this, "album_detail_more");
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
                                .setReportType(2).setCanEdit(canEdit)
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
                        ShareBottomDialog shareBottomDialog = new ShareBottomDialog(SongSheetDetailsActivity.this, musicBean);
                        shareBottomDialog.show();
                    }
                });
        RxView.clicks(layout_musician)
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
        RxView.clicks(dtv_comment_num)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MobclickAgent.onEvent(SongSheetDetailsActivity.this, "album_detail_comment");
                        if (null == itemInfoBean) {
                            return;
                        }
                        Intent intent = new Intent(SongSheetDetailsActivity.this, CommentActivity.class);
                        intent.putExtra(CommentActivity.ITEM_ID, itemInfoBean.getId());
                        intent.putExtra(CommentActivity.TYPE, 2);
                        startActivity(intent);
                    }
                });
        RxView.clicks(dtv_collection_num)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (null == itemInfoBean) {
                            return;
                        }
                        if (!CacheUtils.INSTANCE.getBoolean(SongSheetDetailsActivity.this, Constants.User.IS_LOGIN)) {
                            goActivity(QuickLoginActivityNew.class);
                            return;
                        }
                        showLoadingView();
                        NetWork.INSTANCE.getSongCollect(SongSheetDetailsActivity.this, itemInfoBean.getId() + "", new NetWork.TokenCallBack() {
                            @Override
                            public void doNext(String resultData, Headers headers) {
                                hideLoadingView();
                                com.alibaba.fastjson.JSONObject object = JSON.parseObject(resultData);
                                String data = (String) object.get("data");
                                int collects = itemInfoBean.getCollection();
//                                后台返回的"1"，不是1。
                                if (TextUtils.equals("1", data)) {
                                    itemInfoBean.setCollection((collects + 1));
                                    itemInfoBean.setIsCollection(1);
                                } else {
                                    itemInfoBean.setCollection((collects - 1));
                                    itemInfoBean.setIsCollection(0);
                                }
                                RxBus.getDefault().post(new RefreshDataEvent(0));
                                if (null != dtv_collection_num) {
                                    dtv_collection_num.setText(StringUtils.setNum(itemInfoBean.getCollection()));
                                }
                                collectionView(itemInfoBean.getIsCollection());
                            }

                            @Override
                            public void doError(String msg) {
                                hideLoadingView();
                            }

                            @Override
                            public void doResult() {

                            }
                        });
                    }
                });
        RxView.clicks(iv_music_cover)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (null == itemInfoBean) {
                            return;
                        }
                        Intent intent = new Intent(SongSheetDetailsActivity.this, SongSheetSynopsisActivity.class);
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
                                                        return dataBeen.isIscheck();
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
                                        PlayList.addToList(SongSheetDetailsActivity.this, dataBean);
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        if (isEdit) {
                                            checkSong.setChecked(false);
                                            CheckAll();
                                        }
                                        setSnackBar("添加到播放列表成功", "", R.drawable.icon_success);
                                        if (!MediaService.getMediaPlayer().isPlaying()) {
                                            List<MusicInfo.DataBean> dataBeanList = PlayList.getList(SongSheetDetailsActivity.this);
                                            if (dataBeanList.size() > 0) {
                                                PlayCtrlUtil.INSTANCE.startServiceToPlay(SongSheetDetailsActivity.this, dataBeanList.get(0));
                                            }
                                        }
                                        backBtnEvent();
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
                        downLoad();
                    }
                });
        RxView.clicks(llPlayAll)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
//                        if (isPlaying) {
//                            imgAllPlaying.setImageResource(R.drawable.song_detail_play_false);
//                            isPlaying = false;
//                            for (int i = 0; i < song_recyclerview.getChildCount(); i++) {
//                                if(musicBeanList.size()>0){
//                                    MobclickAgent.onEvent(SongSheetDetailsActivity.this, "songlists");
//                                    PlayCtrlUtil.INSTANCE.playSheet(SongSheetDetailsActivity.this, id);
//                                    LinearLayout layout = (LinearLayout)song_recyclerview.getChildAt(0);// 获得子item的layout
//                                    TextView musicNane = (TextView) layout.findViewById(R.id.tv_music_name);// 从layout中获得控件,根据其id
//                                    TextView nickName = (TextView) layout.findViewById(R.id.tv_music_nickname);// 从layout中获得控件,根据其id
//                                    musicNane.setTextColor(Color.parseColor("#616665"));
//                                    nickName.setTextColor(Color.parseColor("#616665"));
//                                }else {}
//                            }
//
//                        } else {
//                            MobclickAgent.onEvent(SongSheetDetailsActivity.this, "songlists");
//                            PlayCtrlUtil.INSTANCE.playSheet(SongSheetDetailsActivity.this, id);
//                            imgAllPlaying.setImageResource(R.drawable.song_detail_play_true);
//                            isPlaying = true;
//                            if(musicBeanList.size()>0){
//                                songSheetMusicAdapter.setNewData(musicBeanList);
//                                for (int i = 0; i < song_recyclerview.getChildCount(); i++) {
//                                    LinearLayout layout = (LinearLayout)song_recyclerview.getChildAt(0);// 获得子item的layout
//                                    TextView musicNane = (TextView) layout.findViewById(R.id.tv_music_name);// 从layout中获得控件,根据其id
//                                    TextView nickName = (TextView) layout.findViewById(R.id.tv_music_nickname);// 从layout中获得控件,根据其id
//                                    musicNane.setTextColor(Color.parseColor("#ff6699"));
//                                    nickName.setTextColor(Color.parseColor("#ff6699"));
//                                }
//                            }else {}
//
//                        }
//
//                    }
//                });
                        if (MediaService.mediaPlayer != null && MediaService.bean != null && MediaService.mediaPlayer.isPlaying()) {
//                        if (isPlaying) {
                            imgAllPlaying.setImageResource(R.drawable.song_detail_play_false);
                            isPlaying = false;
                            sendBroadcast(new Intent(MediaService.ACTION_PAUSE));
                            for (int i = 0; i < song_recyclerview.getChildCount(); i++) {
                                if(musicBeanList.size()>0){
                                    LinearLayout layout = (LinearLayout)song_recyclerview.getChildAt(i);// 获得子item的layout
                                    TextView musicNane = (TextView) layout.findViewById(R.id.tv_music_name);// 从layout中获得控件,根据其id
                                    TextView nickName = (TextView) layout.findViewById(R.id.tv_music_nickname);// 从layout中获得控件,根据其id
                                    musicNane.setTextColor(Color.parseColor("#2b2b2b"));
                                    nickName.setTextColor(Color.parseColor("#9da2a6"));
                                }else {}
                            }
                        } else if (MediaService.mediaPlayer != null && MediaService.bean != null) {
                            PlayCtrlUtil.INSTANCE.playSheet(SongSheetDetailsActivity.this, id);
                            imgAllPlaying.setImageResource(R.drawable.song_detail_play_true);
                            if (musicBeanList.size() > 0) {
                                if(musicBeanList.size()>0){
                                    for (int i = 0; i < song_recyclerview.getChildCount(); i++) {
                                        LinearLayout layout = (LinearLayout)song_recyclerview.getChildAt(0);// 获得子item的layout
                                        TextView musicNane = (TextView) layout.findViewById(R.id.tv_music_name);// 从layout中获得控件,根据其id
                                        TextView nickName = (TextView) layout.findViewById(R.id.tv_music_nickname);// 从layout中获得控件,根据其id
                                        musicNane.setTextColor(Color.parseColor("#ff6699"));
                                        nickName.setTextColor(Color.parseColor("#ff6699"));
                                    }
                                }else {}

                            } else {
                            }
                        }
                        else {
                            MobclickAgent.onEvent(SongSheetDetailsActivity.this, "songlists");
                            PlayCtrlUtil.INSTANCE.playSheet(SongSheetDetailsActivity.this, id);
                            imgAllPlaying.setImageResource(R.drawable.song_detail_play_true);
                            isPlaying = true;
                            if(musicBeanList.size()>0){
                                for (int i = 0; i < song_recyclerview.getChildCount(); i++) {
                                    LinearLayout layout = (LinearLayout)song_recyclerview.getChildAt(0);// 获得子item的layout
                                    TextView musicNane = (TextView) layout.findViewById(R.id.tv_music_name);// 从layout中获得控件,根据其id
                                    TextView nickName = (TextView) layout.findViewById(R.id.tv_music_nickname);// 从layout中获得控件,根据其id
                                    musicNane.setTextColor(Color.parseColor("#ff6699"));
                                    nickName.setTextColor(Color.parseColor("#ff6699"));
                                }
                            }else {}

                        }
                    }
                });
    }

    private void downLoad() {
        Observable.fromArray(musicBeanList)
                .flatMap(new Function<List<MusicIndex.ItemInfoListBean.MusicBean>, ObservableSource<MusicIndex.ItemInfoListBean.MusicBean>>() {
                    @Override
                    public ObservableSource<MusicIndex.ItemInfoListBean.MusicBean> apply(@NonNull List<MusicIndex.ItemInfoListBean.MusicBean> dataBeen) throws Exception {
                        return Observable.fromIterable(dataBeen)
                                .filter(new Predicate<MusicIndex.ItemInfoListBean.MusicBean>() {
                                    @Override
                                    public boolean test(@NonNull MusicIndex.ItemInfoListBean.MusicBean dataBeen) throws Exception {
                                        return dataBeen.isIscheck();
                                    }
                                });
                    }
                })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MusicIndex.ItemInfoListBean.MusicBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull final MusicIndex.ItemInfoListBean.MusicBean dataBean) {
                        dataBeans = dataBean;
                        handler.post(runnable);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        setSnackBar("添加到下载列表成功", "", R.drawable.icon_success);
                        if (isEdit) {
                            checkSong.setChecked(false);
                            CheckAll();
                        }
                        backBtnEvent();

                    }
                });
    }

    /**
     * （后台规定歌曲的下载链接必须用每首歌id去请求接口获得。。。。。。。）
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String kbps = CacheUtils.INSTANCE.getString(SongSheetDetailsActivity.this, Constants.User.MUSIC_KBP, "128");
            NetWork.INSTANCE.getMusicDown(SongSheetDetailsActivity.this, dataBeans.getId() + "", TextUtils.equals("128", kbps) ? "1" : "2", new NetWork.TokenCallBack() {
                @Override
                public void doNext(String resultData, Headers headers) {
                    String data = null;
                    try {
                        JSONObject object = new JSONObject(resultData);
                        data = object.optString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!TextUtils.isEmpty(data) && dataBeans.getImgpic_info() != null) {
                        MusicBean musicBean = new MusicBean();
                        musicBean.setExt("." + StringUtils.parseSuffix(data));
                        musicBean.setCollection(1);
                        String link = dataBeans.getImgpic_info().getLink();
                        MusicBean.ImgpicInfoBean imgpicInfoBean = new MusicBean.ImgpicInfoBean();
                        imgpicInfoBean.setLink(link);
                        musicBean.setImgpic_info(imgpicInfoBean);

                        musicBean.setMusic_name(dataBeans.getTitle());
                        musicBean.setMusician_name(dataBeans.getNickname());
                        musicBean.setExt("." + StringUtils.parseSuffix(data));
                        musicBean.setMusic_id(dataBeans.getId() + "");
                        musicBean.setUid(dataBeans.getUid());
                        musicBean.setSong_id(dataBeans.getSong_id());
                        TasksManager.getImpl().downLoadFile(musicBean, data, SongSheetDetailsActivity.this);
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
    };

    private static MusicInfo.DataBean sheetBeanResultData(MusicIndex.ItemInfoListBean.MusicBean data, int song_id) {
        MusicInfo.DataBean dataBean = new MusicInfo.DataBean();
        dataBean.setVideo(data.getVideo());
        if (data.getVideo_info() != null) {
            MusicInfo.DataBean.VideoInfoBean videoInfoBean = new MusicInfo.DataBean.VideoInfoBean();
            videoInfoBean.setLink(data.getVideo_info().getLink());
            data.setVideo_info(videoInfoBean);
        }


        dataBean.setMv(data.getMv());
        if (data.getMv_info() != null) {
//            MusicInfo.DataBean.MvInfoBean mvInfoBean = new MusicInfo.DataBean.MvInfoBean();

            MusicInfo.DataBean.MvInfoBean.INSTANCE.setLink(data.getMv_info().getLink());
            data.setMv_info( MusicInfo.DataBean.MvInfoBean.INSTANCE);
        }

        if (data.getImage_info() != null) {
            MusicInfo.DataBean.ImgpicInfoBean imgpicInfoBean = new MusicInfo.DataBean.ImgpicInfoBean();
            imgpicInfoBean.setLink(data.getImage_info().getLink());
            data.setImage_info(imgpicInfoBean);
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
                                        return dataBean.isIscheck();
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
                            case "addSong":
                                Bundle bundle = new Bundle();
                                bundle.putString(MyCollectionSongListActivity.MUSIC_ID, s);
                                bundle.putString(MyCollectionSongListActivity.VIEW_TYPE, "collectionAddSong");
                                bundle.putBoolean(MyCollectionSongListActivity.IS_MULTI_SELECT, false);
                                goActivity(MyCollectionSongListActivity.class, bundle);
                                backBtnEvent();
                                break;
                        }

                    }
                });
    }

    private void deleFromSongSheet() {
        if (songSheetMusicAdapter == null) {
            return;
        }
        Observable.fromArray(songSheetMusicAdapter.getData())
                .flatMap(new Function<List<MusicIndex.ItemInfoListBean.MusicBean>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull List<MusicIndex.ItemInfoListBean.MusicBean> dataBeen) throws Exception {
                        return Observable.fromIterable(dataBeen)
                                .filter(new Predicate<MusicIndex.ItemInfoListBean.MusicBean>() {
                                    @Override
                                    public boolean test(@NonNull MusicIndex.ItemInfoListBean.MusicBean dataBean) throws Exception {
                                        return dataBean.isIscheck();
                                    }
                                })
                                .map(new Function<MusicIndex.ItemInfoListBean.MusicBean, String>() {
                                    @Override
                                    public String apply(@NonNull MusicIndex.ItemInfoListBean.MusicBean dataBean) throws Exception {
                                        return dataBean.getSid() + "";
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
                    public void accept(@NonNull final String s) throws Exception {


                        BaseConfirmDialog.Companion.newInstance()
                                .confirmText("删除")
                                .cancleText("手滑了")
                                .title("删除歌曲")
                                .content("确定要删除歌曲编号《" + s + "》？")
                                .tips("删除后歌曲消失，且不可恢复")
                                .showDialog(SongSheetDetailsActivity.this, new BaseConfirmDialog.onBtClick() {
                                    @Override
                                    public void onConfirm() {

                                        HttpParams params = new HttpParams();
                                        params.put("id", s);
                                        NetWork.INSTANCE.delFromSongSheet(SongSheetDetailsActivity.this, params, new NetWork.TokenCallBack() {
                                            @Override
                                            public void doNext(String resultData, Headers headers) {
                                                setSnackBar("删除成功", "", R.drawable.icon_success);
                                                backBtnEvent();
                                            }

                                            @Override
                                            public void doError(String msg) {
                                                Log.e(TAG, "doError: " + msg);
                                                Log.e(TAG, "从歌单删除！doError！！！: ");

                                            }

                                            @Override
                                            public void doResult() {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancle() {

                                    }
                                });

                    }
                });
    }

    private void rightBtnEvent() {
        MobclickAgent.onEvent(SongSheetDetailsActivity.this, "album_detail_more");
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
                .setCollection(itemInfoBean.getIsCollection())
                .setMusic_name(itemInfoBean.getTitle())
                .setSongName(itemInfoBean.getTitle())
                .setUid(itemInfoBean.getUid())
                .setCommentNum(itemInfoBean.getComment())
                .setImgpic(itemInfoBean.getImgpic())
                .setSignature(itemInfoBean.getSignature())
                .setSong_id(itemInfoBean.getId())
                .setReportType(2).setCanEdit(canEdit)
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
        app_bar.setExpanded(expanded, true);
        if (isEdit == true) {
            llPlayAll.setVisibility(View.GONE);
            tvEditMore.setVisibility(View.GONE);
            checkSong.setVisibility(View.VISIBLE);
            layoutFooter.setVisibility(View.VISIBLE);

            //判断吧是收藏的歌单吧删除歌曲影藏
            if(null!=type&&(!type.equals("")||type!="")){
                if(type.equals("2")||type=="2"){
                    tvCancleCollection.setVisibility(View.GONE);
                }
            }
//            if (songSheetMusicAdapter != null) {
//                checkSong.setText("全选(" + count + "/" + songSheetMusicAdapter.getData().size() + ")");
//            }
        } else {
            llPlayAll.setVisibility(View.VISIBLE);
            tvEditMore.setVisibility(View.VISIBLE);
            checkSong.setVisibility(View.GONE);
            layoutFooter.setVisibility(View.GONE);
        }
        songSheetMusicAdapter.setEdit(isEdit);
        songSheetMusicAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        itemInfoBean = null;
        RxBus.getDefault().remove(mPlayerMusicRefreshData);
        RxBus.getDefault().remove(mSongDetailsRefreshData);
    }

    enum State {
        Expanded,
        Collapsed,
        Ide
    }

    private void bindPondData(MusicIndex.ItemInfoListBean item) {
        pond_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        pond_recyclerview.setNestedScrollingEnabled(false);
        PondAdapter musicDetailsPondAdapter = new PondAdapter(item.getPondList());
        pond_recyclerview.setAdapter(musicDetailsPondAdapter);
    }

    private void bindMusicData(MusicIndex.ItemInfoListBean item) {
        List<MusicIndex.ItemInfoListBean.MusicBean> musics = item.getMusic();
        song_recyclerview.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        song_recyclerview.setLayoutManager(linearLayoutManager);
//        songSheetMusicAdapter = new SongSheetMusicListAdapter(musics, getSupportFragmentManager(), itemInfoBean.getTitle(), isEdit);
        //为了播放我创建的歌单中的mv，需要传过去歌单的id用来做操作
        songSheetMusicAdapter = new SongSheetMusicListAdapter(musics, getSupportFragmentManager(), id, isEdit);
        songSheetMusicAdapter.setCheckedSongListener(new SongSheetMusicListAdapter.ClickCheckedSongListener() {
            @Override
            public void onChecked(@Nullable Boolean aBoolean, int position) {
                countMusic();
                songSheetMusicAdapter.notifyItemChanged(position);
            }

            @Override
            public void onRefreshData() {

            }
        });
        song_recyclerview.setAdapter(songSheetMusicAdapter);
    }

    private void bindRecommendData(MusicIndex.ItemInfoListBean item) {
        if (item.getRecommend() == null || item.getRecommend().size() == 0) {
            ll_recommend.setVisibility(View.GONE);
            return;
        }
        song_sheet_recyclerview.setNestedScrollingEnabled(false);
        song_sheet_recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        if (song_sheet_recyclerview.getAdapter() == null) {
            song_sheet_recyclerview.addItemDecoration(new GridSpacingItemDecoration(3, CommonUtils.INSTANCE.dip2px(this, 6), CommonUtils.INSTANCE.dip2px(this, 6), true));
            songSheetRecommendListAdapter = new SongSheetRecommendListAdapter(item.getRecommend());
            song_sheet_recyclerview.setAdapter(songSheetRecommendListAdapter);
        }
        if (isEdit) {
            song_sheet_recyclerview.setVisibility(View.GONE);
            tv_reco_song.setVisibility(View.GONE);
        } else {
            song_sheet_recyclerview.setVisibility(View.VISIBLE);
            tv_reco_song.setVisibility(View.VISIBLE);
            if (song_sheet_recyclerview == null) return;
            song_recyclerview.scrollToPosition(0);
        }
    }

    /**
     * 循环计数
     */
    private void countMusic() {
        final List<MusicIndex.ItemInfoListBean.MusicBean> dataBeanList = songSheetMusicAdapter.getData();
        Observable.fromArray(dataBeanList)
                .flatMap(new Function<List<MusicIndex.ItemInfoListBean.MusicBean>, ObservableSource<MusicIndex.ItemInfoListBean.MusicBean>>() {
                    @Override
                    public ObservableSource<MusicIndex.ItemInfoListBean.MusicBean> apply(@NonNull List<MusicIndex.ItemInfoListBean.MusicBean> dataBeen) throws Exception {
                        return Observable.fromIterable(dataBeen)
                                .filter(new Predicate<MusicIndex.ItemInfoListBean.MusicBean>() {
                                    @Override
                                    public boolean test(@NonNull MusicIndex.ItemInfoListBean.MusicBean dataBean) throws Exception {
                                        return dataBean.isIscheck();
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
                        count++;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        tv_title.setText("已选" + count + "/" + dataBeanList.size() + "首歌曲");
                        checkSong.setText("全选(" + count + "/" + songSheetMusicAdapter.getData().size() + ")");
                        checkSelectClickView(count);
                        if (dataBeanList.size() == 0) {
                            checkSong.setChecked(false);
                            return;
                        }
                        if (count == dataBeanList.size()) {
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

    /**
     * 全选，取消全选
     */
    private void CheckAll() {
        final List<MusicIndex.ItemInfoListBean.MusicBean> dataBeanList = songSheetMusicAdapter.getData();
        Observable.fromArray(dataBeanList)
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
                        //这里在全选的时候判断那条数据是否被下架，下架的不能选中
                          if(dataBean.getStatus() == 0){
                              dataBean.setIscheck(false);
                          }else {
                              dataBean.setIscheck(checkSong.isChecked());
                          }
                      }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        songSheetMusicAdapter.setNewData(dataBeanList);
                        countMusic();
                    }
                });
    }


    @Override
    public void onBackPressed() {
        backBtnEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void backBtnEvent() {
        tv_close.setVisibility(View.GONE);
        iv_more.setVisibility(View.VISIBLE);
        ll_other.setVisibility(View.VISIBLE);
        count = 0;
        checkSong.setChecked(false);
        if (isEdit) {
            isEdit = false;
            CheckAll();
            checkView(true);
            app_bar.setExpanded(true, true);
            initData();
        } else {
            finish();
        }
    }
}
