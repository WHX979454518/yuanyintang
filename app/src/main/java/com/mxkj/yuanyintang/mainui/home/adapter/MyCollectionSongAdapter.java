package com.mxkj.yuanyintang.mainui.home.adapter;

import android.widget.ImageView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.home.bean.MyCollectionSongBean;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;

/**
 * Created by LiuJie on 2017/10/23.
 */

public class MyCollectionSongAdapter extends BaseQuickAdapter<MyCollectionSongBean.DataBean, BaseViewHolder> {


    public MyCollectionSongAdapter(List<MyCollectionSongBean.DataBean> data) {
        super(R.layout.item_my_song_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyCollectionSongBean.DataBean item, int position) {
        try {
            ImageLoader.with(mContext)
                    .getSize(400,400)
                    .override(80, 80)
                    .url(item.getImgpic_info().getLink())
                    .scale(ScaleMode.CENTER_CROP)
                    .into(helper.<ImageView>getView(R.id.civ_headimg));
        } catch (RuntimeException e) {
        }

        helper.setText(R.id.tv_name, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_play_num, StringUtils.setNum(item.getCounts()));
        helper.setText(R.id.tv_total_num, StringUtils.setNum(item.getTotal()));

    }
}
