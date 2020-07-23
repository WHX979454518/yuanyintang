package com.mxkj.yuanyintang.utils.RichTextutils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static android.content.ContentValues.TAG;


/**
 * Created by CentMeng on 17/11/1.
 */
public class ImageTextUtil {
    public static Drawable getUrlDrawable(String source, TextView mTextView) {
        GlideImageGetter imageGetter = new GlideImageGetter(mTextView.getContext(), mTextView);
        return imageGetter.getDrawable(source);
    }

    public static void setImageText(TextView tv, String srcHtml) {
        String html2 = srcHtml.replace("\r\n", "<br>");
        String html1 = html2.replace("\n", "<br>");
        String html = "<a onselectstart = \"return false\">"+html1+"</a>";

        Spanned htmlStr = Html.fromHtml(html);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            tv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            tv.setTextIsSelectable(true);
        }
        tv.setText(htmlStr);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) tv.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            ImageSpan[] imgs = sp.getSpans(0, end, ImageSpan.class);
            StyleSpan[] styleSpens = sp.getSpans(0, end, StyleSpan.class);
            ForegroundColorSpan[] colorSpans = sp.getSpans(0, end, ForegroundColorSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();
            for (URLSpan url : urls) {
                style.setSpan(url, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF12ADFA"));
                style.setSpan(colorSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for (ImageSpan url : imgs) {
                ImageSpan span = new ImageSpan(getUrlDrawable(url.getSource(), tv), url.getSource());
                style.setSpan(span, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for (StyleSpan styleSpan : styleSpens) {
                style.setSpan(styleSpan, sp.getSpanStart(styleSpan), sp.getSpanEnd(styleSpan), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for (ForegroundColorSpan colorSpan : colorSpans) {
                style.setSpan(colorSpan, sp.getSpanStart(colorSpan), sp.getSpanEnd(colorSpan), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            tv.setText(style);
        }
    }

    public static void setImageText(Context context, TextView tv, String srcHtml, int type) {
        String html2 = srcHtml.replace("\r\n", "<br>");
        String html1 = html2.replace("\n", "<br>");
        String html = "<a onselectstart = \"return false\">"+html1+"</a>";
        Spanned htmlStr = Html.fromHtml(html);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            tv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            tv.setTextIsSelectable(true);
        }
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = htmlStr;
        if (text instanceof Spannable) {
            int end = text.length();
//            Spannable sp = (Spannable) tv.getText();
            Spannable sp = (Spannable) htmlStr;

            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            ImageSpan[] imgs = sp.getSpans(0, end, ImageSpan.class);
            StyleSpan[] styleSpens = sp.getSpans(0, end, StyleSpan.class);
            ForegroundColorSpan[] colorSpans = sp.getSpans(0, end, ForegroundColorSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();
            for (URLSpan url : urls) {
                style.setSpan(url, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF12ADFA"));
                style.setSpan(colorSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for (ImageSpan url : imgs) {
                ImageSpan span = new ImageSpan(getUrlDrawable(url.getSource(), tv), url.getSource());
                style.setSpan(span, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for (StyleSpan styleSpan : styleSpens) {
                style.setSpan(styleSpan, sp.getSpanStart(styleSpan), sp.getSpanEnd(styleSpan), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for (ForegroundColorSpan colorSpan : colorSpans) {
                style.setSpan(colorSpan, sp.getSpanStart(colorSpan), sp.getSpanEnd(colorSpan), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tv.append(style);
        }
    }
}
