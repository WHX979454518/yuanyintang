package com.mxkj.yuanyintang.mainui.newapp.weidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.linsh.lshutils.utils.LshContextUtils.getSystemService;

public class TagEdittext extends EditText implements TextWatcher, View.OnKeyListener {
    private static final int maxLength = 8;
    private static final boolean isChinese2English = true;
    public boolean isAdd = true;//能否继续添加（已添加数量大于规定数量就不能再添加）
    int start;
    int count;
    int before;
    public List<String> sources = new ArrayList<>();
    InputFilter emojiFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {
                int type = Character.getType(source.charAt(index));
                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };
    InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String chars = "\r\n\t ";
            String speChat = "[" + chars + "]";
            Pattern pattern = Pattern.compile(speChat);
            Matcher matcher = pattern.matcher(source.toString());
            if (matcher.find()) {
                String str = source.toString();
                char[] charArr = toCharArray(chars);
                for (char c : charArr) {
                    str = str.replaceAll(new String(new char[]{c}), "");
                }
                return str;
            } else return null;
        }
    };
    public OnAddListener listener;

    public TagEdittext(Context context) {
        super(context);
        init(context);

    }

    public TagEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TagEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        addTextChangedListener(this);
        setOnKeyListener(this);
        initHint(context);
        setFilters(new InputFilter[]{filter, emojiFilter});
        setSoftInputVis(this, true);
    }


    @NonNull
    private static MyImageSpanImage[] getSortedImageSpans(final Editable text) {
        MyImageSpanImage[] spans = text.getSpans(0, text.length(), MyImageSpanImage.class);

        Arrays.sort(spans, new Comparator<MyImageSpanImage>() {
            @Override
            public int compare(MyImageSpanImage o1, MyImageSpanImage o2) {
                int start1 = text.getSpanStart(o1);
                int start2 = text.getSpanStart(o2);
                if (start1 > start2) {
                    return 1;
                } else if (start1 < start2) {
                    return -1;
                }
                return 0;
            }
        });
        return spans;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.start = start;
        this.before = before;
        this.count = count;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (count <= 0) {
            return;
        }
        removeTextChangedListener(this);
        if (Selection.getSelectionEnd(editable) != start + count) {//如果光标位置和最新变化的结尾不相等说明不是正常输入的  全部作废处理
            editable.replace(start, start + count, "");
        } else {
//            onChange(editable);TODO  崩溃
        }
    }

    private void onChange(Editable editable) {
        String changeString = editable.subSequence(start, start + count).toString();
        int sumOfComma = removeAllComma(editable);
        count -= sumOfComma;
        count -= delIfOverMax();
        if (sumOfComma > 0) {
            initTags(getContext());
            return;
        }
        if (count == 0) {
            return;
        }
        setTextSpan(editable);
    }

    private void setTextSpan(Editable editable) {
        CharSequence string = editable.subSequence(start, start + count);
        char[] chars = toCharArray(string);
        int i = 0;
        for (char c : chars) {
            editable.setSpan(new MyImageSpanText(getContext(), getImage(new String(new char[]{c}), false, getContext())), start + i, start + i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            i++;
        }
    }

    private int removeAllComma(Editable editable) {
        int sum = 0;
        while (editable.toString().contains(",")) {//删除所有逗号
            int selEndIndex = Selection.getSelectionEnd(editable);
            int i = editable.toString().indexOf(",");
            editable.replace(i, i + 1, "");
            Selection.setSelection(editable, selEndIndex - (selEndIndex >= i ? 1 : 0));
            sum++;
        }
        return sum;
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
            Log.e("TAG", "onKey:==== ");
            if (isAdd) {
                initTags(getContext());
            }
            if (listener != null) {
                listener.onAdd(sources);
            }
            return true;
        }
        return false;
    }

    private static char[] toCharArray(CharSequence str) {
        if (str instanceof SpannableStringBuilder) {
            str.length();
        }
        char[] charArray = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            charArray[i] = str.charAt(i);
        }
        return charArray;
    }

    private static class MyImageSpanText extends ImageSpan {

        public MyImageSpanText(Context context, Bitmap b) {
            super(context, b);
        }
    }

    private static class MyImageSpanImage extends ImageSpan {

        public MyImageSpanImage(Context context, Bitmap b) {
            super(context, b);
        }

    }

    public void setSoftInputVis(View view, boolean vis) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (vis) {
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public int getPx(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
    }

    public int calculateLength(CharSequence c) {

        if (!isChinese2English) {
            return c.length();
        }

        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            char cc = c.charAt(i);
            if ((cc & 0xffff) <= 0xff) {
                len += 0.5;
            } else {
                len++;
            }
        }
        len = len * 2;
        return (int) Math.round(len);
    }

    private void initHint(Context c) {
        String string = "点击回车创建标签";
        SpannableString text = new SpannableString(string);
        Bitmap image = getImage(string, true, c);
        text.setSpan(new MyImageSpanText(c, image), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setHint(text);
    }

    private int initEt(Editable text, int selectedIndex, int start, int end) {
        if (start >= end) {
            return 0;
        }
        if (selectedIndex >= start && selectedIndex <= end) {//因为只会正常输入不会异常插入所以可以用光标位置判断某块是否超长
            String blockString = text.subSequence(start, end).toString();
            int length = calculateLength(blockString);
            if (length > maxLength) {
                //光标右边的字符串
                String rightString = text.subSequence(selectedIndex, end).toString();
                //光标左边的字符串
                String leftString = text.subSequence(start, selectedIndex).toString();
                //光标右边的字符串英文长度
                int rightLength = calculateLength(rightString);
                //光标左边的字符串英文长度
                int leftLength = calculateLength(leftString);
                //
                char[] leftStringChars = toCharArray(leftString);
                int okIndex = selectedIndex;
                int charSum = 0;
                int leaveLength = maxLength - rightLength;
                for (int i = leftStringChars.length - 1; i >= 0; i--) {
                    char c = leftStringChars[i];
                    if ((c & 0xffff) <= 0xff) {
                        charSum += 1;
                        okIndex--;
                    } else {
                        charSum += 2;
                        okIndex--;
                    }
                    int nowLeaveLength = leftLength - charSum;
                    if (nowLeaveLength <= leaveLength) {
                        break;
                    }
                }
                text.replace(okIndex, selectedIndex, "");
                return selectedIndex - okIndex;
            }
        } else {
            setImageSpan(text, start, end, getContext());
        }
        return 0;
    }

    private int delIfOverMax() {
        final Editable text = getText();
        int selEndIndex = Selection.getSelectionEnd(text);
        int lastEnd = 0;
        MyImageSpanImage[] spans = getSortedImageSpans(text);
        for (MyImageSpanImage span : spans) {
            int start = text.getSpanStart(span);
            int length = initEt(text, selEndIndex, lastEnd, start);
            if (length > 0)
                return length;

            int end = text.getSpanEnd(span);
            lastEnd = Math.max(end, lastEnd);
        }
        int length = text.length();
        length = initEt(text, selEndIndex, lastEnd, length);
        if (length > 0)
            return length;

        return 0;
    }

    public void initTags(Context c) {
        final Editable text = getText();
        int lastEnd = 0;
        MyImageSpanImage[] spans = getSortedImageSpans(text);
        for (MyImageSpanImage span : spans) {
            int start = text.getSpanStart(span);
            int end = text.getSpanEnd(span);
            if (lastEnd < start) {//如果lastEnd 不等于 起始位置 中间的一段就是普通字符串
                setImageSpan(text, lastEnd, start, c);
            }
            lastEnd = Math.max(end, lastEnd);
        }
        if (lastEnd != text.length()) {
            setImageSpan(text, lastEnd, text.length(), c);
        }
    }

    private Bitmap getImage(String string, boolean isHint, Context c) {
        if (string == null) {
            return null;
        }
        FrameLayout fl = new FrameLayout(c);
        fl.setPadding(0, getPx(6), 0, getPx(6));
        TextView tv = new TextView(c);
        tv.setMaxLines(1);
        tv.setLines(1);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv.setText(string);
        tv.setTextColor(isHint ? 0xff888888 : Color.BLACK);
        fl.addView(tv);
        return getBitmapViewByMeasure(fl, (int) getTextViewLength(tv, string), getPx(32));
    }

    // 计算出该TextView中文字的长度(像素)
    public static float getTextViewLength(TextView textView, String text) {
        if (text == null) {
            return 0;
        }
        TextPaint paint = textView.getPaint();
        // 得到使用该paint写上text的时候,像素为多少
        float textLength = paint.measureText(text);
        return textLength;
    }

    private Bitmap getTagImage(String string, Context c) {
        if (string == null) {
            return null;
        }
        string = string.replaceAll(",", "");
        if (string.length() == 0) {
            return null;
        }
        FrameLayout fl = new FrameLayout(c);
        fl.setPadding(getPx(4), getPx(2), getPx(4), getPx(2));
        TextView tv = new TextView(c);
        tv.setPadding(getPx(4), getPx(4), getPx(4), getPx(4));
        tv.setMaxLines(1);
        tv.setLines(1);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv.setText(string);
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundResource(R.drawable.shape_musician_tag);

        fl.addView(tv);
        return getBitmapViewByMeasure(fl, (int) getTextViewLength(tv, string) + getPx(16), getPx(32));
    }


    public static Bitmap getBitmapViewByMeasure(View view, int width, int height) {
        //打开图像缓存
        view.setDrawingCacheEnabled(true);
        //必须调用measure和layout方法才能成功保存可视组件的截图到png图像文件
        //测量View大小
        if (height <= 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        } else if (height > 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        }
        //发送位置和尺寸到View及其所有的子View
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = null;
        try {
            //获得可视组件的截图
            bitmap = view.getDrawingCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void setImageSpan(Editable text, int start, int end, Context c) {
        Bitmap tagImage = getTagImage(text.subSequence(start, end).toString(), c);
        for (MyImageSpanText span2 : text.getSpans(0, length(), MyImageSpanText.class)) {
            text.removeSpan(span2);
        }
        text.setSpan(new MyImageSpanImage(c, tagImage), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void processImageSpan() {
        final Editable text = getText();
        MyImageSpanImage[] spans = getSortedImageSpans(text);
        sources.clear();
        int lastEnd = 0;
        for (MyImageSpanImage span : spans) {
            int start = text.getSpanStart(span);
            int end = text.getSpanEnd(span);
            if (lastEnd == end || start == end) {
                lastEnd = end;
                continue;
            }
            lastEnd = end;
            sources.add(text.toString().substring(start, end));
        }
        //结果
    }

    public List<String> ok(Context c) {
        initTags(c);
        processImageSpan();
        return sources;

    }


    public interface OnAddListener {
        void onAdd(List<String> sources);
    }

    public void setonAddListener(OnAddListener listener) {
        this.listener = listener;
    }

}
