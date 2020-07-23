package com.mxkj.yuanyintang.mainui.myself.my_income.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.data.Constant;
import com.mxkj.yuanyintang.mainui.myself.my_income.bean.IncomeListBean;
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by cpf on 2018/1/16.
 */

public class IncomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static Context mContext;
    public List<IncomeListBean.DataBean.MonthDataBean> mIncomeAdapterBeans = new ArrayList<>();

    public IncomeListAdapter(Context context, List<IncomeListBean.DataBean.MonthDataBean> incomeAdapterBeans) {
        this.mContext = context;
        this.mIncomeAdapterBeans = incomeAdapterBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.IncomeListItemType.CONTENT) {
            return new ContentVH(LayoutInflater.from(mContext).inflate(R.layout.item_income_list_content, parent, false));
        }
        return new TitleVH(LayoutInflater.from(mContext).inflate(R.layout.item_income_list_title, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IncomeListBean.DataBean.MonthDataBean incomeAdapterBean = mIncomeAdapterBeans.get(position);
        if (holder instanceof ContentVH) {
            ((ContentVH) holder).bindData(incomeAdapterBean);
        }
        if (holder instanceof TitleVH) {
            ((TitleVH) holder).bindData(incomeAdapterBean);
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

        TextView tv_month, tv_counts;

        public TitleVH(View itemView) {
            super(itemView);
            tv_month = itemView.findViewById(R.id.tv_month);
            tv_counts = itemView.findViewById(R.id.tv_counts);
            itemView.setTag(true);
        }

        public void bindData(IncomeListBean.DataBean.MonthDataBean IncomeAdapterBean) {
            tv_month.setText(IncomeAdapterBean.getMonth());
            tv_counts.setText("收入小鱼干" + IncomeAdapterBean.getMonth_total());
        }
    }

    static class ContentVH extends RecyclerView.ViewHolder {
        TextView tv_type_title, tv_type_content, tv_time, tv_status_desc, tv_status_num;
        CircleImageView img;

        public ContentVH(View itemView) {
            super(itemView);
            tv_type_title = itemView.findViewById(R.id.tv_type_title);
            tv_status_num = itemView.findViewById(R.id.tv_status_num);
            tv_status_desc = itemView.findViewById(R.id.tv_status_desc);
            tv_type_content = itemView.findViewById(R.id.tv_type_content);
            tv_time = itemView.findViewById(R.id.tv_time);
            img = itemView.findViewById(R.id.img);
            itemView.setTag(false);
        }

        public void bindData(IncomeListBean.DataBean.MonthDataBean incomeAdapterBean) {
            tv_type_title.setText(incomeAdapterBean.getReason());
            tv_type_content.setText(incomeAdapterBean.getRemark());
            tv_time.setText(incomeAdapterBean.getCreate_time());
            int from_type = incomeAdapterBean.getFrom_type();
            tv_status_desc.setVisibility(View.GONE);
            float num = incomeAdapterBean.getFish_num();
            DecimalFormat df = new DecimalFormat("0.00");
            String fish_num = df.format(new Double(num));
            HomeBean.ImgpicInfoBean head_info = incomeAdapterBean.getHead_info();
            if (head_info != null) {
                img.setTag(R.id.img,head_info.getLink());
                if (img.getTag() != null) {
                    if (head_info.getLink().equals(img.getTag(R.id.img))) {
                        ImageLoader.with(mContext).getSize(100, 100).setUrl(head_info).into(img);
                    }
                } else {
                    ImageLoader.with(mContext).getSize(100, 100).setUrl(head_info).into(img);
                }
            }

            switch (from_type) {
                /**fromtype:1,被打赏，2提现，3兑换成甜甜圈*/
                case 1:
                    tv_status_num.setText("+" + fish_num);
                    tv_status_num.setTextColor(Color.parseColor("#ff6699"));
                    break;
                case 2://提现
                    /**状态
                     * status
                     “0” => “队列中”,
                     “1” => “提现成功”,
                     “-1” => “提现失败”,
                     “-2” => “用户取消”,
                     “2” => “提现中”,*/
                    tv_status_desc.setVisibility(View.VISIBLE);
                    IncomeListBean.DataBean.MonthDataBean.CashInfoBean cash_info = incomeAdapterBean.getCash_info();
                    int status = cash_info.getStatus();
                    String icon = cash_info.getIcon();
                    String status_desc = cash_info.getStatus_desc();
                    tv_status_desc.setText(status_desc);
                    tv_status_num.setText(fish_num);
                    switch (status) {
                        case 0:
                            tv_status_desc.setTextColor(Color.parseColor("#ff7f00"));
                            break;
                        case 1:
                            tv_status_desc.setTextColor(Color.parseColor("#5fb336"));
                            tv_status_num.setTextColor(Color.parseColor("#191717"));

                            break;
                        case -1:
                            tv_status_num.setTextColor(Color.parseColor("#9da6a4"));
                            tv_status_desc.setTextColor(Color.parseColor("#ff4c55"));

                            break;
                        case -2:
                            tv_status_num.setTextColor(Color.parseColor("#9da6a4"));
                            tv_status_desc.setTextColor(Color.parseColor("#9da6a4"));

                            break;
                        case 2:
                            tv_status_desc.setTextColor(Color.parseColor("#tv_status_num"));
                            break;
                    }

                    break;
                case 3:
                    img.setImageResource(R.drawable.myincome_icon_charge);
                    tv_status_num.setText(fish_num + "");
                    tv_status_num.setTextColor(Color.parseColor("#191717"));
                    tv_status_num.setTextColor(Color.parseColor("#ff6699"));
                    break;
            }
        }

    }
}
