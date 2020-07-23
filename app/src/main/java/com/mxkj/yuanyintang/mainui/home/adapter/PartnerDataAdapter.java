package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.MultiLinePartnerGroup;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;

import java.util.List;

/**
 * Created by LiuJie on 2017/9/19.
 */

public class PartnerDataAdapter extends BaseQuickAdapter<MusicIndex.ItemInfoListBean.MemberBeanX, BaseViewHolder> {
    public PartnerDataAdapter(List<MusicIndex.ItemInfoListBean.MemberBeanX> data) {
        super(R.layout.my_partner_layout, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MusicIndex.ItemInfoListBean.MemberBeanX item, final int position) {
        MultiLinePartnerGroup multiLinePartnerGroup = helper.getView(R.id.grop_partner);
        final List<MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean> member = item.getMember();
        for (int i = 0; i < member.size(); i++) {
            multiLinePartnerGroup.insert(i, member.get(i));
        }
        TextView view = helper.getView(R.id.tv_type);
        view.setVisibility(View.VISIBLE);
        helper.setText(R.id.tv_type, item.getMusic_type() + ": ");
        multiLinePartnerGroup.setOnCheckChangedListener(new MultiLinePartnerGroup.OnClickedListener() {
            @Override
            public void onItemClicked(MultiLinePartnerGroup group, int position) {
                MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean memberBean = member.get(position);
                if (memberBean.getUid() != 0) {
                    Intent intent = new Intent(mContext, MusicIanDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, item.getMember().get(position).getUid() + "");
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });
    }
}
