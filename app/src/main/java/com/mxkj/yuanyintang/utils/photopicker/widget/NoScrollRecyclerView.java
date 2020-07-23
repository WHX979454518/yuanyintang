package com.mxkj.yuanyintang.utils.photopicker.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * company：MXKJ
 * Created by ... on 2017/2/9.
 */

public class NoScrollRecyclerView extends RecyclerView {
    public NoScrollRecyclerView(Context context) {
        super(context);
        }

    public NoScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
