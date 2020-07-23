package com.mxkj.yuanyintang.widget.bannerLayout.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class ViewHolder extends RecyclerView.ViewHolder {
    private View mConvertView;

    public ViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        initImageLoader();
    }

    private void initImageLoader() {

    }

    @SuppressWarnings("unchecked")
    public <T extends View> T bind(int viewId) {// 通过ViewId得到View

        SparseArray<View> viewHolder = (SparseArray<View>) mConvertView
                .getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            mConvertView.setTag(viewHolder);
        }

        View childView = viewHolder.get(viewId);
        if (childView == null) {
            childView = mConvertView.findViewById(viewId);
            viewHolder.put(viewId, childView);
        }
        return (T) childView;

    }

    /**
     * 设置TextView文字
     *
     * @param resId TextView的id
     * @param text  文字内容
     */
    public void setText(int resId, CharSequence text) {
        if (bind(resId) instanceof TextView)
            ((TextView) bind(resId)).setText(text);
    }

    /**
     * 设置ImageView
     *
     * @param resId TextView的id
     */
    public void setImage(int resId, CharSequence text) {
        if (bind(resId) instanceof ImageView) ;
    }

    /**
     * 设置ImageView
     *
     * @param resId TextView的id
     */
    public void setImageLevel(int resId, int level) {
        if (bind(resId) instanceof ImageView) {
            ((ImageView) bind(resId)).setImageLevel(level);
        }
    }

    /**
     * 通过ViewId设置点击监听
     *
     * @param viewId
     * @param l
     */
    public void setOnClickListener(int viewId, View.OnClickListener l) {// 通过ViewId设置点击监听
        bind(viewId).setOnClickListener(l);
    }

    /**
     * 通过ViewId设置CheckView的Checkd
     *
     * @param viewId
     */
    public void setCheckd(int viewId, boolean isCheckd) {
        ((CheckBox) bind(viewId)).setChecked(isCheckd);
    }

    /**
     * 通过ViewId设置隐藏和显示
     *
     * @param viewId
     * @param visibility
     */
    public void setVisibility(int viewId, int visibility) {// 通过ViewId设置隐藏和显示
        bind(viewId).setVisibility(visibility);
    }
}