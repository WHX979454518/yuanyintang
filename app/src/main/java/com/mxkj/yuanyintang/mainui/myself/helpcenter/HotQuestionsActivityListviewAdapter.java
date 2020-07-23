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
import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.HelperListBean;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.ProblemListBean;

import java.util.List;

/**
 *
 */

public class HotQuestionsActivityListviewAdapter extends MyAdapter {
    Context context;
    List<HelperListBean.DataBean> list;

    public HotQuestionsActivityListviewAdapter(Context context, List<HelperListBean.DataBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.hotquestionactivitylistadapter_item,null);
            vh = new ViewHorder();
            vh.tv= (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(vh);
        }
        vh= (ViewHorder) convertView.getTag();
        final HelperListBean.DataBean helplerListBean = list.get(position);
        vh.tv.setText(helplerListBean.getTitle());

        return convertView;
    }
    class ViewHorder{
        TextView tv;
    }
}
