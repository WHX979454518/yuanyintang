package com.mxkj.yuanyintang.mainui.home.adapter;

import android.support.v4.content.ContextCompat;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
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

public class RecommendMusicDataAdapter extends BaseQuickAdapter<HomeIndex.ItemInfoListBean.SongBeanX.SongBean, BaseViewHolder> {

    public RecommendMusicDataAdapter(List<HomeIndex.ItemInfoListBean.SongBeanX.SongBean> data) {
        super(R.layout.home_item_recommend_music, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final HomeIndex.ItemInfoListBean.SongBeanX.SongBean item, final int position) {
        try {
            ImageLoader.with(mContext)
                    .getSize(400, 400)
                    .override(120, 120)
                    .url(item.getImgpic_info().getLink())
                    .asCircle()
                    .scale(ScaleMode.CENTER_CROP)
                    .into(helper.<CircleImageView>getView(R.id.iv_music_cover));
        } catch (RuntimeException e) {
        }

        helper.setText(R.id.tv_player_num, item.getCounts_text());
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
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        PlayCtrlUtil.INSTANCE.playSheet(mContext, item.getId() + "");
                    }
                });

    }
}
