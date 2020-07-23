package com.mxkj.yuanyintang.mainui.pond.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.extraui.activity.PicturePagerDetailsActivity;
import com.mxkj.yuanyintang.extraui.bean.PictureDataBean;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.mainui.home.data.Constant;
import com.mxkj.yuanyintang.mainui.pond.TagDetialPondList;
import com.mxkj.yuanyintang.mainui.pond.bean.PondBeanNew;
import com.mxkj.yuanyintang.mainui.pond.bean.PondTagBanner;
import com.mxkj.yuanyintang.mainui.pond.widget.FlowLayout;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.widget.NoScrollGridview;
import com.mxkj.yuanyintang.widget.SearTextView;
import com.mxkj.yuanyintang.widget.VerticalImageSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个adapter是因为后台接口改了结构（data以前是数组，现在是对象，下面多了一层dataList）
 * 之前的adapter在很多地方复用，但后台结构只在这一个接口里面改了，所以只能单独写一个adapter出来兼容
 * PondImgGridAdapterNew也是由于同样的原因
 */
public class PondRecyclerAdapterNew extends BaseMultiItemQuickAdapter<PondBeanNew.DataBean.DataListBean, BaseViewHolder> {
    private String tagTitle;
    private String topText;
    private PondTagBanner.DataBean.TagBeanX tag;
    boolean aBoolean;

    public PondRecyclerAdapterNew(List<PondBeanNew.DataBean.DataListBean> data, PondTagBanner.DataBean.TagBeanX tag, Boolean aBoolean) {
        super(data);
        this.aBoolean = aBoolean;
        topText = "//p顶";
        this.tag = tag;
        addItemType(Constant.PondItemType.TYPE_POND_MUTI_IMG, R.layout.pond_item_muti);//多张
        addItemType(Constant.PondItemType.TYPE_POND_NO_IMG, R.layout.pond_item_no);//没有图片
        addItemType(Constant.PondItemType.TYPE_POND_SINGLE_IMG, R.layout.pond_item_single);//单张
    }

