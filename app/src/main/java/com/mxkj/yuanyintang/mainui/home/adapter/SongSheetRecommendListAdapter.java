package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity;
import com.mxkj.yuanyintang.mainui.newapp.home.TodayHotSongActivity;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 歌单
 */
public class SongSheetRecommendListAdapter extends BaseQuickAdapter<MusicIndex.ItemInfoListBean.RecommendBean, BaseViewHolder> {
    public SongSheetRecommendListAdapter(List<MusicIndex.ItemInfoListBean.RecommendBean> data) {
        super(R.layout.musicrecycle_item_recommend_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MusicIndex.ItemInfoListBean.RecommendBean item, int position) {
        if (item != null && item.getImgpic_info() != null) {
            ImageLoader.with(mContext)
                    .getSize(190,190)
                    .setUrl(item.getImgpic_info())
                    .scale(ScaleMode.CENTER_CROP)
                    .into(helper.<ImageView>getView(R.id.cimg_cover));
        }

        helper.setText(R.id.tv_player_num, StringUtils.setNum(item.getCounts()));
        helper.setText(R.id.tv_music_name, StringUtils.isEmpty(item.getTitle()));
        RxView.clicks(helper.<LinearLayout>getView(R.id.layout_click))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (item.getShow_type() == 1) {//新歌速递
//                            TODO  跳转新歌速递
                            MobclickAgent.onEvent(mContext, "home_new");
                            Intent intent = new Intent(mContext, TodayHotSongActivity.class);
                            intent.putExtra("type", "new");
                            mContext.startActivity(intent);
                        } else {
                            if (mContext instanceof SongSheetDetailsActivity) {
                                MobclickAgent.onEvent(mContext, "album_detail_recommend_album");
                            }else {
                                MobclickAgent.onEvent(mContext, "home_sugg_album");
                            }
                            Intent intent = new Intent(mContext, SongSheetDetailsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, item.getId() + "");
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }

                    }
                });
    }
}