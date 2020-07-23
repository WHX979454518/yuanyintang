package com.mxkj.yuanyintang.im.adapter

import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.TextView.BufferType

import com.hyphenate.chat.EMChatRoom
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMConversation.EMConversationType
import com.hyphenate.chat.EMGroup
import com.hyphenate.chat.EMMessage
import com.hyphenate.util.DateUtils
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.im.EaseUI
import com.mxkj.yuanyintang.im.domain.EaseAvatarOptions
import com.mxkj.yuanyintang.im.domain.EaseUser
import com.mxkj.yuanyintang.im.model.EaseAtMessageHelper
import com.mxkj.yuanyintang.im.utils.EaseCommonUtils
import com.mxkj.yuanyintang.im.utils.EaseSmileUtils
import com.mxkj.yuanyintang.im.utils.EaseUserUtils
import com.mxkj.yuanyintang.im.widget.EaseConversationList
import com.mxkj.yuanyintang.im.widget.EaseImageView

import java.util.ArrayList
import java.util.Date

open class EaseConversationAdapter(context: Context, resource: Int, val conversationList: MutableList<EMConversation>) : ArrayAdapter<EMConversation>(context, resource, conversationList) {
    private val copyConversationList: MutableList<EMConversation>
    private var conversationFilter: ConversationFilter? = null
    private var notiyfyByFilter: Boolean = false

    private var primaryColor: Int = 0
    private var secondaryColor: Int = 0
    private var timeColor: Int = 0
    private var primarySize: Int = 0
    private var secondarySize: Int = 0
    private var timeSize: Float = 0.toFloat()

    private var cvsListHelper: EaseConversationList.EaseConversationListHelper? = null

    init {
        copyConversationList = ArrayList()
        copyConversationList.addAll(conversationList)
    }

    override fun getCount(): Int {
        return conversationList.size
    }

