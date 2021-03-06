package com.mxkj.yuanyintang.utils;/**
 * Created by hp on 2016/9/6.
 */

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ScrollView;

import com.mxkj.yuanyintang.R;


/**
 * 时间：2019/1/7 14:52
 */
public class PullScrollView extends ScrollView implements ViewTreeObserver.OnGlobalLayoutListener {

    View view;
    int srcTopMargion;
    float lastY;
    float offsetY;

    public PullScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewTreeObserver observer = getViewTreeObserver();
        if (null != observer)
            observer.addOnGlobalLayoutListener(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view = findViewById(R.id.tvBck);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float y = ev.getY();
        Log.d("onTouchEvent", "action=" + action + ",y=" + y);
        MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算滑动y方向偏移值
                offsetY = y - lastY;
                //向下移动
                if (offsetY > 0) {
                    //滑动到看到所有图片展示，交给原来的逻辑处理
                    if (params.topMargin == 0) {
                        return super.onTouchEvent(ev);
                    }
                    //在不是下拉图片的时候，向下移动，交给原来的逻辑处理
                    if (getScrollY() != 0) {
                        return super.onTouchEvent(ev);
                    }
                    //可以下拉图片的情况
                    params.topMargin += offsetY / 10;
                    Log.d("onTouchEvent", "topMargin" + params.topMargin + ",lastY=" + lastY + ",y=" + y + ",offsetY" + offsetY);
                    if (params.topMargin >= 0) {
                        params.topMargin = 0;
                    }
                    view.setLayoutParams(params);
                    invalidate();
                }
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                //不和原始margion偏移一样的时候
                if (params.topMargin != -srcTopMargion) {
                    Log.d("ACTION_UP", "moveY=" + (srcTopMargion + params.topMargin));
                    //滚动原始偏移值和现在偏移值之间的差值 eg:3~10
                    ObjectAnimator animator = ObjectAnimator.ofInt(this, "moveY", params.topMargin, -srcTopMargion);
                    animator.setDuration(200);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.start();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 设置移动中的Y值
     *
     * @param value
     */
    public void setMoveY(int value) {
        MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
        params.topMargin = value;
        Log.d("computeScroll", "topMargin=" + params.topMargin);
        view.setLayoutParams(params);
        invalidate();
    }

    @Override
    public void onGlobalLayout() {
        MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
        srcTopMargion = -params.topMargin;
        Log.d("srcTopMargion", "" + srcTopMargion);
        getViewTreeObserver()
                .removeGlobalOnLayoutListener(this);
    }
}
