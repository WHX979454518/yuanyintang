package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.bean.MemberGiftListBean;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zuixia on 2018/5/17.
 */

public class SwitcherAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private final Context mContext;
    private final List<MemberGiftListBean.DataBean.DataListBean> mData;

    public SwitcherAdapter(Context context, List<MemberGiftListBean.DataBean.DataListBean> list) {
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_switcher_gift_list, parent, false);
        BaseViewHolder holder = new BaseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (mData.get(position) == null) return;
        TextView tv_position = holder.getView(R.id.tv_position);
        ImageView img_position = holder.getView(R.id.img_position);
        switch (position) {
            case 0:
                img_position.setVisibility(View.VISIBLE);
                tv_position.setVisibility(View.GONE);
                img_position.setImageResource(R.drawable.gift_number1);
                break;
            case 1:
                img_position.setVisibility(View.VISIBLE);
                tv_position.setVisibility(View.GONE);
                img_position.setImageResource(R.drawable.gigt_number2);
                break;
            case 2:
                img_position.setVisibility(View.VISIBLE);
                tv_position.setVisibility(View.GONE);
                img_position.setImageResource(R.drawable.gift_number3);
                break;
            default:
                img_position.setVisibility(View.GONE);
                tv_position.setVisibility(View.VISIBLE);
                tv_position.setText((position + 1) + "");

                break;
        }


        MemberGiftListBean.DataBean.DataListBean dataBean = mData.get(position);
        holder.setText(R.id.nick_name, dataBean.getNickname());
        TextView nick_name = holder.getView(R.id.nick_name);
        nick_name.setTextColor(dataBean.getIs_self() == 1 ? Color.parseColor("#00c9cc") : Color.parseColor("#1a1717"));

//        TODO  礼物数量字段
        holder.setText(R.id.gift_name, "送出" + dataBean.getCounts() + "件礼物");
        CircleImageView img_icon = holder.getView(R.id.img_icon);
        if (dataBean.getHead_link() != null) {
            ImageLoader.with(mContext).getSize(100, 100).url(dataBean.getHead_link()).into(img_icon);
        }
//        反正有些地方isMusic是数字有些地方是字符串，后台是这样返回的，
        if (dataBean.getIs_music() == 3) {
            holder.setVisible(R.id.v_rz, true);
        } else {
            holder.setVisible(R.id.v_rz, false);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}