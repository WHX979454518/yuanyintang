package com.mxkj.yuanyintang.mainui.home.adapter;

import android.graphics.Bitmap;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.config.SingleConfig;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.home.bean.ListeningToSongPeopleBean;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LiuJie on 2017/10/19.
 */

public class ListeningToSongPeopleAdapter extends BaseQuickAdapter<ListeningToSongPeopleBean.DataBean, BaseViewHolder> {


    public ListeningToSongPeopleAdapter(List<ListeningToSongPeopleBean.DataBean> data) {
        super(R.layout.item_listening_to_song_people, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, ListeningToSongPeopleBean.DataBean item, int position) {
        ImageLoader.with(mContext)
                .override(60,60)
.getSize(200,200)
                .url(item.getHead_link())
                .scale(ScaleMode.CENTER_CROP)
                .asCircle()
                .asBitmap(new SingleConfig.BitmapListener() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        helper.<CircleImageView>getView(R.id.civ_headimg).setImageBitmap(bitmap);
                    }

                    @Override
                    public void onFail() {

                    }
                });
        helper.setText(R.id.tv_name, StringUtils.isEmpty(item.getNickname()));
        helper.setText(R.id.tv_sign, StringUtils.isEmpty(item.getSignature()));
        helper.setText(R.id.tv_private, item.getRelation() + "");
//        if (item.getSex() == 1) {
//            iv_sex.setImageResource(R.drawable.icon_male);
//            iv_sex.setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_3dp_blue_60_bg));
//        } else {
//            iv_sex.setImageResource(R.drawable.icon_female);
//            iv_sex.setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_3dp_pink_99_bg));
//        }
    }
}
