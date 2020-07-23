package com.mxkj.yuanyintang.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.musicplayer.activity.PlayerActivity;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.utils.uiutils.Toast;

import static com.mxkj.yuanyintang.musicplayer.service.MediaService.ACTION_NEXT;
import static com.mxkj.yuanyintang.musicplayer.service.MediaService.ACTION_PAUSE;
import static com.mxkj.yuanyintang.musicplayer.service.MediaService.ACTION_PRE;

/**
 * Created by LiuJie on 2017/10/14.
 */

public class VDHLinearLayout extends RelativeLayout {
    ViewDragHelper dragHelper;
    float startX, startY, endX, endY;

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getEndX() {
        return endX;
    }

    public float getEndY() {
        return endY;
    }

    public VDHLinearLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        dragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
//                return child == dragView || child == autoBackView;
                return child == autoBackView;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            // 当前被捕获的View释放之后回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == autoBackView) {
                    dragHelper.settleCapturedViewAt(autoBackViewOriginLeft, autoBackViewOriginTop);
                    if (Math.abs(endX - startX) - Math.abs(endY - startY) > 20) {//水平滑动
                        if (endX - startX > 60) {
                            context.sendBroadcast(new Intent(ACTION_NEXT));
                            Toast.create(MainApplication.Companion.getContext()).show("下一首");
                        } else if (endX - startX < -60) {
                            context.sendBroadcast(new Intent(ACTION_PRE));
                            Toast.create(MainApplication.Companion.getContext()).show("上一首");
                        }
                    } else if (Math.abs(endY - startY) - (Math.abs(endX - startX)) > 20) {//竖直滑动
                        if (endY - startY > 60) {
                            if (MediaService.getMediaPlayer().isPlaying()) {
                                context.sendBroadcast(new Intent(ACTION_PAUSE));
                                Toast.create(MainApplication.Companion.getContext()).show("暂停");
                            }
                        } else if (endY - startY < -60) {
                            context.startActivity(new Intent(context, PlayerActivity.class));
                        }
                    } else {
                        context.startActivity(new Intent(context, PlayerActivity.class));
                    }
                    invalidate();
                }
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            }
        });
        // 设置左边缘可以被Drag
        dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = event.getX();
                endY = event.getY();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    //    View dragView;
//    View edgeDragView;
    View autoBackView;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        autoBackView = findViewById(R.id.autoBackView);
    }

    int autoBackViewOriginLeft;
    int autoBackViewOriginTop;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        autoBackViewOriginLeft = autoBackView.getLeft();
        autoBackViewOriginTop = autoBackView.getTop();
    }
}

