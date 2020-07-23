package com.mxkj.yuanyintang.mainui.home.music_charts.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity;
import com.mxkj.yuanyintang.mainui.home.music_charts.adapter.DoughnutsAdapter;
import com.mxkj.yuanyintang.mainui.home.music_charts.bean.ChartsListBean;
import com.mxkj.yuanyintang.mainui.login_regist.QuickLoginActivityNew;
import com.mxkj.yuanyintang.musicplayer.activity.PlayerActivity;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber;
import com.mxkj.yuanyintang.utils.rxbus.event.PlayerMusicRefreshDataEvent;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity.CHART_ID;
import static com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity.CHART_TIME_TYPE;
import static com.mxkj.yuanyintang.musicplayer.service.MediaService.bean;

public class DoughnutsFragment extends RxFragment {
    private static final String TAG = "MusicChartsFragment";

    private RecyclerView recyclerview;
    private SuperSwipeRefreshLayout superSwipeRefreshLayout;
    private TextView tv_no_data;
    InterfaceRefreshLoadMore interfaceRefreshLoadMore;

    private int type, id;
    private String title;
    private DoughnutsAdapter musiChartAdapter;
    private List<ChartsListBean.DataBean.DataListBean> musicChartBeanList = new ArrayList<>();
    private List<ChartsListBean.DataBean.DataListBean> headBeanList = new ArrayList<>();
    private View rootView;
    boolean isAllPlaying;
    private RxBusSubscriber<PlayerMusicRefreshDataEvent> mPlayerMusicRefreshData;


    View chartsHeaderView;
    TextView tv_score_second;
    TextView tv_song_second;
    TextView support_second;
    TextView support_second2;
    TextView tv_nick_second;
    ImageView img_second;
    ImageView img_play_second;
    private int count = 1;
    RelativeLayout rl_first,rl_two,rl_three;
    /*1*/
    TextView tv_score_first;
    TextView support_first;
    TextView support_first2;

    TextView tv_song_first;
    TextView tv_nick_first;
    ImageView img_first;
    ImageView img_play_first;
    /*3*/
    TextView tv_score_third;
    TextView support_third;
    TextView support_third2;
    TextView tv_song_third;
    TextView tv_nick_third;
    ImageView img_third;
    ImageView img_play_third, img_all_playing;
    private DiaLogBuilder supportDialog;
    private int buttonId;
    private List<DoughnutsAdapter.RewardBean.DataBean.RewardListBean> reward_list;
    private int my_support_count;
    private DoughnutsAdapter.RewardBean.DataBean data;
    public ChartsListBean.DataBean.ShareInfoBean share_info;
    private LinearLayout ll_play_all;

