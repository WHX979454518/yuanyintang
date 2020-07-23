package com.mxkj.yuanyintang.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;


/**
 * 自定义标题栏类
 *
 * @author TAG
 */
public class NavigationBar extends LinearLayout {
    public Button leftButton, rightButton;
    //	ToggleButton rightToggleButton;
    ViewGroup headerViewGroup;
    TextView navTitleTextView;

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationBar init() {
        return init(false);
    }

    /**
     * 初始化
     *
     * @param isRoot
     * @return
     */
    public NavigationBar init(boolean isRoot) {
        headerViewGroup = (ViewGroup) this.findViewById(R.id.headerViewGroup);
        navTitleTextView = (TextView) this.findViewById(R.id.navTitleTextView);
        leftButton = (Button) this.findViewById(R.id.leftButton);
        rightButton = (Button) this.findViewById(R.id.rightButton);
//		rightToggleButton = (ToggleButton) this.findViewById(rightToggleButtonId);
        if (!isRoot) {
            //设置左边按钮的返回功能
            setLeftButton("", new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) getContext()).finish();
                }
            }, true);
        }
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(CharSequence title) {
        navTitleTextView.setText(title);
    }

    /**
     * 设置标题
     *
     * @param color
     */
    public void setTitleColor(int color) {
        navTitleTextView.setTextColor(color);
    }

    /**
     * 添加头
     *
     * @param header
     */
    public void setHeader(ViewGroup header) {
        headerViewGroup.addView(header);
    }

    /**
     * 左边按钮图标
     *
     * @param leftDrawable 图片
     */
    public void setLeftButtonDrawable(Drawable leftDrawable) {
        if (leftButton != null) {
            leftButton.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
        }
    }

    public void setNormalTypeFace() {
        if (null != navTitleTextView) {
            navTitleTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }

    /**
     * 设置左边的按钮
     *
     * @param title
     * @param listener
     * @param haveIcon
     */
    public void setLeftButton(String title, final OnClickListener listener, boolean haveIcon) {
        leftButton.setVisibility(View.VISIBLE);
        if (!haveIcon) {
            leftButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        leftButton.setText(title);
        leftButton.setOnClickListener(listener);
    }

    /**
     * 设置右边的按钮
     *
     * @param title
     * @param listener
     */
    public void setRightButton(String title, Drawable left, Drawable right, Drawable top, Drawable bottom, final OnClickListener listener) {
        rightButton.setVisibility(View.VISIBLE);
        rightButton.setText(title);
        rightButton.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        if (listener!=null) {
            rightButton.setOnClickListener(listener);
        }
    }

    public void setRightButtonImageView(Drawable right) {
        if (rightButton != null) {
            rightButton.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
        }
    }

    public void showLeftButton() {
        if (leftButton != null) {
            leftButton.setVisibility(VISIBLE);
        }
    }

    public void showRightButton() {
        if (rightButton != null) {
            rightButton.setVisibility(VISIBLE);
        }
    }

    public void hideRightButton() {
        if (rightButton != null) {
            rightButton.setVisibility(INVISIBLE);
        }
    }

    public void hideLeftButton() {
        if (leftButton != null) {
            leftButton.setVisibility(INVISIBLE);
        }
    }

    public Button getRightButton() {
        return rightButton;
    }

    public Button getLeftButton() {
        return leftButton;
    }

    /**
     * 左边按钮点击事件
     *
     * @param listener
     */
    public void setOnLeftClick(final OnClickListener listener) {
        leftButton.setOnClickListener(listener);
    }

    /**
     * 左边按钮点击事件
     *
     * @param listener
     */
    public void setOnRightClick(final OnClickListener listener) {
        rightButton.setOnClickListener(listener);
    }

    /**
     * 设置左边的按钮
     *
     * @param title
     * @param listener
     * @param drawable
     */
    public void setLeftButton(String title, final OnClickListener listener, Drawable drawable) {
        leftButton.setVisibility(View.VISIBLE);
        if (drawable != null) {
            leftButton.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        leftButton.setText(title);
        leftButton.setOnClickListener(listener);
    }

    public void setLeftButton(String title, int color,final OnClickListener listener, Drawable drawable) {
        leftButton.setVisibility(View.VISIBLE);
        if (drawable != null) {
            leftButton.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        leftButton.setText(title);
        leftButton.setTextColor(color);
        leftButton.setOnClickListener(listener);
    }

    public void setRightButtonText(CharSequence str) {
        if (rightButton != null)
            rightButton.setText(str);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setRightButtonImage(int i) {
        if (rightButton != null)
            rightButton.setBackgroundResource(i);

    }

    /**
     * 设置右边按钮是否可用
     *
     * @param enabled
     */
    public void enableRightButton(boolean enabled) {
        if (rightButton != null)
            rightButton.setEnabled(enabled);
    }
}