package com.mxkj.yuanyintang.mainui.myself.my_release;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.mainui.message.activity.MessageCenterActivity;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.UploadMusicActivity;
import com.mxkj.yuanyintang.mainui.myself.apdater.MyReleaseAdapterNew;
import com.mxkj.yuanyintang.mainui.myself.settings.activity.NoMobile_goBind_Activity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.mainui.myself.bean.MyReleaseBean;
import com.mxkj.yuanyintang.mainui.myself.bean.MyReleaseCountBean;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
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
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.UploadMusicActivity.DATA;

/**
 * Created by LiuJie on 2017/10/24.
 */

public class MyReleaseActivity extends StandardUiActivity {
    @BindView(R.id.layout_popupmenu)
    FrameLayout layout_popupmenu;
    @BindView(R.id.recyclerview)
    SwipeMenuRecyclerView recyclerview;
    @BindView(R.id.head_recyclerview)
    RecyclerView head_recyclerview;
    @BindView(R.id.view_close)
    View view_close;
    @BindView(R.id.layout_head)
    LinearLayout layout_head;
    @BindView(R.id.no_data)
    LinearLayout no_data;
    @BindView(R.id.uploadworks)
    RelativeLayout uploadworks;

    @BindView(R.id.tv_label_title)
    TextView tv_label_title;
    @BindView(R.id.tv_label_count)
    TextView tv_label_count;
    @BindView(R.id.superSwipeRefreshLayout)
    SuperSwipeRefreshLayout superSwipeRefreshLayout;

    private int page = 1;
    List<MyReleaseCountBean.DataBean> countBeanList = new ArrayList<>();
    List<MyReleaseBean.DataBean> myReleaseBeanList = new ArrayList<>();
    HeadAdapter headAdapter;
    private String selectorType;
    private MyReleaseAdapterNew myReleaseAdapter;

    InterfaceRefreshLoadMore interfaceRefreshLoadMore;

