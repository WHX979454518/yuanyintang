package com.mxkj.yuanyintang.mainui.pond.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.pond.bean.PondHotTagBean;

import java.util.List;

/**
 * Created by LiuJie on 2017/9/28.
 */

public class TagListAdapter extends BaseMultiItemQuickAdapter<PondHotTagBean.DataBean, BaseViewHolder> {
    public TagListAdapter(List<PondHotTagBean.DataBean> data) {
        super(data);
        addItemType(0, R.layout.taglist_item);
        addItemType(1, R.layout.taglist_item_devider);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    protected void convert(BaseViewHolder helper, PondHotTagBean.DataBean item) {
        int itemViewType = helper.getItemViewType();
        if (itemViewType==0) {
            helper.setText(R.id.tag_title, item.getTitle());
        }
    }
}
