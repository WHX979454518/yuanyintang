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
import com.mxkj.yuanyintang.mainui.home.bean.MusicIanMusicBean;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by LiuJie on 2017/9/29.
 */

public class MusicIanMusicAdapter extends BaseQuickAdapter<MusicIanMusicBean.DataBean, BaseViewHolder> {

    public MusicIanMusicAdapter(List<MusicIanMusicBean.DataBean> data) {
        super(R.layout.item_musiclist_music, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MusicIanMusicBean.DataBean item, int position) {
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
        if (item.getImgpic_info() != null) {
            ImageLoader.with(mContext)
                    .override(120, 120)
                    .getSize(400, 400)
                    .url(item.getImgpic_info().getLink())
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
                        PlayCtrlUtil.INSTANCE.play(mContext,item.getId(),0);
                    }
                });
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
