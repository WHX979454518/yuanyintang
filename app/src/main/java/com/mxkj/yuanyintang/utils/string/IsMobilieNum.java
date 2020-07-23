package com.mxkj.yuanyintang.utils.string;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LiuJie on 2016/4/21.
 */
public class IsMobilieNum {
    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 验证手机格式
     * true-->是手机号码
     * false-->不是手机号码
     */
    public static boolean isMobileNumber(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }

        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	    联通：130、131、132、152、155、156、185、186
	    电信：133、153、180、189、（1349卫通） "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"
	    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
	    */
//        String regExp = "^((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[0-9])|(17[0,3,7]))\\d{8}$";
        String regExp = "^1[2,3,4,5,6,7,8,9]\\d{9}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(mobiles);
        return m.find();//boolean
    }

    /**
     * 判断邮编
     *
     * @return
     */
    public static boolean isZipNO(String zipString) {
        String str = "^[1-9][0-9]{5}$";
        return Pattern.compile(str).matcher(zipString).matches();
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    public static boolean isNumber(String str) {
        //采用正则表达式的方式来判断一个字符串是否为数字，这种方式判断面比较全
        //可以判断正负、整数小数
        boolean isDouble = str.matches("-?[0-9]+.*[0-9]*");

        return isDouble;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }


}
