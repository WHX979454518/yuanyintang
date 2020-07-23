package com.mxkj.yuanyintang.mainui.message;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alibaba.fastjson.JSON;
import com.hyphenate.chat.EMMessage;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.im.EaseConstant;
import com.mxkj.yuanyintang.im.ui.EaseChatFragment;
import com.mxkj.yuanyintang.im.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.mxkj.yuanyintang.im.widget.chatrow.EaseChatRow;
import com.mxkj.yuanyintang.im.widget.chatrow.EaseCustomChatRowProvider;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import java.util.List;

public class ChatFragment extends EaseChatFragment implements EaseChatFragmentHelper {

    // constant start from 11 to avoid conflict with constant in base class
    private static final int ITEM_FILE = 12;

    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;//@人功能

    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    private static final int MESSAGE_TYPE_RECALL = 9;

    //red packet code : 红包功能使用的常量
//    private static final int MESSAGE_TYPE_RECV_RED_PACKET = 5;
//    private static final int MESSAGE_TYPE_SEND_RED_PACKET = 6;
//    private static final int MESSAGE_TYPE_SEND_RED_PACKET_ACK = 7;
//    private static final int MESSAGE_TYPE_RECV_RED_PACKET_ACK = 8;
//    private static final int MESSAGE_TYPE_RECV_RANDOM = 11;
//    private static final int MESSAGE_TYPE_SEND_RANDOM = 12;
//    private static final int ITEM_RED_PACKET = 16;
    //end of red packet code

    /**
     * if it is chatBot
     */
    private boolean isRobot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, false);
    }

    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);
//        if (chatType == Constants.EaseConstant.CHATTYPE_SINGLE) {
//            Map<String, RobotUser> robotMap = EaseHelper.getInstance().getRobotList();
//            if (robotMap != null && robotMap.containsKey(toChatUsername)) {
//                isRobot = true;
//            }
//        }
        super.setUpView();
        // set click listener
        hideTitleBar();
//        ((EaseEmojiconMenu) inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());//添加表情
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 1 && "@".equals(String.valueOf(s.charAt(start)))) {
//                        startActivityForResult(new Intent(getActivity(), PickAtUserActivity.class).putExtra("groupId", toChatUsername), REQUEST_CODE_SELECT_AT_USER);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
        //extend menu items
//        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
//        inputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE, extendMenuItemClickListener);
//        if (chatType == Constants.EaseConstant.CHATTYPE_SINGLE) {
//            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
//            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
//        }
        //end of red packet code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
//            switch (resultCode) {
//                case ContextMenuActivity.RESULT_CODE_COPY: // copy
//                    clipboard.setPrimaryClip(ClipData.newPlainText(null,
//                            ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
//                    break;
//                case ContextMenuActivity.RESULT_CODE_DELETE: // delete
//                    conversation.removeMessage(contextMenuMessage.getMsgId());
//                    messageList.refresh();
//                    break;
//
//                case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
//                    Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
//                    intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
//                    startActivity(intent);
//                    break;
//                case ContextMenuActivity.RESULT_CODE_RECALL://recall
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                EMMessage msgNotification = EMMessage.createTxtSendMessage(" ", contextMenuMessage.getTo());
//                                EMTextMessageBody txtBody = new EMTextMessageBody(getResources().getString(R.string.msg_recall_by_self));
//                                msgNotification.addBody(txtBody);
//                                msgNotification.setMsgTime(contextMenuMessage.getMsgTime());
//                                msgNotification.setLocalTime(contextMenuMessage.getMsgTime());
//                                msgNotification.setAttribute(Constants.EaseConstant.MESSAGE_TYPE_RECALL, true);
//                                EMClient.getInstance().chatManager().recallMessage(contextMenuMessage);
//                                EMClient.getInstance().chatManager().saveMessage(msgNotification);
//                                messageList.refresh();
//                            } catch (final HyphenateException e) {
//                                e.printStackTrace();
//                                getActivity().runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        }
//                    }).start();
//                    break;
//
//                default:
//                    break;
//            }
//        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_AT_USER:
                    if (data != null) {
                        String username = data.getStringExtra("username");
                        inputAtUsername(username, false);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
//        设置消息扩展属性
        if (isRobot) {
            //set message extension
            message.setAttribute("em_robot_message", isRobot);
        }
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }


    @Override
    public void onEnterToChatDetails() {
//        进入回话详情
    }

    @Override
    public void onAvatarClick(String username) {
        //handling when user click avatar
        //头像点击
        UserInfo userInfo = getUserInfo();
        if (null != userInfo) {
            if (!TextUtils.equals(userInfo.getData().getId() + "", username)) {
                Intent intent = new Intent(getActivity(), MusicIanDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, username);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }

    }

    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }


    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        //消息框点击事件，demo这里不做覆盖，如需覆盖，return true
        if (!TextUtils.isEmpty(message.getStringAttribute(MSG_TYPE, ""))) {

        }

        //end of red packet code
        return false;
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {

        //end of red packet code
        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
        // no message forward when in chat room
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_FILE: //file
                selectFileFromLocal();
                break;

            default:
                break;
        }
        //keep exist extend menu
        return false;
    }

    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 11;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (message.getBooleanAttribute(Constants.EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(Constants.EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
                //messagee recall
                else if (message.getBooleanAttribute(Constants.EaseConstant.MESSAGE_TYPE_RECALL, false)) {
                    return MESSAGE_TYPE_RECALL;
                }

                //end of red packet code
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
//                // voice call or video call
//                if (message.getBooleanAttribute(Constants.EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
//                        message.getBooleanAttribute(Constants.EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
//                    return new ChatRowVoiceCall(getActivity(), message, position, adapter);
//                }
//                //recall message
//                else if (message.getBooleanAttribute(Constants.EaseConstant.MESSAGE_TYPE_RECALL, false)) {
//                    return new EaseChatRowRecall(getActivity(), message, position, adapter);
//                }
//                //end of red packet code
            }
            return null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected UserInfo getUserInfo() {
        UserInfo userInfo = null;
        userInfo = JSON.parseObject(CacheUtils.INSTANCE.getString(getActivity(),Constants.User.USER_JSON, ""), UserInfo.class);
        if (null == userInfo) {
            return null;
        }
        return userInfo;
    }

}
