package com.mxkj.yuanyintang.mainui.home.music_charts.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.flyco.tablayout.SlidingTabLayout;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.GiiGiftSuccessActivity;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog;
import com.mxkj.yuanyintang.extraui.gift.BotomGiftListDialog;
import com.mxkj.yuanyintang.extraui.gift.CheckBean;
import com.mxkj.yuanyintang.extraui.gift.ConfirmGiveGiftDialog;
import com.mxkj.yuanyintang.extraui.gift.FirstChargeDialog;
import com.mxkj.yuanyintang.mainui.home.adapter.SlidingFragmentViewPager;
import com.mxkj.yuanyintang.mainui.home.music_charts.bean.ChartsHomeNewBean;
import com.mxkj.yuanyintang.mainui.home.music_charts.bean.ChartsListBean;
import com.mxkj.yuanyintang.mainui.home.music_charts.fragment.DoughnutsFragment;
import com.mxkj.yuanyintang.mainui.home.music_charts.fragment.MusicChartsFragment;
import com.mxkj.yuanyintang.mainui.login_regist.QuickLoginActivityNew;
import com.mxkj.yuanyintang.mainui.myself.doughnut.ChargeDoughnutActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.pay.PayUtils;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.event.PlayerMusicRefreshDataEvent;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.widget.NavigationBar;
import com.sina.weibo.sdk.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Headers;

/**
 * 普通分类歌曲的排行榜页面
 */
