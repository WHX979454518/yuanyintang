package com.mxkj.yuanyintang.mainui.myself.activity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnlinePlaySetActivity extends StandardUiActivity {

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
    @BindView(R.id.tv_normal_wifi)
    TextView tvNormalWifi;
    @BindView(R.id.img_normal_wifi)
    ImageView imgNormalWifi;
    @BindView(R.id.rl_normal_wifi)
    RelativeLayout rlNormalWifi;
    @BindView(R.id.tv_high_wifi)
    TextView tvHighWifi;
    @BindView(R.id.img_high_wifi)
    ImageView imgHighWifi;
    @BindView(R.id.rl_high_wifi)
    RelativeLayout rlHighWifi;

    @Override
    public int setLayoutId() {
        return R.layout.activity_online_play_set;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("在线播放音质");
        String netPlay = CacheUtils.INSTANCE.getString(OnlinePlaySetActivity.this,"netPlay");
        String wifiPlay = CacheUtils.INSTANCE.getString(OnlinePlaySetActivity.this,"wifiPlay");
        if (netPlay != null && netPlay.equals("高清")) {
            imgNormal.setVisibility(View.GONE);
            imgHigh.setVisibility(View.VISIBLE);
        } else {
            imgNormal.setVisibility(View.VISIBLE);
            imgHigh.setVisibility(View.GONE);
        }
        if (wifiPlay != null && wifiPlay.equals("高清")) {
            imgNormalWifi.setVisibility(View.GONE);
            imgHighWifi.setVisibility(View.VISIBLE);
        } else {
            imgNormalWifi.setVisibility(View.VISIBLE);
            imgHighWifi.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void initEvent() {

    }

    @OnClick({R.id.leftButton, R.id.rl_normal, R.id.rl_high, R.id.rl_normal_wifi, R.id.rl_high_wifi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leftButton:
                finish();
                break;
            case R.id.rl_normal:
                imgNormal.setVisibility(View.VISIBLE);
                imgHigh.setVisibility(View.GONE);
                CacheUtils.INSTANCE.setString(OnlinePlaySetActivity.this,"netPlay", "标准");
                break;
            case R.id.rl_high:
                imgNormal.setVisibility(View.GONE);
                imgHigh.setVisibility(View.VISIBLE);
                CacheUtils.INSTANCE.setString(OnlinePlaySetActivity.this,"netPlay", "高清");
                break;
            case R.id.rl_normal_wifi:
                imgNormalWifi.setVisibility(View.VISIBLE);
                imgHighWifi.setVisibility(View.GONE);
                CacheUtils.INSTANCE.setString(OnlinePlaySetActivity.this,"wifiPlay", "标准");
                break;
            case R.id.rl_high_wifi:
                imgNormalWifi.setVisibility(View.GONE);
                imgHighWifi.setVisibility(View.VISIBLE);
                CacheUtils.INSTANCE.setString(OnlinePlaySetActivity.this,"wifiPlay", "高清");

                break;
        }
    }
}
