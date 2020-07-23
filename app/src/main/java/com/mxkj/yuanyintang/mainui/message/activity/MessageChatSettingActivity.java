package com.mxkj.yuanyintang.mainui.message.activity;

import android.text.TextUtils;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.ReportOperationDialog;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.event.EMECodeEvent;
import com.suke.widget.SwitchButton;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by LiuJie on 2017/10/24.
 */

public class MessageChatSettingActivity extends StandardUiActivity {

    @BindView(R.id.stick_button)
    SwitchButton stick_button;
    @BindView(R.id.not_disturb_button)
    SwitchButton not_disturb_button;
    @BindView(R.id.tv_clear_msg)
    TextView tv_clear_msg;
    @BindView(R.id.tv_feedback)
    TextView tv_feedback;

    public static final String UID = "_uid";
    private String uid;
    EMConversation conversation;

    @Override
    public int setLayoutId() {
        return R.layout.activity_message_chat_setting;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("聊天设置");
        uid = getIntent().getStringExtra(UID);
        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        onBackPressed();
                    }
                });
        RxView.clicks(tv_clear_msg)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MaterialDialog("是否清空消息？", null, null, new MaterialDialogBtnClickListener() {
                            @Override
                            public void onBtnClick(int code) {
                                if (code == 1) {
                                    if (!TextUtils.isEmpty(uid)) {
                                        EMClient.getInstance().chatManager().deleteConversation(uid, true);
                                    }
                                }
                            }
                        });
                    }
                });

        RxView.clicks(tv_feedback)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MusicBean musicBean = new MusicBean()
                                .setMusicIanId(uid)
                                .setReportType(6)
                                .setReportId(TextUtils.isEmpty(uid) ? 0 : Integer.parseInt(uid));
                        ReportOperationDialog reportOperationDialog = new ReportOperationDialog(musicBean);
                        reportOperationDialog.show(getSupportFragmentManager(), "mReportOperationDialog");
                    }
                });

        conversation = EMClient.getInstance().chatManager().getConversation(uid);
        if (null != conversation && null != conversation.getLastMessage()) {
            stick_button.setChecked(conversation.getLastMessage().getBooleanAttribute("isStick", false));
            not_disturb_button.setChecked(conversation.getLastMessage().getBooleanAttribute("isNotDisturb", false));
            stick_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                    stick(isChecked);
                }
            });

            not_disturb_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                    notDisturb(isChecked);
                }
            });
        }
    }

    private void stick(Boolean isStick) {
        if (isStick) {
            conversation.getLastMessage().setAttribute("stickTime", System.currentTimeMillis());
            conversation.getLastMessage().setAttribute("isStick", true);
        } else {
            conversation.getLastMessage().setAttribute("isStick", false);
        }
        EMClient.getInstance().chatManager().importMessages(conversation.getAllMessages());
    }

    private void notDisturb(Boolean isNotDisturb) {
        if (isNotDisturb) {
            conversation.getLastMessage().setAttribute("isNotDisturb", true);
        } else {
            conversation.getLastMessage().setAttribute("isNotDisturb", false);
        }
        EMClient.getInstance().chatManager().importMessages(conversation.getAllMessages());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        RxBus.getDefault().post(new EMECodeEvent(1));
        finish();
    }
}
