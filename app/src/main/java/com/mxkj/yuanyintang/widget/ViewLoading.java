package com.mxkj.yuanyintang.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.mxkj.yuanyintang.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by LiuJie on 2017/11/16.
 */

public abstract class ViewLoading extends Dialog {
    public abstract void loadCancel();

    GifImageView imageView;

    public ViewLoading(Context context) {
        super(context, R.style.Loading);
        setContentView(R.layout.dialog_loading_view);
        imageView = (GifImageView) findViewById(R.id.img);
        imageView.setImageResource(R.drawable.loading_img);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.dimAmount = 0.6f;
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setAttributes(params);
        }
    }

    @Override
    public void onBackPressed() {
        loadCancel();
        dismiss();
    }
}