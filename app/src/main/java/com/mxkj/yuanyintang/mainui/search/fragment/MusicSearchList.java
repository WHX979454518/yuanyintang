package com.mxkj.yuanyintang.mainui.search.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.mainui.search.SearchResultActivity;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.search.adapter.Music;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Headers;
/**
 * Created by LiuJie on 2017/10/12.
 */
@SuppressLint("ValidFragment")
public class MusicSearchList extends Fragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.swipe_refresh)
    SuperSwipeRefreshLayout swipeRefresh;
    private View view;
    private List<MusicSearchBean.DataBeanX.DataBean> dataBeanList = new ArrayList<>();
    private Music musicAdapter;
    private int page = 1;
    private String s_title;
    private Unbinder unbinder;
    InterfaceRefreshLoadMore interfaceRefreshLoadMore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.searchresult_music, null);
        unbinder = ButterKnife.bind(this, view);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        s_title = getArguments().getString("title");
        musicAdapter = new Music(dataBeanList, s_title, getChildFragmentManager());
        recycler.setAdapter(musicAdapter);
        initData();
        musicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(getActivity(), MusicDetailsActivity.class);
//                intent.putExtra(MUSIC_ID, dataBeanList.get(position).getId() + "");
//                getActivity().startActivity(intent);
                PlayCtrlUtil.INSTANCE.play(getActivity(),dataBeanList.get(position).getId(),0);


            }
        });
        interfaceRefreshLoadMore = new InterfaceRefreshLoadMore(swipeRefresh, getActivity(), new InterfaceRefreshLoadMore.RefreshLoadMoreCallback() {
            @Override
            public void onRefresh() {
                page = 1;
                if (musicAdapter != null && musicAdapter.getFooterLayoutCount() > 0) {
                    if (recycler.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && (recycler.isComputingLayout() == false)) {
                        musicAdapter.removeAllFooterView();
                    }
                }
                initData();
            }

            @Override
            public void onLoadMore() {
                page++;
                if (musicAdapter != null && musicAdapter.getFooterLayoutCount() > 0) {
                    if (recycler.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && (recycler.isComputingLayout() == false)) {

                        musicAdapter.removeAllFooterView();
                    }
                }
                initData();
            }

            @Override
            public void onPushDistance(int distance) {

            }

            @Override
            public void onPullDistance(int distance) {

            }
        });
        return view;
    }

    private void initData() {
        if (getArguments().getString("title") != null) {
            NetWork.INSTANCE.search(getActivity(), "1", s_title, page, new NetWork.TokenCallBack() {
                @Override
                public void doNext(String resultData, Headers headers) {
                    interfaceRefreshLoadMore.setStopRefreshing();
                    JSONObject jsonObject = JSON.parseObject(resultData);
                    JSONObject data2 = jsonObject.getJSONObject("data");
                    if (data2 == null) {
                        return;
                    }
                    JSONObject count = data2.getJSONObject("count");
                    if (count == null) {
                        return;
                    }
                    Integer musicCount = count.getInteger("musicCount");
                    Integer songCount = count.getInteger("songCount");
                    Integer musicianCount = count.getInteger("musicianCount");
                    Integer topicCount = count.getInteger("topicCount");
//                    ((SearchResultActivity) getActivity()).setUserCounts(songCount);
                    if(null!=songCount){
                        ((SearchResultActivity) getActivity()).setUserCounts(songCount);
                    }else {

                    }
//                    if (getActivity() != null) {
                    if (null!=getActivity()&&null!=songCount) {
                        ((SearchResultActivity) getActivity()).setSheetCounts(songCount);
                        ((SearchResultActivity) getActivity()).setMusicCounts(musicCount);
                        ((SearchResultActivity) getActivity()).setUserCounts(musicianCount);
                        ((SearchResultActivity) getActivity()).setPondCounts(topicCount);
                        if (musicCount == 0) {
                            if (songCount == 0) {
                                if (musicianCount == 0) {
                                    if (topicCount == 0) {
                                        ((SearchResultActivity) getActivity()).setCurrPage(0);
                                    } else {
                                        ((SearchResultActivity) getActivity()).setCurrPage(3);
                                    }
                                } else {
                                    ((SearchResultActivity) getActivity()).setCurrPage(2);
                                }
                            } else {
                                ((SearchResultActivity) getActivity()).setCurrPage(1);
                            }
                        }


                    }
                    JSONArray data3 = data2.getJSONArray("data");
                    String string = data3.toJSONString();
                    List<MusicSearchBean.DataBeanX.DataBean> dataBeenlist = JSON.parseArray(string, MusicSearchBean.DataBeanX.DataBean.class);

//                    List<MusicSearchBean.DataBeanX.DataBean> newdataBeenlist = JSON.parseArray(string, MusicSearchBean.DataBeanX.DataBean.class);

                    if (dataBeenlist != null && dataBeenlist.size() > 0) {
                        if (page == 1) {
                            dataBeanList.clear();
                        }
                        dataBeanList.addAll(dataBeenlist);
                        musicAdapter.notifyDataSetChanged();
                    } else {
                        swipeRefresh.setLoadMore(false);
//                        if (musicAdapter != null && musicAdapter.getFooterLayoutCount() > 0) {
//                            if (recycler.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && (recycler.isComputingLayout() == false)) {
//                                musicAdapter.removeAllFooterView();
//                                if (page == 1) {//什么都没有
//                                    musicAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.nothing_layout_search, null));
//                                } else {//没有更多了
//                                    musicAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.no_more_data_text, null));
//                                }
//                            }
//                        }
                        if (page == 1) {//什么都没有
                            musicAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.nothing_layout_search, null));
                        } else {//没有更多了
                            musicAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.no_more_data_text, null));
                        }

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        interfaceRefreshLoadMore.resetRefreshView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
//        initData();
    }
}