public class ChartsListsActivity extends StandardUiActivity {
    public static final String CHART_ID = "charts_id";
    public static final String CHART_TYPE = "charts_type";
    public static final String CHART_TIME_TYPE = "charts_time_type";
    List<String> strings = new ArrayList<>();
    private int realWidth;
    @BindView(R.id.finish)
    Button leftButton;
    @BindView(R.id.navTitleTextViews)
    public TextView navTitleTextViews;
    @BindView(R.id.headerViewGroups)
    FrameLayout headerViewGroups;
    @BindView(R.id.share)
    Button rightButton;
    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.tabs)
    SlidingTabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.main_content)
    RelativeLayout mainContent;
    @BindView(R.id.img_my_gift)
    SimpleDraweeView imgMyGift;
    @BindView(R.id.img_dismiss)
    ImageView imgDismiss;
    @BindView(R.id.ll_gift_success)
    LinearLayout llGiftSuccess;
    @BindView(R.id.tv_my_gift_name)
    TextView tvMyGiftName;
    @BindView(R.id.ll_gift_bck)
    LinearLayout llGiftBck;
    @BindView(R.id.img_head_icon_gift_ani)
    CircleImageView imgHeadIconGiftAni;
    @BindView(R.id.img_gift_ani)
    ImageView imgGiftAni;
    @BindView(R.id.rl_gift_ani)
    RelativeLayout rlGiftAni;
    @BindView(R.id.activity_charts_bgimg)
    ImageView activity_charts_bgimg;

    private String title;
    private int chartType;//1甜甜圈
    private int id;
    private String imgpic_link;
    private ChartsHomeNewBean.DataBean chartBean;
    private List<Fragment> fragments = new ArrayList<>();
    private ChartsListBean.DataBean.ShareInfoBean shareInfo;
    private SlidingFragmentViewPager slidingFragmentViewPager;
    private float cCount = 1f;
    private int my_coin;
    private int gift_id;
    private int music_id;
    private boolean isReGiveGift;
    private BotomGiftListDialog giftListDialog;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_charts_lists;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        realWidth = llGiftBck.getWidth();
        realWidth = UIUtils.dip2px(210, ChartsListsActivity.this);
        hideTitle(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (giftListDialog != null && giftListDialog.isAdded()) {
            giftListDialog.dismiss();
        }
        if (isReGiveGift && gift_id != 0) {
            checkStatus(gift_id);
            isReGiveGift = false;
        }
    }

    @Override
    protected void initEvent() {
        Intent intent = getIntent();
        chartBean = (ChartsHomeNewBean.DataBean) intent.getSerializableExtra(Constants.Other.CHARTS_BEAN);
        if (chartBean != null) {
            chartType = chartBean.getType();
            title = chartBean.getTitle();

            Log.e("lllllll",""+chartBean);
            if(null != chartBean.getHead_info()){
                Glide.with(ChartsListsActivity.this)
                        .load(chartBean.getHead_info().getLink())
//                .placeholder(com.xiao.nicevideoplayer.R.drawable.img_default)
                        .crossFade()
                        .into(activity_charts_bgimg);
            }
//            if(title.equals("音乐人收益榜") || title == "音乐人收益榜"){
//                activity_charts_bgimg.setBackgroundResource(R.mipmap.musicplayer_earnings);
//            }else if(title.equals("音乐收益榜") || title == "音乐收益榜"){
//                activity_charts_bgimg.setBackgroundResource(R.mipmap.music_exceptional);
//            }else if(title.equals("全站音乐榜") || title == "全站音乐榜"){
//                activity_charts_bgimg.setBackgroundResource(R.mipmap.all_music_list);
//            }else if(title.equals("ACG榜") || title == "ACG榜"){
//                activity_charts_bgimg.setBackgroundResource(R.mipmap.acg);
//            }else if(title.equals("国风榜") || title == "国风榜"){
//                activity_charts_bgimg.setBackgroundResource(R.mipmap.huai);
//            }else if(title.equals("VOCALOID") || title == "VOCALOID"){
//                activity_charts_bgimg.setBackgroundResource(R.mipmap.vocaloid);
//            }else if(title.equals("广播剧") || title == "广播剧"){
//                activity_charts_bgimg.setBackgroundResource(R.mipmap.radio);
//            }else if(title.equals("三次元") || title == "三次元"){
//                activity_charts_bgimg.setBackgroundResource(R.mipmap.threeyuan);
//            }
            id = chartBean.getId();
            imgpic_link = chartBean.getImgpic_link();
            navTitleTextViews.setText(StringUtils.isEmpty(title));
        }
        initViewPager();
    }

    private void initViewPager() {
        strings.add("日");
        strings.add("周");
        strings.add("月");
        fragments.clear();
        for (int i = 0; i < 3; i++) {
            Bundle bundle = new Bundle();
            bundle.putString("title", chartBean.getTitle());
            bundle.putInt(CHART_ID, id);
            bundle.putInt(CHART_TIME_TYPE, i + 1);
            if (chartType == 1) {
                DoughnutsFragment fragment = new DoughnutsFragment();
                fragment.setArguments(bundle);
                fragments.add(fragment);
                if(null!=chartBean.getHead_info()){
                    Glide.with(ChartsListsActivity.this)
                            .load(chartBean.getHead_info().getLink())
//                .placeholder(com.xiao.nicevideoplayer.R.drawable.img_default)
                            .crossFade()
                            .into(activity_charts_bgimg);
                }
            } else {
//                MusicChartsFragment fragment = new MusicChartsFragment();
                DoughnutsFragment fragment = new DoughnutsFragment();
                fragment.setArguments(bundle);
                fragments.add(fragment);
                if(null!=chartBean.getHead_info()){
                    Glide.with(ChartsListsActivity.this)
                            .load(chartBean.getHead_info().getLink())
//                .placeholder(com.xiao.nicevideoplayer.R.drawable.img_default)
                            .crossFade()
                            .into(activity_charts_bgimg);
                }

            }
        }
        slidingFragmentViewPager = new SlidingFragmentViewPager(getSupportFragmentManager(), strings, fragments, this);
        viewpager.setAdapter(slidingFragmentViewPager);
        tabs.setViewPager(viewpager);
        tabs.updateTabSelection(0);
        viewpager.setOffscreenPageLimit(3);
    }

    public void setShareInfo(ChartsListBean.DataBean.ShareInfoBean share_info) {
//        this.shareInfo = share_info;
    }

    public void showBotomGiftDialog(int music_id) {
        this.music_id = music_id;
        giftListDialog = new BotomGiftListDialog();
        giftListDialog.setMusic_id(music_id);
        if (CacheUtils.INSTANCE.getBoolean(this, Constants.User.IS_LOGIN)) {
            giftListDialog.show(getSupportFragmentManager(), "giftListDialog");
        } else {
            goActivity(QuickLoginActivityNew.class);
        }
    }


    @OnClick({R.id.finish, R.id.share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.share:
                Fragment currentFragment = slidingFragmentViewPager.getCurrentFragment();
                if (currentFragment instanceof DoughnutsFragment) {
                    shareInfo = ((DoughnutsFragment) currentFragment).share_info;
                } else if (currentFragment instanceof MusicChartsFragment) {
                    shareInfo = ((MusicChartsFragment) currentFragment).share_info;
                }
                if (shareInfo == null) {
                    return;
                }
                MusicBean musicBean = new MusicBean();
                MusicBean.ShareDataBean shareDataBean = new MusicBean.ShareDataBean();
                shareDataBean.setWebImgUrl(StringUtils.isEmpty(shareInfo.getUrl()));
                shareDataBean.setShareUrl(StringUtils.isEmpty(shareInfo.getUrl()));
                shareDataBean.setNickname(StringUtils.isEmpty(shareInfo.getContent()));
                shareDataBean.setType("web");
                shareDataBean.setTopicContent(StringUtils.isEmpty(shareInfo.getContent()));
                shareDataBean.setTitle(StringUtils.isEmpty(shareInfo.getTitle()));
                musicBean.setShareDataBean(shareDataBean);
                ShareBottomDialog shareBottomDialog = new ShareBottomDialog(ChartsListsActivity.this, musicBean);
                shareBottomDialog.show();
                break;
        }
    }


    /*检查能否送礼物*/
    public void checkStatus(int gift_id) {
        showLoadingView();
//        gift_id开始是0，当点击弹框里面的礼物，此方法第一次调用并且给gift_id赋值
        this.gift_id = gift_id;
        HttpParams params = new HttpParams();
        params.put("music_id", music_id + "");
        params.put("gift_id", gift_id + "");
        params.put("source", "app");
        NetWork.INSTANCE.checkGift(this, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                hideLoadingView();
                CheckBean checkBean = JSON.parseObject(resultData, CheckBean.class);
                int code = checkBean.getCode();
                final CheckBean.DataBean data = checkBean.getData();
                if (data == null) {
                    return;
                }
                my_coin = data.getMy_coin();
                switch (code) {
                    case 0:
//                        充值回来，但是充值失败了，就不再弹框。充值成功的话（code=1）就弹出"继续赠送"的弹框
                        if (isReGiveGift) break;
                        isReGiveGift = false;
                        if (data.getOrder_cate() != null && data.getOrder_cate().getSetting() != null && data.getOrder_cate().getSetting().getFirst_charge() > 0) {
                            CheckBean.DataBean.OrderCateBean.SettingBean setting = data.getOrder_cate().getSetting();
                            FirstChargeDialog.Companion.newInstance().showDialog(ChartsListsActivity.this, new FirstChargeDialog.onBtClick() {
                                @Override
                                public void onConfirm() {
                                    startActivity(new Intent(ChartsListsActivity.this, ChargeDoughnutActivity.class));
                                }

                                @Override
                                public void onCancle() {
                                    rechargeDialog(data);
                                }
                            });
                        } else {
                            rechargeDialog(data);
                        }

                        break;
                    case 1:
                        giveGiftDialog(data);
                        break;
                }
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

    /*充值*/
    public void rechargeDialog(final CheckBean.DataBean dataBean) {
        if (dataBean == null || dataBean.getOrder_cate() == null || dataBean.getOrder_cate().getLists() == null) {
            return;
        }
        ConfirmGiveGiftDialog.Companion.newInstance()
                .isRecharge(true)
                .title("甜甜圈不够啦")
                .chargeId(dataBean.getOrder_cate().getLists().getId())
                .giftId(dataBean.getGift_id())
                .type(dataBean.getOrder_cate().getLists().getType())
                .payWay(dataBean.getLast_pay_type())
                .content("已为您选择最佳档位" + (dataBean.getOrder_cate().getLists().getCoin_num()) + "个")
                .oldPrice(dataBean.getOrder_cate().getLists().getPrice() + "")
                .price(dataBean.getOrder_cate().getLists().getDiscount_price() + "")
                .showDialog(ChartsListsActivity.this, new ConfirmGiveGiftDialog.onBtClick() {
                    @Override
                    public void onConfirm() {
                        showLoadingView();
                        int last_pay_type = dataBean.getLast_pay_type();
                        isReGiveGift = true;
//                        1 wx 2 QQ 3 支付宝
                        switch (last_pay_type) {
                            case 0:
                                PayUtils.INSTANCE.pay(ChartsListsActivity.this, dataBean.getOrder_cate().getLists().getId(), 3);
                                break;
                            case 1:
                                PayUtils.INSTANCE.pay(ChartsListsActivity.this, dataBean.getOrder_cate().getLists().getId(), 1);
                                break;
                            case 2:
                                PayUtils.INSTANCE.pay(ChartsListsActivity.this, dataBean.getOrder_cate().getLists().getId(), 2);
                                break;
                            case 3:
                                PayUtils.INSTANCE.pay(ChartsListsActivity.this, dataBean.getOrder_cate().getLists().getId(), 3);
                                break;
                        }
                    }

                    @Override
                    public void onCancle() {

                    }
                });
    }

    /*确认赠送*/
    public void giveGiftDialog(final CheckBean.DataBean dataBean) {
        if (dataBean == null || dataBean.getIcon_info() == null) {
            return;
        }
        String content = "为《" + StringUtils.isEmpty(dataBean.getMusic_title()) + "》送";
        if (isReGiveGift) {
            content = "继续为《" + StringUtils.isEmpty(dataBean.getMusic_title()) + "》送";
            isReGiveGift = false;
        }
        ConfirmGiveGiftDialog.Companion.newInstance()
                .content(content)
                .giftName(dataBean.getName())
                .giftUrl(dataBean.getIcon_info().getLink())
                .tips("剩余：" + dataBean.getMy_coin() + "甜甜圈")
                .showDialog(ChartsListsActivity.this, new ConfirmGiveGiftDialog.onBtClick() {
                    @Override
                    public void onConfirm() {
                        HttpParams params = new HttpParams();
                        params.put("music_id", music_id + "");
                        params.put("gift_id", dataBean.getGift_id() + "");
                        params.put("source", "source");
                        NetWork.INSTANCE.giveGift(ChartsListsActivity.this, params, new NetWork.TokenCallBack() {
                            @Override
                            public void doNext(String resultData, Headers headers) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("GIFT_BEAN", dataBean);
                                goActivity(GiiGiftSuccessActivity.class, bundle);
                                initData();
                                RxBus.getDefault().post(new PlayerMusicRefreshDataEvent());
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

}
