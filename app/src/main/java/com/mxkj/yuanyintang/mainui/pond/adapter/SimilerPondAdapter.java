package com.mxkj.yuanyintang.mainui.pond.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew;
import com.mxkj.yuanyintang.mainui.pond.bean.PondDetialBean;

import java.util.List;

/**
 * Created by LiuJie on 2017/10/12.
 */

public class SimilerPondAdapter extends BaseMultiItemQuickAdapter<PondDetialBean.DataBean.RecommendBean, BaseViewHolder> {
    public SimilerPondAdapter(List<PondDetialBean.DataBean.RecommendBean> data) {
        super(data);
        addItemType(0, R.layout.item_similer_pond);
    }

    @Override
    protected void convert(BaseViewHolder helper, final PondDetialBean.DataBean.RecommendBean dataBean) {
        helper.setOnClickListener(R.id.rl_similar, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: ");
                Intent intent = new Intent(mContext, PondDetialActivityNew.class);
                intent.putExtra("pid", dataBean.getId());
                mContext.startActivity(intent);
            }
        });

        if (dataBean.getTitle() != null) {
            helper.setText(R.id.tv_pond_title, dataBean.getTitle());
        }
        if (dataBean.getImglist_info() != null && dataBean.getImglist_info().size() > 0) {
            String s = dataBean.getImglist_info().get(0).getLink();
            ImageLoader.with(mContext)
                    .url(s)
                    .scale(ScaleMode.CENTER_CROP)
                    .into(helper.getView(R.id.img_pond));
        }
    }
}
