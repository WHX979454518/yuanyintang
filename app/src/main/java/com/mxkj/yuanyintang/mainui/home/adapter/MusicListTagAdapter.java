package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.bean.MusicListTabBean;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;

/**
 * Created by LiuJie on 2017/9/22.
 */

public class MusicListTagAdapter extends BaseQuickAdapter<MusicListTabBean, BaseViewHolder> {

    private Context context;

    public MusicListTagAdapter(List<MusicListTabBean> data, Context context) {
        super(R.layout.item_musiclist_tag, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicListTabBean item, int position) {
        helper.setText(R.id.tv_label, "  " + StringUtils.isEmpty(item.getTitle()) + "  ");
        if (item.isCheck()) {
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(context, R.color.base_red));
            helper.<TextView>getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(context, R.drawable.bg_tag_select_check_true));
        } else {
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(context, R.color.color_66_text));
            helper.<TextView>getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(context, R.drawable.bg_tag_select_check_false));
        }
    }

}
