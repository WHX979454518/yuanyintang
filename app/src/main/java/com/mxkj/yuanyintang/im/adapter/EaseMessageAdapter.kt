/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mxkj.yuanyintang.im.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView

import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.mxkj.yuanyintang.im.EaseConstant
import com.mxkj.yuanyintang.im.model.styles.EaseMessageListItemStyle
import com.mxkj.yuanyintang.im.ui.EaseChatFragment
import com.mxkj.yuanyintang.im.utils.EaseCommonUtils
import com.mxkj.yuanyintang.im.widget.EaseChatMessageList
import com.mxkj.yuanyintang.im.widget.chatrow.EaseChatRow
import com.mxkj.yuanyintang.im.widget.chatrow.EaseChatRowBigExpression
import com.mxkj.yuanyintang.im.widget.chatrow.EaseChatRowError
import com.mxkj.yuanyintang.im.widget.chatrow.EaseChatRowImage
import com.mxkj.yuanyintang.im.widget.chatrow.EaseChatRowMusic
import com.mxkj.yuanyintang.im.widget.chatrow.EaseChatRowSong
import com.mxkj.yuanyintang.im.widget.chatrow.EaseChatRowText
import com.mxkj.yuanyintang.im.widget.chatrow.EaseChatRowVoice
import com.mxkj.yuanyintang.im.widget.chatrow.EaseCustomChatRowProvider

