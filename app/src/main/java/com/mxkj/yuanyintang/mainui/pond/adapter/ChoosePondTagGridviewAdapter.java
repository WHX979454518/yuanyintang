package com.mxkj.yuanyintang.mainui.pond.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.adapter.MyAdapter;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.DetailsActivity;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.ProblemListBean;
import com.mxkj.yuanyintang.mainui.pond.bean.PondHotTagBean;

import java.util.List;

/**
 *
 */

public class ChoosePondTagGridviewAdapter extends MyAdapter {
    Context context;
    List<PondHotTagBean.DataBean> list;

    public ChoosePondTagGridviewAdapter(Context context, List<PondHotTagBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHorder vh = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.choosepondtaggridviewdapter_item,null);
            vh = new ViewHorder();
//            vh.classification_image= (ImageView) convertView.findViewById(R.id.classification_image);
            vh.classification_name= (TextView) convertView.findViewById(R.id.classification_name);
            vh.list_item= (RelativeLayout) convertView.findViewById(R.id.list_item);
            convertView.setTag(vh);
        }
        vh= (ViewHorder) convertView.getTag();
        final PondHotTagBean.DataBean problemListBean = list.get(position);
        vh.classification_name.setText(problemListBean.getTitle());
        //item点击
        vh.list_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                list.remove(problemListBean);
                notifyDataSetChanged();
                Log.e("lllll","长安了");
                return true;
            }
        });
        return convertView;
    }
    class ViewHorder{
        TextView classification_name;
        RelativeLayout list_item;
    }
}
