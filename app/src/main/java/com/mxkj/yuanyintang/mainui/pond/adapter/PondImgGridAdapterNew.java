package com.mxkj.yuanyintang.mainui.pond.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.extraui.activity.PicturePagerDetailsActivity;
import com.mxkj.yuanyintang.extraui.bean.PictureDataBean;
import com.mxkj.yuanyintang.mainui.pond.bean.PondBeanNew;
import com.mxkj.yuanyintang.musicplayer.playcache.CacheListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuJie on 2017/9/27.
 */


/**
 * 之前取Imglist_link这个数组，3月初后台改接口后取Imglist_info数组。
 */
public class PondImgGridAdapterNew extends BaseAdapter implements CacheListener {
    private List<PondBeanNew.DataBean.DataListBean.ImglistInfoBean> dataList;
    private LayoutInflater inflater;
    private Context mContext;
    PondBeanNew.DataBean.DataListBean dataBean;
    ArrayList<String> lists;


    public PondImgGridAdapterNew(PondBeanNew.DataBean.DataListBean dataBean, Context context) {
        this.dataBean = dataBean;
        if (dataBean.getImglist_info() != null) {
            dataList = dataBean.getImglist_info();
        }
        inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public int getCount() {
        if (dataList.size() < 3) {
            return dataList.size();
        }
        return 3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.pond_grid_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img_pond = convertView.findViewById(R.id.img_pond_grid);
        holder.tv_more_pic = convertView.findViewById(R.id.tv_more_pic);
        holder.tv_long_pic = convertView.findViewById(R.id.tv_long_pic);
        if (dataList.size() > 0) {
            if (dataList.size() > 3) {
                if (position == 2) {
                    holder.tv_more_pic.setVisibility(View.VISIBLE);
                    holder.tv_more_pic.setText("查看更多\n" + (dataList.size() - 3) + "张");
                } else {
                    holder.tv_more_pic.setVisibility(View.GONE);
                }
            } else {
                holder.tv_more_pic.setVisibility(View.GONE);
            }
//                判断长图
            PondBeanNew.DataBean.DataListBean.ImglistInfoBean infoBean = dataList.get(position);
            if (dataBean.getImglist_info().get(0).getExt().equals("gif")) {
                holder.tv_long_pic.setVisibility(View.VISIBLE);
                holder.tv_long_pic.setText("动图");
            } else if (infoBean.getIs_long().equals("1")) {//是长图
                if (position != 2) {
                    holder.tv_long_pic.setVisibility(View.VISIBLE);
                }
            } else {//不是长图
                holder.tv_long_pic.setVisibility(View.GONE);
            }
            lists = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                lists.add(dataList.get(i).getLink());
            }
            Glide.with(MainApplication.Companion.getApplication()).load(dataList.get(position).getLink() + "/400/400/3/80?format=0")
                   .asBitmap() .into(holder.img_pond);
            holder.img_pond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    PictureDataBean pictureDataBean = new PictureDataBean()
                            .setId(dataBean.getId() + "")
                            .setCommentNum(dataBean.getHits())
                            .setPhotoList(lists)
                            .setTitle(dataBean.getTitle())
                            .setNickname(dataBean.getNickname())
                            .setType("pond")
                            .setPosition(position)
                            .setHits(dataBean.getHits());
                    bundle.putSerializable(PicturePagerDetailsActivity.PICTURE_DATA, pictureDataBean);
                    Intent intent = new Intent(mContext, PicturePagerDetailsActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        Log.e("TAG", "图片: " + percentsAvailable);
    }

    class ViewHolder {
        ImageView img_pond;
        TextView tv_more_pic, tv_long_pic;
    }
}
