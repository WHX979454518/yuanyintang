package com.mxkj.yuanyintang.base

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.os.SystemClock
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils
import android.util.Log
import android.widget.Toast

import com.alibaba.fastjson.JSON
import com.hyphenate.EMCallBack
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMCmdMessageBody
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMMessage.ChatType
import com.hyphenate.chat.EMMessage.Type
import com.hyphenate.chat.EMOptions
import com.hyphenate.chat.EMTextMessageBody
import com.hyphenate.util.EMLog
import com.hyphenate.util.NetUtils
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.bean.UserInfo
import com.mxkj.yuanyintang.im.EaseConstant
import com.mxkj.yuanyintang.im.EaseUI
import com.mxkj.yuanyintang.im.domain.EaseAvatarOptions
import com.mxkj.yuanyintang.im.domain.EaseEmojicon
import com.mxkj.yuanyintang.im.domain.EaseUser
import com.mxkj.yuanyintang.im.domain.EmojiconExampleGroupData
import com.mxkj.yuanyintang.im.model.EaseAtMessageHelper
import com.mxkj.yuanyintang.im.ui.EaseChatFragment
import com.mxkj.yuanyintang.im.utils.EaseCommonUtils
import com.mxkj.yuanyintang.mainui.message.ChatActivity
import com.mxkj.yuanyintang.mainui.message.activity.CommentsListActivity
import com.mxkj.yuanyintang.mainui.message.activity.SystemMessageListActivity
import com.mxkj.yuanyintang.mainui.message.bean.MessageEMMDataBean
import com.mxkj.yuanyintang.utils.uiutils.NotifyUtil
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.rxbus.RxBus
import com.mxkj.yuanyintang.utils.rxbus.event.EMECodeEvent

import java.util.HashSet
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

open class EaseHelper {
    private var mIsInit: Boolean = false

    private var easeUI: EaseUI? = null

    /**
     * EMEventListener
     */
    private var messageListener: EMMessageListener? = null
    private var appContext: Context? = null

    private var broadcastManager: LocalBroadcastManager? = null

    private var isGroupAndContactListenerRegisted: Boolean = false

    private val executor: ExecutorService

    protected var handler: android.os.Handler? = null

    internal var msgQueue: Queue<String> = ConcurrentLinkedQueue()

    lateinit var connectionListener: EMConnectionListener

    //    根据conversationId  判断消息类型,   CommentsListActivity: admin    动态消息: circle   评论消息: warden
    private var currentNotify: NotifyUtil? = null
    private val requestCode = SystemClock.uptimeMillis().toInt()
    protected var fromUsers = HashSet<String>()
    protected var notificationNum = 0

    /**
     * data sync listener
     */

    init {
        executor = Executors.newCachedThreadPool()
    }

    fun execute(runnable: Runnable) {
        executor.execute(runnable)
    }

    /**
     * init helper
     *
     * @param context application context
     */
    fun init(context: Context) {
        synchronized(this) {
            if ((!mIsInit)) {
                easeUI = EaseUI.getInstance()
                val options = initChatOptions()
                easeUI!!.init(context, options)
                if (EaseUI.getInstance().init(context, options)) {
                    appContext = context
                    EMClient.getInstance().setDebugMode(true)
                    setEaseUIProviders()
                    PreferenceManager.init(context)
                    setGlobalListeners()
                    broadcastManager = LocalBroadcastManager.getInstance(appContext)
                }
                mIsInit = true
            }
        }
    }

    private fun initChatOptions(): EMOptions {
        Log.d(TAG, "init HuanXin Options")
        val options = EMOptions()
        options.acceptInvitationAlways = false
        options.requireAck = true
        options.autoLogin = true
        options.requireDeliveryAck = false
        easeUI!!.settingsProvider = object : EaseUI.EaseSettingsProvider {
            //扬声器
            override fun isSpeakerOpened(): Boolean {
                return false
            }

            //震动
            override fun isMsgVibrateAllowed(message: EMMessage): Boolean {
                return false
            }

            //声音
            override fun isMsgSoundAllowed(message: EMMessage): Boolean {
                return false
            }

            //通知栏
            override fun isMsgNotifyAllowed(message: EMMessage): Boolean {
                return false
            }
        }
        return options
    }

