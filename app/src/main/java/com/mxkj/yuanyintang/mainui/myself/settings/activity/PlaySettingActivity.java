package com.mxkj.yuanyintang.mainui.myself.settings.activity;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.myself.activity.DownloadSetActivity;
import com.mxkj.yuanyintang.mainui.myself.activity.OnlinePlaySetActivity;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.suke.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaySettingActivity extends StandardUiActivity implements SwitchButton.OnCheckedChangeListener {
    @BindView(R.id.headerViewGroup)
    FrameLayout headerViewGroup;
    @BindView(R.id.switch_button_net_play)
    SwitchButton switchButtonNetPlay;
    @BindView(R.id.switch_button_net_download)
    SwitchButton switchButtonNetDownload;
    @BindView(R.id.online_play)
    TextView onlinePlay;
    @BindView(R.id.rl_choosePlayType)
    RelativeLayout rlChoosePlayType;
    @BindView(R.id.downloadtype)
    TextView downloadtype;
    @BindView(R.id.rl_downloadType)
    RelativeLayout rlDownloadType;
    @BindView(R.id.switch_button_lock_screen)
    SwitchButton switchButtonLockScreen;
    @BindView(R.id.switch_button_notify)
    SwitchButton switchButtonNotify;

    @Override
    public int setLayoutId() {
        return R.layout.activity_play_setting;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("播放设置");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        switchButtonNotify.setChecked(CacheUtils.INSTANCE.getBoolean(this,"openNotify", true));
        switchButtonNotify.setOnCheckedChangeListener(this);

        switchButtonNetPlay.setChecked(CacheUtils.INSTANCE.getBoolean(this,"open3gNet", true));
        switchButtonNetPlay.setOnCheckedChangeListener(this);

        switchButtonNetDownload.setChecked(CacheUtils.INSTANCE.getBoolean(this,"openNetDownload", true));
        switchButtonNetDownload.setOnCheckedChangeListener(this);

        switchButtonLockScreen.setChecked(CacheUtils.INSTANCE.getBoolean(this,"openLockPlay", true));
        switchButtonLockScreen.setOnCheckedChangeListener(this);

    }

    @OnClick({R.id.leftButton, R.id.rl_choosePlayType, R.id.rl_downloadType})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leftButton:
                finish();
                break;
            case R.id.rl_choosePlayType:
                startActivity(new Intent(this, OnlinePlaySetActivity.class));
                break;
            case R.id.rl_downloadType:
                startActivity(new Intent(this, DownloadSetActivity.class));
                break;
        }
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.switch_button_net_play:
                if (isChecked) {
                    CacheUtils.INSTANCE.setBoolean(this,"open3gNet", true);
                } else {
                    CacheUtils.INSTANCE.setBoolean(this,"open3gNet", false);
                }
                break;
            case R.id.switch_button_net_download:
                if (isChecked) {
                    CacheUtils.INSTANCE.setBoolean(this,"openNetDownload", true);
                } else {
                    CacheUtils.INSTANCE.setBoolean(this,"openNetDownload", false);
                }
                break;
            case R.id.switch_button_lock_screen:
                if (isChecked) {
                    CacheUtils.INSTANCE.setBoolean(this,"openLockPlay", true);
                } else {
                    CacheUtils.INSTANCE.setBoolean(this,"openLockPlay", false);
                }
                break;
            case R.id.switch_button_notify:
                if (isChecked) {
                    CacheUtils.INSTANCE.setBoolean(this,"openNotify", true);
                } else {
                    CacheUtils.INSTANCE.setBoolean(this,"openNotify", false);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String netPlay = CacheUtils.INSTANCE.getString(this,"netPlay");
        String wifidownload = CacheUtils.INSTANCE.getString(this,"wifidownload");
        if (netPlay != null) {
            onlinePlay.setText(netPlay);
        }
        String mKbp = CacheUtils.INSTANCE.getString(this,Constants.User.MUSIC_KBP, "128");
        if (mKbp != null && mKbp.equals("128")) {
            downloadtype.setText("标清");
        } else {
            downloadtype.setText("高清");
        }
    }
}
