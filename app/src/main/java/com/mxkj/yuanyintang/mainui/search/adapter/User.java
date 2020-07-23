package com.mxkj.yuanyintang.mainui.search.adapter;

import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.widget.SearTextView;

import java.util.List;

/**
 * Created by LiuJie on 2017/10/13.
 */

public class User extends BaseMultiItemQuickAdapter<UserInfo.DataBean, BaseViewHolder> {
    private final String key;

    public User(List<UserInfo.DataBean> data, String key) {
        super(data);
        this.key = key;
        addItemType(0, R.layout.user_searchresult_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfo.DataBean item) {

        ImageLoader.with(mContext)
                .override(45, 45)
                .url(item.getHead_link())
                .scale(ScaleMode.CENTER_CROP)
                .asCircle()
                .into(helper.getView(R.id.img_song));
        SearTextView searTextView = helper.getView(R.id.songname);
        SearTextView searTextView2 = helper.getView(R.id.intro);

        TextView fans = helper.getView(R.id.fans);
        TextView latest_new = helper.getView(R.id.latest_new);



        if (item.getNickname() != null && key != null) {
            searTextView.setSpecifiedTextsColor(item.getNickname(), key, Color.parseColor("#ff6699"));
            searTextView2.setSpecifiedTextsColor(item.getNickname(), key, Color.parseColor("#ff6699"));
//            searTextView2.setText(StringUtils.isEmpty(item.getSignature()));
        } else {
            searTextView.setText(StringUtils.isEmpty(item.getNickname()));
            searTextView2.setText(StringUtils.isEmpty(item.getSignature()));
        }

        fans.setText(StringUtils.isEmpty("粉丝:"+item.getFans_num()));
        if(null!=item.getMusic()){
            if(null != item.getMusic().get(0).getTitle()){
                latest_new.setText(StringUtils.isEmpty("最新作品 《"+item.getMusic().get(0).getTitle()+"》"));
            }else {
                latest_new.setText(StringUtils.isEmpty("最新作品 《暂无最新作品》"));
            }

        }

        if (1 == item.getSex()) {
            ((ImageView)helper.getView(R.id.iv_sex)).setImageResource(R.drawable.icon_male);
            helper.setBackgroundRes(R.id.iv_sex, R.drawable.oval_3dp_blue_60_bg);
            helper.setVisible(R.id.iv_sex, true);
        } else if (0 == item.getSex()) {
            helper.<ImageView>getView(R.id.iv_sex).setImageResource(R.drawable.icon_female);
            helper.setBackgroundRes(R.id.iv_sex, R.drawable.oval_3dp_pink_99_bg);
            helper.setVisible(R.id.iv_sex, true);
        } else {
            helper.setVisible(R.id.iv_sex, false);
        }
        if (item.getIs_music() == 3) {
            helper.setVisible(R.id.iv_is_vip, true);
        } else {
            helper.setVisible(R.id.iv_is_vip, false);
        }
    }
}
