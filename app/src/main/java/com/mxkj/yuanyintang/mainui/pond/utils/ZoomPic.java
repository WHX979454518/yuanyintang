package com.mxkj.yuanyintang.mainui.pond.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by LiuJie on 2017/11/7.
 */

public class ZoomPic {
    public static ImageView zoomPic(Context context, ImageView mImageView) {
        mImageView.setAdjustViewBounds(true);
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        m.getDefaultDisplay().getMetrics(outMetrics);
        ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
        float width = outMetrics.widthPixels;
        if (width < layoutParams.width) {
            float v = width / layoutParams.width;
            layoutParams.width = (int) width;
            layoutParams.height = (int) (layoutParams.height * v);
            mImageView.setLayoutParams(layoutParams);
        }
        return mImageView;
    }
}
