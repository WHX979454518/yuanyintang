package com.mxkj.yuanyintang.mainui.myself.helpcenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.ChatHistoryBean;
import com.mxkj.yuanyintang.mainui.web.CommonWebview;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by zuixia on 2018/4/23.
 */

public class ChartRobotAdapter extends BaseMultiItemQuickAdapter<ChatHistoryBean, BaseViewHolder> {

    public ChartRobotAdapter(List<ChatHistoryBean> data) {
        super(data);
        /**
         * 根据后台的type定义7中布局
         *  1 帮助中心内容
         2 文本内容
         3 链接
         4 新闻类
         5 菜谱类
         6 儿歌类
         7 诗词类*/
        addItemType(1, R.layout.help_chat_helpcenter);
        addItemType(2, R.layout.help_chat_text);
        addItemType(3, R.layout.help_chat_text);//3的时候链接文字颜色改成蓝色或者直接显示超链接文本
        addItemType(4, R.layout.help_chat_news);
        addItemType(5, R.layout.help_chat_news);//菜谱和新闻一样
        addItemType(6, R.layout.help_chat_helpcenter);
        addItemType(7, R.layout.help_chat_helpcenter);
        addItemType(8, R.layout.help_chat_helpcenter_self);

    }

    @Override
    protected void convert(final BaseViewHolder helper, final ChatHistoryBean chatHistoryBean) {
        final List<ChatHistoryBean.MutiTextBean> list = chatHistoryBean.getList();

        switch (chatHistoryBean.getItemType()) {
            case 1:
                if (list != null && list.size() > 0) {
                    final LinearLayout layout = helper.getView(R.id.ll_question_list);
                    layout.removeAllViews();
                    Observable.fromArray(list).flatMap(new Function<List<ChatHistoryBean.MutiTextBean>, ObservableSource<ChatHistoryBean.MutiTextBean>>() {
                        @Override
                        public ObservableSource<ChatHistoryBean.MutiTextBean> apply(@NonNull List<ChatHistoryBean.MutiTextBean> dataBeen) throws Exception {
                            return Observable.fromIterable(dataBeen);
                        }
                    }).subscribe(new Observer<ChatHistoryBean.MutiTextBean>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull final ChatHistoryBean.MutiTextBean dataBean) {
                            View view = LayoutInflater.from(mContext).inflate(R.layout.simple_list_item_1, layout, false);
                            TextView tv_desc = view.findViewById(R.id.tv_desc);
                            tv_desc.setText(StringUtils.isEmpty(dataBean.getTitle()));
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //TODO  帮助中心
                                    Intent intent = new Intent(mContext, CommonWebview.class);
                                    intent.putExtra("url", dataBean.getShare_url());
                                    intent.putExtra("from_yxy", true);
                                    intent.putExtra("title", dataBean.getTitle());
                                    mContext.startActivity(intent);
                                }
                            });
                            layout.addView(view);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {
//                                notifyDataAdapter();
                        }
                    });
                }
                break;
            case 2:
                helper.setText(R.id.tv_text, StringUtils.isEmpty(chatHistoryBean.getMsgText()));
                break;
            case 3://网址
                TextView textView = helper.getView(R.id.tv_text);
                textView.setText(chatHistoryBean.getMsgText());
                textView.setTextColor(Color.parseColor("#45a2ff"));
                helper.setOnClickListener(R.id.ll_to_webview, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, CommonWebview.class);
                        intent.putExtra("url", chatHistoryBean.getMsgText());
                        intent.putExtra("from_yxy", true);
                        intent.putExtra("title", getData().get(helper.getPosition() + 1).getMsgText());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case 4:
                if (list != null && list.size() > 0) {
                    helper.setText(R.id.tv_text, StringUtils.isEmpty(list.get(0).getArticle()));
                    if (list.get(0).getIcon_link() != null) {
                        helper.getView(R.id.img_text).setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(list.get(0).getIcon_link()).override(440,440).into((ImageView) helper.getView(R.id.img_text));
                    }
                    helper.setOnClickListener(R.id.ll_to_webview, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, CommonWebview.class);
                            intent.putExtra("url", list.get(0).getDetailurl());
                            intent.putExtra("title", list.get(0).getSource());
                            intent.putExtra("content", list.get(0).getArticle());
                            intent.putExtra("img", list.get(0).getIcon_link());
                            intent.putExtra("from_yxy", true);
                            mContext.startActivity(intent);
                        }
                    });
                }

                break;
            case 5:
                if (list != null && list.size() > 0) {
                    helper.setText(R.id.tv_text, StringUtils.isEmpty(list.get(0).getName()));
                    if (list.get(0).getIcon_link() != null) {
                        helper.getView(R.id.img_text).setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(list.get(0).getIcon_link()).override(440,440).into((ImageView) helper.getView(R.id.img_text));
                    }
                    helper.setOnClickListener(R.id.ll_to_webview, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, CommonWebview.class);
                            intent.putExtra("url", list.get(0).getDetailurl());
                            intent.putExtra("title", list.get(0).getName());
                            intent.putExtra("content", list.get(0).getInfo());
                            intent.putExtra("img", list.get(0).getIcon_link());
                            intent.putExtra("from_yxy", true);
                            mContext.startActivity(intent);
                        }
                    });
                }
                break;
            case 6:

                break;
            case 7:

                break;
            case 8:
                helper.setText(R.id.tv_text, StringUtils.isEmpty(chatHistoryBean.getMsgText()));
                break;
        }
    }
}
