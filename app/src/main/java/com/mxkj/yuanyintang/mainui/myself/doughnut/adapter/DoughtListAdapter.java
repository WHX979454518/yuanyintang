package com.mxkj.yuanyintang.mainui.myself.doughnut.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.data.Constant;
import com.mxkj.yuanyintang.mainui.myself.bean.MyDoughnutBean;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by cpf on 2018/1/16.
 */

public class DoughtListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context mContext;
    public List<MyDoughnutBean.DataBeanX.DataBean.MonthDataBean> mIncomeAdapterBeans = new ArrayList<>();

    public DoughtListAdapter(Context context, List<MyDoughnutBean.DataBeanX.DataBean.MonthDataBean> incomeAdapterBeans) {
        this.mContext = context;
        this.mIncomeAdapterBeans = incomeAdapterBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.IncomeListItemType.CONTENT) {
            return new ContentVH(LayoutInflater.from(mContext).inflate(R.layout.item_my_doughnut, parent, false));
        }
        return new TitleVH(LayoutInflater.from(mContext).inflate(R.layout.item_income_list_title, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mIncomeAdapterBeans.size()<=position)return;
        MyDoughnutBean.DataBeanX.DataBean.MonthDataBean monthDataBean= mIncomeAdapterBeans.get(position);

        if (holder instanceof ContentVH) {
            ((ContentVH) holder).bindData(monthDataBean);
        }
        if (holder instanceof TitleVH) {
            ((TitleVH) holder).bindData(monthDataBean);
        }
    }

    @Override
    public int getItemCount() {
        return mIncomeAdapterBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mIncomeAdapterBeans.get(position).getItemType();
    }

    static class TitleVH extends RecyclerView.ViewHolder {

        TextView tv_month;

        public TitleVH(View itemView) {
            super(itemView);
            tv_month = itemView.findViewById(R.id.tv_month);
            itemView.setTag(true);
        }

        public void bindData(MyDoughnutBean.DataBeanX.DataBean.MonthDataBean IncomeAdapterBean) {
            tv_month.setText(IncomeAdapterBean.getMonth());
        }
    }

    static class ContentVH extends RecyclerView.ViewHolder {
        TextView tv_title, tv_name,tv_num,tv_time;
        CircleImageView img;

        public ContentVH(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_num = itemView.findViewById(R.id.tv_num);
            tv_time = itemView.findViewById(R.id.tv_time);
            itemView.setTag(false);
        }

        public void bindData(MyDoughnutBean.DataBeanX.DataBean.MonthDataBean dataBean) {
            tv_title.setText(dataBean.getReason());
            tv_name.setText(dataBean.getRemark());
            tv_num.setText(dataBean.getCoin_num()>0?"+"+dataBean.getCoin_num():dataBean.getCoin_num()+"");
            tv_time.setText(dataBean.getCreate_time()+"");
            tv_num.setTextColor(dataBean.getCoin_num()>0?Color.parseColor("#ff6699"):Color.parseColor("#191717"));
        }

    }
}
