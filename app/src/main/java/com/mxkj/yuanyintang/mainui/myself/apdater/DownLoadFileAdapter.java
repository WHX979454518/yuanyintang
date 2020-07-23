package com.mxkj.yuanyintang.mainui.myself.apdater;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.database.updatafilesql.UpDataFileBean;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.MusicOperationDialog;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by LiuJie on 2017/10/10.
 */

public class DownLoadFileAdapter extends BaseQuickAdapter<UpDataFileBean, BaseViewHolder> {

    private FragmentManager fragmentManager;

    public DownLoadFileAdapter(List<UpDataFileBean> data, FragmentManager fragmentManager) {
        super(R.layout.item_my_collection, data);
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void convert(BaseViewHolder helper, final UpDataFileBean item, final int position) {
        Log.d(TAG, "convert: --------->" + item.getSuccess());
        ImageLoader.with(mContext)
                .getSize(200,200)

                .override(50, 50)
                .url(item.getImg_url())
                .scale(ScaleMode.CENTER_CROP)
                .rectRoundCorner(4)
                .into(helper.<ImageView>getView(R.id.iv_music_cover));

//        //判断能否下载和是否是原创
//        if (item.getIsdown() == 1) {
//            helper.setVisible(R.id.isdown, true);
//        } else {
//            helper.setVisible(R.id.isdown, false);
//        }
//        if (item.getMusic_type() == 1) {
//            helper.setVisible(R.id.music_type, true);
//        } else {
//            helper.setVisible(R.id.music_type, false);
//        }


        helper.setText(R.id.tv_music_name, StringUtils.isEmpty(item.getMusic_name()));
        helper.setText(R.id.tv_music_nickname, StringUtils.isEmpty(item.getNickname()));
        RxView.clicks(helper.getView(R.id.layout_click)).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                PlayCtrlUtil.INSTANCE.localityPlay(mContext, item, true,true);
            }
        });
        RxView.clicks(helper.getView(R.id.iv_more))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MusicBean musicBean = new MusicBean()
                                .setMusic_name(item.getMusic_name())
                                .setCommentNum(0)
                                .setUid(item.getUid())
                                .setMusic_id(item.getMusic_id() + "")
                                .setMusician_name(item.getNickname())
                                .setSong_id(item.getSong_id())
                                .setSongName(item.getSongName())
                                .setCollection(item.getCollection())
                                .setPosition(position)
                                .setType(6)
                                .setReportId(item.getId())
                                .setCommentType(1)
                                .setMultiSelect(false);
//                                .setPlayTime(item.getPlayTime());
                        String link = item.getImg_url();
                        MusicBean.ImgpicInfoBean imgpicInfoBean = new MusicBean.ImgpicInfoBean();
                        imgpicInfoBean.setLink(link);
                        musicBean.setImgpic_info(imgpicInfoBean);

                        final MusicOperationDialog musicOperationDialog = new MusicOperationDialog(musicBean, fragmentManager);
                        musicOperationDialog.show(fragmentManager, position + "");
                        musicOperationDialog.setSetMusicOperationListener(new MusicOperationDialog.SetMusicOperationListener() {
                            @Override
                            public void onCollection(int collection, int position) {
                                item.setCollection(collection);
                                notifyDataSetChanged();
                                musicOperationDialog.dismiss();
                            }

                            @Override
                            public void getType(int type) {
                                PlayCtrlUtil.INSTANCE.localityPlay(mContext, item, true,true);
                            }
                        });
                    }
                });
    }
}
