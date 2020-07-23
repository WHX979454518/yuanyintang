package com.mxkj.yuanyintang.utils.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by zuixia on 2018/3/21.
 */

public class GetBitmapFromFile {
    public static Bitmap getBitmap(File file) {
        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
        bfOptions.inDither = false;
        bfOptions.inPurgeable = true;
        bfOptions.inTempStorage = new byte[1024 * 1024];
        bfOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        bfOptions.inInputShareable = true;
//        如果inJustDecoedBounds设置为true的话，解码bitmap时可以只返回其高、宽和Mime类型，而不必为其申请内存
//        bfOptions.inJustDecodeBounds = true;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
            Bitmap bmp = null;
            if (fs != null)
                try {
                    bmp = BitmapFactory.decodeStream(fs, null, bfOptions);
                    return bmp;
                } finally {
                    if (fs != null) {
                        try {
                            fs.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
