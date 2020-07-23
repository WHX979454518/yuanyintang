package com.mxkj.yuanyintang.im.ui;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMTextMessageBody;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;
import com.mxkj.yuanyintang.im.EaseConstant;
import com.mxkj.yuanyintang.im.EaseUI;
import com.mxkj.yuanyintang.im.widget.EaseChatMessageList;
import com.mxkj.yuanyintang.im.domain.EaseEmojicon;
import com.mxkj.yuanyintang.im.domain.EaseUser;
import com.mxkj.yuanyintang.im.model.EaseAtMessageHelper;
import com.mxkj.yuanyintang.im.utils.EaseCommonUtils;
import com.mxkj.yuanyintang.im.utils.EaseUserUtils;
import com.mxkj.yuanyintang.im.widget.EaseAlertDialog;
import com.mxkj.yuanyintang.im.widget.EaseChatExtendMenu;
import com.mxkj.yuanyintang.im.widget.EaseChatInputMenu;
import com.mxkj.yuanyintang.im.widget.EaseVoiceRecorderView;
import com.mxkj.yuanyintang.im.widget.chatrow.EaseCustomChatRowProvider;
import com.mxkj.yuanyintang.mainui.message.activity.SelectorMyCollectionSongListActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.event.EMECodeEvent;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Headers;

public class EaseChatFragment extends EaseBaseFragment implements EMMessageListener {
    /*-
     txt判断音乐字段根据msgType 判断
            音乐：music
            歌单：song
            置顶
    -*/

    protected static final String TAG = "EaseChatFragment";
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    protected static final int REQUEST_CODE_MUSIC = 4;
    protected static final int REQUEST_CODE_SONG = 5;

    /**
     * params to fragment
     */
    protected Bundle fragmentArgs;
    protected int chatType;
    protected String toChatUsername;
    protected String toChatOtherNickname;
    protected String toChatOtherAvatar;
    protected String toChatOtherUid;
    protected String toChatuserNickname;
    protected String toChatUserAvatar;
    protected String isMusic;
    protected String toMusic;
    protected String isRelation;
    protected String toRelation;
    protected EaseChatMessageList messageList;
    protected EaseChatInputMenu inputMenu;

    protected EMConversation conversation;

    protected InputMethodManager inputManager;
    protected ClipboardManager clipboard;

    protected Handler handler = new Handler();
    protected File cameraFile;
    protected EaseVoiceRecorderView voiceRecorderView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ListView listView;

    protected boolean isloading;
    protected boolean haveMoreData = true;
    protected int pagesize = 20;
    protected GroupListener groupListener;
    protected ChatRoomListener chatRoomListener;
    protected EMMessage contextMenuMessage;

    static final int ITEM_PICTURE = 1;
    static final int ITEM_TAKE_PICTURE = 2;
    static final int ITEM_MUSIC = 3;
    static final int ITEM_SONG_LIST = 4;

    public static final String MSG_TYPE = "msgType";
    public static final String _ID = "id";
    public static final String TITLE = "title";
    public static final String NICKNAME = "nickname";
    public static final String IMGPIC_LINK = "imgpic_link";
    public static final String USER_AVATAR = "userAvatar";
    public static final String USER_NAME = "userName";
    public static final String USER_UID = "userUid";
    public static final String OTHER_AVATAR = "otherAvatar";
    public static final String OTHER_NAME = "otherName";
    public static final String OTHER_UID = "otherUid";
    public static final String IS_MUSIC = "isMusic";
    public static final String IS_RELATION = "isRelation";

