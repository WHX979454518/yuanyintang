package com.mxkj.yuanyintang.mainui.myself.apdater;

import android.content.Context;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.jakewharton.rxbinding2.view.RxView;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.mainui.myself.bean.PlayerHistoryBean;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.MusicOperationDialog;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.mxkj.yuanyintang.utils.constant.Constants.User.MUSIC_DIR_PRIVATE;

/**
 * Created by LiuJie on 2017/10/12.
 */

public class PlayerHistoryAdapter extends BaseQuickAdapter<PlayerHistoryBean.DataBean, BaseViewHolder> {

    private FragmentManager fragmentManager;

    public PlayerHistoryAdapter(List<PlayerHistoryBean.DataBean> data, FragmentManager fragmentManager) {
        super(R.layout.item_my_collection, data);
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final PlayerHistoryBean.DataBean item, final int position) {
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


        //判断是否已经下载
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

        //判断是否是原创
        if (item.getMusic_type() == 1) {
            helper.setVisible(R.id.music_type, true);
        } else {
            helper.setVisible(R.id.music_type, false);
        }


        helper.setText(R.id.tv_music_name, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_music_nickname, StringUtils.isEmpty(item.getNickname()));
        helper.setText(R.id.tv_music_name_gray, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_music_nickname_gray, StringUtils.isEmpty(item.getNickname()));
        //判断是否下架
        if (item.getStatus() == 0) {
//            helper.getView(R.id.tv_music_name).setVisibility(View.GONE);
            helper.getView(R.id.tv_music_name_gray).setVisibility(View.VISIBLE);
//            helper.getView(R.id.tv_music_nickname).setVisibility(View.GONE);
            helper.getView(R.id.tv_music_nickname_gray).setVisibility(View.VISIBLE);
        }

        if (item.getCheck()) {
            helper.setVisible(R.id.check_song, true);
            helper.setVisible(R.id.iv_music_cover, false);
        } else {
            helper.setVisible(R.id.check_song, false);
            helper.setVisible(R.id.iv_music_cover, true);
        }
        helper.<CheckBox>getView(R.id.check_song).setChecked(item.getSingle_selection());
        RxView.clicks(helper.getView(R.id.layout_click))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (item.getCheck()) {
                            if (null != checkedSongListener) {
                                checkedSongListener.onChecked(!item.getSingle_selection(), position);
                            }
                        } else {
                            PlayCtrlUtil.INSTANCE.play(mContext, item.getId(), 0);
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
                                .setUid(item.getUid())
                                .setMusic_id(item.getId() + "")
                                .setMusician_name(item.getNickname())
                                .setSong_id(item.getPid())
                                .setPosition(position)
                                .setCollection(item.getCollection())
                                .setCommentNum(item.getComment())
                                .setType(1)
                                .setReportId(item.getId())
                                .setCommentType(1)
                                .setMultiSelect(false)
                                .setPlayTime(item.getPlaytime());
                        try{
                            String link = item.getImgpic_info().getLink();
                            MusicBean.ImgpicInfoBean imgpicInfoBean = new MusicBean.ImgpicInfoBean();
                            imgpicInfoBean.setLink(link);
                            musicBean.setImgpic_info(imgpicInfoBean);
                        }catch (RuntimeException e){}



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

                            }
                        });
                    }
                });

    }

    public interface ClickCheckedSongListener {
        void onChecked(Boolean aBoolean, int position);
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
