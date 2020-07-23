package com.mxkj.yuanyintang.mainui.home.music_charts.adapter;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.music_charts.bean.ChartsListBean;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.event.PlayerMusicRefreshDataEvent;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * Created by LiuJie on 2017/9/29.
 */

public class MusicChartsAdapter extends BaseMultiItemQuickAdapter<ChartsListBean.DataBean.DataListBean, BaseViewHolder> {
    public MusicChartsAdapter(List<ChartsListBean.DataBean.DataListBean> data) {
        super(data);
        addItemType(1, R.layout.item_music_charts);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ChartsListBean.DataBean.DataListBean item) {
        if (item == null) {
            return;
        }
        if (null != MediaService.bean) {
            if (MediaService.bean.getId() == item.getMusic_id()) {
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
                        if (MediaService.bean!=null&&MediaService.bean.getId()==item.getMusic_id()){
                            mContext.sendBroadcast(new Intent(MediaService.ACTION_PAUSE));
                        }else {
                            MediaService.bean = null;
                            RxBus.getDefault().post(new PlayerMusicRefreshDataEvent());
                            PlayCtrlUtil.INSTANCE.play(mContext, item.getMusic_id(), 0);
                        }
                    }
                });
        RxView.clicks(helper.getView(R.id.ll_to_detial))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
//                        Intent intent = new Intent(mContext, MusicDetailsActivity.class);
//                        intent.putExtra(MUSIC_ID, item.getMusic_id() + "");
//                        mContext.startActivity(intent);
                        PlayCtrlUtil.INSTANCE.play(mContext,item.getMusic_id(),0);
                    }
                });

        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_score, StringUtils.isEmpty(item.getScore_total_desc() + ""));
        if (getHeaderLayoutCount()>0) {
            helper.setText(R.id.tv_order, String.valueOf(helper.getPosition() + 3));
        }else{
            helper.setText(R.id.tv_order, String.valueOf(helper.getPosition()+1));
        }

        helper.setText(R.id.tv_nickname, StringUtils.isEmpty(item.getNickname()));
        ImageLoader.with(mContext).getSize(65, 65).url(item.getImgpic_link()).into(helper.getView(R.id.iv_music_bg));
        ImageView iv_status = helper.getView(R.id.iv_order_status);
        if (item.getGrow_status() == 1) {
            iv_status.setImageResource(R.drawable.chart_order_up);
        } else if (item.getGrow_status() == -1) {
            iv_status.setImageResource(R.drawable.chart_order_down);
        } else {
            iv_status.setImageResource(R.drawable.charts_order_normal);

        }
    }
}
