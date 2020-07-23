package com.mxkj.yuanyintang.musicplayer.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.util.Log;

public class GetColorFromBitmap {
    private static Palette.Swatch swatch;

    public static void getColor(Bitmap bitmap, final AfterGetColor afterGetColor) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                swatch = palette.getDarkVibrantSwatch();
//                swatch = palette.getLightVibrantSwatch();
                if (swatch != null) {
                    Log.e("TAG", "onGenerated: " + swatch.getRgb());
                    afterGetColor.doNext(swatch.getRgb());
                } else {
                    afterGetColor.doNext(Color.GRAY);
                }
            }
        });
    }

    public interface AfterGetColor {
        void doNext(int color);
    }
}
