package com.mxkj.yuanyintang.mainui.pond;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.widget.popup.BasePopup;
import com.jakewharton.rxbinding2.view.RxView;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew;
import com.mxkj.yuanyintang.mainui.pond.adapter.PondRecyclerAdapterNew;
import com.mxkj.yuanyintang.mainui.pond.bean.PondBeanNew;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.home.utils.FeedRootRecyclerView;
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration;
import com.mxkj.yuanyintang.utils.app.CommonUtils;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.VpSuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

public class TagDetialPondList extends StandardUiActivity {
    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.view_title)
    TextView viewTitle;
    @BindView(R.id.menu_right_top)
    ImageView menuRightTop;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.recyclerview)
    FeedRootRecyclerView mRecyclerview;
    @BindView(R.id.swipe_refresh)
    VpSuperSwipeRefreshLayout swipeRefresh;
    List<PondBeanNew.DataBean.DataListBean> dataBeanList = new ArrayList<>();
    private PondRecyclerAdapterNew pondRecyclerAdapter;
    private String selectTab;
    private int page = 1;

    public static final String TAG_ID = "_tag_id";
    public static final String TAG_TITLE = "_tag_title";
    private int tagId;

    InterfaceRefreshLoadMore interfaceRefreshLoadMore;

    @Override
    public int setLayoutId() {
        return R.layout.activity_tag_detial_pond;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        hideTitle(true);
        ButterKnife.bind(this);
        selectTab = "update_time-desc";
        tagId = getIntent().getIntExtra(TAG_ID, 0);
        viewTitle.setText(StringUtils.isEmpty(getIntent().getStringExtra(TAG_TITLE)));
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.addItemDecoration(new MyRecyclerDetorration(this, LinearLayoutManager.VERTICAL, CommonUtils.INSTANCE.dip2px(this, 5), R.color.bg_color,true));
        pondRecyclerAdapter = new PondRecyclerAdapterNew(dataBeanList, viewTitle.getText().toString());
        pondRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(TagDetialPondList.this, PondDetialActivityNew.class);
                intent.putExtra("pid", dataBeanList.get(position).getId());
                startActivity(intent);
            }
        });
        mRecyclerview.setAdapter(pondRecyclerAdapter);
    }

    @Override
    protected void initEvent() {
        interfaceRefreshLoadMore = new InterfaceRefreshLoadMore(swipeRefresh, TagDetialPondList.this, new InterfaceRefreshLoadMore.RefreshLoadMoreCallback() {
            @Override
            public void onRefresh() {
                page = 1;
                initPondList();

            }

            @Override
            public void onLoadMore() {
                page++;
                initPondList();
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
        initPondList();
    }

    private void initPondList() {
        HttpParams params = new HttpParams();
        params.put("tag", tagId);
        params.put("p", page + "");
        params.put("order", selectTab);
        NetWork.INSTANCE.getPond("","", this, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                interfaceRefreshLoadMore.setStopRefreshing();
                Log.e(TAG, "doNext: " + resultData);
                PondBeanNew pondBean = JSON.parseObject(resultData, PondBeanNew.class);
                List<PondBeanNew.DataBean.DataListBean> data = pondBean.getData().getData_list();
                if (page == 1) {
                    dataBeanList.clear();
                }
                dataBeanList.addAll(data);
                Log.e(TAG, "doNext: " + dataBeanList.size());
                pondRecyclerAdapter.setNewData(dataBeanList);
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

    @OnClick({R.id.finish, R.id.menu_right_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.menu_right_top:
                SimpleCustomPop mQuickCustomPopup = new SimpleCustomPop(TagDetialPondList.this);
                mQuickCustomPopup.anchorView(menuRightTop)
                        .offset(-10, 0)
                        .gravity(Gravity.BOTTOM)
                        .showAnim(null)
                        .dismissAnim(null)
                        .dimEnabled(true)
                        .show();
                break;
        }
    }

    private class SimpleCustomPop extends BasePopup<SimpleCustomPop> {
        TextView tv_default;
        TextView tv_newest;
        TextView tv_at_most_comment;

        public SimpleCustomPop(Context context) {
            super(context);
            setCanceledOnTouchOutside(true);
        }

        @Override
        public View onCreatePopupView() {
            View inflate = View.inflate(mContext, R.layout.sort_pond_tag_detial, null);
            tv_default = inflate.findViewById(R.id.tv_default);
            tv_newest = inflate.findViewById(R.id.tv_newest);
            tv_at_most_comment = inflate.findViewById(R.id.tv_at_most_comment);
            tv_default.setTextColor(ContextCompat.getColor(TagDetialPondList.this, R.color.color_17_text));
            tv_newest.setTextColor(ContextCompat.getColor(TagDetialPondList.this, R.color.color_17_text));
            tv_at_most_comment.setTextColor(ContextCompat.getColor(TagDetialPondList.this, R.color.color_17_text));
            switch (selectTab) {
                case "update_time-desc":
                    tv_default.setTextColor(ContextCompat.getColor(TagDetialPondList.this, R.color.base_red));
                    break;
                case "create_time-desc":
                    tv_newest.setTextColor(ContextCompat.getColor(TagDetialPondList.this, R.color.base_red));
                    break;
                case "thcount-desc":
                    tv_at_most_comment.setTextColor(ContextCompat.getColor(TagDetialPondList.this, R.color.base_red));
                    break;
            }
            return inflate;
        }

        @Override
        public void setUiBeforShow() {
            RxView.clicks(tv_default)
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            selectTab = "update_time-desc";
                            page = 1;

                            initPondList();
                            dismiss();
                        }
                    });
            RxView.clicks(tv_newest)//最新发布
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            selectTab = "create_time-desc";
                            page = 1;

                            initPondList();
                            dismiss();
                        }
                    });
            RxView.clicks(tv_at_most_comment)//人气
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            selectTab = "thcount-desc";
                            page = 1;

                            initPondList();
                            dismiss();
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        interfaceRefreshLoadMore.resetRefreshView();
    }
}
