package com.mxkj.yuanyintang.mainui.home;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zwj on 2018/5/17.
 */
public class PartnerView extends LinearLayout {
    Context context;

    public PartnerView(Context context) {
        super(context);
        this.context = context;
    }

    public PartnerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setData(MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean memberBean, int i) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.partner_item_view, null);
        addView(rootView);
        if (memberBean == null) return;
        TextView tv_nick_name = rootView.findViewById(R.id.tv_nick_name);
        RelativeLayout rl_icon = rootView.findViewById(R.id.rl_icon);
        CircleImageView img_icon = rootView.findViewById(R.id.img_icon);
        ImageView v_rz = rootView.findViewById(R.id.v_rz);
//        TextView tv_type = rootView.findViewById(R.id.tv_type);
        if (i == 0) {
//            tv_type.setVisibility(VISIBLE);
        } else {
//            tv_type.setVisibility(GONE);
        }

        if (memberBean.getIs_music() != null && memberBean.getIs_music().equals("3")) {
            v_rz.setVisibility(VISIBLE);
        } else {
            v_rz.setVisibility(GONE);
        }
        tv_nick_name.setTextColor(memberBean.getUid() == 0 ? Color.parseColor("#1a1717") : Color.parseColor("#ff4c55"));
        tv_nick_name.setText(StringUtils.isEmpty(memberBean.getNickname()));
//        tv_type.setText(memberBean.getMusic_type() == null ? "" : memberBean.getMusic_type());
        if (memberBean.getHead_info() != null && memberBean.getHead_info().getLink() != null) {
            ImageLoader.with(context).getSize(100, 100).setUrl(memberBean.getHead_info()).into(img_icon);
        } else {
//            rl_icon.setVisibility(GONE);
        }
    }

}
