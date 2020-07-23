package com.mxkj.yuanyintang.mainui.myself.my_release.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;

/**
 * Created by zwj on 2018/5/17.
 */

public class AddPartnerView extends LinearLayout {
    Context context;

    public AddPartnerView(Context context) {
        super(context);
        this.context = context;
    }

    public AddPartnerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setData(MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean memberBean) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.partner_item_add, null);
        addView(rootView);
        if (memberBean == null) return;
        TextView tv_name = rootView.findViewById(R.id.tv_name);
        LinearLayout ll_main_view = rootView.findViewById(R.id.ll_main_view);
        ImageView iv_del_item = rootView.findViewById(R.id.iv_del_item);
        TextView tv_divider = rootView.findViewById(R.id.tv_divider);
        tv_name.setText(memberBean.getNickname());
        if (memberBean.getNickname().equals("+添加用户")){
            tv_name.setTextColor(Color.parseColor("#ff6699"));
            ll_main_view.setBackgroundColor(Color.WHITE);
            iv_del_item.setVisibility(GONE);
            tv_divider.setVisibility(GONE);
        }else{
            if (memberBean.getUid()==0){
                tv_name.setTextColor(Color.parseColor("#ff6699"));
            }else{
                tv_name.setTextColor(Color.parseColor("#ff6699"));

            }

        }

    }
}
