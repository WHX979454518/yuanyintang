package com.mxkj.yuanyintang.mainui.login_regist.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.app.CommonUtils;

/**
 * Created by LiuJie on 2018/1/15.
 */

public class EmailView extends LinearLayout {
    Context context;
    String coutInfo;//用户输入的@符之前的内容
    private String[] emailSufixs = new String[]{"qq.com", "163.com", "gmail.com", "sina.com",
            "foxmail.com", "139.com", "sina.com"};

    public EmailView(Context context) {
        super(context);
        this.context = context;
    }

    public EmailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setCoutText(String cout, final EmailViewItemCallback callback) {
        this.coutInfo = cout;
        ListView listView = new ListView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtils.INSTANCE.dip2px(context,200));
        listView.setLayoutParams(layoutParams);
        EmailViewAdapter emailViewAdapter = new EmailViewAdapter();
        listView.setAdapter(emailViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                callback.onEmailItemClick(coutInfo + emailSufixs[i]);
            }
        });
        listView.setDivider(null);
        addView(listView);
    }

    private class EmailViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return emailSufixs.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null)
                v = LayoutInflater.from(getContext()).inflate(R.layout.item_email_view, null);
            TextView tv = (TextView) v.findViewById(R.id.tv_email);
            tv.setText(coutInfo + emailSufixs[position]);
            return v;
        }
    }

    public interface EmailViewItemCallback {
        void onEmailItemClick(String text);
    }

}
