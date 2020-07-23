package com.mxkj.yuanyintang.mainui.myself.settings.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.mainui.myself.settings.SettingActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.myself.settings.utils.GetSafeNum;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
public class ChangeCoutActivity extends StandardUiActivity {
    private UMShareAPI mShareAPI;
    @BindView(R.id.headerViewGroup)
    FrameLayout headerViewGroup;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.rl_e_mail)
    RelativeLayout rlEMail;
    @BindView(R.id.wechat)
    ImageView wechat;
    @BindView(R.id.tv_wechat_nick)
    TextView tvWechatNick;
    @BindView(R.id.bind_wechat)
    TextView bindWechat;
    @BindView(R.id.rl_wechat)
    RelativeLayout rlWechat;
    @BindView(R.id.sina)
    ImageView sina;
    @BindView(R.id.tv_sina_nick)
    TextView tvSinaNick;
    @BindView(R.id.bind_sina)
    TextView bindSina;
    @BindView(R.id.rl_sina)
    RelativeLayout rlSina;
    @BindView(R.id.qq)
    ImageView qq;
    @BindView(R.id.tv_qq_nick)
    TextView tvQqNick;
    @BindView(R.id.bind_qq)
    TextView bindQq;
    @BindView(R.id.tv_bind_tips)
    TextView tv_bind_tips;
    @BindView(R.id.rl_qq)
    RelativeLayout rlQq;
    private int bindType;
    public static boolean needRelogin = true;
    UserInfo.DataBean.BindBean bindBeanWechat;
    UserInfo.DataBean.BindBean bindBeanSina;
    UserInfo.DataBean.BindBean bindBeanQQ;

    @Override
    public int setLayoutId() {
        return R.layout.activity_change_cout;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mShareAPI = UMShareAPI.get(this);
        setTitleText("更改账号绑定");
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {

    }

    private void setBaseInfo() {
        if (SettingActivity.Companion.getUserInfoBean() == null || SettingActivity.Companion.getUserInfoBean().getData() == null) {
            return;
        }
        List<UserInfo.DataBean.BindBean> bind = SettingActivity.Companion.getUserInfoBean().getData().getBind();
        if (bind != null && bind.size() > 0) {
            for (int i = 0; i < bind.size(); i++) {
                UserInfo.DataBean.BindBean bindBean = bind.get(i);
                int type = bindBean.getType();
                switch (type) {
                    case 1://微信
                        //微信绑定
                        bindBeanWechat = bindBean;
                        if (!TextUtils.isEmpty(bindBean.getOpenid())) {
                            tvWechatNick.setText(StringUtils.isEmpty(bindBean.getNickname()));
                            bindWechat.setText("解绑");
                            bindWechat.setTextColor(Color.parseColor("#9da6a4"));
                            if (TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getMobile()) && TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getEmail()) && bind.size() == 1) {
                                bindWechat.setVisibility(View.GONE);
                            } else {
                                bindWechat.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                    case 2://qq
                        //QQ绑定
                        bindBeanQQ = bindBean;
                        if (!TextUtils.isEmpty(bindBean.getOpenid())) {
                            tvQqNick.setText(StringUtils.isEmpty(bindBean.getNickname()));
                            bindQq.setText("解绑");
                            bindQq.setTextColor(Color.parseColor("#9da6a4"));
                            if (TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getMobile()) && TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getEmail()) && bind.size() == 1) {
                                bindQq.setVisibility(View.GONE);
                            } else {
                                bindWechat.setVisibility(View.VISIBLE);
                            }
                        }
                        break;

                    case 3:
                        //新浪绑定
                        bindBeanSina = bindBean;
                        if (!TextUtils.isEmpty(bindBean.getOpenid())) {
                            tvSinaNick.setText(StringUtils.isEmpty(bindBean.getNickname()));
                            bindSina.setText("解绑");
                            bindSina.setTextColor(Color.parseColor("#9da6a4"));
                            if (TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getMobile()) && TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getEmail()) && bind.size() == 1) {
                                bindSina.setVisibility(View.GONE);
                            } else {
                                bindWechat.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                }
            }
        }
    }

    @OnClick({R.id.leftButton, R.id.rl_phone, R.id.rl_e_mail, R.id.bind_wechat, R.id.rl_wechat, R.id.bind_sina, R.id.rl_sina, R.id.bind_qq, R.id.rl_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leftButton:
                finish();
                break;
            case R.id.rl_phone:
                if (SettingActivity.Companion.getUserInfoBean().getData() != null && !TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getMobile())) {
                    Intent intent = new Intent(this, ChangePhoneActivity.class);
                    intent.putExtra("cgType", 0);//更改啥
                    intent.putExtra("cgBy", 0);//通过什么更改0 手机 1 邮箱
                    startActivity(intent);
                } else if (SettingActivity.Companion.getUserInfoBean().getData() != null && !TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getEmail())) {
                    //没有手机，验证邮箱去绑定新的手机
                    Intent intent = new Intent(this, ChangePhoneActivity.class);
                    intent.putExtra("cgType", 0);
                    intent.putExtra("cgBy", 1);//通过什么更改0 手机 1 邮箱
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, NoMobile_goBind_Activity.class);
                    intent.putExtra("cgType", 0);
                    startActivity(intent);
                }
                break;
            case R.id.rl_e_mail:
                if (SettingActivity.Companion.getUserInfoBean().getData() != null && !TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getEmail())) {
                    //已经绑定邮箱，验证邮箱去更改绑定新的邮箱
                    Intent intent = new Intent(this, ChangePhoneActivity.class);
                    intent.putExtra("cgType", 1);//更改啥
                    intent.putExtra("cgBy", 1);//通过什么更改0 手机 1 邮箱
                    startActivity(intent);
                } else if (SettingActivity.Companion.getUserInfoBean().getData() != null && TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getEmail())) {
                    //没有绑定邮箱，验证邮箱去绑定新的邮箱
                    Intent intent = new Intent(this, ChangePhoneStep2.class);
                    intent.putExtra("cgType", 1);
                    intent.putExtra("cgBy", 0);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, NoMobile_goBind_Activity.class);
                    intent.putExtra("cgType", 1);
                    startActivity(intent);
                }
                needRelogin = false;
                break;
            case R.id.bind_wechat:
                bindType = 1;
                if (bindBeanWechat != null && !TextUtils.isEmpty(bindBeanWechat.getOpenid())) {
                    //解绑
                    unBind(1);
                } else {
                    if (isInstallClient(SHARE_MEDIA.WEIXIN) == true) {
                        UMShareAPI.get(ChangeCoutActivity.this).deleteOauth(ChangeCoutActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                    } else {
                    }
                }
                break;
            case R.id.rl_wechat:
                break;
            case R.id.bind_sina:
                bindType = 3;
                if (bindBeanSina != null && !TextUtils.isEmpty(bindBeanSina.getOpenid())) {

                    //解绑
                    unBind(3);
                } else {
                    if (isInstallClient(SHARE_MEDIA.SINA) == true) {
//                        UMShareAPI.get(ChangeCoutActivity.this).deleteOauth(ChangeCoutActivity.this, SHARE_MEDIA.SINA, umAuthListener);
                        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.SINA, umAuthListener);
                    } else {
                    }
                }
                break;
            case R.id.rl_sina:
                break;
            case R.id.bind_qq:
                bindType = 2;
                if (bindBeanQQ != null && !TextUtils.isEmpty(bindBeanQQ.getOpenid())) {
                    //解绑
                    unBind(2);
                } else {
                    if (isInstallClient(SHARE_MEDIA.QQ) == true) {
                        UMShareAPI.get(ChangeCoutActivity.this).deleteOauth(ChangeCoutActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
                    } else {
                    }
                }
                break;
            case R.id.rl_qq:
                break;
        }
    }

    public UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onComplete(@NonNull final SHARE_MEDIA platform, int action, @NonNull final Map<String, String> data) {
            if (data != null) {
                final HttpParams params = new HttpParams();
                params.put("openid", data.get("uid"));
                params.put("nickname", data.get("name"));
                params.put("head", data.get("iconurl"));
                params.put("sex", data.get("gender"));
                if (platform.equals(SHARE_MEDIA.WEIXIN)) {
                    params.put("type", 1 + "");
                } else if (platform.equals(SHARE_MEDIA.SINA)) {
                    params.put("type", 3 + "");
                } else if (platform.equals(SHARE_MEDIA.QQ)) {
                    params.put("type", 2 + "");
                }
                NetWork.INSTANCE.canBind(ChangeCoutActivity.this, platform, data, new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
                        JSONObject jsonObject = JSON.parseObject(resultData);
                        String code = jsonObject.getString("data");
                        NetWork.INSTANCE.bindThirdCout(ChangeCoutActivity.this, params, new NetWork.TokenCallBack() {
                            @Override
                            public void doNext(String resultData, Headers headers) {
                                UserInfoUtil.getUserInfoById(0, ChangeCoutActivity.this, new UserInfoUtil.UserCallBack() {
                                    @Override
                                    public void doNext(UserInfo infoBean) {
                                        if (infoBean != null) {
                                            SettingActivity.Companion.setUserInfoBean(infoBean);
                                            startActivity(new Intent(ChangeCoutActivity.this, ChangeCoutActivity.class));
                                            finish();
                                        }
                                    }
                                });
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
                    public void doError(String msg) {
                    }

                    @Override
                    public void doResult() {

                    }
                });
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
        }
    };

    public void unBind(final int type) {
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.content(
                "解绑后就不能通过该账号登录")//
                .btnText("解绑", "取消")//
                .titleTextSize(16)
                .titleTextColor(ContextCompat.getColor(this, R.color.color_14_text))
                .contentTextColor(ContextCompat.getColor(this, R.color.color_66_text))
                .contentTextSize(14)
                .btnTextSize(14)
                .btnTextColor(ContextCompat.getColor(this, R.color.base_red), ContextCompat.getColor(this, R.color.base_red))
                .showAnim(null)//
                .dismissAnim(null)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        HttpParams params = new HttpParams();
                        params.put("type", type + "");
                        NetWork.INSTANCE.unBindThird(ChangeCoutActivity.this, params, new NetWork.TokenCallBack() {
                            @Override
                            public void doNext(String resultData, Headers headers) {
                                JSONObject object = JSON.parseObject(resultData);
                                Integer code = object.getInteger("code");
                                String msg = object.getString("msg");
                                if (code == 0) {
                                    setSnackBar(StringUtils.isEmpty(msg), "", R.drawable.icon_fails);
                                } else {
                                    setSnackBar(StringUtils.isEmpty(msg), "", R.drawable.icon_success);
                                    UserInfoUtil.getUserInfoById(0, ChangeCoutActivity.this, new UserInfoUtil.UserCallBack() {
                                        @Override
                                        public void doNext(UserInfo infoBean) {
                                            if (infoBean != null) {
                                                SettingActivity.Companion.setUserInfoBean(infoBean);
                                                startActivity(new Intent(ChangeCoutActivity.this, ChangeCoutActivity.class));
                                                finish();
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void doError(String msg) {

                            }

                            @Override
                            public void doResult() {

                            }
                        });
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                }
        );
    }

    public boolean isInstallClient(SHARE_MEDIA platform) {
        return mShareAPI.isInstall(ChangeCoutActivity.this, platform);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserInfoUtil.getUserInfoById(0, ChangeCoutActivity.this, new UserInfoUtil.UserCallBack() {
            @Override
            public void doNext(UserInfo infoBean) {
                if (infoBean != null) {
                    SettingActivity.Companion.setUserInfoBean(infoBean);
                    if (SettingActivity.Companion.getUserInfoBean().getData() != null) {
                        Log.e("ppppp",""+GetSafeNum.getSafeNumStr(SettingActivity.Companion.getUserInfoBean().getData().toString()));
                        if (!TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getEmail())) {
                            tvEmail.setText(GetSafeNum.getSafeNumStr(SettingActivity.Companion.getUserInfoBean().getData().getEmail()));
                        } else {
                            tvEmail.setText("未绑定");
                        }
                    }
                    if (!TextUtils.isEmpty(SettingActivity.Companion.getUserInfoBean().getData().getMobile())) {
                        tvPhone.setText(GetSafeNum.getSafeNumStr(SettingActivity.Companion.getUserInfoBean().getData().getMobile()));
                        tv_bind_tips.setVisibility(View.GONE);
                    } else {
                        tvPhone.setText("未绑定");
                        tv_bind_tips.setVisibility(View.VISIBLE);
                    }
                    setBaseInfo();
                }
            }
        });
        ActivityCollector.INSTANCE.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.INSTANCE.removeActivity(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
