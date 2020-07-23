package com.mxkj.yuanyintang.mainui.home.gift_charts;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog;
import com.mxkj.yuanyintang.mainui.home.adapter.SlidingFragmentViewPager;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GiftChartsActivity extends StandardUiActivity {

    @BindView(R.id.v_statusbar)
    View vStatusbar;
    @BindView(R.id.img_share)
    ImageView img_share;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.tab_charts)
    SlidingTabLayout tabCharts;
    @BindView(R.id.vp_charts)
    ViewPager vpCharts;
    @BindView(R.id.activity_charts_bgimg)
    ImageView activity_charts_bgimg;
    private View statusBarView;

    ArrayList<Fragment> fragmentList = new ArrayList<>();
    public static String CHARTS_TYPE = "CHARTS_TYPE";
    public static String INCOME_CLASS_ID = "CLASS_ID";
    public static String EXPEN_CLASS_ID = "EXPEN_CLASS_ID";
    public static String TIME_TYPE = "TIME_TYPE";//2 周,4总榜
    public static String HEAD_INFO_LINK= "HEAD_INFO_LINK";//详情页的头部大图
    private int chartsType, class_id, expen_class_id;
    private String headinfolink;
    private SlidingFragmentViewPager slidingFragmentViewPager;
    private List<String> strings = new ArrayList<>();
    private GiftChartsBean.DataBean.ShareInfoBean shareInfo;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_gift_charts;
    }

    @Override
    protected void initView() {
        hideTitle(true);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        Intent intent = getIntent();
        chartsType = intent.getIntExtra(CHARTS_TYPE, 0);
        class_id = intent.getIntExtra(INCOME_CLASS_ID, 0);
        expen_class_id = intent.getIntExtra(EXPEN_CLASS_ID, 0);
        headinfolink = intent.getStringExtra(HEAD_INFO_LINK);


        Bundle bundle;
        StatusBarUtil.baseTransparentStatusBar(this);
        statusBarView = findViewById(R.id.v_statusbar);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StatusBarUtil.getStatusBarHeight(this));
        statusBarView.setLayoutParams(params);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            statusBarView.setVisibility(View.GONE);
        }
//        1 甜甜圈榜，2音乐榜 ，3.贡献，4收益
        if(chartsType == 3){
            strings.add("周榜");
            bundle = new Bundle();
            bundle.putInt(CHARTS_TYPE, 3);
            bundle.putInt(EXPEN_CLASS_ID, expen_class_id);
            GiftChartsFragment weekFrag = new GiftChartsFragment();
            weekFrag.setArguments(bundle);
            strings.add("总榜");
            GiftChartsAllFragment giftChartsAllFragment = new GiftChartsAllFragment();
            giftChartsAllFragment.setArguments(bundle);
            fragmentList.add(weekFrag);
            fragmentList.add(giftChartsAllFragment);
//            activity_charts_bgimg.setBackgroundResource(R.mipmap.power_exceptional);
            Glide.with(GiftChartsActivity.this)
                    .load(headinfolink)
//                .placeholder(com.xiao.nicevideoplayer.R.drawable.img_default)
                    .crossFade()
                    .into(activity_charts_bgimg);
        }else {
            strings.add("周榜");
            bundle = new Bundle();
            bundle.putInt(CHARTS_TYPE, 4);
            bundle.putInt(EXPEN_CLASS_ID, class_id);
            GiftChartsFragment weekFrag = new GiftChartsFragment();
            weekFrag.setArguments(bundle);
            strings.add("总榜");
            GiftChartsAllFragment giftChartsAllFragment = new GiftChartsAllFragment();
            giftChartsAllFragment.setArguments(bundle);
            fragmentList.add(weekFrag);
            fragmentList.add(giftChartsAllFragment);
//            activity_charts_bgimg.setBackgroundResource(R.mipmap.musicplayer_earnings);
            Glide.with(GiftChartsActivity.this)
                    .load(headinfolink)
//                .placeholder(com.xiao.nicevideoplayer.R.drawable.img_default)
                    .crossFade()
                    .into(activity_charts_bgimg);
        }


        slidingFragmentViewPager = new SlidingFragmentViewPager(getSupportFragmentManager(), strings, fragmentList, this);
        vpCharts.setAdapter(slidingFragmentViewPager);
        tabCharts.setViewPager(vpCharts);
        vpCharts.setOffscreenPageLimit(3);
        if (chartsType == 4) {
            tabCharts.updateTabSelection(1);
            vpCharts.setCurrentItem(1);
        }else{
            tabCharts.updateTabSelection(0);
            vpCharts.setCurrentItem(0);
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFragment = slidingFragmentViewPager.getCurrentFragment();
                if (currentFragment instanceof GiftChartsFragment) {
                    shareInfo = ((GiftChartsFragment) currentFragment).share_info;
                }
                if (shareInfo == null) {
                    return;
                }
                MusicBean musicBean = new MusicBean();
                MusicBean.ShareDataBean shareDataBean = new MusicBean.ShareDataBean();
                if (shareInfo.getIcon_info() != null) {
                    shareDataBean.setWebImgUrl(StringUtils.isEmpty(shareInfo.getIcon_info().getLink()));
                }
                shareDataBean.setShareUrl(StringUtils.isEmpty(shareInfo.getUrl()));
                shareDataBean.setNickname(StringUtils.isEmpty(shareInfo.getContent()));
                shareDataBean.setType("web");
                shareDataBean.setTopicContent(StringUtils.isEmpty(shareInfo.getContent()));
                shareDataBean.setTitle(StringUtils.isEmpty(shareInfo.getTitle()));
                musicBean.setShareDataBean(shareDataBean);
                ShareBottomDialog shareBottomDialog = new ShareBottomDialog(GiftChartsActivity.this, musicBean);
                shareBottomDialog.show();
            }
        });
    }
}
