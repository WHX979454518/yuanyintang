package com.mxkj.yuanyintang.mainui.myself.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.mainui.myself.apdater.MyPondAdapter;
import com.mxkj.yuanyintang.mainui.myself.bean.MyPondBean;
import com.mxkj.yuanyintang.mainui.pond.activity.PublishPond;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber;
import com.mxkj.yuanyintang.utils.rxbus.event.MyPondRefreshEvent;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

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
 * Created by LiuJie on 2017/10/10.
 */

public class MyPondActivity extends StandardUiActivity {
    @BindView(R.id.recyclerview)
    SwipeMenuRecyclerView recyclerview;
    @BindView(R.id.head_recyclerview)
    RecyclerView head_recyclerview;
    @BindView(R.id.layout_popupmenu)
    FrameLayout layout_popupmenu;
    @BindView(R.id.view_close)
    View view_close;
    @BindView(R.id.layout_head)
    LinearLayout layout_head;
    @BindView(R.id.tv_label_title)
    TextView tv_label_title;
    @BindView(R.id.tv_label_count)
    TextView tv_label_count;
    @BindView(R.id.superSwipeRefreshLayout)
    SuperSwipeRefreshLayout superSwipeRefreshLayout;
    MyPondAdapter myPondAdapter;

    List<MyPondBean.DataBean.PondBean> myPondBeanList = new ArrayList<>();
    List<MyPondBean.DataBean.CountBean> countBeanList = new ArrayList<>();
    HeadAdapter headAdapter;

    private String selectorType;
    private int page = 1;

    private Disposable mMyPondRefreshEvent;
    private Boolean aBoolean = false;
    InterfaceRefreshLoadMore interfaceRefreshLoadMore;

