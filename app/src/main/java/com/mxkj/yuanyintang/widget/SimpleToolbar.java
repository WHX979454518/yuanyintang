package com.mxkj.yuanyintang.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.mxkj.yuanyintang.R;

/**
 * Created by LiuJie on 2017/10/12.
 */

public class SimpleToolbar extends Toolbar {

    /**
     * 左侧Title
     */
    private ImageView iv_back;
    /**
     * 中间Title
     */
    private TextView tv_title;
    /**
     * 右侧Title
     */
    private ImageView iv_more;

    public SimpleToolbar(Context context) {
        this(context, null);
    }

    public SimpleToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_more = (ImageView) findViewById(R.id.iv_more);
    }

    //设置title右边点击事件
    public void setRightClickListener(OnClickListener onClickListener) {
        iv_more.setOnClickListener(onClickListener);
    }
}
