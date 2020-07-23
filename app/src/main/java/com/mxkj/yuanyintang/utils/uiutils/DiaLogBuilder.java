package com.mxkj.yuanyintang.utils.uiutils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.mxkj.yuanyintang.R;

public class DiaLogBuilder {
    private Dialog dialog;
    private Window window;
    private Context context;
    private static final String TAG = "DiaLogBuilder";

    public DiaLogBuilder(final Context context) {
        super();
        this.dialog = new Dialog(context, R.style.DialogBuilder);
        this.context = context;
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (selectSexDiaLogDismissLister != null) {
                    selectSexDiaLogDismissLister.onDiaLogDismss();
                }
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                Log.d(TAG, "onKey: --------->");
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    dialog.dismiss();
                }
                return false;
            }
        });
    }

    /*
    * 设置从哪里弹出
    */
    public DiaLogBuilder setCanceledOnTouchOutside(Boolean touchOutside) {
        dialog.setCanceledOnTouchOutside(touchOutside);
        return this;
    }

    /*
     * 设置从哪里弹出
     */
    public DiaLogBuilder setGrvier(int grvier) {
        if (window == null) {
            window = dialog.getWindow();
        }
        window.setGravity(grvier);
        return this;
    }

    /*
     * 设置视图
     */
    public DiaLogBuilder setContentView(View view) {
        dialog.setContentView(view);
        return this;
    }

    /*
     * 设置是否全屏
     */
    public DiaLogBuilder setFullScreen() {
        if (window == null) {
            window = dialog.getWindow();
        }
        LayoutParams layouParams = window.getAttributes();
        layouParams.width = LayoutParams.MATCH_PARENT;
        return this;
    }

    /*
     * 显示Dialog
     */
    public DiaLogBuilder show() {
        if (!dialog.isShowing()) {
            try {
                dialog.show();
            } catch (RuntimeException e) {

            }
        }
        return this;
    }

    /*
     * 隐藏Dialog
     */
    public DiaLogBuilder setDismiss() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }catch (RuntimeException e){}

        return this;
    }

    /*
     * Dialog的动画
     */
    public DiaLogBuilder setAniMo(int anim) {
        if (window == null) {
            window = dialog.getWindow();
        }
        window.setWindowAnimations(anim);
        return this;
    }

    public interface DismissLisenter {
        void onDiaLogDismss();
    }

    private DismissLisenter selectSexDiaLogDismissLister;

    public void setDiaLogDismissLister(DismissLisenter selectSexDiaLogDismissLister) {
        this.selectSexDiaLogDismissLister = selectSexDiaLogDismissLister;
    }

}
