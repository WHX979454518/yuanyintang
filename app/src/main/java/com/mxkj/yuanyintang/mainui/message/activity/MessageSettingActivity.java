package com.mxkj.yuanyintang.mainui.message.activity;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.uiutils.Toast;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.suke.widget.SwitchButton;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

/**
 * Created by LiuJie on 2017/11/12.
 */

public class MessageSettingActivity extends StandardUiActivity {

    @BindView(R.id.switch_button_hello)
    SwitchButton switch_button_hello;
    @BindView(R.id.switch_people_who)
    SwitchButton switch_people_who;
    @BindView(R.id.not_disturb_button)
    SwitchButton not_disturb_button;
    @BindView(R.id.switch_button_sound)
    SwitchButton switch_button_sound;
    @BindView(R.id.switch_button_shock)
    SwitchButton switch_button_shock;
    @BindView(R.id.tv_change_content)
    TextView tv_change_content;
    @BindView(R.id.et_greet_content)
    EditText et_greet_content;

    @Override
    public int setLayoutId() {
        return R.layout.activity_message_setting;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("消息设置");
        initSwitchButton();
    }

    private void initSwitchButton() {
        RxView.clicks(tv_change_content)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (!TextUtils.isEmpty(et_greet_content.getText().toString())) {
                            changeHellContents();
                        } else {
                            Toast.create(MessageSettingActivity.this).show("请输入打招呼的内容");
                        }
                    }
                });
        UserInfo userInfo = getUserInfo();
        if (null != userInfo) {
            et_greet_content.setText("你好~我是" + userInfo.getData().getNickname() + "！交个朋友好么？");
        }
        if (!TextUtils.isEmpty(CacheUtils.INSTANCE.getString(MessageSettingActivity.this, Constants.Other.MSG_HELL_CONTENT))) {
            et_greet_content.setText(CacheUtils.INSTANCE.getString(MessageSettingActivity.this, Constants.Other.MSG_HELL_CONTENT));
        }
        switch_button_hello.setChecked(CacheUtils.INSTANCE.getBoolean(MessageSettingActivity.this, Constants.Other.IS_SETTING_MSG_HELLO, false));
        not_disturb_button.setChecked(CacheUtils.INSTANCE.getBoolean(MessageSettingActivity.this, Constants.Other.IS_NIGHT_NOT_DISTURB_MSG, false));
        switch_people_who.setChecked(CacheUtils.INSTANCE.getBoolean(MessageSettingActivity.this, Constants.Other.IS_NOT_CONCERN_NOT_DISTURB_MSG, false));
        switch_button_sound.setChecked(CacheUtils.INSTANCE.getBoolean(MessageSettingActivity.this, Constants.Other.IS_SETTING_MSG_SOUND, false));
        switch_button_shock.setChecked(CacheUtils.INSTANCE.getBoolean(MessageSettingActivity.this, Constants.Other.IS_SETTING_MSG_VIBRATE, false));
        not_disturb_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, final boolean isChecked) {
                try {
                    if (isChecked) {
                        EMClient.getInstance().pushManager().disableOfflinePush(23, 7);
                    } else {
                        EMClient.getInstance().pushManager().enableOfflinePush();
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                CacheUtils.INSTANCE.setBoolean(MessageSettingActivity.this, Constants.Other.IS_NIGHT_NOT_DISTURB_MSG, isChecked);
            }
        });
        switch_people_who.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                CacheUtils.INSTANCE.setBoolean(MessageSettingActivity.this, Constants.Other.IS_NOT_CONCERN_NOT_DISTURB_MSG, isChecked);
            }
        });
        switch_button_sound.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                CacheUtils.INSTANCE.setBoolean(MessageSettingActivity.this, Constants.Other.IS_SETTING_MSG_SOUND, isChecked);
            }
        });
        switch_button_shock.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                CacheUtils.INSTANCE.setBoolean(MessageSettingActivity.this, Constants.Other.IS_SETTING_MSG_VIBRATE, isChecked);
            }
        });
    }

    private void changeHellContents() {
        if (!switch_button_hello.isChecked()) {
            MaterialDialog("是否开启打招呼设置？",null,null, new MaterialDialogBtnClickListener() {
                @Override
                public void onBtnClick(int code) {
                    if (1 == code) {
                        setHello();
                    }
                }
            });
        } else {
            setHello();
        }
    }

    private void setHello() {
        if (TextUtils.isEmpty(et_greet_content.getText().toString())) {
            setSnackBar("请设置打招呼的内容", "", R.drawable.icon_good);
            return;
        }
        switch_button_hello.setChecked(true);
        showLoadingView();
        NetWork.INSTANCE.getMessageHello(MessageSettingActivity.this, switch_button_hello.isChecked() ? 1 : 0, et_greet_content.getText().toString(), new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                hideLoadingView();
                setSnackBar("设置打招呼成功", "", R.drawable.icon_success);
                CacheUtils.INSTANCE.setBoolean(MessageSettingActivity.this, Constants.Other.IS_SETTING_MSG_HELLO, true);
                CacheUtils.INSTANCE.setString(MessageSettingActivity.this, Constants.Other.MSG_HELL_CONTENT, et_greet_content.getText().toString());
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
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }
}
