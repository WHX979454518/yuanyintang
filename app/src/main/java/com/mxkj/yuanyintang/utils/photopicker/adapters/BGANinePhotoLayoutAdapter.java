package com.mxkj.yuanyintang.utils.photopicker.adapters;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import com.mxkj.yuanyintang.utils.photopicker.widget.BGANinePhotoLayout;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/12/5 上午12:44
 * 描述:
 */
public class BGANinePhotoLayoutAdapter {

    @BindingAdapter({"bga_npl_delegate"})
    public static void setDelegate(@NonNull BGANinePhotoLayout ninePhotoLayout, BGANinePhotoLayout.Delegate delegate) {
        ninePhotoLayout.setDelegate(delegate);
    }

    @BindingAdapter({"bga_npl_data"})
    public static void setData(@NonNull BGANinePhotoLayout ninePhotoLayout, @NonNull ArrayList<String> data) {
        ninePhotoLayout.setData(data);
    }
}
