package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.config.SingleConfig;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.mainui.home.bean.HomeIndex;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by LiuJie on 2017/9/19.
 */

public class RecommendMusicianDataAdapter extends BaseQuickAdapter<HomeIndex.ItemInfoListBean.MusicianBeanX.MusicianBean, BaseViewHolder> {

    public RecommendMusicianDataAdapter(List<HomeIndex.ItemInfoListBean.MusicianBeanX.MusicianBean> data) {
        super(R.layout.home_item_recommend_musician, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HomeIndex.ItemInfoListBean.MusicianBeanX.MusicianBean item, final int position) {
        ImageLoader.with(mContext)
                .getSize(200, 200)
                .override(90, 90)
                .url(item.getHead_link())
                .asCircle()
                .scale(ScaleMode.CENTER_CROP)
                .asBitmap(new SingleConfig.BitmapListener() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        helper.<CircleImageView>getView(R.id.cimg_cover).setImageBitmap(bitmap);
                    }

                    @Override
                    public void onFail() {

                    }
                });
        helper.setText(R.id.tv_nickname, StringUtils.isEmpty(item.getNickname()));
        helper.setText(R.id.tv_music_name, StringUtils.isEmpty(item.getMusic().getTitle()));
        if (item.getIs_music() == 3) {
            helper.setVisible(R.id.iv_is_vip, true);
        } else {
            helper.setVisible(R.id.iv_is_vip, false);
        }
        if (null != MediaService.bean) {
            if (MediaService.bean.getId() == item.getMusic().getId()) {
                if (MediaService.getMediaPlayer().isPlaying()) {
                    helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.home_icon_red_player_true));
                    helper.setTextColor(R.id.tv_music_name, ContextCompat.getColor(mContext, R.color.base_red));
                } else {
                    helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.home_icon_red_player_false));
                    helper.setTextColor(R.id.tv_music_name, ContextCompat.getColor(mContext, R.color.grey_a6_text));
                }
            } else {
                helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.home_icon_red_player_false));
                helper.setTextColor(R.id.tv_music_name, ContextCompat.getColor(mContext, R.color.grey_a6_text));
            }
        } else {
            helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.home_icon_red_player_false));
            helper.setTextColor(R.id.tv_music_name, ContextCompat.getColor(mContext, R.color.grey_a6_text));
        }

        RxView.clicks(helper.getView(R.id.layout_click))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(mContext, MusicIanDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, item.getMusic().getUid() + "");
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });

        RxView.clicks(helper.getView(R.id.layout_click_music))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
//                        Intent intent = new Intent(mContext, MusicDetailsActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString(MusicDetailsActivity.MUSIC_ID, item.getMusic().getId() + "");
//                        intent.putExtras(bundle);
//                        mContext.startActivity(intent);
                        PlayCtrlUtil.INSTANCE.play(mContext, item.getMusic().getId(), 0);
                    }
                });

        RxView.clicks(helper.getView(R.id.iv_player))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        PlayCtrlUtil.INSTANCE.play(mContext, item.getMusic().getId(), 0);
                    }
                });
    }
}
