package com.mxkj.yuanyintang.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * company：MXKJ
 * Created by ... on 2017/9/20.
 */
/**
 *
 * 搜索关键字标红
 *
 *
 * */
public class SearTextView extends TextView {
    public SearTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSpecifiedTextsColor(@NonNull String text, @NonNull String specifiedTexts, int color) {
        if (text==null){
            return;
        }
        List<Integer> sTextsStartList = new ArrayList<>();

        int sTextLength = specifiedTexts.length();
        String temp = text;
        int lengthFront = 0;//记录被找出后前面的字段的长度
        int start = -1;
        do {
            start = temp.indexOf(specifiedTexts);

            if (start != -1) {
                start = start + lengthFront;
                sTextsStartList.add(start);
                lengthFront = start + sTextLength;
                temp = text.substring(lengthFront);

                if (text.toUpperCase().equals(text)){

                }
            }
        } while (start != -1);

        SpannableStringBuilder styledText = new SpannableStringBuilder(text);
        for (Integer i : sTextsStartList) {
            styledText.setSpan(
                    new ForegroundColorSpan(color),
                    i,
                    i + sTextLength,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        setText(styledText);
    }
}
