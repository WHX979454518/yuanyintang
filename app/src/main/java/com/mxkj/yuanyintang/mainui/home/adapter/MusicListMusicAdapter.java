package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.mainui.home.bean.MusicListMusicBean;
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

public class MusicListMusicAdapter extends BaseQuickAdapter<MusicListMusicBean, BaseViewHolder> {

    public MusicListMusicAdapter(List<MusicListMusicBean> data) {
        super(R.layout.item_musiclist_music, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MusicListMusicBean item, final int position) {
        if (item.getImgpic_info() != null) {
            ImageLoader.with(MainApplication.Companion.getApplication())
                    .getSize(480, 480)
                    .placeHolder(R.drawable.nothing_no_txt)
                    .url(item.getImgpic_info().getLink())
                    .error(R.drawable.nothing_no_txt)
                    .into(helper.getView(R.id.img_music));
        }
        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_company_name, StringUtils.isEmpty(item.getNickname()));
        helper.setText(R.id.tv_audience_num, StringUtils.setNum(item.getCounts()));

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
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        RxBus.getDefault().post(new RefreshIsPlayerEvent("music/list/music", position, !item.getCheck()));
                    }
                });
        RxView.clicks(helper.getView(R.id.layout_click))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
//                        Intent intent = new Intent(mContext, MusicDetailsActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString(MusicDetailsActivity.MUSIC_ID, item.getId() + "");
//                        intent.putExtras(bundle);
//                        mContext.startActivity(intent);
                        PlayCtrlUtil.INSTANCE.play(mContext,item.getId() ,0);
                    }
                });
        RxView.clicks(helper.getView(R.id.tv_company_name))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(mContext, MusicIanDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, item.getUid() + "");
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });
        RxView.clicks(helper.getView(R.id.iv_player))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        PlayCtrlUtil.INSTANCE.play(mContext, item.getId(), item.getSong_id());
                    }
                });
    }
}
