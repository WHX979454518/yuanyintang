package com.mxkj.yuanyintang.utils.uiutils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 *GridView的更具数量多少来计算自适应的高度，一定要记得去除以每行有几个的问题，这里是一行2个，所以高度算出来除以2,还有药记得吧每个item的间距算进去
 * ，10是这里的5+5（android:horizontalSpacing="5dip"android:verticalSpacing="5dip"）
 */

public class SetGridViewViewHeightBasedOnChildren {
    public static void setListViewHeightBasedOnChildren(GridView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {

            return;

        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight
                + (10 * (listAdapter.getCount() - 1)))/2;
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);

    }
}
