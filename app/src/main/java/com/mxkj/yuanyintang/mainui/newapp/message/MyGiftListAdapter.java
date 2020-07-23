package com.mxkj.yuanyintang.mainui.newapp.message;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zuixia on 2018/8/23.
 */

public class MyGiftListAdapter extends BaseQuickAdapter<MygiftListBean.DataBean, com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder> {
    public MyGiftListAdapter(List<MygiftListBean.DataBean> data) {
        super(R.layout.item_my_gift_list, data);
    }

    @Override
    protected void convert(com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder helper, final MygiftListBean.DataBean item, int position) {
        ImageLoader.with(mContext)
                .getSize(200, 200)
                .override(50, 50)
                .url(item.getImgpic_link())
                .asCircle()
                .scale(ScaleMode.CENTER_CROP)
                .into(helper.<CircleImageView>getView(R.id.civ_headimg));
        ImageLoader.with(mContext)
                .getSize(200, 200)
                .override(50, 50)
                .url(item.getIcon_link())
                .asCircle()
                .scale(ScaleMode.CENTER_CROP)
                .into(helper.<CircleImageView>getView(R.id.img_gift));

        helper.setText(R.id.tv_name, StringUtils.isEmpty(item.getNickname()));
        helper.setText(R.id.tv_desc, StringUtils.isEmpty(item.getCreate_time()+"  送出"+StringUtils.isEmpty(item.getGift_name()))+"(+"+item.getCoin_num()+"甜甜圈)");
    }
}
