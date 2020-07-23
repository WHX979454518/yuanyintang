package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.home.bean.ContainSongBean;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by LiuJie on 2017/10/20.
 */

public class ContainSongListAdapter extends BaseQuickAdapter<ContainSongBean.DataBean, BaseViewHolder> {

    public ContainSongListAdapter(List<ContainSongBean.DataBean> data) {
        super(R.layout.item_contain_song_list, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ContainSongBean.DataBean item, final int position) {
        try{
            ImageLoader.with(mContext)
                    .getSize(200,200)
//                    .asCircle()
//                    .override(60, 60)
                    .url(item.getImgpic_info().getLink())
                    .into(helper.<CircleImageView>getView(R.id.civ_headimg));
        }catch (RuntimeException e){}



        helper.setText(R.id.tv_name, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_play_num, StringUtils.setNum(item.getTotal())+"首,");
        helper.setText(R.id.tv_music_num, "播放"+StringUtils.setNum(item.getCounts())+"次");
        if (item.getIs_song() == 1) {
            helper.<CheckBox>getView(R.id.check_song).setChecked(true);
        } else {
            helper.<CheckBox>getView(R.id.check_song).setChecked(false);
        }
        helper.<CheckBox>getView(R.id.check_song).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CacheUtils.INSTANCE.getBoolean(mContext,Constants.User.IS_LOGIN)) {
                    if (null != isSongLisListener) {
                        isSongLisListener.isSong(position);
                    }
                } else {
                    helper.<CheckBox>getView(R.id.check_song).setChecked(false);
                    Intent intent = new Intent();
                    intent.setClass(mContext, LoginRegMainPage.class);
                    mContext.startActivity(intent);
                }

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

    }

    public interface IsSongLisListener {
        void isSong(int position);
    }

    private IsSongLisListener isSongLisListener;

    public void setIsSongLisListener(IsSongLisListener isSongLisListener) {
        this.isSongLisListener = isSongLisListener;
    }
}
