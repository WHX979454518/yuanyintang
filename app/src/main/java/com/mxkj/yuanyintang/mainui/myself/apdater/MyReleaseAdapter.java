package com.mxkj.yuanyintang.mainui.myself.apdater;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.UploadMusicActivity;
import com.mxkj.yuanyintang.mainui.myself.bean.MyReleaseBean;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by LiuJie on 2017/10/24.
 */

public class MyReleaseAdapter extends BaseQuickAdapter<MyReleaseBean.DataBean, BaseViewHolder> {


    public MyReleaseAdapter(List<MyReleaseBean.DataBean> data, Context context) {
        super(R.layout.item_my_release, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MyReleaseBean.DataBean item, int position) {

        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_label, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_nickname, StringUtils.isEmpty(item.getNickname()));
        helper.setText(R.id.tv_time, StringUtils.isEmpty(item.getUpdate_time()));
        try{
            ImageLoader.with(mContext)
                    .getSize(200,200)
                    .override(50, 50)
                    .url(item.getImgpic_info().getLink())
                    .scale(ScaleMode.CENTER_CROP)
                    .rectRoundCorner(4)
                    .into(helper.getView(R.id.iv_headImg));
        }catch (RuntimeException e){}



        //全部（无参数） 1代表审核中 2代表审核通过 -1代表审核失败 0代表下线 4代表草稿
        if (item.getAdmin_status() == -1) {
            helper.setText(R.id.tv_label, "未上线");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_3dp_orange_f3_bg_orange_8a_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(mContext, R.color.orange_8a));
        } else if (item.getAdmin_status() == 0) {
            helper.setText(R.id.tv_label, "未上线");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_3dp_orange_f3_bg_orange_8a_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(mContext, R.color.orange_8a));
        } else if (item.getAdmin_status() == 2) {
            helper.setText(R.id.tv_label, "已上线");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_3dp_blue_f9_bg_blue_c4_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(mContext, R.color.blue_c4));
        } else if (item.getAdmin_status() == 1) {
            helper.setText(R.id.tv_label, "审核中");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_3dp_green_fa_bg_green_d1_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(mContext, R.color.green_d1));
        } else if (item.getAdmin_status() == 4) {
            helper.setText(R.id.tv_label, "草稿");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_3dp_green_fa_bg_green_cd_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(mContext, R.color.green_cd));
        } else if (item.getAdmin_status() == 1&&item.getStatus()==1) {
            helper.setText(R.id.tv_label, "修改中");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_3dp_green_fa_bg_green_cd_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(mContext, R.color.green_cd));
        }
        RxView.clicks(helper.getView(R.id.layout_click)).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (item.getStatus() == 1) {
                            PlayCtrlUtil.INSTANCE.play(mContext,item.getId(),0);
                        } else {
                            Intent intent = new Intent(mContext, UploadMusicActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(UploadMusicActivity.DATA, item.getId());
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    }
                });
    }
}
