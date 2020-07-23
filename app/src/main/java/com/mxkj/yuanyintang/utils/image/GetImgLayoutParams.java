package com.mxkj.yuanyintang.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by zuixia on 2018/3/21.
 */

public class GetImgLayoutParams {
    public static ViewGroup.LayoutParams getLayoutParams(Context context,Bitmap bitmap, ImageView imageView) {
        WindowManager m = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        m.getDefaultDisplay().getMetrics(outMetrics);
        int screenWidth = outMetrics.widthPixels;

        float rawWidth = bitmap.getWidth();
        float rawHeight = bitmap.getHeight();
        float width = 0;
        float height = 0;
        Log.i("hello", "原始图片高度：" + rawHeight + "原始图片宽度：" + rawWidth);
        Log.i("hello", "原始高宽比：" + (rawHeight / rawWidth));
        if (rawWidth > screenWidth) {
            Log.i("hello", "缩小");
            height = (screenWidth / rawWidth) * rawHeight;
            width = screenWidth;
        } else {
            Log.i("hello", "放大");
            width = screenWidth;
            height = rawHeight * ((screenWidth / rawWidth));
        }
        Log.i("hello", "处理后图片高度：" + height + "处理后图片宽度：" + width);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) width, (int) height);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = (int) width;
        params.height = (int) height;
        return params;
    }

}
