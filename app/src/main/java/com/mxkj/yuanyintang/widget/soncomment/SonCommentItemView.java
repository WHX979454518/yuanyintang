package com.mxkj.yuanyintang.widget.soncomment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.comment.Comment;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.utils.RichTextutils.ImageTextUtil;
import com.mxkj.yuanyintang.widget.CommentEditDialogFrag;

import java.util.List;

import static android.content.ContentValues.TAG;
import static com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.MUSICIAN_ID;

/*
* 用于展示多级子评论
* */
public class SonCommentItemView extends LinearLayout {
    Context context;
    FragmentManager supportFragmentManager;
    private CommentEditDialogFrag dialogForComment_son;

    public SonCommentItemView(Context context) {
        super(context);
        this.context = context;
    }

    public SonCommentItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setSonViews(final FragmentManager supportFragmentManager, final List<Comment.DataBean.SonBean> sonBeanList) {
        this.supportFragmentManager = supportFragmentManager;
        int forI = sonBeanList.size();
        if (sonBeanList.size() > 3) {
            forI = 3;
        }
        for (int i = 0; i < forI; i++) {
            TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.item__son_comment, null);
            final int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogForComment_son = new CommentEditDialogFrag(sonBeanList.get(finalI).getNickname(), 0, 0, sonBeanList.get(finalI).getId(), 0, new CommentEditDialogFrag.successCallBack() {
                        @Override
                        public void callBack(String json) {

                        }
                    });
                    if (dialogForComment_son != null) {
                        if (dialogForComment_son.isAdded()) {
                            dialogForComment_son.dismiss();
                        }
                    }
                    dialogForComment_son.show(supportFragmentManager, "");
                }
            });

            int startFrom2;
            int endFrom1;
            String str_1;
            int length;
            int endFrom2;
            if (sonBeanList.size() > i) {
                if (sonBeanList.get(i).getTo_nickname() == null) {
                    sonBeanList.get(i).setRevert(" ");
                }
                if (sonBeanList.get(i).getNickname() == null) {
                    sonBeanList.get(i).setNickname(" ");
                }
                if (TextUtils.isEmpty(sonBeanList.get(i).getTo_nickname()) || sonBeanList.get(i).getTo_nickname().equals(sonBeanList.get(i).getNickname())) {
                    str_1 = sonBeanList.get(i).getNickname() + " : ";
                    length = str_1.length();
                    endFrom1 = sonBeanList.get(i).getNickname().length();
                    SpannableStringBuilder ssb = new SpannableStringBuilder(str_1);
                    MyClickableSpan clickSpan = new MyClickableSpan(context, sonBeanList.get(i).getNickname(), sonBeanList.get(i));
                    ssb.setSpan(clickSpan, 0, endFrom1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    MyClickableSpan clickSpan3 = new MyClickableSpan(context, sonBeanList.get(i).getContent(), sonBeanList.get(i));
                    ssb.setSpan(clickSpan3, endFrom1 + 1, length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.append(ssb);
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    ImageTextUtil.setImageText(context, textView, sonBeanList.get(i).getContent(), 1);

                } else {
                    str_1 = sonBeanList.get(i).getNickname() + " 回复 " + sonBeanList.get(i).getTo_nickname() + " : ";
                    length = str_1.length();
                    endFrom1 = sonBeanList.get(i).getNickname().length();
                    startFrom2 = endFrom1 + 4;//回复的前后加了一个空格
                    endFrom2 = startFrom2 + sonBeanList.get(i).getTo_nickname().length();
                    SpannableStringBuilder ssb = new SpannableStringBuilder(str_1);
                    MyClickableSpan clickSpan = new MyClickableSpan(context, sonBeanList.get(i).getNickname(), sonBeanList.get(i));
                    ssb.setSpan(clickSpan, 0, endFrom1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    MyClickableSpan clickSpan2 = new MyClickableSpan(context, sonBeanList.get(i).getTo_nickname(), sonBeanList.get(i));
                    ssb.setSpan(clickSpan2, startFrom2, endFrom2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    MyClickableSpan clickSpan3 = new MyClickableSpan(context, sonBeanList.get(i).getContent(), sonBeanList.get(i));
                    ssb.setSpan(clickSpan3, endFrom2 + 1, length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.append(ssb);
//                必须加这一句，否则就无法被点击
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    ImageTextUtil.setImageText(context, textView, sonBeanList.get(i).getContent(), 1);
                }
            }
            addView(textView);
        }
        if (sonBeanList.size() > 3) {
            TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.item__son_comment, null);
            textView.setTextColor(Color.parseColor("#ff4c55"));
            textView.setText("共" + sonBeanList.size() + "条评论 >>");
            textView.setPadding(0,10,0,10);
            addView(textView);
        }
    }


    class MyClickableSpan extends ClickableSpan {
        private Context context;
        private Comment.DataBean.SonBean sonBean;
        private String text;

        public MyClickableSpan(Context context, String text, Comment.DataBean.SonBean sonBean) {
            this.context = context;
            this.text = text;
            this.sonBean = sonBean;
        }

        //在这里设置字体的大小，等待各种属性
        public void updateDrawState(@NonNull TextPaint ds) {
            if (text != null && sonBean.getNickname() != null && sonBean.getTo_nickname() != null) {
                if (text.equals(sonBean.getNickname()) || text.equals(sonBean.getTo_nickname())) {
                    ds.setColor(Color.parseColor("#5bc3f3"));
                }
            }
        }

        @Override
        public void onClick(View widget) {
            if (sonBean != null) {
                Log.e(TAG, "onClick: ::text===" + text + "------getContent====" + sonBean.getContent());
                if (text.equals(sonBean.getNickname())) {
                    Intent intent = new Intent(context, MusicIanDetailsActivity.class);
                    intent.putExtra(MUSICIAN_ID, sonBean.getUid() + "");
                    context.startActivity(intent);
                } else if (text.equals(sonBean.getTo_nickname())) {
                    Intent intent = new Intent(context, MusicIanDetailsActivity.class);
                    intent.putExtra(MUSICIAN_ID, sonBean.getTo_uid() + "");
                    context.startActivity(intent);
//                } else if (text.equals(sonBean.getContent())) {
                } else {
                    dialogForComment_son = new CommentEditDialogFrag(sonBean.getNickname(), 0, 0, sonBean.getId(), 0, new CommentEditDialogFrag.successCallBack() {
                        @Override
                        public void callBack(String json) {
                            //TODO  发布成功
                        }
                    });
                    if (dialogForComment_son != null) {
                        if (dialogForComment_son.isAdded()) {
                            dialogForComment_son.dismiss();
                        }
                    }
                    dialogForComment_son.show(supportFragmentManager, "");
                }
            }
        }
    }
}
