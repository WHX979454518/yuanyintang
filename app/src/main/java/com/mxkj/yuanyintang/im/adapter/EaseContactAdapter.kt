package com.mxkj.yuanyintang.im.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import android.util.SparseIntArray
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.SectionIndexer
import android.widget.TextView

import com.mxkj.yuanyintang.R
import com.hyphenate.util.EMLog
import com.mxkj.yuanyintang.im.EaseUI
import com.mxkj.yuanyintang.im.domain.EaseAvatarOptions
import com.mxkj.yuanyintang.im.domain.EaseUser
import com.mxkj.yuanyintang.im.utils.EaseUserUtils
import com.mxkj.yuanyintang.im.widget.EaseImageView

import java.util.ArrayList

open class EaseContactAdapter(context: Context, private val res: Int, internal var userList: MutableList<EaseUser>) : ArrayAdapter<EaseUser>(context, res, userList), SectionIndexer {
    internal lateinit var list: MutableList<String>
    internal var copyUserList: MutableList<EaseUser> = ArrayList()
    private val layoutInflater: LayoutInflater
    private var positionOfSection: SparseIntArray? = null
    private var sectionOfPosition: SparseIntArray? = null
    private var myFilter: MyFilter? = null
    private var notiyfyByFilter: Boolean = false

    private var primaryColor: Int = 0
    private var primarySize: Int = 0
    private var initialLetterBg: Drawable? = null
    private var initialLetterColor: Int = 0

    init {
        copyUserList.addAll(userList)
        layoutInflater = LayoutInflater.from(context)
    }

    private class ViewHolder {
        internal var avatar: ImageView? = null
        internal var nameView: TextView? = null
        internal var headerView: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            if (res == 0)
                convertView = layoutInflater.inflate(R.layout.ease_row_contact, parent, false)
            else
                convertView = layoutInflater.inflate(res, null)
            holder.avatar = convertView!!.findViewById<View>(R.id.avatar) as ImageView
            holder.nameView = convertView.findViewById<View>(R.id.name) as TextView
            holder.headerView = convertView.findViewById<View>(R.id.header) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val user = getItem(position)
        if (user == null)
            Log.d("ContactAdapter", position.toString() + "")
        val username = user!!.username
        val header = user.initialLetter

        if (position == 0 || header != null && header != getItem(position - 1)!!.initialLetter) {
            if (TextUtils.isEmpty(header)) {
                holder.headerView!!.visibility = View.GONE
            } else {
                holder.headerView!!.visibility = View.VISIBLE
                holder.headerView!!.text = header
            }
        } else {
            holder.headerView!!.visibility = View.GONE
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

        EaseUserUtils.setUserNick(username, holder.nameView)
        EaseUserUtils.setUserAvatar(context, username, holder.avatar)


        if (primaryColor != 0)
            holder.nameView!!.setTextColor(primaryColor)
        if (primarySize != 0)
            holder.nameView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize.toFloat())
        if (initialLetterBg != null)
            holder.headerView!!.setBackgroundDrawable(initialLetterBg)
        if (initialLetterColor != 0)
            holder.headerView!!.setTextColor(initialLetterColor)

        return convertView
    }

    override fun getItem(position: Int): EaseUser? {
        return super.getItem(position)
    }

    override fun getCount(): Int {
        return super.getCount()
    }

    override fun getPositionForSection(section: Int): Int {
        return positionOfSection!!.get(section)
    }

    override fun getSectionForPosition(position: Int): Int {
        return sectionOfPosition!!.get(position)
    }

    override fun getSections(): Array<String> {
        positionOfSection = SparseIntArray()
        sectionOfPosition = SparseIntArray()
        val count = count
        list = ArrayList()
        list.add(context.getString(R.string.search_header))
        positionOfSection!!.put(0, 0)
        sectionOfPosition!!.put(0, 0)
        for (i in 1 until count) {

            val letter = getItem(i)!!.initialLetter
            var section = list.size - 1
            if (list[section] != null && list[section] != letter) {
                list.add(letter)
                section++
                positionOfSection!!.put(section, i)
            }
            sectionOfPosition!!.put(i, section)
        }
        return list.toTypedArray()
    }

    override fun getFilter(): Filter {
        if (myFilter == null) {
            myFilter = MyFilter(userList)
        }
        return myFilter as MyFilter
    }

    protected inner class MyFilter(myList: List<EaseUser>) : Filter() {
        internal var mOriginalList: List<EaseUser>? = null

        init {
            this.mOriginalList = myList
        }

        @Synchronized
        override fun performFiltering(prefix: CharSequence?): Filter.FilterResults {
            val results = Filter.FilterResults()
            if (mOriginalList == null) {
                mOriginalList = ArrayList()
            }
            EMLog.d(TAG, "contacts original size: " + mOriginalList!!.size)
            EMLog.d(TAG, "contacts copy size: " + copyUserList.size)

            if (prefix == null || prefix.length == 0) {
                results.values = copyUserList
                results.count = copyUserList.size
            } else {

                if (copyUserList.size > mOriginalList!!.size) {
                    mOriginalList = copyUserList
                }
                val prefixString = prefix.toString()
                val count = mOriginalList!!.size
                val newValues = ArrayList<EaseUser>()
                for (i in 0 until count) {
                    val user = mOriginalList!![i]
                    val username = user.username

                    if (username.startsWith(prefixString)) {
                        newValues.add(user)
                    } else {
                        val words = username.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val wordCount = words.size

                        // Start at index 0, in case valueText starts with space(s)
                        for (word in words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(user)
                                break
                            }
                        }
                    }
                }
                results.values = newValues
                results.count = newValues.size
            }
            EMLog.d(TAG, "contacts filter results size: " + results.count)
            return results
        }

        @Synchronized
        override fun publishResults(constraint: CharSequence,
                                    results: Filter.FilterResults) {
            userList.clear()
            userList.addAll(results.values as List<EaseUser>)
            EMLog.d(TAG, "publish contacts filter results size: " + results.count)
            if (results.count > 0) {
                notiyfyByFilter = true
                notifyDataSetChanged()
                notiyfyByFilter = false
            } else {
                notifyDataSetInvalidated()
            }
        }
    }


    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        if (!notiyfyByFilter) {
            copyUserList.clear()
            copyUserList.addAll(userList)
        }
    }

    fun setPrimaryColor(primaryColor: Int): EaseContactAdapter {
        this.primaryColor = primaryColor
        return this
    }


    fun setPrimarySize(primarySize: Int): EaseContactAdapter {
        this.primarySize = primarySize
        return this
    }

    fun setInitialLetterBg(initialLetterBg: Drawable): EaseContactAdapter {
        this.initialLetterBg = initialLetterBg
        return this
    }

    fun setInitialLetterColor(initialLetterColor: Int): EaseContactAdapter {
        this.initialLetterColor = initialLetterColor
        return this
    }

    companion object {
        private val TAG = "ContactAdapter"
    }

}
