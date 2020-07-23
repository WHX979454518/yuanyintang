package com.mxkj.yuanyintang.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;

import java.util.List;

import static com.mxkj.yuanyintang.musicplayer.service.MediaService.bean;

public class MusicListAdapter extends BaseAdapter {
    private List<MusicInfo.DataBean> listBeen;
    private Context context;
    private ViewHolder holder;

    public MusicListAdapter(List<MusicInfo.DataBean> listBeen, Context context) {
        this.listBeen = listBeen;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (listBeen == null) {
            return 0;
        }
        return listBeen.size();
    }

    @Nullable
    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Nullable
    @Override
    public View getView(final int position, @Nullable View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_song_list, null);
            holder = new ViewHolder();
            holder.tv_songName = convertView.findViewById(R.id.tv_songName);
            holder.tv_singer = convertView.findViewById(R.id.tv_singer);
            holder.img_like = convertView.findViewById(R.id.imh_like);
            holder.img_delete = convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (listBeen.size() > 0 && listBeen.size() > position) {
            if (listBeen.get(position).isCurrentPlaying()) {
                holder.tv_singer.setTextColor(Color.parseColor("#ff6699"));
                holder.tv_songName.setTextColor(Color.parseColor("#ff6699"));
            } else {
                holder.tv_singer.setTextColor(Color.parseColor("#9da2a6"));
                holder.tv_songName.setTextColor(Color.parseColor("#2b2b2b"));
            }
            holder.img_like.setOnClickListener(new View.OnClickListener() {//收藏
                @Override
                public void onClick(View v) {
                    PlayCtrlUtil.INSTANCE.collectSong(context, listBeen.get(position), new PlayCtrlUtil.ChgCollectCallBack() {
                        @Override
                        public void chgCollect(MusicInfo.DataBean dataBean1) {
                            for (int i = 0; i < listBeen.size(); i++) {
                                if (dataBean1.getId() == listBeen.get(i).getId()) {
                                    listBeen.get(i).setCollection(dataBean1.getCollection());
                                    break;
                                }
                            }
                            if (bean != null) {
                                if (bean.getId() == dataBean1.getId()) {
                                    bean.setCollection(dataBean1.getCollection());
                                    context.sendBroadcast(new Intent("cgCollect"));
                                }
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
            });
            holder.tv_singer.setText(" --" + listBeen.get(position).getNickname());
            holder.tv_songName.setText(listBeen.get(position).getTitle());
            if (listBeen.get(position).getCollection() == 1) {
                holder.img_like.setImageResource(R.drawable.fullplay_like_red);
            } else {
                holder.img_like.setImageResource(R.drawable.fullplay_like_normal);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_songName, tv_singer;
        ImageView img_like;
        LinearLayout img_delete;
    }
}
