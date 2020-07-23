package com.mxkj.yuanyintang.mainui.pond.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.pond.bean.PondDetialBean;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LiuJie on 2017/10/12.
 */

public class VoteAdapter_PondDetial extends BaseAdapter {
    private List<PondDetialBean.DataBean.VoteBean> voteBeanList;
    private Context context;
    private PondDetialBean.DataBean data;

    public VoteAdapter_PondDetial(Context context, List<PondDetialBean.DataBean.VoteBean> voteBeanList, PondDetialBean.DataBean data) {
        this.context = context;
        this.voteBeanList = voteBeanList;
        this.data = data;
    }

    @Override
    public int getCount() {
        return voteBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.vote_item_pond_detial, null);
        ViewHolder holder = new ViewHolder(view);
        PondDetialBean.DataBean.VoteBean voteBean = voteBeanList.get(i);
        if (voteBean != null) {
            holder.tvVoteOptionName.setText(voteBean.getOptionname());
            holder.ckVote.setChecked(voteBean.isChecked());

            if(voteBean.isChecked() == true){
                holder.tvPercent.setTextColor(Color.parseColor("#ff6699"));
                holder.tvTotalVote.setTextColor(Color.parseColor("#ff6699"));
            }

            if (data.getIs_vote() == 1) {
                holder.progress.setProgress(voteBean.getVotenum());
                holder.progress.setMax(data.getSumvotenum());
                float v = (float) voteBean.getVotenum() / data.getSumvotenum() * 100;
                DecimalFormat fnum = new DecimalFormat("##0.0");
                String dd = fnum.format(v);
                holder.tvPercent.setText(dd + "%");
                holder.tvTotalVote.setText(voteBean.getVotenum() + "票");

//                holder.ckVote.setVisibility(View.GONE);
                Log.e("TAG", "getView: getVotenum()" + voteBean.getVotenum() + "getSumvotenum" + data.getSumvotenum());
            } else {//还没有投票，判断票型是不是可见。如果已投票，票型直接显示出来
//                if (data.getHide() == 2) {
//                    holder.progress.setVisibility(View.INVISIBLE);
//                    holder.tvPercent.setVisibility(View.INVISIBLE);
//                    holder.tvTotalVote.setVisibility(View.INVISIBLE);
//                } else {
                    holder.progress.setProgress(voteBean.getVotenum());
                    holder.progress.setMax(data.getSumvotenum());
                    float v = (float) voteBean.getVotenum() / data.getSumvotenum() * 100;
                    DecimalFormat fnum = new DecimalFormat("##0.00");
                    String dd = fnum.format(v);
                    if(dd.equals("NaN")){
                        holder.tvPercent.setText(0 + "%");
                    }else {
                        holder.tvPercent.setText(dd + "%");
                    }
                    holder.tvTotalVote.setText(voteBean.getVotenum() + "票");
//                }
//                holder.progress.setProgress(0);
//                holder.progress.setMax(0);
//                holder.tvTotalVote.setText("");
            }
        }
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_vote_optionName)
        TextView tvVoteOptionName;
        @BindView(R.id.ck_vote)
        CheckBox ckVote;
        @BindView(R.id.progress)
        ProgressBar progress;
        @BindView(R.id.tv_percent)
        TextView tvPercent;
        @BindView(R.id.tv_totalVote)
        TextView tvTotalVote;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
