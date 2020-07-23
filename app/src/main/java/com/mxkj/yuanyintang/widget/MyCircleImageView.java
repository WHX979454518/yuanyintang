package com.mxkj.yuanyintang.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by fire on 2017/11/12.
 * Date：2017/11/12
 * Author: fire
 * Description:
 */

public class MyCircleImageView extends CircleImageView {

    private int mWidth;
    private int mHeight;
    private Paint mPaint;

    public MyCircleImageView(Context context) {
        super(context);
        initPaint(context);
    }

    public MyCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public MyCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint(context);
    }

    private void initPaint(Context context) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);// 设置红色
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2f, getWidth() / 2f, getWidth() / 2f, mPaint);
        super.onDraw(canvas);
    }
}
