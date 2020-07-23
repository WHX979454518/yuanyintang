package com.mxkj.yuanyintang.mainui.home.music_charts.fragment;

import android.content.Intent;
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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity;
import com.mxkj.yuanyintang.mainui.home.music_charts.adapter.MusicChartsAdapter;
import com.mxkj.yuanyintang.mainui.home.music_charts.bean.ChartsListBean;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.layoutmanager.FullyLinearLayoutManager;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber;
import com.mxkj.yuanyintang.utils.rxbus.event.PlayerMusicRefreshDataEvent;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity.CHART_ID;
import static com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity.CHART_TIME_TYPE;

public class MusicChartsFragment extends RxFragment {
    private static final String TAG = "MusicChartsFragment";
    private RecyclerView recyclerview;
    private SuperSwipeRefreshLayout superSwipeRefreshLayout;
    private TextView tv_no_data;
    InterfaceRefreshLoadMore interfaceRefreshLoadMore;

    private int type, id;
    private MusicChartsAdapter musiChartAdapter;
    private List<ChartsListBean.DataBean.DataListBean> musicChartBeanList = new ArrayList<>();
    private List<ChartsListBean.DataBean.DataListBean> headBeanList = new ArrayList<>();
    private View rootView;
    private RxBusSubscriber<PlayerMusicRefreshDataEvent> mPlayerMusicRefreshData;
    View chartsHeaderView;
    TextView tv_score_second;
    TextView tv_song_second;
    TextView tv_nick_second;
    ImageView img_second;
    ImageView img_play_second;
    /*2*/
    TextView tv_score_first;
    TextView tv_song_first;
    TextView tv_nick_first;
    ImageView img_first;
    ImageView img_play_first;
    /*3*/
    TextView tv_score_third;
    TextView tv_song_third;
    TextView tv_nick_third;
    ImageView img_third;
    ImageView img_play_third,img_all_playing;
    private boolean isAllPlaying;
    private LinearLayout ll_play_all;

