package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.bean.HomeIndex;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;

import java.util.List;

/**
 * Created by LiuJie on 2017/10/26.
 */
public class PondPhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    Context context;
    List<HomeIndex.ItemInfoListBean.TopicBean.ImglistInfoBean> imglist_info;
    public PondPhotoAdapter(List<HomeIndex.ItemInfoListBean.TopicBean.ImglistInfoBean> imglist_info, List<String> data, Context context) {
        super(R.layout.homerecycler_pond_item_photo, data);
        this.context = context;
        this.imglist_info = imglist_info;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item, int position) {
        Glide.with(mContext)
                .load(item+ "/400/400/3/80?format=0")
                .asBitmap()
                .into((ImageView) helper.getView(R.id.iv_img));

        if (position == 2) {
            helper.setVisible(R.id.tv_more, true);
            if (getData().size() - 2 > 0) {
                helper.setText(R.id.tv_more, "查看更多\n" + (getData().size() - 3) + "张");
            } else {
                helper.setText(R.id.tv_more, "查看更多");
            }
        } else {
            helper.setVisible(R.id.tv_more, false);
            if (imglist_info != null && imglist_info.size() > position) {
                if (imglist_info.get(position).getExt().equals("gif")) {
                    helper.getView(R.id.tv_long_pic).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_long_pic, "动图");
                } else if (imglist_info.get(position).getIs_long().equals("1")) {
                    helper.getView(R.id.tv_long_pic).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.tv_long_pic).setVisibility(View.GONE);
                }
            }
        }

    }

    @Override

    public int getItemCount() {
        if (imglist_info != null && imglist_info.size() > 0) {
            if (imglist_info.size() > 3) {
                return 3;
            } else {
                return imglist_info.size();
            }
        }
        return 0;
    }

}