    protected fun setEaseUIProviders() {
        val avatarOptions = EaseAvatarOptions()
        avatarOptions.avatarShape = 1
        easeUI!!.avatarOptions = avatarOptions
        easeUI!!.userProfileProvider = EaseUI.EaseUserProfileProvider { username -> getUserInfo(username) }
        //set emoji icon provider
        easeUI!!.emojiconInfoProvider = object : EaseUI.EaseEmojiconInfoProvider {

            override fun getEmojiconInfo(emojiconIdentityCode: String): EaseEmojicon? {
                val data = EmojiconExampleGroupData.getData()
                for (emojicon in data.emojiconList) {
                    if (emojicon.identityCode == emojiconIdentityCode) {
                        return emojicon
                    }
                }
                return null
            }

            override fun getTextEmojiconMapping(): Map<String, Any>? {
                return null
            }
        }

        //set notification options, will use default if you don't set it
        //        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotificationInfoProvider() {
        //
        //            @Override
        //            public String getTitle(EMMessage message) {
        //                //you can update title here
        //                return MainApplication.Companion.context.getString(R.string.app_name);
        //            }
        //
        //            @Override
        //            public int getSmallIcon(EMMessage message) {
        //                //you can update icon here
        //                return R.mipmap.ic_launcher;
        //            }
        //
        //            @Override
        //            public String getDisplayedText(EMMessage message) {
        //                Log.d(TAG, "getDisplayedText: -----通知栏收到消息----->");
        //                // be used on notification bar, different text according the message type.
        //                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
        //                if (message.getType() == Type.TXT) {
        //                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
        //                }
        //                EaseUser user = getUserInfo(message.getFrom());
        //                if (user != null) {
        //                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
        //                        return String.format(appContext.getString(R.string.at_your_in_group), user.getNick());
        //                    }
        //                    return user.getNick() + ": " + ticker;
        //                } else {
        //                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
        //                        return String.format(appContext.getString(R.string.at_your_in_group), message.getFrom());
        //                    }
        //                    return message.getFrom() + ": " + ticker;
        //                }
        //            }
        //
        //            @Override
        //            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
        //                // here you can customize the text.
        //                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
        //                Log.d(TAG, "getLatestText: ----------->");
        //                return null;
        //            }
        //
        //            @Override
        //            public Intent getLaunchIntent(EMMessage message) {
        //                // you can set what activity you want display when user click the notification
        //                Intent intent = new Intent(appContext, ChatActivity.class);
        //                // open calling activity if there is call
        //                ChatType chatType = message.getChatType();
        //                if (chatType == ChatType.Chat) { // single chat message
        //                    intent.putExtra("userId", message.getFrom());
        //                    intent.putExtra("chatType", Constants.EaseConstant.CHATTYPE_SINGLE);
        //                }
        //                return intent;
        //            }
        //        });
    }

