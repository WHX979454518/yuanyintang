package com.mxkj.yuanyintang.mainui.myself.settings.utils;

import android.text.TextUtils;

/**
 * Created by LiuJie on 2017/10/24.
 */

public class GetSafeNum {
    public static String getSafeNumStr(String pNumber) {
        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');

                } else {

                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return "";
    }
}
