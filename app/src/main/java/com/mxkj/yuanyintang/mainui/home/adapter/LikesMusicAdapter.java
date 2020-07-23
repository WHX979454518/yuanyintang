package com.mxkj.yuanyintang.mainui.home.adapter;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.mainui.home.bean.LikesMusicBean;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.extraui.dialog.MusicOperationDialog;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;

import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.widget.avloadingview.AVLoadingIndicatorView;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by LiuJie on 2017/10/20.
 */

public class LikesMusicAdapter extends BaseQuickAdapter<LikesMusicBean.DataBean, BaseViewHolder> {

    private FragmentManager fragmentManage;

    public LikesMusicAdapter(List<LikesMusicBean.DataBean> data, FragmentManager fragmentManager) {
        super(R.layout.musicrecycle_item_music_list, data);
        this.fragmentManage = fragmentManager;
    }

    @Override
    protected void convert(BaseViewHolder helper, final LikesMusicBean.DataBean item, final int position) {
        helper.setText(R.id.tv_music_name, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_music_nickname, StringUtils.isEmpty(item.getNickname()));
//        helper.setText(R.id.tv_table, (position + 1) + "");
        ImageView imageView = helper.<ImageView>getView(R.id.iv_more);
        RxView.clicks(imageView)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MusicBean musicBean = new MusicBean()
                                .setMusic_name(item.getTitle())
                                .setCommentNum(item.getComment())
                                .setUid(item.getUid())
                                .setMusic_id(item.getId() + "")
                                .setMusician_name(item.getNickname())
                                .setPosition(position)
                                .setSong_id(0)
                                .setCollection(0)
                                .setType(7)
                                .setReportId(item.getId())
                                .setMultiSelect(false)
                                .setCommentType(1)
                                .setPlayTime(item.getPlaytime());
                        String link = item.getImgpic_info().getLink();
                        MusicBean.ImgpicInfoBean imgpicInfoBean = new MusicBean.ImgpicInfoBean();
                        imgpicInfoBean.setLink(link);
                        musicBean.setImgpic_info(imgpicInfoBean);
                        MusicOperationDialog musicOperationDialog = new MusicOperationDialog(musicBean, fragmentManage);
                        musicOperationDialog.show(fragmentManage, position + "");

                    }
                });

        if (null != MediaService.bean) {
            if (MediaService.bean.getId() == item.getId()) {
                if (MediaService.getMediaPlayer().isPlaying()) {
//                    helper.setVisible(R.id.tv_table, false);
//                    helper.<AVLoadingIndicatorView>getView(R.id.avLoadingView).setVisibility(View.VISIBLE);
                } else {
//                    helper.setVisible(R.id.tv_table, true);
//                    helper.<AVLoadingIndicatorView>getView(R.id.avLoadingView).setVisibility(View.GONE);
                }
            } else {
//                helper.setVisible(R.id.tv_table, true);
//                helper.<AVLoadingIndicatorView>getView(R.id.avLoadingView).setVisibility(View.GONE);
            }
        } else {
//            helper.setVisible(R.id.tv_table, true);
//            helper.<AVLoadingIndicatorView>getView(R.id.avLoadingView).setVisibility(View.GONE);
        }

        RxView.clicks(helper.getView(R.id.layout_click))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        PlayCtrlUtil.INSTANCE.play(mContext, item.getId(), item.getSong_id());
                    }
                });
    }
}
