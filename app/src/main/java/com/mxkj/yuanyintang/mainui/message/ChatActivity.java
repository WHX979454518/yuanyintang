package com.mxkj.yuanyintang.mainui.message;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.im.EaseConstant;
import com.mxkj.yuanyintang.im.ui.EaseChatFragment;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.message.activity.MessageChatSettingActivity;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.event.EMECodeEvent;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class ChatActivity extends StandardUiActivity {
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;
    private static final String TAG = "ChatActivity";

    @Override
    public int setLayoutId() {
        return R.layout.em_activity_chat;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        UserInfo userInfo = JSON.parseObject(CacheUtils.INSTANCE.getString(ChatActivity.this, Constants.User.USER_JSON), UserInfo.class);
        toChatUsername = getIntent().getExtras().getString(Constants.EaseConstant.EXTRA_USER_ID);
        String otherName = getIntent().getExtras().getString(Constants.EaseConstant.EXTRA_OTHER_NAME, "");
        String otherAvatar = getIntent().getExtras().getString(Constants.EaseConstant.EXTRA_OTHER_AVATAR, "");
        final String otherUid = getIntent().getExtras().getString(EaseConstant.EXTRA_OTHER_UID, "");
        String isRelation = getIntent().getExtras().getString(EaseConstant.EXTRA_IS_RELATION, "");
        String isMusic = getIntent().getExtras().getString(EaseConstant.EXTRA_IS_MUSIC, "");
        String toRelation = getIntent().getExtras().getString(EaseConstant.EXTRA_TO_RELATION, "");
        String toMusic = getIntent().getExtras().getString(EaseConstant.EXTRA_TO_MUSIC, "");
        setTitleText(otherName);
        setRightButtonImageView(ContextCompat.getDrawable(this, R.drawable.icon_more_black));

        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Object o) throws Exception {
                        onBackPressed();
                    }
                });

        RxView.clicks(getNavigationBar().getRightButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Object o) throws Exception {
                        Bundle bundle = new Bundle();
                        bundle.putString(MessageChatSettingActivity.UID, toChatUsername);
                        goActivity(MessageChatSettingActivity.class, bundle);
                    }
                });
        activityInstance = this;
        //get user id or group id
        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EMConversation.EMConversationType.Chat, true);
        if (null != userInfo && null != userInfo.getData()) {
            Bundle bundle = new Bundle();
            bundle.putString(EaseConstant.EXTRA_USER_NAME, userInfo.getData().getNickname());
            bundle.putString(EaseConstant.EXTRA_USER_AVATAR, userInfo.getData().getHead_link());
            bundle.putString(EaseConstant.EXTRA_OTHER_AVATAR, otherAvatar);
            bundle.putString(EaseConstant.EXTRA_OTHER_NAME, otherName);
            bundle.putString(EaseConstant.EXTRA_OTHER_UID, otherUid);
            bundle.putString(EaseConstant.EXTRA_USER_ID, toChatUsername);
            bundle.putString(EaseConstant.EXTRA_IS_RELATION, isRelation);
            bundle.putString(EaseConstant.EXTRA_TO_RELATION, toRelation);
            bundle.putString(EaseConstant.EXTRA_IS_MUSIC, isMusic);
            bundle.putString(EaseConstant.EXTRA_TO_MUSIC, toMusic);
            chatFragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
        new RxPermissions(ChatActivity.this).requestEach(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {

                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            finish();
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                            finish();
                        }
                    }
                });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        UserInfoUtil.getUserInfoByJson(Constants.User.USER_JSON, new UserInfoUtil.UserCallBack() {
            @Override
            public void doNext(UserInfo infoBean) {
                if (infoBean != null && infoBean.getData() != null) {
                    if (TextUtils.isEmpty(infoBean.getData().getMobile())) {
//                        goActivity(BindMobileActivity.class);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        RxBus.getDefault().post(new EMECodeEvent(100));
        chatFragment.onBackPressed();
        finish();
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        new RxPermissions(ChatActivity.this).requestEach(permissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {

                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
//                            setSnackBar("没有外部存储权限,请到系统设置开启源音塘的存储权限", "去设置", R.drawable.icon_fails);
                        }
                    }
                });
    }


}
