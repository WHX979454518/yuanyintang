package com.mxkj.yuanyintang.utils.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.mxkj.yuanyintang.utils.uiutils.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by LiuJie on 2017/10/25.
 */

public class ImageUtils {
    //保存文件到指定路径
    public static File saveImageToGallery(Context context, Bitmap bmp) {

        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "yytPicture";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Toast.create(context).show("保存成功    " + storePath);
            if (isSuccess) {
                return file;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化处理图片参数
     * @author   fenghao
     * @param url  url地址
     * @param params  参数  a=1&b=2
     * @return String
     */
    public static String ImageFormat(String url,String params) {
        String result="";
        if(url.isEmpty()){
            return  "";
        }
        if(url.indexOf("?")>-1){
            url="&"+params;
        }else {
            url="?"+params;
        }
        return  url;
    }
}
