package com.mxkj.yuanyintang.im.widget.chatrow;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.mxkj.yuanyintang.R;

public class EaseChatRowError extends EaseChatRow {

    private TextView contentView;

    public EaseChatRowError(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(R.layout.ease_row_error, this);
    }

    @Override
    protected void onFindViewById() {
        contentView = findViewById(R.id.tv_error);
    }

    @Override
    public void onSetUpView() {
        contentView.setText(message.getStringAttribute("text","您的操作太频繁啦，休息一下再发吧"));
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {

    }


}
