package com.mxkj.yuanyintang.mainui.myself.activity;

import android.Manifest;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.flyco.dialog.widget.popup.BasePopup;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration;
import com.mxkj.yuanyintang.mainui.myself.apdater.NearbyPeopleAdapter;
import com.mxkj.yuanyintang.mainui.myself.bean.NearbyPeopleBean;
import com.mxkj.yuanyintang.utils.app.CommonUtils;
import com.mxkj.yuanyintang.utils.uiutils.Toast;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber;
import com.mxkj.yuanyintang.utils.rxbus.event.PlayerMusicRefreshDataEvent;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

/**
 * Created by LiuJie on 2017/10/23.
 */

public class NearbyPeopleActivity extends StandardUiActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.superSwipeRefreshLayout)
    SuperSwipeRefreshLayout superSwipeRefreshLayout;
    private int page = 1;
    private NearbyPeopleAdapter nearbyPeopleAdapter;
    List<NearbyPeopleBean.DataBean> dataBeanList = new ArrayList<>();
    private String selectTab;

    private Double latitude = 0D;
    private Double longitude = 0D;
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mLocationClient = null;

    private Disposable mPlayerMusicRefreshData;

    InterfaceRefreshLoadMore interfaceRefreshLoadMore;

    @Override
    public int setLayoutId() {
        return R.layout.activity_nearby_people;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        selectTab = "";
        setTitleText("附近");
        setRightButtonImageView(ContextCompat.getDrawable(this, R.drawable.icon_more_black));
        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MobclickAgent.onEvent(NearbyPeopleActivity.this,"home_near_back");
                        finish();
                    }
                });
        RxView.clicks(getNavigationBar().getRightButton())
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MobclickAgent.onEvent(NearbyPeopleActivity.this,"home_near_choose");

                        SimpleCustomPop mQuickCustomPopup = new SimpleCustomPop(NearbyPeopleActivity.this);
                        mQuickCustomPopup.anchorView(getNavigationBar().getRightButton())
                                .offset(-10, 0)
                                .gravity(Gravity.BOTTOM)
                                .showAnim(null)
                                .dismissAnim(null)
                                .dimEnabled(true)
                                .show();
                    }
                });
        initRecyclerView();
        getLocalPosition();
    }

    private void getLocalPosition() {
        new RxPermissions(NearbyPeopleActivity.this).requestEach(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            //初始化定位
                            mLocationClient = new AMapLocationClient(getApplicationContext());
                            //设置定位回调监听
                            mLocationClient.setLocationListener(mLocationListener);
                            //初始化定位参数
                            mLocationOption = new AMapLocationClientOption();
                            //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
                            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                            //设置是否返回地址信息（默认返回地址信息）
                            mLocationOption.setNeedAddress(true);
                            //设置是否只定位一次,默认为false
                            mLocationOption.setOnceLocation(true);
                            //设置是否强制刷新WIFI，默认为强制刷新
                            mLocationOption.setWifiActiveScan(true);
                            //设置是否允许模拟位置,默认为false，不允许模拟位置
                            mLocationOption.setMockEnable(false);
                            //设置定位间隔,单位毫秒,默认为2000ms
                            mLocationOption.setInterval(2000);
                            //给定位客户端对象设置定位参数
                            mLocationClient.setLocationOption(mLocationOption);
                            //启动定位
                            mLocationClient.startLocation();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        } else {

                        }
                    }
                });

    }

    private void netData(Double latitude, Double longitude) {
        showLoadingView();
        NetWork.INSTANCE.getMemberNearby(this, latitude, longitude, page, selectTab, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                hideLoadingView();
                NearbyPeopleBean nearbyPeopleBean = JSON.parseObject(resultData, NearbyPeopleBean.class);
                if (null != nearbyPeopleBean) {
                    refreshData(nearbyPeopleBean);
                }
            }

            @Override
            public void doError(String msg) {
                hideLoadingView();
                if (page > 1) {
                    page--;
                }
                interfaceRefreshLoadMore.setStopRefreshing();
            }

            @Override
            public void doResult() {

            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nearbyPeopleAdapter = new NearbyPeopleAdapter(dataBeanList);
        recyclerView.setAdapter(nearbyPeopleAdapter);
        interfaceRefreshLoadMore = new InterfaceRefreshLoadMore(superSwipeRefreshLayout, this, new InterfaceRefreshLoadMore.RefreshLoadMoreCallback() {
            @Override
            public void onRefresh() {
                page = 1;
                netData(latitude, longitude);
            }

            @Override
            public void onLoadMore() {
                page++;
                netData(latitude, longitude);
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

    }

    private void refreshData(NearbyPeopleBean nearbyPeopleBean) {
        interfaceRefreshLoadMore.setStopRefreshing();
        if (page == 1) {
            dataBeanList.clear();
        }
        dataBeanList.addAll(nearbyPeopleBean.getData());
        nearbyPeopleAdapter.setNewData(dataBeanList);
    }

    @Override
    protected void initEvent() {
        mPlayerMusicRefreshData = RxBus.getDefault().toObservable(PlayerMusicRefreshDataEvent.class)
                .subscribeWith(new RxBusSubscriber<PlayerMusicRefreshDataEvent>() {
                    @Override
                    protected void onEvent(PlayerMusicRefreshDataEvent playerMusicRefreshDataEvent) throws Exception {
                        if (null != nearbyPeopleAdapter) {
                            nearbyPeopleAdapter.notifyDataSetChanged();
                        }
                    }
                });
        RxBus.getDefault().add(mPlayerMusicRefreshData);
    }

    class SimpleCustomPop extends BasePopup<SimpleCustomPop> {
        @BindView(R.id.tv_look_online_people)
        TextView tv_look_online_people;
        @BindView(R.id.tv_opposite_people)
        TextView tv_look_opposite_people;
        @BindView(R.id.tv_opposite_all)
        TextView tv_opposite_all;

        public SimpleCustomPop(Context context) {
            super(context);
            setCanceledOnTouchOutside(true);
        }

        @Override
        public View onCreatePopupView() {
            View inflate = View.inflate(mContext, R.layout.pop_layout_nearby_sort, null);
            ButterKnife.bind(this, inflate);
            return inflate;
        }

        @Override
        public void setUiBeforShow() {
            tv_look_online_people.setTextColor(ContextCompat.getColor(NearbyPeopleActivity.this, R.color.color_17_text));
            tv_look_opposite_people.setTextColor(ContextCompat.getColor(NearbyPeopleActivity.this, R.color.color_17_text));
            tv_opposite_all.setTextColor(ContextCompat.getColor(NearbyPeopleActivity.this, R.color.color_17_text));
            switch (selectTab) {
                case "2":
                    tv_look_online_people.setTextColor(ContextCompat.getColor(NearbyPeopleActivity.this, R.color.base_red));
                    break;
                case "1":
                    tv_look_opposite_people.setTextColor(ContextCompat.getColor(NearbyPeopleActivity.this, R.color.base_red));
                    break;
                case "":
                    tv_opposite_all.setTextColor(ContextCompat.getColor(NearbyPeopleActivity.this, R.color.base_red));
                    break;
            }
            tv_look_online_people.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectTab = "2";
                    page = 1;
                    netData(latitude, longitude);
                    dismiss();
                }
            });
            tv_look_opposite_people.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectTab = "1";
                    page = 1;
                    netData(latitude, longitude);
                    dismiss();
                }
            });
            tv_opposite_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectTab = "";
                    page = 1;
                    netData(latitude, longitude);
                    dismiss();
                }
            });
        }
    }

    private AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    int locationType = aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    double lat = aMapLocation.getLatitude();//获取纬度
                    double longit = aMapLocation.getLongitude();//获取经度
                    latitude = lat;
                    longitude = longit;
                    netData(latitude, longitude);
                } else {
                    Toast.create(NearbyPeopleActivity.this).show("未获取到定位" + aMapLocation.getErrorCode());
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
        interfaceRefreshLoadMore.resetRefreshView();
        RxBus.getDefault().remove(mPlayerMusicRefreshData);
    }
}
