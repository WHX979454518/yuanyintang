package com.mxkj.yuanyintang.mainui.myself.doughnut;

import android.graphics.Color;
import android.graphics.Paint;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseMultiItemQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by LiuJie on 2017/9/25.
 */

public class ChargeItemAdapter extends BaseMultiItemQuickAdapter<ChargeItemBean.DataBean.ListsBean, BaseViewHolder> {
    private int maxHasLoadPosition;

    public ChargeItemAdapter(List<ChargeItemBean.DataBean.ListsBean> data) {
        super(data);
        addItemType(0, R.layout.chargr_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChargeItemBean.DataBean.ListsBean item, int position) {
        if (maxHasLoadPosition < position) {
            maxHasLoadPosition = position;
        }
        NumberFormat nf = new DecimalFormat("#.##");

        if (item.getType() == 2) {//限时
            helper.getView(R.id.tv_charge_type).setBackgroundResource(R.drawable.charge_type_time);
            helper.setText(R.id.tv_charge_type, "限时");
            helper.setVisible(R.id.tv_old_price, true);
            helper.setText(R.id.tv_old_price, "￥" + nf.format(item.getPrice()));
            TextView tv_old_price = helper.getView(R.id.tv_old_price);
            tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv_old_price.getPaint().setAntiAlias(true);
            helper.setText(R.id.tv_price, "￥" + nf.format(item.getDiscount_price()));

        } else {
            helper.setVisible(R.id.tv_charge_type, false);
            helper.setText(R.id.tv_price, "￥" + nf.format(item.getPrice()));
        }
        helper.setText(R.id.dought_num, item.getCoin_num() + "个");
//        选中事件
        CheckBox checkBox = helper.getView(R.id.ck_box);
        TextView dought_num = helper.getView(R.id.dought_num);
        checkBox.setChecked(item.isChecked());
        if (item.isChecked()){
            dought_num.setTextColor(Color.parseColor("#ff6699"));
        }else{
            dought_num.setTextColor(Color.parseColor("#2b2b2b"));
        }
    }
}
