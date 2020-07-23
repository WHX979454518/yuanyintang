package com.mxkj.yuanyintang.mainui.message.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.im.EaseConstant;
import com.mxkj.yuanyintang.im.ui.EaseChatFragment;
import com.mxkj.yuanyintang.mainui.message.ChatActivity;
import com.mxkj.yuanyintang.mainui.message.activity.BroadcastMsgListActivity;
import com.mxkj.yuanyintang.mainui.message.activity.CommentsListActivity;
import com.mxkj.yuanyintang.mainui.message.activity.MessageCenterActivity;
import com.mxkj.yuanyintang.mainui.message.activity.SystemMessageListActivity;
import com.mxkj.yuanyintang.mainui.message.bean.MessageEMMDataBean;
import com.mxkj.yuanyintang.utils.app.TimeUtils;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.mxkj.yuanyintang.mainui.message.activity.MessageCenterActivity.TYPE;

public class MessageCenterAdapterNew extends BaseQuickAdapter<EMConversation, BaseViewHolder> {
    private Context context;

    public MessageCenterAdapterNew(List<EMConversation> data, Context context) {
        super(R.layout.message_item_conversation, data);
        this.context = context;
    }

    private void adminView(BaseViewHolder helper, EMConversation item) {
        helper.setVisible(R.id.iv_is_vip, false);
        if (null != item.getLastMessage().ext().get("data")) {
            MessageEMMDataBean messageDataBean = JSON.parseObject(item.getLastMessage().ext().get("data").toString(), MessageEMMDataBean.class);
            if (null != messageDataBean) {
                helper.setText(R.id.tv_content, StringUtils.isEmpty(messageDataBean.getContent()));
            }
        } else {
            EMTextMessageBody emaMessageBody = (EMTextMessageBody) item.getLastMessage().getBody();
            helper.setText(R.id.tv_content, StringUtils.isEmpty(emaMessageBody.getMessage().toString()));
        }
    }

