package com.mxkj.yuanyintang.utils.string;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by LiaoXiang on 2015/11/18.
 */
public class StringUtils {
    /**
     * 是否为空字符或null
     *
     * @param str
     * @return
     */
    public static String isEmpty(String str) {
        return TextUtils.isEmpty(str) ? "" : str.toString();
    }

    /**
     * int大于一定值变化
     *
     * @return
     */
    public static String setNum(int num) {
        if (num > 99999) {
            return num / 10000 + "万";
        } else {
            return num + "";
        }
    }

    /**
     * 字符更改颜色
     *
     * @param context 上下文
     * @param content 内容
     * @param start   变换颜色文字开始位置
     * @param end     变换颜色文字结束位置
     * @param color   变换的颜色
     * @return
     */
    public static SpannableStringBuilder getSpannableString(Context context, String content, int start, int end, int color) {
        SpannableStringBuilder s = new SpannableStringBuilder(content);
        s.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

//    /**
//     * 取double数据后面两位小数的上界
//     *
//     * @param dbl 需转换的数据
//     * @return 返回double型
//     */
//    public static double getValueWith2Suffix(double dbl) {
//        BigDecimal bg = new BigDecimal(dbl);
//        return bg.setScale(3, BigDecimal.ROUND_DOWN).doubleValue();
//    }
//    /**
//     * 取double数据后面两位小数的上界
//     *
//     * @param dbl 需转换的数据
//     * @return 返回double型
//     */
//    public static double getValueWith2Suffix(double dbl) {
//        return  new java.text.DecimalFormat("#.00").format(dbl);
//    }


    private static final String TAG = "StringUtils";

    /**
     * 取小数点后两位
     *
     * @param dbl
     * @return 返回字符型
     */
    public static String getStringValueWith2Suffix(double dbl) {
        return TextUtils.isEmpty(IsMobilieNum.subZeroAndDot(String.format("%.2f", dbl)))
                ? "0"
                : IsMobilieNum.subZeroAndDot(IsMobilieNum.subZeroAndDot(String.format("%.2f", dbl)));
    }

    /**
     * 检查字符串是否为电话号码的方法,并返回true or false的判断值
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        String expression = "((^(13|14|15|17|18)[0-9]{9}$))";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[\n\t]"; //要过滤掉的字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 日期转毫秒
     *
     * @param date
     * @return
     */
    public static String getTime(String date) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(date);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 13);
        } catch (ParseException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re_time;
    }

    /**
     * 日期转毫秒
     *
     * @param date
     * @return
     */
    public static Long getLongTime(String date) {
        long l = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(date);
            l = d.getTime();
        } catch (ParseException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return l;
    }

    /*去字符串中的空格*/
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    private final static Pattern pattern = Pattern.compile("\\S*[?]\\S*");

    /**
     * 获取链接的后缀名
     *
     * @return
     */
    public static String parseSuffix(String url) {

        Matcher matcher = pattern.matcher(url);

        String[] spUrl = url.toString().split("/");
        int len = spUrl.length;
        String endUrl = spUrl[len - 1];

        if (matcher.find()) {
            String[] spEndUrl = endUrl.split("\\?");
            return spEndUrl[0].split("\\.")[1];
        }
        return endUrl.split("\\.")[1];
    }
}
