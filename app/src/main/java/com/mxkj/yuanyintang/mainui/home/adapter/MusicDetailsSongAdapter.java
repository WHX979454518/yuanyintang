package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MusicDetailsSongAdapter extends BaseQuickAdapter<MusicIndex.ItemInfoListBean.LikesBean, BaseViewHolder> {
    public MusicDetailsSongAdapter(List<MusicIndex.ItemInfoListBean.LikesBean> data) {
        super(R.layout.item_musiclist_music, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MusicIndex.ItemInfoListBean.LikesBean item, int position) {
        if (item.getImgpic_info() != null) {
            ImageLoader.with(mContext)
                    .getSize(800, 800)
                    .override(110, 110)
                    .url(item.getImgpic_info().getLink())
                    .asSquare()
                    .scale(ScaleMode.CENTER_CROP)
                    .into(helper.<ImageView>getView(R.id.img_music));
        }
        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_company_name, StringUtils.isEmpty(item.getNickname()));
        helper.setText(R.id.tv_audience_num, StringUtils.setNum(item.getCounts()));
        RxView.clicks(helper.getView(R.id.layout_click))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
//                        Intent intent = new Intent(mContext, MusicDetailsActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString(MusicDetailsActivity.MUSIC_ID, item.getId() + "");
//                        intent.putExtras(bundle);
//                        mContext.startActivity(intent);

                        PlayCtrlUtil.INSTANCE.play(mContext,item.getId(),0);

                    }
                });
        if (null != MediaService.bean) {
            if (MediaService.bean.getId() == item.getId()) {
                if (MediaService.getMediaPlayer().isPlaying()) {
                    helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_true));
                } else {
                    helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_false));
                }
            } else {
                helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_false));
            }
        } else {
            helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_false));
        }
        RxView.clicks(helper.getView(R.id.iv_player))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        PlayCtrlUtil.INSTANCE.play(mContext, item.getId(), 0);
                    }
                });
    }
}
