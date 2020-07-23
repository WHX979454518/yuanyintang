package com.mxkj.yuanyintang.mainui.newapp.weidget

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.Config
import android.graphics.PorterDuff.Mode
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import com.mxkj.yuanyintang.utils.app.CommonUtils

class RoundCornorImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ImageView(context, attrs, defStyle) {

    private val paint: Paint = Paint()

    /**
     * 绘制圆角矩形图片
     * @author caizhiming
     */
    override fun onDraw(canvas: Canvas) {

        val mDrawable = drawable
        var bitmap: Bitmap

        if (null != drawable) {
            when (mDrawable) {
                is BitmapDrawable -> {
                    bitmap = mDrawable.bitmap
                }
                else -> {
                    val w = mDrawable.intrinsicWidth
                    val h = mDrawable.intrinsicHeight
                    bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    canvas.drawColor(Color.WHITE)
                    mDrawable.setBounds(0, 0, w, h)
                    mDrawable.draw(canvas)
                }
            }
            val b = getRoundBitmap(bitmap, CommonUtils.dip2px(context, 6F))
            val rectSrc = Rect(0, 0, b.width, b.height)
            val rectDest = Rect(0, 0, width, height)
            paint.reset()
            canvas.drawBitmap(b, rectSrc, rectDest, paint)

        } else {
            super.onDraw(canvas)
        }
    }

    private fun getRoundBitmap(bitmap: Bitmap, roundPx: Int): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width,
                bitmap.height, Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = -0xbdbdbe

        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        val x = bitmap.width

        canvas.drawRoundRect(rectF, roundPx.toFloat(), roundPx.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output


    }
}
