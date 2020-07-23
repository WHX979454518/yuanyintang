package com.mxkj.yuanyintang.mainui.pond.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.newapp.weidget.RoundCornorImageView;
import com.mxkj.yuanyintang.mainui.pond.bean.PondDetialBean;
import com.mxkj.yuanyintang.utils.photopicker.util.BGAPhotoPickerUtil;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.GetImgLayoutParams;
import com.mxkj.yuanyintang.widget.largeImage.LargeImageView;
import com.sina.weibo.sdk.utils.UIUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by LiuJie on 2017/12/4.
 */

public class PondDetialImgListAdapter extends BaseQuickAdapter {
    private List<String> dataList;
    List<PondDetialBean.DataBean.ImglistInfoBean> imglist_info;


    public PondDetialImgListAdapter(@LayoutRes int layoutResId, @Nullable List data, List<PondDetialBean.DataBean.ImglistInfoBean> imglist_info) {
        super(layoutResId, data);
        this.dataList = data;
        this.imglist_info = imglist_info;
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        View itemView = holder.itemView;
//        final ImageView imgReply = itemView.findViewById(R.id.img_reply);
        final RoundCornorImageView large_view = itemView.findViewById(R.id.large_view);
        int anInt = CacheUtils.INSTANCE.getInt(mContext, Constants.Other.WIDTH, 0);
        String url = dataList.get(position);
        String imgUrl="";


        // 将Uri转bitmap 去压缩（新增）
        try {
            Bitmap sourceBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), Uri.parse(url));
            // bitmap转uri
            url = String.valueOf(Uri.parse(MediaStore.Images.Media.insertImage(mContext.getContentResolver(), sourceBitmap, null,null)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (url.contains("?")) {
//            imgUrl = url + "&w=" + UIUtils.dip2px(anInt, mContext) + "&h=0&quality=80";
            imgUrl = url + UIUtils.dip2px(anInt, mContext) + "&h=0&quality=1";//新增
        } else {
//            imgUrl = url + "?log_at=3&w=" + UIUtils.dip2px(anInt, mContext) + "&h=0&quality=80";
            imgUrl = url + "?log_at=3" + UIUtils.dip2px(anInt, mContext) + "&h=0&quality=1";//新增
        }
        Log.e(TAG, "onBindViewHolder: " + imgUrl);
        Log.e("imgUrlimgUrl", "" + imgUrl);
        if (imglist_info != null && imglist_info.size() > 0) {
            String ext = imglist_info.get(position).getExt();
            if (ext.equals("gif")) {
                imgUrl = dataList.get(position) + "/" + UIUtils.dip2px(anInt, mContext) + "/0/1/0/1/?format=0";
//                large_view.setImageResource(R.drawable.nothing);
                Glide.with(mContext).load(imgUrl).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(large_view);
            } else {
                //做了处理不会出现oom
                imgUrl = dataList.get(position) + "/" + UIUtils.dip2px(anInt, mContext) + "/0/1/0/1/?format=0";
                Glide.with(BGAPhotoPickerUtil.sApp).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        setBitmap(resource, large_view);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    }
                });
            }
        }
    }

    private void setBitmap(Bitmap file, ImageView imgReply) {
        if (file != null) {
            float width = file.getWidth();
            float height = file.getHeight();
            if (width > 0 && height > 0) {
                ViewGroup.LayoutParams lp = GetImgLayoutParams.getLayoutParams(mContext, file, imgReply);
                imgReply.setImageBitmap(file);
                imgReply.setLayoutParams(lp);
            }
        }
    }

}
