package com.mxkj.yuanyintang.mainui.myself;

/**
 * Created by LiuJie on 2017/11/1.
 */

public class IdCardUtil {
    // 判断是否符合身份证号码的规范
    public static boolean isIDCard(String IDCard) {
        if (IDCard != null) {
            String IDCardRegex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)";
            return IDCard.matches(IDCardRegex);
        }
        return false;
    }
}