    @Override
    protected void convert(final BaseViewHolder helper, final EMConversation item) {
        if (item == null || item.getLastMessage() == null) return;
        if (item.getLastMessage().getBooleanAttribute("isStick", false)) {
            helper.setBackgroundColor(R.id.layout_msg, ContextCompat.getColor(mContext, R.color.grey_e8_text));
        } else {
            if (item.getLastMessage().getBooleanAttribute("isMsgBox", false)) {
                helper.setText(R.id.tv_name, "消息盒子");
            }
        }
        if (!TextUtils.isEmpty(item.getLastMessage().getStringAttribute(EaseConstant.EXTRA_IS_MUSIC, "")) && TextUtils.equals("3", item.getLastMessage().getStringAttribute(EaseConstant.EXTRA_IS_MUSIC, ""))) {
            helper.setVisible(R.id.iv_is_vip, true);
        } else {
            helper.setVisible(R.id.iv_is_vip, false);
        }
        if (TextUtils.equals("msgBox", item.getLastMessage().conversationId())) {
            helper.setText(R.id.tv_name, "消息盒子");
            helper.setImageResource(R.id.civ_headimg, R.drawable.ease_icon_msg_box);
            adminView(helper, item);
        } else {
            if (item.getLastMessage().getType() == EMMessage.Type.TXT) {
                helper.setText(R.id.tv_content, StringUtils.isEmpty(((EMTextMessageBody) item.getLastMessage().getBody()).getMessage()));
            } else if (item.getLastMessage().getType() == EMMessage.Type.IMAGE) {
                helper.setText(R.id.tv_content, "[图片]");
            } else if (item.getLastMessage().getType() == EMMessage.Type.VOICE) {
                helper.setText(R.id.tv_content, "[语音]");
            }
            if (item.getLastMessage().direct() == EMMessage.Direct.RECEIVE) {
                helper.setText(R.id.tv_name, item.getLastMessage().getStringAttribute(EaseChatFragment.USER_NAME, ""));
                Log.e(TAG, "convert: " + StringUtils.isEmpty(item.getLastMessage().getStringAttribute(EaseChatFragment.USER_AVATAR, "")));
                Glide.with(mContext)
                        .load(StringUtils.isEmpty(item.getLastMessage().getStringAttribute(EaseChatFragment.USER_AVATAR, "")))
                        .error(R.drawable.defualt_img)
                        .into((CircleImageView) helper.getView(R.id.civ_headimg));
            } else {
                helper.setText(R.id.tv_name, item.getLastMessage().getStringAttribute(EaseChatFragment.OTHER_NAME, ""));
                Log.e(TAG, "convert: ====" + StringUtils.isEmpty(item.getLastMessage().getStringAttribute(EaseChatFragment.OTHER_AVATAR, "")));
                Glide.with(mContext)
                        .load(StringUtils.isEmpty(item.getLastMessage().getStringAttribute(EaseChatFragment.OTHER_AVATAR, "")))
                        .error(R.drawable.defualt_img)
                        .into((CircleImageView) helper.getView(R.id.civ_headimg));
            }
        }
        helper.setText(R.id.tv_time, TimeUtils.timestampToDateChn(item.getLastMessage().getMsgTime()));

        if (item.getLastMessage().getBooleanAttribute("isMsgBox", false)) {
            int disturbUnreadCount = item.getLastMessage().getIntAttribute("isNotDisturbUnreadCount", 0);
            helper.setVisible(R.id.tv_msg_count, disturbUnreadCount > 0);
            helper.setText(R.id.tv_msg_count, disturbUnreadCount > 99 ? "99+" : disturbUnreadCount + "");
        } else {
            helper.setVisible(R.id.tv_msg_count, item.getUnreadMsgCount() > 0);
            helper.setText(R.id.tv_msg_count, item.getUnreadMsgCount() > 99 ? "99+" : item.getUnreadMsgCount() + "");
        }

        RxView.clicks(helper.getView(R.id.layout_msg))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (item.getLastMessage() == null) {
                            getData().clear();
                            notifyDataSetChanged();
                            return;
                        }

                        if (TextUtils.equals("admin", item.getLastMessage().conversationId())) {
                            goActivity(SystemMessageListActivity.class);
                        } else if (TextUtils.equals("circle", item.getLastMessage().conversationId())) {
                        } else if (TextUtils.equals("warden", item.getLastMessage().conversationId())) {
                            goActivity(CommentsListActivity.class);
                        } else if (TextUtils.equals("msgBox", item.getLastMessage().conversationId())) {
                            Bundle bundle = new Bundle();
                            bundle.putString(TYPE, "4");
                            bundle.putString(MessageCenterActivity.TITLE, "消息盒子");
                            goActivity(MessageCenterActivity.class, bundle);
                        } else if (TextUtils.equals("msgBroadcast", item.getLastMessage().conversationId())) {
                            Intent intent = new Intent(mContext, BroadcastMsgListActivity.class);
                            intent.putExtra(TYPE, "1");
                            mContext.startActivity(intent);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.EaseConstant.EXTRA_USER_ID, StringUtils.isEmpty(item.conversationId()));
                            bundle.putString(EaseConstant.EXTRA_OTHER_UID, StringUtils.isEmpty(item.conversationId()));
                            if (item.getLastMessage().direct() == EMMessage.Direct.SEND) {
                                bundle.putString(EaseChatFragment.OTHER_NAME, item.getLastMessage().getStringAttribute(EaseConstant.EXTRA_OTHER_NAME, ""));
                                bundle.putString(EaseChatFragment.OTHER_AVATAR, item.getLastMessage().getStringAttribute(EaseConstant.EXTRA_OTHER_AVATAR, ""));
                            } else {
                                bundle.putString(EaseChatFragment.OTHER_NAME, item.getLastMessage().getStringAttribute(EaseConstant.EXTRA_USER_NAME, ""));
                                bundle.putString(EaseChatFragment.OTHER_AVATAR, item.getLastMessage().getStringAttribute(EaseConstant.EXTRA_USER_AVATAR, ""));
                            }
                            goActivity(ChatActivity.class, bundle);
                        }
                        if (!item.getLastMessage().getBooleanAttribute("isMsgBox", false)) {
                            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(item.conversationId());
                            if (conversation != null) {
                                conversation.markAllMessagesAsRead();
                                helper.setVisible(R.id.tv_msg_count, false);
                            }
                        }
                    }
                });
    }

    protected void goActivity(Class<?> mClass) {
        goActivity(mClass, null);
    }

    protected void goActivity(Class<?> mClass, Bundle bundle) {
        Intent intent = new Intent(context, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

}
