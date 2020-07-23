package com.mxkj.yuanyintang.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;


/**
 * Created by ~Wang~ on 2018/11/29
 */
public class SaveMessage {

    public static void SaveLogintoken(String logintoken, Context context){
        SharedPreferences spf = context.getSharedPreferences("logintoken", Context.MODE_PRIVATE);
        SharedPreferences.Editor spfe = spf.edit();
        spfe.putString("logintoken",logintoken);
        spfe.commit();
    }
    public static String huoquLogintoken(Context context) {
        SharedPreferences spf = context.getSharedPreferences("logintoken", Context.MODE_PRIVATE);
        String logintoken = spf.getString("logintoken","");
        return logintoken;//取出共享参数
    }
    public static void clearLogintoken(Context context){
        SharedPreferences spf = context.getSharedPreferences("logintoken", Context.MODE_PRIVATE);
        SharedPreferences.Editor spfe = spf.edit();
        spfe.clear().commit();
    }
    public static String getStandardTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss",
                Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = new Date(timestamp+60*60*1000);
        sdf.format(date);
        return sdf.format(date);
    }

}
