package com.mxkj.yuanyintang.mainui.pond.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.adapter.MyAdapter;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.utils.ScreenUtils;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.HelperListBean;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.WebSharUrlBean;
import com.mxkj.yuanyintang.mainui.newapp.ExpandTextView;
import com.mxkj.yuanyintang.mainui.pond.bean.ChoosePlayListTagBean;
import com.mxkj.yuanyintang.mainui.pond.bean.PondHotTagBean;
import com.mxkj.yuanyintang.mainui.pond.widget.ChoosePondTag;
import com.mxkj.yuanyintang.mainui.pond.widget.FlowLayout;
import com.mxkj.yuanyintang.mainui.web.CommonWebview;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.app.CommonUtils;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;


public class ChoosePondTagAdapter extends MyAdapter {
    List<ChoosePlayListTagBean .DataBean> list;
    Context context;
    private Handler myhandler;
    private List<PondHotTagBean.DataBean> selectedTagList = new ArrayList<>();//已选中的

    public ChoosePondTagAdapter(List<ChoosePlayListTagBean .DataBean> list, Context context,Handler myhandler) {
        this.list = list;
        this.context = context;
        this.myhandler = myhandler;
    }

    public ChoosePondTagAdapter(List<ChoosePlayListTagBean .DataBean> dataList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.choosepongtagadapter_item,null);
            vh = new ViewHorder();
            vh.title= (TextView) convertView.findViewById(R.id.title);
            vh.playlist_tag_hot= (FlowLayout) convertView.findViewById(R.id.playlist_tag_hot);
            convertView.setTag(vh);
        }
        vh= (ViewHorder) convertView.getTag();
        final ChoosePlayListTagBean .DataBean item = list.get(position);
        if(item.getTitle().equals("")||item.getTitle() ==""){

        }else {
            vh.title.setText(item.getTitle()+"标签");
        }

        vh.playlist_tag_hot.removeAllViews();
        List<ChoosePlayListTagBean.DataBean.TagsBean> tagList = new ArrayList<>();//场景里面的tags
        tagList = item.getTags();
        for (int i = 0; i < tagList.size(); i++) {
            final CheckBox ckTag = (CheckBox) LayoutInflater.from(context).inflate(
                    R.layout.choose_pond_tag_hottag, vh.playlist_tag_hot, false);
            //点击事件
            final int finalI = i;
            ckTag.setText(tagList.get(i).getTitle());

            for (int j = 0; j < selectedTagList.size(); j++) {
                if (selectedTagList.get(j).getTitle().equals(tagList.get(i).getTitle())) {
                    ckTag.setChecked(true);
                }
            }

            final int finalI1 = i;
            ckTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Message message = Message.obtain();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("flag",b);
                    bundle.putString("tag", item.getTags().get(finalI1).getTitle());
                    bundle.putInt("potion",finalI);
                    message.setData(bundle);
                    myhandler.sendMessage(message);
                }
            });
            vh.playlist_tag_hot.addView(ckTag);
        }
        return convertView;
    }

    class ViewHorder{
        TextView title;
        FlowLayout playlist_tag_hot;
    }
}
