package com.mxkj.yuanyintang.utils.photopicker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.photopicker.adapter.BGAPhotoPageAdapter;
import com.mxkj.yuanyintang.utils.photopicker.photoview.PhotoViewAttacher;
import com.mxkj.yuanyintang.utils.photopicker.util.BGAPhotoPickerUtil;
import com.mxkj.yuanyintang.utils.photopicker.widget.BGAHackyViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BGAPhotoPickerPreviewActivity extends AppCompatActivity implements PhotoViewAttacher.OnViewTapListener {
    private static final String EXTRA_PREVIEW_IMAGES = "EXTRA_PREVIEW_IMAGES";
    private static final String EXTRA_SELECTED_IMAGES = "EXTRA_SELECTED_IMAGES";
    private static final String EXTRA_MAX_CHOOSE_COUNT = "EXTRA_MAX_CHOOSE_COUNT";
    private static final String EXTRA_CURRENT_POSITION = "EXTRA_CURRENT_POSITION";
    private static final String EXTRA_IS_FROM_TAKE_PHOTO = "EXTRA_IS_FROM_TAKE_PHOTO";
    @BindView(R.id.hvp_photo_picker_preview_content)
    BGAHackyViewPager hvpPhotoPickerPreviewContent;
    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.tv_photo_picker_preview_choose)
    TextView tvPhotoPickerPreviewChoose;
    @BindView(R.id.rl_photo_picker_preview_choose)
    RelativeLayout rlPhotoPickerPreviewChoose;
    @BindView(R.id.bt_sure)
    TextView btSure;
    private ArrayList<String> mSelectedImages;
    private BGAPhotoPageAdapter mPhotoPageAdapter;
    private int mMaxChooseCount = 1;
    private static int totalChooseCount = 1;
    /**
     * 右上角按钮文本
     */
    private String mTopRightBtnText;

    private boolean mIsHidden = false;
    /**
     * 上一次标题栏显示或隐藏的时间戳
     */
    private long mLastShowHiddenTime;
    /**
     * 是否是拍完照后跳转过来
     */
    private boolean mIsFromTakePhoto;

    /**
     * @param context         应用程序上下文
     * @param maxChooseCount  图片选择张数的最大值
     * @param selectedImages  当前已选中的图片路径集合，可以传null
     * @param previewImages   当前预览的图片目录里的图片路径集合
     * @param currentPosition 当前预览图片的位置
     * @param isFromTakePhoto 是否是拍完照后跳转过来
     * @return
     */
    @NonNull
    public static Intent newIntent(Context context, int maxChooseCount, ArrayList<String> selectedImages, ArrayList<String> previewImages, int currentPosition, boolean isFromTakePhoto) {
        Log.e("TAG", "newIntent: selectedImages==" + selectedImages.size() + "previewImages==" + previewImages.size() + "maxChooseCount" + maxChooseCount);
        Intent intent = new Intent(context, BGAPhotoPickerPreviewActivity.class);
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedImages);
        intent.putStringArrayListExtra(EXTRA_PREVIEW_IMAGES, previewImages);
        intent.putExtra(EXTRA_MAX_CHOOSE_COUNT, maxChooseCount);
        intent.putExtra(EXTRA_CURRENT_POSITION, currentPosition);
        intent.putExtra(EXTRA_IS_FROM_TAKE_PHOTO, isFromTakePhoto);
        return intent;
    }

    /**
     * 获取已选择的图片集合
     *
     * @param intent
     * @return
     */
    public static ArrayList<String> getSelectedImages(@NonNull Intent intent) {
        if (intent != null) {
            return intent.getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
        }
        return null;
    }

    /**
     * 是否是拍照预览
     *
     * @param intent
     * @return
     */
    public static boolean getIsFromTakePhoto(@NonNull Intent intent) {
        if (intent != null) {
            return intent.getBooleanExtra(EXTRA_IS_FROM_TAKE_PHOTO, false);
        }
        return false;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
        setListener();
        processLogic(savedInstanceState);
    }

    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.bga_pp_activity_photo_picker_preview);
        ButterKnife.bind(this);
    }

    protected void setListener() {
        hvpPhotoPickerPreviewContent.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                handlePageSelectedStatus();
            }
        });
    }


    protected void processLogic(Bundle savedInstanceState) {
        // 获取图片选择的最大张数
        mMaxChooseCount = getIntent().getIntExtra(EXTRA_MAX_CHOOSE_COUNT, 1);
        if (mMaxChooseCount < 1) {
            mMaxChooseCount = 1;
        }

        mSelectedImages = getIntent().getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
        ArrayList<String> previewImages = getIntent().getStringArrayListExtra(EXTRA_PREVIEW_IMAGES);
        if (previewImages != null && previewImages.size() > 0) {
            if (TextUtils.isEmpty(previewImages.get(0))) {
                // 从BGAPhotoPickerActivity跳转过来时，如果有开启拍照功能，则第0项为""
                previewImages.remove(0);
            }
        }
        // 处理是否是拍完照后跳转过来
        mIsFromTakePhoto = getIntent().getBooleanExtra(EXTRA_IS_FROM_TAKE_PHOTO, false);
        if (mIsFromTakePhoto) {
            // 如果是拍完照后跳转过来，一直隐藏底部选择栏
            rlPhotoPickerPreviewChoose.setVisibility(View.INVISIBLE);
        }
        int currentPosition = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);

        // 获取右上角按钮文本
        mTopRightBtnText = getString(R.string.bga_pp_confirm);
        mPhotoPageAdapter = new BGAPhotoPageAdapter(this, this, previewImages);
        hvpPhotoPickerPreviewContent.setAdapter(mPhotoPageAdapter);
        hvpPhotoPickerPreviewContent.setCurrentItem(currentPosition);
        handlePageSelectedStatus();
        // 过2秒隐藏标题栏和底部选择栏
        rlPhotoPickerPreviewChoose.postDelayed(new Runnable() {
            @Override
            public void run() {
                hiddenToolBarAndChooseBar();
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.bga_pp_menu_photo_picker_preview, menu);
        MenuItem menuItem = menu.findItem(R.id.item_photo_picker_preview_title);
        renderTopRightBtn();
        handlePageSelectedStatus();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, mSelectedImages);
        intent.putExtra(EXTRA_IS_FROM_TAKE_PHOTO, mIsFromTakePhoto);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private void handlePageSelectedStatus() {
        if (tvPosition == null || mPhotoPageAdapter == null) {
            return;
        }
        tvPosition.setText((hvpPhotoPickerPreviewContent.getCurrentItem() + 1) + "/" + mPhotoPageAdapter.getCount());
        if (mSelectedImages.contains(mPhotoPageAdapter.getItem(hvpPhotoPickerPreviewContent.getCurrentItem()))) {
            tvPhotoPickerPreviewChoose.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_xuantupian_true, 0, 0, 0);
        } else {
            tvPhotoPickerPreviewChoose.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_xuantupian_false, 0, 0, 0);
        }
    }

    /**
     * 渲染右上角按钮
     */
    private void renderTopRightBtn() {
        if (mIsFromTakePhoto) {
            btSure.setEnabled(true);
        } else if (mSelectedImages.size() == 0) {
            btSure.setEnabled(false);
        } else {
            btSure.setEnabled(true);
            if (hvpPhotoPickerPreviewContent != null && mPhotoPageAdapter != null) {
                tvPosition.setText((hvpPhotoPickerPreviewContent.getCurrentItem() + 1) + "/" + mPhotoPageAdapter.getCount());
            }
        }
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        if (System.currentTimeMillis() - mLastShowHiddenTime > 500) {
            mLastShowHiddenTime = System.currentTimeMillis();
            if (mIsHidden) {
                showTitleBarAndChooseBar();
            } else {
                hiddenToolBarAndChooseBar();
            }
        }
    }

    private void showTitleBarAndChooseBar() {
        if (rlPhotoPickerPreviewChoose != null) {
            ViewCompat.animate(rlPhotoPickerPreviewChoose).translationY(0).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = false;
                }
            }).start();
        }

        if (!mIsFromTakePhoto && rlPhotoPickerPreviewChoose != null) {
            rlPhotoPickerPreviewChoose.setVisibility(View.VISIBLE);
            ViewCompat.setAlpha(rlPhotoPickerPreviewChoose, 0);
            ViewCompat.animate(rlPhotoPickerPreviewChoose).alpha(1).setInterpolator(new DecelerateInterpolator(2)).start();
        }
    }

    private void hiddenToolBarAndChooseBar() {
        if (rlPhotoPickerPreviewChoose != null) {
            ViewCompat.animate(rlPhotoPickerPreviewChoose).translationY(-rlPhotoPickerPreviewChoose.getHeight()).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = true;
                    if (rlPhotoPickerPreviewChoose != null) {
                        rlPhotoPickerPreviewChoose.setVisibility(View.INVISIBLE);
                    }
                }
            }).start();
        }

        if (!mIsFromTakePhoto) {
            if (rlPhotoPickerPreviewChoose != null) {
                ViewCompat.animate(rlPhotoPickerPreviewChoose).alpha(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }
        }
    }

    @OnClick({R.id.finish, R.id.tv_photo_picker_preview_choose, R.id.bt_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.tv_photo_picker_preview_choose:
                String currentImage = mPhotoPageAdapter.getItem(hvpPhotoPickerPreviewContent.getCurrentItem());
                if (mSelectedImages.contains(currentImage)) {
                    mSelectedImages.remove(currentImage);
                    tvPhotoPickerPreviewChoose.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_xuantupian_false, 0, 0, 0);
                    renderTopRightBtn();
                } else {
                    if (mMaxChooseCount == 1) {
                        // 单选

                        mSelectedImages.clear();
                        mSelectedImages.add(currentImage);
//                        tvPhotoPickerPreviewChoose.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bga_pp_ic_cb_checked, 0, 0, 0);
                        tvPhotoPickerPreviewChoose.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_xuantupian_false, 0, 0, 0);
                        renderTopRightBtn();
                    } else {
                        // 多选
                        if (mMaxChooseCount == mSelectedImages.size()) {
                            BGAPhotoPickerUtil.show(getString(R.string.bga_pp_toast_photo_picker_max, mMaxChooseCount));
                        } else {
                            mSelectedImages.add(currentImage);
                            tvPhotoPickerPreviewChoose.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_xuantupian_true, 0, 0, 0);
                            renderTopRightBtn();
                        }
                    }
                }
                break;
            case R.id.bt_sure:
                Intent intent = new Intent();
                intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, mSelectedImages);
                intent.putExtra(EXTRA_IS_FROM_TAKE_PHOTO, mIsFromTakePhoto);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}