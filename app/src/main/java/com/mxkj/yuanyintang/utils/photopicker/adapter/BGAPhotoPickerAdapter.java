package com.mxkj.yuanyintang.utils.photopicker.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.photopicker.imageloader.BGAImage;
import com.mxkj.yuanyintang.utils.photopicker.model.BGAImageFolderModel;
import com.mxkj.yuanyintang.utils.photopicker.util.BGAPhotoPickerUtil;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

public class BGAPhotoPickerAdapter extends BGARecyclerViewAdapter<String> {
    @Nullable
    private ArrayList<String> mSelectedImages = new ArrayList<>();
    private int mImageSize;
    private boolean mTakePhotoEnabled;

    public BGAPhotoPickerAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.bga_pp_item_photo_picker);
        mImageSize = BGAPhotoPickerUtil.getScreenWidth() / 6;
    }

    @Override
    public int getItemViewType(int position) {
        if (mTakePhotoEnabled && position == 0) {
            return R.layout.bga_pp_item_photo_camera;
        } else {
            return R.layout.bga_pp_item_photo_picker;
        }
    }

    @Override
    public void setItemChildListener(@NonNull BGAViewHolderHelper helper, int viewType) {
        if (viewType == R.layout.bga_pp_item_photo_camera) {
            helper.setItemChildClickListener(R.id.iv_item_photo_camera_camera);
        } else {
            helper.setItemChildClickListener(R.id.iv_item_photo_picker_photo);
        }
    }

    @Override
    protected void fillData(@NonNull BGAViewHolderHelper helper, int position, String model) {
        if (getItemViewType(position) == R.layout.bga_pp_item_photo_picker) {
            BGAImage.display(helper.getImageView(R.id.iv_item_photo_picker_photo), R.mipmap.bga_pp_ic_holder_dark, model, mImageSize);
            if (mSelectedImages.contains(model)) {
                helper.setVisibility(R.id.iv_selected, View.VISIBLE);
            } else {
                helper.setVisibility(R.id.iv_selected, View.GONE);
                helper.setText(R.id.tv_item_photo_picker_flag, "");
                helper.getImageView(R.id.iv_item_photo_picker_photo).setColorFilter(null);
            }
            for (int i = 0; i < mSelectedImages.size(); i++) {
                if (mSelectedImages.get(i).equals(model)) {
                    helper.setText(R.id.tv_item_photo_picker_flag, (i + 1) + "");
                }
            }
        }
    }

    public void setSelectedImages(@Nullable ArrayList<String> selectedImages) {
        if (selectedImages != null) {
            mSelectedImages = selectedImages;
        }
        notifyDataSetChanged();
    }

    @Nullable
    public ArrayList<String> getSelectedImages() {
        return mSelectedImages;
    }

    public int getSelectedCount() {
        return mSelectedImages.size();
    }

    public void setImageFolderModel(@NonNull BGAImageFolderModel imageFolderModel) {
        mTakePhotoEnabled = imageFolderModel.isTakePhotoEnabled();
        setData(imageFolderModel.getImages());
    }
}
