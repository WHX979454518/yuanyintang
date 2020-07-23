package com.mxkj.yuanyintang.mainui.myself.my_income.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.BaseActivity;
import com.mxkj.yuanyintang.mainui.myself.my_income.bean.CashHistoryBean;
import com.mxkj.yuanyintang.mainui.myself.my_income.dialog.BotomCashTips;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class CashListAdapter extends BaseMultiItemQuickAdapter<CashHistoryBean.DataBean, BaseViewHolder> {
    public CashListAdapter(List<CashHistoryBean.DataBean> data) {
        super(data);
        addItemType(0, R.layout.item_cash_list);
        addItemType(1, R.layout.item_cash_list);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final CashHistoryBean.DataBean dataBean) {
        int status = dataBean.getStatus();
        baseViewHolder.setText(R.id.tv_status, StringUtils.isEmpty(dataBean.getStatus_desc()));
        NumberFormat nf = new DecimalFormat("#.##");
        String s = nf.format(dataBean.getFish_num());
        baseViewHolder.setText(R.id.tv_title, "提现" + s + "个小鱼干");
        baseViewHolder.setText(R.id.tv_content, StringUtils.isEmpty(dataBean.getRemark()));
        baseViewHolder.setText(R.id.tv_time, StringUtils.isEmpty(dataBean.getCreate_time()));
        TextView tv_status = baseViewHolder.getView(R.id.tv_status);
        TextView tv_status_desc = baseViewHolder.getView(R.id.tv_status_desc);
        switch (status) {
            case 0:
                tv_status.setTextColor(Color.parseColor("#ff7f00"));
                tv_status_desc.setVisibility(View.VISIBLE);
                tv_status_desc.setText(StringUtils.isEmpty(dataBean.getExamine_time()));
                break;
            case 1:
                tv_status_desc.setVisibility(View.GONE);
                tv_status.setTextColor(Color.parseColor("#5fb336"));
                break;
            case -1:
                tv_status_desc.setVisibility(View.GONE);
                tv_status.setTextColor(Color.parseColor("#ff4c55"));
                tv_status_desc.setVisibility(View.VISIBLE);
                tv_status_desc.setText("查看原因");
                tv_status_desc.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.tips_red), null);
                tv_status_desc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BotomCashTips botomCashTips = new BotomCashTips(StringUtils.isEmpty(dataBean.getReason()));
                        botomCashTips.show(((BaseActivity) mContext).getSupportFragmentManager(), "CASH");
                    }
                });
                break;
            case -2:
                tv_status_desc.setVisibility(View.GONE);

                tv_status.setTextColor(Color.parseColor("#9da6a4"));
                break;
            case 2:
                tv_status_desc.setVisibility(View.GONE);

                tv_status.setTextColor(Color.parseColor("#4ca6ff"));
                break;
        }
    }
}
