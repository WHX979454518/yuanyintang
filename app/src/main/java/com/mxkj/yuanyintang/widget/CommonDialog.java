package com.mxkj.yuanyintang.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;


public class CommonDialog extends Dialog implements
        View.OnClickListener {

    private TextView tv_common_dialog_title;
    private TextView tv_common_dialog_msg;


    public Button btn_cancle;
    public Button btn_ok;

    /* //写资源id
     private int title;
     private int message = 0;
     private int cancle;
     private int enter;
     private String msgString = null;*/
    private String title;
    private String message;
    private String cancle;
    private String enter;
    @Nullable
    private String msgString = null;
    private CommonDialogCallback commonDialogCallback;
    public ImageView img_status;

    public CommonDialog(@NonNull Context context, boolean cancelable,
                        OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CommonDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

   /* public CommonDialog(Context context, int title, int message, int cancle,
                        int enter, CommonDialogCallback commonDialogCallback) {
//        super(context, R.style.ServerDetailDialogStyle);
        super(context);
        this.title = title;
        this.message = message;
        this.cancle = cancle;
        this.enter = enter;
        this.commonDialogCallback = commonDialogCallback;
        View view = View.inflate(context, R.layout.layout_pop_cancle_sign, null);
        setContentView(view);

        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        attributes.width = (int) (width * 0.8);
        window.setAttributes(attributes);

        initView(view);
        initEvent(view);
    }*/

    public CommonDialog(@NonNull Context context, String title, String msgString,
                        String cancle, String enter, CommonDialogCallback commonDialogCallback) {
        super(context, R.style.ServerDetailDialogStyle);
//        super(context);
        this.title = title;
        this.msgString = msgString;
        this.cancle = cancle;
        this.enter = enter;
        this.commonDialogCallback = commonDialogCallback;
        View view = View.inflate(context, R.layout.layout_pop_cancle_sign, null);
        setContentView(view);

        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams attributes = window != null ? window.getAttributes() : null;
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        attributes.width = (int) (width * 0.8);
        attributes.y = 0;
        window.setAttributes(attributes);
        initView(view);
        initEvent(view);
    }

    public interface CommonDialogCallback {
        void btnOkClick(View v);
    }

    private void initView(@NonNull View view) {
        tv_common_dialog_title = (TextView) view
                .findViewById(R.id.tv_common_dialog_title);
        img_status = (ImageView) view
                .findViewById(R.id.img_status);
        tv_common_dialog_msg = (TextView) view
                .findViewById(R.id.tv_common_dialog_msg);
        btn_cancle = (Button) view.findViewById(R.id.cancle);
        btn_ok = (Button) view.findViewById(R.id.sure);

        tv_common_dialog_title.setText(title);
        if (msgString != null) {
            tv_common_dialog_msg.setText(msgString);
        } else {
            tv_common_dialog_msg.setText(message);
        }

        btn_cancle.setText(cancle);
        btn_ok.setText(enter);
    }

    private void initEvent(View view) {

        btn_cancle.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.cancle:
                dismiss();
                break;

            case R.id.sure:
                commonDialogCallback.btnOkClick(v);
                break;
        }

    }

}
