package com.mxkj.yuanyintang.mainui.message.adapter;

import android.content.Context;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.mainui.message.bean.SystemMessageListBean;

import java.util.List;

/**
 * Created by LiuJie on 2017/11/10.
 */

public class TrendsMessageListAdapter extends BaseQuickAdapter<SystemMessageListBean.DataBeanXX.DataBeanX, BaseViewHolder> {

    private Context context;

    public TrendsMessageListAdapter(List<SystemMessageListBean.DataBeanXX.DataBeanX> data, Context context) {
        super(R.layout.message_item_trendsmsg_list, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SystemMessageListBean.DataBeanXX.DataBeanX item, int position) {

    }
}
