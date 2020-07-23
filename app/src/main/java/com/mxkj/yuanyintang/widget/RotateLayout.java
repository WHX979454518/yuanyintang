package com.mxkj.yuanyintang.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

public class RotateLayout extends FrameLayout {
    /**
     * 判断view是点击还是移动的距离
     */
    private int mTouchSlop;
    /**
     * 关闭窗口的判断值
     */
    private float mCloseDegree = 60;
    /**
     * 窗口的最大旋转度数
     */
    private float mClosedDegree = 85;

    /**
     * 触摸最后一次的坐标
     */
    private float mLastX;
    private float mLastY;
    /**
     * 正在拖动
     */
    private boolean isTouchMove = false;
    private Context mContext;
    /**
     * 记录手势速度
     */
    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private float mInterceptX = 0;
    private float mInterceptY = 0;
    private boolean isLeftToRight = false;

    //动画
    private ValueAnimator mAnimator;
    private boolean mAnimatorIsCancel = false;
    private RotateListener mRotateListener;
    /**
     * 背景视图，用来现遮罩层
     */
    private View mBackgroundView;
    /**
     * 不拦截视图
     */
    private View mIgnoreView;
    /**
     * 竖直滑动视图
     */
    private View mVerticalScrollView;
    /**
     * 是否是竖直滑动
     */
    private boolean isVerticalScroll = true;
    private float mStartX;
    private float mStartY;
    private int verticalMinDistance = 40;
    public boolean isInterCept = false;

    public RotateLayout(Context context) {
        super(context);
        init(context);
    }

    public RotateLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RotateLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        final ViewConfiguration configuration = ViewConfiguration
                .get(getContext());
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int mScreensWidth = display.getWidth();
        int mScreensHeigh = display.getHeight();
        ViewHelper.setPivotX(this, mScreensWidth * 0.5f);
        ViewHelper.setPivotY(this, 1.5f * mScreensHeigh);
        ViewHelper.setRotation(this, mClosedDegree);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float distanceY = event.getY() - mStartY;
                float distanceX = event.getX() - mStartX;

//                if (Math.abs(distanceX) > verticalMinDistance) {
//                    水平滑动距离大于竖直距离
                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    return true;
//                        竖直距离大于水平+70，加一个向下缩放消失的动画
                } else if (Math.abs(distanceY) - 70 > Math.abs(distanceX)) {
//                        TODO  animation

                    return true;
                } else {
//
                    return false;
                }
