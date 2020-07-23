package com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mxkj.yuanyintang.R;

public class CircleProgressView extends View {
    private int max=100; //最大值
    private float outRoundWidth; //圆的宽度
    private float inRoundWidth; //圆的宽度
    private int progress; //当前进度
    public static final int STROKE = 0;
    public static final int FILL = 1;
    private Bitmap logoBitmap;
    private int center;

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar);
        outRoundWidth = typedArray.getDimension(R.styleable.CustomProgressBar_roundWidthBar, 34);
        inRoundWidth = typedArray.getDimension(R.styleable.CustomProgressBar_roundWidthBar_in, 28);
        Glide.with(context).load(R.drawable.img_scaning).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                logoBitmap = resource;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOut(canvas);
        drawIn(canvas);
//        drawLogo(canvas);

    }

    private void drawLogo(Canvas canvas) {
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        if (logoBitmap != null) {
            int bWidth = logoBitmap.getWidth();
            int bHeight = logoBitmap.getHeight();
            Rect mSrcRect, mDestRect;
            mSrcRect = new Rect(bWidth,bWidth,bWidth,bHeight);
            mDestRect = new Rect(getWidth() / 2,getWidth() / 2,0,0);
            canvas.drawBitmap(logoBitmap,mSrcRect,mDestRect,mPaint);
        }
    }

    private void drawOut(Canvas canvas) {
        Paint mPaint = new Paint();
        //画背景圆环
        center = getWidth() / 2;
        //设置半径
        float radius = center - outRoundWidth;
        //设置圆圈的颜色
        mPaint.setColor(Color.parseColor("#ffecf6"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(outRoundWidth);//圆环的宽度
        mPaint.setAntiAlias(true);//设置抗锯齿

        //画外圈
        canvas.drawCircle(center, center, radius, mPaint);

        //画进度百分比
//        mPaint.setColor(Color.parseColor("#ffebf5"));
//        mPaint.setStrokeWidth(0);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //画圆弧
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        mPaint.setColor(Color.parseColor("#ffebf5"));
        mPaint.setStrokeWidth(outRoundWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        //设置笔帽
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //话进度
        canvas.drawArc(oval, 0, 360 * progress / max, false, mPaint);

    }

    private void drawIn(Canvas canvas) {
        Paint mPaint = new Paint();
        //画背景圆环
        center = getWidth() / 2;
        //设置半径
        float radius = center - outRoundWidth - inRoundWidth;
        //设置圆圈的颜色
        mPaint.setColor(Color.parseColor("#ffe4f2"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(inRoundWidth);//圆环的宽度
        mPaint.setAntiAlias(true);//设置抗锯齿

        //画外圈
        canvas.drawCircle(center, center, radius, mPaint);

        //画进度百分比
        mPaint.setColor(Color.parseColor("#ffdbed"));
        mPaint.setStrokeWidth(0);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //画圆弧
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        mPaint.setColor(Color.parseColor("#ffdbed"));
        mPaint.setStrokeWidth(inRoundWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        //设置笔帽
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //话进度
        canvas.drawArc(oval, 180, -360 * progress / max, false, mPaint);

    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setProgress(int progress) {
        if (progress < 0) {
//            throw new IllegalArgumentException("进度progress不能小于0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }
}