open class EaseMessageAdapter(private val context: Context, private val toChatUsername: String, chatType: Int, private val listView: ListView) : BaseAdapter() {
    var itemTypeCount: Int = 0
    private val conversation: EMConversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true)
    internal var messages: Array<EMMessage>? = null
    private var itemClickListener: EaseChatMessageList.MessageListItemClickListener? = null
    private var customRowProvider: EaseCustomChatRowProvider? = null
    val isShowUserNick: Boolean = false
    val isShowAvatar: Boolean = false
    val myBubbleBg: Drawable? = null
    val otherBubbleBg: Drawable? = null
    private var itemStyle: EaseMessageListItemStyle? = null

    internal var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        private fun refreshList() {
            val allMsg = conversation.allMessages
            messages = allMsg.toTypedArray()
            conversation.markAllMessagesAsRead()
            notifyDataSetChanged()
        }

        override fun handleMessage(message: android.os.Message) {
            when (message.what) {
                HANDLER_MESSAGE_REFRESH_LIST -> refreshList()
                HANDLER_MESSAGE_SELECT_LAST -> if (messages != null && messages!!.size > 0) {
                    listView.setSelection(messages!!.size - 1)
                }
                HANDLER_MESSAGE_SEEK_TO -> {
                    val position = message.arg1
                    listView.setSelection(position)
                }
                else -> {
                }
            }
        }
    }

    fun refresh() {
        if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
            return
        }
        val msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST)
        handler.sendMessage(msg)
    }

    fun refreshSelectLast() {
        val TIME_DELAY_REFRESH_SELECT_LAST = 100
        handler.removeMessages(HANDLER_MESSAGE_REFRESH_LIST)
        handler.removeMessages(HANDLER_MESSAGE_SELECT_LAST)
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_REFRESH_LIST, TIME_DELAY_REFRESH_SELECT_LAST.toLong())
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_SELECT_LAST, TIME_DELAY_REFRESH_SELECT_LAST.toLong())
    }
    fun refreshSeekTo(position: Int) {
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST))
    }

    override fun getItem(position: Int): EMMessage? {
        return if (messages != null && position < messages!!.size) {
            messages!![position]
        } else null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * get count of messages
     */
    override fun getCount(): Int {
        return if (messages == null) 0 else messages!!.size
    }

    /**
     * get number of message type, here 14 = (EMMessage.Type) * 2
     */
    override fun getViewTypeCount(): Int {
        return if (customRowProvider != null && customRowProvider!!.customChatRowTypeCount > 0) {
            customRowProvider!!.customChatRowTypeCount + 14
        } else 14
    }

    /**
     * get type of item
     */
    override fun getItemViewType(position: Int): Int {
        val message = getItem(position) ?: return -1
        if (customRowProvider != null && customRowProvider!!.getCustomChatRowType(message) > 0) {
            return customRowProvider!!.getCustomChatRowType(message) + 13
        }
        if (message.type == EMMessage.Type.TXT) {
            if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                return if (message.direct() == EMMessage.Direct.RECEIVE) MESSAGE_TYPE_RECV_EXPRESSION else MESSAGE_TYPE_SENT_EXPRESSION
            }
            Log.e(TAG, "getItemViewType: " + message.getStringAttribute(EaseChatFragment.MSG_TYPE, ""))
            when (message.getStringAttribute(EaseChatFragment.MSG_TYPE, "")) {
                "music" -> return if (message.direct() == EMMessage.Direct.RECEIVE) MESSAGE_TYPE_RECV_MUSIC else MESSAGE_TYPE_SENT_MUSIC
                "song" -> return if (message.direct() == EMMessage.Direct.RECEIVE) MESSAGE_TYPE_RECV_SONG else MESSAGE_TYPE_SENT_SONG
                "error" -> return MESSAGE_TYPE_ERROR
                "" -> return if (message.direct() == EMMessage.Direct.RECEIVE) MESSAGE_TYPE_RECV_TXT else MESSAGE_TYPE_SENT_TXT
            }
        }
        if (message.type == EMMessage.Type.IMAGE) {
            return if (message.direct() == EMMessage.Direct.RECEIVE) MESSAGE_TYPE_RECV_IMAGE else MESSAGE_TYPE_SENT_IMAGE
        }
        //        if (message.getType() == EMMessage.Type.LOCATION) {
        //            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
        //        }
        return if (message.type == EMMessage.Type.VOICE) {
            if (message.direct() == EMMessage.Direct.RECEIVE) MESSAGE_TYPE_RECV_VOICE else MESSAGE_TYPE_SENT_VOICE
        } else -1
        //        if (message.getType() == EMMessage.Type.VIDEO) {
        //            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
        //        }
        //        if (message.getType() == EMMessage.Type.FILE) {
        //            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
        //        }
        // invalid
    }

    protected fun createChatRow(context: Context, message: EMMessage?, position: Int): EaseChatRow? {
        var chatRow: EaseChatRow? = null
        if (customRowProvider != null && customRowProvider!!.getCustomChatRow(message, position, this) != null) {
            return customRowProvider!!.getCustomChatRow(message, position, this)
        }


        Log.e(TAG, "createChatRow: " + message!!.type)

        when (message.type) {
            EMMessage.Type.TXT -> if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                chatRow = EaseChatRowBigExpression(context, message, position, this)
            } else {
                when (message.getStringAttribute(EaseChatFragment.MSG_TYPE, "")) {
                    "music" -> chatRow = EaseChatRowMusic(context, message, position, this)
                    "song" -> chatRow = EaseChatRowSong(context, message, position, this)
                    "" -> chatRow = EaseChatRowText(context, message, position, this)
                    "error" -> {
                        Log.e(TAG, "createChatRow:错误信息 ")
                        chatRow = EaseChatRowError(context, message, position, this)
                    }
                }
            }
        //            case LOCATION:
        //                chatRow = new EaseChatRowLocation(context, message, position, this);
        //                break;
        //            case FILE:
        //                chatRow = new EaseChatRowFile(context, message, position, this);
        //                break;
            EMMessage.Type.IMAGE -> chatRow = EaseChatRowImage(context, message, position, this)
            EMMessage.Type.VOICE -> chatRow = EaseChatRowVoice(context, message, position, this)
        //            case VIDEO:
        //                chatRow = new EaseChatRowVideo(context, message, position, this);
        //                break;
            else -> {
            }
        }
        return chatRow
    }

    @SuppressLint("NewApi")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val message = getItem(position)
        if (convertView == null) {
            convertView = createChatRow(context, message, position)
        }
        (convertView as EaseChatRow).setUpView(message, position, itemClickListener, itemStyle)
        return convertView
    }

    fun setItemStyle(itemStyle: EaseMessageListItemStyle) {
        this.itemStyle = itemStyle
    }

    fun setItemClickListener(listener: EaseChatMessageList.MessageListItemClickListener) {
        itemClickListener = listener
    }

    fun setCustomChatRowProvider(rowProvider: EaseCustomChatRowProvider) {
        customRowProvider = rowProvider
    }

    companion object {
        private val TAG = "msg"
        private val HANDLER_MESSAGE_REFRESH_LIST = 0
        private val HANDLER_MESSAGE_SELECT_LAST = 1
        private val HANDLER_MESSAGE_SEEK_TO = 2
        private val MESSAGE_TYPE_RECV_TXT = 0
        private val MESSAGE_TYPE_SENT_TXT = 1
        private val MESSAGE_TYPE_SENT_IMAGE = 2
        private val MESSAGE_TYPE_SENT_LOCATION = 3
        private val MESSAGE_TYPE_RECV_LOCATION = 4
        private val MESSAGE_TYPE_RECV_IMAGE = 5
        private val MESSAGE_TYPE_SENT_VOICE = 6
        private val MESSAGE_TYPE_RECV_VOICE = 7
        private val MESSAGE_TYPE_SENT_VIDEO = 8
        private val MESSAGE_TYPE_RECV_VIDEO = 9
        private val MESSAGE_TYPE_SENT_FILE = 10
        private val MESSAGE_TYPE_RECV_FILE = 11
        private val MESSAGE_TYPE_SENT_EXPRESSION = 12
        private val MESSAGE_TYPE_RECV_EXPRESSION = 13
        private val MESSAGE_TYPE_SENT_MUSIC = 14
        private val MESSAGE_TYPE_RECV_MUSIC = 15
        private val MESSAGE_TYPE_SENT_SONG = 16
        private val MESSAGE_TYPE_RECV_SONG = 17
        private val MESSAGE_TYPE_ERROR = 18
    }
}