package com.mxkj.yuanyintang.mainui.home.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by LiuJie on 2017/9/20.
 */

public class FeedRootRecyclerView extends BetterRecyclerView {
    public FeedRootRecyclerView(Context context) {
        this(context, null);
    }

    public FeedRootRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeedRootRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    /* do nothing */
    }
}