    private ImageView notoing_img;
    private TextView nooting_tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_charts, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initEvent();
        return rootView;
    }

    private void initEvent() {
        mPlayerMusicRefreshData = RxBus.getDefault().toObservable(PlayerMusicRefreshDataEvent.class)
                .subscribeWith(new RxBusSubscriber<PlayerMusicRefreshDataEvent>() {
                    @Override
                    protected void onEvent(PlayerMusicRefreshDataEvent playerMusicRefreshDataEvent) throws Exception {
                        if (null != musiChartAdapter) {
                            netData();
                        }
                    }
                });
        RxBus.getDefault().add(mPlayerMusicRefreshData);
        id = getArguments().getInt(CHART_ID, 0);
        type = getArguments().getInt(CHART_TIME_TYPE, 0);
        title = getArguments().getString("title");
        tv_no_data = rootView.findViewById(R.id.tv_no_data);
        ll_play_all = rootView.findViewById(R.id.ll_play_all);
        notoing_img = rootView.findViewById(R.id.notoing_img);
        nooting_tv  = rootView.findViewById(R.id.nooting_tv);
        img_all_playing = rootView.findViewById(R.id.img_all_playing);
        initRecyclerView();
        netData();
        ll_play_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isAllPlaying == true) {
                    isAllPlaying = false;
                    img_all_playing.setImageResource(R.mipmap.icon_index_songlist_play3x);
                    getActivity().sendBroadcast(new Intent(MediaService.ACTION_PAUSE));
                } else {
                    isAllPlaying = true;
                    img_all_playing.setImageResource(R.mipmap.icon_index_songlist_play2);
                    if (MediaService.mediaPlayer != null && MediaService.bean != null && !MediaService.mediaPlayer.isPlaying()) {
                        getActivity().sendBroadcast(new Intent(MediaService.ACTION_PAUSE));
                    } else {
//                        PlayCtrlUtil.playChartsList(getActivity(), musicChartBeanList);
                        PlayCtrlUtil.INSTANCE.playChartsList(getActivity(), id, type);
                    }
                }
            }
        });
    }

    private void netData() {
        if (id == 0) {
            return;
        }
        HttpParams params = new HttpParams();
        params.put("class_id", id + "");
        params.put("type", type + "");
        params.put("row", 30 + "");
        NetWork.INSTANCE.getChartsList(getActivity(), 1, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                interfaceRefreshLoadMore.setStopRefreshing();
                ChartsListBean chartsListBean = JSON.parseObject(resultData, ChartsListBean.class);
                if (chartsListBean == null) {
                    return;
                }
                ChartsListBean.DataBean data = chartsListBean.getData();
                /*
                * 类别信息
                * */
                ChartsListBean.DataBean.ClassInfoBean class_info = data.getClass_info();
                if (class_info != null) {
                    String title = class_info.getTitle();
                    ((ChartsListsActivity) getActivity()).navTitleTextViews.setText(StringUtils.isEmpty(title));
                }else {
                    ll_play_all.setVisibility(View.GONE);
                    superSwipeRefreshLayout.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.GONE);
                    notoing_img.setVisibility(View.VISIBLE);
                    nooting_tv.setVisibility(View.VISIBLE);
                }
                /*分享信息*/
                share_info = data.getShare_info();
                if (share_info != null) {
                    ((ChartsListsActivity) getActivity()).setShareInfo(share_info);
                }
                List<ChartsListBean.DataBean.DataListBean> dataList = data.getData_list();
                if (dataList != null && dataList.size() > 0) {
                    headBeanList.clear();
                    musicChartBeanList.clear();
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.size() > 3) {
                            if (chartsHeaderView != null && musiChartAdapter.getHeaderLayoutCount() == 0) {
                                musiChartAdapter.addHeaderView(chartsHeaderView);
                            }
                        }
                        if (dataList.size() >= 3 && i < 3) {
                            headBeanList.add(dataList.get(i));
                        } else {
                            musicChartBeanList.add(dataList.get(i));
                        }
                    }
                    initHeaderEvent();
                    musiChartAdapter.setNewData(musicChartBeanList);
                    interfaceRefreshLoadMore.setStopRefreshing();
                    if (musiChartAdapter.getFooterLayoutCount() == 0) {
                        musiChartAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.no_more_data_text, null));
                    }
                } else {
                    musiChartAdapter.removeAllHeaderView();
                    isNotDataView();
                }
            }

            @Override
            public void doError(String msg) {
                interfaceRefreshLoadMore.setStopRefreshing();
            }

            @Override
            public void doResult() {

            }
        });
    }

    private void initRecyclerView() {
        superSwipeRefreshLayout = rootView.findViewById(R.id.superSwipeRefreshLayout);
        superSwipeRefreshLayout.setEnabled(false);
        recyclerview = rootView.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (recyclerview.getAdapter() == null) {
            musiChartAdapter = new DoughnutsAdapter(musicChartBeanList, type, id,title);
            recyclerview.setAdapter(musiChartAdapter);
            initHeaderView();
        }
        interfaceRefreshLoadMore = new InterfaceRefreshLoadMore(superSwipeRefreshLayout, getActivity(), new InterfaceRefreshLoadMore.RefreshLoadMoreCallback() {
            @Override
            public void onRefresh() {
                netData();
            }

            @Override
            public void onLoadMore() {
            }

            @Override
            public void onPushDistance(int distance) {

            }

            @Override
            public void onPullDistance(int distance) {

            }
        });
    }

    private void isNotDataView() {
        superSwipeRefreshLayout.setLoadMore(false);
        if (null != tv_no_data) {
            if (musicChartBeanList.size() > 0) {
                tv_no_data.setVisibility(View.GONE);
            } else {
                tv_no_data.setVisibility(View.VISIBLE);
                ll_play_all.setVisibility(View.GONE);
                ll_play_all.setVisibility(View.GONE);
                notoing_img.setVisibility(View.VISIBLE);
                nooting_tv.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.getDefault().remove(mPlayerMusicRefreshData);
    }

    private void initHeaderView() {
        chartsHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_dough_charts_head, null);
         /*2*/
        rl_two = chartsHeaderView.findViewById(R.id.rl_two);
        tv_score_second = chartsHeaderView.findViewById(R.id.tv_score_second);
        tv_song_second = chartsHeaderView.findViewById(R.id.tv_song_second);
        tv_nick_second = chartsHeaderView.findViewById(R.id.tv_nick_second);
        img_second = chartsHeaderView.findViewById(R.id.img_second);
        img_play_second = chartsHeaderView.findViewById(R.id.img_play_second);
        support_first = chartsHeaderView.findViewById(R.id.support_first);
        support_first2 = chartsHeaderView.findViewById(R.id.support_first2);
        /*1*/
        rl_first = chartsHeaderView.findViewById(R.id.rl_first);
        tv_score_first = chartsHeaderView.findViewById(R.id.tv_score_first);
        tv_song_first = chartsHeaderView.findViewById(R.id.tv_song_first);
        tv_nick_first = chartsHeaderView.findViewById(R.id.tv_nick_first);
        img_first = chartsHeaderView.findViewById(R.id.img_first);
        img_play_first = chartsHeaderView.findViewById(R.id.img_play_first);
        support_second = chartsHeaderView.findViewById(R.id.support_second);
        support_second2 = chartsHeaderView.findViewById(R.id.support_second2);
           /*3*/
        rl_three = chartsHeaderView.findViewById(R.id.rl_three);
        tv_score_third = chartsHeaderView.findViewById(R.id.tv_score_third);
        tv_song_third = chartsHeaderView.findViewById(R.id.tv_song_third);
        tv_nick_third = chartsHeaderView.findViewById(R.id.tv_nick_third);
        img_third = chartsHeaderView.findViewById(R.id.img_third);
        img_play_third = chartsHeaderView.findViewById(R.id.img_play_third);
        support_third = chartsHeaderView.findViewById(R.id.support_third);
        support_third2 = chartsHeaderView.findViewById(R.id.support_third2);

        if(title.equals("音乐打赏榜") || title == "音乐打赏榜"){

        }else {
            support_first.setVisibility(View.GONE);
            support_second.setVisibility(View.GONE);
            support_third.setVisibility(View.GONE);
            support_first2.setVisibility(View.VISIBLE);
            support_second2.setVisibility(View.VISIBLE);
            support_third2.setVisibility(View.VISIBLE);
        }
    }

    private void initHeaderEvent() {
        if(type == 1){
//            support_first.setVisibility(View.GONE);
//            support_second.setVisibility(View.GONE);
//            support_third.setVisibility(View.GONE);
        }
        if (headBeanList.size() > 2) {
            /*1*/
            final ChartsListBean.DataBean.DataListBean dataListBeanFirst = headBeanList.get(0);
            tv_score_first.setText(dataListBeanFirst.getTtq_total_desc() + "");
            tv_song_first.setText(dataListBeanFirst.getTitle());


            if (MediaService.mediaPlayer != null && MediaService.mediaPlayer.isPlaying()) {
                if(bean != null && bean.getId() == dataListBeanFirst.getMusic_id()){
                    dataListBeanFirst.setPlaying(true);
                    tv_song_first.setTextColor(Color.parseColor("#ff6699"));
                    tv_nick_first.setTextColor(Color.parseColor("#ff6699"));
                }
            }
            if(dataListBeanFirst.isPlaying()){
                tv_song_first.setTextColor(Color.parseColor("#ff6699"));
                tv_nick_first.setTextColor(Color.parseColor("#ff6699"));
            }else {
                tv_song_first.setTextColor(Color.parseColor("#2b2b2b"));
                tv_nick_first.setTextColor(Color.parseColor("#9da2a6"));
            }
            rl_first.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(count == 2 && dataListBeanFirst.isPlaying()){
                        Intent intent = new Intent(getActivity(),PlayerActivity.class);
                        startActivity(intent);
                    }else {
                        PlayCtrlUtil.INSTANCE.play(getActivity(),dataListBeanFirst.getMusic_id(),0);
                        count = 2;
                    }
                }
            });
            if(type == 1){
                tv_nick_first.setText("本日打赏：" + dataListBeanFirst.getCoin());
            }else if(type == 2){
                tv_nick_first.setText("本周打赏：" + dataListBeanFirst.getCoin());
            }else if(type == 3){
                tv_nick_first.setText("本月打赏：" + dataListBeanFirst.getCoin());
            }
            img_first = chartsHeaderView.findViewById(R.id.img_first);
            img_play_first = chartsHeaderView.findViewById(R.id.img_play_first);
            updatePlayStatus(0, img_play_first, img_first, support_first, dataListBeanFirst);
            /*2*/
            final ChartsListBean.DataBean.DataListBean dataListBeanSecond = headBeanList.get(1);
            tv_score_second.setText(dataListBeanSecond.getTtq_total_desc() + "");
            tv_song_second.setText(dataListBeanSecond.getTitle());
            if (MediaService.mediaPlayer != null && MediaService.mediaPlayer.isPlaying()) {
                if(bean != null && bean.getId() == dataListBeanSecond.getMusic_id()){
                    dataListBeanSecond.setPlaying(true);
                    tv_song_second.setTextColor(Color.parseColor("#ff6699"));
                    tv_nick_second.setTextColor(Color.parseColor("#ff6699"));
                }
            }
            if(dataListBeanSecond.isPlaying()){
                tv_song_second.setTextColor(Color.parseColor("#ff6699"));
                tv_nick_second.setTextColor(Color.parseColor("#ff6699"));
            }else {
                tv_song_second.setTextColor(Color.parseColor("#2b2b2b"));
                tv_nick_second.setTextColor(Color.parseColor("#9da2a6"));
            }
            rl_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(count == 2 && dataListBeanSecond.isPlaying()){
                        Intent intent = new Intent(getActivity(),PlayerActivity.class);
                        startActivity(intent);
                    }else {
                        PlayCtrlUtil.INSTANCE.play(getActivity(),dataListBeanSecond.getMusic_id(),0);
                        count = 2;
                    }
                }
            });

            if(type == 1){
                tv_nick_second.setText("本日打赏：" + dataListBeanSecond.getCoin());
            }else if(type == 2){
                tv_nick_second.setText("本周打赏：" + dataListBeanSecond.getCoin());
            }else if(type == 3){
                tv_nick_second.setText("本月打赏：" + dataListBeanSecond.getCoin());
            }
            img_second = chartsHeaderView.findViewById(R.id.img_second);
            img_play_second = chartsHeaderView.findViewById(R.id.img_play_second);
            updatePlayStatus(1, img_play_second, img_second, support_second, dataListBeanSecond);

            /*3*/
            final ChartsListBean.DataBean.DataListBean dataListBeanThird = headBeanList.get(2);
            tv_score_third.setText(dataListBeanThird.getTtq_total_desc() + "");
            tv_song_third.setText(dataListBeanThird.getTitle());
            if (MediaService.mediaPlayer != null && MediaService.mediaPlayer.isPlaying()) {
                if(bean != null && bean.getId() == dataListBeanThird.getMusic_id()){
                    dataListBeanThird.setPlaying(true);
                    tv_song_third.setTextColor(Color.parseColor("#ff6699"));
                    tv_nick_third.setTextColor(Color.parseColor("#ff6699"));
                }
            }
            if(dataListBeanThird.isPlaying()){
                tv_song_third.setTextColor(Color.parseColor("#ff6699"));
                tv_nick_third.setTextColor(Color.parseColor("#ff6699"));
            }else {
                tv_song_third.setTextColor(Color.parseColor("#2b2b2b"));
                tv_nick_third.setTextColor(Color.parseColor("#9da2a6"));
            }
            rl_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(count == 2 && dataListBeanThird.isPlaying()){
                        Intent intent = new Intent(getActivity(),PlayerActivity.class);
                        startActivity(intent);
                    }else {
                        PlayCtrlUtil.INSTANCE.play(getActivity(),dataListBeanThird.getMusic_id(),0);
                        count = 2;
                    }
                }
            });


            if(type == 1){
                tv_nick_third.setText("本日打赏：" + dataListBeanThird.getCoin());
            }else if(type == 2){
                tv_nick_third.setText("本周打赏：" + dataListBeanThird.getCoin());
            }else if(type == 3){
                tv_nick_third.setText("本月打赏：" + dataListBeanThird.getCoin());
            }
            img_third = chartsHeaderView.findViewById(R.id.img_third);
            img_play_third = chartsHeaderView.findViewById(R.id.img_play_third);
            updatePlayStatus(2, img_play_third, img_third, support_third, dataListBeanThird);
        }
    }

    public void supportHim(final int position) {
        int music_id = headBeanList.get(position).getMusic_id();
        ((ChartsListsActivity) getActivity()).showBotomGiftDialog(music_id);
    }

    private void updatePlayStatus(final int position, ImageView playIcon, ImageView img_bck, TextView support_view, final ChartsListBean.DataBean.DataListBean dataListBean) {
        if (dataListBean.getImgpic_info() != null) {
            ImageLoader.with(MainApplication.Companion.getApplication()).getSize(100, 100).rectRoundCorner(2).url(dataListBean.getImgpic_info().getLink()).into(img_bck);
        }

        if (null != MediaService.bean) {
            if (MediaService.bean.getId() == dataListBean.getMusic_id()) {
                if (MediaService.getMediaPlayer().isPlaying()) {
                    playIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon_music_player_white_normal_true));
                } else {
                    playIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon_music_player_white_normal_false));
                }
            } else {
                playIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon_music_player_white_normal_false));
            }
        } else {
            playIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon_music_player_white_normal_false));
        }
        playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayCtrlUtil.INSTANCE.play(getActivity(), dataListBean.getMusic_id(), 0);
            }
        });
        img_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), MusicDetailsActivity.class);
//                intent.putExtra(MUSIC_ID, dataListBean.getMusic_id() + "");
//                getActivity().startActivity(intent);
                PlayCtrlUtil.INSTANCE.play(getActivity(),dataListBean.getMusic_id(),0);
            }
        });

        support_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CacheUtils.INSTANCE.getBoolean(MainApplication.Companion.getApplication(), Constants.User.IS_LOGIN)) {
                    supportHim(position);
                } else {
                    getActivity().startActivity(new Intent(getActivity(), QuickLoginActivityNew.class));
                }
            }
        });

    }


}
