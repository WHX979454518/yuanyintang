package com.mxkj.yuanyintang.mainui.pond.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.extraui.AgreeAnimationUtil;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.mainui.login_regist.ForgetStepNew2;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.mainui.myself.my_income.bean.IncomeHomeBean;
import com.mxkj.yuanyintang.mainui.newapp.pond.PondInfo;
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew;
import com.mxkj.yuanyintang.mainui.pond.bean.PondBean;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.mainui.home.data.Constant;
import com.mxkj.yuanyintang.mainui.pond.TagDetialPondList;
import com.mxkj.yuanyintang.mainui.pond.bean.PondBeanNew;
import com.mxkj.yuanyintang.mainui.pond.bean.PondTagBanner;
import com.mxkj.yuanyintang.mainui.pond.widget.FlowLayout;
import com.mxkj.yuanyintang.extraui.activity.PicturePagerDetailsActivity;
import com.mxkj.yuanyintang.extraui.bean.PictureDataBean;
import com.mxkj.yuanyintang.utils.photopicker.widget.NoScrollRecyclerView;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.widget.NoScrollGridview;
import com.mxkj.yuanyintang.widget.SearTextView;
import com.mxkj.yuanyintang.widget.VerticalImageSpan;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class PondRecyclerAdapter extends BaseMultiItemQuickAdapter<PondBean.DataBean, BaseViewHolder> {
    private String tagTitle;
    private String topText;
    private PondTagBanner.DataBean.TagBeanX tag;
    boolean aBoolean;

    public PondRecyclerAdapter(List<PondBean.DataBean> data, PondTagBanner.DataBean.TagBeanX tag, Boolean aBoolean) {
        super(data);
        this.aBoolean = aBoolean;
        topText = "//p顶";
        this.tag = tag;
        addItemType(Constant.PondItemType.TYPE_POND_MUTI_IMG, R.layout.pond_item_muti);//多张
        addItemType(Constant.PondItemType.TYPE_POND_NO_IMG, R.layout.pond_item_no);//没有图片
        addItemType(Constant.PondItemType.TYPE_POND_SINGLE_IMG, R.layout.pond_item_single);//单张
    }

    public PondRecyclerAdapter(List<PondBean.DataBean> data, String title) {
        super(data);
        topText = "//p顶";
        this.tagTitle = title;
        if (tagTitle == null) {
            tagTitle = "";
        }
        topText = "//p顶";
        addItemType(Constant.PondItemType.TYPE_POND_MUTI_IMG, R.layout.pondrecycleradapter_item_muti);//多张
        addItemType(Constant.PondItemType.TYPE_POND_NO_IMG, R.layout.pondrecycleradapter_item_no);//没有图片
        addItemType(Constant.PondItemType.TYPE_POND_SINGLE_IMG, R.layout.pondrecycleradapter_item_single);//单张
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final PondBean.DataBean dataBean) {
        if (aBoolean) {
            if (viewHolder.getAdapterPosition() == 1) {
                viewHolder.getView(R.id.layout_pond_hot).setVisibility(View.VISIBLE);
            } else {
                viewHolder.getView(R.id.layout_pond_hot).setVisibility(View.GONE);
            }
        }
        viewHolder.setText(R.id.tvTime,dataBean.getCreate_time());
        /**
         * 多图
         * */
        if (viewHolder.getItemViewType() == Constant.PondItemType.TYPE_POND_MUTI_IMG) {
            commenData(viewHolder, dataBean);
            NoScrollGridview gridview = viewHolder.getView(R.id.pond_img_grid);
            PondImgGridAdapter adapter = new PondImgGridAdapter(dataBean, mContext);
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
                List<PondBean.DataBean.ImglistInfoBean> imglist_info = dataBean.getImglist_info();
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

        agree(viewHolder, dataBean);
    }

    private void commenData(final BaseViewHolder viewHolder, final PondBean.DataBean dataBean) {
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
        final List<PondBean.DataBean.HashtagBean> hashtag = dataBean.getHashtag();
        String nickname = dataBean.getNickname();
        if (nickname == null) {
            nickname = "";
        }


        viewHolder.setText(R.id.tvShareNum, StringUtils.isEmpty(dataBean.getShare_counts_text()));
        viewHolder.setText(R.id.tvCommentNum, StringUtils.isEmpty(dataBean.getThcount_text()));
        viewHolder.setText(R.id.tvAgreeNum, StringUtils.isEmpty(dataBean.getAgrees_text()));
        TextView shaarenumber = viewHolder.getView(R.id.tvShareNum);
        TextView commentsnumber = viewHolder.getView(R.id.tvCommentNum);
        TextView agreenumber = viewHolder.getView(R.id.tvAgreeNum);
        if(shaarenumber.getText().equals("0") || shaarenumber.getText() == "0"){
            shaarenumber.setText("分享");
        }
        if(commentsnumber.getText().equals("0") || commentsnumber.getText() == "0"){
            commentsnumber.setText("评论");
        }
        if(agreenumber.getText().equals("0") || agreenumber.getText() == "0"){
            agreenumber.setText("点赞");
        }

        /**
         * 分享
         * */
        viewHolder.setOnClickListener(R.id.llShare, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(mContext,"pond_share");
                if (CacheUtils.INSTANCE.getBoolean(mContext, Constants.User.IS_LOGIN, false)) {
                    TextView tvshareNum = viewHolder.getView(R.id.tvShareNum);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //自己想做的事情
                            viewHolder.setText(R.id.tvShareNum, (dataBean.getShare_counts_text() + 1).toString());
                        }
                    },4000);
                    MusicBean musicBean = new MusicBean();
                    MusicBean.ShareDataBean shareDataBean = new MusicBean.ShareDataBean();
                    shareDataBean.setType("pond");
                    shareDataBean.setNickname(dataBean.getNickname());
                    shareDataBean.setTopicContent(dataBean.getContent());
                    shareDataBean.setTitle(dataBean.getTitle() + "");
                    //新加的有mv的显示mv
//                    shareDataBean.setMv(dataBean.item_info.mv + "");
//                    Log.e("yyyyy",""+dataBean.item_info.mv)
//                    shareDataBean.is_self(dataBean.getIs_self()+"");
                    Log.e("yyyyy",""+dataBean.getIs_self());
                    if (dataBean.getItemType() == 1) {
                        shareDataBean.setMuisic_id(dataBean.getId());
                    }else{
                        shareDataBean.setMuisic_id(dataBean.getId());
                    }
                    if (dataBean.getImglist_info() != null && dataBean.getImglist_info().size() > 0) {
                        shareDataBean.setWebImgUrl(dataBean.getImglist_info().get(0).getLink());
                        shareDataBean.setImage_link(dataBean.getImglist_info().get(0).getLink());
                    }
////                val shareUrl = ApiAddress.H5_BASE_URL + "topic/detail?id=" + dataBean.id
                    shareDataBean.setShareUrl(dataBean.getShare_url());
                    musicBean.setShareDataBean(shareDataBean);
                    ShareBottomDialog shareBottomDialog = new ShareBottomDialog(mContext, musicBean);
                    shareBottomDialog.show();
                } else {
                    mContext.startActivity(new Intent(mContext, LoginRegMainPage.class));
                }
            }
        });
        /**
         * 评论
         * */
        viewHolder.setOnClickListener(R.id.llComment, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(PondDetialActivityNew.PID, dataBean.getId());
                Intent intent = new Intent(mContext, PondDetialActivityNew.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });


