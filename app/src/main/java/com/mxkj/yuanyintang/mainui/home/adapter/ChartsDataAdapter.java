package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.BaseActivity;
import com.mxkj.yuanyintang.mainui.home.bean.HomeIndex;
import com.mxkj.yuanyintang.mainui.home.data.Constant;
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity;
import com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity;
import com.mxkj.yuanyintang.mainui.home.music_charts.bean.ChartsHomeBean;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;

import java.util.List;

import static com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.CHARTS_TYPE;
import static com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.EXPEN_CLASS_ID;
import static com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.INCOME_CLASS_ID;

public class ChartsDataAdapter extends BaseMultiItemQuickAdapter<HomeIndex.ItemInfoListBean.BillboardBean.BillboardsBean, BaseViewHolder> {

    public ChartsDataAdapter(List<HomeIndex.ItemInfoListBean.BillboardBean.BillboardsBean> data) {
        super(data);
        addItemType(Constant.HomeCharts.MUSIC, R.layout.home_item_charts);
        addItemType(Constant.HomeCharts.INCOME, R.layout.home_item_charts_income);
    }

    @Override
    protected void convert(BaseViewHolder helper, final HomeIndex.ItemInfoListBean.BillboardBean.BillboardsBean item) {
        if (item == null) {
            return;
        }
        final int type = item.getType();
        if (type == 1 || type == 2) {//音乐类型
            if (null != MediaService.bean && item.getMusic() != null) {
                if (MediaService.bean.getId() == item.getMusic().getId()) {
                    if (MediaService.mediaPlayer != null && MediaService.mediaPlayer.isPlaying()) {
                        helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_true));
                    } else {
                        helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_false));
                    }
                } else {
                    helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_false));
                }
            } else {
                helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_false));
            }
            helper.setOnClickListener(R.id.iv_player, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_true));
                    if (item != null && item.getMusic() != null) {
                        PlayCtrlUtil.INSTANCE.play(mContext, item.getMusic().getId(), 0);
                    } else {
                        if (mContext instanceof BaseActivity) {
                            ((BaseActivity) mContext).setSnackBar("当前歌曲错误，无法播放", "", R.drawable.icon_fails);
                        }
                    }
                }
            });
            helper.setOnClickListener(R.id.layout_click, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    ChartsHomeBean.DataBean dataBean = new ChartsHomeBean.DataBean();
                    dataBean.setTitle(item.getTitle());
                    dataBean.setType(item.getType());
                    dataBean.setId(item.getId());
                    if (item.getImgpic_info() != null) {
                        dataBean.setImgpic_link(item.getImgpic_info().getLink());
                    }
                    bundle.putSerializable(Constants.Other.CHARTS_BEAN, dataBean);
                    goActivity(ChartsListsActivity.class, bundle);
                }
            });

            if (!TextUtils.isEmpty(item.getImgpic_link())) {
                ImageLoader.with(mContext)
                        .getSize(230, 180)
                        .override(115, 90)
                        .rectRoundCorner(3)
                        .url(item.getIcon_link())
                        .res(R.drawable.nothing)
                        .scale(ScaleMode.CENTER_CROP)
                        .into(helper.getView(R.id.iv_charts_type));
            }

            if (item.getMusic() != null) {
                ImageLoader.with(mContext)
                        .getSize(100, 100)
                        .override(230, 180)
                        .rectRoundCorner(3)
                        .url(item.getMusic().getImgpic_link())
                        .res(R.drawable.nothing)
                        .scale(ScaleMode.CENTER_CROP)
                        .into(helper.getView(R.id.iv_charts_bck));
            }
            GradientDrawable myGrad = (GradientDrawable) helper.getView(R.id.rl_music_bck).getBackground();
            myGrad.setColor(Color.parseColor(TextUtils.isEmpty(item.getBorder_bg_color()) ? "#00000000" : "#" + item.getBorder_bg_color()));

        } else if (type == 3 || type == 4) {//收益类型
            final HomeIndex.ItemInfoListBean.BillboardBean.BillboardsBean.MemberBean member = item.getMember();
            if (member == null) {
                return;
            }
            if (item.getIcon_link() != null) {
                ImageLoader.with(mContext)
                        .getSize(230, 180)
                        .override(115, 90)
                        .rectRoundCorner(3)
                        .url(item.getIcon_link())
                        .res(R.drawable.nothing)
                        .scale(ScaleMode.CENTER_CROP)
                        .into(helper.getView(R.id.iv_charts_income_bck));
            }

            if (member.getHead_info() != null) {
                ImageLoader.with(mContext)
                        .getSize(200, 200)
                        .url(member.getHead_info().getLink())
                        .res(R.drawable.nothing)
                        .scale(ScaleMode.CENTER_CROP)
                        .asCircle()
                        .into(helper.getView(R.id.iv_charts_type));
            }


            helper.setOnClickListener(R.id.layout_click, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, GiftChartsActivity.class);
                    if (type == 3) {
                        intent.putExtra(EXPEN_CLASS_ID, item.getId());
                        intent.putExtra(INCOME_CLASS_ID, item.getToggle_class().getId());
                        intent.putExtra(CHARTS_TYPE, 3);
                    } else if (type == 4) {
                        intent.putExtra(INCOME_CLASS_ID, item.getId());
                        intent.putExtra(EXPEN_CLASS_ID, item.getToggle_class().getId());
                        intent.putExtra(CHARTS_TYPE, 4);

                    }
                    mContext.startActivity(intent);
                }
            });
        }
    }

    protected void goActivity(Class<?> mClass, Bundle bundle) {


        Intent intent = new Intent(mContext, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}
