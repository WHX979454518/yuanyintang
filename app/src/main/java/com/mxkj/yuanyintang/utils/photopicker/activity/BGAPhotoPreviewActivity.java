/**
 * Copyright 2016 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mxkj.yuanyintang.utils.photopicker.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.photopicker.util.BGAAsyncTask;
import com.mxkj.yuanyintang.utils.photopicker.util.BGASavePhotoTask;

import java.io.File;
import java.util.ArrayList;

import cn.bingoogolapple.androidcommon.adapter.BGAOnNoDoubleClickListener;

import com.mxkj.yuanyintang.utils.photopicker.adapter.BGAPhotoPageAdapter;
import com.mxkj.yuanyintang.utils.photopicker.imageloader.BGAImage;
import com.mxkj.yuanyintang.utils.photopicker.imageloader.BGAImageLoader;
import com.mxkj.yuanyintang.utils.photopicker.util.BGAPhotoPickerUtil;
import com.mxkj.yuanyintang.utils.photopicker.widget.BGAHackyViewPager;
import com.mxkj.yuanyintang.utils.photopicker.photoview.PhotoViewAttacher;

public class BGAPhotoPreviewActivity extends BGAPPToolbarActivity implements PhotoViewAttacher.OnViewTapListener, BGAAsyncTask.Callback<Void> {
    private static final String EXTRA_SAVE_IMG_DIR = "EXTRA_SAVE_IMG_DIR";
    private static final String EXTRA_PREVIEW_IMAGES = "EXTRA_PREVIEW_IMAGES";
    private static final String EXTRA_CURRENT_POSITION = "EXTRA_CURRENT_POSITION";
    private static final String EXTRA_IS_SINGLE_PREVIEW = "EXTRA_IS_SINGLE_PREVIEW";
    private static final String EXTRA_PHOTO_PATH = "EXTRA_PHOTO_PATH";

    private TextView mTitleTv;
    private ImageView mDownloadIv;
    private BGAHackyViewPager mContentHvp;
    private BGAPhotoPageAdapter mPhotoPageAdapter;

    private boolean mIsSinglePreview;

    private File mSaveImgDir;

    private boolean mIsHidden = false;
    @Nullable
    private BGASavePhotoTask mSavePhotoTask;

    /**
     * 上一次标题栏显示或隐藏的时间戳
     */
    private long mLastShowHiddenTime;

    /**
     * 获取查看多张图片的intent
     *
     * @param context
     * @param saveImgDir      保存图片的目录，如果传null，则没有保存图片功能
     * @param previewImages   当前预览的图片目录里的图片路径集合
     * @param currentPosition 当前预览图片的位置
     * @return
     */
    @NonNull
    public static Intent newIntent(Context context, File saveImgDir, ArrayList<String> previewImages, int currentPosition) {
        Intent intent = new Intent(context, BGAPhotoPreviewActivity.class);
        intent.putExtra(EXTRA_SAVE_IMG_DIR, saveImgDir);
        intent.putStringArrayListExtra(EXTRA_PREVIEW_IMAGES, previewImages);
        intent.putExtra(EXTRA_CURRENT_POSITION, currentPosition);
        intent.putExtra(EXTRA_IS_SINGLE_PREVIEW, false);
        return intent;
    }

    /**
     * 获取查看单张图片的intent
     *
     * @param context
     * @param saveImgDir 保存图片的目录，如果传null，则没有保存图片功能
     * @param photoPath  图片路径
     * @return
     */
    @NonNull
    public static Intent newIntent(Context context, File saveImgDir, String photoPath) {
        Intent intent = new Intent(context, BGAPhotoPreviewActivity.class);
        intent.putExtra(EXTRA_SAVE_IMG_DIR, saveImgDir);
        intent.putExtra(EXTRA_PHOTO_PATH, photoPath);
        intent.putExtra(EXTRA_CURRENT_POSITION, 0);
        intent.putExtra(EXTRA_IS_SINGLE_PREVIEW, true);
        return intent;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setNoLinearContentView(R.layout.bga_pp_activity_photo_preview);
        mContentHvp = getViewById(R.id.hvp_photo_preview_content);
    }

    @Override
    protected void setListener() {
        mContentHvp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                renderTitleTv();
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mSaveImgDir = (File) getIntent().getSerializableExtra(EXTRA_SAVE_IMG_DIR);
        if (mSaveImgDir != null && !mSaveImgDir.exists()) {
            mSaveImgDir.mkdirs();
        }

        ArrayList<String> previewImages = getIntent().getStringArrayListExtra(EXTRA_PREVIEW_IMAGES);

        mIsSinglePreview = getIntent().getBooleanExtra(EXTRA_IS_SINGLE_PREVIEW, false);
        if (mIsSinglePreview) {
            previewImages = new ArrayList<>();
            previewImages.add(getIntent().getStringExtra(EXTRA_PHOTO_PATH));
        }

        int currentPosition = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);

        mPhotoPageAdapter = new BGAPhotoPageAdapter(this, this, previewImages);
        mContentHvp.setAdapter(mPhotoPageAdapter);
        mContentHvp.setCurrentItem(currentPosition);

        // 过2秒隐藏标题栏
        mToolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                hiddenTitleBar();
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.bga_pp_menu_photo_preview, menu);
        MenuItem menuItem = menu.findItem(R.id.item_photo_preview_title);
        View actionView = menuItem.getActionView();
        mTitleTv = (TextView) actionView.findViewById(R.id.tv_photo_preview_title);
        mDownloadIv = (ImageView) actionView.findViewById(R.id.iv_photo_preview_download);
        mDownloadIv.setOnClickListener(new BGAOnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mSavePhotoTask == null) {
                    savePic();
                }
            }
        });

        if (mSaveImgDir == null) {
            mDownloadIv.setVisibility(View.INVISIBLE);
        }

        renderTitleTv();

        return true;
    }

    private void renderTitleTv() {
        if (mTitleTv == null || mPhotoPageAdapter == null) {
            return;
        }

        if (mIsSinglePreview) {
            mTitleTv.setText(R.string.bga_pp_view_photo);
        } else {
            mTitleTv.setText((mContentHvp.getCurrentItem() + 1) + "/" + mPhotoPageAdapter.getCount());
        }
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        if (System.currentTimeMillis() - mLastShowHiddenTime > 500) {
            mLastShowHiddenTime = System.currentTimeMillis();
            if (mIsHidden) {
                //需求：单击图片退出预览
//                finish();
                showTitleBar();
            } else {
//                finish();
                hiddenTitleBar();
            }
        }
    }

    private void showTitleBar() {
        if (mToolbar != null) {
            ViewCompat.animate(mToolbar).translationY(0).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = false;
                }
            }).start();
        }
    }

    private void hiddenTitleBar() {
        if (mToolbar != null) {
            ViewCompat.animate(mToolbar).translationY(-mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = true;
                }
            }).start();
        }
    }

    private synchronized void savePic() {
        if (mSavePhotoTask != null) {
            return;
        }
//TODO
        final String url = mPhotoPageAdapter.getItem(mContentHvp.getCurrentItem()) + "/0/0/1/60";
        File file;
        String fileType = ".png";

        if (url.startsWith("file")) {
            file = new File(url.replace("file://", ""));
            if (file.exists()) {
                String mimeType = MediaFile.getMimeType(file);
                int fileTypeForMimeType = MediaFile.getFileTypeForMimeType(mimeType);
                switch (fileTypeForMimeType) {
                    case MediaFile.FILE_TYPE_JPEG:
                        fileType = ".jpeg";
                        break;
                    case MediaFile.FILE_TYPE_GIF:
                        fileType = ".gif";
                        break;
                    case MediaFile.FILE_TYPE_PNG:
                        fileType = ".png";
                        break;
                    case MediaFile.FILE_TYPE_BMP:
                        fileType = ".bmp";
                        break;
                    case MediaFile.FILE_TYPE_WBMP:
                        fileType = ".wbmp";
                        break;
                }


                BGAPhotoPickerUtil.showSafe(getString(R.string.bga_pp_save_img_success_folder, file.getParentFile().getAbsolutePath()));
                return;
            }
        }

        // 通过MD5加密url生成文件名，避免多次保存同一张图片
        file = new File(mSaveImgDir, BGAPhotoPickerUtil.md5(url) + fileType);
        if (file.exists()) {
            BGAPhotoPickerUtil.showSafe(getString(R.string.bga_pp_save_img_success_folder, mSaveImgDir.getAbsolutePath()));
            return;
        }

        mSavePhotoTask = new BGASavePhotoTask(this, this, file);


        BGAImage.download(url, new BGAImageLoader.DownloadDelegate() {
            @Override
            public void onSuccess(String url, Bitmap bitmap) {
                mSavePhotoTask.setBitmapAndPerform(bitmap);
            }

            @Override
            public void onFailed(String url) {
                mSavePhotoTask = null;
                BGAPhotoPickerUtil.show(R.string.bga_pp_save_img_failure);
            }
        });
    }

    @Override
    public void onPostExecute(Void aVoid) {
        mSavePhotoTask = null;
    }

    @Override
    public void onTaskCancelled() {
        mSavePhotoTask = null;
    }

    @Override
    protected void onDestroy() {
        if (mSavePhotoTask != null) {
            mSavePhotoTask.cancelTask();
            mSavePhotoTask = null;
        }
        super.onDestroy();
    }
}