/**
 * 点击头像和昵称
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
                        PondBean.DataBean.HashtagBean hashtagBean = hashtag.get(finalI);
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

    private void agree(final BaseViewHolder helper, final PondBean.DataBean dataBean) {
        final ImageView img_agree = helper.getView(R.id.img_agree);
        final TextView tv_agree_num = helper.getView(R.id.tvAgreeNum);
        Log.e("uuuuuu",""+dataBean.getIs_agree());
        if (dataBean.getIs_agree() == 1) {
            img_agree.setImageResource(R.drawable.icon_agreed);
            tv_agree_num.setText(dataBean.getAgrees());
        } else {
            img_agree.setImageResource(R.drawable.icon_disagree);
        }
        /**
         * 点赞
         * */
        helper.setOnClickListener(R.id.llAgree, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(mContext,"pond_agree");
                if (dataBean.getIs_agree() == 0) {
                    AgreeAnimationUtil.INSTANCE.setAnim(mContext, (ImageView) helper.getView(R.id.ani_agree), img_agree, AgreeAnimationUtil.COMMENT_AGREE);
                }
                HttpParams params = new HttpParams();
                params.put("type", 5+"");
                params.put("item_id", dataBean.getId()+ "");
                NetWork.INSTANCE.agree(mContext,params, new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
                        try {
                        JSONObject jsonObject = JSON.parseObject(resultData);
                        int code = jsonObject.getInteger("code");
                        if (code == 200) {
                            if (dataBean.getIs_agree() == 1) {
                                img_agree.setImageResource(R.drawable.icon_disagree);
                                tv_agree_num.setText(dataBean.getAgrees() - 1);
                                dataBean.setIs_agree(0);
                                dataBean.setAgrees(dataBean.getAgrees() - 1);
                                tv_agree_num.setTextColor(Color.parseColor("#616366"));
                            } else {
                                img_agree.setImageResource(R.drawable.icon_agreed);
                                tv_agree_num.setText(dataBean.getAgrees() + "");
                                dataBean.setIs_agree(1);
                                dataBean.setAgrees(1 + dataBean.getAgrees());
                                tv_agree_num.setTextColor(Color.parseColor("#ff6699"));
                            }
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void doError(String msg) {

                    }

                    @Override
                    public void doResult() {

                    }
                });
            }
        });

    }

}
