package com.mxkj.yuanyintang.mainui.search.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.search.fragment.MusicSearchBean;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.MusicOperationDialog;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.video.MvVideoActivitykt;
import com.mxkj.yuanyintang.widget.SearTextView;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.mxkj.yuanyintang.utils.constant.Constants.User.MUSIC_DIR_PRIVATE;

/**
 * Created by LiuJie on 2017/10/13.
 */

public class Music extends BaseMultiItemQuickAdapter<MusicSearchBean.DataBeanX.DataBean, BaseViewHolder> {
    private String key;
    private FragmentManager fragmentManager;

    public Music(List<MusicSearchBean.DataBeanX.DataBean> data, String key, FragmentManager fragmentManager) {
        super(data);
        this.fragmentManager = fragmentManager;
        this.key = key;
        addItemType(0, R.layout.music_searchresult_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MusicSearchBean.DataBeanX.DataBean item) {
        if (item.getImgpic_info()!=null){
            ImageLoader.with(mContext)
                    .getSize(200,200)

                    .override(45, 45)
                    .url(item.getImgpic_info().getLink())
                    .scale(ScaleMode.CENTER_CROP)
                    .rectRoundCorner(5)
                    .into(helper.getView(R.id.img_song));
        }
        SearTextView searTextView = helper.getView(R.id.songname);
        if (item.getTitle() != null && key != null) {
            searTextView.setSpecifiedTextsColor(item.getTitle(), key, Color.parseColor("#ff6699"));
        } else {
            searTextView.setText(item.getTitle());
        }
        SearTextView searTextView2 = helper.getView(R.id.singer);
        if (item.getNickname() != null && key != null) {
            searTextView2.setSpecifiedTextsColor(item.getNickname(), key, Color.parseColor("#ff6699"));
        } else {
            searTextView2.setText(item.getNickname());
        }


        //判断是否已经下载
        if(TextUtils.isEmpty(item.getTitle())){
            System.currentTimeMillis();
        }else {
            StringUtils.isEmpty(item.getTitle());
        }
        String filePath = FileDownloadUtils.generateFilePath(getFilePath(mContext),item.getTitle());
        if(new File(filePath).exists()){
            helper.getView(R.id.isdown).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.isdown).setVisibility(View.GONE);
        }

        //判断是否是原创
        if (item.getMusic_type() == 1) {
            helper.getView(R.id.music_type).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.music_type).setVisibility(View.GONE);
        }

        //看是否有mv，如果有显示mv小标识
        if (item.getMv()==null||item.getMv().equals("")) {
            helper.getView(R.id.jinrituijian_img).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.jinrituijian_img).setVisibility(View.VISIBLE);
        }
        RxView.clicks(helper.getView(R.id.jinrituijian_img))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if(null==item.getMv_info().getLink()||item.getMv_info().getLink().equals("")){

                          }else{
                          toMV(item.getId(),item.getMv_info().getLink(),item.getUid(),item.getTitle(),item.getNickname(),item.getImgpic());
                         }
                    }
                });


        Log.e("pppppppp",""+item.toString());
        helper.setOnClickListener(R.id.more_menu, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicBean musicBean = new MusicBean()
                        .setMusic_name(item.getTitle())
                        .setCommentNum(item.getCounts())
                        .setUid(item.getUid())
                        .setMusic_id(item.getId() + "")
                        .setMusician_name(item.getNickname())
//                        .setSong_id(item.getSong_id())
//                        .setSongName(item.getSong_title())
                        .setCollection(item.getCollection())
                        .setPosition(helper.getPosition())
                        .setType(1)
                        .setCommentType(1)
                        .setReportId(item.getId())
                        .setMultiSelect(false)
                        .setPlayTime(item.getPlaytime())
                        .setCommentNum(item.getComment())
                        .setMv(item.getMv());

                if (item.getMv_info() != null) {
                    String mvlink = item.getMv_info().getLink();
                    MusicBean.MvInfoBean mvInfoBean = new MusicBean.MvInfoBean();
                    mvInfoBean.setLink(mvlink);
                    musicBean.setMvInfoBean(mvInfoBean);
                }


                String link = item.getImgpic_info().getLink();
                MusicBean.ImgpicInfoBean imgpicInfoBean = new MusicBean.ImgpicInfoBean();
                imgpicInfoBean.setLink(link);
                musicBean.setImgpic_info(imgpicInfoBean);
                final MusicOperationDialog musicOperationDialog = new MusicOperationDialog(musicBean, fragmentManager);
                musicOperationDialog.show(fragmentManager, helper.getPosition() + "");
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

    //跳转MV
    private void toMV(int id ,String mvurl,int uid,String title,String nickname,String imgpic_link) {
        Intent intent = new Intent(mContext, MvVideoActivitykt.class);
        Bundle bundle = new Bundle();
        bundle.putInt("mv", id);
        bundle.putString("mvurl", mvurl);
        bundle.putInt("uid", uid);
        bundle.putString("title", title);
        bundle.putString("nickname", nickname);
        bundle.putString("imgpic_link", imgpic_link);
        bundle.putString("bioashi", 1+ "");
        intent.putExtra("mvdate",bundle);
        mContext.startActivity(intent);
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
