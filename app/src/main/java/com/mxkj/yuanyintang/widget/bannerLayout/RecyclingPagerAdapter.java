package com.mxkj.yuanyintang.widget.bannerLayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.mxkj.yuanyintang.widget.bannerLayout.util.ViewHolder;

import java.util.List;

/**
 * Created by liaoxiang on 16/3/21.
 */
public abstract class RecyclingPagerAdapter<T> extends BaseRecyclingPagerAdapter {

    private List<T> mList;
    private int mResId;
    private Context context;
    private LayoutInflater layoutInflater;

    public RecyclingPagerAdapter(Context context, List<T> mList, int resId) {
        this.mList = mList;
        this.mResId = resId;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(mResId, null);
        }
        ViewHolder viewHolder = new ViewHolder(convertView);
        if (mList != null && mList.size() > 0) {
            T t = mList.get(position);
            if (t != null) {
                onBind(position, t, viewHolder);
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
        if (mList != null && mList.size() > 0) return mList.size();
        return 0;
    }

    /**
     * 绑定数据
     */
    protected abstract void onBind(int position, T data, ViewHolder holder);
}
