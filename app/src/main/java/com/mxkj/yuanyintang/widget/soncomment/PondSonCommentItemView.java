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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialReplyDialogFrag;
import com.mxkj.yuanyintang.mainui.pond.bean.PondCommentBean;
import com.mxkj.yuanyintang.utils.RichTextutils.ImageTextUtil;

import java.util.List;

import static com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.MUSICIAN_ID;

/*
* 用于展示多级子评论
* */
public class PondSonCommentItemView extends LinearLayout {
    Context context;
    FragmentManager supportFragmentManager;
    private PondDetialReplyDialogFrag dialogForComment;

    public PondSonCommentItemView(Context context) {
        super(context);
        this.context = context;
    }

    public PondSonCommentItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setSonViews(final PondCommentBean.DataBean dataBean, final FragmentManager supportFragmentManager, final List<PondCommentBean.DataBean.ComListsBean> sonBeanList) {
        this.supportFragmentManager = supportFragmentManager;
        removeAllViews();
        for (int i = 0; i < sonBeanList.size(); i++) {
            TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.item__son_comment, null);
            final int finalI = i;
            final PondCommentBean.DataBean.ComListsBean comListsBean = sonBeanList.get(finalI);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogForComment = new PondDetialReplyDialogFrag(comListsBean.getNickname(), supportFragmentManager, comListsBean.getId(), dataBean.getId(), new PondDetialReplyDialogFrag.successCallBack() {
                        @Override
                        public void callBack(String json) {
                            JSONObject jsonObject = JSON.parseObject(json);
                            PondCommentBean.DataBean.ComListsBean comListsBean = jsonObject.getObject("data", PondCommentBean.DataBean.ComListsBean.class);
//                            if (dataBean.getCom_lists() != null) {
//                                dataBean.getCom_lists().add(comListsBean);
//                                if (dataList != null) {
//                                    for (int i = 0; i < dataList.size(); i++) {
//                                        if (dataList.get(i).getId() == dataBean.getId()) {
//                                            dataList.get(i).setCom_lists(dataBean.getCom_lists());
//                                            callback.onSuccess(dataList);
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
                        }
                    });
                    if (dialogForComment != null) {
                        if (dialogForComment.isAdded()) {
                            dialogForComment.dismiss();
                        }
                        dialogForComment.show(supportFragmentManager, "");
                    }
                }
            });

            int startFrom2;
            int endFrom1;
            String str_1;
            int length;
            int endFrom2;
            if (comListsBean != null) {
                if (comListsBean.getTo_nickname() == null) {
                    comListsBean.setTo_nickname("");
                }
                if (comListsBean.getNickname() == null) {
                    comListsBean.setNickname("");
                }
                if (TextUtils.isEmpty(comListsBean.getTo_nickname()) || comListsBean.getTo_nickname().equals(comListsBean.getNickname())) {
                    //总长度
                    str_1 = comListsBean.getNickname() + " : ";
                    length = str_1.length();
                    //第一个昵称结束
                    endFrom1 = comListsBean.getNickname().length();
                    SpannableStringBuilder ssb = new SpannableStringBuilder(str_1);
                    MyClickableSpan clickSpan = new  MyClickableSpan(context, comListsBean.getNickname(), comListsBean);
                    ssb.setSpan(clickSpan, 0, endFrom1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                     MyClickableSpan clickSpan3 = new  MyClickableSpan(context, comListsBean.getContent(), comListsBean);
                    ssb.setSpan(clickSpan3, endFrom1 + 1, length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.append(ssb);
//                必须加这一句，否则就无法被点击
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    //总长度
                    str_1 = comListsBean.getNickname() + " 回复 " + comListsBean.getTo_nickname() + " : ";
                    length = str_1.length();
                    //第一个昵称结束
                    endFrom1 = comListsBean.getNickname().length();
                    //第二个昵称开始的位置
                    startFrom2 = endFrom1 + 4;//回复的前后加了一个空格
                    //第二个昵称结束的位置
                    endFrom2 = startFrom2 + comListsBean.getTo_nickname().length();
                    SpannableStringBuilder ssb = new SpannableStringBuilder(str_1);
/**文字的点击事件 消灭重复代码*/
                     MyClickableSpan clickSpan = new  MyClickableSpan(context, comListsBean.getNickname(), comListsBean);
                    ssb.setSpan(clickSpan, 0, endFrom1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                     MyClickableSpan clickSpan2 = new  MyClickableSpan(context, comListsBean.getTo_nickname(), comListsBean);
                    ssb.setSpan(clickSpan2, startFrom2, endFrom2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                     MyClickableSpan clickSpan3 = new  MyClickableSpan(context, comListsBean.getContent(), comListsBean);
                    ssb.setSpan(clickSpan3, endFrom2 + 1, length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.append(ssb);
                    //必须加这一句，否则就无法被点击
                     textView.setMovementMethod(LinkMovementMethod.getInstance());
                }


                ImageTextUtil.setImageText(context, textView, comListsBean.getContent(), 1);

            }
            addView(textView);
        }
    }

    class MyClickableSpan extends ClickableSpan {
        private Context context;
        private PondCommentBean.DataBean.ComListsBean sonBean;
        private String text;

        public MyClickableSpan(Context context, String text, PondCommentBean.DataBean.ComListsBean sonBean) {
            this.context = context;
            this.text = text;
            this.sonBean = sonBean;
        }

        //在这里设置字体的大小，等待各种属性
        public void updateDrawState(@NonNull TextPaint ds) {
            if (text != null && sonBean.getNickname() != null && sonBean.getTo_nickname() != null) {
                if (text.equals(sonBean.getNickname())) {
                    if (sonBean.getSex() == 0) {
                        ds.setColor(Color.parseColor("#ff6699"));
                    } else {
                        ds.setColor(Color.parseColor("#2fabf1"));
                    }
                }
                if (text.equals(sonBean.getTo_nickname())) {
                    if (sonBean.getTo_sex() == 0) {
                        ds.setColor(Color.parseColor("#ff6699"));
                    } else {
                        ds.setColor(Color.parseColor("#2fabf1"));
                    }
                }
            }
        }

        @Override
        public void onClick(View widget) {
            if (sonBean != null) {
                if (text.equals(sonBean.getNickname())) {
                    Intent intent = new Intent(context, MusicIanDetailsActivity.class);
                    intent.putExtra(MUSICIAN_ID, sonBean.getUid() + "");
                    context.startActivity(intent);
                } else if (text.equals(sonBean.getTo_nickname())) {
                    Intent intent = new Intent(context, MusicIanDetailsActivity.class);
                    intent.putExtra(MUSICIAN_ID, sonBean.getTo_uid() + "");
                    context.startActivity(intent);
                } else if (text.equals(sonBean.getContent())) {
                    dialogForComment = new PondDetialReplyDialogFrag(sonBean.getNickname(), supportFragmentManager, sonBean.getId(), sonBean.getId(), new PondDetialReplyDialogFrag.successCallBack() {
                        @Override
                        public void callBack(String json) {
                            JSONObject jsonObject = JSON.parseObject(json);
                            PondCommentBean.DataBean.ComListsBean comListsBean = jsonObject.getObject("data", PondCommentBean.DataBean.ComListsBean.class);
//                            if (dataBean.getCom_lists() != null) {
//                                dataBean.getCom_lists().add(comListsBean);
//                                if (dataList != null) {
//                                    for (int i = 0; i < dataList.size(); i++) {
//                                        if (dataList.get(i).getId() == dataBean.getId()) {
//                                            dataList.get(i).setCom_lists(dataBean.getCom_lists());
//                                            callback.onSuccess(dataList);
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
                        }
                    });
                    if (dialogForComment != null) {
                        if (dialogForComment.isAdded()) {
                            dialogForComment.dismiss();
                        }
                        dialogForComment.show(supportFragmentManager, "");
                    }
                }

            }
        }
    }

}
