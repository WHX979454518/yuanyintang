package com.mxkj.yuanyintang.mainui.pond.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.pond.bean.PondCommentBean;
import com.mxkj.yuanyintang.extraui.activity.PicturePagerDetailsActivity;
import com.mxkj.yuanyintang.extraui.bean.PictureDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/11.
 */

public class Hori_ImgListAdapter_Pond extends RecyclerView.Adapter<Hori_ImgListAdapter_Pond.ViewHolder> {
    private LayoutInflater mInflater;
    private Context context;
    private PondCommentBean.DataBean dataBean;
    private ArrayList<String> imgLists;
    Bundle bundle;


    public Hori_ImgListAdapter_Pond(Context context, PondCommentBean.DataBean dataBean) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.dataBean = dataBean;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView mImg;
        TextView tv_long_pic;
    }

    @Override
    public int getItemCount() {
        if (dataBean != null && dataBean.getImglist_info() != null) {
            return dataBean.getImglist_info().size();
        }
        return 0;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.pond_comment_img_recycler_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImg = view.findViewById(R.id.item_image);
        viewHolder.tv_long_pic = view.findViewById(R.id.tv_long_pic);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        bundle = new Bundle();
        if (dataBean != null) {
            List<PondCommentBean.DataBean.ImglistInfoBean> imglist_info = dataBean.getImglist_info();
            imgLists = new ArrayList<>();
            if (imglist_info != null && imglist_info.size() > 0) {
                if (dataBean.getImglist_info().get(i).getExt().equals("gif")) {
                    viewHolder.tv_long_pic.setVisibility(View.VISIBLE);
                    viewHolder.tv_long_pic.setText("动图");
                } else if (dataBean.getImglist_info().get(i).getIs_long().equals("1")) {//是长图
                    viewHolder.tv_long_pic.setVisibility(View.VISIBLE);
                } else {//不是长图
                    viewHolder.tv_long_pic.setVisibility(View.GONE);
                }
                for (int j = 0; j < imglist_info.size(); j++) {
                    imgLists.add(imglist_info.get(j).getLink());
                }
            } else {

            }
        }

        ImageLoader.with(context)
                .format(0)
                .getSize(100,100)
                .url(imgLists.get(i))

                .scale(ScaleMode.CENTER_CROP)
                .placeHolder(R.drawable.nothing)
                .into(viewHolder.mImg);
        viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureDataBean pictureDataBean = new PictureDataBean()
                        .setId(dataBean.getId() + "")
                        .setCommentNum(dataBean.getCom_count())
                        .setPhotoList(imgLists)
                        .setTitle(dataBean.getContent())
                        .setNickname(dataBean.getNickname())
                        .setPosition(i)
                        .setHits(dataBean.getHits())
                        .setType("");
                bundle.putSerializable(PicturePagerDetailsActivity.PICTURE_DATA, pictureDataBean);
                Intent intent = new Intent(context, PicturePagerDetailsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }
}