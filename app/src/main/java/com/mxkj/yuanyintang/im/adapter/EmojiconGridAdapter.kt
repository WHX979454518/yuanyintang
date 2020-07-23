package com.mxkj.yuanyintang.im.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.im.domain.EaseEmojicon
import com.mxkj.yuanyintang.im.utils.EaseSmileUtils
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader

class EmojiconGridAdapter(context: Context, textViewResourceId: Int, objects: List<EaseEmojicon>, private val emojiconType: EaseEmojicon.Type) : ArrayAdapter<EaseEmojicon>(context, textViewResourceId, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = if (emojiconType == EaseEmojicon.Type.BIG_EXPRESSION) {
                View.inflate(context, R.layout.ease_row_big_expression, null)
            } else {
                View.inflate(context, R.layout.ease_row_expression, null)
            }
        }

        val imageView = convertView!!.findViewById<View>(R.id.iv_expression) as ImageView
        val textView = convertView.findViewById<View>(R.id.tv_name) as TextView
        val emojicon = getItem(position)
        if (textView != null && emojicon!!.name != null) {
            textView.text = emojicon.name
        }

        if (EaseSmileUtils.DELETE_KEY == emojicon!!.emojiText) {
            imageView.setImageResource(R.drawable.ease_delete_expression)
        } else {
            if (emojicon.icon != 0) {
                imageView.setImageResource(emojicon.icon)
            } else if (emojicon.iconPath != null) {
                ImageLoader.with(context)
                        .url(emojicon.iconPath)
                        .placeHolder(R.drawable.ease_default_expression)
                        .into(imageView)
            }
        }


        return convertView
    }

}
