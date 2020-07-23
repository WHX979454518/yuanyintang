package com.mxkj.yuanyintang.mainui.home.gift_charts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;

/**
 * Created by LiuJie on 2017/9/29.
 */

public class GiftChartsAdapter extends BaseMultiItemQuickAdapter<GiftChartsBean.DataBean.DataListBean, BaseViewHolder> {
    private int charts_type, time_type;

    public GiftChartsAdapter(List<GiftChartsBean.DataBean.DataListBean> data, int charts_type, int time_type) {
        super(data);
        addItemType(1, R.layout.item_gift_charts);
        addItemType(2, R.layout.item_gift_my_charts);
        this.charts_type = charts_type;
        this.time_type = time_type;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GiftChartsBean.DataBean.DataListBean item) {
        if (item == null) {
            return;
        }

        helper.setOnClickListener(R.id.ll_to_detial, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MusicIanDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, item.getMember_id() + "");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        helper.setText(R.id.tv_score, StringUtils.isEmpty(item.getTotal_desc() + ""));
        helper.setText(R.id.tv_order, String.valueOf(item.getScore()));
        helper.setText(R.id.tv_nickname, StringUtils.isEmpty(item.getNickname()));
        if (item.getHead_info() != null) {
            ImageLoader.with(mContext).getSize(65, 65).setUrl(item.getHead_info()).into(helper.getView(R.id.img_icon));
        }
        helper.setVisible(R.id.v_rz, item.getIs_music() == 3 ? true : false);
        ImageView iv_status = helper.getView(R.id.iv_order_status);
        if (charts_type == 4) {
            helper.setText(R.id.tv_type, "收益");
        } else {
            helper.setText(R.id.tv_type, "贡献");
        }
        if (time_type == 2) {
            iv_status.setVisibility(View.VISIBLE);
        } else {
            iv_status.setVisibility(View.GONE);
        }
        TextView tv_nick = helper.getView(R.id.tv_nickname);
        tv_nick.setTextColor(item.getIs_self() == 1 ? Color.parseColor("#00c9cc") : Color.parseColor("#1a1717"));
        if (item.getGrow_status() == 1) {
            iv_status.setImageResource(R.drawable.chart_order_up);
        } else if (item.getGrow_status() == -1) {
            iv_status.setImageResource(R.drawable.chart_order_down);
        } else {
            iv_status.setImageResource(R.drawable.charts_order_normal);
        }

//        helper.setVisible(R.id.tv_tips,false);
        TextView view = helper.getView(R.id.tv_tips);
        TextView tv_order = helper.getView(R.id.tv_order);
        if (view==null)return;
        int score = item.getScore();
        if (score>1000){//未上榜
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
            helper.setText(R.id.tv_order, String.valueOf(item.getScore()));
        }
    }
}
