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
import com.mxkj.yuanyintang.mainui.pond.adapter.PondRecyclerAdapter;
import com.mxkj.yuanyintang.mainui.pond.adapter.PondRecyclerAdapterNew;
import com.mxkj.yuanyintang.mainui.pond.bean.PondBean;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration;
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew;
import com.mxkj.yuanyintang.utils.app.CommonUtils;
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
public class PondSearchList extends Fragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.swipe_refresh)
    SuperSwipeRefreshLayout swipeRefresh;
    private View view;
    private List<PondBean.DataBean> dataBeanList = new ArrayList<>();
    private PondRecyclerAdapter adapter;
    private int page = 1;
    private String s_title = "";
    private Unbinder unbinder;
    InterfaceRefreshLoadMore interfaceRefreshLoadMore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.searchresult_pond, null);
        unbinder = ButterKnife.bind(this, view);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        s_title = getArguments().getString("title");
        recycler.addItemDecoration(new MyRecyclerDetorration(getActivity(), LinearLayoutManager.VERTICAL, CommonUtils.INSTANCE.dip2px(getActivity(), 5), R.color.dividing_line_color, true));
        adapter = new PondRecyclerAdapter(dataBeanList, s_title);
        recycler.setAdapter(adapter);
        initData();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(PondDetialActivityNew.PID, dataBeanList.get(position).getId());
                Intent intent = new Intent(getActivity(), PondDetialActivityNew.class);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });

        interfaceRefreshLoadMore = new InterfaceRefreshLoadMore(swipeRefresh, getActivity(), new InterfaceRefreshLoadMore.RefreshLoadMoreCallback() {
            @Override
            public void onRefresh() {
                page = 1;
                if (adapter != null && adapter.getFooterLayoutCount() > 0) {
                    if (recycler.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && (recycler.isComputingLayout() == false)) {
                        adapter.removeAllFooterView();
                    }
                }
                initData();
            }

            @Override
            public void onLoadMore() {
                page++;
                if (adapter != null && adapter.getFooterLayoutCount() > 0) {
                    if (recycler.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && (recycler.isComputingLayout() == false)) {

                        adapter.removeAllFooterView();
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
            NetWork.INSTANCE.search(getActivity(), "4", s_title, page, new NetWork.TokenCallBack() {

                @Override
                public void doNext(String resultData, Headers headers) {
                    interfaceRefreshLoadMore.setStopRefreshing();
                    JSONObject o = JSON.parseObject(resultData).getJSONObject("data");
                    String string = JSON.toJSONString(o.getJSONArray("data"));
                    List<PondBean.DataBean> dataBeen = JSON.parseArray(string, PondBean.DataBean.class);
                    if (dataBeen != null && dataBeen.size() > 0) {
                        if (page == 1) {
                            dataBeanList.clear();
                        }
                        dataBeanList.addAll(dataBeen);
                        adapter.setNewData(dataBeanList);
                    } else {
                        swipeRefresh.setLoadMore(false);
//                        if (adapter != null && adapter.getFooterLayoutCount() > 0) {
//                            if (recycler.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && (recycler.isComputingLayout() == false)) {
//                                adapter.removeAllFooterView();
//                                if (page == 1) {//什么都没有
//                                    adapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.nothing_layout_search, null));
//                                } else {//没有更多了
//                                    adapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.no_more_data_text, null));
//                                }
//                            }
//                        }
                        if (page == 1) {//什么都没有
                            adapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.nothing_layout_search, null));
                        } else {//没有更多了
                            adapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.no_more_data_text, null));
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
