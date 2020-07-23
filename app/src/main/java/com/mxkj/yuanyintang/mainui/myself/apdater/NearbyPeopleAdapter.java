package com.mxkj.yuanyintang.mainui.myself.apdater;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.mainui.myself.bean.NearbyPeopleBean;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.widget.MultiLineRadioGroup;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class NearbyPeopleAdapter extends BaseQuickAdapter<NearbyPeopleBean.DataBean, BaseViewHolder> {

    public NearbyPeopleAdapter(List<NearbyPeopleBean.DataBean> data) {
        super(R.layout.item_nearby_people, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final NearbyPeopleBean.DataBean item, int position) {
//        TODO  添加标签布局  获取标签数据，动态往Linearlayout加，之前removeAllViews
        MultiLineRadioGroup mTag = helper.getView(R.id.mulrg_tag);
        List<String> tag = item.getTag();
        if (tag != null) {
            for (int i = 0; i < tag.size(); i++) {
                if (i > 2) {
                    break;
                }
                mTag.insert(i, tag.get(i));
            }
        }
        ImageLoader.with(mContext)
                .getSize(200, 200)
                .override(50, 50)
                .url(item.getHead_link())
                .asCircle()
                .scale(ScaleMode.CENTER_CROP)
                .into(helper.<CircleImageView>getView(R.id.civ_headimg));
        if (item.getIs_music() == 3) {
            helper.setVisible(R.id.iv_is_vip, true);
        } else {
            helper.setVisible(R.id.iv_is_vip, false);
        }
        if (1 == item.getSex()) {
            helper.<ImageView>getView(R.id.iv_sex).setImageResource(R.drawable.icon_male);
            helper.setBackgroundRes(R.id.ll_age_sex, R.drawable.oval_3dp_blue_60_bg);
            helper.setVisible(R.id.iv_sex, true);
        } else if (0 == item.getSex()) {
            helper.<ImageView>getView(R.id.iv_sex).setImageResource(R.drawable.icon_female);
            helper.setBackgroundRes(R.id.ll_age_sex, R.drawable.oval_3dp_pink_99_bg);
            helper.setVisible(R.id.iv_sex, true);
        } else {
            helper.setVisible(R.id.iv_sex, false);
            helper.setBackgroundColor(R.id.ll_age_sex, Color.WHITE);
            helper.setBackgroundRes(R.id.ll_age_sex, R.drawable.oval_3dp_pink_fuck_bg);
        }

        helper.setText(R.id.tv_distance, item.getDistance() + "");
        helper.setText(R.id.tv_age, item.getDay() + "");
        if (item.getDay() == 0) {
            helper.setText(R.id.tv_age, "保密");
        }
        helper.setText(R.id.tv_name, StringUtils.isEmpty(item.getNickname()));
        helper.setText(R.id.tv_sign, StringUtils.isEmpty(item.getSignature()));
        RxView.clicks(helper.getView(R.id.civ_headimg))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MobclickAgent.onEvent(mContext,"home_near_musician");
                        Intent intent = new Intent(mContext, MusicIanDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, item.getUid() + "");
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });


    }
}