    @Override
    public int setLayoutId() {
        return R.layout.activity_my_pond;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        selectorType = "";
        setTitleText("我的池塘");
        setRightButtonText("发布");
        getNavigationBar().getRightButton().setTextColor(ContextCompat.getColor(this, R.color.base_red));
        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        backPopupMenu();
                    }
                });
        RxView.clicks(getNavigationBar().getRightButton())
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        goActivity(PublishPond.class);
                    }
                });
        RxView.clicks(view_close)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        layout_popupmenu.setVisibility(View.GONE);
                        layout_popupmenu.setAnimation(android.view.animation.AnimationUtils.loadAnimation(MyPondActivity.this, R.anim.dd_menu_out));
                    }
                });
        RxView.clicks(layout_head)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (layout_popupmenu.getVisibility() == View.GONE) {
                            layout_popupmenu.setVisibility(View.VISIBLE);
                            layout_popupmenu.setAnimation(android.view.animation.AnimationUtils.loadAnimation(MyPondActivity.this, R.anim.dd_menu_in));
                        } else {
                            layout_popupmenu.setVisibility(View.GONE);
                            layout_popupmenu.setAnimation(android.view.animation.AnimationUtils.loadAnimation(MyPondActivity.this, R.anim.dd_menu_out));
                        }
                    }
                });
        initSwipeRecyclerView();
        initHeadRecyclerView();
    }

    private void backPopupMenu() {
        if (layout_popupmenu.getVisibility() == View.GONE) {
            finish();
        } else {
            layout_popupmenu.setVisibility(View.GONE);
            layout_popupmenu.setAnimation(android.view.animation.AnimationUtils.loadAnimation(MyPondActivity.this, R.anim.dd_menu_out));
        }
    }

    private void initHeadRecyclerView() {
        head_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        countBeanList.add(new MyPondBean.DataBean.CountBean());
        countBeanList.add(new MyPondBean.DataBean.CountBean());
        countBeanList.add(new MyPondBean.DataBean.CountBean());
        countBeanList.add(new MyPondBean.DataBean.CountBean());
        countBeanList.add(new MyPondBean.DataBean.CountBean());
        headAdapter = new HeadAdapter();
        head_recyclerview.setAdapter(headAdapter);
        headAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int count = 0;//	未上线-1/1审核中/2已上线/3草稿箱
                String title = "";
                switch (position) {
                    case 0:
                        count = countBeanList.get(0).getAll();
                        title = "全部";
                        selectorType = "";
                        break;
                    case 1:
                        count = countBeanList.get(1).getOnline();
                        title = "已上线";
                        selectorType = "2";
                        break;
                    case 2:
                        count = countBeanList.get(2).getFail();
                        title = "未上线";
                        selectorType = "-1";
                        break;
                    case 3:
                        count = countBeanList.get(3).getAuditing();
                        title = "审核中";
                        selectorType = "1";
                        break;
                    case 4:
                        count = countBeanList.get(4).getDraft();
                        title = "草稿";
                        selectorType = "3";
                        break;
                }
                tv_label_count.setText("（共" + count + "条）");
                tv_label_title.setText(title);
                layout_popupmenu.setVisibility(View.GONE);
                layout_popupmenu.setAnimation(android.view.animation.AnimationUtils.loadAnimation(MyPondActivity.this, R.anim.dd_menu_out));
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        NetWork.INSTANCE.getMemberPond(this, selectorType, page, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                MyPondBean myPondBean = JSON.parseObject(resultData, MyPondBean.class);
                refreshData(myPondBean);
            }

            @Override
            public void doError(String msg) {
                if (page > 1) {
                    page--;
                }
            }

            @Override
            public void doResult() {
                interfaceRefreshLoadMore.setStopRefreshing();
            }
        });
    }

    private void refreshData(MyPondBean myPondBean) {
        if (countBeanList.size() > 0) {
            countBeanList.get(0).setAll(myPondBean.getData().getCount().getAll());
            countBeanList.get(1).setOnline(myPondBean.getData().getCount().getOnline());
            countBeanList.get(2).setFail(myPondBean.getData().getCount().getFail());
            countBeanList.get(3).setAuditing(myPondBean.getData().getCount().getAuditing());
            countBeanList.get(4).setDraft(myPondBean.getData().getCount().getDraft());
            if (!aBoolean) {
                aBoolean = true;
                tv_label_count.setText("（共" + myPondBean.getData().getCount().getAll() + "条）");
            }
        }
        recyclerview.removeAllViews();
        if (page == 1) {
            myPondBeanList.clear();
        }
        myPondBeanList.addAll(myPondBean.getData().getPond());
        myPondAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initEvent() {
        mMyPondRefreshEvent = RxBus.getDefault().toObservable(MyPondRefreshEvent.class)
                .subscribeWith(new RxBusSubscriber<MyPondRefreshEvent>() {
                    @Override
                    protected void onEvent(MyPondRefreshEvent myPondRefreshEvent) throws Exception {
                        initData();
                    }
                });
        RxBus.getDefault().add(mMyPondRefreshEvent);
    }

    private void initSwipeRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        myPondAdapter = new MyPondAdapter(myPondBeanList, this) {
            @Override
            public int getItemViewType(int position) {
                return position;
            }
        };
        recyclerview.setAdapter(myPondAdapter);

        recyclerview.setSwipeMenuCreator(swipeMenuCreator);
        recyclerview.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                int menuAdapterPosition = menuBridge.getAdapterPosition();
                switch (menuPosition) {
                    case 0:
                        if (myPondBeanList.get(menuAdapterPosition).getStatus() == 2) {
                            delPond(myPondBeanList.get(menuAdapterPosition).getId(), menuAdapterPosition);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(PublishPond.DATA, myPondBeanList.get(menuAdapterPosition));
                            goActivity(PublishPond.class, bundle);
                        }
                        break;
                    case 1:
                        delPond(myPondBeanList.get(menuAdapterPosition).getId(), menuAdapterPosition);
                        break;
                }
            }
        });
        interfaceRefreshLoadMore = new InterfaceRefreshLoadMore(superSwipeRefreshLayout, this, new InterfaceRefreshLoadMore.RefreshLoadMoreCallback() {
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
    }

    private void delPond(final int id, final int position) {
        BaseConfirmDialog.Companion.newInstance()
                .confirmText("删除")
                .cancleText("手滑了")
                .title("删除池塘")
                .content("确定要删除池塘？")
                .tips("删除后该条池塘消失，且不可恢复")
                .showDialog(MyPondActivity.this, new BaseConfirmDialog.onBtClick() {
                    @Override
                    public void onConfirm() {
                        NetWork.INSTANCE.getMemberDeletePond(MyPondActivity.this, id + "", new NetWork.TokenCallBack() {

                            @Override
                            public void doNext(String resultData, Headers headers) {
                                if (null != myPondAdapter) {
                                    myPondBeanList.remove(position);
                                    myPondAdapter.notifyItemRemoved(position);
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
                    public void onCancle() {

                    }
                });



    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dimen_56);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            if (myPondBeanList.get(viewType).getStatus() != 2) {
                SwipeMenuItem editItem = new SwipeMenuItem(MyPondActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(MyPondActivity.this, R.color.grey_cc_text))
                        .setText("  编辑") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setTextSize(12)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(editItem);// 添加一个按钮到右侧侧菜单。
            }

            SwipeMenuItem deleteItem = new SwipeMenuItem(MyPondActivity.this)
                    .setBackgroundColor(ContextCompat.getColor(MyPondActivity.this, R.color.base_red))
                    .setText("  删除") // 文字，还可以设置文字颜色，大小等。。
                    .setTextColor(Color.WHITE)
                    .setTextSize(12)
                    .setWidth(width)
                    .setHeight(height);

            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
        }
    };


    class HeadAdapter extends BaseQuickAdapter<MyPondBean.DataBean.CountBean, BaseViewHolder> {

        public HeadAdapter() {
            super(R.layout.item_my_pond_head, countBeanList);
        }

        @Override
        protected void convert(BaseViewHolder helper, MyPondBean.DataBean.CountBean item, int position) {
            switch (position) {
                case 0:
                    helper.setText(R.id.tv_label, "全部  （" + item.getAll() + "）");
                    break;
                case 1:
                    helper.setText(R.id.tv_label, "已上线  （" + item.getOnline() + "）");
                    break;
                case 2:
                    helper.setText(R.id.tv_label, "未上线  （" + item.getFail() + "）");
                    break;
                case 3:
                    helper.setText(R.id.tv_label, "审核中  （" + item.getAuditing() + "）");
                    break;
                case 4:
                    helper.setText(R.id.tv_label, "草稿  （" + item.getDraft() + "）");
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPopupMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        interfaceRefreshLoadMore.resetRefreshView();
        RxBus.getDefault().remove(mMyPondRefreshEvent);
    }
}