    public ChartsListBean.DataBean.ShareInfoBean share_info;

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
                            initHeaderEvent();
                            musiChartAdapter.setNewData(musicChartBeanList);//收到开始播放的消息，更新播放状态
                        }
                    }
                });
        RxBus.getDefault().add(mPlayerMusicRefreshData);
        id = getArguments().getInt(CHART_ID, 0);
        type = getArguments().getInt(CHART_TIME_TYPE, 0);
        tv_no_data = rootView.findViewById(R.id.tv_no_data);
        ll_play_all = rootView.findViewById(R.id.ll_play_all);
        img_all_playing = rootView.findViewById(R.id.img_all_playing);
        ll_play_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAllPlaying == true) {
                    isAllPlaying = false;
                    img_all_playing.setImageResource(R.mipmap.icon_index_songlist_play);
                    getActivity().sendBroadcast(new Intent(MediaService.ACTION_PAUSE));
                } else {
                    isAllPlaying = true;
                    img_all_playing.setImageResource(R.mipmap.icon_index_songlist_play2);
                    if (MediaService.mediaPlayer != null && MediaService.bean != null && !MediaService.mediaPlayer.isPlaying()) {
                        getActivity().sendBroadcast(new Intent(MediaService.ACTION_PAUSE));
                    } else {
                        PlayCtrlUtil.INSTANCE.playChartsList(getActivity(),id,type);
                    }
                }
            }
        });
        initRecyclerView();
        netData();
    }

    private void netData() {
        if (id == 0) {
            return;
        }
        HttpParams params = new HttpParams();
        params.put("class_id", id + "");
        params.put("type", type + "");
        NetWork.INSTANCE.getChartsList(getActivity(), 1, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                interfaceRefreshLoadMore.setStopRefreshing();
                ChartsListBean chartsListBean = JSON.parseObject(resultData, ChartsListBean.class);
                if (chartsListBean == null) {
                    return;
                }
                ChartsListBean.DataBean data = chartsListBean.getData();
                ChartsListBean.DataBean.ClassInfoBean class_info = data.getClass_info();
                share_info = data.getShare_info();
                if (share_info != null) {
                    ((ChartsListsActivity) getActivity()).setShareInfo(share_info);
                }
                final List<ChartsListBean.DataBean.DataListBean> dataList = data.getData_list();
                headBeanList.clear();
                musicChartBeanList.clear();
                if (dataList != null && dataList.size() > 0) {
                    if (chartsHeaderView != null&&musiChartAdapter.getHeaderLayoutCount()==0) {
                        musiChartAdapter.addHeaderView(chartsHeaderView);
                    }
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.size()>3&&i < 3) {
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
        recyclerview.setLayoutManager(new FullyLinearLayoutManager(getActivity()));
        if (recyclerview.getAdapter() == null) {
            musiChartAdapter = new MusicChartsAdapter(musicChartBeanList);
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
                netData();
            }

            @Override
            public void onPushDistance(int distance) {

            }

            @Override
            public void onPullDistance(int distance) {

            }
        });
        superSwipeRefreshLayout.setLoadMore(false);
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition == totalItemCount - 1
                        && visibleItemCount > 0) {
//                    page++;
//                    netData();
                }
            }
        });

    }

    private void initHeaderView() {
        chartsHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_dough_charts_head, null);
         /*1*/
        tv_score_second = chartsHeaderView.findViewById(R.id.tv_score_second);
        tv_song_second = chartsHeaderView.findViewById(R.id.tv_song_second);
        tv_nick_second = chartsHeaderView.findViewById(R.id.tv_nick_second);
        img_second = chartsHeaderView.findViewById(R.id.img_second);
        img_play_second = chartsHeaderView.findViewById(R.id.img_play_second);
        /*2*/
        tv_score_first = chartsHeaderView.findViewById(R.id.tv_score_first);
        tv_song_first = chartsHeaderView.findViewById(R.id.tv_song_first);
        tv_nick_first = chartsHeaderView.findViewById(R.id.tv_nick_first);
        img_first = chartsHeaderView.findViewById(R.id.img_first);
        img_play_first = chartsHeaderView.findViewById(R.id.img_play_first);
           /*3*/
        tv_score_third = chartsHeaderView.findViewById(R.id.tv_score_third);
        tv_song_third = chartsHeaderView.findViewById(R.id.tv_song_third);
        tv_nick_third = chartsHeaderView.findViewById(R.id.tv_nick_third);
        img_third = chartsHeaderView.findViewById(R.id.img_third);
        img_play_third = chartsHeaderView.findViewById(R.id.img_play_third);


    }

    private void initHeaderEvent() {
        if (headBeanList.size() > 2) {
            /*1*/
            ChartsListBean.DataBean.DataListBean dataListBeanFirst = headBeanList.get(0);
            tv_score_first.setText(dataListBeanFirst.getScore_total_desc() + "");
            tv_song_first.setText(dataListBeanFirst.getTitle());
            tv_nick_first.setText(dataListBeanFirst.getNickname());
            img_first = chartsHeaderView.findViewById(R.id.img_first);
            img_play_first = chartsHeaderView.findViewById(R.id.img_play_first);
            updatePlayStatus(img_play_first, dataListBeanFirst, img_first);

            /*2*/
            ChartsListBean.DataBean.DataListBean dataListBeanSecond = headBeanList.get(1);
            tv_score_second.setText(dataListBeanSecond.getScore_total_desc() + "");
            tv_song_second.setText(dataListBeanSecond.getTitle());
            tv_nick_second.setText(dataListBeanSecond.getNickname());
            img_second = chartsHeaderView.findViewById(R.id.img_second);

            img_play_second = chartsHeaderView.findViewById(R.id.img_play_second);
            updatePlayStatus(img_play_second, dataListBeanSecond, img_second);

            /*3*/
            ChartsListBean.DataBean.DataListBean dataListBeanThird = headBeanList.get(2);
            tv_score_third.setText(dataListBeanThird.getScore_total_desc() + "");
            tv_song_third.setText(dataListBeanThird.getTitle());
            tv_nick_third.setText(dataListBeanThird.getNickname());
            img_third = chartsHeaderView.findViewById(R.id.img_third);
            img_play_third = chartsHeaderView.findViewById(R.id.img_play_third);
            updatePlayStatus(img_play_third, dataListBeanThird, img_third);
        }
    }

    private void updatePlayStatus(ImageView img_play, final ChartsListBean.DataBean.DataListBean dataListBean, ImageView img_bck) {
        if (dataListBean.getImgpic_info() != null) {
            ImageLoader.with(MainApplication.Companion.getApplication()).getSize(100, 100).rectRoundCorner(2).url(dataListBean.getImgpic_info().getLink()).into(img_bck);
        }
        if (null != MediaService.bean) {
            if (MediaService.bean.getId() == dataListBean.getMusic_id()) {
                if (MediaService.getMediaPlayer().isPlaying()) {
                    img_play.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon_music_player_white_normal_true));
                } else {
                    img_play.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon_music_player_white_normal_false));
                }
            } else {
                img_play.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon_music_player_white_normal_false));
            }
        } else {
            img_play.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon_music_player_white_normal_false));
        }
        img_play.setOnClickListener(new View.OnClickListener() {
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
    }

    private void isNotDataView() {
        interfaceRefreshLoadMore.setStopRefreshing();
        if (null != tv_no_data) {
            if (musicChartBeanList.size() > 0) {
                tv_no_data.setVisibility(View.GONE);
            } else {
                tv_no_data.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.getDefault().remove(mPlayerMusicRefreshData);
        if (null != interfaceRefreshLoadMore) {
            interfaceRefreshLoadMore.resetRefreshView();
        }
        superSwipeRefreshLayout = null;
    }
}
