package com.mxkj.yuanyintang.base.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyAdapter extends BaseAdapter {

    public abstract int getCount();

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public abstract View getView(int position, View convertView, ViewGroup parent);
}
