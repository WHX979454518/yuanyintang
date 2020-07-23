//package com.mxkj.yuanyintang.mainui.myself;
//
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.widget.NestedScrollView;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.hyphenate.chat.EMClient;
//import com.jakewharton.rxbinding2.view.RxView;
//import com.mxkj.yuanyintang.base.MainApplication;
//import com.mxkj.yuanyintang.R;
//import com.mxkj.yuanyintang.base.activity.HomeActivity;
//import com.mxkj.yuanyintang.base.bean.UserInfo;
//import com.mxkj.yuanyintang.base.fragment.BaseFragment;
//import com.mxkj.yuanyintang.mainui.home.activity.LikesMusicActivity;
//import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity;
//import com.mxkj.yuanyintang.mainui.home.bean.HomeIndex;
//import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
//import com.mxkj.yuanyintang.mainui.myself.doughnut.MyDoughnutActivityNew;
//import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew;
//import com.mxkj.yuanyintang.mainui.web.CommonWebview;
//import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
//import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
//import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
//import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
//import com.mxkj.yuanyintang.mainui.message.activity.MessageCenterActivity;
//import com.mxkj.yuanyintang.mainui.myself.activity.MyFollowAndFansActivity;
//import com.mxkj.yuanyintang.mainui.myself.apdater.MySelfAdapter;
//import com.mxkj.yuanyintang.mainui.myself.bean.MySelfBean;
//import com.mxkj.yuanyintang.utils.rxbus.RxBus;
//import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
//import com.mxkj.yuanyintang.utils.string.StringUtils;
//import com.mxkj.yuanyintang.utils.constant.Constants;
//import com.mxkj.yuanyintang.utils.file.CacheUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.annotations.NonNull;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Consumer;
//import io.reactivex.functions.Predicate;
//import io.reactivex.schedulers.Schedulers;
//
//import static com.mxkj.yuanyintang.mainui.home.fragment.HomeFragment.SHOW_SYS_MSG;
//import static com.mxkj.yuanyintang.mainui.home.fragment.HomeFragment.STR_SYS_MSG;
//public class MySelfFragment extends BaseFragment {
//    private RecyclerView my_recyclerview;
//    private RecyclerView music_recyclerview;
//    private View v_statusbar;
//    private ImageView iv_head_bg;
//    private NestedScrollView scrollview;
//    private FrameLayout layout_title;
//    private RelativeLayout rl_msg_center;
//    private LinearLayout layout_personal, layout_follow, layout_fans, layout_doughnut;
//    private TextView tv_msg_count, tv_name, tv_sign, tv_follow_num, tv_fans_num, tv_doughnut_num;
//    private CircleImageView civ_headimg;
//    private int height = 0;
//    MySelfAdapter myAdapter, musicAdapter;
//    List<MySelfBean> myDataList = new ArrayList<>();
//    List<MySelfBean> musicDataList = new ArrayList<>();
//    private static final String TAG = "MySelfFragment";
//    private UserInfo userInfo;
//    private HomeActivity homeActivity;
//    private Disposable disposable;
//    private RelativeLayout rl_msg;
//
//    @Override
//    public int getLayoutId() {
//        StatusBarUtil.baseTransparentStatusBar(getActivity());
//        sysMsgEvent();
//        return R.layout.fragment_my_self;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        homeActivity = (HomeActivity) getActivity();
//        initHeight();
//        initView();
//    }
//
//    public void outLoginView() {
//        if (null == tv_name) {
//            return;
//        }
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                tv_name.setText("登录源音塘");
//                tv_sign.setText("");
//                tv_follow_num.setText("-");
//                tv_fans_num.setText("-");
//                tv_doughnut_num.setText("-");
//                ImageLoader.with(getActivity())
//                        .res(R.drawable.icon_not_login)
//                        .asCircle()
//                        .scale(ScaleMode.CENTER_CROP)
//                        .into(civ_headimg);
//            }
//        });
//    }
//
//    private void initHeight() {
//        rl_msg_center = findViewById(R.id.rl_msg_center);
//        v_statusbar = findViewById(R.id.v_statusbar);
//        iv_head_bg = findViewById(R.id.iv_head_bg);
//        scrollview = findViewById(R.id.scrollview);
//        layout_title = findViewById(R.id.layout_title);
//        tv_msg_count = findViewById(R.id.tv_msg_count);
//        rl_msg = findViewById(R.id.rl_msg);
//
//        civ_headimg = findViewById(R.id.civ_headimg);
//        tv_name = findViewById(R.id.tv_name);
//        tv_sign = findViewById(R.id.tv_sign);
//        tv_follow_num = findViewById(R.id.tv_follow_num);
//        tv_fans_num = findViewById(R.id.tv_fans_num);
//        tv_doughnut_num = findViewById(R.id.tv_doughnut_num);
//        height = getResources().getDimensionPixelOffset(R.dimen.dimen_40);
//
//        int statusHeight = StatusBarUtil.getStatusBarHeight(getActivity());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, statusHeight);
//            v_statusbar.setLayoutParams(params);
//            height += statusHeight;
//        }
//        layout_title.getBackground().setAlpha(0);
//        scrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                //处理标题的透明度
//                if (scrollY <= height && scrollY >= 0) {
//                    float scale = (float) scrollY / height;
//                    float alpha = (255 * scale);
//                    layout_title.getBackground().setAlpha((int) alpha);
//                } else if (scrollY > height) {
//                    layout_title.getBackground().setAlpha(255);
//                }
//            }
//        });
//        RxView.clicks(rl_msg_center)
//                .throttleFirst(2, TimeUnit.SECONDS)
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(@NonNull Object o) throws Exception {
//                        getActivity().startActivity(new Intent(getActivity(), MessageCenterActivity.class));
//                    }
//                });
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        RxBus.getDefault().remove(disposable);
//
//    }
//
//    private void initView() {
//        my_recyclerview = findViewById(R.id.my_recyclerview);
//        music_recyclerview = findViewById(R.id.music_recyclerview);
//        layout_personal = findViewById(R.id.layout_personal);
//        layout_follow = findViewById(R.id.layout_follow);
//        layout_fans = findViewById(R.id.layout_fans);
//        layout_doughnut = findViewById(R.id.layout_doughnut);
//        my_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 4));
//        music_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 4));
//        my_recyclerview.setNestedScrollingEnabled(false);
//        music_recyclerview.setNestedScrollingEnabled(false);
//
//        myDataList.add(new MySelfBean(R.drawable.myself_collection, "我的收藏"));
//        myDataList.add(new MySelfBean(R.drawable.myself_song_sheet, "我的歌单"));
//        myDataList.add(new MySelfBean(R.drawable.myself_player_histroy, "播放历史"));
//        myDataList.add(new MySelfBean(R.drawable.myself_local_histroy, "本地记录"));
//        myDataList.add(new MySelfBean(R.drawable.myself_pond, "我的池塘"));
//        myDataList.add(new MySelfBean(R.drawable.icon_mine_people, "附近的人"));
////        myDataList.add(new MySelfBean(R.drawable.myself_feedback, "意见反馈"));
//        myDataList.add(new MySelfBean(R.drawable.myself_icon_help_center, "帮助中心"));
//        myDataList.add(new MySelfBean(R.drawable.myself_setting, "设置"));
//        musicDataList.add(new MySelfBean(R.drawable.myself_real_name, "实名认证"));
//        musicDataList.add(new MySelfBean(R.drawable.myself_release, "我的发布"));
//        musicDataList.add(new MySelfBean(R.drawable.icon_mine_money, "我的收益"));
//        myAdapter = new MySelfAdapter(myDataList, getActivity(), "1");
//        musicAdapter = new MySelfAdapter(musicDataList, getActivity(), "2");
//        my_recyclerview.setAdapter(myAdapter);
//        music_recyclerview.setAdapter(musicAdapter);
//        initMsgView();
//
//        setMsgCount(homeActivity.getMsgUnReadNum());
//
//        RxView.clicks(layout_personal)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(@NonNull Object o) throws Exception {
//                        if (CacheUtils.INSTANCE.getBoolean(getActivity(), Constants.User.IS_LOGIN)) {
//                            if (null != userInfo && null != userInfo.getData()) {
//                                Bundle bundle = new Bundle();
//                                bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, userInfo.getData().getId() + "");
//                                goActivity(MusicIanDetailsActivity.class, bundle);
//                            }
//                        } else {
//                            goActivity(LoginRegMainPage.class);
//                        }
//                    }
//                });
//        RxView.clicks(layout_follow)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(@NonNull Object o) throws Exception {
//                        if (CacheUtils.INSTANCE.getBoolean(getActivity(), Constants.User.IS_LOGIN)) {
//                            if (null == userInfo) {
//                                return;
//                            }
//
//                            Bundle bundle = new Bundle();
//                            bundle.putString(MyFollowAndFansActivity.TITLE_NAME, "我的关注");
//                            bundle.putString(MyFollowAndFansActivity.TYPE, "follow");
//                            bundle.putString(MyFollowAndFansActivity.NOT_DATA_TEXT, "您还没有关注哦~");
//                            bundle.putString(MyFollowAndFansActivity.TO_CLICK_TEXT, "去和音乐人互动");
//                            bundle.putInt(MyFollowAndFansActivity.COUNT, userInfo.getData().getCount().getAttention());
//                            goActivity(MyFollowAndFansActivity.class, bundle);
//                        } else {
//                            goActivity(LoginRegMainPage.class);
//                        }
//                    }
//                });
//        RxView.clicks(layout_fans)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(@NonNull Object o) throws Exception {
//                        if (CacheUtils.INSTANCE.getBoolean(getActivity(), Constants.User.IS_LOGIN)) {
//                            if (null == userInfo) {
//                                return;
//                            }
//                            Bundle bundle = new Bundle();
//                            bundle.putString(MyFollowAndFansActivity.TITLE_NAME, "我的粉丝");
//                            bundle.putString(MyFollowAndFansActivity.TYPE, "fans");
//                            bundle.putString(MyFollowAndFansActivity.NOT_DATA_TEXT, "您还没有粉丝哦~");
//                            bundle.putString(MyFollowAndFansActivity.TO_CLICK_TEXT, "去冒泡");
//                            bundle.putInt(MyFollowAndFansActivity.COUNT, userInfo.getData().getCount().getFans());
//                            goActivity(MyFollowAndFansActivity.class, bundle);
//
//                        } else {
//                            goActivity(LoginRegMainPage.class);
//                        }
//                    }
//                });
//        RxView.clicks(layout_doughnut)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(@NonNull Object o) throws Exception {
//                        if (CacheUtils.INSTANCE.getBoolean(getActivity(), Constants.User.IS_LOGIN)) {
//                            goActivity(MyDoughnutActivityNew.class);
//                        } else {
//                            goActivity(LoginRegMainPage.class);
//                        }
//                    }
//                });
//    }
//
//    private void initMsgView() {
//        if (CacheUtils.INSTANCE.getString(getActivity(),STR_SYS_MSG) != null) {
//            final HomeIndex.ItemInfoListBean.SystemMsgBean systemMsgBean = JSON.parseObject(CacheUtils.INSTANCE.getString(getActivity(), STR_SYS_MSG), HomeIndex.ItemInfoListBean.SystemMsgBean.class);
//            View sysMsgHeadView = LayoutInflater.from(MainApplication.Companion.getApplication()).inflate(R.layout.homerecycle_item_system_msg, null);
//            TextView tv_system_msg = sysMsgHeadView.findViewById(R.id.tv_system_msg);
//            ImageView hide_sys_msg = sysMsgHeadView.findViewById(R.id.hide_sys_msg);
//            if (systemMsgBean != null) {
//                tv_system_msg.setText(systemMsgBean.getText());
//            }
//            LinearLayout layout_system_msg = sysMsgHeadView.findViewById(R.id.layout_system_msg);
//            hide_sys_msg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    EMClient.getInstance().chatManager().markAllConversationsAsRead();
//                    CacheUtils.INSTANCE.setBoolean(getActivity(), SHOW_SYS_MSG, false);
//                    CacheUtils.INSTANCE.setString(getActivity(), STR_SYS_MSG, null);
//                    RxBus.getDefault().post(systemMsgBean);
//                }
//            });
//            sysMsgHeadView.setClickable(true);
//            sysMsgHeadView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    msgClickGo(systemMsgBean.getType(), systemMsgBean.getUrl(), systemMsgBean.getId() + "");
//                }
//            });
//            rl_msg.addView(layout_system_msg);
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Observable.just(CacheUtils.INSTANCE.getBoolean(getActivity(), Constants.User.IS_LOGIN, false))
//                .filter(new Predicate<Boolean>() {
//                    @Override
//                    public boolean test(@NonNull Boolean aBoolean) throws Exception {
//                        return aBoolean;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(@NonNull Boolean aBoolean) throws Exception {
//                        netData();
//                    }
//                });
//    }
//
//    private void netData() {
//        Log.e(TAG, "netData:getUserInfoById ");
//        UserInfoUtil.getUserInfoById(0, getActivity(), new UserInfoUtil.UserCallBack() {
//            @Override
//            public void doNext(UserInfo infoBean) {
//                if (infoBean != null && infoBean.getData() != null) {
//                    userInfo = infoBean;
//                    if (userInfo.getData() != null && userInfo.getData().getHead_link() != null) {
//                        ImageLoader.with(getActivity())
//                                .override(80, 80)
//                                .url(userInfo.getData().getHead_link())
//                                .asCircle()
//                                .scale(ScaleMode.CENTER_CROP)
//                                .into(civ_headimg);
//                        tv_name.setText(StringUtils.isEmpty(userInfo.getData().getNickname()));
//                        tv_sign.setText(StringUtils.isEmpty(userInfo.getData().getSignature()));
//                        if (userInfo.getData().getCount() != null) {
//                            tv_follow_num.setText(StringUtils.setNum(userInfo.getData().getCount().getAttention()));
//                            tv_fans_num.setText(StringUtils.setNum(userInfo.getData().getCount().getFans()));
//                        }
//                        tv_doughnut_num.setText(StringUtils.setNum(userInfo.getData().getCoin_counts()));
//                    }
//                }
//
//            }
//        });
//    }
//
//    public void setMsgCount(int msgCount) {
//        if (null != tv_msg_count) {
//            if (getActivity() != null && !CacheUtils.INSTANCE.getBoolean(getActivity(), Constants.User.IS_LOGIN)) {
//                tv_msg_count.setVisibility(View.GONE);
//                return;
//            }
//            if (msgCount <= 0) {
//                tv_msg_count.setVisibility(View.GONE);
//            } else {
//                tv_msg_count.setVisibility(View.VISIBLE);
//                tv_msg_count.setText(msgCount > 9 ? "9+" : msgCount + "");
//            }
//        }
//    }
//
//    private void sysMsgEvent() {
//        disposable = RxBus.getDefault().toObservable(HomeIndex.ItemInfoListBean.SystemMsgBean.class)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<HomeIndex.ItemInfoListBean.SystemMsgBean>() {
//                    @Override
//                    public void accept(final HomeIndex.ItemInfoListBean.SystemMsgBean systemMsgBean) throws Exception {
//                        if (CacheUtils.INSTANCE.getBoolean(getActivity(), SHOW_SYS_MSG, true) == false) {
//                            rl_msg.removeAllViews();
//                        }
//                    }
//                });
//        RxBus.getDefault().add(disposable);
//    }
//
//    private void msgClickGo(String type, String url, String id) {
//        if (type != null && type.equals("page")) {
//            Bundle bundle = new Bundle();
//            switch (url) {
//                case "home":
//                    break;
//                case "topicDetails":
//                    if (!TextUtils.isEmpty(id)) {
//                        bundle.putInt(PondDetialActivityNew.PID, Integer.parseInt(id));
//                        goActivity(PondDetialActivityNew.class, bundle);
//                    }
//                    break;
//                case "musicDetails":
//                    PlayCtrlUtil.INSTANCE.play(getActivity(),Integer.valueOf(id),0);
//                    break;
//                case "musicianDetailHome":
//                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id);
//                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 0);
//                    goActivity(MusicIanDetailsActivity.class, bundle);
//                    break;
//                case "musicianDetailMusic":
//                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id);
//                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 1);
//                    goActivity(MusicIanDetailsActivity.class, bundle);
//                    break;
//                case "musicianDetailSongSheet":
//                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id);
//                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 2);
//                    goActivity(MusicIanDetailsActivity.class, bundle);
//                    break;
//                case "musicianDetailTopic":
//                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id);
//                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 3);
//                    goActivity(MusicIanDetailsActivity.class, bundle);
//                    break;
//                case "songSheetDetails":
//                    bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, id);
//                    goActivity(SongSheetDetailsActivity.class, bundle);
//                    break;
//                case "likesSongSheetDetails":
//                    bundle.putString(LikesMusicActivity.MUSICIAN_ID, id);
//                    goActivity(LikesMusicActivity.class, bundle);
//                    break;
//            }
//        } else if (type != null && type.equals("activity")) {
//            Intent intent = new Intent(getActivity(), CommonWebview.class);
//            intent.putExtra("url", url);
//            intent.putExtra("activity", "activity");
//            getActivity().startActivity(intent);
//        }
//    }
//
//}