    /**
     * set global listener
     */
    protected fun setGlobalListeners() {
        // create the global connection listener
        connectionListener = object : EMConnectionListener {
            override fun onDisconnected(error: Int) {
                EMLog.d("global listener", "onDisconnect$error")
                if (error == EMError.USER_REMOVED) {// 显示帐号已经被移除
                    RxBus.getDefault().post(EMECodeEvent(201))
                    CacheUtils.setBoolean(appContext, Constants.User.IS_LOGIN, false)
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {// 显示帐号在其他设备登录
                    RxBus.getDefault().post(EMECodeEvent(202))
                    CacheUtils.setBoolean(appContext, Constants.User.IS_LOGIN, false)
                } else if (error == EMError.USER_REMOVED) {//账户被删除
                    RxBus.getDefault().post(EMECodeEvent(201))
                    CacheUtils.setBoolean(appContext, Constants.User.IS_LOGIN, false)
                } else if (error == EMError.USER_NOT_FOUND) {//账户不存在
                    RxBus.getDefault().post(EMECodeEvent(201))
                    CacheUtils.setBoolean(appContext, Constants.User.IS_LOGIN, false)
                } else {
                    //连接不到聊天服务器
                    if (NetUtils.hasNetwork(MainApplication.Companion.context)) {

                    } else {

                    }//当前网络不可用，请检查网络设置
                }
            }

            override fun onConnected() {

            }
        }

        //register connection listener
        EMClient.getInstance().addConnectionListener(connectionListener)
        //register group and contact event listener
        //        registerGroupAndContactListener();
        //register message event listener
        registerMessageListener()

    }

    internal fun showToast(message: String) {
        Log.d(TAG, "receive invitation to join the group：$message")
        if (handler != null) {
            val msg = Message.obtain(handler, 0, message)
            handler!!.sendMessage(msg)
        } else {
            msgQueue.add(message)
        }
    }

    private fun getUserInfo(username: String): EaseUser {
        // To get instance of EaseUser, here we get it from the user list in memory
        // You'd better cache it if you get it from your server
        var user: EaseUser? = null
        //        if (username.equals(EMClient.getInstance().getCurrentUser()))
        //            return getUserProfileManager().getCurrentUserInfo();
        //        user = getContactList().get(username);
        //        if (user == null && getRobotList() != null) {
        //            user = getRobotList().get(username);
        //        }
        // if user is not in your contacts, set inital letter for him/her
        if (user == null) {
            user = EaseUser(username)
            EaseCommonUtils.setUserInitialLetter(user)
        }
        user = EaseUser(username)
        return user
    }

    /**
     * Global listener
     * If this event already handled by an activity, you don't need handle it again
     * activityList.size() <= 0 means all activities already in background or not in Activity Stack
     */
    private fun registerMessageListener() {
        messageListener = object : EMMessageListener {
            val broadCastReceiver: BroadcastReceiver? = null
            override fun onMessageReceived(messages: List<EMMessage>) {
                for (message in messages) {
                    RxBus.getDefault().post(EMECodeEvent(100))

                    Log.d(TAG, "onMessageReceived: ------->" + message.conversationId())
                    Log.d(TAG, "onMessageReceived: ------->" + message.type)
                    Log.d(TAG, "onMessageReceived: ------->" + message.from)
                    Log.d(TAG, "onMessageReceived: ---user---->" + message.userName)
                    Log.d(TAG, "onMessageReceived: ----to--->" + message.to)
                    Log.d(TAG, "onMessageReceived: ------->" + message.getStringAttribute(EaseChatFragment.USER_NAME, ""))
                    Log.d(TAG, "onMessageReceived: ------->" + message.getStringAttribute(EaseChatFragment.OTHER_NAME, ""))
                    Log.d(TAG, "onMessageReceived: ------->" + message.from)
                    Log.d(TAG, "onMessageReceived: ------->" + message.ext().toString())
                    Log.d(TAG, "onMessageReceived: ----box--->" + message.body.toString())
                    Log.d(TAG, "onMessageReceived: ------->" + message.ext()["uid"])
                    Log.d(TAG, "onMessageReceived: ------->" + message.ext()["data"])
                    val conversation = EMClient.getInstance().chatManager().getConversation(message.conversationId())
                    if (conversation.allMessages.size > 1) {
                        val emMessage = conversation.allMessages[conversation.allMessages.size - 2]
                        if (emMessage.getBooleanAttribute("isStick", false)) {
                            conversation.lastMessage.setAttribute("isStick", true)
                            conversation.lastMessage.setAttribute("stickTime", conversation.lastMessage.getLongAttribute("stickTime", -1))
                        }
                        if (emMessage.getBooleanAttribute("isNotDisturb", false)) {
                            conversation.lastMessage.setAttribute("isNotDisturb", true)
                        }
                    }
                    if (TextUtils.equals("admin", message.conversationId())) {
                        conversation.lastMessage.setAttribute("isNotDisturb", true)
                        EMClient.getInstance().chatManager().getConversation("admin").lastMessage.ext()["data"] = message.getStringAttribute("data", "")
                    } else if (TextUtils.equals("warden", message.conversationId())) {
                        conversation.lastMessage.setAttribute("isNotDisturb", false)
                        val emMessage = EMClient.getInstance().chatManager().getConversation("warden").lastMessage
                        emMessage.setAttribute("item_type", "warden")
                        val warden = emMessage.ext()
                        warden["data"] = message.getStringAttribute("data", "")

                    } else if (TextUtils.equals("circle", message.conversationId())) {
                        val ext = message.ext()
                        var userIcon: String? = null
                        if (ext != null) {
                            userIcon = ext["userAvatar"] as String
                        }
                        val receive_new_msg = Intent()
                        receive_new_msg.action = "receive_new_msg"
                        receive_new_msg.putExtra("userIcon", userIcon)
                        appContext!!.sendBroadcast(receive_new_msg)

                    } else {
                        if (CacheUtils.getBoolean(appContext, Constants.Other.IS_NOT_CONCERN_NOT_DISTURB_MSG)) {
                            when {
                                TextUtils.equals("0", message.getStringAttribute("toRelation", "")) -> {
                                    conversation.lastMessage.setAttribute("isNotDisturb", true)
                                    EMClient.getInstance().chatManager().getConversation("msgBox").lastMessage.ext()["data"] = message.getStringAttribute("data", "")
                                }
                                TextUtils.equals("", message.getStringAttribute("toRelation", "")) -> {
                                    conversation.lastMessage.setAttribute("isNotDisturb", true)
                                    EMClient.getInstance().chatManager().getConversation("msgBox").lastMessage.ext()["data"] = message.getStringAttribute("data", "")
                                }
                                else -> {
                                    conversation.lastMessage.setAttribute("isNotDisturb", false)
                                    EMClient.getInstance().chatManager().getConversation("msgBox").lastMessage.ext()["data"] = message.getStringAttribute("data", "")
                                }
                            }
                        }
                    }

                    if (!easeUI!!.hasForegroundActivies()) {
                    }
                    if (!CacheUtils.getBoolean(appContext, Constants.Other.IS_NIGHT_NOT_DISTURB_MSG, false)) {
                        if (MainApplication.application!!.currentActivity is ChatActivity) {
                            if (!TextUtils.equals(message.userName, (MainApplication.application!!.currentActivity as ChatActivity).toChatUsername)) {
                                notifierMsg(message)
                            }
                        } else {
                            notifierMsg(message)
                        }
                        RxBus.getDefault().post(EMECodeEvent(100))
                    }
                }
                EMClient.getInstance().chatManager().importMessages(messages)
                RxBus.getDefault().post(EMECodeEvent(100))
            }

            override fun onCmdMessageReceived(messages: List<EMMessage>) {
                for (message in messages) {
                    EMLog.d(TAG, "receive command message")
                    //get message body
                    val cmdMsgBody = message.body as EMCmdMessageBody
                    val action = cmdMsgBody.action()//获取自定义action
                    if (action == "__Call_ReqP2P_ConferencePattern") {
                        val title = message.getStringAttribute("em_apns_ext", "conference call")
                        Toast.makeText(appContext, title, Toast.LENGTH_LONG).show()
                    }
                    //end of red packet code
                    //获取扩展属性 此处省略
                    //maybe you need get extension of your message
                    //message.getStringAttribute("");
                    EMLog.d(TAG, String.format("Command：action:%s,message:%s", action, message.toString()))
                }
            }

            override fun onMessageRead(messages: List<EMMessage>) {

            }

            override fun onMessageDelivered(message: List<EMMessage>) {}

            override fun onMessageRecalled(messages: List<EMMessage>) {
                for (msg in messages) {
                    if (msg.chatType == ChatType.GroupChat && EaseAtMessageHelper.get().isAtMeMsg(msg)) {
                        EaseAtMessageHelper.get().removeAtMeGroup(msg.to)
                    }
                    val msgNotification = EMMessage.createReceiveMessage(Type.TXT)
                    val txtBody = EMTextMessageBody(String.format(appContext!!.getString(R.string.msg_recall_by_user), msg.from))
                    msgNotification.addBody(txtBody)
                    msgNotification.from = msg.from
                    msgNotification.to = msg.to
                    msgNotification.isUnread = false
                    msgNotification.msgTime = msg.msgTime
                    msgNotification.setLocalTime(msg.msgTime)
                    msgNotification.chatType = msg.chatType
                    msgNotification.setAttribute(Constants.EaseConstant.MESSAGE_TYPE_RECALL, true)
                    EMClient.getInstance().chatManager().saveMessage(msgNotification)
                }
            }

            override fun onMessageChanged(message: EMMessage, change: Any) {

            }
        }
        EMClient.getInstance().chatManager().addMessageListener(messageListener)
    }

    private fun notifierMsg(message: EMMessage) {
        val smallIcon = R.mipmap.ic_launcher
        var ticker = ""
        var title = ""
        var content = ""
        var intent: Intent? = null

        when {
            TextUtils.equals("admin", message.conversationId()) -> {
                ticker = msg_ch[3]
                title = "源小依"
                intent = Intent(appContext, SystemMessageListActivity::class.java)
                content = notifyContent(message)

            }
            TextUtils.equals("circle", message.conversationId()) -> {

            }
            TextUtils.equals("warden", message.conversationId()) -> {
                ticker = when {
                    TextUtils.isEmpty(message.getStringAttribute("data", "")) -> msg_ch[4]
                    JSON.parseObject(message.ext()["data"].toString(), MessageEMMDataBean::class.java) == null -> msg_ch[4]
                    else -> JSON.parseObject(message.ext()["data"].toString(), MessageEMMDataBean::class.java).content
                }
                title = "源小依"
                intent = Intent(appContext, CommentsListActivity::class.java)
                content = notifyContent(message)

            }
            TextUtils.equals("msgBox", message.conversationId()) -> {

            }
            else -> {
                ticker = msg_ch[0]
                title = message.getStringAttribute(EaseChatFragment.USER_NAME, "")
                when {
                    message.type == EMMessage.Type.TXT -> content = (message.body as EMTextMessageBody).message
                    message.type == EMMessage.Type.IMAGE -> content = msg_ch[1]
                    message.type == EMMessage.Type.VOICE -> content = msg_ch[2]
                }
                fromUsers.add(message.from)
                val bundle = Bundle()
                //         TODO   我也没明白刘杰写这里几个ID代表的啥
                bundle.putString(Constants.EaseConstant.EXTRA_USER_ID, StringUtils.isEmpty(message.conversationId()))
                bundle.putString(EaseConstant.EXTRA_OTHER_UID, StringUtils.isEmpty(message.conversationId()))
                bundle.putString(EaseConstant.EXTRA_USER_ID, StringUtils.isEmpty(message.conversationId()))
                if (message.direct() == EMMessage.Direct.SEND) {
                    bundle.putString(EaseChatFragment.OTHER_NAME, message.getStringAttribute(EaseConstant.EXTRA_OTHER_NAME, ""))
                    bundle.putString(EaseChatFragment.OTHER_AVATAR, message.getStringAttribute(EaseConstant.EXTRA_OTHER_AVATAR, ""))
                } else {
                    bundle.putString(EaseChatFragment.OTHER_NAME, message.getStringAttribute(EaseConstant.EXTRA_USER_NAME, ""))
                    bundle.putString(EaseChatFragment.OTHER_AVATAR, message.getStringAttribute(EaseConstant.EXTRA_USER_AVATAR, ""))
                }
                intent = Intent(appContext, ChatActivity::class.java)
                intent.putExtras(bundle)
            }
        }

        if (!TextUtils.equals("circle", message.conversationId())) {//TODO屏蔽动态消息
            //实例化工具类，并且调用接口
            val pIntent = PendingIntent.getActivity(appContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val notify1 = NotifyUtil(appContext, NOTIFY_ID)
            notify1.notify_normal_singline(pIntent, smallIcon, ticker, title, content, Constants.Other.getIsSettingMsgSound(), Constants.Other.getIsSettingMsgVibrate(), false)
            currentNotify = notify1
        }

    }

    private fun notifyContent(message: EMMessage): String {
        var content = ""
        if (null != message.ext()["data"]) {
            val messageDataBean = JSON.parseObject(message.ext()["data"].toString(), MessageEMMDataBean::class.java)
            if (null != messageDataBean) {
                content = StringUtils.isEmpty(messageDataBean.content)
                return content
            }
        }
        return content
    }

    /**
     * this function can be override
     */
    fun resetNotification() {
        resetNotificationCount()
        cancelNotificaton()
    }

    internal fun resetNotificationCount() {
        notificationNum = 0
        fromUsers.clear()
    }

    internal fun cancelNotificaton() {
        currentNotify!!.clear()
    }

    @Synchronized
    internal fun reset() {
        isGroupAndContactListenerRegisted = false
    }

    interface EmLoginListener {
        fun onSuccess()

        fun onError(message: String)
    }

    companion object {

        protected val TAG = "DemoHelper"

        private var instance: EaseHelper? = null

        @Synchronized
        fun getInstance(): EaseHelper {
            if (instance == null) {
                instance = EaseHelper()
            }
            return instance as EaseHelper
        }

        protected const val NOTIFY_ID = 2733
        protected val msg_eng = arrayOf("sent a message", "sent a picture", "sent a voice", "sent location message", "sent a video", "sent a file", "%1 contacts sent %2 messages")
        protected val msg_ch = arrayOf("您收到一条新消息", "[图片]", "[语音]", "源小依给你发送了一条系统消息", "源小依给你发送了一条评论消息", "发来一个文件", "%1个联系人发来%2条消息")

        fun emLogin(resultData: String?, emLoginListener: EmLoginListener?) {
            resultData?.let {
                val userInfo = JSON.parseObject(resultData,UserInfo::class.java)
                if (null == userInfo?.data) {
                    return
                }
                EMClient.getInstance().login(userInfo?.data?.id.toString() + "", "123456", object : EMCallBack {
                    override fun onSuccess() {
                        RxBus.getDefault().post(EMECodeEvent(100))
                        EMClient.getInstance().groupManager().loadAllGroups()
                        EMClient.getInstance().chatManager().loadAllConversations()
                        emLoginListener?.onSuccess()
                    }

                    override fun onProgress(progress: Int, status: String) {

                    }

                    override fun onError(code: Int, message: String) {
                        emLoginListener?.onError("$message  code$code")
                    }
                })
            }
        }

        fun setMsgBoxContent(conversation: EMConversation) {
            val conversationMsgBox = EMClient.getInstance().chatManager().getConversation("msgBox")
            if (conversation.lastMessage.ext()["item_type"] == null) {
                when {
                    conversation.lastMessage.type == EMMessage.Type.TXT -> conversationMsgBox.lastMessage.setAttribute("content", StringUtils.isEmpty((conversation.lastMessage.body as EMTextMessageBody).message))
                    conversation.lastMessage.type == EMMessage.Type.IMAGE -> conversationMsgBox.lastMessage.setAttribute("content", "[图片]")
                    conversation.lastMessage.type == EMMessage.Type.VOICE -> conversationMsgBox.lastMessage.setAttribute("content", "[语音]")
                }
            } else {
                val messageDataBean = JSON.parseObject(conversation.lastMessage.ext()["data"].toString(), MessageEMMDataBean::class.java)
                conversationMsgBox.lastMessage.setAttribute("content", StringUtils.isEmpty(messageDataBean.content))
            }
        }
    }

}
