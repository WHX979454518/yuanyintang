package com.mxkj.yuanyintang.mainui.myself.helpcenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.adapter.MyAdapter;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.ProblemListBean;

import java.util.List;

/**
 *
 */

public class HotQuestionsActivityGridviewAdapter extends MyAdapter {
    Context context;
    List<ProblemListBean.DataBean> list;

    public HotQuestionsActivityGridviewAdapter(Context context, List<ProblemListBean.DataBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.hotquestionactivtygridviewdapter_item,null);
            vh = new ViewHorder();
            vh.classification_image= (ImageView) convertView.findViewById(R.id.classification_image);
            vh.classification_name= (TextView) convertView.findViewById(R.id.classification_name);
            vh.list_item= (RelativeLayout) convertView.findViewById(R.id.list_item);
            convertView.setTag(vh);
        }
        vh= (ViewHorder) convertView.getTag();
        final ProblemListBean.DataBean problemListBean = list.get(position);
        vh.classification_name.setText(problemListBean.getTitle());
        if(null != problemListBean.getImgpic_info()){
            Glide.with(context)
                    .load(problemListBean.getImgpic_info().getLink())
                    .into(vh.classification_image);
        }else {
            vh.classification_image.setVisibility(View.GONE);
        }
        //item点击
        vh.list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",problemListBean.getTitle());
                bundle.putString("class_id",problemListBean.getId()+"");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHorder{
        ImageView classification_image;
        TextView classification_name;
        RelativeLayout list_item;
    }
}