//                }
//                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    /**
     * 是否在不处理视图中
     *
     * @param event
     * @return
     */
    private boolean isInIgnoreView(MotionEvent event) {
        if (mIgnoreView != null) {
            int[] location = new int[2];
            mIgnoreView.getLocationOnScreen(location);
            int mDragViewTopY = location[1];
            int mDragViewButtomY = mDragViewTopY + mIgnoreView.getHeight();
            if ((mDragViewTopY <= event.getRawY() && event.getRawY() <= mDragViewButtomY)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            obtainVelocityTracker(event);
            int actionId = event.getAction();
            switch (actionId) {
                case MotionEvent.ACTION_DOWN:
                    //旋转后需要获取的是在屏幕上面的位置，不再是view里面的位置
                    mStartX = event.getX();
                    mLastX = event.getRawX();
                    mLastY = event.getRawY();
                    mInterceptX = event.getRawX();
                    mInterceptY = event.getRawY();
                    if (mVerticalScrollView != null) {
                        mVerticalScrollView.onTouchEvent(event);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isVerticalScroll) {
                        float curX = event.getRawX();
                        float curY = event.getRawY();
                        int deltaX = (int) (mInterceptX - curX);
                        int deltaY = (int) (mInterceptY - curY);
                        isLeftToRight = (mLastX - curX) < 0;
                        ////如果linerlayout下没有view消费move事件
                        if (isTouchMove || (Math.abs(deltaX) > mTouchSlop && Math.abs(deltaY) < mTouchSlop && !isInIgnoreView(event))) {
                            isTouchMove = true;
                            //根据当前的tan值计算角度
                            float lastW = getWidth() * 0.5f - mInterceptX;
                            float lastH = 1.5f * getHeight() - mInterceptY;
                            float lastDegree = (float) Math.toDegrees(Math.atan(lastH / lastW));
                            if (lastDegree < 0) {
                                lastDegree += 180;
                            }
                            float curW = getWidth() * 0.5f - curX;
                            float curH = 1.5f * getHeight() - curY;
                            float curDegree = (float) Math.toDegrees(Math.atan(curH / curW));
                            if (curDegree < 0) {
                                curDegree += 180;
                            }
                            float degree = curDegree - lastDegree;
                            //设置旋转中心，中心位置在view视图下方
                            ViewHelper.setPivotX(this, getWidth() * 0.5f);
                            ViewHelper.setPivotY(this, 1.5f * getHeight());
                            ViewHelper.setRotation(this, degree);
                            //绘画遮罩层
                            drawMask();
                        } else if ((Math.abs(deltaY) > mTouchSlop && Math.abs(deltaX) < mTouchSlop && !isInIgnoreView(event))) {
                            if (mVerticalScrollView != null) {
                                isVerticalScroll = true;
                                mVerticalScrollView.onTouchEvent(event);
                            }
                        }


                        mLastX = curX;
                        mLastY = curY;
                    } else {
                        if (mVerticalScrollView != null) {
                            mVerticalScrollView.onTouchEvent(event);
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    //判断点击事件
                    float curX = event.getRawX();
                    float curY = event.getRawY();
                    int deltaX = (int) (mInterceptX - curX);
                    int deltaY = (int) (mInterceptY - curY);
                    if (Math.abs(deltaX) < mTouchSlop && Math.abs(deltaY) < mTouchSlop) {
                        if (mRotateListener != null) {
                            mRotateListener.onClick();
                            return true;
                        }
                    }

                    if (isVerticalScroll) {
                        //先判断是否有竖直滑动view
                        if (mVerticalScrollView != null) {
                            mVerticalScrollView.onTouchEvent(event);
                        }
                        isVerticalScroll = false;
                        releaseVelocityTracker();
                        mInterceptX = 0;
                        mInterceptY = 0;
                        mLastX = 0;
                        mLastY = 0;
                        isTouchMove = false;
                        isLeftToRight = false;
                        return true;
                    }

                    int deltaXX = (int) (mInterceptX - mLastX);
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);

                    int xVelocity = (int) velocityTracker.getXVelocity();
                    Log.e("TAGTAG", "isTouchMove = " + isTouchMove);
                    Log.e("TAGTAG", "Math.abs(xVelocity) = " + Math.abs(xVelocity));
                    Log.e("TAGTAG", "mMinimumVelocity * 3 = " + mMinimumVelocity * 3);
                    Log.e("TAGTAG", "!isInIgnoreView(event) = " + !isInIgnoreView(event));
                    Log.e("TAGTAG", "Math.abs(deltaXX) = " + Math.abs(deltaXX));
                    Log.e("TAGTAG", "mTouchSlop = " + mTouchSlop);
                    if (isTouchMove && Math.abs(xVelocity) > mMinimumVelocity * 3 && !isInIgnoreView(event) && Math.abs(deltaXX) > mTouchSlop) {
                        if (isLeftToRight) {
                            closeView();
                        } else {
                            closeConvertView();
                        }
                    } else {
                        if (this.getRotation() > mCloseDegree) {
                            closeView();
                        } else if (this.getRotation() < -mCloseDegree) {
                            closeConvertView();
                        } else {
                            resetView();
                        }
                    }
                    releaseVelocityTracker();
                    mInterceptX = 0;
                    mInterceptY = 0;
                    mLastX = 0;
                    mLastY = 0;
                    isTouchMove = false;
                    isLeftToRight = false;

                    break;
                default:

            }
        } catch (Exception e) {
            Log.e("TAGTAG", e.toString());
        }
        return true;
    }

    /**
     * @param event
     */
    private void obtainVelocityTracker(MotionEvent event) {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 释放
     */
    private void releaseVelocityTracker() {

        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;

        }

    }

    /**
     * 关闭
     */
    private void closeConvertView() {
        if (mAnimator != null) {
            if (mAnimator.isRunning()) {
                mAnimatorIsCancel = true;
                mAnimator.removeAllListeners();
                mAnimator.cancel();
            }
            mAnimator = null;
        }
        //使用开源动画库nineoldandroids来兼容api11之前的版本
        mAnimator = ValueAnimator.ofFloat(this.getRotation(), -mClosedDegree);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (!isTouchMove && !mAnimatorIsCancel) {
                    Number number = (Number) valueAnimator.getAnimatedValue();
                    RotateLayout.this.setRotation(number.floatValue());
                    //绘画遮罩层
                    drawMask();
                }
            }
        });
        mAnimator.setDuration(800);

        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mRotateListener != null)
                    mRotateListener.close();
            }
        });


        mAnimator.setInterpolator(sInterpolator);
        mAnimator.start();
        mAnimatorIsCancel = false;
    }


    /**
     * 还原
     */
    public void resetView() {
        if (mAnimator != null) {
            if (mAnimator.isRunning()) {
                mAnimatorIsCancel = true;
                mAnimator.removeAllListeners();
                mAnimator.cancel();
            }
            mAnimator = null;
        }
        //使用开源动画库nineoldandroids来兼容api11之前的版本
        mAnimator = ValueAnimator.ofFloat(this.getRotation(), 0f);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (!isTouchMove && !mAnimatorIsCancel) {
                    Number number = (Number) valueAnimator.getAnimatedValue();
                    RotateLayout.this.setRotation(number.floatValue());
                    //绘画遮罩层
                    drawMask();
                }

            }
        });
        mAnimator.setDuration(800);
        mAnimator.setInterpolator(sInterpolator);
        mAnimator.start();
        mAnimatorIsCancel = false;
    }

    private Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    public void finish() {
        {
            if (mAnimator != null) {

                if (mAnimator.isRunning()) {
                    mAnimatorIsCancel = true;

                    mAnimator.removeAllListeners();
                    mAnimator.cancel();
                }
                mAnimator = null;
            }
            //使用开源动画库nineoldandroids来兼容api11之前的版本
            mAnimator = ValueAnimator.ofFloat(0, mClosedDegree);

            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    if (!isTouchMove && !mAnimatorIsCancel) {
                        Number number = (Number) valueAnimator.getAnimatedValue();
                        RotateLayout.this.setRotation(number.floatValue());
                        //绘画遮罩层
                        drawMask();
                    }

                }
            });
            mAnimator.setDuration(800);

            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    if (mRotateListener != null)
                        mRotateListener.close();

                }
            });
            mAnimator.setInterpolator(sInterpolator);
            mAnimator.start();
            mAnimatorIsCancel = false;
        }
    }

    /**
     * 关闭
     */
    public void closeView() {
        if (mAnimator != null) {
            if (mAnimator.isRunning()) {
                mAnimatorIsCancel = true;
                mAnimator.removeAllListeners();
                mAnimator.cancel();
            }
            mAnimator = null;
        }
        //使用开源动画库nineoldandroids来兼容api11之前的版本
        mAnimator = ValueAnimator.ofFloat(this.getRotation(), mClosedDegree);

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (!isTouchMove && !mAnimatorIsCancel) {
                    Number number = (Number) valueAnimator.getAnimatedValue();
                    RotateLayout.this.setRotation(number.floatValue());
                    //绘画遮罩层
                    drawMask();
                }

            }
        });
        mAnimator.setDuration(800);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if (mRotateListener != null)
                    mRotateListener.close();

            }
        });
        mAnimator.setInterpolator(sInterpolator);
        mAnimator.start();
        mAnimatorIsCancel = false;
    }

    public void setBackgroundView(View mBackgroundView) {
        this.mBackgroundView = mBackgroundView;
        //绘画遮罩层
        drawMask();

    }

    /**
     * 绘画遮罩层
     */
    private void drawMask() {
        if (mBackgroundView != null) {
            float rotation = this.getRotation();
            float percent = Math.abs(rotation) * 1.0f / mCloseDegree;
            int alpha = 200 - (int) (200 * percent);
            mBackgroundView.setBackgroundColor(Color.argb(Math.max(alpha, 0), 0, 0, 0));
        }
    }

    public static abstract class RotateListener {
        public abstract void close();

        public void onClick() {

        }

        ;
    }

    public void setmRotateListener(RotateListener mRotateListener) {
        this.mRotateListener = mRotateListener;
    }

    public void setVerticalScrollView(View verticalScrollView) {
        mVerticalScrollView = verticalScrollView;
    }

    public void setIgnoreView(View mIgnoreView) {
        this.mIgnoreView = mIgnoreView;
    }
}
