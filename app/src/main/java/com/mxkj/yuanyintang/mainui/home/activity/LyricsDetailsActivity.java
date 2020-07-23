package com.mxkj.yuanyintang.mainui.home.activity;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.utils.file.SdUtil;
import com.mxkj.yuanyintang.musicplayer.lyric_remusic.DefaultLrcParser;
import com.mxkj.yuanyintang.musicplayer.lyric_remusic.LrcRow;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.extraui.activity.PictureDetailsActivity;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by LiuJie on 2017/10/24.
 */

public class LyricsDetailsActivity extends StandardUiActivity {

    @BindView(R.id.iv_bg)
    ImageView iv_bg;
    @BindView(R.id.iv_bg_img)
    ImageView iv_bg_img;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_lyrics)
    TextView tv_lyrics;
    @BindView(R.id.leftBT)
    Button leftButton;

    MusicIndex.ItemInfoListBean itemInfoBean;

    @Override
    public int setLayoutId() {
        return R.layout.activity_lyrics_details;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        StatusBarUtil.transparentStatusBar(this);
        ButterKnife.bind(this);
        hideTitle(true);
        itemInfoBean = (MusicIndex.ItemInfoListBean) getIntent().getSerializableExtra("data");
        leftButton.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.close_back_white), null, null, null);
        RxView.clicks(leftButton)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        finish();
                    }
                });
        RxView.clicks(iv_bg_img)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (null == iv_bg_img) {
                            return;
                        }
                        try {
                            Intent intent = new Intent(LyricsDetailsActivity.this, PictureDetailsActivity.class);
                            intent.putExtra("url", itemInfoBean.getImgpic_info().getLink());
                            startActivity(intent);
                        } catch (RuntimeException e) {
                        }
                    }
                });

        if (null == itemInfoBean) {
            return;
        }
        try {
            ImageLoader.with(this)
                    .override(0, 0)
                    .url(itemInfoBean.getImgpic_info().getLink())
                    .blur(25)
                    .scale(ScaleMode.CENTER_CROP)
                    .into(iv_bg);
            ImageLoader.with(this)
                    .override(0, 0)
                    .url(itemInfoBean.getImgpic_info().getLink())
                    .scale(ScaleMode.CENTER_CROP)
                    .into(iv_bg_img);
        } catch (RuntimeException e) {
        }

        tv_title.setText(StringUtils.isEmpty(itemInfoBean.getTitle()) + " - " + itemInfoBean.getNickname());
        List<LrcRow> lrcRows = getLrcRows();
        String lyricStr = "";
        if (lrcRows != null) {
            for (LrcRow lrc : lrcRows) {
                lyricStr += (lrc.getContent() + "\n");
            }
            tv_lyrics.setText(lyricStr);

        } else {
            if (!itemInfoBean.getLyrics().contains("[")) {
                tv_lyrics.setText(itemInfoBean.getLyrics());
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private List<LrcRow> getLrcRows() {
        List<LrcRow> rows = null;
        InputStream is = null;
        String arr = itemInfoBean.getLyrics();
        if (arr != null) {
            SdUtil.writeToSd("/yytmusic/lyrics", itemInfoBean.getTitle() + ".lrc", arr.getBytes());
        }
        try {
            is = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/yytmusic/lyrics/" + itemInfoBean.getTitle() + ".lrc");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is == null) {
                return null;
            }
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            rows = DefaultLrcParser.getIstance().getLrcRows(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

}
