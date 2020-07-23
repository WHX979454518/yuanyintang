package com.mxkj.yuanyintang.utils.photopicker.adapters;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;

import com.mxkj.yuanyintang.utils.photopicker.widget.BGASortableNinePhotoLayout;

import java.util.ArrayList;
public class BGASortableNinePhotoLayoutAdapter {

    @BindingAdapter({"bga_snpl_delegate"})
    public static void setDelegate(@NonNull BGASortableNinePhotoLayout sortableNinePhotoLayout, BGASortableNinePhotoLayout.Delegate delegate) {
        sortableNinePhotoLayout.setDelegate(delegate);
    }

    @BindingAdapter({"bga_snpl_data"})
    public static void setData(@NonNull BGASortableNinePhotoLayout sortableNinePhotoLayout, ArrayList<String> data) {
        sortableNinePhotoLayout.setData(data);
    }

    @BindingAdapter({"bga_snpl_editable"})
    public static void setData(@NonNull BGASortableNinePhotoLayout sortableNinePhotoLayout, boolean editable) {
        sortableNinePhotoLayout.setEditable(editable);
    }
}
