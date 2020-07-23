package com.mxkj.yuanyintang.mainui.home.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.extraui.activity.PictureDetailsActivity;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.widget.MultiLineRadioGroup;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by LiuJie on 2018/1/9.
 */

public class SongSheetSynopsisActivity extends StandardUiActivity {

    @BindView(R.id.iv_bg)
    ImageView iv_bg;
    @BindView(R.id.iv_bg_img)
    ImageView iv_bg_img;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_synopsis)
    TextView tv_synopsis;
    @BindView(R.id.leftBT)
    Button leftButton;
    @BindView(R.id.mulrg_tag)
    MultiLineRadioGroup mulrg_tag;

    MusicIndex.ItemInfoListBean itemInfoBean;

    @Override
    public int setLayoutId() {
        return R.layout.activity_song_sheet_synopsis;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        hideTitle(true);
        ButterKnife.bind(this);
        StatusBarUtil.transparentStatusBar(this);
        leftButton.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.close_back_white), null, null, null);
        itemInfoBean = (MusicIndex.ItemInfoListBean) getIntent().getSerializableExtra("data");

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
                        if (null == itemInfoBean) {
                            return;
                        }
                        if (itemInfoBean.getImgpic_info() != null) {
                            Intent intent = new Intent(SongSheetSynopsisActivity.this, PictureDetailsActivity.class);
                            intent.putExtra("url", itemInfoBean.getImgpic_info().getLink());
                            startActivity(intent);
                        }
                    }
                });

        if (null == itemInfoBean) {
            return;
        }
        try {
            ImageLoader.with(this)
                    .getSize(400, 400)
                    .url(itemInfoBean.getImgpic_info().getLink())
                    .scale(ScaleMode.CENTER_CROP)
                    .blur(25)
                    .into(iv_bg);
            ImageLoader.with(this)
                    .getSize(400, 400)
                    .override(200, 200)
                    .url(itemInfoBean.getImgpic_info().getLink())
                    .scale(ScaleMode.CENTER_CROP)
                    .into(iv_bg_img);
        } catch (RuntimeException e) {
        }


        tv_title.setText(StringUtils.isEmpty(itemInfoBean.getTitle()) + " - " + itemInfoBean.getNickname());
        tv_synopsis.setText(StringUtils.isEmpty(itemInfoBean.getRemark()));
        List<MusicIndex.ItemInfoListBean.TagsBean> tags = itemInfoBean.getTags();
        if (tags == null) {
            return;
        }
        mulrg_tag.removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            mulrg_tag.insert(i, tags.get(i).getTitle());
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }
}
