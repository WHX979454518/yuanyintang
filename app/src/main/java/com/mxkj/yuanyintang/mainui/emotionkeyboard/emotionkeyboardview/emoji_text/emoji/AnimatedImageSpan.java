package com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.emoji_text.emoji;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.style.DynamicDrawableSpan;


public class AnimatedImageSpan extends DynamicDrawableSpan {

    private Drawable mDrawable;

    public AnimatedImageSpan(Drawable d) {
        super();
        mDrawable = d;
        final Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            public void run() {
                ((AnimatedGifDrawable) mDrawable).nextFrame();
                mHandler.postDelayed(this, ((AnimatedGifDrawable) mDrawable).getFrameDuration());
            }
        });
    }

    @Override
    public Drawable getDrawable() {
        return ((AnimatedGifDrawable) mDrawable).getDrawable();
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        if (d == null)
            return 0;
        Rect rect = d.getBounds();

        if (fm != null) {
            fm.ascent = -rect.bottom;
            fm.descent = 0;

            fm.top = fm.ascent;
            fm.bottom = 0;
        }

        return rect.right;
    }
    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        if (drawable == null)
            return;
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        //构造函数中有一个对齐方式的参数，但只提供了 ALIGN_BASELINE（顶部对齐）、ALIGN_BOTTOM （底部对齐）两种对齐方式，没有居中对齐的方式， 所以只能重写ImageSpan实现图片的居中。
        int transY = (y + fontMetricsInt.descent + y + fontMetricsInt.ascent) / 2 - drawable.getBounds().bottom/2;
        canvas.save();
        canvas.translate(x,transY);
        drawable.draw(canvas);
        canvas.restore();
//================================
//        Drawable b = getDrawable();
//        if (b == null)
//            return;
//        canvas.save();
//        int transY = bottom - b.getBounds().bottom;
//        if (mVerticalAlignment == ALIGN_BOTTOM) {
//            transY -= paint.getFontMetricsInt().descent;
//        }
//        canvas.translate(x, transY);
//        b.draw(canvas);
//        canvas.restore();
    }
}
