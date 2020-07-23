package com.mxkj.yuanyintang.mainui.login_regist.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.mxkj.yuanyintang.R;

/**
 * Created by LiuJie on 2018/1/15.
 */

public class EmailpopUpWindow extends PopupWindow {
    private PopupWindow popupWindow;
    private View view;
    private EmailView emailView;
    private String[] emailSufixs = new String[]{"@qq.com", "@163.com", "@gmail.com", "@sina.com",
            "@foxmail.com", "@139.com", "@sina.com"};
    public void showPop(final Context context, View spaceView, String coutText, final EmailPopCallBack callBack) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.popwindow_email, null);
        }
        if (popupWindow == null) {
            popupWindow = new PopupWindow(view,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            emailView = view.findViewById(R.id.email_view);
            emailView.setCoutText(coutText, new EmailView.EmailViewItemCallback() {
                @Override
                public void onEmailItemClick(String text) {
                    callBack.popEmailItemClick(text);
                    dismiss();
                }
            });
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int popupWidth = view.getMeasuredWidth();    //  获取测量后的宽度
            int popupHeight = view.getMeasuredHeight();  //获取测量后的高度
            int[] location = new int[2];
            popupWindow.setBackgroundDrawable(new BitmapDrawable());//注意这里如果不设置，下面的setOutsideTouchable(true);允许点击外部消失会失效
            popupWindow.setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
            popupWindow.setFocusable(true);
            spaceView.getLocationOnScreen(location);
            isShow(coutText, spaceView);
        } else {
            if (!popupWindow.isShowing()) {
                isShow(coutText, spaceView);
                emailView.setCoutText(coutText, new EmailView.EmailViewItemCallback() {
                    @Override
                    public void onEmailItemClick(String text) {
                        callBack.popEmailItemClick(text);
                        dismiss();
                    }
                });
            }
        }

/**
 *  在android7.0上，如果不主动约束PopuWindow的大小，比如，设置布局大小为 MATCH_PARENT,那么PopuWindow会变得尽可能大，以至于 view下方无空间完全显示PopuWindow，而且view又无法向上滚动，此时PopuWindow会主动上移位置，直到可以显示完全。
 *　解决办法：主动约束PopuWindow的内容大小，重写showAsDropDown方法：
 * @param anchor
 */
        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) context).getWindow().setAttributes(lp);
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    private void isShow(String coutText, View spaceView) {
        int index = coutText.indexOf("@");
        if (index == -1) {
            if (coutText.matches("^[a-zA-Z0-9_]+$")&&!coutText.contains(".com")) {
                popupWindow.showAsDropDown(spaceView);    // 以触发弹出窗的view为基准，出现在view的正下方，弹出的pop_view左上角正对view的左下角  偏移量默认为0,0
            } else
                dismiss();//当用户中途输入非法字符时，关闭下拉提示框
        } else {
            popupWindow.showAsDropDown(spaceView);    // 以触发弹出窗的view为基准，出现在view的正下方，弹出的pop_view左上角正对view的左下角  偏移量默认为0,0
        }
    }

    public void dismissPop() {
        popupWindow.dismiss();
    }

    public interface EmailPopCallBack {
        void popEmailItemClick(String cout);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }
}
