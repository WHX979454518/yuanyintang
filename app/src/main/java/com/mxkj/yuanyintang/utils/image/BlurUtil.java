package com.mxkj.yuanyintang.utils.image;

/**
 * company：MXKJ
 * Created by ... on 2017/6/27.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class BlurUtil {
    //分别在x轴 和 y轴方向上进行高斯模糊
    public static Bitmap gaussBlurUseGauss(Bitmap bitmap, int radius) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        //生成一张新的图片
        Bitmap outBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //定义一个临时数组存储原始图片的像素 值
        int[] pix = new int[w * h];

        //将图片像素值写入数组
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        //进行模糊
        initCBlur1(pix, w, h, radius);

        //将数据写入到 图片
        outBitmap.setPixels(pix, 0, w, 0, 0, w, h);

        //返回结果
        return outBitmap;
    }

    //利用均值模糊 逼近 高斯模糊
    public static Bitmap gaussBlurUseAvg(Bitmap bitmap, int radius) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        //生成一张新的图片
        Bitmap outBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //定义一个临时数组存储原始图片的像素 值
        int[] pix = new int[w * h];

        //将图片像素值写入数组
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        //进行模糊
        initCBlur2(pix, w, h, radius);

        //将数据写入到 图片
        outBitmap.setPixels(pix, 0, w, 0, 0, w, h);

        //返回结果

        return outBitmap;
    }

    //原始的高斯模糊 方法
    private static native void initCBlur1(int[] pix, int w, int h, int r);

    //利用均值模糊进行拟合 高斯模糊
    private static native void initCBlur2(int[] pix, int w, int h, int r);

//    //加载native模块
//    static {
//        System.loadLibrary("blur_jni");
//    }

    public static void glideBlurImg(Context context, String url, int bitmapPool, ImageView v) {
        ImageLoader.with(context)
                .url(url)
                .scale(ScaleMode.CENTER_CROP)
                .blur(bitmapPool)
                .into(v);
    }

    public static void glideBlurImg(Context context, Uri uri, int bitmapPool, ImageView v) {
        Glide.with(context)
                .load(uri)
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(context, bitmapPool, 4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(v);
    }
}
