package com.mxkj.yuanyintang.utils.image;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mxkj.yuanyintang.utils.luban.Luban;
import com.mxkj.yuanyintang.utils.luban.OnCompressListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwj on 2017/9/26.
 */

/**
 * 图片压缩工具，传入图片路径的集合，返回压缩后的路径集合
 * 压缩算法使用鲁班  compile 'top.zibin:Luban:1.0.9'
 */
public class IMGCompressUtils {
    public static int count;
    @NonNull
    public static List<File> files = new ArrayList<>();

    @NonNull
    public static List<File> CompressorImage1(int num, final Context context, @NonNull final List<String> path, @NonNull final CompressCallback callback) throws IOException {
        if (num == 0) {
            files.clear();
            count = num;
        }
        File actualImage = new File(path.get(count));
        /**
         * 获取文件header（判断是不是gif，gif不做压缩）
         * */
        String fileType = FileType.getFileType(path.get(count));
        if (fileType != null && fileType.equals("gif")) {
            files.add(actualImage);
            count++;
            if (files.size() == path.size()) {
                callback.fileCallback(files);
                count = 0;
                files.clear();
            } else {
                try {
                    CompressorImage1(count, context, path, callback);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            Luban.with(context)
                    .load(actualImage)                     //传人要压缩的图片
                    .putGear(3)
                    .setCompressListener(new OnCompressListener() { //设置回调
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(@NonNull File file) {
                            files.add(file);
                            count++;
                            if (files.size() == path.size()) {
                                callback.fileCallback(files);
                                count = 0;
                                files.clear();
                            } else {
                                try {
                                    CompressorImage1(count, context, path, callback);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    }).launch();    //启动压缩
        }
        return files;
    }

    public interface CompressCallback {
        void fileCallback(List<File> files);
    }
}