    override fun getItem(arg0: Int): EMConversation? {
        return if (arg0 < conversationList.size) {
            conversationList[arg0]
        } else null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ease_row_chat_history, parent, false)
        }
        var holder: ViewHolder? = convertView!!.tag as ViewHolder
        if (holder == null) {
            holder = ViewHolder()
            holder.name = convertView.findViewById<View>(R.id.name) as TextView
            holder.unreadLabel = convertView.findViewById<View>(R.id.unread_msg_number) as TextView
            holder.message = convertView.findViewById<View>(R.id.message) as TextView
            holder.time = convertView.findViewById<View>(R.id.time) as TextView
            holder.avatar = convertView.findViewById<View>(R.id.avatar) as ImageView
            holder.msgState = convertView.findViewById(R.id.msg_state)
            holder.list_itease_layout = convertView.findViewById<View>(R.id.list_itease_layout) as RelativeLayout
            holder.motioned = convertView.findViewById<View>(R.id.mentioned) as TextView
            convertView.tag = holder
        }
        holder.list_itease_layout!!.setBackgroundResource(R.drawable.ease_mm_listitem)
        val conversation = getItem(position)
        val username = conversation!!.conversationId()
        when {
            conversation.type == EMConversationType.GroupChat -> {
                val groupId = conversation.conversationId()
                if (EaseAtMessageHelper.get().hasAtMeMsg(groupId)) {
                    holder.motioned!!.visibility = View.VISIBLE
                } else {
                    holder.motioned!!.visibility = View.GONE
                }
                holder.avatar!!.setImageResource(R.drawable.ease_group_icon)
                val group = EMClient.getInstance().groupManager().getGroup(username)
                holder.name!!.text = if (group != null) group.groupName else username
            }
            conversation.type == EMConversationType.ChatRoom -> {
                holder.avatar!!.setImageResource(R.drawable.ease_group_icon)
                val room = EMClient.getInstance().chatroomManager().getChatRoom(username)
                holder.name!!.text = if (room != null && !TextUtils.isEmpty(room.name)) room.name else username
                holder.motioned!!.visibility = View.GONE
            }
            else -> {
                EaseUserUtils.setUserAvatar(context, username, holder.avatar)
                EaseUserUtils.setUserNick(username, holder.name)
                holder.motioned!!.visibility = View.GONE
            }
        }

        val avatarOptions = EaseUI.getInstance().avatarOptions
        if (avatarOptions != null && holder.avatar is EaseImageView) {
            val avatarView = holder.avatar as EaseImageView?
            if (avatarOptions.avatarShape != 0)
                avatarView!!.setShapeType(avatarOptions.avatarShape)
            if (avatarOptions.avatarBorderWidth != 0)
                avatarView!!.setBorderWidth(avatarOptions.avatarBorderWidth)
            if (avatarOptions.avatarBorderColor != 0)
                avatarView!!.setBorderColor(avatarOptions.avatarBorderColor)
            if (avatarOptions.avatarRadius != 0)
                avatarView!!.setRadius(avatarOptions.avatarRadius)
        }
        if (conversation.unreadMsgCount > 0) {
            holder.unreadLabel!!.text = conversation.unreadMsgCount.toString()
            holder.unreadLabel!!.visibility = View.VISIBLE
        } else {
            holder.unreadLabel!!.visibility = View.INVISIBLE
        }

        if (conversation.allMsgCount != 0) {
            val lastMessage = conversation.lastMessage
            var content: String? = null
            if (cvsListHelper != null) {
                content = cvsListHelper!!.onSetItemSecondaryText(lastMessage)
            }
            holder.message!!.setText(EaseSmileUtils.getSmiledText(context, EaseCommonUtils.getMessageDigest(lastMessage, this.context)),
                    BufferType.SPANNABLE)
            if (content != null) {
                holder.message!!.text = content
            }
            holder.time!!.text = DateUtils.getTimestampString(Date(lastMessage.msgTime))
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState!!.visibility = View.VISIBLE
            } else {
                holder.msgState!!.visibility = View.GONE
            }
        }
        holder.name!!.setTextColor(primaryColor)
        holder.message!!.setTextColor(secondaryColor)
        holder.time!!.setTextColor(timeColor)
        if (primarySize != 0)
            holder.name!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize.toFloat())
        if (secondarySize != 0)
            holder.message!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondarySize.toFloat())
        if (timeSize != 0f)
            holder.time!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeSize)

        return convertView
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        if (!notiyfyByFilter) {
            copyConversationList.clear()
            copyConversationList.addAll(conversationList)
            notiyfyByFilter = false
        }
    }

    override fun getFilter(): Filter {
        if (conversationFilter == null) {
            conversationFilter = ConversationFilter(conversationList)
        }
        return conversationFilter as ConversationFilter
    }


    fun setPrimaryColor(primaryColor: Int) {
        this.primaryColor = primaryColor
    }

    fun setSecondaryColor(secondaryColor: Int) {
        this.secondaryColor = secondaryColor
    }

    fun setTimeColor(timeColor: Int) {
        this.timeColor = timeColor
    }

    fun setPrimarySize(primarySize: Int) {
        this.primarySize = primarySize
    }

    fun setSecondarySize(secondarySize: Int) {
        this.secondarySize = secondarySize
    }

    fun setTimeSize(timeSize: Float) {
        this.timeSize = timeSize
    }


    private inner class ConversationFilter(mList: List<EMConversation>) : Filter() {
        internal var mOriginalValues: List<EMConversation>? = null

        init {
            mOriginalValues = mList
        }

        override fun performFiltering(prefix: CharSequence?): Filter.FilterResults {
            val results = Filter.FilterResults()

            if (mOriginalValues == null) {
                mOriginalValues = ArrayList()
            }
            if (prefix == null || prefix.isEmpty()) {
                results.values = copyConversationList
                results.count = copyConversationList.size
            } else {
                if (copyConversationList.size > mOriginalValues!!.size) {
                    mOriginalValues = copyConversationList
                }
                val prefixString = prefix.toString()
                val count = mOriginalValues!!.size
                val newValues = ArrayList<EMConversation>()

                for (i in 0 until count) {
                    val value = mOriginalValues!![i]
                    var username = value.conversationId()

                    val group = EMClient.getInstance().groupManager().getGroup(username)
                    if (group != null) {
                        username = group.groupName
                    } else {
                        val user = EaseUserUtils.getUserInfo(username)
                    }

                    if (username.startsWith(prefixString)) {
                        newValues.add(value)
                    } else {
                        val words = username.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val wordCount = words.size
                        for (word in words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(value)
                                break
                            }
                        }
                    }
                }

                results.values = newValues
                results.count = newValues.size
            }
            return results
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            conversationList.clear()
            if (results.values != null) {
                conversationList.addAll(results.values as List<EMConversation>)
            }
            if (results.count > 0) {
                notiyfyByFilter = true
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }

    fun setCvsListHelper(cvsListHelper: EaseConversationList.EaseConversationListHelper) {
        this.cvsListHelper = cvsListHelper
    }

    private class ViewHolder {
        /** who you chat with  */
        internal var name: TextView? = null
        /** unread message count  */
        internal var unreadLabel: TextView? = null
        /** content of last message  */
        internal var message: TextView? = null
        /** time of last message  */
        internal var time: TextView? = null
        /** avatar  */
        internal var avatar: ImageView? = null
        /** status of last message  */
        internal var msgState: View? = null
        /** layout  */
        internal var list_itease_layout: RelativeLayout? = null
        internal var motioned: TextView? = null
    }
}

