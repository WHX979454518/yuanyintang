package com.mxkj.yuanyintang.mainui.pond.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.pond.bean.PondTagBanner;

import java.util.List;

/**
 * Created by LiuJie on 2017/9/28.
 */

public class HotTagAdapter extends BaseMultiItemQuickAdapter<PondTagBanner.DataBean.TagBeanX.TagBean, BaseViewHolder> {
    public HotTagAdapter(List<PondTagBanner.DataBean.TagBeanX.TagBean> data) {
        super(data);
        addItemType(1, R.layout.pond_hot_tag);
    }

    @Override
    protected void convert(BaseViewHolder helper, PondTagBanner.DataBean.TagBeanX.TagBean item) {
        helper.setText(R.id.tag_title, item.getTitle());
        ImageLoader.with(mContext)
                .getSize(400, 200)
                .rectRoundCorner(4)
                .url(item.getHead_link())
                .placeHolder(R.drawable.similar_pond_default_pic)
                .scale(ScaleMode.CENTER_CROP)
                .into(helper.getView(R.id.img_tag));
    }
}
