package com.mxkj.yuanyintang.utils.image.imageloader.loader;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.MemoryCategory;
import com.mxkj.yuanyintang.utils.image.imageloader.config.SingleConfig;
import com.mxkj.yuanyintang.utils.image.imageloader.utils.DownLoadImageService;


/**
 * Created by doudou on 2017/4/10.
 */

public interface ILoader {

    void init(Context context, int cacheSizeInM, MemoryCategory memoryCategory, boolean isInternalCD);

    void request(SingleConfig config);

    void pause();

    void resume();

    void clearDiskCache();

    void clearMomoryCache(View view);

    void clearMomory();

    boolean  isCached(String url);

    void trimMemory(int level);

    void clearAllMemoryCaches();

    void saveImageIntoGallery(DownLoadImageService downLoadImageService);
}
