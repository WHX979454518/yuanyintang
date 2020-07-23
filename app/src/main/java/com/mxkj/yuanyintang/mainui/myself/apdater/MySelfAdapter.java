//package com.mxkj.yuanyintang.mainui.myself.apdater;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//
//import com.alibaba.fastjson.JSON;
//import com.jakewharton.rxbinding2.view.RxView;
//import com.mxkj.yuanyintang.R;
//import com.mxkj.yuanyintang.base.bean.UserInfo;
//import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
//import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
//import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
//import com.mxkj.yuanyintang.mainui.myself.activity.DownLoadFileListActivity;
//import com.mxkj.yuanyintang.mainui.myself.activity.EditPersonalProfileActivity;
//import com.mxkj.yuanyintang.mainui.myself.activity.MyCollectionActivity;
//import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
//import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
//import com.mxkj.yuanyintang.mainui.myself.activity.MyPondActivity;
//import com.mxkj.yuanyintang.mainui.myself.my_release.MyReleaseActivity;
//import com.mxkj.yuanyintang.mainui.myself.activity.MySongListActivity;
//import com.mxkj.yuanyintang.mainui.myself.activity.PlayerHistoryActivity;
//import com.mxkj.yuanyintang.mainui.myself.celebrity.RealInfoAuthStep1;
//import com.mxkj.yuanyintang.mainui.myself.helpcenter.HelpCenterActivity;
//import com.mxkj.yuanyintang.mainui.myself.my_income.activity.MyIncomeActivity;
//import com.mxkj.yuanyintang.mainui.myself.settings.SettingActivity;
//import com.mxkj.yuanyintang.mainui.myself.bean.MySelfBean;
//import com.mxkj.yuanyintang.mainui.myself.settings.activity.NoMobile_goBind_Activity;
//import com.mxkj.yuanyintang.utils.constant.Constants;
//import com.mxkj.yuanyintang.utils.file.CacheUtils;
//import com.mxkj.yuanyintang.utils.rxbus.RxBus;
//import com.mxkj.yuanyintang.utils.rxbus.event.SelectTabChangeEvent;
//
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import io.reactivex.annotations.NonNull;
//import io.reactivex.functions.Consumer;
//
///**
// * Created by LiuJie on 2017/9/30.
// */
//
//public class MySelfAdapter extends BaseQuickAdapter<MySelfBean, BaseViewHolder> {
//    private Context context;
//    private String type;
//
//    public MySelfAdapter(List<MySelfBean> data, Context context, String type) {
//        super(R.layout.item_my_self, data);
//        this.context = context;
//        this.type = type;
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, MySelfBean item, final int position) {
//        helper.setImageResource(R.id.iv_ican, item.getDrawable());
//        helper.setText(R.id.tv_name, item.getName());
//        if (item.getCheck()) {
//            helper.setVisible(R.id.iv_red_oval, true);
//        } else {
//            helper.setVisible(R.id.iv_red_oval, false);
//        }
//        RxView.clicks(helper.getConvertView())
//                .throttleFirst(2, TimeUnit.SECONDS)
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(@NonNull Object o) throws Exception {
//                        if (TextUtils.equals("1", type)) {
//                            switch (position) {
//                                case 0:
//                                    goActivity(MyCollectionActivity.class, true);
//                                    break;
//                                case 1:
////                                    goActivity(MySongListActivity.class, true);
//                                    break;
//                                case 2:
//                                    goActivity(PlayerHistoryActivity.class, true);
//                                    break;
//                                case 3:
//                                    goActivity(DownLoadFileListActivity.class);
//                                    break;
//                                case 4:
//                                    goActivity(MyPondActivity.class, true);
//                                    break;
//                                case 5:
//                                    if (CacheUtils.INSTANCE.getBoolean(context, Constants.User.IS_LOGIN)) {
//                                        RxBus.getDefault().post(new SelectTabChangeEvent(101));
//                                    } else {
//                                        goActivity(LoginRegMainPage.class, false);
//                                    }
//                                    break;
//                                case 6:
//                                    goActivity(HelpCenterActivity.class);
//                                    break;
//                                case 7:
////                                    goActivity(SettingActivity.class, false);
//                                    break;
//                            }
//                        } else if (TextUtils.equals("2", type)) {
//                            String json = CacheUtils.INSTANCE.getString(mContext, Constants.User.USER_JSON);
//                            switch (position) {
//                                case 0:
//                                    if (CacheUtils.INSTANCE.getBoolean(context, Constants.User.IS_LOGIN)) {
//                                        UserInfoUtil.getUserInfoByJson(json, new UserInfoUtil.UserCallBack() {
//                                            @Override
//                                            public void doNext(UserInfo infoBean) {
//                                                if (infoBean != null && infoBean.getData() != null) {
//                                                    isUserEvent(infoBean, 0);
//                                                }
//                                            }
//                                        });
//                                    } else {
//                                        goActivity(LoginRegMainPage.class);
//                                    }
//                                    break;
//                                case 1:
//                                    if (CacheUtils.INSTANCE.getBoolean(context, Constants.User.IS_LOGIN)) {
//                                        UserInfoUtil.getUserInfoByJson(json, new UserInfoUtil.UserCallBack() {
//                                            @Override
//                                            public void doNext(UserInfo infoBean) {
//                                                if (infoBean != null && infoBean.getData() != null) {
//                                                    if (infoBean.getData().getIs_auth() != 3) {
//                                                        BaseConfirmDialog.Companion.newInstance().title("未实名认证").content("您还没有实名认证\n\n发布音乐需要先完成认证哦~~").confirmText("知道了").isOneBtn(true).showDialog(mContext, new BaseConfirmDialog.onBtClick() {
//                                                            @Override
//                                                            public void onConfirm() {
//
//                                                            }
//
//                                                            @Override
//                                                            public void onCancle() {
//                                                            }
//                                                        });
//                                                    } else {
//                                                        goActivity(MyReleaseActivity.class);
//                                                    }
//                                                }
//                                            }
//                                        });
//                                    } else {
//                                        goActivity(LoginRegMainPage.class);
//                                    }
//                                    break;
//
//                                case 2:
//                                    if (CacheUtils.INSTANCE.getBoolean(context, Constants.User.IS_LOGIN)) {
//                                        UserInfoUtil.getUserInfoByJson(json, new UserInfoUtil.UserCallBack() {
//                                            @Override
//                                            public void doNext(UserInfo infoBean) {
//                                                goActivity(MyIncomeActivity.class);
//                                            }
//                                        });
//                                    } else {
//                                        goActivity(LoginRegMainPage.class);
//                                    }
//                                    break;
//                            }
//                        }
//                    }
//                });
//    }
//
//    private void isUserEvent(UserInfo infoBean, int type) {
//        if (infoBean != null) {
//            if (infoBean.getData() != null && TextUtils.isEmpty(infoBean.getData().getMobile())) {
//                BaseConfirmDialog.Companion.newInstance().title("未绑定手机").content("您还没有绑定手机\n\n为了您的账号安全\n请绑定后再认证哟").confirmText("去绑定").isOneBtn(true).showDialog(mContext, new BaseConfirmDialog.onBtClick() {
//                    @Override
//                    public void onConfirm() {
//                        goActivity(NoMobile_goBind_Activity.class);
//                    }
//
//                    @Override
//                    public void onCancle() {
//
//                    }
//                });
//                return;
//            }
//        }
//        int is_auth = infoBean.getData().getIs_auth();
//        if (is_auth == 0) {
//            BaseConfirmDialog.Companion.newInstance().title("未完善资料").content("您还没有完善账号资料\n\n为了您的账号安全\n请完成后再实名认证哟").confirmText("去完善").isOneBtn(true).showDialog(mContext, new BaseConfirmDialog.onBtClick() {
//                @Override
//                public void onConfirm() {
//                    UserInfo userInfo = getUserInfo();
//                    if (null == userInfo) {
//                        goActivity(LoginRegMainPage.class);
//                        return;
//                    }
//                    Bundle bundle2 = new Bundle();
//                    bundle2.putSerializable(EditPersonalProfileActivity.DATA, userInfo);
//                    goActivity(EditPersonalProfileActivity.class, bundle2);
//                }
//
//                @Override
//                public void onCancle() {
//
//                }
//            });
//        } else {
//            goActivity(RealInfoAuthStep1.class);
//        }
//    }
//
//
//    protected UserInfo getUserInfo() {
//        UserInfo userInfo = null;
//        userInfo = JSON.parseObject(CacheUtils.INSTANCE.getString(context, Constants.User.USER_JSON, ""), UserInfo.class);
//        if (null == userInfo) {
//            return null;
//        }
//        return userInfo;
//    }
//
//
//    /**
//     * 跳转页面
//     *
//     * @param mClass 目标页面
//     */
//    protected void goActivity(Class<?> mClass, Boolean isLogin) {
//        if (isLogin) {
//            if (CacheUtils.INSTANCE.getBoolean(context, Constants.User.IS_LOGIN)) {
//                goActivity(mClass, null, isLogin);
//            } else {
//                goActivity(LoginRegMainPage.class, null, isLogin);
//            }
//        } else {
//            goActivity(mClass, null, isLogin);
//        }
//    }
//
//    /**
//     * 跳转页面
//     *
//     * @param mClass 目标页面
//     */
//    protected void goActivity(Class<?> mClass) {
//        goActivity(mClass, null, false);
//    }
//
//    /**
//     * 跳转页面带参数
//     *
//     * @param mClass 目标页面
//     * @param bundle 参数
//     */
//    protected void goActivity(Class<?> mClass, Bundle bundle, Boolean isLogin) {
//        Intent intent = new Intent(context, mClass);
//        if (bundle != null) {
//            intent.putExtras(bundle);
//        }
//        context.startActivity(intent);
//    }
//
//    protected void goActivity(Class<?> mClass, Bundle bundle) {
//        Intent intent = new Intent(context, mClass);
//        if (bundle != null) {
//            intent.putExtras(bundle);
//        }
//        context.startActivity(intent);
//    }
//
//
//}
