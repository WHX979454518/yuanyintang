package com.mxkj.yuanyintang.mainui.search.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.search.SheetBean;
import com.mxkj.yuanyintang.widget.SearTextView;

import java.util.List;

/**
 * Created by LiuJie on 2017/10/13.
 */

public class Sheet extends BaseMultiItemQuickAdapter<SheetBean.DataBean, BaseViewHolder> {
    private final String key;

    public Sheet(List<SheetBean.DataBean> data, String key) {
        super(data);
        this.key = key;
        addItemType(0, R.layout.sheet_searchresult_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, SheetBean.DataBean item) {

//        ImageLoader.with(mContext)
//                .getSize(200,200)
//
//                .override(45, 45)
//                .url(item.getImgpic_info().getLink())
//                .asCircle()
//                .error(R.drawable.nothing)
//                .into(helper.getView(R.id.img_song));
        if (item.getImgpic_info() != null) {
            ImageLoader
                    .with(mContext).url(item.getImgpic_info().getLink())
                    .placeHolder(R.drawable.nothing_no_txt)
                    .getSize(200, 200)
                    .into(helper.getView(R.id.img_song));
        }else{
            ImageLoader
                    .with(mContext)
                    .res(R.drawable.nothing_no_txt)
                    .into(helper.getView(R.id.img_song));
        }
        if (item.getTitle() != null && key != null) {
            SearTextView searTextView = helper.getView(R.id.songname);
            searTextView.setSpecifiedTextsColor(item.getTitle(), key, Color.parseColor("#ff6699"));
//            searTextView.setText(item.getTitle());
        }
        //TODO  缺少音乐总数
//        helper.setText(R.id.total_musicNum, item.getCounts() + "").setText(R.id.total_playNum, item.getCounts() + "");
        helper.setText(R.id.tv_num, item.getTotal() + "首,播放" + item.getCounts() + "次");

    }
}
