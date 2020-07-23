package com.mxkj.yuanyintang.mainui.home.fragment;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.fragment.BaseLazyFragment;
import com.mxkj.yuanyintang.mainui.newapp.pond.PondAdapter;
import com.mxkj.yuanyintang.mainui.newapp.pond.PondInfo;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.mainui.home.adapter.MusicIanPondAdapter;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIanPondBean;
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration;
import com.mxkj.yuanyintang.utils.app.CommonUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class PondFragment extends BaseLazyFragment implements AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = "PondFragment";
    private RecyclerView recyclerview;
    private TextView tv_no_data;
    String id;
    int page = 1;
    PondAdapter musicIanPondAdapter;
    List<PondInfo.DataBean.DataListBean> dataBeanList = new ArrayList<>();
    private String data;

    public void setData(String data) {
        this.data = data;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_musician_pond;
    }

    @Override
    protected void onFirstVisibleToUser() {
        id = getArguments().getString("id");
        tv_no_data = findViewById(R.id.tv_no_data);
        initRecyclerView();
        addListener();
        netData();
    }

    private void netData() {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        NetWork.INSTANCE.getMusicianTopic(getActivity(), page, id, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                JSONObject jsonObject = JSON.parseObject(resultData);
                String jsonArray = jsonObject.getJSONArray("data").toJSONString();
                List list = JSON.parseArray(jsonArray, PondInfo.DataBean.DataListBean.class);
                if (list != null && list.size() > 0) {
                    if (page == 1) {
                        dataBeanList.clear();
                    }
                    dataBeanList.addAll(list);
                    musicIanPondAdapter.notifyDataSetChanged();
                } else {
                    if (page > 1 && musicIanPondAdapter.getFooterLayoutCount() == 0) {
                        musicIanPondAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.no_more_data_text, null));
                    }
                }
                isNotDataView();
            }

            @Override
            public void doError(String msg) {
                if (page > 1) {
                    page--;
                }
            }

            @Override
            public void doResult() {

            }
        });
    }

    private void initRecyclerView() {
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addItemDecoration(new MyRecyclerDetorration(getActivity(), LinearLayoutManager.VERTICAL, CommonUtils.INSTANCE.dip2px(getActivity(), 5), R.color.bg_color, true));
        musicIanPondAdapter = new PondAdapter(dataBeanList);
        recyclerview.setAdapter(musicIanPondAdapter);
        if (!TextUtils.isEmpty(data)) {
            page++;
            List<PondInfo.DataBean.DataListBean> list = JSON.parseArray(data, PondInfo.DataBean.DataListBean.class);
            dataBeanList.clear();
            dataBeanList.addAll(list);
            musicIanPondAdapter.notifyDataSetChanged();
            isNotDataView();
        }
    }

    @Override
    protected void onVisibleToUser() {
        addListener();
    }

    @Override
    protected void onInvisibleToUser() {
        removeListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void isNotDataView() {
        if (null != tv_no_data) {
            if (dataBeanList.size() > 0) {
                tv_no_data.setVisibility(View.GONE);
            } else {
                tv_no_data.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addListener() {
        MusicIanDetailsActivity activity = (MusicIanDetailsActivity) getActivity();
        if (activity != null && !activity.isFinishing() && activity instanceof MusicIanDetailsActivity) {
            (activity).addListener(this);
        }
    }

    private void removeListener() {
        MusicIanDetailsActivity activity = (MusicIanDetailsActivity) getActivity();
        if (activity != null && !activity.isFinishing() && activity instanceof MusicIanDetailsActivity) {
            (activity).removeListener(this);
        }
    }

    int verticalOffset;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        this.verticalOffset = verticalOffset;
        if (dataBeanList.size() > 0) {
            if (verticalOffset == 0) {
            }
        } else {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("musician_pond");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MobclickAgent.onPageEnd("musician_pond");

    }

}
