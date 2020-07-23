package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Context;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;

import java.util.List;

/**
 * Created by LiuJie on 2017/9/27.
 */

public class MusicDetailsListeningAdapter extends BaseQuickAdapter<MusicIndex.ItemInfoListBean.RelatedBean, BaseViewHolder> {

    private Context context;

    public MusicDetailsListeningAdapter(List<MusicIndex.ItemInfoListBean.RelatedBean> data, Context context) {
        super(R.layout.music_detail_item_listening, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicIndex.ItemInfoListBean.RelatedBean item, int position) {
        ImageLoader.with(mContext)
                .getSize(200,200)
                .override(40, 40)
                .url(item.getHead_link())
                .asCircle()
                .scale(ScaleMode.CENTER_CROP)
                .into(helper.getView(R.id.cimg_cover));
        if (item.getIs_music() == 3) {
            helper.setVisible(R.id.iv_is_vip, true);
        } else {
            helper.setVisible(R.id.iv_is_vip, false);
        }
    }
}
