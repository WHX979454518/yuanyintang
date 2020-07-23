package com.mxkj.yuanyintang.mainui.myself.apdater;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.mainui.myself.bean.MyCollectionBean;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.MusicOperationDialog;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.mxkj.yuanyintang.musicplayer.service.MediaService.bean;
import static com.mxkj.yuanyintang.utils.constant.Constants.User.MUSIC_DIR_PRIVATE;

/**
 * Created by LiuJie on 2017/10/9.
 */

public class MyCollectionAdapter extends BaseQuickAdapter<MyCollectionBean.DataBean, BaseViewHolder> {

    private FragmentManager fragmentManager;
    private Handler myhandler;

    public MyCollectionAdapter(List<MyCollectionBean.DataBean> data, FragmentManager fragmentManager, Handler myhandler) {
        super(R.layout.item_my_collection, data);
        this.fragmentManager = fragmentManager;
        this.myhandler = myhandler;
    }
    public MyCollectionAdapter(List<MyCollectionBean.DataBean> data, FragmentManager fragmentManager) {
        super(R.layout.item_my_collection, data);
        this.fragmentManager = fragmentManager;
    }
    @Override
    protected void convert(final BaseViewHolder helper, final MyCollectionBean.DataBean item, final int position) {

        final TextView tv_music_name = helper.getView(R.id.tv_music_name);
        final TextView tv_music_nickname = helper.getView(R.id.tv_music_nickname);
        if (MediaService.mediaPlayer != null && MediaService.mediaPlayer.isPlaying()) {
            if (bean != null && bean.getId() == item.getId()) {
                item.setPlaying(true);
                tv_music_name.setTextColor(Color.parseColor("#ff6699"));
                tv_music_nickname.setTextColor(Color.parseColor("#ff6699"));
            }else {
                tv_music_name.setTextColor(Color.parseColor("#2b2b2b"));
                tv_music_nickname.setTextColor(Color.parseColor("#9da2a6"));
            }
        }

        helper.setText(R.id.tv_music_name, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_music_nickname, StringUtils.isEmpty(item.getNickname()));
        helper.setText(R.id.tv_music_name_gray, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_music_nickname_gray, StringUtils.isEmpty(item.getNickname()));

        try{
            ImageLoader.with(mContext)
                    .getSize(200,200)
                    .override(50, 50)
                    .url(item.getImgpic_info().getLink())
                    .error(R.drawable.logo_defaulft)
                    .scale(ScaleMode.CENTER_CROP)
                    .rectRoundCorner(5)
                    .into(helper.getView(R.id.iv_music_cover));
        }catch (RuntimeException e){}

        //判断是否是原创
//        if (item.getIsdown() == 1) {
//            helper.setVisible(R.id.isdown, true);
//        } else {
//            helper.setVisible(R.id.isdown, false);
//        }
        if (item.getMusic_type() == 1) {
            helper.setVisible(R.id.music_type, true);
        } else {
            helper.setVisible(R.id.music_type, false);
        }
        //是否已经下载
        if(TextUtils.isEmpty(item.getTitle())){
            System.currentTimeMillis();
        }else {
            StringUtils.isEmpty(item.getTitle());
        }
        String filePath = FileDownloadUtils.generateFilePath(getFilePath(mContext),item.getTitle());
        if(new File(filePath).exists()){
            helper.setVisible(R.id.isdown, true);
        }else {
            helper.setVisible(R.id.isdown, false);
        }

        //判断是否下架
        if (item.getStatus() == 0) {
//            helper.getView(R.id.tv_music_name).setVisibility(View.GONE);
            helper.getView(R.id.tv_music_name_gray).setVisibility(View.VISIBLE);
//            helper.getView(R.id.tv_music_nickname).setVisibility(View.GONE);
            helper.getView(R.id.tv_music_nickname_gray).setVisibility(View.VISIBLE);
        }


        if (null == item.getCheck()) {
            item.setCheck(false);
        }
        if (item.getCheck()) {
            helper.setVisible(R.id.check_song, true);
            helper.setVisible(R.id.iv_music_cover, false);
            helper.setVisible(R.id.iv_more, false);
            helper.setVisible(R.id.iv_more_show, true);
        } else {
            helper.setVisible(R.id.iv_music_cover, true);
            helper.setVisible(R.id.check_song, false);
            helper.setVisible(R.id.iv_more, true);
            helper.setVisible(R.id.iv_more_show, false);

        }
        helper.<CheckBox>getView(R.id.check_song).setChecked(item.getSingle_selection());



        RxView.clicks(helper.getView(R.id.layout_click))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (item.getCheck()) {
                            if (null != checkedSongListener) {
                                checkedSongListener.onChecked(!item.getSingle_selection(), position);
                            }
                        } else {
//                            Intent intent = new Intent(mContext, MusicDetailsActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString(MusicDetailsActivity.MUSIC_ID, item.getId() + "");
//                            intent.putExtras(bundle);
//                            mContext.startActivity(intent);
                            PlayCtrlUtil.INSTANCE.play(mContext,item.getId(),0);

                            Message message = Message.obtain();
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putInt("position",position);
                            message.setData(bundle);
                            myhandler.sendMessage(message);

                        }
                    }
                });
        RxView.clicks(helper.getView(R.id.iv_more))
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
                                .setSong_id(item.getSong_id())
                                .setStatu(item.getStatus()+"")
                                .setCollection(item.getCollection())
                                .setPosition(position)
                                .setType(10)
                                .setCommentType(1)
                                .setReportId(item.getSid())
                                .setMultiSelect(false)
                                .setPlayTime(item.getPlaytime());
                        try{
                            MusicBean.ShareDataBean shareDataBean = new MusicBean.ShareDataBean();
                            shareDataBean.setType("music");
                            if (null != item.getVideo_info()) {
                                shareDataBean.setVideo_link(item.getVideo_info().getLink());
                            }
                            try {
                                shareDataBean.setImage_link(item.getImgpic_info().getLink());
                                shareDataBean.setWebImgUrl(item.getImgpic_info().getLink());
                            } catch (RuntimeException e) {
                            }
                            shareDataBean.setUid(item.getUid());
                            shareDataBean.setMuisic_id(item.getId());
                            shareDataBean.setNickname(item.getNickname());
                            shareDataBean.setTitle(item.getTitle());
//                            shareDataBean.setShare_link(item.getsh);
//                            = bean!!.share_url
//                            shareDataBean.setMv();
//                            = bean!!.mv_info!!.link
                            musicBean.setShareDataBean(shareDataBean);

                            String link = item.getImgpic_info().getLink();
                            MusicBean.ImgpicInfoBean imgpicInfoBean = new MusicBean.ImgpicInfoBean();
                            imgpicInfoBean.setLink(link);
                            musicBean.setImgpic_info(imgpicInfoBean);
                        }catch (RuntimeException e){}


                        final MusicOperationDialog musicOperationDialog = new MusicOperationDialog(musicBean, fragmentManager);
                        musicOperationDialog.show(fragmentManager, position + "");
                        musicOperationDialog.setSetMusicOperationListener(new MusicOperationDialog.SetMusicOperationListener() {
                            @Override
                            public void onCollection(int collection, int type) {
                                if (null != checkedSongListener) {
                                    checkedSongListener.onRefreshData();
                                }
//                                item.setCollection(collection);
//                                notifyDataSetChanged();
                                musicOperationDialog.dismiss();
                            }

                            @Override
                            public void getType(int type) {

                            }
                        });
                    }
                });

    }

    public interface ClickCheckedSongListener {
        void onChecked(Boolean aBoolean, int position);

        void onRefreshData();
    }

    private ClickCheckedSongListener checkedSongListener;

    public void setCheckedSongListener(ClickCheckedSongListener checkedSongListener) {
        this.checkedSongListener = checkedSongListener;
    }


    private String getFilePath(Context context) {
        String path = "";
        if(CacheUtils.INSTANCE.getBoolean(context, MUSIC_DIR_PRIVATE, false)){
            path = (Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + "Music");
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        }else {
            path =Constants.Path.APP_PATH + File.separator + "Music";
        }
        return path;
    }
}
