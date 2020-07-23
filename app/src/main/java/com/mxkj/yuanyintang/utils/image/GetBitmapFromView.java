package com.mxkj.yuanyintang.utils.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.mxkj.yuanyintang.musicplayer.service.MediaService.bean;

/**
 * Created by LiuJie on 2017/11/4.
 */

public class GetBitmapFromView {
    public static Bitmap createViewBitmap(Context context, View v) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        int width1 = display.getWidth();
        int height1 = display.getHeight()-320;
        Bitmap bmp = Bitmap.createBitmap(width1, height1, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawColor(Color.WHITE);
        v.draw(canvas);
        return bmp;
    }
    public static File saveMyBitmap(String bitName, Bitmap mBitmap) {
        File f2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "yytPicture");
        if (!f2.exists()) {
            f2.mkdirs();
        }
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "yytPicture"+ File.separator+bitName + System.currentTimeMillis() + ".jpg");
        if (f.exists()){
            f.delete();
        }
        try {
            f.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
            mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }
}
