package com.mxkj.yuanyintang.mainui.home.music_charts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.adapter.MyAdapter;
import com.mxkj.yuanyintang.mainui.home.music_charts.bean.ChartsHomeNewBean;

import java.util.List;


public class ChartsHomeNewActivityAdapter extends MyAdapter {
    List<ChartsHomeNewBean.DataBean> list;
    Context context;

    public ChartsHomeNewActivityAdapter(List<ChartsHomeNewBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public ChartsHomeNewActivityAdapter(List<ChartsHomeNewBean.DataBean> dataList) {
        super();
        this.list = dataList;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHorder vh = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.chargr_newitem,null);
            vh = new ViewHorder();
            vh.chargr_name= (TextView) convertView.findViewById(R.id.chargr_name);
            vh.icon_value_first_name= (TextView) convertView.findViewById(R.id.icon_value_first_name);
            vh.icon_value_second_name= (TextView) convertView.findViewById(R.id.icon_value_second_name);
            vh.icon_value_third_name= (TextView) convertView.findViewById(R.id.icon_value_third_name);
            vh.chaegr_newitem_img= (ImageView) convertView.findViewById(R.id.chaegr_newitem_img);
            convertView.setTag(vh);
        }
        vh= (ViewHorder) convertView.getTag();
        ChartsHomeNewBean.DataBean item = list.get(position);
        vh.chargr_name.setText(item.getTitle());

//        if (item.getItem_list() != null) {
//            int n = 0;  //保存元素个数的变量
//            for (int i = 0; i < item.getItem_list().size(); i++) {
//                if (null != item.getItem_list().get(i)) n++;
//            }
//        }
        if(item.getItem_list().size() == 1){
            vh.icon_value_first_name.setText(item.getItem_list().get(0));
        }else if(item.getItem_list().size() == 2){
            vh.icon_value_first_name.setText(item.getItem_list().get(0));
            vh.icon_value_second_name.setText(item.getItem_list().get(1));
        }else if(item.getItem_list().size() == 3){
            vh.icon_value_first_name.setText(item.getItem_list().get(0));
            vh.icon_value_second_name.setText(item.getItem_list().get(1));
            vh.icon_value_third_name.setText(item.getItem_list().get(2));
        }
        Glide.with(context)
                .load(item.getPic_link())
//                .placeholder(com.xiao.nicevideoplayer.R.drawable.img_default)
                .crossFade()
                .into(vh.chaegr_newitem_img);
//        try {
//            vh.workfragment_item_image.setImageResource(userInfoInfo.getIconId());
//        } catch (NumberFormatException e) {
//
//        }
        return convertView;
    }

    class ViewHorder{
        TextView chargr_name,icon_value_first_name,icon_value_second_name,icon_value_third_name;
        ImageView chaegr_newitem_img;
    }
}
