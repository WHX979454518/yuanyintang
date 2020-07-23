package com.mxkj.yuanyintang.mainui.pond.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.widget.SearTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleCheckListAdapter extends BaseAdapter {
    private List<MusicInfo.DataBean> entities = new ArrayList<>();
    private String setSpecialText;
    private int count;

    public SingleCheckListAdapter() {
    }

    public void setMaxCount(int count) {
        this.count = count;
    }

    public List<MusicInfo.DataBean> getEntities() {
        return entities;
    }

    public void setEntities(List<MusicInfo.DataBean> entities) {
        this.entities = entities;
        notifyDataSetChanged();
    }

    public void setSpecialText(String setSpecialText) {
        this.setSpecialText = setSpecialText;
    }

    @Override
    public int getCount() {
        if (entities != null && entities.size() > 0) {
            if (count != 0) {
                if (count > 50) {
                    return 50;
                } else {
                    return count;
                }
            }
            return entities.size();
        }
        return 0;
    }

    @Override
    public MusicInfo.DataBean getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addmusic_tonewsheet, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (entities.size()>position){
            MusicInfo.DataBean entity = entities.get(position);
            if (setSpecialText != null) {
                holder.tvTitle.setSpecifiedTextsColor(entity.getTitle(), setSpecialText, Color.parseColor("#ff6699"));
                holder.nickname.setSpecifiedTextsColor(entity.getNickname(), setSpecialText, Color.parseColor("#ff6699"));
            } else {
                holder.tvTitle.setText(entity.getTitle());
                holder.nickname.setText(entity.getNickname());
            }
            if (entity.isChecked()) {
                holder.cbSelect.setChecked(true);
            } else {
                holder.cbSelect.setChecked(false);
            }
        }

        return convertView;
    }

    public void updateStatus(@Nullable MusicInfo.DataBean entity) {
        if (entities == null || entity == null) {
            return;
        }
        for (int i = 0; i < entities.size(); i++) {
            if (entity.getId() == entities.get(i).getId()) {
                entities.get(i).setChecked(true);
            } else {
                entities.get(i).setChecked(false);
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @Nullable
        @BindView(R.id.cb_select)
        CheckBox cbSelect;
        @Nullable
        @BindView(R.id.songname)
        SearTextView tvTitle;
        @BindView(R.id.singer)
        SearTextView nickname;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
