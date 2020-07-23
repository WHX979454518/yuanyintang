package com.mxkj.yuanyintang.mainui.myself.settings.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyphenate.chat.EMClient;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.DegreeBean;
import com.mxkj.yuanyintang.mainui.login_regist.NewDeviceAuthActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.utils.constant.Constants.Other.EM_LOGIN;

public class About extends StandardUiActivity {
    @BindView(R.id.tv_sina)
    TextView tvSina;
    @BindView(R.id.rl_sina)
    RelativeLayout rlSina;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.rl_e_mail)
    RelativeLayout rlEMail;
    @BindView(R.id.rl_qq)
    RelativeLayout rlQq;
    @BindView(R.id.rl_wechat)
    RelativeLayout rlWechat;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.feedback_qq_group)
    TextView feedback_qq_group;
    @BindView(R.id.wx_mp)
    TextView wx_mp;
    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("关于我们");
        AboutContent();
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            tvVersion.setText("当前版本：" + version);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @OnClick({R.id.rl_e_mail, R.id.rl_qq, R.id.rl_wechat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_e_mail:
                break;
            case R.id.rl_qq:
                break;
            case R.id.rl_wechat:
                break;
        }
    }

    @OnClick(R.id.leftButton)
    public void onViewClicked() {
        finish();
    }

    /**
     * 关于源音塘页面
     */
    private void AboutContent() {
        HttpParams params = new HttpParams();
        NetWork.INSTANCE.aboutContent(this, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                hideLoadingView();
                AboutBean aboutBean = JSON.parseObject(resultData, AboutBean.class);
                AboutBean.DataBean data = aboutBean.getData();
                tvSina.setText(data.getAbout_sinaweibo());
                tvEmail.setText(data.getAbout_email());
                feedback_qq_group.setText(data.getAbout_feedback_qq_group());
                wx_mp.setText(data.getAbout_wx_mp());
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

}
