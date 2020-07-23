package com.mxkj.yuanyintang.mainui.newapp.pond;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mxkj.yuanyintang.base.MainApplication;
import com.sina.weibo.sdk.utils.UIUtils;
public class PondTouchLinearLayout extends LinearLayout {
    private float downX;
    private float downY;
    private float scrollY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//TODO  下滑，判断是不是到顶  ，到顶则消费，上滑，直接顶上去
        int minH = UIUtils.dip2px(64, getContext());
        int maxH = UIUtils.dip2px(135, getContext());
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - downX;
                float dy = ev.getY() - downY;
                if (Math.abs(dy) > Math.abs(dx) + 60) {
                    if (dy > 0 && layoutParams.topMargin < maxH&& MainApplication.Companion.isTop()==true) {//下滑
                        layoutParams.setMargins(0, maxH, 0, 0);
                        setLayoutParams(layoutParams);
                        return true;
                    } else
                    if (dy < 0 && layoutParams.topMargin >minH) {
                        layoutParams.setMargins(0, layoutParams.topMargin - 10, 0, 0);
                        setLayoutParams(layoutParams);
                        return true;
                    }
                }

        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int minH = UIUtils.dip2px(64, getContext());
        int maxH = UIUtils.dip2px(135, getContext());
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - downX;
                float dy = ev.getY() - downY;
                if (Math.abs(dy) > Math.abs(dx) + 60) {
//                    TODO  从recycler传来是不是已经到顶，到顶再处理
                    if (dy > 0 && layoutParams.topMargin < maxH&& MainApplication.Companion.isTop()==true) {//下滑
                        layoutParams.setMargins(0, layoutParams.topMargin + 10, 0, 0);
                        setLayoutParams(layoutParams);
                        return true;
                    } else if (dy < 0 && layoutParams.topMargin > minH) {
                        layoutParams.setMargins(0, layoutParams.topMargin - 10, 0, 0);
                        setLayoutParams(layoutParams);
                        return true;
                    }
                }

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int minH = UIUtils.dip2px(64, getContext());
        int maxH = UIUtils.dip2px(135, getContext());
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                float dx = ev.getX() - downX;
                float dy = ev.getY() - downY;
                if (Math.abs(dy) > Math.abs(dx) + 60) {
                    if (dy > 0 && layoutParams.topMargin < maxH) {//下滑
                        layoutParams.setMargins(0, maxH, 0, 0);
                        setLayoutParams(layoutParams);
                        return true;
                    } else if (dy < 0 && layoutParams.topMargin > minH) {
                        layoutParams.setMargins(0, minH, 0, 0);
                        setLayoutParams(layoutParams);
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx2 = ev.getX() - downX;
                float dy2 = ev.getY() - downY;
                if (Math.abs(dy2) > Math.abs(dx2) + 60) {
                    if (dy2 > 0 && layoutParams.topMargin < maxH&&MainApplication.Companion.isTop()) {//下滑
                        layoutParams.setMargins(0, (int) (layoutParams.topMargin+Math.abs(dy2)), 0, 0);
                        setLayoutParams(layoutParams);
                        return true;
                    } else if (dy2 < 0 && layoutParams.topMargin > minH) {
                        layoutParams.setMargins(0, (int) (layoutParams.topMargin - Math.abs(dy2)), 0, 0);
                        setLayoutParams(layoutParams);
                        return true;
                    }
                }
        }
        return super.onTouchEvent(ev);
    }

    public PondTouchLinearLayout(Context context) {
        super(context);
    }

    public PondTouchLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
