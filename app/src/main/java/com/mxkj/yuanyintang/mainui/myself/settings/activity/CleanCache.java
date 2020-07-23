package com.mxkj.yuanyintang.mainui.myself.settings.activity;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.musicplayer.playcache.StorageUtils;
import com.mxkj.yuanyintang.mainui.myself.settings.utils.GlideCacheUtil;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CleanCache extends StandardUiActivity {
    @BindView(R.id.tv_picDataSize)
    TextView tvPicDataSize;
    @BindView(R.id.rl_cleanPic)
    RelativeLayout rlCleanPic;
    @BindView(R.id.tv_musicSize)
    TextView tvMusicSize;
    @BindView(R.id.rl_cleanMusic)
    RelativeLayout rlCleanMusic;
    @BindView(R.id.headerViewGroup)
    FrameLayout headerViewGroup;
    private GlideCacheUtil glideCacheUtil;

    @Override
    public int setLayoutId() {
        return R.layout.activity_clean_cache;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("清理缓存");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        glideCacheUtil = GlideCacheUtil.getInstance();
        setCachrSize();
    }

    private void setCachrSize() {
        tvPicDataSize.setText(glideCacheUtil.getCacheSize(this));
        File cacheDirectory = StorageUtils.getCacheDirectory(this, true);
        File cacheExDirectory = StorageUtils.getExternalCacheDir(this);
        try {
            long cache = glideCacheUtil.getFolderSize(cacheDirectory);
            long cacheExDir = glideCacheUtil.getFolderSize(cacheExDirectory);
            String formatSize = GlideCacheUtil.getFormatSize(cache + cacheExDir);
            tvMusicSize.setText(formatSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.rl_cleanPic, R.id.rl_cleanMusic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_cleanPic:
                final MaterialDialog dialog = new MaterialDialog(this);
                dialog.content(
                        "清除图片文字缓存？")//
                        .btnText("取消", "确定")//
                        .titleTextSize(16)
                        .titleTextColor(ContextCompat.getColor(this, R.color.color_14_text))
                        .contentTextColor(ContextCompat.getColor(this, R.color.color_66_text))
                        .contentTextSize(14)
                        .btnTextSize(14)
                        .btnTextColor(ContextCompat.getColor(this, R.color.base_red)
                                , ContextCompat.getColor(this, R.color.base_red))
                        .showAnim(null)//
                        .dismissAnim(null)//
                        .show();

                dialog.setOnBtnClickL(
                        new OnBtnClickL() {//left btn click listener
                            @Override
                            public void onBtnClick() {
                                dialog.dismiss();
                            }
                        },
                        new OnBtnClickL() {//right btn click listener
                            @Override
                            public void onBtnClick() {
                                glideCacheUtil.clearImageAllCache(CleanCache.this);
                                CacheUtils.INSTANCE.clearSpData(CleanCache.this);
                                tvPicDataSize.setText("0.0");
                                setSnackBar("清理完成", "", R.drawable.icon_success);
                                dialog.dismiss();
                            }
                        }
                );

                break;
            case R.id.rl_cleanMusic:
                final MaterialDialog dialog2 = new MaterialDialog(this);
                dialog2.content(
                        "清除歌曲缓存？")//
                        .btnText("取消", "确定")//
                        .titleTextSize(16)
                        .titleTextColor(ContextCompat.getColor(this, R.color.color_14_text))
                        .contentTextColor(ContextCompat.getColor(this, R.color.color_66_text))
                        .contentTextSize(14)
                        .btnTextSize(14)
                        .btnTextColor(ContextCompat.getColor(this, R.color.base_red)
                                , ContextCompat.getColor(this, R.color.base_red))
                        .showAnim(null)//
                        .dismissAnim(null)//
                        .show();

                dialog2.setOnBtnClickL(
                        new OnBtnClickL() {//left btn click listener
                            @Override
                            public void onBtnClick() {
                                dialog2.dismiss();
                            }
                        },
                        new OnBtnClickL() {//right btn click listener
                            @Override
                            public void onBtnClick() {
                                dialog2.dismiss();
                                showLoadingView();
                                File cacheDirectory = StorageUtils.getCacheDirectory(CleanCache.this, true);
                                File cacheExDirectory = StorageUtils.getExternalCacheDir(CleanCache.this);
                                glideCacheUtil.deleteFolderFile(cacheDirectory.getPath(), false);
                                glideCacheUtil.deleteFolderFile(cacheExDirectory.getPath(), false);
                                tvMusicSize.setText("0.0");
                                hideLoadingView();
                                setSnackBar("清理完成", "", R.drawable.icon_success);

                            }
                        }
                );
                break;
        }
    }

    @OnClick(R.id.leftButton)
    public void onViewClicked() {
        finish();
    }


}