    public PondRecyclerAdapterNew(List<PondBeanNew.DataBean.DataListBean> data, String title) {
        super(data);
        topText = "//p顶";
        this.tagTitle = title;
        if (tagTitle == null) {
            tagTitle = "";
        }
        topText = "//p顶";
        addItemType(Constant.PondItemType.TYPE_POND_MUTI_IMG, R.layout.pond_item_muti);//多张
        addItemType(Constant.PondItemType.TYPE_POND_NO_IMG, R.layout.pond_item_no);//没有图片
        addItemType(Constant.PondItemType.TYPE_POND_SINGLE_IMG, R.layout.pond_item_single);//单张
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final PondBeanNew.DataBean.DataListBean dataBean) {
        if (aBoolean) {
            if (viewHolder.getAdapterPosition() == 1) {
                viewHolder.getView(R.id.layout_pond_hot).setVisibility(View.VISIBLE);
            } else {
                viewHolder.getView(R.id.layout_pond_hot).setVisibility(View.GONE);
            }
        }
        /**
         * 多图
         * */
        if (viewHolder.getItemViewType() == Constant.PondItemType.TYPE_POND_MUTI_IMG) {
            commenData(viewHolder, dataBean);
            NoScrollGridview gridview = viewHolder.getView(R.id.pond_img_grid);
            PondImgGridAdapterNew adapter = new PondImgGridAdapterNew(dataBean, mContext);
            gridview.setAdapter(adapter);
        }
        /**
         * 无图
         *
         * */
        else if (viewHolder.getItemViewType() == Constant.PondItemType.TYPE_POND_NO_IMG) {
            commenData(viewHolder, dataBean);
        } /**
         * 单张
         * */
        else if (viewHolder.getItemViewType() == Constant.PondItemType.TYPE_POND_SINGLE_IMG) {
            commenData(viewHolder, dataBean);
            if (null != dataBean.getImglist_info() && dataBean.getImglist_info().size() > 0) {
                final List<String> imglist_link = new ArrayList<>();
                List<PondBeanNew.DataBean.DataListBean.ImglistInfoBean> imglist_info = dataBean.getImglist_info();
                for (int i = 0; i < imglist_info.size(); i++) {
                    imglist_link.add(imglist_info.get(i).getLink());
                }
                ImageLoader.with(mContext)
                        .override(115, 115)
                        .getSize(400, 400)
                        .url(imglist_link.get(0))
                        .error(R.drawable.img_errpr)
                        .scale(ScaleMode.CENTER_CROP)
                        .into(viewHolder.getView(R.id.img_pond));
                if (dataBean.getImglist_info().get(0).getExt().equals("gif")) {
                    viewHolder.getView(R.id.tv_long_pic).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.tv_long_pic, "动图");
                } else if (dataBean.getImglist_info().get(0).getIs_long().equals("1")) {//是长图
                    viewHolder.getView(R.id.tv_long_pic).setVisibility(View.VISIBLE);
                } else {//不是长图
                    viewHolder.getView(R.id.tv_long_pic).setVisibility(View.GONE);
                }

                viewHolder.setOnClickListener(R.id.img_pond, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        PictureDataBean pictureDataBean = new PictureDataBean()
                                .setId(dataBean.getId() + "")
                                .setCommentNum(dataBean.getHits())
                                .setPhotoList(imglist_link)
                                .setTitle(dataBean.getTitle())
                                .setNickname(dataBean.getNickname())
                                .setType("pond")
                                .setPosition(0)
                                .setHits(dataBean.getHits());
                        bundle.putSerializable(PicturePagerDetailsActivity.PICTURE_DATA, pictureDataBean);
                        Intent intent = new Intent(mContext, PicturePagerDetailsActivity.class);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });
            }

        } else if (viewHolder.getItemViewType() == Constant.PondItemType.TYPE_TAG) {
        }
    }

    private void commenData(BaseViewHolder viewHolder, final PondBeanNew.DataBean.DataListBean dataBean) {
        if (tag != null) {
            PondTagBanner.DataBean.TagBeanX.PondImgBean pond_img = tag.getPond_img();
            if (pond_img != null) {
                try {
                    String imgpic_link = pond_img.getImgpic_info().getLink();
                    Glide.with(mContext).load(imgpic_link + "/400/400/1/80?format=0").asBitmap().into((ImageView) viewHolder.getView(R.id.img_hot_pond));

                } catch (RuntimeException e) {
                }

            }
        }

        /**
         * 标签
         * */
        final List<PondBeanNew.DataBean.DataListBean.HashtagBean> hashtag = dataBean.getHashtag();
        String nickname = dataBean.getNickname();
        if (nickname == null) {
            nickname = "";
        }
/**
 * 点击头像和昵称
 *
 * */
        viewHolder.setOnClickListener(R.id.nickname, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MusicIanDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, dataBean.getUid() + "");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        viewHolder.setOnClickListener(R.id.img_icon, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MusicIanDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, dataBean.getUid() + "");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        String head_link = dataBean.getHead_link();
        if (dataBean.getIs_music() == 3) {
            viewHolder.setVisible(R.id.img_vip, true);
        } else {
            viewHolder.setVisible(R.id.img_vip, false);
        }
        int thcount = dataBean.getHits();
        viewHolder.setText(R.id.nickname, nickname)
                .setText(R.id.tv_comment_num, thcount + "");
        SearTextView sTv = viewHolder.getView(R.id.pond_title);
        if (dataBean.getRecommend() == 1) {
            SpannableStringBuilder ssb = new SpannableStringBuilder(topText + " " + StringUtils.isEmpty(dataBean.getTitle()));
            Drawable d = mContext.getResources().getDrawable(R.drawable.img_top);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            VerticalImageSpan imageSpan = new VerticalImageSpan(d);
//          用ImageSpan替换文本
            ssb.setSpan(imageSpan, 0, topText.length(), ImageSpan.ALIGN_BASELINE);
            sTv.setText(ssb);
        } else {
//以下用于搜索结果页面关键字标红
            if (!TextUtils.isEmpty(tagTitle)) {
                sTv.setSpecifiedTextsColor(StringUtils.isEmpty(dataBean.getTitle()), tagTitle, Color.parseColor("#ff6699"));
            } else {
                sTv.setText(StringUtils.isEmpty(dataBean.getTitle()));
            }
        }

        FlowLayout tagView = viewHolder.getView(R.id.pond_tag);
        tagView.removeAllViews();
        for (int i = 0; i < 3; i++) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.child_pond_tag, tagView, false);
            CheckBox ckTag = view.findViewById(R.id.ckTag);
            if (hashtag.size() > i) {
                ckTag.setText(hashtag.get(i).getTitle());
                if (hashtag.get(i).getTitle().equals(tagTitle)) {
                    ckTag.setTextColor(Color.parseColor("#ff6666"));
                    ckTag.setBackgroundResource(R.drawable.shape_bg_tagred);
                }
                final int finalI = i;
                ckTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        Intent intent = new Intent(mContext, TagDetialPondList.class);
                        PondBeanNew.DataBean.DataListBean.HashtagBean hashtagBean = hashtag.get(finalI);
                        Bundle bundle = new Bundle();
                        bundle.putInt(TagDetialPondList.TAG_ID, hashtagBean.getId());
                        bundle.putString(TagDetialPondList.TAG_TITLE, hashtagBean.getTitle());
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });
                tagView.addView(ckTag);
            }
            if (head_link != null) {
                ImageLoader.with(mContext)
                        .getSize(200, 200)

                        .override(25, 25)
                        .url(head_link)
                        .scale(ScaleMode.CENTER_CROP)
                        .asCircle()
                        .into(viewHolder.getView(R.id.img_icon));
            }
        }
    }
}