    Handler myhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 1:
                    initDatass();
                    break;
            }
            return false;
        }
    });


    @Override
    public int setLayoutId() {
        return R.layout.activity_my_release;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("我的作品");
        setRightButtonText("上传");
        getNavigationBar().getRightButton().setTextColor(ContextCompat.getColor(this, R.color.base_red));
        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        backPopupMenu();
                    }
                });
        RxView.clicks(getNavigationBar().getRightButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        UserInfo userInfo = getUserInfo();
                        if (null != userInfo) {
                            if (!userInfo.getData().getMobile().equals("")) {
                                goActivity(UploadMusicActivity.class);
                            } else {
//                                setSnackBar("你还不是音乐人", "", R.drawable.icon_good);
                                MobclickAgent.onEvent(MyReleaseActivity.this,"mine_realname");
                                String json1 = CacheUtils.INSTANCE.getString(MyReleaseActivity.this, Constants.User.USER_JSON);
                                UserInfoUtil.getUserInfoByJson(json1, new UserInfoUtil.UserCallBack() {
                                    @Override
                                    public void doNext(UserInfo infoBean) {
                                        if (null!=infoBean.getData()) {
                                            if (infoBean != null) {
                                                if (null!=infoBean.getData() && TextUtils.isEmpty(infoBean.getData().getMobile())) {
                                                    BaseConfirmDialog.Companion.newInstance().title("未绑定手机").content("您还没有绑定手机\n\n为了您的账号安全\n请绑定后再认证哟")
                                                            .confirmText("去绑定").isOneBtn(true).showDialog(MyReleaseActivity.this,
                                                            new BaseConfirmDialog.onBtClick() {
                                                                @Override
                                                                public void onConfirm() {
                                                                    Intent intent = new Intent(MyReleaseActivity.this,NoMobile_goBind_Activity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }

                                                                @Override
                                                                public void onCancle() {

                                                                }
                                                            });
                                                }
                                            }
                                        }


                                    }
                                });




                            }
                        }else {

                        }
                    }
                });
        RxView.clicks(uploadworks)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        UserInfo userInfo = getUserInfo();
                        if (null != userInfo) {
                            if (!userInfo.getData().getMobile().equals("")) {
                                goActivity(UploadMusicActivity.class);
                            } else {
//                                setSnackBar("你还不是音乐人", "", R.drawable.icon_good);
                                MobclickAgent.onEvent(MyReleaseActivity.this,"mine_realname");
                                String json1 = CacheUtils.INSTANCE.getString(MyReleaseActivity.this, Constants.User.USER_JSON);
                                UserInfoUtil.getUserInfoByJson(json1, new UserInfoUtil.UserCallBack() {
                                    @Override
                                    public void doNext(UserInfo infoBean) {
                                        if (null!=infoBean.getData()) {
                                            if (infoBean != null) {
                                                if (null!=infoBean.getData() && TextUtils.isEmpty(infoBean.getData().getMobile())) {
                                                    BaseConfirmDialog.Companion.newInstance().title("未绑定手机").content("您还没有绑定手机\n\n为了您的账号安全\n请绑定后再认证哟")
                                                            .confirmText("去绑定").isOneBtn(true).showDialog(MyReleaseActivity.this,
                                                            new BaseConfirmDialog.onBtClick() {
                                                                @Override
                                                                public void onConfirm() {
                                                                    Intent intent = new Intent(MyReleaseActivity.this,NoMobile_goBind_Activity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }

                                                                @Override
                                                                public void onCancle() {

                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        }else {

                        }
                    }
                });
        RxView.clicks(view_close)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        layout_popupmenu.setVisibility(View.GONE);
                        layout_popupmenu.setAnimation(android.view.animation.AnimationUtils.loadAnimation(MyReleaseActivity.this, R.anim.dd_menu_out));
                    }
                });
        RxView.clicks(layout_head)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (layout_popupmenu.getVisibility() == View.GONE) {
                            layout_popupmenu.setVisibility(View.VISIBLE);
                            layout_popupmenu.setAnimation(android.view.animation.AnimationUtils.loadAnimation(MyReleaseActivity.this, R.anim.dd_menu_in));
                        } else {
                            layout_popupmenu.setVisibility(View.GONE);
                            layout_popupmenu.setAnimation(android.view.animation.AnimationUtils.loadAnimation(MyReleaseActivity.this, R.anim.dd_menu_out));
                        }
                    }
                });
        initSwipeRecyclerView();
        initHeadRecyclerView();
        selectorType = "";
    }

    private void initSwipeRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        myReleaseAdapter = new MyReleaseAdapterNew(myReleaseBeanList,myhandler);
        recyclerview.setAdapter(myReleaseAdapter);
        recyclerview.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        recyclerview.setSwipeMenuCreator(swipeMenuCreator);
        recyclerview.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                final int adapterPosition = menuBridge.getAdapterPosition();
                int menuPosition = menuBridge.getPosition();
                int itemType = myReleaseBeanList.get(adapterPosition).getItemType();
                Bundle bundle;
                switch (itemType) {
                    case 10:
                        bundle = new Bundle();
                        bundle.putSerializable(DATA, myReleaseBeanList.get(adapterPosition).getId());
                        goActivity(UploadMusicActivity.class, bundle);
                        break;
                    case 11:
                        switch (menuPosition) {
                            case 0:
                                bundle = new Bundle();
                                bundle.putSerializable(DATA, myReleaseBeanList.get(adapterPosition).getId());
                                goActivity(UploadMusicActivity.class, bundle);
                                break;
                            case 1:
                                BaseConfirmDialog.Companion.newInstance()
                                        .confirmText("删除")
                                        .cancleText("手滑了")
                                        .title("删除歌曲")
                                        .content("确定要删除《" + myReleaseBeanList.get(adapterPosition).getTitle() + "》？")
                                        .tips("删除后该条歌曲消失，且不可恢复")
                                        .showDialog(MyReleaseActivity.this, new BaseConfirmDialog.onBtClick() {
                                            @Override
                                            public void onConfirm() {
                                                delRelease(myReleaseBeanList.get(adapterPosition).getId(), adapterPosition);
                                            }

                                            @Override
                                            public void onCancle() {

                                            }
                                        });

                                break;
                        }
                        break;
                    case 20:
                        bundle = new Bundle();
                        bundle.putSerializable(DATA, myReleaseBeanList.get(adapterPosition).getId());
                        goActivity(UploadMusicActivity.class, bundle);
                        break;
                    case 21:
                        switch (menuPosition) {
                            case 0:
                                bundle = new Bundle();
                                bundle.putSerializable(DATA, myReleaseBeanList.get(adapterPosition).getId());
                                bundle.putSerializable("BEAN", myReleaseBeanList.get(adapterPosition));
                                goActivity(UploadMusicActivity.class, bundle);
                                break;
                            case 1:
//                                TODO  申请下线
                                final BaseConfirmDialog baseConfirmDialog = BaseConfirmDialog.Companion.newInstance()
                                        .confirmText("申请下线")
                                        .cancleText("手滑了")
                                        .title("申请歌曲下线")
                                        .content("确定要申请《" + myReleaseBeanList.get(adapterPosition).getTitle() + "》下线？")
                                        .tips("歌曲下线后，该音乐将无法显示")
                                        .isShowEdittext(true);
                                baseConfirmDialog.showDialog(MyReleaseActivity.this, new BaseConfirmDialog.onBtClick() {
                                    @Override
                                    public void onConfirm() {
                                        if (TextUtils.isEmpty(baseConfirmDialog.getEt_reason().getText())) {
                                            setSnackBar("请输入申请下线原因", "", R.drawable.icon_fails);
                                            return;
                                        }
                                        HttpParams params = new HttpParams();
                                        params.put("id", myReleaseBeanList.get(adapterPosition).getId() + "");
                                        params.put("reason", baseConfirmDialog.getEt_reason().getText().toString());
                                        showLoadingView();
                                        NetWork.INSTANCE.applyDownLine(MyReleaseActivity.this, params, new NetWork.TokenCallBack() {
                                            @Override
                                            public void doNext(String resultData, Headers headers) {
                                                setSnackBar("申请成功~", "", R.drawable.icon_success);
                                                initData();
                                                hideLoadingView();
                                                baseConfirmDialog.dismiss();
                                            }

                                            @Override
                                            public void doError(String msg) {
                                                hideLoadingView();
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

                                break;
                        }
                        break;
                    case 22:
                        switch (menuPosition) {
                            case 0:
                                bundle = new Bundle();
                                bundle.putSerializable(DATA, myReleaseBeanList.get(adapterPosition).getId());
                                goActivity(UploadMusicActivity.class, bundle);
                                break;
                        }
                        break;
                    case 1:
                        switch (menuPosition) {
                            case 0:
                                bundle = new Bundle();
                                bundle.putSerializable(DATA, myReleaseBeanList.get(adapterPosition).getId());
                                goActivity(UploadMusicActivity.class, bundle);
                                break;
                            case 1:
//                                BaseConfirmDialog.Companion.newInstance()
//                                        .confirmText("删除")
//                                        .cancleText("手滑了")
//                                        .title("删除歌曲")
//                                        .content("确定要删除《" + myReleaseBeanList.get(adapterPosition).getTitle() + "》？")
//                                        .tips("删除后该条歌曲消失，且不可恢复")
//                                        .showDialog(MyReleaseActivity.this, new BaseConfirmDialog.onBtClick() {
//                                            @Override
//                                            public void onConfirm() {
//                                                delRelease(myReleaseBeanList.get(adapterPosition).getId(), adapterPosition);
//                                            }
//
//                                            @Override
//                                            public void onCancle() {
//
//                                            }
//                                        });
//                                break;
                            case 2:
                                BaseConfirmDialog.Companion.newInstance()
                                        .confirmText("撤回申请")
                                        .cancleText("手滑了")
                                        .title("撤销申请")
                                        .content("确定要撤回《" + myReleaseBeanList.get(adapterPosition).getTitle() + "》？")
                                        .tips("源小伊正在审核您的作品,撤销后将终止审核")
                                        .showDialog(MyReleaseActivity.this, new BaseConfirmDialog.onBtClick() {
                                            @Override
                                            public void onConfirm() {
                                                backRelease(myReleaseBeanList.get(adapterPosition).getId());
                                            }

                                            @Override
                                            public void onCancle() {

                                            }
                                        });


                                break;
                        }

                        break;
                    case 4:
                        switch (menuPosition) {
                            case 0:
                                bundle = new Bundle();
                                bundle.putSerializable(DATA, myReleaseBeanList.get(adapterPosition).getId());
                                goActivity(UploadMusicActivity.class, bundle);
                                break;
                            case 1:
                                BaseConfirmDialog.Companion.newInstance()
                                        .confirmText("删除")
                                        .cancleText("手滑了")
                                        .title("删除歌曲")
                                        .content("确定要删除《" + myReleaseBeanList.get(adapterPosition).getTitle() + "》？")
                                        .tips("删除后该条歌曲消失，且不可恢复")
                                        .showDialog(MyReleaseActivity.this, new BaseConfirmDialog.onBtClick() {
                                            @Override
                                            public void onConfirm() {
                                                delRelease(myReleaseBeanList.get(adapterPosition).getId(), adapterPosition);
                                            }

                                            @Override
                                            public void onCancle() {

                                            }
                                        });
                                break;
                        }

                        break;
                }
            }
        });
        interfaceRefreshLoadMore = new InterfaceRefreshLoadMore(superSwipeRefreshLayout, this, new InterfaceRefreshLoadMore.RefreshLoadMoreCallback() {
            @Override
            public void onRefresh() {
                page = 1;
                initDatass();
            }

            @Override
            public void onLoadMore() {
                page++;
                initDatass();
            }

            @Override
            public void onPushDistance(int distance) {

            }

            @Override
            public void onPullDistance(int distance) {

            }

        });
    }

    /**
     * 删除的接口
     */
    private void delRelease(int id, int adapterPosition) {
        NetWork.INSTANCE.deleRelease(MyReleaseActivity.this, new HttpParams("id", id + ""), new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                setSnackBar("删除成功~", "", R.drawable.icon_success);
                initDatass();
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });

    }

    /**
     * 撤回申请的接口
     */
    private void backRelease(int id) {
        NetWork.INSTANCE.backRelease(MyReleaseActivity.this, new HttpParams("id", id + ""), new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                setSnackBar("撤回成功~", "", R.drawable.icon_success);
                initDatass();
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });

    }

    private void initHeadRecyclerView() {
        head_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        headAdapter = new HeadAdapter();
        head_recyclerview.setAdapter(headAdapter);
        headAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int count = 0;
                String title = "";//全部（无参数） 1代表审核中 2代表审核通过 -1代表审核失败 0代表下线 4代表草稿
                switch (position) {
                    case 0:
                        count = countBeanList.get(0).getAll();
                        title = "全部";
                        selectorType = "";
                        break;
                    case 1:
                        count = countBeanList.get(1).getOk();
                        title = "已上线";
                        selectorType = "2";
                        break;
                    case 2:
                        count = countBeanList.get(2).getNo();
                        title = "未上线";
                        selectorType = "0";
                        break;
                    case 3:
                        count = countBeanList.get(3).getIng();
                        title = "审核中";
                        selectorType = "1";
                        break;
                    case 4:
                        count = countBeanList.get(4).getDraft();
                        title = "草稿";
                        selectorType = "4";
                        break;
                }
                tv_label_count.setText("（共" + count + "条）");
                tv_label_title.setText(title);
                layout_popupmenu.setVisibility(View.GONE);
                layout_popupmenu.setAnimation(android.view.animation.AnimationUtils.loadAnimation(MyReleaseActivity.this, R.anim.dd_menu_out));
                initData();
            }
        });
        NetWork.INSTANCE.getMusicIanInfo(this, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                MyReleaseCountBean releaseCountBean = JSON.parseObject(resultData, MyReleaseCountBean.class);
                if (null != releaseCountBean) {
                    refreshData(releaseCountBean);
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
    protected void initData() {
    }

    private void refreshData(MyReleaseBean myReleaseBean) {
        if (page == 1) {
            myReleaseBeanList.clear();
        }
        if (myReleaseBean == null || myReleaseBean.getData() == null) {
            return;
        }
        myReleaseBeanList.addAll(myReleaseBean.getData());
        myReleaseAdapter.notifyDataSetChanged();
        interfaceRefreshLoadMore.setStopRefreshing();
    }

    private void refreshData(MyReleaseCountBean myReleaseCountBean) {
        tv_label_count.setText("（共" + myReleaseCountBean.getData().getAll() + "条）");
        countBeanList.clear();
        for (int i = 0; i < 5; i++) {
            countBeanList.add(myReleaseCountBean.getData());
        }
        headAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initEvent() {
    }

    private void backPopupMenu() {
        if (layout_popupmenu.getVisibility() == View.GONE) {
            finish();
        } else {
            layout_popupmenu.setVisibility(View.GONE);
            layout_popupmenu.setAnimation(android.view.animation.AnimationUtils.loadAnimation(MyReleaseActivity.this, R.anim.dd_menu_out));
        }
    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dimen_56);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            SwipeMenuItem editItem = new SwipeMenuItem(MyReleaseActivity.this)
                    .setBackgroundColor(ContextCompat.getColor(MyReleaseActivity.this, R.color.grey_cc_text))
                    .setText("  修改")
                    .setTextColor(Color.WHITE)
                    .setTextSize(12)
                    .setWidth(width)
                    .setHeight(height);
            SwipeMenuItem deleteItem = new SwipeMenuItem(MyReleaseActivity.this)
                    .setBackgroundColor(ContextCompat.getColor(MyReleaseActivity.this, R.color.base_red))
                    .setText("  删除")
                    .setTextColor(Color.WHITE)
                    .setTextSize(12)
                    .setWidth(width)
                    .setHeight(height);


            SwipeMenuItem chehuishenqing = new SwipeMenuItem(MyReleaseActivity.this)
                    .setBackgroundColor(ContextCompat.getColor(MyReleaseActivity.this, R.color.blue_c4))
                    .setText("  撤回申请")
                    .setTextColor(Color.WHITE)
                    .setTextSize(12)
                    .setWidth(width)
                    .setHeight(height);

            switch (viewType) {
                case 10:
                    swipeRightMenu.addMenuItem(editItem);
                    deleteItem.setText(" 下线申请中");
                    swipeRightMenu.addMenuItem(deleteItem);
                    break;
                case 11:
                    swipeRightMenu.addMenuItem(editItem);
                    swipeRightMenu.addMenuItem(deleteItem);
                    break;
                case 20:
                    swipeRightMenu.addMenuItem(editItem);
                    break;
                case 21:
                    swipeRightMenu.addMenuItem(editItem);
                    deleteItem.setText(" 申请下线");
                    swipeRightMenu.addMenuItem(deleteItem);
                    break;
                case 22:
                    swipeRightMenu.addMenuItem(editItem);
                    deleteItem.setText(" 下线申请中");
                    deleteItem.setTextColor(Color.parseColor("#80ffffff"));
                    swipeRightMenu.addMenuItem(deleteItem);
                    break;
                case 1:
                    //审核中的
                    swipeRightMenu.addMenuItem(editItem);
                    //swipeRightMenu.addMenuItem(deleteItem);
                    swipeRightMenu.addMenuItem(chehuishenqing);
                    break;
                case 4:
                    swipeRightMenu.addMenuItem(editItem);
                    swipeRightMenu.addMenuItem(deleteItem);
                    break;

            }

        }
    };

    class HeadAdapter extends BaseQuickAdapter<MyReleaseCountBean.DataBean, BaseViewHolder> {

        public HeadAdapter() {
            super(R.layout.item_my_pond_head, countBeanList);
        }

        @Override
        protected void convert(BaseViewHolder helper, MyReleaseCountBean.DataBean item, int position) {
            switch (position) {
                case 0:
                    helper.setText(R.id.tv_label, "全部  （" + item.getAll() + "）");
                    break;
                case 1:
                    helper.setText(R.id.tv_label, "已上线  （" + item.getOk() + "）");
                    break;
                case 2:
                    helper.setText(R.id.tv_label, "未上线  （" + item.getNo() + "）");
                    break;
                case 3:
                    helper.setText(R.id.tv_label, "审核中  （" + item.getIng() + "）");
                    break;
                case 4:
                    helper.setText(R.id.tv_label, "草稿  （" + item.getDraft() + "）");
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        page=1;
        initDatass();
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
    }

    private void initDatass() {
        NetWork.INSTANCE.getMusicdraft(this, selectorType, page, "id-desc", new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                Log.e(TAG, "doNext: " + resultData);
                MyReleaseBean myReleaseBean = JSON.parseObject(resultData, MyReleaseBean.class);

                if(page == 1 && (null == myReleaseBean.getData() || myReleaseBean.getData().size()==0 || myReleaseBean.getData().equals(""))){
                    layout_head.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                }

                if (null != myReleaseBean && myReleaseBean.getData() != null) {
                    refreshData(myReleaseBean);
                }
            }

            @Override
            public void doError(String msg) {
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

    @Override
    protected void onRestart() {
        super.onRestart();
        initDatass();
    }
}
