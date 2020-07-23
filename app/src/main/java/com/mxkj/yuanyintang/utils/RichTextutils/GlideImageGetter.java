package com.mxkj.yuanyintang.utils.RichTextutils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.R;

import java.util.HashSet;
import java.util.Set;

public class GlideImageGetter implements Html.ImageGetter, Drawable.Callback {

    private final Context mContext;

    private final TextView mTextView;

    private final Set<ImageGetterViewTarget> mTargets;

    public static GlideImageGetter get(View view) {
        return (GlideImageGetter) view.getTag(R.id.drawable_callback_tag);
    }

    public void clear() {
        GlideImageGetter prev = get(mTextView);
        if (prev == null) return;

        for (ImageGetterViewTarget target : prev.mTargets) {
            Glide.clear(target);
        }
    }

    public GlideImageGetter(Context context, TextView textView) {
//        this.mContext = context.getApplicationContext();
        this.mContext = MainApplication.Companion.getApplication();
        this.mTextView = textView;

//        clear(); 屏蔽掉这句在TextView中可以加载多张图片
        mTargets = new HashSet<>();
        mTextView.setTag(R.id.drawable_callback_tag, this);
    }

    @Override
    public Drawable getDrawable(String url) {
        final UrlDrawable_Glide urlDrawable = new UrlDrawable_Glide();
        System.out.println("Downloading from: " + url);
        Glide.with(mContext)
                .load(url)
                .override(60,60)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new ImageGetterViewTarget(mTextView, urlDrawable));

        return urlDrawable;

    }

    @Override
    public void invalidateDrawable(Drawable who) {
        mTextView.invalidate();
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {

    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {

    }

    private class ImageGetterViewTarget extends ViewTarget<TextView, GlideDrawable> {

        private final UrlDrawable_Glide mDrawable;

        private ImageGetterViewTarget(TextView view, UrlDrawable_Glide drawable) {
            super(view);
            mTargets.add(this);
            this.mDrawable = drawable;
        }

        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            Rect rect;
            rect = new Rect(0, 0, 60, 60);
            resource.setBounds(rect);
            mDrawable.setBounds(rect);
            mDrawable.setDrawable(resource);
            if (resource instanceof Animatable) {
                mDrawable.setCallback(get(getView()));
                resource.setLoopCount(GlideDrawable.LOOP_FOREVER);
                resource.start();
            }

            getView().setText(getView().getText());
            getView().invalidate();
        }

        private Request request;

        @Override
        public Request getRequest() {
            return request;
        }

        @Override
        public void setRequest(Request request) {
            this.request = request;
        }
    }
}