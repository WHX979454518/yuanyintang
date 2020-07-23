package com.mxkj.yuanyintang.widget.bannerLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.mxkj.yuanyintang.widget.bannerLayout.autoscoll.AutoScrollViewPager;

/**
 * Created by LiuJie on 2016/10/7.
 */
public class ClickViewPager extends AutoScrollViewPager {
    private OnItemClickListener mOnItemClickListener;

    public ClickViewPager(Context context) {
        super(context);
        setup();
    }
    public ClickViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }
    private void setup() {
        final GestureDetector tapGestureDetector = new    GestureDetector(getContext(), new TapGestureListener());
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tapGestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getCurrentItem());
            }
            return true;
        }
    }
}
