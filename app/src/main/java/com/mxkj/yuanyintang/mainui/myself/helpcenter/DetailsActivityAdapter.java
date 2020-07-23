package com.mxkj.yuanyintang.mainui.myself.helpcenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.adapter.MyAdapter;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.utils.ScreenUtils;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.HelperListBean;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.WebSharUrlBean;
import com.mxkj.yuanyintang.mainui.newapp.ExpandTextView;
import com.mxkj.yuanyintang.mainui.web.CommonWebview;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.app.CommonUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;


public class DetailsActivityAdapter extends MyAdapter {
    List<HelperListBean.DataBean> list;
    Context context;
    int count=1;

    public DetailsActivityAdapter(List<HelperListBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public DetailsActivityAdapter(List<HelperListBean.DataBean> dataList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.detailsactivity_item,null);
            vh = new ViewHorder();
            vh.title= (TextView) convertView.findViewById(R.id.title);

            vh.text_color= (TextView) convertView.findViewById(R.id.text_color);

            vh.iv_expand_icon= (ImageView) convertView.findViewById(R.id.iv_expand_icon);
             vh.expandTextView = convertView.findViewById(R.id.exTvContent);
            // 设置TextView可展示的宽度 ( 父控件宽度 - 左右margin - 左右padding）
            int whidth = ScreenUtils.getScreenWidth(context) - CommonUtils.INSTANCE.dip2px(context, 20f);
            vh.expandTextView.initWidth(whidth);
//            vh.expandTextView.setExpandText("收起");
//            vh.expandTextView.setCloseText("展开");
            vh.expandTextView.setMaxLines(10);
            convertView.setTag(vh);
        }
        vh= (ViewHorder) convertView.getTag();
        final HelperListBean.DataBean item = list.get(position);
        vh.title.setText(item.getTitle());

//        SpannableString spannableString = new SpannableString("查看更多");
//        //设置字体前景色
//        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //正则匹配中文及中文标点符号
        String reg="[^\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]";
        String str = item.getContent().replaceAll(reg, "");

        vh.expandTextView.setCloseText(str);
        final ViewHorder finalVh = vh;

        vh.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count==1){
                    finalVh.expandTextView.setVisibility(View.VISIBLE);
                    finalVh.text_color.setVisibility(View.VISIBLE);
                    finalVh.iv_expand_icon.setImageResource(R.mipmap.icon_help_more_open3x);
                    count = 2;
                }else {
                    finalVh.expandTextView.setVisibility(View.GONE);
                    finalVh.text_color.setVisibility(View.GONE);
                    finalVh.iv_expand_icon.setImageResource(R.mipmap.icon_help_more3x);
                    count = 1;
                }
            }
        });
        vh.expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetWork.INSTANCE.getHelpProblemListDetailsDetails(context, item.getId()+"", new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
                        WebSharUrlBean webSharUrlBean = JSON.parseObject(resultData, WebSharUrlBean.class);
                        Intent intent = new Intent(context, CommonWebview.class);
                        intent.putExtra("url", webSharUrlBean.getData().getInfo().getShare_url());
                        intent.putExtra("title", webSharUrlBean.getData().getInfo().getTitle());
                        context.startActivity(intent);
                    }

                    @Override
                    public void doError(String msg) {

                    }

                    @Override
                    public void doResult() {

                    }
                });

            }
        });
        vh.text_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetWork.INSTANCE.getHelpProblemListDetailsDetails(context, item.getId()+"", new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
                        WebSharUrlBean webSharUrlBean = JSON.parseObject(resultData, WebSharUrlBean.class);
                        Intent intent = new Intent(context, CommonWebview.class);
                        intent.putExtra("url", webSharUrlBean.getData().getInfo().getShare_url());
                        intent.putExtra("title", webSharUrlBean.getData().getInfo().getTitle());
                        context.startActivity(intent);
                    }

                    @Override
                    public void doError(String msg) {

                    }

                    @Override
                    public void doResult() {

                    }
                });

            }
        });
        return convertView;
    }

    class ViewHorder{
        TextView title,text_color;
        ExpandTextView expandTextView;
        ImageView iv_expand_icon;
    }
}
