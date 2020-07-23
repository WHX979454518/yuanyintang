package com.mxkj.yuanyintang.mainui.myself.my_income;

import android.text.Editable;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zuixia on 2018/6/5.
 */

public class EdittextUtils {
    /**
     * 金额输入框中的内容限制（小数点后2位）
     */
    private static int selectionStart;
    private static int selectionEnd;

    public static void judgeNumber(Editable edt, EditText edit) {
        if (edt != null && edt.length() > 0) {
            selectionStart = edit.getSelectionStart();
            selectionEnd = edit.getSelectionEnd();
            if (!isOnlyPointNumber(edit.getText().toString())) {
                String s = edit.getText().toString();
                edt.delete(selectionStart - 1, selectionEnd);
                edit.setText(edt);
            }
        }
    }

    public static boolean isOnlyPointNumber(String number) {//保留两位小数
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d{0,2}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
}
