package com.mxkj.yuanyintang.mainui.dynamic.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.FragmentManager
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.mainui.comment.Comment
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.utils.RichTextutils.ImageTextUtil
import com.mxkj.yuanyintang.widget.CommentEditDialogFrag

import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.Companion.MUSICIAN_ID

class SonCommentAdapter(internal var dataBean: Comment.DataBean, internal var sonBeanList: List<Comment.DataBean.SonBean>, internal var supportFragmentManager: FragmentManager, internal var context: Context) : BaseAdapter() {
    private var editDialogFrag: CommentEditDialogFrag? = null

    override fun getCount(): Int {
        return if (sonBeanList.size > 3) {
            3
        } else sonBeanList.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
        var view = view
        view = LayoutInflater.from(context).inflate(R.layout.item_soncomment, null)
        val startFrom2: Int
        val endFrom1: Int
        val str1: String
        val length: Int
        val endFrom2: Int
        val tvSoncomment = view.findView<TextView>(R.id.tv_soncomment)
        if (sonBeanList.size > i) {
            if (sonBeanList[i].to_nickname == null) {
                sonBeanList[i].revert = " "
            }
            if (sonBeanList[i].nickname == null) {
                sonBeanList[i].nickname = " "
            }
            if (TextUtils.isEmpty(sonBeanList[i].to_nickname) || sonBeanList[i].to_nickname == sonBeanList[i].nickname) {
                str1 = sonBeanList[i].nickname!! + " : "
                length = str1.length
                endFrom1 = sonBeanList[i].nickname!!.length
                val ssb = SpannableStringBuilder(str1)
                val clickSpan = MyClickableSpan(context, sonBeanList[i].nickname, sonBeanList[i])
                ssb.setSpan(clickSpan, 0, endFrom1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                val clickSpan3 = MyClickableSpan(context, sonBeanList[i].content, sonBeanList[i])
                ssb.setSpan(clickSpan3, endFrom1 + 1, length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tvSoncomment.append(ssb)
                tvSoncomment!!.movementMethod = LinkMovementMethod.getInstance()
                ImageTextUtil.setImageText(context, tvSoncomment, sonBeanList[i].content, 1)

            } else {
                str1 = sonBeanList[i].nickname + " 回复 " + sonBeanList[i].to_nickname + " : "
                length = str1.length
                endFrom1 = sonBeanList[i].nickname!!.length
                startFrom2 = endFrom1 + 4//回复的前后加了一个空格
                endFrom2 = startFrom2 + sonBeanList[i].to_nickname!!.length
                val ssb = SpannableStringBuilder(str1)
                val clickSpan = MyClickableSpan(context, sonBeanList[i].nickname, sonBeanList[i])
                ssb.setSpan(clickSpan, 0, endFrom1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                val clickSpan2 = MyClickableSpan(context, sonBeanList[i].to_nickname, sonBeanList[i])
                ssb.setSpan(clickSpan2, startFrom2, endFrom2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                val clickSpan3 = MyClickableSpan(context, sonBeanList[i].content, sonBeanList[i])
                ssb.setSpan(clickSpan3, endFrom2 + 1, length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tvSoncomment!!.append(ssb)
                tvSoncomment!!.movementMethod = LinkMovementMethod.getInstance()
                ImageTextUtil.setImageText(context, tvSoncomment, sonBeanList[i].content, 1)
            }
        }
        return view
    }

    internal inner class MyClickableSpan(private val context: Context, private val text: String?, private val sonBean: Comment.DataBean.SonBean?) : ClickableSpan() {

        //在这里设置字体的大小，等待各种属性
        override fun updateDrawState(ds: TextPaint) {
            if (text != null && sonBean!!.nickname != null && sonBean.to_nickname != null) {
                if (text == sonBean.nickname || text == sonBean.to_nickname) {
                    ds.color = Color.parseColor("#5bc3f3")
                }
            }
        }

        override fun onClick(widget: View) {
            if (sonBean != null) {
                if (text == sonBean.nickname) {
                    val intent = Intent(context, MusicIanDetailsActivity::class.java)
                    intent.putExtra(MUSICIAN_ID, sonBean.uid.toString() + "")
                    context.startActivity(intent)
                } else if (text == sonBean.to_nickname) {
                    val intent = Intent(context, MusicIanDetailsActivity::class.java)
                    intent.putExtra(MUSICIAN_ID, sonBean.to_uid.toString() + "")
                    context.startActivity(intent)
                } else if (text == sonBean.content) {
                    editDialogFrag = CommentEditDialogFrag(sonBean.nickname, 0, 0, sonBean.id, 0, CommentEditDialogFrag.successCallBack {
                        //TODO  发布成功
                    })
                    if (editDialogFrag != null) {
                        if (editDialogFrag!!.isAdded) {
                            editDialogFrag!!.dismiss()
                        }
                    }
                    editDialogFrag!!.show(supportFragmentManager, "")
                }

            }
        }

    }

    private fun <T : View> View.findView(viewId: Int): T {
        var viewHolder: SparseArray<View> = tag as? SparseArray<View> ?: SparseArray()
        tag = viewHolder
        var childView = viewHolder.get(viewId)
        if (null == childView) {
            childView = findViewById(viewId)
            viewHolder.put(viewId, childView)
        }
        return childView as T
    }


}
