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
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity;
import com.mxkj.yuanyintang.mainui.search.SheetBean;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.search.adapter.Sheet;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout;
import com.umeng.message.proguard.P;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Headers;
import static com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity.SONG_SHEET_ID;

/**
 * Created by LiuJie on 2017/10/12.
 */
@SuppressLint("ValidFragment")
public class SheetSearchList extends Fragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.swipe_refresh)
    SuperSwipeRefreshLayout swipeRefresh;
    private View view;
    private List<SheetBean.DataBean> dataBeanList = new ArrayList<>();
    private Sheet sheetAdapter;
    private int page = 1;
    private String s_title = "";
    private Unbinder unbinder;

    InterfaceRefreshLoadMore interfaceRefreshLoadMore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.searchresult_music, null);
        unbinder = ButterKnife.bind(this, view);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        s_title = getArguments().getString("title");
        sheetAdapter = new Sheet(dataBeanList, s_title);
        recycler.setAdapter(sheetAdapter);

        sheetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent_detial = new Intent(getActivity(), SongSheetDetailsActivity.class);
                intent_detial.putExtra(SONG_SHEET_ID, dataBeanList.get(position).getId() + "");
                getActivity().startActivity(intent_detial);
            }
        });

        initData();

        interfaceRefreshLoadMore = new InterfaceRefreshLoadMore(swipeRefresh, getActivity(), new InterfaceRefreshLoadMore.RefreshLoadMoreCallback() {
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

        return view;
    }

    private void initData() {
        if (getArguments().getString("title") != null) {
            NetWork.INSTANCE.search(getActivity(), "3", s_title, page, new NetWork.TokenCallBack() {

                @Override
                public void doNext(String resultData, Headers headers) {
                    interfaceRefreshLoadMore.setStopRefreshing();
                    JSONObject o = JSON.parseObject(resultData).getJSONObject("data");
                    JSONObject count = o.getJSONObject("count");

                    String string = JSON.toJSONString(o.getJSONArray("data"));
                    List<SheetBean.DataBean> dataBeen = JSON.parseArray(string, SheetBean.DataBean.class);
                    if (dataBeen != null && dataBeen.size() > 0) {
                        if (page == 1) {
                            dataBeanList.clear();
                        }
                        dataBeanList.addAll(dataBeen);
                        sheetAdapter.setNewData(dataBeanList);
                    } else {
//                            swipeRefresh.setLoadMore(false);
                        if (page == 1) {//什么都没有
                            sheetAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.nothing_layout_search, null));
                        } else {//没有更多了
                            sheetAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.no_more_data_text, null));
                        }
                        if(null!=swipeRefresh){
                            swipeRefresh.setLoadMore(false);
                        }
                        if (sheetAdapter != null && sheetAdapter.getFooterLayoutCount() > 0) {
                            if (recycler.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && (recycler.isComputingLayout() == false)) {
                                sheetAdapter.removeAllFooterView();
                                if (page == 1) {//什么都没有
                                    sheetAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.nothing_layout_search, null));
                                } else {//没有更多了
                                    sheetAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.no_more_data_text, null));
                                }
                            }
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
}
