package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.mainui.home.bean.MusicListSongBean;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.event.RefreshIsPlayerEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by LiuJie on 2017/9/22.
 */

public class MusicListSongAdapter extends BaseQuickAdapter<MusicListSongBean, BaseViewHolder> {
    public MusicListSongAdapter(List<MusicListSongBean> data) {
        super(R.layout.item_musiclist_song, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MusicListSongBean item, final int position) {
        try{
            ImageLoader.with(mContext)
                    .override(240,240)
                    .getSize(240,240)
                    .setUrl(item.getImgpic_info())
                    .asCircle()
                    .scale(ScaleMode.CENTER_CROP)
                    .into(helper.<ImageView>getView(R.id.iv_music_cover));
        }catch (RuntimeException e){}
        helper.setText(R.id.tv_player_num, item.getCounts() + "");
        helper.setText(R.id.tv_music_name, StringUtils.isEmpty(item.getTitle()));

        if (null != MediaService.bean) {
            if (MediaService.bean.getSong_id() == item.getId()) {
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
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (!item.getCheck()) {
                            PlayCtrlUtil.INSTANCE.play(mContext, item.getId(), 0);
                        } else {

                        }
                        RxBus.getDefault().post(new RefreshIsPlayerEvent("music/list/song", position, !item.getCheck()));
                    }
                });

        RxView.clicks(helper.getView(R.id.layout_click))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(mContext, SongSheetDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, item.getId() + "");
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });
        RxView.clicks(helper.getView(R.id.iv_player))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        PlayCtrlUtil.INSTANCE.playSheet(mContext, item.getId() + "");
                    }
                });
    }
}
