package com.mxkj.yuanyintang.im.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.mxkj.yuanyintang.R;
import com.hyphenate.exceptions.HyphenateException;
import com.mxkj.yuanyintang.im.ui.EaseChatFragment;
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;

/**
 * Created by LiuJie on 2017/10/16.
 */

public class EaseChatRowSong extends EaseChatRow {


    private TextView tv_title;
    private TextView tv_nickname;
    private ImageView iv_cover;
    private RelativeLayout layout_player;

    public EaseChatRowSong(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_message_song : R.layout.ease_row_sent_message_song, this);
    }

    @Override
    protected void onFindViewById() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        iv_cover = (ImageView) findViewById(R.id.iv_cover);
        layout_player = findViewById(R.id.layout_player);
        layout_player.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayCtrlUtil.INSTANCE.playSheet(context, message.getStringAttribute(EaseChatFragment._ID, ""));
            }
        });
    }

    @Override
    public void onSetUpView() {
        try {
            if (!TextUtils.isEmpty(message.getStringAttribute(EaseChatFragment._ID))) {
                tv_title.setText(message.getStringAttribute(EaseChatFragment.TITLE));
                tv_nickname.setText(message.getStringAttribute(EaseChatFragment.NICKNAME));
                Glide.with(context)
                        .load(message.getStringAttribute(EaseChatFragment.IMGPIC_LINK))
                        .into(iv_cover);
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        handleTextMessage();
    }

    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS:
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        } else {
            if (!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(context, SongSheetDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, message.getStringAttribute(EaseChatFragment._ID, ""));
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.RGB_565 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }

}
