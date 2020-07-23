package com.mxkj.yuanyintang.mainui.emotionkeyboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.utils.EmotionUtils;

import java.util.List;

public class EmotionGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> emotionNames;
    private int itemWidth;
    private String emotion_map_type;

    public EmotionGridViewAdapter(Context context, List<String> emotionNames, int itemWidth, String emotion_map_type) {
        this.context = context;
        this.emotionNames = emotionNames;
        this.itemWidth = itemWidth;
        this.emotion_map_type = emotion_map_type;
    }

    @Override
    public int getCount() {
        return emotionNames.size();
    }

    @Override
    public String getItem(int position) {
        return emotionNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_emotion_item, null);
            holder.img_emotioin = convertView.findViewById(R.id.img_emotion);
            holder.tv_emotioin = convertView.findViewById(R.id.tv_emotion);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img_emotioin.setPadding(itemWidth / 8, itemWidth / 8, itemWidth / 8, itemWidth / 8);
        holder.tv_emotioin.setPadding(itemWidth / 8, itemWidth / 8, itemWidth / 8, itemWidth / 8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, itemWidth);
        holder.img_emotioin.setLayoutParams(params);
        holder.tv_emotioin.setLayoutParams(params);
        String emotionName = emotionNames.get(position);
        Glide.with(context).load(EmotionUtils.getImgByName(context, emotion_map_type, emotionName)).error(R.drawable.nothing).into(holder.img_emotioin);
//        holder.tv_emotioin.setText(emotionName);
        return convertView;
    }

    class ViewHolder {
        private ImageView img_emotioin;
        private TextView tv_emotioin;
    }

}
