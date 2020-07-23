package com.mxkj.yuanyintang.mainui.home.music_charts.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity;
import com.mxkj.yuanyintang.mainui.home.music_charts.adapter.ChartsHomeNewActivityAdapter;
import com.mxkj.yuanyintang.mainui.home.music_charts.bean.ChartsHomeNewBean;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.constant.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.CHARTS_TYPE;
import static com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.EXPEN_CLASS_ID;
import static com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.HEAD_INFO_LINK;
import static com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.INCOME_CLASS_ID;

public class ChartsHomeActivity extends StandardUiActivity {
    @BindView(R.id.lv_charts)
    ListView lvCharts;

    RelativeLayout leftback;



    ChartsHomeNewActivityAdapter adapter;
    List<ChartsHomeNewBean.DataBean> dataList = new ArrayList<>();
    private int page = 1;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_charts_home;
    }

    @Override
    protected void initView() {
//        setTitleText("源音塘排行榜");
        hideLeftButton();
        hideRightButton();
        hideTitle(true);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        NetWork.INSTANCE.getChartsHomeList(this, page, new NetWork.TokenCallBack() {
//        NetWork.INSTANCE.getChartsHomeList(this, page, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                ChartsHomeNewBean chartsHomeNewBean = JSON.parseObject(resultData, ChartsHomeNewBean.class);
                List<ChartsHomeNewBean.DataBean> data = chartsHomeNewBean.getData();
                if (data != null && data.size() > 0) {
                    if (page == 1) {
                        dataList.clear();
                    }
                    dataList.addAll(data);
                    adapter = new ChartsHomeNewActivityAdapter(dataList,ChartsHomeActivity.this);
                    lvCharts.setAdapter(adapter);
//                    adapter.setList(dataList);
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
    protected void initEvent() {
//        adapter = new BaseBindingAdapter<ChartsHomeBean.DataBean, ChartsHomeItemBinding>(ChartsHomeActivity.this, dataList, R.layout.charts_home_item) {
//            @Override
//            public void bindObj(ChartsHomeItemBinding itemBinding, ChartsHomeBean.DataBean dataBean) {
//                itemBinding.setObj(dataBean);
//            }
//
//            @Override
//            public void operateView(View view, ChartsHomeBean.DataBean dataBean) {
//                super.operateView(view, dataBean);
//                ImageView imageView = view.findViewById(R.id.img_charts);
//                int anInt = CacheUtils.INSTANCE.getInt(ChartsHomeActivity.this, Constants.Other.WIDTH, 0);
//                if (dataBean != null && !TextUtils.isEmpty(dataBean.getImgpic_link())) {
//                    ImageLoader.with(MainApplication.Companion.getApplication()).url(dataBean.getImgpic_link()).into(imageView);
//                }
//            }
//        };
//        lvCharts.setAdapter(adapter);

        leftback = findViewById(R.id.leftback);
        leftback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        lvCharts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (dataList.size() > i) {
                    ChartsHomeNewBean.DataBean dataBean = dataList.get(i);
                    if (dataBean==null)return;
                    int type = dataBean.getType();
                    if (type == 1 || type == 2) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.Other.CHARTS_BEAN, dataBean);
                        goActivity(ChartsListsActivity.class, bundle);
                    } else if (type == 3 || type == 4) {
                        Intent intent = new Intent(ChartsHomeActivity.this, GiftChartsActivity.class);
                        if (type == 3) {
                            intent.putExtra(EXPEN_CLASS_ID, dataBean.getId());
                            intent.putExtra(INCOME_CLASS_ID, dataBean.getToggle_class().getId());
                            intent.putExtra(CHARTS_TYPE, 3);
                            intent.putExtra(HEAD_INFO_LINK, dataBean.getHead_info().getLink());

                        } else if (type == 4) {
                            intent.putExtra(INCOME_CLASS_ID, dataBean.getId());
                            intent.putExtra(EXPEN_CLASS_ID, dataBean.getToggle_class().getId());
                            intent.putExtra(CHARTS_TYPE, 4);
                            intent.putExtra(HEAD_INFO_LINK, dataBean.getHead_info().getLink());
                        }
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
