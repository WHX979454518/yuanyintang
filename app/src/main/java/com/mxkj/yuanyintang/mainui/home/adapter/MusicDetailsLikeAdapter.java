package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity;
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

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MusicDetailsLikeAdapter extends BaseQuickAdapter<MusicIndex.ItemInfoListBean.SongAllBean, BaseViewHolder> {
    public MusicDetailsLikeAdapter(List<MusicIndex.ItemInfoListBean.SongAllBean> data) {
        super(R.layout.home_item_recommend_music, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MusicIndex.ItemInfoListBean.SongAllBean item, int position) {
        if (item.getImgpic_info() != null) {
            ImageLoader.with(mContext)
                    .getSize(400, 400)
                    .override(110, 110)
                    .url(item.getImgpic_info().getLink())
                    .error(R.drawable.logo_defaulft)
                    .scale(ScaleMode.CENTER_CROP)
                    .rectRoundCorner(10)
                    .into(helper.<CircleImageView>getView(R.id.iv_music_cover));
        }


        helper.setText(R.id.tv_player_num, StringUtils.setNum(item.getCounts()));
        helper.setText(R.id.tv_music_name, StringUtils.isEmpty(item.getTitle()));
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
                        PlayCtrlUtil.INSTANCE.playSheet(mContext, item.getId() + "");
                    }
                });
    }
}