    protected int[] itemStrings = {R.string.attach_picture, R.string.attach_take_pic, R.string.attach_music, R.string.attach_song_list};
    protected int[] itemdrawables = {R.drawable.ease_chat_image_selector, R.drawable.ease_chat_takepic_selector,
            R.drawable.ease_chat_music_selector, R.drawable.ease_chat_song_list_selector};
    protected int[] itemIds = {ITEM_PICTURE, ITEM_TAKE_PICTURE, ITEM_MUSIC, ITEM_SONG_LIST};
    private boolean isMessageListInited;
    protected MyItemClickListener extendMenuItemClickListener;
    protected boolean isRoaming = false;
    private ExecutorService fetchQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_chat, container, false);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, boolean roaming) {
        isRoaming = roaming;
        return inflater.inflate(R.layout.ease_fragment_chat, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        fragmentArgs = getArguments();
        if (null == fragmentArgs) {
            getActivity().finish();
            return;
        }
        // check if single chat or group chat
        chatType = fragmentArgs.getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        // userId you are chat with or group id
        toChatUsername = fragmentArgs.getString(EaseConstant.EXTRA_USER_ID);
        toChatOtherUid = fragmentArgs.getString(EaseConstant.EXTRA_OTHER_UID);
        toChatOtherNickname = fragmentArgs.getString(EaseConstant.EXTRA_OTHER_NAME);
        toChatOtherAvatar = fragmentArgs.getString(EaseConstant.EXTRA_OTHER_AVATAR);
        toChatuserNickname = fragmentArgs.getString(EaseConstant.EXTRA_USER_NAME);
        toChatUserAvatar = fragmentArgs.getString(EaseConstant.EXTRA_USER_AVATAR);
        isMusic = fragmentArgs.getString(EaseConstant.EXTRA_IS_MUSIC);
        isRelation = fragmentArgs.getString(EaseConstant.EXTRA_IS_RELATION);
        super.onActivityCreated(savedInstanceState);
    }

    private void upChatHead(final String otherAvatar, final String head_link, EMConversation conversationOther) {
        if (null == conversationOther) {
            return;
        }
        List<EMMessage> var = conversationOther.getAllMessages();
        for (int i = 0; i < var.size(); i++) {
            if (var.get(i).direct() == EMMessage.Direct.SEND) {
                var.get(i).setAttribute(EaseConstant.EXTRA_USER_AVATAR, head_link);
            } else {
                var.get(i).setAttribute(EaseConstant.EXTRA_USER_AVATAR, otherAvatar);
            }
        }
        EMClient.getInstance().chatManager().importMessages(var);
    }

    /**
     * init view
     */
    protected void initView() {
        voiceRecorderView = getView().findViewById(R.id.voice_recorder);

        messageList = getView().findViewById(R.id.message_list);
        if (chatType != EaseConstant.CHATTYPE_SINGLE)
            messageList.setShowUserNick(true);
//        messageList.setAvatarShape(1);
        listView = messageList.getListView();

        extendMenuItemClickListener = new MyItemClickListener();
        inputMenu = getView().findViewById(R.id.input_menu);
        registerExtendMenuItem();
        // init input menu
        inputMenu.init(null);
        inputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
            }

            @Override
            public void onSendVoiceMsg(String fileName, int duration) {
                sendVoiceMessage(fileName, duration);
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }
        });

        swipeRefreshLayout = messageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (isRoaming) {
            fetchQueue = Executors.newSingleThreadExecutor();
        }
    }

    protected void setUpView() {
        titleBar.setTitle(toChatUsername);
        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            // set title
            if (EaseUserUtils.getUserInfo(toChatUsername) != null) {
                EaseUser user = EaseUserUtils.getUserInfo(toChatUsername);
                if (user != null) {
                    titleBar.setTitle(user.getNick());
                }
            }
            titleBar.setRightImageResource(R.drawable.ease_mm_title_remove);
        } else {
            titleBar.setRightImageResource(R.drawable.ease_to_group_details_normal);
            if (chatType == EaseConstant.CHATTYPE_GROUP) {
                //group chat
                EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
                if (group != null)
                    titleBar.setTitle(group.getGroupName());
                // listen the event that user moved out group or group is dismissed
                groupListener = new GroupListener();
                EMClient.getInstance().groupManager().addGroupChangeListener(groupListener);
            } else {
                chatRoomListener = new ChatRoomListener();
                EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomListener);
//                onChatRoomViewCreation();
            }

        }
        if (chatType != EaseConstant.CHATTYPE_CHATROOM) {
            onConversationInit();
            onMessageListInit();
        }

        titleBar.setLeftLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleBar.setRightLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                    emptyHistory();
                } else {
                    toGroupDetails();
                }
            }
        });

        setRefreshLayoutListener();

        // show forward message if the message is not null
        String forward_msg_id = getArguments().getString("forward_msg_id");
        if (forward_msg_id != null) {
            forwardMessage(forward_msg_id);
        }
    }

    /**
     * register extend menu, item id need > 3 if you override this method and keep exist item
     */
    protected void registerExtendMenuItem() {
        for (int i = 0; i < itemStrings.length; i++) {
            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
        }
    }


    protected void onConversationInit() {
        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        upChatHead(toChatOtherAvatar, toChatUserAvatar, conversation);
        conversation.markAllMessagesAsRead();
        if (!isRoaming) {
            final List<EMMessage> msgs = conversation.getAllMessages();
            int msgCount = msgs != null ? msgs.size() : 0;
            if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
                String msgId = null;
                if (msgs != null && msgs.size() > 0) {
                    msgId = msgs.get(0).getMsgId();
                }
                conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
            }
        } else {
            fetchQueue.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().chatManager().fetchHistoryMessages(
                                toChatUsername, EaseCommonUtils.getConversationType(chatType), pagesize, "");
                        final List<EMMessage> msgs = conversation.getAllMessages();
                        int msgCount = msgs != null ? msgs.size() : 0;
                        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
                            String msgId = null;
                            if (msgs != null && msgs.size() > 0) {
                                msgId = msgs.get(0).getMsgId();
                            }
                            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
                        }
                        messageList.refreshSelectLast();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    protected void onMessageListInit() {
        messageList.init(toChatUsername, chatType, chatFragmentHelper != null ?
                chatFragmentHelper.onSetCustomChatRowProvider() : null);
        setListItemClickListener();

        messageList.getListView().setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                inputMenu.hideExtendMenuContainer();
                return false;
            }
        });

        isMessageListInited = true;
    }

    protected void setListItemClickListener() {
        messageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {

            @Override
            public void onUserAvatarClick(String username) {
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onAvatarClick(username);
                }
            }

            @Override
            public void onUserAvatarLongClick(String username) {
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onAvatarLongClick(username);
                }
            }

            @Override
            public void onResendClick(final EMMessage message) {
                new EaseAlertDialog(getActivity(), R.string.resend, R.string.confirm_resend, null, new EaseAlertDialog.AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (!confirmed) {
                            return;
                        }
                        resendMessage(message);
                    }
                }, true).show();
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {
                contextMenuMessage = message;
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onMessageBubbleLongClick(message);
                }
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                if (chatFragmentHelper == null) {
                    return false;
                }
                return chatFragmentHelper.onMessageBubbleClick(message);
            }
        });
    }

    protected void setRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!isRoaming) {
                            loadMoreLocalMessage();
                        } else {
                            loadMoreRoamingMessages();
                        }
                    }
                }, 600);
            }
        });
    }

    private void loadMoreLocalMessage() {
        if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
            List<EMMessage> messages;
            try {
                messages = conversation.loadMoreMsgFromDB(conversation.getAllMessages().size() == 0 ? "" : conversation.getAllMessages().get(0).getMsgId(),
                        pagesize);
            } catch (Exception e1) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            if (messages.size() > 0) {
                messageList.refreshSeekTo(messages.size() - 1);
                if (messages.size() != pagesize) {
                    haveMoreData = false;
                }
            } else {
                haveMoreData = false;
            }
            isloading = false;
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages),
                    Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadMoreRoamingMessages() {
        if (!haveMoreData) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages),
                    Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        if (fetchQueue != null) {
            fetchQueue.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<EMMessage> messages = conversation.getAllMessages();
                        EMClient.getInstance().chatManager().fetchHistoryMessages(
                                toChatUsername, EaseCommonUtils.getConversationType(chatType), pagesize,
                                (messages != null && messages.size() > 0) ? messages.get(0).getMsgId() : "");
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    } finally {
                        Activity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadMoreLocalMessage();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // capture new image
                if (cameraFile != null && cameraFile.exists())
                    sendImageMessage(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            } else if (requestCode == REQUEST_CODE_MAP) { // location
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    sendLocationMessage(latitude, longitude, locationAddress);
                } else {
                    Toast.makeText(getActivity(), R.string.unable_to_get_loaction, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CODE_SONG) {
                String music_id = data.getStringExtra(_ID);
                String title = data.getStringExtra(TITLE);
                String nickname = data.getStringExtra(NICKNAME);
                String imgpic_link = data.getStringExtra(IMGPIC_LINK);
                if (!TextUtils.isEmpty(music_id)) {
                    EMMessage message = EMMessage.createTxtSendMessage("[歌单]", toChatUsername);
                    message.setAttribute(MSG_TYPE, "song");
                    message.setAttribute(_ID, music_id);
                    message.setAttribute(TITLE, title);
                    message.setAttribute(NICKNAME, nickname);
                    message.setAttribute(IMGPIC_LINK, imgpic_link);
                    message.setAttribute(USER_NAME, toChatuserNickname);
                    message.setAttribute(USER_AVATAR, toChatUserAvatar);
                    message.setAttribute(OTHER_AVATAR, toChatOtherAvatar);
                    message.setAttribute(OTHER_NAME, toChatOtherNickname);
                    sendMessage(message);
                } else {
                    if (null != getActivity()) {
                        Toast.makeText(getActivity(), "歌单不存在哦~", Toast.LENGTH_LONG).show();
                    }
                }
            } else if (requestCode == REQUEST_CODE_MUSIC) {
                String music_id = data.getStringExtra(_ID);
                String title = data.getStringExtra(TITLE);
                String nickname = data.getStringExtra(NICKNAME);
                String imgpic_link = data.getStringExtra(IMGPIC_LINK);
                if (!TextUtils.isEmpty(music_id)) {
                    EMMessage message = EMMessage.createTxtSendMessage("[音乐]", toChatUsername);
                    message.setAttribute(MSG_TYPE, "music");
                    message.setAttribute(_ID, music_id);
                    message.setAttribute(TITLE, title);
                    message.setAttribute(NICKNAME, nickname);
                    message.setAttribute(IMGPIC_LINK, imgpic_link);
                    message.setAttribute(USER_NAME, toChatuserNickname);
                    message.setAttribute(USER_AVATAR, toChatUserAvatar);
                    message.setAttribute(OTHER_AVATAR, toChatOtherAvatar);
                    message.setAttribute(OTHER_NAME, toChatOtherNickname);
                    sendMessage(message);
                } else {
                    if (null != getActivity()) {
                        Toast.makeText(getActivity(), "音乐不存在哦~", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isMessageListInited)
            messageList.refresh();
        EaseUI.getInstance().pushActivity(getActivity());
        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(this);
        EaseUI.getInstance().popActivity(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (groupListener != null) {
            EMClient.getInstance().groupManager().removeGroupChangeListener(groupListener);
        }

        if (chatRoomListener != null) {
            EMClient.getInstance().chatroomManager().removeChatRoomListener(chatRoomListener);
        }
    }

    public void onBackPressed() {
        if (inputMenu.onBackPressed()) {
            getActivity().finish();
        }
    }

    // implement methods in EMMessageListener
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            String username = null;
            // group message
            if (message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }

            // if the message is for current conversation
            if (username.equals(toChatUsername) || message.getTo().equals(toChatUsername) || message.conversationId().equals(toChatUsername)) {
                messageList.refreshSelectLast();
                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
                conversation.markMessageAsRead(message.getMsgId());
            } else {
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object change) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    /**
     * handle the click event for extend menu
     */
    class MyItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
            if (chatFragmentHelper != null) {
                if (chatFragmentHelper.onExtendMenuItemClick(itemId, view)) {
                    return;
                }
            }
            switch (itemId) {
                case ITEM_TAKE_PICTURE:
                    selectPicFromCamera();
                    break;
                case ITEM_PICTURE:
                    selectPicFromLocal();
                    break;
                case ITEM_MUSIC:
                    Intent intent = new Intent("com.mxkj.yuanyintang.ui.pond.AddMusicNew");
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "chat");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_CODE_MUSIC);
                    break;
                case ITEM_SONG_LIST:
                    Intent intent1 = new Intent(getActivity(), SelectorMyCollectionSongListActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putBoolean(SelectorMyCollectionSongListActivity.IS_NEW_MUSIC, true);
                    intent1.putExtras(bundle1);
                    startActivityForResult(intent1, REQUEST_CODE_SONG);
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * input @
     *
     * @param username
     */
    protected void inputAtUsername(String username, boolean autoAddAtSymbol) {
        if (EMClient.getInstance().getCurrentUser().equals(username) || chatType != EaseConstant.CHATTYPE_GROUP) {
            return;
        }
        EaseAtMessageHelper.get().addAtUser(username);
        EaseUser user = EaseUserUtils.getUserInfo(username);
        if (user != null) {
            username = user.getNick();
        }
        if (autoAddAtSymbol)
            inputMenu.insertText("@" + username + " ");
        else
            inputMenu.insertText(username + " ");
    }

    /**
     * input @
     *
     * @param username
     */
    protected void inputAtUsername(String username) {
        inputAtUsername(username, true);
    }

    //send message
    protected void sendTextMessage(String content) {
        //     TODO   消息过滤、以及显示错误状态
        Log.e(TAG, "sendTextMessage:===== " + toChatOtherUid);
        HttpParams params = new HttpParams();
        params.put("content", content);
        params.put("to_uid", toChatOtherUid + "");
        NetWork.INSTANCE.check_msg(getActivity(), params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                JSONObject object = JSON.parseObject(resultData);
                JSONObject data = object.getJSONObject("data");
                if (data != null) {
                    String content1 = data.getString("content");
                    if (EaseAtMessageHelper.get().containsAtUsername(content1)) {

                        sendAtMessage(content1);
                    } else {
                        EMMessage message = EMMessage.createTxtSendMessage(content1, toChatUsername);
                        sendMessage(message);
                    }
                }
            }

            @Override
            public void doError(String msg) {
                Log.e(TAG, "doError: " + msg);
                createErrorMessage(msg);
            }

            @Override
            public void doResult() {

            }
        });
    }

    /**
     * send @ message, only support group chat message
     *
     * @param content
     */
    @SuppressWarnings("ConstantConditions")
    private void sendAtMessage(String content) {
        if (chatType != EaseConstant.CHATTYPE_GROUP) {
            EMLog.e(TAG, "only support group chat message");
            return;
        }
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
        if (EMClient.getInstance().getCurrentUser().equals(group.getOwner()) && EaseAtMessageHelper.get().containsAtAll(content)) {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG, EaseConstant.MESSAGE_ATTR_VALUE_AT_MSG_ALL);
        } else {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG,
                    EaseAtMessageHelper.get().atListToJsonArray(EaseAtMessageHelper.get().getAtMessageUsernames(content)));
        }
        sendMessage(message);

    }


    protected void sendBigExpressionMessage(String name, String identityCode) {
        EMMessage message = EaseCommonUtils.createExpressionMessage(toChatUsername, name, identityCode);
        sendMessage(message);
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        sendMessage(message);
    }

    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        sendMessage(message);
    }

    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        sendMessage(message);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        sendMessage(message);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        sendMessage(message);
    }


    protected void createErrorMessage(String msg) {
        EMMessage message = EMMessage.createTxtSendMessage("[error]", toChatUsername);
        message.setAttribute(MSG_TYPE, "error");
        message.setAttribute(USER_NAME, toChatuserNickname);
        message.setAttribute(USER_AVATAR, toChatUserAvatar);
        message.setAttribute(OTHER_AVATAR, toChatOtherAvatar);
        message.setAttribute(OTHER_NAME, toChatOtherNickname);
        message.setAttribute("text", msg);
        conversation.insertMessage(message);
        EMClient.getInstance().chatManager().saveMessage(message);
        messageList.refreshSelectLast();
        messageList.refresh();
    }


    protected void sendMessage(EMMessage message) {
        if (message == null) {
            return;
        }
        message.setAttribute(USER_NAME, toChatuserNickname);
        message.setAttribute(USER_AVATAR, toChatUserAvatar);
        message.setAttribute(OTHER_AVATAR, toChatOtherAvatar);
        message.setAttribute(OTHER_NAME, toChatOtherNickname);
        message.setAttribute(IS_MUSIC, isMusic);
        message.setAttribute(IS_RELATION, isRelation);
        EMConversation emConversation = EMClient.getInstance().chatManager().getAllConversations().get(message.conversationId());
        if (emConversation != null && null != emConversation.getLastMessage()) {
            message.setAttribute("isStick", emConversation.getLastMessage().getBooleanAttribute("isStick", false));
            message.setAttribute("stickTime", emConversation.getLastMessage().getLongAttribute("stickTime", -1));
            message.setAttribute("isNotDisturb", emConversation.getLastMessage().getBooleanAttribute("isNotDisturb", false));
        }
        if (chatFragmentHelper != null) {
            //set extension
            chatFragmentHelper.onSetMessageAttributes(message);
        }
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            message.setChatType(ChatType.GroupChat);
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(ChatType.ChatRoom);
        }
        //send message
        EMClient.getInstance().chatManager().sendMessage(message);
        //refresh ui
        if (isMessageListInited) {
            messageList.refreshSelectLast();
        }
        RxBus.getDefault().post(new EMECodeEvent(100));


    }

    public void resendMessage(EMMessage message) {
        message.setStatus(EMMessage.Status.CREATE);
        message.setAttribute(USER_NAME, toChatuserNickname);
        message.setAttribute(USER_AVATAR, toChatUserAvatar);
        message.setAttribute(OTHER_AVATAR, toChatOtherAvatar);
        message.setAttribute(OTHER_NAME, toChatOtherNickname);
        EMClient.getInstance().chatManager().sendMessage(message);
        messageList.refresh();
    }

    //===================================================================================


    /**
     * send image
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(file.getAbsolutePath());
        }

    }

    /**
     * send file
     *
     * @param uri
     */
    protected void sendFileByUri(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;

            try {
                cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(getActivity(), R.string.File_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        sendFileMessage(filePath);
    }

    /**
     * capture new image
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(getActivity(), R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        //noinspection ResultOfMethodCallIgnored
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * select local image
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }


    /**
     * clear the conversation history
     */
    protected void emptyHistory() {
        String msg = getResources().getString(R.string.Whether_to_empty_all_chats);
        new EaseAlertDialog(getActivity(), null, msg, null, new EaseAlertDialog.AlertDialogUser() {

            @Override
            public void onResult(boolean confirmed, Bundle bundle) {
                if (confirmed) {
                    if (conversation != null) {
                        conversation.clearAllMessages();
                    }
                    messageList.refresh();
                    haveMoreData = true;
                }
            }
        }, true).show();
    }

    /**
     * open group detail
     */
    protected void toGroupDetails() {
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            if (chatFragmentHelper != null) {
                chatFragmentHelper.onEnterToChatDetails();
            }
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            if (chatFragmentHelper != null) {
                chatFragmentHelper.onEnterToChatDetails();
            }
        }
    }

    /**
     * hide
     */
    protected void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * forward message
     *
     * @param forward_msg_id
     */
    protected void forwardMessage(String forward_msg_id) {
        final EMMessage forward_msg = EMClient.getInstance().chatManager().getMessage(forward_msg_id);
        EMMessage.Type type = forward_msg.getType();
        switch (type) {
            case TXT:
                if (forward_msg.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                    sendBigExpressionMessage(((EMTextMessageBody) forward_msg.getBody()).getMessage(),
                            forward_msg.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null));
                } else {
                    // get the content and send it
                    String content = ((EMTextMessageBody) forward_msg.getBody()).getMessage();
                    sendTextMessage(content);
                }
                break;
            case IMAGE:
                // send image
                String filePath = ((EMImageMessageBody) forward_msg.getBody()).getLocalUrl();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        // send thumb nail if original image does not exist
                        filePath = ((EMImageMessageBody) forward_msg.getBody()).thumbnailLocalPath();
                    }
                    sendImageMessage(filePath);
                }
                break;
            default:
                break;
        }

        if (forward_msg.getChatType() == ChatType.ChatRoom) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(forward_msg.getTo());
        }
    }

    /**
     * listen the group event
     */
    class GroupListener extends EaseGroupListener {

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            getActivity().runOnUiThread(new Runnable() {

                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getActivity(), R.string.you_are_group, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onGroupDestroyed(final String groupId, String groupName) {
            // prompt group is dismissed and finish this activity
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getActivity(), R.string.the_current_group_destroyed, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }
    }

    /**
     * listen chat room event
     */
    class ChatRoomListener extends EaseChatRoomListener {

        @Override
        public void onChatRoomDestroyed(final String roomId, final String roomName) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (roomId.equals(toChatUsername)) {
                        Toast.makeText(getActivity(), R.string.the_current_chat_room_destroyed, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onRemovedFromChatRoom(final String roomId, final String roomName, final String participant) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (roomId.equals(toChatUsername)) {
                        Toast.makeText(getActivity(), R.string.quiting_the_chat_room, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onMemberJoined(final String roomId, final String participant) {
            if (roomId.equals(toChatUsername)) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "member join:" + participant, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @Override
        public void onMemberExited(final String roomId, final String roomName, final String participant) {
            if (roomId.equals(toChatUsername)) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "member exit:" + participant, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }


    }

    protected EaseChatFragmentHelper chatFragmentHelper;

    public void setChatFragmentHelper(EaseChatFragmentHelper chatFragmentHelper) {
        this.chatFragmentHelper = chatFragmentHelper;
    }

    public interface EaseChatFragmentHelper {
        /**
         * set message attribute
         */
        void onSetMessageAttributes(EMMessage message);

        /**
         * enter to chat detail
         */
        void onEnterToChatDetails();

        /**
         * on avatar clicked
         *
         * @param username
         */
        void onAvatarClick(String username);

        /**
         * on avatar long pressed
         *
         * @param username
         */
        void onAvatarLongClick(String username);

        /**
         * on message bubble clicked
         */
        boolean onMessageBubbleClick(EMMessage message);

        /**
         * on message bubble long pressed
         */
        void onMessageBubbleLongClick(EMMessage message);

        /**
         * on extend menu item clicked, return true if you want to override
         *
         * @param view
         * @param itemId
         * @return
         */
        boolean onExtendMenuItemClick(int itemId, View view);

        /**
         * on set custom chat row provider
         *
         * @return
         */
        EaseCustomChatRowProvider onSetCustomChatRowProvider();
    }

}
