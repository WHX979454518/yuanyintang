package com.mxkj.yuanyintang.musicplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.BaseActivity;
import com.mxkj.yuanyintang.musicplayer.activity.ChooseLyricActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LiuJie on 2017/10/31.
 */

public class ChooseLyricAdapter extends BaseAdapter {
    private List<ChooseLyricActivity.ChooseLyricBean> chooseLyricBeanList;
    Context context;
    Activity activity;
    private int totalCout = 0;
    private static final int MAX_COUNT = 50;

    public ChooseLyricAdapter(List<ChooseLyricActivity.ChooseLyricBean> chooseLyricBeanList, Context context, Activity activity) {
        this.chooseLyricBeanList = chooseLyricBeanList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return chooseLyricBeanList.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_choose_lyrics, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final ChooseLyricActivity.ChooseLyricBean chooseLyricBean = chooseLyricBeanList.get(i);
        if (chooseLyricBean != null) {
            holder.tvContent.setText(chooseLyricBean.getText().trim());
            holder.ckChooseLyric.setChecked(chooseLyricBean.isChecked());
            if (chooseLyricBean.isChecked() == true) {
                holder.img_choose.setVisibility(View.VISIBLE);
                holder.llLyricItem.setBackgroundColor(Color.parseColor("#f7f7f7"));
                holder.tvContent.setTextColor(Color.parseColor("#ff6699"));
            } else {
                holder.img_choose.setVisibility(View.INVISIBLE);
                holder.llLyricItem.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.tvContent.setTextColor(Color.parseColor("#9da6a4"));
            }
        }
        holder.llLyricItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chooseLyricBeanList.get(i).isChecked() == false) {
                    if (totalCout < MAX_COUNT) {
                        totalCout++;
                        chooseLyricBeanList.get(i).setChecked(true);
                    } else {
                        //最多
                        if (activity instanceof BaseActivity) {
                            ((BaseActivity) activity).setSnackBar("最多只能选择" + MAX_COUNT + "句歌词", "", R.drawable.icon_good);
                        }
                    }
                } else {
                    chooseLyricBeanList.get(i).setChecked(false);
                    totalCout--;
                }
                notifyDataSetChanged();

            }
        });


        return view;
    }

    public List<ChooseLyricActivity.ChooseLyricBean> getDataList() {
        if (chooseLyricBeanList != null) {
            return chooseLyricBeanList;
        }
        return null;
    }

    class ViewHolder {
        @BindView(R.id.ck_chooseLyric)
        CheckBox ckChooseLyric;
        @BindView(R.id.img_choose)
        ImageView img_choose;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.ll_lyric_item)
        RelativeLayout llLyricItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
