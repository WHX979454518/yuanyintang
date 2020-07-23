package com.mxkj.yuanyintang.utils.app;

import java.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * company：MXKJ
 * Created by ... on 2016/12/29.
 */

public class TimeUtils {
    @NonNull
    public static String timeUtil(Long time) {
        String s = ((time / 1000) % 60) + "";
        int m = (int) ((time / 1000) / 60);
        if (((time / 1000) % 60) < 10) {
            s = "0" + s;
        }
        if (m >= 10) {
            return m + ":" + s;
        } else {
            return "0" + m + ":" + s;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String timestampToDate(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(timestamp);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String timestampToDateSlash(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(timestamp);
    }

    public static String timestampToDateChn(long timestamp) throws NumberFormatException {
        return new java.text.SimpleDateFormat("MM-dd HH:mm").format(new Date(timestamp));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String timestampToHandM(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(timestamp);
    }

    public static String getTime(Date date) {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    public static String getFetureDate(long expire) {
        //PHP和Java时间戳存在三位位差，用000补齐
        if (String.valueOf(expire).length() == 10) {
            expire = expire * 1000;
        }
        Date date = new Date(expire);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(date);
        if (result.startsWith("0")) {
            result = result.substring(1);
        }
        return result;
    }
    /**
     * 时间戳转换日期格式
     *
     * @param timestamp 单位秒
     * @return
     */
    public static String getCurrentTime(long timestamp) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return f.format(new Date(timestamp));
    }

    /**
     * 获取时间和当前间隔。
     *
     * @param time
     * @return
     */
    public static String getTimeDesc(long time) {

        String desc = "";
        try {
            Date n = new Date();
            long delay = n.getTime() - time;
            long secondsOfHour = 60 * 60;
            long secondsOfDay = secondsOfHour * 24;
            // 相差的秒数
            long delaySeconds = delay / 1000;

            if (delaySeconds < 10) {
                desc = "刚刚";
            } else if (delaySeconds <= 60) {
                desc = delaySeconds + "秒前";
            } else if (delaySeconds < secondsOfHour) {
                desc = (delaySeconds / 60) + "分前";
            } else if (delaySeconds < secondsOfDay) {
                desc = getTimeHM(time);
            } else {
                desc = getCurrentTimeMMDD(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return desc;
    }

    /**
     * 获取时分
     *
     * @param time
     * @return
     */
    public static String getTimeHM(long time) throws ParseException {
        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("HH:mm", Locale.CHINA);
        Date date = null;
        date = new Date(time);
        return formater.format(date);
    }

    /**
     * 时间戳转换日期格式
     *
     * @param timestamp 单位毫秒
     * @return
     */
    public static String getCurrentTimeMMDD(long timestamp) {

        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("MM-dd", Locale.CHINA);

        return f.format(new Date(timestamp));

    }

    public static String getCurrentTimeMS(long timestamp) {

        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("mm:ss", Locale.CHINA);

        return f.format(new Date(timestamp));

    }

}
