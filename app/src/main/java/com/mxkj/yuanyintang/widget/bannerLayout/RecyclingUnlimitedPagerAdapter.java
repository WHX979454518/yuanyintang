package com.mxkj.yuanyintang.widget.bannerLayout;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.mxkj.yuanyintang.widget.bannerLayout.util.ViewHolder;

import java.util.List;

/**
 * Created by LiuJie on 2017/5/17.
 */

public abstract class RecyclingUnlimitedPagerAdapter<T> extends BaseRecyclingPagerAdapter {

    private List<T> mList;
    private int mResId;
    private Context context;
    private LayoutInflater layoutInflater;
    private ViewPager viewPager;
    /**
     * 这个size一定要比较大才行，默认为轮播图片张数的30倍。
     */
    public RecyclingUnlimitedPagerAdapter(ViewPager viewPager, Context context, List<T> mList, int resId){
        this.mList = mList;
        this.mResId = resId;
        this.context = context;
        this.viewPager =viewPager;
        if (context!=null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView==null){
            convertView = layoutInflater.inflate(mResId,null);
        }
        ViewHolder viewHolder = new ViewHolder(convertView);
        if (mList != null && mList.size() > 0) {
           position %= mList.size();
            T t=mList.get(position);
            if (t!=null) {
                onBind(position, t, viewHolder);
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            if (mList.size() >1) {
                return mList.size()*30;
            }else if (mList.size() ==1){
                return 1;
            }
        }
        return 0;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
//        super.finishUpdate(container);
        if (mList.size() > 1&&viewPager instanceof ClickViewPager) {
            int position = viewPager.getCurrentItem();
            if (position == 0) {
                //count代表轮播图片的数量。
                position = mList.size();
                viewPager.setCurrentItem(position, false);
            } else if (position == mList.size()*30 - 1) {
                position = mList.size()*30 - 1;
                viewPager.setCurrentItem(position, false);
            }
        }
    }

    /**
     * 绑定数据
     */
    protected abstract void onBind(int position, T data, ViewHolder holder);
}
