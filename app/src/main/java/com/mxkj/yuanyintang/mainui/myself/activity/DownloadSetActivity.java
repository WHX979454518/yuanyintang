package com.mxkj.yuanyintang.mainui.myself.activity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadSetActivity extends StandardUiActivity {

    @BindView(R.id.headerViewGroup)
    FrameLayout headerViewGroup;
    @BindView(R.id.tv_normal)
    TextView tvNormal;
    @BindView(R.id.img_normal)
    ImageView imgNormal;
    @BindView(R.id.rl_normal)
    RelativeLayout rlNormal;
    @BindView(R.id.tv_high)
    TextView tvHigh;
    @BindView(R.id.img_high)
    ImageView imgHigh;
    @BindView(R.id.rl_high)
    RelativeLayout rlHigh;

    @Override
    public int setLayoutId() {
        return R.layout.activity_download_set;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @OnClick({R.id.leftButton, R.id.rl_normal, R.id.rl_high})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leftButton:
                finish();
                break;
            case R.id.rl_normal:
                imgNormal.setVisibility(View.VISIBLE);
                imgHigh.setVisibility(View.GONE);
                CacheUtils.INSTANCE.setString(DownloadSetActivity.this,Constants.User.MUSIC_KBP, "128");
                break;
            case R.id.rl_high:
                imgNormal.setVisibility(View.GONE);
                imgHigh.setVisibility(View.VISIBLE);
                CacheUtils.INSTANCE.setString(DownloadSetActivity.this,Constants.User.MUSIC_KBP, "320");
                break;
        }
    }
}
